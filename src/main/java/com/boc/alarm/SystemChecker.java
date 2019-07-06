package com.boc.alarm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.javassist.bytecode.Descriptor.Iterator;

import com.boc.common.DateTool;
import com.boc.common.StringUtils;
import com.boc.db.DBTool1;

public class SystemChecker implements Runnable {

	private static final String RECENT = "recent";
	public static final String COL = "col";
	public static final String RULES = "rules";
	public static final String MEASURESQL = "measuresql";
	public static final String SYSRULE_CRITERIA = "criteria";
	public static final String SYSRULE_SYSCODE = "syscode";
	public static final String SYSRULE_TBL = "tbl";
	public static final String SYSRULE_COL = COL;
	public static final String POLLTIME = "pollTime";
	public static final String SYSRULE_RULES = RULES;
	Boolean issame = true;
	public static String SEP_CHARACTER = "|";
	private static String mesuresql;

	List<Map> rulemap = new ArrayList<Map>();
	// Map<String,List<Rule>> rulemap=new HashMap<String,List<Rule>>();

	public Boolean getIssame() {
		return issame;
	}

	public void setIssame(Boolean issame) {
		this.issame = issame;
	}

	public List<Map> getRulemap() {
		return rulemap;
	}

	public void setRulemap(List<Map> rulemap) {
		this.rulemap = rulemap;
	}

	public static SystemChecker getInstance(String sys) {
		SystemChecker sc = new SystemChecker();
		List<Map> lm = DBTool1.runsql("select * from sysrule");
		List<Map> rulemap = new ArrayList<Map>();
		Boolean issame = true;
		String lasttbl = "";// store the lasttbl;
		for (Map m : lm) {
			String syscode = (String) m.get(SYSRULE_SYSCODE);
			String tbl = (String) m.get(SYSRULE_TBL);
			String col = (String) m.get(SYSRULE_COL);
			String rules = (String) m.get(SYSRULE_RULES);
			String criteria = StringUtils.nullprocess((String) m.get(SYSRULE_CRITERIA));
			mesuresql = "select $COL$,pollTime from $TBL$ " + criteria + " order by pollTime desc limit 0,10";
			List<Rule> lr = getLr(rules);
			Map m1 = new HashMap();
			m1.put(COL, col);
			m1.put(MEASURESQL, mesuresql);
			m1.put(RULES, lr);
			m1.put(RECENT,DateTool.getCurHour1());
			if (!lasttbl.equals(tbl) && !lasttbl.equals(""))
				issame = false;
			lasttbl = tbl;// 判断是否是一张表
			rulemap.add(m1);
		}
		sc.setRulemap(rulemap);
		sc.setIssame(issame);
		return sc;
	}

	public static List<Rule> getLr(String rules) {
		// TODO Auto-generated method stub
		List<Rule> lr = new ArrayList<Rule>();
		String[] rulestr = rules.split(SystemChecker.SEP_CHARACTER);
		if (rulestr != null) {
			for (String s : rulestr) {
				Rule rule = RuleTool.getRule(s);
				lr.add(rule);
			}
		}
		return lr;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		check();
	}

	private CheckResult check() {
		// TODO Auto-generated method stub

		// checklist()
		List<CheckResult> lc = new ArrayList<CheckResult>();
		if (this.issame) {
			Map m = DBTool1.getOne(this.mesuresql);
		} else {
			for (Map m : rulemap) {
				String sql = (String) m.get(SystemChecker.MEASURESQL);
				String col = (String) m.get(SystemChecker.COL);
				Map record = getValue(sql,m);
				if (record != null) {
					BigDecimal bd = (BigDecimal) record.get(col);
					Date pollTime = (Date) record.get(SystemChecker.POLLTIME);
					List<Rule> lr = (List<Rule>) m.get(SystemChecker.RULES);
					CheckResult cr = RuleTool.checklist(pollTime, bd, lr, "OR");
					lc.add(cr);
				}
			}
		}

		return combinResult(lc, "OR");

	}

	private Map getValue(String sql,Map m) {
		// TODO Auto-generated method stub
		Map record= DBTool1.getOne(sql);
		Date d=(Date) record.get(SystemChecker.POLLTIME);
		Date recent =(Date)m.get(RECENT);
		if(d.after(recent)) {			
			m.put(SystemChecker.RECENT, d);
			return record;
			
		}else
			return null;
		
	}

	private CheckResult combinResult(List<CheckResult> lc, String mode) {
		// TODO Auto-generated method stub
		CheckResult cr = new CheckResult();
		String msg = "";
		Boolean result = false;
		int truecount = 0;
		if (lc.size() > 0) {
			for (CheckResult c : lc) {
				if (c.getResult())
					truecount++;
			}
			if (truecount == lc.size())
				result = true;
			if (mode.equals("AND")) {
				if (truecount > 0)
					result = true;
			}
		}else {
			result=true;			
		}

			cr.setResult(result);
			cr.setMsg(msg);
			return cr;
		
	}

}
