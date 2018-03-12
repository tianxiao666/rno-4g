package com.iscreate.plat.networkresource.engine.figure.execution;

import java.util.List;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;
import com.iscreate.plat.networkresource.engine.figure.Figure;
import com.iscreate.plat.networkresource.engine.figure.Figurenode;

public class QueryOneFigureNameExecution implements Execution<String> {

	private ApplicationEntity entity;

	public QueryOneFigureNameExecution(ApplicationEntity entity) {
		this.entity = entity;
	}
	/**
	 * 通过entity（实体）获取表Figure中的FigureName
	 * (mysql to oracle 迁移:dateService封装查询记录方法context.queryList(q)或context.queryEntity(q)会发生SqlException，sql语句不标准，用context.executeSelectSQL方法替代）
	 */
	public String doExecution(ContextFactory contextFactory) {
		if (entity == null) {
			return null;
		}
		Context context = contextFactory.CreateContext();
		String type = entity.getType();
		long id = entity.getId();
		//Query q = context.createQueryBuilder(Figurenode.MY_TYPE);
		//q.add(Restrictions.eq(Figurenode.ENTYPE_KEY, type));
		//q.add(Restrictions.eq(Figurenode.ENID_KEY, id));
		StringBuffer conStr = new StringBuffer();
		conStr.append(Figurenode.ENTYPE_KEY+"='"+type+"'");
		conStr.append(" and "+Figurenode.ENID_KEY+"='"+id+"'");

		String sql = "select "+ResourceCommon.getSelectSqlAttributsString(Figurenode.MY_TYPE)+" from "+Figurenode.MY_TYPE+" where "+conStr;
		//System.out.println("sqlString:"+sql);
		SqlContainer sqlContainer = context.createSqlContainer(sql);
		List<BasicEntity> bes = context.executeSelectSQL(sqlContainer,
				Figurenode.MY_TYPE);
		//BasicEntity be = context.queryEntity(q);
		if (bes== null) {
			return null;
		}
		BasicEntity be = bes.get(0);
		Figurenode node = Figurenode.changeFromEntity(be);
		long figureId = node.getFigureId();
		//q = context.createQueryBuilder(Figure.MY_TYPE);
		//q.add(Restrictions.eq(Figure.FIGUREID_KEY, figureId));
		conStr = new StringBuffer();
		conStr.append(Figure.FIGUREID_KEY+"='"+figureId+"'");
		sql = "select "+ResourceCommon.getSelectSqlAttributsString(Figure.MY_TYPE)+" from "+Figure.MY_TYPE+" where "+conStr;
		sqlContainer = context.createSqlContainer(sql);
		bes = context.executeSelectSQL(sqlContainer,
				Figure.MY_TYPE);
		//be = context.queryEntity(q);
		if (bes == null) {
			return null;
		}
		 be = bes.get(0);
		String figureName = be.getValue(Figure.FIGURENAME_KEY);
		return figureName;
	}

}
