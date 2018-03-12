package com.iscreate.op.dao.system;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.iscreate.op.pojo.system.SysAccount;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.pojo.system.SysOrgUserPm;
import com.iscreate.op.pojo.system.SysRole;
import com.iscreate.op.pojo.system.SysRoleType;
import com.iscreate.op.pojo.system.SysUserRelaPost;
import com.iscreate.op.pojo.system.SysUserRelaRole;

public interface SysOrgUserDao {
	
	/**
	 * 根据账号获取人员信息
	 * @param account
	 * @return
	 */
	public SysOrgUser getSysStaffByOrgUserId(long orgUserId);
	/**
	 * 根据组织Id获取组织与账号的关系
	* @date May 7, 2013 2:16:09 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @return        
	* @throws
	 */
	public int getOrgAccountRelationToPageTotalByOrgId(long orgId);
	
	/**
	 * 根据账号获取人员信息(1.2.1)
	 * 
	 * @param account
	 * @return
	 */
	public SysOrgUserPm getSysOrgUserPOJOByOrgUserId(long orgUserId);

	
	/**
	 * 根据组织Id获取组织与账号的关系（分页）
	 * 
	 * @date May 7, 2013 2:22:37 PM
	 * @Description: TODO
	 * @param
	 * @param orgId
	 * @param
	 * @param currentPage
	 * @param
	 * @param pageSize
	 * @param isPage
	 * @return
	 * @throws
	 */
	public List<Map<String, Object>> getAccountToPageByOrgId(final long orgId,
			final int currentPage, final int pageSize , final String conditions, boolean isPage);
	

	
	/**
	 * 根据账号获取用户信息
	* @date May 9, 2013 10:04:25 AM
	* @Description: TODO 
	* @param @param session
	* @param @param account
	* @param @return        
	* @throws
	 */
	public SysOrgUser getSysOrgUserByAccount(String account);
	
	

	
	
	
	

	
	/**
	 * 根据人员ID获取人员详细信息
	* @date May 13, 2013 1:59:16 PM
	* @Description: TODO 
	* @param @param orgUserId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getSysOrgUserByOrgUserId(long orgUserId);
	
	
	/**
	 * 
	 * 根据组织ID获取人员名称
	 * @date 
	 * 
	 * @param orgId  组织ID
	 * @author Li.hb 
	 * @return
	 */
	public List<Map<String, Object>> getUserNameByOrgId(String orgId);

	
	
	
	/**
	 * 根据组织Id获取人员
	 * 
	 * @date May 7, 2013 2:22:37 PM
	 * @Description: TODO
	 * @param
	 * @param orgId
	 * @param
	 * @return
	 * @throws
	 */
	public List<Map<String, Object>> getUserByOrgId(final long orgId) ;
	
	/**
	 * 根据企业Id过滤获取人员
	* @date May 14, 2013 2:14:09 PM
	* @Description: TODO 
	* @param @param enterpriseId 企业ID
	* @param @param isFilter  需要过滤已选择人员
	* @param @param conditions
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getSysOrgUserByEnterpriseId(final long enterpriseId,boolean isFilter, final String conditions);
	

	
	/**
	 * 保存账号所属角色关系
	* @date May 14, 2013 5:02:07 PM
	* @Description: TODO 
	* @param @param sysUserRelaRole
	* @param @return        
	* @throws
	 */
	public Serializable savaSysUserRelaRole(SysUserRelaRole sysUserRelaRole);
	
	/**
	 * 根据人员ID批量删除人员角色关系
	* @date May 14, 2013 5:07:52 PM
	* @Description: TODO 
	* @param @param accountId
	* @param @throws Exception        
	* @throws
	 */
	public void bulkDeleteSysUserRelaRole(final long accountId) throws Exception;
	
	
	/**
	 * 根据账号与角色类型标识获取的角色信息
	* @author ou.jh
	* @date May 24, 2013 2:22:36 PM
	* @Description: TODO 
	* @param @param account
	* @param @param code        
	* @throws
	 */
	public List<Map<String, Object>> getUserRoleByAccountAndCode(String account,String code);
	
	/**
	 * 根据组织ID与角色类型获取组织下人员列表
	* @author ou.jh
	* @date May 24, 2013 2:44:01 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param roleCode
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUserByOrgIdAndRoleCode(long orgId,String roleCode);
	

	/**
	 * 根据组织ID集合与角色类型获取人员
	* @author ou.jh
	* @date May 28, 2013 1:53:04 PM
	* @Description: TODO 
	* @param @param orgIdsList
	* @param @param Role
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUserAndAccountByOrgIds(List<Long> orgIdsList, String Role);
	
	/**
	 * 获取人员账号获取人员所在企业
	* @author ou.jh
	* @date May 28, 2013 2:17:05 PM
	* @Description: TODO 
	* @param @param account
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getEnterpriseByAccount(String account);
	
	/**
	 * 根据组织ID与角色类型获取组织下人员列表(带人员最后一次有记录的经纬度)
	* @author ou.jh
	* @date May 24, 2013 2:44:01 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param roleCode
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUserAndLastCoordinatesByOrgIdSAndRoleCode(List<Long> orgIdsList,String roleCode);
	
	/**
	 * 根据用户ID获取账号信息
	* @author ou.jh
	* @date Sep 22, 2013 1:46:40 PM
	* @Description: TODO 
	* @param @param orgUserId
	* @param @return        
	* @throws
	 */
	public Map<String, Object> getAccountByOrgUserId(long orgUserId);
	

	
	/**
	 * 根据时间获取合同快到期
	* @author ou.jh
	* @date Mar 13, 2014 3:57:25 PM
	* @Description: TODO 
	* @param @param month
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getSysOrgUserContractEndTime(String startTime ,String endTime);
}
