package android.source.tuangou.framework.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.source.tuangou.framework.Application;
import android.source.tuangou.framework.util.LogUtil;
import android.view.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


/*
 * Activity的基类
 * */
public abstract class BaseActivity extends Activity{

	//activity基类的控制类
	BaseActivityController controller;
	//Menu的接口类
	MenuMediator menuMediator;

	public BaseActivity(){
		super();
	}

	//获取menu接口类对象
	public MenuMediator getMenuMediator(){
		return menuMediator;
	}

	public void onCreate(Bundle bundle){
		
		super.onCreate(bundle);
		
		//获取menu接口类
		menuMediator = Application.getInstance().getGlobalMenuMediator();
		//获取传递过来的数据
		Bundle bundle1 = getIntent().getExtras();
		
		//判断是否携带了controller数据
		if (bundle1 == null || !bundle1.containsKey("controller")) {
			return;
		}else{
			Object obj = bundle1.get("controller");
			if (!(obj instanceof BaseActivityController)){
				try {
					if (!(obj instanceof String)) {
						Class class1 = Class.forName(obj.toString());
						Class aclass[] = new Class[1];
						aclass[0] = Activity.class;
						Constructor constructor = class1.getConstructor(aclass);
						Object aobj[] = new Object[1];
						aobj[0] = this;
						BaseActivityController baseactivitycontroller1 = (BaseActivityController) constructor
								.newInstance(aobj);
						controller = baseactivitycontroller1;
					}
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}
					
				
			
			} else{
				BaseActivityController baseactivitycontroller = (BaseActivityController)bundle1.get("controller");
				controller = baseactivitycontroller;
				controller.setContext(this);
			}
			return;
		}
	}

	//创建菜单函数
	public boolean onCreateOptionsMenu(Menu menu){
		boolean flag = super.onCreateOptionsMenu(menu);
		boolean flag1;
		if (menuMediator == null || menuMediator.createMenu(this, menu))
			flag1 = true;
		else
			flag1 = false;
		return flag1;
	}

	//按键处理函数
	public boolean onKeyDown(int i, KeyEvent keyevent){
		boolean flag;
		if (controller != null && !controller.onKeyDown(i, keyevent))
		{
			LogUtil.d("do not exit.");
			flag = false;
		} else
		{
			flag = super.onKeyDown(i, keyevent);
		}
		return flag;
	}

	//menu选择函数
	public boolean onOptionsItemSelected(MenuItem menuitem){
		boolean flag = super.onOptionsItemSelected(menuitem);
		boolean flag1;
		if (menuMediator == null || menuMediator.optionsItemSelected(this, menuitem))
			flag1 = true;
		else
			flag1 = false;
		return flag1;
	}

	//显示menu前函数，每次显示都会调用
	public boolean onPrepareOptionsMenu(Menu menu){
		boolean flag = super.onPrepareOptionsMenu(menu);
		boolean flag1;
		if (menuMediator == null || menuMediator.prepareOptionsMenu(this, menu))
			flag1 = true;
		else
			flag1 = false;
		return flag1;
	}

	//设置activity类的控制类
	public void setController(BaseActivityController baseactivitycontroller){
		controller = baseactivitycontroller;
	}

	//设置menu接口
	public void setMenuMediator(MenuMediator menumediator){
		menuMediator = menumediator;
	}
}
