package com.yizhao.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.yizhao.core.ATManager;
import com.yizhao.core.AsyncWorkHandler;
import com.yizhao.core.Const;
import com.yizhao.util.FileUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadActivity extends Activity{
	
	private AsyncWorkHandler handler_download;	//从属第二步-下载更新APK文件
	
	private FileUtils fu;
	
	private File downloadFile;//APK文件
	 
	private InputStream inputStream = null;
	
	private FileOutputStream outputStream = null;
	
	private HttpURLConnection connection = null;
	
	private String apk_name;
	
	private Intent intent;
	
	private String apk_url;
	
	private int size = 1;//APK更新包的大小
	
	private long hasRead = 0;//APK更新已读取多少字节
	
	private int index = 0;//APK更新进度
	
	private Message message = null;//handle消息，公用
	
	private boolean flagB = true;
	
	private ProgressBar progressBar = null;
	
	public TextView appstart_view;
	
	private Context _context;
	
	private Intent intent_appstart;
	
	private boolean sdCard;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.download);
        
        intent = this.getIntent();
        
        _context = this;
        
        fu = new FileUtils();
        
        appstart_view = (TextView)findViewById(R.id.appstart_text);
        
        progressBar = (ProgressBar)findViewById(R.id.downloadBar);
        
        intent_appstart = new Intent(this,AppStartActivity.class);
        
        //检查是否有SD卡
		 sdCard = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		 
		 if(!sdCard){
   			 startActivityForResult(intent_appstart, 1);//1代表下载动作完成
   			 //handler_ir_check.doWork(null);
   			 Toast.makeText(_context, R.string.download_nosdcard, Toast.LENGTH_SHORT).show();
   			 finish();
   		 }else{
   			 apk_name = intent.getStringExtra("apk_name");
   			 apk_url = intent.getStringExtra("apk_url");
   			 initHandler();
   			 handler_download.doWork(null);
   		 }
		 
    }
    
    private void initHandler(){

        //异步下载APK文件
          handler_download = new AsyncWorkHandler(){
          	
          	@Override
          	public Object excute(Map<String,String> param){
          		
          		 try{
          			 
          			 File dir = fu.createSDDir(Const.SD_DIR);//创建目录
      				 
      				 downloadFile = fu.createSDFile(dir.getPath()+"/"+apk_name);//创建新文件
      				 
      				 URL url = new URL(apk_url);  

      				 connection = (HttpURLConnection)url.openConnection();//开启HTTP连接
      				 
      				 connection.setConnectTimeout(Const.TIMEOUT_15);//设置15秒超时
      				 
      				 size = connection.getContentLength();//获取内容长度

      				 inputStream = connection.getInputStream();//得到输入流
      				 
      				 outputStream = new FileOutputStream(downloadFile);//文件输出流
      				 
      				 byte[] buffer = new byte[Const.BYTE_SIZE_INT];
      				 
      				 do{
      					 int numread = inputStream.read(buffer);  
      					 if(numread <= 0) {
      						 break;   
      					 }
      					 outputStream.write(buffer, 0, numread);
      					 hasRead+=numread;  
      					 index = (int)(hasRead*100)/size;  
      					 message = new Message();  
      					 message.what = 1;
      					 sendMessage(message);
      				 }while(flagB);
      				 
      				 close();
      				 
          		 }catch (Exception e){//下载异常，发送消息
          			 e.printStackTrace();  
          			 message = new Message();
                  	 message.what = -1;
                  	 sendMessage(message);
          		 }finally{
          			close();
                 }
                   return null;
          	};
          	
              @Override  
              public void handleMessage(Message msg){
              	if(msg.what == 1){//下载中,下载正常
                      progressBar.setProgress(index); 
                      if (index >= 99 && flagB) {
                      	appstart_view.setText(R.string.setup_ready);
                          progressBar.setVisibility(View.GONE);
                          if(downloadFile!=null){
                          	showDialog(Const.DIALOG_YES_NO_MESSAGE);
                      	}else{
                      		Toast.makeText(_context, R.string.download_retry, Toast.LENGTH_SHORT).show();//下载文件有错误
                      		setResult(RESULT_FIRST_USER,intent_appstart); 
                          	finish();
                      	}
                      }else{
                      	appstart_view.setText("下载中,请稍候,已下载 "+index+"%");
                      }
                  }else if(msg.what == -1){//文件下载异常
                  	flagB = false;
                  	if(downloadFile!=null){
                  		fu.delFile(downloadFile.getPath());//删除临时文件
                  	}
                  	Toast.makeText(_context, R.string.download_retry, Toast.LENGTH_SHORT).show();
                  	setResult(RESULT_FIRST_USER,intent_appstart); 
                  	finish();
                  }
                 
              }
          };
    }
    
    
    private void close(){
    	
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
    
    /**
     * 打开安装包文件
     * @param f
     */
    public void setup(){
        Intent intent = new Intent();   
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
        intent.setAction(android.content.Intent.ACTION_VIEW);   
        String type = "application/vnd.android.package-archive";   
        intent.setDataAndType(Uri.fromFile(downloadFile), type);   
        startActivity(intent);
        ATManager.exitClient(_context);
        finish();
    }
    
    
    
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	 switch (id) {
    	 
	         case Const.DIALOG_YES_NO_MESSAGE:
	        	 return new AlertDialog.Builder(_context)   
	                .setTitle(R.string.download_finish)
	                .setIcon(R.drawable.icon)
	                .setMessage(R.string.setup_ask)
	                .setPositiveButton("安装",   
	                        new DialogInterface.OnClickListener() {
	                            @Override
								public void onClick(DialogInterface dialog,   
	                                    int which) {
	                            	dialog.dismiss();
	                            	setup();
	                            }   
	                        })   
	                .setNegativeButton("取消",   
	                        new DialogInterface.OnClickListener(){
	                            @Override
								public void onClick(DialogInterface dialog,   
	                                    int which) {
	                                dialog.cancel();
	                                setResult(RESULT_FIRST_USER,intent_appstart); 
	                              	finish();
	                            }   
	             }).create();
	        	 
	        	 
    	 }
    	 
     	return null;
    }
    
    /**
     * 监听返回键事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
    	if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){
    		close();
    		flagB = false;
    		setResult(RESULT_FIRST_USER,intent_appstart); 
    	}
    	return super.onKeyDown(keyCode, event);
    	
    }
    
    
    
}