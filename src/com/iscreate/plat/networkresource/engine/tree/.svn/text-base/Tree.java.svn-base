package com.iscreate.plat.networkresource.engine.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.DefaultParam;
import com.iscreate.plat.networkresource.engine.tool.Queue;

public class Tree {

	Log log = LogFactory.getLog(this.getClass());
	// 数据操作接口
	Context context;
	// 命令列表,一个有序的数组
	private Queue<Command> commands = new Queue<Command>();
	// 树的名称,在创建节点对象时,该名称会传递下去。不同名称的树节点存储在不同的表中。
//	private String name = "";
	private long treeId = 0;
	// 该树/子树的节点列表
	Treenode[] nodes = new Treenode[20];
	// 构建树的帮助对象,该对象内部包含了大部分构建树的实现过程
	TreeHelper helper = null;
	// 该参数记录树节点的个数
	int ns = 0;
	// 该树/子树的根
	Treenode root = null;

	/**
	 * 带参数的构造函数,该参数标识该树的名称
	 * 
	 * @param treeId
	 */
	Tree(long treeId) {
		this.treeId = treeId;
		init();
	}

	/**
	 * 为树设置根值。该方法为内部方法，通过树引擎构建该对象树后，会通过该方法设置树的根
	 * 
	 * @param root
	 */
	void setRoot(ApplicationEntity root) {
		this.entityLegel(root);
		Treenode r = helper.searchOrCreateNodeAndCommand(root);
		this.root = r;
		r.hadLoadChild = true;
	}

	/**
	 * 为树设置根值。该方法为内部方法，通过该树对象获取一个子树对象时使用。
	 * 
	 * @param root
	 */
	void setRoot(Treenode root) {
		this.root = root;
		this.addOneNode(root);
		root.hadLoadChild = true;
	}

	/**
	 * 在根对象下增加一个子对象。
	 * 
	 * @param child
	 * @return
	 */
	public Tree addChild(ApplicationEntity child) {
		String functionName = "addChild(ApplicationEntity child)";
		log.debug("方法'" + functionName + "'开始运行。");
		// 判断参数的有效性
		if (this.treeIllegel())
			return this;
		if (!this.entityLegel(child)) {
			log.debug("参数'child'为空。方法运行结束。");
			return this;
		}
		Treenode cn = helper.searchOrCreateNodeAndCommand(child);
		cn.setParent(root);
		log.debug("方法'" + functionName + "'运行成功。");
		return this;
	}

	/**
	 * 在指定的父对象下增加一个子对象
	 * 
	 * @param parent
	 * @param child
	 * @return
	 */
	public Tree addChild(ApplicationEntity parent, ApplicationEntity child) {
		String functionName = "addChild(ApplicationEntity parent, ApplicationEntity child)";
		log.debug("方法'" + functionName + "'开始运行。");
		// 判断参数的有效性
		if (this.treeIllegel())
			return this;
		if (!this.entityLegel(parent)) {
			log.debug("第一个参数'parent'为空。方法运行结束。");
			return this;
		}
		if (!this.entityLegel(child)) {
			log.debug("第二个参数'child'为空。方法运行结束。");
			return this;
		}
		Treenode pn = helper.searchOrCreateNodeAndCommand(parent);
		Treenode cn = helper.searchOrCreateNodeAndCommand(child);
		if (pn.getParent() == null) {
			pn.setParent(root);
		}
		cn.setParent(pn);
		log.debug("方法'" + functionName + "'运行成功。");
		return this;
	}

	/**
	 * 获取树的根
	 * 
	 * @return
	 */
	public ApplicationEntity getRoot() {
		if (this.root != null) {
			return root.getApplicationEntity();
		}
		return null;
	}

	/**
	 * 删除一个子节点以及其下的所有结构
	 * 
	 * @param node
	 * @return
	 */
	public Tree delChild(ApplicationEntity node) {
		String functionName = "delChild(ApplicationEntity node)";
		log.debug("方法'" + functionName + "'开始运行。");
		// 判断参数的有效性
		if (this.treeIllegel())
			return this;
		if (!this.entityLegel(node)) {
			log.debug("参数'node'为空。方法运行结束。");
			return this;
		}
		log.debug("准备查找该应用数据对象:'" + dissectEntity(node) + "'所对应的节点对象。");
		Treenode n = helper.searchTreenodeOnly(node);
		if (n == null) {
			log.debug("该应用数据对象:'" + dissectEntity(node) + "'并不存在该对象树中。");
			return this;
		}
		log.debug("准备删除节点信息");
		Queue<Treenode> queue = new Queue<Treenode>();
		queue.push(n);
		while (queue.hasElements()) {
			Treenode tn = queue.pop();
			helper.loadOneLevelChildren(tn);
			helper.removeTreenodeAndCommand(tn);
			List<Treenode> children = helper.getChildren(tn);
			for (Treenode child : children) {
				queue.push(child);
			}
		}
		return this;
	}

