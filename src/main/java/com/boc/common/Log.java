package com.boc.common;

import org.apache.log4j.Logger;

public class Log {
	static Logger logger=Logger.getRootLogger();
	public static void info(Object s) {
		logger.info(s);
	}
	
	public static void debug(Object s) {
		logger.debug(s);;
	}
	
	
	public static void error(Object s) {
		logger.error(s);;
	}
	
	
	public static void warn(Object s) {
		logger.warn(s);;
	}

}