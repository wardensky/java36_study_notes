package com.zch.java36.lesson14;

/**
 * @Description 本例说明各种单例的写法
 * @author zch
 * @time 2018年7月20日 上午11:04:07
 * 
 */
public class SingletonDemo {

}

/**
 * @Description 懒加载的单例，最常见。
 * @author zch
 * @time 2018年7月20日 上午10:59:55
 * 
 */
class Demo1 {
	private static Demo1 inst;

	private Demo1() {
	}

	public static Demo1 getInst() {
		if (inst == null) {
			inst = new Demo1();
		}
		return inst;
	}

}

/**
 * @Description 基于双重检查的单例
 * @author zch
 * @time 2018年7月20日 上午11:03:43
 * 
 */
class Demo2 {

	private static volatile Demo2 inst;

	private Demo2() {
	}

	public static Demo2 getInst() {
		if (inst == null) {
			synchronized (Demo2.class) {
				if (inst == null) {
					inst = new Demo2();
				}
			}
		}
		return inst;
	}
}

class Resource {
}

/**
 * @Description 枚举方式单例，effective java里面有介绍
 * @author zch
 * @time 2018年7月20日 上午11:14:16
 * 
 */
enum Demo3 {

	INSTANCE;

	private Resource instance;

	Demo3() {
		instance = new Resource();
	}

	public Resource getInstance() {
		return instance;
	}
}

/**
 * @Description 内部类实现单例。对象初始化过程中有隐藏的初始化锁。
 * @author zch
 * @time 2018年7月20日 上午11:21:14
 * 
 */
class Demo4 {
	private Demo4() {
	}

	public static Demo4 getInstance() {
		return Holder.inst;
	}

	private static class Holder {
		private static Demo4 inst = new Demo4();
	}
}