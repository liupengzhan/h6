package com.boc.collecter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.boc.alarm.BaselineRuler;
import com.boc.alarm.CheckResult;
import com.boc.alarm.GapRuler;
import com.boc.alarm.Rule;
import com.boc.alarm.RuleTool;
import com.boc.common.DateTool;
import com.boc.common.NumTool;
import com.boc.db.DBTool1;
import com.boc.msg.HttpRequest;

public class SCSProcess {

	static Map cache = new HashMap();
	static  String LAST_DATE="last_date";
	static  String LAST_REC="last_rec";
	static  String MAXSQL="SELECT * FROM trans_tbl_scs ORDER BY pollTime DESC LIMIT 1";
	static  String INSERTSQL = "INSERT INTO trans_tbl_scs (appName,pollTime,txnVol) VALUES ('SCS',STR_TO_DATE('$POLL_TIME$','%Y-%m-%d %H:%i:%s'),$TXNVOL$);";
	
	
	
	public static void getData(Map para) {
		
		String s = HttpRequest.sendPost2(HttpRequest.scs_url, para);
		System.out.println(HttpRequest.scs_url);
		// System.out.println(s);
		List<Map> lm = (List<Map>) JSON.parse(s);
		Date last_date = (Date) cache.get(SCSProcess.LAST_DATE);
		if (last_date == null) {
			last_date = initmax();
		}
		// List<Map> m
		Date newdate=last_date;
		Map lastrec=(Map)cache.get(SCSProcess.LAST_REC);
		for (Map m : lm) {
			String date_str = (String) m.get("record_time");
		//	System.out.println(date_str);
			Date date = DateTool.toDate(date_str, "yyyy-MM-dd HH:mm:ss");
			
			if (date.after(last_date)) {
				
				check(m,lastrec,date);
				
				String sql = transql(m);
				
			
			//    System.out.println(sql);
			int i=DBTool1.update(sql);
			    newdate=date;
			    lastrec=m;
			    
			}

		}
		if(newdate!=null) {
			cache.put(SCSProcess.LAST_DATE, newdate);
			cache.put(SCSProcess.LAST_REC,lastrec);
		}
		
		
		//checkLast();

	}

	private static Boolean check(Map m, Map lastrec, Date date) {
		// TODO Auto-generated method stub
		//Integer curVol =Integer.valueOf((String) m.get("txn"));
		//Integer lastVol =Integer.valueOf((String) lastrec.get("txn"));

		//初始化规则表
		List<Rule>  lr=new ArrayList<Rule>();
		Rule r=GapRuler.getInstance("scs");
		lr.add(r);
		
		Rule base=BaselineRuler.getInstance("scs_baseline");
		lr.add(base);
		
		
		Integer curVol =(Integer) m.get("txn");
		
		
		//Integer lastVol =(Integer)lastrec.get("txn");
		Integer lastVol=r.getHis().getLast().intValue();
		//System.out.println("SCS CHECK,curVol:"+curVol+",lastVol:"+lastVol+","+Math.abs(curVol-lastVol));
		CheckResult cr=RuleTool.checklist(date,curVol,lr,"OR");
		//System.out.println("****checklist:"+cr.getResult()+","+cr.getMsg());
		if(!cr.getResult()) {
			DBTool1.setHealth(0.0,"scs");
			HttpRequest.sendMsg(cr.getMsg());
			return false;
			
		}else {
			DBTool1.setHealth(1.0,"scs");
		}
	/*	if(!r.check(new Date(),NumTool.toBigDecimal(curVol)))
		{
			DBTool1.setHealth(0.0,"scs");
			HttpRequest.sendMsg("SCS TXN EXCEPTION ,Last txn is "+lastVol+",current is "+curVol);
			return false;
		}else {
			DBTool1.setHealth(1.0,"scs");
			
		}*/
//		if(Math.abs(curVol-lastVol)>threshold) {
//			HttpRequest.sendMsg("SCS TXN EXCEPTION ,Last txn is "+lastVol+",current is "+curVol);
//			return false;
//		}
		
		
	//	if()
		return  true;
	}
	

	

	private static Date initmax() {
		// TODO Auto-generated method stub
		Date d;
		List<Map> lm=DBTool1.runsql(SCSProcess.MAXSQL);
		if(lm.size()>0) {
			Map m=lm.get(0);
		    d=(Date)m.get("pollTime");
		    Map lastrec=new HashMap();
		    lastrec.put("txn", (Integer)m.get("txnVol"));
		 
		    cache.put(SCSProcess.LAST_REC,lastrec);
		    return d;
		    
		}
		
		
		return null;
	}

	private static String transql(Map m) {
		// TODO Auto-generated method stub
		String sql = SCSProcess.INSERTSQL;
		sql = sql.replace("$TXNVOL$", m.get("txn").toString()).replace("$POLL_TIME$", m.get("record_time").toString());
		return sql;
		// return null;
	}

	public static void main(String[] args) {
		Map para = new HashMap();
		para.put("tran_type", "SCS");
		para.put("tran_province", "全国");
		para.put("tran_grade", "minute");
		para.put("tran_time", "0#7");
		getData(para);

	}

}
