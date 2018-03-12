package com.iscreate.plat.networkresource.engine.figure.cmd;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.DefaultParam;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;

public class CommandUpdateAppEntity implements Command {

	private ApplicationEntity entity;

	public CommandUpdateAppEntity(ApplicationEntity entity) {
		this.entity = entity;
	}
	/**
	 * 插入或更新一个实体记录   （dataService 源码因为没有 采用下面方法整改）
	 *(mysql to oracle 迁移:dateService封装插入记录方法context.update(entity, q)会发生SqlException，sql语句不标准，用context.executeUpdateSQL方法替代，拼装sql）
	 *
	 */
	public Integer doExecution(ContextFactory contextFactory) {
		Log log = LogFactory.getLog(getClass());
		Context context = contextFactory.CreateContext();
		//Query q = context.createQueryBuilder(entity);
		HashMap<String, Object> condition = new HashMap<String, Object>();
		boolean hasCondition = false;
		String conStr="";
		for (String key : entity.primaryKeys()) {
			hasCondition = true;
			//q.add(Restrictions.eq(key, entity.getValue(key)));
			if("_entityType".equals(key)){
				if("".equals(conStr+"")){
					conStr = "ENTITY_TYPE='"+entity.getValue(key)+"'";
				}else{
					conStr = "and ENTITY_TYPE=='"+entity.getValue(key)+"'";
				}
			}else if("_entityId".equals(key)){
				if("".equals(conStr+"")){
					conStr = "ENTITY_ID='"+entity.getValue(key)+"'";
				}else{
					conStr = "and ENTITY_ID='"+entity.getValue(key)+"'";
				}
			}else{
				if("".equals(conStr+"")){
					conStr = key+"='"+entity.getValue(key)+"'";
				}else{
					conStr = "and "+key+"='"+entity.getValue(key)+"'";
				}
			}
			condition.put(key, entity.getValue(key));
		}
		if (!hasCondition) {
			//q.add(Restrictions.eq(DefaultParam.idKey, entity.getId()));
			conStr = " ENTITY_ID='"+entity.getId()+"'";
			condition.put(DefaultParam.idKey, entity.getId());
		}
		log.debug("使用条件'" + condition + "'查询应用数据对象。");
		String sqlString ="select "+ResourceCommon.getSelectSqlAttributsString(entity.getValue("_entityType")+"")+" from "+entity.getValue("_entityType")+" where "+conStr;
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = context.createSqlContainer(sqlString);
		List<BasicEntity> beList=context.executeSelectSQL(sc, entity.getValue("_entityType")+"");
		//BasicEntity be = context.queryEntity(q);
		int result;
		if (beList == null) {
			result = 0;
			log.debug("库中不存在该数据对象，操作结果：" + result);
		} else {
			//entity.setId(Long.parseLong(be.getValue(DefaultParam.idKey) + ""));
			//result = context.update(entity, q);
			BasicEntity be = beList.get(0);
			sqlString = "update "+entity.getValue("_entityType")+" set "+ResourceCommon.getUpdateAttributesSqlString(entity)+" where ENTITY_ID="+be.getValue(DefaultParam.idKey);
			//System.out.println("sqlString:"+sqlString);
			sc = context.createSqlContainer(sqlString);
			result = context.executeUpdateSQL(sc, entity.getValue("_entityType")+"");
			log.debug("库中已经存在该数据对象。");
		}
		return result;
	}

}
