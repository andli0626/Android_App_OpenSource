package android.source.tuangou;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.source.tuangou.framework.Application;
import android.source.tuangou.framework.Config;
import android.source.tuangou.framework.ServiceManager;
import android.source.tuangou.framework.auth.Session;
import android.source.tuangou.framework.store.beans.Preferences;
import android.source.tuangou.framework.update.UpdateService;
import android.source.tuangou.framework.update.VersionManager;
import android.source.tuangou.framework.util.StringUtil;
import android.source.tuangou.services.Umeng;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 加载页面
 * */
public class InitView extends Activity {

	private ImageView imageView;
	private TextView info;
	Handler pdHandler;
	private ProgressBar progressBar;
	private TextView rate;

	public InitView() {
		Handler1 mHandler1 = new Handler1();
		pdHandler = mHandler1;
	}

	// 其它工作函数
	private void doOtherJobs() {
		// 开启位置服务监听器
		ServiceManager.getLocationService().startLocationListener();

		// 不影响UI的轻量级线程
		AsyncTask1 mAsyncTask1 = new AsyncTask1();
		AsyncTask asynctask = mAsyncTask1.execute();
	}

	// 跳转到主界面
	private void startMainActivity() {
		Intent intent = new Intent(this, MainView.class);
		startActivity(intent);
		finish();
	}

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		// 设置自定义标题模式
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		// 设置布局文件
		setContentView(R.layout.launch);

		// 设置相关资源
		Umeng.setReportPolicy();

		// 设置自定义标题
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);

		// 进度条控件
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);

		// 信息显示控件
		info = (TextView) findViewById(R.id.tips);
		info.setText("团购");

		// 信息显示控件
		rate = (TextView) findViewById(R.id.rate);

		// 图片显示控件
		imageView = (ImageView) findViewById(R.id.download);

		// 删除数据库中temp_preferences表
		android.source.tuangou.beans.Preferences.getInstance().removeAll();

		// 客户端版本检测是否需要更新
		if (VersionManager.checkClientUpdate()) {
			// 客户端更新对话框
			android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
					this);
			String s = getResources().getString(R.string.new_apk_title);

			android.app.AlertDialog.Builder builder1 = builder.setTitle(s)
					.setMessage(R.string.new_apk_text);

			OnClickListener1 mOnClickListener1 = new OnClickListener1();
			android.app.AlertDialog.Builder builder2 = builder1
					.setPositiveButton(R.string.dlg_positive, mOnClickListener1);

			OnClickListener2 mOnClickListener2 = new OnClickListener2();
			builder2.setNegativeButton(R.string.dlg_negative, mOnClickListener2)
					.create().show();
		} else {
			// 不需要更新，做其他工作
			doOtherJobs();
		}
	}

	// actiivty暂停函数
	public void onPause() {
		super.onPause();
		Umeng.onPause(this);
	}

	// activity重新显示函数
	public void onResume() {
		super.onResume();
		Umeng.onResume(this);
	}

	// handler类，处理版本更新模块消息
	private class Handler1 extends Handler {

		final InitView this$0;

		public void handleMessage(Message message) {
			// 检测具体消息
			switch (message.what) {
			// 正在检测的消息处理
			case 0:
				info.setText("正在检测版本...");
				break;
			// 更新消息处理
			case 5:
				imageView.setVisibility(0);
				progressBar.setVisibility(0);
				info.setText("正在更新...");
				int i = message.arg1;
				progressBar.setProgress(i);
				TextView textview = rate;
				StringBuilder stringbuilder = new StringBuilder();
				int j = message.arg1;
				String s = stringbuilder.append(j).append("%").toString();
				textview.setText(s);
				if (message.arg1 == 100)
					Toast.makeText(InitView.this, "更新完成", 0).show();
				break;

			default:
				return;
			}
			return;
		}

		Handler1() {
			super();
			this$0 = InitView.this;
		}
	}

	// 轻量级别线程
	private class AsyncTask1 extends AsyncTask<Void, Void, Void> {
		final InitView this$0;

		protected Void doInBackground(Void... avoid) {
			try {
				String s = Config.PAGE_SOURCE;
				// 判断页面数据源是否为本地
				if ("local".equalsIgnoreCase(s)) {
					Looper.prepare();
					UpdateService.checkWebFileUpdate(pdHandler, true);
					Looper.loop();
				}

				XmlResourceParser xmlresourceparser;
				int i;
				String s1;
				String s2;
				xmlresourceparser = getResources().getXml(R.xml.partner);
				i = xmlresourceparser.getEventType();
				s1 = "0b2e3f";
				s2 = "0";
				while (true) {
					System.out.println("i = " + i);
					if (i == 1) {
						if (StringUtil.isEmpty(
								Preferences.getInstance().getRequestKey())
								.booleanValue()) {
							Preferences preferences = Preferences.getInstance();
							TelephonyManager telephonymanager = (TelephonyManager) getSystemService("phone");
							preferences.initRequestKey(telephonymanager,
									"partner", s1);
							Preferences.getInstance().save("umeng", s2);
						}
						break;
					} else {
						if (i != 2) {
							int k = xmlresourceparser.next();
							i = k;
							continue;
						} else {
							String s3 = xmlresourceparser.getName();

							if (!"partner".equalsIgnoreCase(s3)) {
								int k = xmlresourceparser.next();
								i = k;
								continue;
							} else {
								s1 = xmlresourceparser.getAttributeValue(null,
										"id");
								s2 = StringUtil.getValueOrDefault(
										xmlresourceparser.getAttributeValue(
												null, "umeng"), "0");
								break;
							}
						}
					}
				}

				if (Application.getSession().getCurrentLoginUser() != null) {
					Session session = Application.getSession();
					String s4 = Application.getSession().getCurrentLoginUser().name;
					String s5 = Application.getSession().getCurrentLoginUser().password;
					int j = session.login(s4, s5);
				}
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			return null;
		}

		protected void onPostExecute(Void void1) {
			super.onPostExecute(void1);
			imageView.setVisibility(View.INVISIBLE);
			info.setVisibility(View.INVISIBLE);
			rate.setVisibility(View.INVISIBLE);
			progressBar.setVisibility(View.INVISIBLE);
			// 进入主界面
			startMainActivity();
		}

		AsyncTask1() {
			super();
			this$0 = InitView.this;
		}
	}

	// 客户端对话框按钮点击事件监听器
	private class OnClickListener1 implements
			android.content.DialogInterface.OnClickListener {

		final InitView this$0;

		public void onClick(DialogInterface dialoginterface, int i) {
			// 客户端更新
			UpdateService.checkClientUpdate(pdHandler);
		}

		OnClickListener1() {
			super();
			this$0 = InitView.this;
		}
	}

	// 控件点击监听函数
	private class OnClickListener2 implements
			android.content.DialogInterface.OnClickListener {

		final InitView this$0;

		public void onClick(DialogInterface dialoginterface, int i) {
			doOtherJobs();
		}

		OnClickListener2() {
			super();
			this$0 = InitView.this;
		}
	}

}
