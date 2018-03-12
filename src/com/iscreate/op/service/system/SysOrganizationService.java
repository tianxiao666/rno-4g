package com.iscreate.op.service.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.iscreate.op.constant.OrganizationConstant;
import com.iscreate.op.pojo.organization.Staff;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.plat.tree.TreeNode;


public interface SysOrganizationService {

	/**
	 * 
	 * @description: 获取登陆人所属的企业
	 * @author：yuan.yw
	 * @param account
	 * @return     
	 * @return Map<String,String>     
	 * @date：Sep 26, 2013 11:10:59 AM
	 */
	public Map<String,String> getLoginIdBelongEnterpriseTypeService(String account);
	
	public List<Map<String,Object>> getUserIdByRoleCode(
			String code) ;
	
	/**
	 * 获取所有最高级组织列表
	 * @param enterpriseType
	 */
	public List<Map<String,String>> getAllTheTopProviderOrgService(String enterpriseType);
	/**
	 * 根据企业Ids，类型获取组织树(根节点树)
	 * @param enterpriseId
	 * @param enterpriseType
	 */
	public void getProviderOrgTreeDownwardByEnterpriseIdAjaxService(String enterpriseIds,String enterpriseType);
	
	/**
	 * 通过组织id获取组织 
	* @author ou.jh
	* @date Nov 4, 2013 3:59:08 PM
	* @Description: TODO 
	* @param @param orgId        
	* @throws
	 */
	public SysOrg getOrgByOrgId(long orgId);
	
	/**
	 * 根据orgUserId获取所在公司的第二级组织
	* @author ou.jh
	* @date Aug 27, 2013 2:25:11 PM
	* @Description: TODO 
	* @param @param orgUserId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getAllSecondOrgListByOrgUserId(long orgUserId);
	
	/**
	 * 根据orgUserId获取所在公司的第二级组织
	* @author ou.jh
	* @date Aug 27, 2013 2:25:11 PM
	* @Description: TODO 
	* @param @param orgUserId
	* @param @return        
	* @throws
	 */
	public String getAllSecondOrgStringByOrgUserId(long orgUserId);
	

	
	/**
	 * 
	 * @description: 根据人员获取其所属关联组织最高级组织架构
	 * @author：yuan.yw
	 * @param accountId
	 * @return     
	 * @return List<SysOrg>     
	 * @date：May 28, 2013 3:58:30 PM
	 */
	public List<SysOrg> getTopLevelOrgByOrgUserId(long orgUserId);
	/**
	 * 根据组织Ids获取组织树
	 * @param orgIds
	 * 
	 */
	public void getProviderOrgTreeDownwardByorgIdsAjaxService(String orgIds);
	/**
	 * 
	 * @description: 根据所传类型获取相关数据字典
	 * @author：     
	 * @return void     
	 * @date：May 8, 2013 11:47:53 AM
	 */
	public void getDictionaryAjaxService(String dictionaryType);
	
	/**
	 * 根据orgUserId获取所在公司的第二级组织
	* @author ou.jh
	* @date Aug 27, 2013 2:25:11 PM
	* @Description: TODO 
	* @param @param orgUserId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUserSecondOrgListByOrgUserId(long orgUserId);
	
	/**
	 * 根据orgUserId获取所在公司的第二级组织
	* @author ou.jh
	* @date Aug 27, 2013 2:25:11 PM
	* @Description: TODO 
	* @param @param orgUserId
	* @param @return        
	* @throws
	 */
	public String getUserSecondOrgStringByOrgUserId(long orgUserId);
	
	/**
	 * 根据组织Id获取组织信息(组织架构详细信息,上级ID为中文)
	 * @param orgId
	 * @return
	 */
	public Map<String,String> getProviderOrgByOrgIdtoMapService(long orgId);
	/**
	 * 获取所有未选中的企业
	 * @return
	 */
	public List<Map<String,String>> getAllNoChoiceEnterpriseService(String orgType);
	/**
	 * 保存组织
	 * @param orgJsonStr
	 */
	public void txSaveProviderOrgInfoAjaxService(String orgJsonStr);
	/**
	 * 修改组织
	 * @param orgJsonStr
	 */
	public void txUpdateProviderOrgInfoAjaxService(String orgJsonStr);
	
