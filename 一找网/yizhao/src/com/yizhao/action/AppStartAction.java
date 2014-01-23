package com.yizhao.action;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.os.Environment;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yizhao.activity.CommActivityGroup;
import com.yizhao.activity.R;
import com.yizhao.bean.APKBean;
import com.yizhao.bean.HotProductBean;
import com.yizhao.bean.IRBean;
import com.yizhao.blog.UserInfo;
import com.yizhao.core.AsyncWorkHandler;
import com.yizhao.core.CacheManager;
import com.yizhao.core.Const;
import com.yizhao.core.DBHelper;
import com.yizhao.core.HttpManager;
import com.yizhao.util.DensityUtil;
import com.yizhao.util.FileUtils;

public class AppStartAction {
	
	private static Activity activity;

    private TextView appstart_view;//启动界面提示文字对象
	
	private AsyncWorkHandler handler_ir;//从属第三步-下载更新IR文件的对象
	
	private AsyncWorkHandler handler_initData;//第四步-初始化系统信息及加载IR文件
    
    private int curIndexEnd;//最终的更新结束点
	
	private FileUtils fu;//文件操作工具

	private Message message = null;//handle消息，公用
	
    private ProgressBar progressBar = null;//进度条,用于更新APK与IR文件的进度展现
	
    public String apk_name;//APK更新包的文件名
    public String apk_url;//APK更新包地址

    private ArrayList<String> ir_list;//IR所需更新文件,如ir6,ir8
    
    private int ir_size;//IR所需更新文件个数
    private int ir_curr = 1;//IR当前更新到第几个文件,默认第一个
    private int ir_index;//IR更新进度条
    
    private List<String> autoList;
    
    private String autoText;

    private String fileName;
    
    private long count;//加载IR文件
    
    public boolean flagA = true;
    public boolean flagC = true;
    public boolean flagD = true;
    
    
	public AppStartAction(Activity _activity,TextView textview,ProgressBar _progressBar) {
		
		activity = _activity;
		appstart_view = textview;
		progressBar = _progressBar;
		fu = new FileUtils();
		
		initHandler();
		
	}
	
