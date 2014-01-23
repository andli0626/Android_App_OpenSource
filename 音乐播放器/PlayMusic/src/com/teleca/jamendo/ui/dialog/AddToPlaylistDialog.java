
package com.teleca.jamendo.ui.dialog;

import java.util.ArrayList;

import com.teleca.jamendo.activity.playview.PlayMethod;
import com.teleca.jamendo.api.IDatabase;
import com.teleca.jamendo.api_impl.DatabaseImpl;
import com.teleca.jamendo.model.Album;
import com.teleca.jamendo.model.Playlist;
import com.teleca.jamendo.model.Track;
import com.teleca.jamendo.R;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Allows adding track/album to a new playlist or a selected one from the list
 * 
 * @author Lukasz Wisniewski
 */
public class AddToPlaylistDialog extends Dialog {
	
	private Activity mActivity;
	
	private ListView mListView;
	private EditText mEditText;
	private Button mButton;
	
	private Playlist mPlaylistEntry;
	private Album mAlbum;
	private Track[] mTracks;
	
	private IDatabase mDatabase;

	public AddToPlaylistDialog(Activity context) {
		super(context);
		init(context);
	}

	public AddToPlaylistDialog(Activity context, int theme) {
		super(context, theme);
		init(context);
	}

	public AddToPlaylistDialog(Activity context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(context);
	}
	
	/**
	 * Sharable code between constructors
	 */
	private void init(Activity context){
		mActivity = context;
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_to_playlist);

		mDatabase = new DatabaseImpl(mActivity);
		ArrayList<String> availablePlaylistsAL = mDatabase.getAvailablePlaylists();
		String[] availablePlaylists = new String[availablePlaylistsAL.size()];
		availablePlaylistsAL.toArray(availablePlaylists);
		
		mListView = (ListView)findViewById(R.id.PlaylistListView);
		mListView.setAdapter(new ArrayAdapter<String>(this.getContext(),
                android.R.layout.simple_list_item_1, availablePlaylists));
		mListView.setOnItemClickListener(mPlaylistItemClick);
		
		mButton = (Button)findViewById(R.id.PlaylistNewButton);
		mButton.setOnClickListener(mButtonClick);
		
		mEditText = (EditText)findViewById(R.id.PlaylistEditText);
	}
	
	public void setPlaylistEntry(Playlist mPlaylistEntry) {
		this.mPlaylistEntry = mPlaylistEntry;
	}
	
	public void setPlaylistAlbum(Track[] tracks, Album album) {
		this.mAlbum = album;
		this.mTracks = tracks;
	}

	public Playlist getPlaylistEntry() {
		return mPlaylistEntry;
	}

	private android.view.View.OnClickListener mButtonClick = new android.view.View.OnClickListener(){

		@Override
		public void onClick(View v) {
			String playlistName = mEditText.getText().toString();
			addToPlaylist(playlistName);
		}
		
	};
	
	private OnItemClickListener mPlaylistItemClick = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position,
				long time) {
			String playlistName = (String)adapterView.getAdapter().getItem(position);
			addToPlaylist(playlistName);
		}
		
	};
	
	private void addToPlaylist(String playlistName){
		PlayMethod playlist = mDatabase.loadPlaylist(playlistName);
		if(playlist == null){
			playlist = new PlayMethod();
		}
		
		if(getPlaylistEntry() != null)
			playlist.addPlaylistEntry(getPlaylistEntry());
		
		if(mAlbum != null && mTracks != null)
			playlist.addTracks(mTracks, mAlbum);
		
		if(playlistName.length() == 0 || playlistName.startsWith(" "))
			return;
		
		mDatabase.savePlaylist(playlist, playlistName);
		
		Toast.makeText(AddToPlaylistDialog.this.getContext(), R.string.added_to_playlist, Toast.LENGTH_SHORT).show();
		AddToPlaylistDialog.this.cancel();
	}

}
