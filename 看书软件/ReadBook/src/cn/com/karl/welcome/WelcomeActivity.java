package cn.com.karl.welcome;


import cn.com.karl.reader.R;
import cn.com.karl.tabhost.TabHostActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class WelcomeActivity extends Activity {

	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.welcome);
	        start();
	    }
	    public void start() {
			new Thread() {
				public void run() {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Intent intent = new Intent();
					intent.setClass(WelcomeActivity.this, TabHostActivity.class);
					startActivity(intent);
					finish();
				}
			}.start();

		}
}
