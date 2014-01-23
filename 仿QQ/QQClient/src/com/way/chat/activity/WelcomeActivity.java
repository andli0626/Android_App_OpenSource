package com.way.chat.activity;

import com.way.Constants;
import com.way.util.SharePreferenceUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;

/**
 * 欢迎界面
 * 
 * @author way
 */
public class WelcomeActivity extends Activity {
	private SharePreferenceUtil util;
	private Handler mHandler;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		util = new SharePreferenceUtil(this, Constants.SAVE_USER);
		if (util.getisFirst()) {
			// 创建添加快捷方式的Intent
			Intent addIntent = new Intent(
					"com.android.launcher.action.INSTALL_SHORTCUT");
			String title = getResources().getString(R.string.app_name);
			// 加载快捷方式的图标
			Parcelable icon = Intent.ShortcutIconResource.fromContext(
					WelcomeActivity.this, R.drawable.icon);
			// 创建点击快捷方式后操作Intent,该处当点击创建的快捷方式后，再次启动该程序
			Intent myIntent = new Intent(WelcomeActivity.this,
					WelcomeActivity.class);
			// 设置快捷方式的标题
			addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
			// 设置快捷方式的图标
			addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
			// 设置快捷方式对应的Intent
			addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, myIntent);
			// 发送广播添加快捷方式
			sendBroadcast(addIntent);
			util.setIsFirst(false);
		}
		initView();
	}

	public void initView() {
		if (util.getIsStart()) {// 如果正在后台运行
			goFriendListActivity();
		} else {// 如果是首次运行
			mHandler = new Handler();
			mHandler.postDelayed(new Runnable() {

				public void run() {
					// TODO Auto-generated method stub
					goLoginActivity();
				}
			}, 1000);
		}
	}

	public void goLoginActivity() {
		Intent intent = new Intent();
		intent.setClass(this, LoginView.class);
		startActivity(intent);
		finish();
	}

	public void goFriendListActivity() {
		Intent i = new Intent(this, FriendListActivity.class);
		startActivity(i);
		util.setIsStart(false);
		finish();
	}

}