package com.iscreate.op.dao.cardispatch;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.op.constant.WorkManageConstant;
import com.iscreate.op.dao.informationmanage.BaseDao;
import com.iscreate.op.pojo.cardispatch.CardispatchWorkorder;

public interface CardispatchWorkorderDao extends BaseDao<CardispatchWorkorder>{

	public abstract List<Map<String,Object>> findApplyWorkorderByToId(String woId, String toId,
			String workType);

	public abstract List<Map<String,String>> findCardispatchWorkorderTasktracerecord(String woId);

	public abstract boolean deleteFeerecord(Map param_map);

	public abstract List<Map<String, String>> findFeerecordList(Map param_map, String startTime,
			String endTime);

	public abstract List<Map<String, String>> findFeerecordListByWoId(String woId);

	public abstract Long saveFeerecord(Map param_map);

	public abstract int updateCardispatchWorkorder(Map param_map, Map set_map);

	public abstract Map<String, String> findSingleCardispatchWorkorderByWoId(String woId);

	public abstract Map<String, String> findSingleCardispatchWorkorder(Map param_map);

	public abstract List<Map<String,String>> findCardispatchWordorderByStateForSize(Map param_map,
			String state, Integer size);

	public abstract List<Map<String,String>> findCardispatchWordorderByState(Map param_map,
			String state);

	public abstract Long saveCardispatchWorkorder(Map param_Map);

	public abstract boolean checkUseCarPerson(String accountId, String woId);

	
	/**
	 * 根据车辆司机关系id,读取时间段内车辆工单记录里程读数
	 * @param cdPairId - 车辆司机关系id
	 * @param beginTime - 工单创建时间起始范围
	 * @param endTime - 工单创建时间结束范围
	 * @return (Long) 车辆时间段内总里程读数
	 */
	public abstract Double findWorkorderTotalMileageByCdPairIdInTime(
			String cdPairId, String beginTime, String endTime);

	/**
	 * 根据工单号woId,获取工单用车、还车时间、车牌
	 * @param woId - 工单号
	 * @return (Map<String,String>) 车牌(carNumber)、用车(realUseCarTime)、还车(realReturnCarTime)时间信息 , 
	 */
	public abstract Map<String,String> findUseReturnCarTimeByWoId(String woId);

	public abstract Map<String,String> findFirstUseCarWorkorderInTimeByCarNumber(String carNumber,
			String startTime);

	public abstract Map<String,String> findLastReturnCarWorkorderInTimeByCarNumber(
			String carNumber, String endTime);





}
