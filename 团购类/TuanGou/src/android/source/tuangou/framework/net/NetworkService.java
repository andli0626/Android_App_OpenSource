package android.source.tuangou.framework.net;

import android.os.*;
import android.source.tuangou.framework.Config;
import android.source.tuangou.framework.store.beans.Preferences;
import android.source.tuangou.framework.update.VersionManager;
import android.source.tuangou.framework.util.LogUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.*;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;


/*
 * 网络服务类
 * */
public class NetworkService{

	private static NetworkService instance;
	private final int CONNECTION_TIMEOUT = 60000;//连接超时
	private final int SOCKET_TIMEOUT = 0x1d4c0;//
	private DefaultHttpClient httpClient;

	//网络服务构造函数
	public NetworkService(){
		//http通信参数类
		BasicHttpParams basichttpparams = new BasicHttpParams();
		//设置连接超时时间
		HttpConnectionParams.setConnectionTimeout(basichttpparams, 60000);
		//设置连接成功后，发送请求后最大等待Server响应的时间
		HttpConnectionParams.setSoTimeout(basichttpparams, 120000);

		/*
		 * PlainSocketFactory--创建和初始化普通的（不加密的）套接字的默认工厂
		 * Scheme类代表了一个协议模式
		 * SchemeRegistry类用来维持一组Scheme，当去通过请求URI建立连接时，HttpClient可以从中选择
		 * 
		 * */
		SchemeRegistry schemeregistry = new SchemeRegistry();
		PlainSocketFactory plainsocketfactory = PlainSocketFactory.getSocketFactory();
		Scheme scheme = new Scheme("http", plainsocketfactory, 80);
		Scheme scheme1 = schemeregistry.register(scheme);
		
		ThreadSafeClientConnManager threadsafeclientconnmanager = new 
			ThreadSafeClientConnManager(basichttpparams, schemeregistry);
		
		DefaultHttpClient defaulthttpclient = new DefaultHttpClient
			(threadsafeclientconnmanager, basichttpparams);
		
		httpClient = defaulthttpclient;
	}

	//获取get请求参数
	private String addGetParams(String s){
		StringBuilder stringbuilder = new StringBuilder();
		String s1 = Preferences.requestKeyName;
		StringBuilder stringbuilder1 = stringbuilder.append(s1).append("=");
		String s2 = Preferences.getInstance().getRequestKey();
		StringBuilder stringbuilder2 = stringbuilder1.append(s2).append("&ver=");
		String s3 = Config.CLIENT_TAG;
		StringBuilder stringbuilder3 = stringbuilder2.append(s3).append(":");
		String s4 = VersionManager.getFullVersion();
		String s5 = stringbuilder3.append(s4).toString();
		String s6;
		if (s.indexOf("?") < 0)
			s6 = (new StringBuilder()).append(s).append("?").append(s5).toString();
		else
			s6 = (new StringBuilder()).append(s).append("&").append(s5).toString();
		return s6;
	}

	private void sendFailureMsg(int i, String s, Handler handler)
	{
		Message message = new Message();
		Bundle bundle = new Bundle();
		bundle.putInt("status", 600);
		bundle.putString("error", s);
		message.setData(bundle);
		boolean flag = handler.sendMessage(message);
	}

