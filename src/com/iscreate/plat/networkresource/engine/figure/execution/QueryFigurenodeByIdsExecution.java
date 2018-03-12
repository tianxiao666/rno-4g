package com.iscreate.plat.networkresource.engine.figure.execution;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;
import com.iscreate.plat.networkresource.engine.figure.Figurenode;

public class QueryFigurenodeByIdsExecution implements
		Execution<Map<String, Figurenode>> {

	private Collection<String> nids;

	public QueryFigurenodeByIdsExecution(Collection<String> nids) {
		this.nids = nids;
	}
	/**
	 * 通过图节点id获取图（Figurenode）记录
	 * (mysql to oracle 迁移:dateService封装查询记录方法context.queryList(q)会发生SqlException，sql语句不标准，用context.executeSelectSQL方法替代）
	 */
	public Map<String, Figurenode> doExecution(ContextFactory contextFactory) {
		Map<String, Figurenode> fns = new HashMap<String, Figurenode>();
		if (nids == null) {
			return fns;
		}
		String[] figurenodeIds = new String[nids.size()];
		nids.toArray(figurenodeIds);
		Context context = contextFactory.CreateContext();
		
		//Query q = context.createQueryBuilder(Figurenode.MY_TYPE);
		//q.add(Restrictions.in(Figurenode.ID_KEY, figurenodeIds));
		//List<BasicEntity> bes = context.queryList(q);
		String idStr = "";
		String conStr = "";
		int i =0;
		for(int index=0;index<figurenodeIds.length;index++){//oracle in 字符数不能大于1000个
			idStr += ","+figurenodeIds[index];
			if(i>=50||index==figurenodeIds.length-1){
				idStr="("+idStr.substring(1, idStr.length())+")";
				if(!"".equals(conStr)){
					conStr += " or "+Figurenode.ID_KEY+" in "+idStr;
				}else{
					conStr += Figurenode.ID_KEY+" in "+idStr;
				}
				idStr = "";
				i=0;
			}
			i++;
			//sf.append(","+id);
		}
		String sql = "select "+ResourceCommon.getSelectSqlAttributsString(Figurenode.MY_TYPE)+" from "+Figurenode.MY_TYPE+" where "+conStr;
		//System.out.println("sqlString:"+sql);
		SqlContainer sqlContainer = context.createSqlContainer(sql);
		List<BasicEntity> bes = context.executeSelectSQL(sqlContainer,
				Figurenode.MY_TYPE);
		if (bes == null) {
			return fns;
		}
		for (BasicEntity be : bes) {
			Figurenode fn = Figurenode.changeFromEntity(be);
			fns.put(Long.toString(fn.getId()), fn);
		}
		return fns;
	}

}
