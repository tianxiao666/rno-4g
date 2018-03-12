package com.iscreate.op.dao.cardispatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.dao.informationmanage.BaseDaoImpl;
import com.iscreate.op.pojo.cardispatch.CardispatchCar;

public class CarFuelReportDaoImpl extends BaseDaoImpl<CardispatchCar> implements CarFuelReportDao {
	/**
	 * 
	 * @description: 根据组织id查询组织的车辆信息（包括下级组织）(分页)
	 * @author：yuan.yw
	 * @param orgId
	 * @param indexStart
	 * @param indexEnd
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 2, 2013 11:29:09 AM
	 */
	public List<Map<String,Object>> getCarInfoByOrgId(String orgId,int indexStart,int indexEnd){
		if(orgId==null && "".equals(orgId)){
			return null;
		}	
		String sql = "select car.carnumber, car.id, car.carbizid, car.orgname"+
		"  from (select c.*,ROWNUM num  from (select c.carnumber, c.id, c.carbizid, o.name orgname"+
		"          from car_car c"+
		"          left join sys_org o on o.org_id = c.carbizid"+
		"         where c.carbizid in (select org_id"+
		"                                from sys_org"+
		"                              connect by prior org_id = parent_id"+
		"                               start with org_id = "+orgId+")"+
		"         order by case when c.carbizid="+orgId+" then 1 else 0 end desc,o.name desc,c.carnumber desc) c ) car"+
		" where car.num >= "+indexStart+" and car.num <="+indexEnd;
		if(indexStart==0){
			sql = " select c.carnumber,c.id,c.carbizid,o.name orgname"+
			" from car_car c left join sys_org o on o.org_id = c.carbizid "+
			" where  c.carbizid in "+
			" (select org_id from sys_org connect by prior org_id= parent_id start with org_id="+orgId+")"+
			"  order by case when c.carbizid="+orgId+" then 1 else 0 end desc,o.name desc,c.carnumber desc ";
		}
		String countSql = " select count(*) count"+
						" from car_car c "+
						" where  c.carbizid in "+
						" (select org_id from sys_org connect by prior org_id= parent_id start with org_id="+orgId+")";
		List<Map<String,Object>> carList = super.executeFindListMapObject(sql);
		List<Map<String,Object>> countList = super.executeFindListMapObject(countSql);
		String count ="0";
		List<Map<String,Object>> resultList =  new ArrayList<Map<String,Object>>();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		if(countList!=null && !countList.isEmpty()){
			Map<String,Object> countMap = countList.get(0);
			count = countMap.get("COUNT")+"";
		}
		resultMap.put("count", count);
		resultMap.put("carList", carList);
		resultList.add(resultMap);
		return resultList;
	}
	/**
	 * 
	 * @description: 根据时间（2013-07-12），组织id，车辆id字符串获取车辆费用相关信息
	 * @author：yuan.yw
	 * @param dateString
	 * @param carIds
	 * @param orgId
	 * @return
	 * @return List<Map<String,Object>>
	 * @date：Aug 2, 2013 11:08:25 AM
	 */
	public List<Map<String, Object>> getCarFuelInfoByDateAndCarId(
			String[] dateString, String carIds,String orgId) {
		if(dateString==null || dateString.length<2 || carIds==null || "".equals(carIds)|| orgId==null || "".equals(orgId)){
			return null;
		}
		String sql = "select c.id carid,"+
					"        round(cr.insreading,2) insreading,"+
					"       round(cb.fuelbills,2) fuelbills,"+
					"       round(cg.gpsmileage/1000,2) gpsmileage,"+
					"       case when cg.gpsmileage=0 then 0 else round(cb.fuelbills*1000/cg.gpsmileage,2) end gpsfuel,"+
					"       case when cr.insreading=0 then 0 else round(cb.fuelbills/cr.insreading,2) end insfuel,"+
					"       c.carNumber,"+
					"       c.carBizid,"+
					"       o.name orgname"+
					"  from car_car c"+
					"  left join (select max(cr.instrument_reading)-min(cr.instrument_reading) insreading, cr.car_id"+
					"               from car_instrument_reading cr"+
					"              where cr.status=1 and cr.car_id in ("+carIds+") and cr.recording_time >= to_date('"+dateString[0]+"', 'yyyy-mm-dd')"+
					"                and cr.recording_time <= to_date('"+dateString[1]+"', 'yyyy-mm-dd')"+
					"              group by cr.car_id) cr on cr.car_id = c.id"+
					"  left join (select sum(cb.fuel_bills) fuelbills, cb.car_id"+
					"               from car_fuel_bills cb"+
					"              where cb.status=1 and cb.car_id in ("+carIds+") and cb.recording_time >= to_date('"+dateString[0]+"', 'yyyy-mm-dd')"+
					"                and cb.recording_time <= to_date('"+dateString[1]+"', 'yyyy-mm-dd')"+
					"              group by cb.car_id) cb on cb.car_id = c.id"+
					"  left join (select sum(cg.gpsmileage) gpsmileage, cg.car_id"+
					"               from car_gpsmileage cg where cg.car_id in ("+carIds+") "+
					"                                          and cg.gpsdate >="+
					"                                              to_date('"+dateString[0]+" 00:00:00',"+
					"                                                      'yyyy-mm-dd hh24:mi:ss')"+
					"                                          and cg.gpsdate <="+
					"                                              to_date('"+dateString[1]+" 23:59:59',"+
					"                                                      'yyyy-mm-dd hh24:mi:ss')"+
					"              group by cg.car_id) cg on cg.car_id = c.id"+
					" left join sys_org o on o.org_id=c.carbizid "+
					" where c.id in ("+carIds+")"+
					" group by c.id, cr.insreading, cb.fuelbills, cg.gpsmileage,c.carnumber,c.carbizid,o.name "+
					" order by case when c.carBizid="+orgId+" then 1 else 0 end desc,o.name desc,c.carnumber desc ";
		return super.executeFindListMapObject(sql);
	}
	/**
	 * 
	 * @description: 根据时间（2013-07-12），组织id获取车辆费用相关信息
	 * @author：yuan.yw
	 * @param dateString
	 * @param orgId
	 * @return
	 * @return List<Map<String,Object>>
	 * @date：Aug 2, 2013 11:08:25 AM
	 */
	public List<Map<String, Object>> getCarFuelInfoByDateAndOrgId(
			String[] dateString, String orgId) {
		if(dateString==null || dateString.length<2 || orgId==null || "".equals(orgId)){
			return null;
		}
		String sql = "select c.id carid,"+
		            "       round(cr.insreading,2) insreading,"+
					"       round(cb.fuelbills,2) fuelbills,"+
					"       round(cg.gpsmileage/1000,2) gpsmileage,"+
					"       case when cg.gpsmileage=0 then 0 else round(cb.fuelbills*1000/cg.gpsmileage,2) end gpsfuel,"+
					"       case when cr.insreading=0 then 0 else round(cb.fuelbills/cr.insreading,2) end insfuel,"+
					"       c.carnumber,"+
					"       c.carbizid,"+
					"       o.name orgname"+
					"  from car_car c"+
					"  left join (select max(cr.instrument_reading)-min(cr.instrument_reading) insreading, cr.car_id"+
					"               from car_instrument_reading cr"+
					"              where cr.status=1 and cr.car_id in (select c.id from car_car c where c.carbizid in(select so.org_id from sys_org so connect by prior so.org_id=so.parent_id start with so.org_id="+orgId+"))"+
					"                and cr.recording_time >= to_date('"+dateString[0]+"', 'yyyy-mm-dd')"+
					"                and cr.recording_time <= to_date('"+dateString[1]+"', 'yyyy-mm-dd')"+
					"              group by cr.car_id) cr on cr.car_id = c.id"+
					"  left join (select sum(cb.fuel_bills) fuelbills, cb.car_id"+
					"               from car_fuel_bills cb"+
					"              where cb.status=1 and cb.car_id in (select c.id from car_car c where c.carbizid in(select so.org_id from sys_org so connect by prior so.org_id=so.parent_id start with so.org_id="+orgId+"))"+
					"                and cb.recording_time >= to_date('"+dateString[0]+"', 'yyyy-mm-dd')"+
					"                and cb.recording_time <= to_date('"+dateString[1]+"', 'yyyy-mm-dd')"+
					"              group by cb.car_id) cb on cb.car_id = c.id"+
					"  left join (select sum(cg.gpsmileage) gpsmileage, cg.car_id"+
					"               from car_gpsmileage cg"+
					"              where cg.car_id in (select c.id from car_car c where c.carbizid in(select so.org_id from sys_org so connect by prior so.org_id=so.parent_id start with so.org_id="+orgId+"))"+
					"                and cg.gpsdate >="+
					"                    to_date('"+dateString[0]+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')"+
					"                and cg.gpsdate <="+
					"                    to_date('"+dateString[1]+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss')"+
					"              group by cg.car_id) cg on cg.car_id = c.id"+
					"   left join sys_org o on o.org_id=c.carbizid"+
					" where c.id in (select c.id from car_car c where c.carbizid in(select so.org_id from sys_org so connect by prior so.org_id=so.parent_id start with so.org_id="+orgId+"))"+
					" group by c.id, cr.insreading, cb.fuelbills, cg.gpsmileage,c.carnumber,c.carbizid,o.name"+
					" order by case when c.carbizid="+orgId+" then 1 else 0 end desc,o.name desc,c.carnumber desc";
		return super.executeFindListMapObject(sql);
	}
}
