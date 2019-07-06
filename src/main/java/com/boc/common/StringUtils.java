package com.boc.common;

public class StringUtils {

	public static boolean isBlank(String expression) {
		// TODO Auto-generated method stub
		return expression.equals("");
	}

	public static String nullprocess(String str) {
		// TODO Auto-generated method stub
		if (str == null)
			return "";
		else
			return str;
	}

}
