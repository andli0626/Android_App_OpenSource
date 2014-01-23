package android.source.tuangou.framework.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.source.tuangou.framework.Config;
import android.source.tuangou.framework.base.BaseActivity;
import android.source.tuangou.framework.util.LogUtil;
import android.source.tuangou.framework.util.StringUtil;
import android.source.tuangou.framework.webridge.ScriptHelper;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class WebActivity extends BaseActivity
{

	final class T8WebViewClient extends WebViewClient{

		final WebActivity this$0;

		public void onPageFinished(WebView webview, String s)
		{
			WebActivity webactivity = WebActivity.this;
			String as[] = new String[0];
			webactivity.callJSFunc("_page_loaded", as);
			WebActivity webactivity1 = WebActivity.this;
			String as1[] = new String[1];
			as1[0] = "true";
			webactivity1.callJSFunc("_page_appeared", as1);
		}

		private T8WebViewClient()
		{
			super();
			this$0 = WebActivity.this;
		}

	}


	private String currentUrl;
	protected Handler mHandler;
	protected String requestJSON;
	private String title;
	protected WebView webView;

	public WebActivity()
	{
	}

	private void bindJavascriptHook()
	{
		ScriptHelper.bindJavascriptObject(this);
	}

	public void callJS(String s)
	{
		WebView webview = webView;
		String s1 = (new StringBuilder()).append("javascript:").append(s).toString();
		webview.loadUrl(s1);
		if (Config.DEBUG.booleanValue())
			LogUtil.d((new StringBuilder()).append("Call js: ").append(s).toString());
	}

	public  void callJSFunc(String s, String as[])
	{
		StringBuilder stringbuilder = new StringBuilder();
		StringBuilder stringbuilder1 = stringbuilder.append("if (typeof ").append(s).append(" == 'function')").append(s).append("(");
		String s1 = StringUtil.join(Arrays.asList(as), ",");
		StringBuilder stringbuilder2 = stringbuilder1.append(s1).append(")");
		String s2 = stringbuilder.toString();
		callJS(s2);
	}

	public String getCurrentUrl()
	{
		return currentUrl;
	}

	public Handler getHandler()
	{
		return mHandler;
	}

	public String getQueryParams()
	{
		return requestJSON;
	}

	public WebView getWebView()
	{
		return webView;
	}

	protected abstract void initLayout();

	public void loadUrl(String s)
	{
		String s2;
		currentUrl = s;
		try {
			StringBuilder stringbuilder = new StringBuilder();
			String s1 = Config.WEB_FILE_PREFIX;
			s2 = stringbuilder.append(s1).append(s).toString();
			webView.clearCache(true);
			String s3 = (new URL(s2)).getQuery();
			JSONObject jsonobject = new JSONObject();
			if (!StringUtil.isEmpty(s3).booleanValue()) {
				String as[] = Pattern.compile("&").split(s3);
				int i = 0;
				do {
					int j = as.length;
					if (i >= j)
						break;
					String s4 = as[i];
					int k = s4.indexOf("=");
					int l = s4.indexOf("#");
					if (l != -1) {
						int i1 = l + 1;
						String s5 = s4.substring(i1);
						JSONObject jsonobject1 = jsonobject.put("__anchor__",
								s5);
					}
					if (k != -1) {
						String s6 = s4.substring(0, k);
						String s7;
						if (l == -1) {
							int j1 = k + 1;
							s7 = s4.substring(j1);
						} else {
							int k1 = k + 1;
							s7 = s4.substring(k1, l);
						}
						if (!StringUtil.isEmpty(s6).booleanValue()
								&& !StringUtil.isEmpty(s7).booleanValue()) {
							String s8 = Uri.decode(s7);
							JSONObject jsonobject2 = jsonobject.put(s6, s8);
						}
					}
					i++;
				} while (true);
			}
			String s9 = jsonobject.toString();
			requestJSON = s9;
			webView.loadUrl(s2);
			if (Config.DEBUG.booleanValue()) {
				LogUtil.d((new StringBuilder()).append("Load url: ").append(s2)
						.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
		
	}

	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		Bundle bundle1 = getIntent().getExtras();
		if (bundle1 != null)
		{
			String s = bundle1.getString("url");
			currentUrl = s;
			String s1 = bundle1.getString("ui_title");
			title = s1;
		}
		WebView webview = new WebView(this);
		webView = webview;
		initLayout();
		WebView webview1 = webView;
		T8WebViewClient t8webviewclient = new T8WebViewClient();
		webview1.setWebViewClient(t8webviewclient);
		bindJavascriptHook();
		Handler handler = new Handler();
		mHandler = handler;
		if (!StringUtil.isEmpty(currentUrl).booleanValue())
		{
			String s2 = currentUrl;
			loadUrl(s2);
		}
		if (!StringUtil.isEmpty(title).booleanValue())
		{
			String s3 = title;
			setTitle(s3);
		}
	}

	protected void onResume(){
		super.onResume();
		String as[] = new String[1];
		as[0] = "false";
		callJSFunc("_page_appeared", as);
	}

	public void setCurrentUrl(String s)
	{
		currentUrl = s;
	}

	public void setWebActivityProperty(String s, Object obj)
	{
	}
}
