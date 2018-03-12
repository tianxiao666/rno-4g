package com.iscreate.plat.tree;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateTemplate;


public class TreeDaoImpl implements TreeDao{
	
	private HibernateTemplate hibernateTemplate;
	private SessionFactory sessionFactory;
	
	/**
	 * 根据外部值和树Id获取树节点
	 * @param referenceValue
	 * @param treeId
	 * @return
	 */
	public TreeNode findTreeNodeByReferenceValueAndTreeId(String referenceValue,long treeId){
		String hql = "from TreeNode t where t.referenceValue='"+referenceValue+"' and t.treeId="+treeId+" and t.status=1";
		List<TreeNode> list = hibernateTemplate.find(hql);
		TreeNode treeNode = null;
		if(list!=null && list.size()>0){
			treeNode = list.get(0);
		}
		return treeNode;
	}
	
	/**
	 * 根据树Id获取树节点
	 * @param treeId
	 * @return
	 */
	public List<TreeNode> findTreeNodeByTreeId(long treeId){
		String hql = "from TreeNode t where t.treeId="+treeId+" and t.status=1";
		return hibernateTemplate.find(hql);
	}
	
	/**
	 * 根据树节点Id获取树的实例
	 * @param id
	 * @return
	 */
	public TreeNode findTreeNodeById(long id){
		String hql = "from TreeNode t where t.id="+id+" and t.status=1";
		List<TreeNode> list = hibernateTemplate.find(hql);
		TreeNode treeNode = null;
		if(list!=null && list.size()>0){
			treeNode = list.get(0);
		}
		return treeNode;
	}
	
	/**
	 * 根据上级节点ID获取树实例
	 * @param parentId
	 * @return
	 */
	public List<TreeNode> findTreeByParentId(long parentId){
		String hql = "from TreeNode t where t.parentId="+parentId+" and t.status=1";
		return (List<TreeNode>)hibernateTemplate.find(hql);
	}
	
	/**
	 * 根据树Id和上级节点Id获取树
	 * @param treeId
	 * @param parentId
	 * @return
	 */
	public List<TreeNode> findTreeByTreeIdAndParentId(long treeId,long parentId){
		String hql = "from TreeNode t where t.treeId="+treeId+" and t.parentId="+parentId+" and t.status=1";
		return (List<TreeNode>)hibernateTemplate.find(hql);
	}
	
	
	/**
	 * 根据外部类型获取最高级树
	 * @param referenceType
	 * @return
	 */
	public List<TreeNode> findTheTopTreeNodeByReFerenceType(String referenceType){
		String hql = "from TreeNode t where t.referenceType='"+referenceType+"' and t.parentId="+0+" and t.status=1";
		return (List<TreeNode>)hibernateTemplate.find(hql);
	}
	
	
	
	
	
	//===================================
	
	/**
	 * 根据外部Id和外部类型获取树集合
	 * @param referenceId
	 * @param referenceType
	 * @return
	 */
	public TreeNode findTreeByReferenceValueAndReferenceType(String referenceValue,String referenceType){
		String hql = "from TreeNode t where t.referenceValue='"+referenceValue+"' and t.referenceType='"+referenceType+"' and t.status=1";
		List<TreeNode> list = (List<TreeNode>)hibernateTemplate.find(hql);
		TreeNode treeNode = null;
		if(list!=null && list.size()>0){
			treeNode = list.get(0);
		}
		return treeNode;
	}

	
	
	/**
	 * 根据节点Id获取树实例
	 * @param treeId
	 * @return
	 */
	public List<TreeNode> findTreeById(long id){
		String hql = "from TreeNode t where t.id="+id+" and t.status=1";
		return (List<TreeNode>)hibernateTemplate.find(hql);
	}
	
	
	
	/**
	 * 根据组织Id获取树节点实例
	 * @param orgId
	 * @return
	 */
	public TreeNode findTreeNodeByReferenceValue(String referenceValue){
		String hql = "from TreeNode t where t.referenceValue='"+referenceValue+"' and t.status=1";
		List<TreeNode> list = (List<TreeNode>)hibernateTemplate.find(hql);
		TreeNode t = null;
		if(list!=null && list.size()>0){
			t = list.get(0);
		}
		return t;
	}
	
	/**
	 * 根据外部类型获取树节点
	 * @param referenceType
	 * @return
	 */
	public List<TreeNode> findTreeNodeByReFerenceType(String referenceType){
		String hql = "from TreeNode t where t.referenceType='"+referenceType+"' and t.status=1";
		return (List<TreeNode>)hibernateTemplate.find(hql);
	}
	
	/**
	 * 保存树组织
	 * @param treeNode
	 */
	public void saveTreeNode(TreeNode treeNode){
		hibernateTemplate.save(treeNode);
	}
	
	/**
	 * 修改树组织
	 * @param treeNode
	 */
	public void updateTreeNode(TreeNode treeNode){
		hibernateTemplate.update(treeNode);
	}
	
	/**
	 * 修改树组织状态为0
	 * @param id
	 */
	public void updateTreeNodeAtStatusById(long id){
		List<TreeNode> findTreeById = this.findTreeById(id);
		if(findTreeById!=null && findTreeById.size()>0){
			TreeNode treeNode = findTreeById.get(0);
			treeNode.setStatus(0);
			treeNode.setUpdateDate(new Date());
			hibernateTemplate.update(treeNode);
		}
	}
	
	/**
	 * 保存树
	 * @param tree
	 */
	public Long saveTree(Tree tree){
		Serializable save = this.hibernateTemplate.save(tree);
		long parseLong = Long.parseLong(save.toString());
		return parseLong;
	}
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	
}
