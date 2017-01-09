package com.gionee.wms.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程工具类.
 * 
 * @author Kevin
 */
public abstract class ThreadUtils {

	private static ExecutorService executorService = Executors.newFixedThreadPool(5);
	
	/**
	 * 自定义ThreadFactory, 可定制线程池的名称.
	 */
	public static class MyThreadFactory implements ThreadFactory {

		private final String poolPrefix;
		private final AtomicInteger threadNumber = new AtomicInteger(1);

		public MyThreadFactory(String poolName) {
			poolPrefix = poolName + "_pool";
		}

		@Override
		public Thread newThread(Runnable runable) {
			return new Thread(runable, poolPrefix + threadNumber.getAndIncrement());
		}
	}
	
	public static ExecutorService getThreadPool(){
		return executorService;
	}
}
