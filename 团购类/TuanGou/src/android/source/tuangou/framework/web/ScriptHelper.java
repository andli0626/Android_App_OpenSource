package android.source.tuangou.framework.web;

import android.source.tuangou.framework.Config;
import android.source.tuangou.framework.web.bridges.ConnectivityBridge;
import android.source.tuangou.framework.web.bridges.LocationBridge;
import android.source.tuangou.framework.web.bridges.LogBridge;
import android.source.tuangou.framework.web.bridges.NetBridge;
import android.source.tuangou.framework.web.bridges.SessionBridge;
import android.source.tuangou.framework.web.bridges.StoreBridge;
import android.source.tuangou.framework.web.bridges.WebBridge;
import android.webkit.WebView;

import java.util.*;

/*
 * ScripHelper帮助类
 * */
public class ScriptHelper{

	Map bridges;

	public ScriptHelper(){
		try {
			bridges = new HashMap();
			
			NetBridge netbridge = new NetBridge();
			Object obj = bridges.put("android_net", netbridge);

			LogBridge logbridge = new LogBridge();
			Object obj1 = bridges.put("android_log", logbridge);

			SessionBridge sessionbridge = new SessionBridge();
			Object obj2 = bridges.put("android_session", sessionbridge);

			StoreBridge storebridge = new StoreBridge();
			Object obj3 = bridges.put("android_store", storebridge);

			LocationBridge locationbridge = new LocationBridge();
			Object obj4 = bridges.put("android_lbs", locationbridge);

			WebBridge webbridge = new WebBridge();
			Object obj5 = bridges.put("android_web", webbridge);

			ConnectivityBridge connectivitybridge = new ConnectivityBridge();
			Object obj6 = bridges.put("android_connect", connectivitybridge);
			
			
			Iterator iterator = Config.JAVASCRIPT_BRIDGES.entrySet().iterator();
			while(iterator.hasNext()){
				java.util.Map.Entry entry = (java.util.Map.Entry) iterator.next();
				
				String s = (String) entry.getKey();
				Class class1 = Class.forName((String) entry.getValue());
				Object obj7 = class1.newInstance();
				Object obj8 = bridges.put(s, obj7);

				System.out.println("s = "+s +" obj7 = "+obj7);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return;
		
	}

	//绑定javascript，建立通过javascript调用java的程序接口
	public void bindJavascriptObject(WebActivity webactivity){
		System.out.println("bindJavascriptObject bridges.size() = "+bridges.size());
		
		if (bridges.size() > 0){
			WebView webview = webactivity.getWebView();
			String s;
			Object obj;
			
			Iterator iterator = bridges.entrySet().iterator();
			while(iterator.hasNext()){
				java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
				s = (String)entry.getKey();
				obj = entry.getValue();
				
				System.out.println("bindJavascriptObject s = "+s+" obj = "+obj);
				
				//ScriptBridge设置context
				if (obj instanceof ScriptBridge){
					((ScriptBridge)obj).setContext(webactivity);
				}
				
				//添加javasscript接口，桥接java和javascript的一个函数，可以通过javascript调用java的程序接口
				/*
				 * 是实例化一个对象，在html的js中调用，第二个参数是实例化对象的别名，
				 * 如果要使用这个obj，则在js中使用的名字就是interfaceName
				 * */
				 webview.addJavascriptInterface(obj, s);
			}
			

		}
	}
}
