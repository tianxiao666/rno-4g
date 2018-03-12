package com.iscreate.plat.networkresource.engine.figure.cmd;




import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;


public class CommandSaveAppEntity implements Command {

	private ApplicationEntity entity;

	public CommandSaveAppEntity(ApplicationEntity entity) {
		this.entity = entity;
	}
	/**
	 * 插入一个实体记录   （dataService 源码因为没有 采用下面方法整改）
	 *(mysql to oracle 迁移:dateService封装插入记录方法context.insert(e)会发生SqlException，sql语句不标准，用context.executeInsertSQL方法替代，拼装sql）
	 *
	 */
	public Integer doExecution(ContextFactory contextFactory) {
		Log log = LogFactory.getLog(getClass());
		Context context = contextFactory.CreateContext();
		int result;
		Map<String,String> mp= ResourceCommon.getInsertAttributesAndValuesStringMap(entity);
		if(mp!=null &&!mp.isEmpty()){
			String attrs = mp.get("attrStr");
			String values = mp.get("valueStr");
			String type = entity.getValue("_entityType");
			String sqlString = " insert into "+type+"("+attrs+") values("+values+")";
			//System.out.println("sqlString:"+sqlString);
			SqlContainer sc = context.createSqlContainer(sqlString);
			result = context.executeInsertSQL(sc,type);
		}else{
			result=0;
		}
		//result = context.insert(entity);
		log.debug("插入该数据对象。操作结果：" + result);
		return result;
	}

}
