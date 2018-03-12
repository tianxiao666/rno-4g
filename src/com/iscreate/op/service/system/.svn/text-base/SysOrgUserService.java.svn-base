package com.iscreate.op.service.system;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.system.SysAccount;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.pojo.system.SysOrgUserPm;
import com.iscreate.op.pojo.system.SysRole;
import com.iscreate.op.pojo.system.SysRoleType;
import com.iscreate.op.pojo.system.SysUserRelaPost;

public interface SysOrgUserService {
	/**
	 * 根据组织Id获取人员(分页)
	 * 
	 * @date May 7, 2013 2:14:08 PM
	 * @Description: TODO
	 * @param
	 * @param orgId
	 *            组织ID
	 * @param
	 * @param currentPage
	 *            查询页面数字
	 * @param
	 * @param pageSize
	 *            总页数
	 * @param
	 * @return
	 * @throws
	 */
	public Map<String, Object> getAccountListToPageByOrgIdService(long orgId,
			int currentPage, int pageSize ,String conditions);

	/**
	 * 根据账号获取人员信息
	 * @param account
	 * @return
	 */
	public SysOrgUser getSysStaffByOrgUserId(long orgUserId);
	
	/**
	 * 根据登录人账号获取用户信息
	 * 
	 * @param account
	 * @return
	 */
	public SysOrgUser getSysOrgUserByAccount(String account);

	/**
	 * 根据人员id获取人员角色(String)
	* @author ou.jh
	* @date May 24, 2013 9:43:26 AM
	* @Description: TODO 
	* @param @param orgUserId      
	* @throws
	 */
	public String getUserRolesStringByUserId(long orgUserId);

	

	/**
	 * 根据人员id获取人员角色
	* @author ou.jh
	* @date May 24, 2013 9:43:26 AM
	* @Description: TODO 
	* @param @param orgUserId      
	* @throws
	 */
	public List<SysRole> getUserRolesByUserId(long orgUserId);
	

	
	
	/**
	 * 根据账号获取人员
	 * @param accountId
	 */
	public Map<String,Object> getProviderAccountByAccountIdAjaxService(long orgUserId);
	
	/**
	 * 根据角色类型id获取角色
	* @date May 13, 2013 2:52:08 PM
	* @Description: TODO 
	* @param @param roleTypeId
	* @param @return        
	* @throws
	 */
	public List<SysRole> getSysRoleByRoleTypeId(long roleTypeId);
	
	
	/**
	 * 获取全部角色类型
	* @date May 13, 2013 4:01:39 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public List<SysRoleType> getAllSysRoleType();
	
	
	/**
	 * 保存用户信息
	* @date May 14, 2013 9:14:28 AM
	* @Description: TODO 
	* @param @param sysOrgUser
	* @param @throws Exception        
	* @throws
	 */
	public Serializable txSaveSysOrgUser(SysOrgUser sysOrgUser)
		throws Exception ;
	
	
	/**
	 * 保存人员基本信息(v1.1.3)
	* @date May 21, 2013 1:57:38 PM
	* @Description: TODO 
	* @param @param orgJsonStr
	* @param @return        
	* @throws
	 */
	public long txSaveOrgUserAndAccount(SysOrgUserPm sysOrgUser,SysAccount sysAccount);
	
	
	
	/**
	 * 根据组织Id获取人员
	* @date May 14, 2013 1:38:10 PM
	* @Description: TODO 
	* @param @param enterpriseId
	* @param @param choiceAccountType
	* @param @param orgId
	* @param @param pinyin
	* @param @param conditions
	* @param @return        
	* @throws
	 */
	public List<Map<String,Object>> getSysOrgUserByOrgIdService(long enterpriseId,String choiceAccountType,long orgId ,String pinyin,String conditions);

