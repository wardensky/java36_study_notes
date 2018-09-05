package com.zch.java36.lesson21;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureTest {
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		ExecutorService es = Executors.newFixedThreadPool(2);
		for (int i = 0; i < 5; i++) {
			Future<Integer> future = es.submit(new Callable<Integer>() {
				public Integer call() throws Exception {
					Integer ret = new Random().nextInt(100);
					System.out.println(Thread.currentThread().getName() + "return : \t" + ret);
					return ret;
				}
			});
			System.out.println(future.get());
		}
		es.shutdown();
	}
}
