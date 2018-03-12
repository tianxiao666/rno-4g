package com.iscreate.op.service.workmanage.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class IdGeneratorUtil {


	static ApplicationContext context;
	static ComboPooledDataSource confDataSource;
	
	
	static int initCode = 1;
	
	static Map<String, String> sqlMap = new HashMap<String, String>();

	public static long nextLong(String flagName) throws SQLException {
		if(context==null){
			HttpServletRequest request = ServletActionContext.getRequest();
			context = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		}
		
		confDataSource = (ComboPooledDataSource) context.getBean("oracleDataSource");
		Connection conn = null;
		long resultCode=0l;
		if (confDataSource != null) {
			conn = confDataSource.getConnection();
			Map<String, String> sqlMap = getSqlMapByFlagName(flagName);

			long currentCode = getCurrentCode(conn);
			
			//System.out.println("currentCode==="+currentCode);
			
			if (currentCode == 0l) {
				// insert
				saveCode(conn);
				currentCode=getCurrentCode(conn);
			}else{
				updateCode(conn);
			}
			
			
			resultCode=getCurrentCode(conn);
//			conn.commit();
			
			//System.out.println("更新后code==="+currentCode);
		}
		if (conn != null) {
//			conn.setAutoCommit(true);
			conn.close();
		}
		return resultCode;
	}

	public static Map<String, String> getSqlMapByFlagName(String flagName) {
		try {
//			String selectSql = "SELECT code FROM TBL_UNIQUE_UNIQUETABLE WHERE name = '"+ flagName + "' ";
//			String insertSql = "INSERT INTO TBL_UNIQUE_UNIQUETABLE (name, code) VALUES ('"+ flagName + "', ?)";
//			String updateSql = "UPDATE TBL_UNIQUE_UNIQUETABLE SET code = ? WHERE name = '"+ flagName + "'";
			
			String selectSql = "select code from wm_id_unique where name = '"+ flagName + "' ";
			String insertSql = "insert into wm_id_unique (name, code) values ('"+ flagName + "', ?)";
			String updateSql = "update wm_id_unique set code = ? where name = '"+ flagName + "'";
			
			
			sqlMap.put("select", selectSql);
			sqlMap.put("insert", insertSql);
			sqlMap.put("update", updateSql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sqlMap;
	}

	/**
	 * 获取当前存在的code
	 * 
	 * @param connection
	 * @throws SQLException
	 */
	public static long getCurrentCode(Connection connection)
			throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		long currentCode = 0l;
		try {
			pstmt = connection.prepareStatement(sqlMap.get("select"));
			rs = pstmt.executeQuery();
			if (rs.next()) {
				currentCode = rs.getLong("code");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		}
		return currentCode;
	}

	/**
	 * 保存code
	 * 
	 * @param connection
	 * @throws SQLException
	 */
	public static int saveCode(Connection connection)
			throws SQLException {
		PreparedStatement pstmt = null;
		int resultCode = 0;
		try {
			String insertSql=sqlMap.get("insert");
			pstmt = connection.prepareStatement(insertSql);
			pstmt.setInt(1, initCode);
			resultCode = pstmt.executeUpdate();
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
		}
		return resultCode;
	}

	/**
	 * 更新code
	 * 
	 * @param connection
	 * @throws SQLException
	 */
	public static int updateCode(Connection connection)
			throws SQLException {
		
		int resultCode = 0;
		PreparedStatement pstmt = null;
	    try {
	    	long currentCode = getCurrentCode(connection);
	    	long targetCode=currentCode+1;
	    	
	    	pstmt = connection.prepareStatement(sqlMap.get("update"));
	    	pstmt.setLong(1, targetCode);
	    	resultCode=pstmt.executeUpdate();
	    } finally {
	      if (pstmt != null)pstmt.close();
	    }
		return resultCode;
	}

	public ComboPooledDataSource getConfDataSource() {
		return confDataSource;
	}

	public void setConfDataSource(ComboPooledDataSource confDataSource) {
		this.confDataSource = confDataSource;
	}

}
