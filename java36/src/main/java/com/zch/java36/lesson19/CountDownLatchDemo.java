package com.zch.java36.lesson19;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {
	public static SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

	public static void main(String[] args) {
		CountDownLatch latch = new CountDownLatch(2);
		Task1 t1 = new Task1(latch);
		Task2 t2 = new Task2(latch);
		t1.start();
		t2.start();

		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println(df.format(new Date()) + " 所有任务完成");
	}

}

class Task1 extends Thread {
	private CountDownLatch latch;

	public Task1(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(1000);

		} catch (Exception ex) {
		} finally {
			System.out.println(CountDownLatchDemo.df.format(new Date()) + " task1完成");
			this.latch.countDown();
		}
	}

}

class Task2 extends Thread {
	private CountDownLatch latch;

	public Task2(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(3000);

		} catch (Exception ex) {
		} finally {
			System.out.println(CountDownLatchDemo.df.format(new Date()) + " task2完成");
			this.latch.countDown();
		}
	}
}