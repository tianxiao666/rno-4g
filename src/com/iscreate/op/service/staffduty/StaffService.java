package com.iscreate.op.service.staffduty;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.organization.Staff;

public interface StaffService {
	/**
	 * 根据条件获取人员列表
	 * @param conditions 查询条件集合
	 * @return
	 */
	public List<Map> getStaffListByConditionsService(Map conditions);
	
	/**
	 * 获取人员基本信息（经纬度、信息、所属组织等）
	 * @param account 人员帐号
	 * @return
	 */
	public Map getStaffBaseInfoService(String account);
	
	/**
	 * 获取人员任务信息(“完成 未完成”，”抢修、巡检、车辆“)
	 * @param account 人员帐号
	 * @param conditions 查询条件集
	 * @return
	 */
	public List<Map> getStaffTaskInfoService(String account,Map conditions);
}
