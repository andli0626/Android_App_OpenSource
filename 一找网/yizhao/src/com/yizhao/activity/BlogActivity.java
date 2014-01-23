package com.yizhao.activity;

import java.io.File;
import java.util.Map;

import weibo4android.http.ImageItem;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.tencent.weibo.api.T_API;
import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.utils.Configuration;
import com.tencent.weibo.utils.OAuthClient;
import com.yizhao.blog.BlogAction;
import com.yizhao.blog.BlogOAuth;
import com.yizhao.blog.BlogTencentBean;
import com.yizhao.blog.UserInfo;
import com.yizhao.blog.BlogBean;
import com.yizhao.core.AsyncWorkHandler;
import com.yizhao.core.CacheManager;
import com.yizhao.core.Const;
import com.yizhao.core.DBHelper;
import com.yizhao.util.ImageUtil;
import com.yizhao.util.NetUtil;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BlogActivity extends Activity{
	
	private TextView dialog_bt_fx_sina;//sina
	
	private TextView dialog_bt_fx_tencent;//tencent

	private TextView dialog_bt_fx_sms;//短信分享
	
	private TextView dialog_bt_fx_other;//短信分享
	
	private TextView dialog_bt_cancel;//取消
	
	private BlogOAuth auth_sina;//sina
	
	public static OAuth oauth;//tencent
	
	public static OAuthClient auth;//tencent
	
	private UserInfo user_sina;//blog user info
	
	private UserInfo user_tx;//blog user info
	
	private static String fx_type;//click type
	
	private String clientIp;
	
	private Context _context;
	
	private CacheManager cm;
	
	private AsyncWorkHandler handler_blog_sina;

	private AsyncWorkHandler handler_blog_tx;
	
	private String text;//分享内容
	
	private String localPicPath;//用于腾讯微博分享的图片本地SD卡路径
	
	private ImageItem imageitem;//用于新浪微博分享的图片封装
	
	private File file;//用于其它分享
	
	private boolean blog_tx_work;
	
	private boolean blog_sina_work;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.fx);
        
        _context = this;
        
        cm = CacheManager.getInstance();

        dialog_bt_fx_sina = (TextView)findViewById(R.id.dialog_bt_fx_sina);
        dialog_bt_fx_tencent = (TextView)findViewById(R.id.dialog_bt_fx_tencent);
        dialog_bt_fx_sms = (TextView)findViewById(R.id.dialog_bt_fx_sms);
        dialog_bt_fx_other = (TextView)findViewById(R.id.dialog_bt_fx_other);
        
        dialog_bt_cancel = (TextView)findViewById(R.id.dialog_bt_cancel);
        
        
        Intent intent = getIntent();
        
        //是否存在腾讯微博token
		if(intent.hasExtra("oauth_token")) {
			String oauth_token = intent.getStringExtra("oauth_token");
			String oauth_token_secret = intent.getStringExtra("oauth_token_secret");
			setToken(oauth_token, oauth_token_secret);
		}
        
		//获取分享内容文字信息
        text = this.getIntent().getStringExtra("text");
        
        //从上一个Activity传递过来的localPicPath
        localPicPath = this.getIntent().getStringExtra("localPicPath");
        
        //生成新浪微博分享所需图片数据对象
        if(localPicPath!=null && !"".equals(localPicPath)){
			try {
				file = new File(localPicPath);
				Bitmap bm = ImageUtil.getLoacalBitmap(localPicPath);
				byte[] img_b = ImageUtil.bitmap2Bytes(bm);
				imageitem = new ImageItem("pic",img_b);
			} catch (Exception e) {
				e.printStackTrace();
			}
        } 
        
        Log.d(Const.TAG, "BlogActivity.imageitem="+imageitem+",text="+text);
        
        dialog_bt_fx_other.setOnClickListener(new Button.OnClickListener(){
            @Override
			public void onClick(View v){
            	
            	//自动提示选择手机里能发送图片/文字的应用程序
            	Intent intent = new Intent(Intent.ACTION_SEND);
 				intent.putExtra(Intent.EXTRA_TITLE,getString(R.string.app_name));
 				
 				if(file!=null){
 					intent.setType("image/text");
 	 				intent.putExtra(Intent.EXTRA_TEXT,text);
 					intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(file));
 				}else{
 					intent.setType("text");
 					intent.putExtra(Intent.EXTRA_TEXT,text);
 				}
 				
 				startActivity(Intent.createChooser(intent, "请选择"));
            	
            }
         });
        
        dialog_bt_fx_sms.setOnClickListener(new Button.OnClickListener(){
            @Override
			public void onClick(View v){
            	
 				//如果只是短信转发，则放开这段，屏蔽上一段
            	if(text!=null && !"".equals(text)){
            		Uri uri = Uri.parse("smsto:");
                	Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                	it.putExtra("sms_body", text);
                	startActivity(it);
                	finish();
            	}else{
            		Toast.makeText(_context, "转发失败,内容为空！", Toast.LENGTH_LONG).show();
            		finish();
            	}
            	
            }
         });
        
        
	   	dialog_bt_fx_sina.setOnClickListener(new Button.OnClickListener(){
	           @Override
				public void onClick(View v){
	        	   
	        	   fx_type = "sina";
	        	   
	        	   if(!blog_sina_work){
	        		   
	        		   blog_sina_work = true;
	        		   
		        	   user_sina = cm.getUser_sina();
		        	   
		        	   if(user_sina==null){
		        		   
		        		   //跳转至新浪服务器验证
		        		   handler_blog_sina = new AsyncWorkHandler(){
					        	@Override
					        	public Object excute(Map<String,String> map){
					        		
					        		auth_sina = new BlogOAuth(Const.APPKEY_SINA,Const.APPSECRET_SINA,"yizhaoApp://BlogActivity",
					        				   Const.SINA_REQUEST_TOKEN_URL,Const.SINA_ACCESS_TOKEN_URL,Const.SINA_AUTHRIZE_URL);
					        		
					        		Integer res = auth_sina.RequestAccessToken(BlogActivity.this);
					        		return res;
					        	};
					        	
					            @Override  
					            public void handleMessage(Message msg){
					            	blog_sina_work = false;
					            	if(msg.obj!=null){
					            		Integer result = (Integer)msg.obj;
					            		if(result!=0){
					            			removeDialog(Const.URL_WAIT);
						        			Toast.makeText(_context, R.string.con_fail, Toast.LENGTH_LONG).show();
						        			finish();
						        		 }else{
						        			 removeDialog(Const.URL_WAIT);
						        		 }
					            	}else{
					            		removeDialog(Const.URL_WAIT);
					            		Toast.makeText(_context, R.string.busy, Toast.LENGTH_LONG).show();
					            	}
					            }
					            
					        };
					        showDialog(Const.URL_WAIT);
					        handler_blog_sina.doWork(null);
		        		   
						}else{
							//分享业务逻辑
							handler_blog_sina = new AsyncWorkHandler(){
					        	@Override
					        	public Object excute(Map<String,String> map){
					        		BlogBean weibo = null;
					                Log.d(Const.TAG, "BlogActivity.dialog_bt_fx_sina="+imageitem+",text="+text);
					        		if(text!=null && !"".equals(text)){
					        			weibo = BlogAction.writeBlog(user_sina.getUserKey(),user_sina.getUserSecret(),text,imageitem);
					        		}
					        		return weibo;
					        	};
					        	
					            @Override  
					            public void handleMessage(Message msg){
					            	blog_sina_work = false;
					            	if(msg.obj!=null){
					            		BlogBean weibo = (BlogBean)msg.obj;
					            		Toast.makeText(_context, weibo.getResMsg(), Toast.LENGTH_LONG).show();
					            	}else{
					            		Toast.makeText(_context, R.string.fx_fail, Toast.LENGTH_LONG).show();
					            	}
					            	removeDialog(Const.PROGRESSBAR_WAIT);
					            	BlogActivity.this.finish();
					            }
					        };
					        showDialog(Const.PROGRESSBAR_WAIT);
					        handler_blog_sina.doWork(null);
						}
	        	   }else{
	            		 showDialog(Const.PROGRESSBAR_WAIT);
	            	}
	           }
	        });
	   	
	   	dialog_bt_fx_tencent.setOnClickListener(new Button.OnClickListener(){
	            @Override
	 			public void onClick(View v){
	            	
	            	fx_type = "tencent";
	            	
	            	if(!blog_tx_work){
	            		
	            		blog_tx_work = true;

		            	Configuration.wifiIp = NetUtil.getLocalIpAddress();
		            	
		        		oauth = new OAuth("yizhaoApp://BlogActivity"); // 初始化OAuth请求令牌
		        		oauth.setOauth_consumer_key(Const.APPKEY_TENCENT);
		        		oauth.setOauth_consumer_secret(Const.APPSECRET_TENCENT);
		        		
		        		user_tx = cm.getUser_tx();
		        		
		        		if(user_tx==null){
		        			
	        				//跳转至腾讯服务器验证
		        		   handler_blog_tx = new AsyncWorkHandler(){
					        	@Override
					        	public Object excute(Map<String,String> map){
					        		
					        		Uri uri = null;
					        		
					        		try {
										auth = new OAuthClient();// OAuth 认证对象
										// 获取request token
										oauth = auth.requestToken(oauth);
										if (oauth.getStatus() == 1) {
											Toast.makeText(_context,R.string.appkey_fail, Toast.LENGTH_LONG).show();
											removeDialog(Const.URL_WAIT);
										} else {
											String url = "http://open.t.qq.com/cgi-bin/authorize?oauth_token="+oauth.getOauth_token();
											uri = Uri.parse(url);
										}
									
									} catch (Exception e) {
										e.printStackTrace();
									}
									
									return uri;
					        		
					        	};
					        	
					            @Override  
					            public void handleMessage(Message msg){
					            	blog_tx_work = false;
					            	if(msg.obj!=null){
					            		Uri uri = (Uri)msg.obj;
										removeDialog(Const.URL_WAIT);
										startActivity(new Intent(Intent.ACTION_VIEW, uri));
					            	}else{
					            		removeDialog(Const.URL_WAIT);
					            		Toast.makeText(_context, R.string.con_fail, Toast.LENGTH_LONG).show();
					            	}
					            }
					            
					        };
					        showDialog(Const.URL_WAIT);
					        handler_blog_tx.doWork(null);
							
		        		}else{
		        			
		        			//有登录则直接分享
		        		   handler_blog_tx = new AsyncWorkHandler(){
					        	@Override
					        	public Object excute(Map<String,String> map){
					        		
					        		BlogTencentBean bean = null;
					        		
					        		setToken(user_tx.getUserKey(),user_tx.getUserSecret());
				        			
				        			T_API tapi = new T_API();
				        			
				        			try{
				        				String s = null;
				        				
				        				if(localPicPath==null || "".equals(localPicPath)){
				        					s = tapi.add(oauth, "json", text, clientIp);
				        				}else{
				        					s = tapi.add_pic(oauth, "json", text, clientIp, localPicPath);
				        				}
				    					
				    					Log.d(Const.TAG, "BlogActivity.tapi.add|s="+s);
				    					
				    					if(s!=null && !"".equals(s)){
				    						Gson gson = new Gson();
				    						try{
				    							bean = gson.fromJson(s,new TypeToken<BlogTencentBean>(){}.getType());
				    						}catch(JsonParseException e){
				    							Log.e(Const.TAG, "BlogActivity.dialog_bt_fx_tencent|JsonParseException",e);
				    						}
				    					}
				    					
				    				}catch (Exception e){
				    					e.printStackTrace();
				    				}

			    					return bean;
					        		
					        	};
					        	
					            @Override  
					            public void handleMessage(Message msg){
					            	blog_tx_work = false;
					            	if(msg.obj!=null){
					            		BlogTencentBean bean = (BlogTencentBean)msg.obj;
					            		if(bean.getRet()==0){
					            			removeDialog(Const.PROGRESSBAR_WAIT);
					            			Toast.makeText(_context, "分享成功！", Toast.LENGTH_LONG).show();
					            		}else{
					            			removeDialog(Const.PROGRESSBAR_WAIT);
						            		Toast.makeText(_context, R.string.fx_fail, Toast.LENGTH_LONG).show();
					            		}
					            	}else{
					            		removeDialog(Const.PROGRESSBAR_WAIT);
					            		Toast.makeText(_context, R.string.fx_fail, Toast.LENGTH_LONG).show();
					            	}
					            }
					            
					        };
					        showDialog(Const.PROGRESSBAR_WAIT);
					        handler_blog_tx.doWork(null);
		        			
		    				
		        		}
	            		
	            	}else{
	            		 showDialog(Const.PROGRESSBAR_WAIT);
	            	}
	            	
	             }
	          });
	   	
	   	dialog_bt_cancel.setOnClickListener(new Button.OnClickListener(){
	           @Override
				public void onClick(View v) {
	        	   finish();
	           }  
	        });
	        
	}
    
    
    
    /**
     * call back from sina blog
     */
    @Override
    protected void onNewIntent(Intent intent) {
    	
		Log.d(Const.TAG, "BlogActivity.onNewIntent...fx_type="+fx_type);
		
		if("tencent".equals(fx_type)){
			
			Uri uri = intent.getData();
	
			if(uri != null) {
				String oauth_verifier = uri.getQueryParameter("oauth_verifier");	
				String oauth_token = uri.getQueryParameter("oauth_token");	

				Log.d(Const.TAG, "BlogActivity.onNewIntent|tencent.oauth_verifier="+oauth_verifier+",tencent.oauth_token="+oauth_token);
				
				if(oauth_verifier!=null && oauth_token!=null){
					getToken(oauth_verifier, oauth_token);
					Toast.makeText(_context, R.string.logined_tencent, Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(_context, R.string.login_fail, Toast.LENGTH_LONG).show();
				}
				finish();
				
			}
		}
		
		if("sina".equals(fx_type)){
			
			user_sina = auth_sina.GetAccessToken(intent);
	    	
			Log.d(Const.TAG, "BlogActivity.onNewIntent|user_sina="+user_sina);
			
			if(user_sina!=null){
				//分享业务逻辑
				cm.setUser_sina(user_sina);
				DBHelper db = new DBHelper(BlogActivity.this);
				db.saveUser(user_sina.getType(),user_sina.getUserKey(), user_sina.getUserSecret());
				db.close();
				Toast.makeText(_context, R.string.logined_sina, Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(_context, R.string.login_fail, Toast.LENGTH_LONG).show();
			}
			
	    	finish();
	    	
    	}
    	super.onNewIntent(intent);
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	 switch(id){
	         case Const.PROGRESSBAR_WAIT:
	        	 ProgressDialog dialog = new ProgressDialog(_context);   
	             dialog.setMessage("操作中,请稍候...");   
	             dialog.setIndeterminate(true);   
	             dialog.setCancelable(true);
	             return dialog;
	         case Const.URL_WAIT:
	        	 ProgressDialog dialog_w = new ProgressDialog(_context);   
	        	 dialog_w.setMessage("与微博服务器连接中,请稍候...");   
	        	 dialog_w.setIndeterminate(true);   
	        	 dialog_w.setCancelable(true);
	             return dialog_w;
    	 }
    	 
     	return null;
    }
	
    /**
	 * get token from verifier code
	 * @param oauth_verifier
	 * @param oauth_token
	 */
	public void getToken(String oauth_verifier, String oauth_token) {
		
		oauth.setOauth_verifier(oauth_verifier);
		oauth.setOauth_token(oauth_token);
		
		clientIp = Configuration.wifiIp;
			
		try {//验证accesstoken是否有效，可能过期?
			oauth = auth.accessToken(oauth);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (oauth.getStatus() == 2) {
			Log.d(Const.TAG,"Get Access Token failed!");
			return;
		} else {			
			Log.d(Const.TAG, "OAuthActivity Oauth_token : " + oauth.getOauth_token());
			Log.d(Const.TAG, "OAuthActivity Oauth_token_secret : " + oauth.getOauth_token_secret());
			
			// 已经拿到access token，可以使用oauth对象访问所有API了
			// 将access token存储到数据库里
			DBHelper db = new DBHelper(BlogActivity.this);
			db.saveUser("tencent",oauth.getOauth_token(), oauth.getOauth_token_secret());
			db.close();
			user_tx = new UserInfo();
			user_tx.setType("tencent");
			user_tx.setToken(oauth.getOauth_token());
			user_tx.setTokenSecret(oauth.getOauth_token_secret());
			cm.setUser_tx(user_tx);
		}					
	}
	
	public void setToken(String oauth_token, String oauth_token_secret) {
		oauth.setOauth_token(oauth_token);//放入验证key
		oauth.setOauth_token_secret(oauth_token_secret);//放入验证码
	}
}