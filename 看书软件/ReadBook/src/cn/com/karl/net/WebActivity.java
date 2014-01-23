package cn.com.karl.net;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import cn.com.karl.reader.R;

public class WebActivity extends Activity {
	
	ImageView image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.webcity);
		
//		webView = (WebView) findViewById(R.id.web_city);
//		String url = "https://www.baidu.com";
//		webView.loadUrl(url);
		image = (ImageView) findViewById(R.id.web_image);
		Intent intent= new Intent();        
		intent.setAction("android.intent.action.VIEW");    
		Uri content_url = Uri.parse("http://www.baidu.com");   
		intent.setData(content_url);  
		startActivity(intent);
		image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent= new Intent();        
				intent.setAction("android.intent.action.VIEW");    
				Uri content_url = Uri.parse("http://www.baidu.com");   
				intent.setData(content_url);  
				startActivity(intent);
				
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitDialog();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
	public void exitDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("你确定退出吗？").setCancelable(false)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
//						finish();
						//完全退出activity的方法
						Intent startMain = new Intent(Intent.ACTION_MAIN);  
						startMain.addCategory(Intent.CATEGORY_HOME);//必须，没有这个你可以看看效果~  
						startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//可无  
						startActivity(startMain);  
						System.exit(0);//关键,如果换成 finish()效果表面一样，但实际并无关进程  
					}
				})
				.setNegativeButton("返回", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
}
