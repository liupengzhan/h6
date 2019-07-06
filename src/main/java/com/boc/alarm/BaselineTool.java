package com.boc.alarm;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boc.common.DateTool;
import com.boc.db.DBTool1;

public class BaselineTool {

	public static Map<String, List<Map>> cachedSecBas = new HashMap<String, List<Map>>();

	public static void initBaseline(String tbl, String dtype, String app, String mesure, String channel, String txntype,
			String txncode, String date) {

		String baselineid = getPre(tbl) + app + "-" + dtype + "-" + mesure + "-" + channel + "-" + txntype + "-"
				+ txncode;
		if (tbl.startsWith("bocnet_")) {
			app = "BOCNETAPP";
		}

		deleteBaselineid(baselineid);

		// insert baselineid
		String sql1 = "insert into baselineid values ('$APP$','$DTYPE$','$CHANNEL$','$TXNTYPE$','$TXNCODE$','$MESURE$','"
				+ baselineid + "')";
		sql1 = sql1.replace("$APP$", app).replace("$DTYPE$", dtype).replace("$MESURE$", mesure)
				.replace("$CHANNEL$", channel).replace("$TXNTYPE$", txntype).replace("$TXNCODE$", txncode);
		// System.out.println(sql1);
		DBTool1.update(sql1);

		deleteBaseline(baselineid);

		String sql2 = "insert into baseline SELECT '$BASEID$', date_format(pollTime,'%H:%i'),AVG($MESURE$) FROM  $TAB$ WHERE $POLLTIME$   AND $WEEKSEG$ AND  appName='$APP$' and txnChannel='$CHANNEL$' and txntype='$TXNTYPE$' and txncode='$TXNCODE$' GROUP BY  date_format(pollTime,'%H:%i')";

		sql2 = sql2.replace("$APP$", app).replace("$BASEID$", baselineid).replace("$TAB$", tbl)
				.replace("$MESURE$", mesure).replace("$CHANNEL$", channel).replace("$TXNTYPE$", txntype)
				.replace("$TXNCODE$", txncode).replace("$POLLTIME$", getPolltime(date));
		sql2 = processDtype(dtype, sql2);
		sql2 = processall(sql2);

		// System.out.println(sql2);
		DBTool1.update(sql2);

	}

	private static String processDtype(String dtype, String sql2) {
		if (dtype.equals("WEEKDAY")) {
			sql2 = sql2.replace("$WEEKSEG$",
					" DAYOFWEEK(pollTime) != 1   AND DAYOFWEEK(pollTime) != 7 and date(pollTime)!='2019-06-07'");
		} else if (dtype.startsWith("DAY")) {
			String weekday = dtype.replace("DAY", "");
			sql2 = sql2.replace("$WEEKSEG$", " DAYOFWEEK(pollTime) !=" + weekday + " and date(pollTime)!='2019-06-07'");

		} else {
			sql2 = sql2.replace("$WEEKSEG$", " (DAYOFWEEK(pollTime) = 1   or DAYOFWEEK(pollTime) = 7 ) ");
		}
		return sql2;
	}

