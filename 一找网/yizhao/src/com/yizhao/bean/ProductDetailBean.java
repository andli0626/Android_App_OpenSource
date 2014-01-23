package com.yizhao.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductDetailBean implements Serializable{
	
	private static final long serialVersionUID = -7160210544600464481L;
	
	private String result;
	private String id;
	private String category;
	private String name;
	private String coverImage;
	private int highprice;
	private int lowprice;
	private int shops;
	private int reviews;
	private String photos;
	private int size;
	private ArrayList<ShopsBean> fileList;
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCoverImage() {
		return coverImage;
	}
	public void setCoverImage(String coverImage) {
		this.coverImage = coverImage;
	}
	public int getHighprice() {
		return highprice;
	}
	public void setHighprice(int highprice) {
		this.highprice = highprice;
	}
	public int getLowprice() {
		return lowprice;
	}
	public void setLowprice(int lowprice) {
		this.lowprice = lowprice;
	}
	public int getShops() {
		return shops;
	}
	public void setShops(int shops) {
		this.shops = shops;
	}
	public int getReviews() {
		return reviews;
	}
	public void setReviews(int reviews) {
		this.reviews = reviews;
	}
	public String getPhotos() {
		return photos;
	}
	public void setPhotos(String photos) {
		this.photos = photos;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public ArrayList<ShopsBean> getFileList() {
		return fileList;
	}
	public void setFileList(ArrayList<ShopsBean> fileList) {
		this.fileList = fileList;
	}
}
