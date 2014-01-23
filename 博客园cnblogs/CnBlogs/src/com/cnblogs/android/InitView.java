package com.cnblogs.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

/**
 * ÉÁÆÁ
 * 
 * @author walkingp
 * @date:2011-12
 * 
 */
public class InitView extends SuperView {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		// ÉèÖÃÑÓ³Ù£¬²¥·ÅµÇÂ½½çÃæ
		new Handler().postDelayed(new Runnable() {
			public void run() {
				RedirectMainActivity();
			}
		}, 3000);
	}

	/**
	 * Ìø×ª
	 */
	private void RedirectMainActivity() {
		Intent i = new Intent();
		i.setClass(InitView.this, MainView.class);
		startActivity(i);
		InitView.this.finish();
	}
}
