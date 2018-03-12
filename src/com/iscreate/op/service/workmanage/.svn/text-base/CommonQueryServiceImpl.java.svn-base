package com.iscreate.op.service.workmanage;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.workmanage.WorkmanageStatusreg;
import com.iscreate.op.service.workmanage.util.CommonQueryUtil;
import com.iscreate.op.service.workmanage.util.DataSelectUtil;



public class CommonQueryServiceImpl implements CommonQueryService {

	
	private static final Log logger = LogFactory.getLog(CommonQueryServiceImpl.class);
	
	
	private DataSelectUtil dataSelectUtil;
	
	
	private HibernateTemplate hibernateTemplate;
	
	
	public int firstResult=0;
	public int maxResult=0;
	
	
	
	/**
	 * 公共查询
	 * @param queryEntityName 查询实体名称
	 * @param conditionString 附加条件
	 * @return
	 */
	public Map<String,Object> commonQueryService(String queryEntityName,String conditionString){
		return this.commonQueryService(null, null, null, null, queryEntityName, null, conditionString);
	}
	
	/**
	 * 公共查询
	 * @param queryEntityName 查询实体名称
	 * @param inputCondition 查询输入条件
	 * @return
	 */
	public Map<String,Object> commonQueryService(String queryEntityName,Map<String,String> inputCondition){
		return this.commonQueryService(null, null, null, null, queryEntityName, inputCondition, null);
	}
	
	
	/**
	 * 公共查询
	 * @param orderName	排序列
	 * @param order 升序or降序
	 * @param queryEntityName 查询实体名称
	 * @param conditionString 附加条件
	 * @return
	 */
	public Map<String,Object> commonQueryService(String orderName,String order,String queryEntityName,String conditionString){
		return this.commonQueryService(null, null, orderName, order, queryEntityName, null, conditionString);
	}
	
	
	
	
	/**
	 * 公共查询
	 * @param orderName	排序列
	 * @param order 升序or降序
	 * @param queryEntityName 查询实体名称
	 * @param inputCondition 查询输入条件
	 * @return
	 */
	public Map<String,Object> commonQueryService(String orderName,String order,String queryEntityName,Map<String,String> inputCondition){
		return this.commonQueryService(null, null, orderName, order, queryEntityName, inputCondition, null);
	}
	
	
	
