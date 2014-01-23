package com.teleca.jamendo.util;

import java.util.WeakHashMap;
import android.graphics.Bitmap;

/**
 * 图片缓存类
 * 
 * @author lilin
 * @date 2011-12-24 下午02:49:56
 * @ClassName: ImgCache
 */
public class ImgCache extends WeakHashMap<String, Bitmap> {
	public boolean isCached(String url) {
		return containsKey(url) && get(url) != null;
	}

}
