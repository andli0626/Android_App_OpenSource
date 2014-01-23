package com.teleca.jamendo.ui.dialog;

import com.teleca.jamendo.api.IServerApi;
import com.teleca.jamendo.api_impl.ServerApiImpl;
import com.teleca.jamendo.model.Track;
import com.teleca.jamendo.model.ErrorMsg;
import com.teleca.jamendo.R;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

/**
 * Dialog representing lyrics
 * 
 * @author Lukasz Wisniewski
 */
public class LyricsDialog extends Dialog {
	
	private Activity mActivity;
	
	/**
	 * Remember last used track
	 */
	private static Track mTrack;
	
	/**
	 * Remember last used lyrics
	 */
	private static String mLyrics;
	
	private ViewFlipper mViewFlipper;
	private TextView mTextView;

	public LyricsDialog(Activity context, Track track) {
		super(context);
		init(context, track);
	}
	
	/**
	 * Sharable code between constructors
	 * 
	 * @param context
	 * @param track
	 */
	private void init(Activity context, Track track){
		mActivity = context;
		setContentView(R.layout.lyrics);
		setTitle(R.string.lyrics);
		
		mViewFlipper = (ViewFlipper)findViewById(R.id.LyricsViewFlipper);
		mTextView = (TextView)findViewById(R.id.LyricsTextView);
		
		if(track != mTrack){
			new LyricsTask().execute(track);
		} else {
			showLyrics();
		}
	}
	
	/**
	 * Show us the lyrics
	 */
	private void showLyrics(){
		if(mLyrics == null || mLyrics.equals("null")){
			mTextView.setText(R.string.no_lyrics);
		}else{
			mTextView.setText(mLyrics);
		}
		mViewFlipper.setDisplayedChild(1);
	}
	
	/**
	 * do background JamendoGet2Api.getTrackLyrics
	 * 
	 * @author Lukasz Wisniewski
	 */
	private class LyricsTask extends AsyncTask<Track, ErrorMsg, String>{

		@Override
		public String doInBackground(Track... params) {
			IServerApi service = new ServerApiImpl();
			try {
				return service.getTrackLyrics(params[0]);
			} catch (ErrorMsg e) {
				publishProgress(e);
				dismiss();
				this.cancel(true);
				return null;
			}
		}

		@Override
		public void onPostExecute(String result) {
			super.onPostExecute(result);
			mLyrics = result;
			showLyrics();
		}
		
		@Override
		protected void onProgressUpdate(ErrorMsg... values) {
			Toast.makeText(mActivity, values[0].getMessage(), Toast.LENGTH_LONG).show();
			super.onProgressUpdate(values);
		}
	}

}
