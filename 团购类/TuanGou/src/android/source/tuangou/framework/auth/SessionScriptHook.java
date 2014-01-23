package android.source.tuangou.framework.auth;

import android.source.tuangou.framework.Application;
import android.source.tuangou.framework.ui.WebActivity;
import android.source.tuangou.framework.webridge.ScriptHook;



public class SessionScriptHook extends ScriptHook
{

	public SessionScriptHook(WebActivity webactivity)
	{
		super(webactivity);
	}

	public String getJsObjectName()
	{
		return "android_session";
	}

	public boolean isLogin()
	{
		return Application.getSession().isLogin();
	}
}
