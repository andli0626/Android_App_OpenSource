package com.yizhao.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.yizhao.action.ProductAction;
import com.yizhao.adapter.ReceiveAdapter;
import com.yizhao.bean.AuthBean;
import com.yizhao.bean.ReceiveBean;
import com.yizhao.core.AsyncWorkHandler;
import com.yizhao.core.Const;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ReceiveActivity extends Activity{
	
	private Context _context;
	
	private TextView receive_count;
	
	private ListView receive_listview;
	
	private ReceiveAdapter receiveAdapter;

	private LayoutInflater inflater;
	
	private View footer;

    private Intent _intent;
    
    private String pid;
    
    private int curpage = 1;//当前页
    
    private int pages = 1;//共多少页
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        setContentView(R.layout.receive);
        
        super.onCreate(savedInstanceState);
        
        _context = this;

        _intent = this.getIntent();
        
        inflater = LayoutInflater.from(_context);
        
        footer = inflater.inflate(R.layout.receive_footer, null);
        
        receive_listview = (ListView)findViewById(R.id.receive_listview);
        
        receive_listview.addFooterView(footer);
        
        pid = _intent.getStringExtra("product_id");
        
        receive_count = (TextView)findViewById(R.id.receive_count);

        //第一次拉取数据(异步)
        showDialog(Const.PROGRESSBAR_WAIT);
        Map<String,String> param = new HashMap<String,String>();
        param.put("product_id", pid);
        param.put("p", "1");
        AsyncWorkHandler asyncQueryHandler = new AsyncWorkHandler(){
            @Override
            public Object excute(Map<String, String> map) {
            	return ProductAction.getProductReceive(map);
            }
            
            @Override
            public void handleMessage(Message msg) {
            	if(msg.obj!=null){
	            	ReceiveBean bean = (ReceiveBean)msg.obj;
	            	if("true".equals(bean.getResult())){
	            		pages = (bean.getReviews()%Const.PAGE_SIZE_INT == 0) ?  bean.getReviews()/Const.PAGE_SIZE_INT : bean.getReviews()/Const.PAGE_SIZE_INT+1;
	            		int count = bean.getReviews();
	            		receive_count.setText(count+"条");
	            		ArrayList<AuthBean> fileList = bean.getFileList();
	            		receiveAdapter = new ReceiveAdapter(_context);
	            		receiveAdapter.setReceiveList(fileList);
	            		if(receiveAdapter!=null){
	                		receive_listview.setAdapter(receiveAdapter);
	                	}
	            		_intent.putExtra("status", true);
	            	}
            	}
            	removeDialog(Const.PROGRESSBAR_WAIT);
            }
        };
    	asyncQueryHandler.doWork(param);
    	
    	//查看更多
    	footer.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(curpage < pages){
					int cur = curpage+1;
			        AsyncWorkHandler asyncQueryHandler = new AsyncWorkHandler(){
			            @Override
			            public Object excute(Map<String, String> map) {
			            	return ProductAction.getProductReceive(map);
			            }
			            @Override
			            public void handleMessage(Message msg) {
			            	if(msg.obj!=null){
				            	ReceiveBean bean = (ReceiveBean)msg.obj;
				            	if("true".equals(bean.getResult())){
				            		ArrayList<AuthBean> tmpList = bean.getFileList();
				            		if(tmpList!=null && tmpList.size() > 0){
				            			curpage++;
				            			receiveAdapter.containsList(tmpList);
				            			Log.d(Const.TAG, "ReceiveActivity.AsyncWork|curpage="+curpage+",pages="+pages+",receiveAdapter.fileList.size="+receiveAdapter.getReceiveList().size());
				            			receiveAdapter.notifyDataSetChanged();
				            		}
				            	}
			            	}
			            	removeDialog(Const.PROGRESSBAR_WAIT);
			            }
			        };
			        //异步获取信息
			        showDialog(Const.PROGRESSBAR_WAIT);
			        Map<String,String> param = new HashMap<String,String>();
			        param.put("product_id", pid);
			        param.put("p", ""+cur);
			    	asyncQueryHandler.doWork(param);
				}else{
					Toast.makeText(_context, "已经到最后一页", Toast.LENGTH_SHORT).show();
				}
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
         }
    	return null;
    }
}