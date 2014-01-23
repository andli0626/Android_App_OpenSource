package com.yizhao.blog;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
 

public class TokenStore {
	
	public static String fileName = "token_store";
	
	public static void store(Activity activity, com.tencent.weibo.beans.OAuth oauth) {
		SharedPreferences settings = activity.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		
		SharedPreferences.Editor editor = settings.edit();
		
		editor.putString("oauth_token", oauth.getOauth_token());
		editor.putString("oauth_token_secret", oauth.getOauth_token_secret());	
		
		editor.commit();		
	}
	
	public static String[] fetch(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		
		String oauth_token = settings.getString("oauth_token", null);
		String oauth_token_secret = settings.getString("oauth_token_secret", null);
		
		return new String[] {oauth_token, oauth_token_secret};
	}
	
	public static void clear(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		
		SharedPreferences.Editor editor = settings.edit();
		
		editor.clear();  
        editor.commit(); 
	}	
}
