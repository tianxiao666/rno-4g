package com.iscreate.op.dao.system;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.system.SysPermissionType;


public class SysPermissionTypeDaoImpl implements SysPermissionTypeDao{
	private HibernateTemplate hibernateTemplate;

	/**
	 * 获取第一级权限类型
	 * @return
	 */
	public List<SysPermissionType> getRootPermissionType(){
		String hql = "select s from SysPermissionType s where parentId is null or parentId =''";
		return (List<SysPermissionType>) hibernateTemplate.find(hql);
	}
	
	/**
	 * 获取全部权限类型
	 * 
	 * @return
	 */
	public List<SysPermissionType> getPermissionTypeByParentId(long parentId){
		String hql = "from SysPermissionType where parentId="+parentId;
		return (List<SysPermissionType>) hibernateTemplate.find(hql);
	}
	
	
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
}
