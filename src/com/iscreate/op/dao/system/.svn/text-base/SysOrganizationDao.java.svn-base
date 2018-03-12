package com.iscreate.op.dao.system;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.informationmanage.InformationEnterprise;

import com.iscreate.op.pojo.organization.Staff;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.pojo.system.SysOrgAttributeExt;
import com.iscreate.op.pojo.system.SysOrgRelaArea;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.pojo.system.SysUserRelaOrg;

public interface SysOrganizationDao {
	/**
	 * 获取所有最高级的服务商组织
	 * @param enterpriseType 企业类型
	 * @return
	 */
	public List<SysOrg> getAllTheTopProviderOrg(String enterpriseType);
	
	public List<Map<String,Object>> getUserIdByRoleCode(
			String code) ;
	/**
	 * 根据企业ids  类型获取下级及本组织列表list 递归
	 * @param enterpriseIds企业ids 
	 * @param enterpriseType 企业类型
	 * 
	 * @return
	 */
	public List<Map<String,Object>> getSelfAndChildOrgsListByEnterpriseId(String enterpriseIds,String enterpriseType);
	
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
	 * 根据组织ids获取下级及本组织列表list 递归
	 * @param orgIds 组织ids 
	 * 
	 * 
	 * @return
	 */
	public List<Map<String,Object>>  getSelfAndChildOrgsListByOrgIds(String orgIds);
	/**
	 * 
	 * @description: 根据所传类型获取组织相关数据字典
	 * @author：     
	 * @return void     
	 * @date：May 8, 2013 11:47:53 AM
	 */
	public List<Map<String,Object>> getDictionaryForOrg(String dictionaryType);
	
	/**
	 * 根据orgUserId获取所在的第二级组织
	* @author ou.jh
	* @date Aug 27, 2013 2:25:11 PM
	* @Description: TODO 
	* @param @param orgUserId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUserSecondOrgByOrgUserId(long orgUserId);
	/**
	 * 
	 * @description:根据企业类型获取没有组织的企业
	 * @author：
	 * @param type 类型
	 * @return     
	 * 
	 * @return List<InformationEnterprise>     
	 * @date：May 8, 2013 2:57:23 PM
	 */
	public List<InformationEnterprise> getNoHasOrgEnterpriseMessageByType(String type);
	/**
	 * 通过组织id获取组织
	 * @param org 组织id
	 * @return
	 */
	public SysOrg getOrgByOrgId(long orgId);
	/**
	 * 通过组织id获取组织相关信息
	 * @param org 组织id
	 * @return
	 */
	public Map<String,Object> getOrganizationMessageByOrgId(long orgId);
	
	/**
	 * 通过账号获取企业
	 * @param account 账号
	 * @return
	 */
	public InformationEnterprise getAccountEnterpriseByAccount(String account);
	
	/**
	 * 
	 * @description: 根据orguserid获取关联组织列表的最上级组织
	 * @author：yuan.yw
	 * @param account
	 * @return
	 * @return List<Map<String,Object>>
	 * @date：2013-5-23
	 */
	public List<Map<String,Object>> getAssociateTopOrgByOrgUserId(long orgUserId);
	/**
	 * 保存实体
	 * @param entity 实体
	 * @return
	 */
	public Serializable saveEntity(Object entity);
	/**
	 * 更新实体
	 * @param entity 实体
	 * @return
	 */
	public void updateEntity(Object entity);
	/**
	 * 删除实体
	 * @param entity 实体
	 * @return
	 */
	public void deleteEntity(Object entity);
	/**
	 * 
	 * @description: 获取组织与区域关系
	 * @author：
	 * @param orgId
	 * @return     
	 * @return SysOrgRelaArea     
	 * @date：May 9, 2013 1:15:05 PM
	 */
	public List<SysOrgRelaArea> getSysOrgRelaAreaByorgId(long orgId);
	/**
	 * 
	 * @description: 获取组织与人员关系
	 * @author：
	 * @param orgId
	 * @return     
	 * @return SysUserRelaOrg     
	 * @date：May 9, 2013 1:15:24 PM
	 */
	public List<SysUserRelaOrg> getSysUserRelaOrgByorgId(long orgId);
	/**
	 * 
	 * @description: 获取组织拓展属性
	 * @author：
	 * @param orgId
	 * @return     
	 * @return SysUserRelaOrg     
	 * @date：May 9, 2013 1:15:24 PM
	 */
	public List<SysOrgAttributeExt> getSysOrgAttributeExtByorgId(long orgId);
	/**
	 * 
	 * @description: 根据账号获取权限组织
	 * @author：
	 * @param account
	 * @return     
	 * @return List<SysOrg>     
	 * @date：May 9, 2013 1:35:12 PM
	 */
	public List<Map<String,Object>> getAuthorityOrgByAccount(String account);
	/**
	 * 
	 * @description: 根据账号获取最高级组织
	 * @author：
	 * @param account
	 * @return     
	 * @return List<SysOrg>     
	 * @date：May 9, 2013 1:35:12 PM
	 */
	public List<Map<String,Object>> getTheTopOrgByAccount(String account);
	
