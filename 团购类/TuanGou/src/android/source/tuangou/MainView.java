package android.source.tuangou;

import java.util.HashMap;
import java.util.Map;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.source.tuangou.framework.ServiceManager;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.ViewFlipper;


/*
 * 团购类主界面
 * 
 * ActivityGroup--用于切换activity
 * */
public class MainView extends ActivityGroup{

	/*
	 * 相关变量定义
	 * */
	static final String DAILY_DEALS = "daily_deals";
	static final String MORE = "more";
	static final String MY_DEALS = "my_deals";
	static final String NEAR_DEALS = "near_deals";
	LocalActivityManager activityManager;
	ViewFlipper container;
	ActivityContainer innerContainer;
	Map stacks;

	public MainView(){
	
	}

	//底部类别控件初始化
	private void initRadios(){
		//设置状态变化监听器
		CheckedChangeListener mCheckedChangeListener = new CheckedChangeListener();
		((RadioButton)findViewById(R.id.btn_tab_today_deals)).setOnCheckedChangeListener(mCheckedChangeListener);
		((RadioButton)findViewById(R.id.btn_tab_near_deals)).setOnCheckedChangeListener(mCheckedChangeListener);
		((RadioButton)findViewById(R.id.btn_tab_my_deals)).setOnCheckedChangeListener(mCheckedChangeListener);
		((RadioButton)findViewById(R.id.btn_tab_more_deals)).setOnCheckedChangeListener(mCheckedChangeListener);
	}

	//打开堆栈红的activity
	private void openStack(String name, String file, int resId){
		//根据资源id，获取相应字符串
		String s2 = getResources().getString(resId);

		//获取activity堆栈中的activity
		ActivityStack activitystack = (ActivityStack)stacks.get(name);
		//打开堆栈中对应的activity以及初始化
		innerContainer.openStackOrInit(activitystack, file, s2);
	}

	/*
	 * activity的创建方法
	 * */
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		boolean flag = requestWindowFeature(Window.FEATURE_OPTIONS_PANEL);
		//设置布局文件
		setContentView(R.layout.main);
		
		//创建activty堆栈
		stacks = new HashMap();
		
		//保存activity堆栈
		ActivityStack activitystack = new ActivityStack();
		Object obj = stacks.put("daily_deals", activitystack);
		
		ActivityStack activitystack1 = new ActivityStack();
		Object obj1 = stacks.put("near_deals", activitystack1);
		
		ActivityStack activitystack2 = new ActivityStack();
		Object obj2 = stacks.put("my_deals", activitystack2);
		
		ActivityStack activitystack3 = new ActivityStack();
		Object obj3 = stacks.put("more", activitystack3);
		
		//获取布局文件中显示内容的控件
		container = (ViewFlipper)findViewById(R.id.container);
		//获取activity管理类
		activityManager = getLocalActivityManager();
		
		//创建内容activity
		Intent intent = new Intent(this, ActivityContainer.class);
		Window window = activityManager.startActivity("main_container", intent);
		innerContainer = (ActivityContainer)window.getContext();

		//获取内容actiivty
		View view = window.getDecorView();
		//将内容activity添加到视图中
		container.addView(view);
		container.showNext();
		
		//打开activity堆栈中的activity
		openStack("daily_deals", "category.html", R.string.daily_deals);
		//初始化底部分类
		initRadios();
	}

	//重启activity函数
	protected void onRestart(){
		ServiceManager.getLocationService().startLocationListener();
		super.onRestart();
	}

	//activity中断函数
	public void onStop(){
		ServiceManager.getLocationService().removeLocationListener();
		super.onStop();
	}


	//控件状态监听器
	private class CheckedChangeListener
		implements android.widget.CompoundButton.OnCheckedChangeListener{

		final MainView this$0;

		//控件状态改变监听函数
		public void onCheckedChanged(CompoundButton compoundbutton, boolean flag){
			if (!flag) {
				return;
			} else{
				switch(compoundbutton.getId()){
				
				//今日团购
				case R.id.btn_tab_today_deals:
					openStack("daily_deals", "category.html", R.string.daily_deals);
					break;
					
				//附件团购	
				case R.id.btn_tab_near_deals:
					openStack("near_deals", "nearby.html", R.string.nearby_deals);
					break;
				
				//我的团购
				case R.id.btn_tab_my_deals:
					openStack("my_deals", "mydeal.html", R.string.my_deals);
					break;
				
				//更多
				case R.id.btn_tab_more_deals:
					openStack("more", "options.html", R.string.more_options);
					break;
					
				}
			}
		}

		CheckedChangeListener(){
			super();
			this$0 = MainView.this;
		}
	}

}
