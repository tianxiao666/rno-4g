package com.iscreate.op.dao.cardispatch;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.cardispatch.CardispatchInstrumentReading;

public class CardispatchInstrumentReadingDaoImpl implements CardispatchInstrumentReadingDao{
	private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	/**
	 * 添加车辆仪表读数
	* @author ou.jh
	* @date Aug 1, 2013 10:56:49 AM
	* @Description: TODO 
	* @param @param cardispatchInstrumentReading        
	* @throws
	 */
	public void addCardispatchInstrumentReading(CardispatchInstrumentReading cardispatchInstrumentReading){
		this.hibernateTemplate.save(cardispatchInstrumentReading);
	}
	
	/**
	 * 更新车辆仪表读数
	* @author ou.jh
	* @date Aug 1, 2013 10:57:03 AM
	* @Description: TODO 
	* @param @param cardispatchInstrumentReading        
	* @throws
	 */
	public void updateCardispatchInstrumentReading(CardispatchInstrumentReading cardispatchInstrumentReading){
		this.hibernateTemplate.update(cardispatchInstrumentReading);
	}
	
	/**
	 * 根据ID获取车辆仪表读数
	* @author ou.jh
	* @date Aug 5, 2013 2:47:30 PM
	* @Description: TODO 
	* @param @param id
	* @param @return        
	* @throws
	 */
	public CardispatchInstrumentReading getCardispatchInstrumentReading(long id){
		return this.hibernateTemplate.get(CardispatchInstrumentReading.class, id);
	}

	
	/**
	 * 根据ID获取车辆仪表读数
	* @author ou.jh
	* @date Aug 1, 2013 11:08:01 AM
	* @Description: TODO 
	* @param @param id        
	* @throws
	 */
	public Map<String,Object> getCardispatchInstrumentReadingById(long id){
			String sql =" select "
						+"	cir.instrument_reading_id \"instrument_reading_id\","
						+"	cir.instrument_reading \"instrument_reading\","
						+"	cir.car_id \"car_id\","
						+"	cir.recording_time \"recording_time\","
						+"	cir.create_time \"create_time\","
						+"	cir.create_user_id \"create_user_id\","
						+"	cir.create_remarks \"create_remarks\","
						+"	cir.delete_time \"delete_time\","
						+"	cir.delete_user_id \"delete_user_id\","
						+"	cir.delete_cause \"delete_cause\","
						+"	cir.status \"status\","
						+"	cc.carnumber \"car_car\","
						+"	cu.name \"create_user\" ,"
						+"	du.name \"delete_user\" ,"
						+"	so.name \"orgName\""
						+" "
						+"	 from car_Instrument_Reading cir "
						+"	left join car_car cc on cc.id = cir.car_id "
						+"  left join sys_org_user cu on cir.create_user_id = cu.org_user_id "
						+"  left join sys_org_user du on cir.delete_user_id = du.org_user_id "
						+"	left join sys_org so on so.org_id = cc.carbizid "
						+"	where cir.instrument_reading_id = " + id;
			List<Map<String,Object>> executeSqlForObject = executeSqlForObject(sql);
			if(executeSqlForObject != null && executeSqlForObject.size() > 0){
				return executeSqlForObject.get(0);
			}else{
				return null;
			}
	}
	
