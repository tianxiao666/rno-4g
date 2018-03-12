package com.iscreate.plat.networkresource.dictionary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.engine.tool.Queue;
import com.iscreate.plat.networkresource.engine.tree.Tree;
import com.iscreate.plat.networkresource.engine.tree.TreeEngine;


public class Dictionary {

	Log log = LogFactory.getLog(getClass());

	private TreeEngine engine = null;

	private final String TREE_NAME = "Dictionary";

	final String ROOT_ENTRY_NAME = "_rootEntry";

	private final String SPLIT_CHARACTOR = ",";

	private DictionaryOperator operator = null;

	public Dictionary(TreeEngine engine) {
		this.engine = engine;
		operator = new DictionaryOperator();
		operator.setContextFactory(engine.getContextFactory());
	}

	/**
	 * 删除目录以及该目录下的所有子目录信息
	 * 
	 * @param dn
	 * @throws EntryOperationException
	 */
	public void removeEntry(String dn) throws EntryOperationException {
		String functionName = "removeEntry(String dn)";
		log.debug("开始执行方法:'" + functionName + "'。");
		if (dn == null || dn.isEmpty()) {
			EntryOperationConsequence con = new EntryOperationConsequence(
					"Entry-Opera-0001", "访问字典的路径'dn'不能为空。");
			log.debug("方法结束。" + con.getCode() + ":" + con.getConsequence());
			throw new EntryOperationException(con);
		}
		String pdn = targetParent(dn);
		String rdn = targetName(dn);
		log.debug("需要查找的目录:'" + dn + "'。");
		log.debug("目标父目录:'" + pdn + "'。");
		log.debug("目标目录:'" + rdn + "'。");
		Tree tree = this.loadEntryTreeByDn(pdn);
		if (tree == null) {
			EntryOperationConsequence con = new EntryOperationConsequence(
					"Entry-Opera-0002", "在获取目标路径的父路径'dn:" + pdn
							+ "'时，该路径的目录不存在。");
			log.debug("方法结束。" + con.getCode() + ":" + con.getConsequence());
			throw new EntryOperationException(con);
		}
		List<ApplicationEntity> children = tree.getChildren(tree.getRoot());
		for (ApplicationEntity child : children) {
			Entry entry = Entry.changeFromEntity(child);
			if (rdn.equals(entry.getValue(Entry.RDN_KEY))) {
				log.debug("准备删除目标目录：'" + rdn + "'");
				tree.delChild(entry, true);
				tree.storeTree();
				return;
			}
		}
		EntryOperationConsequence con = new EntryOperationConsequence(
				"Entry-Opera-0002", "访问字典的路径'dn:" + dn + "'不存在。");
		log.debug("方法结束。" + con.getCode() + ":" + con.getConsequence());
		throw new EntryOperationException(con);
	}

