package com.iscreate.op.dao.report;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class BaseStationOutDaoImpl implements BaseStationOutDao {
private HibernateTemplate hibernateTemplate;
	
	

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}



	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/* (non-Javadoc)
	 * @see com.iscreate.op.dao.report.baseStationOutDao#getbaseStationOut(long, java.lang.String, java.lang.String)
	 */
	public List<Map<String, Object>> getbaseStationOut(final String orgId,final String beginTime,final String endTime){
		List<Map<String, Object>> list =  this.hibernateTemplate
		.execute(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql ="SELECT " +
//					"rusd.ID \"id\"," +
//					"rusd.WOID," +
//					"rusd.WOTITLE," +
//					"rusd.CREATETIME," +
//					"rusd.faultOccuredTime \"faultOccuredTime\"," +
//					"rusd.latestAllowedTime \"latestAllowedTime\"," +
//					"rusd.workOrderAcceptedTime \"workOrderAcceptedTime\"," +
//					"rusd.alarmClearTime \"alarmClearTime\"," +
//					"rusd.LASTEDITTIME," +
//					"rusd.organizationId \"organizationId\"," +
//					"rusd.CREATEPERSON," +
//					"rusd.PARTICIPANT," +
//					"rusd.ISOVERTIME," +
//					"rusd.STATUS," +
//					"rusd.isEmergencyFault \"isEmergencyFault\"," +
//					"rusd.sideeffectService \"sideeffectService\"," +
//					"rusd.faultInWhichProvince \"faultInWhichProvince\"," +
//					"rusd.faultInWhichCity \"faultInWhichCity\"," +
//					"rusd.faultInWhichCountry \"faultInWhichCountry\"," +
//					"rusd.faultInWhichTown \"faultInWhichTown\"," +
//					"rusd.faultType \"faultType\"," +
//					"rusd.faultLevel \"faultLevel\"," +
//					"rusd.faultArea \"faultArea\"," +
//					"rusd.workAcceptedTime \"workAcceptedTime\"," +
//					"rusd.troudleshootingTime \"troudleshootingTime\"," +
//					"rusd.workOrderProcessingTime \"workOrderProcessingTime\"," +
//					"rusd.AcceptedTimeRate \"AcceptedTimeRate\"," +
//					"rusd.ProcessTimeRate \"ProcessTimeRate\"," +
//					"rusd.CompletionRate \"CompletionRate\"," +
//					"rusd.faultCause \"faultCause\"," +
//					"rusd.baseStationLevel \"baseStationLevel\"," +
//					"rusd.acceptProfessional \"acceptProfessional\"," +
//					"rusd.faultGenera \"faultGenera\"," +
					"rusd.networkResourceId \"networkResourceId\"," +
					"rusd.networkResourceType \"networkResourceType\" " +													
						" FROM report_urgencyrepair rusd WHERE " +
						" rusd.faultType = 'HALTED_CELL' " +
						" AND rusd.CREATETIME >= to_date('"+beginTime+"','yyyy-mm-dd hh24:mi:ss') AND rusd.CREATETIME <= to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') " +
								" AND rusd.organizationId in ("+orgId+") AND rusd.networkResourceId is not null GROUP BY rusd.networkResourceId,rusd.networkResourceType";
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List find = query.list();
				return find;
			}
		});
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.iscreate.op.dao.report.baseStationOutDao#getAreaBaseStationCount(java.lang.String)
	 */
	public List<Map<String, Object>> getAreaBaseStationCount(final String areaId){
		List<Map<String, Object>> list =  this.hibernateTemplate
		.execute(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql ="SELECT " +
						"r.id \"id\"," +
						"r.areaId \"areaId\"," +
						"r.baseStationCount \"baseStationCount\" " +
						" FROM report_area_basestationcnt r WHERE r.areaId IN ("+areaId+")";
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List find = query.list();
				return find;
			}
		});
		return list;
	}
}
