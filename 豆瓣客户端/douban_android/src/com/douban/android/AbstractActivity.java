package com.douban.android;

import android.app.Activity;
import android.content.SharedPreferences;

import com.douban.android.util.DoubanUtil;
import com.douban.android.util.PreferencesUtil;
import com.google.gdata.client.douban.DoubanService;

public abstract class AbstractActivity extends Activity {
	/**
	 * 得到授权后的豆瓣服务。
	 * @return
	 */
	public DoubanService getAuthDoubanService() {

		DoubanService myService = new DoubanService("subApplication",
				DoubanUtil.apiKey, DoubanUtil.secret, false);
		SharedPreferences settings = getSharedPreferences(
				PreferencesUtil.preferencesDouban, 0);

		String acessToken = settings.getString(PreferencesUtil.acessToken,
				"false");
		String acessTokenSecret = settings.getString(
				PreferencesUtil.acessTokenSecret, "false");
		myService.setAccessToken(acessToken, acessTokenSecret);
		return myService;
	}
}
