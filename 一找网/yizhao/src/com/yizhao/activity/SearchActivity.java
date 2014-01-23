package com.yizhao.activity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.yizhao.action.ProductAction;
import com.yizhao.adapter.SearchAdapter;
import com.yizhao.bean.ImageBean;
import com.yizhao.bean.SearchBean;
import com.yizhao.bean.SearchProductBean;
import com.yizhao.core.ATManager;
import com.yizhao.core.AsyncWorkHandler;
import com.yizhao.core.Const;
import com.yizhao.util.BitmapDownloaderTask;
import com.yizhao.util.ImageUtil;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
//import android.view.KeyEvent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchActivity extends Activity{
	
	private ListView listView;
	
	private TextView tv;
	
	private ImageView goback;
	
	private SearchAdapter listItemAdapter;
	
	private ArrayList<SearchProductBean> searchList;
	
	private Context _context;
	
	private Intent _intent;

	private LayoutInflater inflater;
	
	private View footer;
	
    private int curpage = 1;//当前页
    
    private int pages = 1;//共多少页
    
    private String keyworld;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_res);
        
        ATManager.addActivity(this);
        
        _context = this;
        
        _intent = this.getIntent();

        inflater = LayoutInflater.from(_context);
        
        footer = inflater.inflate(R.layout.search_footer, null);
        
        listView = (ListView)findViewById(R.id.search_listview);
        
        listView.addFooterView(footer);
        
        goback = (ImageView)findViewById(R.id.search_goback);
        
        tv = (TextView)findViewById(R.id.search_count_tv);
        
        keyworld = _intent.getStringExtra("sname");
        
        AsyncWorkHandler asyncQueryHandler = new AsyncWorkHandler(){
        	
            @Override
            public Object excute(Map<String, String> params) {
            	return ProductAction.getSearchBean(params);
            }
            
            @Override
            public void handleMessage(Message msg) {
            	
            	if(msg.obj!=null){
            		
            		SearchBean bean = (SearchBean)msg.obj;
            		if("true".equals(bean.getResult())){
            			
                      	tv.setText("共"+bean.getTotal()+"个搜索结果");
                      	
                     	pages = (bean.getTotal()%Const.PAGE_SIZE_INT == 0) ?  bean.getTotal()/Const.PAGE_SIZE_INT : bean.getTotal()/Const.PAGE_SIZE_INT+1;
                     	
                      	searchList = bean.getFilelist();
                      	
                      	listItemAdapter = new SearchAdapter(_context,searchList);
                    	
                      	if(listItemAdapter!=null){
                    		
                    		listView.setAdapter(listItemAdapter);
                    		
                    		 //开启多个线程去拉取图片------begin
                            if(searchList!=null){
                            	
                            	int len = searchList.size();
                            	
                                for(int i = 0; i < len; i++){
                                	
                                	final String id = searchList.get(i).getId();
                                	String image_url = ImageUtil.getPicUrl(searchList.get(i).getCoverImage(), 1);
                                	BitmapDownloaderTask task = new BitmapDownloaderTask(image_url){
                                		@Override
                                		protected void onPostExecute(
                                				Bitmap bm) {
                                			if(isCancelled()){
                                				bm = null;
                                			}
                                			if(bm!=null){
                                				ImageBean bean = new ImageBean();
	                                    		bean.id = id;
	                                    		bean.bitmap = bm;
	                                    		listItemAdapter.putBitmap(bean.id, bean.bitmap);
	                                    		listItemAdapter.notifyDataSetChanged();
	                                    	}
                                		}
                                	};
                                	task.execute("");
                                }
                                
                            }
                            //开启多个线程去拉取图片------end
                    	}
                      	
                    }
            	}
            	
            	removeDialog(Const.PROGRESSBAR_WAIT);
            	
            }
            
        };
        //异步获取信息,实现两个方法excute跟onCompleteWork
        Map<String,String> params = new HashMap<String,String>();
        showDialog(Const.PROGRESSBAR_WAIT);
        params.put("n",keyworld);
        params.put("from", _intent.getStringExtra("from"));
        params.put("p", "1");
    	asyncQueryHandler.doWork(params);    	
    	
    	
    	footer.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(curpage < pages){
					
					int cur = curpage+1;
					
					AsyncWorkHandler asyncQueryHandler = new AsyncWorkHandler(){
			        	
			            @Override
			            public Object excute(Map<String, String> params) {
			            	 return ProductAction.getSearchBean(params);
			            }
			            
			            @Override
			            public void handleMessage(Message msg) {
			            	
			            	if(msg.obj!=null){
			            		
			            		SearchBean bean = (SearchBean)msg.obj;
			            		
			            		if("true".equals(bean.getResult())){
			                      	
			            			ArrayList<SearchProductBean> tmpList = bean.getFilelist();
			            			
			            			if(tmpList!=null && tmpList.size() > 0){
			            				
			            				curpage++;
			            				
			            				for(SearchProductBean spbean : tmpList){
			            					searchList.add(spbean);
			            				}
			            				
			            				Log.d(Const.TAG, "SearchActivity.AsyncWork|curpage="+curpage+",pages="+pages+",fileList.size="+searchList.size());
			            				
			            				listItemAdapter.notifyDataSetChanged();
			            				
			            				//开启多个后台线程去拉取图片
			                    		
			                            if(tmpList!=null){
			                            	int len = tmpList.size();
			                            	
			                                for(int i = 0; i < len; i++){
			                                	
			                                	final String id = tmpList.get(i).getId();
			                                	String image_url = ImageUtil.getPicUrl(tmpList.get(i).getCoverImage(), 1);
			                                	BitmapDownloaderTask task = new BitmapDownloaderTask(image_url){
			                                		@Override
			                                		protected void onPostExecute(
			                                				Bitmap bm) {
			                                			if(isCancelled()){
			                                				bm = null;
			                                			}
			                                			if(bm!=null){
			                                				ImageBean bean = new ImageBean();
				                                    		bean.id = id;
				                                    		bean.bitmap = bm;
				                                    		listItemAdapter.putBitmap(bean.id, bean.bitmap);
				                                    		listItemAdapter.notifyDataSetChanged();
				                                    	}
			                                		}
			                                	};
			                                	task.execute("");
			                                }
			                                
			                            }
			                          
			            			}
			                      	
			                    }
			            		
			            	}
			            	
			            	removeDialog(Const.PROGRESSBAR_WAIT);
			            	
			            }
			            
			        };
			        //异步获取信息,实现两个方法excute跟onCompleteWork
			        Map<String,String> params = new HashMap<String,String>();
			        showDialog(Const.PROGRESSBAR_WAIT);
			        params.put("n",keyworld);
			        params.put("from", _intent.getStringExtra("from"));
			        params.put("p", ""+cur);
			    	asyncQueryHandler.doWork(params);
				}else{
					Toast.makeText(_context, "已经到最后一页", Toast.LENGTH_SHORT).show();
				}
				
			}
    		
    	});
    	
    	
    	listView.setOnItemClickListener(new OnItemClickListener(){
    		@Override
    		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
    				long arg3) {
    			Intent intent = new Intent();
    			intent.putExtra("product_id", searchList.get(position).getId());
            	intent.setClass(_context, DetailActivityGroup.class);
            	startActivity(intent);
    		}
    	});
    	
    	
    	goback.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				SearchActivity.this.finish();
			}
        });
        
    }

    @Override
    protected Dialog onCreateDialog(int id) {
    	 switch (id) {
	         case Const.PROGRESSBAR_WAIT:
	        	 ProgressDialog wait_pd = new ProgressDialog(this);
	        	 wait_pd.setMessage(Const.SEARCHING);
	        	 return wait_pd;
         }
    	return null;
    }
    
    

//    /**
//     * 监听返回键事件
//     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event){
//    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//    		ATManager.delActivity(this);
//    	}
//    	return super.onKeyDown(keyCode, event);
//    }
}