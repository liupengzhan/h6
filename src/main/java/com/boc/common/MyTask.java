package com.boc.common;

public class MyTask implements Runnable {
	
	private int tasknum;
	
	public MyTask(int tasknum) {
		this.tasknum=tasknum;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("task "+tasknum+" now running.. "+tasknum);
		
			try {
				for(int i=1;i<10;i++) {
				Thread.currentThread().sleep(1000);
				System.out.println("task "+tasknum+" now complete.. "+i+"0%");
				
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("task "+tasknum+" is endding! "+tasknum);
		

	}

}
