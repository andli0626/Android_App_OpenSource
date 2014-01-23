package com.yizhao.blog;

import weibo4android.Weibo;

public class BlogBean extends Weibo{
	
	private static final long serialVersionUID = -3353131973567497968L;

	private boolean result;
	
	private String resMsg;

	public boolean isOK() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getResMsg() {
		return resMsg;
	}

	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}
	

}
