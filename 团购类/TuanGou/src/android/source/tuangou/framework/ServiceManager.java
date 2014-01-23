package android.source.tuangou.framework;

import android.content.Context;
import android.source.tuangou.framework.base.TelephonyManager;
import android.source.tuangou.framework.lbs.AddressService;
import android.source.tuangou.framework.lbs.LocationService;
import android.source.tuangou.framework.net.NetworkService;
import android.source.tuangou.framework.store.DatabaseManager;

/*
 * 客户端所有的服务管理类
 * */
public class ServiceManager{

	static AddressService addressService;
	static Context context;
	static DatabaseManager databaseManager;
	static LocationService locationService;
	static NetworkService networkService;
	static TelephonyManager telephonyManager;

	public ServiceManager(){
		
	}

	//获取地址服务
	public static AddressService getAddressService(){
		return addressService;
	}

	public static DatabaseManager getDatabaseManager(){
		return databaseManager;
	}

	//获取位置服务类
	public static LocationService getLocationService(){
		return locationService;
	}

	public static NetworkService getNetworkService(){
		return networkService;
	}

	public static TelephonyManager getTelephonyManager(){
		return telephonyManager;
	}

	//服务管理初始化
	public static void init(Context context1){
		context = context1;
		
		//创建网络服务对象
		networkService = new NetworkService();
		//创建本地服务对象
		locationService = new LocationService(context1);
		//创建地址服务对象
		addressService = new AddressService();
		//创建数据库管理服务对象
		databaseManager = new DatabaseManager(context1);
		//创建电话管理服务对象
		telephonyManager = new TelephonyManager(context1);
	}
}
