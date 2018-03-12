package com.iscreate.op.service.system;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.system.SysPermission;
import com.iscreate.op.pojo.system.SysPermission;
import com.iscreate.op.pojo.system.SysPermissionType;
import com.iscreate.op.pojo.system.SysRoleRelaPermission;

public interface SysPermissionService {

	boolean txCanAccessResource(String account, String uri);

	/**
	 * 获取权限树集合
	 * 
	 * @return List<Map>
	 */
	public List<Map<String, Object>> getPermissionTree();
	
	/**
	 * 根据角色获取权限树集合（整棵权限树获取，标识角色对每个权限的权限）
	 * 
	 * @return List<Map>
	 */
	public List<Map<String, Object>> getPermissionTreeByRole(long roleId);
	
	/**
	 * 根据用户和角色获取权限树集合（整棵权限树获取，标识用户对每个权限的权限）
	 * 
	 * @return List<Map>
	 */
	public List<Map<String, Object>> getPermissionTreeByUser(long orgUserId,String roleids);

	/**
	 * 根据角色获取相关权限资源
	 * 
	 * @param roleId
	 * @return
	 */
	public List<SysPermission> getPermissionByRole(final long roleId);

	/**
	 * 根据角色 获取权限资源对象和角色资源关联对象(SysPermission,SysRoleRelaPermission)
	 * 
	 * @param roleId
	 * @return
	 */
	public List<Map<String, Object>> getPermissionAndRoleRelaPermissionMapByRole(
			long roleId);

	
	
	/**
	 * 
	 * @description: 添加权限
	 * @author：yuan.yw
	 * @param permission     
	 * @return long     
	 * @date：Jan 14, 2014 2:01:34 PM
	 */
	public long txAddPermission(SysPermission permission);

	/**
	 * 
	 * @description: 修改权限
	 * @author：yuan.yw
	 * @param permission     
	 * @return void     
	 * @date：Jan 14, 2014 1:56:45 PM
	 */
	public void txUpdatePermission(SysPermission permission);
	
	/**
	 * 根据Id删除权限
	 */
	public void txDeletePermissionById(long id);
	

	
	/**
	 * 
	 * @description: 删除权限实体
	 * @author：yuan.yw
	 * @param permission     
	 * @return void     
	 * @date：Jan 14, 2014 1:45:39 PM
	 */
	public void txDeletepermission(SysPermission permission);

	/**
	 * 根据Id获取权限
	 * 
	 * @param id
	 * @return
	 */
	public SysPermission getPermissionById(long id);
	/**
	 * 
	 * @description: 根据Id获取权限
	 * @author：yuan.yw
	 * @param id
	 * @return     
	 * @return SysPermissionPmdev     
	 * @date：Jan 14, 2014 1:42:55 PM
	 */
	public SysPermission getPermissionPmdevById(long id);

	/**
	 * 获取全部权限类型
	 * 
	 * @return
	 */
	public List<SysPermissionType> getPermissionTypeByParentId(long parentId);
	
	/**
	 * 保存角色与权限关联
	 * @param roleRelaPermission
	 */
	public void txAddRoleRelaPermission(SysRoleRelaPermission roleRelaPermission);
	
	/**
	 * 获取第一级权限类型
	 * 
	 * @return
	 */
	public List<SysPermissionType> getRootPermissionType();
	/**
	 * @author duhw
	 * @create_time 2013-06-06
	 * 通过用户账号标识得到用户的权限（权限通过flag(0,1)标识用户是否有权限）
	 */
	public List<Map<String,Object>> getPermissionListByAccountId(long account_id);
	
	
	/**
	 * 删除权限与角色关联关系
	* @author ou.jh
	* @date Jun 19, 2013 1:56:50 PM
	* @Description: TODO 
	* @param @param permissionId
	* @param @param roleId
	* @param @return        
	* @throws
	 */
	public boolean deleteRoleRelaPermissionById(long permissionId,long roleId);
	
	/**
	 * 删除角色下所有的权限关联关系
	 * @author li.hb
	 * @date 2014-1-13 下午3:15:59
	 * @Description: TODO 
	 * @param @param roleId
	 * @param @return        
	 * @throws
	 */
	public boolean deleteRoleRelaPermissionByRoleId(long roleId);
	
	/**
	 * 根据账号获取模块(PM项目管理)
	* @author ou.jh
	* @date Jun 7, 2013 2:15:42 PM
	* @Description: TODO 
	* @param @param account        
	* @throws
	 */
	public List<Map<String, Object>> getPMPermissionModuleByAccount(long org_user_id);
	
	/**
	 * 根据多个角色获取权限树集合（整棵权限树获取，标识角色对每个权限的权限）  -new
	 * 
	 * @return List<Map>
	 */
	public List<Map<String, Object>> getPermissionTreeByRoles(String roleids);
	
	
	/**
	 * 更新用户权限
	 */
	public int updateUserPermission(long orgUserId,String pIds);
	
	/**
	 * 根据角色获取权限
	 */
	public String getPermissionIdsByRoleIds(String roleids);
	/**
	 * 
	 * @description: 根据项目编码 类型获取权限list
	 * @author：yuan.yw
	 * @param proCode
	 * @param type
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jan 14, 2014 10:42:58 AM
	 */
	public List<Map<String, Object>> getPermissionListByProCodeAndType(String proCode,String type);
	/**
	 * 
	 * @description: 根据权限id删除本权限及子权限
	 * @author：yuan.yw
	 * @param permissionId
	 * @return     
	 * @return boolean     
	 * @date：Jan 14, 2014 4:15:55 PM
	 */
	public boolean detletSelfAndChildPermissionByPermissionId(long permissionId);
}
