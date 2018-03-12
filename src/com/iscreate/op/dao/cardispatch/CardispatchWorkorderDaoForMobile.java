package com.iscreate.op.dao.cardispatch;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.op.constant.WorkManageConstant;
import com.iscreate.op.dao.informationmanage.BaseDao;
import com.iscreate.op.pojo.cardispatch.CardispatchWorkorder;

public interface CardispatchWorkorderDaoForMobile extends BaseDao<CardispatchWorkorder>{

	public abstract List<Map<String,String>> findCarWorkOrderListBySearchForMobile(Map param_map,
			String useCarTime_startTime, String useCarTime_endTime, String gender, String upordown, Integer size);

	public abstract List<Map<String,String>> findCardispatchWordorderByStateForSize(Map param_map,
			String state, Integer size);

	public abstract Map<String, String> findCardispatchWorkerorderType(String woId);

	public abstract boolean checkUseCarPerson(String accountId, String woId);

	public abstract Map<String,String> findSingleCardispatchWorkorderByWoIdForMobile(
			String woId);

	public abstract Map<String,String> findCardispatchWorkorderAssociateTaskByWoIdForMobile(
			String woId);

	public abstract List<Map<String,String>> findCardispatchWorkorderFeerecordByWoIdForMobile(
			String woId);





}
