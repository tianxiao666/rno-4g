package com.iscreate.plat.networkresource.engine.figure.cmd;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;
import com.iscreate.plat.networkresource.engine.figure.Figureline;
import com.iscreate.plat.networkresource.engine.figure.FigurelineType;

public class CommandDeleteFigurelineByNodeId implements Command {
	private long nodeId;
	private FigurelineType type;

	public CommandDeleteFigurelineByNodeId(long nodeId, FigurelineType type) {
		this.nodeId = nodeId;
		this.type = type;
	}
	/**
	 * 根据nodeId 删除figureline表里的记录
	 *
	 *(mysql to oracle 迁移:dateService封装删除记录方法context.delete(q)会发生SqlException，sql语句不标准，用context.executeDeleteSQL方法替代）
	 */
	public Integer doExecution(ContextFactory contextFactory) {
		Log log = LogFactory.getLog(getClass());
		log.debug("准备删除边对象");
		HashMap<String, Object> condition = new HashMap<String, Object>();
		Context context = contextFactory.CreateContext();
		int result = -1;
		if (type == null) {
			/*Query q = context.createQueryBuilder(Figureline.MY_TYPE);
			q.add(Restrictions.or(
					Restrictions.eq(Figureline.LEFTID_KEY, nodeId),
					Restrictions.eq(Figureline.RIGHTID_KEY, nodeId)));*/
			condition.put("条件1", Figureline.LEFTID_KEY + ":" + nodeId
					+ "  OR  " + Figureline.RIGHTID_KEY + ":" + nodeId);
			String sqlString = "select id \"id\",leftId \"leftId\",rightId \"rightId\",linkType \"linkType\",figureId \"figureId\",lefttype \"lefttype\",righttype \"righttype\",birthdate \"birthdate\",ENTITY_TYPE \"_entityType\" from "+Figureline.MY_TYPE+" where "+Figureline.LEFTID_KEY+"="+nodeId+" or "+Figureline.RIGHTID_KEY+"="+nodeId;
			//System.out.println("sqlString:"+sqlString);
			SqlContainer sc = context.createSqlContainer(sqlString);
			List<BasicEntity> beList =context.executeSelectSQL(sc,Figureline.MY_TYPE);
			//List<BasicEntity> beList =context.queryList(q);
			log.debug("准备删除边对象，删除条件:'" + condition + "'");
			if(beList !=null && beList.size()>0){
				sqlString ="delete from "+Figureline.MY_TYPE+" where "+Figureline.LEFTID_KEY+"="+nodeId+" or "+Figureline.RIGHTID_KEY+"="+nodeId;
				sc = context.createSqlContainer(sqlString);
				result = context.executeDeleteSQL(sc, Figureline.MY_TYPE);
				//result = context.delete(q);
			}else{
				result=1;
			}
			log.debug("删除边对象的结果：" + result);
		} else {
			//Query q = context.createQueryBuilder(Figureline.MY_TYPE);
			String conStr ="";
			switch (type) {
			case CLAN:
				/*q.add(Restrictions.or(
						Restrictions.eq(Figureline.LEFTID_KEY, nodeId),
						Restrictions.eq(Figureline.RIGHTID_KEY, nodeId)));
				q.add(Restrictions.eq(Figureline.TYPE_KEY, FigurelineType.CLAN));*/
				conStr = "("+Figureline.LEFTID_KEY+"="+nodeId+" or "+Figureline.RIGHTID_KEY+"="+nodeId+") and  "+Figureline.TYPE_KEY+"='"+FigurelineType.CLAN+"'";
				condition.put("条件1", Figureline.LEFTID_KEY + ":" + nodeId
						+ "  OR  " + Figureline.RIGHTID_KEY + ":" + nodeId);
				condition.put("条件2", Figureline.TYPE_KEY + ":"
						+ FigurelineType.CLAN);
				break;
			case PARENT:
			/*	q.add(Restrictions.eq(Figureline.RIGHTID_KEY, nodeId));
				q.add(Restrictions.eq(Figureline.TYPE_KEY, FigurelineType.CLAN));*/
				conStr =  Figureline.RIGHTID_KEY+"="+nodeId+" and  "+Figureline.TYPE_KEY+"='"+FigurelineType.CLAN+"'";
				condition.put("条件1", Figureline.RIGHTID_KEY + ":" + nodeId);
				condition.put("条件2", Figureline.TYPE_KEY + ":"
						+ FigurelineType.CLAN);
				break;
			case CHILD:
				//q.add(Restrictions.eq(Figureline.LEFTID_KEY, nodeId));
				//q.add(Restrictions.eq(Figureline.TYPE_KEY, FigurelineType.CLAN));
				conStr =  Figureline.LEFTID_KEY+"="+nodeId+" and  "+Figureline.TYPE_KEY+"='"+FigurelineType.CLAN+"'";
				condition.put("条件1", Figureline.LEFTID_KEY + ":" + nodeId);
				condition.put("条件2", Figureline.TYPE_KEY + ":"
						+ FigurelineType.CLAN);
				break;
			case LINK:
				/*q.add(Restrictions.or(
						Restrictions.eq(Figureline.LEFTID_KEY, nodeId),
						Restrictions.eq(Figureline.RIGHTID_KEY, nodeId)));
				q.add(Restrictions.eq(Figureline.TYPE_KEY, FigurelineType.LINK));*/
				conStr = "("+Figureline.LEFTID_KEY+"="+nodeId+" or "+Figureline.RIGHTID_KEY+"="+nodeId+") and  "+Figureline.TYPE_KEY+"='"+FigurelineType.LINK+"'";				
				condition.put("条件1", Figureline.LEFTID_KEY + ":" + nodeId
						+ "  OR  " + Figureline.RIGHTID_KEY + ":" + nodeId);
				condition.put("条件2", Figureline.TYPE_KEY + ":"
						+ FigurelineType.LINK);
				break;
			case FORWORD:
				/*q.add(Restrictions.eq(Figureline.RIGHTID_KEY, nodeId));
				q.add(Restrictions.eq(Figureline.TYPE_KEY, FigurelineType.LINK));*/
				conStr =  Figureline.RIGHTID_KEY+"="+nodeId+" and  "+Figureline.TYPE_KEY+"='"+FigurelineType.LINK+"'";
				condition.put("条件1", Figureline.RIGHTID_KEY + ":" + nodeId);
				condition.put("条件2", Figureline.TYPE_KEY + ":"
						+ FigurelineType.LINK);
				break;
			case BACKWORD:
				/*q.add(Restrictions.eq(Figureline.LEFTID_KEY, nodeId));
				q.add(Restrictions.eq(Figureline.TYPE_KEY, FigurelineType.LINK));*/
				conStr =  Figureline.LEFTID_KEY+"="+nodeId+" and  "+Figureline.TYPE_KEY+"='"+FigurelineType.LINK+"'";
				condition.put("条件1", Figureline.LEFTID_KEY + ":" + nodeId);
				condition.put("条件2", Figureline.TYPE_KEY + ":"
						+ FigurelineType.LINK);
				break;
			}
			String sqlString = "select id \"id\",leftId \"leftId\",rightId \"rightId\",linkType \"linkType\",figureId \"figureId\",lefttype \"lefttype\",righttype \"righttype\",birthdate \"birthdate\",ENTITY_TYPE \"_entityType\" from "+Figureline.MY_TYPE+" where "+conStr;
			//System.out.println("sqlString:"+sqlString);
			SqlContainer sc = context.createSqlContainer(sqlString);
			List<BasicEntity> beList =context.executeSelectSQL(sc,Figureline.MY_TYPE);
			//List<BasicEntity> beList =context.queryList(q);
			log.debug("准备删除边对象，删除条件:'" + condition + "'");
			if(beList !=null && beList.size()>0){
				sqlString ="delete from "+Figureline.MY_TYPE+" where "+conStr;
				//System.out.println("sqlString:"+sqlString);
				sc = context.createSqlContainer(sqlString);
				result = context.executeDeleteSQL(sc, Figureline.MY_TYPE);
				//result = context.delete(q);
			}else{
				result=1;
			}
			log.debug("删除边对象的结果：" + result);
		}
		if (result == 0) {
			result = -1;
		}
		return result;
	}

}
