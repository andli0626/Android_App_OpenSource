package android.source.tuangou.framework.webridge;

import android.source.tuangou.framework.ui.WebActivity;

public class TestScriptHook extends ScriptHook
{

	public TestScriptHook(WebActivity webactivity)
	{
		super(webactivity);
	}

	public String saySomething(String s, String s1)
	{
		return (new StringBuilder()).append(s).append(": do it baby ").append(s1).toString();
	}
}