	/**
	 * 将一个数据信息定义到指定的目标目录下。该目录不能事先存在。
	 * 
	 * @param dn
	 * @param entry
	 * @throws EntryOperationException
	 */
	public void addEntry(String dn, BasicEntity entry)
			throws EntryOperationException {
		String functionName = "addEntry(String dn, BasicEntity entry)";
		log.debug("开始执行方法:'" + functionName + "'。");
		if (dn == null || dn.isEmpty()) {
			EntryOperationConsequence con = new EntryOperationConsequence(
					"Entry-Opera-0001", "访问字典的路径'dn'不能为空。");
			log.debug("方法结束。" + con.getCode() + ":" + con.getConsequence());
			throw new EntryOperationException(con);
		}
		String pdn = targetParent(dn);
		String rdn = targetName(dn);
		log.debug("需要查找的目录:'" + dn + "'。");
		log.debug("目标父目录:'" + pdn + "'。");
		log.debug("目标目录:'" + rdn + "'。");
		Tree tree = this.loadEntryTreeByDn(pdn);
		if (tree == null) {
			EntryOperationConsequence con = new EntryOperationConsequence(
					"Entry-Opera-0002", "在获取目标路径的父路径'dn:" + pdn
							+ "'时，该路径的目录不存在。");
			log.debug("方法结束。" + con.getCode() + ":" + con.getConsequence());
			throw new EntryOperationException(con);
		}
		BasicEntity be = operator.getEntryByDn(dn);
		if (be != null) {
			EntryOperationConsequence con = new EntryOperationConsequence(
					"Entry-Opera-0003", "访问字典的路径'dn:" + dn + "'已经存在。");
			log.debug("方法结束。" + con.getCode() + ":" + con.getConsequence());
			throw new EntryOperationException(con);
		}
		// List<ApplicationEntity> children = tree.getChildren(tree.getRoot());
		// log.debug("准备校验父目录'dn:" + pdn + "'下是否已经存在相应的目标目录。父目录下的子目录个数："
		// + (children == null ? 0 : children.size()));
		// if (children != null) {
		// for (ApplicationEntity child : children) {
		// Entry e = Entry.changeFromEntity(child);
		// if (rdn.equals(e.getRdn())) {
		// EntryOperationConsequence con = new EntryOperationConsequence(
		// "Entry-Opera-0003", "访问字典的路径'dn:" + dn + "'已经存在。");
		// log.debug("方法结束。" + con.getCode() + ":"
		// + con.getConsequence());
		// throw new EntryOperationException(con);
		// }
		// }
		// }
		Entry ne = Entry.createEntry(entry);
		ne.setDn(dn);
		ne.setRdn(rdn);
		log.debug("创建一个目标目录：'dn:" + dn + "'。");
		tree.addChild(ne);
		log.debug("准备保存目标目录。");
		tree.storeTree();
		log.debug("方法'" + functionName + "'成功运行");
	}

	/**
	 * 更新目录的存储信息。
	 * 
	 * @param dn
	 * @param entry
	 * @throws EntryOperationException
	 */
	public void replaceEntry(String dn, BasicEntity entry)
			throws EntryOperationException {
		String functionName = "replaceEntry(String dn, BasicEntity entry)";
		log.debug("开始执行方法:'" + functionName + "'。");
		if (dn == null || dn.isEmpty()) {
			EntryOperationConsequence con = new EntryOperationConsequence(
					"Entry-Opera-0001", "访问字典的路径'dn'不能为空。");
			log.debug("方法结束。" + con.getCode() + ":" + con.getConsequence());
			throw new EntryOperationException(con);
		}
		Entry e = this.getEntryByDn(dn);
		if (e == null) {
			EntryOperationConsequence con = new EntryOperationConsequence(
					"Entry-Opera-0001", "访问字典的路径'dn'不能为空。");
			log.debug("方法结束。" + con.getCode() + ":" + con.getConsequence());
			throw new EntryOperationException(con);
		}
		e.setEntryDefine(entry);
		log.debug("查找旧目录，查询条件是'" + Entry.DN_KEY + "=" + dn + "'");
		log.debug("旧目录的数据信息替换为：'" + entry.getValue(Entry.ATTRDEFINE_KEY) + "'");
		operator.updateEntryByDn(dn, e);
		// Context context = engine.getContextFactory().CreateContext();
		// Query q = context.createQueryBuilder(Entry.ENTRYMODULE_NAME);
		// q.add(Restrictions.eq(Entry.DN_KEY, dn));
		// System.out.println("查找旧目录，查询条件是'" + Entry.DN_KEY + "=" + dn + "'");
		// System.out.println("旧目录的数据信息替换为：'" + e.getValue(Entry.ATTRDEFINE_KEY)
		// + "'");
		// context.update(e, q);
	}

	/**
	 * 判断字典路径目录是否存在
	 * @param dn
	 * @return
	 * @throws EntryOperationException
	 */
	public boolean hasEntry(String dn) throws EntryOperationException{
		String functionName = "hasEntry(String dn)";
		boolean isOk = true;
		log.debug("开始执行方法:'" + functionName + "'。");
		if (dn == null || dn.isEmpty()) {
			EntryOperationConsequence con = new EntryOperationConsequence(
					"Entry-Opera-0001", "访问字典的路径'dn'不能为空。");
			log.debug("方法结束。" + con.getCode() + ":" + con.getConsequence());
			throw new EntryOperationException(con);
		}
		
		Tree tree = this.loadEntryTreeByDn(dn);
		if (tree == null) {
			//访问字典的路径不存在
			isOk = false;
		}
		
		return isOk;
	}
	
