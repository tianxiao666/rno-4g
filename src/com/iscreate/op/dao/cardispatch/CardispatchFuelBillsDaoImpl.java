package com.iscreate.op.dao.cardispatch;

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

import com.iscreate.op.pojo.cardispatch.CardispatchFuelBills;

public class CardispatchFuelBillsDaoImpl implements CardispatchFuelBillsDao {
	
	private HibernateTemplate hibernateTemplate;
	
	
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}



	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}


	/**
	 * 添加车辆油费
	* @author ou.jh
	* @date Aug 1, 2013 10:51:40 AM
	* @Description: TODO 
	* @param @param cardispatchFuelBills        
	* @throws
	 */
	public void addCardispatchFuelBills(CardispatchFuelBills cardispatchFuelBills){
		this.hibernateTemplate.save(cardispatchFuelBills);
	}
	
	/**
	 * 更新车辆油费
	* @author ou.jh
	* @date Aug 1, 2013 10:52:48 AM
	* @Description: TODO 
	* @param @param cardispatchFuelBills        
	* @throws
	 */
	public void updateCardispatchFuelBills(CardispatchFuelBills cardispatchFuelBills){
		this.hibernateTemplate.update(cardispatchFuelBills);
	}
	
	/**
	 * 根据ID获取车辆油费读数
	* @author ou.jh
	* @date Aug 5, 2013 2:47:30 PM
	* @Description: TODO 
	* @param @param id
	* @param @return        
	* @throws
	 */
	public CardispatchFuelBills getCardispatchFuelBillsBy(long id){
		return this.hibernateTemplate.get(CardispatchFuelBills.class, id);
	}
	
	
	/**
	 * 根据ID获取车辆油费
	* @author ou.jh
	* @date Aug 1, 2013 11:08:01 AM
	* @Description: TODO 
	* @param @param id        
	* @throws
	 */
	public Map<String,Object> getCardispatchFuelBillsById(long id){
			String sql =" select "
						+"	cfb.fuel_bills_id \"fuel_bills_id\","
						+"	cfb.fuel_bills \"fuel_bills\","
						+"	cfb.car_id \"car_id\","
						+"	cfb.recording_time \"recording_time\","
						+"	cfb.create_time \"create_time\","
						+"	cfb.create_user_id \"create_user_id\","
						+"	cfb.create_remarks \"create_remarks\","
						+"	cfb.delete_time \"delete_time\","
						+"	cfb.delete_user_id \"delete_user_id\","
						+"	cfb.delete_cause \"delete_cause\","
						+"	cfb.status \"status\","
						+"	cc.carnumber \"car_car\","
						+"	cu.name \"create_user\" ,"
						+"	du.name \"delete_user\" ,"
						+"	so.name \"orgName\""
						+" "
						+"	 from car_Fuel_Bills cfb "
						+"	left join car_car cc on cc.id = cfb.car_id "
						+"  left join sys_org_user cu on cfb.create_user_id = cu.org_user_id "
						+"  left join sys_org_user du on cfb.delete_user_id = du.org_user_id "
						+"	left join sys_org so on so.org_id = cc.carbizid "
						+"	where cfb.fuel_bills_id = " + id;
			List<Map<String,Object>> executeSqlForObject = executeSqlForObject(sql);
			if(executeSqlForObject != null && executeSqlForObject.size() > 0){
				return executeSqlForObject.get(0);
			}else{
				return null;
			}
	}
	
	/**
	 * 根据车辆油费（分页）
	* @author ou.jh
	* @date Aug 1, 2013 11:08:01 AM
	* @Description: TODO 
	* @param @param id        
	* @throws
	 */
	public List<Map<String,Object>> getCardispatchFuelBillsPaging(String startTime,String endTime,long orgId,String carId,int currentPage, int pageSize){
		String sql = "select * "
							+"	from ( select  r.fuel_bills_id \"fuel_bills_id\","
						    +"   r.fuel_bills \"fuel_bills\","
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
							+"	cfb.fuel_bills_id ,"
							+"	cfb.fuel_bills ,"
							+"	cfb.car_id ,"
							+"	cfb.recording_time ,"
							+"	cfb.create_time ,"
							+"	cfb.create_user_id ,"
							+"	cfb.create_remarks ,"
							+"	cfb.delete_time ,"
							+"	cfb.delete_user_id ,"
							+"	cfb.delete_cause ,"
							+"	cfb.status ,"
							+"	cc.carNumber ,"
							+"	cc.carType ,"
							+"	cu.name cuName ,"
							+"	du.name duName ,"
							+"	so.name orgName "
							+" "
							+"  from car_fuel_bills cfb "
							+"  left join car_car cc on cfb.car_id = cc.id "
							+"  left join sys_org_user cu on cfb.create_user_id = cu.org_user_id "
							+"  left join sys_org_user du on cfb.delete_user_id = du.org_user_id "
							+"	left join sys_org so on so.org_id = cc.carbizid "
							+"	where so.path like '%/" + orgId + "/%'";
			if(carId != null && !carId.isEmpty()){
				sql = sql  +  " and cfb.car_id = " + carId;
			}else{
				sql = sql;
			}
			if(startTime != null && !startTime.isEmpty() && endTime != null && !endTime.isEmpty() ){
				sql = sql  +  "  and cfb.recording_time >= to_date('" + startTime + "', 'yyyy-mm-dd') "
							+ "  and cfb.recording_time <= to_date('" + endTime + "', 'yyyy-mm-dd') ";
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
			sql = sql + "  order by cc.carNumber,cfb.fuel_bills,cfb.recording_time )  r) r1  where r1.ro >= " + mixResults + " and r1.ro <= " + maxResults + " ";		
			List<Map<String,Object>> executeSqlForObject = this.executeSqlForObject(sql);		  
			return executeSqlForObject;
	}
	
	
	/**
	 * 根据车辆油费
	* @author ou.jh
	* @date Aug 1, 2013 11:08:01 AM
	* @Description: TODO 
	* @param @param id        
	* @throws
	 */
	public List<Map<String,Object>> getCardispatchFuelBills(String startTime,String endTime,long orgId,String carId){
		String sql = " select  r.fuel_bills_id \"fuel_bills_id\","
						    +"   r.fuel_bills \"fuel_bills\","
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
							+"	cfb.fuel_bills_id ,"
							+"	cfb.fuel_bills ,"
							+"	cfb.car_id ,"
							+"	cfb.recording_time ,"
							+"	cfb.create_time ,"
							+"	cfb.create_user_id ,"
							+"	cfb.create_remarks ,"
							+"	cfb.delete_time ,"
							+"	cfb.delete_user_id ,"
							+"	cfb.delete_cause ,"
							+"	cfb.status ,"
							+"	cc.carNumber ,"
							+"	cc.carType ,"
							+"	cu.name cuName ,"
							+"	du.name duName ,"
							+"	so.name orgName ,"
							+"	rownum ro"
							+" "
							+"  from car_fuel_bills cfb "
							+"  left join car_car cc on cfb.car_id = cc.id "
							+"  left join sys_org_user cu on cfb.create_user_id = cu.org_user_id "
							+"  left join sys_org_user du on cfb.delete_user_id = du.org_user_id "
							+"	left join sys_org so on so.org_id = cc.carbizid "
							+"	where so.path like '%/" + orgId + "/%'";
			if(carId != null && !carId.isEmpty()){
				sql = sql  +  " and cfb.car_id = " + carId;
			}else{
				sql = sql;
			}
			if(startTime != null && !startTime.isEmpty() && endTime != null && !endTime.isEmpty() ){
				sql = sql  +  "  and cfb.recording_time >= to_date('" + startTime + "', 'yyyy-mm-dd') "
							+ "  and cfb.recording_time <= to_date('" + endTime + "', 'yyyy-mm-dd') ";
			}else{
				sql = sql;
			}

			sql = sql + "  order by cc.carNumber,cfb.fuel_bills,cfb.recording_time ) r ";		
			List<Map<String,Object>> executeSqlForObject = this.executeSqlForObject(sql);		  
			return executeSqlForObject;
	}
	
	/**
	 * 根据车辆油费总数
	* @author ou.jh
	* @date Aug 1, 2013 11:08:01 AM
	* @Description: TODO 
	* @param @param id        
	* @throws
	 */
	public int getCardispatchFuelBillsCount(String startTime,String endTime,long orgId,String carId){
		String sql = " select count(*) \"co\" "
						+"  from car_Fuel_Bills cfb "
						+"  left join car_car cc on cfb.car_id = cc.id "
						+"  left join sys_org_user cu on cfb.create_user_id = cu.org_user_id "
						+"  left join sys_org_user du on cfb.create_user_id = du.org_user_id "
						+"	left join sys_org so on so.org_id = cc.carbizid "
						+"	where so.path like '%/" + orgId + "/%'";
			if(carId != null && !carId.isEmpty()){
				sql = sql  +  " and cfb.car_id = " + carId;
			}else{
				sql = sql;
			}	   
			if(startTime != null && !startTime.isEmpty() && endTime != null && !endTime.isEmpty() ){
				sql = sql  +  "  and cfb.recording_time >= to_date('" + startTime + "', 'yyyy-mm-dd') "
							+ "  and cfb.recording_time <= to_date('" + endTime + "', 'yyyy-mm-dd') ";
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
	 * 根据年月获取最大油费
	* @author ou.jh
	* @date Aug 1, 2013 11:08:01 AM
	* @Description: TODO 
	* @param @param id        
	* @throws
	 */
	public double getMaxBillsByDate(String date,long carId){
		if(date == null || date.isEmpty()){
			return 0;
		}
			String sql =" select max(cfb.Fuel_Bills) \"maxBills\" "
						+"	  from car_Fuel_Bills cfb "
						+"	 where cfb.recording_time <= to_date('"+date+"', 'yyyy-mm-dd')' and cfb.status = 1  and cfb.car_Id = " + carId;

			List<Map<String,Object>> executeSqlForObject = executeSqlForObject(sql);
			if(executeSqlForObject != null && executeSqlForObject.size() > 0){
				 Map<String, Object> map = executeSqlForObject.get(0);
				 if(map != null){
					 if(map.get("maxBills") != null && !map.get("maxBills").equals("")){
						 return Double.parseDouble(map.get("maxBills").toString());
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
