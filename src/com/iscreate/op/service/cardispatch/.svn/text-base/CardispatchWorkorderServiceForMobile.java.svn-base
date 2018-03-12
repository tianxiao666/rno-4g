package com.iscreate.op.service.cardispatch;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.informationmanage.common.DateUtil;
import com.iscreate.op.action.informationmanage.common.ObjectUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.constant.CardispatchConstant;
import com.iscreate.op.constant.WorkManageConstant;
import com.iscreate.op.pojo.cardispatch.CardispatchWorkorder;
import com.iscreate.op.pojo.organization.Staff;
import com.iscreate.op.pojo.publicinterface.Tasktracerecord;
import com.iscreate.op.pojo.workmanage.WorkmanageWorkorder;
import com.iscreate.op.service.informationmanage.BaseService;
import com.iscreate.op.service.publicinterface.SessionService;

public interface CardispatchWorkorderServiceForMobile extends BaseService<CardispatchWorkorder>{

	public abstract List<Map<String,String>> findCarWorkOrderListBySearchForMobile(Map param_map,
			String useCarTime_startTime, String useCarTime_endTime, String gender, String upordown , Integer size);

	public abstract List<Map<String,String>> findCardispatchWordorderByStateForSize(Map param_map,
			String state, Integer size);

	public abstract String enterCardispatchWorkorder(String woId, Map<String,String> requestParamMap);

	public abstract Map<String,String> findSingleCardispatchWorkorderByWoIdForMobile(
			String woId);

	public abstract Map<String,String> findCardispatchWorkorderAssociateTaskByWoIdForMobile(
			String woId);

	public abstract List<Map<String,String>> findCardispatchWorkorderFeerecordByWoIdForMobile(
			String woId);

	public abstract Integer findCardispatchWordorderByStateForSizeCount(
			Map param_map, String state, Integer size);

	public abstract boolean remindersCarTask(String WOID, String remindersReason);

	public abstract List<Map<String,String>> findCardispatchTasktracerecordByWoIdForMobile(
			String woId);

	
	/****************** service *******************/
	/**
	 * 
	 * @description: 根据条件获取车辆相关信息（分页）
	 * @author：yuan.yw
	 * @param conditionMap
	 * @param indexStart
	 * @param indexEnd
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jul 23, 2013 5:09:03 PM
	 */
	public abstract List<Map<String,Object>> getCarStateMonitorListByCondition(Map<String,Object> conditionMap,String indexStart,String indexEnd);
	/**
	 * 
	 * @description: 根据车辆id获取相关信息
	 * @author：yuan.yw
	 * @param carId
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Jul 24, 2013 10:19:48 AM
	 */
	public abstract Map<String,Object> getCarRelatedInformationByCarId(String carId);
	
	
}
