package android.source.tuangou.framework.webridge;

import android.source.tuangou.framework.ui.WebActivity;
import android.source.tuangou.framework.util.LogUtil;

public class LogHook extends ScriptHook
{

	public LogHook(WebActivity webactivity)
	{
		super(webactivity);
	}

	public void d(String s)
	{
		LogUtil.d(s);
	}

	public void e(String s)
	{
		LogUtil.e(null, s);
	}

	public String getJsObjectName()
	{
		return "android_log";
	}

}
