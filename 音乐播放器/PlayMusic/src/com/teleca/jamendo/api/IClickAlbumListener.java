package com.teleca.jamendo.api;

import com.teleca.jamendo.model.Album;

/**
 * 点击相册接口
 * 
 * @author lilin
 * @date 2011-12-12 下午11:46:13
 * @ClassName: IOnAlbumClickListener
 */
public interface IClickAlbumListener {
	public void onAlbumClicked(Album album);
}