	/**
	 * 删除一个子节点以及其下的所有结构，同时并相应的数据对象删除
	 * 
	 * @param node
	 * @param physically
	 * @return
	 */
	public Tree delChild(ApplicationEntity node, boolean physically) {
		String functionName = "delChild(ApplicationEntity node, boolean physically)";
		log.debug("方法'" + functionName + "'开始运行。");
		// 判断参数的有效性
		if (this.treeIllegel())
			return this;
		if (!this.entityLegel(node)) {
			log.debug("参数'node'为空。方法运行结束。");
			return this;
		}
		log.debug("准备查找该应用数据对象:'" + dissectEntity(node) + "'所对应的节点对象。");
		Treenode n = helper.searchTreenodeOnly(node);
		if (n == null) {
			log.debug("该应用数据对象:'" + dissectEntity(node) + "'并不存在该对象树中。");
			return this;
		}
		log.debug("准备删除节点信息");
		Queue<Treenode> queue = new Queue<Treenode>();
		queue.push(n);
		while (queue.hasElements()) {
			Treenode tn = queue.pop();
			helper.loadOneLevelChildren(tn);
			List<Treenode> children = helper.getChildren(tn);
			log.debug("先获取删除节点：'" + dissectEntity(tn) + "'的子节点，个数："
					+ children.size());
			for (Treenode child : children) {
				queue.push(child);
			}
			if (physically) {
				helper.removeNodeAndEntityAndCommand(tn);
			} else {
				helper.removeTreenodeAndCommand(tn);
			}
			log.debug("已经删除节点：'" + dissectEntity(tn) + "'。");
		}
		return this;
	}

	/**
	 * 获取指定父对象下的一个子对象
	 * 
	 * @param parent
	 * @return
	 */
	public ApplicationEntity getOneChild(ApplicationEntity parent) {
		String functionName = "getOneChild(ApplicationEntity parent)";
		log.debug("方法'" + functionName + "'开始运行。");
		// 判断参数的有效性
		if (this.treeIllegel())
			return null;
		if (!this.entityLegel(parent)) {
			log.debug("参数'parent'为空。方法运行结束。");
			return null;
		}
		Treenode node = helper.searchTreenodeOnly(parent);
		if (node == null) {
			log.debug("树中没有找到应用数据对象:'" + dissectEntity(parent) + "'对应的节点对象。");
			return null;
		}
		helper.loadOneLevelChildren(node);
		Treenode mynode = helper.getOneChild(node);
		if (mynode == null) {
			log.debug("并没有在树的节点列表中找到父节点:'" + dissectEntity(node) + "'的子节点。");
			return null;
		} else {
			log.debug("已经找到父节点:'" + dissectEntity(node) + "'相应的子节点。");
			log.debug("正在获取子节点:'" + dissectEntity(mynode) + "'所包含的通用对象。");
			ApplicationEntity e = mynode.getApplicationEntity();
			log.debug("方法'" + functionName + "'运行成功。");
			return e;
		}
	}

	/**
	 * 获取父对象下的所有子对象
	 * 
	 * @param parent
	 * @return
	 */
	public List<ApplicationEntity> getChildren(ApplicationEntity parent) {
		String functionName = "getChildren(ApplicationEntity parent)";
		log.debug("方法'" + functionName + "'开始运行。");
		ArrayList<ApplicationEntity> es = new ArrayList<ApplicationEntity>();
		// 判断参数的有效性
		if (this.treeIllegel())
			return es;
		if (!this.entityLegel(parent)) {
			log.debug("参数'parent'为空。方法运行结束。");
			return es;
		}
		Treenode node = helper.searchTreenodeOnly(parent);
		if (node == null) {
			log.debug("树中没有找到应用数据对象:'" + dissectEntity(parent) + "'对应的节点对象。");
			return es;
		}
		helper.loadOneLevelChildren(node);
		List<Treenode> mynodes = helper.getChildren(node);
		log.debug("应用数据对象的子对象个数:" + mynodes.size());
		for (Treenode n : mynodes) {
			ApplicationEntity e = n.getApplicationEntity();
			if (e != null) {
				es.add(e);
			}
		}
		log.debug("方法'" + functionName + "'运行成功。");
		return es;
	}

