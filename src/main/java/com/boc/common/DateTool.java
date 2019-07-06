package com.boc.common;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.slf4j.LoggerFactory;

public class DateTool {
	public static String getCurHour() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:00");
		String curr = df.format(new Date());
		return curr;
	}

	/********************** 柱状图默认End时间 ****************************/
	public static String getColumnEndDay() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String curr = df.format(new Date());
		return curr;
	}

	public static Date getCurHour1() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:00");
		String curr = df.format(new Date());
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date d = null;
		try {
			d = sdf1.parse(curr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return d;

	}

	public static Date toDate(String stime, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		Date d = null;
		try {
			d = df.parse(stime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return d;

	}

	public static String getCur1() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String curr = df.format(new Date());
		return curr;
	}

	public static String getDay() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00");
		String curr = df.format(new Date());
		return curr;
	}

	public static String getDay1() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String curr = df.format(new Date());
		return curr;
	}

	public static String getYestoday() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.DATE, -1);
		String curr = df.format(c.getTime());

		return curr;
	}

	public static String getLast() {
		Date d = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00");
		String curr = df.format(d.getTime() - 24 * 60 * 60 * 1000);
		return curr;
	}

	public static String getLastHour() {
		Date d = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String curr = df.format(d.getTime() - 60 * 60 * 1000);
		return curr;
	}

	public static Date toDateMm(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm");
		try {
			return sdf.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static String getStart(String time, Double h, Integer model) {
		// TODO Auto-generated method stub
		int hour = 0;
		int min = 0;
		if (Math.round(h) == h) {
			hour = h.intValue();
		} else {
			min = (new Double(h * 60)).intValue();
			hour = h.intValue();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date d = sdf.parse(time);
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal = cutTime(model, cal);
			System.out.println(hour);
			System.out.println(min);
			if (Math.round(h) == h) {
				cal.add(Calendar.HOUR, hour);
			} else {
				cal.add(Calendar.MINUTE, min);
			}
			SimpleDateFormat chfm = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
			return chfm.format(cal.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static String getEnd(String time, Integer model) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date d = sdf.parse(time);
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal = cutTime(model, cal);
			SimpleDateFormat chfm = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
			return chfm.format(cal.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Calendar cutTime(Integer model, Calendar cal) {
		if (model == 3) {
			return cal;
		} else if (model == 1) { // 小时取整
			cal.set(Calendar.MINUTE, 0);
		} else { // 30min取整
			int m = cal.get(Calendar.MINUTE);
			if (m >= 30) {
				cal.set(Calendar.MINUTE, 30);
			} else {

				cal.set(Calendar.MINUTE, 0);
			}
		}
		return cal;
	}

	/********************** 柱状图默认Start时间 ****************************/
	public static String getColumnStartDay() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String startDay = df.format(new Date());
		Date dateNow = new Date();
		Date dateBefore_7 = new Date();

		Calendar tmpCalendar = Calendar.getInstance();
		tmpCalendar.setTime(dateNow);
		tmpCalendar.add(tmpCalendar.DAY_OF_MONTH, -29);

		dateBefore_7 = tmpCalendar.getTime();
		startDay = df.format(dateBefore_7);
		return startDay;
	}

	public static String getIntertime(String stime, String etime) {
		// TODO Auto-generated method stub
		String et = etime.substring(12);
		if (etime.endsWith("00:00"))
			et = "23:59";
		System.out.println(etime);
		String s = stime.substring(12) + "-" + et;
		System.out.println("intertime:" + s);
		return s;
	}

	public static String toStringMm(Date time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return df.format(time);

	}

	public static String toStringMm1(Date time) {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		return df.format(time);

	}

	public static String getPeriod(String time) {
		// TODO Auto-generated method stub
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			System.out.println(time);
			Date dtime = df.parse(time);
			SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd");
			return df1.format(dtime);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static String getPeriod() {
		// TODO Auto-generated method stub

		Date dtime = new Date();
		SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd");
		return df1.format(dtime);

	}

	public static String getDay2() {
		Date d = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String curr = df.format(new Date(d.getTime() - 24 * 60 * 60 * 1000));
		// System.out.println("day2:"+curr);
		return curr;
	}

	public static String getChtime(String time) {
		// TODO Auto-generated method stub
		// Date date=new Date(time);
		// Calendar c=Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String start = "";
		String end = "";
		try {
			Date d = sdf.parse(time);
			SimpleDateFormat chfm = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");

			return chfm.format(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static String getUnixTS() {
		long time = System.currentTimeMillis();
		return String.valueOf(time);

	}

	public static Boolean isWeekend(Date polltime) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(polltime);
		int day = ca.get(Calendar.DAY_OF_WEEK);
		System.out.println(day);

		if (day == 7 || day == 1)
			return true;
		else
			return false;

		// TODO Auto-generated method stub

	}

	public static Integer getWeekday(Date polltime) {
		/*
		 * return 0:Sunday return 1:Monday .......
		 */
		Calendar ca = Calendar.getInstance();
		ca.setTime(polltime);
		int day = ca.get(Calendar.DAY_OF_WEEK);
		System.out.println(day);
		return day - 1;

		// TODO Auto-generated method stub

	}

	public static int getMins(Date polltime) {
		return polltime.getHours() * 60 + polltime.getMinutes();
	}

	public static Boolean inSec(Date polltime, Date stime, Date etime) {
		// TODO Auto-generated method stub
		int s = DateTool.getMins(stime);
		
		int now = DateTool.getMins(polltime);
		if(now>1440-60)now=1440-61;
		int e = DateTool.getMins(etime);
	//	Log.debug(s + "," + now + "," + e);
		if (s <= now && e > now) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {

		System.out.println(isWeekend(new Date()));
		Log.info("datetoool");
//		System.out.println(DateTool.getUnixTS());
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(DateTool.getUnixTS());
//		Date d = new Date();
//
//		System.out.println(DateTool.toDate("20181014 00:00", "yyyyMMdd HH:mm"));
//		List<Map> lm = DBTool1
//				.runsql("select max(pollTime) as maxtime from bocnet_app_tbl");
//		java.sql.Timestamp sd = (java.sql.Timestamp) lm.get(0).get("maxtime");
//		Date cd = Date.from(sd.toInstant());
//		System.out.println(cd);
//
//		if (DateTool.getCurHour1().getTime() > d.getTime()) {
//			System.out.println("1");
//		} else {
//			System.out.println("2");
//
//		}

//		System.out.println(DateTool.getLastHour());
//
//		Date d1 = DateTool.toDate("20190301", "yyyyMMdd");
//		Calendar c = Calendar.getInstance();
//		c.setTime(d1);
//		c.add(Calendar.DATE, -1);
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		String yes = df.format(c.getTime());
//		// LogFactory.getl
//		LoggerFactory.getLogger(DateTool.class).info(yes);

	}

}
