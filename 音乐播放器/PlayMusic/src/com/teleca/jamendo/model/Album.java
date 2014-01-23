package com.teleca.jamendo.model;

import java.io.Serializable;

/**
 * 相册
 * 
 * @author lilin
 * @date 2011-12-13 上午12:32:50
 * @ClassName: Album
 */
public class Album implements Serializable {

	private static final long serialVersionUID = 8517633545835124349L;

	/**
	 * numeric id of the album
	 */
	private int id;

	/**
	 * link to the cover of the album
	 */
	private String image;

	/**
	 * name of the album
	 */
	private String name;

	/**
	 * Rating of the album
	 */
	private double rating;

	/**
	 * Display name of the artist.
	 */
	private String artistName;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public double getRating() {
		return rating;
	}

}
