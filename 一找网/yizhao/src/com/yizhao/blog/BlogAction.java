package com.yizhao.blog;

import java.net.URLEncoder;

import weibo4android.Status;
import weibo4android.WeiboException;
import weibo4android.http.ImageItem;

import com.yizhao.core.Const;

import android.util.Log;

public class BlogAction {
	
	public static BlogBean writeBlog(String token,String tokenSecret,String text,ImageItem item){
		BlogBean weibo = null;
			try {
				System.setProperty("weibo4j.oauth.consumerKey", Const.APPKEY_SINA);
	            System.setProperty("weibo4j.oauth.consumerSecret", Const.APPSECRET_SINA);
	            weibo = getWeibo(token,tokenSecret);
	            weibo.setHttpConnectionTimeout(Const.TIMEOUT_15);
	            weibo.setHttpReadTimeout(Const.TIMEOUT_10);
	            if(text!=null){
	            	Status status =	null;
	            	if(item!=null){
	            		status = weibo.uploadStatus(URLEncoder.encode(text), item);
			       	}else{
			       		status = weibo.updateStatus(URLEncoder.encode(text));
			       	}
			       	Log.d(Const.TAG,"BlogAction.newBlog|status id = "+status.getId() + ",status create at "+status.getCreatedAt());
			       	weibo.setResult(true);
			       	weibo.setResMsg("分享成功！");
	            }else{
	            	Log.d(Const.TAG, "BlogAction.newBlog|text and item is null");
	            	return null;
	    	    }
		     }catch(WeiboException e){
		    	Log.e(Const.TAG, "BlogAction.newBlog|WeiboException:");
		    	e.printStackTrace();
		    	weibo.setResult(false);
		    	weibo.setResMsg("操作过快,您刚刚分享过该信息！");
		     }catch (Exception e) {
		    	Log.e(Const.TAG, "BlogAction.newBlog|Exception:",e);
		    	e.printStackTrace();
		    	return null;
			 }
	    return weibo;
	}
	

	private static BlogBean getWeibo(String token,String tokenSecret){
		BlogBean weibo = new BlogBean();
		weibo.setToken(token, tokenSecret);
		return weibo;
	}

}
