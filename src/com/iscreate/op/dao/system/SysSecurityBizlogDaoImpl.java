package com.iscreate.op.dao.system;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.system.SysSecurityBizlog;


public class SysSecurityBizlogDaoImpl implements SysSecurityBizlogDao {
	private HibernateTemplate hibernateTemplate;
	
	/**
	 * 保存业务日志
	 * @param bizlog
	 */
	public void saveSysSecurityBizlog(SysSecurityBizlog bizlog){
		this.hibernateTemplate.save(bizlog);
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	
}
