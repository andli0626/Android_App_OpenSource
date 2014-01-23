package android.source.tuangou.framework.net;

import android.os.Handler;
import android.os.Message;
import android.source.tuangou.framework.ui.WebActivity;
import android.source.tuangou.framework.util.StringUtil;
import android.source.tuangou.framework.webridge.ScriptHook;


public class NetScriptHook extends ScriptHook
{

	private NetworkService net;

	public NetScriptHook(WebActivity webactivity)
	{
		super(webactivity);
		NetworkService networkservice = NetworkService.sharedInstance();
		net = networkservice;
	}

	public void get(final String id, String s, final String dataType)
	{
		final WebActivity ctx = getContext();
		Handler1 mHandler1 = new Handler1(dataType,id,ctx);
		net.get(s, mHandler1);
	}

	public String getJsObjectName()
	{
		return "android_net";
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

	private class Handler1 extends Handler{

		final NetScriptHook this$0;
		final WebActivity ctx;
		final String dataType;
		final String id;

		public void handleMessage(Message message)
		{
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
			webactivity = ctx;
			as = new String[3];
			stringbuilder1 = (new StringBuilder()).append("'");
			s3 = id;
			s4 = stringbuilder1.append(s3).append("'").toString();
			as[0] = s4;
			s5 = (new StringBuilder()).append("'").append(i).append("'").toString();
			as[1] = s5;
			as[2] = s1;
			webactivity.callJSFunc("_on_ajax_finished", as);
		}

		Handler1(String dataType, String id,WebActivity webActivity ){
			super();
			this$0 = NetScriptHook.this;
			this.dataType = dataType;
			ctx = webActivity;
			this.id = id;
		}
	}


	private class Handler2 extends Handler
	{

		final NetScriptHook this$0;
		final WebActivity ctx;
		final String dataType;
		final String id;

		public void handleMessage(Message message)
		{
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
			webactivity = ctx;
			as = new String[3];
			stringbuilder1 = (new StringBuilder()).append("'");
			s3 = id;
			s4 = stringbuilder1.append(s3).append("'").toString();
			as[0] = s4;
			s5 = (new StringBuilder()).append("'").append(i).append("'").toString();
			as[1] = s5;
			as[2] = s1;
			webactivity.callJSFunc("_on_ajax_finished", as);
		}

		Handler2(String dataType, String id, WebActivity webActivity){
			super();
			this$0 = NetScriptHook.this;
			this.dataType = dataType;
			ctx = webActivity;
			this.id = id;
		}
	}

}
