package com.iscreate.op.pojo.home;


import java.util.Date;

/**
 * HomeItemRelaRole entity. @author MyEclipse Persistence Tools
 */

public class HomeItemRelaRole implements java.io.Serializable {

	private Long homeItemId;
	private Long roleId;
	private Date createtime;
	private Date updatetime;
	private String description;
	private Long itemRow;
	private Long itemColumn;
	private Long itemHeight;
	private Long itemWidth;


	// Constructors

	/** default constructor */
	public HomeItemRelaRole() {
	}


	public Long getHomeItemId() {
		return homeItemId;
	}


	public void setHomeItemId(Long homeItemId) {
		this.homeItemId = homeItemId;
	}


	public Long getRoleId() {
		return roleId;
	}


	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}


	public Date getCreatetime() {
		return createtime;
	}


	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}


	public Date getUpdatetime() {
		return updatetime;
	}


	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Long getItemRow() {
		return itemRow;
	}


	public void setItemRow(Long itemRow) {
		this.itemRow = itemRow;
	}


	public Long getItemColumn() {
		return itemColumn;
	}


	public void setItemColumn(Long itemColumn) {
		this.itemColumn = itemColumn;
	}


	public Long getItemHeight() {
		return itemHeight;
	}


	public void setItemHeight(Long itemHeight) {
		this.itemHeight = itemHeight;
	}


	public Long getItemWidth() {
		return itemWidth;
	}


	public void setItemWidth(Long itemWidth) {
		this.itemWidth = itemWidth;
	}

	/** full constructor */
	

	// Property accessors

	

}