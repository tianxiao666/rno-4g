package com.iscreate.op.dao.cardispatch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.informationmanage.common.ArrayUtil;
import com.iscreate.op.action.informationmanage.common.DBUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.constant.CardispatchConstant;
import com.iscreate.op.dao.informationmanage.BaseDaoImpl;
import com.iscreate.op.pojo.cardispatch.CardispatchCar;
import com.iscreate.op.pojo.cardispatch.CardispatchCarterminalpair;
import com.iscreate.op.pojo.cardispatch.CardispatchDriver;
import com.iscreate.op.pojo.cardispatch.CardispatchDrivercarpair;
import com.iscreate.op.pojo.cardispatch.CardispatchOndutydrivercarpair;
import com.iscreate.op.pojo.cardispatch.CardispatchWorkorder;
import com.iscreate.op.pojo.cardispatch.CardispathTerminal;


@SuppressWarnings({"unchecked","unused"})
public class CardispatchCarDaoImpl  extends BaseDaoImpl<CardispatchCar> implements CardispatchCarDao {
	
	
	private static final String car_Table = " (SELECT c.* , id as carId FROM " + DBUtil.getTableName(CardispatchCar.class) + " c ) car "; 
	private static final String driver_Table = " (SELECT d.* , id as driverId FROM  " + DBUtil.getTableName(CardispatchDriver.class) + " d ) driver "; 
	private static final String terminal_Table = " (SELECT t.* , id as terminalId FROM  " + DBUtil.getTableName(CardispathTerminal.class) + " t ) terminal "; 
	private static final String car_terminal_Table = " (SELECT ct.* , id as ctpairId FROM  " + DBUtil.getTableName(CardispatchCarterminalpair.class) + " ct ) ctpair "; 
	private static final String car_driver_Table = " (SELECT cd.* , id as cdpairId FROM  " + DBUtil.getTableName(CardispatchDrivercarpair.class) + " cd ) cdpair "; 
	
	
	
	/**
	 * @author:du.hw
	 * @craete_time:2013-05-27
	 * 通过用户标识得到用户有权访问的车辆列表
	 * @param org_user_id
	 * @return
	 */
	public List<Map<String,Object>> getCarListByUserId(long org_user_id){
	   String sql = 
		   "select f.terminalname \"terminalName\","+
	       " f.clientversion \"clientversion\","+
	       " f.launchedtime \"launchedTime\","+
	       " f.terminalcomment \"terminalComment\","+
	       " f.statelasttime \"stateLastTime\","+
	       " f.mobiletype \"mobileType\","+
	       " f.terminalstate \"terminalState\","+
	       " f.id \"terminalId\","+
	       " f.monthlyrent \"monthlyRent\","+
	       " f.clientimei \"clientimei\","+
	       " f.telphoneno \"telphoneNo\","+
	       " f.terminalpic \"terminalPic\","+
	       " f.terminalbizid \"terminalbizid\","+
	       " f.expiredtime \"expiredTime\","+
	       " f.terminalstate \"carState\","+
	       " a.seatcount \"seatCount\","+
	       " a.id \"carId\","+
	       " a.id \"id\","+
	       " a.id \"objectIdentity\","+
	       " a.carnumber \"objectName\","+
	       " a.carnumber \"carNumber\","+
	       " a.status \"status\","+
	       " a.carpic \"carPic\","+
	       " a.carbrand \"carBrand\","+
	       " a.passengernumber \"passengerNumber\","+
	       " a.carbizid \"carBizId\","+
	       " a.cartype \"carType\","+
	       " a.carage \"carAge\","+
	       " a.carmarker \"carMarker\","+
	       " a.custodyfee \"custodyfee\","+
	       " a.carmodel \"carModel\","+
	       " a.loadWeight \"loadWeight\","+
	       " a.leasepay \"leasePay\","+
	       " nvl(g.jingdu,0) \"longitude\","+
	       " nvl(g.weidu,0) \"latitude\" from car_car a"+
	 " inner join (select * from sys_org connect by prior org_id=parent_id start with org_id "+
	         "     in (select org_id from sys_user_rela_org where org_user_id="+org_user_id+")) "+
	         "     b on b.org_id=a.carbizid"+
	 " left join car_cardriverpair c on  c.car_id=a.id "+
	 " left join car_driver d on d.id=c.driver_id"+
	 " left join car_carterminalpair e on e.car_id=a.id"+
	 " left join car_terminal f on f.id=e.terminal_id"+
	 " left join ("+
	  "   select distinct a.clientid,a.jingdu,a.weidu,a.picktime from mobile_gps_location a inner join ("+
	   "    select max(picktime) picktime,clientid from mobile_gps_location where picktime>(sysdate-10) and iscorrect=1  group by clientid"+
	   "    ) b on b.clientid=a.clientid and b.picktime=a.picktime"+
	   "    where a.picktime>(sysdate-10) and iscorrect=1"+
	"  ) g on g.clientid=f.id";
	   List<Map<String, Object>> returnList = super.executeFindListMapObject(sql);
	   return returnList;
	}
	
	/**
	 * 根据车辆id,获取车辆最后一次还车里程读数
	 * @param carId 车辆id
	 * @return 车辆信息
	 */
	public String findCarLastMileage ( String carId ) {
		String whereString = "";
		if ( StringUtil.isNullOrEmpty(carId) ) {
			return null;
		}
		whereString = " WHERE 1=1  AND workorder.realReturnCarMileage IS NOT NULL  AND carId = '" + carId + "'";
		String sql = 	"	SELECT " + 
						"		workorder.realReturnCarMileage \"realReturnCarMileage\" , " +
						"		workorder.realReturnCarTime \"realReturnCarTime\" " + 
						"	FROM  " + 
								car_Table + 
						"		INNER JOIN (SELECT cd.* , id AS drivercarpairId FROM " + DBUtil.getTableName(CardispatchDrivercarpair.class) + " cd ) drivercarpair ON drivercarpair.car_id = car.carId " + 
						"		INNER JOIN (SELECT w.* , id AS workorderId FROM   " + DBUtil.getTableName(CardispatchWorkorder.class) + " w ) workorder ON workorder.carDriverPairId =  drivercarpair.drivercarpairId " + 
							whereString + 
						"	ORDER BY " + 
						"		workorder.realReturnCarTime DESC ";
		List<Map<String, String>> list = super.executeFindList(sql);
		String mileage = "0";
		if ( !ArrayUtil.isNullOrEmpty(list) ) {
			mileage = list.get(0).get("realReturnCarMileage");
		}
		return mileage;
	}
	
	/**
	 * 根据车辆id,查询车辆状态
	 * @parma carId 车辆状态
	 * @return 车辆信息
	 */
	public Map<String,String> getCarStateByCarId ( String carId ) {		
		String sql = 	" 	SELECT " +
								CardispatchConstant.CAR_COLUMNTEXT  + " , " +   CardispatchConstant.TERMINAL_COLUMNTEXT + 
						"	FROM " + 
								car_Table + 
						" 		LEFT JOIN " + DBUtil.getTableName(CardispatchCarterminalpair.class) + " carterminalpair ON carterminalpair.car_id = car.id " + 
						" 		LEFT JOIN " + terminal_Table + " ON terminal.id = carterminalpair.terminal_id";
		List<Map<String, String>> list = super.executeFindList(sql );
		Map<String,String> result_map = new HashMap<String, String>();
		if ( list != null && list.size() > 0 ) {
			result_map = list.get(0);
		}
		return result_map;
	}
	
