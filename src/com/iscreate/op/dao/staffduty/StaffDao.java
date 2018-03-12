package com.iscreate.op.dao.staffduty;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.organization.Staff;

public interface StaffDao {
	/**
	 * 条件获取人员列表
	 * @param orgId 组织架构Id
	 * @param staffName 人员姓名
	 * @return
	 */
	public List<Staff> getStaffListByConditions(String orgId,String staffName);
	
	/**
	 * 按条件获取人员列表
	 * @param skillId 技能Id
	 * @param experienceAge 工作年限
	 * @param sex 性别
	 * @param startDutyTime 值班开始时间
	 * @param endDutyTime 值班结束时间
	 * @return
	 */
	public List<Staff> getStaffListByConditions(String skillId,String experienceAge,String sex,String startDutyTime,String endDutyTime);
	
	/**
	 * 执行查询操作
	 * @param hql
	 * @return
	 */
	public List executeHqlFind(String hql);
}
