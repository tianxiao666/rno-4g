package com.iscreate.plat.networkresource.engine.figure.execution;

import java.util.ArrayList;
import java.util.List;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;
import com.iscreate.plat.networkresource.engine.figure.Figureline;
import com.iscreate.plat.networkresource.engine.figure.Figurenode;

public class QueryAllFigurelineExecution implements Execution<List<Figureline>> {

	private long figureId;

	public QueryAllFigurelineExecution(long figureId ) {
		this.figureId = figureId;
	}

	/**
	 * 通过图ID，获取该图中的所有边对象（figureline）
	 * (mysql to oracle 迁移:dateService封装查询记录方法context.queryList(q)会发生SqlException，sql语句不标准，用context.executeSelectSQL方法替代）
	 */
	public List<Figureline> doExecution(ContextFactory contextFactory) {
		List<Figureline> fls = new ArrayList<Figureline>();
		Context context = contextFactory.CreateContext();
		//Query q = context.createQueryBuilder(Figureline.MY_TYPE);
		//q.add(Restrictions.eq(Figureline.FIGUREID_KEY, figureId));
		String sqlString ="select "+ResourceCommon.getSelectSqlAttributsString(Figureline.MY_TYPE)+" from "+ Figurenode.MY_TYPE + " where "+Figureline.FIGUREID_KEY+"="+figureId;
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = context.createSqlContainer(sqlString);
		List<BasicEntity> bes = context.executeSelectSQL(sc, Figureline.MY_TYPE);
		//List<BasicEntity> bes = context.queryList(q);
		if (bes != null) {
			for (BasicEntity be : bes) {
				fls.add(Figureline.changeFromEntity(be));
			}
		}
		return fls;
	}

}
