package com.yizhao.blog;

public class UserInfo {
	
	private String type;
	private String userKey;
	private String userSecret;
	
	public String getUserKey() {
		return userKey;
	}
	public void setToken(String userKey) {
		this.userKey = userKey;
	}
	public String getUserSecret() {
		return userSecret;
	}
	public void setTokenSecret(String userSecret) {
		this.userSecret = userSecret;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("type=").append(type).append(",userKey=").append(userKey).append("userSecret=").append(userSecret);
		return sb.toString();
	}
}
