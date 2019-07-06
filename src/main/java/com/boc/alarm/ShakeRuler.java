package com.boc.alarm;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

import com.boc.db.DBTool1;

public class ShakeRuler implements Rule {
	

	BigDecimal thresh;
	LinkedList<BigDecimal> history = new LinkedList<BigDecimal>();
	@Override
	public void init(String rid) {
		// TODO Auto-generated method stub
		String sql = "select * from rules where rid='RID'";
		sql = sql.replace("RID", rid);
		Map m = DBTool1.getFirst(DBTool1.runsql(sql));
		if (m != null) {
			/* init ruler table */
		//	gap = (Integer) m.get("para1");
		//	continues = (Integer) m.get("para2");

		}
		/* TODO */
	
	//	initHis(10);
		
	}

	

	public LinkedList<BigDecimal> getHis() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean check(Date polltime, BigDecimal current1) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getRulerid() {
		// TODO Auto-generated method stub
		return "";
	}



	@Override
	public String getSys() {
		// TODO Auto-generated method stub
		return null;
	}

}
