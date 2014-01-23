package com.yizhao.action;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.yizhao.bean.DetailShopsBean;
import com.yizhao.bean.HotProductBean;
import com.yizhao.bean.ProductDetailBean;
import com.yizhao.bean.ReceiveBean;
import com.yizhao.bean.SearchBean;
import com.yizhao.core.Const;
import com.yizhao.core.HttpManager;

public class ProductAction {
	
	/**
	 * 获取热门产品，此处是5个热门产品及5个促销产品
	 * @return
	 */
	public static HotProductBean getHotProduct(){
		HotProductBean bean = null;
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		String s = new HttpManager(Const.HOT_URL).submitRequest(params);
		
		//String s = "{\"result\":\"true\",\"popFileList\":[{\"id\":\"71f1039617ec1f6f66aafb95e9cd7903\",\"category\":\"channel1\",\"name\":\"Apple/苹果 iphone 3GS(8G) 联通版\",\"coverImage\":\"2051013649952884228.jpg\",\"lowprice\":10700,\"highprice\":34200,\"reviews\":34,\"shops\":4},{\"id\":\"71f1039617ec1f6f66aafb95e9cd7903\",\"category\":\"channel1\",\"name\":\"Apple/苹果 iphone 3GS(8G) 联通版\",\"coverImage\":\"2051013649952884228.jpg\",\"lowprice\":10700,\"highprice\":34200,\"reviews\":34,\"shops\":4}],\"popSize\":2,\"promFileList\":[{\"id\":\"compulte_1\",\"name\":\"sales_promotion_product_name1\"},{\"id\":\"compulte_2\",\"name\":\"sales_promotion_product_name2\"}],\"promSize\":2}";
		
		Log.d(Const.TAG, "ProductAction.getHotProduct|jsonStr="+s);
		
		if(s!=null && !"".equals(s)){
			Gson gson = new Gson();
			try{
				bean = gson.fromJson(s,new TypeToken<HotProductBean>(){}.getType());
			}catch(JsonParseException e){
				Log.e(Const.TAG, "ProductAction.getHotProduct|JsonParseException",e);
			}
		}
		return bean;
	}
	
	/**
	 * 搜索产品
	 * @param map
	 * @return
	 */
	public static SearchBean getSearchBean(Map<String, String> map){
		
		Log.d(Const.TAG, "ProductAction.getSearchBean|map="+map+",keyworld="+URLEncoder.encode(map.get("n")));
		
		SearchBean bean = null;

		String from = map.get("from");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if("product".equals(from)){
			params.add(new BasicNameValuePair("c", map.get("n")));
		}else{
			params.add(new BasicNameValuePair("n", map.get("n")));
		}
		params.add(new BasicNameValuePair("p", map.get("p")));
		params.add(new BasicNameValuePair("psize", Const.PAGE_SIZE));
		
		String s = new HttpManager(Const.SEARCH_URL).submitRequest(params);
		
		//String s = "{\"result\":\"true\",\"filelist\":[{\"id\":\"71f1039617ec1f6f66aafb95e9cd7903\",\"category\":\"channel1\",\"name\":\"Apple/苹果 iphone 3GS(8G) 联通版\",\"coverImage\":\"2051013649952884228.jpg\",\"refprice\":10700,\"shops\":123,\"reviews\":12},{\"id\":\"71f1039617ec1f6f66aafb95e9cd7903\",\"category\":\"channel1\",\"name\":\"Apple/苹果 iphone 3GS(16G) 移动版\",\"coverImage\":\"2051013649952884228.jpg\",\"refprice\":10700,\"shops\":123,\"reviews\":12}],\"size\":3,\"total\":253,\"page\":1}";

		Log.d(Const.TAG, "ProductAction.getSearchBean|jsonStr="+s);
		
		if(s!=null && !"".equals(s)){
			Gson gson = new Gson();
			try{
				bean = gson.fromJson(s,new TypeToken<SearchBean>(){}.getType());
			}catch(JsonParseException e){
				Log.e(Const.TAG, "ProductAction.getSearchBean|JsonParseException",e);
			}
		}
		
		return bean;
		
	}
	