	private void initHandler(){
		
		//初始化-最后初始化
		handler_initData = new AsyncWorkHandler(){
        	@Override
        	public Object excute(Map<String,String> map) {
        		
        		init();
        		
            	//需要判断是否有SD卡
            	boolean sdCard = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
               	
            	if(sdCard){
            		
            		autoList = new ArrayList<String>();
            		
            		if(curIndexEnd < 1){
            			curIndexEnd = Const.IR_LOCAL_VERSION;
            		}
            		
            		for(int i = 1; i <= curIndexEnd; i++){
            			
            			fileName = "ir"+i+".txt";
            			
            			Log.d(Const.TAG, "IR load...fileName="+fileName);
            			
            			File f = new File(Environment.getExternalStorageDirectory() + "/"+Const.SD_DIR+"/"+fileName);//这是对应文件路径
    	        		
            			if(f!=null){
	    	        		try {
	    	        			if(f.length() > 0){
		    	        			InputStream in = new BufferedInputStream(new FileInputStream(f));
		    	        			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		    		        		while(flagA && (autoText=br.readLine())!=null){
		    		        			count++;
			    	        			message = new Message();
		    		        			message.what = 1;
		    		        			sendMessage(message);
		    		        			autoList.add(autoText);
		    		    			}
		    		    			br.close();
		    		    			in.close();
	    		    			}
	    		    		} catch (FileNotFoundException e){
	    		    			e.printStackTrace();
	    		    		} catch (UnsupportedEncodingException e1) {
	    		    			e1.printStackTrace();
	    		    		} catch (IOException e) {
	    		    			e.printStackTrace();
	    		    		}
    		    		}
            		}
            		
                	CacheManager.getInstance().setAutocomplete_list(autoList);
            		message = new Message();
    				message.what =3;
    				sendMessage(message);
                	
            	}
            	
            	return null;
        	};
        	
            @Override  
            public void handleMessage(Message msg){
            	if (msg.what == 1) {//加载中...
            		appstart_view.setText("正在载入"+fileName+"\n已载入"+count+"个关键字");
            	} else if(msg.what == 3){
            		if(flagA){
	            		Toast.makeText(activity, "共载入"+count+"个关键字", Toast.LENGTH_LONG).show();
	            		Intent it = new Intent(activity, CommActivityGroup.class);
	            		activity.startActivity(it);
	            		activity.finish();
            		}
            	}
            }
        };
        
        
      //初始化-异步下载-较复杂
        handler_ir = new AsyncWorkHandler(){
        	
        	@Override
        	public Object excute(Map<String,String> param){
        		
        	ir_size = ir_list.size();
        		
        	//需要判断是否有SD卡
   			 boolean sdCard = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
            	
   			 if(sdCard){
   				 
        		for(String ir_name : ir_list){

        			int ir_curr_len = 0;//当前文件的长度
        			
        			long ir_hasRead = 0;//当前文件已读取长度
        			
        			File tmpFile = null;
        			
        			InputStream inputStream = null;
            		
            		FileOutputStream outputStream = null;
            		
            		HttpURLConnection connection = null;
        			
	        		try {
	        			
	        			String[] name_ex = ir_name.split("\\.");
	        			
	        			tmpFile = File.createTempFile(name_ex[0], name_ex[1]);//写入缓存盘
	        			
	        			URL url = new URL(Const.IR_URL+ir_name);
	        			
	        			connection = (HttpURLConnection)url.openConnection();//开启HTTP连接
        				 
	        			connection.setConnectTimeout(Const.TIMEOUT_15);//设置15秒超时
       				 
	        			ir_curr_len = connection.getContentLength();//获取内容长度
	        			
	        			Log.d(Const.TAG, "ir_name="+ir_name+",ir_curr_len="+ir_curr_len);
	        			
	        			inputStream = connection.getInputStream();//得到输入流
	        			
	        			outputStream = new FileOutputStream(tmpFile);//文件输出流
       				 
	        			byte[] buffer = new byte[Const.BYTE_SIZE_INT];
	       				 
	   					do{
	   						int numread = inputStream.read(buffer); 
	   						if(numread <= 0) {
	   		                    break;   
	   		                }
	   						outputStream.write(buffer, 0, numread);
	   						ir_hasRead+=numread;  
	   						ir_index = (int)(ir_hasRead*100)/ir_curr_len;  
		       				message = new Message();  
		       				message.what = 2;
		       				sendMessage(message);
	   					}while(flagD);
	   					
	        		}catch(FileNotFoundException e){//网络文件不存在，则发送消息通知
	        			connection.disconnect();
	        			message = new Message();
	        			message.what = -2;
	        			sendMessage(message);
	        			e.printStackTrace();  
	        		}catch (Exception e) {
	        			connection.disconnect();
	        			message = new Message();  
       					message.what = -1;
       					sendMessage(message);
       					e.printStackTrace();  
	        		} finally {
		        		ir_curr++;
	        			if(connection!=null){//需要关闭,否则有可能造成网络阻塞
	        				connection.disconnect();
	        			}
	        			if(inputStream!=null){//输入流一定要关闭
	        				try {
	        					inputStream.close();
	        				} catch (IOException e) {
								inputStream = null;
								e.printStackTrace();
							}
		                 }
	                	 if(outputStream!=null){//输出流一定要关闭
	                		try {
	                			outputStream.close();
	 						} catch (IOException e) {
	 							outputStream = null;
	 							e.printStackTrace();
	 						}
	                	 }
	                 }
	        		
	        		//拷贝临时文件至目标目录
	        		if(flagD && tmpFile!=null && (tmpFile.length()==ir_curr_len)){
	        			try {
		        			File targetFile = null;
		        			File dir = fu.createSDDir(Const.SD_DIR);//创建目录
		        			targetFile = fu.createSDFile(dir.getAbsolutePath()+"/"+ir_name);//创建新文件
	       				 	fu.copyFile(tmpFile, targetFile);
	       				 	fu.delFile(tmpFile.getAbsolutePath());
						}catch (IOException e) {
							e.printStackTrace();
						}
	        		}
	        		
        		}
	        	message = new Message();  
				message.what = 1;
				sendMessage(message);
   			 }else{
   				message = new Message();  
				message.what = 3;
				sendMessage(message);
   			 }
   			 return null;
        	};
        	
            @Override  
            public void handleMessage(Message msg){
            	
            	if (msg.what == 2) {//下载中,下载正常
                    progressBar.setProgress(ir_index);
                    appstart_view.setText("共需更新"+ir_size+"个文件,正下载第 "+ir_curr+"个文件,已下载 "+ir_index+"%");
                }else if(msg.what == 3){//下载不正常,没有SD卡
                	if(flagD){
	                	Toast.makeText(activity, R.string.download_nosdcard, Toast.LENGTH_SHORT).show();
	                	appstart_view.setText(R.string.appstart_initing);
	                	handler_initData.doWork(null);
                	}
                }else if (msg.what == 1){//全部下载完成
                	if(flagD){
	                    progressBar.setProgress(100);
	                    appstart_view.setText(R.string.downloaded);
	                    handler_initData.doWork(null);
                    }
                }else if(msg.what == -1){//文件下载异常
                	Toast.makeText(activity, "第"+ir_curr+"个文件下载失败！", Toast.LENGTH_LONG).show();
                }else if(msg.what == -2){//下载不正常，网络文件没找到
                	Toast.makeText(activity, "第"+ir_curr+"个文件下载失败,文件不存在！", Toast.LENGTH_LONG).show();
                }
            }
        };
        
        
	}
	
