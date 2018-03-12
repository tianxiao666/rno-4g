package com.iscreate.plat.networkresource.engine.figure.cmd;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;

public class CommandDeleteAppEntity implements Command {
	private ApplicationEntity entity;

	public CommandDeleteAppEntity(ApplicationEntity entity) {
		this.entity = entity;
	}
	/**
	 * 传入参数entity ，删除一个实体 entity记录
	 *(mysql to oracle 迁移:dateService封装删除记录方法context.delete(q)会发生SqlException，sql语句不标准，用context.executeDeleteSQL方法替代）
	 *
	 */
	public Integer doExecution(ContextFactory contextFactory) {
		Log log = LogFactory.getLog(getClass());
		String type = entity.getType();
		long id = entity.getId();
		log.debug("准备删除对象：'" + type + "'-'" + id + "'。");
		Context context = contextFactory.CreateContext();
		//Query q = context.createQueryBuilder(type);
		//q.add(Restrictions.eq(DefaultParam.idKey, id));
		String sqlString = "delete from "+type+" where ENTITY_ID="+id;
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = context.createSqlContainer(sqlString);
		int result = context.executeDeleteSQL(sc,type);
		//int result = context.delete(q);
		log.debug("对象：'" + type + "'-'" + id + "'删除的结果：" + result);
		if (result == 0) {
			result = -1;
		}
		return result;
	}
}
