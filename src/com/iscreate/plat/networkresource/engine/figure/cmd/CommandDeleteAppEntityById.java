package com.iscreate.plat.networkresource.engine.figure.cmd;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;

public class CommandDeleteAppEntityById implements Command {
	private String type;
	private long id;

	public CommandDeleteAppEntityById(String type, long id) {
		this.type = type;
		this.id = id;
	}
	/**
	 * 根据id，type删除一个实体 entity记录
	 *(mysql to oracle 迁移:dateService封装查询记录方法context.queryList(q)会发生SqlException，sql语句不标准，用context.executeSelectSQL方法替代）
	 *
	 */
	public Integer doExecution(ContextFactory contextFactory) {
		Log log = LogFactory.getLog(getClass());
		log.debug("准备删除对象：'" + type + "'-'" + id + "'。");
		Context context = contextFactory.CreateContext();
		//Query q = context.createQueryBuilder(type);
		//q.add(Restrictions.eq(DefaultParam.idKey, id));
		String sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(type)+" from "+type+" where ENTITY_ID="+id;
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = context.createSqlContainer(sqlString);
		List<BasicEntity> beList =context.executeSelectSQL(sc, type);//查询记录
		//List<BasicEntity> beList =context.queryList(q);
		int result=-1;
		if(beList !=null && beList.size()>0){
			sqlString ="delete from "+type+" where ENTITY_ID="+id;//删除记录
			//System.out.println("sqlString:"+sqlString);
			sc = context.createSqlContainer(sqlString);
			result = context.executeDeleteSQL(sc, type);
		}else{
			result=1;
		}
		log.debug("对象：'" + type + "'-'" + id + "'删除的结果：" + result);
		if (result == 0) {
			result = -1;
		}
		return result;
	}
}