	public static void initBaseline(String tbl, String dtype, String app, String mesure, String channel, String txntype,
			String txncode) {

		initBaseline(tbl, dtype, app, mesure, channel, txntype, txncode, "");

		/*
		 * String baselineid = getPre(tbl) + app + "-" + dtype + "-" +
		 * mesure+"-"+channel+"-"+txntype+"-"+txncode; if(tbl.startsWith("bocnet_")) {
		 * app="BOCNETAPP"; }
		 * 
		 * 
		 * deleteBaselineid(baselineid);
		 * 
		 * 
		 * //insert baselineid String sql1 =
		 * "insert into baselineid values ('$APP$','$DTYPE$','$CHANNEL$','$TXNTYPE$','$TXNCODE$','$MESURE$','"
		 * + baselineid+"')"; sql1 = sql1.replace("$APP$", app).replace("$DTYPE$",
		 * dtype).replace("$MESURE$", mesure).replace("$CHANNEL$",
		 * channel).replace("$TXNTYPE$", txntype).replace("$TXNCODE$", txncode); //
		 * System.out.println(sql1); DBTool1.update(sql1);
		 * 
		 * deleteBaseline(baselineid);
		 * 
		 * String sql2 =
		 * "insert into baseline SELECT '$BASEID$', date_format(pollTime,'%H:%i'),AVG($MESURE$) FROM  $TAB$ WHERE pollTime > date_sub(now(),interval 1 WEEK)   AND $WEEKSEG$ AND  appName='$APP$' and txnChannel='$CHANNEL$' and txntype='$TXNTYPE$' and txncode='$TXNCODE$' GROUP BY  date_format(pollTime,'%H:%i')"
		 * ; sql2 = sql2.replace("$APP$", app).replace("$BASEID$",
		 * baselineid).replace("$TAB$", tbl).replace("$MESURE$",
		 * mesure).replace("$CHANNEL$", channel).replace("$TXNTYPE$",
		 * txntype).replace("$TXNCODE$", txncode); if (dtype.equals("WEEKDAY")) { sql2 =
		 * sql2.replace("$WEEKSEG$",
		 * " DAYOFWEEK(pollTime) != 1   AND DAYOFWEEK(pollTime) != 7 and date(pollTime)!='2019-06-07'"
		 * ); } else { sql2 = sql2.replace("$WEEKSEG$",
		 * " (DAYOFWEEK(pollTime) = 1   or DAYOFWEEK(pollTime) = 7 ) "); }
		 * sql2=processall(sql2);
		 * 
		 * // System.out.println(sql2); DBTool1.update(sql2);
		 */
	}

	private static String getPolltime(String date) {
		// TODO Auto-generated method stub
		if (date.equals("")) {
			return "pollTime > date_sub(now(),interval 1 WEEK)";
		} else if (date.startsWith("20") && date.length() == 10) {
			return "date(pollTime)='" + date + "'";
		}
		return "";

	}

	private static void deleteBaseline(String baselineid) {
		String sql = "delete from baseline where bid='$BID$'";
		sql = sql.replace("$BID$", baselineid);
		// System.out.println(sql);
		DBTool1.update(sql);

	}

	private static void deleteBaselineid(String baselineid) {
		String sql = "delete from baselineid where bid='$BID$'";
		sql = sql.replace("$BID$", baselineid);
		// System.out.println(sql);
		DBTool1.update(sql);
	}

	private static String getPre(String tbl) {
		// TODO Auto-generated method stub
		if (tbl.equals("trans_tbl"))
			return "TRANS-";
		if (tbl.equals("bocnet_detail_tbl"))
			return "BOCNET-";
		// if(tbl.equals("trans_tbl"))return "TRANS-";
		return "TRANS-";

	}

	private static String processall(String sql2) {
		// TODO Auto-generated method stub
		String sql = sql2.replace("and txnChannel='ALL'", "").replace("and txntype='ALL'", "")
				.replace("and txncode='ALL'", "");
		return sql;
	}

	public static Integer getBas(String baselineid2, Date polltime) {
		// TODO Auto-generated method stub
		String sql = "select * from  baseline where bid='$BASELINEID$' and pollTime='$POLLTIME$'";

		String timestr = DateTool.toStringMm1(polltime);
		sql = sql.replace("$POLLTIME$", timestr).replace("$BASELINEID$", baselineid2);
		Map m = DBTool1.getFirst(DBTool1.runsql(sql));
		if (m == null) {
			String newsql = "select * from  baseline where bid='$BASELINEID$' and pollTime>DATE_SUB('$POLLTIME$',INTERVAL 5 MINUTE))";

			newsql = newsql.replace("$POLLTIME$", timestr).replace("$BASELINEID$", baselineid2);
			Map m1 = DBTool1.getFirst(DBTool1.runsql(sql));
			if (m1 == null) {
				return 1;
			} else
				return (Integer) m.get("bvalue");

		} else
			return (Integer) m.get("bvalue");

	}



