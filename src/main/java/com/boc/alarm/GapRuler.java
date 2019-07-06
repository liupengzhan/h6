package com.boc.alarm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jboss.logging.Logger;

import com.boc.db.DBTool1;

public class GapRuler implements Rule {
	private static final String SEC_BASELINE_BVALUE = "bvalue";
	static HashMap<String, Rule> maps = new HashMap<String, Rule>();
//	String rid;
	Integer gap = 0;// 波动值的大小
	Integer continues = 1;// 与前continues分钟的数据相比
	String info = "gap ruler";
	String rulerid;
	int checkout=0;
	String gapBaseline="";
	String sys;

	LinkedList<BigDecimal> history = new LinkedList<BigDecimal>();

	public void init(String rid) {
		rulerid = rid;
		String sql = "select * from rules where rid='RID' and rtype='gap'";
		sql = sql.replace("RID", rid);
		Map m = DBTool1.getFirst(DBTool1.runsql(sql));
		sys=(String)m.get("sys");
		if (m != null) {
			/* init ruler table */
			gap = getGap(m,"para1");

			continues = (Integer) m.get("para2");
			String tmp=(String) m.get("cpara1");
			if(tmp!=null)gapBaseline=tmp;

		}
		/* TODO */
		

		initHis(10);

	}

	private Integer getGap(Map m,String para1) {
		// TODO Auto-generated method stub
		String cpara1=(String)m.get("cpara1");
		if(cpara1==null) {
		 return (Integer) m.get(para1);
		}else {
			if(cpara1.equals(""))return (Integer) m.get(para1);
			else {
				gapBaseline=cpara1;
				return 0;
			}
		}
	
	}

	private void initHis(int size) {
		for (int i = 0; i < size; i++) {
			history.add(new BigDecimal(0));
		}

	}

//	public Boolean check(BigDecimal current1) {
//		int size = history.size();
//		BigDecimal bd = history.get(size - continues);
//		push(current1);
//		Integer cgap = Math.abs(current1.subtract(bd).intValue());
//		System.out.println(history);
//		return cgap < gap;
//
//	}

	private void push(BigDecimal current) {
		// TODO Auto-generated method stub
		history.addLast(current);
		history.removeFirst();
	}

	public static synchronized Rule getInstance(String rid) {
		if (maps.containsKey(rid)) {
			return maps.get(rid);
		} else {
			Rule r = new GapRuler();
			r.init(rid);
			maps.put(rid, r);
			return r;
		}
	}

	public LinkedList<BigDecimal> getHis() {
		// TODO Auto-generated method stub
		return history;
	}

	@Override
	public Boolean check(Date polltime, BigDecimal current1) {

		int size = history.size();
		BigDecimal bd = history.get(size - continues);
		
		push(current1);
		if(!gapBaseline.equals("")) {
			gap=BaselineTool.getSecBas(gapBaseline, polltime,SEC_BASELINE_BVALUE);
		}
		Integer cgap = Math.abs(current1.subtract(bd).intValue());
		System.out.println("CHECKING "+this.rulerid+" gap,current " + current1 + ",last is " + bd.toString() + ",cgap is " +cgap+",control gap is " + gap);
		System.out.println("history is :"+history);
		System.out.println("CHCEKING "+this.rulerid+" check times:"+checkout);
		
		if(this.checkout==0) {
			System.out.println("Starting check,return true");
			this.checkout++;
			return true;
		}
		this.checkout++;
		if (cgap < gap) {
			return true;
			
		} else {
			info = rulerid + " exception, current1 is " + current1 + ",last is " + bd.toString() + ",bigger than " + gap
					+ ".";
			return false;
		}
	}

	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
		return info;
	}

	@Override
	public String getRulerid() {
		// TODO Auto-generated method stub
		return this.rulerid;
	}

	@Override
	public String getSys() {
		// TODO Auto-generated method stub
		return sys;
	}

}
