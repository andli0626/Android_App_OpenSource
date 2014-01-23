package android.source.tuangou.framework.file;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.source.tuangou.framework.net.NetworkService;
import android.widget.ImageView;


public class ImageService
{
	public class ShowCaptchaAsyncTask extends ShowImageAsyncTask
	{

		final ImageService this$0;

		protected  Drawable doInBackground(Void... avoid){
			String s = url;
			ImageService imageservice = ImageService.sharedInstance();
			return imageservice.requestDrawable(s);
		}


		public ShowCaptchaAsyncTask(ImageView imageview, String s){
			super(imageview, s);
			this$0 = ImageService.this;
		}
	}

	public class ShowImageAsyncTask extends AsyncTask<Void,Void,Drawable>{

		private ImageView imgView;
		final ImageService this$0;
		protected String url;

		protected  Drawable doInBackground(Void... avoid)
		{
			ImageService imageservice = ImageService.sharedInstance();
			String s = url;
			return imageservice.getDrawable(s);
		}


		protected void onPostExecute(Drawable drawable){
			
		}

		
		private ShowImageAsyncTask(){
			super();
			this$0 = ImageService.this;
		}

		public ShowImageAsyncTask(ImageView imageview, String s){
			super();
			this$0 = ImageService.this;
			imgView = imageview;
			url = s;
		}
	}


	private static final String IMAGE_FOLDER = "/Android/data/com.tuan800.android/cache/";
	private static final String TUAN800_CACHE_FOLDER_V1_0 = "/Tuan800_Cache/";
	private static ImageService instance;
	private Map mapImages;
	private NetworkService networkService;

	private ImageService()
	{
		HashMap hashmap = new HashMap();
		mapImages = hashmap;
		NetworkService networkservice = NetworkService.sharedInstance();
		networkService = networkservice;
	}

	public static void removeV1_0ImageCacheFolder()
	{
		StringBuilder stringbuilder = new StringBuilder();
		String s = Environment.getExternalStorageDirectory().getPath();
		boolean flag = FileHelper.delete(stringbuilder.append(s).append("/Tuan800_Cache/").toString());
	}

	public static ImageService sharedInstance()
	{
		if (instance == null)
		{
			StringBuilder stringbuilder = new StringBuilder();
			String s = Environment.getExternalStorageDirectory().getPath();
			String s1 = stringbuilder.append(s).append("/Android/data/com.tuan800.android/cache/").toString();
			File file = new File(s1);
			boolean flag;
			if (!file.exists())
				flag = file.mkdirs();
			instance = new ImageService();
		}
		return instance;
	}

	public Drawable getDrawable(String s)
	{
		if (mapImages.size() > 80)
			mapImages.clear();
		return null;
	}

	public Drawable requestDrawable(String s)
	{
		Drawable drawable = null;
		HttpResponse httpresponse;
		try {
			if (s == null) {
				return drawable;
			}
			httpresponse = networkService.getResponse(s);
			if (httpresponse == null) {
				return drawable;
			}
			int i = httpresponse.getStatusLine().getStatusCode();
			if (200 != i) {
				return drawable;
			}
			Drawable drawable1 = Drawable.createFromStream(httpresponse
					.getEntity().getContent(), "http");
			drawable = drawable1;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return drawable;
	}

	public AsyncTask updateCaptchaImageView(ImageView imageview)
	{
		ShowCaptchaAsyncTask showcaptchaasynctask = new ShowCaptchaAsyncTask(imageview, "http://api.tuan800.com/mobile_api/android/get_captcha");
		return showcaptchaasynctask.execute();
	}

	public AsyncTask updateImageView(ImageView imageview, String s)
	{
		ShowImageAsyncTask showimageasynctask = new ShowImageAsyncTask(imageview, s);
		return showimageasynctask.execute();
	}
}
