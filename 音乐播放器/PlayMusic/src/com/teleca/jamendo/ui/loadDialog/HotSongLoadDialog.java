package com.teleca.jamendo.ui.loadDialog;

import java.util.Hashtable;

import org.json.JSONException;

import android.app.Activity;

import com.teleca.jamendo.MyApplication;
import com.teleca.jamendo.activity.playview.PlayView;
import com.teleca.jamendo.activity.playview.PlayMethod;
import com.teleca.jamendo.api.IServerApi;
import com.teleca.jamendo.api_impl.ServerApiImpl;
import com.teleca.jamendo.model.Album;
import com.teleca.jamendo.model.ErrorMsg;
import com.teleca.jamendo.model.Playlist;
import com.teleca.jamendo.model.Track;

/**
 * 热门歌曲加载对话框
 * 
 * @author lilin
 * @date 2012-1-6 下午10:35:02
 * @ClassName: HotSongLoadDialog
 */
public class HotSongLoadDialog extends LoadDialog<Void, PlayMethod> {

	public Activity activity;

	// 构造方法1
	public HotSongLoadDialog(Activity activity, int loadingMsg, int failMsg) {
		super(activity, loadingMsg, failMsg);
		this.activity = activity;
	}

	// 构造方法2
	public HotSongLoadDialog(Activity activity, String loadingMsg,
			String failMsg) {
		super(activity, loadingMsg, failMsg);
		this.activity = activity;
	}

	public PlayMethod doInBackground(Void... params) {
		IServerApi server = new ServerApiImpl();
		int[] id = null;
		try {
			id = server.getTop100Listened();
			Album[] albums = server.getAlbumsByTracksId(id);
			Track[] tracks = server.getTracksByTracksId(id, MyApplication
					.getInstance().getStreamEncoding());
			if (albums == null || tracks == null)
				return null;
			Hashtable<Integer, Playlist> hashtable = new Hashtable<Integer, Playlist>();
			for (int i = 0; i < tracks.length; i++) {
				Playlist playlistEntry = new Playlist();
				playlistEntry.setAlbum(albums[i]);
				playlistEntry.setTrack(tracks[i]);
				hashtable.put(tracks[i].getId(), playlistEntry);
			}

			// creating playlist in the correct order
			PlayMethod playlist = new PlayMethod();
			for (int i = 0; i < id.length; i++) {
				playlist.addPlaylistEntry(hashtable.get(id[i]));
			}
			return playlist;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ErrorMsg e) {
			publishProgress(e);
		}
		return null;
	}

	public void doStuffWithResult(PlayMethod playlist) {
		if (playlist.size() <= 0) {
			failLoadMsg();
			return;
		}

		PlayView.launch(activity, playlist);
	}

}
