package com.iscreate.plat.networkresource.engine.tree;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.DefaultParam;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.structure.template.StructurePrimary;

class Treenode extends BasicEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2168593943892991105L;

	static final String treeidkey = "treeId";
	// 节点ID的KEY
	static final String nidkey = "nodeId";
	// 父节点ID的KEY
	static final String pidkey = "parentId";
	// 应用对象TYPE的KEY
	static final String entkey = "aenType";
	// 应用对象ID的KEY
	static final String eidkey = "aenId";
	// 节点类型的KEY
	private final String typeKey = DefaultParam.typeKey;
	// private final String idkey = DefaultParam.idKey;

	// 节点所包含的应用对象
	ApplicationEntity entity = null;
	// 父节点
	Treenode parent = null;
	// 节点所在的树对象引用
	Tree tree = null;
	// 标记是否已经该节点对象的子节点信息LOAD到内存
	boolean hadLoadChild = false;

	/**
	 * 无参构造函数，该方法用于从ENTITY对象转换成TREENODE对象
	 */
	private Treenode(String nodeId) {
		if(nodeId==null || "".equals(nodeId) || "null".equals(nodeId)){
			long id = 0;
			WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(
		            ServletActionContext.getServletContext());
			BeanFactory factory = (BeanFactory)wac;   
			ContextFactory contextFactory = (ContextFactory) factory.getBean("contextFactory");  
			id = StructurePrimary.getEntityPrimaryKey("Treenode",contextFactory.CreateContext());
			setNodeId(id);
		}
		
	}

	/**
	 * 节点的构造函数。构建节点同时为节点命名，<br/>
	 * 数据操作接口将根据该名称来判断节点信息的存储位置。<br/>
	 * 
	 * @param treeId
	 */
	Treenode(long treeId) {
		String n = this.getClass().getSimpleName();
		super.set(treeidkey,treeId);
		super.set(typeKey, n);
		long id = 0;
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(
	            ServletActionContext.getServletContext());
		BeanFactory factory = (BeanFactory)wac;   
		ContextFactory contextFactory = (ContextFactory) factory.getBean("contextFactory");  
		id = StructurePrimary.getEntityPrimaryKey("Treenode",contextFactory.CreateContext());
		setNodeId(id);
	}

	/**
	 * 获取节点ID，如果该节点还没有ID，则用IDBuilder对象为节点创建一个ID。
	 * 
	 * @return
	 */
	public long getNodeId() {
		long id = Long.parseLong(super.getValue(nidkey)+"");
		return id;
	}

	/**
	 * 设置节点ID，如果包含该应用对象的节点已经存在，<br/>
	 * 那么获取已有的节点之后为该节点重新设置ID。<br/>
	 * 
	 * @param id
	 */
	public void setNodeId(long id) {
		if (id > 0) {
			super.set(nidkey, id);
			// super.set(idkey, id);
		}
	}

	public String getNodeName() {
		String name = super.getValue(typeKey);
		return name;
	}

	/**
	 * 获取父节点。如果父节点的引用为空，则根据父ID去加载父节点
	 * 
	 * @return
	 */
	public Treenode getParent() {
		if (this.parent != null) {
			return parent;
		}
		long id = 0;
		if(super.getValue(pidkey) != null){
			id =  Long.parseLong(super.getValue(pidkey)+"");
		}
		if (tree != null) {
			// 如果该节点是树的根，不需要去找它的父节点。
			if (this == tree.root) {
				return null;
			}
			// 直接从树的节点中找父节点。
			for (int i = 0; i < tree.ns; i++) {
				Treenode n = tree.nodes[i];
				if (n.getNodeId() == id) {
					parent = n;
					break;
				}
			}
		}
		return parent;
	}

	/**
	 * 将父节点的引用添加到当前节点中
	 * 
	 * @param parent
	 */
	public void setParent(Treenode parent) {
		if (parent == null) {
			return;
		}
		this.parent = parent;
		long id = parent.getNodeId();
		super.set(pidkey, id);
	}

	/**
	 * 获取应用数据对象的引用。如果节点中的数据对象引用为空，<br/>
	 * 则根据应用对象的类型及ID去加载。<br/>
	 * 
	 * @return
	 */
	public ApplicationEntity getApplicationEntity() {
		if (entity != null) {
			return entity;
		}
		String type = super.getValue(entkey);
		String id = super.getValue(eidkey);
		if (tree != null) {
			entity = tree.context.getApplicationEntityById(type, id);
		}
		return entity;
	}

	/**
	 * 设置节点引用的应用数据对象。
	 * 
	 * @param e
	 */
	public void setApplicationEntity(ApplicationEntity e) {
		if (e == null) {
			return;
		}
		entity = e;
		String type = e.getType();
		String id = Long.toString(e.getId());
		super.set(entkey, type);
		super.set(eidkey, id);
	}

	/**
	 * 重构基类的获值方法，该方法为保存节点对象时使用。<br/>
	 * 由于树的构建是在内存中先完全实现，所以树中节点的信息可能是重新构建。 但该节点所包含的应用数据对象、或者是父节点可能已经存在，但这需要通过
	 * 库操作才知道。所以，树节点中保存了应用对象及父节点的引用，从而保证这 两个对象的ID值最新。因此，在获取这两个值的时候，先要判断引用是否为空
	 * 如果不为空，先引用的对象ID设置到节点对象相应的键，从而达到保证该值最 新的效果。<br/>
	 */
	public <T> T getValue(String key) {
		if (eidkey.equals(key)) {
			if (entity != null) {
				String id = Long.toString(entity.getId());
				super.set(eidkey, id);
			}
		} else if (pidkey.equals(key)) {
			if (parent != null) {
				long id = parent.getNodeId();
				super.set(pidkey, id);
			}
		}
		return super.getValue(key);
	}

	void removeKey(String key) {
		super.remove(key);
	}

	/**
	 * 从Entity对象转换成为TreeNode对象
	 * 
	 * @param entity
	 * @return
	 */
	public static Treenode changeFromEntity(BasicEntity entity) {
		if (entity == null) {
			return null;
		}
		Treenode n = new Treenode(entity.getValue(nidkey).toString());
		for (String key : entity.keyset()) {
			n.set(key, entity.getValue(key));
		}
		return n;
	}

}
