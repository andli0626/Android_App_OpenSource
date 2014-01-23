package com.yizhao.activity;

import com.yizhao.core.ATManager;
import com.yizhao.core.Const;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * 使用ActivityGroup来切换Activity和Layout
 * @author
 */
public class CommActivityGroup extends ActivityGroup {

    private ScrollView container = null;
    
    private Context context;

    private LinearLayout menu_ss;
    
    private LinearLayout menu_fl;
    
    private LinearLayout menu_sc;
    
    private LocalActivityManager localActivityManager;
    
    private static final String ProductActivity = "ProductActivity";
    
    private static final String IndexGalleryActivity = "IndexGalleryActivity";
    
    private void resumeActivity(Activity activity,Class<?> cls,String activityId){
    	
//    		boolean status = false;
    		
    		container.removeAllViews();
    		 
//    		if(activity!=null){
//    			status = activity.getIntent().getBooleanExtra("status", false);
//    		}
    		
    		Log.d(Const.TAG, "CommActivityGroup.resumeActivity|activity="+activity+",activityId="+activityId);
    		
//    		if(activity!=null && status){
//	    		View activityView = activity.getWindow().getDecorView();
//				container.addView(activityView);
//    		}else{
    			Intent it = new Intent(this, cls);
    			container.addView(getLocalActivityManager().startActivity(activityId,it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView());
//   	    }

    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        // 设置视图
        setContentView(R.layout.layout_module);
        
        ATManager.addActivity(this);
        
        context = this;
        
        localActivityManager = getLocalActivityManager();

        container = (ScrollView)findViewById(R.id.container_index);
        menu_ss = (LinearLayout)findViewById(R.id.menu_ss);
        menu_fl = (LinearLayout)findViewById(R.id.menu_fl);
        menu_sc = (LinearLayout)findViewById(R.id.menu_sc);
        

        //第一次载入
    	if(localActivityManager.getActivity("IndexGalleryActivity")==null){
	        Intent it = new Intent(this, IndexGalleryActivity.class);
	        container.addView(getLocalActivityManager().startActivity(IndexGalleryActivity,it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView());
    	}
        
        //首页搜索
        menu_ss.setOnClickListener(new OnClickListener() {
        	
            @Override
            public void onClick(View v) {

                menu_ss.setBackgroundResource(R.drawable.menu_change);
    			menu_fl.setBackgroundResource(R.drawable.menu_def);
    			menu_sc.setBackgroundResource(R.drawable.menu_def);
    			
            	Activity forward_activity = localActivityManager.getActivity(IndexGalleryActivity);

            	resumeActivity(forward_activity,IndexGalleryActivity.class,IndexGalleryActivity);
            	
            }
        });
        
        //分类
        menu_fl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
    			menu_ss.setBackgroundResource(R.drawable.menu_def);
    			menu_fl.setBackgroundResource(R.drawable.menu_change);
    			menu_sc.setBackgroundResource(R.drawable.menu_def);
    			
            	Activity forward_activity = localActivityManager.getActivity(ProductActivity);
            	
        		resumeActivity(forward_activity,ProductActivity.class,ProductActivity);
        			
            }
        });

        //收藏
        menu_sc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, MyScActivity.class);
                startActivity(it);
            }
        });
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
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	 switch (id) {
	         case Const.DIALOG_YES_NO_MESSAGE:
	        	 return new AlertDialog.Builder(context)
	             .setTitle("确定退出程序？")//设置对话框的标题
	             .setPositiveButton("确定", new DialogInterface.OnClickListener() {//设置按下表示确定按钮时按钮的text，和按钮的事件监听器
	                 @Override
					public void onClick(DialogInterface dialog, int whichButton) {
	                	 removeDialog(Const.DIALOG_YES_NO_MESSAGE);
	                	 ATManager.exitClient(context);
	                 }
	             })
	             .setNegativeButton("取消", new DialogInterface.OnClickListener() {//设置取消按钮的text 和监听器
	                 @Override
					public void onClick(DialogInterface dialog, int whichButton) {
	                	 dialog.dismiss();
	                 }
	             })
	             .create();
    	 }

     	return null;
    }

    /**
     * 监听返回键事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
    		showDialog(Const.DIALOG_YES_NO_MESSAGE);
    		return true;
    	}else{
    		return super.onKeyDown(keyCode, event);
    	}
    }
    
}
