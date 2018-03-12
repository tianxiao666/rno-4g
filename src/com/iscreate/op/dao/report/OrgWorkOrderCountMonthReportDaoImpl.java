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

import com.iscreate.op.pojo.report.ReportOrgWorkOrderCountMonth;


public class OrgWorkOrderCountMonthReportDaoImpl implements OrgWorkOrderCountMonthReportDao{
	
	private HibernateTemplate hibernateTemplate;
	
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}


	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}


	/**
	 * 根据组织Id获取该组织某段时间内的报表数据
	 * @param orgId 组织Id
	 * @param beginTime 开始时间 
	 * @param endTime 结束时间
	 * @return
	 */
	public List<Map<String, Object>> getOrgWorkOrderCountReportByOrgId(
			final long orgId,final String beginTime,final String endTime) {
		List<Map<String, Object>> list =  this.hibernateTemplate
		.execute(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sqlString = "select " +
						"r.id \"id\"," +
						"r.orgId \"orgId\"," +
						"r.workOrderCount \"workOrderCount\"," +
						"r.statisticalTime \"statisticalTime\"" +
						" from report_orgwocount_month r where orgId="+orgId+" and  " +
						" statisticalTime >= to_date('"+beginTime+"','yyyy-mm-dd hh24:mi:ss') and statisticalTime < to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss')";
				SQLQuery query = session.createSQLQuery(sqlString);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List find = query.list();
				return find;
			}
		});
		return list;
	}


	
}
