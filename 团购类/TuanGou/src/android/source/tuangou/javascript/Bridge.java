package android.source.tuangou.javascript;

import android.os.Bundle;
import android.source.tuangou.ActivityContainer;
import android.source.tuangou.HeaderWebActivity;
import android.source.tuangou.beans.CityList;
import android.source.tuangou.framework.lbs.City;
import android.source.tuangou.framework.store.beans.Preferences;
import android.source.tuangou.framework.util.LogUtil;
import android.source.tuangou.framework.util.StringUtil;
import android.source.tuangou.framework.web.ScriptBridge;
import android.source.tuangou.services.Umeng;

import java.util.Iterator;
import org.json.JSONObject;

public class Bridge extends ScriptBridge{

	public Bridge(){
		
	}

	//获得city
	public String getCity(){
		//获得city的id
		String s = Preferences.getInstance().get("current_city_id");
		String s1;
		System.out.println("javascript call java: cityId = "+s);
		
		if (StringUtil.isEmpty(s).booleanValue()){
			s1 = "{name: '北京', id: 1, pinyin: 'beijing'}";
		} else{
			//根据city的id来获取相应的city
			City city = CityList.getInstance().getCityById(s);
			if (city == null){
				s1 = "{name: '北京', id: 1, pinyin: 'beijing'}";
			} else{
				StringBuilder stringbuilder = (new StringBuilder()).append("{name: '");
				String s2 = city.name;
				StringBuilder stringbuilder1 = stringbuilder.append(s2).append("', id: ");
				String s3 = city.id;
				StringBuilder stringbuilder2 = stringbuilder1.append(s3).append(", pinyin: '");
				String s4 = city.pinyin;
				s1 = stringbuilder2.append(s4).append("'}").toString();
			}
		}
		System.out.println("javascript call java: city = "+s1);
		return s1;
	}

	public void getCityList(){
		final String ret = CityList.getInstance().getAllJson().replace("\\n", "").trim();
		final HeaderWebActivity ctx = getHeaderWebActivity();
		
		System.out.println("getCityList ret = "+ret);
		Runnable3 mRunnable3 = new Runnable3(ctx, ret);
		runInHandlerThread(mRunnable3);
	}

	//获取顶部webActivity
	public HeaderWebActivity getHeaderWebActivity(){
		HeaderWebActivity headerwebactivity;
		if (context == null){
			LogUtil.e(new Exception("No header web activity found in Bridge"));
			headerwebactivity = null;
		} else{
			headerwebactivity = (HeaderWebActivity)context;
		}
		return headerwebactivity;
	}

	public String getPreference(String s)
	{
		return Preferences.getInstance().get(s);
	}

	public void openWebActivity(String s){
		System.out.println("javaScrpit call java: openWebActivity "+s);
		
		final HeaderWebActivity ctx = getHeaderWebActivity();
		final Bundle data = new Bundle();

		JSONObject jsonobject;
		Iterator iterator;
		try {
			
			//转换成JSONObject对象
			jsonobject = StringUtil.parseJSON(s);
			
			iterator = jsonobject.keys();
			while (iterator.hasNext()) {
				
				String s1 = iterator.next().toString();
				String s2 = jsonobject.getString(s1);
				data.putString(s1, s2);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
		
		openWebActivityRunnable mopenWebActivityRunnable = new openWebActivityRunnable(ctx,data);
		//运行线程
		runInHandlerThread(mopenWebActivityRunnable);
		
		return;
	}

	public void savePreference(String key, String value){
		System.out.println("javascript call java savePreference key = "+key +" value = "+value);
		
		Preferences.getInstance().save(key, value);
	}

	public void uiBack(final String args)
	{
		final HeaderWebActivity ctx = getHeaderWebActivity();
		Runnable2 mRunnable2 = new Runnable2(ctx,args);
		runInHandlerThread(mRunnable2);
	}

	public void umengEvent(String s)
	{
		Umeng.umengEvent(getHeaderWebActivity(), s);
	}

	private class Runnable3
		implements Runnable
	{

		final Bridge this$0;
		final HeaderWebActivity ctx;
		final String ret;

		public void run()
		{
			HeaderWebActivity headerwebactivity = ctx;
			String as[] = new String[1];
			String s = ret;
			as[0] = s;
			headerwebactivity.callJSFunc("_on_citylist_finished", as);
		}

		Runnable3(HeaderWebActivity headerwebactivity, String s){
			super();
			this$0 = Bridge.this;
			ctx = headerwebactivity;
			ret = s;
		}
	}


	/*
	 * 打开webActivity的runnable线程
	 * */
	private class openWebActivityRunnable
		implements Runnable{

		final Bridge this$0;
		final HeaderWebActivity ctx;
		final Bundle data;

		public void run(){
			
			ActivityContainer activitycontainer = ctx.getStack();
			//通过ActivityContainer打开webActivity
			activitycontainer.openWebActivity(data);
		}

		openWebActivityRunnable(HeaderWebActivity headerwebactivity,Bundle bundle){
			super();
			this$0 = Bridge.this;
			ctx = headerwebactivity;
			data = bundle;
		}
	}


	private class Runnable2
		implements Runnable
	{

		final Bridge this$0;
		final String args;
		final HeaderWebActivity ctx;

		public void run()
		{
			ActivityContainer activitycontainer = ctx.getStack();
			String s = args;
			activitycontainer.back(s);
		}

		Runnable2(HeaderWebActivity headerwebactivity ,String s){
			super();
			this$0 = Bridge.this;
			ctx = headerwebactivity;
			args = s;
		}
	}

}
