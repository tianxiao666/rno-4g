package com.iscreate.op.dao.cardispatch;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.informationmanage.common.DBUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.constant.CardispatchConstant;
import com.iscreate.op.constant.WorkManageConstant;
import com.iscreate.op.dao.informationmanage.BaseDaoImpl;
import com.iscreate.op.pojo.cardispatch.CardispatchApplyworkorder;
import com.iscreate.op.pojo.cardispatch.CardispatchCar;
import com.iscreate.op.pojo.cardispatch.CardispatchCarterminalpair;
import com.iscreate.op.pojo.cardispatch.CardispatchDriver;
import com.iscreate.op.pojo.cardispatch.CardispatchDrivercarpair;
import com.iscreate.op.pojo.cardispatch.CardispatchFeerecord;
import com.iscreate.op.pojo.cardispatch.CardispatchWorkorder;
import com.iscreate.op.pojo.cardispatch.CardispathTerminal;
import com.iscreate.op.pojo.workmanage.WorkmanageWorkorder;

@SuppressWarnings({"rawtypes","unchecked","unused","hiding"})
public class CardispatchWorkorderDaoImpl extends BaseDaoImpl<CardispatchWorkorder> implements CardispatchWorkorderDao {

	private static final String car_Table = " (SELECT c.* , id as carId FROM " + DBUtil.getTableName(CardispatchCar.class) + " c ) car "; 
	private static final String driver_Table = " (SELECT d.* , id as driverId FROM " + DBUtil.getTableName(CardispatchDriver.class) + " d ) driver "; 
	private static final String terminal_Table = " (SELECT t.* , id as terminalId FROM " + DBUtil.getTableName(CardispathTerminal.class) + " t ) terminal "; 
	private static final String car_terminal_Table = " (SELECT ctp.* , id as ctpairId FROM " + DBUtil.getTableName(CardispatchCarterminalpair.class) + " ctp ) ctpair "; 
	private static final String car_driver_Table = " (SELECT cdp.* , id as cdpairId FROM " + DBUtil.getTableName(CardispatchDrivercarpair.class) + " cdp ) cdpair "; 
	private static final String car_workorder_Table = " ( SELECT w.* , id as workorderId FROM " + DBUtil.getTableName(CardispatchWorkorder.class) + " w ) workorder ";
	
	
	
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
						"		" + car_workorder_Table + 
						"	WHERE " + 
						"		workorder.useCarPersonId = '" + accountId + "' " +  
						"		AND workorder.woId = '" + woId + "' ";
		List<Map<String, String>> list = super.executeFindList(sql);
		boolean flag = false;
		if ( list != null && list.size() > 0 ) {
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 创建车辆调度工单
	 * @param param_Map 参数集合
	 * @return (Long) 工单id
	 */
	public Long saveCardispatchWorkorder ( Map param_Map ) {
		param_Map.put("id", new StringBuffer(CardispatchConstant.SEQ_WORKORDER + ".nextval"));
		//planReturnCarTime    planUseCarTime  
		param_Map.put("planReturnCarTime", new StringBuffer("to_date('" + param_Map.get("planReturnCarTime") + "','yyyy-mm-dd hh24:mi:ss')") );
		param_Map.put("planUseCarTime", new StringBuffer("to_date('" + param_Map.get("planUseCarTime") + "','yyyy-mm-dd hh24:mi:ss')") );
		Long num = txsave(param_Map);
		return num;
	}
	
	/**
	 * 根据工单、车辆、司机、终端信息、工单状态为参,获取数据
	 * @param param_map - 工单、车辆、司机、终端信息参数
	 * @param state - 工单状态
	 * @return (List<Map<String,String>>) 工单、车辆、司机、终端信息集合
	 */
	public List<Map<String,String>> findCardispatchWordorderByState ( Map param_map , String state ) {
		List<Map<String, String>> list = findCardispatchWordorderByStateForSize(param_map, state, null);
		return list;
	}
	
	
	
	/**
	 * 根据工单、车辆、司机、终端信息、工单状态为参,获取数据
	 * @param param_map - 工单、车辆、司机、终端信息参数
	 * @param state - 工单状态
	 * @param size - 数据条目数
	 * @return (List<Map<String,String>>) 工单、车辆、司机、终端信息集合
	 */
	public List<Map<String,String>> findCardispatchWordorderByStateForSize ( Map param_map , String state , Integer size ) {
		String whereString = "";
		String startTime = "";
		String endTime = "";
		if ( param_map != null ) {
			if ( param_map.containsKey("startTime") && !(param_map.get("startTime")+"").isEmpty() ) {
				startTime += " AND workorder.realUseCarTime >= to_date('" + param_map.get("startTime")+"','yyyy-mm-dd hh24:mi:ss')";
				param_map.remove("startTime");
			}
			if ( param_map.containsKey("endTime") && !(param_map.get("endTime")+"").isEmpty() ) {
				endTime += " AND workorder.realReturnCarTime <= to_date('" + param_map.get("endTime")+"','yyyy-mm-dd hh24:mi:ss')";
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
		whereString = DBUtil.getWhereString(param_map, new DBWhereCallBack() {
			public String callBack(String columnName, StringBuffer opera,
					StringBuffer value) {
				if ( columnName.equals("woTitle") ) {
					String v = value.toString();
					v = StringUtil.handleDbSensitiveString(v);
					opera.delete( 0 , opera.length() );
					opera.append("LIKE");
					return "%" + v + "%";
				}
				return null;
			}
		});
		if ( !StringUtil.isNullOrEmpty(state) ) {
			if( state.equalsIgnoreCase(CardispatchConstant.CARDISATCHWORKORDER_ESTATE_RETURNCAR) || state.equalsIgnoreCase(CardispatchConstant.CARDISATCHWORKORDER_ESTATE_CLOSEORDER) ) {
				//已关闭工单,已完成
				whereString += " AND workorderStatus = " + WorkManageConstant.WORKORDER_END + " ";
			} else if ( state.equalsIgnoreCase(CardispatchConstant.CARDISATCHWORKORDER_ESTATE_USECAR) ) {
				//待还车,已用车
				whereString += " AND workorderStatus = " + WorkManageConstant.WORKORDER_WAIT4RETURNCAR + " ";
			} else if ( state.equalsIgnoreCase(CardispatchConstant.CARDISATCHWORKORDER_ESTATE_SENDCAR) ) {
				//待用车,已派车
				whereString += " AND workorderStatus = " + WorkManageConstant.WORKORDER_WAIT4USECAR + " ";
			} else if ( state.equalsIgnoreCase(CardispatchConstant.CARDISATCHWORKORDER_ESTATE_CREATEORDER) || state.equalsIgnoreCase(CardispatchConstant.CARDISATCHWORKORDER_ESTATE_PENDINGWORKORDER) ) {
				//待派车,已创建工单
				whereString += " AND workorderStatus = " + WorkManageConstant.WORKORDER_WAIT4ASSIGNCAR + " ";
			}  else if ( state.equalsIgnoreCase(CardispatchConstant.CARDISATCHWORKORDER_ESTATE_UNFINISH) ) {
				//未完成
				whereString += " AND workorderStatus != " + WorkManageConstant.WORKORDER_END + " ";
			}
		}
		whereString += startTime + endTime;
		String limitString = "";
		if ( size != null ) {
			limitString += " LIMIT 0 , " + size;
		}
		String orderString = " ORDER BY work.createTime DESC ";
		String selectString = 	" apply.associateWorkType \"associateWorkType\" , apply.associateToId \"associateToId\" , apply.associateWoId \"associateWoId\" " +
								" , work.workorderStatus \"workorderStatus\" , work.woTitle \"woTitle\" , work.creatorName \"creatorName\" , work.creator \"creator\" , work.isOverTime \"isOverTime\" , work.isRead \"isRead\" , work.createTime \"createTime\" " +
								" ," + CardispatchConstant.WORKORDER_COLUMNTEXT + 
								" ," + CardispatchConstant.DRIVER_COLUMNTEXT + 
								" ," + CardispatchConstant.CAR_COLUMNTEXT + 
								" ," + CardispatchConstant.TERMINAL_COLUMNTEXT + 
								"";
		String sql = 	" 	SELECT " + 
						"		" + selectString + 
						"	FROM  " + 
						"		(SELECT w.*, status AS workorderStatus FROM  " + DBUtil.getTableName(WorkmanageWorkorder.class) + " w ) work " +
						"		INNER JOIN  " + DBUtil.getTableName(CardispatchWorkorder.class) + " workorder ON workorder.woId = work.woId " + 
						"		LEFT JOIN  " + DBUtil.getTableName(CardispatchDrivercarpair.class) + " cardriverpair ON cardriverpair.id = workorder.carDriverPairId " + 
						"		LEFT JOIN (SELECT c.* , id AS carId FROM  " + DBUtil.getTableName(CardispatchCar.class) + " c ) car ON car.id = cardriverpair.car_id " + 
						"		LEFT JOIN  " + DBUtil.getTableName(CardispatchDriver.class) + " driver ON driver.id = cardriverpair.driver_id " + 
						"		LEFT JOIN  " + DBUtil.getTableName(CardispatchCarterminalpair.class) + " carterminalpair ON carterminalpair.car_id = car.id " + 
						"		LEFT JOIN  " + DBUtil.getTableName(CardispathTerminal.class) + " terminal ON terminal.id = carterminalpair.terminal_id " + 
						"		LEFT JOIN (SELECT a.* , id AS applyId FROM  " + DBUtil.getTableName(CardispatchApplyworkorder.class) + " a ) apply ON apply.carWoId = workorder.woId "
						+ whereString + limitString;
		List<Map<String, String>> list = super.executeFindList(sql);
		return list;
	}
	
	
	
	
	/**
	 * 根据工单、车辆、司机、终端信息为参,查询数据(单个)
	 * @param param_map - 信息参数
	 * @return (Map<String, String>) 工单、车辆、司机、终端信息
	 */
	public Map<String, String> findSingleCardispatchWorkorder ( Map param_map ) {
		List<Map<String, String>> list = findCardispatchWordorderByState(param_map,null);
		Map<String,String> map = new LinkedHashMap<String, String>();
		if ( list != null && list.size() > 0 && list.get(0) != null ) {
			map = list.get(0);
		}
		return map;
	}
	
	
	
	/**
	 * 根据工单id,查询数据(单个)
	 * @param param_map - 信息参数
	 * @return (Map<String, String>) 工单、车辆、司机、终端信息
	 */
	public Map<String, String> findSingleCardispatchWorkorderByWoId ( String woId ) {
		Map param_map = new LinkedHashMap();
		param_map.put("workorder.woId", woId);
		Map<String, String> result_map = findSingleCardispatchWorkorder(param_map);
		return result_map;
	}
	
	
	/**
	 * 根据工单号woId,获取工单用车、还车时间、车牌
	 * @param woId - 工单号
	 * @return (Map<String,String>) 车牌(carNumber)、用车(realUseCarTime)、还车(realReturnCarTime)时间信息 , 
	 */
	public Map<String,String> findUseReturnCarTimeByWoId ( String woId ) {
		Map<String,String> result_map = new LinkedHashMap<String, String>();
		if ( StringUtil.isNullOrEmpty(woId) ) {
			return result_map;
		}
		String sql = "SELECT " +
				"		carNumber \"carNumber\" , realUseCarTime \"realUseCarTime\" , realReturnCarTime \"realReturnCarTime\" " +
				"	FROM  " +
				"		" + DBUtil.getTableName(CardispatchWorkorder.class) + " wo " +
				"		INNER JOIN  " + DBUtil.getTableName(CardispatchDrivercarpair.class) + " cdPair ON cdPair.id = wo.carDriverPairId " + 
				"		INNER JOIN  " + DBUtil.getTableName(CardispatchCar.class) + " car ON car.id = cdPair.car_id " +
				"	WHERE " +
				"		woId = '" + woId + "'";
		List<Map<String, String>> list = super.executeFindList(sql);
		if ( list != null && list.size() > 0 ) {
			result_map = list.get(0);
		}
		return result_map;
	}
	
	
	
	
	/**
	 * 更新工单信息
	 * @param param_map - where条件
	 * @param set_map - 要更新的数据
	 * @return (int) 是否操作成功
	 */
	public int updateCardispatchWorkorder ( Map param_map , Map set_map ) {
		if ( set_map.get("realUseCarMileage") != null && !((set_map.get("realUseCarMileage")+"").isEmpty()) ) {
			set_map.put("realUseCarMileage", Double.valueOf(set_map.get("realUseCarMileage")+"") );
		}
		if ( set_map.get("realReturnCarMileage") != null && !((set_map.get("realReturnCarMileage")+"").isEmpty()) ) {
			set_map.put("realReturnCarMileage", Double.valueOf(set_map.get("realReturnCarMileage")+"") );
		}
		if ( set_map.get("realUseCarTime") != null && !((set_map.get("realUseCarTime")+"").isEmpty()) ) {
			set_map.put("realUseCarTime", new StringBuffer( "to_date('" + set_map.get("realUseCarTime")+"','yyyy-mm-dd hh24:mi:ss')" ) );
		}
		if ( set_map.get("realReturnCarTime") != null && !((set_map.get("realReturnCarTime")+"").isEmpty()) ) {
			set_map.put("realReturnCarTime", new StringBuffer( "to_date('" + set_map.get("realReturnCarTime")+"','yyyy-mm-dd hh24:mi:ss')" ) );
		}
		int txupdate = super.txupdate(param_map, set_map);
		return txupdate;
	}
	
	/**
	 * 保存行车费用
	 * @param param_map 费用信息
	 * @return (Long) 费用id 
	 */
	public Long saveFeerecord ( Map param_map ) {
		param_map.put("id", new StringBuffer(CardispatchConstant.SEQ_FEERECORD + ".nextval" ));
		param_map.put("feeAmount", Double.valueOf(param_map.get("feeAmount")+"") );
		Long txsave = super.txsave(param_map,CardispatchFeerecord.class);
		return txsave;
	}
	
	/**
	 * 根据工单id,获取工单、费用数据
	 * @param param_map 工单、费用参数
	 * @return (List<Map<String,String>>)工单、费用信息集合
	 */
	public List<Map<String, String>> findFeerecordListByWoId ( String woId ) {
		String whereString = " WHERE workorder.woId = '"+woId+"' ";
		String sql = 	" 	SELECT " + 
						"		 " + CardispatchConstant.FEECORD_COLUMNTEXT + 
						"	FROM  " + 
						"		(SELECT w.* , id AS workorderId FROM " + DBUtil.getTableName(CardispatchWorkorder.class) + " w ) workorder " + 
						"		INNER JOIN (SELECT f.* , id AS feeId FROM  " + DBUtil.getTableName(CardispatchFeerecord.class) + " f ) fee ON fee.woId = workorder.woId"
						+ whereString;
		List<Map<String, String>> list = super.executeFindList(sql);
		return list;
	}
	
	/**
	 * 根据工单、车辆参数、费用产生的时间区间,获取数据
	 * @param param_map - 参数集合
	 * @param startTime 费用产生的起始时间
	 * @param endTime 费用产生的结束时间
	 * @return (List<Map<String, String>>)  工单、车辆、费用信息集合
	 */
	public List<Map<String, String>> findFeerecordList ( Map param_map , String startTime , String endTime ) {
		String feeString = "";
		if ( param_map.containsKey("feeAmount") && param_map.get("feeAmount") != null ) {
			String feeAmount = param_map.get("feeAmount")+"";
			if ( feeAmount.indexOf("~") == -1 ) {
				feeString += " AND feeAmount > " + feeAmount;
			} else {
				String[] split = feeAmount.split("~");
				if ( split != null && split.length == 2 ) {
					feeString += " AND feeAmount >= " + split[0] + " AND feeAmount <= " + split[1];
				}
			}
		}
		param_map.remove("feeAmount");
		String whereString = getWhereString(param_map, null);
		whereString += feeString;
		if ( !StringUtil.isNullOrEmpty(startTime) ) {
			whereString += " AND to_date(happenTime,'yyyy-mm-dd hh24:mi:ss') >= to_date('"+startTime+"','yyyy-mm-dd hh24:mi:ss') ";
		}
		if ( !StringUtil.isNullOrEmpty(endTime) ) {
			whereString += " AND to_date(happenTime,'yyyy-mm-dd hh24:mi:ss') <= to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') ";
		}
		String sql = 	" 	SELECT " + 
						"		fee.*  , " + CardispatchConstant.WORKORDER_COLUMNTEXT + " , staffName \"staffName\" " + 
						"	FROM  " + 
						"		(SELECT w.* , id AS workorderId FROM " + DBUtil.getTableName(CardispatchWorkorder.class) + " w ) workorder " + 
						"		INNER JOIN (SELECT f.* , id AS feeId FROM  " + DBUtil.getTableName(CardispatchFeerecord.class) + " f ) fee ON fee.woId = workorder.woId " + 
						"		INNER JOIN  " + DBUtil.getTableName(CardispatchDrivercarpair.class) + " pair ON pair.id = workorder.carDriverPairId " + 
						"		INNER JOIN (SELECT c.* , id AS carId FROM  " + DBUtil.getTableName(CardispatchWorkorder.class) + " c ) car ON car.id = pair.car_id " +
						" 		INNER JOIN (SELECT sf.* , name AS staffName , id AS StaffId FROM staff sf ) staff ON staff.account = fee.confirmPeople "
						+ whereString;
		List<Map<String, String>> list = super.executeFindList(sql);
		return list;
	}
	
	/**
	 * 根据费用信息参数,删除费用信息记录
	 * @param param_map - 费用信息参数集合
	 * @return (boolean)操作是否成功
	 */
	public boolean deleteFeerecord ( Map param_map ) {
		int txdelete = super.txdelete(param_map,CardispatchFeerecord.class);
		boolean flag =  txdelete > 0;
		return flag;
	}
	
	/**
	 * 根据工单woId , 查询工单处理信息
	 * @param woId - 工单woId
	 * @return (List<Map<String,String>>) 工单集合信息
	 */
	public List<Map<String,String>> findCardispatchWorkorderTasktracerecord ( String woId ) {
		String sql = 	"	SELECT " +
						"		handleWay \"handleWay\" , handleTime \"handleTime\" , " +
						"		handler \"handler\" " +
						"	FROM " +
						"		tasktracerecord " + 
						"	WHERE " +
						"		woId='" + woId + "'";
		List<Map<String, String>> list = executeFindList(sql);
		return list;
	}
	
	
	
	/**
	 * 根据申请参数,获取车辆调度单信息集合
	 * @param woId - (抢修..)的工单woId
	 * @param toId - (抢修..)的任务单toId
	 * @param workType - 申请的类型(CardispatchConstant.CARDISATCHWORKORDER_APPLYTYPE_)
	 */
	public List<Map<String,Object>> findApplyWorkorderByToId ( String woId , String toId , String workType ) {
		String whereString = " WHERE 1=1 ";
		if ( !StringUtil.isNullOrEmpty(woId) ) {
			whereString += " AND apply.associateWoId = '" + woId + "'";
		}
		if ( !StringUtil.isNullOrEmpty(toId) ) {
			whereString += " AND apply.associateToId = '" + toId + "'";
		}
		if ( !StringUtil.isNullOrEmpty(workType) ) {
			if ( workType.equalsIgnoreCase(CardispatchConstant.CARDISATCHWORKORDER_APPLYTYPE_URGENTREPAIR) ) {
				whereString += " AND apply.associateWorkType = '" + workType + "'";
			} else if ( workType.equalsIgnoreCase(CardispatchConstant.CARDISATCHWORKORDER_APPLYTYPE_CAR) ) {
				whereString += " AND apply.associateWorkType IS NULL ";
			}
		}
		String selectString = 	" apply.carWoId \"carWoId\" , apply.associateWorkType \"associateWorkType\" , apply.associateToId \"associateToId\" , apply.associateWoId \"associateWoId\" " +
								" , wo.status \"workorderStatus\" , wo.woTitle \"woTitle\" , wo.creatorName \"creatorName\" , wo.creator \"creator\" , wo.isOverTime \"isOverTime\" , wo.isRead \"isRead\" , to_char(wo.createTime,'yyyy-mm-dd hh24:mm:ss') \"createTime1\" " +
								" , " + CardispatchConstant.WORKORDER_COLUMNTEXT + 
								"";
		
		String sql = 	"	SELECT " + 
						"		 " + selectString + 
						"	FROM  " + 
						"		 " + DBUtil.getTableName(WorkmanageWorkorder.class) + "  wo " + 
						"		LEFT JOIN " + DBUtil.getTableName(CardispatchWorkorder.class) + " workorder ON wo.woId = workorder.woId " + 
						"		LEFT JOIN  " + DBUtil.getTableName(CardispatchApplyworkorder.class) + "  apply ON apply.carWoId = workorder.woId "
							+ whereString;
		List<Map<String, Object>> list = super.executeFindListMapObject(sql);
		return list;
	}
	
	
	/**
	 * 根据车辆司机关系id,读取时间段内车辆工单记录里程读数
	 * @param cdPairId - 车辆司机关系id
	 * @param beginTime - 工单创建时间起始范围
	 * @param endTime - 工单创建时间结束范围
	 * @return (Long) 车辆时间段内总里程读数
	 */
	public Double findWorkorderTotalMileageByCdPairIdInTime ( String cdPairId , String beginTime , String endTime ) {
		Double totalMileage = 0d;
		if ( cdPairId == null || cdPairId.isEmpty() ) {
			return totalMileage;
		}
		String whereString = " WHERE 1=1 AND realUseCarMileage IS NOT NULL AND realReturnCarMileage IS NOT NULL AND realReturnCarMileage > realUseCarMileage ";
		if ( beginTime != null && !beginTime.isEmpty() ) {
			whereString += " AND createTime >= '" + beginTime + "' ";
		}
		if ( endTime != null && !endTime.isEmpty() ) {
			whereString += " AND createTime <= '" + endTime + "' ";
		}
		whereString += " AND carDriverPairId = " + cdPairId;
		String sql = 	"	SELECT " + 
						"		SUM( realReturnCarMileage - realUseCarMileage ) \"useCarMileage\" " + 
						"	FROM  " + 
						"		" + DBUtil.getTableName(CardispatchWorkorder.class) + " workorder " + 
						"	INNER JOIN " + DBUtil.getTableName(WorkmanageWorkorder.class) + " wo ON wo.woId = workorder.woId " + whereString ;
		List<Map<String, String>> list = executeFindList(sql);
		if ( list != null && list.size() > 0 ) {
			Map<String, String> map = list.get(0);
			String useCarMileage = map.get("useCarMileage");
			if ( useCarMileage != null && !useCarMileage.isEmpty() && !useCarMileage.equals("null") ) {
				totalMileage = Double.valueOf(useCarMileage);
			}
		}
		return totalMileage;
	}
	
	
	/**
	 * 根据车牌获取在一段时间的最先用车的工单
	 * @param carNumber - 车牌
	 * @param startTime - 时间
	 * @return 
	 */
	public Map<String,String> findFirstUseCarWorkorderInTimeByCarNumber ( String carNumber , String startTime ) {
		Map<String,String> result_map = new LinkedHashMap<String, String>();
		String whereSql = " WHERE 1=1 AND realUseCarTime IS NOT NULL ";
		if ( !StringUtil.isNullOrEmpty(carNumber) ) {
			whereSql += " AND carNumber = '" + carNumber + "' ";
		}
		if ( !StringUtil.isNullOrEmpty(startTime) ) {
			whereSql += " AND realUseCarTime >= to_date('" + startTime + "','yyyy-mm-dd hh24:mi:ss') ";
		}
		String sql = 	" 	SELECT " + 
						" 		realUseCarMileage \"realUseCarMileage\" " + 
						"	FROM " + 
						"		" + car_workorder_Table + 
						"		INNER JOIN " + car_driver_Table + " ON cdpair.cdpairId = workorder.carDriverPairId " + 
						"		INNER JOIN " + car_Table + " ON car.carId = cdpair.car_id " + 
						"		INNER JOIN " + driver_Table + " ON driver.driverId = cdpair.driver_id " + whereSql + 
						"	ORDER BY " +
						"		realUseCarTime";
		List<Map<String, String>> list = super.executeFindList(sql);
		if ( list != null & list.size() > 0 ) {
			result_map = list.get(0);
		}
		return result_map;
	}

	/**
	 * 根据车牌获取在一段时间的最后还车的工单
	 * @param carNumber - 车牌
	 * @param endTime - 时间
	 * @return 
	 */
	public Map<String,String> findLastReturnCarWorkorderInTimeByCarNumber ( String carNumber , String endTime ) {
		Map<String,String> result_map = new LinkedHashMap<String, String>();
		String whereSql = " WHERE 1=1 AND realReturnCarTime IS NOT NULL ";
		if ( !StringUtil.isNullOrEmpty(carNumber) ) {
			whereSql += " AND carNumber = '" + carNumber + "' ";
		}
		if ( !StringUtil.isNullOrEmpty(endTime) ) {
			whereSql += " AND realReturnCarTime <= to_date('" + endTime + "','yyyy-mm-dd hh24:mi:ss') ";
		}
		String sql = 	" 	SELECT " + 
						" 		realReturnCarMileage \"realReturnCarMileage\" " + 
						"	FROM " + 
						"		" + car_workorder_Table + 
						"		INNER JOIN " + car_driver_Table + " ON cdpair.cdpairId = workorder.carDriverPairId " + 
						"		INNER JOIN " + car_Table + " ON car.carId = cdpair.car_id " + 
						"		INNER JOIN " + driver_Table + " ON driver.driverId = cdpair.driver_id " + whereSql + 
						"	ORDER BY " + 
						"		realReturnCarTime DESC ";
		List<Map<String, String>> list = super.executeFindList(sql);
		if ( list != null & list.size() > 0 ) {
			result_map = list.get(0);
		}
		return result_map;
	}
	
	
	
}
