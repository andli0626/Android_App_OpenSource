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

import com.teleca.jamendo.activity.artist.ArtistActivity;
import com.teleca.jamendo.api.IServerApi;
import com.teleca.jamendo.api_impl.ServerApiImpl;
import com.teleca.jamendo.model.Album;
import com.teleca.jamendo.model.Artist;
import com.teleca.jamendo.model.ErrorMsg;

/**
 * pre-ArtistActivity loading dialog
 * 
 * @author Lukasz Wisniewski
 */
public class ArtistLoadDialog extends LoadDialog<String, Artist>{

	public ArtistLoadDialog(Activity activity, int loadingMsg,
			int failMsg) {
		super(activity, loadingMsg, failMsg);
	}
	
	/**
	 * Artist discography
	 */
	Album[] mAlbums = null; 

	@Override
	public Artist doInBackground(String... params) {
		IServerApi jamendoGet2Api = new ServerApiImpl();
		Artist artist = null;
		try {
			artist = jamendoGet2Api.getArtist(params[0]);
			mAlbums =  jamendoGet2Api.searchForAlbumsByArtistName(params[0]);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		} catch (ErrorMsg e) {
			publishProgress(e);
			this.cancel(true);
		}
		return artist;
	}

	@Override
	public void doStuffWithResult(Artist artist) {
		Intent intent = new Intent(mActivity, ArtistActivity.class);
		intent.putExtra("artist", artist);
		ArrayList<Album> albums = new ArrayList<Album>();
		for(Album album : mAlbums){
			albums.add(album);
		}
		intent.putExtra("albums", albums);
		mActivity.startActivity(intent);
	}
	
}
