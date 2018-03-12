package com.iscreate.op.dao.cardispatch;

import java.util.List;
import java.util.Map;

import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.action.informationmanage.common.DBUtil.DBWhereCallBack;
import com.iscreate.op.dao.informationmanage.BaseDao;
import com.iscreate.op.pojo.cardispatch.CardispatchCar;

public interface CardispatchGpsDao  extends BaseDao<CardispatchCar> {

	/**
	 * 根据车牌查询时间内的里程读数
	 * @param carNumber - 车牌号(null)
	 * @param date - 日期(null)
	 * @param startTime - 起始时间(null)
	 * @param endTime - 结束时间(null)
	 * @return (List<Map<String,String>>) 车辆里程信息
	 */
	public abstract List<Map<String,String>> findCarGpsInDateTime(String carNumber,String startTime, String endTime);
	
	public abstract List<Map<String,String>> findAllCarGpsMileage(String bizId,List<String> date_list,String startHour, String endHour);

	
	
	
}
