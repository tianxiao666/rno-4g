package com.iscreate.op.service.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.op.dao.common.CommonDao;
import com.iscreate.op.dao.system.SysPermissionDao;
import com.iscreate.op.dao.system.SysPermissionTypeDao;
import com.iscreate.op.dao.system.SysRoleRelaPermissionDao;
import com.iscreate.op.dao.system.SysUserRelaPermissionDao;
import com.iscreate.op.pojo.system.SysPermission;
import com.iscreate.op.pojo.system.SysPermission;
import com.iscreate.op.pojo.system.SysPermissionType;
import com.iscreate.op.pojo.system.SysRoleRelaPermission;

public class SysPermissionServiceImpl  implements SysPermissionService {
	private SysRoleRelaPermissionDao sysRoleRelaPermissionDao;
	private SysPermissionDao sysPermissionDao;
	private SysPermissionTypeDao sysPermissionTypeDao;
	private SysUserRelaPermissionDao sysUserRelaPermissionDao;
	private CommonDao commonDao;
   
	public SysUserRelaPermissionDao getSysUserRelaPermissionDao() {
		return sysUserRelaPermissionDao;
	}

	public void setSysUserRelaPermissionDao(
			SysUserRelaPermissionDao sysUserRelaPermissionDao) {
		this.sysUserRelaPermissionDao = sysUserRelaPermissionDao;
	}
	Object synobject = new Object();

	/**
	 * 能否访问指定uri
	 * 
	 * @param account
	 * @param uri
	 * @return
	 */
	public boolean txCanAccessResource(String account, String uri) {
		synchronized (synobject) {

			SysPermission resource = commonDao
					.geUniqueObjectByPropertyAndValue(SysPermission.class,
							"url", uri);
			if (resource == null) {// 资源不受控,可以访问
				return true;
			}

			if (resource.getEnalbed() == 1) {// 资源需要验证

				// 根据账号和访问资源获取该账号的角色和权限的关系，如果存在该关系记录，则证明该账号有访问资源的权限，否则没有权限访问资源
				SysRoleRelaPermission srrp = sysRoleRelaPermissionDao
						.getSysRoleRelaPermissionByAccountAndURI(account, uri);
				if (srrp == null) {
					return false;
				} else {
					return true;
				}

			} else {// 资源不需验证,可以直接访问
				return true;
			}

		}
	}

	/**
	 * 获取权限树集合
	 * 
	 * @return List<Map>
	 */
	public List<Map<String, Object>> getPermissionTree() {
		// 获取第一级权限类型
		List<SysPermission> typeList = sysPermissionDao.getRootPermission();
		if (typeList == null || typeList.isEmpty()) {
			// 没找到根权限
			return null;
		}
		// 存储权限树集
		List<Map<String, Object>> treeList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list = sysPermissionDao.getPermissionTreeByType();
		// 根据第一级权限找权限树以及每类权限关联的权限资源
		for (SysPermission type : typeList) {
			Map map = new HashMap();
			if (list != null && !list.isEmpty()) {
				map = list2Tree(type.getPermissionId() + "", list);
				treeList.add(map);

			}
		}

		return treeList;

	}
	
