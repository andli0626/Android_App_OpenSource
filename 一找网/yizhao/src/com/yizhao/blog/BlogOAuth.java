package com.yizhao.blog;

//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.util.List;
import java.util.SortedSet;

//import org.apache.http.HttpResponse;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.params.CoreProtocolPNames;
//import org.apache.http.protocol.HTTP;

import com.yizhao.core.Const;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

public class BlogOAuth {
	
    private CommonsHttpOAuthConsumer httpOauthConsumer;
    private OAuthProvider httpOauthprovider;
    private String callBackUrl;
    
    public String consumerKey;
    public String consumerSecret;
    
    private String request_token_url;
    private String access_token_url; 
    private String authrize_url;
    
    public BlogOAuth(String consumerKey,String consumerSecret,String _callBackUrl,
    		String _request_token_url,String _access_token_url,String _authrize_url)
    {
        this.consumerKey=consumerKey;
        this.consumerSecret=consumerSecret;
        this.callBackUrl=_callBackUrl;
        this.request_token_url = _request_token_url;
        this.access_token_url = _access_token_url;
        this.authrize_url = _authrize_url;
    }
    
    
    public int RequestAccessToken(Activity activity){
    	
        int retult = -1;
        
        
            httpOauthConsumer = new CommonsHttpOAuthConsumer(consumerKey,consumerSecret);
            
            httpOauthprovider = new DefaultOAuthProvider(request_token_url,access_token_url,authrize_url);
            
            Log.d(Const.TAG, "OAuth.RequestAccessToken|httpOauthConsumer="+httpOauthConsumer+",httpOauthprovider="+httpOauthprovider);
            
			try {
				String authUrl = httpOauthprovider.retrieveRequestToken(httpOauthConsumer, callBackUrl);
				Log.d(Const.TAG, "OAuth.RequestAccessToken|authUrl="+authUrl);
				activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
				retult = 0;
			} catch (OAuthMessageSignerException e) {
				retult = -2;
				Log.e(Const.TAG, "OAuth.RequestAccessToken|OAuthMessageSignerException!");
				e.printStackTrace();
			} catch (OAuthNotAuthorizedException e) {
				retult = -3;
				Log.e(Const.TAG, "OAuth.RequestAccessToken|OAuthNotAuthorizedException!");
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				retult = -4;
				Log.e(Const.TAG, "OAuth.RequestAccessToken|OAuthExpectationFailedException!");
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				retult = -5;
				Log.e(Const.TAG, "OAuth.RequestAccessToken|OAuthCommunicationException!");
				e.printStackTrace();
			}
            
            Log.d(Const.TAG, "OAuth.RequestAccessToken|startActivity...");
            
            
            
        return retult;
    }
    