	/**
	 * 根据产品ID获取详情
	 * @param map
	 * @return
	 */
	public static ProductDetailBean getProductDetail(Map<String, String> map){

		Log.d(Const.TAG, "ProductAction.getProductDetail|map="+map);
		
		ProductDetailBean bean = null;

		//设置HTTP POST请求参数必须用NameValuePair对象
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", map.get("product_id")));
		
		String s = new HttpManager(Const.DETAIL_URL).submitRequest(params);
		
		//String s = "{\"result\":\"true\",\"id\":\"71f1039617ec1f6f66aafb95e9cd7903\",\"category\":\"channel\",\"name\":\"product_name\",\"coverImage\":\"2051013649952884228.jpg\",\"highprice\":560000,\"lowprice\":380000,\"shops\":125,\"reviews\":344,\"photos\":\"2051013649952884228.jpg|2051013649952884228.jpg\",\"fileList\":[{\"shopName\":\"shopname1\",\"price\":5410000,\"sellUrl\":\"www.baidu.com\"},{\"shopName\":\"shopname2\",\"price\":5410000,\"sellUrl\":\"www.baidu.com\"}],\"size\":2}";
		
		if(s!=null && !"".equals(s)){
			Gson gson = new Gson();
			try{
				bean = gson.fromJson(s,new TypeToken<ProductDetailBean>(){}.getType());
			}catch(JsonParseException e){
				Log.e(Const.TAG, "ProductAction.getProductDetail|JsonParseException",e);
			}
		}
		
		return bean;
		
	}
	
	/**
	 * 根据产品ID获取评论
	 * @param map
	 * @return
	 */
	public static ReceiveBean getProductReceive(Map<String, String> map){

		ReceiveBean bean = null;
		
		Log.d(Const.TAG, "ProductAction.getProductReceive|map="+map);
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", map.get("product_id")));
		params.add(new BasicNameValuePair("p", map.get("p")));
		params.add(new BasicNameValuePair("psize", Const.PAGE_SIZE));
		
		String s = new HttpManager(Const.REVIEWS_URL).submitRequest(params);
		
		//String s = "{\"result\":\"true\",\"id\":\"71f1039617ec1f6f66aafb95e9cd7903\",\"name\":\"product_name1\",\"reviews\":2,\"fileList\":[{\"author\":\"张三\",\"writeTime\":1234567000,\"fromType\":1,\"from\":\"一找网\",\"content\":\"屏幕高分辨率,整体感觉比较好,速度比3GS快,全新的A4处理器,性价比高\"},{\"author\":\"李四\",\"writeTime\":1234567000,\"fromType\":1,\"from\":\"百度网\",\"content\":\"屏幕高分辨率,整体感觉比较好,速度比3GS快,全新的A4处理器,性价比高\"}],\"size\":2,\"page\":1}";
		
		if(s!=null && !"".equals(s)){
			Gson gson = new Gson();
			try{
				bean = gson.fromJson(s,new TypeToken<ReceiveBean>(){}.getType());
			}catch(JsonParseException e){
				Log.e(Const.TAG, "ProductAction.getProductReceive|JsonParseException",e);
			}
		}
		
		return bean;
		
	}
	
	/**
	 * 根据产品ID获取商家列表
	 * @param map
	 * @return
	 */
	public static DetailShopsBean getProductShops(Map<String, String> map){

		DetailShopsBean bean = null;
		
		Log.d(Const.TAG, "ProductAction.getProductShops|map="+map);
		

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", map.get("product_id")));
		params.add(new BasicNameValuePair("p", map.get("p")));
		params.add(new BasicNameValuePair("psize", Const.PAGE_SIZE));
		
		String s = new HttpManager(Const.SHOPS_URL).submitRequest(params);
		
		//String s = "{\"result\":\"true\",\"id\":\"71f1039617ec1f6f66aafb95e9cd7903\",\"name\":\"product_name1\",\"shops\":2,\"fileList\":[{\"shopName\":\"shopname1\",\"price\":5410000,\"sellUrl\":\"www.baidu.com\"},{\"shopName\":\"shopname2\",\"price\":5610000,\"sellUrl\":\"www.qq.com\"}],\"size\":2,\"page\":1}";
		
		if(s!=null && !"".equals(s)){
			Gson gson = new Gson();
			try{
				bean = gson.fromJson(s,new TypeToken<DetailShopsBean>(){}.getType());
			}catch(JsonParseException e){
				Log.e(Const.TAG, "ProductAction.getProductShops|JsonParseException",e);
			}
		}
		
		return bean;
		
	}
	
	
	
}