	/**
	 * 根据角色获取权限树集合（整棵权限树获取，标识角色对每个权限的权限）
	 * 
	 * @return List<Map>
	 */
	public List<Map<String, Object>> getPermissionTreeByRole(long roleId) {
		/*// 获取第一级权限类型
		List<SysPermission> typeList = sysPermissionDao.getRootPermission();
		if (typeList == null || typeList.isEmpty()) {
			// 没找到根权限
			return null;
		}
		// 存储权限树集
		List<Map<String, Object>> treeList = new ArrayList<Map<String, Object>>();*/
		List<Map<String, Object>> list = sysPermissionDao.getPermissionTreeByRole(roleId);
		/*// 根据第一级权限找权限树以及每类权限关联的权限资源
		for (SysPermission type : typeList) {
			Map map = new HashMap();
			if (list != null && !list.isEmpty()) {
				map = list2Tree(type.getPermissionId() + "", list);
				treeList.add(map);

			}
		}*/

		return list;

	}
	

	
	/**
	 * 
	 * @description: 添加权限
	 * @author：yuan.yw
	 * @param permission     
	 * @return long     
	 * @date：Jan 14, 2014 2:01:34 PM
	 */
	public long txAddPermission(SysPermission permission){
		long id =0;
		Serializable s = commonDao.saveObject(permission);
		if(s!=null){
			id = Long.valueOf(s+"");
		}
		return id;
		
	}
	
	
	/**
	 * 
	 * @description: 修改权限
	 * @author：yuan.yw
	 * @param permission     
	 * @return void     
	 * @date：Jan 14, 2014 1:56:45 PM
	 */
	public void txUpdatePermission(SysPermission permission){
		commonDao.updateObject(permission);
	}
	/**
	 * 根据Id获取权限
	 * 
	 * @param id
	 * @return
	 */
	public SysPermission getPermissionById(long id) {
		return commonDao.getObjectById(SysPermission.class, id);
	}
	/**
	 * 
	 * @description: 根据Id获取权限
	 * @author：yuan.yw
	 * @param id
	 * @return     
	 * @return SysPermissionPmdev     
	 * @date：Jan 14, 2014 1:42:55 PM
	 */
	public SysPermission getPermissionPmdevById(long id){
		return commonDao.getObjectById(SysPermission.class, id);
	}
	/**
	 * 根据Id删除权限
	 */
	public void txDeletePermissionById(long id){
		sysPermissionDao.deletePermissionById(id);
	}
	
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
	public boolean deleteRoleRelaPermissionById(long permissionId,long roleId){
		return sysPermissionDao.deleteRoleRelaPermissionById(permissionId,roleId);
	}
	
	/**
	 * 删除角色下所有的权限关联关系
	 * @author li.hb
	 * @date 2014-1-13 下午3:15:59
	 * @Description: TODO 
	 * @param @param roleId
	 * @param @return        
	 * @throws
	 */
	public boolean deleteRoleRelaPermissionByRoleId(long roleId)
	{
		return sysPermissionDao.deleteRoleRelaPermissionByRoleId(roleId);
	}
	
	
	
	/**
	 * 
	 * @description: 删除权限实体
	 * @author：yuan.yw
	 * @param permission     
	 * @return void     
	 * @date：Jan 14, 2014 1:45:39 PM
	 */
	public void txDeletepermission(SysPermission permission){
		commonDao.deleteObject(permission);
	}
	/**
	 * 将list<Map>类型转换成存储树结构的map
	 * 
	 * @param id
	 * @param list
	 * @return
	 */
	public Map<String, Object> list2Tree(String id,
			List<Map<String, Object>> list) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 添加自身
		for (Map<String, Object> m : list) {
			if (m.get("PID").toString().equals(id)) {
				map.put("permission", m);
				break;
			}
		}

