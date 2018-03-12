/**
 * 
 */
package com.iscreate.plat.networkresource.dataservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.DefaultParam;
import com.iscreate.plat.networkresource.common.tool.Entity;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;
import com.iscreate.plat.system.datasourcectl.DataSourceConst;
import com.iscreate.plat.system.datasourcectl.DataSourceContextHolder;


/**
 * @author HuangFu
 * 
 */
public class SqlDatabaseServiceImpl implements SqlDatabaseService {
	private static final Log logger = LogFactory
			.getLog(SqlDatabaseServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.iscreate.dataservice.SqlDatabaseService#insert(com.iscreate.dataservice.DataSourcePojo,
	 *      com.iscreate.common.tool.Entity, com.iscreate.dataservice.Query)
	 */

	public int insert(Object object, BasicEntity basicEntity)
			throws SQLException {
		Connection conn = null;
		PreparedStatement pstm = null;
		String tableName = null;
		StringBuffer sql = null;
		try {
			tableName = basicEntity.getValue(DefaultParam.typeKey);
			sql = new StringBuffer();
			// ---------------组装SQL---------begin
			sql.append("insert into ");
			sql.append(tableName);
			if (basicEntity.keySize() == 0 || tableName == null)
				return 0;
			sql.append("(");
			for (String key : basicEntity.keyset()) {
				logger.debug("objectName=====" + key);
				logger.debug("objectValue=====" + basicEntity.getValue(key));
				if (!DefaultParam.typeKey.equals(key)
						&& !DefaultParam.idKey.equals(key)) {
					sql.append(key + ",");
				}
			}
			if (sql.charAt(sql.length() - 1) == ',')
				sql.deleteCharAt(sql.length() - 1);
			sql.append(") ");
			sql.append("values (");
			for (String key : basicEntity.keyset()) {
				if (!DefaultParam.typeKey.equals(key)
						&& !DefaultParam.idKey.equals(key)) {
					sql.append("\"" + basicEntity.getValue(key) + "\",");
				}
			}
			if (sql.charAt(sql.length() - 1) == ',')
				sql.deleteCharAt(sql.length() - 1);
			sql.append(") ");
			logger.debug("-------------insert SQL：" + sql);

			conn = DataSourceConn.initInstance().getConnection();  //Conn.getConn().getConnection();
			conn.setAutoCommit(false);
			pstm = conn.prepareStatement(sql.toString());
			pstm.execute();
			conn.commit();
		} catch (SQLException e) {
			conn.rollback();
			e.printStackTrace();
			logger.debug("Insert data faile and rallback date succeesful");
			return 0;
		} finally {
			if (pstm != null)
				pstm.close();
			if (conn != null)
				conn.close();
		}
		logger.debug("Insert data succeesfull");
		return 1;
	}

	public int executeDeleteSQL(Object object, SqlContainer sqlContainer)
			throws SQLException {
		// TODO Auto-generated method stub
		DataSource dataSource = null;
		Connection conn = null;
		PreparedStatement pstm = null;
		if (sqlContainer.getSqlString() == null
				|| "".equals(sqlContainer.getSqlString()))
			return 0;
		try {

			conn = DataSourceConn.initInstance().getConnection();  //Conn.getConn().getConnection();
			conn.setAutoCommit(false);
			pstm = conn.prepareStatement(sqlContainer.getSqlString());

			if (sqlContainer.getParemters().size() > 0) {
				for (int i = 0; i < sqlContainer.getParemters().size(); i++) {
					if (sqlContainer.getParemters().get(i) != null) {
						pstm.setString(i + 1, (String) sqlContainer
								.getParemters().get(i));
					}
				}
			}

			pstm.execute();
			conn.commit();
		} catch (SQLException e) {
			conn.rollback();
			e.printStackTrace();
			logger.debug("delete data faile and rallback date succeesful");
			return 0;
		} finally {
			if (pstm != null)
				pstm.close();
			if (conn != null)
				conn.close();
		}
		logger.debug("delete data succeesfull");
		return 1;
	}

