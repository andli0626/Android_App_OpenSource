package com.yizhao.bean;

import java.util.ArrayList;

public class DetailShopsBean {
	
	private String result;
	private String id;
	private String name;
	private int shops;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getShops() {
		return shops;
	}
	public void setShops(int shops) {
		this.shops = shops;
	}
	public ArrayList<ShopsBean> getFileList() {
		return fileList;
	}
	public void setFileList(ArrayList<ShopsBean> fileList) {
		this.fileList = fileList;
	}

}