	/**
	 * 删除组织
	 * @param orgId
	 */
	public void txDeleteProviderOrgInfoAjaxService(long orgId);
	/**
	 * 根据账号获取可操作组织架构的权限
	 * @param account
	 */
	public void getAuthorityByAccountAjaxService(String account);
	/**
	 * 
	 * @description: 获取最高级组织
	 * @author：yuan.yw
	 * @param account     
	 * @return void     
	 * @date：Sep 26, 2013 11:16:45 AM
	 */
	public void getTheTopProviderOrgAjaxService(String account);
	/**
	 * 根据人员Ajax获取最高组织
	 * @param account
	 */
	public void getTopLevelOrgByAccountAjaxService(String account);
	/**
	 * @author:duhw
	 * 根据组织标识获取所有上级组织
	 * @param orgId
	 * @return List<Map>
	 */
	public List<Map<String, Object>> getAllTheTopOrgByOrgIdService(long orgId);
	/**
	 * @author:duhw
	 * 根据用户标识获取所有组织架构树（包括上级组织和下级组织）
	 * 注：此树是完整的架构树，从根节点到最底层的节点
	 * @param org_user_id:用户标识
	 * @return List<Map>
	 */
	public List<Map<String, Object>> getAllTreeOrgByUserIdService(long org_user_id);
	/**
	 * 
	 * @description: 通过组织id获取人员列表
	 * @author：
	 * @param orgId
	 * @return     
	 * @return List<SysOrgUser>     
	 * @date：May 11, 2013 6:50:43 PM
	 */
	public List<SysOrgUser> getUserListByOrgIdService(long orgId);
	/**
	 * 创建组织与人员的关系
	 */
	public void addOrgStaffAjaxService(long orgId, String account);
	/**
	 * ajax根据账号ID删除改账号与组织的关系
	 */
	public void txDeleteOrgStaffAjaxService(String account,long orgId);
	
	/**
	 * 创建组织与人员的关系
	 */
	public void addOrgStaffService(long orgId, String orgUserId);
	/**
	 * ajax根据账号获取该账号以下的组织树集合
	 * @param account
	 */
	public void getOrgTreeDownwardByAccountAjaxService(String account);
	/**
	 * 
	 * @description: 根据组织Id向下获取项目
	 * @author：yuan.yw
	 * @param orgId
	 * @return     
	 * @return List<Map<String,String>>     
	 * @date：May 27, 2013 11:04:29 AM
	 */
	public List<Map<String, String>> getProjectToDownwardOrgByOrgId(long orgId);
	/**
	 * 
	 * @description: 获取最高级组织 当前登录人
	 * @author：yuan.yw
	 * @param account
	 * @return     
	 * @return Map<String,String>     
	 * @date：May 27, 2013 11:50:16 AM
	 */
	public Map<String,String> getTopOrgService(String account);
	/**
	 * 
	 * @description: 根据项目Id获取该项目所属的组织
	 * @author：yuan.yw
	 * @param projectId 项目id
	 * @return     
	 * @return List<Map<String,String>>     
	 * @date：May 27, 2013 2:02:43 PM
	 */
	public List<Map<String,String>> getOrgByProjectIdService(String projectId);
	
	/**
	 * 
	 * @description: 根据组织Id获取上一级或下一级的组织
	 * @author：yuan.yw
	 * @param orgId 组织id
	 * @param level 级别  child 下一级 parent 上一级
	 * @return     
	 * @return List<SysOrg>     
	 * @date：May 27, 2013 3:02:07 PM
	 */
	public List<SysOrg> getUpOrNextLevelOrgByOrgIdService(long orgId,String level);
	
