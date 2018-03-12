package com.iscreate.plat.networkresource.engine.figure.cmd;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;
import com.iscreate.plat.networkresource.engine.figure.Figurenode;

public class CommandDeleteFigurenode implements Command {
	private Figurenode node;

	public CommandDeleteFigurenode(Figurenode node) {
		this.node = node;
	}
	public CommandDeleteFigurenode(ApplicationEntity ae) {
		this.node = new Figurenode();
		this.node.setId(Long.valueOf(ae.getValue("id")+""));
	}
	/**
	 * 传入Figurnode实例 删除figurenode表里的记录
	 *
	 *(mysql to oracle 迁移:dateService封装删除记录方法context.delete(q)会发生SqlException，sql语句不标准，用context.executeDeleteSQL方法替代）
	 */
	public Integer doExecution(ContextFactory contextFactory) {
		Log log = LogFactory.getLog(getClass());
		log.debug("准备删除节点对象，删除的节点ID:'" + node.getId() + "'");
		Context context = contextFactory.CreateContext();
	//	Query q = context.createQueryBuilder(Figurenode.MY_TYPE);
	//	q.add(Restrictions.eq(Figurenode.ID_KEY, node.getId()));
		String sqlString =" select id \"id\",figureId \"figureId\",entityType \"entityType\",entityId \"entityId\",birthdate \"birthdate\",ENTITY_TYPE \"_entityType\"  from "+Figurenode.MY_TYPE+" where "+Figurenode.ID_KEY+"="+node.getId();
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = context.createSqlContainer(sqlString);
		List<BasicEntity> beList =context.executeSelectSQL(sc,Figurenode.MY_TYPE);
		//List<BasicEntity> beList =context.queryList(q);
		int result=-1;
		if(beList !=null && beList.size()>0){
			sqlString =" delete  from "+Figurenode.MY_TYPE+" where "+Figurenode.ID_KEY+"="+node.getId();
			//System.out.println("sqlString:"+sqlString);
			sc = context.createSqlContainer(sqlString);
			result = context.executeDeleteSQL(sc, Figurenode.MY_TYPE);
		}else{
			result=1;
		}
		log.debug("'删除节点对象的结果：" + result);
		if (result == 0) {
			result = -1;
		}
		return result;
	}
}
