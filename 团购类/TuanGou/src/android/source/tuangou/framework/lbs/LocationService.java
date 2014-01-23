package android.source.tuangou.framework.lbs;

import android.content.Context;
import android.location.*;
import android.os.Bundle;
import android.source.tuangou.framework.util.LogUtil;


/*
 * 本地服务类
 * */
public class LocationService
{

	Location GPS_Location;
	Location Net_Location;
	boolean gps_enabled;
	LocationListener locationListenerGps;
	LocationListener locationListenerNet;
	LocationManager locationManager;
	boolean net_enabled;

	//构造函数
	public LocationService(Context context){
		gps_enabled = false;//gps使能标识
		net_enabled = false;//网络使能标识
		GPS_Location = null;//gps位置
		Net_Location = null;//网络位置
		
		//Gps位置服务监听器
		locationListenerGps = new ListenerGps();
		
		//网络位置服务监听器
		locationListenerNet = new ListenerNet();
		
		//位置管理
		if (locationManager == null){
			//创建位置管理类
			locationManager = (LocationManager)context.getSystemService("location");
		}
	}

	//获取具体位置的经纬度
	public Location getLocation(){
		Location location;
		if (GPS_Location != null){
			location = GPS_Location;
		}
		else if (Net_Location != null)
			location = Net_Location;
		else
			location = null;
		
		return location;
	}

	//gps监听
	public void listenGps(){
		boolean flag = locationManager.isProviderEnabled("gps");
		gps_enabled = flag;
		if (!gps_enabled){
			return;
		}
		//gps每半分钟更新当前位置
		locationManager.requestLocationUpdates("gps", 30000L, 0F, locationListenerGps);

	}

	public void listenNet()
	{
		boolean flag = locationManager.isProviderEnabled("network");
		net_enabled = flag;
		if (!net_enabled){
			return;
		}
		LocationManager locationmanager = locationManager;
		LocationListener locationlistener = locationListenerNet;
		locationmanager.requestLocationUpdates("network", 30000L, 0F, locationlistener);
	}

	public void removeGpsListener()
	{
		LocationManager locationmanager = locationManager;
		LocationListener locationlistener = locationListenerGps;
		locationmanager.removeUpdates(locationlistener);
	}

	public void removeLocationListener()
	{
		LocationManager locationmanager = locationManager;
		LocationListener locationlistener = locationListenerNet;
		locationmanager.removeUpdates(locationlistener);
		LocationManager locationmanager1 = locationManager;
		LocationListener locationlistener1 = locationListenerGps;
		locationmanager1.removeUpdates(locationlistener1);
	}

	public void removeNetListener()
	{
		LocationManager locationmanager = locationManager;
		LocationListener locationlistener = locationListenerNet;
		locationmanager.removeUpdates(locationlistener);
	}

	//开启位置服务监听器--gps和net
	public void startLocationListener(){
		listenGps();
		listenNet();
	}

	//Gps位置监听器
	private class ListenerGps
		implements LocationListener{

		final LocationService this$0;

		//位置改变函数
		public void onLocationChanged(Location location){
			
			GPS_Location = location;
		}

		public void onProviderDisabled(String s){
			
		}

		public void onProviderEnabled(String s){
		}

		public void onStatusChanged(String s, int i, Bundle bundle){
		}

		ListenerGps(){
			super();
			this$0 = LocationService.this;
		}
	}

	//网络位置监听器
	private class ListenerNet
		implements LocationListener{

		final LocationService this$0;

		//位置改变函数
		public void onLocationChanged(Location location){
			Net_Location = location;
		}

		public void onProviderDisabled(String s){
			
		}

		public void onProviderEnabled(String s){
			
		}

		public void onStatusChanged(String s, int i, Bundle bundle){
			
		}

		ListenerNet(){
			super();
			this$0 = LocationService.this;
		}
	}

}
