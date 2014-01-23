package com.yizhao.bean;

import java.util.ArrayList;

public class HotProductBean {
		
	private String result;

	private int popSize;

	private int promSize;

	private ArrayList<ProductBean> popFileList;
	
	private ArrayList<SaleBean> promFileList;
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public ArrayList<ProductBean> getPopFileList() {
		return popFileList;
	}

	public void setPopFileList(ArrayList<ProductBean> popFileList) {
		this.popFileList = popFileList;
	}

	public int getPopSize() {
		return popSize;
	}

	public void setPopSize(int popSize) {
		this.popSize = popSize;
	}
	
	public int getPromSize() {
		return promSize;
	}

	public void setPromSize(int promSize) {
		this.promSize = promSize;
	}

	public ArrayList<SaleBean> getPromFileList() {
		return promFileList;
	}

	public void setPromFileList(ArrayList<SaleBean> promFileList) {
		this.promFileList = promFileList;
	}
}



