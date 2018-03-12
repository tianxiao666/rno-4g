package com.iscreate.op.dao.system;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.constant.InformationConstant;
import com.iscreate.op.pojo.informationmanage.InformationEnterprise;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.pojo.system.SysOrgAttributeExt;
import com.iscreate.op.pojo.system.SysOrgRelaArea;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.pojo.system.SysUserRelaOrg;

public class SysOrganizationDaoImpl implements SysOrganizationDao {
	private HibernateTemplate hibernateTemplate;

	/**
	 * 获取所有最高级的组织
	 * 
	 * @param enterpriseType
	 *            企业类型
	 * @return
	 */
	public List<SysOrg> getAllTheTopProviderOrg(String enterpriseType) {
		String hql = "select o from SysOrg o,InformationEnterprise f where (o.parentId='0' or o.parentId is null) and o.status=1 and o.enterpriseId=f.id ";
		if (!"".equals(enterpriseType) && enterpriseType != null) {
			String cooperative="";
			if ("CARRIEROPERATOR".equals(enterpriseType)) {
				cooperative = InformationConstant.ENTERPRISE_TYPE_CARRIEROPERATOR;
			} else {
				cooperative = InformationConstant.ENTERPRISE_TYPE_SERVER;
			}
			hql += " and f.cooperative='" + cooperative + "'";
		}
		return (List<SysOrg>) hibernateTemplate.find(hql);
	}

	/**
	 * 根据企业id 类型获取组织列表list 
	 * 
	 * @param enterpriseIds
	 *            企业ids
	 * @param enterpriseType
	 *            企业类型
	 * 
	 * @return
	 */
	public List<Map<String,Object>> getSelfAndChildOrgsListByEnterpriseId(
			String enterpriseIds, String enterpriseType) {
		String sql = "";
		String cooperative = "";
		if ("CARRIEROPERATOR".equals(enterpriseType)) {
			cooperative = InformationConstant.ENTERPRISE_TYPE_CARRIEROPERATOR;
		} else {
			cooperative = InformationConstant.ENTERPRISE_TYPE_SERVER;
		}
		if (enterpriseIds != null && !"".equals(enterpriseIds)) {
			sql = "select * from (select "+this.getOrganizationTableAttributeAlias("Sys_Org", "o")+
			      " from Sys_Org o connect by prior o.org_Id = o.parent_Id " +
			      " start with (o.parent_Id='0' or o.parent_Id is null) " +
			      "   and o.status=1 and o.enterprise_Id in (select f.id from proj_enterprise f where f.cooperative='"
					+ cooperative + "' and f.id in (" + enterpriseIds + "))) where \"status\"=1";
			System.out.println(sql);
			return (List<Map<String,Object>>)this.executeSqlForObject(sql);
		} else {
			return null;
		}

	}
	
	public List<Map<String,Object>> getUserIdByRoleCode(
			String code) {
		String sql = "";
			sql =" select sou.name username, "
				+"   sou.org_user_id, "
				+"   sou.email, "
			    +"   sou.backupemail, "
			    +"   so.org_id, "
			    +"   so.name orgname "
				+"	  from sys_role           s, "
				+"       sys_org_user       sou, "
				+"       sys_user_rela_role ss, "
				+"       sys_account        sa, "
				+"       sys_org            so, "
				+"       sys_user_rela_org  suro "
				+" where s.role_id = ss.role_id "
				+"   and sou.org_user_id = sa.org_user_id "
				+"   and ss.org_user_id = sa.org_user_id "
				+"   and so.org_id = suro.org_id "
				+"   and suro.org_user_id = sou.org_user_id "
				+"   and s.code = '"+code+"' ";
			return (List<Map<String,Object>>)this.executeSqlForObject(sql);

	}
	
	
	
	public List<Map<String,Object>> getUserIdByRoleCode(long orgId,
			String code) {
		String sql = "";
			sql =" select distinct sou.name username, "
				+"   sou.org_user_id, "
				+"   sou.email, "
			    +"   sou.backupemail "
				+"	  from sys_role           s, "
				+"       sys_org_user       sou, "
				+"       sys_user_rela_role ss, "
				+"       sys_account        sa, "
				+"       sys_org            so, "
				+"       sys_user_rela_org  suro "
				+" where s.role_id = ss.role_id "
				+"   and sou.org_user_id = sa.org_user_id "
				+"   and ss.org_user_id = sa.org_user_id "
				+"   and so.org_id = suro.org_id "
				+"   and suro.org_user_id = sou.org_user_id "
				+"   and s.code = '"+code+"' and so.path like '%/" + orgId + "/%'";
			return (List<Map<String,Object>>)this.executeSqlForObject(sql);

	}
	
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
	public List<Map<String,Object>> getUserIdByPostCode(long orgId,String code) 
	{
		
		String sql = "select u.org_user_id, rp.post_code, rp.org_id,u.name"+
					"  from sys_org_user u,sys_user_rela_post rp,sys_org so"+
					"  where u.org_user_id = rp.org_user_id"+
					"   and rp.post_code = '"+code+"'"+
					"   and rp.status = 'A'"+
					"   and rp.org_id = so.org_id and so.path like '%/" + orgId + "/%'";
		
		System.out.println(sql);
		return (List<Map<String,Object>>)this.executeSqlForObject(sql);
	}
	
	
	


