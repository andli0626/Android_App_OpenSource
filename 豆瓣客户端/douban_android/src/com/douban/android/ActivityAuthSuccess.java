package com.douban.android;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.douban.android.util.DoubanUtil;
import com.douban.android.util.PreferencesUtil;
import com.google.gdata.client.douban.DoubanService;
import com.google.gdata.data.douban.UserEntry;
import com.google.gdata.util.ServiceException;

/**
 * @author haiyang 获取token,并且进行保存
 */
public class ActivityAuthSuccess extends AbstractActivity {
	DoubanService myService;
	SharedPreferences settings;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		saveAccessToken();
		saveUserInfo();
		Intent intent = new Intent(ActivityAuthSuccess.this,
				ActivityShowSaying.class);
		startActivity(intent);
	}

	private String saveAccessToken() {

		myService = new DoubanService("subApplication",
				DoubanUtil.apiKey, DoubanUtil.secret, true);
		String url = myService.getAuthorizationUrl(DoubanUtil.callback);

		 settings = getSharedPreferences(
				PreferencesUtil.preferencesDouban, 0);
		
		String requestToken = settings.getString(PreferencesUtil.oauthToken,
				"false");
		String requestTokenSecret = settings.getString(PreferencesUtil.oauthTokenSecret,
		"false");
		Log.i("jinyan", "requestToken=" + requestToken);
		myService.setRequestToken(requestToken);
		myService.setRequestTokenSecret(requestTokenSecret);
		
		ArrayList<String> list = myService.getAccessToken();
		String acessToken = list.get(0);
		String acessTokenSecret = list.get(1);
		Log.i("jinyan", "acessToken=" + acessToken);
		Log.i("jinyan", "acessTokenSecret=" + acessTokenSecret);
		settings.edit().putString(PreferencesUtil.acessToken, acessToken)
				.putString(PreferencesUtil.acessTokenSecret, acessTokenSecret)
				.commit();

		return url;
	}
	/*
	 * 保存用户的信息，目前只先保存用户名
	 */
	private void saveUserInfo(){
		Log.i("jinyan","saveUserInfo");
		try {
			DoubanService Service = getAuthDoubanService();
			UserEntry userEntry=	Service.getAuthorizedUser();
			settings.edit().putString(PreferencesUtil.userName, userEntry.getUid()).commit();
			Log.i("jinyan","saveUserInfo"+userEntry.getUid());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}