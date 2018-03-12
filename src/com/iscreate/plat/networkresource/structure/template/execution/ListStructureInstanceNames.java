package com.iscreate.plat.networkresource.structure.template.execution;



import java.util.ArrayList;
import java.util.List;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;
import com.iscreate.plat.networkresource.engine.figure.Figure;
import com.iscreate.plat.networkresource.engine.figure.execution.Execution;

public class ListStructureInstanceNames implements Execution<List<String>> {

	private String structureModuleName;

	public ListStructureInstanceNames(String structureModuleName) {
		this.structureModuleName = structureModuleName;
	}

	/**
	 * 查询模板的名称
	 */
	public List<String> doExecution(ContextFactory contextFactory) {
		Context context = contextFactory.CreateContext();
		//Query query = context.createQueryBuilder(Figure.MY_TYPE);
		//query.add(Restrictions.like(Figure.FIGURENAME_KEY, "%"+structureModuleName+"%"));
		String sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(Figure.MY_TYPE)+" from "+Figure.MY_TYPE+" where "+Figure.FIGURENAME_KEY+" like '%"+structureModuleName+"%'";
		SqlContainer sc = context.createSqlContainer(sqlString);
		List<BasicEntity> bes = context.executeSelectSQL(sc, Figure.MY_TYPE);
		//List<BasicEntity> bes = context.queryList(query);
		List<String> names = new ArrayList<String>(); 
		if(bes != null){
			for(BasicEntity be : bes){
				String name = be.getValue(Figure.FIGURENAME_KEY);
				if(name!= null){
					if(name.indexOf("-")>0){
						names.add(name.substring(name.indexOf("-")+1));
					}else{
						names.add(name);
					}
				}
			}
		}		
		return names;
	}

}
