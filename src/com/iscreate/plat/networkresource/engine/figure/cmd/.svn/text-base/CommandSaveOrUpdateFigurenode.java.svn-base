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
import com.iscreate.plat.networkresource.engine.figure.Figurenode;

public class CommandSaveOrUpdateFigurenode implements Command {

	private Figurenode node;

	public CommandSaveOrUpdateFigurenode(Figurenode node) {
		this.node = node;
	}
	public CommandSaveOrUpdateFigurenode(BasicEntity be) {
		this.node = new Figurenode();
		this.node.setEntityId(Long.valueOf(be.getValue(Figurenode.ENID_KEY)+""));
		this.node.setEntityType(be.getValue(Figurenode.ENTYPE_KEY)+"");
		this.node.setId(Long.valueOf(be.getValue(Figurenode.ID_KEY)+""));
		this.node.setBirthdate(be.getValue(Figurenode.DATE_KEY)+"");
		this.node.setPath(be.getValue(Figurenode.PATH)+"");
		if(be.getValue(Figurenode.PARENT_FIGURENODE_ID)!=null){
			this.node.setParentFigurenodeId(Long.valueOf(be.getValue(Figurenode.PARENT_FIGURENODE_ID)+""));
		}
		
	}
	/**
	 * Figurenode表插入或更新一个记录 （dataService 源码因为没有 采用下面方法整改） (mysql to oracle
	 * 迁移:dateService封装插入记录方法context.update(entity,
	 * q)会发生SqlException，sql语句不标准，用context.executeUpdateSQL方法替代，拼装sql）
	 * 
	 */
	public Integer doExecution(ContextFactory contextFactory) {
		Log log = LogFactory.getLog(getClass());
		Context context = contextFactory.CreateContext();
		// Query q = context.createQueryBuilder(Figurenode.MY_TYPE);
		// 组建查询条件以及显示条件
		HashMap<String, Object> condition = new HashMap<String, Object>();
		// q.add(Restrictions.eq(Figurenode.ENTYPE_KEY, node.getEntityType()));
		condition.put(Figurenode.ENTYPE_KEY, node.getEntityType());
		// q.add(Restrictions.eq(Figurenode.ENID_KEY, node.getEntityId()));
		condition.put(Figurenode.ENID_KEY, node.getEntityId());
		// q.add(Restrictions.eq(Figurenode.FIGUREID_KEY, node.getFigureId()));
		
		
		StringBuffer conStr = new StringBuffer();
		conStr
				.append(Figurenode.ENTYPE_KEY + "='" + node.getEntityType()
						+ "'");
		conStr.append(" and " + Figurenode.ENID_KEY + "=" + node.getEntityId());
		if(node.getFigureId()!=0){
			condition.put(Figurenode.FIGUREID_KEY, node.getFigureId());
			conStr.append(" and " + Figurenode.FIGUREID_KEY + "="
					+ node.getFigureId());
		}
		

		log.debug("使用条件'" + condition + "'查询应用数据对象。");
		// 在库中查询是否存在该图节点
		String sqlString = "select "
				+ ResourceCommon
						.getSelectSqlAttributsString(Figurenode.MY_TYPE)
				+ " from " + Figurenode.MY_TYPE + " where " + conStr;
		// System.out.println("sqlString:"+sqlString);
		SqlContainer sc = context.createSqlContainer(sqlString);
		List<BasicEntity> bs = context.executeSelectSQL(sc, Figurenode.MY_TYPE);
		// List<BasicEntity> bs = context.queryList(q);
		int result = 0;
		if (bs == null) {
			String birthdate = "";
			birthdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS")
					.format(new Date());
			node.setBirthdate(birthdate);
			Map<String, String> mp = ResourceCommon
					.getInsertAttributesAndValuesStringMap(node);
			sqlString = "insert into " + Figurenode.MY_TYPE + "("
					+ mp.get("attrStr") + ") values (" + mp.get("valueStr")
					+ ")";
			// System.out.println("sqlString:"+sqlString);
			sc = context.createSqlContainer(sqlString);
			result = context.executeInsertSQL(sc, Figurenode.MY_TYPE);
			// result = context.insert(node);
			log.debug("库中不存在该图节点对象，插入该图节点对象。操作结果：" + result);
		} else {
			// node.setId(Long.parseLong(bs.get(0).getValue(Figurenode.ID_KEY) +
			// ""));
			// node.setBirthdate(bs.get(0).getValue(Figurenode.DATE_KEY) + "");
			// result = context.update(node, q);
			//yuan.yw add 2013-06-28
			if ((!"".equals(node.getValue(Figurenode.PATH))
					&& node.getValue(Figurenode.PATH) != null)
					||(!"".equals(node.getValue(Figurenode.PARENT_FIGURENODE_ID))
					&& node.getValue(Figurenode.PARENT_FIGURENODE_ID) != null)) {
				sqlString = "update " + Figurenode.MY_TYPE + " set ";
				String str = "";
				if(!"".equals(node.getValue(Figurenode.PATH))
						&& node.getValue(Figurenode.PATH) != null){
					str += " path='"+ node.getValue(Figurenode.PATH) + "' ";
				}else if("null".equals(node.getValue(Figurenode.PATH))){
					str += " path=null ";
				}
				if(!"".equals(node.getValue(Figurenode.PARENT_FIGURENODE_ID))
						&& node.getValue(Figurenode.PARENT_FIGURENODE_ID) != null){
					if("".equals(str)){
						str += " parent_figurenode_id='"+ node.getValue(Figurenode.PARENT_FIGURENODE_ID) + "' ";
					}else{
						str += ",parent_figurenode_id='"+ node.getValue(Figurenode.PARENT_FIGURENODE_ID) + "' ";
					}
					
				}else if("null".equals(node.getValue(Figurenode.PARENT_FIGURENODE_ID)+"")){
					
					if("".equals(str)){
						str += " parent_figurenode_id=null ";
					}else{
						str += ",parent_figurenode_id=null ";
					}
				}
				sqlString = sqlString + str + " where "+ conStr;
				sc = context.createSqlContainer(sqlString);
				result = context.executeUpdateSQL(sc, Figurenode.MY_TYPE);
			} else {
				result = 1;
			}

			// result = 1;
			log.debug("库中已经存在该图节点对象。");
		}
		return result;
	}

}