	public int executeInsertSQL(Object object, SqlContainer sqlContainer)
			throws SQLException {

		Connection conn = null;
		PreparedStatement pstm = null;
		if (sqlContainer.getSqlString() == null
				|| "".equals(sqlContainer.getSqlString()))
			return 0;
		try {
			conn = DataSourceConn.initInstance().getConnection();  //Conn.getConn().getConnection();
			conn.setAutoCommit(false);
			pstm = conn.prepareStatement(sqlContainer.getSqlString());
			if (sqlContainer.getParemters().size() > 0) {
				for (int i = 0; i < sqlContainer.getParemters().size(); i++) {
					if (sqlContainer.getParemters().get(i) != null) {
						pstm.setString(i + 1, (String) sqlContainer
								.getParemters().get(i));
					}
				}
			}

			pstm.execute();
			conn.commit();
		} catch (SQLException e) {
			conn.rollback();
			e.printStackTrace();
			logger.debug("Insert data faile and rallback date succeesful");
			return 0;
		} finally {
			if (pstm != null)
				pstm.close();
			if (conn != null)
				conn.close();
		}
		logger.debug("Insert data succeesfull");
		return 1;
	}

	public List<BasicEntity> executeSelectSQL(Object object, SqlContainer sqlContainer)
			throws SQLException {

		List list = null;
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		ResultSetMetaData resultSetMetaData = null;
		if (sqlContainer.getSqlString() == null
				|| "".equals(sqlContainer.getSqlString()))
			return null;
		try {
			DataSourceContextHolder.setDataSourceType(DataSourceConst.netResDs);
			conn = DataSourceConn.initInstance().getConnection();  //Conn.getConn().getConnection();
			// conn.setAutoCommit(false);
			pstm = conn.prepareStatement(sqlContainer.getSqlString());

			if (sqlContainer.getParemters().size() > 0) {
				for (int i = 0; i < sqlContainer.getParemters().size(); i++) {
					if (sqlContainer.getParemters().get(i) != null) {
						pstm.setString(i + 1, (String) sqlContainer
								.getParemters().get(i));
					}
				}
			}
            //
			rs = pstm.executeQuery();
			Entity entity = null;
			
			String ColumnName = null;
			int tableNameCount = 1;
			while (rs.next()) {
				if (list == null)
					list = new ArrayList();
				entity = new Entity();
				resultSetMetaData = rs.getMetaData();
				String tableName = null;
				for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
					if (i == 1) {
						tableName = resultSetMetaData.getTableName(i);
					} else if (!tableName.equals(resultSetMetaData
							.getTableName(i))) {
						tableNameCount++;
					}

					ColumnName = resultSetMetaData.getColumnLabel(i);
					String entityColumnName = "";

					entityColumnName = ColumnName;
					entity.setValue(entityColumnName, rs.getString(ColumnName));

					logger.debug("Column Name:" + entityColumnName
							+ "->Column Value:" + rs.getString(ColumnName));
				}
				//entity.setValue("_entityType", "     ");

				list.add(entity);
			}
			// conn.commit();
		} catch (SQLException e) {
			// conn.rollback();
			e.printStackTrace();
			logger.debug("Select data faile and rallback date succeesful");
			return null;
		} finally {
	
			if (rs != null) {
				rs.close();
			}
			if (pstm != null)
				pstm.close();
			if (conn != null)
				conn.close();
		}
		logger.debug("Select data succeesfull");
		return list;

	}

	public int executeUpdateSQL(Object object, SqlContainer sqlContainer)
			throws SQLException {
		Connection conn = null;
		PreparedStatement pstm = null;
		if (sqlContainer.getSqlString() == null
				|| "".equals(sqlContainer.getSqlString()))
			return 0;
		try {
			conn = DataSourceConn.initInstance().getConnection();  //Conn.getConn().getConnection();
			conn.setAutoCommit(false);
			pstm = conn.prepareStatement(sqlContainer.getSqlString());
			if (sqlContainer.getParemters().size() > 0) {
				for (int i = 0; i < sqlContainer.getParemters().size(); i++) {
					if (sqlContainer.getParemters().get(i) != null) {
						pstm.setString(i + 1, (String) sqlContainer
								.getParemters().get(i));
					}
				}
			}

			pstm.execute();
			conn.commit();
		} catch (SQLException e) {
			conn.rollback();
			e.printStackTrace();
			logger.debug("Update data faile and rallback date succeesful");
			return 0;
		} finally {
			if (pstm != null)
				pstm.close();
			if (conn != null)
				conn.close();
		}
		logger.debug("Update data succeesfull");
		return 1;
	}

}