	/**
	 * 根据组织ids获取组织列表list 
	 * 
	 * @param orgIds
	 *            组织ids 数组
	 * 
	 * 
	 * @return
	 */
	public List<Map<String,Object>> getSelfAndChildOrgsListByOrgIds(String orgIds) {
		String sql = "";
		if (orgIds != null && !"".equals(orgIds)) {
			sql = "select "+this.getOrganizationTableAttributeAlias("Sys_Org", "so")+",sot.code \"orgAttrbuteType\" from (select o.*  from Sys_Org o where o.status=1 connect by prior o.org_Id = o.parent_Id start with o.org_Id in ("
					+ orgIds + ")) so left join sys_org_type sot on sot.org_type_id = so.org_type_id ";
			return (List<Map<String,Object>>) this.executeSqlForObject(sql);
		} else {
			return null;
		}

	}

	/**
	 * 
	 * @description: 根据所传类型获取组织相关数据字典
	 * @author：
	 * @return void
	 * @date：May 8, 2013 11:47:53 AM
	 */
	public List<Map<String,Object>> getDictionaryForOrg(String dictionaryType) {
		if (dictionaryType == null || "".equals(dictionaryType)) {
			return null;
		}
		String sql = "select "+this.getOrganizationTableAttributeAlias("Sys_Org_Type", "s")+" from (select st.* from Sys_Org_Type st connect by prior st.org_Type_Id = st.parent_Id start with st.code='"
				+ dictionaryType + "') s where s.code<>'"+dictionaryType+"'";
		return (List<Map<String,Object>>) this.executeSqlForObject(sql);
		
	}

	/**
	 * 
	 * @description:获取没有组织的企业
	 * @author：
	 * @return
	 * @return List<InformationEnterprise>
	 * @date：May 8, 2013 2:57:23 PM
	 */
	public List<InformationEnterprise> getNoHasOrgEnterpriseMessageByType(
			String type) {
		if (type == null || "".equals(type)) {
			return null;
		}
		String hql = "select f from InformationEnterprise f where f.cooperative='"
				+ type
				+ "' and f.id not in (select o.enterpriseId from SysOrg o where (o.parentId='0' or o.parentId is null) and o.status=1 and o.enterpriseId is not null)";
		return (List<InformationEnterprise>) hibernateTemplate.find(hql);
	}

	/**
	 * 通过组织id获取组织
	 * 
	 * @param org
	 *            组织id
	 * @return
	 */
	public SysOrg getOrgByOrgId(long orgId) {
		String hql = "from SysOrg o where o.orgId=" + orgId;
		List<SysOrg> list = hibernateTemplate.find(hql);
		SysOrg org = null;
		if (list != null && list.size() == 1) {
			org = list.get(0);
		}
		return org;
	}

	/**
	 * 通过组织id获取组织相关信息
	 * 
	 * @param org
	 *            组织id
	 * @return
	 */
	public Map<String, Object> getOrganizationMessageByOrgId(long orgId) {//yuan.yw 修改 组织类型获取
		if("".equals(orgId+"")||"0".equals(orgId+"")){
			return null;
		}
		String sql = "select a.area_Id \"areaId\",a.name \"areaName\","+this.getOrganizationTableAttributeAlias("Sys_Org", "o")+",u.name \"dutyPersonName\",f.fullName \"enterpriseName\",so.name \"parentOrgName\",sd.name \"type\",st2.name \"orgAttribute\",st2.code \"orgAttributeCode\" from Sys_Org o "
				+" left join Sys_Org_User u on u.org_User_Id = o.org_User_Id "
				+" left join proj_enterprise f on f.id = o.enterprise_Id "
				+" left join Sys_Org so on so.org_Id = o.parent_Id "
				+" left join Sys_Org_type st1 on st1.org_Type_Id = o.org_Type_Id "
				+" left join Sys_Org_type st2 on st2.org_Type_Id = o.business_Type_Id "
				+" left join Sys_Org_Rela_Area sa on sa.org_Id = o.org_Id "
				+" left join Sys_Area a on a.area_Id = sa.area_Id "
				+" left join Sys_Dictionary sd on sd.code = o.org_type "
				+ " where o.org_Id="+orgId;
		List<Map<String, Object>> list = this.executeSqlForObject(sql);
		Map<String, Object> mp = null;
		if (list != null && list.size() > 0) {
			mp = list.get(0);
		}
		return mp;
	}

	/**
	 * 通过账号获取企业
	 * 
	 * @param account
	 *            账号
	 * @return
	 */
	public InformationEnterprise getAccountEnterpriseByAccount(String account) {
		if ("".equals(account)) {
			return null;
		}
		String hql = "select f from SysAccount a,SysOrgUser u, SysUserRelaOrg so,SysOrg o,InformationEnterprise f where a.account='"
				+ account
				+ "' and a.orgUserId=u.orgUserId and u.orgUserId=so.orgUserId and so.orgId=o.orgId and o.enterpriseId=f.id";
		List<InformationEnterprise> list = hibernateTemplate.find(hql);
		InformationEnterprise enterprise = null;
		if (list != null && list.size() == 1) {
			enterprise = list.get(0);
		}
		return enterprise;
	}
	
