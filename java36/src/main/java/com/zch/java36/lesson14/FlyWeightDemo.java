package com.zch.java36.lesson14;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 享元模式例子
 * @author zch
 * @time 2018年7月23日 上午11:12:03
 * 
 */
public class FlyWeightDemo {

}

interface FlyWeight {
	void action(String externalState);
}

class ConcreteFlyWeight implements FlyWeight {
	private static final Logger LOG = LoggerFactory.getLogger(ConcreteFlyWeight.class);
	private String name;

	public ConcreteFlyWeight(String name) {
		this.name = name;
	}

	@Override
	public void action(String externalState) {
		LOG.info("name = {}, outerState = {}", this.name, externalState);
	}
}

class FlyWeightFactory {
	private static final Logger LOG = LoggerFactory.getLogger(FlyWeightFactory.class);
	private static ConcurrentHashMap<String, FlyWeight> allFlyWeight = new ConcurrentHashMap<String, FlyWeight>();

	public static FlyWeight getFlyWeight(String name) {
		if (allFlyWeight.get(name) == null) {
			synchronized (allFlyWeight) {
				if (allFlyWeight.get(name) == null) {
					LOG.info("Instance of name = {} does not exist, creating it");
					FlyWeight flyWeight = new ConcreteFlyWeight(name);
					LOG.info("Instance of name = {} created");
					allFlyWeight.put(name, flyWeight);
				}
			}
		}
		return allFlyWeight.get(name);
	}
}