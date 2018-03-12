package com.iscreate.op.service.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.iscreate.op.dao.common.CommonDao;
import com.iscreate.op.dao.system.SysOrganizationDao;
import com.iscreate.op.dao.system.SysUserRelaPermissionDao;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.pojo.system.SysPermission;

public class SysUserRelaPermissionServiceImpl implements SysUserRelaPermissionService{
	
	public SysUserRelaPermissionDao sysUserRelaPermissionDao;//用户关联权限
	public SysOrganizationDao sysOrganizationDao;//组织
	public SysUserRelaPostService sysUserRelaPostService;//用户岗位
	public CommonDao commonDao;//公共dao
	
	/**
	 * @author duhw
	 * @create_time 2014-01-14
	 * 通过用户标识得到用户的权限
	 * permissionType:(菜单权限),(数据权限)
	 * flag:true(得到需要验证的权限),false(所有验的权限)
	 * 注：用户的权限包括用户sys_user_rela_permission表中的权限和sys_role_rela_permission中的权限
	 * * return:所有的权限列表（如果用户有权限，则相应的权限数据flag=1）
	 */
	public List<Map<String,Object>> getPermissionListByUserId(long user_id,String system,String permissionType,boolean flag){
		return sysUserRelaPermissionDao.getPermissionListByUserId(user_id,system,permissionType,flag);
	}

	
	
	/**
	 * @author ou.jh
	 * @create_time 2014-01-14
	 * 通过用户标识得到一级用户的权限
	 * permissionType:MENU(菜单权限),DATA(数据权限)
	 * flag:true(得到需要验证的权限),false(所有验的权限)
	 * 注：用户的权限包括用户sys_user_rela_permission表中的权限和sys_role_rela_permission中的权限
	 * * return:所有的权限列表（如果用户有权限，则相应的权限数据flag=1）
	 */
	public List<Map<String,Object>> getFirstPermissionListByUserId(long user_id,String system,String permissionType,boolean flag){
		return sysUserRelaPermissionDao.getFirstPermissionListByUserId(user_id,system,permissionType,flag);
	}


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
	public List<Map<String,Object>> getSysPermissionListByProCodeAndTypeAndServType(String proCode,String type,String servType,long orgUserId){	
		return this.sysUserRelaPermissionDao.getSysPermissionListByProCodeAndTypeAndServType(proCode, type, servType, orgUserId);
	}
	/**
	 * 
	 * @description:  （PM）根据资源所属系统编码 资源类型 关联的SERV_TYPE 删除用户关联数据权限
	 * @author：yuan.yw
	 * @return     
	 * @return boolean     
	 * @date：Jan 13, 2014 10:49:06 AM
	 */
	public boolean deleteUserPMDataPermissionByProCodeAndTypeAndServType(String proCode,String type,String servType,long orgUserId){
		return this.sysUserRelaPermissionDao.deleteUserPMDataPermissionByProCodeAndTypeAndServType(proCode, type, servType, orgUserId);
	}
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
	public boolean savePMUserRelaData(long orgUserId,long permissionId){
		return this.sysUserRelaPermissionDao.savePMUserRelaData(orgUserId, permissionId);
	}
	/**
	 * 
	 * @description: 根据权限id删除用户权限
	 * @author：yuan.yw
	 * @param permissionId
	 * @return     
	 * @return boolean     
	 * @date：Jan 14, 2014 1:47:40 PM
	 */
	public boolean deleteUserRelaPermissionByPermissionId(long permissionId){
		return this.sysUserRelaPermissionDao.deleteUserRelaPermissionByPermissionId(permissionId);
	}

	
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
	public List<Map<String,Object>> getSysPermissionListByOrgUserIdAndParentId(long orgUserId , long parentId){
		return this.sysUserRelaPermissionDao.getSysPermissionListByOrgUserIdAndParentId(orgUserId, parentId);
	}
	
	
	/**
	 * 
	 * @description: 添加部门权限资源(本class类中使用)
	 * @author：yuan.yw
	 * @param org
	 * @return     
	 * @return long     
	 * @date：Jan 15, 2014 12:00:24 PM
	 */
	public  long addPermissionByOrg(SysOrg org){
		long orgPermissionId=0L;
		SysPermission permission = new SysPermission();
		permission.setProCode("PM");
		permission.setName(org.getName());
		permission.setCode("PM_Org"+org.getOrgId());
		permission.setType("PM_DataResource");
		permission.setServId(org.getOrgId());
		permission.setServType("PM_Org");
		permission.setParentId(0L);
		permission.setCreateTime(new Date());
		permission.setEnalbed(1);
		//添加部门权限资源
		Serializable s=this.commonDao.saveObject(permission);
		if(s!=null){
			orgPermissionId = Long.valueOf(s+"");
			permission=this.commonDao.getObjectById(SysPermission.class, orgPermissionId);
			if(permission!=null){//更新path路径
				permission.setPath("/"+orgPermissionId+"/");
				this.commonDao.updateObject(permission);
			}
		}
		return orgPermissionId;
	}
	/**
	 * 
	 * @description: 添加项目权限(本class类中使用)
	 * @author：yuan.yw
	 * @param project
	 * @param orgPermissionId
	 * @return     
	 * @return long     
	 * @date：Jan 15, 2014 12:03:19 PM
	 */
	public long addPermissionByProjectMapAndOrgPemissionId(Map<String,Object> project,long orgPermissionId){
		long projectPermissionId=0L;
		SysPermission permission = new SysPermission();
		permission.setProCode("PM");
		permission.setName(project.get("NAME")+"");
		permission.setCode("PM_Project"+project.get("PRO_ID"));
		permission.setType("PM_DataResource");
		permission.setServId(Long.valueOf(project.get("PRO_ID")+""));
		permission.setServType("PM_Project");
		permission.setParentId(orgPermissionId);
		permission.setCreateTime(new Date());
		permission.setEnalbed(1);
		//添加项目权限资源
		Serializable s=this.commonDao.saveObject(permission);
		if(s!=null){
			projectPermissionId = Long.valueOf(s+"");
			permission=this.commonDao.getObjectById(SysPermission.class, projectPermissionId);
			if(permission!=null){//更新path路径
				permission.setPath("/"+orgPermissionId+"/"+projectPermissionId+"/");
				this.commonDao.updateObject(permission);
			}
		}
		return projectPermissionId;
	}
	
