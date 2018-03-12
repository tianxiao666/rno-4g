package com.iscreate.op.service.workmanage;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.workmanage.WorkmanageStatusreg;

public class WorkmanageStatusregServiceImpl implements
		WorkmanageStatusregService {
	
	
	private HibernateTemplate hibernateTemplate;
	
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public WorkmanageStatusreg getWorkmanageStatusregById(long statusId) {
		String hql="select o from WorkmanageStatusreg o where o.id=?";
		WorkmanageStatusreg statusreg=null;
		try {
			List list=this.hibernateTemplate.find(hql,statusId);
			if(list!=null && !list.isEmpty()){
				statusreg=(WorkmanageStatusreg)list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusreg;
	}

}
