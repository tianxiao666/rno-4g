package com.iscreate.op.dao.system;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.dao.common.BaseDao;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.pojo.system.SysOrgUserPm;
import com.iscreate.op.pojo.system.SysRole;
import com.iscreate.op.pojo.system.SysUserRelaPost;
import com.iscreate.op.pojo.system.SysUserRelaRole;

public class SysOrgUserDaoImpl extends BaseDao implements SysOrgUserDao {
	
	private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private HibernateTemplate hibernateTemplate;
	
	private SysAreaDao sysAreaDao;
	
	

	public SysAreaDao getSysAreaDao() {
		return sysAreaDao;
	}

	public void setSysAreaDao(SysAreaDao sysAreaDao) {
		this.sysAreaDao = sysAreaDao;
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	
	

	/**
	 * 根据账号获取人员信息
	 * 
	 * @param account
	 * @return
	 */
	public SysOrgUser getSysStaffByOrgUserId(long orgUserId) {
		String hql = "from SysOrgUser s where s.orgUserId=" + orgUserId + "";
		List<SysOrgUser> list = (List<SysOrgUser>) hibernateTemplate.find(hql);
		SysOrgUser sysOrgUser = null;
		if (list != null && list.size() > 0) {
			sysOrgUser = list.get(0);
		}
		return sysOrgUser;
	}
	
	
	
	/**
	 * 根据账号获取人员信息(1.2.1)
	 * 
	 * @param account
	 * @return
	 */
	public SysOrgUserPm getSysOrgUserPOJOByOrgUserId(long orgUserId) {
		String hql = "from SysOrgUserPm s where s.orgUserId=" + orgUserId + "";
		List<SysOrgUserPm> list = (List<SysOrgUserPm>) hibernateTemplate.find(hql);
		SysOrgUserPm sysOrgUser = null;
		if (list != null && list.size() > 0) {
			sysOrgUser = list.get(0);
		}
		return sysOrgUser;
	}
	
	



	
	/**
	 * 保存账号所属角色关系
	* @date May 14, 2013 5:02:07 PM
	* @Description: TODO 
	* @param @param sysUserRelaRole
	* @param @return        
	* @throws
	 */
	public Serializable savaSysUserRelaRole(SysUserRelaRole sysUserRelaRole){
		Serializable serializable = this.hibernateTemplate.save(sysUserRelaRole);
		return serializable;
	}

	/**
	 * 根据组织Id获取组织与账号的关系
	 * 
	 * @date May 7, 2013 2:16:09 PM
	 * @Description: TODO
	 * @param
	 * @param orgId
	 * @param
	 * @return
	 * @throws
	 */
	public int getOrgAccountRelationToPageTotalByOrgId(long orgId) {
		// final String sqlString = "select a.* from system_authority_account a
		// inner join org_account_relation o on (a.account=o.account) where
		// a.status=1 and o.orgId="+orgId;
		// List<Account> list = hibernateTemplate.execute(new
		// HibernateCallback<List<Account>>() {
		// public List<Account> doInHibernate(Session session) throws
		// HibernateException, SQLException {
		// SQLQuery query = session.createSQLQuery(sqlString);
		// query.addEntity(Account.class);
		// List<Account> find = query.list();
		// return find;
		// }
		// });
		// return list;
		final String sqlString = "select count(1) from SYS_ORG_USER a inner join SYS_USER_RELA_ORG o on (a.ORG_USER_ID=o.ORG_USER_ID) inner join sys_account sa on (a.org_user_id = sa.org_user_id) where a.status=1 and o.ORG_ID="
				+ orgId;
		Integer count = hibernateTemplate
				.execute(new HibernateCallback<Integer>() {
					public Integer doInHibernate(Session session)
							throws HibernateException, SQLException {
						SQLQuery query = session.createSQLQuery(sqlString);
						int num = Integer.valueOf(query.uniqueResult() + "");
						return num;
					}
				});
		return count;
	}
	
	
	
	/**
	 * 
	 * 根据组织ID获取人员名称
	 * 
	 * @param orgId
	 * @date 2013-08-23
	 * @author Li.hb
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getUserNameByOrgId(String orgId)
	{
		List<Map<String, Object>>  list = null;
		
		if("".equals(orgId))
		{
			return list;
		}
		
		String sql = "select min(a.name) || '(' || substr(min(acc.account), 0,(instr(min(acc.account), '@')) - 1） || ')' \"label\", min(a.name) || '(' || substr(min(acc.account), 0,(instr(min(acc.account), '@')) - 1） || ')' \"value\", a.org_user_id user_id from SYS_ORG_USER a, SYS_USER_RELA_ORG o, sys_org so, sys_account acc where a.ORG_USER_ID = o.ORG_USER_ID and a.status = 1 and o.status = 'A' and o.ORG_ID = so.org_id and a.org_user_id = acc.org_user_id and so.path like '%/"+orgId+"/%' group by a.org_user_id ";
		
		list = this.executeSqlForList(sql, hibernateTemplate);
		
		return list;
	}

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
	 * @param isPage 是否分页
	 * @return
	 * @throws
	 */
	public List<Map<String, Object>> getAccountToPageByOrgId(final long orgId,
			final int currentPage, final int pageSize , final String conditions, boolean isPage) {
		String sqlString = " SELECT * "
							+"  FROM (SELECT A.*, ROWNUM RN "
							+"          FROM (select o.org_user_id \"org_user_id\", "
							+"                       o.name \"name\", "
							+"                       o.tel \"tel\", "
							+"                       o.mobile \"mobile\", "
							+"                       o.email \"email\", "
							+"                       o.address \"address\", "
							+"                       o.description \"description\", "
							+"                       o.gender \"gender\", "
							+"                       to_char(o.birthday, 'yyyy-mm-dd hh24:mi:ss') \"birthday\", "
							+"                       o.idcard \"idcard\", "
							+"                       to_char(o.createtime, 'yyyy-mm-dd hh24:mi:ss') \"createtime\", "
							+"                       to_char(o.updatetime, 'yyyy-mm-dd hh24:mi:ss') \"updatetime\", "
							+"                       o.enterprise_id \"enterprise_id\", "
							+"                       o.mobileemail \"mobileemail\", "
							+"                       o.backupemail \"backupemail\", "
							+"                       o.status \"status\", "
							+"                       a.account_id \"account_id\", "
							+"                       a.account \"account\", "
							+"                       a.status \"account_status\", "
							+"                       a.password \"password\", "
							+"                       a.last_login_time \"last_login_time\", "
							+"                       rt1.roleName \"roleName\" "
							+"                 from sys_org_user o "
							+"                inner join SYS_USER_RELA_ORG uo on (o.ORG_USER_ID = "
							+"                                                   uo.ORG_USER_ID) "
							+"                inner join sys_account a on a.org_user_id = o.org_user_id "
							+"                 left join (select t.org_user_id, LISTAGG(t.name, ',') WITHIN "
							+"                            GROUP( "
							+"                            order by t.createtime) roleName "
							+"                             from (select a.org_user_id, "
							+"                                          rt.name, "
							+"                                          rt.createtime "
							+"                                     from sys_account a "
							+"                                     left join sys_user_rela_role urr on a.org_user_id = "
							+"                                                                         urr.org_user_id "
							+"                                     left join sys_role r on urr.role_id = "
							+"                                                             r.role_id "
							+"                                    left join sys_role_type rt on rt.role_type_id = "
							+"                                                                  r.role_type_id "
							+"                                   where rt.name is not null "
							+"                                   group by a.org_user_id, "
							+"                                            rt.name, "
							+"                                            rt.createtime) t "
							+"                           group by t.org_user_id) rt1 on rt1.org_user_id = "
							+"                                                            o.org_user_id "
							+"                where o.status = 1 "
							+"                  and uo.ORG_ID = " + orgId + " ";
		if(conditions == null || conditions.equals("")){
			sqlString = sqlString + " ";
		}else{
			sqlString = sqlString + " and o.name like '%"+conditions+"%' ";
		}
		sqlString = sqlString + " ) A  ";
		if(isPage == true){
			int mixResults = (currentPage - 1) * pageSize;
			if(mixResults != 0){
				mixResults = mixResults + 1 ;
			}else{
				mixResults = mixResults;
			}
			int maxResults  = currentPage * pageSize;
			sqlString = sqlString 	+" WHERE ROWNUM <= " + maxResults + ") "
									+" WHERE RN >= " + mixResults;
		}else{
			sqlString = sqlString;
		}
		final String sql = sqlString;
		List<Map<String, Object>> list = hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session session)
							throws HibernateException, SQLException {
						SQLQuery query = session.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//						query.setMaxResults(pageSize);
//						query.setFirstResult((currentPage - 1) * pageSize);
						List<Map<String, Object>> find = query.list();
						return find;
					}
				});
		return list;
	}
	
	
	
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
	public List<Map<String, Object>> getSysOrgUserByEnterpriseId(final long enterpriseId,boolean isFilter, final String conditions) {
		String sqlString = " select distinct o.org_user_id \"org_user_id\","
						     +"   o.name \"name\","
						     +"   o.tel \"tel\","
						     +"   o.mobile \"mobile\","
						     +"   o.email \"email\","
						     +"   o.address \"address\","
						     +"   o.description \"description\","
						     +"   o.gender \"gender\","
						     +"   to_char(o.birthday, 'yyyy-mm-dd hh24:mi:ss') \"birthday\","
						     +"   o.idcard \"idcard\","
						     +"   to_char(o.createtime, 'yyyy-mm-dd hh24:mi:ss') \"createtime\","
						     +"   to_char(o.updatetime, 'yyyy-mm-dd hh24:mi:ss') \"updatetime\","
						     +"   o.enterprise_id \"enterprise_id\","
						     +"   o.mobileemail \"mobileemail\","
						     +"   o.backupemail \"backupemail\","
						     +"   o.status \"status\","
						     +"   a.account_id \"account_id\","
						     +"   a.account \"account\","
						     +"   a.password \"password\","
						     +"   a.last_login_time \"last_login_time\","
						     +"   a.description \"description\","
						     +"   rt1.roleName \"roleName\","
						     +"   so.org_id \"org_id\""
						     +"	from sys_org_user o "
						     +"	inner join sys_account a on a.org_user_id = o.org_user_id "
						     +"	left join (select t.org_user_id, LISTAGG(t.name, ',') WITHIN "
						     +"    GROUP( "
						     +"    order by t.createtime) roleName "
						     +"     from (select a.org_user_id, rt.name, rt.createtime "
						     +"          from sys_account a "
						     +"         left join sys_user_rela_role urr on a.account_id = "
						     +"                                              urr.account_id "
						     +"          left join sys_role r on urr.role_id = r.role_id "
						     +"          left join sys_role_type rt on rt.role_type_id = "
						     +"                                        r.role_type_id "
						     +"        where rt.name is not null "
						     +"        group by a.org_user_id, "
						     +"                  rt.name, "
						     +"                  rt.createtime) t "
						     +"     group by t.org_user_id) rt1 on rt1.org_user_id = "
						     +"                                          o.org_user_id "
						     +"     left join SYS_USER_RELA_ORG uo on (o.ORG_USER_ID = uo.ORG_USER_ID)"
						     +"     left join SYS_ORG so on so.org_id = uo.org_id "	     
						     +"	where o.status = 1 "
						     +"	and o.enterprise_id = "+ enterpriseId ;
		if(isFilter == true){
			sqlString = sqlString + " and so.org_id is null ";
		}else{
			sqlString = sqlString;
		}
		if(conditions == null || conditions.equals("")){
			sqlString = sqlString + " ";
		}else{
			sqlString = sqlString + " and o.name like '%"+conditions+"%' ";
		}
		final String sql = sqlString;
		System.out.println(sql);
		List<Map<String, Object>> list = this.executeSqlForList(sql, hibernateTemplate);
		return list;
	}

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
	public List<Map<String, Object>> getUserByOrgId(final long orgId) {
		String sqlString = " select distinct o.org_user_id \"org_user_id\","
						     +"   o.name \"name\","
						     +"   o.tel \"tel\","
						     +"   o.mobile \"mobile\","
						     +"   o.email \"email\","
						     +"   o.address \"address\","
						     +"   o.description \"description\","
						     +"   o.gender \"gender\","
						     +"   to_char(o.birthday, 'yyyy-mm-dd hh24:mi:ss') \"birthday\","
						     +"   o.idcard \"idcard\","
						     +"   to_char(o.createtime, 'yyyy-mm-dd hh24:mi:ss') \"createtime\","
						     +"   to_char(o.updatetime, 'yyyy-mm-dd hh24:mi:ss') \"updatetime\","
						     +"   o.enterprise_id \"enterprise_id\","
						     +"   o.mobileemail \"mobileemail\","
						     +"   o.backupemail \"backupemail\","
						     +"   o.status \"status\","
						     +"   a.account_id \"account_id\","
						     +"   a.account \"account\","
						     +"   a.password \"password\","
						     +"   a.last_login_time \"last_login_time\","
						     +"   a.description \"description\","
						     +"   rt1.roleName \"roleName\","
						     +"   so.org_id \"org_id\""
						     +"	from sys_org_user o "
						     +"	inner join SYS_USER_RELA_ORG uo on (o.ORG_USER_ID = uo.ORG_USER_ID) "
						     +" inner join SYS_ORG so on (so.org_id = uo.org_id) "
						     +"	inner join sys_account a on a.org_user_id = o.org_user_id "
						     +"	left join (select t.org_user_id, LISTAGG(t.name, ',') WITHIN "
						     +"    GROUP( "
						     +"    order by t.createtime) roleName "
						     +"     from (select a.org_user_id, rt.name, rt.createtime "
						     +"          from sys_account a "
						     +"         left join sys_user_rela_role urr on a.account_id = "
						     +"                                              urr.account_id "
						     +"          left join sys_role r on urr.role_id = r.role_id "
						     +"          left join sys_role_type rt on rt.role_type_id = "
						     +"                                        r.role_type_id "
						     +"        where rt.name is not null "
						     +"        group by a.org_user_id, "
						     +"                  rt.name, "
						     +"                  rt.createtime) t "
						     +"     group by t.org_user_id) rt1 on rt1.org_user_id = "
						     +"                                          o.org_user_id "
						     +"	where o.status = 1 "
						     +"	and uo.ORG_ID = "+ orgId ;
		final String sql = sqlString;
		List<Map<String, Object>> list = hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session session)
							throws HibernateException, SQLException {
						SQLQuery query = session.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//						query.setMaxResults(pageSize);
//						query.setFirstResult((currentPage - 1) * pageSize);
						List<Map<String, Object>> find = query.list();
						return find;
					}
				});
		return list;
	}



	/**
	 * * 获取账号指定类型的角色 指定的角色类型不存在，或者用户不存在
	 * 
	 * @date May 7, 2013 2:32:14 PM
	 * @Description: TODO
	 * @param
	 * @param session
	 * @param
	 * @param roleTypeCode
	 * @param
	 * @param accountId
	 * @param
	 * @return
	 * @param
	 * @throws AuthorityException
	 * @throws
	 */
	private List<SysRole> getUserRolesByAccountId(Session session,
			String accountId) {
		final String faccount = accountId;
		Query query = session
				.createQuery("select r from SysRole as r,SysUserRelaRole as air ,SysAccount rt where rt.account=:account and r.roleId=air.roleId and rt.accountId = air.accountId");
		query.setString("account", faccount);
		return query.list();
	}

	/**
	 * 根据用户获取角色 
	 */
	public List<SysRole> getUserRoles(final String accont) {
		List<SysRole> list = hibernateTemplate
				.executeFind(new HibernateCallback<List<SysRole>>() {

					public List<SysRole> doInHibernate(Session session)
							throws HibernateException, SQLException {
						// authorizeService.setSession(session);
						try {
							return getUserRolesByAccountId(session, accont);
						} catch (Exception e) {
							return null;
						}
					}
				});
		return list;
	}

	/**
	 * 根据账号获取用户信息
	 * 
	 * @date May 9, 2013 10:04:25 AM
	 * @Description: TODO
	 * @param
	 * @param session
	 * @param
	 * @param account
	 * @param
	 * @return
	 * @throws
	 */
	public SysOrgUser getSysOrgUserByAccount(String account) {
		String hql = "select a from SysOrgUser a, SysAccount s where a.orgUserId = s.orgUserId and s.account = '"
				+ account + "'";
		List list = hibernateTemplate.find(hql);
		if (list != null && list.size() > 0) {
			SysOrgUser sysOrgUser = (SysOrgUser) list.get(0);
			return sysOrgUser;
		} else {
			return null;
		}
	}
	
	

	
	
	
	
	/**
	 * 根据人员ID获取人员详细信息
	* @date May 13, 2013 1:59:16 PM
	* @Description: TODO 
	* @param @param orgUserId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getSysOrgUserByOrgUserId(long orgUserId){
		String sql = " select distinct o.org_user_id \"org_user_id\","
				     +"   o.name \"name\","
				     +"   o.tel \"tel\","
				     +"   o.mobile \"mobile\","
				     +"   o.email \"email\","
				     +"   o.address \"address\","
				     +"   o.description \"description\","
				     +"   o.gender \"gender\","
				     +"   to_char(o.birthday, 'yyyy-mm-dd hh24:mi:ss') \"birthday\","
				     +"   o.idcard \"idcard\","
				     +"   to_char(o.createtime, 'yyyy-mm-dd hh24:mi:ss') \"createtime\","
				     +"   to_char(o.updatetime, 'yyyy-mm-dd hh24:mi:ss') \"updatetime\","
				     +"   o.enterprise_id \"enterprise_id\","
				     +"   o.mobileemail \"mobileemail\","
				     +"   o.backupemail \"backupemail\","
				     +"   o.status \"status\","
				     +"   a.account_id \"account_id\","
				     +"   a.account \"account\","
				     +"   a.password \"password\","
				     +"   a.last_login_time \"last_login_time\","
				     +"   a.description \"description\","
				     +"   rt1.roleName \"roleName\""
				     +"	from sys_org_user o "
				     +"	inner join sys_account a on a.org_user_id = o.org_user_id "
				     +"	left join (select t.org_user_id, LISTAGG(t.name, ',') WITHIN "
				     +"    GROUP( "
				     +"    order by t.createtime) roleName "
				     +"     from (select a.org_user_id, rt.name, rt.createtime "
				     +"          from sys_account a "
				     +"         left join sys_user_rela_role urr on a.org_user_id = "
				     +"                                              urr.org_user_id "
				     +"          left join sys_role r on urr.role_id = r.role_id "
				     +"          left join sys_role_type rt on rt.role_type_id = "
				     +"                                        r.role_type_id "
				     +"        where rt.name is not null "
				     +"        group by a.org_user_id, "
				     +"                  rt.name, "
				     +"                  rt.createtime) t "
				     +"     group by t.org_user_id) rt1 on rt1.org_user_id = "
				     +"                                          o.org_user_id "
				     +"	where o.status = 1 "
					  +" and o.org_user_id = " + orgUserId;
		return (List<Map<String, Object>>)this.executeSqlForList(sql, hibernateTemplate);
	}
	



	

	
	/**
	 * 根据人员ID删除人员角色关系
	* @date May 14, 2013 5:07:52 PM
	* @Description: TODO 
	* @param @param accountId
	* @param @throws Exception        
	* @throws
	 */
	public void bulkDeleteSysUserRelaRole(final long accountId) throws Exception {
        final String queryString = "delete SysUserRelaRole s where s.accountId = " + accountId;
        hibernateTemplate.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(queryString);
                return query.executeUpdate();
            }
        });
    }

	/**
	 * 根据账号与角色类型标识获取的角色信息
	* @author ou.jh
	* @date May 24, 2013 2:22:36 PM
	* @Description: TODO 
	* @param @param account
	* @param @param code        
	* @throws
	 */
	public List<Map<String, Object>> getUserRoleByAccountAndCode(String account,String code){
		String sql =" select sr.* "
					+"  from sys_account sa, sys_user_rela_role surr, sys_role sr "
					+"	 where sa.account_id = surr.account_id "
					+"	   and surr.role_id = sr.role_id ";
		if(account != null){
			sql = sql + "	   and sa.account = '"+account+"'";
		}else{
			//人员账号为空
			return null;
			
		}
		if(code != null){
			sql = sql + "	   and sr.code = '"+code+"'";
		}else{
			//角色类型标识为空
			return null;
			
		}
		return (List<Map<String, Object>>)this.executeSqlForList(sql, hibernateTemplate);
	}
	
	/**
	 * 根据用户ID获取账号信息
	* @author ou.jh
	* @date Sep 22, 2013 1:46:40 PM
	* @Description: TODO 
	* @param @param orgUserId
	* @param @return        
	* @throws
	 */
	public Map<String, Object> getAccountByOrgUserId(long orgUserId){
		String sql =" select * "
					+"  from sys_account sa,sys_org_user sou"
					+"	 where sa.org_user_id = sou.org_user_id and  sa.org_user_id =  " +orgUserId;
		List<Map<String, Object>> list = this.executeSqlForList(sql, hibernateTemplate);
		if(list != null && list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
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
	public List<Map<String, Object>> getUserByOrgIdAndRoleCode(long orgId,String roleCode){
		String sqlString = " select distinct o.org_user_id \"org_user_id\","
						     +"   o.name \"name\","
						     +"   o.name name,"
						     +"   o.tel \"tel\","
						     +"   o.mobile \"mobile\","
						     +"   o.email \"email\","
						     +"   o.email email,"
						     +"   o.address \"address\","
						     +"   o.description \"description\","
						     +"   o.gender \"gender\","
						     +"   to_char(o.birthday, 'yyyy-mm-dd hh24:mi:ss') \"birthday\","
						     +"   o.idcard \"idcard\","
						     +"   to_char(o.createtime, 'yyyy-mm-dd hh24:mi:ss') \"createtime\","
						     +"   to_char(o.updatetime, 'yyyy-mm-dd hh24:mi:ss') \"updatetime\","
						     +"   o.enterprise_id \"enterprise_id\","
						     +"   o.mobileemail \"mobileemail\","
						     +"   o.backupemail \"backupemail\","
						     +"   o.status \"status\","
						     +"   a.account_id \"account_id\","
						     +"   a.account \"account\","
						     +"   a.account account,"
						     +"   a.password \"password\","
						     +"   a.last_login_time \"last_login_time\","
						     +"   a.description \"description\","
						     +"   rt1.roleName \"roleName\","
						     +"   so.org_id \"org_id\","
						     +"   r.code "
						     +"	from sys_org_user o "
						     +"	inner join SYS_USER_RELA_ORG uo on (o.ORG_USER_ID = uo.ORG_USER_ID) "
						     +" inner join SYS_ORG so on (so.org_id = uo.org_id) "
						     +"	inner join sys_account a on a.org_user_id = o.org_user_id "
						     +"	left join (select t.org_user_id, LISTAGG(t.name, ',') WITHIN "
						     +"    GROUP( "
						     +"    order by t.createtime) roleName "
						     +"     from (select a.org_user_id, rt.name, rt.createtime "
						     +"          from sys_account a "
						     +"         left join sys_user_rela_role urr on a.account_id = "
						     +"                                              urr.account_id "
						     +"          left join sys_role r on urr.role_id = r.role_id "
						     +"          left join sys_role_type rt on rt.role_type_id = "
						     +"                                        r.role_type_id "
						     +"        where rt.name is not null "
						     +"        group by a.org_user_id, "
						     +"                  rt.name, "
						     +"                  rt.createtime) t "
						     +"     group by t.org_user_id) rt1 on rt1.org_user_id = "
						     +"                                          o.org_user_id "
						     +" left join sys_user_rela_role urr on a.account_id = urr.account_id "
						     +" left join sys_role r on urr.role_id = r.role_id "
						     +"	where o.status = 1 ";
			sqlString = sqlString +"	and uo.ORG_ID = "+ orgId;
			sqlString = sqlString +" and r.code = '"+roleCode+"' ";
				final String sql = sqlString;
			return (List<Map<String, Object>>)this.executeSqlForList(sql, hibernateTemplate);
	}
	
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
	public List<Map<String, Object>> getUserAndLastCoordinatesByOrgIdSAndRoleCode(List<Long> orgIdsList,String roleCode){
		String orgIds = "";
		if(orgIdsList!=null && orgIdsList.size() >0){
			for (Long orgId : orgIdsList) {
				orgIds += orgId+",";
			}
			if(orgIds != null && !orgIds.equals("")){
				orgIds = orgIds.substring(0,orgIds.length()-1);
			}else{
				return null;
			}
		}else{
			return null;
		}
		String sqlString = " select distinct o.org_user_id \"org_user_id\", "
					      +"  o.name \"name\", "
					      +"  o.tel \"tel\", "
					      +"  o.mobile \"mobile\", "
					      +"  o.email \"email\", "
					      +"  o.address \"address\", "
					      +"  o.description \"description\", "
					      +"  o.gender \"gender\", "
					      +"  to_char(o.birthday, 'yyyy-mm-dd hh24:mi:ss') \"birthday\", "
					      +"  o.idcard \"idcard\", "
					      +"  to_char(o.createtime, 'yyyy-mm-dd hh24:mi:ss') \"createtime\", "
					      +"  to_char(o.updatetime, 'yyyy-mm-dd hh24:mi:ss') \"updatetime\", "
					      +"  o.enterprise_id \"enterprise_id\", "
					      +"  o.mobileemail \"mobileemail\", "
					      +"  o.backupemail \"backupemail\", "
					      +"  o.status \"status\", "
					      +"  a.account_id \"account_id\", "
					      +"  a.account \"account\", "
					      +"  a.password \"password\", "
					      +"  a.last_login_time \"last_login_time\", "
					      +"  a.description \"description\", "
					      +"  rt1.roleName \"roleName\", "
					      +"  so.org_id \"org_id\", "
					      +"  pgl.longitude \"longitude\", "
					      +"  pgl.latitude \"latitude\", "
					      +"  pgl.pickTime \"pickTime\" "
					      +"	from sys_org_user o "
					      +"	inner join SYS_USER_RELA_ORG uo on (o.ORG_USER_ID = uo.ORG_USER_ID) "
					      +"	inner join SYS_ORG so on (so.org_id = uo.org_id) "
					      +"	inner join sys_account a on a.org_user_id = o.org_user_id "
					      +"	left join((select * "
					      +"	       from pda_gps_location pgl, "
					      +"	            (select pgl.userid us, max(pgl.picktime) mpicktime "
					      +"	               from pda_gps_location pgl "
					      +"              group by pgl.userid) pg "
					      +"	      where pgl.userid = pg.us "
					      +"	        and pg.mpicktime = pgl.picktime)) pgl on a.account = "
					      +"	                                                 pgl.userid "
					      +"	left join (select t.org_user_id, LISTAGG(t.name, ',') WITHIN "
					      +"      GROUP( "
					      +"      order by t.createtime) roleName "
					      +"       from (select a.org_user_id, rt.name, rt.createtime "
					      +"               from sys_account a "
					      +"         left join sys_user_rela_role urr on a.account_id = "
					      +"                                             urr.account_id "
					      +"         left join sys_role r on urr.role_id = r.role_id "
					      +"         left join sys_role_type rt on rt.role_type_id = "
					      +"                                       r.role_type_id "
					      +"        where rt.name is not null "
					      +"         group by a.org_user_id, rt.name, rt.createtime) t "
					      +" group by t.org_user_id) rt1 on rt1.org_user_id = "
					      +"                                o.org_user_id "
					      +" left join sys_user_rela_role urr on a.account_id = urr.account_id "
						  +" left join sys_role r on urr.role_id = r.role_id "
					      +" where o.status = 1 "
			     +"	and uo.ORG_ID in ("+ orgIds + ") and r.code = '"+roleCode+"' ";
				final String sql = sqlString;
			return (List<Map<String, Object>>)this.executeSqlForList(sql, hibernateTemplate);
	}
	
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
	public List<Map<String, Object>> getUserAndAccountByOrgIds(List<Long> orgIdsList, String Role){
		String orgIds = "";
		if(orgIdsList!=null && orgIdsList.size() >0){
			for (Long orgId : orgIdsList) {
				orgIds += orgId+",";
			}
		}else{
			return null;
		}
		String sql = 	
					" select sa.*, "
					+"  suro.org_id,sou.* "
					+"	from SYS_ACCOUNT       sa, "
					+"  SYS_ORG_USER    sou,"
					+"	SYS_USER_RELA_ORG suro, "
					+"	SYS_USER_RELA_ROLE surr, "
					+"	SYS_ROLE sr "
					+"	where sa.org_user_id = suro.org_user_id "
					+"	and surr.account_id = sa.account_id "
					+"	and suro.ORG_USER_ID = sa.ORG_USER_ID "
					+"	and surr.role_id = sr.role_id ";
		if(Role != null && !Role.equals("")){
			sql = sql +"		and sr.code = '"+Role+"' ";
		}else{
			return null;
		}			
		if(orgIds != null && !orgIds.equals("")){
			orgIds = orgIds.substring(0,orgIds.length()-1);
			sql = sql +"		and suro.org_id in ("+orgIds+")";
		}else{
			return null;
		}
		return (List<Map<String, Object>>)this.executeSqlForList(sql, hibernateTemplate);
	}
	
	/**
	 * 获取人员账号获取人员所在企业
	* @author ou.jh
	* @date May 28, 2013 2:17:05 PM
	* @Description: TODO 
	* @param @param account
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getEnterpriseByAccount(String account){
		String sql = 
			" select pe.* "
			+"  from PROJ_ENTERPRISE   pe, "
			+"       sys_org_user      sou, "
			+"       SYS_USER_RELA_ORG suro, "
			+"		 sys_account      sa, "
			+"       SYS_ORG           so "
			+" where sou.org_user_id = suro.org_user_id "
			+"   and so.org_id = suro.org_id "
			+"   and so.enterprise_id = pe.id "
			+"	 and sa.org_user_id = sou.org_user_id ";
		if(account != null && !account.equals("")){
			  sql = sql  + " and sa.account = ' '";
		}else{
			return null;
		}
		return (List<Map<String, Object>>)this.executeSqlForList(sql, hibernateTemplate);
	}
	
	
	/**
	 * 根据时间获取合同快到期
	* @author ou.jh
	* @date Mar 13, 2014 3:57:25 PM
	* @Description: TODO 
	* @param @param month
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getSysOrgUserContractEndTime(String startTime ,String endTime){
//		String sql = " select p.*,wm_concat(o.name) orgname"
//					+"  from sys_org_user p "
//					+"  left join sys_user_rela_org suro on suro.org_user_id = p.org_user_id"
//					+"  left join sys_org o on o.org_id = suro.org_id "
//					+" where p.contract_end_time >= "
//					+"  to_date('"+startTime+"', 'yyyy-mm-dd') "
//					+" and p.contract_start_time <= "
//					+"  to_date('"+endTime+"', 'yyyy-mm-dd') ";
		String sql = " select p.*, orgname "
					+" 			from sys_org_user p "
					+"   left join (select pi.org_user_id, wm_concat(pi.orgname) orgname "
					+"               from (select distinct p.org_user_id, o.name orgname "
					+"                        from sys_org_user   p, "
					+"                             sys_user_rela_org suro, "
					+"                             sys_org           o "
					+"                       where suro.org_user_id = p.org_user_id "
					+"                         and o.org_id = suro.org_id "
					 +"                         and suro.status = 'A') pi "
					 +"               group by pi.org_user_id) o on o.org_user_id = p.org_user_id "
					 +"  where p.contract_end_time >= to_date('"+startTime+"', 'yyyy-mm-dd') "
					 +"    and p.contract_end_time <= to_date('"+endTime+"', 'yyyy-mm-dd') and p.status = 1 order by p.contract_end_time ";
		return this.executeSqlForList(sql, hibernateTemplate);		
	}
	
	
	
//	/**
//	 * 更新用户和岗位关系
//	* @author ou.jh
//	* @date Sep 3, 2013 2:28:34 PM
//	* @Description: TODO 
//	* @param @param infoMap
//	* @param @param id
//	* @param @return        
//	* @throws
//	 */
//	public long updateSysUserRelaPost(Map<String, Object> infoMap,long orgUserId,long orgId){
//		//update 表 set 字段1 = 值,字段2 = 值 where 字段3 = 值
//		//INSERT语句
//		String sql = "update SYS_USER_RELA_POST s set ";
//		sql = sql + "s.mod_time = sysdate";
//		if(infoMap != null){
//			for(String key:infoMap.keySet()){
//				if(infoMap.get(key) != null){
//					//判断类型(date)
//					if(infoMap.get(key).getClass().toString().indexOf("java.util.Date")  > - 1){
//						String format = sf.format(infoMap.get(key));
//						sql = sql + ",s."+key + " = to_date('"+format+"','yyyy-mm-dd hh24:mi:ss')";
//					}
//					//判断类型(String)
//					else if(infoMap.get(key).getClass().toString().indexOf("java.lang.String")  > - 1){
//						sql = sql + ",s."+key + " = '"+infoMap.get(key)+"'";
//					}
//					//判断类型（其他）
//					else {
//						sql = sql + ",s."+key + " = "+infoMap.get(key)+"";
//					}
//				}else{
//					continue;
//				}
//			}
//			sql = sql + " where s.ORG_USER_ID = " + orgUserId + " and s.ORG_ID" + orgId;
//		}else{
//			return 0;
//		}
//		return executeSql(sql,hibernateTemplate);
//	}
	
	
	
	
}
