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


import com.iscreate.op.pojo.system.SysOrg;

public class CountFaultBaseStationTopFiveTenByOrgReportDaoImpl implements
		CountFaultBaseStationTopFiveTenByOrgReportDao {

	
	private HibernateTemplate hibernateTemplate;
	
	
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	
	/**
	 * 获取基站故障数最差分布的数据
	 * @param allSubOrgWithChildList 当前组织及其所有的子组织集合
	 * @param targetAllSubList 目标的组织的及其下级组织
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public List<Map<String, Object>> getCountFaultBaseStationTopFiveTenData(final List<SysOrg> allSubOrgWithChildList,final List<Map<String,Object>> targetAllSubList,final String beginTime,final String endTime){
		List<Map<String, Object>> list=this.hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				if(allSubOrgWithChildList!=null && !allSubOrgWithChildList.isEmpty() && targetAllSubList!=null && !targetAllSubList.isEmpty() ){
					StringBuilder sb_param=new StringBuilder();
					for(int i=0;i<allSubOrgWithChildList.size();i++){
//						sb_param.append("?,");
						sb_param.append(allSubOrgWithChildList.get(i).getOrgId()+",");
					}
					sb_param.deleteCharAt(sb_param.length()-1);
					
					
					StringBuilder sb_param2=new StringBuilder();
					for(int i=0;i<targetAllSubList.size();i++){
						sb_param2.append(targetAllSubList.get(i).get("orgId")+",");
					}
					sb_param2.deleteCharAt(sb_param2.length()-1);
					
//					String sqlString ="select count(t100.basestationId) \"baseStationCount\" from " +
//							" (select t20.basestationId,t20.basestationType,t20.orgId from (select t10.basestationId,t10.basestationType,t10.orgId from " +
//							" (select t1.basestationId,t1.basestationType,t1.workOrderCount,t1.countDate,t1.orgId from report_topten  t1  where " +
//							" t1.countDate <= to_date('"+beginTime+"','yyyy-mm-dd hh24:mi:ss') and t1.countDate >= to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss')" +
//							" group by t1.basestationId,t1.basestationType " +
//							" ORDER BY workOrderCount DESC) t10 where t10.orgId in ("+sb_param.toString()+") and t10.countDate <= to_date('"+beginTime+"','yyyy-mm-dd hh24:mi:ss') and t10.countDate >= to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') " +
//									"   order by t10.workOrderCount desc) t20 where rownum <= 50 ) " +
//									" where t100.orgId in ("+sb_param2.toString()+");";
					String sqlString =
					"select count(t100.basestationId) \"baseStationCount\" "+
					"  from (select t20.basestationId, t20.basestationType, t20.orgId "+
					         " from (select t10.basestationId, t10.basestationType, t10.orgId "+
					                 " from (select t1.basestationId, "+
					                       "        t1.basestationType, "+
					                       "        t1.workOrderCount, "+
					                        "       t1.countDate, "+
					                       "        t1.orgId "+
					                       "   from report_topten t1 "+
					                        " where t1.countDate >= "+
					                        "       to_date('"+beginTime+"', "+
					                         "              'yyyy-mm-dd hh24:mi:ss') "+
					                         "  and t1.countDate < "+
					                           "    to_date('"+endTime+"', "+
					                                 "      'yyyy-mm-dd hh24:mi:ss') "+
					                       "  group by t1.basestationId, "+
					                             "     t1.basestationType, "+
					                              "    t1.workOrderCount, "+
					                            "      t1.countDate, "+
					                             "     t1.orgId "+
					                       "  ORDER BY t1.workOrderCount DESC) t10 "+
					               "  where t10.orgId in "+
					                      " ("+sb_param.toString()+") "+
					          	     "  and t10.countDate >= "+
					                    "   to_date('"+beginTime+"', 'yyyy-mm-dd hh24:mi:ss') "+
					                 "  and t10.countDate < "+
					                     "  to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss') "+
					               "  order by t10.workOrderCount desc) t20 "+
					        " where rownum <= 50) t100 "+
					 " where t100.orgId in "+
					       " ("+sb_param2.toString()+") ";
					SQLQuery query = session.createSQLQuery(sqlString);
//					for(int i=0;i<allSubOrgWithChildList.size();i++){
//        				query.setParameter(i,allSubOrgWithChildList.get(i).getId());
//        			}
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					List find = query.list();
					return find;
				}
				return null;
			}
		});
		return list;
	}
	
	
	/**
	 * 按地域获取基站故障数最差分布的数据
	 * @param allSubAreaWithChildList
	 * @param targetAllSubAreaIdList
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public List<Map<String, Object>> getCountFaultBaseStationTopFiveTenDataForArea(final List<String> allSubAreaWithChildList,final List<String> targetAllSubAreaIdList,final String beginTime,final String endTime){
		List<Map<String, Object>> list=this.hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				if(allSubAreaWithChildList!=null && !allSubAreaWithChildList.isEmpty() && targetAllSubAreaIdList!=null && !targetAllSubAreaIdList.isEmpty() ){
					StringBuilder sb_param=new StringBuilder();
					for(int i=0;i<allSubAreaWithChildList.size();i++){
//						sb_param.append("?,");
						sb_param.append(allSubAreaWithChildList.get(i)+",");
					}
					sb_param.deleteCharAt(sb_param.length()-1);
					
					
					StringBuilder sb_param2=new StringBuilder();
					for(int i=0;i<targetAllSubAreaIdList.size();i++){
						sb_param2.append(targetAllSubAreaIdList.get(i)+",");
					}
					sb_param2.deleteCharAt(sb_param2.length()-1);
					
					String sqlString =
						"select count(t100.basestationId) \"baseStationCount\" "+
						"  from (select t20.basestationId, t20.basestationType, t20.areaId "+
						         " from (select t10.basestationId, t10.basestationType, t10.areaId "+
						                 " from (select t1.basestationId, "+
						                       "        t1.basestationType, "+
						                       "        t1.workOrderCount, "+
						                        "       t1.countDate, "+
						                       "        t1.areaId "+
						                       "   from report_topten t1 "+
						                        " where t1.countDate >= "+
						                        "       to_date('"+beginTime+"', "+
						                         "              'yyyy-mm-dd hh24:mi:ss') "+
						                         "  and t1.countDate < "+
						                           "    to_date('"+endTime+"', "+
						                                 "      'yyyy-mm-dd hh24:mi:ss') "+
						                       "  group by t1.basestationId, "+
						                             "     t1.basestationType, "+
						                              "    t1.workOrderCount, "+
						                            "      t1.countDate, "+
						                             "     t1.areaId "+
						                       "  ORDER BY t1.workOrderCount DESC) t10 "+
						               "  where t10.areaId in "+
						                      " ("+sb_param.toString()+") "+
						          	     "  and t10.countDate >= "+
						                    "   to_date('"+beginTime+"', 'yyyy-mm-dd hh24:mi:ss') "+
						                 "  and t10.countDate < "+
						                     "  to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss') "+
						               "  order by t10.workOrderCount desc) t20 "+
						        " where rownum <= 50) t100 "+
						 " where t100.areaId in "+
						       " ("+sb_param2.toString()+") ";
					
					SQLQuery query = session.createSQLQuery(sqlString);
//					for(int i=0;i<allSubAreaWithChildList.size();i++){
//        				query.setParameter(i,allSubAreaWithChildList.get(i));
//        			}
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					List find = query.list();
					return find;
				}
				return null;
			}
		});
		return list;
	}
	
	/**
	 * 按项目获取基站故障数最差分布的数据
	 * @param allSubOrgWithChildList 当前组织及其所有的子组织集合
	 * @param targetOrgList 目标的组织集合
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public List<Map<String, Object>> getCountFaultBaseStationTopFiveTenDataForProject(final List<String> allProjectIdList,final String targetProjectId,final String beginTime,final String endTime){
		List<Map<String, Object>> list=this.hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				if(allProjectIdList!=null && !allProjectIdList.isEmpty() && targetProjectId!=null && !targetProjectId.isEmpty()){
					StringBuilder sb_param=new StringBuilder();
					for(int i=0;i<allProjectIdList.size();i++){
//						sb_param.append("?,");
						sb_param.append(allProjectIdList.get(i)+",");
					}
					sb_param.deleteCharAt(sb_param.length()-1);
					
					
					StringBuilder sb_param2=new StringBuilder();
					sb_param2.append(targetProjectId);
					
					String sqlString =
						"select count(t100.basestationId) \"baseStationCount\" "+
						"  from (select t20.basestationId, t20.basestationType, t20.projectId "+
						         " from (select t10.basestationId, t10.basestationType, t10.projectId "+
						                 " from (select t1.basestationId, "+
						                       "        t1.basestationType, "+
						                       "        t1.workOrderCount, "+
						                        "       t1.countDate, "+
						                       "        t1.projectId "+
						                       "   from report_topten_project t1 "+
						                        " where t1.countDate >= "+
						                        "       to_date('"+beginTime+"', "+
						                         "              'yyyy-mm-dd hh24:mi:ss') "+
						                         "  and t1.countDate < "+
						                           "    to_date('"+endTime+"', "+
						                                 "      'yyyy-mm-dd hh24:mi:ss') "+
						                       "  group by t1.basestationId, "+
						                             "     t1.basestationType, "+
						                              "    t1.workOrderCount, "+
						                            "      t1.countDate, "+
						                             "     t1.projectId "+
						                       "  ORDER BY t1.workOrderCount DESC) t10 "+
						               "  where t10.projectId in "+
						                      " ("+sb_param.toString()+") "+
						          	     "  and t10.countDate >= "+
						                    "   to_date('"+beginTime+"', 'yyyy-mm-dd hh24:mi:ss') "+
						                 "  and t10.countDate < "+
						                     "  to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss') "+
						               "  order by t10.workOrderCount desc) t20 "+
						        " where rownum <= 50) t100 "+
						 " where t100.projectId in "+
						       " ("+sb_param2.toString()+") ";
					SQLQuery query = session.createSQLQuery(sqlString);
					
//					for(int i=0;i<allProjectIdList.size();i++){
//        				query.setParameter(i,allProjectIdList.get(i));
//        			}
					
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					List find = query.list();
					return find;
				}
				return null;
			}
		});
		return list;
		
	}
	
	
	
	
	
	
	
	/**
	 * 获取基站故障数最差分布的数据
	 * @param orgList
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	@Deprecated
	public List<Map<String, Object>> getCountFaultBaseStationTopFiveTenData(final List<SysOrg> orgList,final String beginTime,final String endTime){
		
		List<Map<String, Object>> list=this.hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				if(orgList!=null && !orgList.isEmpty()){
					StringBuilder sb_param=new StringBuilder();
					for(int i=0;i<orgList.size();i++){
//						sb_param.append("?,");
						sb_param.append(orgList.get(i).getOrgId()+",");
					}
					sb_param.deleteCharAt(sb_param.length()-1);
					
//					String sqlString ="select count(*) as baseStationCount from (select t1.* from report_faultBaseStationTopTen as t1 group by t1.basestationId,t1.basestationType,t1.orgId ORDER BY workOrderCount DESC limit 0,50) t10 where t10.orgId in  ("+sb_param.toString()+") and t10.countDate between '"+beginTime+"' and '"+endTime+"'";
					
					String sqlString ="select count(*)  \"baseStationCount\" " +
							"from (select t1.* from report_TopTen  t1 " +
							"group by t1.basestationId,t1.basestationType ORDER BY workOrderCount DESC) t10" +
							" where t10.orgId in  ("+sb_param.toString()+") and " +
							" t10.countDate >= to_date('"+beginTime+"','yyyy-mm-dd hh24:mi:ss') and t10.countDate < to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') " +
									" order by t10.workOrderCount desc rownum <= 50";
					
					
					
//					错误的:String sqlString="select count(*) as baseStationCount from (select * from (select t1.* from report_faultBaseStationTopTen as t1 group by basestationId,basestationType) as t10 where t10.orgId in ("+sb_param.toString()+") and t10.countDate between '"+beginTime+"' and '"+endTime+"' ORDER BY t10.workOrderCount DESC limit 0,50) t20";
					
					SQLQuery query = session.createSQLQuery(sqlString);
//					for(int i=0;i<orgList.size();i++){
//        				query.setParameter(i,orgList.get(i).getId());
//        			}
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					List find = query.list();
					return find;
				}
				return null;
			}
		});
		return list;
	}
	
	/**
	 * 获取基站故障数最差分布的环比数据
	 * @param orgList
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	@Deprecated
	public List<Map<String, Object>> getCountFaultBaseStationTopFiveTenLoopCompareData(final List<SysOrg> orgList,final String beginTime,final String endTime){
		List<Map<String, Object>> list=this.hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				if(orgList!=null && !orgList.isEmpty()){
					StringBuilder sb_param=new StringBuilder();
					for(int i=0;i<orgList.size();i++){
						sb_param.append("?,");
//						sb_param.append(orgList.get(i).getId()+",");
					}
					sb_param.deleteCharAt(sb_param.length()-1);
					
					String sqlString ="select count(*)  \"baseStationCount\" from " +
							"(select t1.* from report_TopTen  t1 " +
							"group by t1.basestationId,t1.basestationType,t1.orgId " +
							"ORDER BY workOrderCount DESC rownum <= 50) t10 " +
							"where t10.orgId in  ("+sb_param.toString()+") " +
							" and  t10.countDate >= to_date('"+beginTime+"','yyyy-mm-dd hh24:mi:ss') and t10.countDate <= to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') ";
					SQLQuery query = session.createSQLQuery(sqlString);
					for(int i=0;i<orgList.size();i++){
        				query.setParameter(i,orgList.get(i).getOrgId());
        			}
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					List find = query.list();
					return find;
				}
				return null;
			}
		});
		return list;
	}
	
	
	
	/**
	 * 按地域获取基站故障数最差分布的数据
	 * @param orgList
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public List<Map<String, Object>> getCountFaultBaseStationTopFiveTenDataForArea(final List<String> areaIdList,final String beginTime,final String endTime){
		
		List<Map<String, Object>> list=this.hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				if(areaIdList!=null && !areaIdList.isEmpty()){
					StringBuilder sb_param=new StringBuilder();
					for(int i=0;i<areaIdList.size();i++){
						sb_param.append("?,");
//						sb_param.append(areaIdList.get(i).getId()+",");
					}
					sb_param.deleteCharAt(sb_param.length()-1);
					
					
					String sqlString ="select count(*) as \"baseStationCount\" from " +
							"(select t1.* from report_TopTen  t1 " +
							" group by t1.basestationId,t1.basestationType,t1.orgId ORDER BY workOrderCount DESC rownum <= 50) " +
							" t10 where t10.areaId in  ("+sb_param.toString()+") " +
							" and  t10.countDate >= to_date('"+beginTime+"','yyyy-mm-dd hh24:mi:ss') and t10.countDate <= to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') ";
					SQLQuery query = session.createSQLQuery(sqlString);
					for(int i=0;i<areaIdList.size();i++){
        				query.setParameter(i,areaIdList.get(i));
        			}
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					List find = query.list();
					return find;
				}
				return null;
			}
		});
		return list;
	}
	
	/**
	 * 按地域获取基站故障数最差分布的环比数据
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String,Object>> getCountFaultBaseStationTopFiveTenLoopCompareDataForArea(final List<String> areaIdList,final String beginTime,final String endTime){
		
		List<Map<String, Object>> list=this.hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				if(areaIdList!=null && !areaIdList.isEmpty()){
					StringBuilder sb_param=new StringBuilder();
					for(int i=0;i<areaIdList.size();i++){
						sb_param.append("?,");
//						sb_param.append(orgList.get(i).getId()+",");
					}
					sb_param.deleteCharAt(sb_param.length()-1);
					
					String sqlString ="select count(*)  \"baseStationCount\" " +
							" from (select by t1.basestationId,t1.basestationType,t1.orgId from report_TopTen  t1 " +
							" group by t1.basestationId,t1.basestationType,t1.orgId " +
							" ORDER BY workOrderCount DESC rownum <= 50) t10 " +
							" where t10.areaId in  ("+sb_param.toString()+") and " +
							"  t10.countDate >= to_date('"+beginTime+"','yyyy-mm-dd hh24:mi:ss') and t10.countDate < to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') ";
					SQLQuery query = session.createSQLQuery(sqlString);
					for(int i=0;i<areaIdList.size();i++){
        				query.setParameter(i,areaIdList.get(i));
        			}
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					List find = query.list();
					return find;
				}
				return null;
			}
		});
		return list;
	}
	
}
