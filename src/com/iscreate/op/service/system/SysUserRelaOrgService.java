package com.iscreate.op.service.system;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.system.SysUserRelaOrg;

public interface SysUserRelaOrgService {
	/**
	 * @author:duhw
	 * @create_time:2013-05-29
	 * 根据人员标识得到人员所有的上级领导
	 */
	public List<Map<String,Object>> getAllTopUserByOrgUserId(long org_user_id);
	/**
	 * @author:duhw
	 * @create_time:2013-05-13
	 * @param org_user_id:用户标识
	 * @return 组织列表(包括上级组织和下级组织)
	 */
	public List<Map<String,Object>> getAllTreeOrgByOrgUserId(long org_user_id);

	/**
	 * 
	 * @description: 根据组织orgId 和 orgUserIds 获取组织与人员关系
	 * @author：
	 * @param orgId
	 * @param orgUserIds
	 * @return     
	 * @return List<SysUserRelaOrgDao>     
	 * @date：May 13, 2013 3:49:48 PM
	 */
	public List<SysUserRelaOrg> getSysUserRelaOrgListByOrgUserIdsAndOrgId(long orgId,String orgUserIds);
	/**
	 * 
	 * @description: 根据账号获取组织列表(包括上级组织和下级组织)
	 * @author：yuan.yw
	 * @param account
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：May 23, 2013 2:59:49 PM
	 */
	public List<Map<String,Object>> getAllTreeOrgByAccount(String account);
	
	/**
	 * 更新用户和部门关系状态
	* @author ou.jh
	* @date Sep 3, 2013 2:28:34 PM
	* @Description: TODO 
	* @param @param infoMap
	* @param @param id
	* @param @return        
	* @throws
	 */
	public int updateSysUserRelaOrgStatus(String status,long orgUserId);
	
	/**
	 * 保存用户与组织关系
	* @author ou.jh
	* @date Jan 14, 2014 5:42:37 PM
	* @Description: TODO 
	* @param @param entity
	* @param @return        
	* @throws
	 */
	public int saveSysUserRelaOrg(SysUserRelaOrg sysUserRelaOrg);
}
