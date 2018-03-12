package com.iscreate.op.service.cardispatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.iscreate.op.action.informationmanage.common.DBUtil;
import com.iscreate.op.action.informationmanage.common.DateUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.pojo.cardispatch.CardispatchOndutydrivercarpair;
import com.iscreate.op.service.informationmanage.BaseService;

public interface CardispatchDutyService extends BaseService<CardispatchOndutydrivercarpair> {

	/**
	 * 根据业务单元、日期、账号,获取条件符合的排班信息
	 * @param dutyDate 日期List集合
	 * @param carId 车辆id
	 * @param freId 班次List集合
	 * @param carBizId 车辆所在组织List集合
	 * @return 排班信息集合
	 */
	public Map<String,Map<String,List<Map<String,Object>>>> cardispatchDutyCalendar(Map<String, String> requestParamMap);

	public List<Map<String,Object>> findCarDutyFreq();

	/**
	 * 根据业务单元、日期、账号,获取条件符合的排班信息
	 * @param dutyDate 日期List集合
	 * @param carId 车辆id
	 * @param freId 班次List集合
	 * @param carBizId 车辆所在组织List集合
	 * @return 排班信息集合
	 */
	public Map<String,Map<String,Map<String,Map<String,Object>>>> cardispatchDutyList(Map<String, String> requestParamMap);

	public void txupdateDuty(Map para_map);

	public boolean deleteDuty(Map param_map);
	
	/**
	 * 判断是否当天排班
	 * @param paramMap 参数集合
	 * @return
	 */
	public boolean checkIsPlanDutyToday(Map<String,String> paramMap);
}
