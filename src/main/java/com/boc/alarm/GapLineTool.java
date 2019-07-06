package com.boc.alarm;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.boc.common.DateTool;
import com.boc.common.NumTool;
import com.boc.db.DBTool1;

public class GapLineTool {
	public static void printGap(String sql,String col) {
		List<Map> lm=DBTool1.runsql(sql);
		BigDecimal bd=new BigDecimal(0);
		BigDecimal last=new BigDecimal(0);
		for(Map m:lm) {
		   	bd=NumTool.toBigDecimal(m.get(col));
		   	Date d=(Date)m.get("pollTime");
		   	System.out.println(DateTool.toStringMm1(d)+",  "+bd.subtract(last));
		   	last=bd;
		}
	}
	
	public static void main(String[] args) {
		printGap("select pollTime,txnVol from trans_tbl_scs where date(pollTime)='2019-06-25'","txnVol");
		
		
		
	}

}