	/**
	 * 根据orgUserId获取所在公司的第二级组织
	* @author ou.jh
	* @date Aug 27, 2013 2:25:11 PM
	* @Description: TODO 
	* @param @param orgUserId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getAllSecondOrgListByOrgUserId(long orgUserId){
		String sql = 
					"	select distinct o.* "
					+"	  from sys_org o "
					+"	 where o.parent_id in "
					+"	       (select o.org_id "
					+"	          from sys_org o "
					+"	         where (o.parent_id = 0 or o.parent_id is null) "
					+"	           and o.enterprise_id in ( "
                                   
					+"	                                   select o.enterprise_id "
					+"	                                     from proj_enterprise   f, "
					+"	                                           sys_org_user      u, "
					+"	                                           sys_user_rela_org so, "
					+"	                                           sys_org           o "
					+"	                                    where u.org_user_id = so.org_user_id "
					+"	                                      and so.org_id = o.org_id "
					+"	                                      and o.enterprise_id = f.id "
					+"	                                      and u.org_user_id = "+orgUserId+")) ";
	
		return (List<Map<String,Object>>) this.executeSqlForObject(sql);
	}
	
	/**
	 * 根据orgUserId获取所在的第二级组织
	* @author ou.jh
	* @date Aug 27, 2013 2:25:11 PM
	* @Description: TODO 
	* @param @param orgUserId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUserSecondOrgByOrgUserId(long orgUserId){
		String sql = 
					"select distinct o.* "
					+"  from sys_org o, "
					+"       (select substr(fn.path, "
					+"                      instr(fn.path, '/', instr(fn.path, '/', 1, 1) + 1, 1) + 1, "
					+"                      instr(fn.path, "
					+"                            '/', "
					+"                            instr(fn.path, "
					+"                                 '/', "
					+"                                  instr(fn.path, '/', 1, 1) + 1, "
					+"                                  1) + 1, "
					+"                            1) - 1 - "
					+"                      instr(fn.path, '/', instr(fn.path, '/', 1, 1) + 1, 1)) pp "
					+"         from sys_org_user u, sys_user_rela_org so, sys_org fn "
					+"         where u.org_user_id = so.org_user_id "
					+"           and so.org_id = fn.org_id "
					+"           and u.org_user_id = " + orgUserId + ") o1 "
					+" where o.org_id = o1.pp ";
		return (List<Map<String,Object>>) this.executeSqlForObject(sql);
	}
	
	


	/**
	 * 保存实体
	 * 
	 * @param entity
	 *            实体
	 * @return
	 */
	public Serializable saveEntity(Object entity) {
		Serializable s =hibernateTemplate.save(entity);
		
		return s;
	}

	/**
	 * 更新实体
	 * 
	 * @param entity
	 *            实体
	 * @return
	 */
	public void updateEntity(Object entity) {
		hibernateTemplate.update(entity);
	}

	/**
	 * 删除实体
	 * 
	 * @param entity
	 *            实体
	 * @return
	 */
	public void deleteEntity(Object entity) {
		hibernateTemplate.delete(entity);
	}

	/**
	 * 
	 * @description: 获取组织与区域关系
	 * @author：
	 * @param orgId
	 * @return
	 * @return SysOrgRelaArea
	 * @date：May 9, 2013 1:15:05 PM
	 */
	public List<SysOrgRelaArea> getSysOrgRelaAreaByorgId(long orgId) {
		String hql = "from SysOrgRelaArea o where o.orgId=" + orgId;
		List<SysOrgRelaArea> list = hibernateTemplate.find(hql);
		return list;
	}

	/**
	 * 
	 * @description: 获取组织与人员关系
	 * @author：
	 * @param orgId
	 * @return
	 * @return SysUserRelaOrg
	 * @date：May 9, 2013 1:15:24 PM
	 */
	public List<SysUserRelaOrg> getSysUserRelaOrgByorgId(long orgId) {
		String hql = "from SysUserRelaOrg o where o.orgId=" + orgId;
		List<SysUserRelaOrg> list = hibernateTemplate.find(hql);
		return list;
	}

	/**
	 * 
	 * @description: 获取组织拓展属性
	 * @author：
	 * @param orgId
	 * @return
	 * @return SysUserRelaOrg
	 * @date：May 9, 2013 1:15:24 PM
	 */
	public List<SysOrgAttributeExt> getSysOrgAttributeExtByorgId(long orgId) {
		String hql = "from SysOrgAttributeExt o where o.orgId=" + orgId;
		List<SysOrgAttributeExt> list = hibernateTemplate.find(hql);
		return list;
	}

