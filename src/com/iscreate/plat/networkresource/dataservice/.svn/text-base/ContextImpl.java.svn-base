package com.iscreate.plat.networkresource.dataservice;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;

/**
 * @author HuangFu
 *
 */
public class ContextImpl implements Context {
	
	private static final Log logger = LogFactory
	.getLog(ContextImpl.class);
	/**
	 * 所有数据源存储MAP
	 */
	private Map<String, Object> dataSourceMap = null;

	/**
	 * @param dataSourcePojo
	 */
	public ContextImpl(Map<String,Object> map) {
			dataSourceMap = map;
	}

	/* (non-Javadoc)
	 * @see com.iscreate.dataservice.Context#beginTransaction()
	 */

	/**
	public void beginTransaction() {
		// TODO Auto-generated method stub
		}
    ***/
	/* (non-Javadoc)
	 * @see com.iscreate.dataservice.Context#commitTransaction()
	 */

	/***
	public void commitTransaction() {
		// TODO Auto-generated method stub

	}
    ***/ 
	/* (non-Javadoc)
	 * @see com.iscreate.dataservice.Context#createQueryBuilder()
	 */
	

	public Query createQueryBuilder(String tableName) {
		// TODO Auto-generated method stub
		return new Query(tableName);
	}

	/* (non-Javadoc)
	 * @see com.iscreate.dataservice.Context#delete(com.iscreate.dataservice.Query)
	 */
	
	/***
	public boolean delete(Query query) {
		// TODO Auto-generated method stub
		DataSourceVo dataSourceVo = getDataSourceVo(query);
		String type = null;
		if(dataSourceVo==null){
			return false;
		}
		type = dataSourceVo.getDataSourceType();
		
		//关系数据库
		if("SQL".equals(type)){
			try {
				return new SqlDatabaseServiceImpl().delete(dataSourceVo, query);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}else{
			try {
				return new NoSqlDatabaseServiceImpl().delete(dataSourceVo, query);
			} catch (MongoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
	}
     **/

	/* (non-Javadoc)
	 * @see com.iscreate.dataservice.Context#insert(java.lang.Object)
	 */
	/**********************
	@Override
	public boolean insert(BasicEntity basicEntity) {
		// TODO Auto-generated method stub		
		DataSourceVo dataSourceVo = getDataSourceVo(basicEntity);
		String type = null;
		if(dataSourceVo==null){
			return false;
		}
		type = dataSourceVo.getDataSourceType();
		//关系数据库
		if("SQL".equals(type)){
			try {
				return new SqlDatabaseServiceImpl().insert(dataSourceVo, basicEntity);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}			
		}else{
			try {
				return new NoSqlDatabaseServiceImpl().insert(dataSourceVo, basicEntity);
			} catch (MongoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}		
	}
	**************/
	/* (non-Javadoc)
	 * @see com.iscreate.dataservice.Context#update(com.iscreate.common.tool.Entity, com.iscreate.dataservice.Query)
	 */
	/*********************
	@Override
	public boolean update(BasicEntity basicEntity, Query query) {
		// TODO Auto-generated method stub
		DataSourceVo dataSourceVo = getDataSourceVo(query);
		String type = null;
		if(dataSourceVo==null){
			return false;
		}
		type = dataSourceVo.getDataSourceType();
		//关系数据库
		if("SQL".equals(type)){
			try {
				return new SqlDatabaseServiceImpl().update(dataSourceVo, basicEntity, query);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}			
		}else{
			try {
				return new NoSqlDatabaseServiceImpl().update(dataSourceVo, basicEntity, query);
			} catch (MongoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		
	}
	***********************/
	
	/* (non-Javadoc)
	 * @see com.iscreate.dataservice.Context#queryList(com.iscreate.dataservice.Query)
	 */

	/*********************
	public List<Entity> queryList(Query query) {
		// TODO Auto-generated method stub
		DataSourceVo dataSourceVo = getDataSourceVo(query);
		String type = null;
		if(dataSourceVo==null){
			return null;
		}
		type = dataSourceVo.getDataSourceType();
		List<Entity> list = null;
		if("SQL".equals(type)){
			try {
				list = new SqlDatabaseServiceImpl().query(dataSourceVo, query);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}else{
			try {
				list = new NoSqlDatabaseServiceImpl().query(dataSourceVo, query);
			} catch (MongoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return list;
	}
*********************/
	/* (non-Javadoc)
	 * @see com.iscreate.dataservice.Context#rollbackTransaction()
	 */

	/*********
	public void rollbackTransaction() {
		// TODO Auto-generated method stub

	}
******/
	/* (non-Javadoc)
	 * @see com.iscreate.dataservice.Context#setAutoCommit(java.lang.Boolean)
	 */

	/*****
	public void setAutoCommit(Boolean flag) {
		// TODO Auto-generated method stub
		
	}
*****/
	/***
	private DataSourceVo getDataSourceVo(Query query){
		DataSourceVo dataSourceVo = null;
		try {
			String dataSourceName = ((String[])EntityConfigUtil.getEntityConfigMap().get(query.getEntityOrClassName()))[0];
			dataSourceVo =  (DataSourceVo)dataSourceMap.get(dataSourceName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return dataSourceVo;
	}
   **/
	
    /**
	private DataSourceVo getDataSourceVo(BasicEntity basicEntity){
		DataSourceVo dataSourceVo = null;
		try {
			String dataSourceName = ((String[])EntityConfigUtil.getEntityConfigMap().get(basicEntity.getValue(DefaultParam.typeKey)))[0];
			dataSourceVo =  (DataSourceVo)dataSourceMap.get(dataSourceName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return dataSourceVo;
	}
	***/
	/******************
	private DataSourceVo getDataSourceVo(String entityName){
		DataSourceVo dataSourceVo = null;
		try {
			String dataSourceName = ((String[])EntityConfigUtil.getEntityConfigMap().get(entityName))[0];
			dataSourceVo =  (DataSourceVo)dataSourceMap.get(dataSourceName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return dataSourceVo;
	}
	******************/
	
	public SqlContainer createSqlContainer(String sqlString) {
		// TODO Auto-generated method stub
		return new SqlContainer(sqlString);
	}




	public int executeDeleteSQL(SqlContainer sqlContainer,String entityName) {
		
		// TODO Auto-generated method stub
		/***
		DataSourceVo dataSourceVo = getDataSourceVo(entityName);
		String type = null;
		if(dataSourceVo==null){
			return false;
		}
		type = dataSourceVo.getDataSourceType();
		***/
		//关系数据库
			try {
				return new SqlDatabaseServiceImpl().executeDeleteSQL(null, sqlContainer);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}
		
		
	}


	public int executeInsertSQL(SqlContainer sqlContainer,String entityName) {
		
			try {
				return new SqlDatabaseServiceImpl().executeInsertSQL(null, sqlContainer);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}
		
	}

	
	public List executeSelectSQL(SqlContainer sqlContainer,String entityName) {
		// TODO Auto-generated method stub
			try {
				List list= new SqlDatabaseServiceImpl().executeSelectSQL(null, sqlContainer);
				return list;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
	}

	
	public int executeUpdateSQL(SqlContainer sqlContainer,String entityName) {
			try {
				return new SqlDatabaseServiceImpl().executeUpdateSQL(null, sqlContainer);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}
	}
}
