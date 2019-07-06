package com.boc.common;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.boc.db.DBTool1;

public class NumTool {
	
	public static float  cmpInteger(Integer i1,Integer i2) {
		//System.out.println(i1/i2);
		Float f1=new Float(i1);
		Float f2=new Float(i2);
		return Math.abs(1-f1/f2);		
	}
	
	
	public static float  cmpDecimal(BigDecimal i1,BigDecimal i2) {
		System.out.println(i1.divide(i2,4,BigDecimal.ROUND_UP));
		return Math.abs(1-i1.divide(i2,4,BigDecimal.ROUND_UP).floatValue());		
	}
	
	public static void main(String[] args) {
		String t="SELECT * FROM alarm_setting WHERE alarmid='E124' AND  times='22:50'  AND TYPE='DAY'";
		List<Map> lm=DBTool1.runsql(t);
		BigDecimal f=(BigDecimal)lm.get(0).get("txnRatioSuc");
		System.out.println(cmpDecimal(new BigDecimal(99),new BigDecimal(90)));
		
 
	}
	
	public static BigDecimal toBigDecimal(Object o0) {
		BigDecimal bg = new BigDecimal(0);
		//bg = (BigDecimal)o0;
			if (o0 instanceof Integer)
				bg = BigDecimal.valueOf(((Integer) o0).longValue());
			if (o0 instanceof BigDecimal)
				bg = (BigDecimal) o0;
			if (o0 instanceof Float)
				bg = BigDecimal.valueOf(((Float) o0).longValue());
			if (o0 instanceof Long)
				bg = BigDecimal.valueOf(((Long) o0).longValue());
			if (o0 instanceof Double)
				bg = BigDecimal.valueOf(((Double) o0).longValue());
			if (o0 instanceof BigDecimal)
				bg=(BigDecimal)o0;
			return bg;
	}
	
	

}
