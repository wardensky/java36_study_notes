# Java并发类库提供的线程池有哪几种？ 分别有什么特点？


![图片](./images/executors.png)


###Java线程池学习

####Executor框架简介
 在Java 5之后，并发编程引入了一堆新的启动、调度和管理线程的API。Executor框架便是Java 5中引入的，其内部使用了线程池机制，它在java.util.cocurrent 包下，通过该框架来控制线程的启动、执行和关闭，可以简化并发编程的操作。因此，在Java 5之后，通过Executor来启动线程比使用Thread的start方法更好，除了更易管理，效率更好（用线程池实现，节约开销）外，还有关键的一点：有助于避免this逃逸问题——如果我们在构造器中启动一个线程，因为另一个任务可能会在构造器结束之前开始执行，此时可能会访问到初始化了一半的对象用Executor在构造器中。

Executor框架包括：线程池，Executor，Executors，ExecutorService，CompletionService，Future，Callable等。

Executor接口中之定义了一个方法execute（Runnable command），该方法接收一个Runable实例，它用来执行一个任务，任务即一个实现了Runnable接口的类。ExecutorService接口继承自Executor接口，它提供了更丰富的实现多线程的方法，比如，ExecutorService提供了关闭自己的方法，以及可为跟踪一个或多个异步任务执行状况而生成 Future 的方法。 可以调用ExecutorService的shutdown方法来平滑地关闭 ExecutorService，调用该方法后，将导致ExecutorService停止接受任何新的任务且等待已经提交的任务执行完成(已经提交的任务会分两类：一类是已经在执行的，另一类是还没有开始执行的)，当所有已经提交的任务执行完毕后将会关闭ExecutorService。因此我们一般用该接口来实现和管理多线程。



ExecutorService的生命周期包括三种状态：运行、关闭、终止。创建后便进入运行状态，当调用了shutdown方法时，便进入关闭状态，此时意味着ExecutorService不再接受新的任务，但它还在执行已经提交了的任务，当素有已经提交了的任务执行完后，便到达终止状态。如果不调用shutdown方法，ExecutorService会一直处在运行状态，不断接收新的任务，执行新的任务，服务器端一般不需要关闭它，保持一直运行即可。

Executors提供了一系列工厂方法用于创先线程池，返回的线程池都实现了ExecutorService接口。  


######可缓存的线程池
```
 public static ExecutorService newCachedThreadPool()
```
> - 创建一个可缓存的线程池，调用execute将重用以前构造的线程（如果线程可用）。如果现有线程没有可用的，则创建一个新线程并添加到池中。终止并从缓存中移除那些已有 60 秒钟未被使用的线程。
> - 缓存型池通常用于执行一些生存期很短的异步型任务, 因此在一些面向连接的daemon型SERVER中用得不多。但对于生存期短的异步任务，它是Executor的首选。-能reuse的线程，必须是timeout IDLE内的池中线程，缺省timeout是60s,超过这个IDLE时长，线程实例将被终止及移出池。注意，放入CachedThreadPool的线程不必担心其结束，超过TIMEOUT不活动，其会自动被终止。

######固定数目线程池
```
public static ExecutorService newFixedThreadPool(int nThreads)
```
>创建固定数目线程的线程池。

