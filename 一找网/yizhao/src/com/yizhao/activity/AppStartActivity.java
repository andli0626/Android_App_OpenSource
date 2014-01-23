package com.yizhao.activity;


import java.util.Map;

import com.yizhao.action.AppStartAction;
import com.yizhao.core.ATManager;
import com.yizhao.core.AsyncWorkHandler;
import com.yizhao.core.Const;
import com.yizhao.core.DBHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yizhao.util.NetUtil;

public class AppStartActivity extends Activity{

	private Context _context;
	
	private AppStartAction asi;
	
	private TextView appstart_text;
	
	private ProgressBar progressBar = null;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.welcome);
        
        ATManager.addActivity(this);
       
        _context = this;
        
        appstart_text = (TextView)findViewById(R.id.appstart_text);
        
        progressBar = (ProgressBar)findViewById(R.id.downloadBar);
    
        appstart_text.setText(R.string.appstart_initing);

        new DBHelper(this).close();

        asi = new AppStartAction(this,appstart_text,progressBar);
        
        asi.initLocalIR();
        
        appstart_text.setText(R.string.reading_update);
        
        //初始化-入口
        AsyncWorkHandler handler_init = new AsyncWorkHandler(){
        	@Override
        	public Object excute(Map<String,String> map) {
        		
        		boolean isConnection = NetUtil.isNetworkAvailable(_context);
                
        		Integer hasNewVersion = 0;
        		
                if(isConnection){
                	hasNewVersion = asi.checkVersion();
                }
                
                return hasNewVersion;
        	};
        	
            @Override  
            public void handleMessage(Message msg){
        		Integer result = (Integer)msg.obj;
        		switch(result){
            		case 1://APK有更新
            			showDialog(Const.DIALOG_APK_UPDATE);
            			break;
            		default:
            			int res = asi.checkIR();
            			if(res==1){
            				showDialog(Const.DIALOG_IR_UPDATE);
            			}else{
            				asi.getHandler_initData().doWork(null);
            			}
            			break;
        		}
            }
        };
        
        handler_init.doWork(null);
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	 switch (id) {
    	 
    	 	case Const.DIALOG_YES_NO_MESSAGE:
        	 return new AlertDialog.Builder(_context)
             .setTitle("确定退出程序？")//设置对话框的标题
             .setPositiveButton("确定", new DialogInterface.OnClickListener() {//设置按下表示确定按钮时按钮的text，和按钮的事件监听器
                 @Override
				public void onClick(DialogInterface dialog, int whichButton) {
                	 removeDialog(Const.DIALOG_YES_NO_MESSAGE);
                	 ATManager.exitClient(_context);
                 }
             })
             .setNegativeButton("取消", new DialogInterface.OnClickListener() {//设置取消按钮的text 和监听器
                 @Override
				public void onClick(DialogInterface dialog, int whichButton) {
                	 dialog.dismiss();
                 }
             })
             .create();
        	 
	         case Const.DIALOG_APK_UPDATE:
	        	 return new AlertDialog.Builder(_context)
	                .setTitle(R.string.checked_newapk)
	                .setIcon(R.drawable.icon)
	                .setMessage(R.string.upload_ask) 
	                .setPositiveButton("更新",   
	                        new DialogInterface.OnClickListener() {
	                            @Override
								public void onClick(DialogInterface dialog,int which) {
	                            	dialog.dismiss();
	                            	Intent intent = new Intent(_context, DownloadActivity.class);
	                            	intent.putExtra("apk_name", asi.apk_name);//只有检测到版本才会提示DIALOG，所以这里肯定有值
	                            	intent.putExtra("apk_url", asi.apk_url);//只有检测到版本才会提示DIALOG，所以这里肯定有值
	                            	appstart_text.setText("");
	                            	startActivityForResult(intent,0);
	                            }   
	                        })   
	                .setNegativeButton("取消",   
	                        new DialogInterface.OnClickListener(){
	                            @Override
								public void onClick(DialogInterface dialog,int which) {
	                                dialog.cancel();
	                                appstart_text.setText(R.string.reading_update);
	                                int result = asi.checkIR();
	                                if(result==1){
	                                	showDialog(Const.DIALOG_IR_UPDATE);
	                                }else{
	                                	appstart_text.setText(R.string.appstart_initing);
	                                	asi.getHandler_initData().doWork(null);
	                                }
	                            }   
	             }).create();
	        	 
	         case Const.DIALOG_IR_UPDATE:
	        	 return new AlertDialog.Builder(_context)   
	                .setTitle(R.string.checked_newir)   
	                .setIcon(R.drawable.icon)   
	                .setMessage(R.string.upload_ask)
	                .setPositiveButton("更新",   
	                        new DialogInterface.OnClickListener() {
	                            @Override
								public void onClick(DialogInterface dialog,   
	                                    int which) {
	                            	dialog.dismiss();
	                                progressBar.setVisibility(View.VISIBLE);
	                                appstart_text.setText(R.string.reading_update);
	                                asi.getHandler_ir().doWork(null);
	                            }   
	                        })   
	                .setNegativeButton("取消",   
	                        new DialogInterface.OnClickListener(){
	                            @Override
								public void onClick(DialogInterface dialog,   
	                                    int which) {
	                                dialog.cancel();
	                                appstart_text.setText(R.string.appstart_initing);
	                                asi.getHandler_initData().doWork(null);
	                            }   
	             }).create();
    	 }
    	 
     	return null;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	Log.d(Const.TAG, "AppStartActivity.onActivityResult...resultCode="+resultCode);
    	switch (resultCode) {
    	case RESULT_FIRST_USER:
    		int result = asi.checkIR();
            if(result==1){
            	showDialog(Const.DIALOG_IR_UPDATE);
            }else{
            	appstart_text.setText(R.string.appstart_initing);
            	asi.getHandler_initData().doWork(null);
            }
    		break;
    	default:
    	    break;
    	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0,1,1,"返回");
    	menu.add(0,2,2,"退出");
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==2){
			showDialog(Const.DIALOG_YES_NO_MESSAGE);
		}
    	return super.onOptionsItemSelected(item);
    }
    

    /**
     * 监听返回键事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
    		asi.flagA = false;
    		asi.flagC = false;
    		asi.flagD = false;
    		finish();
    	}
    	return super.onKeyDown(keyCode, event);
    }
    
	
}