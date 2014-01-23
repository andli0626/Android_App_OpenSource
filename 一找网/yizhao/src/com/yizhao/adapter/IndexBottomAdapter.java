package com.yizhao.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yizhao.activity.R;
import com.yizhao.bean.SaleBean;

public class IndexBottomAdapter extends BaseAdapter{
	
    private Context context;
    
	private ArrayList<SaleBean> promFileList;
	
    private int promSize;
    
    public IndexBottomAdapter(Context c,ArrayList<SaleBean> _promFileList){
    	
        context = c;
        
        promFileList = _promFileList;
        
        if(promFileList!=null){
        	promSize = promFileList.size();//因是固定的条目，所以只计算一次
        }
        
    }
    
    @Override
    public int getCount() {
        return promSize;
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
    	View template = inflater.inflate(R.layout.g_bottom, null);
    	
    	if(promFileList!=null){
    		TextView textView = (TextView)template.findViewById(R.id.name);
        	textView.setText(promFileList.get(position).getName());
    	}
    	
        return template;
    }
}
