package com.teleca.jamendo.model;

import java.io.Serializable;

/**
 * 歌手
 * 
 * @author lilin
 * @date 2011-12-28 下午12:27:15
 * @ClassName: ArtistModal
 */
public class Artist implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String idstr;
	private String name;
	private String image;
	private String url;
	private String mbgid;
	private int mbid;

	// Description of the artist (written by the artist)
	private String[] genre;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIdstr() {
		return idstr;
	}

	public void setIdstr(String idstr) {
		this.idstr = idstr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMbgid() {
		return mbgid;
	}

	public void setMbgid(String mbgid) {
		this.mbgid = mbgid;
	}

	public int getMbid() {
		return mbid;
	}

	public void setMbid(int mbid) {
		this.mbid = mbid;
	}

	public String[] getGenre() {
		return genre;
	}

	public void setGenre(String[] genre) {
		this.genre = genre;
	}

}