	/**
	 * 根据车辆仪表读数（分页）
	* @author ou.jh
	* @date Aug 1, 2013 11:08:01 AM
	* @Description: TODO 
	* @param @param id        
	* @throws
	 */
	public List<Map<String,Object>> getCardispatchInstrumentReadingPaging(String startTime,String endTime,long orgId,String carId,int currentPage, int pageSize){
		String sql = "select * "
						+"	from (  select  r.instrument_reading_id \"instrument_reading_id\","
					    +"   r.instrument_reading \"instrument_reading\","
					    +"   r.car_id \"car_id\","
					    +"   r.recording_time \"recording_time\","
					    +"   r.create_time \"create_time\","
					    +"   r.create_user_id \"create_user_id\","
					    +"   r.create_remarks \"create_remarks\","
					    +"   r.delete_time \"delete_time\","
					    +"   r.delete_user_id \"delete_user_id\","
					    +"   r.delete_cause \"delete_cause\","
					    +"   r.status \"status\","
					    +"   r.carNumber \"carNumber\","
					    +"   r.carType \"carType\","
					    +"   r.cuName \"create_user\","
					    +"   r.duName \"delete_user\","
					    +"   r.orgName \"orgName\","
					    +"   rownum ro"
					    +"   from (select "
						+"	cir.instrument_reading_id ,"
						+"	cir.instrument_reading ,"
						+"	cir.car_id ,"
						+"	cir.recording_time ,"
						+"	cir.create_time ,"
						+"	cir.create_user_id ,"
						+"	cir.create_remarks ,"
						+"	cir.delete_time ,"
						+"	cir.delete_user_id ,"
						+"	cir.delete_cause ,"
						+"	cir.status ,"
						+"	cc.carNumber ,"
						+"	cc.carType ,"
						+"	cu.name cuName ,"
						+"	du.name duName ,"
						+"	so.name orgName "
						+" "
						+"  from car_Instrument_Reading cir "
						+"  left join car_car cc on cir.car_id = cc.id "
						+"  left join sys_org_user cu on cir.create_user_id = cu.org_user_id "
						+"  left join sys_org_user du on cir.delete_user_id = du.org_user_id "
						+"	left join sys_org so on so.org_id = cc.carbizid "
						+"	where so.path like '%/" + orgId + "/%'";
			if(carId != null && !carId.isEmpty()){
				sql = sql  +  " and cir.car_id = " + carId;
			}else{
				sql = sql;
			}
			if(startTime != null && !startTime.isEmpty() && endTime != null && !endTime.isEmpty() ){
				sql = sql  +  "  and cir.recording_time >= to_date('" + startTime + "', 'yyyy-mm-dd') "
							+ "  and cir.recording_time <= to_date('" + endTime + "', 'yyyy-mm-dd') ";
			}else{
				sql = sql;
			}
			int mixResults = (currentPage - 1) * pageSize;
			if(mixResults != 0){
				mixResults = mixResults + 1 ;
			}else{
				mixResults = mixResults;
			}
			int maxResults  = currentPage * pageSize;
			sql = sql + " order by cc.carNumber,cir.instrument_reading,cir.recording_time )  r) r1  where r1.ro >= " + mixResults + " and r1.ro <= " + maxResults + " ";		   
			List<Map<String,Object>> executeSqlForObject = this.executeSqlForObject(sql);		  
			return executeSqlForObject;
	}
	