	/**
	 * 
	 * @description: 通过用户id获取用户的权限部门list(PM项目使用)
	 * @author：yuan.yw
	 * @param orgUserId 用户id
	 * @return     部门ORG信息list
	 * @return     List<Map<String,Object>> 
	 * @date：Jan 16, 2014 10:27:21 AM
	 */
	public List<Map<String,Object>> getUserPermissionOrgListByUserId(long orgUserId){
		return this.sysUserRelaPermissionDao.getUserPermissionOrgListByUserId(orgUserId);
	}
	
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
	public List<Map<String,Object>> getUserPermissionProjectListByOrgIdAndUserId(long orgId,long orgUserId){
		return this.sysUserRelaPermissionDao.getUserPermissionProjectListByOrgIdAndUserId(orgId,orgUserId);
	}
	/**
	 * 
	 * @description: 通过用户id获取用户的所有权限项目list(PM项目使用)
	 * @author：yuan.yw
	 * @param orgUserId
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jan 17, 2014 3:21:01 PM
	 */
	public List<Map<String,Object>> getUserPermissionProjectListByUserId(long orgUserId){
		return this.sysUserRelaPermissionDao.getUserPermissionProjectListByOrgIdAndUserId(0,orgUserId);
	}
	
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
	public List<Map<String,Object>> getUserPermissionMenuListByUserIdAndSystemCode(long orgUserId,String systemCode){
		return this.sysUserRelaPermissionDao.getUserPermissionMenuListByUserIdAndSystemCode(orgUserId, systemCode);
	}
	public void setSysUserRelaPermissionDao(
			SysUserRelaPermissionDao sysUserRelaPermissionDao) {
		this.sysUserRelaPermissionDao = sysUserRelaPermissionDao;
	}
	public SysUserRelaPermissionDao getSysUserRelaPermissionDao() {
		return sysUserRelaPermissionDao;
	}

	public SysOrganizationDao getSysOrganizationDao() {
		return sysOrganizationDao;
	}

	public void setSysOrganizationDao(SysOrganizationDao sysOrganizationDao) {
		this.sysOrganizationDao = sysOrganizationDao;
	}



	public SysUserRelaPostService getSysUserRelaPostService() {
		return sysUserRelaPostService;
	}



	public void setSysUserRelaPostService(
			SysUserRelaPostService sysUserRelaPostService) {
		this.sysUserRelaPostService = sysUserRelaPostService;
	}



	public CommonDao getCommonDao() {
		return commonDao;
	}



	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}
	
}
