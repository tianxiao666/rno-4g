package com.iscreate.op.dao.cardispatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.informationmanage.common.ArrayUtil;
import com.iscreate.op.action.informationmanage.common.DBUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.constant.CardispatchConstant;
import com.iscreate.op.constant.WorkManageConstant;
import com.iscreate.op.dao.informationmanage.BaseDaoImpl;
import com.iscreate.op.pojo.cardispatch.CardispatchApplyworkorder;
import com.iscreate.op.pojo.cardispatch.CardispatchCar;
import com.iscreate.op.pojo.cardispatch.CardispatchDriver;
import com.iscreate.op.pojo.cardispatch.CardispatchDrivercarpair;
import com.iscreate.op.pojo.cardispatch.CardispatchFeerecord;
import com.iscreate.op.pojo.cardispatch.CardispatchWorkorder;

@SuppressWarnings({"rawtypes","unchecked","unused","hiding"})
public class CardispatchWorkorderDaoImplForMobile extends BaseDaoImpl<CardispatchWorkorder> implements CardispatchWorkorderDaoForMobile {

	
	/**
	 * 根据woId,获取相关行车费用信息集合
	 * @param woId - 工单woId
	 * @return (List<Map<String,String>>) 行车费用信息集合
	 */
	public List<Map<String,String>> findCardispatchWorkorderFeerecordByWoIdForMobile ( String woId ) {
		String sql = " SELECT fee.id \"id\" , fee.feeType \"feeType\" , fee.feeAmount \"feeAmount\" , fee.description \"description\"  FROM  " + DBUtil.getTableName(CardispatchFeerecord.class) + " fee WHERE woId = '" + woId + "'";
		List<Map<String, String>> list = super.executeFindList(sql);
		return list;
	}
	
	
	/**
	 * 查询车辆调度单的关联信息
	 * @param woId 车辆调度单woId
	 * @return (Map<String,String) 关联信息
	 */
	public Map<String,String> findCardispatchWorkorderAssociateTaskByWoIdForMobile ( String woId ) {
		String sql = 	" SELECT " +
						"	id \"id\" , carWoId \"carWoId\" , associateWorkType \"associateWorkType\" , associateToId \"associateToId\" , associateWoId \"associateWoId\" " +
						" FROM  " + DBUtil.getTableName(CardispatchApplyworkorder.class) + " WHERE carWoId = '" + woId + "'";
		List<Map<String, String>> list = super.executeFindList(sql);
		Map<String,String> result_map = new HashMap<String, String>();
		if ( !ArrayUtil.isNullOrEmpty(list) ) {
			result_map = list.get(0);
		}
		return result_map;
	}
	
	
	/**
	 * 根据工单woId,获取单张车辆调度单信息
	 * @param woId - 车辆调度单woId
	 * @return (Map<String,String>) 相应的车辆调度单信息
	 */
	public Map<String,String> findSingleCardispatchWorkorderByWoIdForMobile ( String woId ) {
		String selectString = 	" driver.driverName \"driverName\" " +
								" , driver.accountId \"accountId\"" +
								" , driver.driverPhone \"driverPhone\"" +
								" , car.carNumber \"carNumber\"" +
								", vwork.\"woId\" \"woId\"" +
								", vwork.\"woTitle\" \"woTitle\"" +
								", vwork.\"status\" \"status\"" +
								", vwork.\"creator\" \"creator\"" +
								", vwork.\"creatorName\" \"creatorName\"" +
								", vwork.\"creatorOrgId\" \"creatorOrgId\"" +
								", vwork.\"createTime\" \"createTime\"" +
								", vwork.\"isRead\" \"isRead\"" +
								", vwork.\"statusName\" \"statusName\"" +
								", workorder.useCarPersonId \"useCarPersonId\"" +
								", useCarType \"useCarType\"" +
								", applyDescription \"applyDescription\"" +   
								", workorder.planUseCarTime \"planUseCarTime\"" +
								", workorder.planUseCarAddress \"planUseCarAddress\"" +
								", workorder.sendCarPersonName \"sendCarPersonName\"" +
								", workorder.realUseCarTime \"realUseCarTime\"" +
								", workorder.realUseCarMileage \"realUseCarMileage\"" +
								", useCarAddressDescription \"useCarAddressDescription\"" +
								", carDriverPairId \"carDriverPairId\"" +
								", workorder.planReturnCarTime \"planReturnCarTime\"" +
								", workorder.realReturnCarTime \"realReturnCarTime\"" +
								", workorder.realReturnCarAddress \"realReturnCarAddress\"" +
								",workorder.criticalClass \"criticalClass\"" +  
								" , carId \"carId\"" +
								" , vwork.\"finalCompleteTime\" \"finalCompleteTime\"" +
								" , vwork.\"currentHandlerName\" \"currentHandlerName\"" +
								", vwork.\"currentHandler\" \"currentHandler\"" +
								", workorder.realReturnCarMileage \"realReturnCarMileage\"" +
								", vwork.\"createTime\" \"createTime\"";
		String sql = 	" SELECT " + 
							selectString + 
						" FROM " + 
						" 	V_WM_CAR_WORKORDER vwork " + 
						"	INNER JOIN " + DBUtil.getTableName(CardispatchWorkorder.class) + " workorder ON workorder.woId = vwork.\"woId\" " + 
						"	LEFT JOIN  " + DBUtil.getTableName(CardispatchDrivercarpair.class) + "  cardriverpair ON cardriverpair.id = workorder.carDriverPairId " + 
						"	LEFT JOIN (SELECT c.* , id AS carId FROM  " + DBUtil.getTableName(CardispatchCar.class) + " c ) car ON car.carId = cardriverpair.car_id " + 
						"	LEFT JOIN  " + DBUtil.getTableName(CardispatchDriver.class) + "  driver ON driver.id = cardriverpair.driver_id " +
						" WHERE " +
						"	vwork.\"woId\" = '" + woId + "'";
		List<Map<String, String>> list = super.executeFindList(sql);
		Map<String,String> result_map = new HashMap<String, String>();
		if ( !ArrayUtil.isNullOrEmpty(list) ) {
			result_map = list.get(0);
		}
		return result_map;
	}
	
	
	/**
	 * 验证账号是否该工单的用车人
	 * @param accountId 账号
	 * @param woId 工单id
	 * @return (boolean) 是否用车人 - true 是该工单的用车人
	 */
	public boolean checkUseCarPerson ( String accountId , String woId ) {		
		String sql = 	" 	SELECT " + 
						"		 " + CardispatchConstant.WORKORDER_COLUMNTEXT + 
						"	FROM  " + 
						"		 " + DBUtil.getTableName(CardispatchWorkorder.class) + " workorder " + 
						"	WHERE " + 
						"		useCarPersonId = '" + accountId + "' " +  
						"		AND woId = '" + woId + "' ";
		List<Map<String, String>> list = super.executeFindList(sql);
		boolean flag = false;
		if ( list != null && list.size() > 0 ) {
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 根据工单信息为参,获取工单信息
	 * @param param_map 车辆、工单信息
	 * @param state 工单状态
	 * @param size 返回数目
	 * @return (List<Map<String,String>>) 信息集合
	 */
	public List<Map<String,String>> findCardispatchWordorderByStateForSize ( Map param_map , String state , Integer size ) {
		String whereString = " WHERE 1=1 ";
		String startTime = "";
		String endTime = "";
		if ( param_map != null ) {
			if ( param_map.containsKey("startTime") && !(param_map.get("startTime")+"").isEmpty() ) {
				startTime += " AND wo.realUseCarTime >= to_date('" + param_map.get("startTime")+"','yyyy-mm-dd hh24:mi:ss')";
				param_map.remove("startTime");
			}
			if ( param_map.containsKey("endTime") && !(param_map.get("endTime")+"").isEmpty() ) {
				endTime += " AND wo.realReturnCarTime <= to_date('" + param_map.get("endTime")+"','yyyy-mm-dd hh24:mi:ss')";
				param_map.remove("endTime");
			}
			if ( param_map.containsKey("createStartTime") && !(param_map.get("createStartTime")+"").isEmpty() ) {
				startTime += " AND work.createTime >= to_date('" + param_map.get("createStartTime")+"','yyyy-mm-dd hh24:mi:ss')";
				param_map.remove("createStartTime");
			}
			if ( param_map.containsKey("createEndTime") && !(param_map.get("createEndTime")+"").isEmpty() ) {
				endTime += " AND work.createTime <= to_date('" + param_map.get("createEndTime")+"','yyyy-mm-dd hh24:mi:ss')";
				param_map.remove("createEndTime");
			}
		}
		whereString = DBUtil.getWhereString(param_map, null);
		if ( !StringUtil.isNullOrEmpty(state) ) {
			if( state.equalsIgnoreCase(CardispatchConstant.CARDISATCHWORKORDER_ESTATE_RETURNCAR) || state.equalsIgnoreCase(CardispatchConstant.CARDISATCHWORKORDER_ESTATE_CLOSEORDER) ) {
				//已关闭工单,已完成
				whereString += " AND vwork.status = '" + WorkManageConstant.WORKORDER_END + "' ";
			} else if ( state.equalsIgnoreCase(CardispatchConstant.CARDISATCHWORKORDER_ESTATE_USECAR) ) {
				//待还车,已用车
				whereString += " AND vwork.\"status\" = '" + WorkManageConstant.WORKORDER_WAIT4RETURNCAR + "' ";
			} else if ( state.equalsIgnoreCase(CardispatchConstant.CARDISATCHWORKORDER_ESTATE_SENDCAR) ) {
				//待用车,已派车
				whereString += " AND vwork.\"status\" = '" + WorkManageConstant.WORKORDER_WAIT4USECAR + "' ";
			} else if ( state.equalsIgnoreCase(CardispatchConstant.CARDISATCHWORKORDER_ESTATE_CREATEORDER) || state.equalsIgnoreCase(CardispatchConstant.CARDISATCHWORKORDER_ESTATE_PENDINGWORKORDER) ) {
				//待派车,已创建工单
				whereString += " AND vwork.\"status\" = '" + WorkManageConstant.WORKORDER_WAIT4ASSIGNCAR + "' ";
			}  else if ( state.equalsIgnoreCase(CardispatchConstant.CARDISATCHWORKORDER_ESTATE_UNFINISH) ) {
				//未完成
				whereString += " AND vwork.\"status\" != '" + WorkManageConstant.WORKORDER_END + "' ";
			}
		}
		
		whereString += startTime + endTime;
		String limitString = "";
		if ( size != null && size >= 0 ) {
			limitString = " AND rowNum > " + (size-1)*20 + " AND rowNum <= " + size*20 ;
		}
		whereString += limitString ;
		String orderString = " ORDER BY vwork.\"createTime\" DESC ";
		String selectString = 	"	vwork.\"woId\" \"woId\" , " +
								" vwork.\"woTitle\" \"woTitle\" ,  " +
								"vwork.\"status\" \"status\" ,  " +
								"vwork.\"creator\" \"creator\" ,  " +
								"vwork.\"creatorName\" \"creatorName\" ,  " +
								"vwork.\"creatorOrgId\" \"creatorOrgId\" ,  " +
								"vwork.\"createTime\" \"createTime\" ,  " +
								"vwork.\"isRead\" \"isRead\" ,  " +
								"vwork.\"statusName\" \"statusName\" ,  " +
								"workorder.useCarPersonId \"useCarPersonId\" ,  " +
								"workorder.useCarType \"useCarType\" ,  " +
								"workorder.applyDescription \"applyDescription\" ,  " +  
								" workorder.planUseCarTime \"planUseCarTime\" ,  " +
								"workorder.planUseCarAddress \"planUseCarAddress\" ,  " +
								"workorder.sendCarPersonName \"sendCarPersonName\" , " +
								"workorder.realUseCarTime \"realUseCarTime\" ,  " +
								"workorder.realUseCarMileage \"realUseCarMileage\" ,  " +
								"useCarAddressDescription \"useCarAddressDescription\" ,  " +
								"carDriverPairId \"carDriverPairId\" ,  " +
								"workorder.planReturnCarTime \"planReturnCarTime\" ,  " +
								"workorder.realReturnCarTime \"realReturnCarTime\" ,  " +
								"workorder.realReturnCarAddress \"realReturnCarAddress\" " ;
		String sql = 	" SELECT "
							+ selectString + 
						" FROM " + 
						" 	V_WM_CAR_WORKORDER vwork  " + 
						" 	INNER JOIN " + DBUtil.getTableName(CardispatchWorkorder.class) + " workorder ON  workorder.woId = vwork.\"woId\"  " + whereString + orderString ;
		List<Map<String, String>> list = super.executeFindList(sql);
		return list;
	}
	
	
	/**
	 * 根据车辆、工单信息为参,获取工单信息
	 * @param param_map 车辆、工单信息
	 * @param useCarTime_startTime 计划用车起始时间
	 * @param useCarTime_endTime 计划用车结束时间
	 * @param gender 排序字段
	 * @param upordown 升序降序
	 * @param size 返回数目
	 * @return (List<Map<String,String>>) 信息集合
	 */
	public List<Map<String,String>> findCarWorkOrderListBySearchForMobile ( Map param_map , String useCarTime_startTime , String useCarTime_endTime , String gender , String upordown  , Integer size  ) {
		String whereString = " WHERE 1=1 ";
		String startTime = "";
		String endTime = "";
		if ( !StringUtil.isNullOrEmpty(useCarTime_startTime) ) {
			startTime += " AND planUseCarTime >= to_date('" + useCarTime_startTime +"','yyyy-mm-dd hh24:mi:ss')";
		}
		if ( !StringUtil.isNullOrEmpty(useCarTime_endTime) ) {
			endTime += " AND planUseCarTime <= to_date('" + useCarTime_endTime +"','yyyy-mm-dd hh24:mi:ss')";
		}
		if ( param_map.containsKey("woTitle") && param_map.get("woTitle") != null ) {
			Object woTital = param_map.remove("woTitle");
			param_map.put("\"woTitle\"",woTital);
		}
		whereString = DBUtil.getWhereString(param_map, new DBWhereCallBack() {
			public String callBack(String columnName, StringBuffer opera,
					StringBuffer value) {
				if ( columnName.equals("carNumber") || columnName.equals("\"woTitle\"") ) {
					opera.delete(0, opera.length());
					opera.append("LIKE");
					return "%" + value + "%";
				}
				return null;
			}
		});
		String orderString = "";
		if ( !StringUtil.isNullOrEmpty(gender) ) {
			orderString += " ORDER BY " + gender + " " + upordown;
		}
		whereString += startTime + endTime;
		String limitString = "";
		if ( size != null && size >= 0 ) {
			limitString = " AND rowNum > " + (size-1)*20 + " AND rowNum <= " + size*20 ;
		}
		whereString += limitString ;
		String vworkString = " v.\"woId\" \"woId\" ,  " +
							"v.\"woTitle\" \"woTitle\" ,  " +
							"v.\"status\" \"status\" ,  " +
							"v.\"creator\" \"creator\" ,  " +
							"v.\"creatorName\" \"creatorName\" ,  " +
							"v.\"creatorOrgId\" \"creatorOrgId\" ,  " +
							"v.\"createTime\" \"createTime\" ,  " +
							"v.\"isRead\" \"isRead\" ,  " +
							"v.\"statusName\" \"statusName\" ";
		String selectString = 	"	vwork.\"woId\" \"woId\" , " +
								" vwork.\"isRead\" \"isRead\" ,  " +
								" vwork.\"statusName\" \"statusName\" , "  + 
								" vwork.\"woTitle\" \"woTitle\" ,  " +
								"vwork.\"creator\" \"creator\" ,  " +
								"vwork.\"creatorName\" \"creatorName\" ,  " +
								"vwork.\"creatorOrgId\" \"creatorOrgId\" ,  " +
								"vwork.\"createTime\" \"createTime\" ,  " +
								"driver.driverName \"driverName\" ,  " +
								"driver.accountId \"accountId\" ,  " +
								"car.carNumber \"carNumber\" ,   " +
								"workorder.useCarPersonId \"useCarPersonId\" ,  " +
								"useCarType \"useCarType\" ,  " +
								"workorder.applyDescription \"applyDescription\" ,  " + 
								" workorder.planUseCarTime \"planUseCarTime\" ,  " +
								"workorder.planUseCarAddress \"planUseCarAddress\" ,  " +
								"workorder.realUseCarTime \"realUseCarTime\" ,  " +
								"workorder.realUseCarMileage \"realUseCarMileage\" ,  " +
								"useCarAddressDescription \"useCarAddressDescription\" ,  " +
								"carDriverPairId \"carDriverPairId\" ,  " +
								"workorder.planReturnCarTime \"planReturnCarTime\" ,  " +
								"workorder.realReturnCarTime \"realReturnCarTime\" ,  " +
								"workorder.realReturnCarAddress \"realReturnCarAddress\" , " +
								"workorder.criticalClass \"criticalClass\" ";
		String sql = 	" SELECT " + 
							selectString + 
						" FROM " + 
						" (SELECT " + vworkString + " FROM V_WM_CAR_WORKORDER v ) vwork " + 
						" INNER JOIN " + DBUtil.getTableName(CardispatchWorkorder.class) + " workorder ON workorder.woId = vwork.\"woId\" " + 
						" LEFT JOIN  " + DBUtil.getTableName(CardispatchDrivercarpair.class) + "  cardriverpair ON cardriverpair.id = workorder.carDriverPairId " + 
						" LEFT JOIN  " + DBUtil.getTableName(CardispatchCar.class) + "  car ON car.id = cardriverpair.car_id " + 
						" LEFT JOIN  " + DBUtil.getTableName(CardispatchDriver.class) + "  driver ON driver.id = cardriverpair.driver_id " + 
						whereString + orderString  ;
		List<Map<String, String>> list = super.executeFindList(sql);
		list = new ArrayList<Map<String,String>>();
		return list;
	}
	
	
	public Map<String, String> findCardispatchWorkerorderType ( String woId ) {
		String sql = "SELECT * FROM V_WM_CAR_WORKORDER WHERE \"woId\" = '" + woId + "'";
		List<Map<String, String>> list = super.executeFindList(sql);
		Map<String, String> result_map = new HashMap<String, String>();
		if ( !ArrayUtil.isNullOrEmpty(list) ) {
			result_map = list.get(0);
		}
		return result_map;
	}
	
	
}
