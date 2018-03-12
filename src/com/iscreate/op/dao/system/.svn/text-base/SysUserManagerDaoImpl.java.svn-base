package com.iscreate.op.dao.system;

import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.dao.common.BaseDao;

public class SysUserManagerDaoImpl extends BaseDao implements SysUserManagerDao
{
	private HibernateTemplate hibernateTemplate;
	
	
	
	/**
	 * 根据姓名/账号 和 部门ID 分页查询用户列表
	 * @author li.hb
	 * @date 2014-1-9 下午5:47:59
	 * @Description: TODO 
	 * @param @param userName
	 * @param @param orgId
	 * @param @return        
	 * @throws
	 */
	public List<Map<String, Object>> findSysUserManagerListDao(String userName,long orgId,int currentPage,int pageSize) 
	{

		String whereSql = "";
		
		if(null != userName && !"".equals(userName) )
		{
			whereSql += " and (u.name like '%"+userName+"%' or account like '%"+userName+"%') ";
		}
		
		if(orgId!=0)
		{
			whereSql += " and o.org_id = "+orgId+" ";
		}
		
		
		int mixResults = (currentPage - 1) * pageSize;
		if(mixResults != 0){
			mixResults = mixResults + 1 ;
		}else{
			mixResults = mixResults;
		}
		int maxResults  = currentPage * pageSize;
		
		
		String sql ="select *"+
					"  from (select p.*, ro.role, org.orgName, rownum ro"+
					"          from (select u.org_user_id,"+
					"                       min(u.name)        userName,"+
					"                       min(a.account)     account,"+
					"                       "+
					"                       min(u.email) email,"+
					"                       min(d1.name) status"+
					"                  from SYS_ORG_USER u"+
					"                  left join sys_user_rela_org uro"+
					"                    on uro.org_user_id = u.org_user_id"+
					"                  left join SYS_ORG o"+
					"                    on o.org_id = uro.org_id"+
					"                  left join SYS_ACCOUNT a"+
					"                    on u.org_user_id = a.org_user_id"+
					"                  left join sys_dictionary d"+
					"                    on d.code = 'USER_STATUS'"+
					"                  left join sys_dictionary d1"+
					"                    on d1.parent_id = d.data_type_id"+
					"                   and d1.code = TO_CHAR(u.status)"+
					"                 where 1 = 1"+ whereSql + " group by u.org_user_id) p"+
					"          left join (select u.org_user_id,"+
					"						case"+
					"                             when max((select d1.name"+
					"                                        from sys_dictionary d,"+
					"                                             sys_dictionary d1"+
					"                                       where d.code = 'System'"+
					"                                         and d1.parent_id = d.data_type_id"+
					"                                         and d1.code = r.pro_code)) is not null then"+
					"                              listagg((select '(' || d1.name || ')'"+
					"                                         from sys_dictionary d,"+
					"                                              sys_dictionary d1"+
					"                                        where d.code = 'System'"+
					"                                          and d1.parent_id = d.data_type_id"+
					"                                          and d1.code = r.pro_code) || r.name,"+
					"                                      '<br>') within GROUP(order by r.name)"+
					"                           end role"+
					"                      from SYS_ORG_USER u"+
					"                      left join Sys_User_Rela_Role p"+
					"                        on p.org_user_id = u.org_user_id and p.status = 'A'"+
					"                      left join SYS_ROLE r"+
					"                        on r.role_id = p.role_id"+
					"                     group by u.org_user_id) ro"+
					"            on p.org_user_id = ro.org_user_id"+
					"          left join (select u.org_user_id,"+
					"                           case"+
					"                             when max((select d4.name"+
					"                                        from sys_dictionary d2,"+
					"                                             sys_dictionary d3,"+
					"                                             sys_dictionary d4"+
					"                                       where d2.code = o.org_type"+
					"                                         and d3.parent_id = d2.data_type_id"+
					"                                         and d4.parent_id = d3.data_type_id"+
					"                                         and d4.code = up.post_code)) is not null then"+
					"                              listagg(o.name || '(' ||"+
					"                                      (select d4.name"+
					"                                         from sys_dictionary d2,"+
					"                                              sys_dictionary d3,"+
					"                                              sys_dictionary d4"+
					"                                        where d2.code = o.org_type"+
					"                                          and d3.parent_id = d2.data_type_id"+
					"                                          and d4.parent_id = d3.data_type_id"+
					"                                          and d4.code = up.post_code) || ')',"+
					"                                      '<br>') within GROUP(order by o.name)"+
					"                           end orgName"+
					"                      from SYS_ORG_USER u"+
					"                      left join SYS_USER_RELA_POST up"+
					"                        on u.org_user_id = up.org_user_id"+
					"                       and up.status = 'A'"+
					"                      left join SYS_ORG o"+
					"                        on o.org_id = up.org_id"+
					"                     group by u.org_user_id) org"+
					"            on p.org_user_id = org.org_user_id) p1"+
					" where p1.ro >= "+mixResults+" "+
					" and p1.ro <= "+maxResults+""+"";
		
		System.out.println(sql);
		
		
		List<Map<String, Object>> list = this.executeSqlForList(sql, hibernateTemplate);
		
		
		return list;
	}
	
	
	/**
	 * 根据姓名/账号 和 部门ID 查询用户列表总数
	 * @author li.hb
	 * @date 2014-1-9 下午5:47:59
	 * @Description: TODO 
	 * @param @param userName
	 * @param @param orgId
	 * @param @return        
	 * @throws
	 */
	public int findSysUserManagerCountDao(String userName,long orgId)
	{
		
		String whereSql = "";
		
		if(null != userName && !"".equals(userName) )
		{
			whereSql += " and (u.name like '%"+userName+"%' or account like '%"+userName+"%') ";
		}
		
		if(orgId!=0)
		{
			whereSql += " and o.org_id = "+orgId+" ";
		}
		
		String sql ="select p.*, ro.role, org.orgName, rownum ro"+
					"          from (select u.org_user_id,"+
					"                       min(u.name)        userName,"+
					"                       min(a.account)     account,"+
					"                       "+
					"                       min(u.email) email,"+
					"                       min(d1.name) status"+
					"                  from SYS_ORG_USER u"+
					"                  left join sys_user_rela_org uro"+
					"                    on uro.org_user_id = u.org_user_id"+
					"                  left join SYS_ORG o"+
					"                    on o.org_id = uro.org_id"+
					"                  left join SYS_ACCOUNT a"+
					"                    on u.org_user_id = a.org_user_id"+
					"                  left join sys_dictionary d"+
					"                    on d.code = 'USER_STATUS'"+
					"                  left join sys_dictionary d1"+
					"                    on d1.parent_id = d.data_type_id"+
					"                   and d1.code = TO_CHAR(u.status)"+
					"                 where 1 = 1"+ whereSql + " group by u.org_user_id) p"+
					"          left join (select u.org_user_id,"+
					"                           listagg('('||dd.name||')'||r.name, '<br>') within GROUP(order by r.name) role"+
					"                      from SYS_ORG_USER u"+
					"                      left join Sys_User_Rela_Role p"+
					"                        on p.org_user_id = u.org_user_id and p.status = 'A'"+
					"                      left join SYS_ROLE r"+
					"                        on r.role_id = p.role_id"+
					"					   left join (select d1.* from sys_dictionary d,sys_dictionary d1 where d.code = 'System' and d1.parent_id = d.data_type_id) dd "+
                    "						 on dd.code = r.pro_code "+
					"                     group by u.org_user_id) ro"+
					"            on p.org_user_id = ro.org_user_id"+
					"          left join (select u.org_user_id,"+
					"                           case"+
					"                             when max((select d4.name"+
					"                                        from sys_dictionary d2,"+
					"                                             sys_dictionary d3,"+
					"                                             sys_dictionary d4"+
					"                                       where d2.code = o.org_type"+
					"                                         and d3.parent_id = d2.data_type_id"+
					"                                         and d4.parent_id = d3.data_type_id"+
					"                                         and d4.code = up.post_code)) is not null then"+
					"                              listagg(o.name || '(' ||"+
					"                                      (select d4.name"+
					"                                         from sys_dictionary d2,"+
					"                                              sys_dictionary d3,"+
					"                                              sys_dictionary d4"+
					"                                        where d2.code = o.org_type"+
					"                                          and d3.parent_id = d2.data_type_id"+
					"                                          and d4.parent_id = d3.data_type_id"+
					"                                          and d4.code = up.post_code) || ')',"+
					"                                      '<br>') within GROUP(order by o.name)"+
					"                           end orgName"+
					"                      from SYS_ORG_USER u"+
					"                      left join SYS_USER_RELA_POST up"+
					"                        on u.org_user_id = up.org_user_id"+
					"                       and up.status = 'A'"+
					"                      left join SYS_ORG o"+
					"                        on o.org_id = up.org_id"+
					"                     group by u.org_user_id) org"+
					"            on p.org_user_id = org.org_user_id";
		
		List<Map<String, Object>> countList = this.executeSqlForList(sql, hibernateTemplate);
		
		return countList.size();
	}
	

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
