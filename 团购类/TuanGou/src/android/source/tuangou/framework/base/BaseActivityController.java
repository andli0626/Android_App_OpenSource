package android.source.tuangou.framework.base;

import android.app.Activity;
import android.view.KeyEvent;

/*
 * Activity基类的控制类
 * */
public abstract class BaseActivityController
	implements IActivityKeyListener{

	Activity context;

	public BaseActivityController(){
		
	}

	public BaseActivityController(Activity activity){
		context = activity;
	}

	public Activity getContext(){
		return context;
	}

	//activity按键的监听接口函数
	public boolean onKeyDown(int i, KeyEvent keyevent){
		return true;
	}

	public void setContext(Activity activity){
		context = activity;
	}
}
