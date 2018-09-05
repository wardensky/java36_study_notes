package com.zch.java36.lesson19;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreDemo {

	private static final int COUNT = 10;
	private static Semaphore s = new Semaphore(5);

	private static ExecutorService es = Executors.newFixedThreadPool(COUNT);

	public static void main(String[] args) {
		for (int i = 0; i < COUNT * 2; i++) {
			es.execute(new Runnable() {

				@Override
				public void run() {
					try {
						s.acquire();
						Thread.sleep(2000);
						System.out.println(CountDownLatchDemo.df.format(new Date()) + " save data");
						s.release();
					} catch (InterruptedException e) {

					}
				}
			});
		}
		es.shutdown();
	}
}
