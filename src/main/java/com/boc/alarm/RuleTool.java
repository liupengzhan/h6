package com.boc.alarm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.boc.common.NumTool;
import com.boc.common.StringUtils;
import com.boc.db.DBTool1;

public class RuleTool {
	public static CheckResult checklist(Date date, Integer curval, List<Rule> lr, String andor) {

		return checklist(date, NumTool.toBigDecimal(curval), lr, andor);

	}

	public static CheckResult checklist(Date date, BigDecimal curval, List<Rule> lr, String andor) {
		CheckResult sr = new CheckResult();
		Boolean result = true;
		StringBuffer sb = new StringBuffer();
		int tag = 0;
		System.out.println("RULE SIZE:" + lr.size());
		for (Rule r : lr) {

			if (andor.equals("OR")) {
				if (!r.check(date, curval)) {
					if (result)
						result = false;
					sb.append(r.getInfo());
				} else {
					System.out.println("checking " + r.getRulerid() + " result is true");
				}

				/*
				 * if (andor.equals("AND")) result=true; if (r.check(date, curval)){
				 * System.out.println("rule checking result is true"); sb.append(""); break; }
				 * else { tag++; sb.append(r.getInfo()); } if(tag==lr.size())result=false;
				 * 
				 */
			} else if (andor.equals("AND")) {
				// System.out.println("CHECKLIST IN AND..");
				if (r.check(date, curval)) {
					System.out.println("AND model ,rule checking result is true");
					result = true;
					sb.append("");
					break;
				} else {
					result = false;
					// System.out.println("and ,rule checking result is false;");
					sb.append(r.getInfo());
				}
			}

		}

		sr.setResult(result);
		sr.setMsg(sb.toString());
		return sr;

	}

	public static void testand() {
		ArrayList<Boolean> al = new ArrayList<Boolean>();
		for (int i = 0; i <= 10; i++) {

			if (i == 5)
				al.add(true);
			else
				al.add(false);
		}
		Boolean result = true;
		;
		Integer i = 0;
		for (Boolean b : al) {

			if (b) {
				System.out.println("in if true,loop is " + i);
				result = true;
				i++;
				break;
			} else {
				result = false;
				System.out.println("in if else,loop is " + i);
				i++;

			}
		}
		System.out.println("result:" + result);

	}

	public static void testScs() {
		Rule r = GapRuler.getInstance("scs");
		String sql = "SELECT pollTime,txnVol FROM trans_tbl_scs  ORDER BY pollTime DESC LIMIT 0,10 ";
		List<Map> lm = getTenData(sql);
		for (Map m : lm) {
			Date polltime = (Date) m.get("pollTime");
			BigDecimal value = NumTool.toBigDecimal(m.get("txnVol"));
			Boolean re=r.check(polltime, value);
			System.out.println(re+","+r.getInfo());
		}
	}

	private static List<Map> getTenData(String sql) {
		// TODO Auto-generated method stub
		List<Map> lm=DBTool1.runsql(sql);
		return lm;
	}

	public static void main(String[] args) {
		// testand();
		testScs();
	}

	public static Rule getRule(String s) {
		// TODO Auto-generated method stub
		if(getRuleType(s).equals("gap")) {
			return GapRuler.getInstance(s);
		}
		if(getRuleType(s).equals("baseline")) {
			return BaselineRuler.getInstance(s);
		}
		if(getRuleType(s).equals("simple")) {
			return BaselineRuler.getInstance(s);
		}
		return null;
	}

	private static String getRuleType(String s) {
		// TODO Auto-generated method stub
		Map m=DBTool1.getFirst(DBTool1.runsql("select * from rules where rid='"+s+"'"));
		String rtype=(String) m.get("rtype");
		return StringUtils.nullprocess(rtype);
	}
}