	/**
	 * 根据车辆仪表读数
	* @author ou.jh
	* @date Aug 1, 2013 11:08:01 AM
	* @Description: TODO 
	* @param @param id        
	* @throws
	 */
	public List<Map<String,Object>> getCardispatchInstrumentReading(String startTime,String endTime,long orgId,String carId){
		String sql = " select  r.instrument_reading_id \"instrument_reading_id\","
					    +"   r.instrument_reading \"instrument_reading\","
					    +"   r.car_id \"car_id\","
					    +"   r.recording_time \"recording_time\","
					    +"   r.create_time \"create_time\","
					    +"   r.create_user_id \"create_user_id\","
					    +"   r.create_remarks \"create_remarks\","
					    +"   r.delete_time \"delete_time\","
					    +"   r.delete_user_id \"delete_user_id\","
					    +"   r.delete_cause \"delete_cause\","
					    +"   r.status \"status\","
					    +"   r.carNumber \"carNumber\","
					    +"   r.carType \"carType\","
					    +"   r.cuName \"create_user\","
					    +"   r.duName \"delete_user\","
					    +"   r.orgName \"orgName\","
					    +"   r.ro "
					    +"   from (select "
						+"	cir.instrument_reading_id ,"
						+"	cir.instrument_reading ,"
						+"	cir.car_id ,"
						+"	cir.recording_time ,"
						+"	cir.create_time ,"
						+"	cir.create_user_id ,"
						+"	cir.create_remarks ,"
						+"	cir.delete_time ,"
						+"	cir.delete_user_id ,"
						+"	cir.delete_cause ,"
						+"	cir.status ,"
						+"	cc.carNumber ,"
						+"	cc.carType ,"
						+"	cu.name cuName ,"
						+"	du.name duName ,"
						+"	so.name orgName ,"
						+"	rownum ro"
						+" "
						+"  from car_Instrument_Reading cir "
						+"  left join car_car cc on cir.car_id = cc.id "
						+"  left join sys_org_user cu on cir.create_user_id = cu.org_user_id "
						+"  left join sys_org_user du on cir.delete_user_id = du.org_user_id "
						+"	left join sys_org so on so.org_id = cc.carbizid "
						+"	where so.path like '%/" + orgId + "/%'";
			if(carId != null && !carId.isEmpty()){
				sql = sql  +  " and cir.car_id = " + carId;
			}else{
				sql = sql;
			}
			if(startTime != null && !startTime.isEmpty() && endTime != null && !endTime.isEmpty() ){
				sql = sql  +  "  and cir.recording_time >= to_date('" + startTime + "', 'yyyy-mm-dd') "
							+ "  and cir.recording_time <= to_date('" + endTime + "', 'yyyy-mm-dd') ";
			}else{
				sql = sql;
			}
			sql = sql + " order by cc.carNumber,cir.instrument_reading,cir.recording_time ) r ";
			List<Map<String,Object>> executeSqlForObject = this.executeSqlForObject(sql);		  
			return executeSqlForObject;
	}
	
	/**
	 * 根据车辆仪表读数总数	
	* @author ou.jh
	* @date Aug 1, 2013 11:08:01 AM
	* @Description: TODO 
	* @param @param id        
	* @throws
	 */
	public int getCardispatchInstrumentReadingCount(String startTime,String endTime,long orgId,String carId){
		String sql = " select count(*) \"co\" "
						+"  from car_Instrument_Reading cir "
						+"  left join car_car cc on cir.car_id = cc.id "
						+"  left join sys_org_user cu on cir.create_user_id = cu.org_user_id "
						+"  left join sys_org_user du on cir.create_user_id = du.org_user_id "
						+"	left join sys_org so on so.org_id = cc.carbizid "
						+"	where so.path like '%/" + orgId + "/%'";
			if(carId != null && !carId.isEmpty()){
				sql = sql  +  " and cir.car_id = " + carId;
			}else{
				sql = sql;
			}	   
			if(startTime != null && !startTime.isEmpty() && endTime != null && !endTime.isEmpty() ){
				sql = sql  +  "  and cir.recording_time >= to_date('" + startTime + "', 'yyyy-mm-dd') "
							+ "  and cir.recording_time <= to_date('" + endTime + "', 'yyyy-mm-dd') ";
			}else{
				sql = sql;
			}
			List<Map<String,Object>> executeSqlForObject = this.executeSqlForObject(sql);		
			if(executeSqlForObject != null && executeSqlForObject.size() > 0){
				 Map<String, Object> map = executeSqlForObject.get(0);
				 if(map != null){
					 if(map.get("co") != null && !map.get("co").equals("")){
						 String string = map.get("co").toString();
						 int count = Integer.parseInt(string);
						 return count;
					 }else{
						 return 0;
					 }
				 }else{
					 return 0;
				 }
			}else{
				return 0;
			}
	}
	
	
	/**
	 * 根据年月获取最大仪表读数
	* @author ou.jh
	* @date Aug 1, 2013 11:08:01 AM
	* @Description: TODO 
	* @param @param id        
	* @throws
	 */
	public double getMaxReadingByDate(String date,long carId){
		if(date == null || date.isEmpty()){
			return 0;
		}
			String sql =" select max(cir.instrument_reading) \"maxReading\" "
						+"	  from car_Instrument_Reading cir "
						+"	 where cir.recording_time <= to_date('"+date+"', 'yyyy-mm-dd') and cir.status = 1 and cir.car_Id = " + carId;

			List<Map<String,Object>> executeSqlForObject = executeSqlForObject(sql);
			if(executeSqlForObject != null && executeSqlForObject.size() > 0){
				 Map<String, Object> map = executeSqlForObject.get(0);
				 if(map != null){
					 if(map.get("maxReading") != null && !map.get("maxReading").equals("")){
						 return Double.parseDouble(map.get("maxReading").toString());
					 }else{
						 return 0;
					 }
				 }else{
					 return 0;
				 }
			}else{
				return 0;
			}
	}
	
