package android.source.tuangou;

import android.content.DialogInterface;
import android.os.Bundle;
import android.source.tuangou.framework.util.StringUtil;
import android.source.tuangou.framework.web.WebActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


/*
 * HeaderWebActivity--头部headerWebActivity类
 * */
public class HeaderWebActivity extends WebActivity{

	Button backButton;
	String id;
	TextView titleText;

	public HeaderWebActivity(){
	}

	public String getId()
	{
		return id;
	}

	//获得ActivityContainer对象
	public ActivityContainer getStack(){
		return (ActivityContainer)getParent();
	}

	//初始化布局函数--顶部和添加webview对象
	protected void initLayout(){
		setContentView(R.layout.header_web);
		
		LinearLayout linearlayout = (LinearLayout)findViewById(R.id.root);
		backButton = (Button)findViewById(R.id.btn_main_title_back);
		
		//获取数据
		Bundle bundle = getIntent().getExtras();
		//判断是否是第一个，是的话就不要显示back按钮
		if (bundle != null && bundle.getBoolean("isFirst")){
			backButton.setVisibility(View.GONE);
		}
		//设置返回按钮的单击事件监听器
		backButton.setOnClickListener(new OnClickListener1());

		//获取显示标题的控件
		titleText = (TextView)findViewById(R.id.txt_main_title);
		
		//获取webView对象
		WebView webview = getWebView();
		android.widget.LinearLayout.LayoutParams layoutparams = new android.widget.
			LinearLayout.LayoutParams(-1, -1, 1F);
		//设置webview的布局参数
		webview.setLayoutParams(layoutparams);
		//将控件设置到正好显示
		webview.setInitialScale(100);
		//设置控件不显滚动条
		webview.setVerticalScrollBarEnabled(false);
		//设置控件长按事件监听器
		webview.setOnLongClickListener(new OnLongClickListener2());
		//webview控件的设置类对象
		WebSettings websettings = webview.getSettings();
		//启用JavaScript
		websettings.setJavaScriptEnabled(true);
		websettings.setJavaScriptCanOpenWindowsAutomatically(true);
		android.webkit.WebSettings.LayoutAlgorithm layoutalgorithm = android.webkit.WebSettings.LayoutAlgorithm.NORMAL;
		websettings.setLayoutAlgorithm(layoutalgorithm);
		//线性布局中添加webview对象
		linearlayout.addView(webview);
	}

	//按键的事件处理函数
	public boolean onKeyDown(int keyCode, KeyEvent keyevent){
		boolean flag;
		//getRepeatCount--判断是否有重复，按下返回键且没有重复
		if (keyCode == keyevent.KEYCODE_BACK && 
				keyevent.getRepeatCount() == 0){
			
			//判断是否为最后一个，是的话提示退出对话框
			if (!getStack().backToStackBottom()){
			
				android.app.Activity activity = getParent();
				android.app.AlertDialog.Builder builder = (new android.app.AlertDialog.
						Builder(activity)).setTitle("退出程序").setMessage("是否退出团购大全？");
				
				OnClickListener4 mOnClickListener4 = new OnClickListener4();
				android.app.AlertDialog.Builder builder1 = builder.setPositiveButton("确定", mOnClickListener4);
				
				OnClickListener3 mOnClickListener3 = new OnClickListener3();
				builder1.setNegativeButton("取消", mOnClickListener3).create().show();
			}
			flag = true;
		} else{
			flag = super.onKeyDown(keyCode, keyevent);
		}
		return flag;
	}

	public void setId(String s){
		id = s;
	}

	public void setUITitle(String s){
		titleText.setText(s);
	}

	public void triggerJavascript(int i, String s){
		if (StringUtil.isEmpty(s).booleanValue())
			s = "null";
		switch(i){
			case 1:
				String as[] = new String[1];
				as[0] = s;
				callJSFunc("_on_page_back", as);
				break;
			case 2:
				String as1[] = new String[1];
				as1[0] = s;
				callJSFunc("_on_page_resume", as1);
				break;
		}
		return;
	
	}

	private class OnClickListener1
		implements android.view.View.OnClickListener{

		final HeaderWebActivity this$0;

		public void onClick(View view){
			getStack().back(null);
		}

		OnClickListener1(){
			super();
			this$0 = HeaderWebActivity.this;
		}
	}


	private class OnLongClickListener2
		implements android.view.View.OnLongClickListener{

		final HeaderWebActivity this$0;

		public boolean onLongClick(View view){
			HeaderWebActivity headerwebactivity = HeaderWebActivity.this;
			String as[] = new String[0];
			headerwebactivity.callJSFunc("_on_long_click", as);
			return true;
		}

		OnLongClickListener2(){
			super();
			this$0 = HeaderWebActivity.this;
		}
	}


	private class OnClickListener4
		implements android.content.DialogInterface.OnClickListener{

		final HeaderWebActivity this$0;

		public void onClick(DialogInterface dialoginterface, int i){
			finish();
		}

		OnClickListener4(){
			super();
			this$0 = HeaderWebActivity.this;
		}
	}


	private class OnClickListener3
		implements android.content.DialogInterface.OnClickListener{

		final HeaderWebActivity this$0;

		public void onClick(DialogInterface dialoginterface, int i){
			
		}

		OnClickListener3(){
			super();
			this$0 = HeaderWebActivity.this;
		}
	}

}
