package com.yizhao.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yizhao.activity.R;
import com.yizhao.bean.SearchProductBean;
import com.yizhao.core.DBHelper;
import com.yizhao.util.DataFormalUtil;
import com.yizhao.util.FileUtils;

public class SCAdapter extends BaseAdapter{
	
    private Context context;

    private ArrayList<SearchProductBean> searchList;
    
    private FileUtils fileUtils;
    
    private HashMap<String,Bitmap> bitmap_Map = new HashMap<String,Bitmap>();
    
    public SCAdapter(Context c,ArrayList<SearchProductBean> _searchList,FileUtils _fileUtils){
        context = c;
        searchList = _searchList;
        fileUtils = _fileUtils;
    }

    public void putBitmap(String id,Bitmap bitmap){
    	bitmap_Map.put(id, bitmap);
    }
    
    public ArrayList<SearchProductBean> getNoImageList(){
		ArrayList<SearchProductBean> res_list = new ArrayList<SearchProductBean>();
    	if(searchList!=null){
    		int len = searchList.size();
	    	for(int i = 0 ; i < len; i++){
	    		if(searchList.get(i).getHasGetPic()==0){
	    			res_list.add(searchList.get(i));
	    		}
	    	}
    	}
    	return res_list;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
    	
    	//inflater对象可以把xml转换为view
    	LayoutInflater inflater = LayoutInflater.from(context);
    	View template = inflater.inflate(R.layout.mysc_view, null);
    	
    	if(searchList!=null){
    		
	    	TextView productName = (TextView)template.findViewById(R.id.productName);
	    	TextView productMoney = (TextView)template.findViewById(R.id.productMoney);
	    	TextView sellCount = (TextView)template.findViewById(R.id.sellCount);
	    	TextView tkCount = (TextView)template.findViewById(R.id.tkCount);
	    	ImageView mysc_del_bt = (ImageView)template.findViewById(R.id.mysc_del_bt);

	    	
	    	OnClickListener onclickListenner = new OnClickListener(){
				@Override
				public void onClick(View v) {
					
					DBHelper dbHelper = new DBHelper(context);
					//此处执行删除操作
					String pid = searchList.get(position).getId();
					
					boolean res = dbHelper.delData(pid);
					
					
					
					for(int i = 0 ; i < searchList.size(); i++){
						if(searchList.get(i).getId().equals(pid)){
							fileUtils.delFile(searchList.get(i).getCoverImage());
							searchList.remove(i);
							break;
						}
					}

					SCAdapter.this.notifyDataSetChanged();

					String msg = "删除失败";
					
					if(res)
						msg="删除成功";
					
					Toast toast=Toast.makeText(v.getContext(), msg, Toast.LENGTH_SHORT);
	        		toast.show();
				}
	    		
	    	};
	    	
	    	mysc_del_bt.setOnClickListener(onclickListenner);
	    	
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
