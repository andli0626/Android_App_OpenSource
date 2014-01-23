package cn.com.karl.tabhost;

import cn.com.karl.collect.CollectActivity;
import cn.com.karl.net.WebActivity;
import cn.com.karl.reader.BookShelfActivity;
import cn.com.karl.reader.R;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class TabHostActivity extends TabActivity implements
		OnCheckedChangeListener {
	TabHost tabhost;
	private RadioGroup maintab;
	private Intent local_book;// 本地书架
	private Intent book_city;// 网络书城
	private Intent user_collect;// 我的收藏
	// private Intent image;// 精美图片
	// int user_id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.tabhost_bottom);
		maintab = (RadioGroup) findViewById(R.id.main_tab);
		maintab.setOnCheckedChangeListener(this);
		tabhost = getTabHost();
		local_book = new Intent(this, BookShelfActivity.class);// 跳到主页面
		tabhost.addTab(tabhost
				.newTabSpec("local_book")
				.setIndicator("本地书架",
						getResources().getDrawable(R.drawable.book_icon))
				.setContent(local_book));
		book_city = new Intent(this, WebActivity.class);// 跳到网络书城
		tabhost.addTab(tabhost
				.newTabSpec("book_city")
				.setIndicator("我的信息",
						getResources().getDrawable(R.drawable.bookcity_icon))
				.setContent(book_city));
		user_collect = new Intent(this, CollectActivity.class);// 跳到我的收藏
		tabhost.addTab(tabhost
				.newTabSpec("invitation")
				.setIndicator("我的帖子",
						getResources().getDrawable(R.drawable.collect_icon))
				.setContent(user_collect));
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		switch (checkedId) {
		case R.id.radio_main:
			tabhost.setCurrentTabByTag("local_book");
			break;

		case R.id.radio_net:
			tabhost.setCurrentTabByTag("book_city");
			break;
		case R.id.radio_collect:
			tabhost.setCurrentTabByTag("invitation");
			break;
//		case R.id.radio_image:
//			tabhost.setCurrentTabByTag("image");
//			break;
		}

	}
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(1, 1, 1, "发表帖子");
//		menu.add(1, 1, 2, "修改密码");
//		menu.add(1, 1, 3, "百度地图");
//		menu.add(1, 1, 4, "查看收藏");
//		menu.add(1, 1, 5, "关于我们");
//		menu.add(1, 1, 6, "退出程序");
//		return super.onCreateOptionsMenu(menu);
//	}
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		Intent intent;
//		if (item.getTitle().equals("扫描sd卡")) {
//			intent = new Intent(this,AboutAcitvity.class);
//			startActivity(intent);
//		} else if (item.getTitle().equals("退出程序")) {
//			dialog();
//		} else if (item.getTitle().equals("修改密码")) {
//			intent = new Intent(this, UpdatePassword.class);
//			startActivity(intent);
//		} else if (item.getTitle().equals("百度地图")) {
//			intent = new Intent(this, BMapApiDemoMain.class);
//			startActivity(intent);
//		} else if (item.getTitle().equals("发表帖子")) {
//			intent = new Intent(this, PublishInvitation.class);
//			startActivity(intent);
//		}else{
//			intent = new Intent(this);
//			startActivity(intent);
//		}
//		return super.onOptionsItemSelected(item);
//	}
//
//	public void dialog() {
//		new AlertDialog.Builder(this).setTitle("提示").setMessage("是否退出本程序？")
//				.setPositiveButton("是的", new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//
//						// 完全退出activity的方法
//						Intent startMain = new Intent(Intent.ACTION_MAIN);
//						startMain.addCategory(Intent.CATEGORY_HOME);// 必须，没有这个你可以看看效果~
//						startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 可无
//						startActivity(startMain);
//						System.exit(0);// 关键,如果换成 finish()效果表面一样，但实际并无关进程
//					}
//				}).setNeutralButton("取消", null).create().show();
//	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();
	}
}
