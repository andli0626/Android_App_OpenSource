package com.yizhao.action;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.yizhao.bean.APKBean;
import com.yizhao.bean.IRBean;
import com.yizhao.core.Const;
import com.yizhao.core.HttpManager;

public class AppAction {
	
	/**
	 * 查询版本信息
	 * @return
	 */
	public static APKBean checkVersion(){
		
		APKBean bean = null;
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		String s = new HttpManager(Const.APKURL).submitRequest(params);
		
		//String s = "{"result":"true","url":"http://www.yeezhao.com/mobilefile/update/v1.apk","version":"1.0"}";
		
		Log.d(Const.TAG, "AppAction.checkVersion|jsonStr="+s);
		
		if(s!=null && !"".equals(s)){
			Gson gson = new Gson();
			try{
				bean = gson.fromJson(s,new TypeToken<APKBean>(){}.getType());
			}catch(JsonParseException e){
				Log.e(Const.TAG, "AppAction.checkVersion|JsonParseException",e);
			}
		}
		return bean;
	}
	
	/**
	 * 查询IR更新信息
	 * @return
	 */
	public static IRBean checkIR(){
		
		IRBean bean = null;
		
		//从网络获取是否有更新
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		String s = new HttpManager(Const.IR_CHECK).submitRequest(params);
		
		//String s = "{\"result\":\"true\",\"curFile\":\"ir8.txt\",\"size\":95250}";
		
		Log.d(Const.TAG, "AppAction.checkIR|jsonStr="+s);
		
		if(s!=null && !"".equals(s)){
			Gson gson = new Gson();
			try{
				bean = gson.fromJson(s,new TypeToken<IRBean>(){}.getType());
			}catch(JsonParseException e){
				Log.e(Const.TAG, "AppAction.checkIR|JsonParseException",e);
			}
		}
		
		return bean;
		
	}
}
