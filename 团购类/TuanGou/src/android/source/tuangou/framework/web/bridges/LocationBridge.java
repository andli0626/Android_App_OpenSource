package android.source.tuangou.framework.web.bridges;

import android.location.Location;
import android.source.tuangou.framework.ServiceManager;
import android.source.tuangou.framework.lbs.AddressService;
import android.source.tuangou.framework.lbs.LocationService;
import android.source.tuangou.framework.web.ScriptBridge;

public class LocationBridge extends ScriptBridge
{

	public LocationBridge(){
	}

	//获取当前地址
	public String getCurrentAddress(){
		return ServiceManager.getAddressService().getCurrentAddress();
	}

	//获取当前city
	public String getCurrentCity(){
		return ServiceManager.getAddressService().getCurrentCity();
	}

	//GPS定位获取经纬度
	public String getLatitudeAndLongitude(){
		Location location = ServiceManager.getLocationService().getLocation();
		
		String s;
		if (location != null){
			StringBuilder stringbuilder = (new StringBuilder()).append("{latitude:");
			double d = location.getLatitude();
			StringBuilder stringbuilder1 = stringbuilder.append(d).append(",longitude:");
			double d1 = location.getLongitude();
			s = stringbuilder1.append(d1).append("}").toString();
		} else
		{
			s = null;
		}
		System.out.println("javaScript call java: getLatitudeAndLongitude location = "+s);
		return s;
	}
}