	/**
	 * 根据车辆司机配对Id，获取车辆信息
	 * @param cardriverpairId 车辆司机配对id
	 * @return (Map<String,String>) 车辆信息
	 */
	public Map<String,String> getCarByCarDriverPairId ( String cardriverpairId ) {
		Map<String,String> result_map = new HashMap<String, String>();
		String whereString = " WHERE 1=1 AND cardriverpairId = " + cardriverpairId ; 
		String selectString  =  CardispatchConstant.CAR_COLUMNTEXT + " , " +  CardispatchConstant.DRIVER_COLUMNTEXT + " , " + " cardriverpairId \"cardriverpairId\" ";
		String sql = 	" 	SELECT " +
								selectString +
						"	FROM " + 
								car_Table + 
						" 		INNER JOIN ( SELECT * , id AS cardriverpairId FROM " + DBUtil.getTableName(CardispatchDrivercarpair.class) + ") cardriverpair ON cardriverpair.car_id = car.id " + 
						" 		LEFT JOIN " + driver_Table + " ON driver.id = cardriverpair.driver_id " + whereString;
		List<Map<String, String>> list = super.executeFindList(sql);
		if ( list != null && list.size() > 0 ) {
			result_map = list.get(0);
		}
		return result_map;
	}
	
	/**
	 * 根据车辆id，获取车辆信息
	 * @param carId 车辆id
	 * @return (Map<String,String>) 车辆信息
	 */
	public Map<String,String> findCarAllInfoById ( String carId ) {
		String selectString = CardispatchConstant.CAR_COLUMNTEXT + " ,car.longitude \"longitude\",car.latitude \"latitude\", " + CardispatchConstant.DRIVER_COLUMNTEXT + " , " + CardispatchConstant.TERMINAL_COLUMNTEXT + " , cardriverpairId  \"cardriverpairId\" ";
		String sql = 	"	SELECT " + 
								selectString + 
						"	FROM  " + 
								car_Table +  
						"		LEFT JOIN (SELECT cd.* , id as cardriverpairId FROM  " + DBUtil.getTableName(CardispatchDrivercarpair.class) + " cd ) driverPair ON driverPair.car_id = car.id   " + 
						"		LEFT JOIN " + driver_Table + " ON driver.id = driverPair.driver_id  " +  
						"		LEFT JOIN " + DBUtil.getTableName(CardispatchCarterminalpair.class) + " terminalpair ON terminalpair.car_id = car.id  " + 
						"		LEFT JOIN " + terminal_Table + " ON terminalpair.terminal_id = terminal.id " + 
						"	WHERE 1=1 AND carId = " + carId;
		List<Map<String, String>> list = super.executeFindList(sql);
		Map<String,String> result_map = new LinkedHashMap<String, String>();
		if ( !ArrayUtil.isNullOrEmpty(list) ) {
			result_map = list.get(0);
		}
		return result_map;
	}
	
	
	/**
	 * 验证车牌是否已经存在
	 * @param carNumber 车牌
	 * @return (Map<String,String>) 如果存在则返回车辆信息
	 */
	public Map<String,String> checkCarNumber ( String carNumber ) {
		String whereString = " WHERE 1=1 "; 
		if ( !StringUtil.isNullOrEmpty(carNumber) ) {
			whereString += " AND carNumber = ? ";
		}
		String sql = "SELECT " + CardispatchConstant.CAR_COLUMNTEXT + " FROM " + DBUtil.getTableName(CardispatchCar.class) + " car " + whereString;
		List<Map<String, String>> list = super.executeFindList(sql,carNumber);
		Map<String, String> result_map = null; 
		if ( !ArrayUtil.isNullOrEmpty(list) ) {
			result_map = list.get(0);
		}
		return result_map;
	}
	
	/**
	 * 根据车辆id，获取车辆信息
	 * @param carId 车辆id
	 * @return (Map<String,Object>) 车辆信息
	 */
	public Map<String, Object> findCarInfoById ( String carId ) {
		String sql = "SELECT " + CardispatchConstant.CAR_COLUMNTEXT + " FROM  " + DBUtil.getTableName(CardispatchCar.class) + " car WHERE id = " + carId;
		List<Map<String, Object>> list = super.executeFindListMapObject(sql);
		Map<String, Object> result_Map = new LinkedHashMap<String, Object>();
		if ( list != null && list.size() > 0 ) {
			result_Map = list.get(0);
		}
		return result_Map;
	}
	
	/**
	 * 根据车辆组织id，获取车辆信息集合
	 * @param bizId组织id
	 * @return (List<Map<String,Object>>) 车辆信息集合
	 */
	public List<Map<String,Object>> findCarListByBizId ( String bizId ) {
		String sql = "SELECT " + CardispatchConstant.CAR_COLUMNTEXT + " FROM " + DBUtil.getTableName(CardispatchCar.class) + " car WHERE carBizId = " + bizId;
		List<Map<String, Object>> list = super.executeFindListMapObject(sql);
		return list;
	}
	
	
	public List<Map<String,Object>> findCarList ( Map param_map ) {
		String whereString = super.getWhereString(param_map, new DBWhereCallBack() {
			public String callBack(String columnName, StringBuffer opera,
					StringBuffer value) {
				if ( columnName.equals("carNumber") || columnName.equals("driverName") || columnName.equals("clientimei") ) {
					String v = value.toString();
					v = StringUtil.handleDbSensitiveString(v);
					opera.delete( 0 , opera.length() );
					opera.append("LIKE");
					return "%" + v + "%";
				}
				return null;
			}
		});
		String selectString = CardispatchConstant.CAR_COLUMNTEXT + " , carBizName \"carBizName\" , driverBizName \"driverBizName\" , " + CardispatchConstant.DRIVER_COLUMNTEXT + " , " + CardispatchConstant.TERMINAL_COLUMNTEXT + " , carId \"carId\" , driverPair.cardriverpairId \"cardriverpairId\"";
								
		String sql = 	"	SELECT " + 
								selectString+
						"	FROM  " + 
						"		( SELECT c.* , c.id as carId , o.name AS carBizName FROM " + DBUtil.getTableName(CardispatchCar.class) + " c LEFT JOIN sys_org o ON o.org_id = c.carBizId ) car  " +  
						"		LEFT JOIN (SELECT cd.* , id as cardriverpairId FROM  " + DBUtil.getTableName(CardispatchDrivercarpair.class) + " cd ) driverPair ON driverPair.car_id = car.id   " + 
						"		LEFT JOIN ( 	SELECT   " + 
						"					d.* , o.name AS driverBizName  " +  
						"				FROM   " + 
						"					 " + DBUtil.getTableName(CardispatchDriver.class) + "  d  " +  
						"					LEFT JOIN sys_org o ON o.org_id = d.driverBizId ) driver ON driver.id = driverPair.driver_id  " +  
						"		LEFT JOIN  " + DBUtil.getTableName(CardispatchCarterminalpair.class) + "  terminalpair ON terminalpair.car_id = car.id  " + 
						"		LEFT JOIN  " + DBUtil.getTableName(CardispathTerminal.class) + "  terminal ON terminalpair.terminal_id = terminal.id "
							+ whereString ;
		List<Map<String, Object>> list = super.executeFindListMapObject(sql);
		return list;
	}
	
	
	
