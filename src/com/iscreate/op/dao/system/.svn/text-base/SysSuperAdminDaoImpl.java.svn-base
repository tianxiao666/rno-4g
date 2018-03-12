package com.iscreate.op.dao.system;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * 超级用户信息dao
	 * 
	 * @author ou.jh
	 * @date Jun 20, 2013
	 * @Description: TODO
	 * @param 
	 * @return 
	 * @throws
 */
public class SysSuperAdminDaoImpl implements SysSuperAdminDao {
	
	private HibernateTemplate hibernateTemplate;
	
	
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * 根据密码获取超级用户信息
	* @author ou.jh
	* @date Jun 20, 2013 3:55:52 PM
	* @Description: TODO 
	* @param @param account        
	* @throws
	 */
	public List<Map<String, Object>> getSysSuperAdminByPassword(String password){
		String sql = "select sa.account_id      \"account_id\", "
					 +"      sa.account         \"account\", "
					 +"      sa.password        \"password\", "
					 +"      sa.org_user_id     \"orgUserId\", "
					 +"      sou.status          \"status\", "
					 +"      sa.last_login_time \"lastLoginTime\", "
					 +"      sa.description     \"description\", "
					 +"      sa.createtime      \"createtime\", "
					 +"      sa.updatetime      \"updatetime\" "
					 +"  from SYS_SUPER_ADMIN ssa, Sys_Account sa,sys_org_user sou "
					 +" where ssa.account = sa.account  and sa.org_user_id=sou.org_user_id"
					 +" and sa.password = '"+password+"'";
		return this.executeSqlForObject(sql);
	}

	
	
	/**
	 * 数据库查询方法
	* @author ou.jh
	* @date Jun 20, 2013 3:56:22 PM
	* @Description: TODO 
	* @param @param sqlString
	* @param @return        
	* @throws
	 */
	private List<Map<String, Object>> executeSqlForObject(final String sqlString) {
		List<Map<String, Object>> list = hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(
							Session session) throws HibernateException,
							SQLException {
						SQLQuery query = session.createSQLQuery(sqlString);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> find = query.list();
						return find;
					}
				});
		return list;
	}
}
