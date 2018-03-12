package com.iscreate.plat.networkresource.engine.figure.execution;

import java.util.ArrayList;
import java.util.List;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;
import com.iscreate.plat.networkresource.engine.figure.Figurenode;

public abstract class ListFigurenodeExecution implements
		Execution<List<Figurenode>> {

	private long figureId;
	private List<Object> params = new ArrayList<Object>();

	public void setFigureId(long figureId) {
		this.figureId = figureId;
	}

	public void set(Object obj) {
		params.add(obj);
	}
	/**
	 * 根据 figureId 及条件值 获取figurenode 记录集
	 * 
	 * (mysql to oracle 迁移:dateService封装查询记录方法context.queryList(q)会发生SqlException，sql语句不标准，用context.executeSelectSQL方法替代）
	 */
	public List<Figurenode> doExecution(ContextFactory contextFactory) {
		Context context = contextFactory.CreateContext();
		///Query query = context.createQueryBuilder(Figurenode.MY_TYPE);
		List<Figurenode> nodes = new ArrayList<Figurenode>();
		if (figureId == 0 || figureId < 0) {
			return nodes;
		}
		//query.add(Restrictions.eq(Figurenode.FIGUREID_KEY, figureId));
		String queryString = Figurenode.FIGUREID_KEY+"="+figureId;
		QueryEnvironment env = new QueryEnvironment(contextFactory);
		for (Object o : params) {
			env.set(o);
		}
		queryString=this.buildFigurenodeQuery(env, queryString);
		if (queryString != null&&!"".equals(queryString)) {
			String conStr = queryString;
			//conStr = conStr.replace("\"", "'");
			//conStr = conStr.replace("_entityId", "\"_ENTITYID\"");
			//conStr = conStr.replace("_entityType", "\"_ENTITYTYPE\"");
			String sqlString ="select "+ResourceCommon.getSelectSqlAttributsString(Figurenode.MY_TYPE)+" from " + Figurenode.MY_TYPE+ " where "+conStr;
			//System.out.println("sqlString:"+sqlString);
			SqlContainer sc = context.createSqlContainer(sqlString);
			List<BasicEntity> bes = context.executeSelectSQL(sc,Figurenode.MY_TYPE);
			//List<BasicEntity> bes = context.queryList(query);
			if (bes != null) {
				for (BasicEntity be : bes) {
					nodes.add(Figurenode.changeFromEntity(be));
				}
			}
		}
		return nodes;
	}

	public abstract String buildFigurenodeQuery(QueryEnvironment env,
			String figurenodeQuery);
}
