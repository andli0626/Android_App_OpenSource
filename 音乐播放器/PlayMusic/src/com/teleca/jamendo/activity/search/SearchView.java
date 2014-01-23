package com.teleca.jamendo.activity.search;

import java.util.ArrayList;

import org.json.JSONException;

import com.teleca.jamendo.activity.playview.PlayView;
import com.teleca.jamendo.adapter.AlbumAdp;
import com.teleca.jamendo.adapter.PlaylistRemoteAdp;
import com.teleca.jamendo.api.IServerApi;
import com.teleca.jamendo.api_impl.ServerApiImpl;
import com.teleca.jamendo.model.Album;
import com.teleca.jamendo.model.PlaylistRemote;
import com.teleca.jamendo.model.ErrorMsg;
import com.teleca.jamendo.ui.loadDialog.LoadDialog;
import com.teleca.jamendo.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 搜索界面
 * 
 * @author lilin
 * @date 2012-1-6 下午09:19:33
 * @ClassName: SearchView
 */
public class SearchView extends Activity {

	enum SearchMode {
		Artist, Tag, UserPlaylist, UserStarredAlbums
	};

	private Spinner mSearchSpinner;
	private ListView mSearchListView;
	private EditText mSearchEditText;
	private Button mSearchButton;
	private ViewFlipper mViewFlipper;

	private PlaylistRemote[] mPlaylistRemotes = null;

	private SearchMode mSearchMode;

	public static void launch(Context c) {
		Intent intent = new Intent(c, SearchView.class);
		c.startActivity(intent);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.searchview);

