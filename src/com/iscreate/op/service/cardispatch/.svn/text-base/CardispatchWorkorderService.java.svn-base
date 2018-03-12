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

public interface CardispatchWorkorderService extends BaseService<CardispatchWorkorder>{

	/****************** service *******************/
	public String txCreateCardiaptchWorkorder(Map<String, String> cardispatchWorkorder, Map actionMap);

	public Map<String, String> findSingleCardispatchWorkorder(Map param_map);

	public Boolean txSendCar(Map<String, String> param_map, Map<String,Object> actionMap);

	public Boolean txUseCar(Map<String, String> param_map, Map<String,Object> actionMap);

	public boolean saveFeeAmount(Map param_map);

	public List<Map<String, String>> findFeerecordListByWoId(String woId);

	public boolean deleteFeeAmount(Map param_map);

	public boolean txReturnCar(Map<String, String> param_map, Map<String,Object> actionMap);

	/****************** service *******************/
	public String findCardispatchWorkerorderType(String woId);

	public abstract List<Map<String,String>> findCardispatchWordorderByState(Map param_map,
			String state);

	public abstract List<Map<String, String>> findFeerecordList(Map param_map, String startTime,
			String endTime);

	public abstract String enterCardispatchWorkorder(String woId, Map<String,String> requestParamMap);

	public abstract List<Map<String,Object>> findApplyWorkorderByToId(String woId, String toId,
			String workType);

	public abstract List<Map<String,String>> findCardispatchWordorderByStateForSize(Map param_map,
			String state, Integer size);

	
	
}
