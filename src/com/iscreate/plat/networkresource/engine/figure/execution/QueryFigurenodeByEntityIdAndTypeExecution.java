package com.iscreate.plat.networkresource.engine.figure.execution;


import java.util.List;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;
import com.iscreate.plat.networkresource.engine.figure.Figurenode;

public class QueryFigurenodeByEntityIdAndTypeExecution implements
		Execution<BasicEntity> {

	private long entityId;
	private String entityType;

	public QueryFigurenodeByEntityIdAndTypeExecution(long entityId,String entityType) {
		this.entityId = entityId;
		this.entityType=entityType;
	}
	/**
	 * 
	 * @description: 通过entityId和entityType查询figurenode记录
	 * @author：yuan.yw
	 * @param contextFactory
	 * @return     
	 *      
	 * @date：Jun 28, 2013 2:29:11 PM
	 */
	public BasicEntity doExecution(ContextFactory contextFactory) {
		BasicEntity resultBe = null;
		if (this.entityId == 0L || this.entityType== null) {
			return resultBe;
		}
		Context context = contextFactory.CreateContext();
		String sql = "select "+ResourceCommon.getSelectSqlAttributsString(Figurenode.MY_TYPE)+" from "+Figurenode.MY_TYPE+" where entityid="+this.entityId+" and entitytype='"+this.entityType+"'";
		SqlContainer sqlContainer = context.createSqlContainer(sql);
		List<BasicEntity> bes = context.executeSelectSQL(sqlContainer,
				Figurenode.MY_TYPE);
		if (bes == null) {
			return resultBe;
		}
		
		return bes.get(0);
	}

}