	/**
	 * 
	 * @description:根据所在组织架构和组织类型 级别类型 向下或向上 获取组织架构列表
	 * @author：yuan.yw
	 * @param orgId
	 * @param typeCode
	 * @param levelType  child 向下 parent 向上
	 * @return     
	 * @return List<Map<String,Object>     
	 * @date：May 27, 2013 4:04:37 PM
	 */
	public List<Map<String,Object>> getOrgListDownwardOrUpwardByOrgTypeAndOrgIdService(
			long orgId, String typeCode,String levelType);
	/**
	 * 根据组织向下获取所有下属组织架构 SysOrg
	 * @description: TODO
	 * @author：yuan.yw
	 * @param orgId
	 * @return     
	 * @return List<SysOrg>     
	 * @date：May 28, 2013 10:04:55 AM
	 */
	public List<SysOrg> getOrgListDownwardByOrg(long orgId);
	/**
	 * 根据组织向下获取所有下属组织架构 Map
	 * @description: TODO
	 * @author：yuan.yw
	 * @param orgId
	 * @return     
	 * @return List<Map<String,Object>     
	 * @date：May 28, 2013 10:04:55 AM
	 */
	public List<Map<String,Object>> getOrgListMapDownwardByOrg(long orgId);
	/**
	 * 
	 * @description: 根据组织Id和组织属性获取下一级的所属组织
	 * @author：yuan.yw
	 * @param orgId
	 * @param orgAttribute
	 * @return     
	 * @return List<Map<String,Object>     
	 * @date：May 28, 2013 1:09:45 PM
	 */
	public List<Map<String,Object>> getProviderOrgNextByOrgIdAndOrgAttrService(long orgIdString,String orgAttribute);
	/**
	 * 
	 * @description: 根据组织向上获取所有上级组织架构
	 * @author：
	 * @param orgId
	 * @return     
	 * @return List<SysOrg>     
	 * @date：May 28, 2013 2:20:08 PM
	 */
	public List<SysOrg> getOrgListUpwardByOrg(long orgId);
	/**
	 * 
	 * @description: 根据组织类型和人员获取组织架构
	 * @author：yuan.yw
	 * @param accountId
	 * @param typeCode
	 * @return     
	 * @return List<SysOrg>     
	 * @date：May 28, 2013 3:16:37 PM
	 */
	public List<SysOrg> getOrgByAccountAndOrgType(
			String accountId, String typeCode);
	/**
	 * 
	 * @description: 根据人员获取其所属关联组织最高级组织架构
	 * @author：yuan.yw
	 * @param accountId
	 * @return     
	 * @return List<SysOrg>     
	 * @date：May 28, 2013 3:58:30 PM
	 */
	public List<SysOrg> getTopLevelOrgByAccount(String accountId);
	
	/**
	 * 
	 * @description: 根据组织id 角色类型 级别参数(组织向上、向下) 获取人员列表
	 * @author：yuan.yw
	 * @param orgId 组织id
	 * @param roleCode 角色类型
	 * @param  level  upward 向上 downward 向下 all 向上及向下  downOrUpward 向下或向上
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：May 29, 2013 10:52:10 AM
	 */
	public List<Map<String,Object>> getProviderStaffByOrgIdAndRoleCode(long orgId,String roleCode,String level);
	/**
	 * 
	 * @description: 根据角色类型和账号获取组织架构
	 * @author：yuan.yw
	 * @param account
	 * @param roleCode
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：May 29, 2013 2:49:49 PM
	 */
	public List<Map<String,Object>> getOrgByAccountAndRoleCode(
			 String account,String roleCode);
	/**
	 * 
	 * @description: 根据账号获取关联组织
	 * @author：yuan.yw
	 * @param account
	 * @return     
	 * @return List<SysOrg>     
	 * @date：May 29, 2013 3:32:06 PM
	 */
	public List<SysOrg> getOrgByAccountService(String account);
	/**
	 * 
	 * @description: 根据组织Id和组织属性 级别（上一级 下一级 向上 向下） 获取相关组织
	 * @author：yuan.yw
	 * @param orgId  组织id
	 * @param orgAttribute 组织属性
	 * @param level 级别  up 上一级 next 下一级 upward 向上 downward 向下
	 * @return     
	 * @return List<Map<String,Object>     
	 * @date：May 29, 2013 4:15:48 PM
	 */
	public List<Map<String,Object>> getProviderOrgByOrgIdAndOrgAttrByLevelService(long orgId,String orgAttribute,String level);
	/**
	 * 
	 * @description: 所有组织Map
	 * @author：yuan.yw
	 * @return     
	 * @return Map<String,SysOrg>     
	 * @date：May 30, 2013 11:24:01 AM
	 */
	public Map<String,SysOrg> getAllOrgToMap();
	/***
	 * 
	 * @description: 根据组织ids获取组织树
	 * @author：yuan.yw
	 * @param orgIds
	 * @return     
	 * @return List<Map>     
	 * @date：May 30, 2013 11:52:51 AM
	 */
	public List<Map<String,Object>> getOrgTreeDownwardByorgIds(String orgIds);
	

	public List<Map<String,Object>> getUserIdByRoleCode(long orgId,
			String code);

	/**
	 * 根据岗位Code与部门ID获取人员
	 * @author li.hb
	 * @date 2014-1-17 上午10:08:22
	 * @Description: TODO 
	 * @param @param orgId
	 * @param @param code
	 * @param @return        
	 * @throws
	 */
	public List<Map<String,Object>> getUserIdByPostCode(long orgId,String code);
	
	/**
	 * 获取iscreate公司于事业部列表
	* @author ou.jh
	* @date Jan 9, 2014 2:06:30 PM
	* @Description: TODO 
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getIscreateCompanyAndBusinessUnitList();
	/**
	 * 
	 * @description: 获取iscreate公司信息
	 * @author：yuan.yw
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Jan 10, 2014 9:51:20 AM
	 */
	public Map<String,Object> getIscreateCompanyMap();
}