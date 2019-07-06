package com.example.demo;


import java.util.Date;
import java.util.Map;

import org.apache.log4j.rolling.RollingFileAppender;

import com.boc.common.DateTool;
import com.boc.common.Log;
import com.boc.db.DBTool3;
public class TestLog {
	
	public static void main(String[] args) {
		Map m=DBTool3.getFirst(DBTool3.runsql("select * from t2"));
		Date stime=(Date) m.get("stime");
		Date etime=(Date) m.get("etime");
		Log.debug(DateTool.inSec(new Date(), stime, etime));
		//stime.get
	}
	
}
