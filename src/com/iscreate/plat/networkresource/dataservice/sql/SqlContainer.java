package com.iscreate.plat.networkresource.dataservice.sql;



import java.util.ArrayList;
import java.util.List;

/**
 * @author HuangFu
 *
 */
public class SqlContainer {
	
	/**
	 * SQL语句
	 */
	private String sqlString;
	/**
	 * SQL参数列表
	 */
	private List<Object> paremters = new ArrayList<Object>();
	
	
	
	/**
	 * @param sqlString
	 */
	public SqlContainer(String sqlString) {
		this.sqlString = sqlString;
	}

	/**
	 * @param sqlString
	 * @param paremters
	 */
	public SqlContainer(String sqlString, List<Object> paremters) {
		this.sqlString = sqlString;
		this.paremters = paremters;
	}


	public void setString(int parameterIndex, String x) {
		// TODO Auto-generated method stub
		this.paremters.add(parameterIndex, x);
	}

	public String getSqlString() {
		return sqlString;
	}

	private void setSqlString(String sqlString) {
		this.sqlString = sqlString;
	}

	public List<Object> getParemters() {
		return paremters;
	}

	private void setParemters(List<Object> paremters) {
		this.paremters = paremters;
	}

}