	/**
	 * 公共查询
	 * @param start 记录开始下标
	 * @param limit 每页记录显示数量
	 * @param orderName 排序列
	 * @param order 顺序or降序
	 * @param queryEntityName 查询实体名称
	 * @param inputCondition 表单输入条件
	 * @param conditionString 附加条件
	 * @return
	 */
	public Map<String, Object> commonQueryService(final String start, final String limit,
			String orderName, String order, String queryEntityName,
			Map<String, String> inputCondition, String conditionString) {
		
		
		int count = 0;
		Map<String,Object> map = null;
		
		if(queryEntityName!=null&&!"".equals(queryEntityName)){
			queryEntityName = queryEntityName.trim();
		}else{
			return null;
		}
		
		String sql = "SELECT * FROM "+queryEntityName+" t where ";
		String countSql = "SELECT count(*) as \"count\" FROM "+queryEntityName+" t where ";
		
//		String sql = "SELECT * FROM "+queryEntityName+" t ";
//		String countSql = "SELECT count(*) as count FROM "+queryEntityName+" t ";
		
		//String countSql = "SELECT * FROM "+queryEntityName+" t where ";
		
		//组装查询输入的条件
		StringBuilder formSqlString = new StringBuilder();
		if(inputCondition!=null){
			for(String key:inputCondition.keySet()){
				if(key!=null && !"".equals(key)){
					if(key.indexOf("_operator")<0 && key.indexOf("_type")<0){	//操作符与字段类型标识除外
						
						if(inputCondition.get(key)!=null && !"".equals(inputCondition.get(key))){
							if("<=".equals(inputCondition.get(key+"_operator"))){
								if(formSqlString.length()!=0){
									formSqlString.append(" AND ");
								}							
								formSqlString.append("\""+key.subSequence(0, key.length()-1)+"\"");
								formSqlString.append(" "+CommonQueryUtil.getSqlOperator(inputCondition.get(key+"_operator")));
								
								//时间类型
								if("dateTime".equals(inputCondition.get(key+"_type").trim())){
//									formSqlString.append(" str_to_date('"+inputCondition.get(key).toString().trim()+"','%Y-%m-%d %H:%i:%s') ");
									formSqlString.append(" to_date('"+inputCondition.get(key).toString().trim()+"','yyyy-MM-dd HH24:mi:ss') ");
								//日期类型
								}else if("date".equals(inputCondition.get(key+"_type").trim())){
//									formSqlString.append(" str_to_date('"+inputCondition.get(key).toString().trim()+"','%Y-%m-%d') ");
									formSqlString.append(" to_date('"+inputCondition.get(key).toString().trim()+"','yyyy-MM-dd HH24:mi:ss') ");
								}else{
									formSqlString.append("\"");
									formSqlString.append(inputCondition.get(key).toString().trim());
									formSqlString.append("\" ");
								}
							}else if("like".equals(inputCondition.get(key+"_operator"))){
								if(formSqlString.length()!=0){
									formSqlString.append(" AND ");
								}
								formSqlString.append("\""+key+"\"");
								formSqlString.append(" "+CommonQueryUtil.getSqlOperator(inputCondition.get(key+"_operator")));
								
								String input_val=inputCondition.get(key).toString().trim();
								
								if(input_val.indexOf("%")>-1){
									input_val=getEscapeSpecialChar(input_val, "%");
									formSqlString.append(" '%");
									formSqlString.append(""+input_val);
									formSqlString.append("%' ");
									formSqlString.append(" escape '/'");
								}else{
									
									if("dateTime".equals(inputCondition.get(key+"_type").trim())){
										formSqlString.append(" to_date('"+inputCondition.get(key).toString().trim()+"','yyyy-MM-dd HH24:mi:ss') ");
									}else if("date".equals(inputCondition.get(key+"_type").trim())){
										formSqlString.append(" to_date('"+inputCondition.get(key).toString().trim()+"','yyyy-MM-dd') ");
									}else{
										formSqlString.append(" '%");
										formSqlString.append(input_val);
										formSqlString.append("%' ");
									}
								}
								
							}else{
								
								if("dataScope".equals(inputCondition.get(key+"_type").trim())){
									
								}else{
									if(formSqlString.length()!=0){
										formSqlString.append(" AND ");
									}
									formSqlString.append("\""+key+"\"");
									formSqlString.append(" "+CommonQueryUtil.getSqlOperator(inputCondition.get(key+"_operator")));
									
								}
								
								//时间类型
								if("dateTime".equals(inputCondition.get(key+"_type").trim())){
//									formSqlString.append(" str_to_date('"+inputCondition.get(key).toString().trim()+"','%Y-%m-%d %H:%i:%s') ");
									formSqlString.append(" to_date('"+inputCondition.get(key).toString().trim()+"','yyyy-MM-dd HH24:mi:ss') ");
								//日期类型
								}else if("date".equals(inputCondition.get(key+"_type").trim())){
//									formSqlString.append(" str_to_date('"+inputCondition.get(key).toString().trim()+"','%Y-%m-%d') ");
									formSqlString.append(" to_date('"+inputCondition.get(key).toString().trim()+"','yyyy-MM-dd HH24:mi:ss') ");
								}else if("dataScope".equals(inputCondition.get(key+"_type").trim())){
									
								}else{
									formSqlString.append("\'");
									formSqlString.append(inputCondition.get(key).toString().trim());
									formSqlString.append("\' ");
								}
							}
						}
					}
				}
			}
		}
		
		//查询条件存在的情况
		if(formSqlString!=null&&formSqlString.length()>0){
			sql +=formSqlString;
			countSql +=formSqlString;
		}else{
			sql +="1=1";
			countSql +="1=1";
		}
		
		//附加查询条件存在的情况
		if(conditionString!=null&&!"".equals(conditionString)){
			sql +=" "+conditionString;
			countSql +=" "+conditionString;
		}
		
		//排序条件
		if((orderName!=null && !"".equals(orderName)) && (order!=null && !"".equals(order))){
			
			if(orderName.indexOf(",")!=-1){
				String[] arrOrderName=orderName.split(",");
				String[] arrorder=order.split(",");
				for(int i=0;i<arrOrderName.length;i++){
					if(arrOrderName[i].contains("\"")){
						if(i==0){
							sql +=" order by "+arrOrderName[i]+" "+arrorder[i];
							countSql +=" order by "+arrOrderName[i]+" "+arrorder[i];
						}else{
							sql +=" , "+arrOrderName[i]+" "+arrorder[i];
							countSql +=" , "+arrOrderName[i]+" "+arrorder[i];
						}
					}else{
						if(i==0){
							sql +=" order by '"+arrOrderName[i]+"' "+arrorder[i];
							countSql +=" order by '"+arrOrderName[i]+"' "+arrorder[i];
						}else{
							sql +=" , '"+arrOrderName[i]+"' "+arrorder[i];
							countSql +=" , '"+arrOrderName[i]+"' "+arrorder[i];
						}
					}
					
				}
			}else{
				if(orderName.contains("\"")){
					sql +=" order by "+orderName+" "+order;
					countSql +=" order by "+orderName+" "+order;
				}else{
					sql +=" order by '"+orderName+"' "+order;
					countSql +=" order by '"+orderName+"' "+order;
				}
				
			}
		}
		
		//分页条件
		if((start!=null && !"".equals(start)) && (limit!=null &&!"".equals(limit))){
//			sql +=" limit "+start+","+limit;
//			countSql +=" limit "+start+","+limit;
		}
		
		//String limit_countSql = "SELECT count(*) as count FROM ("+countSql+") t2";
		
		List<Map> list=null;
		List<Map> countList=null;
		try {
//			list=this.dataSelectUtil.selectDataWithCondition(sql, null);
//			countList = this.dataSelectUtil.selectDataWithCondition(countSql, null);
			
			final String executeSql=sql;
			final String executeCountSql=countSql;
			
			
//			if((start!=null && !"".equals(start)) && (limit!=null &&!"".equals(limit))){
//				firstResult=Integer.parseInt(start);
//				maxResult=Integer.parseInt(limit);
//			}
			
			
			//获取数据
			list=this.hibernateTemplate.executeFind(new HibernateCallback(){
				public List doInHibernate(Session session) throws HibernateException, SQLException
				{
					SQLQuery sqlQuery =session.createSQLQuery(executeSql);
					
					if((start!=null && !"".equals(start)) && (limit!=null &&!"".equals(limit))){
						
						int maxResult=Integer.parseInt(limit);
						int firstResult=Integer.parseInt(start);
						
						sqlQuery.setMaxResults(maxResult);
						sqlQuery.setFirstResult(firstResult);
					}
					
					sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					List result=sqlQuery.list();
					
					return result;
				}
			});
			
			//获取总数
			countList=this.hibernateTemplate.executeFind(new HibernateCallback(){
				public List doInHibernate(Session session) throws HibernateException, SQLException
				{
					SQLQuery sqlQuery =session.createSQLQuery(executeCountSql);
					sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					List result=sqlQuery.list();
					
					return result;
				}
			});
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("进行工作管理公共查询的时候，有可能数据库链接异常或者执行的sql编写有误，引起该异常");
		}
		
		//System.out.println("sql=="+sql);
		
		if(countList!=null && countList.size()>0){
			String countString = countList.get(0).get("count")==null?"0":countList.get(0).get("count").toString();
			count = Integer.valueOf(countString);
		}
		
		if(list!=null && list.size()>0){
			map = new HashMap<String,Object>();
			map.put("count", count);
			map.put("entityList", list);
		}
		
		return map;
	}

	
	
	
	
