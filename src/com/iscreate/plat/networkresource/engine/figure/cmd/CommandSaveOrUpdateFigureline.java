package com.iscreate.plat.networkresource.engine.figure.cmd;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;
import com.iscreate.plat.networkresource.engine.figure.Figureline;

public class CommandSaveOrUpdateFigureline implements Command {

	private Figureline line;

	public CommandSaveOrUpdateFigureline(Figureline line) {
		this.line = line;
	}
	/**
	 * Figureline表插入或更新一个记录   （dataService 源码因为没有 采用下面方法整改）
	 *(mysql to oracle 迁移:dateService封装插入记录方法context.update(entity, q)会发生SqlException，sql语句不标准，用context.executeUpdateSQL方法替代，拼装sql）
	 *
	 */
	public Integer doExecution(ContextFactory contextFactory) {
		Log log = LogFactory.getLog(getClass());
		Context context = contextFactory.CreateContext();
		//Query q = context.createQueryBuilder(Figureline.MY_TYPE);
		HashMap<String, Object> condition = new HashMap<String, Object>();
		condition.put(Figureline.LEFTID_KEY, line.getLeftId());
		//q.add(Restrictions.eq(Figureline.LEFTID_KEY, line.getLeftId()));
		condition.put(Figureline.RIGHTID_KEY, line.getRightId());
		//q.add(Restrictions.eq(Figureline.RIGHTID_KEY, line.getRightId()));
		condition.put(Figureline.FIGUREID_KEY, line.getFigureId());
		//q.add(Restrictions.eq(Figureline.FIGUREID_KEY, line.getFigureId()));
		condition.put(Figureline.TYPE_KEY, line.getLineType());
		//q.add(Restrictions.eq(Figureline.TYPE_KEY, line.getLineType()));
		condition.put(Figureline.LEFTTYPE_KEY, line.getValue(Figureline.LEFTTYPE_KEY));
		//q.add(Restrictions.eq(Figureline.LEFTTYPE_KEY, line.getValue(Figureline.LEFTTYPE_KEY)));
		condition.put(Figureline.RIGHTTYPE_KEY, line.getValue(Figureline.RIGHTTYPE_KEY));
		//q.add(Restrictions.eq(Figureline.RIGHTTYPE_KEY,line.getValue(Figureline.RIGHTTYPE_KEY)));
		log.debug("使用条件'" + condition + "'查询边对象。");
		StringBuffer conStr = new StringBuffer();
		conStr.append(Figureline.LEFTID_KEY+"="+line.getLeftId());
		conStr.append(" and "+Figureline.RIGHTID_KEY+"="+line.getRightId());
		conStr.append(" and "+Figureline.FIGUREID_KEY+"="+line.getFigureId());
		conStr.append(" and "+Figureline.TYPE_KEY+"='"+line.getLineType()+"'");
		conStr.append(" and "+Figureline.LEFTTYPE_KEY+"='"+line.getValue(Figureline.LEFTTYPE_KEY)+"'");
		conStr.append(" and "+Figureline.RIGHTTYPE_KEY+"='"+line.getValue(Figureline.RIGHTTYPE_KEY)+"'");

		String sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(Figureline.MY_TYPE)+" from "+Figureline.MY_TYPE+" where "+conStr;
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = context.createSqlContainer(sqlString);
		List<BasicEntity> beList=context.executeSelectSQL(sc, Figureline.MY_TYPE);
		//BasicEntity be = context.queryEntity(q);
		int result = 1;
		if (beList == null) {
			//q = context.createQueryBuilder(Figureline.MY_TYPE);
			condition.clear();
			condition.put(Figureline.LEFTID_KEY, line.getLeftId());
			//q.add(Restrictions.eq(Figureline.RIGHTID_KEY, line.getLeftId()));
			condition.put(Figureline.RIGHTID_KEY, line.getRightId());
			//q.add(Restrictions.eq(Figureline.LEFTID_KEY, line.getRightId()));
			condition.put(Figureline.FIGUREID_KEY, line.getFigureId());
			//q.add(Restrictions.eq(Figureline.FIGUREID_KEY, line.getFigureId()));
			condition.put(Figureline.TYPE_KEY, line.getLineType());
			//q.add(Restrictions.eq(Figureline.TYPE_KEY, line.getLineType()));
			condition.put(Figureline.LEFTTYPE_KEY, line.getValue(Figureline.LEFTTYPE_KEY));
			//q.add(Restrictions.eq(Figureline.LEFTTYPE_KEY, line.getValue(Figureline.RIGHTTYPE_KEY)));
			condition.put(Figureline.RIGHTTYPE_KEY, line.getValue(Figureline.RIGHTTYPE_KEY));
			//q.add(Restrictions.eq(Figureline.RIGHTTYPE_KEY,line.getValue(Figureline.LEFTTYPE_KEY)));
			log.debug("使用条件'" + condition + "'查询边对象。");
			conStr = new StringBuffer();
			conStr.append(Figureline.RIGHTID_KEY+"="+line.getLeftId());
			conStr.append(" and "+Figureline.LEFTID_KEY+"="+line.getRightId());
			conStr.append(" and "+Figureline.FIGUREID_KEY+"="+line.getFigureId());
			conStr.append(" and "+Figureline.TYPE_KEY+"='"+line.getLineType()+"'");
			conStr.append(" and "+Figureline.RIGHTTYPE_KEY+"='"+line.getValue(Figureline.LEFTTYPE_KEY)+"'");
			conStr.append(" and "+Figureline.LEFTTYPE_KEY+"='"+line.getValue(Figureline.RIGHTTYPE_KEY)+"'");

			sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(Figureline.MY_TYPE)+" from "+Figureline.MY_TYPE+" where "+conStr;
			//System.out.println("sqlString:"+sqlString);
			sc = context.createSqlContainer(sqlString);
			beList=context.executeSelectSQL(sc, Figureline.MY_TYPE);
			//be = context.queryEntity(q);
		}
		if (beList == null) {
			String birthdate = "";
			birthdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS")
					.format(new Date());
			line.setBirthdate(birthdate);
			Map<String,String> mp =  ResourceCommon.getInsertAttributesAndValuesStringMap(line);
			sqlString = "insert into "+Figureline.MY_TYPE +"("+mp.get("attrStr")+") values ("+mp.get("valueStr")+")";
			//System.out.println("sqlString:"+sqlString);
			sc = context.createSqlContainer(sqlString);
			result=context.executeInsertSQL(sc, Figureline.MY_TYPE);
			//result = context.insert(line);
			log.debug("库中不存在该边对象，插入该边对象。操作结果：" + result);
			return result;
		}
		log.debug("边对象已经存在库中。");
		return result;
	}

}
