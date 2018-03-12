package com.iscreate.op.service.system;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.system.SysRole;
import com.iscreate.op.pojo.system.SysRole;
import com.iscreate.op.pojo.system.SysRoleType;

public interface SysRoleService {
	/**
	 * 根据角色类型获取角色列表
	 * @param roleTypeId
	 * @return
	 */
	public List<SysRole> getRolesByRoleTypeCode(long roleType);
	
	/**
	 * 获取全部角色
	 * @param roleTypeId
	 * @return
	 */
	public List<SysRole> getAllRole();
	
	/**
	 * 获取全部角色类型
	 * @param roleTypeId
	 * @return
	 */
	public List<SysRoleType> getAllRoleType();
	
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
	 * 保存角色
	 * @param sysRole
	 */
	public void txAddRole(SysRole sysRole);
	/**
	 * 根据Id删除角色
	 * @param roleId
	 */
	public void txDeleteRoleById(long roleId);
	
	/**
	 * 根据Id获取角色
	 * @param roleId
	 * @return
	 */
	public SysRole getRoleById(long roleId);
	
	/**
	 * 修改角色
	 * @param sysRole
	 */
	public void txUpdateRole(SysRole sysRole);

	/**
	 * 根据用户获取角色 
	 */
	public List<SysRole> getUserRoles(final String account);

	/**
	 * 根据用户获取角色 
	 */
	public List<SysRole> getUserRoles(final long orgUserId);
	
	/**
	 * 根据用户和系统获取角色 
	 */
	public List<Map<String,Object>>  getSystemsByUserAndSystem(long orgUserId,String systemCode); 
	/**
	 * @author du.hw
	 * @create 2014-01-14
	 * 根据用户标识得到用户角色
	 * return:所有的角色（通过flag标识出用户是否有角色） 
	 */
	public List<Map<String,Object>> getUserRolesByUserId(long user_id,String system);

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

	/**
	 * 更新用户角色 
	 */
	public int updateUserRole(long orgUserId, String roleIds);

}
