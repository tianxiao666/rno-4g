package com.iscreate.op.dao.cardispatch;

import java.util.List;
import java.util.Map;

import com.iscreate.op.action.informationmanage.common.DBUtil;
import com.iscreate.op.dao.informationmanage.BaseDao;
import com.iscreate.op.pojo.cardispatch.CardispatchOndutydrivercarpair;

public interface CardispatchDutyDao extends BaseDao<CardispatchOndutydrivercarpair> {

	/**
	 * 根据业务单元、日期、账号,获取条件符合的排班信息
	 * @param dutyDate 日期List集合
	 * @param carId 车辆id
	 * @param freId 班次List集合
	 * @param carBizId 车辆所在组织List集合
	 * @return 排班信息集合
	 */
	public List<Map<String,Object>> getCarDutyList(Map param_map);

	public List<Map<String,Object>> findCarDutyFreq();

	public abstract boolean findDutyExists(String dutyDate, String freId, String carId);

}
