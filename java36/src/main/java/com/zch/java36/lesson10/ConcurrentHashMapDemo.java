package com.zch.java36.lesson10;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConcurrentHashMapDemo {
	private AtomicBoolean locked = new AtomicBoolean(false);

	void aa() {

		ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
		map.put("a", "b");
	}
}
