package com.teleca.jamendo.util;

import java.util.Hashtable;
import java.util.LinkedList;

/**
 * 请求缓存
 * 
 * @author lilin
 * @date 2011-12-27 下午08:47:34
 * @ClassName: RequestCache
 */
public class RequestCache {
	// 缓存最大量
	private static int maxCache = 10;

	@SuppressWarnings("unchecked")
	private LinkedList history;
	private Hashtable<String, String> cache;

	@SuppressWarnings("unchecked")
	public RequestCache() {
		history = new LinkedList();
		cache = new Hashtable<String, String>();
	}

	@SuppressWarnings("unchecked")
	public void put(String url, String data) {
		history.add(url);
		// 如果超过最大缓存，就需要清理
		if (history.size() > maxCache) {
			String old_url = (String) history.poll();
			cache.remove(old_url);
		}
		cache.put(url, data);
	}

	public String get(String url) {
		return cache.get(url);
	}
}
