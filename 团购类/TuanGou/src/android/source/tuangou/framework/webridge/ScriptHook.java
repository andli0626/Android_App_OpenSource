package android.source.tuangou.framework.webridge;

import android.source.tuangou.framework.ui.WebActivity;

public abstract class ScriptHook
	implements IScriptHook
{

	private WebActivity ctx;

	public ScriptHook(WebActivity webactivity)
	{
		ctx = webactivity;
	}

	public WebActivity getContext()
	{
		return ctx;
	}

	public String getJsObjectName()
	{
		return "tuan800_android";
	}

	public void runInHandlerThread(Runnable runnable)
	{
		boolean flag = getContext().getHandler().post(runnable);
	}
}
