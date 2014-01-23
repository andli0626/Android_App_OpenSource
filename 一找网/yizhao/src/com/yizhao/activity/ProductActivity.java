package com.yizhao.activity;

import com.yizhao.core.Const;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class ProductActivity extends Activity{
	
	private LinearLayout product_1;
	private LinearLayout product_2;
	private LinearLayout product_3;
	private LinearLayout product_4;
	private LinearLayout product_5;
	
	private Context _context;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.product);
        super.onCreate(savedInstanceState);
        _context = this;
        product_1 = (LinearLayout)findViewById(R.id.product_1);
        product_2 = (LinearLayout)findViewById(R.id.product_2);
        product_3 = (LinearLayout)findViewById(R.id.product_3);
        product_4 = (LinearLayout)findViewById(R.id.product_4);
        product_5 = (LinearLayout)findViewById(R.id.product_5);
        
        OnClickListener listener = new OnClickListener(){
			@Override
			public void onClick(View v) {
				
				String keyworld = "";//此处要对输入进行校验
				
				if(v.getId()==R.id.product_1){
					keyworld = Const.PRODUCT_NAME[0];
				}else if(v.getId()==R.id.product_2){
					keyworld = Const.PRODUCT_NAME[1];
				}else if(v.getId()==R.id.product_3){
					keyworld = Const.PRODUCT_NAME[2];
				}else if(v.getId()==R.id.product_4){
					keyworld = Const.PRODUCT_NAME[3];
				}else if(v.getId()==R.id.product_5){
					keyworld = Const.PRODUCT_NAME[4];
				}
				
				Intent it = new Intent(_context, SearchActivity.class);
				it.putExtra("sname",keyworld);
				it.putExtra("from","product");
            	startActivity(it);
			}
        };
        
        product_1.setOnClickListener(listener);
        product_2.setOnClickListener(listener);
        product_3.setOnClickListener(listener);
        product_4.setOnClickListener(listener);
        product_5.setOnClickListener(listener);
        
        this.getIntent().putExtra("status", true);
    }
}