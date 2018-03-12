package com.iscreate.op.dao.report;

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



public class FaultBaseStationTopTenReportDaoImpl implements
		FaultBaseStationTopTenReportDao {

	private HibernateTemplate hibernateTemplate;

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	/**
	 * 根据组织id集合获取前10基站故障数的数据
	 * @param subOrgList
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public List<Map<String, Object>> getFaultBaseStationTopTenData(final List<Map<String,Object>> subOrgList,final String beginTime,final String endTime){
		
		List<Map<String, Object>> list =  this.hibernateTemplate
		.execute(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				
				if(subOrgList!=null && !subOrgList.isEmpty()){
					
					StringBuffer sb_param=new StringBuffer();
					if(subOrgList!=null && subOrgList.size()!=0){
						for(int i=0;i<subOrgList.size();i++){
							//sb_param.append("?").append(",");
							sb_param.append(subOrgList.get(i).get("orgId")).append(",");
						}
						sb_param.deleteCharAt(sb_param.length()-1);
					}
					
					String sqlString =
							"select "+
							"t1.baseStationName \"baseStationName\"," +
							"t1.baseStationType \"baseStationType\"," +
							"t1.baseStationId \"baseStationId\"," +
							"t1.workOrderCount \"workOrderCount\"," +
							"t1.countDate \"countDate\"," +
							"t1.areaId \"areaId\"" +
							" from report_topten  t1  " +
							" where t1.orgId in ("+sb_param.toString()+") and " +
							" t1.countDate >= to_date('"+beginTime+"','yyyy-mm-dd hh24:mi:ss') and t1.countDate < to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss')  " +
							" group by basestationId,basestationType,baseStationName,workOrderCount,areaId,countDate  ORDER BY workOrderCount DESC ";
					SQLQuery query = session.createSQLQuery(sqlString);
					
//					for(int i=0;i<subOrgList.size();i++){
//						query.setParameter(i, subOrgList.get(i).getId());
//					}
					
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					List list = new ArrayList();
					List find = query.list();
					if(find != null){
						if(find.size() > 10){
							for(int i = 0;i < 10;i++){
								list.add(find.get(i));
							}
						}else{
							list = find;
						}
					}
					return list;
					
				}
				return null;
			}
		});
		
		return list;
	}
	

	
	/**
	 * 根据项目id集合获取前10基站故障数的数据
	 * @param projectIdList
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public List<Map<String,Object>> getFaultBaseStationTopTenDataByProjectIdList(final List<String> projectIdList,final String beginTime,final String endTime){
		
		List<Map<String, Object>> list =  this.hibernateTemplate
		.execute(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				
				if(projectIdList!=null && !projectIdList.isEmpty()){
					
					StringBuffer sb_param=new StringBuffer();
					if(projectIdList!=null && projectIdList.size()!=0){
						for(int i=0;i<projectIdList.size();i++){
//							sb_param.append("?").append(",");
							sb_param.append(projectIdList.get(i)).append(",");
						}
						sb_param.deleteCharAt(sb_param.length()-1);
					}
					
					String sqlString ="select " +
							"t1.baseStationName \"baseStationName\"," +
							"t1.baseStationType \"baseStationType\"," +
							"t1.baseStationId \"baseStationId\"," +
							"t1.workOrderCount \"workOrderCount\"," +
							"t1.countDate \"countDate\""+
							" from report_topten_project  t1  " +
							"where t1.projectId in("+sb_param.toString()+") " +
							" and   t1.countDate >= to_date('"+beginTime+"','yyyy-mm-dd hh24:mi:ss') and t1.countDate < to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') " +
							" group by basestationId,basestationType,baseStationName,workOrderCount,areaId,countDate  ORDER BY workOrderCount DESC ";
					SQLQuery query = session.createSQLQuery(sqlString);
					
//					for(int i=0;i<projectIdList.size();i++){
//						query.setParameter(i, projectIdList.get(i));
//					}
					
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					List find = query.list();
					List list = new ArrayList();
					if(find != null){
						if(find.size() > 10){
							for(int i = 0;i < 10;i++){
								list.add(find.get(i));
							}
						}else{
							list = find;
						}
					}
					return list;
					
				}
				return null;
			}
		});
		
		return list;
	}
}
