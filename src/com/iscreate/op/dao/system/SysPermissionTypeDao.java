package com.iscreate.op.dao.system;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.system.SysPermissionType;

public interface SysPermissionTypeDao {
	/**
	 * 获取第一级权限类型
	 * 
	 * @return
	 */
	public List<SysPermissionType> getRootPermissionType();

	/**
	 * 获取全部权限类型
	 * 
	 * @return
	 */
	public List<SysPermissionType> getPermissionTypeByParentId(long parentId);
}
