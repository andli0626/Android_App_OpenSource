package com.yizhao.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.yizhao.activity.R;

public class DetailImageAdapter extends BaseAdapter{
	
    private Context context;
    
    private ArrayList<Bitmap> pics;
    
    public DetailImageAdapter(Context c){
        context = c;
    }
    
    @Override
    public int getCount(){
    	int count = 0;
    	if(pics!=null){
    		count = pics.size();
    	}
    	return count;
    }
    
    public void addPic(Bitmap bitmap){
    	if(pics==null){
    		pics = new ArrayList<Bitmap>();
    	}
    	pics.add(bitmap);
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
    	View template = inflater.inflate(R.layout.detail_pics, null);
    	
    	if(pics!=null){
        	Bitmap t_bitmap = pics.get(position);
        	if(t_bitmap!=null){
	        	ImageView imageView = (ImageView)template.findViewById(R.id.detailimage);
	        	imageView.setImageBitmap(t_bitmap);
        	}
    	}
        return template;
    }
}