	/**
	 * 获取工单定义的所有状态列表
	 * @return
	 */
	public List<Map> getAllWorkorderStatusList(){
//		try {
//			String sql="select * from workmanage_statusreg where statusType=1 order by id asc";
//			List<Map> list=this.dataSelectUtil.selectDataWithCondition(sql, null);
//			return list;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		List<Map> list=null;
		
		list=this.hibernateTemplate.executeFind(new HibernateCallback(){

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql="select * from wm_statusreg where statusType=1 order by id asc";
				SQLQuery sqlQuery =session.createSQLQuery(sql);
				sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List result=sqlQuery.list();
				return result;
			}
			
		});
		return list;
	}
	
	
	/**
	 * 获取任务单定义的所有状态列表
	 * @return
	 */
	public List<Map> getAllTaskorderStatusList(){
//		try {
//			String sql="select * from workmanage_statusreg where statusType=2 order by id asc";
//			List<Map> list=this.dataSelectUtil.selectDataWithCondition(sql, null);
//			return list;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		List<Map> list=null;
		
		list=this.hibernateTemplate.executeFind(new HibernateCallback(){

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql="select * from wm_statusreg where statusType=2 order by id asc";
				SQLQuery sqlQuery =session.createSQLQuery(sql);
				sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List result=sqlQuery.list();
				return result;
			}
			
		});
		return list;
	}
	
	
	public static String getEscapeSpecialChar(String val,String filterChar){
		if(val!=null && !"".equals(val)){
			val=val.replace(filterChar, "/"+filterChar);
		}
		return val;
	}
	
	
	
	
	
	public DataSelectUtil getDataSelectUtil() {
		return dataSelectUtil;
	}

	public void setDataSelectUtil(DataSelectUtil dataSelectUtil) {
		this.dataSelectUtil = dataSelectUtil;
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	
	

}
