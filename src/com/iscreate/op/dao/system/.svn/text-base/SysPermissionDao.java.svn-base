package com.iscreate.op.dao.system;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.system.SysPermission;

public interface SysPermissionDao {
	/**
	 * 获取第一级权限
	 * 
	 * @return
	 */
	public List<SysPermission> getRootPermission();

	/**
	 * 根据业务模块获取其权限树
	 */
	public List<Map<String, Object>> getPermissionTreeByType();
	
	/**
	 * 根据角色获取其权限树
	 */
	public List<Map<String, Object>> getPermissionTreeByRole(final long roleId);

	/**
	 * 根据多个角色获取其权限树
	 */
	public List<Map<String, Object>> getPermissionTreeByRoles(final String roleIds);
	
	/**
	 * 根据用户获取其菜单权限树 -new
	 */
	public List<Map<String, Object>> getPermissionTreeByUserId(final long orgUserId);
	/**
	 * 根据角色获取权限资源
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
	 * 删除权限
	 * 
	 * @param permissionId
	 * @return
	 */
	public boolean deletePermissionById(final long permissionId);
	
	/**
	 * @author du.hw
	 * @create_time 2013-06-06
	 * 根据用户账号标识得到用户权限（返回的是所有需要控制的权限列表，当前用户拥有的权限，其权限数据中标识flag为1,否则为0）
	 *注：此处得到是所有的可用权限（即：需要验证的权限）
	 */
	public List<Map<String,Object>> getAllEnablePermissionByAccountId(long account_id);

	
	
	/**
	 * 根据父ID与账号获取模块
	* @author ou.jh
	* @date Jun 7, 2013 2:58:43 PM
	* @Description: TODO 
	* @param @param parent_id
	* @param @param account
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getPermissionByParentIdAndAccount(long parent_id,String account);
	
	/**
	 * 根据父ID与账号获取个人工作空间
	* @author ou.jh
	* @date Jun 7, 2013 2:58:43 PM
	* @Description: TODO 
	* @param @param parent_id
	* @param @param account
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getPermissionWorkPlatByAccount(String account);
	
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
	public boolean deleteRoleRelaPermissionById(final long permissionId,final long roleId);
	
	/**
	 * 根据角色ID删除角色与权限关联关系
	 * @author li.hb
	 * @date 2014-1-13 下午3:17:33
	 * @Description: TODO 
	 * @param @param roleId
	 * @param @return        
	 * @throws
	 */
	public boolean deleteRoleRelaPermissionByRoleId(final long roleId);
	
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
	
		/**
		 * 
		 * @description: 根据角色获取权限
		 * @author：zhang.wy1
		 * @param roleids
		 * @return
		 * @return: String          
		 * @date：2014-1-9 下午4:32:21
		 */
	public String getPermissionIdsByRoleIds(String roleids);
	

	/**
	 * 
	 * @description: 删除用户权限
	 * @author：zhang.wy1
	 * @param orgUserId
	 * @return
	 * @return: int          
	 * @date：2014-1-15 下午12:53:21
	 */
   public int deleteUserPermission(final long orgUserId);
	/**
	 * 
	 * @description: 添加用户权限
	 * @author：zhang.wy1
	 * @param orgUserId
	 * @param pid
	 * @return
	 * @return: int          
	 * @date：2014-1-15 下午12:54:21
	 */
   public int addUserPermission(final long orgUserId,final String pid);
}
