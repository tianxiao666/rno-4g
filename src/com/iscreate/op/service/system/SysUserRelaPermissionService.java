package com.iscreate.op.service.system;

import java.util.List;
import java.util.Map;

public interface SysUserRelaPermissionService {
	/**
	 * @author duhw
	 * @create_time 2014-01-14
	 * 通过用户标识得到用户的权限
	 * permissionType:(菜单权限),(数据权限)
	 * flag:true(得到需要验证的权限),false(所有验的权限)
	 * 注：用户的权限包括用户sys_user_rela_permission表中的权限和sys_role_rela_permission中的权限
	 * * return:所有的权限列表（如果用户有权限，则相应的权限数据flag=1）
	 */
	public List<Map<String,Object>> getPermissionListByUserId(long user_id,String system,String permissionType,boolean flag);
	
	/**
	 * @author ou.jh
	 * @create_time 2014-01-14
	 * 通过用户标识得到一级用户的权限
	 * permissionType:MENU(菜单权限),DATA(数据权限)
	 * flag:true(得到需要验证的权限),false(所有验的权限)
	 * 注：用户的权限包括用户sys_user_rela_permission表中的权限和sys_role_rela_permission中的权限
	 * * return:所有的权限列表（如果用户有权限，则相应的权限数据flag=1）
	 */
	public List<Map<String,Object>> getFirstPermissionListByUserId(long user_id,String system,String permissionType,boolean flag);
	/**
	 * 
	 * @description: 根据资源所属系统编码 资源类型 关联的SERV_TYPE获取系统资源list(关联用户和权限关系表查询 记录判断用户是否已关联资源)
	 * @author：yuan.yw
	 * @param proCode
	 * @param type
	 * @param servType
	 * @param orgUserId
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jan 9, 2014 6:30:57 PM
	 */
	public List<Map<String,Object>> getSysPermissionListByProCodeAndTypeAndServType(String proCode,String type,String servType,long orgUserId);
	/**
	 * 
	 * @description:  （PM）根据资源所属系统编码 资源类型 关联的SERV_TYPE 删除用户关联数据权限
	 * @author：yuan.yw
	 * @return     
	 * @return boolean     
	 * @date：Jan 13, 2014 10:49:06 AM
	 */
	public boolean deleteUserPMDataPermissionByProCodeAndTypeAndServType(String proCode,String type,String servType,long orgUserId);
	/**
	 * 
	 * @description: (pm)保存用户数据范围
	 * @author：yuan.yw
	 * @param orgUserId
	 * @param permissionId
	 * @return     
	 * @return boolean     
	 * @date：Jan 13, 2014 11:08:39 AM
	 */
	public boolean savePMUserRelaData(long orgUserId,long permissionId);
	/**
	 * 
	 * @description: 根据权限id删除用户权限
	 * @author：yuan.yw
	 * @param permissionId
	 * @return     
	 * @return boolean     
	 * @date：Jan 14, 2014 1:47:40 PM
	 */
	public boolean deleteUserRelaPermissionByPermissionId(long permissionId);
	
	/**
	 * 根据用户ID与权限父级ID获取的权限列表
	* @author ou.jh
	* @date Jan 15, 2014 10:11:36 AM
	* @Description: TODO 
	* @param @param orgUserId
	* @param @param parentId
	* @param @return        
	* @throws
	 */
	public List<Map<String,Object>> getSysPermissionListByOrgUserIdAndParentId(long orgUserId , long parentId);
	/**
	 * 
	 * @description: 通过用户id获取用户的权限部门list(PM项目使用)
	 * @author：yuan.yw
	 * @param orgUserId 用户id
	 * @return     部门ORG信息list
	 * @return     List<Map<String,Object>> 
	 * @date：Jan 16, 2014 10:27:21 AM
	 */
	public List<Map<String,Object>> getUserPermissionOrgListByUserId(long orgUserId);
	
	/**
	 * 
	 * @description: 通过部门id 用户id获取用户的权限项目list(PM项目使用)
	 * @author：yuan.yw
	 * @param orgId 部门id  
	 * @param orgUserId 用户id
	 * @return    项目PROJECT信息List 
	 * @return List<Map<String,Object>>     
	 * @date：Jan 16, 2014 10:31:42 AM
	 */
	public List<Map<String,Object>> getUserPermissionProjectListByOrgIdAndUserId(long orgId,long orgUserId);
	/**
	 * 
	 * @description: 通过用户id获取用户的所有权限项目list(PM项目使用)
	 * @author：yuan.yw
	 * @param orgUserId
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jan 17, 2014 3:21:01 PM
	 */
	public List<Map<String,Object>> getUserPermissionProjectListByUserId(long orgUserId);
	/**
	 * 
	 * @description: 通过用户id 系统编码code获取用户权限菜单list
	 * @author：yuan.yw
	 * @param orgUserId 用户id
	 * @param systemCode 系统编码code
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jan 20, 2014 3:56:54 PM
	 */
	public List<Map<String,Object>> getUserPermissionMenuListByUserIdAndSystemCode(long orgUserId,String systemCode);
}