	public List<ApplicationEntity> getAllChildren() {
		return new ArrayList<ApplicationEntity>();
	}

	public List<ApplicationEntity> searchPath(ApplicationEntity child) {
		return new ArrayList<ApplicationEntity>();
	}

	public ApplicationEntity getParent(ApplicationEntity child) {
		String functionName = "getParent(ApplicationEntity child)";
		log.debug("方法'" + functionName + "'开始运行。");
		// 判断参数的有效性
		if (this.treeIllegel())
			return null;
		if (!this.entityLegel(child)) {
			log.debug("参数'child'为空。方法运行结束。");
			return null;
		}
		Treenode node = helper.searchTreenodeOnly(child);
		if (node == null) {
			log.debug("树中没有找到应用数据对象:'" + dissectEntity(child) + "'对应的节点对象。");
			return null;
		}
		Treenode pn = node.getParent();
		// 新增功能
		if (pn == null) {
			pn = helper.loadParent(node);
		}
		if (pn == null) {
			log.debug("并没有在树的节点列表中找到子节点:'" + dissectEntity(node) + "'的父节点。");
			return null;
		} else {
			log.debug("已经找到子节点:'" + dissectEntity(node) + "'相应的父节点。");
			log.debug("正在获取父节点:'" + dissectEntity(pn) + "'所包含的通用对象。");
			ApplicationEntity e = pn.getApplicationEntity();
			log.debug("方法'" + functionName + "'运行成功。");
			return e;
		}
	}

	public boolean hadChildren(ApplicationEntity parent) {
		// 判断参数的有效性
		if (this.treeIllegel())
			return false;
		if (!this.entityLegel(parent)) {
			log.debug("参数'parent'为空 ，不进行判断。");
			return false;
		}
		Treenode node = helper.searchTreenodeOnly(parent);
		if (node == null) {
			log.debug("树中没有找到应用数据对象:'" + dissectEntity(parent) + "'对应的节点对象。");
			return false;
		}
		helper.loadOneLevelChildren(node);
		Treenode mynode = helper.getOneChild(node);
		if (mynode == null) {
			log.debug("并没有在树的节点列表中找到父节点:'" + dissectEntity(node) + "'的子节点。");
			return false;
		} else {
			log.debug("已经找到父节点:'" + dissectEntity(node) + "'相应的子节点。");
			return true;
		}
	}

	public boolean hadParent(ApplicationEntity child) {
		// 判断参数的有效性
		if (this.treeIllegel())
			return false;
		if (!this.entityLegel(child)) {
			log.debug("参数'parent'为空 ，不进行判断。");
			return false;
		}
		Treenode node = helper.searchTreenodeOnly(child);
		if (node == null) {
			log.debug("树中没有找到应用数据对象:'" + dissectEntity(child) + "'对应的节点对象。");
			return false;
		}
		Treenode parent = node.getParent();
		if (parent == null) {
			log.debug("并没有在树的节点列表中找到子节点:'" + dissectEntity(node) + "'的父节点。");
			return false;
		} else {
			log.debug("已经找到子节点:'" + dissectEntity(node) + "'相应的父节点。");
			return true;
		}
	}

	public Tree getSubTree(ApplicationEntity node) {
		String functionName = "getSubTree(ApplicationEntity node)";
		log.debug("方法'" + functionName + "'开始运行。");
		// 判断参数的有效性
		if (this.treeIllegel())
			return this;
		if (!this.entityLegel(node)) {
			log.debug("参数'node'为空。方法运行结束。");
			return this;
		}
		log.debug("准备查找该应用数据对象:'" + dissectEntity(node) + "'所对应的节点对象。");
		Treenode n = helper.searchTreenodeOnly(node);
		if (n == null) {
			log.debug("该应用数据对象:'" + dissectEntity(node) + "'并不存在该对象树中。");
			return this;
		}
		Queue<Treenode> queue = new Queue<Treenode>();
		queue.push(n);
		Tree t = new Tree(treeId);
		t.root = n;
		t.context = this.context;
		while (queue.hasElements()) {
			Treenode tn = queue.pop();
			t.addOneNode(tn);
			List<Treenode> children = helper.getChildren(tn);
			for (Treenode child : children) {
				queue.push(child);
			}
		}
		return t;
	}

