package com.zch.java36.lesson19;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExchangerDemo {
	private static final Exchanger<String> exgr = new Exchanger<String>();
	private static ExecutorService threadPool = Executors.newFixedThreadPool(2);

	public static void main(String[] args) {
		threadPool.execute(new Task5(exgr));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadPool.execute(new Task6(exgr));
		threadPool.shutdown();
	}
}

class Task5 extends Thread {
	private Exchanger<String> exchanger;

	public Task5(Exchanger<String> latch) {
		this.exchanger = latch;
	}

	@Override
	public void run() {
		try {
			String A = "我是task5";
			String value = exchanger.exchange(A);
			System.out.println("task5 value = " + value);
		} catch (Exception ex) {
		} finally {

		}
	}

}

class Task6 extends Thread {
	private Exchanger<String> exchanger;

	public Task6(Exchanger<String> latch) {
		this.exchanger = latch;
	}

	@Override
	public void run() {
		try {
			String A = "我是task6啊啊啊啊";

			String value = exchanger.exchange(A);

			System.out.println("task6 value = " + value);
		} catch (Exception ex) {
		} finally {

		}
	}
}