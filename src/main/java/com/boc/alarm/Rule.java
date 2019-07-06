package com.boc.alarm;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;

public interface  Rule {

	
	public abstract void init(String rid);
	
	public abstract Boolean  check(Date polltime,BigDecimal current1);
	
	public abstract String  getInfo();

    public abstract LinkedList<BigDecimal> getHis();
    
    public String getRulerid();
	
    public String getSys();
	
	

	
	

}
