package com.iscreate.plat.networkresource.engine.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;
import com.iscreate.plat.networkresource.dataservice.Context;

/**
 * 
 * @filename: OperationContext.java
 * @classpath: com.iscreate.engine.tree
 * @description: 树相关操作
 * @author：
 * @date：Mar 23, 2013 1:47:44 PM
 * @version：
 */
class OperationContext implements com.iscreate.plat.networkresource.engine.tree.Context {

	ContextFactory contextFactory = null;

	public void setContextFactory(ContextFactory factory) {
		this.contextFactory = factory;
	}

	public void saveApplicationEntity(ApplicationEntity e) {
		if (e == null) {
			return;
		}
		String name = e.getType();
		if (name == null || name.isEmpty()) {
			return;
		}
		Context c = contextFactory.CreateContext();
		Map<String,String> mp = ResourceCommon.getInsertAttributesAndValuesStringMap(e);
		String sqlString = "insert into "+name+"("+mp.get("attrStr")+") values("+mp.get("valueStr")+")";
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = c.createSqlContainer(sqlString);
		c.executeInsertSQL(sc, name);
		//c.insert(e);
	}

	public void updateApplicationEntity(ApplicationEntity e) {
		if (e == null) {
			return;
		}
		String id = Long.toString(e.getId());
		Context c = contextFactory.CreateContext();
		//com.iscreate.dataservice.Query q = c.createQueryBuilder(e.getType());
		/*q.add(com.iscreate.dataservice.query.restrictions.Restrictions.eq(
				DefaultParam.idKey, id));*/
		String sqlString = "update "+e.getType()+" set "+ResourceCommon.getUpdateAttributesSqlString(e)+" where ENTITY_ID="+id;
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = c.createSqlContainer(sqlString);
		c.executeUpdateSQL(sc, e.getType());
		//c.update(e, q);
	}

	public void deleteApplicationEntity(ApplicationEntity e) {
		if (e == null) {
			return;
		}
		String name = e.getType();
		String id = Long.toString(e.getId());
		if (name == null || name.isEmpty()) {
			return;
		}
		Context c = contextFactory.CreateContext();
		/*com.iscreate.dataservice.Query q = c.createQueryBuilder(name);
		q.add(com.iscreate.dataservice.query.restrictions.Restrictions.eq(
				DefaultParam.idKey, id));*/
		String sqlString = "delete from "+name+" where ENTITY_ID="+id;
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = c.createSqlContainer(sqlString);
		c.executeDeleteSQL(sc, name);
		//c.delete(q);
	}

	public ApplicationEntity getApplicationEntityById(String type, String id) {
		if (type == null || id == null) {
			return null;
		}
		Context c = contextFactory.CreateContext();
		/*com.iscreate.dataservice.Query q = c.createQueryBuilder(type);
		q.add(com.iscreate.dataservice.query.restrictions.Restrictions.eq(
				DefaultParam.idKey, id));*/
		String sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(type)+" from "+type+" where ENTITY_ID="+id;
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = c.createSqlContainer(sqlString);
		List<BasicEntity> bs = c.executeSelectSQL(sc, type);
		ApplicationEntity e = null;
		if (bs != null && !bs.isEmpty()) {
			e = ApplicationEntity.changeFromEntity(bs.get(0));
			if (e.containKey("_id")) {
				e.remove("_id");
			}
		}
		return e;
	}

