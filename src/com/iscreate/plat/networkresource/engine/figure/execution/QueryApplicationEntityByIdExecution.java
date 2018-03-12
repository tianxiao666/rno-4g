package com.iscreate.plat.networkresource.engine.figure.execution;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;

public class QueryApplicationEntityByIdExecution implements
		Execution<ApplicationEntity> {
	private String type;
	private long id;

	public QueryApplicationEntityByIdExecution(String type, long id) {
		this.type = type;
		this.id = id;
	}
	/**
	 * 通过实体id ，实体类型type，获取数据对象（实体）
	 * (mysql to oracle 迁移:dateService封装查询记录方法context.queryEntity(q)会发生SqlException，sql语句不标准，用context.executeSelectSQL方法替代）
	 */
	public ApplicationEntity doExecution(ContextFactory contextFactory) {
		Log log = LogFactory.getLog(getClass());
		log.debug("准备查询数据对象，查询条件:'" + type + "-" + id + "'");
		Context context = contextFactory.CreateContext();
		//Query q = context.createQueryBuilder(type);
		//q.add(Restrictions.eq(DefaultParam.idKey, id));
		String sql = "select "+ResourceCommon.getSelectSqlAttributsString(type)+" from "+type+" where ENTITY_ID="+id;
		//System.out.println("sqlString:"+sql);
		SqlContainer sqlContainer = context.createSqlContainer(sql);
		List<BasicEntity> bes = context.executeSelectSQL(sqlContainer,
				type);
		//BasicEntity bs = context.queryEntity(q);
		if (bes == null) {
			return null;
		} else {
			BasicEntity bs = bes.get(0);
			ApplicationEntity ae = ApplicationEntity.changeFromEntity(bs);
			return ae;
		}
	}

}
