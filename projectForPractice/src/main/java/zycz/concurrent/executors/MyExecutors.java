package zycz.concurrent.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * * 
 * @ClassName MyExecutors.java
 * <p>Description: java.util.concurrent.Executors类
 * 提供了一些列静态方法创建线程池，返回的线程池都实现了ExecutorService接口。
 * 源码里AbstractExecutorService类实现了ExecutorService接口，
 * ThreadPoolExecutor继承AbstractExecutorService。</p>
 * @author Administrator
 * @date 2017年11月17日 上午10:37:41
 */
public class MyExecutors {
	public static void main(String[] args) {
		//ThreadPoolExecutor的构造方法,后两个参数是可选的
//		ThreadPoolExecutor pool = new ThreadPoolExecutor(
//									int corePoolSize, 
//									int maximumPoolSize, 
//									long keepAliveTime,
//									TimeUnit unit, 
//									BlockingQueue<Runnable> workQueue, 
//									ThreadFactory threadFactory, 
//									RejectedExecutionHandler handler);
		//用定时线程池模拟心跳
		ScheduledExecutorService scheduledExecute = Executors.newScheduledThreadPool(5);
		//建任务
		Runnable task = new Runnable() {
			
			@Override
			public void run() {
				System.out.println("心跳。。。。");
			}
		};
		/*scheduleAtFixedRate方法的参数initialDelay代表延迟多长时间开始执行，period代表执行后间隔多长时间执行
		public ScheduledFuture<?> scheduleAtFixedRate(Runnable command,  
                long initialDelay,  
                long period,  
                TimeUnit unit)*/
		//command执行周期会影响period，比如command执行完需要5秒，下面这个period=5，其实相当于间隔10秒
		scheduledExecute.scheduleAtFixedRate(task, 3, 5, TimeUnit.SECONDS);
	}
	
	/*** 
	 * 方法名 : newCacheThreadPool()(无界线程池，可以进行自动线程回收)  
	 * <p>Description: 创建可缓存的线程池，如果线程池中的线程在60秒未被使用就将被移除，
	 * 在执行新的任务时，当线程池中有之前创建的可用线程就重用可用线程，否则就新建一条线程</p>
	 * @author yu.zhang
	 * @date 2017年11月17日 上午10:45:11
	 * @version 1.0 
	 * @return ExecutorService
	 * <p>修改履历：</p>
	 */
	public static ExecutorService newCacheThreadPool() {
		//Executors.newCachedThreadPool()的源码里就是keepAliveTime=60,代表线程数大于corePoolSize
		//时最大空闲
//		return Executors.newCachedThreadPool();
		//SynchronousQueue队列内部仅允许容纳一个元素。当一个线程插入一个元素后会被阻塞，除非这个元素被另一个线程消费
		return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
	}
	
	/*** 
	 * 方法名 : newFixedThreadPool()(固定大小线程池)
	 * <p>Description: 创建可重用且固定线程数的线程池，如果线程池中的所有线程都处于活动状态，
	 * 此时再提交任务就在队列中等待，直到有可用线程；如果线程池中的某个线程由于异常而结束时，线程池
	 * 就会再补充一条新线程。</p>
	 * @author yu.zhang
	 * @date 2017年11月17日 上午11:11:33
	 * @version 1.0 
	 * @param paramInt
	 * @return ExecutorService
	 * <p>修改履历：</p>
	 */
	public static ExecutorService newFixedThreadPool(int paramInt) {
		return Executors.newFixedThreadPool(paramInt);
//		return new ThreadPoolExecutor(paramInt, paramInt, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
	}
	/*** 
	 * 方法名 : newSingleThreadPool()(单个后台线程)
	 * <p>Description: 创建一个单线程的Executor，如果该线程因为异常而结束就新建一
	 * 条线程来继续执行后续的任务</p>
	 * @author yu.zhang
	 * @date 2017年11月17日 下午1:48:47
	 * @version 1.0 
	 * @return ExecutorService
	 * <p>修改履历：</p>
	 */
	public static ExecutorService newSingleThreadPool() {
		return Executors.newSingleThreadExecutor();
//		return new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
	}
	/*** 
	 * 方法名 : newScheduleThreadPool()(定时执行线程池,无边界)
	 * <p>Description: 创建一个可延迟执行或定期执行的线程池</p>
	 * @author yu.zhang 
	 * @date 2017年11月17日 下午2:27:59
	 * @version 1.0 
	 * @return ExecutorService
	 * <p>修改履历：</p>
	 */
	public static ExecutorService newScheduleThreadPool(){
		//5是初始coreSizePool,即核心5条线程
		return Executors.newScheduledThreadPool(5);
//		return new ThreadPoolExecutor(5, Integer.MAX_VALUE, 0L, TimeUnit.NANOSECONDS, new DelayedWorkQueue());
	}
}
