package com.boc.collecter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.boc.common.DateTool;
import com.boc.db.DBTool1;
import com.boc.msg.HttpRequest;

public class CommonCollecter {
	Map cache = new HashMap();
	static String LAST_DATE = "last_date";
	static String LAST_RECS = "last_rec";
	String maxsql = "SELECT * FROM trans_tbl_scs ORDER BY pollTime DESC LIMIT 1";
	String INSERTSQL = "INSERT INTO trans_tbl_scs (appName,pollTime,txnVol) VALUES ('SCS',STR_TO_DATE('$POLL_TIME$','%Y-%m-%d %H:%i:%s'),$TXNVOL$);";
	String date_format = "yyyy-MM-dd HH:mm:ss";// 时间如果是字符型，其格式
	String record_column = "record_time";// 代表时间的列名

	public void getData(Integer threshhold, Map para) {

		String s = HttpRequest.sendPost2(HttpRequest.scs_url, para);
		System.out.println(HttpRequest.scs_url);
		// System.out.println(s);
		List<Map> lm = (List<Map>) JSON.parse(s);
		Date last_date = (Date) cache.get(this.LAST_DATE);
		if (last_date == null) {
			last_date = initmax();
		}
		// List<Map> m
		Date newdate = last_date;
		Object lastrec = cache.get(this.LAST_RECS);
		for (Map m : lm) {
			Object date_object = (String) m.get(this.record_column);
			// System.out.println(date_str);
			Date date;
			if (date_object instanceof String)
				date = DateTool.toDate((String) date_object, date_format);
			else
				date = (Date) date_object;

			if (date.after(last_date)) {
				check(m, lastrec, threshhold);
				String sql = transql(m);

				// System.out.println(sql);
				int i = DBTool1.update(sql);
				newdate = date;
				lastrec = m;

			}

		}
		if (newdate != null) {
			cache.put(this.LAST_DATE, newdate);
			cache.put(this.LAST_RECS, lastrec);
		}

		// checkLast();

	}

	private Boolean check(Map m, Object lastrecs, Integer threshold) {
		// TODO Auto-generated method stub
		// Integer curVol =Integer.valueOf((String) m.get("txn"));
		// Integer lastVol =Integer.valueOf((String) lastrec.get("txn"));

		if (lastrecs instanceof Map) {
			Map lastrec=(Map)lastrecs;
			Integer curVol = (Integer) m.get("txn");
			Integer lastVol = (Integer)lastrec.get("txn");
			System.out.println("SCS CHECK,curVol:" + curVol + "lastVol:" + lastVol + "," + Math.abs(curVol - lastVol));

			if (Math.abs(curVol - lastVol) > threshold) {
				HttpRequest.sendMsg("SCS TXN EXCEPTION ,Last txn is " + lastVol + ",current is " + curVol);
				return false;
			}
		}
		return true;
	}

	private Date initmax() {
		// TODO Auto-generated method stub
		Date d;
		List<Map> lm = DBTool1.runsql(SCSProcess.MAXSQL);
		if (lm.size() > 0) {
			Map m = lm.get(0);
			d = (Date) m.get("pollTime");
			Map lastrec = new HashMap();
			lastrec.put("txn", (Integer) m.get("txnVol"));

			cache.put(SCSProcess.LAST_REC, lastrec);
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

}
