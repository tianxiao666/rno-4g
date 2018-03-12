package com.iscreate.plat.tree;

import java.util.List;


public interface TreeDao {
	
	/**
	 * 根据外部值和树Id获取树节点
	 * @param referenceValue
	 * @param treeId
	 * @return
	 */
	public TreeNode findTreeNodeByReferenceValueAndTreeId(String referenceValue,long treeId);
	
	/**
	 * 根据树Id获取树节点
	 * @param treeId
	 * @return
	 */
	public List<TreeNode> findTreeNodeByTreeId(long treeId);
	
	/**
	 * 根据树节点Id获取树的实例
	 * @param id
	 * @return
	 */
	public TreeNode findTreeNodeById(long id);

	/**
	 * 根据外部Id和外部类型获取树集合
	 * @param referenceId
	 * @param referenceType
	 * @return
	 */
	public TreeNode findTreeByReferenceValueAndReferenceType(String referenceValue,String referenceType);
	/**
	 * 根据上级节点ID获取树实例
	 * @param parentId
	 * @return
	 */
	public List<TreeNode> findTreeByParentId(long parentId);
	
	/**
	 * 根据节点Id获取树实例
	 * @param treeId
	 * @return
	 */
	public List<TreeNode> findTreeById(long id);
	
	/**
	 * 根据树Id和上级节点Id获取树
	 * @param treeId
	 * @param parentId
	 * @return
	 */
	public List<TreeNode> findTreeByTreeIdAndParentId(long treeId,long parentId);
	
	/**
	 * 根据组织Id获取树节点实例
	 * @param orgId
	 * @return
	 */
	public TreeNode findTreeNodeByReferenceValue(String referenceValue);
	
	/**
	 * 根据外部类型获取树节点
	 * @param referenceType
	 * @return
	 */
	public List<TreeNode> findTreeNodeByReFerenceType(String referenceType);
	
	/**
	 * 保存树组织
	 * @param treeNode
	 */
	public void saveTreeNode(TreeNode treeNode);
	
	/**
	 * 修改树组织
	 * @param treeNode
	 */
	public void updateTreeNode(TreeNode treeNode);
	
	/**
	 * 修改树组织状态为0
	 * @param id
	 */
	public void updateTreeNodeAtStatusById(long id);
	
	/**
	 * 根据外部类型获取最高级树
	 * @param referenceType
	 * @return
	 */
	public List<TreeNode> findTheTopTreeNodeByReFerenceType(String referenceType);
	
	/**
	 * 保存树
	 * @param tree
	 */
	public Long saveTree(Tree tree);
}
