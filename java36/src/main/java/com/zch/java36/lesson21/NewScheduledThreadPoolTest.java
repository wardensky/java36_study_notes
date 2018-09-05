package com.zch.java36.lesson21;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class NewScheduledThreadPoolTest {
	public static void main(String[] args) {
		final ScheduledExecutorService job = Executors.newScheduledThreadPool(2, new ThreadFactory() {
			public Thread newThread(Runnable r) {
				return new Thread(r, "MY_THREAD");
			}
		});

		job.scheduleAtFixedRate(new Runnable() {
			public void run() {
				System.out.println(Thread.currentThread().getName() + "线程被调用了。");
			}
		}, 1, 1, TimeUnit.SECONDS);
	}
}
