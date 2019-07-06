package com.boc.alarm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.boc.common.DateTool;
import com.boc.db.DBTool1;

public class BaselineRuler implements Rule {

	String rulerid;
	String baseline_para1;
	String baseline_para2;
	Integer percent;
	Integer repeat;
	LinkedList<Integer> history = new LinkedList<Integer>();
	LinkedList<BigDecimal> vhistory = new LinkedList<BigDecimal>();
	static HashMap<String, Rule> maps = new HashMap<String, Rule>();
	String info = "baseline ruler,";
	static Integer LOWVALUE = 30;
	public static String WEEKDETAIL="WEEKDETAIL";
	String sys;
	
	@Override
	public void init(String rid) {
		// TODO Auto-generated method stub
		rulerid = rid;
		// controlled by rule parameter table
		String sql = "select * from rules where rid='RID' and rtype='baseline'";
		sql = sql.replace("RID", rid);
		Map m = DBTool1.getFirst(DBTool1.runsql(sql));
		baseline_para1 = (String) m.get("cpara1"); // 基线的ID ，例如SCS_WE_ALL_VOL"
		baseline_para2 = (String) m.get("cpara2"); // 基线的ID2 ，例如SCS_WK_ALL_VOL"
		percent = (Integer) m.get("para1"); // 基线的波动范围 例如15 ,表示偏离15%才告警
		repeat = (Integer) m.get("para2");// 异常的重复次数，用于抑制抖动，例如，连续2次异常才告警
		sys=(String)m.get("sys");
		for (int i = 0; i < 10; i++) {
			history.add(0);
		}
		for (int i = 0; i < 10; i++) {
			vhistory.add(new BigDecimal(0));
		}

	}

	@Override
	public Boolean check(Date polltime, BigDecimal current1) {
		String baseline = "";
		baseline = getBaseline(polltime);

		Integer bvalue = BaselineTool.getBas(baseline, polltime);
		int current_percent = (Math.abs(current1.intValue() - bvalue) * 100) / bvalue;
		System.out.println("CHECKING " + this.rulerid + " baseline,current is " + current1 + ",baseline value is "
				+ bvalue + ",current percent is " + current_percent + ",control percent is " + percent + "%");
		ListTool.push(vhistory, current1);
		System.out.println("Last ten monitor value :"+vhistory);
		if (current_percent > percent) {
			if (bvalue > BaselineRuler.LOWVALUE) {
				System.out.println("percent is :" + current_percent);
				ListTool.push(history, 1);
				System.out.println("BASELINE exception repeat time: " + ListTool.getRepeat(history)
						+ ",control value is " + repeat);
				if (ListTool.getRepeat(history) >= repeat) // 判断是否抖动
				{

					info = rulerid + " exception,more than " + repeat + " times deviate the baseline,value is "
							+ current1.toString() + ",base value is " + bvalue;
					return false;
				} else
					return true;
			} else {
				info = rulerid + ",too small bvalue,no need to caculate";
				return true;
			}
		} else {

			ListTool.push(history, 0);
			return true;
		}

	}

	private String getBaseline(Date polltime) {
		String baseline;
		if (baseline_para2.contains(BaselineRuler.WEEKDETAIL)) {  //如果cpara2含有WEEKDETAIL关键字，就使用7天的基线
			Integer day=DateTool.getWeekday(polltime);
			String daystr="DAY"+day;
			baseline=baseline_para2.replace(BaselineRuler.WEEKDETAIL, daystr);

		} else {

			if (DateTool.isWeekend(polltime))
				baseline = this.baseline_para1;
			else
				baseline = this.baseline_para2;
		}
		return baseline;
	}

	public LinkedList<BigDecimal> getHis() {
		// TODO Auto-generated method stub
		return vhistory;
	}

	public static synchronized Rule getInstance(String rid) {
		if (maps.containsKey(rid)) {
			return maps.get(rid);
		} else {
			Rule r = new BaselineRuler();
			r.init(rid);
			maps.put(rid, r);
			return r;
		}
	}

	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
		return info;
	}

	public static void main(String[] args) {
		System.out.println(BaselineTool.getBas("SCS_WE_ALL_VOL", new Date()));
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
