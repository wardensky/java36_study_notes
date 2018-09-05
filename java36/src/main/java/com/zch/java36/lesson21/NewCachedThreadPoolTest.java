package com.zch.java36.lesson21;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewCachedThreadPoolTest {

	public static void main(String[] args) {
		int sleepTime = 1;
		ExecutorService executorService = Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++) {
			executorService.execute(new Runnable() {
				public void run() {
					try {
						Thread.sleep(1000 * sleepTime );
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(Thread.currentThread().getName() + "线程被调用了。");
				}
			});
			System.out.println("************* a" + i + " *************");
		}
		executorService.shutdown();
	}
}
