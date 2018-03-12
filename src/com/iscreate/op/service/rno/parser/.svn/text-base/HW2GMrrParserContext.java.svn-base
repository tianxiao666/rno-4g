package com.iscreate.op.service.rno.parser;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class HW2GMrrParserContext {

	private long areaId;
	private long cityId;
	
	//相关的preparedstatment
	private Map<String,PreparedStatement> preparedStmts=new HashMap<String,PreparedStatement>();
	
	private StringBuilder errMsg=new StringBuilder();

	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}
	
	public void appedErrorMsg(String msg){
		errMsg.append(msg);
	}
	
	public String getErrorMsg(){
		return errMsg.toString();
	}
	
	public void closeAllStatement() {
		for(PreparedStatement s:preparedStmts.values()){
			if(s!=null){
				try {
					s.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}


	public boolean setPreparedStatment(String name,
			PreparedStatement stmt) {
		if(name==null || "".equals(name.trim()) || stmt==null){
			return false;
		}
		preparedStmts.put(name, stmt);
		
		return true;
	}

	public PreparedStatement getPreparedStatement(String name){
		if(name==null || "".equals(name.trim())){
			return null;
		}
		return preparedStmts.get(name);
	}




}
