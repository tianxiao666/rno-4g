package com.iscreate.plat.networkresource.engine.figure.execution;

import java.util.ArrayList;
import java.util.List;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;

public class QueryApplicationEntityByIdsExecution implements
		Execution<List<ApplicationEntity>> {

	private String[] ids;
	//private Query query;
	private String query;
	private String aetName;
	public QueryApplicationEntityByIdsExecution(String query, String[] ids,String aetName) {
		this.query = query;
		this.ids = ids;
		this.aetName = aetName;
	}
	/**
	 * 通过图_entityId String[]及条件query，获取关联的数据对象（实体）
	 * (mysql to oracle 迁移:dateService封装查询记录方法context.queryList(q)会发生SqlException，sql语句不标准，用context.executeSelectSQL方法替代）
	 */
	public List<ApplicationEntity> doExecution(ContextFactory contextFactory) {
		Context context = contextFactory.CreateContext();
		String qStr = query;//query.toSqlString();
		String conStr = "";
		if(qStr!=null&&!"".equals(qStr)){
			qStr = qStr.replace("\"", "'");
			qStr = qStr.replace("_entityId", "ENTITY_ID");
			qStr = qStr.replace("_entityType", "ENTITY_TYPE");
			conStr = qStr;
		}
		//query.add(Restrictions.in(DefaultParam.idKey, ids));
		
		if(ids!=null&&ids.length>0){
			String cdStr ="";
			StringBuffer idStrs =new StringBuffer();
			int i=0;
			for(int index=0;index<ids.length;index++){//oracle in 表达式字符长度不超过1000字符处理
				idStrs.append(","+ids[index]);
				if(i>=50||index==ids.length-1){
					if(!"".equals(cdStr+"")){
						cdStr = " or ENTITY_ID in("+idStrs.substring(1)+")";
					}else{
						cdStr = "( ENTITY_ID in("+idStrs.substring(1)+")";
					}
					idStrs=new StringBuffer();
					i=0;
				}
				i++;
			}
			if(!"".equals(conStr+"")){
				conStr +=" and "+cdStr +")";
			}else{
				conStr +=cdStr +")";
			}
			
			
		}
		
		String sql = "select "+ResourceCommon.getSelectSqlAttributsString(this.aetName)+" from "+this.aetName+" where "+conStr;
		//System.out.println("sqlString:"+sql);
		SqlContainer sqlContainer = context.createSqlContainer(sql);
		List<BasicEntity> bes = context.executeSelectSQL(sqlContainer,
				this.aetName);
		//List<BasicEntity> bes = context.queryList(query);
		List<ApplicationEntity> aes = new ArrayList<ApplicationEntity>();
		if (bes != null) {
			for (BasicEntity be : bes) {
				aes.add(ApplicationEntity.changeFromEntity(be));
			}
		}
		return aes;
	}

}
