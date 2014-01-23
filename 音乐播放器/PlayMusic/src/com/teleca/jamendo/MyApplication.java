package com.teleca.jamendo;

import java.io.File;
import java.util.ArrayList;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.teleca.jamendo.activity.playview.PlayMethod;
import com.teleca.jamendo.api.IPlayEngine;
import com.teleca.jamendo.api.IPlayEngineListener;
import com.teleca.jamendo.api.IServerApi;
import com.teleca.jamendo.model.Playlist;
import com.teleca.jamendo.service.DownloadService;
import com.teleca.jamendo.service.PlayerService;
import com.teleca.jamendo.util.Caller;
import com.teleca.jamendo.util.ImgCache;
import com.teleca.jamendo.util.RequestCache;
import com.teleca.jamendo.util.download.IDownloadData;
import com.teleca.jamendo.util.download.DownloadHelper;
import com.teleca.jamendo.util.download.IDownload;
import com.teleca.jamendo.util.download.DownloadJob;

/**
 * Singleton with hooks to Player and Download Service
 * 
 * @author Lukasz Wisniewski
 */
public class MyApplication extends Application {

	/**
	 * Tag used for DDMS logging
	 */
	public static String TAG = "jamendo";

	/**
	 * Singleton pattern
	 */
	private static MyApplication instance;

	/**
	 * Image cache, one for all activities and orientations
	 */
	private ImgCache mImageCache;

	/**
	 * Web request cache, one for all activities and orientations
	 */
	private RequestCache mRequestCache;

	/**
	 * Service player engine
	 */
	private IPlayEngine mServicePlayerEngine;

	/**
	 * Intent player engine
	 */
	private IPlayEngine mIntentPlayerEngine;

	/**
	 * Player engine listener
	 */
	private IPlayEngineListener mPlayerEngineListener;

	/**
	 * Download interface
	 */
	private IDownload mDownloadInterface;

	/**
	 * Stored in Application instance in case we destory Player service
	 */
	private PlayMethod mPlaylist;

	/**
	 * Stores info about this session's finished downloads
	 */
	private ArrayList<DownloadJob> mCompletedDownloads;

	/**
	 * Stores info about this session's downloads that are queued
	 */
	private ArrayList<DownloadJob> mQueuedDownloads;

	public static MyApplication getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mImageCache = new ImgCache();
		mRequestCache = new RequestCache();

		Caller.setRequestCache(mRequestCache);
		instance = this;

