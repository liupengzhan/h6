package com.boc.alarm;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.apache.ibatis.reflection.ArrayUtil;

import com.boc.common.Log;

public class ListTool {
	
	public static void push(LinkedList ll,Integer i) {
		ll.removeFirst();
		ll.add(i);
	//	System.out.println(ArrayUtil.toString(ll));
	}
	
	
	public static void push(LinkedList ll,BigDecimal i) {
		ll.removeFirst();
		ll.add(i);
	}
	
	public static void print(List list) {
		String s="";
		for(Object o:list) {
			s+=o.toString()+",";
			
		}
		System.out.println(s);
	}
	
	public static void main(String[] args) {
		LinkedList<Integer> ll=new LinkedList<Integer>();
		for (int i = 0; i < 10; i++) {
			ll.add(0);
		}
		for (int j =0;j<10;j++) {
			push(ll,j);
			print(ll);
		}
		ll.set(9, 1);
		ll.set(8, 0);
		ll.set(7, 1);
		
		Log.info(getRecent(4,ll));
		
	}
	
	
	public static Integer getRepeat(List<Integer> history) {
		// TODO Auto-generated method stub
		int count=0;
		for(int i=0;i<10;i++)
		{
			if(history.get(i)==0) {
				count=0;
			}else {
				count++;
			}
			
		}

		return count;
	}


	public static Integer getRecent(Integer range, LinkedList<Integer> resulthis) {
		// TODO Auto-generated method stub
		int result=0;
		int size=resulthis.size();
		if(range>size) {
			return -1;
		}
		for(int i=1;i<=range;i++)
		{
			int idx=size-i;
			if(resulthis.get(idx)==1)result++;
		}
		return result;
	}

}