	public List<Map<String,Object>> findCarDriverPairList ( Map param_map ) {
		String whereString = super.getWhereString(param_map, new DBWhereCallBack() {
			public String callBack(String columnName, StringBuffer opera,
					StringBuffer value) {
				if ( columnName.equals("carNumber") ) {
					String v = value.toString();
					v = StringUtil.handleDbSensitiveString(v);
					opera.delete( 0 , opera.length() );
					opera.append("LIKE");
					return "%" + v + "%";
				}
				return null;
			}
		});
		whereString += " AND carNumber IS NOT NULL AND driverName IS NOT NULL ";
		String selectString = CardispatchConstant.CAR_COLUMNTEXT + " , carBizName \"carBizName\" , driverBizName \"driverBizName\" , " + CardispatchConstant.DRIVER_COLUMNTEXT + " , " + CardispatchConstant.TERMINAL_COLUMNTEXT + " , carId \"carId\" , driverPair.cardriverpairId \"cardriverpairId\" ";
		String sql = 	"	SELECT " + 
								selectString + 
						"	FROM  " + 
						"		( SELECT c.* , c.id as carId , o.name AS carBizName FROM " + DBUtil.getTableName(CardispatchCar.class) + " c LEFT JOIN sys_org o ON o.org_id = c.carBizId ) car  " +  
						"		LEFT JOIN (SELECT p.* , id as cardriverpairId FROM  " + DBUtil.getTableName(CardispatchDrivercarpair.class) + " p ) driverPair ON driverPair.car_id = car.id   " + 
						"		LEFT JOIN ( 	SELECT   " + 
						"					d.* , o.name AS driverBizName  " +  
						"				FROM   " + 
						"					 " + DBUtil.getTableName(CardispatchDriver.class) + "  d  " +  
						"					LEFT JOIN sys_org o ON o.org_id = d.driverBizId ) driver ON driver.id = driverPair.driver_id  " +  
						"		LEFT JOIN  " + DBUtil.getTableName(CardispatchCarterminalpair.class) + "  terminalpair ON terminalpair.car_id = car.id  " + 
						"		LEFT JOIN  " + DBUtil.getTableName(CardispathTerminal.class) + "  terminal ON terminalpair.terminal_id = terminal.id "
							+ whereString ;
		List<Map<String, Object>> list = executeFindListMapObject(sql);
		Map<String,String> key_same_map = new HashMap<String, String>();
		if ( list != null && list.size() > 0 ) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				if ( key_same_map.containsKey(map.get("carNumber")+"")) {
					list.remove(i);
					i--;
				} else {
					key_same_map.put(map.get("carNumber")+"", null);
				}
			}
		}
		return list;
	}
	
	
	
	
	public List<Map<String,Object>> findCarDriverPairListIsDuty ( Map param_map , Map duty_param_Map ) {
		String whereString = super.getWhereString(param_map, new DBWhereCallBack() {
			public String callBack(String columnName, StringBuffer opera,
					StringBuffer value) {
				if ( columnName.equals("carNumber") ) {
					String v = value.toString();
					v = StringUtil.handleDbSensitiveString(v);
					opera.delete( 0 , opera.length() );
					opera.append("LIKE");
					return "%" + v + "%";
				}
				return null;
			}
		});
		
		whereString = whereString.replace("AND terminalState in ('3')", "AND terminalState in ('3')  or  terminalState is null");
		
		whereString += " AND carNumber IS NOT NULL ";
		String dutyWhereString = super.getWhereString(duty_param_Map, null); 
		String selectString = CardispatchConstant.CAR_COLUMNTEXT + "  , " + CardispatchConstant.DRIVER_COLUMNTEXT + " , " + CardispatchConstant.TERMINAL_COLUMNTEXT + " , carId \"carId\" , driverPair.cardriverpairId \"cardriverpairId\" ";
		String sql = 	"	SELECT " + 
								selectString + ",car.longitude \"jingdu\" ,car.latitude \"weidu\" "+//",gps.clientid \"clientId\",gps.pickTime \"pickTime\",gps.jingdu   \"jingdu\",gps.weidu    \"weidu\",gps.picktime \"pickTime\""+
						"	FROM  " + 
								car_Table +  
						"		LEFT JOIN (SELECT p.* , id as cardriverpairId FROM " + DBUtil.getTableName(CardispatchDrivercarpair.class) + " p ) driverPair ON driverPair.car_id = car.id   " + 
						"		LEFT JOIN ( 	SELECT   " + 
						"					d.*   " +  
						"				FROM   " + 
						"					 " + DBUtil.getTableName(CardispatchDriver.class) + "  d  )  driver ON driver.id = driverPair.driver_id  " +  
						"		LEFT JOIN  " + DBUtil.getTableName(CardispatchCarterminalpair.class) + "  terminalpair ON terminalpair.car_id = car.id  " + 
						"		LEFT JOIN " + terminal_Table + " ON terminalpair.terminal_id = terminal.id  "//+ 
						//"left join (select * from mobile_gps_location where id in ( select max(id) from mobile_gps_location g where g.isCorrect = 1 group by car_id)) gps on car.id = gps.car_id"
							+ whereString;
		
		List<Map<String, Object>> list = executeFindListMapObject(sql);
		Map<String,String> key_same_map = new HashMap<String, String>();
		if ( list != null && list.size() > 0 ) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				if ( key_same_map.containsKey(map.get("carNumber")+"")) {
					list.remove(i);
					i--;
				} else {
					key_same_map.put(map.get("carNumber")+"", null);
				}
			}
		}
		
		String freIdString = "";
		String[] freIds = (String[]) duty_param_Map.get("freId");
		if ( freIds != null && freIds.length > 0 ) {
			freIdString = " AND freId in (";
			for (int i = 0; i < freIds.length; i++) {
				String string = freIds[i];
				freIdString += string;
				if ( i != freIds.length - 1 ) {
					freIdString += " , ";
				}
			}
			freIdString += " )";
		} else {
			freIdString = "";
		}
		
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			sql = "SELECT 1 as aa FROM " + DBUtil.getTableName(CardispatchOndutydrivercarpair.class) + "  WHERE 1=1 " + freIdString + " AND dutyDate = to_date( '" + duty_param_Map.get("dutyDate") + "' ,'yyyy-mm-dd')  AND carId = " + map.get("carId");
			List<Map<String,String>> executeFindList = super.executeFindList(sql);
			if ( executeFindList != null && executeFindList.size() > 0 ) {
				map.put("isArranged", "是");
			} else {
				map.put("isArranged", "否");
			}
		}
		return list;
	}
	
	/**
	 * 根据车辆ID与开始结束时间查询车辆经纬度
	 * 
	 * @author LI.HB
	 * @param carId  车辆ID
	 * @param sTime  开始时间
	 * @param eTime  结束时间
	 * 
	 * @Create_Time 2013-08-07
	 * 
	 */
	public List<Map<String,String>>  findCurrentDayLatLngList (String carId,String sTime,String eTime)
	{
		String sql = "select car_id,jingdu,weidu,picktime"+
					"  from mobile_gps_location"+
					" where picktime between"+
					"       to_date('"+sTime+"', 'yyyy-mm-dd hh24:mi:ss') and"+
					"       to_date('"+eTime+"', 'yyyy-mm-dd hh24:mi:ss')"+
					"   and iscorrect = 1"+
					"   and car_id = "+carId+" order by picktime desc";
		
		List<Map<String,String>> list = super.executeFindList(sql);
		
		return list;
	}
	
	/**
	 * 根据组织ID查找车辆牌照
	 * @param orgId
	 * @return
	 */
	public List<String> findCarNumber(String orgId)
	{
		
		List<String> arrlist = new ArrayList<String>();
		
		String sql = "";
		
		if ( orgId != null || !orgId.isEmpty() ) {
			sql = "select carnumber from car_car where carbizid in (select org_id from sys_org where path like '%/"+orgId+"/%')";
		}
		
		List<Map<String, String>> list = super.executeFindList(sql);
		
		for(int i = 0;i<list.size();i++)
		{
			arrlist.add(list.get(i).get("CARNUMBER"));
		}
		
		return arrlist;
	}
	
	/**
	 * 根据组织ID查找车辆
	 * @param orgId
	 * @return
	 */
	public List<Map<String, Object>> findCarByOrgId(long orgId)
	{
		
		List<String> arrlist = new ArrayList<String>();
		
		String sql = "";
		
			sql = "select " + CardispatchConstant.CAR_COLUMNTEXT + " from car_car car where carbizid = " + orgId;
		
		List<Map<String, Object>> list = this.executeFindListMapObject(sql);
		return list;
	}
	
	
	/**
	 * 根据车牌号,查询终端信息
	 * @param carNumber - 车牌号
	 * @return (Map<String,String>) 终端信息
	 */
	public Map<String,String> findTerminalByCarNumber ( String carNumber ) {
		Map<String, String> map = new HashMap<String,String>();
		if ( carNumber == null || carNumber.isEmpty() ) {
			return map;
		}
		String sql = 	" SELECT " + 
							CardispatchConstant.TERMINAL_COLUMNTEXT + 
						" FROM  " + 
						" 	" + DBUtil.getTableName(CardispathTerminal.class) + " terminal " + 
						" 	INNER JOIN " + DBUtil.getTableName(CardispatchCarterminalpair.class) + " carterminalpair ON terminal.id = carterminalpair.terminal_id " + 
						" 	INNER JOIN  " + DBUtil.getTableName(CardispatchCar.class) + "  car ON car.id = carterminalpair.car_id " + 
						" WHERE  " + 
						" 	carNumber = '" + carNumber + "'" ;
		
		
		List<Map<String, String>> list = super.executeFindList(sql);
		if ( list != null && list.size() > 0 ) {
			map = list.get(0);
		}
		return map;
	}
	
	
	
	public boolean deleteCarByIds ( List<String> ids ) {
		String list2InString = DBUtil.list2InLong(ids, "id");
		String sql = "DELETE FROM  " + DBUtil.getTableName(CardispatchCar.class) + "  WHERE 1=1 " + list2InString ;
		Boolean flag = super.executeSql(sql);
		
		list2InString = DBUtil.list2InString(ids, "car_id");
		sql = "DELETE FROM  " + "car_carterminalpair" + "  WHERE 1=1 " + list2InString ;
		flag = super.executeSql(sql);
		
		return flag;
	}
	
	
	
	public boolean clearCarBindTerminal ( String carId , String terminalId ) {
		String whereString = "";
		String carString = "";
		String terminalString = "";
		if ( !StringUtil.isNullOrEmpty(carId) ) {
			carString += " car_id = " + carId;
		}
		if ( !StringUtil.isNullOrEmpty(terminalId) ) {
			terminalString += " terminal_id = " + terminalId;
		}
		if ( !StringUtil.isNullOrEmpty(carString) || !StringUtil.isNullOrEmpty(terminalString) ) {
			whereString += " WHERE ";
			if ( !StringUtil.isNullOrEmpty(carString) && !StringUtil.isNullOrEmpty(terminalString) ) {
				whereString += carString + " OR " + terminalString ;
			} else {
				whereString += carString + terminalString ;
			}
			String sql = "DELETE FROM  " + DBUtil.getTableName(CardispatchCarterminalpair.class) + "  " + whereString ;
			Boolean flag = super.executeSql(sql);
			return flag;
		}
		return false;
	}
	
	
	public boolean clearCarBindDriver ( String carId , String driverId ) {
		String whereString = "";
		String carString = "";
		String driverString = "";
		if ( !StringUtil.isNullOrEmpty(carId) ) {
			carString += " car_id = " + carId;
		}
		if ( !StringUtil.isNullOrEmpty(driverId) ) {
			driverString += " driver_id = " + driverId;
		}
		if ( !StringUtil.isNullOrEmpty(carString) || !StringUtil.isNullOrEmpty(driverString) ) {
			whereString += " WHERE ";
			if ( !StringUtil.isNullOrEmpty(carString) && !StringUtil.isNullOrEmpty(driverString) ) {
				whereString += carString + " OR " + driverString ;
			} else {
				whereString += carString + driverString ;
			}
			String sql = "DELETE FROM  " + DBUtil.getTableName(CardispatchDrivercarpair.class) + "  " + whereString ;
			Boolean flag = super.executeSql(sql);
			return flag;
		}
		return false;
	}
	
	
	public boolean carBindDriver ( Map param_map ) {
		param_map.put("id", new StringBuffer(CardispatchConstant.SEQ_CARDRIVERPAIR + ".nextval"));
		Long txinsert = super.txsave(param_map,CardispatchDrivercarpair.class);
		boolean flag = txinsert > 0 ;
		return flag;
	}
	
	public boolean carBindTerminal ( Map param_map ) {
		param_map.put("id", new StringBuffer( CardispatchConstant.SEQCARTERMINALPAIR+".nextval" ));
		Long txinsert = super.txsave(param_map,CardispatchCarterminalpair.class);
		boolean flag = txinsert > 0 ;
		return flag;
	}
	
	
	/**
	 * 判断 车辆id是否存在绑定关系
	 * 如果存在返回 true
	 * 不存在返回 false
	 * @author li.hb
	 * @create_time 2013.06.24
	 * @param carId
	 * @param terminalId
	 * @return
	 */
	public boolean isCarBindTerminal(String carId , String terminalId)
	{
		boolean flag = true;
		
		String whereString = "";
		String idString ="";
		String terminalIdString = "";
		String sql = "";
		
		List<Map<String, String>> list;
		
		if ( !StringUtil.isNullOrEmpty(carId) )
		{
			terminalIdString = "terminal_id=" + terminalId;
		}
		
		if ( !StringUtil.isNullOrEmpty(carId) ) 
		{
			idString += "car_id ="+carId;
			whereString += "where "+idString;
			sql = "select * from CAR_CARTERMINALPAIR "+whereString+"";
		}
		
		list = super.executeFindList(sql);
		
		if(list.isEmpty() || list == null || list.size() ==0)
		{
			flag = false;
		}
		
		return flag;
	}
	
	
	/**
	 * 
	 * 更新车辆id 与终端id 的绑定关系
	 * @author li.hb
	 * @create_time 2013.06.25
	 * @param carid 车辆ID
	 * @param terminalId 终端id 
	 * 
	 */
	public boolean updateCarBindTerminalId(String carId , String terminalId)
	{
		String whereString = "";
		String idString ="";
		String terminalIdString = "";
		String sql = "";
		
		boolean flag = false;
		
		if ( !StringUtil.isNullOrEmpty(carId) )
		{
			terminalIdString = "terminal_id=" + terminalId;
		}
		
		if ( !StringUtil.isNullOrEmpty(carId) ) 
		{
			idString += "car_id ="+carId;
			whereString += "where "+idString;
			sql = "update CAR_CARTERMINALPAIR set " + terminalIdString +" "+ whereString;
		}
		
		flag = super.executeSql(sql);
		
		return flag;
	}
	
	
	
	/**
	 * 查询组织下的所有车辆信息
	 * @param bizId - 组织集合
	 * @return (List<Map<String,String>>) 车辆信息
	 */
	public List<Map<String,String>> findCarInBiz ( List<Long> bizId ) {
		String bizIdString = DBUtil.list2InLong(bizId, "carBizId");
		String selectString = CardispatchConstant.CAR_COLUMNTEXT + " , " + CardispatchConstant.DRIVER_COLUMNTEXT + " , " + CardispatchConstant.TERMINAL_COLUMNTEXT + " , cdpairId \"cdpairId\" , ctpairId \"ctpairId\" ,s.name \"carBizName\"";
		String sql = 	" 	SELECT " + 
								selectString +  
						"	FROM " +
						"		" + car_Table + 
						"		LEFT JOIN " + car_driver_Table + " on car.carId = cdpair.car_id " + 
						"		LEFT JOIN " + driver_Table + " on driver.driverId = cdpair.driver_id " + 
						"		LEFT JOIN " + car_terminal_Table + " on car.carId = ctpair.car_id " + 
						"		LEFT JOIN " + terminal_Table + " on terminal.terminalId = ctpair.terminal_id " +
						" 		left join sys_org o on car.carbizid = o.org_id"+
						"  left join sys_org s on s.org_id = nvl(substr(o.path,instr(o.path, '/', 1, 3)+1,instr(o.path, '/', 1, 4)-instr(o.path, '/', 1, 3)-1),o.org_id)"+
						"	WHERE 1=1 " + bizIdString +"order by \"carBizName\"" ;
		List<Map<String, String>> list = super.executeFindList(sql);
		return list;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 根据车辆Id,查询车辆信息
	 * @param carNumber
	 * @return
	 */
	public Map<String,String> findByCarId ( String carId ) {
		Map<String,String> map = new HashMap<String, String>();
		String sql = " SELECT " + CardispatchConstant.CAR_COLUMNTEXT + " ,car.longitude \"longitude\",car.latitude \"latitude\" FROM " + DBUtil.getTableName(CardispatchCar.class) + " car WHERE id = '" + carId + "'";
		List<Map<String, String>> list = super.executeFindList(sql);
		if ( list != null && list.size() > 0 ) {
			map = list.get(0);
		}
		return map;
	}
	
	/**
	 * 根据车牌,查询车辆信息
	 * @param carNumber
	 * @return
	 */
	public Map<String,String> findByCarNumber ( String carNumber) {
		Map<String,String> map = new HashMap<String, String>();
		String sql = " SELECT " + CardispatchConstant.CAR_COLUMNTEXT + " FROM " + car_Table + " WHERE carNumber = '" + carNumber + "'";
		List<Map<String, String>> list = super.executeFindList(sql);
		if ( list != null && list.size() > 0 ) {
			map = list.get(0);
		}
		return map;
	}
	
	/**
	 * 
	 * 根据车牌与组织ID，查询车辆信息
	 * 
	 */
	public Map<String,String> findByCarNumberForOrgId ( String carNumber,String orgID) {
		Map<String,String> map = new HashMap<String, String>();
		String sql = " SELECT " + CardispatchConstant.CAR_COLUMNTEXT + " FROM " + "(SELECT c.*, id as carId FROM car_car c  where carbizid in (select org_id from SYS_ORG t where path like '%/"+orgID+"/%')) car" + " WHERE carNumber = '" + carNumber + "'";
		
		List<Map<String, String>> list = super.executeFindList(sql);
		if ( list != null && list.size() > 0 ) {
			map = list.get(0);
		}
		return map;
	}
	/**
	 * 根据车牌,查询车辆最新GPS信息
	 * @param carNumber
	 * @return
	 */
	public Map<String,String> findGPSLoactionByCarNumber ( String carNumber) {
		Map<String,String> map = new HashMap<String, String>();
		String sql =" select f.jingdu \"jingdu\", "
					+"       f.weidu \"weidu\", "
					+"       f.picktime \"picktime\", "
					+"       f.car_id \"carId\", "
					+"       f.carModel \"carModel\", "
					+"       f.carBizId \"carBizId\", "
					+"       f.carNumber \"carNumber\", "
					+"       f.carPic \"carPic\", "
					+"       f.status \"status\", "
					+"       f.carBrand \"carBrand\", "
					+"       f.carType \"carType\", "
					+"       f.loadWeight \"loadWeight\", "
					+"      f.passengerNumber \"passengerNumber\", "
					+"       f.leasePay \"leasePay\", "
					+"       f.custodyFee \"custodyFee\", "
					+"       f.carAge \"carAge\", "
					+"       f.carMarker \"carMarker\", "
					+"       f.seatCount, "
					+"       rownum \"seatCount\" "
					+"  from (select * "
					+"          from mobile_gps_location mgl, car_car car "
					+"         where car.id = mgl.car_id "
					+"           and car.carnumber = '" + carNumber + "' "
					+"           and mgl.iscorrect = 1 "
					+"         order by mgl.picktime desc) f "
					+" where rownum = 1 ";

		List<Map<String,String>> list = super.executeFindList(sql);
		if ( list != null && list.size() > 0 ) {
			map = list.get(0);
		}
		return map;
	}
	
	//TODO
	
	/**
	 * 根据车辆id,获取终端id
	 * @param carId - 车辆id
	 * @return 终端id
	 */
	public String findTerminalIdByCarId ( String carId ) {
		String terminalId = null;
		String sql = " SELECT terminal_id \"terminal_id\" FROM (SELECT p.* , p.id AS ctpairId FROM " + DBUtil.getTableName(CardispatchCarterminalpair.class) + " p ) pair WHERE car_id = " + carId;
		List<Map<String, String>> list = super.executeFindList(sql);
		if ( list != null && list.size() > 0 && list.get(0) != null ) {
			terminalId = list.get(
					0).get("terminal_id");
		}
		return terminalId;
	}
	
	/**
	 * 根据车辆id,获取司机id
	 * @param carId - 车辆id
	 * @return 司机id
	 */
	public String findDriverIdByCarId ( String carId ) {
		String driverId = null;
		String sql = " SELECT driver_id \"driver_id\" FROM ( SELECT cd.* , id AS dcpairId FROM " + DBUtil.getTableName(CardispatchDrivercarpair.class) + " cd ) dcp WHERE car_id = " + carId;
		List<Map<String, String>> list = super.executeFindList(sql);
		if ( list != null && list.size() > 0 && list.get(0) != null ) {
			driverId = list.get(
					0).get("driver_id");
		}
		return driverId;
	}
	
	
	
	
	/**
	 * 根据车辆牌照,查询配对终端id
	 * @param carNumber -车辆牌照
	 * @return (String) 终端id
	 */
	public String findTerminalIdByCarNumber ( String carNumber ) {
		String whereString = " WHERE 1=1 AND carNumber = '" + carNumber + "'";
		String sql = 	"	SELECT " + 
						"		terminal_id \"terminal_id\" " + 
						"	FROM  " + 
						"		" + DBUtil.getTableName(CardispatchCar.class) + " car " + 
						"		INNER JOIN " + DBUtil.getTableName(CardispatchCarterminalpair.class) + " ctpair ON ctpair.car_id = car.id " + whereString;
		List<Map<String, String>> list = super.executeFindList(sql);
		String terminalId = null;
		if ( list != null && list.size() > 0 ) {
			terminalId = list.get(0).get("terminal_id");
		}
		return terminalId;
	}
	
	
	
	/**
	 * 根据组织集合 , 查询车辆信息
	 * @param orgIds - 组织集合
	 * @return 车辆信息
	 */
	public List<Map<String, Object>> findCarListByOrgIdList ( Collection<String> orgIds ) {
		if ( orgIds == null || orgIds.size() == 0 ) {
			return null;
		}
		String whereString = " WHERE 1=1 ";
		String carBizId_string = DBUtil.list2InLong(orgIds, "carBizId");
		String sql = "SELECT " + CardispatchConstant.CAR_COLUMNTEXT + "  FROM " + DBUtil.getTableName(CardispatchCar.class) + " " + whereString + carBizId_string;
		List<Map<String, Object>> list = super.executeFindListForClassMapObject(sql, CardispatchCar.class);
		return list;
	}
	
	
	/**
	 * 根据组织id集合 , 获取车辆终端信息
	 * @parma carId 车辆状态
	 * @return 车辆信息
	 */
	public List<Map<String,Object>> findCarTerminalListByOrgIdList ( Collection<String> orgIds ) {	
		
		if ( orgIds == null || orgIds.size() == 0 ) {
			return null;
		}
		String whereString = " WHERE 1=1 ";
		String carBizId_string = DBUtil.list2InLong(orgIds, "carBizId");
		String sql = 	" 	SELECT " +
								CardispatchConstant.CAR_COLUMNTEXT + " , " + CardispatchConstant.TERMINAL_COLUMNTEXT + 
						"	FROM " + 
								car_Table + 
						" 		LEFT JOIN " + DBUtil.getTableName(CardispatchCarterminalpair.class) + " carterminalpair ON carterminalpair.car_id = car.id " + 
						" 		LEFT JOIN " + terminal_Table + " ON terminal.id = carterminalpair.terminal_id" + whereString + carBizId_string;
		List<Map<String, Object>> list = super.executeFindListMapObject(sql);
		return list;
	}
	
	
	/**
	 * 根据车辆id , 删除车辆司机关系
	 * @param carId - 车辆id
	 * @return
	 */
	public boolean deleteCarDirverPairByCarId ( String carId ) {
		String sql = "DELETE FROM car_cardriverpair where car_id = " + carId;
		Boolean flag = super.executeSql(sql);
		return flag;
	}
	
	
	/**
	 * 根据车辆id , 删除车辆司机关系
	 * @param carId - 车辆id
	 * @return
	 */
	public boolean deleteCarTerminalPairByCarId ( String carId ) {
		String sql = "DELETE FROM car_carterminalpair where car_id = " + carId;
		Boolean flag = super.executeSql(sql);
		return flag;
	}
	
	/**
	 * 
	 * @description:车辆状态监控查询相关信息
	 * @author：yuan.yw
	 * @param param_map
	 * @param duty_param_Map
	 * @param indexStart
	 * @param indexEnd
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jul 22, 2013 5:53:20 PM
	 */
	public List<Map<String,Object>> findCarDriverPairListForMonitor ( Map param_map , Map duty_param_Map,int indexStart,int indexEnd){
		
		String whereString = super.getWhereString(param_map, new DBWhereCallBack() {
			public String callBack(String columnName, StringBuffer opera,
					StringBuffer value) {
				if ( columnName.equals("carNumber") ) {
					String v = value.toString();
					v = StringUtil.handleDbSensitiveString(v);
					opera.delete( 0 , opera.length() );
					opera.append("LIKE");
					return "%" + v + "%";
				}
				return null;
			}
		});
		String stateCondition ="";
		String freIdString = "";
		String[] freIds = (String[]) duty_param_Map.get("freId");
		if ( freIds != null && freIds.length > 0 ) {
			freIdString = " AND fr.freId in (";
			for (int i = 0; i < freIds.length; i++) {
				String string = freIds[i];
				freIdString += string;
				if ( i != freIds.length - 1 ) {
					freIdString += " , ";
				}
			}
			freIdString += " )";
		} else {
			freIdString = "";
		}
		String freTableSql = "";
		String freTableCondition = "";
		if("".equals(freIdString)){
			freTableSql = " LEFT JOIN " + DBUtil.getTableName(CardispatchOndutydrivercarpair.class) + " fr on fr.carId=car.id "+freIdString	+" AND dutyDate = to_date( '" + duty_param_Map.get("dutyDate") + "' ,'yyyy-mm-dd')";
			
		}else {
			freTableSql = " " + DBUtil.getTableName(CardispatchOndutydrivercarpair.class) + " fr ";
			freTableCondition = " AND fr.carId=car.id "+freIdString	+" AND dutyDate = to_date( '" + duty_param_Map.get("dutyDate") + "' ,'yyyy-mm-dd')";
		}
		String selectString = CardispatchConstant.CAR_COLUMNTEXT + " ,car.longitude \"longitude\",car.latitude \"latitude\" , " + CardispatchConstant.DRIVER_COLUMNTEXT + " , " + CardispatchConstant.TERMINAL_COLUMNTEXT + " , driverPair.cardriverpairId \"cardriverpairId\", car.LONLAT_MODIFY_TIME \"mod_time\" ";
		String sql = "";
		String countSql =  "";
		if(indexEnd==0){
			whereString = whereString.replace("AND terminalState in ('3')", "AND terminalState in ('3')  or  terminalState is null");
			
			whereString += " AND carNumber IS NOT NULL ";
			
			sql = car_Table;
			sql = 	"	SELECT CASE WHEN fr.id is null THEN '否' ELSE '是' END  \"isArranged\",sog.name \"driverBizName\",sog1.name \"carBizName\"," + 
							selectString +" ,CASE WHEN wo.taskcount is null THEN 0 ELSE wo.taskcount END \"totalTask\" "+ //",gps.clientid \"clientId\",gps.pickTime \"pickTime\",gps.jingdu   \"jingdu\",gps.weidu    \"weidu\",gps.picktime \"pickTime\""+
					"	FROM  (" + 
							sql +  
					") car	," +
					freTableSql+
					"	LEFT JOIN (SELECT p.* , id as cardriverpairId FROM " + DBUtil.getTableName(CardispatchDrivercarpair.class) + " p ) driverPair ON driverPair.car_id = car.id   " + 
					"		LEFT JOIN ( 	SELECT   " + 
					"					d.*   " +  
					"				FROM   " + 
					"					 " + DBUtil.getTableName(CardispatchDriver.class) + "  d  )  driver ON driver.id = driverPair.driver_id  " +  
					"		LEFT JOIN  " + DBUtil.getTableName(CardispatchCarterminalpair.class) + "  terminalpair ON terminalpair.car_id = car.id  " + 
					"		LEFT JOIN " + terminal_Table + " ON terminalpair.terminal_id = terminal.id  "+ 
					//" LEFT JOIN (select * from mobile_gps_location where id in ( select max(id) from mobile_gps_location g where g.isCorrect = 1 group by car_id)) gps on car.id = gps.car_id"+
					" LEFT JOIN " + DBUtil.getTableName(CardispatchOndutydrivercarpair.class) + " fr on fr.carId=car.id "+freIdString	+" AND dutyDate = to_date( '" + duty_param_Map.get("dutyDate") + "' ,'yyyy-mm-dd')"+
					" LEFT JOIN sys_org sog on sog.org_id=driver.driverBizId"+
					" LEFT JOIN sys_org sog1 on sog1.org_id=car.carBizId"+
					" LEFT JOIN WM_COUNT_WORKORDER_OBJECT wo on wo.resourceid=car.id and wo.resourcetype='car'"
					+whereString+freTableCondition;
		}else{
			if(whereString.indexOf("AND terminalState in ('0')")>-1){
				whereString = whereString.replace("AND terminalState in ('0')", "");
				stateCondition = " AND ct.terminalState='0'";
			}else if(whereString.indexOf("AND terminalState in ('1')")>-1){
				whereString = whereString.replace("AND terminalState in ('1')", "");
				stateCondition = " AND ct.terminalState='1'";
			}else if(whereString.indexOf("AND terminalState in ('2')")>-1){
				whereString = whereString.replace("AND terminalState in ('2')", "");
				stateCondition = " AND ct.terminalState='2'";
			}else if(whereString.indexOf("AND terminalState in ('3')")>-1){
				whereString = whereString.replace("AND terminalState in ('3')", "");
				stateCondition = " AND (ct.terminalState='3' or  ct.terminalState is null)";
			}
			
			whereString += " AND carNumber IS NOT NULL ";
			sql = "SELECT car.* , car.id as carId,ROWNUM num FROM " + DBUtil.getTableName(CardispatchCar.class) + " car  "+whereString+" ";
			if(!"".equals(freTableCondition)){
				sql = "SELECT car.* , car.id as carId,ROWNUM num FROM " + DBUtil.getTableName(CardispatchCar.class) + " car,"+freTableSql+"  "+whereString+" "+freTableCondition;
			}
			if(!"".equals(stateCondition)){
				sql = "SELECT car.* , car.id as carId,ROWNUM num FROM " + DBUtil.getTableName(CardispatchCar.class) + " car   ,car_carterminalpair cp,car_terminal ct  "+whereString+" AND cp.car_id =car.id AND ct.id = cp.terminal_id  "+stateCondition;
				if(stateCondition.indexOf("terminalState='3'")>=0){
					sql+=" UNION ALL SELECT car.*, car.id as carId, ROWNUM num FROM car_car car  " +whereString +" AND NOT EXISTS (SELECT id   FROM car_carterminalpair   WHERE car_id = car.id) ";
				}
			}
			sql ="SELECT * from ("+sql+") r where r.num >= "+indexStart+"and r.num<="+indexEnd;
			sql = 	"	SELECT CASE WHEN fr.id is null THEN '否' ELSE '是' END  \"isArranged\",sog.name \"driverBizName\",sog1.name \"carBizName\", " + 
						selectString +" ,CASE WHEN wo.taskcount is null THEN 0 ELSE wo.taskcount END \"totalTask\" "+  //",gps.clientid \"clientId\",gps.pickTime \"pickTime\",gps.jingdu   \"jingdu\",gps.weidu    \"weidu\",gps.picktime \"pickTime\""+
				"	FROM  (" + 
						sql +  
				") car		LEFT JOIN (SELECT p.* , id as cardriverpairId FROM " + DBUtil.getTableName(CardispatchDrivercarpair.class) + " p ) driverPair ON driverPair.car_id = car.id   " + 
				"		LEFT JOIN ( 	SELECT   " + 
				"					d.*   " +  
				"				FROM   " + 
				"					 " + DBUtil.getTableName(CardispatchDriver.class) + "  d  )  driver ON driver.id = driverPair.driver_id  " +  
				"		LEFT JOIN  " + DBUtil.getTableName(CardispatchCarterminalpair.class) + "  terminalpair ON terminalpair.car_id = car.id  " + 
				"		LEFT JOIN " + terminal_Table + " ON terminalpair.terminal_id = terminal.id  "+ 
				//" LEFT JOIN (select * from mobile_gps_location where id in ( select max(id) from mobile_gps_location g where g.isCorrect = 1 group by car_id)) gps on car.id = gps.car_id"+
				" LEFT JOIN " + DBUtil.getTableName(CardispatchOndutydrivercarpair.class) + " fr on fr.carId=car.id "+freIdString	+" AND dutyDate = to_date( '" + duty_param_Map.get("dutyDate") + "' ,'yyyy-mm-dd')"+
				" LEFT JOIN sys_org sog on sog.org_id=driver.driverBizId"+
				" LEFT JOIN sys_org sog1 on sog1.org_id=car.carBizId"+
				" LEFT JOIN WM_COUNT_WORKORDER_OBJECT wo on wo.resourceid=car.id and wo.resourcetype='car'";
		}
		countSql =  "SELECT count(*) \"count\" FROM " + DBUtil.getTableName(CardispatchCar.class) + " c  "+whereString;
		if(!"".equals(freTableCondition)){
			countSql = "SELECT count(*) \"count\" FROM " + DBUtil.getTableName(CardispatchCar.class) + " car,"+freTableSql+"  "+whereString+" "+freTableCondition;
		}
		if(!"".equals(stateCondition)){
			countSql =  "SELECT count(*) \"count\" FROM " + DBUtil.getTableName(CardispatchCar.class) + " c , car_carterminalpair cp ,car_terminal ct  "+whereString+" AND cp.car_id =c.id AND ct.id = cp.terminal_id "+stateCondition;
			if(stateCondition.indexOf("terminalState='3'")>=0){
				countSql+=" UNION ALL SELECT count(*) \"count\" FROM car_car c   "+whereString+" AND NOT EXISTS (SELECT id   FROM car_carterminalpair   WHERE car_id = c.id) ";
				countSql = "select sum(\"count\") \"count\" from ("+countSql+")";
			}
		}
		List<Map<String, Object>> rlist = executeFindListMapObject(sql);
		Map<String,String> key_same_map = new HashMap<String, String>();
		if ( rlist != null && rlist.size() > 0 ) {
			for (int i = 0; i < rlist.size(); i++) {
				Map<String, Object> map = rlist.get(i);
				if ( key_same_map.containsKey(map.get("carNumber")+"")) {
					rlist.remove(i);
					i--;
				} else {
					key_same_map.put(map.get("carNumber")+"", null);
				}
			}
		}
		List<Map<String,Object>> countList = executeFindListMapObject(countSql);
		int count=0;
		if(countList!=null && !countList.isEmpty()){
			Map<String,Object> mp = countList.get(0);
			count = Integer.parseInt(mp.get("count")+"");
			
		}
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("count", count);
		resultMap.put("entityList", rlist);
		List<Map<String,Object>>  list = new ArrayList<Map<String,Object>>();
		list.add(resultMap);
		return list;
	}

	/**
	 * 
	 * @description: 根据条件获取车辆状态监控列表（分页）
	 * @author：yuan.yw
	 * @param conditionMap
	 * @param GPSMap
	 * @param indexStart
	 * @param indexEnd
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jul 23, 2013 5:09:03 PM
	 */
	public  List<Map<String,Object>> getCarStateMonitorListByCondition(Map<String,Object> conditionMap,Map<String, Object> GPSMap,String indexStart,String indexEnd){
		int end =0;
		if(indexEnd!=null && !"".equals(indexEnd)){
			end = Integer.valueOf(indexStart);
		}
		int start =0;
		if(indexStart!=null && !"".equals(indexStart)){
			start = Integer.valueOf(indexStart);
		} 
		String orgIdSql = "";
		String carTypeSql = "";
		String carStateSql = "";
		String carNumberSql ="";
		if(conditionMap!=null && !conditionMap.isEmpty()){
			if(conditionMap.get("orgId")!=null){
				orgIdSql = " and c.carbizid in (select o.org_id from sys_org o connect by prior o.org_id=o.parent_id start with o.org_id="+conditionMap.get("orgId")+") ";
			}
			if(conditionMap.get("carType")!=null){
				carTypeSql = " and c.carType ='"+conditionMap.get("carType")+"' ";
			}
			if(conditionMap.get("carNumber")!=null){
				carNumberSql = " and c.carNumber  like '%"+conditionMap.get("carNumber")+"%' ";
			}
			if(conditionMap.get("carState")!=null){
				carStateSql = " and ct.terminalState ='"+conditionMap.get("carState")+"' ";
			}
		}
		String GpsCondition="";
		String tableAlias="c.";
		if(GPSMap!=null && !GPSMap.isEmpty()){//最新gps经纬度
			GpsCondition = GpsCondition + " and (("+tableAlias+"longitude between " + GPSMap.get("outerleft")
			+ " and " + GPSMap.get("outerright") + " ) "
			+ " and ("+tableAlias+"latitude between " + GPSMap.get("outerbottom")
			+ " and " + GPSMap.get("outertop") + " )) "
			+ " and ((("+tableAlias+"latitude>" + GPSMap.get("innertop")
			+ ") or ("+tableAlias+"latitude<" + GPSMap.get("innerbottom")
			+ ")) or (("+tableAlias+"longitude>" + GPSMap.get("innerright")
			+ ") or ("+tableAlias+"longitude<" + GPSMap.get("innerleft") + "))) ";
			
		}
		String sql="";
		String countSql="";
		if(!"".equals(carStateSql)){//待初始化状态特殊
			sql = "select c.* ,ct.terminalstate terminalstate"
				 +"	  from car_car c "
				 +"	  ,car_carterminalpair cp "
				 +"	  , car_terminal ct  "
				 +"	 where 1=1 and cp.car_id = c.id and  ct.id =cp.terminal_id "+carStateSql+orgIdSql+carTypeSql+carNumberSql+GpsCondition;
			if(carStateSql.indexOf("'3'")>=0){
				sql +=" union select c.*,null terminalstate"
					 +" from car_car c "
					 +" where 1=1 "+orgIdSql+carTypeSql+carNumberSql+GpsCondition+" and not exists "
					 +"  (select id from car_carterminalpair cp where cp.car_id = c.id) ";
			}
			countSql = "select count(*) \"count\" "  
			      +"         from ("+sql+") c ";
			sql = "select c.id \"carId\",c.carnumber \"carNumber\",c.cartype \"carType\",c.longitude \"longitude\",c.latitude \"latitude\","
                +"         GetDistance(c.latitude, c.longitude,"+conditionMap.get("latitude")+","+conditionMap.get("longitude")+")*1000 \"GPSsum\",c.terminalstate \"terminalState\"  "  
                +"         from ("+sql+") c "
                +" where  1=1 "
                +"          order by \"GPSsum\" ";
			if(start<=0){
				sql = "select * from ("+sql+") r ";
			}else{
				if(end>0&&end>=start){
					sql = "select * from ( select re.*,ROWNUM num from ("+sql+") re where ROWNUM<="+indexEnd+" ) r where r.num>="+indexStart;
				}else{
					sql = "select * from ( select re.*,ROWNUM num from ("+sql+") re ) r where r.num>="+indexStart;
				}
			}
			
			
		}else{
			sql = "select c.id \"carId\",c.carnumber \"carNumber\",c.cartype \"carType\",c.longitude \"longitude\",c.latitude \"latitude\","
                +"         GetDistance(c.latitude, c.longitude,"+conditionMap.get("latitude")+","+conditionMap.get("longitude")+")*1000 \"GPSsum\""  
                +"         from car_car c "
                +" where 1=1  "+orgIdSql+carTypeSql+carNumberSql+GpsCondition
				+"          order by \"GPSsum\" ";
			if(start<=0){
				sql = "select * from ("+sql+") r ";
			}else{
				if(end>0&&end>=start){
					sql = "select * from ( select re.*,ROWNUM num from ("+sql+") re where ROWNUM<="+indexEnd+" ) r where r.num>="+indexStart;
				}else{
					sql = "select * from ( select re.*,ROWNUM num from ("+sql+") re ) r where r.num>="+indexStart;
				}
			}
			sql = "select c.*,ct.terminalState \"terminalState\" "  
                +"         from ("+sql+") c"
                +"         left join car_carterminalpair cp on cp.car_id = c.\"carId\" "
                +"         left join car_terminal ct on ct.id =cp.terminal_id ";
			countSql = "select count(*) \"count\" "  
		      +"         from car_car c "
			  +" where 1=1  "+orgIdSql+carTypeSql+carNumberSql+GpsCondition;
		}
		
		List<Map<String,Object>> carList = this.executeFindListMapObject(sql);
		List<Map<String,Object>> countList = this.executeFindListMapObject(countSql);
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		if(countList!=null && countList.size()>0){
			Map<String,String> key_same_map = new HashMap<String, String>();
			if ( carList != null && carList.size() > 0 ) {//去除重复
				for (int i = 0; i < carList.size(); i++) {
					Map<String, Object> m = carList.get(i);
					if ( key_same_map.containsKey(m.get("carNumber")+"")) {
						carList.remove(i);
						i--;
					} else {
						key_same_map.put(m.get("carNumber")+"", null);
					}
				}
			}
			String count = countList.get(0).get("count")+"";
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("count", count);
			map.put("carList", carList);
			resultList.add(map);
		}else{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("count", "0");
			map.put("carList", null);
			resultList.add(map);
		}
		return resultList;
	}
	/**
	 * 
	 * @description: 根据车辆id获取相关信息
	 * @author：yuan.yw
	 * @param carId
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Jul 24, 2013 10:19:48 AM
	 */
	public  Map<String,Object> getCarRelatedInformationByCarId(String carId){
		if(carId==null || "".equals(carId)){
			return null;
		}
		String sql = "select o.name terminalOrgName,so.name carOrgName,c.id carid,c.carnumber ,c.cartype ,c.carage ,c.carmodel,c.leasepay,c.custodyfee,c.passengernumber,c.loadweight,"
					+"	sog.name driverOrgName,cd.drivername,cd.identificationid,cd.driverphone,cd.driverlicensenumber,cd.accountid,cd.driverage,cd.driveraddress,"
					+"	ct.*,"
					+" c.longitude longitude,c.latitude latitude "
					+"	 from car_car c"
					+"	left join sys_org so on so.org_id = c.carbizid"
					+"	left join car_carterminalpair ctp on ctp.car_id = c.id"
					+"	left join car_cardriverpair cdp on cdp.car_id = c.id"
					+"	left join car_driver cd on cd.id = cdp.driver_id"
					+"	left join sys_org sog on sog.org_id = cd.driverbizid"
					+"	left join car_terminal ct on ct.id = ctp.terminal_id"
					+"	left join sys_org o on o.org_id = ct.terminalbizid"
					+"	where c.id = "+carId+" ";
		List<Map<String,Object>> resultList =this.executeFindListMapObject(sql);
		Map<String,Object>  map =null;
		if(resultList!=null && !resultList.isEmpty()){
			map = resultList.get(0);
		}
		return map;
	}
	
	/***
	 * 
	 * @description: 根据车辆id 时间范围 获取当前车辆最新Gps（若无记录 查询全部时间范围）
	 * @author：yuan.yw
	 * @param carId
	 * @param dateString
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Jul 26, 2013 4:23:14 PM
	 */
	public Map<String,Object> getCarCurrentGpsInfoByCarId(String carId,String dateString ){
		if(carId==null || "".equals(carId)){
			return null;
		}
		String[] dateArray = null;
		String sql = "";
		if(dateString!=null && !"".equals(dateString)){
			dateArray = dateString.split(",");
			sql = " select * from mobile_gps_location where id in ( select max(id) from mobile_gps_location g where g.isCorrect = 1 and g.car_id= "+carId+" and g.picktime <= to_date('"+dateArray[0]+"','yyyy-mm-dd hh24:mi:ss')"+" and g.picktime >= to_date('"+dateArray[1]+"','yyyy-mm-dd hh24:mi:ss')"+") ";
		}else{
			sql = " select * from mobile_gps_location where id in ( select max(id) from mobile_gps_location g where g.isCorrect = 1 and g.car_id= "+carId+") ";
		}
		List<Map<String,Object>> resultList =this.executeFindListMapObject(sql);
		if(resultList==null || resultList.isEmpty()){
			sql = " select * from mobile_gps_location where id in ( select max(id) from mobile_gps_location g where g.isCorrect = 1 and g.car_id= "+carId+") ";
			resultList =this.executeFindListMapObject(sql);
		}
		Map<String,Object>  map =null;
		if(resultList!=null && !resultList.isEmpty()){
			map = resultList.get(0);
		}
		return map;
	}

}


