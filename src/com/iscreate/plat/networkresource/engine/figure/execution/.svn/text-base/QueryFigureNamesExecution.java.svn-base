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

public class QueryFigureNamesExecution implements Execution<String[]> {

	private ApplicationEntity entity;

	public QueryFigureNamesExecution(ApplicationEntity entity) {
		this.entity = entity;
	}
	/**
	 * 通过实体，获取Figure表记录中的 figureName
	 * (mysql to oracle 迁移:dateService封装查询记录方法context.queryList(q)会发生SqlException，sql语句不标准，用context.executeSelectSQL方法替代）
	 */
	public String[] doExecution(ContextFactory contextFactory) {
		if (entity == null) {
			return null;
		}
		String type = entity.getType();
		long id = entity.getId();
		Context context = contextFactory.CreateContext();
		//Query q = context.createQueryBuilder(Figurenode.MY_TYPE);
		//q.add(Restrictions.eq(Figurenode.ENTYPE_KEY, type));
		//q.add(Restrictions.eq(Figurenode.ENID_KEY, id));
		StringBuffer conStr = new StringBuffer();
		conStr.append(Figurenode.ENTYPE_KEY+"='"+type+"'");
		conStr.append(" and "+Figurenode.ENID_KEY+"='"+id+"'");
		String sql = "select "+ResourceCommon.getSelectSqlAttributsString(Figurenode.MY_TYPE)+" from "+Figurenode.MY_TYPE+" where "+conStr;
		//System.out.println("sqlString:"+sql);
		SqlContainer sqlContainer = context.createSqlContainer(sql);
		List<BasicEntity> be = context.executeSelectSQL(sqlContainer,
				Figurenode.MY_TYPE);
		//List<BasicEntity> be = context.queryList(q);
		if (be == null) {
			return null;
		}
		String[] figureIds = new String[be.size()];
		
		conStr = new StringBuffer();
		for (int i = 0; i < be.size(); i++) {
			figureIds[i] = be.get(i).getValue(Figurenode.FIGUREID_KEY);
			if(i==0){
				conStr.append(figureIds[i]);
			}else{
				conStr.append(","+figureIds[i]);
			}
		}
		String cStr = Figure.FIGUREID_KEY+" in("+conStr+")";
		//q = context.createQueryBuilder(Figure.MY_TYPE);
		//q.add(Restrictions.in(Figure.FIGUREID_KEY, figureIds));
		sql = "select "+ResourceCommon.getSelectSqlAttributsString(Figure.MY_TYPE)+" from "+Figure.MY_TYPE+" where "+conStr;
		//System.out.println("sqlString:"+sql);
		sqlContainer = context.createSqlContainer(sql);
		 be = context.executeSelectSQL(sqlContainer,
				 Figure.MY_TYPE);
		//be = context.queryList(q);
		if (be == null) {
			return null;
		}
		String[] figureNames = new String[be.size()];
		for (int i = 0; i < be.size(); i++) {
			figureNames[i] = be.get(i).getValue(Figure.FIGURENAME_KEY);
		}
		return figureNames;
	}

}
