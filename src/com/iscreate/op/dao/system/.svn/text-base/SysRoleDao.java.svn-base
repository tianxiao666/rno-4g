package com.iscreate.op.dao.system;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.system.SysRole;
import com.iscreate.op.pojo.system.SysRole;

public interface SysRoleDao {
	/**
	 * 根据角色类型获取角色列表
	 * @param roleTypeId
	 * @return
	 */
	public List<SysRole> getRolesByRoleTypeCode(long roleTypeId);
	
	/**
	 * 获取全部角色列表
	 * @param roleTypeId
	 * @return
	 */
	public List<SysRole> getAllRole();
	

	/**
	 * 根据系统编码获取对应的角色列表
	 * @author li.hb
	 * @date 2014-1-14 上午9:07:02
	 * @Description: TODO 
	 * @param @param proCode
	 * @param @return        
	 * @throws
	 */
	public List<SysRole> getAllRoleByProCode(String systemCode);
	
	/**
	 * 删除角色
	 * 
	 * @param permissionId
	 * @return
	 */
	public boolean deleteRoleById(final long roleId);
	
	/**
	 * 根据角色类型id获取角色列表
	* @date May 13, 2013 2:52:08 PM
	* @Description: TODO 
	* @param @param roleTypeId
	* @param @return        
	* @throws
	 */
	public List<SysRole> getSysRoleByRoleTypeId(long roleTypeId);
	
	/**
//	 * 根据角色类型编号和用户获取角色
//	 * @return
//	 */
	public List<SysRole> getUserRoles(final String account);
	
	/**
	 * 根据用户获取角色 
	 */
	public List<SysRole> getUserRoles(final long orgUssrId);

	/**
	 * 根据用户和系统获取角色 
	 */
	public List<Map<String,Object>> getSystemsByUserAndSystem(long orgUserId,String systemCode);
	/**
	 * 
	 * @description: 根据权限id删除角色关联的权限关系
	 * @author：yuan.yw
	 * @param permissionId
	 * @return     
	 * @return boolean     
	 * @date：Jan 15, 2014 2:20:02 PM
	 */
	public boolean deleteRoleRelaPermissionByPermissionId(long permissionId);
}
