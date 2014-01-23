package android.source.tuangou.framework.lbs;

import android.location.Location;
import android.source.tuangou.framework.ServiceManager;
import android.source.tuangou.framework.net.NetworkService;
import android.source.tuangou.framework.util.StringUtil;

import org.json.*;

/*
 * 地址服务类
 * */
public class AddressService{

	String currentAddress;
	String currentCity;
	long requestTime;

	//构造函数
	public AddressService(){
		requestTime = 65535L;//请求时间
	}

	//请求获取地址函数
	private void requestAddress(){
		//获取当前的位置信息
		Location location = ServiceManager.getLocationService().getLocation();
		
		//根据位置信息获取当前地址
		try {
			if (location == null) {
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
				
				System.out.println("javaScript call  java requestAddress request = "+s);
				
				s1 = ServiceManager.getNetworkService().getSync(s);
				
				System.out.println("javaScript call  java requestAddress address = "+s1);
				
				if (s1 == null) {
					long l = System.currentTimeMillis();
					requestTime = l;
				} else {
					//对返回的地址信息进行分析
					JSONArray jsonarray;
					int i;
					JSONObject jsonobject = (new JSONObject(s1)).getJSONArray(
							"results").getJSONObject(0);
					String s2 = jsonobject.getString("formatted_address");
					currentAddress = s2;
					jsonarray = jsonobject.getJSONArray("address_components");
					i = jsonarray.length() - 1;

					for (; i > 0; i--) {
						JSONObject jsonobject1 = jsonarray.getJSONObject(i);
						if (jsonobject1.getString("types").indexOf("locality") == -1) {
							continue;
						} else {
							String s3 = jsonobject1.getString("long_name");
							currentCity = s3;
							break;
						}
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}


	}

	//获取地址
	public String getCurrentAddress(){
		//请求时间少于0.6s则返回上一次请求返回的地址
		if (requestTime >= 0L){
			long l = System.currentTimeMillis();
			long l1 = requestTime;
			if (l - l1 <= 600L){
				return currentAddress;
			}
		}
		//请求地址函数
		requestAddress();
		
		System.out.println("javaScrpt call java getCurrentAddress currentAddress = "+currentAddress);
		
		return currentAddress;
	}

	public String getCurrentCity()
	{
		if (StringUtil.isEmpty(currentCity).booleanValue())
			requestAddress();
		return currentCity;
	}
}