	public static NetworkService sharedInstance()
	{
		try {
			if (instance == null)
				instance = new NetworkService();
			return instance;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	//获取网络数据
	public void get(String url, final Handler handler){
		//获取get请求URL
		final String adUrl = addGetParams(url);
		LogUtil.d(adUrl);
		
		final ResponseHandlerGet rh = new ResponseHandlerGet(handler);
		//创建线程获取数据
		(new ThreadGet(adUrl,handler,rh)).start();
	}

	//http请求
	public HttpResponse getResponse(String s){
		HttpResponse httpresponse;
		try {
			HttpGet httpget;
			String s1 = addGetParams(s);
			LogUtil.d(s1);
			httpget = new HttpGet(s1);
			httpresponse = null;
			HttpResponse httpresponse1 = httpClient.execute(httpget);
			httpresponse = httpresponse1;
			return httpresponse;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return null;
	}

	//通过发送http请求，来获取当前具体地址
	public String getSync(String s){
		String s1;
		try {
			HttpResponse httpresponse;
			s1 = "";
			httpresponse = getResponse(s);
			if (httpresponse == null
					|| httpresponse.getStatusLine().getStatusCode() != 200){
				
			}else{
				String s2 = EntityUtils.toString(httpresponse.getEntity());
				return s2;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}

	public void post(String s, Map map, final Handler handler)
	{
		final String abUrl = addGetParams(s);
		final ResponseHandler2 rh = new ResponseHandler2(handler);
		NetworkService networkservice = this;
		final Map params = map;
		final Handler mhandler = handler;
		(new Thread2(params,abUrl,handler,rh)).start();
	}

	//发送http请求
	public String postSync(String Url, Map map){
		HttpPost httppost;
		String s2;
		ArrayList arraylist;
		String s1 = addGetParams(Url);
		httppost = new HttpPost(s1);
		
		
		s2 = "";
		arraylist = new ArrayList();
		String s3 = Preferences.requestKeyName;
		String s4 = Preferences.getInstance().getRequestKey();
		BasicNameValuePair basicnamevaluepair = new BasicNameValuePair(s3, s4);
		boolean flag = arraylist.add(basicnamevaluepair);
		
		//组合数据
		if (map != null){
			Iterator iterator = map.keySet().iterator();
			while(iterator.hasNext()){
				String s5 = (String)iterator.next();
				String s6 = (String)map.get(s5);
				BasicNameValuePair basicnamevaluepair1 = new BasicNameValuePair(s5, s6);
				boolean flag1 = arraylist.add(basicnamevaluepair1);
			}
		}
		
		try {
			String s7;
			UrlEncodedFormEntity urlencodedformentity = new UrlEncodedFormEntity(
					arraylist, "UTF-8");
			httppost.setEntity(urlencodedformentity);
			HttpResponse httpresponse = httpClient.execute(httppost);
			if (httpresponse == null){
				return null;
			}
			//获取http相应的数据
			s7 = EntityUtils.toString(httpresponse.getEntity());
			
			return s7;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "";
	}

	
	//发送http请求
	public Map postSync(String Url, Map map, JsonParser jsonparser){
		//具体发送http请求的函数
		String s1 = postSync(Url, map);
		
		return jsonparser.parse(s1);
	}


	//get函数的相应类
	private class ResponseHandlerGet
		implements ResponseHandler{

		final NetworkService this$0;
		final Handler handler;
		

		public String handleResponse(HttpResponse httpresponse)
			throws ClientProtocolException, IOException{
		
			System.out.println("handleResponse");
			
			org.apache.http.HttpEntity httpentity;
			int i;
			httpentity = httpresponse.getEntity();
			i = httpresponse.getStatusLine().getStatusCode();
			String s;
			s = EntityUtils.toString(httpentity);
			Bundle bundle = new Bundle();
			bundle.putString("responseText", s);
			bundle.putInt("status", i);
			Message message = new Message();
			message.setData(bundle);
			boolean flag = handler.sendMessage(message);
			
			return s;
		}

		ResponseHandlerGet(Handler mHandler){
			this$0 = NetworkService.this;
			handler = mHandler;
		}
	}


	//获取网络数据的线程
	private class ThreadGet extends Thread{

		final NetworkService this$0;
		final String adUrl;
		final Handler handler;
		final ResponseHandler rh;

		public void run(){
			try {
				System.out.println("thread run:");
				HttpGet httpget = new HttpGet(adUrl);
				Object obj = httpClient.execute(httpget, rh);
				
			} catch (IOException ie) {
				// TODO: handle exception
				NetworkService networkservice = NetworkService.this;
				String s1 = ie.getMessage();
				Handler handler1 = handler;
				networkservice.sendFailureMsg(600, s1, handler1);
				return;
			}catch(Exception e){
				LogUtil.e(e);
				NetworkService networkservice1 = NetworkService.this;
				String s2 = e.getMessage();
				Handler handler2 = handler;
				networkservice1.sendFailureMsg(601, s2, handler2);
			}
		}

		//构造函数
		ThreadGet(String url, Handler mHandler, ResponseHandler mResponseHandler){
			this$0 = NetworkService.this;
			adUrl = url;
			rh = mResponseHandler;
			handler = mHandler;
		}
	}


	private class ResponseHandler2
		implements ResponseHandler
	{

		final NetworkService this$0;
		final Handler handler;


		public Object handleResponse(HttpResponse httpresponse)
			throws ClientProtocolException, IOException{
			try {
				org.apache.http.HttpEntity httpentity;
				int i;
				httpentity = httpresponse.getEntity();
				i = httpresponse.getStatusLine().getStatusCode();
				String s;
				s = EntityUtils.toString(httpentity);
				Bundle bundle = new Bundle();
				bundle.putString("responseText", s);
				bundle.putInt("status", i);
				Message message = new Message();
				message.setData(bundle);
				boolean flag = handler.sendMessage(message);
				return s;
			} catch (Exception e) {
				// TODO: handle exception
			}
			return "";
		}

		ResponseHandler2(Handler mHandler)
		{
			this$0 = NetworkService.this;
			this.handler = mHandler;
		}
	}


	private class Thread2 extends Thread
	{

		final NetworkService this$0;
		final String abUrl;
		final Handler handler;
		final Map params;
		final ResponseHandler rh;

		public void run()
		{
			ArrayList arraylist;
			NetworkService networkservice;
			String s4;
			Handler handler1;
			arraylist = new ArrayList();
			String s = Preferences.requestKeyName;
			String s1 = Preferences.getInstance().getRequestKey();
			BasicNameValuePair basicnamevaluepair = new BasicNameValuePair(s, s1);
			boolean flag = arraylist.add(basicnamevaluepair);
			if (params != null){
				for (Iterator iterator = params.keySet().iterator(); iterator.hasNext();){
					String s2 = (String)iterator.next();
					String s3 = (String)params.get(s2);
					BasicNameValuePair basicnamevaluepair1 = new BasicNameValuePair(s2, s3);
					boolean flag1 = arraylist.add(basicnamevaluepair1);
				}

			}
			try
			{
				String s5 = abUrl;
				HttpPost httppost = new HttpPost(s5);
				UrlEncodedFormEntity urlencodedformentity = new UrlEncodedFormEntity(arraylist, "UTF-8");
				httppost.setEntity(urlencodedformentity);
				DefaultHttpClient defaulthttpclient = httpClient;
				ResponseHandler responsehandler = rh;
				Object obj = defaulthttpclient.execute(httppost, responsehandler);
			}
			// Misplaced declaration of an exception variable
			catch (IOException iexception)
			{
				LogUtil.e(iexception);
				networkservice = NetworkService.this;
				s4 = iexception.getMessage();
				handler1 = handler;
				networkservice.sendFailureMsg(600, s4, handler1);
			}
			catch (Exception exception)
			{
				LogUtil.e(exception);
				NetworkService networkservice1 = NetworkService.this;
				String s6 = exception.getMessage();
				Handler handler2 = handler;
				networkservice1.sendFailureMsg(601, s6, handler2);
			}
			return;
		}

		Thread2(Map mapParams,String abUrl,Handler handler,
				ResponseHandler mResponseHandler)
		{
			this$0 = NetworkService.this;
			params = mapParams;
			this.abUrl = abUrl;
			rh = mResponseHandler;
			this.handler = handler;
		}
	}

}
