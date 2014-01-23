package com.teleca.jamendo.activity.playview;

import java.io.Serializable;
import java.util.ArrayList;

import com.teleca.jamendo.model.Album;
import com.teleca.jamendo.model.Playlist;
import com.teleca.jamendo.model.Track;

/**
 * 播放方法
 * 
 * @author lilin
 * @date 2012-1-9 下午06:45:02
 * @ClassName: PlayMethod
 */
public class PlayMethod implements Serializable {

	private static final long serialVersionUID = 1L;

	private ArrayList<Playlist> playlist;

	/**
	 * Keeps record of currently selected track
	 */
	private int selected = -1;

	public PlayMethod() {
		playlist = new ArrayList<Playlist>();
	}

	public void addTrack(Track track, Album album) {
		Playlist playlistEntry = new Playlist();
		playlistEntry.setAlbum(album);
		playlistEntry.setTrack(track);
		playlist.add(playlistEntry);
	}

	public void addTracks(Track[] tracks, Album album) {
		for (int i = 0; i < tracks.length; i++) {
			addTrack(tracks[i], album);
		}
	}

	// 检查列表是否为空
	public boolean isEmpty() {
		return playlist.size() == 0;
	}

	// 选中下一首
	public void selectNext() {
		if (!isEmpty()) {
			selected++;
			selected %= playlist.size();
		}
	}

	// 上一首
	public void selectPrev() {
		if (!isEmpty()) {
			selected--;
			if (selected < 0)
				selected = playlist.size() - 1;
		}
	}

	// 根据index选择歌曲
	public void select(int index) {
		if (!isEmpty()) {
			if (index >= 0 && index < playlist.size())
				selected = index;
		}
	}

	public void selectOrAdd(Track track, Album album) {
		// first search thru available tracks
		for (int i = 0; i < playlist.size(); i++) {
			if (playlist.get(i).getTrack().getId() == track.getId()) {
				select(i);
				return;
			}
		}
		// add track if necessary
		addTrack(track, album);
		select(playlist.size() - 1);
	}

	public int getSelectedIndex() {
		if (isEmpty()) {
			selected = -1;
		}
		if (selected == -1 && !isEmpty()) {
			selected = 0;
		}
		return selected;
	}

	public Playlist getSelectedTrack() {
		Playlist playlistEntry = null;

		if (!isEmpty()) {
			playlistEntry = playlist.get(getSelectedIndex());
		}

		return playlistEntry;

	}

	public void addPlaylistEntry(Playlist playlistEntry) {
		playlist.add(playlistEntry);
	}

	// 数量
	public int size() {
		return playlist == null ? 0 : playlist.size();
	}

	public Playlist getTrack(int index) {
		return playlist.get(index);
	}

	// 删除
	public void remove(int position) {
		if (playlist != null && position < playlist.size() && position >= 0) {

			if (selected >= position) {
				selected--;
			}

			playlist.remove(position);
		}

	}
}
