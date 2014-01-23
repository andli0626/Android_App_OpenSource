package android.source.tuangou.framework.lbs;

import android.location.Location;
import android.source.tuangou.framework.ServiceManager;
import android.source.tuangou.framework.net.NetworkService;
import android.source.tuangou.framework.ui.WebActivity;
import android.source.tuangou.framework.webridge.ScriptHook;

import org.json.*;


public class LbsScriptHook extends ScriptHook
{

	String currentAddress;
	String currentCity;

	public LbsScriptHook(WebActivity webactivity)
	{
		super(webactivity);
		currentAddress = null;
		currentCity = null;
	}

	private void requestAddress()
	{
		Location location = ServiceManager.getLocationService().getLocation();
		try {
			if (location == null) {
				currentAddress = null;
				currentCity = null;
				return;
			} else {
				String s1;
				StringBuilder stringbuilder = (new StringBuilder())
						.append("http://maps.google.com/maps/api/geocode/json?latlng=");
				double d = location.getLatitude();
				StringBuilder stringbuilder1 = stringbuilder.append(d).append(
						",");
				double d1 = location.getLongitude();
				String s = stringbuilder1.append(d1)
						.append("&sensor=true&language=zh-CN").toString();
				s1 = ServiceManager.getNetworkService().getSync(s);
				if (s1 == null) {
					currentAddress = null;
					currentCity = null;
					return;
				} else {
					JSONObject jsonobject = (new JSONObject(s1)).getJSONArray(
							"results").getJSONObject(0);
					String s2 = jsonobject.getString("formatted_address");
					currentAddress = s2;
					String s3 = jsonobject.getJSONArray("address_components")
							.getJSONObject(2).getString("long_name");
					currentCity = s3;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

	}

	public String getCurrentAddress()
	{
		if (currentAddress == null)
			requestAddress();
		return currentAddress;
	}

	public String getCurrentCity()
	{
		if (currentCity == null)
			requestAddress();
		return currentCity;
	}

	public String getJsObjectName()
	{
		return "android_lbs";
	}

	public String getLatitudeAndLongitude()
	{
		Location location = ServiceManager.getLocationService().getLocation();
		String s;
		if (location != null)
		{
			StringBuilder stringbuilder = (new StringBuilder()).append("{Latitude:");
			double d = location.getLatitude();
			StringBuilder stringbuilder1 = stringbuilder.append(d).append(",Longitude:");
			double d1 = location.getLongitude();
			s = stringbuilder1.append(d1).append("}").toString();
		} else
		{
			s = null;
		}
		return s;
	}
}
