package android.source.tuangou.framework.webridge;

import android.source.tuangou.framework.auth.SessionScriptHook;
import android.source.tuangou.framework.lbs.LbsScriptHook;
import android.source.tuangou.framework.net.NetScriptHook;
import android.source.tuangou.framework.store.StoreScriptHook;
import android.source.tuangou.framework.ui.WebActivity;
import android.webkit.WebView;

public class ScriptHelper
{

	public ScriptHelper()
	{
	}

	public static void bindJavascriptObject(WebActivity webactivity)
	{
		WebView webview = webactivity.getWebView();
		NetScriptHook netscripthook = new NetScriptHook(webactivity);
		StoreScriptHook storescripthook = new StoreScriptHook(webactivity);
		LogHook loghook = new LogHook(webactivity);
		WebHook webhook = new WebHook(webactivity);
		LbsScriptHook lbsscripthook = new LbsScriptHook(webactivity);
		SessionScriptHook sessionscripthook = new SessionScriptHook(webactivity);
		String s = netscripthook.getJsObjectName();
		webview.addJavascriptInterface(netscripthook, s);
		String s1 = storescripthook.getJsObjectName();
		webview.addJavascriptInterface(storescripthook, s1);
		String s2 = loghook.getJsObjectName();
		webview.addJavascriptInterface(loghook, s2);
		String s3 = webhook.getJsObjectName();
		webview.addJavascriptInterface(webhook, s3);
		String s4 = lbsscripthook.getJsObjectName();
		webview.addJavascriptInterface(lbsscripthook, s4);
		String s5 = sessionscripthook.getJsObjectName();
		webview.addJavascriptInterface(sessionscripthook, s5);
		JsConfig jsconfig = new JsConfig();
		webview.addJavascriptInterface(jsconfig, "android_config");
	}
}