		mSearchSpinner = (Spinner) findViewById(R.id.SearchSpinner);
		ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
				R.array.search_modes, android.R.layout.simple_spinner_item);
		adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSearchSpinner.setAdapter(adapter);

		mSearchButton = (Button) findViewById(R.id.SearchButton);
		mSearchButton.setOnClickListener(mSearchButtonListener);

		mSearchEditText = (EditText) findViewById(R.id.SearchEditText);
		mSearchListView = (ListView) findViewById(R.id.SearchListView);

		mViewFlipper = (ViewFlipper) findViewById(R.id.SearchViewFlipper);
		if (mSearchListView.getCount() == 0) {
			mViewFlipper.setDisplayedChild(2); // search list hint
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		mSearchMode = (SearchMode) savedInstanceState.getSerializable("mode");
		if (mSearchMode != null) {
			if (mSearchMode.equals(SearchMode.Artist)
					|| mSearchMode.equals(SearchMode.Tag)
					|| mSearchMode.equals(SearchMode.UserStarredAlbums)) {
				AlbumAdp adapter = new AlbumAdp(this);
				adapter.setList((ArrayList<Album>) savedInstanceState
						.get("values"));
				mSearchListView.setAdapter(adapter);
				mSearchListView.setOnItemClickListener(mAlbumClickListener);
			}

			if (mSearchMode.equals(SearchMode.UserPlaylist)) {
				PlaylistRemoteAdp adapter = new PlaylistRemoteAdp(this);
				adapter.setList((ArrayList<PlaylistRemote>) savedInstanceState
						.get("values"));
				mSearchListView.setAdapter(adapter);
				mSearchListView.setOnItemClickListener(mPlaylistClickListener);
			}

			mViewFlipper.setDisplayedChild(savedInstanceState
					.getInt("flipper_page"));
		}
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (mSearchMode != null) {
			outState.putSerializable("mode", mSearchMode);
			if (mSearchMode.equals(SearchMode.Artist)
					|| mSearchMode.equals(SearchMode.Tag)
					|| mSearchMode.equals(SearchMode.UserStarredAlbums)) {
				AlbumAdp adapter = (AlbumAdp) mSearchListView.getAdapter();
				outState.putSerializable("values", adapter.getList());
			}

			if (mSearchMode.equals(SearchMode.UserPlaylist)) {
				PlaylistRemoteAdp adapter = (PlaylistRemoteAdp) mSearchListView
						.getAdapter();
				outState.putSerializable("values", adapter.getList());
			}

			outState.putInt("flipper_page", mViewFlipper.getDisplayedChild());
		}
		super.onSaveInstanceState(outState);
	}

	private OnClickListener mSearchButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			new SearchingDialog(SearchView.this, R.string.searching,
					R.string.search_fail).execute(mSearchSpinner
					.getSelectedItemPosition());
		}

	};

	private OnItemClickListener mAlbumClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long time) {
			Album album = (Album) adapterView.getItemAtPosition(position);
			PlayView.launch(SearchView.this, album);
		}

	};

	private OnItemClickListener mPlaylistClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapterView, View arg1,
				int position, long arg3) {
			PlaylistRemote playlistRemote = (PlaylistRemote) adapterView
					.getItemAtPosition(position);
			PlayView.launch(SearchView.this, playlistRemote);
		}

	};

	/**
	 * Allows cancelling search query
	 * 
	 * @author Lukasz Wisniewski
	 */
	private class SearchingDialog extends LoadDialog<Integer, Integer> {

		private Integer mSearchMode;
		private BaseAdapter mAdapter;

		public SearchingDialog(Activity activity, int loadingMsg, int failMsg) {
			super(activity, loadingMsg, failMsg);
		}

		@Override
		public Integer doInBackground(Integer... params) {
			mSearchMode = params[0];
			switch (mSearchMode) {
			case 0:
				// artist search
				albumSearch(0);
				break;
			case 1:
				// tag search
				albumSearch(1);
				break;
			case 2:
				// playlist search
				playlistSearch();
				break;
			case 3:
				// starred album search
				albumSearch(3);
				break;
			default:
			}
			return mSearchMode;
		}

		@Override
		public void doStuffWithResult(Integer result) {
			mSearchListView.setAdapter(mAdapter);

			if (mSearchListView.getCount() > 0) {
				mViewFlipper.setDisplayedChild(0); // display results
			} else {
				mViewFlipper.setDisplayedChild(1); // display no results message
			}

			// results are albums
			if (mSearchMode.equals(0) || mSearchMode.equals(1)
					|| mSearchMode.equals(3)) {
				mSearchListView.setOnItemClickListener(mAlbumClickListener);
			}

			// results are playlists
			if (mSearchMode.equals(2)) {
				mSearchListView.setOnItemClickListener(mPlaylistClickListener);
			}
		}

		private void albumSearch(int id) {
			IServerApi service = new ServerApiImpl();
			String query = mSearchEditText.getText().toString();
			Album[] albums = null;
			try {
				switch (id) {
				case 0:
					albums = service.searchForAlbumsByArtist(query);
					SearchView.this.mSearchMode = SearchMode.Artist;
					break;
				case 1:
					albums = service.searchForAlbumsByTag(query);
					SearchView.this.mSearchMode = SearchMode.Tag;
					break;
				case 3:
					albums = service.getUserStarredAlbums(query);
					SearchView.this.mSearchMode = SearchMode.UserStarredAlbums;
					break;

				default:
					return;
				}

				AlbumAdp albumAdapter = new AlbumAdp(SearchView.this);
				albumAdapter.setList(albums);
				albumAdapter.setListView(mSearchListView);
				mAdapter = albumAdapter;

			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ErrorMsg e) {
				publishProgress(e);
				this.cancel(true);
			}
		}

		private void playlistSearch() {
			IServerApi service = new ServerApiImpl();
			String user = mSearchEditText.getText().toString();
			try {
				mPlaylistRemotes = service.getUserPlaylist(user);
				if (mPlaylistRemotes != null) {
					PlaylistRemoteAdp purpleAdapter = new PlaylistRemoteAdp(
							SearchView.this);
					purpleAdapter.setList(mPlaylistRemotes);
					mAdapter = purpleAdapter;
					SearchView.this.mSearchMode = SearchMode.UserPlaylist;
				}

			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ErrorMsg e) {
				publishProgress(e);
				this.cancel(true);
			}
		}

	}

}
