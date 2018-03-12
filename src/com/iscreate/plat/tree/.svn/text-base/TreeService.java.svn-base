package com.iscreate.plat.tree;

import java.util.List;


public interface TreeService {
	
	/**
	 * 根据外部值和树Id获取树的下一级树组织
	 * @param referenceValue
	 * @param treeId
	 * @return
	 */
	public List<TreeNode> getChildrenTreeByReferenceValueAndTreeIdService(String referenceValue,long treeId);
	
	/**
	 * 根据外部值和树Id获取树的上一级树组织
	 * @param referenceValue
	 * @param treeId
	 * @return
	 */
	public TreeNode getParentTreeByReferenceValueAndTreeIdService(String referenceValue,long treeId);
	
	/**
	 * 根据树Id获取树组织
	 * @param treeId
	 * @return
	 */
	public List<TreeNode> getTreeByTreeIdService(long treeId);
	
	/**
	 * 根据树Id获取最高级树
	 * @param treeId
	 * @param parentId
	 * @return
	 */
	public List<TreeNode> getTheTopTreeByTreeIdService(long treeId);
	
	/**
	 * 根据外部值和树Id获取树节点实例
	 * @param referenceValue
	 * @param treeId
	 * @return
	 */
	public TreeNode getTreeNodeByReferenceValueAndTreeIdService(String referenceValue,long treeId);
	
	/**
	 * 保存树组织
	 * @param treeNode
	 */
	public void saveTree(TreeNode treeNode);
	
	/**
	 * 修改树组织
	 * @param treeNode
	 */
	public void updateTree(TreeNode treeNode);
	
	/**
	 * 根据Id删除树组织
	 * @param id
	 */
	public void deleteTree(long id);
	
	/**
	 * 根据外部值和类型获取树的下一级树组织（只限组织架构使用）
	 * @param referenceValue
	 * @param treeId
	 * @return
	 */
	public List<TreeNode> getChildrenTreeByReferenceValueAndReferenceTypeService(String referenceValue,String referenceType);
	
	/**
	 * 根据外部值和类型获取树的上一级树组织（只限组织架构使用）
	 * @param referenceValue
	 * @param treeId
	 * @return
	 */
	public TreeNode getParentTreeByReferenceValueAndReferenceTypeService(String referenceValue,String referenceType);
	
	/**
	 * 根据外部值和类型获取树节点实例（只限组织架构使用）
	 * @param referenceValue
	 * @param treeId
	 * @return
	 */
	public TreeNode getTreeNodeByReferenceValueAndReferenceTypeService(String referenceValue,String referenceType);
	
	/**
	 * 根据树节点获取树
	 * @param treeNodeId
	 * @return
	 */
	public TreeNode getTreeNodeByTreeNodeIdService(long treeNodeId);
	
	/**
	 * 根据树节点获取下一级树
	 * @param treeNodeId
	 * @return
	 */
	public List<TreeNode> getNextTreeNodeByTreeNodeIdService(long treeNodeId);
	
	/**
	 * 根据树节点获取上一级树
	 * @param treeNodeId
	 * @return
	 */
	public TreeNode getUpTreeNodeByTreeNodeIdService(long treeNodeId);
	
	/**
	 * 根据外部类型获取最高级树组织
	 * @param referenceType
	 * @return
	 */
	public List<TreeNode> getTheTopTreeByReferenceTypeService(String referenceType);
	
	/**
	 * 保存树
	 * @param tree
	 */
	public Long saveTreeService(Tree tree);
	
}
