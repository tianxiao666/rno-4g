/**
 * 
 */
package com.iscreate.plat.networkresource.dataservice;
import java.sql.SQLException;
import java.util.List;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;


/**
 * @author HuangFu
 * 
 */
public interface SqlDatabaseService {

	/**
	 * 关系数据库insert
	 * 
	 * @param dataSourcePojo
	 * @param entity
	 * @return
	 */
	public int insert(Object object, BasicEntity basicEntity)
			throws SQLException;


	/**
	 * 执行Delete操作的SQL语句
	 * 
	 * @param dataSourceVo
	 * @param sqlContainer
	 * @return
	 */
	public int executeDeleteSQL(Object object,
			SqlContainer sqlContainer) throws SQLException;

	/**
	 * 执行Insert操作的SQL语句
	 * 
	 * @param dataSourceVo
	 * @param sqlContainer
	 * @return
	 */
	public int executeInsertSQL(Object object,
			SqlContainer sqlContainer) throws SQLException;

	/**
	 * 执行Update操作的SQL语句
	 * 
	 * @param dataSourceVo
	 * @param sqlContainer
	 * @return
	 */
	public int executeUpdateSQL(Object object,
			SqlContainer sqlContainer) throws SQLException;

	/**
	 * 执行Select操作的SQL语句
	 * 
	 * @param dataSourceVo
	 * @param sqlContainer
	 * @return
	 */
	public List<BasicEntity> executeSelectSQL(Object object,
			SqlContainer sqlContainer) throws SQLException;
}