	public List<BasicEntity> getEntry(String dn, SearchScope scope,
			String filter) throws EntryOperationException {
		if (dn == null) {
			EntryOperationConsequence con = new EntryOperationConsequence(
					"Entry-Opera-0001", "访问字典的路径'dn'不能为空。");
			throw new EntryOperationException(con);
		}
		Tree tree = this.loadEntryTreeByDn(dn);
		if (tree == null) {
			EntryOperationConsequence con = new EntryOperationConsequence(
					"Entry-Opera-0002", "访问字典的路径'dn:" + dn + "'不存在。");
			throw new EntryOperationException(con);
		}
		List<ApplicationEntity> entites = null;
		switch (scope) {
		case OBJECT:
			entites = new ArrayList<ApplicationEntity>();
			entites.add(tree.getRoot());
			break;
		case ONE_LEVEL:
			entites = tree.getChildren(tree.getRoot());
			break;
		default:
			entites = tree.getChildren(tree.getRoot());
			HashSet<String> elementSet = new HashSet<String>();
			Queue<ApplicationEntity> queue = new Queue<ApplicationEntity>();
			if (entites != null) {
				for (ApplicationEntity e : entites) {
					queue.push(e);
					elementSet.add(Long.toString(e.getId()));
				}
				recurrenceSearch(tree, entites, queue, elementSet);
			}
			break;
		}
		List<BasicEntity> result = new ArrayList<BasicEntity>();
		if (entites == null) {
			return result;
		}
		for (ApplicationEntity entry : entites) {
			Entry e = Entry.changeFromEntity(entry);
			if(e!=null){
				result.add(e.getEntryDefine());
			}
		}
		return result;
	}

	private void recurrenceSearch(Tree tree, List<ApplicationEntity> entites,
			Queue<ApplicationEntity> queue, HashSet<String> elementSet) {
		while (queue.hasElements()) {
			ApplicationEntity entity = queue.pop();
			List<ApplicationEntity> es = tree.getChildren(entity);
			if (es != null) {
				for (ApplicationEntity e : es) {
					if (elementSet.contains(e.getId()))
						continue;
					entites.add(e);
					queue.push(e);
					elementSet.add(Long.toString(e.getId()));
				}
			}
		}
	}

	private Tree loadEntryTreeByDn(String dn) {
		Entry entry = getEntryByDn(dn);
		Tree tree = null;
		// 创建根节点
		if (entry == null && dn.isEmpty()) {
			Entry r = Entry.createEntry(new BasicEntity());
			r.setDn(ROOT_ENTRY_NAME);
			r.setRdn(ROOT_ENTRY_NAME);
			tree = engine.createTree(r, TREE_NAME);
		} else {
			if (entry != null) {
				tree = engine.loadTree(entry, TREE_NAME);
			}
		}
		return tree;
	}

	private Entry getEntryByDn(String dn) {
		if (dn.isEmpty()) {
			dn = ROOT_ENTRY_NAME;
		}
		// ContextFactory contextFactory = engine.getContextFactory();
		// Context c = contextFactory.CreateContext();
		// Query q = c.createQueryBuilder(Entry.ENTRYMODULE_NAME);
		// q.add(com.iscreate.dataservice.query.restrictions.Restrictions.eq(
		// Entry.DN_KEY, dn));
		// List<BasicEntity> list = c.queryList(q);
		// if (list == null || list.isEmpty()) {
		// return null;
		// }
		BasicEntity be = operator.getEntryByDn(dn);
		if (be == null) {
			return null;
		}
		Entry e = Entry.changeFromEntity(be);
		return e;
	}

	private String targetName(String dn) {
		if (dn.indexOf(SPLIT_CHARACTOR) > 0) {
			String name = dn.substring(0, dn.indexOf(SPLIT_CHARACTOR));
			return name;
		} else {
			return dn;
		}
	}

	private String targetParent(String dn) {
		if (dn.indexOf(SPLIT_CHARACTOR) > 0) {
			String pdn = dn.substring(dn.indexOf(SPLIT_CHARACTOR) + 1);
			return pdn;
		} else {
			return "";
		}
	}
}
