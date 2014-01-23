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

import com.teleca.jamendo.activity.album.StarredAlbumsView;
import com.teleca.jamendo.api.IServerApi;
import com.teleca.jamendo.api_impl.ServerApiImpl;
import com.teleca.jamendo.model.Album;
import com.teleca.jamendo.model.ErrorMsg;

import android.app.Activity;
import android.content.Intent;

public class StarredAlbumLoadDialog extends
		LoadDialog<String, Album[]> {

	public StarredAlbumLoadDialog(Activity activity, int loadingMsg, int failMsg) {
		super(activity, loadingMsg, failMsg);
	}

	@Override
	public Album[] doInBackground(String... params) {
		String user = params[0];
		IServerApi service = new ServerApiImpl();
		try {
			return service.getUserStarredAlbums(user);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ErrorMsg e) {
			publishProgress(e);
			cancel(true);
		}
		return null;
	}

	@Override
	public void doStuffWithResult(Album[] result) {
		Intent intent = new Intent(mActivity, StarredAlbumsView.class);
		ArrayList<Album> list = new ArrayList<Album>();
		for(Album a : result)
			list.add(a);
		intent.putExtra("albums", list);
		mActivity.startActivity(intent);
	}

}
