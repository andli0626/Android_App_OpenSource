package com.yizhao.util;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

import com.yizhao.core.Const;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetUtil {
	
    /**
     * 网络连接检测
     * @param ctx
     * @return
     */
    public static boolean isNetworkAvailable(Context ctx) {   
    	boolean netstatus = false;
        try {   
            ConnectivityManager cm = (ConnectivityManager) ctx   
                    .getSystemService(Context.CONNECTIVITY_SERVICE);   
            NetworkInfo info = cm.getActiveNetworkInfo();   
            if(info != null && info.isConnected()){
            	netstatus = true;
            }   
        } catch (Exception e) {   
            e.printStackTrace();   
            netstatus = false;   
        }
        Log.d(Const.TAG, "NetUtil.isNetworkAvailable|netstatus="+netstatus);
        return netstatus;
    } 
    

    /**
     * 通过GPRS获取本地IP
     * @return
     */
	public static String getLocalIpAddress(){

		/* get wifi ip */
		//WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);   
		//WifiInfo wifiInfo = wifiManager.getConnectionInfo();   
		//int ipAddress = wifiInfo.getIpAddress();	
		//Configuration.wifiIp = Utils.intToIp(ipAddress); 
		
		String ip = "127.0.0.1";
        try
        {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
            {
               NetworkInterface intf = en.nextElement();
               for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
               {
                   InetAddress inetAddress = enumIpAddr.nextElement();
                   if (!inetAddress.isLoopbackAddress())
                   {
                	   ip = inetAddress.getHostAddress().toString();
                   }
               }
           }
        }
        catch (SocketException ex){
        	ex.printStackTrace();
        }
        return ip;
        
    }
    
    
    /**
     * 将不带http://头的url增加url头
     * @param def_url
     * @return
     */
    public static String getUrl(String def_url){
    	String res_url=null;
    	if(def_url!=null){
    		if(!def_url.contains(Const.HTTPHEAD)){
    			res_url = Const.HTTPHEAD+def_url;
			}else{
				res_url = def_url;
			}
    	}
    	return res_url;
    }

    
    /**
     * 获取某个URL返回数据的长度-----未使用
     * @param _url
     * @return
     */
    public static long getSizeByUrl(String _url){
    	
    	long size = 0;
    	
    	HttpURLConnection connection = null;
    	
    	try {
    		
			URL url = new URL(_url);
			
			connection = (HttpURLConnection)url.openConnection();//开启HTTP连接
			
			connection.setReadTimeout(Const.TIMEOUT_15);//设置15秒超时
			
			size = connection.getContentLength();//获取内容长度
			
			connection.disconnect();
			
		} catch (Exception e){//下载异常
			
             e.printStackTrace();  
             
		 } finally {
			 
			 if(connection!=null){
				 
				 connection.disconnect();
				 
			 }
			 
		 }
		 
		 return size;
    	
    }
    
    
}
