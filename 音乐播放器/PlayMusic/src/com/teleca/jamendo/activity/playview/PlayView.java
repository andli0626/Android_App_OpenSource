package com.teleca.jamendo.activity.playview;

import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.teleca.jamendo.MyApplication;
import com.teleca.jamendo.R;
import com.teleca.jamendo.activity.MainView;
import com.teleca.jamendo.activity.album.AlbumView;
import com.teleca.jamendo.activity.artist.ArtistActivity;
import com.teleca.jamendo.api.IPlayEngine;
import com.teleca.jamendo.api.IPlayEngineListener;
import com.teleca.jamendo.api.IServerApi;
import com.teleca.jamendo.api_impl.ServerApiImpl;
import com.teleca.jamendo.model.Album;
import com.teleca.jamendo.model.ErrorMsg;
import com.teleca.jamendo.model.License;
import com.teleca.jamendo.model.Playlist;
import com.teleca.jamendo.model.PlaylistRemote;
import com.teleca.jamendo.model.Track;
import com.teleca.jamendo.ui.dialog.AddToPlaylistDialog;
import com.teleca.jamendo.ui.dialog.LyricsDialog;
import com.teleca.jamendo.ui.loadDialog.LoadDialog;
import com.teleca.jamendo.ui.loadDialog.PlayAlbumLoadDialog;
import com.teleca.jamendo.ui.loadDialog.PlaylistRemoteLoadDialog;
import com.teleca.jamendo.util.Helper;
import com.teleca.jamendo.widget.MyImageView;
import com.teleca.jamendo.widget.ReflectableLayout;
import com.teleca.jamendo.widget.ReflectiveSurface;

/**
 * 播放界面
 * 
 * @author lilin
 * @date 2011-12-13 上午12:30:26
 * @ClassName: PlayerView
 */
public class PlayView extends Activity implements OnClickListener {

	// 播放方法
	private PlayMethod playMethod;
	private Album myAlbum = null;
	private TextView artist_tv;
	private TextView song_tv;
	private TextView curTime_tv;
	private TextView totalTime_tv;
	private RatingBar mRatingBar;
	private ProgressBar mProgressBar;

	// 操作按钮
	private ImageButton playBtn;
	private ImageButton nextBtn;
	private ImageButton preBtn;
	private ImageButton stopBtn;

	private MyImageView mCoverImageView;
	private MyImageView mLicenseImageView;

	private Animation mFadeInAnimation;
	private Animation mFadeOutAnimation;

	private ReflectableLayout mReflectableLayout;
	private ReflectiveSurface mReflectiveSurface;

	private String mBetterRes;

