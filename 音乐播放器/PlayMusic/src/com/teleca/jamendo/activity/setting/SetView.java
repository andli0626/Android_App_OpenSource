package com.teleca.jamendo.activity.setting;

import com.teleca.jamendo.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * 设置界面
 * 
 * @author lilin
 * @date 2011-12-13 上午12:06:03
 * @ClassName: SettingsActivity
 */
public class SetView extends PreferenceActivity {
	public static void launch(Context c) {
		Intent intent = new Intent(c, SetView.class);
		c.startActivity(intent);
	}

	public void onCreate(Bundle savedInstanceState) {
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		setContentView(R.layout.settingsview);
		setTitle("系统设置");
	}

}