	/**
     * 启动界面初始化部分信息
     */
    private boolean init(){
    	
    	Log.d(Const.TAG, "AppStartInit.init start...");
    	initBlogInfo();
		Log.d(Const.TAG, "AppStartInit.init blog login info success...");
    	initDisplayPx();
		Log.d(Const.TAG, "AppStartInit.init display info success...");
    	initIndexData();
		Log.d(Const.TAG, "AppStartInit.init index data success...");
		
        
    	return true;
    }
	
	
	/**
	 * 初始化数据库及建立数据表,初始化微博分享登录key
	 */
	private void initBlogInfo(){
		
        DBHelper dbHelp = new DBHelper(activity);
        
        if(dbHelp!=null){
        	
	        Cursor cursor_sina = dbHelp.readUser("sina");
	        if(cursor_sina!=null){
	    		if(!cursor_sina.isAfterLast()){
	    			cursor_sina.moveToFirst();
		        	UserInfo user = new UserInfo();
		        	user.setToken(cursor_sina.getString(1));
		        	user.setTokenSecret(cursor_sina.getString(2));
		        	CacheManager.getInstance().setUser_sina(user);
		            Log.d(Const.TAG, "AppStartInit.initBlogInfo|sina user="+user);
	            }
	    		cursor_sina.close();
	        }
	        
	        Cursor cursor_tx = dbHelp.readUser("tencent");
	        if(cursor_tx!=null){
	    		if(!cursor_tx.isAfterLast()){
	    			cursor_tx.moveToFirst();
		        	UserInfo user = new UserInfo();
		        	user.setToken(cursor_tx.getString(1));
		        	user.setTokenSecret(cursor_tx.getString(2));
		        	CacheManager.getInstance().setUser_tx(user);
		            Log.d(Const.TAG, "AppStartInit.initBlogInfo|tencent user="+user);
	            }
	    		cursor_tx.close();
	        }
	        dbHelp.close();
	        
        }
	}
	
	
	
	/**
	 * 初始化本地IR文件
	 */
	public void initLocalIR(){
		
		boolean sdCard = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);//需要判断是否有SD卡
    	
