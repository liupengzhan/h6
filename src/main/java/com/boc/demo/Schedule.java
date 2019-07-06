package com.boc.demo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.boc.alarm.BaselineRuler;
import com.boc.alarm.CheckResult;
import com.boc.alarm.GapRuler;
import com.boc.alarm.Rule;
import com.boc.alarm.RuleTool;
import com.boc.alarm.SimpleRuler;
import com.boc.collecter.SCSProcess;
import com.boc.common.DateTool;
import com.boc.db.DBTool1;
import com.boc.msg.HttpRequest;

@Component
@Configurable
@EnableScheduling
public class Schedule {
//    @Autowired
//    private CalendarDao1 calendarDao1;
	Map<String, String> hm = null;

	private static final String POLL_TIME = "pollTime";
	Logger log = Logger.getRootLogger();

	@Scheduled(cron = "0 30 11 ? * *") //
	public void m0() {
		System.out.println("===============" + new Date().toLocaleString());
	}

	@Scheduled(fixedRate = 30000) // 每BAN分钟执行一次
	public void scs_check() {
		System.out.println("");
		log.info("SCS CHECK TASK START AT" + new Date().toLocaleString());
		BigDecimal bd = getThresh("SCS_TXN_CHANGE");
		// System.out.println("SCS CHECK thresholds:"+bd);
		Map para = new HashMap();
		para.put("tran_type", "SCS");
		para.put("tran_province", "全国");
		para.put("tran_grade", "minute");
		para.put("tran_time", "0#7");

		SCSProcess.getData(para);
//    	Map m=new HashMap();
//    	m.put("tran_type","SCS");
//    	m.put("tran_province","全国");
//    	m.put("tran_grade","minute");
//    	m.put("tran_time", "0#7");
//    	//HttpRequest.sendMsg();
//    	//System.out.println("你好");
//    	String s=HttpRequest.sendPost2(HttpRequest.scs_url, m);
//    	System.out.println(HttpRequest.scs_url);
//    	//System.out.println(s);
//    	List<Map> o=(List<Map>)JSON.parse(s);
//    	Map rec=o.get(o.size()-1);
//    	
//    	
//    	System.out.println(rec);

		System.out.println("SCS CHECK TASK END AT " + new Date().toLocaleString());
	}

	// ICCD 交易量检测 by caitao
	@Scheduled(fixedRate = 30000) // 每分钟执行一次
	public void iccd_check() {
		System.out.println("");
		System.out.println("ICCD CHECK TASK START AT" + new Date().toLocaleString());
		Map m = DBTool1.getFirst(DBTool1.runsql(
				"select * from trans_tbl where appName='iccd' and  pollTime>date_sub(now(),INTERVAL 5 MINUTE) order by pollTime desc"));
		List<Rule> lr = new ArrayList<Rule>();
		Rule r = GapRuler.getInstance("iccd");
		lr.add(r);
		Rule r1 = BaselineRuler.getInstance("iccd_baseline");
		lr.add(r1);
		CheckResult cr = RuleTool.checklist((Date) m.get(POLL_TIME), (Integer) m.get("txnVol"), lr, "AND");
		if (!cr.getResult())
			HttpRequest.sendMsg(cr.getMsg());
		System.out.println("ICCD CHECK TASK END AT " + new Date().toLocaleString());
	}

	// XPAD 交易量检测by caitao
	@Scheduled(fixedRate = 60000)
	public void xpad_check() {
		System.out.println("");
		System.out.println("XPAD CHECK TASK START AT" + new Date().toLocaleString());
		Map m = DBTool1.getFirst(DBTool1.runsql(
				"select * from bocnet_detail_tbl where txnType='E124' and  pollTime>date_sub(now(),INTERVAL 10 MINUTE) and pollTime<DATE_ADD(now(),INTERVAL 1 MINUTE) order by pollTime desc"));
		List<Rule> lr = new ArrayList<Rule>();
		Rule r = GapRuler.getInstance("xpad");
		lr.add(r);
		Rule r1 = BaselineRuler.getInstance("xpad_baseline");
		lr.add(r1);
		CheckResult cr = RuleTool.checklist((Date) m.get(POLL_TIME), (Integer) m.get("txnVol"), lr, "AND");
		if (!cr.getResult())
			HttpRequest.sendMsg(cr.getMsg());
		System.out.println("XPAD CHECK TASK END AT " + new Date().toLocaleString());
	}

