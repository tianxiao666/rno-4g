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

import com.iscreate.op.action.informationmanage.common.DBUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;


@SuppressWarnings({"rawtypes","unchecked","unused","hiding"})
public class CarCensusReportDaoImpl implements CarCensusReportDao {
	
	/************ 依赖注入 ***********/
	private HibernateTemplate hibernateTemplate; 
	/***************** 属性 *******************/
	
	
	/****************** dao *******************/
	/**
	 * 根据组织id、车辆类型,查询车辆信息
	 * @param carBizId - 车辆组织id
	 * @param carType - 车类型
	 * @return (List<Map<String, String>>) 车辆信息集合
	 */
	public List<Map<String, Object>> censusCarByBiz ( List<String> carBizId , String[] carType ) {
		if ( carBizId == null || carBizId.isEmpty() ) {
			return new ArrayList<Map<String,Object>>();
		}
		
		String whereString = " WHERE 1=1 " ;
		String carTypeString  = DBUtil.list2InString(carType, "carType");
		String bizString = DBUtil.list2InString( carBizId , "carBizId" );
		String sql = " SELECT " +
				"r.carId \"carId\"," +
				"r.carBizId \"carBizId\"," +
				"r.carModel \"carModel\"," +
				"r.carNumber \"carNumber\"," +
				"r.carPic \"carPic\"," +
				"r.carStatus \"carStatus\"," +
				"r.carBrand \"carBrand\"," +
				"r.carType \"carType\"," +
				"r.loadWeight \"loadWeight\"," +
				"r.passengerNumber \"passengerNumber\"," +
				"r.leasePay \"leasePay\"," +
				"r.custodyFee \"custodyFee\"," +
				"r.carAge \"carAge\"," +
				"r.carMarker \"carMarker\"," +
				"r.seatCount \"seatCount\"," +
				"r.cdpairId \"cdpairId\"," +
				"r.driverId \"driverId\"," +
				"r.driverName \"driverName\"," +
				"r.drivingOfAge \"drivingOfAge\"," +
				"r.wage \"wage\"," +
				"r.driverAge \"driverAge\"," +
				"r.accountId \"accountId\"," +
				"r.carBizName \"carBizName\"," +
				"r.ctpairId \"ctpairId\"," +
				"r.terminalId \"terminalId\"," +
				"r.terminalState \"terminalState\"," +
				"r.clientimei \"clientimei\"," +
				"r.terminalName \"terminalName\"," +
				"r.telphoneNo \"telphoneNo\"," +
				"r.launchedTime \"launchedTime\"," +
				"r.expiredTime \"expiredTime\"," +
				"r.stateLastTime \"stateLastTime\"," +
				"r.terminalBizId \"terminalBizId\"," +
				"r.mobileType \"mobileType\"" +
				" FROM report_car r " + whereString + carTypeString + bizString ;
		List<Map<String, Object>> list = executeSqlForObject(sql);
		return list; 
	}
	
