package com.iscreate.op.dao.system;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.system.SysRoleType;

public class SysRoleTypeDaoImpl implements SysRoleTypeDao{
	private HibernateTemplate hibernateTemplate;

	/**
	 * 获取全部角色类型
	 * @return
	 */
	public List<SysRoleType> getAllRoleType(){
		String hql = "from SysRoleType";
		return (List<SysRoleType>)hibernateTemplate.find(hql);
	}
	
	public  static void main(String args[]){
		ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/*.xml");
		SysRoleTypeDao s = (SysRoleTypeDao) ctx.getBean("sysRoleTypeDao");
		System.out.println(s.getAllRoleType());
	}
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
}
