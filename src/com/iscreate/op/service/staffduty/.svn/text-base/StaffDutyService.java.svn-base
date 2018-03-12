package com.iscreate.op.service.staffduty;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.staffduty.StaffdutyDutyinst;
import com.iscreate.op.pojo.staffduty.StaffdutyDutylog;
import com.iscreate.op.pojo.staffduty.StaffdutyDutytemplate;
import com.iscreate.op.pojo.staffduty.StaffdutyFrequency;

public interface StaffDutyService {
	/**
	 * 获取人员值班信息
	 * @param staffId 人员Id
	 * @param dutyDate 值班日期
	 * @param frequencyId 班次
	 * @return
	 */
	public Map getStaffDutyInfo(String staffId,String dutyDate,String frequencyId);
	
	
	/**
	 * 开始值班
	 * @param dutyDate 值班日期
	 * @param dutyInst 值班信息
	 * @return
	 */
	public void startDuty(String dutyDate,StaffdutyDutyinst dutyInst);
	
	
	/**
	 * 添加工作活动日志
	 * @param dutyLog
	 * @return
	 */
	public void addWorkLog(StaffdutyDutylog dutyLog);
	
	
	
	/**
	 * 指定用户下班
	 * @param dutyInst
	 * @return
	 */
	public void offDuty(StaffdutyDutyinst dutyInst);
	
	/**
	 * 获取当前时间对应班别
	 * @return
	 */
	public StaffdutyFrequency getCurrentFrequency();
	
	/**
	 * 获取某一天的值班工作日志
	 * @param dutyDate 值班日期
	 * @return
	 */
	public List<StaffdutyDutylog> getStaffDutyLogByDutyDate(String dutyDate);
	
	/**
	 * 判断当天是否排班
	 * @param accountId
	 * @return
	 */
	public boolean checkIsDutyToday(String accountId);
	
	/**
	 * 获取某天排班模版信息
	 * @param dutyTemplateId
	 * @return
	 */
	public StaffdutyDutytemplate getDutyTemplateById(String dutyTemplateId);
}
