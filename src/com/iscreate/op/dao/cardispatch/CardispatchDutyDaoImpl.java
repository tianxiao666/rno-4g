package com.iscreate.op.dao.cardispatch;

import java.util.List;
import java.util.Map;

import com.iscreate.op.action.informationmanage.common.DBUtil;
import com.iscreate.op.dao.informationmanage.BaseDaoImpl;
import com.iscreate.op.pojo.cardispatch.CardispatchCar;
import com.iscreate.op.pojo.cardispatch.CardispatchDriver;
import com.iscreate.op.pojo.cardispatch.CardispatchDrivercarpair;
import com.iscreate.op.pojo.cardispatch.CardispatchFrequency;
import com.iscreate.op.pojo.cardispatch.CardispatchOndutydrivercarpair;

@SuppressWarnings("unused")
public class CardispatchDutyDaoImpl extends BaseDaoImpl<CardispatchOndutydrivercarpair> implements CardispatchDutyDao {
	
	
	/**
	 * 根据业务单元、日期、账号,获取条件符合的排班信息
	 * (请求参数) 	dutyDate 日期List集合
	 *  			carId 车辆id
	 *  			freId 班次List集合
	 *  			carBizId 车辆所在组织List集合
	 * @return (List<Map<String,Object>>) 排班信息集合
	 */
	public List<Map<String,Object>> getCarDutyList ( Map param_map ) {
		String dutyDateWhere = DBUtil.list2InString(param_map.get("dutyDate"), "to_char(dutyDate,'yyyy-mm-dd')");
		String carBizIdWhere = DBUtil.list2InString(param_map.get("carBizId"), "carBizId");
		String freIdWhere = DBUtil.list2InString(param_map.get("freId"), "freId");
		String carNumberWhere = "";
		if ( param_map.containsKey("carNumber") && param_map.get("carNumber") != null && !(param_map.get("carNumber")+"").isEmpty() ) {
			carNumberWhere = " AND carNumber LIKE '%" + param_map.get("carNumber") + "%'";
		}
		String carIdWhere = "";
		if ( param_map.containsKey("carId") && param_map.get("carId") != null && !(param_map.get("carId")+"").isEmpty() ) {
			carIdWhere = " AND id = " + param_map.get("carId") + " ";
		}
		String selectString = 	" duty.id \"dutyId\" " +
								", fre.freId \"freId\" " +
								", org.carBizName \"carBizName\" " +
								", fre.StartTime \"startTime\" " +
								", pair.carId \"carId\" " + //cardriverpairId
								", fre.EndTime \"endTime\" " +
								",  to_char(DUTYDATE,'yyyy-mm-dd') \"dutyDate\" " +
								", fre.FREQUENCYNAME \"FREQUENCYNAME\" " +
								", freq_eng \"freq_eng\" " +
								", carNumber \"carNumber\"" +
								", driverName \"driverName\" " +
								"";			//查询结果列
		String sqlString = 		"	SELECT " + 
										selectString + 
								"	FROM  " + 
								"		(SELECT * FROM " + DBUtil.getTableName(CardispatchOndutydrivercarpair.class) + "  WHERE 1=1 " + dutyDateWhere + "  ) duty " + 
								"		INNER JOIN (SELECT  " + 
								"						c.* , d.driverName , p.id AS cardriverpairId " + 
								"					FROM " + 
								"						" + DBUtil.getTableName(CardispatchDrivercarpair.class) + " p " + 
								"						LEFT JOIN (SELECT c.* , id as carId FROM " + DBUtil.getTableName(CardispatchCar.class) + " c WHERE 1=1 " + carBizIdWhere + carNumberWhere  + carIdWhere + " ) c ON c.id = p.car_id " + 
								"						LEFT JOIN " + DBUtil.getTableName(CardispatchDriver.class) + " d ON d.id = p.driver_id ) pair ON pair.carId = duty.carId " + 
								"		INNER JOIN (SELECT o.org_id as id , o.name as carBizName FROM sys_org o inner join  PROJ_ENTERPRISE p on p.id=o.enterprise_id and p.cooperative='服务商') org on org.id = pair.carBizId " + 
								"		INNER JOIN (SELECT f.* , id as freId , CASE WHEN FREQUENCYNAME = '白班' THEN 'morning' ELSE 'night'   END AS freq_eng FROM  " + DBUtil.getTableName(CardispatchFrequency.class) + " f WHERE 1=1 " + freIdWhere + " ) fre ON fre.id = duty.freId" ;
		List<Map<String, Object>> list = super.executeFindListMapObject(sqlString);
		return list;
	}
	
	
	/**
	 * 查询班次
	 * @return (List<Map<String,Object>>) 班次集合
	 */
	public List<Map<String,Object>> findCarDutyFreq() {
		String sqlString = "SELECT * FROM " + DBUtil.getTableName(CardispatchFrequency.class) + "";
		List<Map<String, Object>> list = executeFindListMapObject(sqlString);
		return list;
	}
	
	
	public boolean findDutyExists ( String dutyDate  , String freId , String carId) {
		String sqlString = 	" 	SELECT " +
							"		* " +
							"	FROM " +
							"	" + DBUtil.getTableName(CardispatchOndutydrivercarpair.class) + 
							"	WHERE " +
							"		to_char(dutyDate,'yyyy-mm-dd') = '" + dutyDate + "' "+
							"		AND freId = " + freId + 
							"		AND carId = " + carId + 
							"";
		List<Map<String, String>> list = super.executeFindList(sqlString);
		boolean flag = false;
		if ( list != null && list.size() > 0 ) {
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}
	
	
	
}
