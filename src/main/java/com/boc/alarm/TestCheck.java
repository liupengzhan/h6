package com.boc.alarm;

import java.util.Date;

import com.boc.common.NumTool;

public class TestCheck {
	public static void main(String[] args) {
		Rule r=GapRuler.getInstance("scs");
		for(Integer i=0;i<10;i++) {
			System.out.println(r.check(new Date(),NumTool.toBigDecimal(i)));
		}
	}

}