	// XPADS 交易量检测by caitao
	@Scheduled(fixedRate = 60000)
	public void xpadc_check() {
		System.out.println("");
		System.out.println("XPADC CHECK TASK START AT " + new Date().toLocaleString());
		Map m = DBTool1.getFirst(DBTool1.runsql(
				"select * from bocnet_detail_tbl where txnType='E134' and  pollTime>date_sub(now(),INTERVAL 10 MINUTE) and pollTime<DATE_ADD(now(),INTERVAL 1 MINUTE) order by pollTime desc"));
		List<Rule> lr = new ArrayList<Rule>();
		//Ruler r = GapRuler.getInstance("xpadc");
		//lr.add(r);
		// Ruler r1=BaselineRuler.getInstance("xpadc_baseline");
		// lr.add(r1);
		 Rule r = SimpleRuler.getInstance("xpadc_simple");
		 lr.add(r);
		CheckResult cr = RuleTool.checklist((Date) m.get(POLL_TIME), (Integer) m.get("txnVol"), lr, "OR");
		if (!cr.getResult())
			HttpRequest.sendMsg(cr.getMsg());
		System.out.println("XPADC CHECK TASK END AT " + new Date().toLocaleString());
	}

	// GCSC 交易量检测by caitao
	@Scheduled(fixedRate = 60000)
	public void gcsc_check() {
		System.out.println("");
		log.info("GCSC CHECK TASK START AT" + new Date().toLocaleString());
		Map m = DBTool1.getFirst(DBTool1.runsql(
				"select * from bocnet_detail_tbl where txnType='E153' and  pollTime>date_sub(now(),INTERVAL 10 MINUTE) and pollTime<DATE_ADD(now(),INTERVAL 1 MINUTE) order by pollTime desc"));
		List<Rule> lr = new ArrayList<Rule>();
		Rule r = GapRuler.getInstance("gcsc");
		lr.add(r);
		Rule r1 = BaselineRuler.getInstance("gcsc_baseline");
		lr.add(r1);
		CheckResult cr = RuleTool.checklist((Date) m.get(POLL_TIME), (Integer) m.get("txnVol"), lr, "AND");
		if (!cr.getResult())
			HttpRequest.sendMsg(cr.getMsg());
		System.out.println("GCSC CHECK TASK END AT " + new Date().toLocaleString());
	}
	// MCIS 交易量检测by caitao
	
	 @Scheduled(fixedRate = 60000) public void mcis_check() {
	 System.out.println(""); 
	 System.out.println("MCIS CHECK TASK START AT" + new Date().toLocaleString()); 
	 Map m=DBTool1.getFirst(DBTool1.runsql("select * from bocnet_app_tbl where txnType='MCIS' and  pollTime>date_sub(now(),INTERVAL 10 MINUTE) and pollTime<DATE_ADD(now(),INTERVAL 1 MINUTE) order by pollTime desc")); 
	 List<Rule> lr=new ArrayList<Rule>(); 
	 //Ruler r=GapRuler.getInstance("mcis"); 
	 //lr.add(r); 
	 //Ruler r1=BaselineRuler.getInstance("mcis_baseline"); 
	 //lr.add(r1);  
	 Rule r = SimpleRuler.getInstance("mcis_simple");
	 lr.add(r);
	 CheckResult cr=RuleTool.checklist((Date)m.get(POLL_TIME), (Integer)m.get("txnVol"),lr,"OR"); 
	 if(!cr.getResult())HttpRequest.sendMsg(cr.getMsg());
	 System.out.println("MCIS CHECK TASK END AT" + new Date().toLocaleString()); }
	 
	 

	// RCPS 交易量检测by caitao
	@Scheduled(fixedRate = 60000)
	public void rcps_check() {
		System.out.println("");
		log.info("RCPS CHECK TASK START AT" + new Date().toLocaleString());
		Map m = DBTool1.getFirst(DBTool1.runsql(
				"select * from bocnet_detail_tbl where txnType='E127' and  pollTime>date_sub(now(),INTERVAL 10 MINUTE) and pollTime<DATE_ADD(now(),INTERVAL 1 MINUTE) order by pollTime desc"));
		List<Rule> lr = new ArrayList<Rule>();
		Rule r = GapRuler.getInstance("rcps");
		lr.add(r);
		Rule r1 = BaselineRuler.getInstance("rcps_baseline");
		lr.add(r1);
		CheckResult cr = RuleTool.checklist((Date) m.get(POLL_TIME), (Integer) m.get("txnVol"), lr, "AND");
		if (!cr.getResult())
		HttpRequest.sendMsg(cr.getMsg());
		System.out.println("RCPS CHECK TASK END AT " + new Date().toLocaleString());
	}

