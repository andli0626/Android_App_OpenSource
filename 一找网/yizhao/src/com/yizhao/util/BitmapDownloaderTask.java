package com.yizhao.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;

public class BitmapDownloaderTask extends AsyncTask<String,Void,Bitmap>{
	
	private String picUrl;
	
	public BitmapDownloaderTask(String _picUrl){
		picUrl = _picUrl;
	}
	
	@Override
	protected Bitmap doInBackground(String... params) {
		Bitmap bitmap_t = ImageUtil.getBitmap(picUrl);
		return bitmap_t;
	}
	
	@Override
	protected void onPostExecute(Bitmap bitmap) {
		if(isCancelled()){
			bitmap = null;
		}
	}
	

}