	/**
	 * @author:duhw
	 * @create_time:2013-05-11
	 * @param org_id:组织标识
	 * @return List<Map>
	 */
	public List<Map<String, Object>> getAllTopOrgListByOrgId(long org_id);
	/**
	 * 
	 * @description: 根据id获取实体
	 * @author：
	 * @param account
	 * @return
	 * @return List<SysOrg>
	 * @date：May 9, 2013 1:35:12 PM
	 */
	public Object getEntityById(String idName,String idValue,String entityName);
	/**
	 * 
	 * @description: 通过组织id获取人员列表
	 * @author：
	 * @param orgId
	 * @return     
	 * @return List<SysOrgUser>     
	 * @date：May 11, 2013 6:45:20 PM
	 */
	public List<SysOrgUser> getUserListByOrgId(long orgId);
	/**
	 * 
	 * @description: 根据账号获取关联组织的最上级组织
	 * @author：yuan.yw
	 * @param account
	 * @return
	 * @return List<Map<String,Object>>
	 * @date：2013-5-23
	 */
	public List<Map<String,Object>> getAssociateTopOrgByAccount(String account);
	/**
	 * 
	 * @description: 根据组织id获取下级组织列表（包括本身）
	 * @author：yuan.yw
	 * @param orgId
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：May 27, 2013 11:07:06 AM
	 */
	public List<Map<String,Object>> getDownwardOrgListByOrgId(long orgId);
	/**
	 * 
	 * @description: 根据组织id List获取组织列表
	 * @author：yuan.yw
	 * @param orgIds
	 * @return     
	 * @return List<SysOrg>     
	 * @date：May 27, 2013 2:16:09 PM
	 */
	public List<SysOrg> getSysOrgListByOrgIds(List<String> orgIds);
	/**
	 * 
	 * @description: 通过组织id 级别标识类型（child parent）获取上一级或下一级组织列表
	 * @author：yuan
	 * @param orgId
	 * @param levelType
	 * @return     
	 * @return List<SysOrg>     
	 * @date：May 27, 2013 3:11:45 PM
	 */
	public List<SysOrg> getUpOrNextLevelOrgListByOrgIdAndLevleType(long orgId,String levelType);
	/**
	 * 
	 * @description:根据所在组织架构和组织类型 级别类型 向下或向上 获取组织架构列表
	 * @author：yuan.yw
	 * @param orgId
	 * @param typeCode
	 * @param levelType  child 向下 parent 向上
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：May 27, 2013 4:04:37 PM
	 */
	public List<Map<String,Object>> getOrgListDownwardOrUpwardByOrgTypeAndOrgId(String orgId, String typeCode,String levelType);
	/**
	 * 
	 * @description: 通过组织id 级别标识类型（child parent）组织属性 获取上一级或下一级组织列表 Map
	 * @author：yuan
	 * @param orgId
	 * @param levelType
	 * @param orgAttribute
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：May 27, 2013 3:11:45 PM
	 */
	public List<Map<String,Object>> getUpOrNextLevelOrgListByOrgIdAndLevleTypeAndOrgAttribute(long orgId,String levelType,String orgAttribute);
	/**
	 * 
	 * @description: 根据账号及组织类型获取组织列表
	 * @author：yuan.yw
	 * @param account
	 * @param orgType
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：May 28, 2013 3:08:32 PM
	 */
	public List<Map<String,Object>> getOrgListByAccountAndOrgType(String account,String orgType);
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
	public List<SysOrg> getOrgByAccount(String account);
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
	public List<Map<String,Object>> getProviderOrgByOrgIdAndOrgAttrByLevel(long orgId,String orgAttribute,String level);
	/**
	 * 
	 * @description: 获取所有可用的服务商组织
	 * @author：yuan.yw
	 * @return     
	 * @return List<SysOrg>     
	 * @date：May 30, 2013 11:32:45 AM
	 */
	public List<SysOrg> getAllProviderOrg();
	
	/**
	 * 根据组织类型与组织名字获取组织
	* @author ou.jh
	* @date Jun 17, 2013 3:08:33 PM
	* @Description: TODO 
	* @param @param orgName
	* @param @param code
	* @param @return        
	* @throws
	 */
	public List<SysOrg> getOrganizationByOrgNameAndOrgCode(String orgName,String code);
	

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
