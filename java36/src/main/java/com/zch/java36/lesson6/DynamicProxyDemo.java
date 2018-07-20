package com.zch.java36.lesson6;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.Proxy;

public class DynamicProxyDemo {
	public static void main(String[] args) {
		BookFacadeProxy proxy = new BookFacadeProxy();
		BookFacade bookProxy = (BookFacade) proxy.bind(new BookFacadeImpl());
		bookProxy.addBook();
	}
}

interface BookFacade {
	public void addBook();
}

class BookFacadeImpl implements BookFacade {

	@Override
	public void addBook() {
		System.out.println("增加图书方法。。。");
	}
}

class BookFacadeProxy implements InvocationHandler {
	private Object target;

	/**
	 * 绑定委托对象并返回一个代理类
	 * 
	 * @param target
	 * @return
	 */
	public Object bind(Object target) {
		this.target = target;
		// 取得代理对象
		return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this); // 要绑定接口(这是一个缺陷，cglib弥补了这一缺陷)
	}

	@Override
	/**
	 * 调用方法
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = null;
		System.out.println("事物开始");
		// 执行方法
		result = method.invoke(target, args);
		System.out.println("事物结束");
		return result;
	}

}