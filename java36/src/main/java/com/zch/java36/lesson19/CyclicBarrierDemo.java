package com.zch.java36.lesson19;

import java.util.Date;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {
	private static final CyclicBarrier cb = new CyclicBarrier(3);

	public static void main(String[] args) {
		Task3 t3 = new Task3(cb);
		Task4 t4 = new Task4(cb);
		t3.start();
		t4.start();
		try {
			cb.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
		System.out.println(CountDownLatchDemo.df.format(new Date()) + " 所有任务完成");
	}
}

class Task3 extends Thread {
	private CyclicBarrier barrier;

	public Task3(CyclicBarrier latch) {
		this.barrier = latch;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
			this.barrier.await();
		} catch (Exception ex) {
		} finally {
			System.out.println(CountDownLatchDemo.df.format(new Date()) + " task3完成");

		}
	}

}

class Task4 extends Thread {
	private CyclicBarrier barrier;

	public Task4(CyclicBarrier latch) {
		this.barrier = latch;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(3000);
			this.barrier.await();
		} catch (Exception ex) {
		} finally {
			System.out.println(CountDownLatchDemo.df.format(new Date()) + " task4完成");

		}
	}
}