			if(sdCard){
				
				File dir = fu.createSDDir(Const.SD_DIR);
					
	    		for(int i = 1; i <=Const.IR_LOCAL_VERSION; i++){
	    		
		    		String fileName = "ir"+i+".txt";
		    		
	    			String f[] = fileName.split("\\.");
	    			
	    			if(f.length==2){//文件格式验证合法性
	        			
	        			InputStream inputStream = null;
	            		
	        			String sdcard_filepath = Const.SD_DIR+"/"+fileName;
	            		
	        			File old_file = fu.isFileExist(sdcard_filepath);
	        			
	        			if(old_file==null || old_file.length() < 1){
	        				
	        				try {
								old_file = fu.createSDFile(dir.getPath()+"/"+fileName);//创建新文件
							} catch (IOException e) {
								old_file = null;
								e.printStackTrace();
							}
							
							File new_file =	null;
							
	        				try {
								inputStream = activity.getResources().getAssets().open(fileName);
								new_file =	fu.writeFile2SDFromInput(Const.SD_DIR_TMP, fileName, inputStream);
							} catch (IOException e) {
								inputStream = null;
								new_file = null;
								e.printStackTrace();
							}
		        				
	        				if(new_file!=null && old_file!=null){
	        					fu.copyFile(new_file, old_file);
	        					fu.delFile(new_file.getAbsolutePath());
	        				}
	        			}
    	    			
	    			}
	    		}
		}
	}
	
	
	/**
	 * 检测IR版本
	 * @return
	 */
	public int checkIR(){
		
    	int hasNewIR = 0;
    	
    	FileUtils fu = new FileUtils();
    	
		IRBean ir_bean = AppAction.checkIR();
		
		if(ir_bean!=null && "true".equals(ir_bean.getResult()) && ir_bean.getCurFile()!=null && !"".equals(ir_bean.getCurFile())){
			
			String curFileName = ir_bean.getCurFile();//需更新的文件名
			
			int _sub1 = curFileName.indexOf(".");
			
			if(_sub1 > -1){
				
				String[] tmp = curFileName.split("\\.");
				
				curFileName = tmp[0];
				
				int sub2 = curFileName.indexOf("ir");
				
				if(sub2 > -1){
					
					curFileName = curFileName.substring(sub2+2,curFileName.length());
					
					try{
						
						curIndexEnd = Integer.parseInt(curFileName);
					
					}catch(Exception e){
						
						curIndexEnd = -1;
						
						e.printStackTrace();
						
					}
					
				}
				
			}
			
		}
		
		if(curIndexEnd > 0){
			
			ir_list = new ArrayList<String>();
			
	    		for(int i = 1; i <=curIndexEnd; i++){
	    			
    				if(flagC){
    					try {
	    	    			String ir_name = "ir"+i+".txt";
	        				
	        				HttpManager hm = new HttpManager(Const.IR_URL+ir_name);
	        				
	        				long newsize = hm.getContentLength();//获取新文件大小
	
	    					long oldsize = fu.readFileSize(Const.SD_DIR+"/"+ir_name);//获取老文件大小
	
	                     	Log.d(Const.TAG, "AppStartAction.checkIR|"+ir_name+" old size="+oldsize+",new size="+newsize);
	                     	
	    					if(oldsize!=newsize){
	    						ir_list.add(ir_name);
	    					}
    					}catch (Exception e){//网络异常
    		  	             e.printStackTrace();
    		      		}
    				}
    			}
      		
      		if(ir_list.size() > 0){
             	Log.d(Const.TAG, "AppStartAction.checkIR|ir_list.size="+ir_list.size());
             	hasNewIR = 1;
			}
		}
    	Log.d(Const.TAG, "AppStartAction.checkIR|hasNewIR="+hasNewIR);
    	
		return hasNewIR;
	}
	
	
	/**
	 * 初始化屏幕分辨率信息，设定获取图片的大中小像素,初始化网络图片需要拉取的大小，根据dp计算得来
	 */
	private void initDisplayPx(){
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        CacheManager.getInstance().setDisplay_pix_width(dm.widthPixels);
        CacheManager.getInstance().setDisplay_pix_height(dm.heightPixels);
        
        int b_pic_px = 0;
        int m_pic_px = 0;
        int s_pic_px = 0;
        
        if(dm.widthPixels > 1000){//若分辨率宽度大于1000，则统一用此规格
        	b_pic_px = 380;
        	m_pic_px = 280;
        	s_pic_px = 180;
        }else{//否则使用dp转换实际pix
            DensityUtil du = new DensityUtil(dm);
            b_pic_px = du.dip2px(160);
            m_pic_px = du.dip2px(130);
            s_pic_px = du.dip2px(120);
        }
        
        CacheManager.getInstance().setPic_b_option(Const.PIC_OPTION.replace("?", String.valueOf(b_pic_px)));
        CacheManager.getInstance().setPic_m_option(Const.PIC_OPTION.replace("?", String.valueOf(m_pic_px)));
        CacheManager.getInstance().setPic_s_option(Const.PIC_OPTION.replace("?", String.valueOf(s_pic_px)));
        Log.d(Const.TAG, "AppStartInit.initDisplayPx|b_pic_px="+b_pic_px+",m_pic_px="+m_pic_px+",s_pic_px="+s_pic_px);
	}
	
	
	/**
	 * 获取首页热门信息，写入Cache
	 */
	private void initIndexData(){
    	HotProductBean hostProductBean = ProductAction.getHotProduct();
    	if(hostProductBean!=null){
    		CacheManager.getInstance().setHostProductBean(hostProductBean);
    	}
	}
	
//	/**
//     * 获取当前软件版本
//     */
//    private void getCurrentVersion() throws NameNotFoundException{
//        PackageInfo info = activity.getPackageManager().getPackageInfo(activity.getPackageName(),0);
//        
//        if(info!=null){
//	        this.versionName = info.versionName;
//	        this.currVersion = Float.parseFloat(info.versionName);
//        }
//        
//        Log.d(Const.TAG, "AppStartInit.getCurrentVersion|currVersion="+currVersion+",versionName="+versionName);
//            
//    }

    /**
     * 检测软件版本
     * @return 0无需更新 1需更新
     */
    public int checkVersion(){
    	
    	int hasNewVersion = 0;
    	
    	APKBean bean = AppAction.checkVersion();
    	
    	if(bean!=null && "true".equals(bean.getResult())){
    		
        	try{
    			
    			float version = Float.parseFloat(bean.getVersion());
    			
    			String url = bean.getUrl();
    			
    			apk_name = url.substring(url.lastIndexOf("/") + 1,url.length());
    			
    			apk_url = url;
    			
				//apk_url = url.contains(Const.HTTPHEAD)?url:null;
				
    			if(version > Const.APK_Version){
    				hasNewVersion = 1;
    			}
    			
    		}catch(Exception e){}
    	}
    	
    	Log.d(Const.TAG, "AppStartAction.checkVersion|hasNewVersion="+hasNewVersion);
    	
    	return hasNewVersion;
    	
    }

	public AsyncWorkHandler getHandler_initData() {
		return handler_initData;
	}

	public AsyncWorkHandler getHandler_ir() {
		return handler_ir;
	}

    
}