	/**
	 * 
	 * @description: 根据账号获取权限组织
	 * @author：
	 * @param account
	 * @return
	 * @return List<SysOrg>
	 * @date：May 9, 2013 1:35:12 PM
	 */
	public List<Map<String,Object>> getAuthorityOrgByAccount(String account) {
		if ("".equals(account)) {
			return null;
		}
		String sql = " select "+this.getOrganizationTableAttributeAlias("Sys_Org", "o")+" from Sys_Org o connect by prior o.org_Id=o.parent_Id start with o.org_Id in (select so.org_Id from Sys_Account a,Sys_Org_User u, Sys_User_Rela_Org so where a.account='"
				+ account
				+ "' and a.org_User_Id=u.org_User_Id and u.org_User_Id=so.org_User_Id )";
		return (List<Map<String,Object>>) this.executeSqlForObject(sql);
	}
	/**
	 * 
	 * @description: 根据id获取实体
	 * @author：
	 * @param account
	 * @return
	 * @return List<SysOrg>
	 * @date：May 9, 2013 1:35:12 PM
	 */
	public Object getEntityById(String idName,String idValue,String entityName) {
		if("".equals("idName")||"".equals("idValue")||"".equals("entityName")){
			return null;
		}
		String hql = "from "+entityName+" e where e."+idName+"="+idValue;
		List list = hibernateTemplate.find(hql);
		Object o = null;
		if(list!=null && list.size()>0){
			o = list.get(0);
		}
		return o;
	}
	/**
	 * 
	 * @description: 根据账号获取最高级组织
	 * @author：
	 * @param account
	 * @return
	 * @return List<SysOrg>
	 * @date：May 9, 2013 1:35:12 PM
	 */
	public List<Map<String,Object>> getTheTopOrgByAccount(String account) {
		if ("".equals(account)) {
			return null;
		}
		String sql = " select "+this.getOrganizationTableAttributeAlias("Sys_Org", "s")+" from (select o.* from Sys_Org o connect by prior o.parent_Id=o.org_Id start with o.org_Id in (select so.org_Id from Sys_Account a,Sys_Org_User u, Sys_User_Rela_Org so where a.account='"
				+ account
				+ "' and a.org_User_Id=u.org_User_Id and u.org_User_Id=so.org_User_Id )) s where s.parent_Id=0 or s.parent_Id is null";
		List<Map<String,Object>> list = this.executeSqlForObject(sql);
		return list;
	}
	
	/**
	 * 
	 * @author:duhw
	 * @create_time:2013-05-11
	 * @根据组织标识得到所有上级组织
	 * org_id：组织标识
	 * @return List<Map>
	 */
	public List<Map<String,Object>> getAllTopOrgListByOrgId(long org_id) {
		if (org_id == 0) {
			return null;
		}
		String sql = " select level,"+this.getOrganizationTableAttributeAlias("Sys_Org", "s")+" from sys_org s " +
				     " connect by prior s.parent_id=s.org_id"+
                     " start with org_id="+org_id+" order by level desc";
		List<Map<String,Object>> list = this.executeSqlForObject(sql);
		return list;
	}
	



	/**
	 * 
	 * @description: 通过组织id获取人员列表
	 * @author：
	 * @param orgId
	 * @return     
	 * @return List<SysOrgUser>     
	 * @date：May 11, 2013 6:45:20 PM
	 */
	public List<SysOrgUser> getUserListByOrgId(long orgId){
		if("0".equals(orgId)||"".equals(orgId)){
			return null;
		}
		String hql = "select u from SysOrgUser u,SysUserRelaOrg sr where u.orgUserId=sr.orgUserId and sr.orgId="+orgId;
		return (List<SysOrgUser>) hibernateTemplate.find(hql);
	}
	
	/**
	 * 
	 * @description: 根据账号获取关联组织列表的最上级组织
	 * @author：yuan.yw
	 * @param account
	 * @return
	 * @return List<Map<String,Object>>
	 * @date：2013-5-23
	 */
	public List<Map<String,Object>> getAssociateTopOrgByAccount(String account) {
		if ("".equals(account)) {
			return null;
		}
		String sql = "select "+this.getOrganizationTableAttributeAlias("Sys_Org", "o")+" from " +
					"(select s.* from Sys_Account a, Sys_Org_User u, Sys_User_Rela_Org so,Sys_Org s "+
					" where a.account = '"+account+"' "+
					" and a.org_User_Id = u.org_User_Id "+
					" and u.org_User_Id = so.org_User_Id and s.org_id=so.org_id) o " +
					" where (o.parent_id not in ( select so.org_Id from Sys_Account a,Sys_Org_User u, Sys_User_Rela_Org so where a.account='"
					+ account
					+ "' and a.org_User_Id=u.org_User_Id and u.org_User_Id=so.org_User_Id) " +
					" or o.parent_id is null)";
		List<Map<String,Object>> list = this.executeSqlForObject(sql);
		return list;
	}
	
