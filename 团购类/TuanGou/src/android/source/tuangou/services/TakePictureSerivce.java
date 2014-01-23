package android.source.tuangou.services;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

public class TakePictureSerivce
{

	public TakePictureSerivce()
	{
	}

	public static void getPicture(Activity activity)
	{
		Intent intent = new Intent();
		Intent intent1 = intent.setType("image*//*");
		Intent intent2 = intent.setAction("android.intent.action.GET_CONTENT");
		Intent intent3 = Intent.createChooser(intent, null);
		activity.startActivityForResult(intent3, 2);
	}

	public static Bitmap savePicture(Activity activity, int i, int j, Intent intent){
		
		Bitmap bitmap = null;
		try {
			if (i != 1) {
				if (i != 2) {
					bitmap = null;
				} else {
					Uri uri1 = intent.getData();
					bitmap = BitmapFactory.decodeStream(activity
							.getContentResolver().openInputStream(uri1));
				}
			} else {
				if (j != -1) {
					if (j != 0) {
						bitmap = null;
					}
				} else {
					bitmap = (Bitmap) intent.getExtras().get("data");
					if (Environment.getExternalStorageState().equals("mounted")) {
						StringBuilder stringbuilder = new StringBuilder();
						File file = Environment.getExternalStorageDirectory();
						String s = stringbuilder.append(file)
								.append("/camera/").toString();
						File mFile = new File(s);
						boolean flag;
						if (!mFile.exists())
							flag = mFile.mkdir();
						StringBuilder stringbuilder1 = new StringBuilder();
						long l = System.currentTimeMillis();

						String s1 = stringbuilder1.append(l).append(".jpg")
								.toString();
						File file1 = new File(mFile, s1);
						FileOutputStream fileoutputstream = new FileOutputStream(
								file1);
						android.graphics.Bitmap.CompressFormat compressformat = android.graphics.Bitmap.CompressFormat.JPEG;
						boolean flag1 = bitmap.compress(compressformat, 100,
								fileoutputstream);
						fileoutputstream.close();

						Uri uri = Uri.parse((new StringBuilder())
								.append("file://").append(j).toString());
						Intent intent1 = new Intent(
								"android.intent.action.MEDIA_MOUNTED", uri);
						activity.sendBroadcast(intent1);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return bitmap;

	}

	public static void toTackPicture(Activity activity)
	{
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		activity.startActivityForResult(intent, 1);
	}
}
