package com.yizhao.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.yizhao.bean.AuthBean;
import com.yizhao.util.DataFormalUtil;
import com.yizhao.activity.R;

public class ReceiveAdapter extends BaseAdapter{
	
    private Context context;

    private ArrayList<AuthBean> receiveList;
    
    private int count;
    
    public ReceiveAdapter(Context c){
        context = c;
    }
    
	@Override
    public int getCount() {
		if(receiveList!=null){
			count = receiveList.size();
		}else{
			count = 0;
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


	public void setReceiveList(ArrayList<AuthBean> receiveList) {
		this.receiveList = receiveList;
	}
	
	public ArrayList<AuthBean> getReceiveList() {
		return receiveList;
	}
	
    /**
     * 分页查询更新list
     * @param _receiveList
     */
    public void containsList(ArrayList<AuthBean> _receiveList){
    	if(receiveList!=null){
    		for(AuthBean bean : _receiveList){
    			receiveList.add(bean);
    		}
    	}else{
    		receiveList = _receiveList;
    	}
    }
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
    	//inflater对象可以把xml转换为view
    	LayoutInflater inflater = LayoutInflater.from(context);
    	View template = inflater.inflate(R.layout.receive_view, null);
    	
    	if(receiveList!=null){
    		
	    	TextView authName = (TextView)template.findViewById(R.id.authName);
	    	TextView receiveTime = (TextView)template.findViewById(R.id.receiveTime);
	    	TextView from = (TextView)template.findViewById(R.id.from);
	    	TextView content = (TextView)template.findViewById(R.id.content);
	    	
	    	authName.setText(receiveList.get(position).getAuthor());
	    	receiveTime.setText(DataFormalUtil.convertTime(receiveList.get(position).getWriteTime()));
	    	from.setText(receiveList.get(position).getFrom());
	    	content.setText(receiveList.get(position).getContent().replace("<br>", "\n").replace("<br/>", "\n").replace("<br />", "\n"));
	    	
    	}
    	
        return template;
    }
}
