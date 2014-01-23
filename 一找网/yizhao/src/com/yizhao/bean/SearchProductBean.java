package com.yizhao.bean;

public class SearchProductBean {
	
	private String id;
	private String category;//记得缓存首页bitmap
	private String name;
	private String coverImage;
	private int refprice;
	private int shops;
	private int reviews;
	private int hasGetPic;
	
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
	
	public int getRefprice() {
		return refprice;
	}
	
	public void setRefprice(int refprice) {
		this.refprice = refprice;
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

	public int getHasGetPic() {
		return hasGetPic;
	}

	public void setHasGetPic(int hasGetPic) {
		this.hasGetPic = hasGetPic;
	}
	
	

}
