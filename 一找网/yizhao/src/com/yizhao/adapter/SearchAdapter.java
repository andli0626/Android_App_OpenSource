package com.yizhao.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yizhao.activity.R;
import com.yizhao.bean.SearchProductBean;
import com.yizhao.util.DataFormalUtil;

public class SearchAdapter extends BaseAdapter{
	
    private Context context;

    private ArrayList<SearchProductBean> searchList;

	private HashMap<String,Bitmap> bitmap_Map = new HashMap<String,Bitmap>();
    
    public SearchAdapter(Context c,ArrayList<SearchProductBean> _searchList){
        context = c;
        searchList = _searchList;
    }

    public void putBitmap(String id,Bitmap bitmap){
    	bitmap_Map.put(id, bitmap);
    }
    
    
	@Override
    public int getCount() {
		int size = 0;
		if(searchList!=null){
			size = searchList.size();
		}
        return size;
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
    	
    	//inflater对象可以把xml转换为view
    	LayoutInflater inflater = LayoutInflater.from(context);
    	View template = inflater.inflate(R.layout.search_view, null);
    	
    	if(searchList!=null){
    		
	    	TextView productName = (TextView)template.findViewById(R.id.productName);
	    	TextView productMoney = (TextView)template.findViewById(R.id.productMoney);
	    	TextView sellCount = (TextView)template.findViewById(R.id.sellCount);
	    	TextView tkCount = (TextView)template.findViewById(R.id.tkCount);
	    	
	    	//红色字
//	    	SpannableString productName_s = new SpannableString( "Apple/苹果 iPhone "+position+"代(4G)");
//	    	productName_s.setSpan(new ForegroundColorSpan(Color.RED), 0, 2,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//	    	productName.setText(productName_s);
	    	
	    	productName.setText(searchList.get(position).getName());
	    	productMoney.setText(DataFormalUtil.convertPrice(searchList.get(position).getRefprice())+"元");
	    	sellCount.setText(searchList.get(position).getShops()+"家");
	    	tkCount.setText(searchList.get(position).getReviews()+"人");
	    	
	    	Bitmap t_bitmap = bitmap_Map.get(searchList.get(position).getId());
	    	
	    	if(t_bitmap!=null){
	        	ImageView imageView = (ImageView)template.findViewById(R.id.photoSrc);
	        	imageView.setImageBitmap(t_bitmap);
	    	}
	    	
    	}
    	
        return template;
    }
}
