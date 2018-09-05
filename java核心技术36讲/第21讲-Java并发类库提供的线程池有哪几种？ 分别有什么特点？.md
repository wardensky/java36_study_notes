# Java并发类库提供的线程池有哪几种？ 分别有什么特点？





## Executor框架简介

 在Java 5之后，并发编程引入了一堆新的启动、调度和管理线程的API。Executor框架便是Java 5中引入的，其内部使用了线程池机制，它在java.util.cocurrent 包下，通过该框架来控制线程的启动、执行和关闭，可以简化并发编程的操作。因此，在Java 5之后，通过Executor来启动线程比使用Thread的start方法更好，除了更易管理，效率更好（用线程池实现，节约开销）外，还有关键的一点：有助于避免this逃逸问题——如果我们在构造器中启动一个线程，因为另一个任务可能会在构造器结束之前开始执行，此时可能会访问到初始化了一半的对象用Executor在构造器中。

Executor框架包括：线程池，Executor，Executors，ExecutorService，CompletionService，Future，Callable等。下图说明了类关系。


![图片](./images/executors.png)

Executor接口中之定义了一个方法execute（Runnable command），该方法接收一个Runable实例，它用来执行一个任务，任务即一个实现了Runnable接口的类。ExecutorService接口继承自Executor接口，它提供了更丰富的实现多线程的方法，比如，ExecutorService提供了关闭自己的方法，以及可为跟踪一个或多个异步任务执行状况而生成 Future 的方法。 可以调用ExecutorService的shutdown方法来平滑地关闭 ExecutorService，调用该方法后，将导致ExecutorService停止接受任何新的任务且等待已经提交的任务执行完成(已经提交的任务会分两类：一类是已经在执行的，另一类是还没有开始执行的)，当所有已经提交的任务执行完毕后将会关闭ExecutorService。因此我们一般用该接口来实现和管理多线程。



ExecutorService的生命周期包括三种状态：运行、关闭、终止。创建后便进入运行状态，当调用了shutdown方法时，便进入关闭状态，此时意味着ExecutorService不再接受新的任务，但它还在执行已经提交了的任务，当素有已经提交了的任务执行完后，便到达终止状态。如果不调用shutdown方法，ExecutorService会一直处在运行状态，不断接收新的任务，执行新的任务，服务器端一般不需要关闭它，保持一直运行即可。

Executors提供了一系列工厂方法用于创先线程池，返回的线程池都实现了ExecutorService接口。  


### 可缓存的线程池

```
public static ExecutorService newCachedThreadPool()  
```
> - 创建一个可缓存的线程池，调用execute将重用以前构造的线程（如果线程可用）。如果现有线程没有可用的，则创建一个新线程并添加到池中。终止并从缓存中移除那些已有 60 秒钟未被使用的线程。
> - 缓存型池通常用于执行一些生存期很短的异步型任务, 因此在一些面向连接的daemon型SERVER中用得不多。但对于生存期短的异步任务，它是Executor的首选。-能reuse的线程，必须是timeout IDLE内的池中线程，缺省timeout是60s,超过这个IDLE时长，线程实例将被终止及移出池。注意，放入CachedThreadPool的线程不必担心其结束，超过TIMEOUT不活动，其会自动被终止。


**一般来说，CachedTheadPool在程序执行过程中通常会创建与所需数量相同的线程，然后在它回收旧线程时停止创建新线程，因此它是合理的Executor的首选，只有当这种方式会引发问题时（比如需要大量长时间面向连接的线程时），才需要考虑用FixedThreadPool。**

### 固定数目线程池

```
public static ExecutorService newFixedThreadPool(int nThreads)
```

>创建固定数目线程的线程池。

###  单线程线程池

