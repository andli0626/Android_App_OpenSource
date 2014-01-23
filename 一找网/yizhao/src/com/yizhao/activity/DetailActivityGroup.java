package com.yizhao.activity;

import java.util.HashMap;
import java.util.Map;

import com.yizhao.action.ProductAction;
import com.yizhao.bean.ProductDetailBean;
import com.yizhao.core.ATManager;
import com.yizhao.core.AsyncWorkHandler;
import com.yizhao.core.Const;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LocalActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 使用ActivityGroup来切换Activity和Layout
 * @author
 */
public class DetailActivityGroup extends ActivityGroup {
	
	private LinearLayout container = null;

	private ImageView goback;
    
    private Context context;

    private TextView detail_tab1;
    
    private TextView detail_tab2;
    
    private TextView detail_tab3;
    
    private LocalActivityManager localActivityManager;
    
	private ProductDetailBean detailBean;

    private TextView detail_title;
    
    private Intent _intent;
    
    private String pid;
    
    private void resumeActivity(Activity activity,Class<?> cls,String activityId){

		boolean status = false;
		
		if(activity!=null){
			status = activity.getIntent().getBooleanExtra("status", false);
		}
		
		Log.d(Const.TAG, "DetailActivityGroup.resumeActivity|activity="+activity+",activityId="+activityId+",status="+status);
		
		if(activity!=null && status){
    		View activityView = activity.getWindow().getDecorView();
			if(container!=null){
    			container.removeAllViews();
    			container.addView(activityView);
			}
		}else{
			 Intent it = new Intent(this, cls);
		     container.addView(getLocalActivityManager().startActivity(activityId,it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView());
	    }
		
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.detail_module);
        
        ATManager.addActivity(this);
        
        context = this;
        
        _intent = this.getIntent();
        
        localActivityManager = getLocalActivityManager();
        
        pid = _intent.getStringExtra("product_id");
        
        goback = (ImageView)findViewById(R.id.detail_goback);
        
        detail_title = (TextView)findViewById(R.id.detail_title);

        goback.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				DetailActivityGroup.this.finish();
			}
        });
        
        container = (LinearLayout) findViewById(R.id.detailBody);
       
        detail_tab1 = (TextView) findViewById(R.id.detail_tab1);
        detail_tab2 = (TextView) findViewById(R.id.detail_tab2);
        detail_tab3 = (TextView) findViewById(R.id.detail_tab3);
        
        //第一次载入
    	if(localActivityManager.getActivity("DetailActivity")==null){
    		
            Log.d(Const.TAG, "DetailActivity load first...");
	        
	        AsyncWorkHandler asyncQueryHandler = new AsyncWorkHandler(){

	            @Override
	            public Object excute(Map<String, String> map) {
	            	detailBean = ProductAction.getProductDetail(map);
	            	return null;
	            }
	            
	            @Override
	            public void handleMessage(Message msg) {
	            	if(detailBean!=null && detailBean.getName()!=null && !"".equals(detailBean.getName().trim())){
	            		detail_title.setText(detailBean.getName());
            		}
    		        Intent it = new Intent(context, DetailActivity.class);
            		Bundle mBundel = new Bundle();
            		mBundel.putSerializable("detailBean", detailBean);
        	        it.putExtras(mBundel);
        	        container.addView(getLocalActivityManager().startActivity("DetailActivity",it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView(),LayoutParams.FILL_PARENT,
        	        	     LayoutParams.FILL_PARENT);
        	        removeDialog(Const.PROGRESSBAR_WAIT);
	            }
	            
	        };
	        //异步获取信息
	        showDialog(Const.PROGRESSBAR_WAIT);
	        Map<String,String> param = new HashMap<String,String>();
	        param.put("product_id", pid);
	    	asyncQueryHandler.doWork(param);
    	}
        
        //tab1
        detail_tab1.setOnClickListener(new OnClickListener() {
        	
            @Override
            public void onClick(View v) {
            	
            	Activity forward_activity = localActivityManager.getActivity("DetailActivity");
            	
            	container.removeAllViews();

        		if(forward_activity!=null){
            		resumeActivity(forward_activity,DetailActivity.class,"DetailActivity");
            	}else{
            		Intent it = new Intent();
            		it.setClass(context, DetailActivity.class);
            		it.putExtra("product_id", pid);
            		container.addView(localActivityManager.startActivity("DetailActivity",it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView(),LayoutParams.FILL_PARENT,
       	        	     LayoutParams.FILL_PARENT);
                }
            	detail_tab1.setBackgroundResource(R.drawable.detail_tab_select);
            	detail_tab2.setBackgroundResource(R.drawable.detail_tab_def);
            	detail_tab3.setBackgroundResource(R.drawable.detail_tab_def);
            }
        });
        
        //tab2
        detail_tab2.setOnClickListener(new OnClickListener() {
        	 @Override
             public void onClick(View v) {
             	Activity forward_activity = localActivityManager.getActivity("ReceiveActivity");
            	
            	container.removeAllViews();

        		if(forward_activity!=null){
            		resumeActivity(forward_activity,ReceiveActivity.class,"ReceiveActivity");
            	}else{
            		Intent it = new Intent();
            		it.setClass(context, ReceiveActivity.class);
            		it.putExtra("product_id", pid);
            		container.addView(localActivityManager.startActivity("ReceiveActivity",it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView());
                }
             	detail_tab1.setBackgroundResource(R.drawable.detail_tab_def);
             	detail_tab2.setBackgroundResource(R.drawable.detail_tab_select);
             	detail_tab3.setBackgroundResource(R.drawable.detail_tab_def);
             }
        });

        //tab3
        detail_tab3.setOnClickListener(new OnClickListener() {
        	 @Override
             public void onClick(View v) {
        		 Activity forward_activity = localActivityManager.getActivity("ShopActivity");
             	
             	container.removeAllViews();
         		
         		if(forward_activity!=null){
             		resumeActivity(forward_activity,ShopActivity.class,"ShopActivity");
             	}else{
             		Intent it = new Intent();
             		it.setClass(context, ShopActivity.class);
            		it.putExtra("product_id", pid);
             		container.addView(localActivityManager.startActivity("ShopActivity",it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView());
                 }
             	detail_tab1.setBackgroundResource(R.drawable.detail_tab_def);
             	detail_tab2.setBackgroundResource(R.drawable.detail_tab_def);
             	detail_tab3.setBackgroundResource(R.drawable.detail_tab_select);
             }
        });
    }
    
    
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	 switch (id) {
    	 	case Const.PROGRESSBAR_WAIT:
	        	 ProgressDialog wait_pd = new ProgressDialog(this);
	        	 wait_pd.setMessage(Const.LOADING);
	        	 return wait_pd;
	         case Const.DIALOG_YES_NO_MESSAGE:
	        	 return new AlertDialog.Builder(context)
	             .setTitle("加入收藏？")//设置对话框的标题
	             .setPositiveButton("确定", new DialogInterface.OnClickListener() {//设置按下表示确定按钮时按钮的text，和按钮的事件监听器
	                 @Override
					public void onClick(DialogInterface dialog, int whichButton) {
	                	 removeDialog(Const.DIALOG_YES_NO_MESSAGE);
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
    
}
