package com.iscreate.plat.networkresource.engine.figure.execution;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;
import com.iscreate.plat.networkresource.engine.figure.Figureline;
import com.iscreate.plat.networkresource.engine.figure.FigurelineType;
import com.iscreate.plat.networkresource.engine.figure.Figurenode;

public class BasicListFigurenodeExecution extends ListFigurenodeExecution {

	private Figurenode node;
	private FigurelineType figurelineType;
	private String destinationAenType;
	private QueryEnvironment env;
	private Log log = LogFactory.getLog(getClass());

	public BasicListFigurenodeExecution(Figurenode node,
			FigurelineType figurelineType, String destinationAenType) {
		this.node = node;
		this.figurelineType = figurelineType;
		this.destinationAenType = destinationAenType;
		log.debug("BasicListFigurenodeExecution的参数'node'=" + node);
		log.debug("BasicListFigurenodeExecution的参数'figurelineType'="
				+ figurelineType);
		log.debug("BasicListFigurenodeExecution的参数'destinationAenType'="
				+ destinationAenType);
	}

	@Override
	/**
	 * 构建查询条件，从一个节点及要查找的关联类型、目标类型，获取另一组符合条件的图节点
	 */
	public String buildFigurenodeQuery(QueryEnvironment env, String figurenodeQuery) {
		this.env = env;
		if (node == null || figurelineType == null) {
			log.debug("方法参数：node 或 figurelineType为空，组建空查询条件");
			//figurenodeQuery.add(Restrictions.eq(Figurenode.ID_KEY, -1));
			figurenodeQuery =Figurenode.ID_KEY+"='-1'";
			return figurenodeQuery;
		}
		long aenFnId = node.getId();
		// if (aenFnId == null || aenFnId.trim().isEmpty()) {
		// log.debug("获取应用数据对象对应的图节点对象为空");
		// figurenodeQuery.add(Restrictions.eq(Figurenode.ID_KEY, -1));
		// return;
		// }
		String[] figurenodeIds = null;
		switch (figurelineType) {
		case CLAN:
			// String[] pa = parentFigurenodeIds(aenFnId);
			// String[] ca = childFigurenodeIds(aenFnId);
			// figurenodeIds = mergeArray(pa, ca);
			figurenodeIds = childFigurenodeIds(aenFnId);
			break;
		case PARENT:
			figurenodeIds = parentFigurenodeIds(aenFnId);
			break;
		case CHILD:
			figurenodeIds = childFigurenodeIds(aenFnId);
			break;
		case LINK:
			String[] fa = forwordFigurenodeIds(aenFnId);
			String[] ba = backwordFigurenodeIds(aenFnId);
			figurenodeIds = mergeArray(fa, ba);
			break;
		case FORWORD:
			figurenodeIds = forwordFigurenodeIds(aenFnId);
			break;
		case BACKWORD:
			figurenodeIds = backwordFigurenodeIds(aenFnId);
			break;
		}
		log.debug("从相邻边找到的图节点个数为:" + figurenodeIds.length);
		if (figurenodeIds.length == 0) {
			//figurenodeQuery.add(Restrictions.eq(Figurenode.ID_KEY, -1));
			figurenodeQuery =Figurenode.ID_KEY+"='-1'";
			return figurenodeQuery;
		}
		
		//figurenodeQuery.add(Restrictions.in(Figurenode.ID_KEY, figurenodeIds));
		String queryString="";
		if(figurenodeIds!=null && figurenodeIds.length>0){
			StringBuffer ids=new StringBuffer();
			int i=0;
			queryString =" (";
			for(int index=0;index<=figurenodeIds.length-1;index++){
				ids.append(","+figurenodeIds[index]);
				if(i>=50||index==figurenodeIds.length-1){
					if(" (".equals(queryString)){
						queryString += Figurenode.ID_KEY+" in ("+ids.substring(1)+")";
					}else{
						queryString += " or "+Figurenode.ID_KEY+" in ("+ids.substring(1)+")";
					}
					ids=new StringBuffer();
				}
				i++;
			}
			queryString =queryString+") ";
			if (destinationAenType != null && !destinationAenType.isEmpty()) {
				/*figurenodeQuery.add(Restrictions.eq(Figurenode.ENTYPE_KEY,
						destinationAenType));*/
				if("".equals(queryString)){
					queryString +=Figurenode.ENTYPE_KEY+"='"+destinationAenType+"'";
				}else{
					queryString +=" and "+Figurenode.ENTYPE_KEY+"='"+destinationAenType+"'";
				}
			}
			figurenodeQuery += " and " +queryString;

		}else{
			if (destinationAenType != null && !destinationAenType.isEmpty()) {
				queryString +=Figurenode.ENTYPE_KEY+"='"+destinationAenType+"'";
				figurenodeQuery += " and " +queryString;
			}
		}
		return figurenodeQuery;
	}

