package com.iscreate.op.service.system;

import java.util.List;
import java.util.Map;

import com.iscreate.op.dao.common.CommonDao;
import com.iscreate.op.dao.system.SysRoleDao;
import com.iscreate.op.dao.system.SysRoleTypeDao;
import com.iscreate.op.dao.system.SysUserRelaRoleDao;
import com.iscreate.op.pojo.system.SysRole;
import com.iscreate.op.pojo.system.SysRole;
import com.iscreate.op.pojo.system.SysRoleType;

public class SysRoleServiceImpl implements SysRoleService{
	private SysRoleDao sysRoleDao;
	private SysRoleTypeDao sysRoleTypeDao;
	private CommonDao commonDao;
	private SysUserRelaRoleDao sysUserRelaRoleDao;

	/**
	 * 根据角色类型获取角色列表
	 * @param roleTypeId
	 * @return
	 */
	public List<SysRole> getRolesByRoleTypeCode(long roleTypeId){
		return sysRoleDao.getRolesByRoleTypeCode(roleTypeId);
	}
	
	/**
	 * 获取全部角色
	 * @param roleTypeId
	 * @return
	 */
	public List<SysRole> getAllRole(){
		return sysRoleDao.getAllRole();
	}
	
	/**
	 * 根据系统编码获取对应的角色列表
	 * @author li.hb
	 * @date 2014-1-14 上午9:07:02
	 * @Description: TODO 
	 * @param @param proCode
	 * @param @return        
	 * @throws
	 */
	public List<SysRole> getAllRoleByProCode(String systemCode)
	{
		return sysRoleDao.getAllRoleByProCode(systemCode);
	}
	
	/**
	 * 获取全部角色类型
	 * @param roleTypeId
	 * @return
	 */
	public List<SysRoleType> getAllRoleType(){
		return sysRoleTypeDao.getAllRoleType();
	}
	
	/**
	 * 保存角色
	 * @param sysRole
	 */
	public void txAddRole(SysRole sysRole){
		commonDao.saveObject(sysRole);
	}
	
	/**
	 * 修改角色
	 * @param sysRole
	 */
	public void txUpdateRole(SysRole sysRole){
		commonDao.updateObject(sysRole);
	}
	/**
	 * 根据Id获取角色
	 * @param roleId
	 * @return
	 */
	public SysRole getRoleById(long roleId){
		return  commonDao.getObjectById(SysRole.class, roleId);
	}
	
	/**
	 * 根据Id删除角色
	 * @param roleId
	 */
	public void txDeleteRoleById(long roleId){
		sysRoleDao.deleteRoleById(roleId);
	}
	
	/**
	 * 根据用户获取角色 
	 */
	public List<SysRole> getUserRoles(final String account){
		List<SysRole> userRoles = this.sysRoleDao.getUserRoles(account);
		return userRoles;
	}
	/**
	 * @author du.hw
	 * @create 2014-01-14
	 * 根据用户标识得到用户角色
	 * return:所有的角色（通过flag标识出用户是否有角色） 
	 */
	public List<Map<String,Object>> getUserRolesByUserId(long user_id,String system){
		return sysUserRelaRoleDao.getUserRolesByUserId(user_id, system);
	}
	public SysRoleDao getSysRoleDao() {
		return sysRoleDao;
	}

	public void setSysRoleDao(SysRoleDao sysRoleDao) {
		this.sysRoleDao = sysRoleDao;
	}

	public CommonDao getCommonDao() {
		return commonDao;
	}

	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	public SysRoleTypeDao getSysRoleTypeDao() {
		return sysRoleTypeDao;
	}

	public void setSysRoleTypeDao(SysRoleTypeDao sysRoleTypeDao) {
		this.sysRoleTypeDao = sysRoleTypeDao;
	}

	public List<SysRole> getUserRoles(long orgUserId) {
		// TODO Auto-generated method stub
		return sysRoleDao.getUserRoles(orgUserId);
	}
	/**
	 * 更新用户角色 
	 */
	public int updateUserRole(long orgUserId, String roleIds) {	
    	//删除原有用户角色关联
    	sysUserRelaRoleDao.deleteUserRelaRole(orgUserId);
        if(roleIds!=null&&!roleIds.equals("")){
        	String[] roleids =roleIds.split(",");
        	//添加用户角色关联
        	for (int i = 0; i < roleids.length; i++) {
        		sysUserRelaRoleDao.addUserRelaRole(orgUserId, roleids[i]);
			}
        	return 1;
        }	
        else return 0;
	}

	public SysUserRelaRoleDao getSysUserRelaRoleDao() {
		return sysUserRelaRoleDao;
	}

	public void setSysUserRelaRoleDao(SysUserRelaRoleDao sysUserRelaRoleDao) {
		this.sysUserRelaRoleDao = sysUserRelaRoleDao;
	}
	/**
	 * 根据用户和系统获取角色 
	 */
	public List<Map<String, Object>> getSystemsByUserAndSystem(long orgUserId,
			String systemCode) {
		// TODO Auto-generated method stub
		return sysRoleDao.getSystemsByUserAndSystem(orgUserId, systemCode);
	}
	/**
	 * 
	 * @description: 根据权限id删除角色关联的权限关系
	 * @author：yuan.yw
	 * @param permissionId
	 * @return     
	 * @return boolean     
	 * @date：Jan 15, 2014 2:20:02 PM
	 */
	public boolean deleteRoleRelaPermissionByPermissionId(long permissionId){
		return this.sysRoleDao.deleteRoleRelaPermissionByPermissionId(permissionId);
	}
	
	
	
}
