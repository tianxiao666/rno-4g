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

public interface CardispatchCarLocusService extends BaseService<CardispatchWorkorder>{

	/**
	 * 根据WOID获取车辆工单信息
	 * @param woId 
	 * @return
	 */
	public Map getCarWorkOrderInfoService(String woId);
	
	/**
	 * 根据车辆牌照&采集时间获取轨迹信息
	 * @param carNumber 车辆牌照
	 * @param pickTime 采集时间
	 * @return
	 */
	public Map getGpsInfoByPickTime(String carNumber,String beginTime,String endTime,String asc);
}