	private SlidingDrawer slidingDrawer;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.playerview);
		setTitle("播放主界面");
		// 初始化
		initView();
		handleIntent();
		// cupcake backwards compability
		int sdkVersion = Integer.parseInt(Build.VERSION.SDK);
		if (sdkVersion == Build.VERSION_CODES.CUPCAKE) {
			// 抽屉控件单击事件
			new CupcakeListener();
		}

	}

	private void handleIntent() {
		PlayMethod playMethod2 = null;
		if (getIntent().getData() != null) {
			if (!getIntent().getBooleanExtra("handled", false)) {
				new UriLoadingDialog(this, "正在努力加载", "加载失败").execute();
			}
		} else {
			playMethod2 = (PlayMethod) getIntent().getSerializableExtra(
					"playlist");
			loadPlaylist(playMethod2);
		}
	}

	private void loadPlaylist(PlayMethod playMethod) {
		if (playMethod == null)
			return;
		this.playMethod = playMethod;
		if (playMethod != getPlayerEngine().getPlaylist()) {
			// getPlayerEngine().stop();
			getPlayerEngine().openPlaylist(playMethod);
			getPlayerEngine().play();
		}
	}

	private IPlayEngine getPlayerEngine() {
		return MyApplication.getInstance().getPlayerEngineInterface();
	};

	@Override
	public void onResume() {
		super.onResume();
		// register UI listener
		MyApplication.getInstance().setPlayerEngineListener(
				mPlayerEngineListener);

		// refresh UI
		if (getPlayerEngine() != null) {
			// the playlist is empty, abort playback, show message
			if (getPlayerEngine().getPlaylist() == null) {
				Toast.makeText(this, R.string.no_tracks, 5000).show();
				finish();
				return;
			}
			mPlayerEngineListener.onTrackChanged(getPlayerEngine()
					.getPlaylist().getSelectedTrack());
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		// unregister UI listener
		MyApplication.getInstance().setPlayerEngineListener(null);
	}

	/**
	 * 显示按钮
	 */
	private void showMediaBtn() {
		playBtn.setVisibility(View.VISIBLE);
		nextBtn.setVisibility(View.VISIBLE);
		preBtn.setVisibility(View.VISIBLE);
		stopBtn.setVisibility(View.VISIBLE);
	}

	/**
	 * Makes 4-way media gone
	 */
	private void setMediaGone() {
		playBtn.setVisibility(View.GONE);
		nextBtn.setVisibility(View.GONE);
		preBtn.setVisibility(View.GONE);
		stopBtn.setVisibility(View.GONE);
	}

	/**
	 * Sets fade out animation to 4-way media
	 */
	private void setFadeOutAnimation() {
		playBtn.setAnimation(mFadeOutAnimation);
		nextBtn.setAnimation(mFadeOutAnimation);
		preBtn.setAnimation(mFadeOutAnimation);
		stopBtn.setAnimation(mFadeOutAnimation);
	}

	/**
	 * Sets fade out animation to 4-way media
	 */
	private void setFadeInAnimation() {
		playBtn.setAnimation(mFadeInAnimation);
		nextBtn.setAnimation(mFadeInAnimation);
		preBtn.setAnimation(mFadeInAnimation);
		stopBtn.setAnimation(mFadeInAnimation);
	}

	/**
	 * Launches fade in/out sequence
	 */
	private OnClickListener mCoverOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (playBtn.getVisibility() == View.GONE) {
				showMediaBtn();
				setFadeInAnimation();
				playBtn.startAnimation(mFadeInAnimation);
			}
		}

	};

	/**
	 * PlayerEngineListener implementation, manipulates UI
	 */
	private IPlayEngineListener mPlayerEngineListener = new IPlayEngineListener() {

		@Override
		public void onTrackChanged(Playlist playlistEntry) {
			new LicenseTask(playlistEntry.getAlbum(), myAlbum);
			myAlbum = playlistEntry.getAlbum();
			artist_tv.setText(playlistEntry.getAlbum().getArtistName());
			song_tv.setText(playlistEntry.getTrack().getName());
			curTime_tv.setText(Helper.secondsToString(0));
			totalTime_tv.setText(Helper.secondsToString(playlistEntry
					.getTrack().getDuration()));
			mCoverImageView.setImageUrl(playlistEntry.getAlbum().getImage()
					.replaceAll("1.100.jpg", mBetterRes)); // Get higher
			// resolution image
			// 300x300
			mProgressBar.setProgress(0);
			mProgressBar.setMax(playlistEntry.getTrack().getDuration());
			mCoverImageView.performClick();
			if (mRatingBar != null) {
				mRatingBar.setProgress((int) (10 * playlistEntry.getAlbum()
						.getRating()));
				mRatingBar.setMax(10);
			}

			if (getPlayerEngine() != null) {
				if (getPlayerEngine().isPlaying()) {
					playBtn.setImageResource(R.drawable.player_pause_light);
				} else {
					playBtn.setImageResource(R.drawable.player_play_light);
				}
			}
		}

		@Override
		public void onTrackProgress(int seconds) {
			curTime_tv.setText(Helper.secondsToString(seconds));
			mProgressBar.setProgress(seconds);
		}

		@Override
		public void onTrackBuffering(int percent) {
			// int secondaryProgress = (int)
			// (((float)percent/100)*mProgressBar.getMax());
			// mProgressBar.setSecondaryProgress(secondaryProgress);
		}

		@Override
		public void onTrackStop() {
			playBtn.setImageResource(R.drawable.player_play_light);
		}

		@Override
		public boolean onTrackStart() {
			playBtn.setImageResource(R.drawable.player_pause_light);
			return true;
		}

		@Override
		public void onTrackPause() {
			playBtn.setImageResource(R.drawable.player_play_light);
		}

		@Override
		public void onTrackStreamError() {
			Toast.makeText(PlayView.this, R.string.stream_error, 5000).show();
		}

	};

	/**
	 * This creates playlist based on url that was passed in the intent, e.g.
	 * http://www.jamendo.com/pl/track/325654 or
	 * http://www.jamendo.com/pl/album/7505
	 * 
	 * @author Lukasz Wisniewski
	 */
	private class UriLoadingDialog extends LoadDialog<Void, PlayMethod> {

		public UriLoadingDialog(Activity activity, int loadingMsg, int failMsg) {
			super(activity, loadingMsg, failMsg);
		}

		public UriLoadingDialog(Activity activity, String loadingMsg,
				String failMsg) {
			super(activity, loadingMsg, failMsg);
		}

		@Override
		public PlayMethod doInBackground(Void... params) {
			PlayMethod playlist = null;

			Intent intent = getIntent();
			String action = intent.getAction();

			if (Intent.ACTION_VIEW.equals(action)) {
				playlist = new PlayMethod();

				List<String> segments = intent.getData().getPathSegments();
				String mode = segments.get((segments.size() - 2));
				int id = Integer.parseInt(segments.get((segments.size() - 1)));

				IServerApi service = new ServerApiImpl();

				if (mode.equals("track")) {
					try {
						Track[] tracks = service.getTracksByTracksId(
								new int[] { id }, MyApplication.getInstance()
										.getStreamEncoding());
						Album[] albums = service
								.getAlbumsByTracksId(new int[] { id });
						playlist.addTracks(tracks, albums[0]);
					} catch (JSONException e) {
						Log.e(MyApplication.TAG, "sth went completely wrong");
						PlayView.this.finish();
						e.printStackTrace();
					} catch (ErrorMsg e) {
						publishProgress(e);
					}
				}

				if (mode.equals("album")) {
					try {
						Album album = service.getAlbumById(id);
						Track[] tracks = service
								.getAlbumTracks(album, MyApplication
										.getInstance().getStreamEncoding());
						playlist.addTracks(tracks, album);
					} catch (JSONException e) {
						Log.e("jamendroid", "sth went completely wrong");
						PlayView.this.finish();
						e.printStackTrace();
					} catch (ErrorMsg e) {
						publishProgress(e);
					}
				}
			}

			intent.putExtra("handled", true);
			return playlist;
		}

		@Override
		public void doStuffWithResult(PlayMethod result) {
			loadPlaylist(result);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			slidingDrawer.animateToggle();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void albumClickHandler(View target) {
		AlbumView.launch(this, getPlayerEngine().getPlaylist()
				.getSelectedTrack().getAlbum());
	}

	public void artistClickHandler(View target) {
		ArtistActivity.launch(this, getPlayerEngine().getPlaylist()
				.getSelectedTrack().getAlbum().getArtistName());
	}

	public void playlistClickHandler(View target) {
		// PlaylistView.launch(this, false);
	}

	public void homeClickHandler(View target) {
		Intent intent = new Intent(this, MainView.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	public void addOnClick(View v) {
		AddToPlaylistDialog dialog = new AddToPlaylistDialog(PlayView.this);
		dialog.setPlaylistEntry(getPlayerEngine().getPlaylist()
				.getSelectedTrack());
		dialog.show();
		slidingDrawer.animateClose();
	}

	public void lyricsOnClick(View v) {
		Track track = getPlayerEngine().getPlaylist().getSelectedTrack()
				.getTrack();
		new LyricsDialog(PlayView.this, track).show();
		slidingDrawer.animateClose();
	}

	public void downloadOnClick(View v) {
		AlertDialog alertDialog = new AlertDialog.Builder(PlayView.this)
				.setTitle(R.string.download_track_q).setPositiveButton(
						R.string.ok, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								Helper.addToDownloads(PlayView.this,
										getPlayerEngine().getPlaylist()
												.getSelectedTrack());
							}
						}).setNegativeButton(R.string.cancel, null).create();

		alertDialog.show();
		slidingDrawer.animateClose();
	}

	public void shareOnClick(View v) {
		if (playMethod == null || playMethod.getSelectedTrack() == null) {
			return;
		}
		Playlist entry = playMethod.getSelectedTrack();
		Helper.share(PlayView.this, entry);
		slidingDrawer.animateClose();
	}

	License mLicense;

	/**
	 * do background JamendoGet2Api.getAlbumLicense
	 * 
	 * @author Lukasz Wisniewski
	 */
	private class LicenseTask extends AsyncTask<Album, ErrorMsg, License> {

		public LicenseTask(Album newAlbum, Album oldAlbum) {

			boolean runQuery = true;

			if (oldAlbum != null && newAlbum.getId() == oldAlbum.getId()) {
				runQuery = false;
			}

			if (runQuery) {
				this.execute(newAlbum);
			}
		}

		@Override
		protected void onPreExecute() {
			mLicenseImageView.setImageResource(R.drawable.cc_loading);
			super.onPreExecute();
		}

		@Override
		public License doInBackground(Album... params) {

			IServerApi service = new ServerApiImpl();
			try {
				return service.getAlbumLicense(params[0]);
			} catch (ErrorMsg e) {
				return null;
			}
		}

		@Override
		public void onPostExecute(License result) {
			super.onPostExecute(result);
			mLicense = result;

			if (mLicense != null) {
				mLicenseImageView.setImageUrl(mLicense.getImage());
			}
		}

		@Override
		protected void onProgressUpdate(ErrorMsg... values) {
			Toast.makeText(PlayView.this, values[0].getMessage(), 5000).show();
			super.onProgressUpdate(values);
		}

	};

	public void licenseClickHandler(View v) {
		if (mLicense != null) {
			Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mLicense
					.getUrl()));
			startActivity(myIntent);
		}
	}

	// 抽屉盒子里面的控件单击事件
	public class CupcakeListener implements OnClickListener {

		public CupcakeListener() {
			// 点击图标
			findViewById(R.id.SliderHome).setOnClickListener(this);
			findViewById(R.id.SliderAlbum).setOnClickListener(this);
			findViewById(R.id.SliderArtist).setOnClickListener(this);
			findViewById(R.id.SliderPlaylist).setOnClickListener(this);

			// 点击按钮
			findViewById(R.id.SliderLyrics).setOnClickListener(this);
			findViewById(R.id.SliderAddToPlaylist).setOnClickListener(this);
			findViewById(R.id.SliderShare).setOnClickListener(this);
			findViewById(R.id.SliderDownload).setOnClickListener(this);

			// license
			if (mLicenseImageView != null) {
				mLicenseImageView.setOnClickListener(this);
			}
		}

		public void onClick(View v) {
			switch (v.getId()) {
			// icons
			case R.id.SliderHome:
				homeClickHandler(v);
				break;
			case R.id.SliderAlbum:
				albumClickHandler(v);
				break;
			case R.id.SliderArtist:
				artistClickHandler(v);
				break;
			case R.id.SliderPlaylist:
				playlistClickHandler(v);
				break;
			// buttons
			case R.id.SliderLyrics:
				lyricsOnClick(v);
				break;
			case R.id.SliderAddToPlaylist:
				addOnClick(v);
				break;
			case R.id.SliderShare:
				shareOnClick(v);
				break;
			case R.id.SliderDownload:
				downloadOnClick(v);
				break;
			// license
			case R.id.LicenseImageView:
				licenseClickHandler(v);
				break;
			}

		}

	}

	private void initView() {
		// XML binding
		mBetterRes = getResources().getString(R.string.better_res);
		artist_tv = (TextView) findViewById(R.id.ArtistTextView);
		song_tv = (TextView) findViewById(R.id.SongTextView);
		curTime_tv = (TextView) findViewById(R.id.CurrentTimeTextView);
		totalTime_tv = (TextView) findViewById(R.id.TotalTimeTextView);
		mRatingBar = (RatingBar) findViewById(R.id.TrackRowRatingBar);

		mCoverImageView = (MyImageView) findViewById(R.id.CoverImageView);
		mCoverImageView.setOnClickListener(mCoverOnClickListener);
		mCoverImageView.setDefaultImage(R.drawable.no_cd_300);

		mProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);

		mReflectableLayout = (ReflectableLayout) findViewById(R.id.ReflectableLayout);
		mReflectiveSurface = (ReflectiveSurface) findViewById(R.id.ReflectiveSurface);

		if (mReflectableLayout != null && mReflectiveSurface != null) {
			mReflectableLayout.setReflectiveSurface(mReflectiveSurface);
			mReflectiveSurface.setReflectableLayout(mReflectableLayout);
		}
		playBtn = (ImageButton) findViewById(R.id.PlayImageButton);
		playBtn.setOnClickListener(this);

		nextBtn = (ImageButton) findViewById(R.id.NextImageButton);
		nextBtn.setOnClickListener(this);

		preBtn = (ImageButton) findViewById(R.id.PrevImageButton);
		preBtn.setOnClickListener(this);

		stopBtn = (ImageButton) findViewById(R.id.StopImageButton);
		stopBtn.setOnClickListener(this);
		mFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		mFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		mLicenseImageView = (MyImageView) findViewById(R.id.LicenseImageView);
		slidingDrawer = (SlidingDrawer) findViewById(R.id.drawer);
		mFadeInAnimation.setAnimationListener(new AnimationListener() {
			public void onAnimationEnd(Animation animation) {
				new Handler().postDelayed(new Runnable() {
					public void run() {
						if (mFadeInAnimation.hasEnded())
							playBtn.startAnimation(mFadeOutAnimation);
					}

				}, 7500);
			}

			public void onAnimationRepeat(Animation animation) {
				// nothing here
			}

			public void onAnimationStart(Animation animation) {
				showMediaBtn();
			}

		});

		mFadeOutAnimation.setAnimationListener(new AnimationListener() {
			public void onAnimationEnd(Animation animation) {
				setMediaGone();
			}

			public void onAnimationRepeat(Animation animation) {
				// nothing here
			}

			public void onAnimationStart(Animation animation) {
				setFadeOutAnimation();
			}

		});
	}

	// 播放按钮事件
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.PlayImageButton:
			// 播放
		{
			if (getPlayerEngine().isPlaying()) {
				getPlayerEngine().pause();
			} else {
				getPlayerEngine().play();
			}

		}
			break;
		case R.id.NextImageButton:
			// 下一首
		{
			getPlayerEngine().next();

		}
			break;
		case R.id.PrevImageButton:
			// 上一首
		{
			getPlayerEngine().prev();

		}
			break;
		case R.id.StopImageButton:
			// 停止
		{
			getPlayerEngine().stop();

		}
			break;
		default:
			break;
		}
	}

	public static void launch(Context c, PlayMethod playlist) {
		Intent intent = new Intent(c, PlayView.class);
		intent.putExtra("playlist", playlist);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		c.startActivity(intent);
	}

	public static void launch(Activity c, PlaylistRemote playlistRemote) {
		Intent intent = new Intent(c, PlayView.class);
		new PlaylistRemoteLoadDialog(c, "正在努力加载", "加载失败", intent)
				.execute(playlistRemote);
	}

	public static void launch(Activity activity, Album albumModal) {
		new PlayAlbumLoadDialog(activity, "正在努力加载", "加载失败").execute(albumModal);
	}

}