	public List<ApplicationEntity> getApplicationEntites(String type,
			Map<String, Object> condition) {
		if (type == null || condition == null) {
			return new ArrayList<ApplicationEntity>();
		}
		Context c = contextFactory.CreateContext();
		//com.iscreate.dataservice.Query q = c.createQueryBuilder(type);
		StringBuffer conStr = new StringBuffer();
		for (String key : condition.keySet()) {
			/*q.add(com.iscreate.dataservice.query.restrictions.Restrictions.eq(
					key, condition.get(key)));*/
			if("_entityType".equals(key)){
				if(!"".equals(conStr+"")){
					conStr.append(" and ENTITY_TYPE='"+condition.get(key)+"'");
				}else{
					conStr.append("ENTITY_TYPE='"+condition.get(key)+"'");
				}
				
			}else if("_entityId".equals(key)){
				if(!"".equals(conStr+"")){
					conStr.append(" and ENTITY_ID='"+condition.get(key)+"'");
				}else{
					conStr.append("ENTITY_ID='"+condition.get(key)+"'");
				}
				
			}else{
				if(!"".equals(conStr+"")){
					conStr.append(" and "+key+"='"+condition.get(key)+"'");
				}else{
					conStr.append(key+"='"+condition.get(key)+"'");
				}

			}
		}
		String sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(type)+" from "+type+" where "+conStr;
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = c.createSqlContainer(sqlString);
		List<BasicEntity> bs = c.executeSelectSQL(sc, type);
		//List<BasicEntity> bs = c.queryList(q);
		if (bs == null || bs.isEmpty()) {
			return new ArrayList<ApplicationEntity>();
		}
		List<ApplicationEntity> es = new ArrayList<ApplicationEntity>();
		for (BasicEntity be : bs) {
			ApplicationEntity ae = ApplicationEntity.changeFromEntity(be);
			if (ae.containKey("_id")) {
				ae.remove("_id");
			}
			es.add(ae);
		}
		return es;
	}

	public ApplicationEntity getOneApplicationEntity(String type,
			Map<String, Object> condition) {
		if (type == null || condition == null) {
			return null;
		}
		Context c = contextFactory.CreateContext();
		//com.iscreate.dataservice.Query q = c.createQueryBuilder(type);
		StringBuffer conStr = new StringBuffer();
		for (String key : condition.keySet()) {
			/*q.add(com.iscreate.dataservice.query.restrictions.Restrictions.eq(
					key, condition.get(key)));*/
			if("_entityType".equals(key)){
				if(!"".equals(conStr+"")){
					conStr.append(" and ENTITY_TYPE='"+condition.get(key)+"'");
				}else{
					conStr.append("ENTITY_TYPE='"+condition.get(key)+"'");
				}
				
			}else if("_entityId".equals(key)){
				if(!"".equals(conStr+"")){
					conStr.append(" and ENTITY_ID='"+condition.get(key)+"'");
				}else{
					conStr.append("ENTITY_ID='"+condition.get(key)+"'");
				}
				
			}else{
				if(!"".equals(conStr+"")){
					conStr.append(" and "+key+"='"+condition.get(key)+"'");
				}else{
					conStr.append(key+"='"+condition.get(key)+"'");
				}

			}
		}
		String sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(type)+" from "+type+" where "+conStr;
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = c.createSqlContainer(sqlString);
		List<BasicEntity> bs = c.executeSelectSQL(sc, type);
		//List<BasicEntity> bs = c.queryList(q);
		if (bs == null || bs.isEmpty()) {
			return null;
		}
		ApplicationEntity ae = ApplicationEntity.changeFromEntity(bs.get(0));
		if (ae.containKey("_id")) {
			ae.remove("_id");
		}
		return ae;
	}

	public void deleteApplicationEntity(String type,
			Map<String, Object> condition) {
		if (type == null || condition == null) {
			return;
		}
		Context c = contextFactory.CreateContext();
		//com.iscreate.dataservice.Query q = c.createQueryBuilder(type);
		StringBuffer conStr = new StringBuffer();
		for (String key : condition.keySet()) {
			/*q.add(com.iscreate.dataservice.query.restrictions.Restrictions.eq(
					key, condition.get(key)));*/
			if("_entityType".equals(key)){
				if(!"".equals(conStr+"")){
					conStr.append(" and ENTITY_TYPE='"+condition.get(key)+"'");
				}else{
					conStr.append("ENTITY_TYPE='"+condition.get(key)+"'");
				}
				
			}else if("_entityId".equals(key)){
				if(!"".equals(conStr+"")){
					conStr.append(" and ENTITY_ID='"+condition.get(key)+"'");
				}else{
					conStr.append("ENTITY_ID='"+condition.get(key)+"'");
				}
				
			}else{
				if(!"".equals(conStr+"")){
					conStr.append(" and "+key+"='"+condition.get(key)+"'");
				}else{
					conStr.append(key+"='"+condition.get(key)+"'");
				}

			}
		}
		String sqlString = "delete from "+type+" where "+conStr;
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = c.createSqlContainer(sqlString);
		c.executeDeleteSQL(sc, type);
		//c.delete(q);
	}

