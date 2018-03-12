package com.iscreate.op.dao.system;

import org.springframework.orm.hibernate3.HibernateTemplate;

public class SysOrgAttrExtDaoImpl implements SysOrgAttrExtDao {
	private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
}
