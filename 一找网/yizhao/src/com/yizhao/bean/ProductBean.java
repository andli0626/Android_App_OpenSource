package com.yizhao.bean;

//产品Bean
public class ProductBean{
	
	private String id;
	private String category;
	private String name;
	private String coverImage;
	private int lowprice;
	private int highprice;
	private int reviews;
	private int shops;
	
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
	public int getLowprice() {
		return lowprice;
	}
	public void setLowprice(int lowprice) {
		this.lowprice = lowprice;
	}
	public int getHighprice() {
		return highprice;
	}
	public void setHighprice(int highprice) {
		this.highprice = highprice;
	}
	public int getReviews() {
		return reviews;
	}
	public void setReviews(int reviews) {
		this.reviews = reviews;
	}
	public int getShops() {
		return shops;
	}
	public void setShops(int shops) {
		this.shops = shops;
	}
	
	
}