	/**
	 * 
	 * @description: 根据orguserid获取关联组织列表的最上级组织
	 * @author：yuan.yw
	 * @param account
	 * @return
	 * @return List<Map<String,Object>>
	 * @date：2013-5-23
	 */
	public List<Map<String,Object>> getAssociateTopOrgByOrgUserId(long orgUserId) {
		String sql = "select "+this.getOrganizationTableAttributeAlias("Sys_Org", "o")+" from " +
					"(select s.* from  Sys_Org_User u, Sys_User_Rela_Org so,Sys_Org s "+
					" where u.org_User_Id = "+orgUserId+" "+
					" and u.org_User_Id = so.org_User_Id and s.org_id=so.org_id) o " +
					" where (o.parent_id not in ( select so.org_Id from Sys_Org_User u, Sys_User_Rela_Org so where u.org_User_Id = "
					+ orgUserId
					+ " and u.org_User_Id=so.org_User_Id) " +
					" or o.parent_id is null)";
		List<Map<String,Object>> list = this.executeSqlForObject(sql);
		return list;
	}
	/**
	 * 
	 * @description: 根据组织id获取下级组织列表（包括本身）
	 * @author：yuan.yw
	 * @param orgId
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：May 27, 2013 11:07:06 AM
	 */
	public List<Map<String,Object>> getDownwardOrgListByOrgId(long orgId){
		if(orgId==0){
			return null;
		}
		String sql = "select "+this.getOrganizationTableAttributeAlias("Sys_Org", "so")+",st.code \"type\" from (select o.* from Sys_Org o connect by prior o.org_id = o.parent_id start with o.org_id="+orgId+") so left join Sys_Org_type st on st.org_Type_Id = so.org_Type_Id";
		return (List<Map<String,Object>>)this.executeSqlForObject(sql);
	}
	
	/**
	 * 
	 * @description: 根据组织id List获取组织列表
	 * @author：yuan.yw
	 * @param orgIds
	 * @return     
	 * @return List<SysOrg>     
	 * @date：May 27, 2013 2:16:09 PM
	 */
	public List<SysOrg> getSysOrgListByOrgIds(List<String> orgIds){
		if(!(orgIds!=null && !orgIds.isEmpty())){
			return null;
		}
		String hql = " from SysOrg o where 1=1 and (";
		StringBuffer sf = new StringBuffer();
		int indexFlag=0;
		for(int i=0;i<orgIds.size();i++){
			if(indexFlag>50 || indexFlag>=orgIds.size()-1){
				sf.append(","+orgIds.get(i));
				if(i==0){
					hql += " o.orgId in ("+sf.substring(1)+")";
				}else{
					hql += " or o.orgId in ("+sf.substring(1)+")";
				}
				indexFlag=0;
				sf = new StringBuffer();
			}else{
				sf.append(","+orgIds.get(i));
				indexFlag++;
			}
		}
		hql+=")";
		return (List<SysOrg>)this.hibernateTemplate.find(hql);
	}
	
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
	public List<SysOrg> getUpOrNextLevelOrgListByOrgIdAndLevleType(long orgId,String levelType){
		if(orgId==0||levelType==null||"".equals(levelType)){
			return null;
		}
		String hql = "";
		if("child".equals(levelType)){
			hql = "select o from SysOrg o,SysOrg so where o.parentId=so.orgId and so.orgId="+orgId;
		}else if("parent".equals(levelType)){
			hql = "select o from SysOrg o,SysOrg so where o.orgId=so.parentId and so.orgId="+orgId;
		}
		return (List<SysOrg>)this.hibernateTemplate.find(hql);
	}
	
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
	public List<Map<String,Object>> getOrgListDownwardOrUpwardByOrgTypeAndOrgId(String orgId, String typeCode,String levelType){
		if(orgId==null||"0".equals(orgId)|| "".equals(orgId)|| typeCode==null || "".equals(typeCode)|| levelType==null || "".equals(levelType)){
			return null;
		}
		String sql = "";
		if("child".equals(levelType)){
			sql = " select "+this.getOrganizationTableAttributeAlias("Sys_Org", "so")+",u.name \"dutyPersonName\" from (select o.* from Sys_Org o connect by prior o.org_id = o.parent_id start with o.org_id in("+orgId+") ) so  left join Sys_Org_User u on u.org_User_Id = so.org_User_Id where so.org_type_id in (select st.org_type_id from Sys_Org_type st where st.code='"+typeCode+"' )";
		}else if("parent".equals(levelType)){
			sql = " select "+this.getOrganizationTableAttributeAlias("Sys_Org", "so")+",u.name \"dutyPersonName\" from (select o.* from Sys_Org o connect by prior o.parent_id = o.org_id start with o.org_id in("+orgId+")  ) so  left join Sys_Org_User u on u.org_User_Id = so.org_User_Id where so.org_type_id in (select st.org_type_id from Sys_Org_type st where st.code='"+typeCode+"' )";
		}else{
			sql = " select "+this.getOrganizationTableAttributeAlias("Sys_Org", "o")+",u.name \"dutyPersonName\" from  Sys_Org o  left join Sys_Org_User u on u.org_User_Id = o.org_User_Id where  o.org_id in("+orgId+") and o.org_type_id in (select st.org_type_id from Sys_Org_type st where st.code='"+typeCode+"' ) ";
		}
		return (List<Map<String,Object>>)this.executeSqlForObject(sql);
	}
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
	public List<Map<String,Object>> getUpOrNextLevelOrgListByOrgIdAndLevleTypeAndOrgAttribute(long orgId,String levelType,String orgAttribute){
		if(orgId==0||levelType==null||"".equals(levelType)||orgAttribute==null||"".equals(orgAttribute)){
			return null;
		}
		String sql = "";
		if("child".equals(levelType)){
			sql = "select "+this.getOrganizationTableAttributeAlias("Sys_Org", "o")+",st.code \"type\" from Sys_Org o,Sys_Org so,Sys_Org_type st where st.org_Type_Id = o.business_Type_Id and st.code='"+orgAttribute+"'   and o.parent_Id=so.org_Id and so.org_Id="+orgId;
		}else if("parent".equals(levelType)){
			sql = "select "+this.getOrganizationTableAttributeAlias("Sys_Org", "o")+",st.code \"type\" from Sys_Org o,Sys_Org so,Sys_Org_type st where st.org_Type_Id = o.business_Type_Id  and st.code='"+orgAttribute+"'  and o.org_Id=so.parent_Id and so.org_Id="+orgId;
		}
		return (List<Map<String,Object>>)this.executeSqlForObject(sql);
	}
	
