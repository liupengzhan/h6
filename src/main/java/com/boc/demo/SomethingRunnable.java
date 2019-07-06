package com.boc.demo;

public class SomethingRunnable implements Runnable {
	
	String tdata="";
	
	

	public String getTdata() {
		return tdata;
	}



	public void setTdata(String tdata) {
		this.tdata = tdata;
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
		 Thread ct = Thread.currentThread();
		 process(tdata);
		System.out.println(ct.getName()+"=============somthing runnable");

	}



	private void process(String tdata2) {
		System.out.println("tdata is"+tdata);
		
	}

}