	/**
	 * 根据车辆类型,查询车辆信息
	 * @param carType - 车类型
	 * @return (List<Map<String, String>>) 车辆信息集合
	 */
	public List<Map<String, Object>> censusCarByCarType ( String[] carType ) {
		String whereString = " WHERE 1=1 " ;
		String carTypeString  = DBUtil.list2InString(carType, "carType");
		String sql = " SELECT " +
				"r.carId \"carId\"," +
				"r.carBizId \"carBizId\"," +
				"r.carModel \"carModel\"," +
				"r.carNumber \"carNumber\"," +
				"r.carPic \"carPic\"," +
				"r.carStatus \"carStatus\"," +
				"r.carBrand \"carBrand\"," +
				"r.carType \"carType\"," +
				"r.loadWeight \"loadWeight\"," +
				"r.passengerNumber \"passengerNumber\"," +
				"r.leasePay \"leasePay\"," +
				"r.custodyFee \"custodyFee\"," +
				"r.carAge \"carAge\"," +
				"r.carMarker \"carMarker\"," +
				"r.seatCount \"seatCount\"," +
				"r.cdpairId \"cdpairId\"," +
				"r.driverId \"driverId\"," +
				"r.driverName \"driverName\"," +
				"r.drivingOfAge \"drivingOfAge\"," +
				"r.wage \"wage\"," +
				"r.driverAge \"driverAge\"," +
				"r.accountId \"accountId\"," +
				"r.carBizName \"carBizName\"," +
				"r.ctpairId \"ctpairId\"," +
				"r.terminalId \"terminalId\"," +
				"r.terminalState \"terminalState\"," +
				"r.clientimei \"clientimei\"," +
				"r.terminalName \"terminalName\"," +
				"r.telphoneNo \"telphoneNo\"," +
				"r.launchedTime \"launchedTime\"," +
				"r.expiredTime \"expiredTime\"," +
				"r.stateLastTime \"stateLastTime\"," +
				"r.terminalBizId \"terminalBizId\"," +
				"r.mobileType \"mobileType\"" +
				" FROM report_car r" + whereString + carTypeString ;
		List<Map<String, Object>> list = executeSqlForObject(sql);
		return list; 
	}
	
	/**
	 * 根据车辆类型、日期范围、组织id集合,查询车辆里程信息
	 * @param carType - 车辆类型
	 * @param date - 日期范围
	 * @param bizIds - 组织id集合
	 * @return (List<Map<String, String>>) 车辆里程信息集合
	 */
	public List<Map<String,Object>> censusCarMileageInTimes ( String[] carType , String date , List<String> bizIds ) {
		String whereString = " WHERE 1=1 ";
		String carTypeString  = DBUtil.list2InString(carType, "carType");
		String dateString  = " AND \"DATE\" LIKE '%" + date + "%' ";
		String bizString  = DBUtil.list2InString(bizIds, "carBizId");
		whereString += carTypeString + dateString + bizString;
		String sql = 	" 	SELECT \"DATE\"  \"d\" ,  mileage \"mileage\", gpsmileage \"gpsmileage\" FROM report_carMileage "
							+ whereString ;
		List<Map<String,Object>> list = executeSqlForObject(sql);
		return list;
	}
	
	/**
	 * 根据车辆类型、日期范围、组织id集合,查询车辆里程信息
	 * @param carType - 车辆类型
	 * @param date - 日期范围
	 * @param bizIds - 组织id集合
	 * @return (List<Map<String, String>>) 车辆里程信息集合
	 */
	public List<Map<String,Object>> censusCarMileageInTimes ( String[] carType , String startDate , String endDate , List<String> bizIds ) {
		String whereString = " WHERE 1=1 ";
		String carTypeString  = DBUtil.list2InString(carType, "carType");
		String dateString  = " ";
		if ( !StringUtil.isNullOrEmpty(startDate) ) {
			dateString += " AND to_date('\"DATE\"','yyyy-mm-dd hh24:mi:ss') >= to_date('" + startDate + "','yyyy-mm-dd hh24:mi:ss')" ;
		}
		if ( !StringUtil.isNullOrEmpty(endDate) ) {
			dateString += " AND to_date(\"DATE\"','yyyy-mm-dd hh24:mi:ss') <= to_date('" + endDate + "','yyyy-mm-dd hh24:mi:ss')" ;
		}
		String bizString  = DBUtil.list2InString(bizIds, "carBizId");
		whereString += carTypeString + dateString + bizString;
		String sql = 	" 	SELECT \"DATE\", \"d\" ,  mileage \"mileage\", gpsmileage \"gpsmileage\" FROM report_carMileage "
							+ whereString ;
		List<Map<String,Object>> list = executeSqlForObject(sql);
		return list;
	}
	
	
	/**
	 * 根据车牌、日期 , 查询车辆里程信息(对比)
	 * @param bizIds - 组织id集合
	 * @param date - 日期
	 * @param carType - 车类型
	 * @return (List<Map<String, String>>) 车辆里程信息集合
	 */
	public List<Map<String,Object>> censusCarMileageByCarNumberInTimes ( String carNumber , String date ) {
		String whereString = " WHERE 1=1 ";
		String dateString  = " AND \"DATE\" LIKE '%" + date + "%' ";
		String carNumberString  = " AND carNumber = '" + carNumber + "'";
		whereString += carNumberString + dateString;
		String sql = 	" 	SELECT \"DATE\"  \"d\" ,  mileage \"mileage\", gpsmileage \"gpsmileage\" FROM report_carMileage "
							+ whereString ;
		List<Map<String,Object>> list = executeSqlForObject(sql);
		return list;
	}
	
