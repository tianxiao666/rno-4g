package com.iscreate.op.dao.report;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.report.ReportStaffTaskCountMonth;



public class StaffTaskCountMonthReportDaoImpl implements StaffTaskCountMonthReportDao{
	
	private HibernateTemplate hibernateTemplate;
	
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}


	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}


	/**
	 * 根据组织Id获取该组织下属人员某段时间内的报表数据
	 * @param orgId 组织Id
	 * @param beginTime 开始时间 
	 * @param endTime 结束时间
	 * @return
	 */
	public List<ReportStaffTaskCountMonth> getStaffTaskCountReportByOrgId(long orgId, String beginTime, String endTime) {
		String hql = "from ReportStaffTaskCountMonth where orgId="+orgId+" and statisticalTime between '"+beginTime+"' and '"+endTime+"' ";
		return hibernateTemplate.find(hql);
	}


	/**
	 * 根据人员帐号获取该人员某段时间内的报表数据
	 * @param staffAccount 人员帐号
	 * @param beginTime 开始时间 
	 * @param endTime 结束时间
	 * @return
	 */
	public List<ReportStaffTaskCountMonth> getStaffTaskCountReportByOrgId(String staffAccount, String beginTime, String endTime) {
		String hql = "from ReportStaffTaskCountMonth where staffAccount='"+staffAccount+"' and statisticalTime between '"+beginTime+"' and '"+endTime+"' ";
		return hibernateTemplate.find(hql);
	}


	
}
