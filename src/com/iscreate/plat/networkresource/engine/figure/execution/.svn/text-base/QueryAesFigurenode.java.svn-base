package com.iscreate.plat.networkresource.engine.figure.execution;

import java.util.List;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;
import com.iscreate.plat.networkresource.engine.figure.Figurenode;

public class QueryAesFigurenode implements Execution<Figurenode> {

	private long figureId;
	private ApplicationEntity entity;

	public QueryAesFigurenode(long figureId, ApplicationEntity entity) {
		this.figureId = figureId;
		this.entity = entity;
	}

	/**
	 * 通过应用数据对象，获取相应的图节点（figurenode）
	 * (mysql to oracle 迁移:dateService封装查询记录方法context.queryList(q)会发生SqlException，sql语句不标准，用context.executeSelectSQL方法替代）
	 */
	public Figurenode doExecution(ContextFactory contextFactory) {
		if (entity == null || figureId == 0 || figureId < 0) {
			return null;
		}
		Context context = contextFactory.CreateContext();
		String type = entity.getType();
		long id = entity.getId();
		//Query q = context.createQueryBuilder(Figurenode.MY_TYPE);
		//q.add(Restrictions.eq(Figurenode.ENTYPE_KEY, type));
		//q.add(Restrictions.eq(Figurenode.ENID_KEY, id));
		//q.add(Restrictions.eq(Figurenode.FIGUREID_KEY, figureId));
		StringBuffer conStr = new StringBuffer();
		conStr.append(Figurenode.ENTYPE_KEY+"='"+type+"'");
		conStr.append(" and "+Figurenode.ENID_KEY+"="+id);
		conStr.append(" and "+Figurenode.FIGUREID_KEY+"="+figureId);
		String sqlString ="select "+ResourceCommon.getSelectSqlAttributsString(Figurenode.MY_TYPE)+" from "+ Figurenode.MY_TYPE + " where "+conStr;
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = context.createSqlContainer(sqlString);
		List<BasicEntity> beList = context.executeSelectSQL(sc, Figurenode.MY_TYPE);
		//BasicEntity be = context.queryEntity(q);
		if (beList == null) {
			return null;
		}
		BasicEntity be = beList.get(0);
		Figurenode node = Figurenode.changeFromEntity(be);
		return node;
	}

}
