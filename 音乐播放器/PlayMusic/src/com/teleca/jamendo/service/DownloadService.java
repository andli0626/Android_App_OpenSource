/*
 * Copyright (C) 2009 Teleca Poland Sp. z o.o. <android@teleca.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.teleca.jamendo.service;

import java.util.ArrayList;

import com.teleca.jamendo.MyApplication;
import com.teleca.jamendo.activity.download.DownloadView;
import com.teleca.jamendo.model.Playlist;
import com.teleca.jamendo.util.download.IDownloadData;
import com.teleca.jamendo.util.download.DownloadDataImpl;
import com.teleca.jamendo.util.download.DownloadJob;
import com.teleca.jamendo.util.download.IDownloadJobListener;
import com.teleca.jamendo.util.download.MediaScannerNotifier;
import com.teleca.jamendo.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

// TODO sd card listener
/**
 * Background download manager
 * 
 * @author Lukasz Wisniewski
 */
public class DownloadService extends Service {
	
	public static final String ACTION_ADD_TO_DOWNLOAD = "add_to_download";
	
	public static final String EXTRA_PLAYLIST_ENTRY = "playlist_entry";

	private static final int DOWNLOAD_NOTIFY_ID = 667668;
	
	private NotificationManager mNotificationManager = null;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate(){
		super.onCreate();
		Log.i(MyApplication.TAG, "DownloadService.onCreate");
		
		// TODO check download database for any not downloaded things, add the to downloadQueue and start
		mNotificationManager = ( NotificationManager ) getSystemService( NOTIFICATION_SERVICE );
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
		if(intent == null){
			return;
		}
		
		String action = intent.getAction();
		Log.i(MyApplication.TAG, "DownloadService.onStart - "+action);
		
		if(action.equals(ACTION_ADD_TO_DOWNLOAD)){
			Playlist entry = (Playlist) intent.getSerializableExtra(EXTRA_PLAYLIST_ENTRY);
			addToDownloadQueue(entry, startId);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(MyApplication.TAG, "DownloadService.onDestroy");
	}

	private IDownloadJobListener mDownloadJobListener = new IDownloadJobListener(){

		@Override
		public void downloadEnded(DownloadJob job) {
			getQueuedDownloads().remove(job);
			getCompletedDownloads().add(job);
			IDownloadData downloadDatabase = getDownloadDatabase();
			if(downloadDatabase != null){
				downloadDatabase.setStatus(job.getPlaylistEntry(), true);
			}
			displayNotifcation(job);
			new MediaScannerNotifier(DownloadService.this, job);
		}

		@Override
		public void downloadStarted() {
		}

	};
	
	private void displayNotifcation(DownloadJob job)
	{

		String notificationMessage = job.getPlaylistEntry().getTrack().getName() + " - " + job.getPlaylistEntry().getAlbum().getArtistName();

		Notification notification = new Notification(
				android.R.drawable.stat_sys_download_done, notificationMessage, System.currentTimeMillis() );

		PendingIntent contentIntent = PendingIntent.getActivity( this, 0,
				new Intent( this, DownloadView.class ), 0);

		notification.setLatestEventInfo( this, getString(R.string.downloaded),
				notificationMessage, contentIntent );
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		mNotificationManager.notify( DOWNLOAD_NOTIFY_ID, notification );
	}
	
	/**
	 * Interface to database on the remote storage device
	 */
	public static IDownloadData getDownloadDatabase(){
		return new DownloadDataImpl(getDownloadPath()+"/jamendroid.db");
	}

	public static String getDownloadPath(){
		return Environment.getExternalStorageDirectory().getAbsolutePath()+"/music";
	}
	
	public void addToDownloadQueue(Playlist entry, int startId) {
		
		// check database if record already exists, if so abandon starting
		// another download process
		String downloadPath = getDownloadPath();
		
		String downloadFormat = MyApplication.getInstance().getDownloadFormat(); 
		
		DownloadJob downloadJob = new DownloadJob(entry, downloadPath, startId, downloadFormat);
		
		IDownloadData downloadDatabase = getDownloadDatabase();
		if(downloadDatabase != null){
			boolean existst = downloadDatabase.addToLibrary(downloadJob.getPlaylistEntry());
			if(existst)
				return;
		}
		
		downloadJob.setListener(mDownloadJobListener);	
		getQueuedDownloads().add(downloadJob);
		downloadJob.start();
	}
	
	public ArrayList<DownloadJob> getQueuedDownloads(){
		return MyApplication.getInstance().getQueuedDownloads();
	}
	
	public ArrayList<DownloadJob> getCompletedDownloads(){
		return MyApplication.getInstance().getCompletedDownloads();
	}

}
