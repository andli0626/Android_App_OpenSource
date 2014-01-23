package android.source.tuangou.framework.web.bridges;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.source.tuangou.framework.ui.FilledWebActivity;
import android.source.tuangou.framework.util.LogUtil;
import android.source.tuangou.framework.util.StringUtil;
import android.source.tuangou.framework.web.ScriptBridge;
import android.source.tuangou.framework.web.WebActivity;
import android.webkit.WebView;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

public class WebBridge extends ScriptBridge
{

	public WebBridge()
	{
	}

	public String getContextData(String s)
	{
		return getContext().getIntent().getExtras().getString(s);
	}

	public String getQueryParams()
	{
		return getContext().getQueryParams();
	}

	public void loadUrl(String s)
	{
		getContext().loadUrl(s);
	}

	//打开相应的activity
	public void openActivity(String s, String s1){
		
		
		WebActivity webactivity;
		Intent intent;
		try {
			Class class1 = Class.forName(s);
			JSONObject jsonobject = StringUtil.parseJSON(s1);
			webactivity = getContext();
			
			System.out.println("javaScrpit call java: openActivity to class = "+s1);
			
			intent = new Intent(webactivity, class1);
			for (Iterator iterator = jsonobject.keys(); iterator.hasNext();) {
				String s2 = iterator.next().toString();
				String s3 = jsonobject.getString(s2);
				Intent intent1 = intent.putExtra(s2, s3);
			}
			webactivity.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	public void openUrl(String s, String s1)
	{
		JSONObject jsonobject;
		String s2;
		jsonobject = StringUtil.parseJSON(s1);
		s2 = "";
		Object obj;
		if (!jsonobject.has("cls")){
			return;
		}
		try {
			obj = jsonobject.getString("cls");
			s2 = ((String) (obj));
			WebActivity webactivity = getContext();
			Class class1 = null;
			if (!"".equalsIgnoreCase(s2)) {
				obj = Class.forName(s2);
				class1 = ((Class) (obj));

			} else {
				class1 = FilledWebActivity.class;
			}
			Intent intent;
			intent = new Intent(webactivity, class1);
			Intent intent1 = intent.putExtra("url", s);
			for (Iterator iterator = jsonobject.keys(); iterator.hasNext();) {
				String s3 = iterator.next().toString();
				String s4 = jsonobject.getString(s3);
				Intent intent2 = intent.putExtra(s3, s4);
			}
			webactivity.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
	}

	public void refresh()
	{
		WebActivity webactivity = getContext();
		webactivity.getWebView().clearCache(true);
		webactivity.getWebView().reload();
	}

	public void share(final String content)
	{
		final WebActivity ctx = getContext();
		String as[] = new String[2];
		as[0] = "短信";
		as[1] = "电子邮件";
		Activity activity = ctx.getParent();
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
		android.app.AlertDialog.Builder builder1 = builder.setTitle("分享给好友");
		
		OnClickListener1 mOnClickListener1 = new OnClickListener1(content, ctx);
		android.app.AlertDialog.Builder builder2 = builder.setItems(as, mOnClickListener1);
		builder.create().show();
	}

	public void toDealSite(String s)
	{
		LogUtil.i((new StringBuilder()).append("site url:").append(s).toString());
		Uri uri = Uri.parse(s);
		Intent intent = new Intent("android.intent.action.VIEW", uri);
		getContext().startActivity(intent);
	}

	public void viewMap(String s, String s1)
	{
		Uri uri = Uri.parse((new StringBuilder()).append("http://ditu.google.cn/maps?hl=zh&mrt=loc&q=").append(s).toString());
		Intent intent = new Intent("android.intent.action.VIEW", uri);
		getContext().startActivity(intent);
	}

	private class OnClickListener1
		implements android.content.DialogInterface.OnClickListener
	{

		final WebBridge this$0;
		final String content;
		final WebActivity ctx;

		public void onClick(DialogInterface dialoginterface, int i){
			
			switch(i){
			case 0:
				Uri uri = Uri.parse("smsto:");
				Intent intent = new Intent("android.intent.action.SENDTO", uri);
				String s = content;
				Intent intent1 = intent.putExtra("sms_body", s);
				ctx.startActivity(intent);
				break;
			case 1:
				StringBuilder stringbuilder = (new StringBuilder()).append("mailto:?subject=分享一个精品团购&body=");
				String s1 = content;
				Uri uri1 = Uri.parse(stringbuilder.append(s1).toString());
				Intent intent2 = new Intent("android.intent.action.SENDTO", uri1);
				ctx.startActivity(intent2);
				break;
				
			default:
				dialoginterface.dismiss();
			}
		}

		OnClickListener1(String content, WebActivity webActivity)
		{
			super();
			this$0 = WebBridge.this;
			this.content = content;
			ctx = webActivity;
		}
	}

}
