package com.iscreate.plat.networkresource.engine.figure.execution;


import java.util.List;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;
import com.iscreate.plat.networkresource.engine.figure.Figurenode;

public class QueryFigurenodeByPathExecution implements
		Execution<List<BasicEntity>> {

	private String path;


	public QueryFigurenodeByPathExecution(String path) {
		this.path = path;

	}
	/**
	 * 
	 * @description: 通过Path ( like )查询figurenode或资源记录
	 * @author：yuan.yw
	 * @param contextFactory
	 * @return     
	 *      
	 * @date：Jun 28, 2013 2:29:11 PM
	 */
	public List<BasicEntity> doExecution(ContextFactory contextFactory) {
		List<BasicEntity> resultList = null;
		if ("".equals(path) || this.path== null ) {
			return resultList;
		}
		Context context = contextFactory.CreateContext();
		String sql = "select "+ResourceCommon.getSelectSqlAttributsString(Figurenode.MY_TYPE)+" from "+Figurenode.MY_TYPE+"  where  path like '%"+this.path+"%' order by path";
		SqlContainer sqlContainer = context.createSqlContainer(sql);
		resultList = context.executeSelectSQL(sqlContainer,
				Figurenode.MY_TYPE);
		
		return resultList;
	}

}