	/**
	 * 获取父节点的ID
	 * @param aenFnId
	 * @return
	 * (mysql to oracle 迁移:dateService封装查询记录方法context.queryList(q)会发生SqlException，sql语句不标准，用context.executeSelectSQL方法替代）
	 */
	private String[] parentFigurenodeIds(long aenFnId) {
		log.debug("查询图节点：" + aenFnId + "对应的父节点ID");
		HashMap<String, Object> condition = new HashMap<String, Object>();
		Context context = env.getContextFactory().CreateContext();
		//Query q = context.createQueryBuilder(Figureline.MY_TYPE);
		condition.put("查询对应类型", Figureline.MY_TYPE);
		//q.add(Restrictions.eq(Figureline.RIGHTID_KEY, aenFnId));
		condition.put("条件1", Figureline.RIGHTID_KEY + "=" + aenFnId);
		//q.add(Restrictions.eq(Figureline.TYPE_KEY, FigurelineType.CLAN));
		condition.put("条件2", Figureline.TYPE_KEY + "=" + FigurelineType.CLAN);
		log.debug("查询条件：" + condition);
		StringBuffer conStr = new StringBuffer();
		conStr.append(Figureline.RIGHTID_KEY+"="+aenFnId);
		conStr.append(" and "+Figureline.TYPE_KEY+"='"+FigurelineType.CLAN+"'");
		String sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(Figureline.MY_TYPE)
		                   +" from " + Figureline.MY_TYPE+" where "+ conStr;
		////System.out.println("sqlString:"+sqlString);
		SqlContainer sc = context.createSqlContainer(sqlString);
		List<BasicEntity> bes = context.executeSelectSQL(sc, Figureline.MY_TYPE);
		//List<BasicEntity> bes = context.queryList(q);
		if (bes == null) {
			return new String[] {};
		}
		String[] nids = new String[bes.size()];
		for (int i = 0; i < bes.size(); i++) {
			nids[i] = bes.get(i).getValue(Figureline.LEFTID_KEY);
		}
		return nids;
	}

	/**
	 *  获取子节点ID
	 * @param aenFnId
	 * @return
	 * (mysql to oracle 迁移:dateService封装查询记录方法context.queryList(q)会发生SqlException，sql语句不标准，用context.executeSelectSQL方法替代）
	 */
	private String[] childFigurenodeIds(long aenFnId) {
		log.debug("查询图节点：" + aenFnId + "对应的子节点ID");
		Context context = env.getContextFactory().CreateContext();
		HashMap<String, Object> condition = new HashMap<String, Object>();
		//Query q = context.createQueryBuilder(Figureline.MY_TYPE);
		condition.put("查询对应类型", Figureline.MY_TYPE);
		//q.add(Restrictions.eq(Figureline.LEFTID_KEY, aenFnId));
		condition.put("条件1", Figureline.LEFTID_KEY + "=" + aenFnId);
		//q.add(Restrictions.eq(Figureline.TYPE_KEY, FigurelineType.CLAN));
		condition.put("条件2", Figureline.TYPE_KEY + "=" + FigurelineType.CLAN);
		
		log.debug("查询条件：" + condition);
		StringBuffer conStr = new StringBuffer();
		conStr.append(Figureline.LEFTID_KEY+"="+aenFnId);
		conStr.append(" and "+Figureline.TYPE_KEY+"='"+FigurelineType.CLAN+"'");
		String sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(Figureline.MY_TYPE)
		                   +" from " + Figureline.MY_TYPE+" where "+ conStr;
		////System.out.println("sqlString:"+sqlString);
		SqlContainer sc = context.createSqlContainer(sqlString);
		List<BasicEntity> bes = context.executeSelectSQL(sc, Figureline.MY_TYPE);
		//List<BasicEntity> bes = context.queryList(q);
		if (bes == null) {
			return new String[] {};
		}
		String[] nids = new String[bes.size()];
		for (int i = 0; i < bes.size(); i++) {
			nids[i] = bes.get(i).getValue(Figureline.RIGHTID_KEY);
		}
		return nids;
	}

