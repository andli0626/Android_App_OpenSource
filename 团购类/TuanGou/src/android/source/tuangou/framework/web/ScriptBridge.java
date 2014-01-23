package android.source.tuangou.framework.web;

public abstract class ScriptBridge
{

	protected WebActivity context;

	public ScriptBridge()
	{
	}

	public ScriptBridge(WebActivity webactivity)
	{
		context = webactivity;
	}

	public WebActivity getContext()
	{
		return context;
	}

	//通过Handler来运行runnable线程
	public void runInHandlerThread(Runnable runnable){
		boolean flag = getContext().getHandler().post(runnable);
	}

	//设置context
	public void setContext(WebActivity webactivity){
		context = webactivity;
	}
}
