package android.source.tuangou.framework.web.bridges;

import android.source.tuangou.framework.ServiceManager;
import android.source.tuangou.framework.base.TelephonyManager;
import android.source.tuangou.framework.web.ScriptBridge;


public class ConnectivityBridge extends ScriptBridge
{

	public ConnectivityBridge(){
		
	}

	//获取当前网络状态
	public boolean getNetworkState(){
		
		boolean flag;
		int k;
		int l;
		boolean flag1;
		boolean flag2;
		
		//判断wifi状态
		if (ServiceManager.getTelephonyManager().getWifiState() 
				== android.net.NetworkInfo.State.CONNECTED){
			flag = true;
		}else{
			flag = false;
		}
		
		//判断手机网络状态
		if(ServiceManager.getTelephonyManager().getMobileState()
			== android.net.NetworkInfo.State.CONNECTED){
			flag1 = true;
		}else{
			flag1 = false;
		}
		
		if (!flag && !flag1){
			flag2 = false;
		}
		else{
			flag2 = true;
		}
		
		System.out.println("javascrpit call java: getNetworkState = "+ flag2);
		return flag2;
	}
}
