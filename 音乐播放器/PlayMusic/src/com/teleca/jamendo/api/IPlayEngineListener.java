package com.teleca.jamendo.api;

import com.teleca.jamendo.model.Playlist;

public interface IPlayEngineListener {
	public boolean onTrackStart();

	public void onTrackChanged(Playlist playlistEntry);

	public void onTrackProgress(int seconds);

	public void onTrackBuffering(int percent);

	public void onTrackStop();

	public void onTrackPause();

	public void onTrackStreamError();

}
