package com.iscreate.plat.networkresource.engine.figure.execution;

import java.util.List;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;
import com.iscreate.plat.networkresource.engine.figure.Figure;

public class QueryFigureIdByNameExecution implements Execution<Long> {

	private String figureName;

	public QueryFigureIdByNameExecution(String figureName) {
		this.figureName = figureName;
	}
	/**
	 * 通过figureName，获取Figure表记录中figureId
	 * (mysql to oracle 迁移:dateService封装查询记录方法context.queryEntity(q)会发生SqlException，sql语句不标准，用context.executeSelectSQL方法替代）
	 */
	public Long doExecution(ContextFactory contextFactory) {
		Context context = contextFactory.CreateContext();
		//Query q = context.createQueryBuilder(Figure.MY_TYPE);
		//q.add(Restrictions.eq(Figure.FIGURENAME_KEY, figureName));
		String sql = "select "+ResourceCommon.getSelectSqlAttributsString(Figure.MY_TYPE)+" from "+Figure.MY_TYPE+" where "+Figure.FIGURENAME_KEY+"='"+figureName+"'";
		//System.out.println("sqlString:"+sql);
		SqlContainer sqlContainer = context.createSqlContainer(sql);
		List<BasicEntity> bes = context.executeSelectSQL(sqlContainer,
				Figure.MY_TYPE);
		//BasicEntity be = context.queryEntity(q);
		if (bes == null) {
			return new Long(0);
		}
		BasicEntity be = bes.get(0);
		long figureId = Long.parseLong(be.getValue(Figure.FIGUREID_KEY)+"");
		return figureId;
	}

}