	public static Integer getSecBas(String gapBaselineid, Date polltime,String col) {
		// TODO Auto-generated method stub
		// String sql="select * from baseline where bid='$BASELINEID$' and
		// startTime<='$POLLTIME$' and endTime>'$POLLTIME$'";
		List<Map> gapBaseline;

		if (cachedSecBas.get(gapBaselineid) == null) {
			String sql = "select * from  seg_baseline where bid='$BASELINEID$'";
			String timestr = DateTool.toStringMm1(polltime);
			sql = sql.replace("$BASELINEID$", gapBaselineid);
			List<Map> lm = DBTool1.runsql(sql);
			cachedSecBas.put(gapBaselineid, lm);
			gapBaseline = lm;
		} else {
			gapBaseline = cachedSecBas.get(gapBaselineid);
		}

		for (Map m : gapBaseline) {
			Date startTime = (Date) m.get("startTime");
			Date endTime = (Date) m.get("endTime");

			if (DateTool.inSec(polltime, startTime, endTime)) {
				return (Integer)m.get(col);
			}
		}
		return 1;

	}
	
	
	
	
	
	public static void main(String[] args) {
		System.out.println(getSecBas("tpcc_simple1",new Date(),"bvalue"));
		System.out.println(getSecBas("tpcc_simple1",new Date(),"bvalue2"));


		// BaselineTool.initBaseline("trans_tbl_scs", "WEEKDAY", "SCS", "TXNVOL", "ALL",
		// "ALL", "ALL");

		// BaselineTool.initBaseline("trans_tbl", "WEEKDAY", "ICCD", "TXNVOL", "ALL",
		// "ALL", "ALL");

		// BaselineTool.initBaseline("trans_tbl_scs", "WEEKEND", "SCS", "TXNVOL",
		// "ALL","ALL","ALL","2019-06-15");
		// BaselineTool.initBaseline("trans_tbl_scs", "WEEKDAY", "SCS", "TXNVOL",
		// "ALL","ALL","ALL","2019-06-12");
		// BaselineTool.initBaseline("trans_tbl", "WEEKEND", "ICCD", "TXNVOL", "ALL",
		// "ALL", "ALL","2019-06-15");
		// BaselineTool.initBaseline("trans_tbl", "WEEKDAY", "ICCD", "TXNVOL", "ALL",
		// "ALL", "ALL");
		// BaselineTool.initBaseline("trans_tbl", "WEEKEND", "ICCD", "TXNVOL", "ALL",
		// "ALL", "ALL");

		// init for 7天基线
		
		//BaselineTool.initBaseline("bocnet_detail_tbl", "DAY1", "GCSC", "TXNVOL", "ALL","E153","ALL","2019-06-10"); 
		 //BaselineTool.initBaseline("bocnet_detail_tbl", "DAY2", "GCSC", "TXNVOL", "ALL","E153","ALL","2019-06-11");
		//BaselineTool.initBaseline("bocnet_detail_tbl", "DAY3", "GCSC", "TXNVOL", "ALL","E153","ALL","2019-06-12"); 
		//BaselineTool.initBaseline("bocnet_detail_tbl", "DAY4", "GCSC", "TXNVOL", "ALL","E153","ALL","2019-06-13");		 
		// BaselineTool.initBaseline("bocnet_detail_tbl", "DAY5", "GCSC", "TXNVOL", "ALL","E153","ALL","2019-06-14"); 
		// BaselineTool.initBaseline("bocnet_detail_tbl", "DAY6", "GCSC", "TXNVOL", "ALL","E153","ALL","2019-06-15");
		// BaselineTool.initBaseline("bocnet_detail_tbl", "DAY0", "GCSC", "TXNVOL", "ALL","E153","ALL","2019-06-16");
		 

		// BaselineTool.initBaseline("bocnet_detail_tbl", "WEEKDAY", "RCPS", "TXNVOL",
		// "ALL", "E127", "ALL");
		// BaselineTool.initBaseline("bocnet_detail_tbl", "WEEKEND", "RCPS", "TXNVOL",
		// "ALL", "E127", "ALL");
		
		
		
		

	}

}
