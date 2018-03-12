package com.iscreate.op.dao.report;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;


public class OrgStaffCountMonthReportDaoImpl implements OrgStaffCountMonthReportDao{
	
	private HibernateTemplate hibernateTemplate;
	
	private Log log = LogFactory.getLog(this.getClass());
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}


	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}


	/**
	 * 根据组织Id获取人员数量
	 * @param orgId 组织Id
	 * @param beginTime 统计开始时间
	 * @param endTime 统计结束时间
	 * @return
	 */
	public List<Map<String, Object>> getOrgStaffCountByOrgId(final long orgId,final String beginTime,final String endTime) {
		List<Map<String, Object>> list =  this.hibernateTemplate
		.execute(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				if(!isDateParse(beginTime)){
					log.error(beginTime+"为非法时间");
					return null;
				}else{
					log.info(beginTime+"为正常时间");
				}
				if(!isDateParse(endTime)){
					log.error(endTime+"为非法时间");
					return null;
				}else{
					log.info(endTime+"为正常时间");
				}
				String sqlString = "select " +
						"r.id \"id\"," +
						"r.orgId \"orgId\"," +
						"r.staffCount \"staffCount\"," +
						"r.statisticalTime \"statisticalTime\"" +
						" from report_orgstaffcount_month r where orgId="+orgId+" and  " +
				" statisticalTime >= to_date('"+beginTime+"','yyyy-mm-dd hh24:mi:ss') and statisticalTime < to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss')";
				SQLQuery query = session.createSQLQuery(sqlString);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List find = query.list();
				return find;
			}
		});
		return list;
	}


	/**
	 * 根据组织Id获取人员技能分类统计信息 
	 * @param orgId 组织Id
	 * @param beginTime 统计开始时间
	 * @param endTime 统计结束时间
	 * @return
	 */
	public List<Map<String, Object>> getOrgStaffSkillByOrgId(final long orgId,final String beginTime,final String endTime) {
		List<Map<String, Object>> list =  this.hibernateTemplate
		.execute(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				if(!isDateParse(beginTime)){
					log.error(beginTime+"为非法时间");
					return null;
				}else{
					log.info(beginTime+"为正常时间");
				}
				if(!isDateParse(endTime)){
					log.error(endTime+"为非法时间");
					return null;
				}else{
					log.info(endTime+"为正常时间");
				}
				String sqlString = "select " +
						"r.id \"id\"," +
						"r.orgId \"orgId\"," +
						"r.skillName \"skillName\"," +
						"r.staffCount \"staffCount\"," +
						"r.statisticalTime \"statisticalTime\"" +
						" from report_orgstaffskill_month r where orgId="+orgId+" and  " +
						" statisticalTime >= to_date('"+beginTime+"','yyyy-mm-dd hh24:mi:ss') and statisticalTime < to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss')";
				SQLQuery query = session.createSQLQuery(sqlString);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List find = query.list();
				return find;
			}
		});
		return list;
	}



	
	public boolean isDateParse(String dateString){
		boolean returnBool = true;
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 formatter.setLenient(false);
		 try {
			Date parse = formatter.parse(dateString);
		 } catch (ParseException e) {
				// TODO Auto-generated catch block
			 returnBool = false;
		}
		 return returnBool;
	}
	

}
