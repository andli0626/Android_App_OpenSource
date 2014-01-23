package com.yizhao.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.yizhao.bean.ShopsBean;
import com.yizhao.util.DataFormalUtil;
import com.yizhao.activity.R;

public class DetailShopsAdapter extends BaseAdapter{
	
    private Context context;

    private ArrayList<ShopsBean> shopsList;
    
    private int count;
    
    public DetailShopsAdapter(Context c,ArrayList<ShopsBean> list){
        context = c;
        shopsList = list;
    }
    
	@Override
    public int getCount() {
		if(shopsList!=null){
			count = shopsList.size();
		}
        return count;
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
    	View template = inflater.inflate(R.layout.shops_view, null);
    	
    	if(shopsList!=null){
    		
	    	TextView shop_name = (TextView)template.findViewById(R.id.shop_name);
	    	TextView shop_price = (TextView)template.findViewById(R.id.shop_pric);
	    	
	    	shop_name.setText(shopsList.get(position).getShopName());
	    	shop_price.setText(DataFormalUtil.convertPrice(shopsList.get(position).getPrice())+"元");
	    	
    	}
    	
        return template;
    }
}
