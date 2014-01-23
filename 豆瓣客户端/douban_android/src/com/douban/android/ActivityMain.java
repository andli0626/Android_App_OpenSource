package com.douban.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.douban.android.util.PreferencesUtil;

/**
 * @author haiyang 用户欢迎界面
 */
public class ActivityMain extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		if (!acessTokenIsValid())
			showAlertDialog();
		else {
			Intent intent = new Intent(ActivityMain.this,
					ActivityShowSaying.class);
			startActivity(intent);
		}
	}

	/*
	 * 判断SharedPreferences中是否已经存储了acessToken
	 */
	private boolean acessTokenIsValid() {
		SharedPreferences settings = getSharedPreferences(
				PreferencesUtil.preferencesDouban, 0);
		String requestToken = settings.getString(PreferencesUtil.acessToken,
				"false");
		if (requestToken.equals("false"))
			return false;
		else
			return true;
	}

	private void showAlertDialog() {
		final AlertDialog alert = new AlertDialog.Builder(this).create();
		alert.setMessage("这是您是第一次登陆，只需要进行简单的配置，您就可以正常使用啦:)");
		alert.setButton("登陆", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int whichButton) {
				alert.dismiss();
				Intent intent = new Intent(ActivityMain.this,
						ActivityAuth.class);
				startActivity(intent);

			}
		});
		alert.show();
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