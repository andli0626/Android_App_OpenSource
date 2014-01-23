package com.yizhao.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.yizhao.action.ProductAction;
import com.yizhao.adapter.DetailShopsAdapter;
import com.yizhao.bean.DetailShopsBean;
import com.yizhao.bean.ShopsBean;
import com.yizhao.core.AsyncWorkHandler;
import com.yizhao.core.Const;
import com.yizhao.util.NetUtil;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ShopActivity extends Activity{
	
	private Context _context;
	
	private TextView shops_count;
	
	private ListView shops_listview;
	
	private DetailShopsAdapter shopsAdapter;

	private LayoutInflater inflater;
	
	private View footer;

    private Intent _intent;
    
    private String pid;
    
    private int curpage = 1;//当前页
    
    private int pages = 1;//共多少页
    
	private ArrayList<ShopsBean> fileList;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        setContentView(R.layout.shops);
        
        super.onCreate(savedInstanceState);
        
        _context = this;

        _intent = this.getIntent();
        
        inflater = LayoutInflater.from(_context);
        
        footer = inflater.inflate(R.layout.shops_footer, null);
        
        shops_listview = (ListView)findViewById(R.id.shops_listview);
        
        shops_listview.addFooterView(footer);
        
        shops_count = (TextView)findViewById(R.id.shops_count);

        pid = _intent.getStringExtra("product_id");

        AsyncWorkHandler asyncQueryHandler = new AsyncWorkHandler(){
            @Override
            public Object excute(Map<String, String> map) {
            	return ProductAction.getProductShops(map);
            }
            
            @Override
            public void handleMessage(Message msg) {
            	if(msg.obj!=null){
            		DetailShopsBean bean = (DetailShopsBean)msg.obj;
            		if("true".equals(bean.getResult())){
            			pages = (bean.getShops()%Const.PAGE_SIZE_INT == 0) ?  bean.getShops()/Const.PAGE_SIZE_INT : bean.getShops()/Const.PAGE_SIZE_INT+1;
                		shops_count.setText(bean.getShops()+"家");
                		fileList = bean.getFileList();
                		shopsAdapter = new DetailShopsAdapter(_context,fileList);
                		if(shopsAdapter!=null){
                    		shops_listview.setAdapter(shopsAdapter);
                    	}
	            		_intent.putExtra("status", true);
                	}
            	}
            	removeDialog(Const.PROGRESSBAR_WAIT);
            }
        };
        //异步获取信息
        showDialog(Const.PROGRESSBAR_WAIT);
        Map<String,String> param = new HashMap<String,String>();
        param.put("product_id", pid);
        param.put("p", "1");
    	asyncQueryHandler.doWork(param);
        
    	shops_listview.setOnItemClickListener(new OnItemClickListener(){
    		@Override
    		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
    				long arg3) {
	    			Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(NetUtil.getUrl(fileList.get(position).getSellUrl())));
	    			//it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
	    			startActivity(it);
    		}
    	});
    	
    	footer.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(curpage < pages){
					int cur = curpage+1;
			        AsyncWorkHandler asyncQueryHandler = new AsyncWorkHandler(){
			            @Override
			            public Object excute(Map<String, String> map) {
			            	return ProductAction.getProductShops(map);
			            }
			            
			            @Override
			            public void handleMessage(Message msg) {
			            	if(msg.obj!=null){
			            		DetailShopsBean bean = (DetailShopsBean)msg.obj;
			            		if("true".equals(bean.getResult())){
			            			ArrayList<ShopsBean> tmpList = bean.getFileList();
			            			if(tmpList!=null && tmpList.size() > 0){
				            			curpage++;
				            			for(ShopsBean shopBean : tmpList){
				            				fileList.add(shopBean);
				            			}
				            			Log.d(Const.TAG, "ShopActivity.AsyncWork|curpage="+curpage+",pages="+pages+",fileList.size="+fileList.size());
			            				shopsAdapter.notifyDataSetChanged();
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