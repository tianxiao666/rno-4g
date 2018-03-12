package com.iscreate.plat.networkresource.engine.tree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.DefaultParam;
import com.iscreate.plat.networkresource.common.tool.Entity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;
import com.iscreate.plat.networkresource.structure.template.StructurePrimary;



/**
 * 对象树操作引擎，该工厂类用于生成树对象
 * 
 * @author joe
 * 
 */
public class TreeEngine {
	Log log = LogFactory.getLog(this.getClass());

	static Context context;
	ContextFactory contextFactory;
	private static final String TREENAME_KEY = "treeName";
	private static final String TREEID_KEY = "treeId";

	public ContextFactory getContextFactory() {
		return contextFactory;
	}

	/**
	 * 设置数据库操作对象
	 * 
	 * @param context
	 */
	public void setContextFactory(ContextFactory contextFactory) {
		if (TreeEngine.context == null) {
			OperationContext c = new OperationContext();
			c.setContextFactory(contextFactory);
			TreeEngine.context = c;
		} else {
			TreeEngine.context.setContextFactory(contextFactory);
		}
		this.contextFactory = contextFactory;
		// TreeEngine.context = context;
	}

	// /**
	// * 以传入的应用对象为根，构建一棵对象树
	// *
	// * @param e
	// * @return
	// */
	// public Tree createTree(ApplicationEntity e) {
	// Tree t = new Tree("Tree");
	// t.context = TreeEngine.context;
	// if (e == null) {
	// log.debug("应用对象参数'e'为空值，返回一个空树");
	// return t;
	// }
	// t.setRoot(e);
	// return t;
	// }

	public Tree createTree(ApplicationEntity e, String name) {
		long treeId = loadOrCreateTreeId(name);
		Tree t = new Tree(treeId);
		t.context = TreeEngine.context;
		if (e == null) {
			log.debug("应用对象参数'e'为空值，返回一个空树");
			return t;
		}
		t.setRoot(e);
		return t;
	}

	private long loadOrCreateTreeId(String name) {
		com.iscreate.plat.networkresource.dataservice.Context context = contextFactory
				.CreateContext();
		/*Query q = context.createQueryBuilder(Tree.class.getSimpleName());
		q.add(Restrictions.eq(TREENAME_KEY, name));*/

		String sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(Tree.class.getSimpleName())+" from "+ Tree.class.getSimpleName()+" where "+TREENAME_KEY+"='"+name+"'";
		SqlContainer sc = context.createSqlContainer(sqlString);
		List<BasicEntity> rs = context.executeSelectSQL(sc, Tree.class.getSimpleName());
		//List<BasicEntity> rs = context.queryList(q);
		long figureId=0L;
		//long figureId=2;
		if (rs == null) {
			figureId = StructurePrimary.getEntityPrimaryKey("Tree", context);
			Entity e = new Entity();
			e.setValue(DefaultParam.typeKey, Tree.class.getSimpleName());
			e.setValue(TREENAME_KEY, name);
			e.setValue(TREEID_KEY, figureId);
			Map<String,String> mp = ResourceCommon.getInsertAttributesAndValuesStringMap(e);
			sqlString = "insert into "+ResourceCommon.getSelectSqlAttributsString(Tree.class.getSimpleName())+"("+mp.get("attrStr")+") values("+mp.get("valueStr")+")";
			System.out.println("sqlString:"+sqlString);
			sc = context.createSqlContainer(sqlString);
			context.executeInsertSQL(sc, Tree.class.getSimpleName());
			//context.insert(e);
		} else {
			figureId = Long.parseLong(rs.get(0).getValue(TREEID_KEY)+"");
		}
		return figureId;
	}

	//
	// /**
	// * 以传入的应用对象为根，去数据库中将该树结构加载到内存中
	// *
	// * @param e
	// * @return
	// */
	// public Tree loadTree(ApplicationEntity e) {
	// String name = "Tree";
	// Tree t = new Tree(name);
	// t.context = TreeEngine.context;
	// if (e == null) {
	// log.debug("应用对象参数'e'为空值，返回一个空树");
	// return t;
	// }
	// _loadTree(e, t, name);
	// return t;
	// }

	public Tree loadTree(ApplicationEntity e, String name) {
		long treeId = loadOrCreateTreeId(name);
		Tree t = new Tree(treeId);
		t.context = TreeEngine.context;
		if (e == null) {
			log.debug("应用对象参数'e'为空值，返回一个空树");
			return t;
		}
		_loadTree(e, t, treeId);
		return t;
	}

	/**
	 * 根据数据应用对象信息去查找节点对象
	 * 
	 * @param e
	 * @param t
	 */
	private void _loadTree(ApplicationEntity e, Tree t, long treeId) {
		HashMap<String, Object> condition = new HashMap<String, Object>();
		for (String key : e.primaryKeys()) {
			condition.put(key, e.getValue(key));
		}
		ApplicationEntity entity = context.getOneApplicationEntity(e.getType(),
				condition);
		if (entity == null) {
			log.debug("找不到应用数据对象:'" + dissectEntity(e) + "'。");
			return;
		}
		condition.clear();
		condition.put(Treenode.entkey, entity.getType());
		////System.out.println(entity.getId());
		condition.put(Treenode.eidkey, entity.getId());
		condition.put(Treenode.treeidkey, treeId);
		String type = Treenode.class.getSimpleName();
		Treenode n = context.getOneTreenode(type, condition);
		if (n == null) {
			log.debug("找不到应用数据对象:'" + dissectEntity(e) + "'对应的树节点。");
			return;
		}
		t.setRoot(n);
		// HashSet<String> nidcache = new HashSet<String>();
		// nidcache.add(n.getNodeId());
		// _loadTree(n, t, nidcache);
		condition.clear();
		condition.put(Treenode.pidkey, n.getNodeId());
		List<Treenode> nodes = context.getTreenodes(type, condition);
		for (Treenode cn : nodes) {
			t.addOneNode(cn);
		}
	}

	// /**
	// * 根据父节点信息去查找节点对象
	// *
	// * @param n
	// * @param t
	// */
	// private void _loadTree(Treenode n, Tree t, HashSet<String> nidcache) {
	// HashMap<String, Object> condition = new HashMap<String, Object>();
	// condition.put(Treenode.pidkey, n.getNodeId());
	// String type = name + "_" + Treenode.class.getSimpleName();
	// List<Treenode> nodes = context.getTreenodes(type, condition);
	// for (Treenode cn : nodes) {
	// if (!nidcache.contains(cn.getNodeId())) {
	// nidcache.add(cn.getNodeId());
	// t.addOneNode(cn);
	// _loadTree(cn, t, nidcache);
	// }
	// }
	// }

	/**
	 * 从应用数据对象中读取可标识的字符串
	 * 
	 * @param entity
	 * @return
	 */
	private String dissectEntity(ApplicationEntity entity) {
		if (entity == null) {
			return "null";
		} else {
			String info = entity.getValue(DefaultParam.typeKey) + "."
					+ entity.getValue(DefaultParam.idKey);
			return info;
		}
	}
}