	public void saveTreenode(Treenode e) {
		if (e == null) {
			return;
		}
		String name = e.getNodeName();
		if (name == null || name.isEmpty()) {
			return;
		}
		Context c = contextFactory.CreateContext();
		Map<String,String> mp = ResourceCommon.getInsertAttributesAndValuesStringMap(e);
		String sqlString = "insert into "+name+"("+mp.get("attrStr")+") values("+mp.get("valueStr")+")";
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = c.createSqlContainer(sqlString);
		c.executeInsertSQL(sc, name);
		//c.insert(e);
	}

	public void updateTreenode(Treenode e) {
		if (e == null) {
			return;
		}
		long id = e.getNodeId();
		Context c = contextFactory.CreateContext();
		/*com.iscreate.dataservice.Query q = c
				.createQueryBuilder(e.getNodeName());
		q.add(com.iscreate.dataservice.query.restrictions.Restrictions.eq(
				Treenode.nidkey, id));*/
		String sqlString = "update "+e.getNodeName()+" set "+ResourceCommon.getUpdateAttributesSqlString(e)+" where ENTITY_ID="+id;
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = c.createSqlContainer(sqlString);
		c.executeUpdateSQL(sc, e.getNodeName());
		//c.update(e, q);
		
	}

	public void deleteTreenode(Treenode e) {
		if (e == null) {
			return;
		}
		String name = e.getNodeName();
		long id = e.getNodeId();
		if (name == null || name.isEmpty()) {
			return;
		}
		Context c = contextFactory.CreateContext();
		/*com.iscreate.dataservice.Query q = c.createQueryBuilder(name);
		q.add(com.iscreate.dataservice.query.restrictions.Restrictions.eq(
				Treenode.nidkey, id));*/
		String sqlString = "delete from "+name+" where ENTITY_ID="+id;
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = c.createSqlContainer(sqlString);
		c.executeDeleteSQL(sc, name);
		//c.delete(q);
	}

	public Treenode getTreenodeById(String type, String id) {
		if (type == null || id == null) {
			return null;
		}
		Context c = contextFactory.CreateContext();
		/*com.iscreate.dataservice.Query q = c.createQueryBuilder(type);
		q.add(com.iscreate.dataservice.query.restrictions.Restrictions.eq(
				DefaultParam.idKey, id));*/
		String sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(type)+" from "+type+" where ENTITY_ID="+id;
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = c.createSqlContainer(sqlString);
		List<BasicEntity> bs = c.executeSelectSQL(sc, type);
		//List<BasicEntity> bs = c.queryList(q);
		Treenode n = null;
		if (bs != null && !bs.isEmpty()) {
			n = Treenode.changeFromEntity(bs.get(0));
			if (n.containKey("_id")) {
				n.removeKey("_id");
			}
		}
		return n;
	}

	public List<Treenode> getTreenodes(String type,
			Map<String, Object> condition) {
		if (type == null || condition == null) {
			return new ArrayList<Treenode>();
		}
		Context c = contextFactory.CreateContext();
		//com.iscreate.dataservice.Query q = c.createQueryBuilder(type);
		StringBuffer conStr = new StringBuffer();
		for (String key : condition.keySet()) {
			/*q.add(com.iscreate.dataservice.query.restrictions.Restrictions.eq(
					key, condition.get(key)));*/
			if("_entityType".equals(key)){
				if(!"".equals(conStr+"")){
					conStr.append(" and ENTITY_TYPE='"+condition.get(key)+"'");
				}else{
					conStr.append("ENTITY_TYPE='"+condition.get(key)+"'");
				}
				
			}else if("_entityId".equals(key)){
				if(!"".equals(conStr+"")){
					conStr.append(" and ENTITY_ID='"+condition.get(key)+"'");
				}else{
					conStr.append("ENTITY_ID='"+condition.get(key)+"'");
				}
				
			}else{
				if(!"".equals(conStr+"")){
					conStr.append(" and "+key+"='"+condition.get(key)+"'");
				}else{
					conStr.append(key+"='"+condition.get(key)+"'");
				}

			}
		}
		String sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(type)+" from "+type+" where "+conStr;
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = c.createSqlContainer(sqlString);
		List<BasicEntity> bs = c.executeSelectSQL(sc, type);
		//List<BasicEntity> bs = c.queryList(q);
		if (bs == null || bs.isEmpty()) {
			return new ArrayList<Treenode>();
		}
		List<Treenode> es = new ArrayList<Treenode>();
		for (BasicEntity be : bs) {
			Treenode ae = Treenode.changeFromEntity(be);
			if (ae.containKey("_id")) {
				ae.removeKey("_id");
			}
			es.add(ae);
		}
		return es;
	}

