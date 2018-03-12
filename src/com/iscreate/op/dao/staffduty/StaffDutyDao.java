package com.iscreate.op.dao.staffduty;

import java.util.List;

import com.iscreate.op.pojo.staffduty.StaffdutyDutyinst;
import com.iscreate.op.pojo.staffduty.StaffdutyDutylog;
import com.iscreate.op.pojo.staffduty.StaffdutyDutytemplate;
import com.iscreate.op.pojo.staffduty.StaffdutyFrequency;

public interface StaffDutyDao {
	
	/**
	 * 根据值班Id获取人员值班信息
	 * @param dutyInstId
	 * @return
	 */
	public StaffdutyDutyinst getStaffDutyInfoById(String dutyInstId);
	
	/**
	 * 通过排班Id获取人员值班信息
	 * @param dutyTemplateId 排班Id
	 * @return
	 */
	public StaffdutyDutyinst getStaffDutyInfoByDutyTemplateId(String dutyTemplateId);
	
	/**
	 * 获取人员排班信息
	 * @param userId 人员Id
	 * @param dutyDate 排班日期
	 * @param frequencyId 排班班别Id
	 * @return
	 */
	public StaffdutyDutytemplate getStaffDutyInfo(String userId,String dutyDate,String frequencyId);
	
	/**
	 * 开始值班
	 * @param dutyInst
	 * @return
	 */
	public void startDuty(StaffdutyDutyinst dutyInst);
	
	/**
	 * 下班
	 * @param dutyInst
	 * @return
	 */
	public void offDuty(StaffdutyDutyinst dutyInst);
	
	/**
	 * 添加工作日志
	 * @param dutyLog
	 * @return
	 */
	public void addWorkLog(StaffdutyDutylog dutyLog);
	
	/**
	 * 获取当前班别
	 * @return
	 */
	public StaffdutyFrequency getCurrentFrequency();
	
	/**
	 * 通过Id获取班别信息
	 * @param frequencyId
	 * @return
	 */
	public StaffdutyFrequency getFrequencyById(String frequencyId);
	
	/**
	 * 获取某天的值班工作日志
	 * @param dutyDate
	 * @return
	 */
	public List<StaffdutyDutylog> getStaffDutyLogByDutyDate(String dutyDate);
	/**
	 * 获取某人某天的值班工作日志
	 * @param dutyDate
	 * @param staffId
	 * @return
	 */
	public List<StaffdutyDutylog> getStaffDutyLogByDutyDate(String dutyDate,String staffId);
	
	/**
	 * 判断当天是否排班
	 * @param accountId
	 * @param dutyDate
	 * @return
	 */
	public boolean checkIsDuty(String accountId,String dutyDate);
	
	/**
	 * 获取某天排班模版信息
	 * @param dutyTemplateId
	 * @return
	 */
	public StaffdutyDutytemplate getDutyTemplateById(String dutyTemplateId);
	
}
