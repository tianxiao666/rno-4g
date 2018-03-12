package com.iscreate.plat.networkresource.engine.figure.cmd;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;
import com.iscreate.plat.networkresource.engine.figure.Figureline;

public class CommandDeleteFigureline implements Command {
	private Map<String, ?> condition;

	public CommandDeleteFigureline(Map<String, ?> condition) {
		this.condition = condition;
	}
	/**
	 * 根据条件值删除figureline表里的记录
	 *
	 *(mysql to oracle 迁移:dateService封装删除记录方法context.delete(q)会发生SqlException，sql语句不标准，用context.executeDeleteSQL方法替代）
	 */
	public Integer doExecution(ContextFactory contextFactory) {
		Log log = LogFactory.getLog(getClass());
		log.debug("准备删除边对象，删除条件:'" + condition + "'");
		Context context = contextFactory.CreateContext();
		//Query q = context.createQueryBuilder(Figureline.MY_TYPE);
		String sqlStr= "delete from "+Figureline.MY_TYPE+" where ";
		StringBuffer sf = new StringBuffer();
		for (String key : condition.keySet()) {
			Object value = condition.get(key);
			
			if (value instanceof Collection) {
				Collection<?> c = (Collection<?>) value;
				Object[] qc = c.toArray();
				String qcStr="";
				for(Object o:qc){
					qcStr+=",'"+o+"'";
				}
				if(key.equals("_entityType")){
					if("".equals(sf+"")){
						sf.append("ENTITY_TYPE in("+qcStr.substring(1)+")");
					}else{
						sf.append("and ENTITY_TYPE in("+qcStr.substring(1)+")");
					}
				}else{
					if("".equals(sf+"")){
						sf.append(key+" in("+qcStr.substring(1)+")");
					}else{
						sf.append("and "+key+" in("+qcStr.substring(1)+")");
					}
				}
				//q.add(Restrictions.in(key, qc));
			} else {
				if(key.equals("_entityType")){
					if("".equals(sf+"")){
						sf.append("ENTITY_TYPE='"+value+"'");
					}else{
						sf.append("and ENTITY_TYPE='"+value+"'");
					}
				}else{
					if("".equals(sf+"")){
						sf.append(key+"='"+value+"'");
					}else{
						sf.append("and "+key+"='"+value+"'");
					}
				}
				//q.add(Restrictions.eq(key, value));
			}
		}
		sqlStr = sqlStr+sf;
		//System.out.println("sqlString:"+sqlStr);
		SqlContainer sc = context.createSqlContainer(sqlStr);
		int result = context.executeDeleteSQL(sc, Figureline.MY_TYPE);
		//int result = context.delete(q);
		log.debug("以条件'" + condition + "'删除边对象的结果：" + result);
		if (result == 0) {
			result = -1;
		}
		return result;
	}

}
