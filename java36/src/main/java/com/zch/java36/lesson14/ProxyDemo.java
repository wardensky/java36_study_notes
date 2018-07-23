package com.zch.java36.lesson14;

/**
 * @Description 代理模式
 * @author zch
 * @time 2018年7月23日 下午3:20:46
 * 
 */
public class ProxyDemo {
	public static void main(String[] args) {
		Star real = new RealStar();
		Star proxy = new ProxyStar(real);
		proxy.confer();
		proxy.signContract();
		proxy.bookTicket();
		proxy.sing();// 真实对象的操作（明星唱歌）
		proxy.collectMoney();
	}
}

interface Star {
	void confer();// 面谈

	void signContract();// 签合同

	void bookTicket();// 订票

	void sing();// 唱歌

	void collectMoney();// 收尾款
}

class ProxyStar implements Star {
	private Star star;// 真实对象的引用（明星）

	@Override
	public void confer() {
		System.out.println("ProxyStar.confer()");
	}

	@Override
	public void signContract() {
		System.out.println("ProxyStar.signContract()");
	}

	@Override
	public void bookTicket() {
		System.out.println("ProxyStar.bookTicket()");
	}

	@Override
	public void sing() {
		star.sing();// 真实对象的操作（明星唱歌）
	}

	@Override
	public void collectMoney() {
		System.out.println("ProxyStar.collectMoney()");
	}

	public ProxyStar(Star star) {// 通过构造器给真实角色赋值
		this.star = star;
	}
}

class RealStar implements Star {
	@Override
	public void confer() {
		System.out.println("RealStar.confer()");
	}

	@Override
	public void signContract() {
		System.out.println("RealStar.signContract()");
	}

	@Override
	public void bookTicket() {
		System.out.println("RealStar.bookTicket()");
	}

	@Override
	public void sing() {
		System.out.println("张学友.sing()");// 真实角色的操作：真正的业务逻辑
	}

	@Override
	public void collectMoney() {
		System.out.println("RealStar.collectMoney()");
	}
}