	/**
	 * 将整个树结构持久化
	 */
	public void storeTree() {
		while (this.commands.hasElements()) {
			Command command = this.commands.pop();
			log.debug("开始执行命令：" + command.getClass().getSimpleName() + ";执行对象："
					+ dissectEntity(command.entity));
			command.execute();
		}
	}

	/**
	 * 删除该树根点以及以下所有的子节点
	 */
	public void dropTree() {
		String functionName = "dropTree()";
		log.debug("方法'" + functionName + "'开始运行。");
		// 判断参数的有效性
		if (this.treeIllegel())
			return;
		log.debug("准备删除节点信息");
		Queue<Treenode> queue = new Queue<Treenode>();
		queue.push(root);
		while (queue.hasElements()) {
			Treenode tn = queue.pop();
			helper.removeTreenodeAndCommand(tn);
			List<Treenode> children = helper.getChildren(tn);
			for (Treenode child : children) {
				queue.push(child);
			}
		}
		log.debug("方法'" + functionName + "'执行成功。");
	}

	/**
	 * 为树增加点的方法，该方法不对包外的类开放，仅为内部构建树使用
	 * 
	 * @param node
	 */
	void addOneNode(Treenode node) {
		if (this.nodes.length == this.ns) {
			Treenode[] ns = new Treenode[this.ns * 2];
			System.arraycopy(nodes, 0, ns, 0, this.ns);
			nodes = ns;
		}
		node.tree = this;
		nodes[this.ns] = node;
		this.ns++;
		log.debug("已经为树中增加节点。当前树节点个数是：" + this.ns);
	}

	private void removeOneNode(Treenode node) {
		int index = -1;
		for (int i = 0; i < this.ns; i++) {
			if (nodes[i] == node) {
				index = i;
			}
		}
		if (this.ns == 1) {
			nodes[index] = null;
		} else {
			nodes[index] = nodes[this.ns - 1];
			nodes[this.ns - 1] = null;
		}
		this.ns--;
		log.debug("已经从树中删除节点。当前树节点个数是：" + this.ns);
	}

	/**
	 * 判断通用方法中传入的对象是否合法
	 * 
	 * @param entity
	 * @return
	 */
	private boolean entityLegel(ApplicationEntity entity) {
		if (entity == null) {
			return false;
		}
		String id = Long.toString(entity.getId());
		if (id == null || id.isEmpty()) {
			entity.setId(-1);
		}
		return true;
	}

	/**
	 * 通过判断该树的根是否为空来判断当前树是否有效。如果该树无效，树所提供的所有方法都无效。
	 * 
	 * @return
	 */
	private boolean treeIllegel() {
		if (this.root != null) {
			return false;
		}
		log.debug("该树对象中没有根。");
		return true;
	}

	/**
	 * 将传入的应用数据对象转换成一个可以标识它的字符串
	 * 
	 * @param e
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

	/**
	 * 将传入的节点对象转换成一个可以标识它的字符串。
	 * 
	 * @param entity
	 * @return
	 */
	private String dissectEntity(Treenode entity) {
		if (entity == null) {
			return "null";
		} else {
			String info = entity.getValue(DefaultParam.typeKey) + "."
					+ entity.getNodeId();
			return info;
		}
	}

	private String dissectEntity(BasicEntity entity) {
		if (entity instanceof ApplicationEntity) {
			ApplicationEntity e = (ApplicationEntity) entity;
			return dissectEntity(e);
		}
		if (entity instanceof Treenode) {
			Treenode node = (Treenode) entity;
			return dissectEntity(node);
		}
		return "unexpect entity";
	}

	/**
	 * 初始化一个树对象中的内部参数
	 */
	private void init() {
		if (helper == null)
			helper = new TreeHelper();
		this.nodes = new Treenode[20];
		this.ns = 0;
		this.commands.clear();
	}

	/**
	 * 命令工厂方法，根据命令的类型来生产出相应的处理命令
	 * 
	 * @param type
	 * @param e
	 */
	private Command createCommand(CommandType type, BasicEntity e) {
		Command com = null;
		switch (type) {
		case SAVE_NODE:
			com = new CommandSaveNode();
			break;
		case SAVE_ENTITY:
			com = new CommandSaveApplicationEntity();
			break;
		case DELETE_NODE:
			com = new CommandDeleteNode();
			break;
		default:
			com = new CommandDeleteApplicationEntity();
			break;
		}
		com.setExecuteTag(context, e);
		return com;
	}