	/**
	 * 
	 * @description: 根据账号及组织类型获取组织列表
	 * @author：yuan.yw
	 * @param account
	 * @param orgType
	 * @return     
	 * @return List<Map<String,Object>     
	 * @date：May 28, 2013 3:08:32 PM
	 */
	public List<Map<String,Object>> getOrgListByAccountAndOrgType(String account,String orgType){
		if(account==null || "".equals(account)||orgType==null ||"".equals(orgType)){
			return null;
		}
		String sql = "select "+this.getOrganizationTableAttributeAlias("Sys_Org", "sor")+" from Sys_Org sor,Sys_Org_Type st where sor.org_Type_Id = st.org_Type_Id and " 
				    + "sor.org_Id in (select so.org_id from Sys_Org so connect by prior so.org_id = so.parent_id start with so.org_id in " 
					+" (select o.org_id from Sys_Org o,Sys_Org_User su ,Sys_User_Rela_Org sr,Sys_Account sa,Sys_Org_Type st" 
					+" where sa.account='"+account+"'"
					+" and sa.org_User_Id= su.org_User_Id "
					+" and su.org_User_Id = sr.org_User_Id"
					+" and o.org_Id=sr.org_Id ) union "
					+"select so.org_id from Sys_Org so connect by prior so.parent_id = so.org_id start with so.org_id in " 
					+" (select o.org_id from Sys_Org o,Sys_Org_User su ,Sys_User_Rela_Org sr,Sys_Account sa,Sys_Org_Type st" 
					+" where sa.account='"+account+"'"
					+" and sa.org_User_Id= su.org_User_Id "
					+" and su.org_User_Id = sr.org_User_Id"
					+" and o.org_Id=sr.org_Id )) " 
					+ "and sor.parent_Id in (select so.org_id from Sys_Org so connect by prior so.org_id = so.parent_id start with so.org_id in " 
					+" (select o.org_id from Sys_Org o,Sys_Org_User su ,Sys_User_Rela_Org sr,Sys_Account sa,Sys_Org_Type st" 
					+" where sa.account='"+account+"'"
					+" and sa.org_User_Id= su.org_User_Id "
					+" and su.org_User_Id = sr.org_User_Id"
					+" and o.org_Id=sr.org_Id ) union "
					+"select so.org_id from Sys_Org so connect by prior so.parent_id = so.org_id start with so.org_id in " 
					+" (select o.org_id from Sys_Org o,Sys_Org_User su ,Sys_User_Rela_Org sr,Sys_Account sa,Sys_Org_Type st" 
					+" where sa.account='"+account+"'"
					+" and sa.org_User_Id= su.org_User_Id "
					+" and su.org_User_Id = sr.org_User_Id"
					+" and o.org_Id=sr.org_Id )) " 
					+"  and st.code='"+orgType+"'";
		/*String hql = "select o from SysOrg o,SysOrgUser su ,SysUserRelaOrg sr,SysAccount sa,SysOrgType st" 
					+" where sa.account='"+account+"'"
					 +" and sa.orgUserId= su.orgUserId "
					 +" and su.orgUserId = sr.orgUserId"
					 +" and o.orgId=sr.orgId"
					 +" and o.orgTypeId = st.orgTypeId"
					 +" and st.code='"+orgType+"'";
		return (List<SysOrg>)this.hibernateTemplate.find(hql);*/
		return (List<Map<String,Object>>)this.executeSqlForObject(sql);
		
	}
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
	public List<Map<String,Object>> getProviderStaffByOrgIdAndRoleCode(long orgId,String roleCode,String level){
		if(orgId==0||roleCode==null ||"".equals(roleCode)){
			return null;
		}
		List<Map<String,Object>> resultList = null;
		String sql = "select f.* from staff f,sys_account sa,sys_user_rela_role sur,sys_role sr,sys_org_user so,sys_user_rela_org sro"
					+" where  sr.code = '"+roleCode+"'"
					+" and sr.role_id = sur.role_id"
					+" and sur.account_id = sa.account_id"
					+" and sa.account = f.account"
					+" and sa.org_user_id = so.org_user_id"
					+" and so.org_user_id = sro.org_user_id"
					+" and sro.org_id in";
		if("upward".equals(level)){//向上
			sql+="(select o.org_id from sys_org o connect by prior o.parent_id = o.org_id start with o.org_id = "+orgId+")";
			resultList = this.executeSqlForObject(sql);
		}else if("downward".equals(level)){//向下
			sql+="(select o.org_id from sys_org o connect by prior o.org_id = o.parent_id start with o.org_id = "+orgId+")";
			resultList = this.executeSqlForObject(sql);
		}else if("downOrUpward".equals(level)){//向下或向上
			sql+="(select o.org_id from sys_org o connect by prior o.org_id = o.parent_id start with o.org_id = "+orgId+")";
			resultList = this.executeSqlForObject(sql);
			if(!(resultList!=null && !resultList.isEmpty())){
				sql+="(select o2.org_id from sys_org o,sys_org o2 where o.org_id="+orgId+" and o2.org_id=o.parent_id)";
				resultList = this.executeSqlForObject(sql);
				if(!(resultList!=null && !resultList.isEmpty())){
					sql+="(select o.org_id from sys_org o connect by prior o.parent_id = o.org_id start with o.org_id = "+orgId+")";
					resultList = this.executeSqlForObject(sql);
				}
			}
		}else{//向上 向下
			sql+="(select o.org_id from sys_org o connect by prior o.org_id = o.parent_id start with o.org_id = "+orgId
				 +" union select o.org_id from sys_org o connect by prior o.parent_id = o.org_id start with o.org_id = "+orgId+")";
			resultList = this.executeSqlForObject(sql);
		}
		return resultList;
	}
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
			 String account,String roleCode){
		if(account==null ||"".equals(account)||roleCode==null ||"".equals(roleCode)){
			return null;
		}
		String sql = "select distinct "+this.getOrganizationTableAttributeAlias("Sys_Org", "sg")+" from  (select sog.* from sys_org sog where sog.status=1 connect by prior sog.org_id=sog.parent_id start with sog.org_id in ( select o.org_id from sys_org o,sys_account sa,sys_org_user so,sys_user_rela_org sro"
					+" where  sa.account ='"+account+"'"
					+" and sa.org_user_id = so.org_user_id"
					+" and so.org_user_id = sro.org_user_id"
					+" and sro.org_id = o.org_id )  union "
					+"select sog.* from sys_org sog where sog.status=1 connect by prior sog.parent_id=sog.org_id start with sog.org_id in ( select o.org_id from sys_org o,sys_account sa,sys_org_user so,sys_user_rela_org sro"
					+" where   sa.account ='"+account+"'"
					+" and sa.org_user_id = so.org_user_id"
					+" and so.org_user_id = sro.org_user_id"
					+" and sro.org_id = o.org_id )) sg," 
					+"sys_account sa1,sys_user_rela_role sur1,sys_role sr1,sys_org_user so1,sys_user_rela_org sro1 "
					+" where  sr1.code = '"+roleCode+"'"
					+" and sr1.role_id = sur1.role_id"
					+" and sur1.account_id = sa1.account_id"
					+" and sa1.org_user_id = so1.org_user_id"
					+" and so1.org_user_id = sro1.org_user_id"
					+" and sro1.org_id = sg.org_id order by sg.path desc";
		return (List<Map<String,Object>>)this.executeSqlForObject(sql);
		
	}
	/**
	 * 
	 * @description: 根据账号获取关联组织
	 * @author：yuan.yw
	 * @param account
	 * @return     
	 * @return List<SysOrg>     
	 * @date：May 29, 2013 3:32:06 PM
	 */
	public List<SysOrg> getOrgByAccount(String account){
		if(account==null ||"".equals(account)){
			return null;
		}
		String hql = "select o from SysOrg o,SysAccount sa,SysOrgUser su,SysUserRelaOrg sur"
			+" where sa.account = '"+account+"'"
			+" and sa.orgUserId = su.orgUserId"
			+" and su.orgUserId = sur.orgUserId"
			+" and sur.orgId = o.orgId";
		return (List<SysOrg>)this.hibernateTemplate.find(hql);
	}
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
	public List<Map<String,Object>> getProviderOrgByOrgIdAndOrgAttrByLevel(long orgId,String orgAttribute,String level){
		if(orgId==0 ||orgAttribute==null ||"".equals(orgAttribute)||level==null ||"".equals(level)){
			return null;
		}
		String sql ="";
		if("up".equals(level)){
			sql="select "+this.getOrganizationTableAttributeAlias("Sys_Org", "o2")+" from sys_org o,sys_org o2,sys_org_type st" 
			+" where o.org_id="+orgId+" "
			+" and o2.org_id=o.parent_id "
			+" and st.org_type_id=o2.business_type_id "
			+" and st.code ='"+orgAttribute+"'";
		}else if("next".equals(level)){
			sql="select "+this.getOrganizationTableAttributeAlias("Sys_Org", "o")+" from sys_org o,sys_org_type st" 
			+" where o.parent_id="+orgId+""  
			+" and st.org_type_id=o.business_type_id"
			+" and st.code ='"+orgAttribute+"'";
		}else if("upward".equals(level)){
			sql="select "+this.getOrganizationTableAttributeAlias("Sys_Org", "so")+" from sys_org so,sys_org_type st"
				+" where st.code = '"+orgAttribute+"'"
				+" and so.org_id in"
				+" (select o.org_id "
				+" from sys_org o"
				+" connect by prior o.parent_id = o.org_id"
				+" start with o.org_id="+orgId+")";
		}else if("downward".equals(level)){
			sql="select "+this.getOrganizationTableAttributeAlias("Sys_Org", "so")+" from sys_org so,sys_org_type st"
				+" where st.code = '"+orgAttribute+"'"
				+" and so.org_id in"
				+" (select o.org_id "
				+" from sys_org o"
				+" connect by prior o.org_id = o.parent_id"
				+" start with o.org_id="+orgId+")";
		}
		return (List<Map<String,Object>>)this.executeSqlForObject(sql);
	}
	/**
	 * 
	 * @description: 获取所有可用的服务商组织
	 * @author：yuan.yw
	 * @return     
	 * @return List<SysOrg>     
	 * @date：May 30, 2013 11:32:45 AM
	 */
	public List<SysOrg> getAllProviderOrg(){
		String hql = "select o from SysOrg o,InformationEnterprise f where  o.status=1 and o.enterpriseId=f.id "
		             +" and f.cooperative='" + InformationConstant.ENTERPRISE_TYPE_SERVER + "'";
		return (List<SysOrg>) hibernateTemplate.find(hql);
	}
	public List executeSqlForObject(final String sqlString) {
		List<Map<String, Object>> list = hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(
							Session session) throws HibernateException,
							SQLException {
						SQLQuery query = session.createSQLQuery(sqlString);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List find = query.list();
						return find;
					}
				});
		return list;
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	public String getOrganizationTableAttributeAlias(String tableName,String tableAliasName){
		String s = "";
		if(!"".equals(tableAliasName)){
			tableAliasName+=".";
		}
		if("Sys_Org".equalsIgnoreCase(tableName)){
			s=tableAliasName+"org_Id \"orgId\","
			+tableAliasName+"name \"name\","
			+tableAliasName+"tel \"tel\","
			+tableAliasName+"mobile \"mobile\","
			+tableAliasName+"email \"email\","
			+tableAliasName+"address \"address\","
			+tableAliasName+"description \"description\","
			+tableAliasName+"parent_Id \"parentId\","
			+tableAliasName+"path \"path\","
			+tableAliasName+"org_Type_Id \"orgTypeId\","
			+tableAliasName+"business_Type_Id \"businessTypeId\","
			+tableAliasName+"status \"status\","
			+tableAliasName+"org_User_Id \"orgUserId\","
			+tableAliasName+"org_User_Tel \"orgUserTel\","
			+tableAliasName+"longitude \"longitude\","
			+tableAliasName+"latitude \"latitude\","
			+tableAliasName+"createtime \"createtime\","
			+tableAliasName+"updatetime \"updatetime\","
			+tableAliasName+"enterprise_Id \"enterpriseId\"";	
		}else if("Sys_Org_Type".equalsIgnoreCase(tableName)){
			s=tableAliasName+"org_Type_Id \"orgTypeId\","
			+tableAliasName+"code \"code\","
			+tableAliasName+"name \"name\","
			+tableAliasName+"parent_Id \"parentId\","
			+tableAliasName+"createtime \"createtime\","
			+tableAliasName+"updatetime \"updatetime\","
			+tableAliasName+"status \"status\"";
		}
		return s;
	}

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
	public List<SysOrg> getOrganizationByOrgNameAndOrgCode(String orgName,String code){
		String hql = "select so from SysOrg so , SysOrgType sot where sot.orgTypeId = so.orgTypeId and sot.code = '"+code+"' and so.name like '%"+orgName+"%'";
		List<SysOrg> list = hibernateTemplate.find(hql);
		return list;
	}
	
	/**
	 * 获取iscreate公司于事业部列表
	* @author ou.jh
	* @date Jan 9, 2014 2:06:30 PM
	* @Description: TODO 
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getIscreateCompanyAndBusinessUnitList(){
		String sql = "select so.* "
					 +" from sys_org so, "
					 +"      (select * from sys_dictionary where code = 'ISCREATE_ORG_ID') sd "
					 +" where so.org_id = sd.name "
					 +"   or so.parent_id = sd.name order by so.org_id ";
		return this.executeSqlForObject(sql);
	}
	
	/**
	 * 
	 * @description: 获取iscreate公司信息
	 * @author：yuan.yw
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Jan 10, 2014 9:51:20 AM
	 */
	public Map<String,Object> getIscreateCompanyMap(){
		String sql = " select so.* "
			 +" from sys_org so, "
			 +" (select * from sys_dictionary where code = 'ISCREATE_ORG_ID') sd "
			 +" where so.org_id = sd.name ";
		List<Map<String,Object>> list =  this.executeSqlForObject(sql);
		if(list!=null && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	
}
