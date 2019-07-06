package com.boc.db;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbcp.BasicDataSource;

public class DBTool148 {
	static BasicDataSource bds = init();

	static private synchronized BasicDataSource init() {
		BasicDataSource bds = new BasicDataSource();
		bds.setDriverClassName("com.mysql.jdbc.Driver");
		//bds.setUrl("jdbc:mysql://21.122.49.148:3306/transdb");
		//bds.setUsername("yth");
		//bds.setPassword("ythdb");
		
		bds.setUrl("jdbc:mysql://21.122.49.148:3306/transdb?serverTimezone=Asia/Shanghai");
		bds.setUsername("yth");
		bds.setPassword("ythdb");
		
		bds.setTestWhileIdle(true);
		bds.setMaxActive(20);
		bds.setValidationQuery("SELECT 1");
		bds.setTestWhileIdle(true);
		bds.setTimeBetweenEvictionRunsMillis(300000);
		bds.setNumTestsPerEvictionRun(20);
		bds.setMinEvictableIdleTimeMillis(3600000);
		// bds.setConnectionProperties(arg0);
		return bds;
	}

	public static Connection getCon() {

		if (bds == null) {
			bds = init();
		}
		try {
			// System.out.println(bds);
			return bds.getConnection();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			bds = init();
			try {
				return bds.getConnection();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				System.out.println("++++++++++++++++second time");
				e1.printStackTrace();
			}
		}

		return null;
	}

	public static Boolean reset() {
		if (bds != null)
			try {
				bds.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		bds = init();
		return !bds.isClosed();

	}

	public static List<Map> runsql(String sql) {
		System.out.println("===run sql:" + sql);
		System.out.println("********pool size:" + bds.getNumActive());
		try {

			Connection con = getCon();
			if(con!=null){
			Statement stat = con.createStatement();
			ResultSet rs = stat.executeQuery(sql);
			List<Map> lm = rsToList(rs);

			rs.close();
			stat.close();
			con.close();
			// System.out.println("===="+lm);
			return lm;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		return null;

	}

	public static boolean rundml(String sql) {
		System.out.println("===" + sql);
		try {
			Connection con = bds.getConnection();
			if (con != null) {
				Statement stat = con.createStatement();
				Boolean result = stat.execute(sql);

				stat.close();
				con.close();
				System.out.println("===" + result);
				return result;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static int update(String sql) {
		System.out.println("===" + sql);
		try {

			Connection con = bds.getConnection();
			if (con != null) {
				Statement stat = con.createStatement();
				int result = stat.executeUpdate(sql);

				stat.close();
				con.close();
				System.out.println("===" + result);
				return result;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	static List<Map> rsToList(ResultSet rs) {
		// TODO Auto-generated method stub
		try {
			List<Map> lm = new ArrayList<Map>();
			while (rs.next()) {
				Map m = new LinkedHashMap();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					String col = rs.getMetaData().getColumnName(i);
					Object value = rs.getObject(i);
					m.put(col, value);
				}
				lm.add(m);
			}
			return lm;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Date getMaxbysql(String msql) {
		List<Map> lm = DBTool1.runsql(msql);
		try {
			java.sql.Timestamp sd = (java.sql.Timestamp) lm.get(0).get(
					"maxtime");
			return sd;
			//cd = Date.from(sd.toInstant());
		} catch (Exception e) {
			e.printStackTrace();
	
		}
		return null;
	}
	
	public static String VALUECOL="valuecol";
	public static BigDecimal getValue(String msql) {
		List<Map> lm = DBTool1.runsql(msql);
		try {
			Object o0=lm.get(0).get(DBTool1.VALUECOL);
		
			BigDecimal bg=toBigDecimal(o0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new BigDecimal(0);
	}

	private static 	BigDecimal toBigDecimal(Object o0) {
		BigDecimal bg=new BigDecimal(0);
		if (o0 instanceof Integer)
			bg = BigDecimal.valueOf(((Integer) o0).longValue());
		if (o0 instanceof BigDecimal)
			bg = (BigDecimal) o0;
		if (o0 instanceof Float)
			bg = BigDecimal.valueOf(((Float) o0).longValue());
		if (o0 instanceof Long)
			bg = BigDecimal.valueOf(((Long) o0).longValue());
		return bg;
	}
	
	public static void close() {
		try {
			bds.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		Integer val1 = 100;
	//	String sql = "insert into mv_cpu ('VAL1','VAL2','VAL3','VAL4')";
		//sql = sql.replace("VAL1", val1.toString()).replace("VAL2",
				//val1.toString());
          System.out.println(DBTool1.runsql("select count(*)　from　scs_trans_tbl"));
				
//		sql = "insert into bocnet_app_tbl (appName,pollTime,period,txnType,txnChannel,txnVol,txnRatioSuc,txnRespTime) values ('BOCNETAPP',str_to_date('20181011 12:21','%Y%m%d %H:%i'),'20181011','MCIS','MCIS',352725,98.60,90.57)";
//		DBTool1.rundml(sql);
		
		//System.out.println(DBTool1.getMaxbysql("select max(pollTime) as maxtime from trans_tbl tt where appName='ATMP'"));
		// System.out.println(DBTool1.runsql("select abs(dc) as AMOUNT from duo11 where period='20151111' and time='07:00-08:00'"));
		// DBTool1.close();
		// System.out.println(DBTool1.reset());
		// System.out.println(DBTool1.runsql("select abs(dc) as AMOUNT from duo11 where period='20151111' and time='07:00-08:00'"));
	}

}
