package com.yizhao.core;

import java.util.HashMap;
import java.util.List;

import com.yizhao.bean.HotProductBean;
import com.yizhao.blog.UserInfo;

import android.graphics.Bitmap;
import android.util.Log;

public class CacheManager {
	
	private static CacheManager cache = null;
	
	private static Object lock = new Object();
	
	private HotProductBean hostProductBean;

    private HashMap<String,Bitmap> bitmap_Map;

    private String pic_b_option;//首页/产品详情所需设置的图片大小
    
    private String pic_m_option;//收藏与搜索所需设置的图片大小

    private String pic_s_option;//收藏与搜索所需设置的图片大小
    
    private int display_pix_width;//屏幕分辨率（宽）

	private int display_pix_height;//屏幕分辨率（高）
	
	private String[] autoCompleteText;
	
	private List<String> autocomplete_list;
	
	private UserInfo user_sina;
	
	private UserInfo user_tx;
	
	private CacheManager() {
		Log.i(Const.TAG,"CacheManager|new a CacheManager object..................");
	}
	
	/**
	 * 获取本对象单例
	 * @return
	 */
	public static CacheManager getInstance() {
		if (cache == null) {
			synchronized (lock) {
				if (cache == null) {
					cache = new CacheManager();
				}
			}
		}
		return cache;
	}
	
	/**
	 * 获取-自动输入提示缓存
	 * @return String[]
	 */
	public String[] getAutoCompleteText(){
		return autoCompleteText;
	}
	
	
//	/**
//	 * 更新-自动输入提示缓存
//	 * @param autoCompleteTextList
//	 */
//	public void updateAutocompleteTextCache(){
//		if(autocomplete_list!=null){
//			int len = autocomplete_list.size();
//			if(len > 0){
//				String[] _tmp = new String[len];
//				for(int i = 0; i < len; i++){
//					_tmp[i] = autocomplete_list.get(i);
//				}
//				autoCompleteText = _tmp;
//			}
//		}
//	}

   public int getDisplay_pix_width() {
		return display_pix_width;
	}

	public void setDisplay_pix_width(int displayPixWidth) {
		display_pix_width = displayPixWidth;
	}

	public int getDisplay_pix_height() {
		return display_pix_height;
	}

	public void setDisplay_pix_height(int displayPixHeight) {
		display_pix_height = displayPixHeight;
	}
	
	public HotProductBean getHostProductBean() {
		return hostProductBean;
	}

	public void setHostProductBean(HotProductBean bean) {
		hostProductBean = bean;
	}
	
    public void putBitmap(String id,Bitmap bitmap){
    	if(bitmap_Map==null){
    		bitmap_Map = new HashMap<String,Bitmap>();
    	}
    	bitmap_Map.put(id, bitmap);
    }
    
    public Bitmap getBitmap(String id){
    	Bitmap bm = null;
    	if(bitmap_Map!=null){
    		bm = bitmap_Map.get(id);
    	}
    	return bm;
    }
    
	
	public HashMap<String, Bitmap> getBitmap_Map() {
		if(bitmap_Map == null){
			return new HashMap<String, Bitmap>();
		}else{
			return bitmap_Map;
		}
	}

	public String getPic_b_option() {
		return pic_b_option;
	}

	public void setPic_b_option(String pic_b_option) {
		this.pic_b_option = pic_b_option;
	}

	public String getPic_m_option() {
		return pic_m_option;
	}

	public void setPic_m_option(String pic_m_option) {
		this.pic_m_option = pic_m_option;
	}

	public String getPic_s_option() {
		return pic_s_option;
	}

	public void setPic_s_option(String pic_s_option) {
		this.pic_s_option = pic_s_option;
	}

	public UserInfo getUser_sina() {
		return user_sina;
	}

	public void setUser_sina(UserInfo user_sina) {
		this.user_sina = user_sina;
	}

	public UserInfo getUser_tx() {
		return user_tx;
	}

	public void setUser_tx(UserInfo user_tx) {
		this.user_tx = user_tx;
	}

	public void setAutocomplete_list(List<String> autoList) {
		
		this.autocomplete_list = autoList;
		
		if(autocomplete_list!=null){
			int size = autocomplete_list.size();
			autoCompleteText = new String[size];
			autocomplete_list.toArray(autoCompleteText);
		}else{
			autoCompleteText = new String[0];
		}
	}
	
}
