package com.boc.common;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.boc.demo.SomethingRunnable;

@Component
@Configuration
public class ScheduleConfig implements SchedulingConfigurer {

	private volatile ScheduledTaskRegistrar taskRegistrar = null;

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		// TODO Auto-generated method stub
		// taskRegistrar.setScheduler(taskExecutor());
		this.taskRegistrar = taskRegistrar;
		System.out.println("******************************************config");
		addScheduledTask();

	}

	private Executor taskExecutor() {
		return Executors.newScheduledThreadPool(100);
	}

	public void addScheduledTask() {
		// TODO: 取消原定时任务

		// 获取任务调度器
		System.out.println("in add sechedule task()");
		TaskScheduler scheduler = taskRegistrar.getScheduler();
		if (scheduler == null) {
			//scheduler = new DefaultManagedTaskScheduler();
			//scheduler=new ThreadPoolTaskScheduler();
			ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
			executor.setPoolSize(20);
			executor.setThreadNamePrefix("taskExecutor-");
			executor.setWaitForTasksToCompleteOnShutdown(true);
			executor.setAwaitTerminationSeconds(60);
			executor.initialize();
			taskRegistrar.setTaskScheduler(executor);
			
			scheduler=executor;
		}
		

		// 创建Runnable对象（或实现Runnable接口的对象）
		SomethingRunnable task = new SomethingRunnable();
		task.setTdata("task1");
		
		String con="0/1 * * * * ?";
		// 调度任务对象（以固定延迟类型为例）
		long intervalMS = 5 * 1000;
		Date startDate = new Date(System.currentTimeMillis() + intervalMS);
		scheduler.schedule(task,new CronTrigger(con));
		
		SomethingRunnable task2 = new SomethingRunnable();
		task2
		 .setTdata("task2");
		scheduler.scheduleAtFixedRate(task2, 1*1000);

		

	}

}
