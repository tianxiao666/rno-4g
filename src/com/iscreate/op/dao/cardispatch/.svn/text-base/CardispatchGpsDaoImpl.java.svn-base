package com.iscreate.op.dao.cardispatch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.informationmanage.common.DBUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.dao.informationmanage.BaseDaoImpl;
import com.iscreate.op.pojo.cardispatch.CardispatchCar;


@SuppressWarnings({"unchecked","unused"})
public class CardispatchGpsDaoImpl  extends BaseDaoImpl<CardispatchCar> implements CardispatchGpsDao {
	
	
	private static final String car_Table = " (SELECT * , id as carId FROM cardispatch_car) car "; 
	private static final String driver_Table = " (SELECT * , id as driverId FROM cardispatch_driver) driver "; 
	private static final String terminal_Table = " (SELECT * , id as terminalId FROM cardispath_terminal) terminal "; 
	private static final String car_terminal_Table = " (SELECT * , id as ctpairId FROM cardispatch_carterminalpair) ctpair "; 
	private static final String car_driver_Table = " (SELECT * , id as cdpairId FROM cardispatch_drivercarpair ) cdpair "; 
	private static final String car_gps_Table = " (SELECT * , id as gpsId FROM cardispatch_gpsmileage ) gpsmileage ";
	
	
	/**
	 * 根据车牌查询时间内的里程读数
	 * @param carNumber - 车牌号(null)
	 * @param date - 日期(null)
	 * @param startTime - 起始时间(null)
	 * @param endTime - 结束时间(null)
	 * @return (List<Map<String,String>>) 车辆里程信息
	 */
	public List<Map<String,String>> findCarGpsInDateTime ( String carNumber, String startTime , String endTime ) {
		String whereSql = " WHERE 1=1 ";
		String carNumberString = "";
		if ( !StringUtil.isNullOrEmpty( carNumber ) ) {
			carNumberString = " AND car_id = (select id from car_car where carnumber ='"+carNumber+"')";
		}
		String dateString = "";
		if ( !StringUtil.isNullOrEmpty( startTime ) ) {
			dateString += " AND gpsDate >= to_date('" + startTime + "','yyyy-mm-dd hh24:mi:ss') ";
		}
		if ( !StringUtil.isNullOrEmpty( endTime ) ) {
			dateString += " AND gpsDate <= to_date('" + endTime + "','yyyy-mm-dd hh24:mi:ss') ";
		}
		if ( (carNumberString + dateString ).isEmpty() ) {
			return null;
		}
		whereSql += carNumberString + dateString;
		String sql = 	" 	SELECT " + 
						"		carNumber \"carNumber\" , terminalId \"terminalId\" , to_char(gpsDate,'yyyy-mm-dd') \"gpsDate\" , SUM(gpsmileage) \"gpsmileage\" " + 
						"	FROM " + 
						"		car_gpsmileage " + 
								whereSql + 
						"	GROUP BY " + 
						"		carNumber , terminalId , to_char(gpsDate,'yyyy-mm-dd') " + 
						"	ORDER BY " + 
						"		to_char(gpsDate,'yyyy-mm-dd') ";
		
		List<Map<String, String>> list = super.executeFindList(sql);
		
		return list;
	}
	
	
	public List<Map<String,String>> findAllCarGpsMileage(String bizId,List<String> date_list,String startHour, String endHour )
	{
		
		String gpsmileage = "";
		String strDate = "";
		String alias = "nvl(";
		Date date = null;
		boolean b = false;
		
		int sHour = Integer.parseInt(startHour.substring(11,13));
		int eHour = Integer.parseInt(endHour.substring(11,13));
		
		
		if(sHour > eHour)
		{
			b = true;
		}
		
		for (int i=1;i<=date_list.size();i++)
		{
			
			strDate = date_list.get(i-1);
			
			try 
			{
				 date = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
			} catch (ParseException e) 
			{
				e.printStackTrace();
			} 
			
			strDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
			
			if(b)
			{
				gpsmileage += "nvl(round(sum(case when gpsdate between to_date('"+strDate+" "+startHour.substring(11, 19)+"','yyyy/mm/dd hh24:mi:ss') and to_date('"+strDate+" 23:59:59','yyyy/mm/dd hh24:mi:ss') or  gpsdate between to_date('"+strDate+" 00:00:00','yyyy/mm/dd hh24:mi:ss') and to_date('"+strDate+" "+endHour.substring(11, 19)+"','yyyy/mm/dd hh24:mi:ss') then gpsmileage else null end )/1000),0) \""+strDate+"\",";
			}
			else
			{
				gpsmileage += "nvl(round(sum(case when gpsdate between to_date('"+strDate+" "+startHour.substring(11, 19)+"','yyyy/mm/dd hh24:mi:ss') and to_date('"+strDate+" "+endHour.substring(11, 19)+"','yyyy/mm/dd hh24:mi:ss')  then gpsmileage else null end )/1000),0) \""+strDate+"\",";
			}
			
			
			alias += "\""+strDate+"\""+"+";
		}
		
		gpsmileage = gpsmileage.substring(0, gpsmileage.length()-1);
		
		alias = alias.substring(0, alias.length()-1);
		
		alias += ",0) count";
		
		String sql ="select gps.*,"+
						"       car.id                     \"carId\","+
						"       car.id                     \"id\","+
						"       car.carModel               \"carModel\","+
						"       car.carBizId               \"carBizId\","+
						"       car.carNumber              \"carNumber\","+
						"       car.carPic                 \"carPic\","+
						"       car.status                 \"status\","+
						"       car.carBrand               \"carBrand\","+
						"       car.carType                \"carType\","+
						"       car.loadWeight             \"loadWeight\","+
						"       car.passengerNumber        \"passengerNumber\","+
						"       car.leasePay               \"leasePay\","+
						"       car.custodyFee             \"custodyFee\","+
						"       car.carAge                 \"carAge\","+
						"       car.carMarker              \"carMarker\","+
						"       car.seatCount              \"seatCount\","+
						"       driver.id                  \"driverId\","+
						"       driver.id                  \"id\","+
						"       driver.accountId           \"accountId\","+
						"       driver.driverName          \"driverName\","+
						"       driver.identificationId    \"identificationId\","+
						"       driver.driverPhone         \"driverPhone\","+
						"       driver.driverLicenseClass  \"driverLicenseClass\","+
						"       driver.driverLicenseNumber \"driverLicenseNumber\","+
						"       driver.driverStatus        \"driverStatus\","+
						"       driver.driverLicenseType   \"driverLicenseType\","+
						"       driver.drivingOfAge        \"drivingOfAge\","+
						"       driver.driverPic           \"driverPic\","+
						"       driver.driverAddress       \"driverAddress\","+
						"       driver.wage                \"wage\","+
						"       driver.driverAge           \"driverAge\","+
						"       driver.driverBizId         \"driverBizId\","+
						"       terminal.id                \"terminalId\","+
						"       terminal.id                \"id\","+
						"       terminal.terminalName      \"terminalName\","+
						"       terminal.clientversion     \"clientversion\","+
						"       terminal.terminalComment   \"terminalComment\","+
						"       terminal.terminalState     \"terminalState\","+
						"       terminal.clientimei        \"clientimei\","+
						"       terminal.telphoneNo        \"telphoneNo\","+
						"       terminal.launchedTime      \"launchedTime\","+
						"       terminal.expiredTime       \"expiredTime\","+
						"       terminal.terminalPic       \"terminalPic\","+
						"       terminal.mobileType        \"mobileType\","+
						"       terminal.stateLastTime     \"stateLastTime\","+
						"       terminal.terminalBizId     \"terminalBizId\","+
						"       terminal.monthlyRent       \"monthlyRent\","+
						"       cdpairId                   \"cdpairId\","+
						"       ctpairId                   \"ctpairId\","+
						"       s.name                     \"carBizName\"," +
						" 		nvl(round(re.instrumentcount),0)         \"instrumentcount\","+alias+
						"  from (SELECT c.*, id as carId FROM car_car c) car"+
						" left join (select "+
						"                    gps.car_id terminalId,"+gpsmileage+
						"               from car_gpsmileage gps"+
						"              group by gps.car_id) gps"+
						"    on gps.terminalId = car.id"+" inner join sys_org org on car.carBizId = org.org_id and org.path like '%/"+bizId+"/%'"+
						"  left join (select car_id, sum(instrument_reading) instrumentcount"+
						"               from car_instrument_reading"+
						"              where recording_time between"+
						"                    to_date('"+startHour.substring(0, 7)+"', 'yyyy/mm') and"+
						"                    to_date('"+endHour.substring(0, 7)+"', 'yyyy/mm')"+
						"                and status = 1"+
						"              group by car_id) re"+
						"    on re.car_id = car.id"+
						"  LEFT JOIN (SELECT cd.*, id as cdpairId FROM car_cardriverpair cd) cdpair"+
						"    on car.carId = cdpair.car_id"+
						"  LEFT JOIN (SELECT d.*, id as driverId FROM car_driver d) driver"+
						"    on driver.driverId = cdpair.driver_id"+
						"  LEFT JOIN (SELECT ct.*, id as ctpairId FROM car_carterminalpair ct) ctpair"+
						"    on car.carId = ctpair.car_id"+
						"  LEFT JOIN (SELECT t.*, id as terminalId FROM car_terminal t) terminal"+
						"    on terminal.terminalId = ctpair.terminal_id"+
						"  left join sys_org o"+
						"    on car.carbizid = o.org_id"+
						"  left join sys_org s"+
						"    on s.org_id ="+
						"       nvl(substr(o.path,"+
						"                  instr(o.path, '/', 1, 3) + 1,"+
						"                  instr(o.path, '/', 1, 4) - instr(o.path, '/', 1, 3) - 1),"+
						"           o.org_id)";
		
		List<Map<String, String>> list = super.executeFindList(sql);
		return list;
	}
	
	
	
}


