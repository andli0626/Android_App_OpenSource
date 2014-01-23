package android.source.tuangou.framework.web;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.source.tuangou.framework.Config;
import android.source.tuangou.framework.base.BaseActivity;
import android.source.tuangou.framework.util.LogUtil;
import android.source.tuangou.framework.util.StringUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URL;
import java.util.Arrays;
import java.util.regex.Pattern;
import org.json.JSONObject;

public abstract class WebActivity extends BaseActivity
{

	private String currentUrl;
	protected Handler mHandler;
	protected String requestJSON;
	private String title;
	protected WebView webView;


	public WebActivity(){
		
	}
	
	//绑定Javascrip函数
	private void bindJavascriptHook(){
		(new ScriptHelper()).bindJavascriptObject(this);
	}

	//调用javascript
	public void callJS(String s){
		String s1 = (new StringBuilder()).append("javascript:").append(s).toString();
		System.out.println("callJS s1 = "+s1);
		
		webView.loadUrl(s1);
		if (Config.DEBUG.booleanValue()){
			StringBuilder stringbuilder = (new StringBuilder()).append("Call js: ");
			String s2;
			if (s.length() > 101)
				s2 = s.substring(0, 100);
			else
				s2 = s;
			LogUtil.d(stringbuilder.append(s2).toString());
		}
	}

	//调用javascript中的函数
	public  void callJSFunc(String s, String as[]){
		System.out.println("callJSFunc s = "+s);
		for(int i = 0; i < as.length; i++){
			System.out.println("callJSFunc as["+i+"] = "+as[i]);
		}
		
		StringBuilder stringbuilder = new StringBuilder();
		StringBuilder stringbuilder1 = stringbuilder.append("if (typeof ").append(s).append(" == 'function')").append(s).append("(");
		String s1 = StringUtil.join(Arrays.asList(as), ",");
		StringBuilder stringbuilder2 = stringbuilder1.append(s1).append(")");
		String s2 = stringbuilder.toString();
		
		System.out.println("callJSFunc s2 = "+s2);
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

	//获取webview
	public WebView getWebView(){
		return webView;
	}

	protected abstract void initLayout();

	//加载url函数
	public void loadUrl(String s){
		try {
			System.out.println("loadUrl = "+s);
			
			String s2;
			currentUrl = s;
			StringBuilder stringbuilder = new StringBuilder();
			String s1 = Config.WEB_FILE_PREFIX;
			s2 = stringbuilder.append(s1).append(s).toString();
			
			String s3 = (new URL(s2)).getQuery();
			JSONObject jsonobject = new JSONObject();
			
			System.out.println("s3 = "+s3);
			if (!StringUtil.isEmpty(s3).booleanValue()) {
				
				String as[] = Pattern.compile("&").split(s3);
				
				for(int i = 0; i < as.length; i++){
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
				}
			}
			requestJSON = jsonobject.toString();
			System.out.println("requestJSON = "+requestJSON);
			System.out.println("Config.DEBUG = "+Config.DEBUG);
			
			if (Config.DEBUG.booleanValue()) {
				LogUtil.d((new StringBuilder()).append("Load url: ").append(s2)
						.toString());
				webView.clearCache(true);
			}
			
			System.out.println("s2 = "+s2);
			//webView加载url
			webView.loadUrl(s2);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return;
	}

	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		Bundle bundle1 = getIntent().getExtras();
		if (bundle1 != null)
		{
			String s = bundle1.getString("url");
			currentUrl = s;
			String s1 = bundle1.getString("ui_title");
			title = s1;
		}
		System.out.println("currentUrl = "+currentUrl);
		System.out.println("title = "+title);
		
		//创建webView对象
		webView = new WebView(this);
		//初始化布局，初始化了顶部和添加了weiview控件
		initLayout();
		
		//setWebViewClient--表示要加载html,T8WebViewClient帮助WebView处理各种通知、请求事件
		webView.setWebViewClient(new T8WebViewClient());
		
		bindJavascriptHook();
		//创建handler类
		mHandler = new Handler();

		//加载url
		if (!StringUtil.isEmpty(currentUrl).booleanValue()){
			loadUrl(currentUrl);
		}
		
		//设置标题
		if (!StringUtil.isEmpty(title).booleanValue()){
			setUITitle(title);
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

	public void setUITitle(String s)
	{
		setTitle(s);
	}

	public void setWebActivityProperty(String s, Object obj)
	{
	}
	
	/*
	 * WebViewClient主要帮助WebView处理各种通知、请求事件的，比如：
	 * 
	 * onLoadResource onPageStart onPageFinish onReceiveError onReceivedHttpAuthRequest
	 * 
	 * */
	final class T8WebViewClient extends WebViewClient{

		final WebActivity this$0;

		//webview页面结束事件
		public void onPageFinished(WebView webview, String s){
			System.out.println("T8WebViewClient onPageFinished ");
			
			WebActivity webactivity = WebActivity.this;
			
			String as[] = new String[0];
			webactivity.callJSFunc("_page_loaded", as);
			
			String as1[] = new String[1];
			as1[0] = "true";
			//webactivity.callJSFunc("_page_appeared", as1);
		}

		private T8WebViewClient(){
			super();
			this$0 = WebActivity.this;
		}

	}

}
