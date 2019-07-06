package com.example.demo;




import java.util.Date;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Configurable
@EnableScheduling
public class Schedule {
//    @Autowired
//    private CalendarDao1 calendarDao1;
    @Scheduled(cron = "0 30 11 ? * *")  //每一分钟执行一次
    
    public void m0() {
    	  Thread ct = Thread.currentThread();
    	System.out.println(ct.getName()+"==============="+new Date().toLocaleString());
    }
    
    @Scheduled(fixedRate=5000)  //每一分钟执行一次
    public void m2() {
    	 Thread ct = Thread.currentThread();
        System.out.println(ct.getName()+"M2==============="+new Date().toLocaleString());
    }
    
    
    @Scheduled(fixedRate=5000)  //每一分钟执行一次
    public void m3() {
    	 Thread ct = Thread.currentThread();
        System.out.println(ct.getName()+"M3==============="+new Date().toLocaleString());
    }
    
    
//    @Scheduled(cron = "${a}")  //每一分钟执行一次
//    public void m1() {
//        calendarDao1.calendarDelete(1);
//    }
}
