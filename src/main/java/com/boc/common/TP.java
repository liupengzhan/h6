package com.boc.common;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TP {
	public static void main(String[] args) throws InterruptedException {
		ArrayBlockingQueue<Runnable> abq=new ArrayBlockingQueue<Runnable>(5);
		LinkedBlockingQueue<Runnable> lbq=new LinkedBlockingQueue<Runnable>();
		//ThreadPoolExecutor executor=new ThreadPoolExecutor(5,10,200,TimeUnit.MILLISECONDS,abq);
		ThreadPoolExecutor executor=new ThreadPoolExecutor(10,10,200,TimeUnit.MILLISECONDS,lbq);
		for(int i=0;i<15;i++) {
			MyTask mt=new MyTask(i);
			executor.execute(mt);
			System.out.println("now pool size is "+executor.getPoolSize()+",waiting thread number is "+executor.getQueue().size()+",finished number is "+executor.getCompletedTaskCount());
			//Thread.sleep(1000);
		}
		
		while(executor.getActiveCount()>0)
		{
			System.out.println("now pool size is "+executor.getPoolSize()+",waiting thread number is "+executor.getQueue().size()+",finished number is "+executor.getCompletedTaskCount());
			Thread.sleep(2000);
		}
		
		
		
		executor.shutdown();
	}
}
