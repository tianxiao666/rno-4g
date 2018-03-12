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



public class AnalyseFaultCountAndResourceBalanceReportDaoImpl implements
		AnalyseFaultCountAndResourceBalanceReportDao {

	
	private HibernateTemplate hibernateTemplate;
	
	
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}




	/**
	 * 根据组织id集合获取组织对应的每百个基站的人/车/任务量统计分析的数据
	 * @param orgList
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public List<Map<String, Object>> getAnalyseFaultCountAndResourceBalanceData(final List<Map<String,Object>> orgList,final String beginTime,final String endTime){
		 List<Map<String, Object>> list=this.hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				if(orgList!=null && !orgList.isEmpty()){
					StringBuilder sb_param=new StringBuilder();
					for(int i=0;i<orgList.size();i++){
						sb_param.append(orgList.get(i).get("orgId")+",");
						//sb_param.append("?,");
					}
					sb_param.deleteCharAt(sb_param.length()-1);
					
					String sqlString ="select " +
							"sum(t1.peopleCount) \"peopleCount\"," +
							"sum(t1.carCount) \"carCount\"," +
							"sum(t1.workOrderCount) \"workOrderCount\"," +
							"sum(t1.baseStationCount) \"baseStationCount\"" +
							" from report_analysefaultcount  t1  where t1.orgId in ("+sb_param.toString()+") and t1.countDate >= to_date('"+beginTime+"','yyyy-mm-dd hh24:mi:ss') and t1.countDate < to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss')";
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
	 * 根据开始时间、结束时间，获取对应的每百个基站的人/车/任务量统计分析的环比数据
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> getAnalyseFaultCountAndResourceBalanceLoopCompareData(final String beginTime,final String endTime){
		
		List<Map<String, Object>> list=this.hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				StringBuilder sb_param=new StringBuilder();
				
				String sqlString ="select " +
						"sum(t1.peopleCount) \"peopleCount\"," +
						"sum(t1.carCount) \"carCount\"," +
						"sum(t1.workOrderCount) \"workOrderCount\"," +
						"sum(t1.baseStationCount) \"baseStationCount\"" +
						" from report_analysefaultcount  t1  where t1.countDate >= to_date('"+beginTime+"','yyyy-mm-dd hh24:mi:ss') and t1.countDate < to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss')";
				SQLQuery query = session.createSQLQuery(sqlString);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List find = query.list();
				return find;
			}
		});
		return list;
	}
	
	
	/**
	 * 获取组织对应的每百个基站的人/车/任务量统计分析的数据
	 * @param orgList
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public List<Map<String, Object>> getAnalyseFaultCountAndResourceBalanceDataForProject(final List<String> orgList,final String beginTime,final String endTime){
	
		 List<Map<String, Object>> list=this.hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
				public List<Map<String, Object>> doInHibernate(Session session)
						throws HibernateException, SQLException {
					if(orgList!=null && !orgList.isEmpty()){
						StringBuilder sb_param=new StringBuilder();
						for(int i=0;i<orgList.size();i++){
							sb_param.append(orgList.get(i)+",");
//							sb_param.append("?,");
						}
						sb_param.deleteCharAt(sb_param.length()-1);
						
						String sqlString ="select " +
								"sum(t1.peopleCount) \"peopleCount\"," +
								"sum(t1.carCount) \"carCount\"," +
								"sum(t1.workOrderCount) \"workOrderCount\"," +
								"sum(t1.baseStationCount) \"baseStationCount\"" +
								" from report_analysefaultcount  t1  where t1.orgId in ("+sb_param.toString()+") and " +
								" t1.countDate >= to_date('"+beginTime+"','yyyy-mm-dd hh24:mi:ss') and t1.countDate < to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss')";
						SQLQuery query = session.createSQLQuery(sqlString);
//						for(int i=0;i<orgList.size();i++){
//	        				query.setParameter(i,orgList.get(i));
//	        			}
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
	 * 按项目获取每百个基站的人/车/任务量统计分析的环比数据
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String,Object>> getAnalyseFaultCountAndResourceBalanceLoopCompareDataForProject(final String beginTime,final String endTime){
		List<Map<String, Object>> list=this.hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				StringBuilder sb_param=new StringBuilder();
				
				String sqlString ="select sum(t1.peopleCount) \"peopleCount\"," +
						"sum(t1.carCount) \"carCount\"," +
						"sum(t1.workOrderCount) \"workOrderCount\"," +
						"sum(t1.baseStationCount) \"baseStationCount\"" +
						" from report_analysefaultcount  t1  where " +
						" t1.countDate >= to_date('"+beginTime+"','yyyy-mm-dd hh24:mi:ss') and t1.countDate < to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss')";
				SQLQuery query = session.createSQLQuery(sqlString);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List find = query.list();
				return find;
			}
		});
		return list;
	}
	
}
