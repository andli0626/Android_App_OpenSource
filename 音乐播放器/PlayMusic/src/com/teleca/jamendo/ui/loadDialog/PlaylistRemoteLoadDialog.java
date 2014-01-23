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

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;

import com.teleca.jamendo.activity.playview.PlayMethod;
import com.teleca.jamendo.api.IServerApi;
import com.teleca.jamendo.api_impl.ServerApiImpl;
import com.teleca.jamendo.model.PlaylistRemote;
import com.teleca.jamendo.model.ErrorMsg;

/**
 * pre-Player playlist loading dialog
 * 
 * @author Łukasz Wiśniewski
 */
public class PlaylistRemoteLoadDialog extends
		LoadDialog<PlaylistRemote, PlayMethod> {

	private Intent mIntent;

	public PlaylistRemoteLoadDialog(Activity activity, int loadingMsg,
			int failMsg, Intent intent) {
		super(activity, loadingMsg, failMsg);
		mIntent = intent;
	}

	public PlaylistRemoteLoadDialog(Activity activity, String loadingMsg,
			String failMsg, Intent intent) {
		super(activity, loadingMsg, failMsg);
		mIntent = intent;
	}

	@Override
	public PlayMethod doInBackground(PlaylistRemote... params) {

		IServerApi service = new ServerApiImpl();
		PlayMethod playlist = null;
		try {
			playlist = service.getPlaylist(params[0]);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		} catch (ErrorMsg e) {
			publishProgress(e);
			cancel(true);
		}
		return playlist;
	}

	@Override
	public void doStuffWithResult(PlayMethod playlist) {
		mIntent.putExtra("playlist", playlist);
		mActivity.startActivity(mIntent);
	}

}
