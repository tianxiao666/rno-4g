package com.iscreate.op.dao.system;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.dao.common.BaseDao;

public class SysUserRelaRoleDaoImpl extends BaseDao implements SysUserRelaRoleDao {
	private HibernateTemplate hibernateTemplate;
	/**
	 * @author du.hw
	 * @create 2014-01-14
	 * 根据用户标识得到用户角色
	 * return:所有的角色（通过flag标识出用户是否有角色） 
	 */
	public List<Map<String,Object>> getUserRolesByUserId(long user_id,String system){
		String sql = "select a.*,nvl2(b.role_id,1,0) flag from sys_role a"+
		             " left join sys_user_rela_role b on b.org_user_id="+user_id+"' and b.role_id=a.role_id and b.status='A'";
		if(!system.equals("")){
			sql += " where a.pro_code='"+system+"'";
		}
		return this.executeSqlForList(sql, hibernateTemplate);
		             
	}
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * 
	 * @description: 删除用户角色关联
	 * @author：zhang.wy1
	 * @param userId
	 * @return: void          
	 * @date：2014-1-15 下午12:44:07
	 */
	public void deleteUserRelaRole(long userId) {
		// TODO Auto-generated method stub
		final String sql = "update sys_user_rela_role set status='X' , updatetime=sysdate where org_user_id="+userId;
		hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException,
			SQLException {
			session.createSQLQuery(sql).executeUpdate();
			return null;
			}
			});
	}

	/**
	 * 
	 * @description: 添加用户角色关联
	 * @author：zhang.wy1
	 * @param userId
	 * @return: void          
	 * @date：2014-1-15 下午12:44:07
	 */
	public void addUserRelaRole(long userId, String roleId) {
		// TODO Auto-generated method stub
		final String sql = "insert into sys_user_rela_role values("+userId+", "+roleId+",null,sysdate,null,'A')";
		hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException,
			SQLException {
			session.createSQLQuery(sql).executeUpdate();
			return null;
			}
			});
	}

	
}