    public UserInfo GetAccessToken(Intent intent){
    	
        UserInfo user=null;
        
        Uri uri = intent.getData();
        
        String verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
        
        try {
        	
            httpOauthprovider.setOAuth10a(true); 
            httpOauthprovider.retrieveAccessToken(httpOauthConsumer,verifier);
            
        } catch (OAuthMessageSignerException ex) {
        	Log.e(Const.TAG, "OAuth.GetAccessToken|OAuthMessageSignerException..");
            ex.printStackTrace();
        } catch (OAuthNotAuthorizedException ex) {
        	Log.e(Const.TAG, "OAuth.GetAccessToken|OAuthNotAuthorizedException..");
            ex.printStackTrace();
        } catch (OAuthExpectationFailedException ex) {
        	Log.e(Const.TAG, "OAuth.GetAccessToken|OAuthExpectationFailedException..");
            ex.printStackTrace();
        } catch (OAuthCommunicationException ex) {
        	Log.e(Const.TAG, "OAuth.GetAccessToken|OAuthCommunicationException..");
            ex.printStackTrace();
        } catch (Exception ex) {
        	Log.e(Const.TAG, "OAuth.GetAccessToken|OtherException..");
            ex.printStackTrace();
        }
        
        SortedSet<String> user_id= httpOauthprovider.getResponseParameters().get("user_id");
        Log.e(Const.TAG, "OAuth.GetAccessToken|SortedSet<String> user_id="+user_id);
        //String userId=user_id.first();
        String userKey = httpOauthConsumer.getToken();
        String userSecret = httpOauthConsumer.getTokenSecret();
        
//        if(userId!=null && !"".equals(userId)
//        		&&userKey!=null && !"".equals(userKey)
//        			&&userSecret!=null && !"".equals(userSecret)){
	        user=new UserInfo();
	        user.setType("sina");
	        user.setToken(userKey);
	        user.setTokenSecret(userSecret);
        //}
        return user;
    }
    
//    public HttpResponse SignRequest(String token,String tokenSecret,String url,List params)
//    {
//        HttpPost post = new HttpPost(url);
//        //HttpClient httpClient = null;
//        try{
//            post.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
//        } catch (UnsupportedEncodingException e) {
//             e.printStackTrace();
//        }
//        //关闭Expect:100-Continue握手
//        //100-Continue握手需谨慎使用，因为遇到不支持HTTP/1.1协议的服务器或者代理时会引起问题
//        post.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
//        return SignRequest(token,tokenSecret,post);
//    }
    
//    public HttpResponse SignRequest(String token,String tokenSecret,HttpPost post){
//        httpOauthConsumer = new CommonsHttpOAuthConsumer(consumerKey,consumerSecret);
//        httpOauthConsumer.setTokenWithSecret(token,tokenSecret);
//        HttpResponse response = null;
//        try {
//            httpOauthConsumer.sign(post);
//        } catch (OAuthMessageSignerException e) {
//            e.printStackTrace();
//        } catch (OAuthExpectationFailedException e) {
//            e.printStackTrace();
//        } catch (OAuthCommunicationException e) {
//            e.printStackTrace();
//        }
//        //取得HTTP response
//        try {
//            response = new DefaultHttpClient().execute(post);
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return response;
//    }
    
//    /** 
//     * 发表带图片的微博 
//     * @param token 
//     * @param tokenSecret 
//     * @param aFile 
//     * @param status 
//     * @param urlPath 
//     * @return 
//     */  
//    public String uploadStatus(String token, String tokenSecret, File aFile, String status, String urlPath) {  
//        httpOAuthConsumer = new DefaultOAuthConsumer(consumerKey,consumerSecret);  
//        httpOAuthConsumer.setTokenWithSecret(token,tokenSecret);  
//        String result = null;  
//        try {  
//            URL url = new URL(urlPath);  
//            HttpURLConnection request = (HttpURLConnection) url.openConnection();  
//            request.setDoOutput(true);  
//            request.setRequestMethod("POST");  
//            HttpParameters para = new HttpParameters();  
//            para.put("status", URLEncoder.encode(status,"utf-8").replaceAll("\\+", "%20"));  
//            String boundary = "---------------------------37531613912423";  
//            String content = "--"+boundary+"\r\nContent-Disposition: form-data; name=\"status\"\r\n\r\n";  
//            String pic = "\r\n--"+boundary+"\r\nContent-Disposition: form-data; name=\"pic\"; filename=\"image.jpg\"\r\nContent-Type: image/jpeg\r\n\r\n";  
//            byte[] end_data = ("\r\n--" + boundary + "--\r\n").getBytes();   
//            FileInputStream stream = new FileInputStream(aFile);  
//            byte[] file = new byte[(int) aFile.length()];  
//            stream.read(file);  
//            request.setRequestProperty("Content-Type", "multipart/form-data; boundary="+boundary); //设置表单类型和分隔符   
//            request.setRequestProperty("Content-Length", String.valueOf(content.getBytes().length + status.getBytes().length + pic.getBytes().length + aFile.length() + end_data.length)); //设置内容长度   
//            httpOAuthConsumer.setAdditionalParameters(para);  
//            httpOAuthConsumer.sign(request);  
//            OutputStream ot = request.getOutputStream();  
//            ot.write(content.getBytes());  
//            ot.write(status.getBytes());  
//            ot.write(pic.getBytes());  
//            ot.write(file);  
//            ot.write(end_data);  
//            ot.flush();  
//            ot.close();  
//            request.connect();  
//            if (200 == request.getResponseCode()) {  
//                result = "SUCCESS";  
//            }  
//        } catch (FileNotFoundException e1) {  
//            e1.printStackTrace();  
//        } catch (IOException e) {  
//            e.printStackTrace();  
//        } catch (OAuthMessageSignerException e) {  
//            e.printStackTrace();  
//        } catch (OAuthExpectationFailedException e) {  
//            e.printStackTrace();  
//        } catch (OAuthCommunicationException e) {  
//            e.printStackTrace();  
//        }  
//        return result;  
//    }   
}
