package com.iscreate.op.dao.system;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.system.SysRoleRelaPermission;

public class SysRoleRelaPermissionDaoImpl implements SysRoleRelaPermissionDao {
	private HibernateTemplate hibernateTemplate;

	/**
	 * 根据账号和访问资源获取角色和权限关系
	 * 
	 * @param account
	 * @param uri
	 * @return
	 */
	public SysRoleRelaPermission getSysRoleRelaPermissionByAccountAndURI(
			String account, String uri) {
		String hql = "select srrp from SysAccount as sac, SysUserRelaRole as sur,SysRole as sr ,SysRoleRelaPermission as srrp,SysPermission as sp";
		hql += " where sac.account='" + account + "' and sp.url = '" + uri
				+ "'";
		hql += " and sac.accountId = sur.accountId";
		hql += " and sur.roleId = sr.roleId";
		hql += " and srrp.roleId = sr.roleId";
		hql += " and srrp.permissionId = sp.permissionId";
		List list = hibernateTemplate.find(hql);
		if (list == null || list.isEmpty()) {
			return null;
		} else {
			return (SysRoleRelaPermission) list.get(0);
		}
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

}
