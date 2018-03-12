package com.iscreate.plat.tree;

import java.util.Date;

public class TreeNode {

	private long id;
	private long treeId;
	private String treeNodeName;
	private String treeNodeDesc;
	private long parentId;
	private String referenceType;
	private String referenceValue;
	private Date updateDate;
	private int status;
	
	public TreeNode(){
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTreeId() {
		return treeId;
	}

	public void setTreeId(long treeId) {
		this.treeId = treeId;
	}

	public String getTreeNodeName() {
		return treeNodeName;
	}

	public void setTreeNodeName(String treeNodeName) {
		this.treeNodeName = treeNodeName;
	}

	public String getTreeNodeDesc() {
		return treeNodeDesc;
	}

	public void setTreeNodeDesc(String treeNodeDesc) {
		this.treeNodeDesc = treeNodeDesc;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}

	public String getReferenceValue() {
		return referenceValue;
	}

	public void setReferenceValue(String referenceValue) {
		this.referenceValue = referenceValue;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