	/**
	 * 获取前一级节点ID
	 * @param aenFnId
	 * @return
	 * (mysql to oracle 迁移:dateService封装查询记录方法context.queryList(q)会发生SqlException，sql语句不标准，用context.executeSelectSQL方法替代）
	 */
	private String[] forwordFigurenodeIds(long aenFnId) {
		log.debug("查询图节点：" + aenFnId + "对应的前一邻接节点ID");
		Context context = env.getContextFactory().CreateContext();
		HashMap<String, Object> condition = new HashMap<String, Object>();
		//Query q = context.createQueryBuilder(Figureline.MY_TYPE);
		condition.put("查询对应类型", Figureline.MY_TYPE);
		//q.add(Restrictions.eq(Figureline.LEFTID_KEY, aenFnId));
		condition.put("条件1", Figureline.LEFTID_KEY + "=" + aenFnId);
		//q.add(Restrictions.eq(Figureline.TYPE_KEY, FigurelineType.LINK));
		condition.put("条件2", Figureline.TYPE_KEY + "=" + FigurelineType.LINK);
		log.debug("查询条件：" + condition);
		StringBuffer conStr = new StringBuffer();
		conStr.append(Figureline.LEFTID_KEY+"="+aenFnId);
		conStr.append(" and "+Figureline.TYPE_KEY+"='"+FigurelineType.LINK+"'");
		String sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(Figureline.MY_TYPE)
		                   +" from " + Figureline.MY_TYPE+" where "+ conStr;
		////System.out.println("sqlString:"+sqlString);
		SqlContainer sc = context.createSqlContainer(sqlString);
		List<BasicEntity> bes = context.executeSelectSQL(sc, Figureline.MY_TYPE);
		//List<BasicEntity> bes = context.queryList(q);
		if (bes == null) {
			return new String[] {};
		}
		String[] nids = new String[bes.size()];
		for (int i = 0; i < bes.size(); i++) {
			nids[i] = bes.get(i).getValue(Figureline.RIGHTID_KEY);
		}
		return nids;
	}

	/**
	 * 获取后一级节点ID
	 * @param aenFnId
	 * @return
	 * (mysql to oracle 迁移:dateService封装查询记录方法context.queryList(q)会发生SqlException，sql语句不标准，用context.executeSelectSQL方法替代）
	 */
	private String[] backwordFigurenodeIds(long aenFnId) {
		// 组建查询条件及日志
		log.debug("查询图节点：" + aenFnId + "对应的后一邻接节点ID");
		Context context = env.getContextFactory().CreateContext();
		HashMap<String, Object> condition = new HashMap<String, Object>();
		//Query q = context.createQueryBuilder(Figureline.MY_TYPE);
		condition.put("查询对应类型", Figureline.MY_TYPE);
		//q.add(Restrictions.eq(Figureline.RIGHTID_KEY, aenFnId));
		condition.put("条件1", Figureline.RIGHTID_KEY + "=" + aenFnId);
		//q.add(Restrictions.eq(Figureline.TYPE_KEY, FigurelineType.LINK));
		condition.put("条件2", Figureline.TYPE_KEY + "=" + FigurelineType.LINK);
		log.debug("查询条件：" + condition);
		StringBuffer conStr = new StringBuffer();
		conStr.append(Figureline.RIGHTID_KEY+"="+aenFnId);
		conStr.append(" and "+Figureline.TYPE_KEY+"='"+FigurelineType.LINK+"'");
		String sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(Figureline.MY_TYPE)
		                   +" from " + Figureline.MY_TYPE+" where "+ conStr;
		////System.out.println("sqlString:"+sqlString);
		SqlContainer sc = context.createSqlContainer(sqlString);
		List<BasicEntity> bes = context.executeSelectSQL(sc, Figureline.MY_TYPE);
		// 查询
		//List<BasicEntity> bes = context.queryList(q);
		if (bes == null) {
			return new String[] {};
		}
		String[] nids = new String[bes.size()];
		for (int i = 0; i < bes.size(); i++) {
			nids[i] = bes.get(i).getValue(Figureline.LEFTID_KEY);
		}
		return nids;
	}

	/**
	 * 数组合并
	 * 
	 * @param array1
	 * @param array2
	 * @return
	 */
	private String[] mergeArray(String[] array1, String[] array2) {
		String[] array = new String[array1.length + array2.length];
		System.arraycopy(array1, 0, array, 0, array1.length);
		System.arraycopy(array2, 0, array, array1.length, array2.length);
		return array;
	}
}