######单线程线程池
```
public static ExecutorService newSingleThreadExecutor()
```
> - 创建一个单线程化的Executor。newFixedThreadPool与cacheThreadPool差不多，也是能reuse就用，但不能随时建新的线程。
> - 其独特之处:任意时间点，最多只能有固定数目的活动线程存在，此时如果有新的线程要建立，只能放在另外的队列中等待，直到当前的线程中某个线程终止直接被移出池子。和cacheThreadPool不同，FixedThreadPool没有IDLE机制，所以FixedThreadPool多数针对一些很稳定很固定的正规并发线程，多用于服务器。
> - 从方法的源代码看，cache池和fixed池调用的是同一个底层池，只不过参数不同:fixed池线程数固定，并且是0秒IDLE（无IDLE）    
cache池线程数支持0-Integer.MAX_VALUE(显然完全没考虑主机的资源承受能力），60秒IDLE  


######定时及周期执行任务线程池
```
public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize)
```
> - 创建一个支持定时及周期性的任务执行的线程池，多数情况下可用来替代Timer类。单例线程，任意时间池中只能有一个线程。用的是和cache池和fixed池相同的底层池，但线程数目是1-1,0秒IDLE（无IDLE）。

**一般来说，CachedTheadPool在程序执行过程中通常会创建与所需数量相同的线程，然后在它回收旧线程时停止创建新线程，因此它是合理的Executor的首选，只有当这种方式会引发问题时（比如需要大量长时间面向连接的线程时），才需要考虑用FixedThreadPool。**

####Executor代码示例
目录
- newCachedThreadPool
- newFixedThreadPool
- newSingleThreadExecutor
- newScheduledThreadPool

##### newCachedThreadPool
源码
```
package com.chzhao.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewCachedThreadPoolTest {
	public static void main(String[] args) {
		ExecutorService executorService = Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++) {
			executorService.execute(new Runnable() {
				public void run() {
					try {
						Thread.sleep(1000 * 3);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(Thread.currentThread().getName()
							+ "线程被调用了。");
				}
			});
			System.out.println("************* a" + i + " *************");
		}
		executorService.shutdown();
	}
}

```
输出
```
************* a0 *************
************* a1 *************
************* a2 *************
************* a3 *************
************* a4 *************
pool-1-thread-1线程被调用了。
pool-1-thread-4线程被调用了。
pool-1-thread-3线程被调用了。
pool-1-thread-2线程被调用了。
pool-1-thread-5线程被调用了。

```
> 有几个任务启动几个线程

#####newFixedThreadPool
源码
```
package com.chzhao.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewFixedThreadPoolTest {
	public static void main(String[] args) {
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		for (int i = 0; i < 5; i++) {
			executorService.execute(new Runnable() {
				public void run() {
					try {
						Thread.sleep(1000 * 3);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(Thread.currentThread().getName()
							+ "线程被调用了。");
				}
			});
			System.out.println("************* a" + i + " *************");
		}
		executorService.shutdown();
	}
}

```
输出
```
************* a0 *************
************* a1 *************
************* a2 *************
************* a3 *************
************* a4 *************
pool-1-thread-1线程被调用了。
pool-1-thread-3线程被调用了。
pool-1-thread-2线程被调用了。
pool-1-thread-1线程被调用了。
pool-1-thread-3线程被调用了。
```

> 可以看到，虽然要执行5次，只起了3个线程。


#####newSingleThreadExecutor
源码
```
package com.chzhao.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewSingleThreadExecutorTest {
	public static void main(String[] args) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		for (int i = 0; i < 5; i++) {
			executorService.execute(new Runnable() {
				public void run() {
					try {
						Thread.sleep(1000 * 3);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(Thread.currentThread().getName()
							+ "线程被调用了。");
				}
			});
			System.out.println("************* a" + i + " *************");
		}
		executorService.shutdown();
	}
}

```
输出
```
************* a0 *************
************* a1 *************
************* a2 *************
************* a3 *************
************* a4 *************
pool-1-thread-1线程被调用了。
pool-1-thread-1线程被调用了。
pool-1-thread-1线程被调用了。
pool-1-thread-1线程被调用了。
pool-1-thread-1线程被调用了。

```
> 只有一个线程在运行


#####newScheduledThreadPool
源码
```
package com.chzhao.executor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class NewScheduledThreadPoolTest {
	public static void main(String[] args) {
		final ScheduledExecutorService job = Executors.newScheduledThreadPool(
				2, new ThreadFactory() {
					public Thread newThread(Runnable r) {
						return new Thread(r, "MY_THREAD");
					}
				});

		job.scheduleAtFixedRate(new Runnable() {
			public void run() {
				System.out
						.println(Thread.currentThread().getName() + "线程被调用了。");
			}
		}, 1, 1, TimeUnit.SECONDS);
	}
}
```
输出
```
MY_THREAD线程被调用了。
MY_THREAD线程被调用了。
MY_THREAD线程被调用了。
MY_THREAD线程被调用了。
MY_THREAD线程被调用了。
MY_THREAD线程被调用了。
MY_THREAD线程被调用了。
MY_THREAD线程被调用了。
```
> **注意，不能用shutdown**

####Callable和Future
在Java 5之后，任务分两类：一类是实现了Runnable接口的类，一类是实现了Callable接口的类。两者都可以被ExecutorService执行，但是Runnable任务没有返回值，而Callable任务有返回值。并且Callable的call()方法只能通过ExecutorService的submit(Callable<T> task) 方法来执行，并且返回一个 <T>Future<T>，是表示任务等待完成的 Future。

Callable接口类似于Runnable，两者都是为那些其实例可能被另一个线程执行的类设计的。但是 Runnable 不会返回结果，并且无法抛出经过检查的异常而Callable又返回结果，而且当获取返回结果时可能会抛出异常。Callable中的call()方法类似Runnable的run()方法，区别同样是有返回值，后者没有。

当将一个Callable的对象传递给ExecutorService的submit方法，则该call方法自动在一个线程上执行，并且会返回执行结果Future对象。同样，将Runnable的对象传递给ExecutorService的submit方法，则该run方法自动在一个线程上执行，并且会返回执行结果Future对象，但是在该Future对象上调用get方法，将返回null。

源码
```
package com.chzhao.future;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
	public static void main(String[] args) throws InterruptedException,
			ExecutionException {
		ExecutorService es = Executors.newFixedThreadPool(2);
		for (int i = 0; i < 5; i++) {
			Future<Integer> future = es.submit(new Callable<Integer>() {
				public Integer call() throws Exception {
					Integer ret = new Random().nextInt(100);
					System.out.println(Thread.currentThread().getName()
							+ "return : \t" + ret);
					return ret;
				}
			});
			System.out.println(future.get());
		}
		es.shutdown();
	}
}

```
输出
```
pool-1-thread-1return : 	34
34
pool-1-thread-2return : 	51
51
pool-1-thread-1return : 	54
54
pool-1-thread-2return : 	75
75
pool-1-thread-1return : 	35
35
```

参考：
[ 【Java并发编程】之十九：并发新特性—Executor框架与线程池（含代码）](http://blog.csdn.net/ns_code/article/details/17465497)
[Java线程(七)：Callable和Future](http://blog.csdn.net/ghsau/article/details/7451464)
