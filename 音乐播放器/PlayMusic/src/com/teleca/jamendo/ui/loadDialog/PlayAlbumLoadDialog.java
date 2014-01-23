package com.teleca.jamendo.ui.loadDialog;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;

import com.teleca.jamendo.MyApplication;
import com.teleca.jamendo.activity.playview.PlayView;
import com.teleca.jamendo.activity.playview.PlayMethod;
import com.teleca.jamendo.api.IServerApi;
import com.teleca.jamendo.api_impl.ServerApiImpl;
import com.teleca.jamendo.model.Album;
import com.teleca.jamendo.model.Track;
import com.teleca.jamendo.model.ErrorMsg;

/**
 * 加载播放界面对话框
 * 
 * @author lilin
 * @date 2012-1-6 下午09:36:40
 * @ClassName: PlayerAlbumLoadingDialog
 */
public class PlayAlbumLoadDialog extends
		LoadDialog<Album, Track[]> {

	private Album mAlbum;

	public PlayAlbumLoadDialog(Activity activity, String loadingMsg,
			String failMsg) {
		super(activity, loadingMsg, failMsg);
	}

	@Override
	public Track[] doInBackground(Album... params) {
		mAlbum = params[0];

		IServerApi service = new ServerApiImpl();
		Track[] tracks = null;

		try {
			tracks = service.getAlbumTracks(mAlbum, MyApplication.getInstance()
					.getStreamEncoding());
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		} catch (ErrorMsg e) {
			publishProgress(e);
			cancel(true);
		}
		return tracks;

	}

	@Override
	public void doStuffWithResult(Track[] tracks) {

		Intent intent = new Intent(mActivity, PlayView.class);
		PlayMethod playlist = new PlayMethod();
		playlist.addTracks(tracks, mAlbum);

		intent.putExtra("playlist", playlist);
		mActivity.startActivity(intent);
	}

}
