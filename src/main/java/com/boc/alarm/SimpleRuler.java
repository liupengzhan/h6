package com.boc.alarm;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.log4j.Logger;

import com.boc.common.DateTool;
import com.boc.common.Log;
import com.boc.common.NumTool;
import com.boc.db.DBTool1;

public class SimpleRuler implements Rule {

	private static final String BAVLUE2 = "bvalue2";  //sec_baseline表中的列名
	private static final String BAVLUE = "bvalue";//sec_baseline表中的列名
	
	Logger log = Logger.getRootLogger();
	
	static HashMap<String, Rule> maps = new HashMap<String, Rule>(); //缓存已生成ruler实例
//	String rid;

	Integer min = 1;// 下限
	Integer max = 100;// 上限
	Integer times = 1; // 仅有para3的情况下是重复次数，否则是出现次数
	Integer range = 1;// 统计范围，例如，最近的5次中
	String info = "simple ruler";  //用于保存生成的告警信息
	String rulerid;
	String simplebase = "";// 按基线取上下限，在sec_baseline中。
	String sys;           //rules表中的sys
	

	LinkedList<BigDecimal> history = new LinkedList<BigDecimal>();  //监控值的缓存队列，10个
	LinkedList<Integer> resulthis = new LinkedList<Integer>();      //check结果的缓存队列，10个，0为正常，1为不正常

	@Override
	public void init(String rid) {
		// TODO Auto-generated method stub

		rulerid = rid;
		String sql = "select * from rules where rid='RID' and rtype='simple'";
		sql = sql.replace("RID", rid);
		Map m = DBTool1.getFirst(DBTool1.runsql(sql));
		if (m != null) {
			/* init ruler table */
			min = (Integer) m.get("para1");

			max = (Integer) m.get("para2");

			if (m.get("para3") == null) // 仅有para3的情况下是重复次数，否则是出现次数
				times = 1;
			else
				times = (Integer) m.get("para3");

			if (m.get("para4") == null) // para4可以不设置，如果设置，则为”检查范围“，例如 最近的range次中出现continues
				range = 0;
			else
				range = (Integer) m.get("para4");
			// Log.debug(s);
			sys = (String) m.get("sys");

			simplebase = (String) m.get("cpara1");

		}
		/* TODO */
		for (int i = 0; i < 10; i++) {
			history.add(new BigDecimal(0));
		}
		for (int i = 0; i < 10; i++) {
			resulthis.add(0);
		}

	}

	@Override
	public Boolean check(Date polltime, BigDecimal current1) {
		initMinAndMax(polltime);//如果有基线，用基线值
		log.info("current value is :" + current1 + ",pollTime is " + DateTool.toStringMm(polltime));
		ListTool.push(history, current1);
		System.out.println("history value is "+history);
		// TODO Auto-generated method stub
		info = "";
		Integer c = current1.intValue();
		if (range > 1) { // 设置range，即para4的情况
			if (c <= max && c >= min) {
				ListTool.push(resulthis, 0);
				return true;

			} else {
				ListTool.push(resulthis, 1);
				if (ListTool.getRecent(range, resulthis) >= times) {
					info = polltime+","+this.rulerid + ", more than " + times + " times out of control value:" + min + "-" + max
							+ "range:" + range + ",current is " + current1;
					// Log.debug(info);
					log.debug(info);
					return false;
				}
			}

		} else { // 不设置para4的情况
			if (c <= max && c >= min) {
				ListTool.push(resulthis, 0);
				return true;
			} else {
				ListTool.push(resulthis, 1);
				log.debug("repeat time:" + ListTool.getRepeat(resulthis).toString());
				if (ListTool.getRepeat(resulthis) >= times) {
					info = polltime+","+this.rulerid + " continued more than " + times + " times value exception:" + min + "-" + max
							+ ",current is " + current1;
					log.debug(info);
					return false;
				}

			}
		}

		return true;

	}

	private void initMinAndMax(Date polltime) {
		//
		log.info("simple baseline:"+simplebase);
		if (simplebase != null) {
			if (!simplebase.equals("")) {
				
				this.min=BaselineTool.getSecBas(simplebase, polltime, BAVLUE);
				this.max=BaselineTool.getSecBas(simplebase, polltime, BAVLUE2);
				log.debug("min:"+min+",max:"+max);
			}
		}

	}

	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
		return info;
	}

	@Override
	public LinkedList<BigDecimal> getHis() {
		// TODO Auto-generated method stub
		return history;
	}

	public static synchronized Rule getInstance(String rid) {
		if (maps.containsKey(rid)) {
			return maps.get(rid);
		} else {
			Rule r = new SimpleRuler();
			r.init(rid);
			maps.put(rid, r);
			return r;
		}
	}

	@Override
	public String getRulerid() {
		// TODO Auto-generated method stub
		return this.rulerid;
	}

	public static void main(String args[]) {
		Logger logger = Logger.getRootLogger();
		Rule r = SimpleRuler.getInstance("s1");

		int[] arr = new int[] { 11, 12, 23, 24, 15 };
		for (int i = 0; i < arr.length; i++) {
			Boolean b = r.check(new Date(), NumTool.toBigDecimal(arr[i]));
			String info = r.getInfo();
			logger.debug(b + "," + info);
		}
	}

	@Override
	public String getSys() {
		// TODO Auto-generated method stub
		return sys;
	}

}
