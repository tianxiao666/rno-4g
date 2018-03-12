package com.iscreate.op.dao.system;

import java.util.List;

import com.iscreate.op.pojo.system.SysRoleType;

public interface SysRoleTypeDao {
	/**
	 * 获取全部角色类型
	 * @return
	 */
	public List<SysRoleType> getAllRoleType();
}
