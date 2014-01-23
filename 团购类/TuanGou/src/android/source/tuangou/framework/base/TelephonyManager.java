package android.source.tuangou.framework.base;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*
 * 电话管理类
 * */
public class TelephonyManager{

	ConnectivityManager connectivityManager;
	Context context;

	//构造函数
	public TelephonyManager(Context context1){
		context = context1;
		//创建连接管理类
		connectivityManager = (ConnectivityManager)context1.getSystemService("connectivity");
	}

	public android.net.NetworkInfo.State getMobileState()
	{
		return connectivityManager.getNetworkInfo(0).getState();
	}

	public android.net.NetworkInfo.State getWifiState()
	{
		return connectivityManager.getNetworkInfo(1).getState();
	}
}
