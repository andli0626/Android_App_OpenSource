package com.yizhao.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.yizhao.action.ProductAction;
import com.yizhao.adapter.IndexBottomAdapter;
import com.yizhao.adapter.IndexImageAdapter;
import com.yizhao.bean.HotProductBean;
import com.yizhao.bean.ProductBean;
import com.yizhao.bean.SaleBean;
import com.yizhao.core.AsyncWorkHandler;
import com.yizhao.core.CacheManager;
import com.yizhao.core.Const;
import com.yizhao.ui.DetialGallery;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class IndexGalleryActivity extends Activity implements OnItemSelectedListener{
	
	private DetialGallery gallery_center;
	private DetialGallery gallery_bottom;
	
	private AutoCompleteTextView search_text;
	
	private ImageView search_button;
	
	private IndexImageAdapter adapter_gallery_center;
	
	private IndexBottomAdapter adapter_gallery_bottom;
		
	private Context _context;
	
	private Intent _intent;
	
	private ArrayList<ProductBean> popFileList;
	
	private ArrayList<SaleBean> promFileList;
	
	private LinearLayout gallery_center_point;
	
	private OnItemSelectedListener osSelectedListener;
	
	private LayoutInflater inflater;//View工厂类
	
	private int point_count;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        setContentView(R.layout.main);
        
        super.onCreate(savedInstanceState);
        
        osSelectedListener = this;
        
        _context = this;
        
        _intent = this.getIntent();
        
        inflater = LayoutInflater.from(_context);
        
        search_text=(AutoCompleteTextView)findViewById(R.id.index_search_ev);
        
        search_button=(ImageView)findViewById(R.id.index_search_bt);
        
        gallery_center_point = (LinearLayout)findViewById(R.id.gallery_center_point);
        

        //创建适配器 
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,CacheManager.getInstance().getAutoCompleteText());
        search_text.setAdapter(adapter);
        
        //设置输入多少字符后提示，默认值为2
        search_text.setThreshold(2);
        
        gallery_center = (DetialGallery)findViewById(R.id.gallery_center);
        gallery_bottom = (DetialGallery)findViewById(R.id.gallery_bottom);
        
        
      //初始化信息
        AsyncWorkHandler asyncQueryHandler = new AsyncWorkHandler(){
    		
            @Override
            public Object excute(Map<String, String> map) {

            	HotProductBean bean = CacheManager.getInstance().getHostProductBean();
            	
                if(bean==null){
                	bean = ProductAction.getHotProduct();
                }
                
            	return bean;
            	
            }
            
            @Override
            public void handleMessage(Message msg) {
            	
            	if(msg.obj!=null){
            		
            		HotProductBean bean = (HotProductBean)msg.obj;
            		
            		if("true".equals(bean.getResult())){
                    	
                    	popFileList = bean.getPopFileList();
                    	promFileList = bean.getPromFileList();
                    	
                    	int popSize = popFileList.size();
                    	
                    	adapter_gallery_center = new IndexImageAdapter(_context,popFileList);
                    	adapter_gallery_bottom = new IndexBottomAdapter(_context,promFileList);
                    	
                    	//初始化5个分页的点
                    	point_count = popSize;
                    	for(int i = 0 ; i < point_count ; i++){
                    		View template = inflater.inflate(R.layout.coin, null);
                    		gallery_center_point.addView(template);
                    	}
                    	if(gallery_center_point.getChildCount() > 0){
                    		LinearLayout coin_layout = (LinearLayout)gallery_center_point.getChildAt(0);
                    		ImageView child_first = (ImageView)coin_layout.getChildAt(0);
                    		child_first.setImageResource(R.drawable.index_body_coin2);
                    	}
                    	
                        gallery_center.setAdapter(adapter_gallery_center);//设置图片适配器
                        gallery_bottom.setAdapter(adapter_gallery_bottom);
                        
                        gallery_center.setOnItemSelectedListener(osSelectedListener);
                        gallery_bottom.setOnItemSelectedListener(osSelectedListener);
                        
                        removeDialog(Const.PROGRESSBAR_WAIT);
                        
                    	_intent.putExtra("status", true);

                        //开启多个线程去拉取图片------begin
                        for(int i = 0; i < popSize; i++){
                        	
                        	final String id = popFileList.get(i).getId();
                        	final String image_url = ImageUtil.getPicUrl(popFileList.get(i).getCoverImage(),2);
                        	
                        	BitmapDownloaderTask task = new BitmapDownloaderTask(image_url){
                        		
                        		@Override
                        		protected Bitmap doInBackground(String... params) {
                        			Bitmap bm = CacheManager.getInstance().getBitmap(id);
                        			if(bm==null){
                        				bm = ImageUtil.getBitmap(image_url);
                        			}
                        			return bm;
                        		}
                        		
                        		@Override
                        		protected void onPostExecute(Bitmap bm) {
                        			if(isCancelled()){
                        				bm = null;
                        			}
                        			if(bm!=null){
                                		if(!CacheManager.getInstance().getBitmap_Map().containsKey(id)){
                                			CacheManager.getInstance().putBitmap(id, bm);
                                		}
                                		adapter_gallery_center.notifyDataSetChanged();
                                	}
                        		}
                        	};
                        	task.execute("");
                        }
                        //开启多个线程去拉取图片------end
                    }else{
                    	Toast.makeText(_context, "信息不存在,请选择其它商品查看！", Toast.LENGTH_SHORT).show();
                    }
            		
            	}else{
            		Toast.makeText(_context, "网络不稳定,请检查您的网络状态！", Toast.LENGTH_SHORT).show();
            	}
            	
            	removeDialog(Const.PROGRESSBAR_WAIT);
				
            }
            
        };
        showDialog(Const.PROGRESSBAR_WAIT);
    	asyncQueryHandler.doWork(new HashMap<String,String>());
    	
        
        //设置监听器
        gallery_center.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,long arg3) {
            	
            	String product_id = "";
            	
            	if(popFileList!=null){
            		product_id = popFileList.get(position).getId();
            	}
            	
            	Intent intent = new Intent();
            	intent.putExtra("product_id", product_id);
            	intent.setClass(_context, DetailActivityGroup.class);
            	startActivity(intent);
            }
        });
        
        gallery_bottom.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
            	
            	String product_id = "";
            	
            	if(promFileList!=null){
            		product_id = promFileList.get(position).getId();
            	}
               	Intent intent = new Intent();
            	intent.putExtra("product_id", product_id);
            	intent.setClass(_context, DetailActivityGroup.class);
            	startActivity(intent);
            }
        });
        
        search_button.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String keyworld = search_text.getText().toString();
				if(keyworld!=null && !"".equals(keyworld)){
					Intent it = new Intent(_context, SearchActivity.class);
					it.putExtra("sname",keyworld);
	            	startActivity(it);
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


	@Override
	public void onItemSelected(AdapterView<?> parent, View arg1, int position,long arg3) {
		if(parent == gallery_center && point_count >=position) {
			for(int i = 0 ; i < point_count ; i++){
				LinearLayout coin_layout = (LinearLayout)gallery_center_point.getChildAt(i);
        		ImageView child_first = (ImageView)coin_layout.getChildAt(0);
        		child_first.setImageResource(R.drawable.index_body_coin1);
        	}
			LinearLayout coin_layout = (LinearLayout)gallery_center_point.getChildAt(position);
			ImageView child_first = (ImageView)coin_layout.getChildAt(0);
        	child_first.setImageResource(R.drawable.index_body_coin2);
		}
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}