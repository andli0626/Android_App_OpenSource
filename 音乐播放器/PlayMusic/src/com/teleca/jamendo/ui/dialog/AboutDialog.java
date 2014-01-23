package com.teleca.jamendo.ui.dialog;

import com.teleca.jamendo.MyApplication;
import com.teleca.jamendo.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * 关于 对话框
 * 
 * @author lilin
 * @date 2012-1-7 下午01:13:06
 * @ClassName: AboutDialog
 */
public class AboutDialog extends Dialog implements OnClickListener {
	// 显示版本信息
	private TextView versionText;
	// 浏览按钮
	private Button browseBtn;
	// 关于按钮
	private Button aboutBtn;
	// 退出按钮
	private Button quitBtn;
	// 控制页面的切换
	private ViewFlipper changeViewFlipper;
	// 上下文
	private Activity activity;

	// 主界面调用的构造函数
	public AboutDialog(Activity context) {
		super(context);
		init(context);
	}

	public AboutDialog(Activity context, int theme) {
		super(context, theme);
		init(context);
	}

	public AboutDialog(Activity context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(context);
	}

	// 初始化方法
	private void init(final Activity context) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aboutview);
		activity = context;
		initUI();

		// 设置版本信息
		String version = MyApplication.getInstance().getVersion();
		String topText = "版本号 " + version
				+ "\n Copyright © 2012. All Rights Reserved.";
		versionText.setText(topText);
	}

	// 实例话控件
	private void initUI() {
		versionText = (TextView) findViewById(R.id.VersionText);
		browseBtn = (Button) findViewById(R.id.TermsButton);
		browseBtn.setOnClickListener(this);
		quitBtn = (Button) findViewById(R.id.CancelButton);
		quitBtn.setOnClickListener(this);
		changeViewFlipper = (ViewFlipper) findViewById(R.id.CompanyViewFlipper);
		aboutBtn = (Button) findViewById(R.id.AboutCompanyButton);
		aboutBtn.setOnClickListener(this);
	}

	// 按钮点击事件
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.TermsButton:
			// 访问公司网站
		{
			Intent myintent = new Intent(Intent.ACTION_VIEW, Uri
					.parse("http://www.jamendo.com/en/cgu_user"));
			activity.startActivity(myintent);
		}

			break;
		case R.id.CancelButton:
			// 退出
		{
			AboutDialog.this.dismiss();
		}

			break;
		case R.id.AboutCompanyButton:
			// 关于
		{
			int currentCompany = changeViewFlipper.getDisplayedChild();
			if (currentCompany == 0) {
				changeViewFlipper.setDisplayedChild(1); // display Teleca
			} else {
				changeViewFlipper.setDisplayedChild(0); // display Jamendo
			}
		}

			break;

		default:
			break;
		}

	}

}
