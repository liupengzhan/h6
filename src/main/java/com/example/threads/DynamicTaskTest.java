package com.example.threads;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;


public class DynamicTaskTest {

   
    private DynamicTask dynamicTask=new DynamicTask();

   
    public void test() throws InterruptedException {
        List<DynamicTask.TaskConstant> taskConstans = dynamicTask.getTaskConstants();
        DynamicTask.TaskConstant taskConstant = new DynamicTask.TaskConstant();
        taskConstant.setCron("0/5 * * * * ?");
        taskConstant.setTaskId("test1");
        taskConstans.add(taskConstant);


        DynamicTask.TaskConstant taskConstant1 = new DynamicTask.TaskConstant();
        taskConstant1.setCron("0/5 * * * * ?");
        taskConstant1.setTaskId("test2");
        taskConstans.add(taskConstant1);

        DynamicTask.TaskConstant taskConstant2 = new DynamicTask.TaskConstant();
        taskConstant2.setCron("0/5 * * * * ?");
        taskConstant2.setTaskId("test3");
        taskConstans.add(taskConstant2);

        TimeUnit.SECONDS.sleep(40);
        //移除并添加新的配置
        taskConstans.remove(taskConstans.size() - 1);
        DynamicTask.TaskConstant taskConstant3 = new DynamicTask.TaskConstant();
        taskConstant3.setCron("0/5 * * * * ?");
        taskConstant3.setTaskId("test4");
        taskConstans.add(taskConstant3);
        
         //dynamicTask.
//
        TimeUnit.MINUTES.sleep(50);
    }
    
    public static void main(String[] args) throws InterruptedException {
    	new DynamicTaskTest().test();
    	
    }
}