		// 添加子权限
		List<Map<String, Object>> subList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> m : list) {
			// System.out.println(m.get("PARENTID")+":"+id);
			if (m.get("PARENTID") != null
					&& !m.get("PARENTID").toString().equals("")
					&& m.get("PARENTID").toString().equals(id)) {
				Map<String, Object> subMap = list2Tree(m.get("PID").toString(),
						list);
				subList.add(subMap);
			}
		}
		map.put("subPermission", subList);
		return map;

	}
	
	/**
	 * 保存角色与权限关联
	 * @param roleRelaPermission
	 */
	public void txAddRoleRelaPermission(SysRoleRelaPermission roleRelaPermission){
		commonDao.saveObject(roleRelaPermission);
	}

	/**
	 * 获取全部权限类型
	 * 
	 * @return
	 */
	public List<SysPermissionType> getPermissionTypeByParentId(long parentId) {
		return sysPermissionTypeDao.getPermissionTypeByParentId(parentId);
	}
	
	/**
	 * 获取第一级权限类型
	 * 
	 * @return
	 */
	public List<SysPermissionType> getRootPermissionType() {
		return sysPermissionTypeDao.getRootPermissionType();
	}
	/**
	 * @author duhw
	 * @create_time 2013-06-06
	 * 通过用户账号标识得到用户的权限（权限通过flag(0,1)标识用户是否有权限）
	 */
	public List<Map<String,Object>> getPermissionListByAccountId(long account_id){
		return sysPermissionDao.getAllEnablePermissionByAccountId(account_id);
	}
	/**
	 * @author duhw
	 * @create_time 2014-01-14
	 * 通过用户标识得到用户的权限
	 * permissionType:MENU(菜单权限),DATA(数据权限)
	 * flag:true(得到需要验证的权限),false(所有验的权限)
	 * 注：用户的权限包括用户sys_user_rela_permission表中的权限和sys_role_rela_permission中的权限
	 * * return:所有的权限列表（如果用户有权限，则相应的权限数据flag=1）
	 */
	public List<Map<String,Object>> getPermissionListByUserId(long user_id,String system,String permissionType,boolean flag){
		return sysUserRelaPermissionDao.getFirstPermissionListByUserId(user_id, system, permissionType, flag);
	}
	/**
	 * 根据账号获取模块(PM项目管理)
	* @author ou.jh
	* @date Jun 7, 2013 2:15:42 PM
	* @Description: TODO 
	* @param @param account        
	* @throws
	 */
	public List<Map<String, Object>> getPMPermissionModuleByAccount(long org_user_id){
		return sysPermissionDao.getPMPermissionModuleByAccount(org_user_id);
	}

	/**
	 * 根据账号和权限类型获取权限
	 * 
	 * @return
	 */
	public List<SysPermission> getPermissionByAccountAndType() {
		return null;
	}

	/**
	 * 根据账号和权限类型获取子权限
	 * 
	 * @return
	 */
	public List<SysPermission> getSubPermissionByAccountAndType() {
		return null;
	}

	public List<SysPermission> getPermissionByRole(final long roleId) {

		return sysPermissionDao.getPermissionByRole(roleId);
	}

	public List<Map<String, Object>> getPermissionAndRoleRelaPermissionMapByRole(
			long roleId) {
		return sysPermissionDao
				.getPermissionAndRoleRelaPermissionMapByRole(roleId);
	}
	
	
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
	public List<Map<String, Object>> getPermissionListByProCodeAndType(String proCode,String type){
		return sysPermissionDao
		.getPermissionListByProCodeAndType(proCode,type);
		
	}
	/**
	 * 
	 * @description: 根据权限id删除本权限及子权限
	 * @author：yuan.yw
	 * @param permissionId
	 * @return     
	 * @return boolean     
	 * @date：Jan 14, 2014 4:15:55 PM
	 */
	public boolean detletSelfAndChildPermissionByPermissionId(long permissionId){
		return this.sysPermissionDao.detletSelfAndChildPermissionByPermissionId(permissionId);
	}
	public SysRoleRelaPermissionDao getSysRoleRelaPermissionDao() {
		return sysRoleRelaPermissionDao;
	}

	public void setSysRoleRelaPermissionDao(
			SysRoleRelaPermissionDao sysRoleRelaPermissionDao) {
		this.sysRoleRelaPermissionDao = sysRoleRelaPermissionDao;
	}

	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	public SysPermissionDao getSysPermissionDao() {
		return sysPermissionDao;
	}

	public void setSysPermissionDao(SysPermissionDao sysPermissionDao) {
		this.sysPermissionDao = sysPermissionDao;
	}

	public CommonDao getCommonDao() {
		return commonDao;
	}

	public SysPermissionTypeDao getSysPermissionTypeDao() {
		return sysPermissionTypeDao;
	}

	public void setSysPermissionTypeDao(
			SysPermissionTypeDao sysPermissionTypeDao) {
		this.sysPermissionTypeDao = sysPermissionTypeDao;
	}
	/**
	 * 更新用户权限
	 */
	public int updateUserPermission(long orgUserId, String pIds) {
		//删除用户关联权限
		sysPermissionDao.deleteUserPermission(orgUserId);
		if(pIds!=null&&!pIds.equals("")){
			String[] pids= pIds.split(",");
			//添加用户关联权限
			for (int i = 0; i < pids.length; i++) {
				sysPermissionDao.addUserPermission(orgUserId, pids[i]);
			}
			return 1;
		}
		
		return 0;
	}

	/**
	 * 根据角色获取权限
	 */
	public String getPermissionIdsByRoleIds(String roleids) {
		// TODO Auto-generated method stub
		return sysPermissionDao.getPermissionIdsByRoleIds(roleids);
	}
	/**
	 * 根据用户和角色获取权限树集合（整棵权限树获取，标识用户对每个权限的权限）-new
	 * 
	 * @return List<Map>
	 */
	public List<Map<String, Object>> getPermissionTreeByUser(long orgUserId,String roleids) {
		// 获取第一级权限类型
		List<SysPermission> typeList = sysPermissionDao.getRootPermission();
		if (typeList == null || typeList.isEmpty()) {
			// 没找到根权限
			return null;
		}
		if(roleids==null||roleids.equals("")){
			roleids="0";
		}	
		// 存储权限树集
		List<Map<String, Object>> treeList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>>listByRoles= sysPermissionDao
				.getPermissionTreeByRoles(roleids);
		//根据用户直接得到权限
		List<Map<String, Object>> listByUser = sysPermissionDao
				.getPermissionTreeByUserId(orgUserId);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> map:listByRoles){
			boolean flag = false;
			for(Map<String, Object> map1:listByUser){
				//如果通过角色取得的权限的flag为空即没有此权限，就把从用户获得的权限放入树中
				//FLAG为2，为通过角色取得的权限，FLAG为1，通过用户取得的权限
//				if(map.get("PID").equals(map1.get("PID"))&&map.get("FLAG")==null){
					if(map.get("PID").equals(map1.get("PID"))&&map.get("PER_ACCESS")==null){
					
					flag=true;
					list.add(map1);
					break;
				}
			}
			if(flag==false){
				list.add(map);
				}
		}
		// 根据第一级权限找权限树以及每类权限关联的权限资源
		for (SysPermission type : typeList) { 
			Map map = new HashMap();
			if (list != null && !list.isEmpty()) {
				map = list2Tree(type.getPermissionId() + "", list);
				treeList.add(map);
			}
		}

		return treeList;

	}
	/**
	 * 根据多个角色获取权限树集合（整棵权限树获取，标识角色对每个权限的权限）-new
	 * 
	 * @return List<Map>
	 */
	public List<Map<String, Object>> getPermissionTreeByRoles(String roleids) {
		if(roleids==null||roleids.equals("")){
			roleids="0";
		}
		// 获取第一级权限类型
		List<SysPermission> typeList = sysPermissionDao.getRootPermission();
		if (typeList == null || typeList.isEmpty()) {
			// 没找到根权限
			return null;
		}
		// 存储权限树集
		List<Map<String, Object>> treeList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list = sysPermissionDao.getPermissionTreeByRoles(roleids);
		/*// 根据第一级权限找权限树以及每类权限关联的权限资源
		for (SysPermission type : typeList) {
			Map map = new HashMap();
			if (list != null && !list.isEmpty()) {
				map = list2Tree(type.getPermissionId() + "", list);
				treeList.add(map);

			}
		}*/

		return list;

	}
}
