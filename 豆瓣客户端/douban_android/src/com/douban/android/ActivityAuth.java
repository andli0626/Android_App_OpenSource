package com.douban.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.douban.android.util.DoubanUtil;
import com.douban.android.util.PreferencesUtil;
import com.google.gdata.client.douban.DoubanService;

/**
 * @author haiyang �����������������
 */
public class ActivityAuth extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setTitle("豆瓣网������");
		openWebBrowser(getRequestUrl());
	}

	private String getRequestUrl() {

		DoubanService myService = new DoubanService("subApplication",
				DoubanUtil.apiKey, DoubanUtil.secret, true);
		String url = myService.getAuthorizationUrl(DoubanUtil.callback);
		Log.i("jinyan", "url=" + url);

		SharedPreferences settings = getSharedPreferences(
				PreferencesUtil.preferencesDouban, 0);
		settings.edit().putString(PreferencesUtil.oauthToken,
				myService.getRequestToken()).putString(
				PreferencesUtil.oauthTokenSecret,
				myService.getRequestTokenSecret()).commit();

		return url;
	}

	protected void openWebBrowser(String url) {
		Log
				.i(this.getClass().getName(), "about to launch browser, url: "
						+ url);
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}
}