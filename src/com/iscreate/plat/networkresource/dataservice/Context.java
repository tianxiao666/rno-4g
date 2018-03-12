package com.iscreate.plat.networkresource.dataservice;

import java.util.List;

import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;



/**
 * @author HuangFu
 * 
 */
public interface Context {
	/**
	 * 插入对象
	 * @param entity
	 * @return
	 */
	//public boolean insert(BasicEntity basicEntity);
	
	/**
	 * 更新对象
	 * @param entity
	 * @param query
	 * @return
	 */
	//public boolean update(BasicEntity basicEntity,Query query);

	/**
	 * 根据查询条件删除对象
	 * @param query
	 * @return
	 */
	//public boolean delete(Query query);

	/**
	 * 根据查询条件返回对象列表
	 * @param query
	 * @return
	 */
	//public List<Entity> queryList(Query query);

	/**
	 * 创建查询条件对象
	 * @return
	 */
	public Query createQueryBuilder(String tableName);
	
	/**
	 * 创建SQL语句容器
	 * @param sqlString
	 * @return
	 */
	public SqlContainer createSqlContainer(String sqlString);	
	
	/**
	 * 创建SQL语句容器
	 * @param sqlString
	 * @param paramters
	 * @return
	 */
	//public SqlContainer createSqlContainer(String sqlString,List<Object> paramters);	
	
	/**
	 * 执行Insert操作的SQL语句
	 * @param sqlContainer
	 * @return
	 */
	public int executeInsertSQL(SqlContainer sqlContainer,String entityName);
	
	/**
	 * 执行Update操作的SQL语句
	 * @param sqlContainer
	 * @return
	 */
	public int executeUpdateSQL(SqlContainer sqlContainer,String entityName);
	
	/**
	 * 执行Delete操作的SQL语句
	 * @param sqlContainer
	 * @return
	 */
	public int executeDeleteSQL(SqlContainer sqlContainer,String entityName);
	
	/**
	 * 执行Select操作的SQL语句
	 * @param sqlContainer
	 * @return
	 */
	public List executeSelectSQL(SqlContainer sqlContainer,String entityName);
	
	/**
	 * 显式设置是否自动提交事务
	 * @param flag
	 */
	//public void setAutoCommit(Boolean flag);

	/**
	 * 开启事务
	 * 
	 */
	//public void beginTransaction();

	/**
	 * 提交事务
	 */
	//public void commitTransaction();

	/**
	 * 回滚事务
	 */
	///public void rollbackTransaction();

}