	// TPCC CHECK ,TEST THE SIMPLE RULER
	@Scheduled(fixedRate = 60000)
	public void tpcc_check() {
		/*//如果要限定TPCC监控某一时段，可以启用如下代码
		Integer min=DateTool.getMins(new Date());
		if(min>480&&min<1200) {
			//do sth
		}
		*/
		System.out.println("");
		log.info("TPCC CHECK START AT:" + new Date().toLocaleString());
		Map m = DBTool1.getFirst(DBTool1.runsql(
				"select * from bocnet_detail_tbl where txnType='E108' and  pollTime>date_sub(now(),INTERVAL 10 MINUTE) and pollTime<DATE_ADD(now(),INTERVAL 1 MINUTE) order by pollTime desc"));
		List<Rule> lr = new ArrayList<Rule>();
		Rule r = SimpleRuler.getInstance("tpcc_simple");
		lr.add(r);
		
		
		CheckResult cr = RuleTool.checklist((Date) m.get(POLL_TIME), (Integer) m.get("txnVol"), lr, "OR");
		if (!cr.getResult())
			HttpRequest.sendMsg(getUsers(r.getSys()), cr.getMsg());
		log.info("TPCC CHECK TASK END AT " + new Date().toLocaleString());
	}
	
	
	
	// ISTNODE1_3000银联 CHECK ,TEST THE SIMPLE RULER, BY GREEN
	@Scheduled(fixedRate = 120000)
	public void istnode1_3000_check() {
		/*//如果要限定监控某一时段，可以启用如下代码
		Integer min=DateTool.getMins(new Date());
		if(min>480&&min<1200) {
			//do sth
		}
		*/
		System.out.println("");
		log.info("ISTNODE1_3000 银联 CHECK START AT:" + new Date().toLocaleString());
		Map m = DBTool1.getFirst(DBTool1.runsql(
				"select * from trans_tbl_ist where appName='istnode1' and txnType='3000'and pollTime>date_sub(now(),INTERVAL 10 MINUTE) order by pollTime desc"));
		
		List<Rule> lr = new ArrayList<Rule>();
		Rule r = SimpleRuler.getInstance("istnode1_3000_simple");
		lr.add(r);
		Rule r1 = GapRuler.getInstance("istnode1_3000_gap");
		lr.add(r1);
		//Ruler r2 = BaselineRuler.getInstance("istnode1_3000_baseline");
		//lr.add(r2);
		
		CheckResult cr = RuleTool.checklist((Date) m.get(POLL_TIME), (Integer) m.get("txnVol"), lr, "OR");
		if (!cr.getResult())
			HttpRequest.sendMsg(getUsers(r.getSys()), cr.getMsg());
		log.info("ISTNODE1_3000 CHECK TASK END AT " + new Date().toLocaleString());
	}

	// ISTNODE1_165001BANCS CHECK ,TEST THE SIMPLE RULER, BY GREEN
	@Scheduled(fixedRate = 120000)
	public void istnode1_165001_check() {
		/*//如果要限定监控某一时段，可以启用如下代码
		Integer min=DateTool.getMins(new Date());
		if(min>480&&min<1200) {
			//do sth
		}
		*/
		System.out.println("");
		log.info("ISTNODE1_165001 BANCS CHECK START AT:" + new Date().toLocaleString());
		Map m = DBTool1.getFirst(DBTool1.runsql(
				"select * from trans_tbl_ist where appName='istnode1' and txnType='165001'and pollTime>date_sub(now(),INTERVAL 10 MINUTE) order by pollTime desc"));
		
		List<Rule> lr = new ArrayList<Rule>();
		Rule r = SimpleRuler.getInstance("istnode1_165001_simple");
		lr.add(r);
		Rule r1 = GapRuler.getInstance("istnode1_165001_gap");
		lr.add(r1);
		//Ruler r2 = BaselineRuler.getInstance("istnode1_165001_baseline");
		//lr.add(r2);
		
		CheckResult cr = RuleTool.checklist((Date) m.get(POLL_TIME), (Integer) m.get("txnVol"), lr, "OR");
		if (!cr.getResult())
			HttpRequest.sendMsg(getUsers(r.getSys()), cr.getMsg());
		log.info("ISTNODE1_3000 CHECK TASK END AT " + new Date().toLocaleString());
	}
	