	/**
	 * 创建一个树节点对象
	 * 
	 * @param entity
	 * @return
	 */
	private Treenode createTreenode(ApplicationEntity entity) {
		Treenode node = new Treenode(treeId);
		node.setApplicationEntity(entity);
		node.tree = this;
		return node;
	}

	/**
	 * 维护树的内部类。
	 * 
	 * @author joe
	 * 
	 */
	class TreeHelper {
		/**
		 * 为对象树增加一个节点的方法。增加过程：<br/>
		 * 先判断应用数据对象是否已经存在对象树的节点中。<br/>
		 * 如果存在，直接返回该节点对象。<br/>
		 * <br/>
		 * 如果不存在，生成一个保存应用数据对象的命令，加入命令列表<br/>
		 * 创建一个新的节点对象，该节点对象包含了应用数据对象的引用。<br/>
		 * 生成一个保存节点对象的命令，加入命令列表。<br/>
		 * 将该节点对象加入树的节点列表中。<br/>
		 * 返回该节点对象。<br/>
		 * 
		 * @param entity
		 * @return
		 */
		public Treenode searchOrCreateNodeAndCommand(ApplicationEntity entity) {
			// 判断该应用数据对象是否已经存在树中
			log.debug("正在判断该应用数据对象:'" + dissectEntity(entity)
					+ "'是否已经存在树的节点列表中。");
			Treenode node = null;
			for (int i = 0; i < ns; i++) {
				// if (entity.equal(nodes[i].getApplicationEntity())) {
				if (compareToEntity(entity, nodes[i].getApplicationEntity())) {
					node = nodes[i];
				}
			}
			if (node != null) {
				log.debug("在树的节点列表中找到该应用数据对象:'" + dissectEntity(entity) + "'。");
				return node;
			}
			log.debug("应用数据对象不存在节点列表中,准备生成一个保存应用数据对象的命令。");
			Command com = createCommand(CommandType.SAVE_ENTITY, entity);
			commands.push(com);
			log.debug("准备创建一个包含该数据应用对象引用的树节点，并生成相应的保存命令。");
			node = createTreenode(entity);
			com = createCommand(CommandType.SAVE_NODE, node);
			commands.push(com);
			log.debug("准备将该节点对象加入树的节点列表中。");
			addOneNode(node);
			return node;
		}

		/**
		 * 从对象树的节点中直接查找包含了该应用数据对象引用的节点。
		 * 
		 * @param entity
		 * @return
		 */
		public Treenode searchTreenodeOnly(ApplicationEntity entity) {
			log.debug("准备获取应用对象'" + dissectEntity(entity) + "'对应的节点对象");
			Treenode node = null;
			for (int i = 0; i < ns; i++) {
				if (compareToEntity(entity, nodes[i].getApplicationEntity())) {
					node = nodes[i];
					return node;
				}
			}
			return node;
		}

		/**
		 * 从树中删除节点，并生成删除命令
		 * 
		 * @param node
		 */
		public void removeTreenodeAndCommand(Treenode node) {
			log.debug("正在准备为节点:'" + dissectEntity(node) + "'生成删除命令。");
			Command command = createCommand(CommandType.DELETE_NODE, node);
			commands.push(command);
			removeOneNode(node);
		}

		/**
		 * 从树中删除节点，并生成删除命令
		 * 
		 * @param node
		 */
		public void removeNodeAndEntityAndCommand(Treenode node) {
			log.debug("正在准备为节点:'" + dissectEntity(node) + "'生成删除命令。");
			Command command = createCommand(CommandType.DELETE_NODE, node);
			commands.push(command);
			command = createCommand(CommandType.DELETE_ENTITY,
					node.getApplicationEntity());
			commands.push(command);
			removeOneNode(node);
		}

		/**
		 * 在对象树的节点列表中根据父节点信息查找所有子节点对象。判断子节点的父对象是否与指定父对象相同。
		 * 
		 * @param parent
		 * @return
		 */
		public List<Treenode> getChildren(Treenode parent) {
			log.debug("准备开始查找父节点:'" + dissectEntity(parent) + "'的子对象列表。");
			ArrayList<Treenode> myns = new ArrayList<Treenode>();
			for (int i = 0; i < ns; i++) {
				Treenode n = nodes[i];
				if (n.getParent() != null
						&& compareToEntity(n.getParent(), parent)) {
					myns.add(n);
				}
			}
			return myns;
		}

