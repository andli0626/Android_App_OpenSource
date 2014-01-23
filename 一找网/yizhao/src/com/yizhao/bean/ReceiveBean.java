package com.yizhao.bean;

import java.util.ArrayList;

public class ReceiveBean {
	
	private String result;
	private String id;
	private String name;
	private int reviews;
	private int page;
	private int size;
	private ArrayList<AuthBean> fileList;
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getReviews() {
		return reviews;
	}
	public void setReviews(int reviews) {
		this.reviews = reviews;
	}
	public ArrayList<AuthBean> getFileList() {
		return fileList;
	}
	public void setFileList(ArrayList<AuthBean> fileList) {
		this.fileList = fileList;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}

}
