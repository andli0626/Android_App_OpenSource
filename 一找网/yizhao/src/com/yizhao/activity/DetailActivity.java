package com.yizhao.activity;

import java.util.ArrayList;
import java.util.Map;

import com.yizhao.adapter.DetailImageAdapter;
import com.yizhao.bean.ProductDetailBean;
import com.yizhao.bean.ShopsBean;
import com.yizhao.core.ATManager;
import com.yizhao.core.AsyncWorkHandler;
import com.yizhao.core.Const;
import com.yizhao.core.DBHelper;
import com.yizhao.ui.DetialGallery;
import com.yizhao.util.BitmapDownloaderTask;
import com.yizhao.util.DataFormalUtil;
import com.yizhao.util.ImageUtil;
import com.yizhao.util.NetUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;
import android.os.Message;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends Activity{
	
	private Context _context;//当前Activity上下文
	
	private Intent _intent;//转发器
	
	private TextView highprice;//最高报价
	
	private TextView lowprice;//最低报价
	
	private TextView shops;//商家数
	
	private TextView receives;//评论数
	
	private LinearLayout shops_layout;//商家展现容器
	
	private ProductDetailBean detailBean;//产品详情信息
	
	private DetailImageAdapter photoAdapter;//图片适配器
	
	private DetialGallery detailGallery;//图片滑动容器
	
	private LayoutInflater inflater;//View工厂类
	
	private TextView detail_bt_sc;//收藏按钮
	
	private TextView detail_bt_fx;//分享按钮
	
	private ArrayList<ShopsBean> fileList;
	
	private String _coverImage;
	
	private Bitmap coverBitmap;
	
	private String coverImagePath;//图像存储的本地路径
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.detail);
        
        super.onCreate(savedInstanceState);
        
        init();

        if(detailBean!=null && "true".equals(detailBean.getResult())){
        	
			highprice.setText(DataFormalUtil.convertPrice(detailBean.getHighprice())+"元");
			lowprice.setText(DataFormalUtil.convertPrice(detailBean.getLowprice())+"元");
			shops.setText(detailBean.getShops()+"家");
			receives.setText(detailBean.getReviews()+"人");
			
			fileList = detailBean.getFileList();
			
			if(fileList!=null){
				
				for(int i = 0 ; i < fileList.size(); i++){
					View template = inflater.inflate(R.layout.shops_view, null);
					TextView shop_name = (TextView)template.findViewById(R.id.shop_name);
					TextView shop_price = (TextView)template.findViewById(R.id.shop_pric);
					shop_name.setText(fileList.get(i).getShopName());
					shop_price.setText(DataFormalUtil.convertPrice(fileList.get(i).getPrice())+"元");
					template.setBackgroundResource(R.drawable.detail_shops_selec);
					
					final String sellUrl = NetUtil.getUrl(fileList.get(i).getSellUrl());
					
			    	template.setOnClickListener(new OnClickListener(){
			    		@Override
			    		public void onClick(View v) {
			    			Uri uri = Uri.parse(sellUrl);
			    			if(uri!=null){
			    				Intent it = new Intent(Intent.ACTION_VIEW,uri);
				    			startActivity(it);
			    			}else{
			    	        	Toast.makeText(_context, "链接地址不正确,请选择其它商品查看！", Toast.LENGTH_SHORT).show();
			    			}
			    			
			    		}
			    	});
			    	shops_layout.addView(template);
				}
			
			}
			
			_coverImage = detailBean.getCoverImage();
			
        	if(_coverImage!=null){

        		
            	BitmapDownloaderTask task = new BitmapDownloaderTask(_coverImage){
            		
            		@Override
            		protected Bitmap doInBackground(String... params) {
            			//首先获取首页图片，让用户先可见
						try {
							coverBitmap = ImageUtil.getBitmap(ImageUtil.getPicUrl(_coverImage,3));
							photoAdapter.addPic(coverBitmap);
						} catch (Exception e) {
							Log.e(Const.TAG, "DetailActivity._coverImage asyncTask|getCoverImage fail...",e);
						}
            			return null;
            		}
            		
            		@Override
            		protected void onPostExecute(Bitmap bm){
        				photoAdapter.notifyDataSetChanged();
            			//再慢慢获取其它图片
            			String photos = detailBean.getPhotos();
                    	if(photos!=null){
                        	String[] pic_urls = photos.split("\\|");
                        	for(int  i = 0 ; i < pic_urls.length; i++){
                                	String image_url = ImageUtil.getPicUrl(pic_urls[i], 3);
                                	BitmapDownloaderTask task = new BitmapDownloaderTask(image_url){
                                		@Override
                                		protected void onPostExecute(
                                				Bitmap bm) {
                                			if(isCancelled()){
                                				bm = null;
                                			}
                                			if(bm!=null){
                                				photoAdapter.addPic(bm);
                                				photoAdapter.notifyDataSetChanged();
                                        	}
                                		}
                                	};
                                	task.execute("");
                	    	}
                        }
                    	
            			if(isCancelled()){
            				bm = null;
            			}
            			
            			if(bm!=null){
            				photoAdapter.addPic(bm);
            				photoAdapter.notifyDataSetChanged();
                    	}
            		}
            	};
            	
            	task.execute("");
			}
			
			_intent.putExtra("status", true);
	    	
    	}else{
        	Toast.makeText(_context, "信息不存在,请选择其它商品查看！", Toast.LENGTH_SHORT).show();
        	DetailActivity.this.finish();
        }
        
        //收藏商品
        detail_bt_sc.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				boolean sdCard = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
				
				if(sdCard){
				
			        AsyncWorkHandler asyncQueryHandler = new AsyncWorkHandler(){
			            @Override
			            public Object excute(Map<String, String> map) {
			            	
			            	Integer res = -1;
			            	
							if(detailBean!=null && "true".equals(detailBean.getResult())){
								
								try {
									
									DBHelper dbHelp = new DBHelper(_context);
									
									boolean findIt = dbHelp.queryData(detailBean.getId());
									
									if(findIt){
										
										res = 1;//该产品曾被收藏
										
									}else{
											if(coverImagePath==null && coverBitmap!=null){
												coverImagePath = ImageUtil.saveToSDCard(coverBitmap, detailBean.getCoverImage());
						 					}
											
											boolean insert_ok = dbHelp.save(detailBean.getId(), detailBean.getName(), detailBean.getLowprice(), detailBean.getShops(), detailBean.getReviews(),(coverImagePath==null?"":coverImagePath));
											
											if(insert_ok){
												res = 0;
											}
									}
									
									
									dbHelp.close();
								} catch (Exception e) {
									Log.e(Const.TAG, "DetailActivity.detail_bt_sc|Exception:",e);
								}
								
							}
							
							removeDialog(Const.PROGRESSBAR_WAIT);
			        		
			            	return res;
			            }
			            
			            @Override
			            public void handleMessage(Message msg) {
	
							String message = "收藏失败！";
							
							Integer res = -1;
							
							if(msg.obj!=null){
								res = (Integer)msg.obj;
							}
							
							if(res==0){
								message = "收藏成功！";
							}else if(res==1){
								message = "该商品已经收藏！";
							}else{
								message = "收藏失败！";
							}
							
							Toast.makeText(_context, message , Toast.LENGTH_SHORT).show();
							
			            }
			        };
			        //异步获取信息
			        showDialog(Const.PROGRESSBAR_WAIT);
			    	asyncQueryHandler.doWork(null);
				}else{
					Toast.makeText(_context, "收藏失败,请插入存储卡！", Toast.LENGTH_SHORT).show();
				}
			}
        	
        });
        
        
        //分享
        detail_bt_fx.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				 
 				if(detailBean!=null){
 					
 					if(coverImagePath==null && coverBitmap!=null){
						boolean sdCard = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
						if(sdCard){
							coverImagePath = ImageUtil.saveToSDCard(coverBitmap, detailBean.getCoverImage());
						}else{
							Toast.makeText(_context, "没有找到您的存储卡,您只能分享文字信息！", Toast.LENGTH_SHORT).show();
						}
 					}
					
 	            	Intent it = new Intent(_context,BlogActivity.class);
 	            	
 					StringBuffer s_t = new StringBuffer();
 					//商品标题+ “一找网购分析结果：” + 商家数 + “家网店有买，” + 评论数 + “人发表评论。最高价为” + 最高价 + “元，最低价为”+ 最低价 + “元。详情请登陆：+url。”
 					s_t.append(detailBean.getName())
 						.append(" 一找网购分析结果：")
 						.append(detailBean.getShops())
 						.append("家网店有买，")
 						.append(detailBean.getReviews()).append("人发表评论。")
 						.append("最高价为")
 						.append(DataFormalUtil.convertPrice(detailBean.getHighprice()))
 						.append("元，最低价为")
 						.append(DataFormalUtil.convertPrice(detailBean.getLowprice()))
 						.append("元。详情请登陆 :http://www.yeezhao.com/detail-"+detailBean.getId()+".html");
 					
 					it.putExtra("text", s_t.toString());
 					it.putExtra("localPicPath", coverImagePath);
 	 				startActivity(it);
 				}
				
			}
        	
        });
        
    }
    
    /**
     * 初始化所需要的信息
     */
    private void init(){

        _context = this;
        
        _intent = this.getIntent();
        
        inflater = LayoutInflater.from(_context);
        
        highprice = (TextView)findViewById(R.id.detail_highprice);
        
        lowprice = (TextView)findViewById(R.id.detail_lowprice);

        shops = (TextView)findViewById(R.id.detail_shops);

        receives = (TextView)findViewById(R.id.detail_receives);
        
        shops_layout = (LinearLayout)findViewById(R.id.detail_shops_layout);
        
        detail_bt_sc = (TextView)findViewById(R.id.detail_bt_sc);
        
        detail_bt_fx = (TextView)findViewById(R.id.detail_bt_fx);
        
        detailGallery = (DetialGallery)findViewById(R.id.detail_gallery);
        
        detailGallery.setUnselectedAlpha(1.1f);
        
    	photoAdapter = new DetailImageAdapter(_context);
    	
    	detailGallery.setAdapter(photoAdapter);
        
        Object obj = _intent.getSerializableExtra("detailBean");
        
        if(obj!=null){
        	detailBean = (ProductDetailBean)obj;
        }
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
	         case Const.PROGRESSBAR_WAIT:
	        	 ProgressDialog wait_pd = new ProgressDialog(this);
	        	 wait_pd.setMessage(Const.SAVHING);
	        	 return wait_pd; 
    	 }

     	return null;
    }
    

}