package com.iscreate.op.dao.staffduty;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.organization.Staff;
import com.iscreate.op.pojo.staffduty.Skill;
import com.iscreate.op.pojo.staffduty.Staffskill;

public interface StaffSkillDao {
	/**
	 * 根据帐号获取人员技能列表
	 * @param account
	 * @return
	 */
	public List<Staffskill> getStaffSkillListByAccount(String account);
	
	/**
	 * 按条件获取人员列表
	 * @param conditions 条件集
	 * @return
	 */
	public List<Staff> getStaffListByConditions(Map conditions);
	
	/**
	 * 根据Id获取技能信息
	 * @param skillId
	 * @return
	 */
	public Skill getSkillById(long skillId);
	
	/**
	 * 获取所有技能信息
	 * @return
	 */
	public List<Skill> getAllSkillInfo();
	
	/**
	 * 添加人员技能
	 * @param sk
	 */
	public void addStaffSkill(Staffskill sk);
	
	/**
	 * 条件获取人员技能配对
	 * @param sk
	 * @return
	 */
	public Staffskill getStaffSkillByConditions(Staffskill sk);

	/**
	 * 删除人员技能
	 * @param staffSkillId 人员技能Id
	 */
	public void deleteStaffSkill(long staffSkillId);
}
