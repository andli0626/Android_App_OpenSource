

package com.teleca.jamendo.widget;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.RejectedExecutionException;

import com.teleca.jamendo.MyApplication;
import com.teleca.jamendo.util.ImgCache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * ImageView extended class allowing easy downloading
 * of remote images
 * 
 * @author Lukasz Wisniewski
 */
public class MyImageView extends ImageView{
	
	/**
	 * Maximum number of unsuccesful tries of downloading an image
	 */
	private static int MAX_FAILURES = 3;

	public MyImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MyImageView(Context context) {
		super(context);
		init();
	}

	/**
	 * Sharable code between constructors
	 */
	private void init(){
	}
	
	/**
	 * Remote image location
	 */
	private String mUrl;
	
	/**
	 * Currently successfully grabbed url
	 */
	private String mCurrentlyGrabbedUrl;
	
	/**
	 * Remote image download failure counter
	 */
	private int mFailure;

	/**
	 * Position of the image in the mListView
	 */
	private int mPosition;

	/**
	 * ListView containg this image
	 */
	private ListView mListView;
	
	/**
	 * Default image shown while loading or on url not found
	 */
	private Integer mDefaultImage;

	/**
	 * Loads image from remote location
	 * 
	 * @param url eg. http://random.com/abz.jpg
	 */
	public void setImageUrl(String url){
		
		if(mListView == null && mCurrentlyGrabbedUrl != null && mCurrentlyGrabbedUrl.equals(url)){
			// do nothing image is grabbed & loaded, we are golden
			return;
		}
		
		if(mUrl != null && mUrl.equals(url)){
			mFailure++;
			if(mFailure > MAX_FAILURES){
				Log.e(MyApplication.TAG, "Failed to download "+url+", falling back to default image");
				loadDefaultImage();
				return;
			}
		} else {
			mUrl = url;
			mFailure = 0;
		}

		ImgCache imageCache = MyApplication.getInstance().getImageCache();
		if(imageCache.isCached(url)){
			this.setImageBitmap(imageCache.get(url));
		}
		else {
			try{
				new DownloadTask().execute(url);
			} catch (RejectedExecutionException e) {
				// do nothing, just don't crash
			}
		}
	}
	
	/**
	 * Sets default local image shown when remote one is unavailable
	 * 
	 * @param resid
	 */
	public void setDefaultImage(Integer resid){
		mDefaultImage = resid;
	}
	
	/**
	 * Loads default image
	 */
	private void loadDefaultImage(){
		if(mDefaultImage != null)
			setImageResource(mDefaultImage);
	}
	
	/**
	 * Loads image from remote location in the ListView
	 * 
	 * @param url eg. http://random.com/abz.jpg
	 * @param position ListView position where the image is nested
	 * @param listView ListView to which this image belongs
	 */
	public void setImageUrl(String url, int position, ListView listView){
		mPosition = position;
		mListView = listView;
		setImageUrl(url);
	}

	/**
	 * Asynchronous image download task
	 * 
	 * @author Lukasz Wisniewski
	 */
	class DownloadTask extends AsyncTask<String, Void, String>{
		
		private String mTaskUrl;

		@Override
		public void onPreExecute() {
			loadDefaultImage();
			super.onPreExecute();
		}

		@Override
		public String doInBackground(String... params) {

			mTaskUrl = params[0];
			InputStream stream = null;
			URL imageUrl;
			Bitmap bmp = null;

			try {
				imageUrl = new URL(mTaskUrl);
				try {
					stream = imageUrl.openStream();
					bmp = BitmapFactory.decodeStream(stream);
					try {
						if(bmp != null){
							MyApplication.getInstance().getImageCache().put(mTaskUrl, bmp);
							Log.d(MyApplication.TAG, "Image cached "+mTaskUrl);
						} else {
							Log.w(MyApplication.TAG, "Failed to cache "+mTaskUrl);
						}
					} catch (NullPointerException e) {
						Log.w(MyApplication.TAG, "Failed to cache "+mTaskUrl);
					}
				} catch (IOException e) {
					Log.w(MyApplication.TAG, "Couldn't load bitmap from url: " + mTaskUrl);
				} finally {
					try {
						if(stream != null){
							stream.close();
						}
					} catch (IOException e) {}
				}

			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return mTaskUrl;
		}

		@Override
		public void onPostExecute(String url) {
			super.onPostExecute(url);
			
			// target url may change while loading
			if(!mTaskUrl.equals(mUrl))
				return;
			
			Bitmap bmp = MyApplication.getInstance().getImageCache().get(url);
			if(bmp == null){
				Log.w(MyApplication.TAG, "Trying again to download " + url);
				MyImageView.this.setImageUrl(url);
			} else {
				
				// if image belongs to a list update it only if it's visible
				if(mListView != null)
					if(mPosition < mListView.getFirstVisiblePosition() || mPosition > mListView.getLastVisiblePosition())
						return;
				
				MyImageView.this.setImageBitmap(bmp);
				mCurrentlyGrabbedUrl = url;
			}
		}

	};

}