```
public static ExecutorService newSingleThreadExecutor()
```
> - 创建一个单线程化的Executor。newFixedThreadPool与cacheThreadPool差不多，也是能reuse就用，但不能随时建新的线程。
> - 其独特之处:任意时间点，最多只能有固定数目的活动线程存在，此时如果有新的线程要建立，只能放在另外的队列中等待，直到当前的线程中某个线程终止直接被移出池子。和cacheThreadPool不同，FixedThreadPool没有IDLE机制，所以FixedThreadPool多数针对一些很稳定很固定的正规并发线程，多用于服务器。
> - 从方法的源代码看，cache池和fixed池调用的是同一个底层池，只不过参数不同:fixed池线程数固定，并且是0秒IDLE（无IDLE）    
cache池线程数支持0-Integer.MAX_VALUE(显然完全没考虑主机的资源承受能力），60秒IDLE  


###  定时及周期执行任务线程池

```
public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize)
```
> - 创建一个支持定时及周期性的任务执行的线程池，多数情况下可用来替代Timer类。单例线程，任意时间池中只能有一个线程。用的是和cache池和fixed池相同的底层池，但线程数目是1-1,0秒IDLE（无IDLE）。

### 并行执行任务线程池

```
public static ExecutorService newWorkStealingPool(int parallelism)
```
> - 这是一个经常被人忽略的线程池，Java 8 才加入这个创建方法，其内部会构建ForkJoinPool，利用Work-Stealing算法，并行地处理任务，不保证处理顺序。


## 相关源码分析


### 静态方法源码

先看看上述几个静态方法的源码。

```
/**
 * Creates a thread pool that creates new threads as needed, but
 * will reuse previously constructed threads when they are
 * available.  These pools will typically improve the performance
 * of programs that execute many short-lived asynchronous tasks.
 * Calls to {@code execute} will reuse previously constructed
 * threads if available. If no existing thread is available, a new
 * thread will be created and added to the pool. Threads that have
 * not been used for sixty seconds are terminated and removed from
 * the cache. Thus, a pool that remains idle for long enough will
 * not consume any resources. Note that pools with similar
 * properties but different details (for example, timeout parameters)
 * may be created using {@link ThreadPoolExecutor} constructors.
 *
 * @return the newly created thread pool
 */
public static ExecutorService newCachedThreadPool() {
    return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                  60L, TimeUnit.SECONDS,
                                  new SynchronousQueue<Runnable>());
}

```


```
/**
 * Creates a thread pool that reuses a fixed number of threads
 * operating off a shared unbounded queue.  At any point, at most
 * {@code nThreads} threads will be active processing tasks.
 * If additional tasks are submitted when all threads are active,
 * they will wait in the queue until a thread is available.
 * If any thread terminates due to a failure during execution
 * prior to shutdown, a new one will take its place if needed to
 * execute subsequent tasks.  The threads in the pool will exist
 * until it is explicitly {@link ExecutorService#shutdown shutdown}.
 *
 * @param nThreads the number of threads in the pool
 * @return the newly created thread pool
 * @throws IllegalArgumentException if {@code nThreads <= 0}
 */
public static ExecutorService newFixedThreadPool(int nThreads) {
    return new ThreadPoolExecutor(nThreads, nThreads,
                                  0L, TimeUnit.MILLISECONDS,
                                  new LinkedBlockingQueue<Runnable>());
}
```


```

/**
 * Creates an Executor that uses a single worker thread operating
 * off an unbounded queue. (Note however that if this single
 * thread terminates due to a failure during execution prior to
 * shutdown, a new one will take its place if needed to execute
 * subsequent tasks.)  Tasks are guaranteed to execute
 * sequentially, and no more than one task will be active at any
 * given time. Unlike the otherwise equivalent
 * {@code newFixedThreadPool(1)} the returned executor is
 * guaranteed not to be reconfigurable to use additional threads.
 *
 * @return the newly created single-threaded Executor
 */
public static ExecutorService newSingleThreadExecutor() {
    return new FinalizableDelegatedExecutorService
        (new ThreadPoolExecutor(1, 1,
                                0L, TimeUnit.MILLISECONDS,
                                new LinkedBlockingQueue<Runnable>()));
}

```


```
/**
 * Creates a thread pool that can schedule commands to run after a
 * given delay, or to execute periodically.
 * @param corePoolSize the number of threads to keep in the pool,
 * even if they are idle
 * @return a newly created scheduled thread pool
 * @throws IllegalArgumentException if {@code corePoolSize < 0}
 */
public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
    return new ScheduledThreadPoolExecutor(corePoolSize);
}

```


```
/**
 * Creates a thread pool that maintains enough threads to support
 * the given parallelism level, and may use multiple queues to
 * reduce contention. The parallelism level corresponds to the
 * maximum number of threads actively engaged in, or available to
 * engage in, task processing. The actual number of threads may
 * grow and shrink dynamically. A work-stealing pool makes no
 * guarantees about the order in which submitted tasks are
 * executed.
 *
 * @param parallelism the targeted parallelism level
 * @return the newly created thread pool
 * @throws IllegalArgumentException if {@code parallelism <= 0}
 * @since 1.8
 */
public static ExecutorService newWorkStealingPool(int parallelism) {
    return new ForkJoinPool
        (parallelism,
         ForkJoinPool.defaultForkJoinWorkerThreadFactory,
         null, true);
}

```


从上面的源码可以知道：

- newCachedThreadPool、newFixedThreadPool和newSingleThreadExecutor都是通过ThreadPoolExecutor实现的。
- newScheduledThreadPool通过ScheduledThreadPoolExecutor实现。
- newWorkStealingPool通过newWorkStealingPool实现。

### ThreadPoolExecutor源码

看看ThreadPoolExecutor的构造函数

```
/**
 * Creates a new {@code ThreadPoolExecutor} with the given initial
 * parameters and default thread factory and rejected execution handler.
 * It may be more convenient to use one of the {@link Executors} factory
 * methods instead of this general purpose constructor.
 *
 * @param corePoolSize the number of threads to keep in the pool, even
 *        if they are idle, unless {@code allowCoreThreadTimeOut} is set
 * @param maximumPoolSize the maximum number of threads to allow in the
 *        pool
 * @param keepAliveTime when the number of threads is greater than
 *        the core, this is the maximum time that excess idle threads
 *        will wait for new tasks before terminating.
 * @param unit the time unit for the {@code keepAliveTime} argument
 * @param workQueue the queue to use for holding tasks before they are
 *        executed.  This queue will hold only the {@code Runnable}
 *        tasks submitted by the {@code execute} method.
 * @throws IllegalArgumentException if one of the following holds:<br>
 *         {@code corePoolSize < 0}<br>
 *         {@code keepAliveTime < 0}<br>
 *         {@code maximumPoolSize <= 0}<br>
 *         {@code maximumPoolSize < corePoolSize}
 * @throws NullPointerException if {@code workQueue} is null
 */
public ThreadPoolExecutor(int corePoolSize,
                          int maximumPoolSize,
                          long keepAliveTime,
                          TimeUnit unit,
                          BlockingQueue<Runnable> workQueue) {
    this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
         Executors.defaultThreadFactory(), defaultHandler);
}
```

有如下几个关键参数：

- corePoolSize 核心池大小，在默认情况下线程池大小是这个。
- maximumPoolSize  池最大值，可以扩展的大小
- keepAliveTime   一个线程idle的诗句
- unit          上述值的单位
- workQueue    工作队列，在进入池之前，先放置到此队列中，有两种常见队列
  - SynchronousQueue 是一个没有数据缓冲的BlockingQueue，生产者线程对其的插入操作put必须等待消费者的移除操作take，反过来也一样。即此队列的大小为0。
  - LinkedBlockingQueue 由于LinkedBlockingQueue实现是线程安全的，实现了先进先出等特性，是作为生产者消费者的首选，LinkedBlockingQueue 可以指定容量，也可以不指定，不指定的话，默认最大是Integer.MAX_VALUE，其中主要用到put和take方法，put方法在队列满的时候会阻塞直到有队列成员被消费，take方法在队列空的时候会阻塞，直到有队列成员被放进来。


在上面的源码中：
- newCachedThreadPool
  - corePoolSize = 0
  - maximumPoolSize = Integer.MAX_VALUE
  - keepAliveTime = 60
  - unit = 秒
  - workQueue = SynchronousQueue
- newFixedThreadPool
  - corePoolSize = nThreads
  - maximumPoolSize = nThreads
  - keepAliveTime = 0
  - unit = TimeUnit.MILLISECONDS
  - workQueue = LinkedBlockingQueue
- newSingleThreadExecutor
  - corePoolSize = 1
  - maximumPoolSize = 1
  - keepAliveTime = 0
  - unit = TimeUnit.MILLISECONDS
  - workQueue = LinkedBlockingQueue

可以看出，
- newCachedThreadPool可以重用线程，等待队列为空，线程数可以扩展到最大。
- newFixedThreadPool不重用线程，核心池和最大池一样。
- newSingleThreadExecutor是newFixedThreadPool参数为1的情况。


也可以自己调用ThreadPoolExecutor生成Executor


### ScheduledThreadPoolExecutor和ForkJoinPool

这两个分别是按计划调用和并行调用。

```
/**
 * Creates a new {@code ThreadPoolExecutor} with the given initial
 * parameters and default thread factory and rejected execution handler.
 * It may be more convenient to use one of the {@link Executors} factory
 * methods instead of this general purpose constructor.
 *
 * @param corePoolSize the number of threads to keep in the pool, even
 *        if they are idle, unless {@code allowCoreThreadTimeOut} is set
 * @param maximumPoolSize the maximum number of threads to allow in the
 *        pool
 * @param keepAliveTime when the number of threads is greater than
 *        the core, this is the maximum time that excess idle threads
 *        will wait for new tasks before terminating.
 * @param unit the time unit for the {@code keepAliveTime} argument
 * @param workQueue the queue to use for holding tasks before they are
 *        executed.  This queue will hold only the {@code Runnable}
 *        tasks submitted by the {@code execute} method.
 * @throws IllegalArgumentException if one of the following holds:<br>
 *         {@code corePoolSize < 0}<br>
 *         {@code keepAliveTime < 0}<br>
 *         {@code maximumPoolSize <= 0}<br>
 *         {@code maximumPoolSize < corePoolSize}
 * @throws NullPointerException if {@code workQueue} is null
 */
public ThreadPoolExecutor(int corePoolSize,
                          int maximumPoolSize,
                          long keepAliveTime,
                          TimeUnit unit,
                          BlockingQueue<Runnable> workQueue) {
    this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
         Executors.defaultThreadFactory(), defaultHandler);
}

```

```
/**
 * Creates a {@code ForkJoinPool} with the given parameters.
 *
 * @param parallelism the parallelism level. For default value,
 * use {@link java.lang.Runtime#availableProcessors}.
 * @param factory the factory for creating new threads. For default value,
 * use {@link #defaultForkJoinWorkerThreadFactory}.
 * @param handler the handler for internal worker threads that
 * terminate due to unrecoverable errors encountered while executing
 * tasks. For default value, use {@code null}.
 * @param asyncMode if true,
 * establishes local first-in-first-out scheduling mode for forked
 * tasks that are never joined. This mode may be more appropriate
 * than default locally stack-based mode in applications in which
 * worker threads only process event-style asynchronous tasks.
 * For default value, use {@code false}.
 * @throws IllegalArgumentException if parallelism less than or
 *         equal to zero, or greater than implementation limit
 * @throws NullPointerException if the factory is null
 * @throws SecurityException if a security manager exists and
 *         the caller is not permitted to modify threads
 *         because it does not hold {@link
 *         java.lang.RuntimePermission}{@code ("modifyThread")}
 */
public ForkJoinPool(int parallelism,
                    ForkJoinWorkerThreadFactory factory,
                    UncaughtExceptionHandler handler,
                    boolean asyncMode) {
    this(checkParallelism(parallelism),
         checkFactory(factory),
         handler,
         asyncMode ? FIFO_QUEUE : LIFO_QUEUE,
         "ForkJoinPool-" + nextPoolId() + "-worker-");
    checkPermission();
}
```

##  Executor代码示例


### newCachedThreadPool

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

### newFixedThreadPool

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


### newSingleThreadExecutor

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


### newScheduledThreadPool

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

## Callable和Future

在Java 5之后，任务分两类：一类是实现了Runnable接口的类，一类是实现了Callable接口的类。两者都可以被ExecutorService执行，但是Runnable任务没有返回值，而Callable任务有返回值。并且Callable的call()方法只能通过ExecutorService的submit(Callable<T> task) 方法来执行，并且返回一个 <T>Future<T>，是表示任务等待完成的 Future。

Callable接口类似于Runnable，两者都是为那些其实例可能被另一个线程执行的类设计的。但是 Runnable 不会返回结果，并且无法抛出经过检查的异常而Callable又返回结果，而且当获取返回结果时可能会抛出异常。Callable中的call()方法类似Runnable的run()方法，区别同样是有返回值，后者没有。

当将一个Callable的对象传递给ExecutorService的submit方法，则该call方法自动在一个线程上执行，并且会返回执行结果Future对象。同样，将Runnable的对象传递给ExecutorService的submit方法，则该run方法自动在一个线程上执行，并且会返回执行结果Future对象，但是在该Future对象上调用get方法，将返回null。


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

## 参考
- [ 【Java并发编程】之十九：并发新特性—Executor框架与线程池（含代码）](http://blog.csdn.net/ns_code/article/details/17465497)
- [Java线程(七)：Callable和Future](http://blog.csdn.net/ghsau/article/details/7451464)
