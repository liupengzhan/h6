package com.boc.demo;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.alibaba.fastjson.JSON;
import com.boc.db.DBTool1;

@RestController
@Service
public class DController {
	@Autowired 
	private HttpServletRequest request;
	
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public Object listMode() {
		String sql="select count(*) from scs_trans_tbl";
		List<Map> lr = DBTool1.runsql(sql);
		return JSON.toJSONString(lr);
		
	}
	
	
	@RequestMapping(value = "/list1", method = { RequestMethod.GET, RequestMethod.POST })
	public Object list1() {
		
		return JSON.toJSONString("hello,world");
		
	}
	
	
	
	@RequestMapping(value = "/list2", method = { RequestMethod.GET, RequestMethod.POST })
	public Object list2() {
		
	//	String s=RequestContextUtils.g
		String s="";
		s=request.getParameter("para1");
		String sys=request.getParameter("sys");
		String sql="SELECT * FROM trans_tbl WHERE appName='SYS'  LIMIT 1,100";
		sql=sql.replace("SYS", sys);
		List<Map> lr =DBTool1.runsql(sql);
		return JSON.toJSONString(lr);
		
	}
	

}