		mQueuedDownloads = new ArrayList<DownloadJob>();
		mCompletedDownloads = new ArrayList<DownloadJob>();
	}

	/**
	 * Access to global image cache across Activity instances
	 * 
	 * @return
	 */
	public ImgCache getImageCache() {
		return mImageCache;
	}

	/**
	 * This setter should be only used for setting real player engine interface,
	 * e.g. used to pass Player Service's engine
	 * 
	 * @param playerEngine
	 */
	public void setConcretePlayerEngine(IPlayEngine playerEngine) {
		this.mServicePlayerEngine = playerEngine;
	}

	/**
	 * This getter allows performing logical operations on the player engine's
	 * interface from UI space
	 * 
	 * @return
	 */
	public IPlayEngine getPlayerEngineInterface() {
		// request service bind
		if (mIntentPlayerEngine == null) {
			mIntentPlayerEngine = new IntentPlayerEngine();
		}
		return mIntentPlayerEngine;
	}

	/**
	 * This function allows to add listener to the concrete player engine
	 * 
	 * @param l
	 */
	public void setPlayerEngineListener(IPlayEngineListener l) {
		getPlayerEngineInterface().setListener(l);
	}

	/**
	 * This function is used by PlayerService on ACTION_BIND_LISTENER in order
	 * to get to Application's exposed listener.
	 * 
	 * @return
	 */
	public IPlayEngineListener fetchPlayerEngineListener() {
		return mPlayerEngineListener;
	}

	/**
	 * Returns current playlist, used in PlayerSerive in onStart method
	 * 
	 * @return
	 */
	public PlayMethod fetchPlaylist() {
		return mPlaylist;
	}

	// 获取程序的版本号
	public String getVersion() {
		String version = "0.0.0";
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			version = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}

	public void setDownloadInterface(IDownload downloadInterface) {
		mDownloadInterface = downloadInterface;
	}

	public IDownload getDownloadInterface() {
		if (mDownloadInterface == null) {
			mDownloadInterface = new IntentDownloadInterface();
		}
		return mDownloadInterface;
	}

	public void setQueuedDownloads(ArrayList<DownloadJob> mQueuedDownloads) {
		this.mQueuedDownloads = mQueuedDownloads;
	}

	public ArrayList<DownloadJob> getQueuedDownloads() {
		return mQueuedDownloads;
	}

	public void setCompletedDownloads(ArrayList<DownloadJob> mCompletedDownloads) {
		this.mCompletedDownloads = mCompletedDownloads;
	}

	public ArrayList<DownloadJob> getCompletedDownloads() {
		return mCompletedDownloads;
	}

	/**
	 * Since 0.9.8.7 we embrace "bindless" PlayerService thus this adapter. No
	 * big need of code refactoring, we just wrap sending intents around defined
	 * interface
	 * 
	 * @author Lukasz Wisniewski
	 */
	private class IntentPlayerEngine implements IPlayEngine {

		@Override
		public PlayMethod getPlaylist() {
			return mPlaylist;
		}

		@Override
		public boolean isPlaying() {
			if (mServicePlayerEngine == null) {
				// service does not exist thus no playback possible
				return false;
			} else {
				return mServicePlayerEngine.isPlaying();
			}
		}

		@Override
		public void next() {
			if (mServicePlayerEngine != null) {
				playlistCheck();
				mServicePlayerEngine.next();
			} else {
				startAction(PlayerService.ACTION_NEXT);
			}
		}

		@Override
		public void openPlaylist(PlayMethod playlist) {
			mPlaylist = playlist;
			if (mServicePlayerEngine != null) {
				mServicePlayerEngine.openPlaylist(playlist);
			}
		}

		@Override
		public void pause() {
			if (mServicePlayerEngine != null) {
				mServicePlayerEngine.pause();
			}
		}

		@Override
		public void play() {
			if (mServicePlayerEngine != null) {
				playlistCheck();
				mServicePlayerEngine.play();
			} else {
				startAction(PlayerService.ACTION_PLAY);
			}
		}

		@Override
		public void prev() {
			if (mServicePlayerEngine != null) {
				playlistCheck();
				mServicePlayerEngine.prev();
			} else {
				startAction(PlayerService.ACTION_PREV);
			}
		}

		@Override
		public void setListener(IPlayEngineListener playerEngineListener) {
			mPlayerEngineListener = playerEngineListener;
			// we do not want to set this listener if Service
			// is not up and a new listener is null
			if (mServicePlayerEngine != null || mPlayerEngineListener != null) {
				startAction(PlayerService.ACTION_BIND_LISTENER);
			}
		}

		@Override
		public void skipTo(int index) {
			if (mServicePlayerEngine != null) {
				mServicePlayerEngine.skipTo(index);
			}
		}

		@Override
		public void stop() {
			startAction(PlayerService.ACTION_STOP);
			// stopService(new Intent(JamendoApplication.this,
			// PlayerService.class));
		}

		private void startAction(String action) {
			Intent intent = new Intent(MyApplication.this, PlayerService.class);
			intent.setAction(action);
			startService(intent);
		}

		/**
		 * This is required if Player Service was binded but playlist was not
		 * passed from Application to Service and one of buttons: play, next,
		 * prev is pressed
		 */
		private void playlistCheck() {
			if (mServicePlayerEngine != null) {
				if (mServicePlayerEngine.getPlaylist() == null
						&& mPlaylist != null) {
					mServicePlayerEngine.openPlaylist(mPlaylist);
				}
			}
		}

	}

	private class IntentDownloadInterface implements IDownload {

		@Override
		public void addToDownloadQueue(Playlist entry) {
			Intent intent = new Intent(MyApplication.this,
					DownloadService.class);
			intent.setAction(DownloadService.ACTION_ADD_TO_DOWNLOAD);
			intent.putExtra(DownloadService.EXTRA_PLAYLIST_ENTRY, entry);
			startService(intent);
		}

		@Override
		public ArrayList<DownloadJob> getAllDownloads() {
			ArrayList<DownloadJob> allDownloads = new ArrayList<DownloadJob>();
			allDownloads.addAll(mCompletedDownloads);
			allDownloads.addAll(mQueuedDownloads);
			return allDownloads;
		}

		@Override
		public ArrayList<DownloadJob> getCompletedDownloads() {
			return mCompletedDownloads;
		}

		@Override
		public ArrayList<DownloadJob> getQueuedDownloads() {
			return mQueuedDownloads;
		}

		@Override
		public String getTrackPath(Playlist entry) {
			if (entry == null) {
				return null;
			}

			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {

				// we need to check the database to be sure whether file was
				// downloaded completely
				IDownloadData downloadDatabase = DownloadService
						.getDownloadDatabase();
				if (downloadDatabase != null) {
					if (!downloadDatabase.trackAvailable(entry.getTrack()))
						return null;
				}

				// now we may give a reference to this file after we check it
				// really exists
				// in case file was somehow removed manually
				String trackPath = DownloadHelper.getAbsolutePath(entry,
						DownloadService.getDownloadPath());
				String fileNameMP3 = DownloadHelper.getFileName(entry,
						IServerApi.ENCODING_MP3);
				File fileMP3 = new File(trackPath, fileNameMP3);
				if (fileMP3.exists()) {
					String path = fileMP3.getAbsolutePath();
					Log.i(TAG, "Playing from local file: " + fileMP3);
					return path;
				}
				String fileNameOGG = DownloadHelper.getFileName(entry,
						IServerApi.ENCODING_OGG);
				File fileOGG = new File(trackPath, fileNameOGG);
				if (fileOGG.exists()) {
					String path = fileOGG.getAbsolutePath();
					Log.i(TAG, "Playing from local file: " + fileOGG);
					return path;
				}
			}
			return null;
		}

	}

	public String getDownloadFormat() {
		return PreferenceManager.getDefaultSharedPreferences(this).getString(
				"download_format", IServerApi.ENCODING_MP3);
	}

	public String getStreamEncoding() {
		// http://groups.google.com/group/android-developers/msg/c546760177b22197
		// According to JBQ: ogg files are supported but not streamable
		return IServerApi.ENCODING_MP3;
	}

}