		/**
		 * 在对象树的节点列表中根据父节点信息查找所有子节点对象。
		 * 
		 * @param parent
		 * @return
		 */
		public Treenode getOneChild(Treenode parent) {
			log.debug("准备开始查找父节点:'" + dissectEntity(parent) + "'的子对象列表。");
			for (int i = 0; i < ns; i++) {
				Treenode n = nodes[i];
				if (n.getParent() != null
						&& compareToEntity(n.getParent(), parent)) {
					return n;
				}
			}
			return null;
		}

		/**
		 * 根据模板定义，比较两个应用对象是否相同。比较这两个应用对象中所有定义为primary键的值，<br/>
		 * 如果它们都相同，则认为这两个应用对象相同，否则认为相异 。
		 * 
		 * @param left
		 * @param right
		 * @return
		 */
		public boolean compareToEntity(ApplicationEntity left,
				ApplicationEntity right) {
			if (left == null || right == null) {
				return false;
			}
			if (!left.getType().equals(right.getType())) {
				return false;
			}
			log.debug("开始比较对象：'" + left + "'与对象：'" + right + "'");
			for (String key : left.primaryKeys()) {
				Object tv = left.getValue(key);
				Object ev = right.getValue(key);
				log.debug("比较的键：'" + key + "'，两个值分别是:'" + tv + "'与'" + ev
						+ "'。");
				if (tv == null || ev == null) {
					log.debug("值为空，比较的结果是'false'。");
					return false;
				}
				if (!tv.equals(ev)) {
					log.debug("两个值不相等，比较的结果是'false'。");
					return false;
				}
			}
			log.debug("结束比较对象：'" + left + "'与对象：'" + right + "'");
			return true;
		}

		/**
		 * 通过比较两个节点中的应用对象引用来判断两个节点是否相同
		 * 
		 * @param left
		 * @param right
		 * @return
		 */
		public boolean compareToEntity(Treenode left, Treenode right) {
			// log.debug("正在比较节点:'" + dissectEntity(left) + "'与节点:'"
			// + dissectEntity(right) + "'");
			if (left == null || right == null) {
				return false;
			}
			if (compareToEntity(left.getApplicationEntity(),
					right.getApplicationEntity()))
				return true;
			return false;
		}

		/**
		 * 通过父节点的hadLoadChild参数去判断是否已经加载了该节点的子节点信息到内存。
		 * 
		 * @param parent
		 */
		public void loadOneLevelChildren(Treenode parent) {
			if (parent.hadLoadChild) {
				return;
			}
			log.debug("开始去库中加载父节点'" + dissectEntity(parent) + "'的子节点对象。");
			HashMap<String, Object> condition = new HashMap<String, Object>();
			String type = Treenode.class.getSimpleName();
			condition.clear();
			condition.put(Treenode.treeidkey, treeId);
			condition.put(Treenode.pidkey, parent.getNodeId());
			List<Treenode> ns = context.getTreenodes(type, condition);
			for (Treenode cn : ns) {
				addOneNode(cn);
			}
			// 标记该节点的子节点已经加载到内存。
			parent.hadLoadChild = true;
		}
		public Treenode loadParent(Treenode child) {
			HashMap<String, Object> condition = new HashMap<String, Object>();
			String type = Treenode.class.getSimpleName();
			condition.put(Treenode.treeidkey, treeId);
			condition.put(Treenode.nidkey, child.getValue(Treenode.pidkey)+"");
			List<Treenode> ns = context.getTreenodes(type, condition);
			if (ns != null && !ns.isEmpty()) {
				addOneNode(ns.get(0));
				return ns.get(0);
			}
			return null;
		}
	}

	/**
	 * 打印树内部的节点信息。
	 */
	public void dump() {
		log.debug("当前树共有节点个数：" + this.ns + ",节点信息如下");
		for (int i = 0; i < this.ns; i++) {
			log.debug("'节点" + (i + 1) + "'信息:");
			Treenode n = nodes[i];
			if (n != null) {
				for (String key : n.keyset()) {
					log.debug("----" + key + ":" + n.getValue(key));
				}
				ApplicationEntity e = n.getApplicationEntity();
				log.debug("--节点包含的应用对象信息:");
				if (e == null) {
					log.debug("------节点的应用对象为空");
					continue;
				}
				for (String key : e.keyset()) {
					log.debug("------" + key + ":" + e.getValue(key));
				}
			} else {
				log.debug("----该节点是空节点。");
			}
		}
	}
}
