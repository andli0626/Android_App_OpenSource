package com.yizhao.bean;

import java.util.ArrayList;

public class SearchBean {
	
	private String result;
	
	private int size;
	
	private int total;
	
	private int page;

	private ArrayList<SearchProductBean> filelist;
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public ArrayList<SearchProductBean> getFilelist() {
		return filelist;
	}

	public void setFilelist(ArrayList<SearchProductBean> filelist) {
		this.filelist = filelist;
	}

}