	/**
	 * 根据组织、日期范围、车辆类型 , 查询车辆里程信息(环比)
	 * @param bizIds - 组织id集合
	 * @param date - 日期
	 * @param carType - 车类型
	 * @return (List<Map<String, String>>) 车辆里程信息集合
	 */
	public List<Map<String,Object>> censusCarMileageForRoundInTimes ( String[] carType , String date , List<String> bizIds ) {
		String whereString = " WHERE 1=1 ";
		String carTypeString  = DBUtil.list2InString(carType, "carType");
		String dateString  = " AND \"DATE\" LIKE '%" + date + "%' ";
		String bizString  = DBUtil.list2InString(bizIds, "carBizId");
		whereString += carTypeString + dateString + bizString;
		String sql = 	" 	SELECT \"DATE\"  \"d\" ,  SUM(mileage)  \"mileage\" , SUM(gpsmileage)  \"gpsmileage\" FROM report_carMileage "
							+ whereString + 
						"	GROUP BY \"DATE\" ";
		List<Map<String,Object>> list = executeSqlForObject(sql);
		return list;
	}
	
	/**
	 * 根据组织id集合、车辆类型、时间 , 获取组织油费信息 
	 * @param bizIds - 组织id集合
	 * @param time - 时间
	 * @return
	 */
	public List<Map<String, Object>> censusBizOilByBiz ( List<String> bizIds , String time ) {
		String whereString = " WHERE 1=1 ";
		String bizIdString = DBUtil.list2InString(bizIds, "carBizId");
		String timeString = "	AND to_char(yearMonth,'yyyy-mm-dd') LIKE '%" + time + "%'";
		whereString += bizIdString + timeString;
		String sql = 	"	SELECT " + 
						"		carBizId \"carBizId\", yearMonth \"yearMonth\", SUM(oilMoney) \"oilMoney\",  SUM(oilNum) \"oilNum\", SUM(carFee) \"carFee\", SUM(carryOutDay) \"carryOutDay\"  " + 
						"		, SUM(monthMileage) \"monthMileage\" , SUM(tollFee) \"tollFee\" , SUM(parkingFee) \"parkingFee\" , COUNT(1)  \"COUNT\"  " + 
						"	FROM  " + 
						"		report_bizoilbills " + whereString + 
						"	GROUP BY yearMonth , carBizId ";
		List<Map<String,Object>> list = executeSqlForObject(sql);
		return list;
	}
	
	
	/**
	 * 根据车辆牌照、时间, 获取车辆油费信息 
	 * @param carNumber - 车辆牌照
	 * @param time - 时间
	 * @return
	 */
	public List<Map<String, Object>> censusCarOilForRoundInTimes ( List<String> bizIds , String time ) {
		String whereString = " WHERE 1=1 ";
		String timeString = "	AND to_char(yearMonth,'yyyy-mm-dd') LIKE '%" + time + "%' ";
		String bizIdString = DBUtil.list2InString(bizIds, "carBizId");
		whereString += bizIdString + timeString;
		String sql = 	"	SELECT " + 
						"		yearMonth \"yearMonth\", SUM(oilMoney) \"oilMoney\" ,  SUM(oilNum) \"oilNum\" , SUM(carFee) \"carFee\" , SUM(carryOutDay) \"carryOutDay\"  " + 
						"		, SUM(monthMileage) \"monthMileage\" , SUM(tollFee) \"tollFee\" , SUM(parkingFee) \"parkingFee\" , COUNT(1)  \"COUNT\"  " + 
						"	FROM  " + 
						"		report_bizoilbills b " + whereString + 
						"	GROUP BY yearMonth ,carBizId	";
		List<Map<String,Object>> list = executeSqlForObject(sql);
		return list;
	}
	
