package com.example.threads;


import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController; 

/**

 * @author Angel --守护天使

 * @version v.0.1

 * @date 2017年4月6日

 */

@RestController
@Component
public class DynamicTask1 { 

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler; 

    private ScheduledFuture<?> future; 

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
       return new ThreadPoolTaskScheduler();
    } 

    @RequestMapping("/startCron")
    public String startCron() {

       future = threadPoolTaskScheduler.schedule(new MyRunnable(), new CronTrigger("0/5 * * * * *"));
       System.out.println("DynamicTask.startCron()");
       return "startCron";
    } 

    @RequestMapping("/stopCron")
    public String stopCron() {

       if (future != null) {
           future.cancel(true);
       }
       System.out.println("DynamicTask.stopCron()");
       return "stopCron";
    }

    @RequestMapping("/changeCron10")
    public String startCron10() {

       stopCron();// 先停止，在开启.
       future = threadPoolTaskScheduler.schedule(new MyRunnable(), new CronTrigger("*/10 * * * * *"));
       System.out.println("DynamicTask.startCron10()");
       return "changeCron10";
    }

    private class MyRunnable implements Runnable {

       @Override
       public void run() {
           System.out.println("DynamicTask.MyRunnable.run()，" + new Date());
       }
    } 
}


