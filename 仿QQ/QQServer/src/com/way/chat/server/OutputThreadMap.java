package com.way.chat.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 存放写线程的缓存器
 * 
 * @author way
 */
public class OutputThreadMap {
	private HashMap<Integer, OutputThread> map;
	private static OutputThreadMap instance;

	private OutputThreadMap() {
		map = new HashMap<Integer, OutputThread>();
	}

	public synchronized static OutputThreadMap getInstance() {
		if (instance == null) {
			instance = new OutputThreadMap();
		}
		return instance;
	}

	public synchronized void add(Integer id, OutputThread out) {
		map.put(id, out);
	}

	public synchronized void remove(Integer id) {
		map.remove(id);
	}

	public synchronized OutputThread getById(Integer id) {
		return map.get(id);
	}

	public synchronized List<OutputThread> getAll() {
		List<OutputThread> list = new ArrayList<OutputThread>();
		for (Map.Entry<Integer, OutputThread> entry : map.entrySet()) {
			list.add(entry.getValue());
		}
		return list;
	}
}
