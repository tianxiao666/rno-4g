package com.iscreate.op.service.workmanage.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.service.workmanage.WorkOrderHandleServiceImpl;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DataSelectUtil {

	private DataSource confDataSource;
	
	private static final Log logger = LogFactory.getLog(WorkOrderHandleServiceImpl.class);

	public List<Map> selectDataWithCondition(String executeSql,List<String> params) {
		List<Map> resultList = null;
		DataSource ds = null;
		Connection conn = null;
		PreparedStatement pstm = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			if (confDataSource != null) {
				conn = confDataSource.getConnection();
				if (params != null && !params.isEmpty()) {
					pstm = conn.prepareStatement(executeSql);
					for (int i = 0; i < params.size(); i++) {
						pstm.setString(i + 1, params.get(i));
					}
					rs = pstm.executeQuery();
				} else {
					stm = conn.createStatement();
					rs = stm.executeQuery(executeSql);
				}

				resultList = resultSet2MapList(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("jdbc查询数据的时候，数据库链接错误或者要执行的sql编写有误");
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (pstm != null) {
					pstm.close();
				}
				if (stm != null) {
					stm.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return resultList;
	}

	private static List<Map> resultSet2MapList(ResultSet resultSet) {
		List<Map> resultList = new ArrayList<Map>();
		if (resultSet != null) {
			try {
				ResultSetMetaData resultSetMetaData = null;
				String tableName = null;
				String columnName = null;
				int tableNameCount = 1;
				while (resultSet.next()){
					Map<String,Object> tempMap=new HashMap<String,Object>();
					resultSetMetaData = resultSet.getMetaData();
					for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
						columnName = resultSetMetaData.getColumnLabel(i);
						tempMap.put(columnName, resultSet.getString(columnName));
					}

					resultList.add(tempMap);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("jdbc查询数据后的结果集，有可能数据自身错误，引起操作发生错误，");
			} finally {
				
			}
		}
		return resultList;
	}

	public DataSource getConfDataSource() {
		return confDataSource;
	}

	public void setConfDataSource(DataSource confDataSource) {
		this.confDataSource = confDataSource;
	}

	
	
}
