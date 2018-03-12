package com.iscreate.op.dao.test;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.test.Test;
import com.iscreate.op.pojo.test.Test2;

public class TestDaoImpl implements TestDao{
	private HibernateTemplate hibernateTemplate;
	
	public void addTest1(Test test){
		hibernateTemplate.save(test);
	}
	
	public void addTest2(Test2 test){
		hibernateTemplate.save(test);
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	
}
