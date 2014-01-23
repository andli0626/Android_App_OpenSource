package android.source.tuangou.framework.web.bridges;

import android.os.Handler;
import android.os.Message;
import android.source.tuangou.framework.ServiceManager;
import android.source.tuangou.framework.net.NetworkService;
import android.source.tuangou.framework.util.StringUtil;
import android.source.tuangou.framework.web.ScriptBridge;
import android.source.tuangou.framework.web.WebActivity;


public class NetBridge extends ScriptBridge
{

	String err404;
	String err500;
	private NetworkService net;

	public NetBridge(){
		err500 = "'服务器发生错误!'";
		err404 = "'没有找到页面。。。'";
		NetworkService networkservice = ServiceManager.getNetworkService();
		net = networkservice;
	}
	
	//获取网络数据
	public void get(final String id, String url, final String dataType){
		System.out.println("javascript call java: NetBridge get url = "+url+" id = "+id+" dataType = "+dataType);
		
		final WebActivity ctx = getContext();
		HandlerGet mHandlerGet = new HandlerGet(dataType,id,ctx);
		//获取网络数据
		net.get(url, mHandlerGet);
	}

	public String getSync(String s)
	{
		return net.getSync(s);
	}

	public void post(final String id, String s, String s1, final String dataType)
	{
		final WebActivity ctx = getContext();
		Handler2 mHandler2 = new Handler2(dataType,id,ctx);
		java.util.Map map = StringUtil.parseHttpParamsToHash(s1);
		NetworkService.sharedInstance().post(s, map, mHandler2);
	}

	public String postSync(String s, String s1)
	{
		java.util.Map map = StringUtil.parseHttpParamsToHash(s1);
		return net.postSync(s, map);
	}

	
	/*
	 * http通信get请求的handler消息处理
	 * */
	private class HandlerGet extends Handler{

		final NetBridge this$0;
		final WebActivity ctx;
		final String dataType;
		final String id;

		public void handleMessage(Message message){
			String s1;
			int i;
			String s2;
			WebActivity webactivity;
			String as[];
			StringBuilder stringbuilder1;
			String s3;
			String s4;
			String s5;
			
			//根据消息来判断是否获取数据成功
			if (message.getData().containsKey("error")){//获取数据错误
				StringBuilder stringbuilder = (new StringBuilder()).append("'");
				String s = message.getData().getString("error");
				s1 = stringbuilder.append(s).append("'").toString();
				
			} else//获取数据成功
			{
				s1 = message.getData().getString("responseText").replaceAll("\n", "").replaceAll("'", "\\'");
				System.out.println("HandlerGet responseText = "+s1);
			}
			
			i = message.getData().getInt("status");
			s2 = dataType;
			if (!"json".equalsIgnoreCase(s2)){
				s1 = (new StringBuilder()).append("'").append(s1).append("'").toString();
			}
			
			if (i >= 500 && i < 600){
				s1 = err500;
			}
			
			if (i >= 400 && i < 500){
				s1 = err404;
			}
			
			as = new String[3];
			stringbuilder1 = (new StringBuilder()).append("'");
			s3 = id;
			s4 = stringbuilder1.append(s3).append("'").toString();
			as[0] = s4;
			s5 = (new StringBuilder()).append("").append(i).toString();
			as[1] = s5;
			as[2] = s1;
			
			//调用javasscrpit中ajax_finish
			ctx.callJSFunc("_on_ajax_finished", as);
		}

		HandlerGet(String dataType,String id,WebActivity webActivity){
			super();
			this$0 = NetBridge.this;
			this.dataType = dataType;
			ctx = webActivity;
			this.id = id;
		}
	}


	private class Handler2 extends Handler
	{

		final NetBridge this$0;
		final WebActivity ctx;
		final String dataType;
		final String id;

		public void handleMessage(Message message){
			String s1;
			int i;
			String s2;
			WebActivity webactivity;
			String as[];
			StringBuilder stringbuilder1;
			String s3;
			String s4;
			String s5;
			if (message.getData().containsKey("error"))
			{
				StringBuilder stringbuilder = (new StringBuilder()).append("'");
				String s = message.getData().getString("error");
				s1 = stringbuilder.append(s).append("'").toString();
			} else
			{
				s1 = message.getData().getString("responseText").replaceAll("\n", "").replaceAll("'", "\\'");
			}
			i = message.getData().getInt("status");
			s2 = dataType;
			if (!"json".equalsIgnoreCase(s2))
				s1 = (new StringBuilder()).append("'").append(s1).append("'").toString();
			if (i >= 500 && i < 600)
				s1 = err500;
			if (i >= 400 && i < 500)
				s1 = err404;
			webactivity = ctx;
			as = new String[3];
			stringbuilder1 = (new StringBuilder()).append("'");
			s3 = id;
			s4 = stringbuilder1.append(s3).append("'").toString();
			as[0] = s4;
			s5 = (new StringBuilder()).append("").append(i).toString();
			as[1] = s5;
			as[2] = s1;
			webactivity.callJSFunc("_on_ajax_finished", as);
		}

		Handler2(String dataType,String id,WebActivity webActivity){
			super();
			this$0 = NetBridge.this;
			this.dataType = dataType;
			ctx = webActivity;
			this.id = id;
		}
	}

}
