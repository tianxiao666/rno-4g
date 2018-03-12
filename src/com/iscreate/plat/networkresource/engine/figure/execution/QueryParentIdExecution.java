package com.iscreate.plat.networkresource.engine.figure.execution;

import java.util.ArrayList;
import java.util.List;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;
import com.iscreate.plat.networkresource.engine.figure.Figureline;
import com.iscreate.plat.networkresource.engine.figure.FigurelineType;

public class QueryParentIdExecution implements Execution<List<String>> {

	private long figurenodeId;
	
	public QueryParentIdExecution(long figurenodeId) {
		this.figurenodeId = figurenodeId;
	}

	/**
	 * 通过图节点ID获取其父节点ID列表
	 *  (mysql to oracle 迁移:dateService封装查询记录方法context.queryList(q)或context.queryEntity(q)会发生SqlException，sql语句不标准，用context.executeSelectSQL方法替代）
	 */
	public List<String> doExecution(ContextFactory contextFactory) {
		List<String> nids = new ArrayList<String>();
		Context context = contextFactory.CreateContext();
		//Query query = context.createQueryBuilder(Figureline.MY_TYPE);
		//query.add(Restrictions.eq(Figureline.RIGHTID_KEY, figurenodeId));
		//query.add(Restrictions.eq(Figureline.TYPE_KEY, FigurelineType.CLAN));
		StringBuffer conStr = new StringBuffer();
		conStr.append(Figureline.RIGHTID_KEY+"='"+figurenodeId+"'");
		conStr.append(" and "+Figureline.TYPE_KEY+"='"+ FigurelineType.CLAN+"'");

		String sql = "select "+ResourceCommon.getSelectSqlAttributsString(Figureline.MY_TYPE)+" from "+Figureline.MY_TYPE+" where "+conStr;
		//System.out.println("sqlString:"+sql);
		SqlContainer sqlContainer = context.createSqlContainer(sql);
		List<BasicEntity> bes = context.executeSelectSQL(sqlContainer,
				Figureline.MY_TYPE);
		//List<BasicEntity> bes = context.queryList(query);
		if (bes == null) {
			return nids;
		} else {
			for (BasicEntity be : bes) {
				String nid = be.getValue(Figureline.LEFTID_KEY);
				nids.add(nid);
			}
		}
		return nids;
	}
}
