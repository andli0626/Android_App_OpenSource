package com.yizhao.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yizhao.activity.R;
import com.yizhao.bean.ProductBean;
import com.yizhao.core.CacheManager;
import com.yizhao.util.DataFormalUtil;

public class IndexImageAdapter extends BaseAdapter{
	
    private Context context;
    
    private ArrayList<ProductBean> popFileList;
    
    private int popSize;
    
    public IndexImageAdapter(Context c,ArrayList<ProductBean> _popFileList){
        context = c;
        popFileList = _popFileList;
        if(popFileList!=null){
    		popSize = popFileList.size();//因是固定的条目，所以只计算一次
        }
    }
    
    @Override
    public int getCount(){
    	return popSize;
    }
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	LayoutInflater inflater = LayoutInflater.from(context);//inflater对象可以把xml转换为view
    	View template = inflater.inflate(R.layout.center, null);
    	
    	if(popFileList!=null){
    		
    		TextView tv_name = (TextView)template.findViewById(R.id.name);
        	tv_name.setText(popFileList.get(position).getName());

        	TextView tv_lowprice = (TextView)template.findViewById(R.id.lowprice);
        	tv_lowprice.setText(DataFormalUtil.convertPrice(popFileList.get(position).getLowprice())+"元");

        	TextView tv_highprice = (TextView)template.findViewById(R.id.highprice);
        	tv_highprice.setText(DataFormalUtil.convertPrice(popFileList.get(position).getHighprice())+"元");
        	

        	TextView tv_shops = (TextView)template.findViewById(R.id.shops);
        	tv_shops.setText(""+popFileList.get(position).getShops()+"家");

        	TextView tv_reviews = (TextView)template.findViewById(R.id.reviews);
        	tv_reviews.setText(popFileList.get(position).getReviews()+"人");
        	
        	Bitmap t_bitmap = CacheManager.getInstance().getBitmap(popFileList.get(position).getId());
        	if(t_bitmap!=null){
	        	ImageView imageView = (ImageView)template.findViewById(R.id.coverimage);
	        	imageView.setImageBitmap(t_bitmap);
	        	//imageView.setImageBitmap(ImageUtil.createReflectedImage(t_bitmap));
        	}
        	
        	
    	}
    	//http://img03.taobaocdn.com/bao/uploaded/i3/T1GsJIXeXBXXXB60UZ_032045.jpg_sum.jpg
    	//imageView.setImageResource(imageInteger[position]);
//       ImageView imageView = new ImageView(context);
//        imageView.setImageResource(imageInteger[position]);
//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        imageView.setLayoutParams(new Gallery.LayoutParams(136, 88));
//    	Animation animation = AnimationUtils.loadAnimation(context, R.anim.my_scale_action);
//    	template.startAnimation(animation);
        return template;
    }
}
