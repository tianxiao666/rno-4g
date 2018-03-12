package com.iscreate.op.dao.system;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.dao.common.BaseDao;
import com.iscreate.op.pojo.system.SysPermission;
import com.iscreate.op.pojo.system.SysRole;
import com.iscreate.op.pojo.system.SysRole;
import com.iscreate.op.pojo.system.SysRole;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;

public class SysRoleDaoImpl extends BaseDao implements SysRoleDao{
	private HibernateTemplate hibernateTemplate;

	/**
	 * 根据角色类型获取角色列表
	 * @param roleTypeId
	 * @return
	 */
	public List<SysRole> getRolesByRoleTypeCode(long roleTypeId){
		String hql = "from SysRole s where s.roleTypeId="+roleTypeId;
		return (List<SysRole>) hibernateTemplate.find(hql);
	}
	
	/**
	 * 根据角色类型id获取角色列表
	* @date May 13, 2013 2:52:08 PM
	* @Description: TODO 
	* @param @param roleTypeId
	* @param @return        
	* @throws
	 */
	public List<SysRole> getSysRoleByRoleTypeId(long roleTypeId){
		String hql = "select r from SysRole r, SysRoleType s where r.roleTypeId = s.roleTypeId and s.roleTypeId = "
			+ roleTypeId;
		return (List<SysRole>)hibernateTemplate.find(hql);
	}
	
	/**
	 * 获取全部角色列表
	 * @param roleTypeId
	 * @return
	 */
	public List<SysRole> getAllRole(){
		String hql = "from SysRole ";
		return (List<SysRole>) hibernateTemplate.find(hql);
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
		String hql = "from SysRole r where r.proCode = '"+systemCode+"'";
		
		return (List<SysRole>) hibernateTemplate.find(hql);
	}
	
	
	/**
	 * 删除角色
	 * 
	 * @param permissionId
	 * @return
	 */
	public boolean deleteRoleById(final long roleId) {
		final long fid = roleId;
		hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				SysRole resource = (SysRole) session
						.createCriteria(SysRole.class).add(
								Restrictions.eq("roleId", fid))
						.uniqueResult();
				if (resource == null) {
					throw new UserDefinedException("不存在id=" + fid
							+ "的资源，无法执行删除操作！");
				}

				// 删除与业务模块的关联关系
				String sql = "delete from SysRole as module where module.roleId=? ";
				Query query = session.createQuery(sql);
				query.setLong(0, fid);
				int cnt = query.executeUpdate();
				return null;
			}

		});
		return true;
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
	 * 根据用户获取角色 
	 */
	public List<SysRole> getUserRoles(final long orgUssrId) {
		List<SysRole> list = hibernateTemplate
				.executeFind(new HibernateCallback<List<SysRole>>() {

					public List<SysRole> doInHibernate(Session session)
							throws HibernateException, SQLException {
						// authorizeService.setSession(session);
						try {
							return getUserRolesByuserId(session, orgUssrId);
						} catch (Exception e) {
							return null;
						}
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
			String accountId){
		final String faccount = accountId;
		Query query = session
				.createQuery("select r from SysRole as r,SysUserRelaRole as air ,SysAccount rt where rt.account=:account and r.roleId=air.roleId and rt.orgUserId = air.orgUserId");
		query.setString("account", faccount);
		return query.list();
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
	 * @param orgUserId
	 * @param
	 * @return
	 * @param
	 * @throws AuthorityException
	 * @throws
	 */
	private List<SysRole> getUserRolesByuserId(Session session,
			long orgUserId){
		final long faccount = orgUserId;
		Query query = session
				.createQuery("select r from SysRole as r,SysUserRelaRole as air where air.orgUserId=:orgUserId and r.roleId=air.roleId");
		query.setLong("orgUserId", faccount);
		return query.list();
	}
	
	
	
	public  static void main(String args[]){
		ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/*.xml");
		SysRoleDao s = (SysRoleDao) ctx.getBean("sysRoleDao");
		System.out.println(s.getRolesByRoleTypeCode(1).get(0).getName());
	}
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	/**
	 * 根据用户和系统获取角色 
	 */
	public List<Map<String, Object>> getSystemsByUserAndSystem(long orgUserId,
			String systemCode) {
		String sql = "select t.*,nvl2(ur.role_id,1,0) flag from SYS_ROLE t  left join (select role_id from sys_user_rela_role where status='A' and org_user_id="+orgUserId+") ur on t.role_id=ur.role_id where pro_code ='"+systemCode+"'";		
		return this.executeSqlForList(sql, hibernateTemplate);
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
		String sql="update sys_role_rela_permission srrp set srrp.status='X',srrp.mod_time=sysdate where srrp.status!='X' and srrp.permission_id="+permissionId;
		int result =super.executeSql(sql, hibernateTemplate);
		if(result>-1){
			return true;
		}
		return false;
	}

	
}