	public Treenode getOneTreenode(String type, Map<String, Object> condition) {
		if (type == null || condition == null) {
			return null;
		}
		Context c = contextFactory.CreateContext();
		//com.iscreate.dataservice.Query q = c.createQueryBuilder(type);
		StringBuffer conStr = new StringBuffer();
		for (String key : condition.keySet()) {
			/*q.add(com.iscreate.dataservice.query.restrictions.Restrictions.eq(
					key, condition.get(key)));*/
			if("_entityType".equals(key)){
				if(!"".equals(conStr+"")){
					conStr.append(" and ENTITY_TYPE='"+condition.get(key)+"'");
				}else{
					conStr.append("ENTITY_TYPE='"+condition.get(key)+"'");
				}
				
			}else if("_entityId".equals(key)){
				if(!"".equals(conStr+"")){
					conStr.append(" and ENTITY_ID='"+condition.get(key)+"'");
				}else{
					conStr.append("ENTITY_ID='"+condition.get(key)+"'");
				}
				
			}else{
				if(!"".equals(conStr+"")){
					conStr.append(" and "+key+"='"+condition.get(key)+"'");
				}else{
					conStr.append(key+"='"+condition.get(key)+"'");
				}
				
			}
		}
		String sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(type)+" from "+type+" where "+conStr;
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = c.createSqlContainer(sqlString);
		List<BasicEntity> bs = c.executeSelectSQL(sc, type);
		//List<BasicEntity> bs = c.queryList(q);
		if (bs == null || bs.isEmpty()) {
			return null;
		}
		Treenode ae = Treenode.changeFromEntity(bs.get(0));
		if (ae.containKey("_id")) {
			ae.removeKey("_id");
		}
		return ae;
	}

	public void deleteTreenode(String type, Map<String, Object> condition) {
		if (type == null || condition == null) {
			return;
		}
		Context c = contextFactory.CreateContext();
		//com.iscreate.dataservice.Query q = c.createQueryBuilder(type);
		StringBuffer conStr = new StringBuffer();
		for (String key : condition.keySet()) {
			/*q.add(com.iscreate.dataservice.query.restrictions.Restrictions.eq(
					key, condition.get(key)));*/
			if("_entityType".equals(key)){
				if(!"".equals(conStr+"")){
					conStr.append(" and ENTITY_TYPE='"+condition.get(key)+"'");
				}else{
					conStr.append("ENTITY_TYPE='"+condition.get(key)+"'");
				}
				
			}else if("_entityId".equals(key)){
				if(!"".equals(conStr+"")){
					conStr.append(" and ENTITY_ID='"+condition.get(key)+"'");
				}else{
					conStr.append("ENTITY_ID='"+condition.get(key)+"'");
				}
				
			}else{
				if(!"".equals(conStr+"")){
					conStr.append(" and "+key+"='"+condition.get(key)+"'");
				}else{
					conStr.append(key+"='"+condition.get(key)+"'");
				}

			}
		}
		String sqlString = "delete from "+type+" where "+conStr;
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = c.createSqlContainer(sqlString);
		c.executeDeleteSQL(sc, type);
		
		//c.delete(q);
	}
}
