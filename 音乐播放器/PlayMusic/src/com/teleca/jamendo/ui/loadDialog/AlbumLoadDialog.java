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

package com.teleca.jamendo.ui.loadDialog;

import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;

import com.teleca.jamendo.MyApplication;
import com.teleca.jamendo.activity.album.AlbumView;
import com.teleca.jamendo.api.IServerApi;
import com.teleca.jamendo.api_impl.ServerApiImpl;
import com.teleca.jamendo.model.Album;
import com.teleca.jamendo.model.Review;
import com.teleca.jamendo.model.Track;
import com.teleca.jamendo.model.ErrorMsg;

/**
 * pre-AlbumActivity loading (gets Tracks and Reviews)
 * 
 * @author Lukasz Wisniewski
 */
public class AlbumLoadDialog extends LoadDialog<Album, Integer>{
	
	Review[] mReviews;
	Track[] mTracks;
	Album mAlbum;
	
	public AlbumLoadDialog(Activity activity, int loadingMsg, int failMsg) {
		super(activity, loadingMsg, failMsg);
	}
	

	@Override
	public Integer doInBackground(Album... params) {
		mAlbum = params[0];
		try {
			loadReviews(mAlbum);
			loadTracks(mAlbum);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		} catch (ErrorMsg e) {
			publishProgress(e);
			this.cancel(true);
		}
		return 1;
	}

	@Override
	public void doStuffWithResult(Integer result) {
		
		ArrayList<Review> reviews = new ArrayList<Review>();
		ArrayList<Track> tracks = new ArrayList<Track>();
		
		for(Track track : mTracks)
			tracks.add(track);
		for(Review review : mReviews)
			reviews.add(review);
		
		Intent intent = new Intent(mActivity, AlbumView.class);
		intent.putExtra("album", mAlbum);
		intent.putExtra("reviews", reviews);
		intent.putExtra("tracks", tracks);
		mActivity.startActivity(intent);
		
	}

	private void loadReviews(Album album) throws JSONException, ErrorMsg {
		IServerApi server = new ServerApiImpl();
		mReviews = server.getAlbumReviews(album);
	}
	
	private void loadTracks(Album album) throws JSONException, ErrorMsg{
		IServerApi service = new ServerApiImpl();
		mTracks = service.getAlbumTracks(album, MyApplication.getInstance().getStreamEncoding());
	}
}
