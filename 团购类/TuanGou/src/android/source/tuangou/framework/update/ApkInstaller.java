package android.source.tuangou.framework.update;

import android.content.Intent;
import android.net.Uri;
import android.source.tuangou.framework.Application;
import android.source.tuangou.framework.file.FileHelper;

import java.io.File;

public class ApkInstaller
{

	public ApkInstaller()
	{
	}

	public static void install(File file)
	{
		Intent intent = new Intent();
		Intent intent1 = intent.addFlags(0x10000000);
		Intent intent2 = intent.setAction("android.intent.action.VIEW");
		String s = FileHelper.getMIMEType(file);
		Uri uri = Uri.fromFile(file);
		Intent intent3 = intent.setDataAndType(uri, s);
		Application.getInstance().startActivity(intent);
	}

	public static void install(String s)
	{
		install(new File(s));
	}
}