	/**
	 * 根据车辆牌照、时间, 获取车辆油费信息 
	 * @param carNumber - 车辆牌照
	 * @param time - 时间
	 * @return
	 */
	public List<Map<String, Object>> censusCarOilByBiz ( String time , String carNumber ) {
		String whereString = " WHERE 1=1 ";
		String timeString = "	AND to_char(yearMonth,'yyyy-mm-dd') LIKE '%" + time + "%' ";
		String carNumberString = " 	AND carNumber = '" + carNumber + "' ";
		whereString += carNumberString + timeString;
		String sql = 	"	SELECT " +
						"b.id \"id\"," +
						"b.yearMonth \"yearMonth\"," +
						"b.carNumber \"carNumber\"," +
						"b.oilMoney \"oilMoney\"," +
						"b.oilNum \"oilNum\"," +
						"b.carFee \"carFee\"," +
						"b.carryOutDay \"carryOutDay\"," +
						"b.monthMileage \"monthMileage\"," +
						"b.tollFee \"tollFee\"," +
						"b.parkingFee \"parkingFee\"" + 
						"	FROM  " + 
						"		report_caroilbills b " + whereString;
		List<Map<String,Object>> list = executeSqlForObject(sql);
		return list;
	}
	
	
	/**
	 * 统计车辆在时间内的里程
	 * @param carNumber - 车辆牌照
	 * @param time - 时间段
	 * @return
	 */
	public List<Map<String,Object>> censusCarMileageInTime ( String carNumber , String time ) {
		String whereString = " WHERE 1=1 ";
		String timeString = "	AND \"DATE\" LIKE '%" + time + "%' ";
		String carNumberString = " 	AND carNumber = '" + carNumber + "' ";
		whereString += timeString + carNumberString ;
		String sql = 	"	SELECT " +
						"c.carId \"carId\"," +
						"c.carNumber \"carNumber\"," +
						"c.carType \"carType\"," +
						"c.carBizId \"carBizId\"," +
						"c.cdpairId \"cdpairId\"," +
						"c.driverId \"driverId\"," +
						"c.driverName \"driverName\"," +
						"c.ctpairId \"ctpairId\"," +
						"c.terminalId \"terminalId\"," +
						"c.terminalState \"terminalState\"," +
						"c.mobileType \"mobileType\"," +
						"c.mileage \"mileage\"," +
						"c.DATE \"DATE\"," +
						"c.gpsmileage \"gpsmileage\"" +
						"	FROM  " + 
						"		report_carmileage c " + whereString;
		List<Map<String, Object>> list = executeSqlForObject(sql);
		return list;
	}
	
	
	
	
	/************* 公共sql **************/
	public List<Map<String,String>> executeSql ( final String sqlString ) {
		List<Map<String, String>> list = hibernateTemplate.executeFind( new HibernateCallback<List<Map<String,String>>>() {
			public List<Map<String, String>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sqlString);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String, String>> find = query.list();
				return find;
			}
		});
		return list;
	}	
	
	public List<Map<String,Object>> executeSqlForObject ( final String sqlString ) {
		List<Map<String, Object>> list = hibernateTemplate.executeFind( new HibernateCallback<List<Map<String,Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sqlString);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String, Object>> find = query.list();
				return find;
			}
		});
		return list;
	}

	
	
	/***************** getter setter *******************/
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}


	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	
	
}
