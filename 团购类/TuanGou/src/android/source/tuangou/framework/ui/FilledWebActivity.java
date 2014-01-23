package android.source.tuangou.framework.ui;

import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

public class FilledWebActivity extends WebActivity
{

	public FilledWebActivity()
	{
	}

	protected void initLayout()
	{
		getWindow().setFlags(2048, 2048);
		android.widget.LinearLayout.LayoutParams layoutparams = new android.widget.LinearLayout.LayoutParams(-1, -1, 0F);
		android.widget.LinearLayout.LayoutParams layoutparams1 = new android.widget.LinearLayout.LayoutParams(-1, -1, 1F);
		LinearLayout linearlayout = new LinearLayout(this);
		linearlayout.setOrientation(1);
		linearlayout.setBackgroundColor(0xff000000);
		linearlayout.setLayoutParams(layoutparams);
		WebView webview = getWebView();
		webview.setLayoutParams(layoutparams1);
		webview.setInitialScale(100);
		webview.setVerticalScrollBarEnabled(false);
		WebSettings websettings = webview.getSettings();
		websettings.setJavaScriptEnabled(true);
		websettings.setJavaScriptCanOpenWindowsAutomatically(true);
		android.webkit.WebSettings.LayoutAlgorithm layoutalgorithm = android.webkit.WebSettings.LayoutAlgorithm.NORMAL;
		websettings.setLayoutAlgorithm(layoutalgorithm);
		linearlayout.addView(webview);
		setContentView(linearlayout);
	}
}
