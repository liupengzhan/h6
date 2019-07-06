package com.boc.alarm;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.boc.common.NumTool;
import com.boc.db.DBTool1;

public class Simulator {
	
	public static void runRule(String rules,String measuresql) {
		List<Map> lm=DBTool1.runsql(measuresql);
		for(Map m:lm) {
			BigDecimal bd=NumTool.toBigDecimal(m.get("value"));
			Date polltime=(Date)m.get("pollTime");
			RuleTool.checklist(polltime,bd,SystemChecker.getLr(rules),"OR");

		}
		
	}
	
	
	public static void runSys(String sys,Date d) {
		SystemChecker sc=SystemChecker.getInstance(sys);
		
		
	}
	
	
	

}
