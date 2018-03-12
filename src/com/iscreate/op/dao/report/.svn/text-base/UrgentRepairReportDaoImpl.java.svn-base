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


import com.iscreate.op.pojo.urgentrepair.UrgentrepairWorkorder;


/**
 * 统计故障想修报表
 *
 */
public class UrgentRepairReportDaoImpl implements UrgentRepairReportDao{
	
	private HibernateTemplate hibernateTemplate;
	
	

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}



	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}



	/**
	 *  获取故障抢修指定时间段的报表数据
	* @date Nov 2, 20125:02:59 PM
	* @Description: TODO 
	* @param @param bizId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowNames
	* @param @param judges
	* @param @param rowValues
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairBylatestAllowedTimeAndJudge(final List<String>  orgIds,final String beginTime,final String endTime,final List<String> rowNames ,final List<String> judges ,final List<String> rowValues ) {
		List<Map<String, Object>> list =  this.hibernateTemplate
		.execute(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				List find = null;
				if(orgIds != null && !orgIds.isEmpty()){
					String reIdsString = "( rusd.organizationId in ( ";
					for(int i = 0;i < orgIds.size();i++){
						if(i%499 == 0){
							if(i == orgIds.size() - 1 ){
								reIdsString = reIdsString + orgIds.get(i) + " ) )";
							}else{
								reIdsString = reIdsString + orgIds.get(i) + " ) or rusd.organizationId in ( ";
							}
						}else{
							if(i == orgIds.size() - 1 ){
								reIdsString = reIdsString + orgIds.get(i) + " ) )";
							}else{
								reIdsString = reIdsString + orgIds.get(i) + ",";
							}
						}
					}
//					if(reIdsString != null && !reIdsString.equals("")){
//						reIdsString = reIdsString.substring(0, reIdsString.length() - 1);
//					}
					String sqlString ="SELECT " +
					"rusd.ID \"id\"," +
					"rusd.WOID," +
					"rusd.WOTITLE," +
					"rusd.CREATETIME," +
					"rusd.faultOccuredTime \"faultOccuredTime\"," +
					"rusd.latestAllowedTime \"latestAllowedTime\"," +
					"rusd.workOrderAcceptedTime \"workOrderAcceptedTime\"," +
					"rusd.alarmClearTime \"alarmClearTime\"," +
					"rusd.LASTEDITTIME," +
					"rusd.organizationId \"organizationId\"," +
					"rusd.CREATEPERSON," +
					"rusd.PARTICIPANT," +
					"rusd.ISOVERTIME," +
					"rusd.STATUS," +
					"rusd.isEmergencyFault \"isEmergencyFault\"," +
					"rusd.sideeffectService \"sideeffectService\"," +
					"rusd.faultInWhichProvince \"faultInWhichProvince\"," +
					"rusd.faultInWhichCity \"faultInWhichCity\"," +
					"rusd.faultInWhichCountry \"faultInWhichCountry\"," +
					"rusd.faultInWhichTown \"faultInWhichTown\"," +
					"rusd.faultType \"faultType\"," +
					"rusd.faultLevel \"faultLevel\"," +
					"rusd.faultArea \"faultArea\"," +
					"rusd.workAcceptedTime \"workAcceptedTime\"," +
					"rusd.troudleshootingTime \"troudleshootingTime\"," +
					"rusd.workOrderProcessingTime \"workOrderProcessingTime\"," +
					"rusd.AcceptedTimeRate \"AcceptedTimeRate\"," +
					"rusd.ProcessTimeRate \"ProcessTimeRate\"," +
					"rusd.CompletionRate \"CompletionRate\"," +
					"rusd.faultCause \"faultCause\"," +
					"rusd.baseStationLevel \"baseStationLevel\"," +
					"rusd.acceptProfessional \"acceptProfessional\"," +
					"rusd.faultGenera \"faultGenera\"," +
					"rusd.networkResourceId \"networkResourceId\"," +
					"rusd.networkResourceType \"networkResourceType\" " +
					" FROM report_urgencyrepair  rusd  WHERE rusd.latestAllowedTime >= to_date('"+beginTime+"','yyyy-mm-dd hh24:mi:ss') AND rusd.latestAllowedTime < to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') AND "+reIdsString;
					if(rowNames != null && judges != null && rowNames.size() > 0 && judges.size() > 0){
						for(int i = 0; i < rowNames.size(); i++){
							if(rowValues == null || rowValues.get(i) == null || rowValues.get(i).equals("")){
								sqlString = sqlString + " AND (" + rowNames.get(i) + " " + judges.get(i) + " '' or " + rowNames.get(i) + " is null)";
							}else{
								sqlString = sqlString + " AND " + rowNames.get(i) + " " + judges.get(i) + " '" + rowValues.get(i)+"'";
							}
						}
					}
					sqlString = sqlString + "  ORDER BY createtime DESC  ";
					//System.out.println(sqlString);
					SQLQuery query = session.createSQLQuery(sqlString);
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					find = query.list();
				}
				return find;
			}
		});
		return list;
	}
	
	
	/**
	 * 获取故障抢修指定时间段的报表数据(根据资源ID与资源类型)
	* @date Jan 17, 20139:52:04 AM
	* @Description: TODO 
	* @param @param reIds
	* @param @param reType
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowNames
	* @param @param judges
	* @param @param rowValues
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairByreIdsAndreType(final List<String> reIds ,final String reType ,final String beginTime,final String endTime,final List<String> rowNames ,final List<String> judges ,final List<String> rowValues ) {
		List<Map<String, Object>> list =  this.hibernateTemplate
		.execute(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				List find = null;
				if(reIds != null && !reIds.isEmpty()){
					String reIdsString = "( rusd.networkResourceId in ( ";
					for(int i = 0;i < reIds.size();i++){
						if(i%499 == 0){
							if(i == reIds.size() - 1 ){
								reIdsString = reIdsString + reIds.get(i) + " ) )";
							}else{
								reIdsString = reIdsString + reIds.get(i) + " ) or rusd.networkResourceId in ( ";
							}
						}else{
							if(i == reIds.size() - 1 ){
								reIdsString = reIdsString + reIds.get(i) + " ) )";
							}else{
								reIdsString = reIdsString + reIds.get(i) + ",";
							}
						}
					}
//					if(reIdsString != null && !reIdsString.equals("")){
//						reIdsString = reIdsString.substring(0, reIdsString.length() - 1);
//					}
					String sqlString ="SELECT " +
							"rusd.ID \"id\"," +
							"rusd.WOID," +
							"rusd.WOTITLE," +
							"rusd.CREATETIME," +
							"rusd.faultOccuredTime \"faultOccuredTime\"," +
							"rusd.latestAllowedTime \"latestAllowedTime\"," +
							"rusd.workOrderAcceptedTime \"workOrderAcceptedTime\"," +
							"rusd.alarmClearTime \"alarmClearTime\"," +
							"rusd.LASTEDITTIME," +
							"rusd.organizationId \"organizationId\"," +
							"rusd.CREATEPERSON," +
							"rusd.PARTICIPANT," +
							"rusd.ISOVERTIME," +
							"rusd.STATUS," +
							"rusd.isEmergencyFault \"isEmergencyFault\"," +
							"rusd.sideeffectService \"sideeffectService\"," +
							"rusd.faultInWhichProvince \"faultInWhichProvince\"," +
							"rusd.faultInWhichCity \"faultInWhichCity\"," +
							"rusd.faultInWhichCountry \"faultInWhichCountry\"," +
							"rusd.faultInWhichTown \"faultInWhichTown\"," +
							"rusd.faultType \"faultType\"," +
							"rusd.faultLevel \"faultLevel\"," +
							"rusd.faultArea \"faultArea\"," +
							"rusd.workAcceptedTime \"workAcceptedTime\"," +
							"rusd.troudleshootingTime \"troudleshootingTime\"," +
							"rusd.workOrderProcessingTime \"workOrderProcessingTime\"," +
							"rusd.AcceptedTimeRate \"AcceptedTimeRate\"," +
							"rusd.ProcessTimeRate \"ProcessTimeRate\"," +
							"rusd.CompletionRate \"CompletionRate\"," +
							"rusd.faultCause \"faultCause\"," +
							"rusd.baseStationLevel \"baseStationLevel\"," +
							"rusd.acceptProfessional \"acceptProfessional\"," +
							"rusd.faultGenera \"faultGenera\"," +
							"rusd.networkResourceId \"networkResourceId\"," +
							"rusd.networkResourceType \"networkResourceType\" " +
							" FROM report_urgencyrepair  rusd  WHERE rusd.latestAllowedTime >= to_date('"+beginTime+"','yyyy-mm-dd hh24:mi:ss') AND rusd.latestAllowedTime < to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') AND "+reIdsString+" and rusd.networkResourceType ='"+reType+"' " ;
					if(rowNames != null && judges != null && rowNames.size() > 0 && judges.size() > 0){
						for(int i = 0; i < rowNames.size(); i++){
							if(rowValues == null || rowValues.get(i) == null || rowValues.get(i).equals("")){
								sqlString = sqlString + " AND (" + rowNames.get(i) + " " + judges.get(i) + " '' or " + rowNames.get(i) + " is null)";
							}else{
								sqlString = sqlString + " AND " + rowNames.get(i) + " " + judges.get(i) + " '" + rowValues.get(i)+"'";
							}
						}
					}
					sqlString = sqlString + "  ORDER BY createtime DESC  ";
					//System.out.println(sqlString);
					SQLQuery query = session.createSQLQuery(sqlString);
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					find = query.list();
				}
				return find;
			}
		});
		return list;
	}
}
