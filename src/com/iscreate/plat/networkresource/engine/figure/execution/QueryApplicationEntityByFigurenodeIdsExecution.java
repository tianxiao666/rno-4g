package com.iscreate.plat.networkresource.engine.figure.execution;

import java.util.ArrayList;
import java.util.List;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;

public class QueryApplicationEntityByFigurenodeIdsExecution implements
		Execution<List<ApplicationEntity>> {

	private List<String> ids;
	private String  aetName;

	public QueryApplicationEntityByFigurenodeIdsExecution(String aetName, List<String> ids) {
		this.aetName = aetName;
		this.ids = ids;
	}
	/**
	 * 通过图ID List<String>，实体类型aetName，获取关联的数据对象（实体）
	 * (mysql to oracle 迁移:dateService封装查询记录方法context.queryList(q)会发生SqlException，sql语句不标准，用context.executeSelectSQL方法替代）
	 */
	public List<ApplicationEntity> doExecution(ContextFactory contextFactory) {
		Context context = contextFactory.CreateContext();
		String idStr = "";
		String conditionStr = "";
	//	StringBuffer sf = new StringBuffer();
		int i =0;
		for(int index=0;index<ids.size();index++){//oracle in 字符数不能大于1000个
			idStr += ","+ids.get(index);
			if(i<=99){
				idStr="("+idStr.substring(1, idStr.length())+")";
				if(!"".equals(conditionStr)){
					conditionStr += " or figurenode.id in "+idStr;
				}else{
					conditionStr += "figurenode.id in "+idStr;
				}
				idStr = "";
				i=0;
			}else if(index==ids.size()-1){
				idStr="("+idStr.substring(1, idStr.length())+")";
				if(!"".equals(conditionStr)){
					conditionStr += " or figurenode.id in "+idStr;
				}else{
					conditionStr += "figurenode.id in "+idStr;
				}
				idStr = "";
				i=0;
			}
			i++;
			//sf.append(","+id);
		}
		//idStr = sf.toString();
		//idStr="("+idStr.substring(1, idStr.length())+")";
		
		String sql = "select "+ResourceCommon.getSelectSqlAttributsString(aetName)+" from "+aetName+",figurenode where ("+conditionStr+") and "+aetName+".ENTITY_ID=figurenode.entityId";
		
		
		//System.out.println("sqlString:"+sql);
		SqlContainer sqlContainer = context.createSqlContainer(sql);
		List<BasicEntity> bes = context.executeSelectSQL(sqlContainer,
				aetName);
		List<ApplicationEntity> aes = new ArrayList<ApplicationEntity>();
		if (bes != null) {
			for (BasicEntity be : bes) {
				aes.add(ApplicationEntity.changeFromEntity(be));
			}
		}

		return aes;
	}

}
