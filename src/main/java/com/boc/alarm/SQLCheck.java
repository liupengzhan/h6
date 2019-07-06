package com.boc.alarm;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.boc.common.NumTool;
import com.boc.common.DateTool;
import com.boc.db.DBTool1;



public class SQLCheck {
	
	Boolean checkNumber(String sqlid) {
		String sql=getSql(sqlid);
		BigDecimal max=getThreshhold(sqlid,new Date(),1);
		BigDecimal min=getThreshhold(sqlid,new Date(),1);
		
		BigDecimal bg=DBTool1.getValue(sql);		
		if(bg.compareTo(max)<=0&&bg.compareTo(min)>=0)
			return  true;
		else 
			return false;
	}

	private BigDecimal getThreshhold(String sqlid, Date date,int type) {
		// TODO Auto-generated method stub
		String sql="select max+0 as colval from alarmvalue where id=SQLID and poll";
		return DBTool1.getValue(sql);
		
	}

	public String  checkBocnet(String txnid) {
		
		String msg="OK";
		String sql="SELECT pollTime,txnVol,txnRatioSuc,txnRespTime FROM bocnet_detail_tbl WHERE txnType='TXNID'  ORDER BY pollTime desc limit 0,2";
		sql=sql.replace("TXNID", txnid);
	
		
		List<Map> lm=DBTool1.runsql(sql);
		if(lm.size()==2) {
			Map m=lm.get(1);
			Integer txnVol=(Integer)m.get("txnVol");
			BigDecimal txnRationSuc=(BigDecimal)m.get("txnRatioSuc");
			BigDecimal txnRespTime=(BigDecimal)m.get("txnRespTime");
			java.sql.Timestamp sd = (java.sql.Timestamp) m.get(
					"pollTime");
			msg=checkTxnVol(txnid,txnVol,txnRationSuc,txnRespTime,sd); 
			System.out.println("==========="+sd);
			
		}
		
		return msg;
				
		
	}
	
	
	

	private String checkTxnVol(String txnid,Integer txnVol,BigDecimal txnRationSuc,BigDecimal txnRespTime, Timestamp sd) {
		String msg="OK";
		String minite_str=DateTool.toStringMm1(sd);
		//System.out.println(sd);
        Calendar ca=Calendar.getInstance();
        ca.setTime(sd);
        
        int weekday=ca.get(Calendar.DAY_OF_WEEK);
        String type="DAY";
        if(weekday==0||weekday==6)
        	type="WEEK";
        String sql="SELECT * FROM alarm_setting WHERE alarmid='$TXNID$' AND  times='$MINSTR$'  AND TYPE='$TYPE$'";
        sql=sql.replace("$TYPE$", type).replace("$MINSTR$", minite_str).replace("$TXNID$",txnid);

        List<Map> lm=DBTool1.runsql(sql);
        Map m=lm.get(0);
    	Integer txnVol0=(Integer)m.get("txnVol");
		BigDecimal txnRationSuc0=(BigDecimal)m.get("txnRatioSuc");
		BigDecimal txnRespTime0=(BigDecimal)m.get("txnRespTime");
		if(NumTool.cmpInteger(txnVol,txnVol0)>0.2) {
			msg="";
			msg+="txnVol :"+txnVol+",偏离20%,txnBase:"+txnVol0+"\n";
		}
		if(NumTool.cmpDecimal(txnRationSuc,txnRationSuc0)>0.2) {
			msg="";
			msg+="txnRationSuc :"+txnRationSuc+",偏离20%,txnBase:"+txnRationSuc0+"\n";
		}
		if(NumTool.cmpDecimal(txnRespTime,txnRespTime0)>0.5) {
			msg="";
			msg+="txnRespTime :"+txnRespTime+",偏离50%,txnBase:"+txnRespTime0+"\n";
		}
	
        
		return msg;
	}

	private String getSql(String sqlid) {
		// TODO Auto-generated method stub
		
		return null;
	}
	
	public static void main(String[] args) {
		SQLCheck sc=new SQLCheck();
		System.out.println(sc.checkBocnet("E124"));
	}
	
	

}
