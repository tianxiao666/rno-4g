package com.iscreate.plat.tree;

import java.util.List;


public class TreeServiceImpl implements TreeService{

	private TreeDao treeDao;
	
	/**
	 * 根据外部值和树Id获取树的下一级树组织
	 * @param referenceValue
	 * @param treeId
	 * @return
	 */
	public List<TreeNode> getChildrenTreeByReferenceValueAndTreeIdService(String referenceValue,long treeId){
		List<TreeNode> childrenTreeList = null;
		TreeNode treeNode = this.treeDao.findTreeNodeByReferenceValueAndTreeId(referenceValue, treeId);
		if(treeNode!=null){
			long id = treeNode.getId();
			childrenTreeList = this.treeDao.findTreeByParentId(id);
		}
		return childrenTreeList;
	}
	
	/**
	 * 根据外部值和树Id获取树的上一级树组织
	 * @param referenceValue
	 * @param treeId
	 * @return
	 */
	public TreeNode getParentTreeByReferenceValueAndTreeIdService(String referenceValue,long treeId){
		TreeNode parentTree = null;
		TreeNode treeNode = this.treeDao.findTreeNodeByReferenceValueAndTreeId(referenceValue, treeId);
		if(treeNode!=null){
			long parentId = treeNode.getParentId();
			parentTree = this.treeDao.findTreeNodeById(parentId);
		}
		return parentTree;
	}
	
	/**
	 * 根据树Id获取树组织
	 * @param treeId
	 * @return
	 */
	public List<TreeNode> getTreeByTreeIdService(long treeId){
		return this.treeDao.findTreeNodeByTreeId(treeId);
	}
	
	/**
	 * 根据树Id获取最高级树
	 * @param treeId
	 * @param parentId
	 * @return
	 */
	public List<TreeNode> getTheTopTreeByTreeIdService(long treeId){
		return this.treeDao.findTreeByTreeIdAndParentId(treeId, 0);
	}
	
	/**
	 * 根据外部值和树Id获取树节点实例
	 * @param referenceValue
	 * @param treeId
	 * @return
	 */
	public TreeNode getTreeNodeByReferenceValueAndTreeIdService(String referenceValue,long treeId){
		return this.treeDao.findTreeNodeByReferenceValueAndTreeId(referenceValue, treeId);
	}
	
	/**
	 * 保存树组织
	 * @param treeNode
	 */
	public void saveTree(TreeNode treeNode){
		this.treeDao.saveTreeNode(treeNode);
	}
	
	/**
	 * 修改树组织
	 * @param treeNode
	 */
	public void updateTree(TreeNode treeNode){
		this.treeDao.updateTreeNode(treeNode);
	}
	
	/**
	 * 根据Id删除树组织
	 * @param id
	 */
	public void deleteTree(long id){
		this.treeDao.updateTreeNodeAtStatusById(id);
	}
	
	/**
	 * 根据外部值和类型获取树的下一级树组织（只限组织架构使用）
	 * @param referenceValue
	 * @param treeId
	 * @return
	 */
	public List<TreeNode> getChildrenTreeByReferenceValueAndReferenceTypeService(String referenceValue,String referenceType){
		List<TreeNode> childrenTreeList = null;
		TreeNode treeNode = this.treeDao.findTreeByReferenceValueAndReferenceType(referenceValue, referenceType);
		if(treeNode!=null){
			long id = treeNode.getId();
			childrenTreeList = this.treeDao.findTreeByParentId(id);
		}
		return childrenTreeList;
	}
	
	/**
	 * 根据外部值和类型获取树的上一级树组织（只限组织架构使用）
	 * @param referenceValue
	 * @param treeId
	 * @return
	 */
	public TreeNode getParentTreeByReferenceValueAndReferenceTypeService(String referenceValue,String referenceType){
		TreeNode parentTree = null;
		TreeNode treeNode = this.treeDao.findTreeByReferenceValueAndReferenceType(referenceValue, referenceType);
		if(treeNode!=null){
			long parentId = treeNode.getParentId();
			parentTree = this.treeDao.findTreeNodeById(parentId);
		}
		return parentTree;
	}
	
	/**
	 * 根据外部值和类型获取树节点实例（只限组织架构使用）
	 * @param referenceValue
	 * @param treeId
	 * @return
	 */
	public TreeNode getTreeNodeByReferenceValueAndReferenceTypeService(String referenceValue,String referenceType){
		return this.treeDao.findTreeByReferenceValueAndReferenceType(referenceValue, referenceType);
	}
	
	/**
	 * 根据树节点获取树
	 * @param treeNodeId
	 * @return
	 */
	public TreeNode getTreeNodeByTreeNodeIdService(long treeNodeId){
		return this.treeDao.findTreeNodeById(treeNodeId);
	}
	
	/**
	 * 根据树节点获取下一级树
	 * @param treeNodeId
	 * @return
	 */
	public List<TreeNode> getNextTreeNodeByTreeNodeIdService(long treeNodeId){
		return this.treeDao.findTreeByParentId(treeNodeId);
	}
	
	/**
	 * 根据树节点获取上一级树
	 * @param treeNodeId
	 * @return
	 */
	public TreeNode getUpTreeNodeByTreeNodeIdService(long treeNodeId){
		TreeNode findTreeNodeById = this.treeDao.findTreeNodeById(treeNodeId);
		if(findTreeNodeById==null){
			return null;
		}
		long parentId = findTreeNodeById.getParentId();
		return this.treeDao.findTreeNodeById(parentId);
	}
	
	/**
	 * 根据外部类型获取最高级树组织
	 * @param referenceType
	 * @return
	 */
	public List<TreeNode> getTheTopTreeByReferenceTypeService(String referenceType){
		return this.treeDao.findTheTopTreeNodeByReFerenceType(referenceType);
	}
	
	/**
	 * 保存树
	 * @param tree
	 */
	public Long saveTreeService(Tree tree){
		Long saveTree = this.treeDao.saveTree(tree);
		return saveTree;
	}
	
	public TreeDao getTreeDao() {
		return treeDao;
	}

	public void setTreeDao(TreeDao treeDao) {
		this.treeDao = treeDao;
	}

}
