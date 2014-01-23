package com.teleca.jamendo.api;

import com.teleca.jamendo.activity.playview.PlayMethod;

/**
 * 播放引擎接口
 * 
 * @author lilin
 * @date 2012-1-9 下午06:33:48
 * @ClassName: IPlayEngine
 */
public interface IPlayEngine {
	public void openPlaylist(PlayMethod playlist);

	public PlayMethod getPlaylist();

	public void play();

	public boolean isPlaying();

	public void stop();

	public void pause();

	public void next();

	public void prev();

	public void skipTo(int index);

	public void setListener(IPlayEngineListener playerEngineListener);
}
