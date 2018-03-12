package com.iscreate.op.dao.publicinterface;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.publicinterface.Bizlog;

public class BizlogDaoImpl implements BizlogDao {
	private HibernateTemplate hibernateTemplate;
	
	/**
	 * 保存业务日志
	 * @param bizlog
	 */
	public void saveBizlog(Bizlog bizlog){
		this.hibernateTemplate.save(bizlog);
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	
}