	/**
	 * 获取全部角色
	* @date May 13, 2013 4:01:39 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public List<SysRole> getAllSysRole();
	
	
	/**
	 * 修改人员基本信息
	* @date May 14, 2013 4:28:14 PM
	* @Description: TODO 
	* @param @param orgJsonStr        
	* @throws
	 */
	public String txUpdateStaffAndUserRole(String orgJsonStr);
	
	/**
	 * 获取企业url的数据字典
	* @date May 21, 2013 11:11:58 AM
	* @Description: TODO 
	* @param @return        
	* @throws
	 */
	public List<Map<String,String>> getEnterpriseUrlDictionaryService();
	
	/**
	 * 保存人员基本信息
	* @date May 21, 2013 1:57:38 PM
	* @Description: TODO 
	* @param @param orgJsonStr
	* @param @return        
	* @throws
	 */
	public long txSaveStaffAndUserRole(String orgJsonStr);
	
	
	/**
	 * 根据人员账号获取人员角色
	* @author ou.jh
	* @date May 24, 2013 9:43:26 AM
	* @Description: TODO 
	* @param @param account        
	* @throws
	 */
	public List<SysRole> getUserRolesByAccount(String account);
	
	
	
	/**
	 * 检测人员与角色关系
	* @author ou.jh
	* @date May 24, 2013 2:25:55 PM
	* @Description: TODO 
	* @param @param account
	* @param @param code
	* @param @return        
	* @throws
	 */
	public boolean checkUserRoleByAccountAndCode(String account,String code);
	
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
	 * 根据组织ID集合获取维护队长
	* @author ou.jh
	* @date May 28, 2013 10:47:52 AM
	* @Description: TODO 
	* @param @param orgIds        
	* @throws
	 */
	public List<Map<String, Object>> getTeamLeaderByOrgIds(List<Long> orgIdsList);
	
	/**
	 * 根据组织ID集合与角色类型获取人员
	* @author ou.jh
	* @date May 28, 2013 10:47:52 AM
	* @Description: TODO 
	* @param @param orgIds        
	* @throws
	 */
	public List<Map<String, Object>> getUserAndAccountByOrgIds(List<Long> orgIdsList,String roleCode);
	
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
	 * 修改用户信息
	* @date May 14, 2013 9:14:28 AM
	* @Description: TODO 
	* @param @param sysOrgUser
	* @param @throws Exception        
	* @throws
	 */
	public void txUpdateSysOrgUser(SysOrgUser sysOrgUser) throws Exception ;
	
	/**
	 * 修改账号
	* @date May 14, 2013 5:44:48 PM
	* @Description: TODO 
	* @param @param account
	* @param @throws Exception        
	* @throws
	 */
	public void txUpdateAccount(SysAccount account) throws Exception;
	
	
	/**
	 * 根据组织ID获取人员名称
	 * @param orgId
	 * @return
	 */
	public List<Map<String, Object>> getUserNameByOrgId(String orgId);
	
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
	 * 保存人员基本信息(v1.1.3)
	* @date May 21, 2013 1:57:38 PM
	* @Description: TODO 
	* @param @param orgJsonStr
	* @param @return        
	* @throws
	 */
	public long txSaveOrgUserAndAccount(SysOrgUser sysOrgUser,SysAccount sysAccount);
	

	
	/**
	 * 修改人员基本信息
	* @date May 14, 2013 4:28:14 PM
	* @Description: TODO 
	* @param @param orgJsonStr        
	* @throws
	 */
	public long txUpdateOrgUserAndAccount(SysOrgUser sysOrgUser,SysAccount sysAccount);
	
	/**
	 * 根据账号获取人员信息(1.2.1)
	 * 
	 * @param account
	 * @return
	 */
	public SysOrgUserPm getSysOrgUserPOJOByOrgUserId(long orgUserId);
	
	/**
	 * 修改人员基本信息
	* @date May 14, 2013 4:28:14 PM
	* @Description: TODO 
	* @param @param orgJsonStr        
	* @throws
	 */
	public long txUpdateOrgUserAndAccount(SysOrgUserPm sysOrgUser,SysAccount sysAccount);
	
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
