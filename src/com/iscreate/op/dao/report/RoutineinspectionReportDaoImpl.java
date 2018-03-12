package com.iscreate.op.dao.report;

import java.sql.SQLException;
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

public class RoutineinspectionReportDaoImpl implements RoutineinspectionReportDao {

	private HibernateTemplate hibernateTemplate;
	
	private Log log = LogFactory.getLog(this.getClass());

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}



	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	
	/**
	 * 根据组织ID获取巡检报表数据
	* @date Mar 6, 20132:35:33 PM
	* @Description: TODO 
	* @param @param orgId 组织ID
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getRoutineinspectionReport(final String orgId) {
		log.info("进入getRoutineinspectionReport方法");
		List<Map<String, Object>> list =  null;
		try{
			list =  this.hibernateTemplate
			.execute(new HibernateCallback<List<Map<String, Object>>>() {
				public List<Map<String, Object>> doInHibernate(Session session)
						throws HibernateException, SQLException {
					String sqlString ="SELECT " +
							"r.routineinspectionWoId \"routineinspectionWoId\"," +
							"r.planTitle \"planTitle\"," +
							"r.planStartTime \"planStartTime\"," +
							"r.planEndTime \"planEndTime\"," +
							"r.remark \"remark\"," +
							"r.orgId \"orgId\"," +
							"r.templateId \"templateId\"," +
							"r.type \"type\"," +
							"r.routineInspectionProfession \"routineInspectionProfession\"," +
							"r.vipBaseStationRoutineInspectionCount \"vipBaseStationRoutineInspectionCount\"," +
							"r.progress \"progress\"," +
							"r.actualProgress \"actualProgress\"," +
							"r.averageDeviationDistance \"averageDeviationDistance\"," +
							"r.timelyRate \"timelyRate\"," +
							"r.status \"status\"" +
							" FROM report_routineinspection  r where r.orgId in ("+orgId+")";
					//System.out.println(sqlString);
					SQLQuery query = session.createSQLQuery(sqlString);
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					List find = query.list();
					return find;
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
			log.error(this.getClass()+"类里的getRoutineinspectionReport方法数据库链接错误");
		}
		log.info("退出getRoutineinspectionReport方法 返回值为："+list);
		return list;
	}
	
	/**
	 * 根据组织ID获取巡检报表TOP6数据(未关闭)
	* @date Mar 6, 20132:36:15 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getRoutineinspectionReportTOPSix(final String orgId) {
		log.info("进入getRoutineinspectionReportTOPSix方法");
		List<Map<String, Object>> list =  null;
		try{
			list =  this.hibernateTemplate
			.execute(new HibernateCallback<List<Map<String, Object>>>() {
				public List<Map<String, Object>> doInHibernate(Session session)
						throws HibernateException, SQLException {
					String sqlString ="SELECT t.* from ("+
							" SELECT " +
							"r.routineinspectionWoId \"routineinspectionWoId\"," +
							"r.planTitle \"planTitle\"," +
							"r.planStartTime \"planStartTime\"," +
							"r.planEndTime \"planEndTime\"," +
							"r.remark \"remark\"," +
							"r.orgId \"orgId\"," +
							"r.templateId \"templateId\"," +
							"r.type \"type\"," +
							"r.routineInspectionProfession \"routineInspectionProfession\","+
							"r.progress \"progress\"," +
							"r.actualProgress \"actualProgress\"," +
							"r.averageDeviationDistance \"averageDeviationDistance\"," +
							"r.timelyRate \"timelyRate\"," +
							"r.status \"status\"" +
							" FROM report_routineinspection  r where r.orgId in ("+orgId+") and r.status <> '24'";
					sqlString = sqlString + "  ORDER BY planStartTime DESC ) t where rownum <= 6";
					//System.out.println(sqlString);
					SQLQuery query = session.createSQLQuery(sqlString);
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					List find = query.list();
					return find;
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
			log.error(this.getClass()+"类里的getRoutineinspectionReportTOPSix方法数据库链接错误");
		}
		log.info("退出getRoutineinspectionReportTOPSix方法 返回值为："+list);
		return list;
	}
	
	
	/**
	 * 根据组织ID获取巡检报表TOP4数据(已关闭)
	* @date Mar 6, 20132:36:15 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getRoutineinspectionReportTOPFourByClosed(final String orgId) {
		log.info("进入getRoutineinspectionReportTOPFourByClosed方法");
		List<Map<String, Object>> list =  null;
		try{
			list =  this.hibernateTemplate
			.execute(new HibernateCallback<List<Map<String, Object>>>() {
				public List<Map<String, Object>> doInHibernate(Session session)
						throws HibernateException, SQLException {
					String sqlString ="SELECT t.* from ("+
							"SELECT " +
							"r.routineinspectionWoId \"routineinspectionWoId\"," +
							"r.planTitle \"planTitle\"," +
							"r.planStartTime \"planStartTime\"," +
							"r.planEndTime \"planEndTime\"," +
							"r.remark \"remark\"," +
							"r.orgId \"orgId\"," +
							"r.templateId \"templateId\"," +
							"r.type \"type\"," +
							"r.routineInspectionProfession \"routineInspectionProfession\"," +
							"r.progress \"progress\"," +
							"r.actualProgress \"actualProgress\"," +
							"r.averageDeviationDistance \"averageDeviationDistance\"," +
							"r.timelyRate \"timelyRate\"," +
							"r.status \"status\"" +
							" FROM report_routineinspection  r where orgId in ("+orgId+") and status = '24'";
					sqlString = sqlString + "  ORDER BY planStartTime DESC ) t where rownum <= 4";
					//System.out.println(sqlString);
					SQLQuery query = session.createSQLQuery(sqlString);
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					List find = query.list();
					return find;
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
			log.error(this.getClass()+"类里的getRoutineinspectionReportTOPFourByClosed方法数据库链接错误");
		}
		log.info("退出getRoutineinspectionReportTOPFourByClosed方法 返回值为："+list);
		return list;
	}
}
