package android.source.tuangou.services;

import android.app.Activity;
import android.source.tuangou.framework.store.beans.Preferences;

public class Umeng
{

	public Umeng(){
	
	}

	public static void onPause(Activity activity){
		String s = Preferences.getInstance().get("umeng");
	}

	public static void onResume(Activity activity){
		String s = Preferences.getInstance().get("umeng");
	}

	//设置资源方法
	public static void setReportPolicy(){
		//查询数据库中preference表中umeng列
		String s = Preferences.getInstance().get("umeng");
		System.out.println("setReportPolicy s = "+s);
	}

	public static void umengEvent(Activity activity, String s){
		String s1 = Preferences.getInstance().get("umeng");
	}
}