	/**
	 * 根据年月获取最小仪表读数
	* @author ou.jh
	* @date Aug 1, 2013 11:08:01 AM
	* @Description: TODO 
	* @param @param id        
	* @throws
	 */
	public double getMinReadingByDate(String date,long carId){
		if(date == null || date.isEmpty()){
			return 0;
		}
			String sql =" select min(cir.instrument_reading) \"minReading\" "
						+"	  from car_Instrument_Reading cir "
						+"	 where cir.recording_time >= to_date('"+date+"', 'yyyy-mm-dd') and cir.status = 1 and cir.car_Id = " + carId;

			List<Map<String,Object>> executeSqlForObject = executeSqlForObject(sql);
			if(executeSqlForObject != null && executeSqlForObject.size() > 0){
				 Map<String, Object> map = executeSqlForObject.get(0);
				 if(map != null){
					 if(map.get("minReading") != null && !map.get("minReading").equals("")){
						 return Double.parseDouble(map.get("minReading").toString());
					 }else{
						 return 0;
					 }
				 }else{
					 return 0;
				 }
			}else{
				return 0;
			}
	}
	
	/**
	 * 获取车辆当前日期仪表数据
	* @author ou.jh
	* @date Aug 1, 2013 10:47:22 AM
	* @Description: TODO 
	* @param @param date
	* @param @param carId
	* @param @return        
	* @throws
	 */
	public int getReadingCountByDate(String date,long carId){
		if(date == null || date.isEmpty()){
			return 0;
		}
			String sql =" select count(1) \"count\" "
						+"	  from car_Instrument_Reading cir "
						+"	 where cir.recording_time = to_date('"+date+"', 'yyyy-mm-dd') and cir.status = 1 and cir.car_Id = " + carId;

			List<Map<String,Object>> executeSqlForObject = executeSqlForObject(sql);
			if(executeSqlForObject != null && executeSqlForObject.size() > 0){
				 Map<String, Object> map = executeSqlForObject.get(0);
				 if(map != null){
					 if(map.get("count") != null && !map.get("count").equals("")){
						 return Integer.parseInt(map.get("count").toString());
					 }else{
						 return 0;
					 }
				 }else{
					 return 0;
				 }
			}else{
				return 0;
			}
	}
	
	public List<Map<String, Object>> executeSqlForObject(final String sqlString) {
		List<Map<String, Object>> list = hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(
							Session session) throws HibernateException,
							SQLException {
						SQLQuery query = session.createSQLQuery(sqlString);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> find = query.list();
						return find;
					}
				});
		return list;
	}
	
	public int executeSqlForOperateObject(final String sqlString) {
		Integer i = hibernateTemplate
				.execute(new HibernateCallback<Integer>() {
					public Integer doInHibernate(
							Session session) throws HibernateException,
							SQLException {
						Query query=session.createQuery(sqlString);
						int i = query.executeUpdate();
						return i;
					}
				});
		return i;
	}
}
