package com.yizhao.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.yizhao.adapter.SCAdapter;
import com.yizhao.bean.ImageBean;
import com.yizhao.bean.SearchProductBean;
import com.yizhao.core.AsyncWorkHandler;
import com.yizhao.core.DBHelper;
import com.yizhao.util.FileUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MyScActivity extends Activity{
	
	private ImageView goback;
	
	private Context _context;
	
	private ListView mysc_listview;
	
	private SCAdapter listItemAdapter;
	
	private DBHelper dbHelp;
	
	private FileUtils fileUtil;
	
	private ArrayList<SearchProductBean> searchList;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        setContentView(R.layout.mysc);
        
        super.onCreate(savedInstanceState);
        
        fileUtil = new FileUtils();

        _context = this;
        
        dbHelp = new DBHelper(this);
        
        mysc_listview = (ListView)findViewById(R.id.mysc_listview);

        goback = (ImageView)findViewById(R.id.mysc_goback);

        goback.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				MyScActivity.this.finish();
			}
        });
        
        Cursor cursor = dbHelp.loadALL();
        
        if(cursor!=null){
        	cursor.moveToFirst();
			searchList = new ArrayList<SearchProductBean>();
			while(!cursor.isAfterLast()){
				SearchProductBean bean = new SearchProductBean();
				bean.setId(cursor.getString(0));//产品ID
				bean.setName(cursor.getString(1));//产品名称
				bean.setRefprice(cursor.getInt(2));//产品价格
				bean.setShops(cursor.getInt(3));//商家数量
				bean.setReviews(cursor.getInt(4));//评论数量
				bean.setCoverImage(cursor.getString(5));//产品图片地址
				searchList.add(bean);
				cursor.moveToNext();
			}
			cursor.close();
		}
        dbHelp.close();
        listItemAdapter = new SCAdapter(_context,searchList,fileUtil);
        mysc_listview.setAdapter(listItemAdapter);
        
        
        ArrayList<SearchProductBean> pb = listItemAdapter.getNoImageList();
                
        if(pb!=null){
        	
        	int len = pb.size();
        	
            for(int i = 0; i < len; i++){
            	
            	AsyncWorkHandler asyncQueryHandler = new AsyncWorkHandler(){
                    @Override
                    public Object excute(Map<String, String> map) {
                    	
                    	ImageBean bean = null;
                    	
                    	Bitmap bm = null;
                    	 
                    	String pic_url = map.get("coverimage");

                    	if(pic_url!=null && !"".equals(pic_url)){
                    		bm = BitmapFactory.decodeFile(pic_url);
                    	}
                    	
                    	if(bm!=null){
                    		bean = new ImageBean();
                    		bean.id = map.get("id");
                    		bean.bitmap = bm;
                    	}
                    	return bean;
                    }
                    
                    @Override
                    public void handleMessage(Message msg) {
                    	if(msg.obj!=null){
                    		ImageBean bean = (ImageBean)msg.obj;
                    		listItemAdapter.putBitmap(bean.id, bean.bitmap);
                    		listItemAdapter.notifyDataSetChanged();
                    	}
                    }
                    
                };
                HashMap<String,String> _map = new HashMap<String,String>();
                _map.put("id", pb.get(i).getId());
                _map.put("coverimage", pb.get(i).getCoverImage());
                //异步获取图片
            	asyncQueryHandler.doWork(_map);
            }
            
            
        }
        
        
        mysc_listview.setOnItemClickListener(new OnItemClickListener(){
    		@Override
    		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
    				long arg3) {
    			Intent intent = new Intent();
            	intent.setClass(_context, DetailActivityGroup.class);
    			intent.putExtra("product_id", searchList.get(position).getId());
            	startActivity(intent);
    		}
    	});
       
        
    }
    
    
}