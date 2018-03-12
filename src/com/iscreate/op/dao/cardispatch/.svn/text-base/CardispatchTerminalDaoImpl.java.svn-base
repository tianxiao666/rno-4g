package com.iscreate.op.dao.cardispatch;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.FlushMode;

import com.iscreate.op.action.informationmanage.common.ArrayUtil;
import com.iscreate.op.action.informationmanage.common.DBUtil;
import com.iscreate.op.action.informationmanage.common.DateUtil;
import com.iscreate.op.action.informationmanage.common.ObjectUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.constant.CardispatchConstant;
import com.iscreate.op.dao.informationmanage.BaseDaoImpl;
import com.iscreate.op.pojo.cardispatch.CardispatchCar;
import com.iscreate.op.pojo.cardispatch.CardispatchCarterminalpair;
import com.iscreate.op.pojo.cardispatch.CardispathTerminal;

@SuppressWarnings({"rawtypes","unchecked","unused","hiding"})
public class CardispatchTerminalDaoImpl  extends BaseDaoImpl<CardispathTerminal> implements CardispatchTerminalDao {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	public boolean checkTerminalImeiIsExists ( String clientimei ) {
		String whereString = " WHERE 1=1 ";
		if ( !StringUtil.isNullOrEmpty(clientimei) ) {
			whereString += " AND clientimei ='" + clientimei + "' ";
		}
		String sql = " SELECT * FROM " + DBUtil.getTableName(CardispathTerminal.class) + " " + whereString ;
		List<Map<String, String>> list = super.executeFindList(sql);
		boolean flag = false;
		if ( !ArrayUtil.isNullOrEmpty(list) ) {
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 根据车载终端id,获取最新gps位置
	 * @param clientId - 终端id
	 * @return (Map<String,String>) gps信息
	 */
	public Map<String,String> findMaxGpsByClientId ( String terminalId ) {
		Map<String, String> map = new HashMap<String,String>();
		if ( terminalId == null || terminalId.isEmpty() ) {
			return map;
		}
		String sql = "SELECT  to_char(MAX(pickTime),'yyyy-mm-dd hh24:mi:ss') \"maxTime\" FROM mobile_gps_location WHERE isCorrect = 1 AND car_id = (select car_id from car_carterminalpair where terminal_id  ="+terminalId+" ) ";
		
		List<Map<String, String>> list = super.executeFindList(sql);
		String pickTime = null;
		if ( list != null && list.size() > 0 && !StringUtil.isNullOrEmpty(list.get(0).get("maxTime")) ) {
			pickTime = list.get(0).get("maxTime");
		} else {
			return null;
		}
		sql = 	" SELECT " +
				"	 /*+ index(mobile_gps_location IDX_NEWINDEX2MOBILE_GPS_LOCATI) */  " + 
				"	id \"id\" , " +
				"	clientId \"clientId\" , " +
				"	pickTime \"pickTime\" , " + 
				"	jingdu \"jingdu\" , " + 
				"	weidu \"weidu\" " + 
				" FROM " + 
				"	mobile_gps_location g " +
				" WHERE " + 
				"	 isCorrect = 1 AND car_id = (select car_id from car_carterminalpair where terminal_id  ="+terminalId+" )" + 
				" AND pickTime = to_date('" + pickTime + "','yyyy-mm-dd hh24:mi:ss')";
		
		list = super.executeFindList(sql);
		if ( list != null && list.size() > 0 ) {
			map = list.get(0);
		}
		return map;
	}
	
	
	/**
	 * 
	 * 根据车辆ID，时间，查找该时间段内，车辆gps最小的经纬度
	 * 
	 * @author lihb
	 * @createTime ‎2013‎年‎6‎月‎24‎日
	 * @param car_id  车辆id
	 * @param time 时间范围
	 * @return 
	 */
	public Map<String,String> findMinGpsByCarId ( String car_id,String time ) {
		Map<String, String> map = new HashMap<String,String>();
		if ( car_id == null || car_id.isEmpty() ) {
			return map;
		}
		
		String sql = " SELECT id       \"id\"," +
				"        clientId \"clientId\"," +
				"        pickTime \"pickTime\"," +
				"        jingdu   \"jingdu\"," +
				"        weidu    \"weidu\"" +
				"   FROM mobile_gps_location g" +
				"  WHERE isCorrect = 1" +
				"    AND car_id = "+car_id+"" +
				"    and picktime between" +
				"        to_date('"+time.substring(0, 10)+" 00:00:00', 'yyyy-mm-dd,hh24:mi:ss') and" +
				"        to_date('"+time.substring(0, 10)+" 23:59:59', 'yyyy-mm-dd,hh24:mi:ss')" +
				"    and rownum = 1" +
				"  order by picktime";
				
		List<Map<String, String>> list = super.executeFindList(sql);
		if ( list != null && list.size() > 0 ) {
			map = list.get(0);
		}
		return map;
	}
	
	public List<Map<String,String>> findCarHeartbeat(String carNumber, String startTime,String endTime)
	{
		String sql ="select h.IMEI,h.CREATE_TIME"+
					"  from mobile_gps_heartbeat h,"+
					"       (select t.clientimei imei"+
					"          from car_terminal t, car_carterminalpair p, car_car c"+
					"         where c.id = p.car_id"+
					"           and c.carnumber = '"+carNumber+"'"+
					"           and p.terminal_id = t.id) i"+
					" where i.imei = h.imei"+
					"   and create_time between to_date('"+startTime+"', 'yyyy-mm-dd hh24:mi:ss') and"+
					"       to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss')"+
					" order by create_time desc";
		
		
		List<Map<String, String>> list = super.executeFindList(sql);
		
		return list;
	}
	
	
	
	public List<Map<String,Object>> findTerminalList ( Map param_map ) {
		List<Map<String, Object>> list = findTerminalList(param_map, null);
		return list;
	}
	
	public List<Map<String,Object>> findTerminalList ( Map param_map , Boolean isFree ) {
		String whereString = super.getWhereString(param_map, new DBWhereCallBack() {
			public String callBack(String columnName, StringBuffer opera,
					StringBuffer value) {
				if ( columnName.equals("carNumber") || columnName.equals("clientimei") || 
						columnName.equals("launchedTime") || columnName.equals("mobileType") || 
						columnName.equals("telphoneNo")) {
					String v = value.toString();
					v = StringUtil.handleDbSensitiveString(v);
					opera.delete( 0 , opera.length() );
					opera.append("LIKE");
					return "%" + v + "%";
				}
				return null;
			}
		});
		if ( isFree != null ) {
			if ( isFree ) {
				whereString += " AND carId IS NULL ";
			} else {
				whereString += " AND carId IS NOT NULL ";
			}
		}
		String selectString = CardispatchConstant.CAR_COLUMNTEXT + " , " + CardispatchConstant.TERMINAL_COLUMNTEXT + " , carBizName \"carBizName\" , terminalBizName \"terminalBizName\" , carterminalpair.carterminalpairId \"carterminalpairId\" ";
								
		String sql = 	"	SELECT " + 
								selectString + 
						"	FROM  " + 
						"		(SELECT t.*, t.id as terminalId , o.name AS terminalBizName FROM " + DBUtil.getTableName(CardispathTerminal.class) + " t LEFT JOIN sys_org o ON o.org_id = t.terminalBizId ) terminal " + 
						"		LEFT JOIN (SELECT p.*, id as carterminalpairId FROM  " + DBUtil.getTableName(CardispatchCarterminalpair.class) + " p ) carterminalpair  ON carterminalpair.terminal_id = terminal.id  " + 
						"		LEFT JOIN ( SELECT c.* , c.id as carId , o.name AS carBizName FROM  " + DBUtil.getTableName(CardispatchCar.class) + "  c LEFT JOIN sys_org o ON o.org_id = c.carBizId ) car ON carterminalpair.car_id = car.id  "  
						 + whereString ;
		List<Map<String, Object>> list = super.executeFindListMapObject(sql);
		return list;
	}
	
	/**
	 * 
	 * 查询待绑定终端列表
	 * 
	 * @param param_map
	 * @param isFree
	 * @return
	 */
	public List<Map<String,Object>> findTerminalBindingList ( Map param_map , Boolean isFree ) {
		String whereString = super.getWhereString(param_map, new DBWhereCallBack() {
			public String callBack(String columnName, StringBuffer opera,
					StringBuffer value) {
				if ( columnName.equals("carNumber") || columnName.equals("clientimei") || 
						columnName.equals("launchedTime") || columnName.equals("mobileType") || 
						columnName.equals("telphoneNo")) {
					String v = value.toString();
					v = StringUtil.handleDbSensitiveString(v);
					opera.delete( 0 , opera.length() );
					opera.append("LIKE");
					return "%" + v + "%";
				}
				return null;
			}
		});
		if ( isFree != null ) {
			if ( isFree ) {
				whereString += " AND carId IS NULL ";
			} else {
				whereString += " AND carId IS NOT NULL ";
			}
		}
		String selectString = CardispatchConstant.CAR_COLUMNTEXT + " , " + CardispatchConstant.TERMINAL_COLUMNTEXT + " , carBizName \"carBizName\" , terminalBizName \"terminalBizName\" , carterminalpair.carterminalpairId \"carterminalpairId\" ";
								
		String sql = 	"	SELECT " + 
								selectString + 
						"	FROM  " + 
						"		(SELECT t.*, t.id as terminalId , o.name AS terminalBizName FROM " + DBUtil.getTableName(CardispathTerminal.class) + " t LEFT JOIN sys_org o ON o.org_id = t.terminalBizId ) terminal " + 
						"		LEFT JOIN (SELECT p.*, id as carterminalpairId FROM  " + DBUtil.getTableName(CardispatchCarterminalpair.class) + " p ) carterminalpair  ON carterminalpair.terminal_id = terminal.id  " + 
						"		LEFT JOIN ( SELECT c.* , c.id as carId , o.name AS carBizName FROM  " + DBUtil.getTableName(CardispatchCar.class) + "  c LEFT JOIN sys_org o ON o.org_id = c.carBizId ) car ON carterminalpair.car_id = car.id  "  
						 + whereString + "and terminal.id not in  (select t.id from car_terminal t,car_carterminalpair cart where t.id = cart.terminal_id)";
		
		List<Map<String, Object>> list = super.executeFindListMapObject(sql);
		return list;
	}
	
	
	
	
	
	public boolean deleteTerminalByIds ( List<String> ids ) {
		String list2InString = DBUtil.list2InString(ids, "id");
		String sql = "DELETE FROM " + DBUtil.getTableName(CardispathTerminal.class) + " WHERE 1=1 " + list2InString ;
		Boolean flag = super.executeSql(sql);
		list2InString = DBUtil.list2InString(ids, "terminal_id");
		
		sql = "DELETE FROM car_carterminalpair  WHERE 1=1 " + list2InString ;
		flag = super.executeSql(sql);
		
		return flag;
	}
	
	public boolean saveTerminal ( Map param_map ) {
		CardispathTerminal terminal = null;
		try {
			terminal = ObjectUtil.map2Object(param_map, CardispathTerminal.class);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		//保存司机信息
		Long terminalId = super.txinsert(terminal);
		param_map.put("terminalId", terminalId);
		boolean flag = terminalId > 0 ;
		return flag;
	}
	
	
	public boolean updateTerminalById ( Map param_map , String terminalId ) {
		Map<String,String> map = new LinkedHashMap<String, String>();
		map.put("id", terminalId);
		int num = super.txupdate(map, param_map);
		boolean flag = num > 0 ; 
		return flag;
	}
	
	
	/**
	 * 根据参数获取gps信息
	 * @param param_map - gps参数集合
	 * @return (List<Map<String,String>>) gps信息集合
	 */
	public List<Map<String,String>> findGpsByParam ( Map param_map ) {
		String whereString = DBUtil.getWhereString(param_map, null);
		whereString += " AND isCorrect = 1 ";
		String sql = " SELECT gps.id \"gpsId\" , pickTime \"pickTime\" , clientId \"clientId\" , jingdu \"jingdu\" , weidu \"weidu\" FROM mobile_gps_location " + whereString;
		List<Map<String, String>> list = super.executeFindList(sql);
		return list;
	}
	
	
	
	
	/**
	 * 根据
	 * @param carNumberOrId
	 * @param pairId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String,String>> findTerminalGpsList ( String car_id , String startTime , String endTime ) {
		String whereString = " WHERE 1=1  AND isCorrect = 1 ";
		if ( !StringUtil.isNullOrEmpty(car_id) ) {
			whereString += " AND car_id = '" + car_id + "'";
		}
		
		if ( !StringUtil.isNullOrEmpty(startTime) && !StringUtil.isNullOrEmpty(endTime) ) {
			whereString += " AND pickTime BETWEEN to_date('" + startTime + "','yyyy-mm-dd hh24:mi:ss') AND to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') ";
		} else{
			if ( StringUtil.isNullOrEmpty(startTime) && StringUtil.isNullOrEmpty(endTime) ) {
				startTime = "2000-01-01 00:00:00";
				endTime = DateUtil.formatDate(new Date(), "yyyy-MM-dd") + " 23:59:59";
				whereString += " AND pickTime BETWEEN to_date('" + startTime + "','yyyy-mm-dd hh24:mi:ss') AND to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') ";
			} else if ( StringUtil.isNullOrEmpty(startTime) ) {
				startTime = "2000-01-01 00:00:00";
				whereString += " AND pickTime BETWEEN to_date('" + startTime + "','yyyy-mm-dd hh24:mi:ss') AND to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') ";
			} else if ( StringUtil.isNullOrEmpty(endTime) ) {
				endTime = DateUtil.formatDate(new Date(), "yyyy-MM-dd") + " 23:59:59";
				whereString += " AND pickTime BETWEEN to_date('" + startTime + "','yyyy-mm-dd hh24:mi:ss') AND to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') ";
			}
		}
		
		String sql = 	"	SELECT " +  
						"		 id \"gpsId\" , pickTime \"pickTime\" , clientId \"clientId\" , jingdu \"jingdu\" , weidu \"weidu\"  " + 
						"	FROM  " + 
						"		mobile_gps_location"  
							+ whereString + " ORDER BY pickTime ASC " ;
		List<Map<String, String>> list = executeFindList(sql);
		return list;
	}
	
	/**
	 * 根据终端id,获取小于pickTime参数的最新gps数据
	 * @param terminalId - 终端id
	 * @param pickTime - 时间区间
	 * @return (Map<String,String>) gps数据
	 */
	public Map<String,String> findGpsByClientIdInLowerTime ( String terminalId , String beginTime,String endTime,String asc ) {
		String sql = 	"	SELECT  " +
						"		/*+ index(mobile_gps_location IDX_NEWINDEX2MOBILE_GPS_LOCATI) */ " +
						"		id \"id\" , pickTime \"pickTime\" , clientId \"clientId\" , jingdu \"jingdu\" , weidu \"weidu\"  " +
						"	FROM " +
						"		mobile_gps_location " +
						"	WHERE " +
						"		car_id = (select car_id　from car_carterminalpair where terminal_id = " + terminalId + ") " +
						"		AND  pickTime  between  to_date('"+beginTime+"', 'yyyy-mm-dd HH24:mi:ss') and to_date('" + endTime + "','yyyy-mm-dd HH24:mi:ss')  AND isCorrect = 1 " +
						"	ORDER BY " +
						"		pickTime "+asc+"";
		List<Map<String, String>> list = super.executeFindList(sql);
		Map<String,String> result_map = new LinkedHashMap<String, String>();
		if ( list != null && list.size() > 0 ) {
			result_map = list.get(0);
		}
		return result_map;
	}
	
	
	/**
	 * 根据终端id,获取终端信息
	 * @param terminalId - 终端id
	 * @return 终端信息
	 */
	public Map<String,String> findTerminalByTerminalId ( String terminalId ) {
		String sql = "SELECT " + CardispatchConstant.TERMINAL_COLUMNTEXT + " FROM " + DBUtil.getTableName(CardispathTerminal.class) + " WHERE id = " + terminalId ;
		List<Map<String, String>> list = super.executeFindList(sql);
		if ( list == null || list.size() == 0 ) {
			return null;
		}
		return list.get(0);
	}
	
	
}