	// POSP_165001 BANCS CHECK ,TEST THE SIMPLE RULER, BY GREEN
	@Scheduled(fixedRate = 120000)
	public void posp_165001_check() {
		/*//如果要限定监控某一时段，可以启用如下代码
		Integer min=DateTool.getMins(new Date());
		if(min>480&&min<1200) {
			//do sth
		}
		*/
		System.out.println("");
		log.info("POSP_165001 BANCS CHECK START AT:" + new Date().toLocaleString());
		Map m = DBTool1.getFirst(DBTool1.runsql(
				"select * from trans_tbl where appName='pospnode2' and txnType='165001'and pollTime>date_sub(now(),INTERVAL 10 MINUTE) order by pollTime desc"));
		
		List<Rule> lr = new ArrayList<Rule>();
		Rule r = SimpleRuler.getInstance("posp_165001_simple");
		lr.add(r);
		Rule r1 = GapRuler.getInstance("posp_165001_gap");
		lr.add(r1);
		//Ruler r2 = BaselineRuler.getInstance("posp_165001_baseline");
		//lr.add(r2);
		
		CheckResult cr = RuleTool.checklist((Date) m.get(POLL_TIME), (Integer) m.get("txnVol"), lr, "OR");
		if (!cr.getResult())
			HttpRequest.sendMsg(getUsers(r.getSys()), cr.getMsg());
		log.info("ISTNODE1_3000 CHECK TASK END AT " + new Date().toLocaleString());
	}
	
	
	@Scheduled(fixedRate = 60000)
	public void ipps_check() {
		
		String tpssql="SELECT AVG(VALUE) as tps,DATE_SUB(NOW(),INTERVAL 60 SECOND) FROM ipps_dc_0003 id WHERE id.logTime>DATE_SUB(NOW(),INTERVAL 60 SECOND) AND NAME='IPPS' ";
		String respsql="SELECT AVG(VALUE) as resp,DATE_SUB(NOW(),INTERVAL 60 SECOND) FROM ipps_dc_0004 id WHERE id.logTime>DATE_SUB(NOW(),INTERVAL 60 SECOND) AND NAME='IPPS' ";
		String sucsql="SELECT AVG(VALUE) as suc,DATE_SUB(NOW(),INTERVAL 60 SECOND) FROM ipps_dc_0002 id WHERE id.logTime>DATE_SUB(NOW(),INTERVAL 60 SECOND) AND NAME='IPPS' ";
		
		String info="";
		Map m=DBTool1.getFirst(DBTool1.runsql(tpssql));
		BigDecimal tps=(BigDecimal)m.get("tps");
		Rule rtps=SimpleRuler.getInstance("ipps_tps");
		if(!rtps.check(new Date(), tps)) {info=info+rtps.getInfo();}
		DBTool1.rundml("update dashboard_cfg set txnVol="+tps.intValue()+" where syscode='ipps'");
		Map m1=DBTool1.getFirst(DBTool1.runsql(respsql));
		BigDecimal resp=(BigDecimal)m1.get("resp");
		
		Rule rresp=SimpleRuler.getInstance("ipps_resp");
		DBTool1.rundml("update dashboard_cfg set txnRespTime="+resp.intValue()+" where syscode='ipps'");
		
		Map m2=DBTool1.getFirst(DBTool1.runsql(sucsql));
		Double suc=(Double)m2.get("suc");
		DBTool1.rundml("update dashboard_cfg set txnRatioSuc="+suc.toString()+" where syscode='ipps'");
		
		if(!rresp.check(new Date(), resp)) {
			info=info+rresp.getInfo();
		}
		
		if(!info.equals("")) {
			HttpRequest.sendMsg(getUsers(rresp.getSys()), info);
			DBTool1.setHealth(0.0,"ipps");
		}else {
			DBTool1.setHealth(1.0,"ipps");
		}
		//Boolean 
		
		
		
		
		
	}
	

	private String getUsers(String system) {

		if (hm == null) {
			hm=new HashMap<String,String>();
			List<Map> lm = DBTool1.runsql("select * from msgusers");
			for (Map m : lm) {
				String sys = (String) m.get("sys");
				String users = (String) m.get("users");
				hm.put(sys, users);

			}
		}
		String result = hm.get(system);
		if (result != null)
			return result;
		else
			return "";

	}

	private BigDecimal getThresh(String mname) {
		// TODO Auto-generated method stub
		String sql = "select * from thresholds where measure='MNAME'";
		sql = sql.replace("MNAME", mname);
		List<Map> lm = DBTool1.runsql(sql);
		Map m = DBTool1.getFirst(lm);
		if (m != null) {
			return (BigDecimal) m.get("value");

		}
		return null;

	}

//    @Scheduled(cron = "${a}")  //每一分钟执行一次
//    public void m1() {
//        calendarDao1.calendarDelete(1);
//    }
	public static void main(String[] args) {
	/*	Schedule sc=new Schedule();
		for(int i=1;i<=10;i++) {
			sc.tpcc_check();
			try {
				Thread.sleep(100000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
	}
}
