package com.iscreate.op.service.staffduty;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.staffduty.Skill;
import com.iscreate.op.pojo.staffduty.Staffskill;

public interface StaffSkillService {
	/**
	 * 条件获取人员列表
	 * @param conditions
	 * @return
	 */
	public List<Map> getStaffListByConditionsServiceForStaffSkill(Map conditions);
	
	/**
	 * 根据帐号获取人员技能列表
	 * @param account 人员帐号
	 * @return
	 */
	public List<Map> getStaffSkillListByAccount(String account);
	
	/**
	 * 获取所有技能信息
	 * @return
	 */
	public List<Skill> getAllSkillInfo();
	
	/**
	 * 添加人员技能
	 * @param sk
	 */
	public boolean addStaffSkill(Staffskill sk);
	
	/**
	 * 判断是否为相同人员技能
	 * @param sk
	 */
	public boolean checkIsSameSkill(Staffskill sk);

	/**
	 * 删除人员技能
	 * @param staffSkillId 人员技能Id
	 */
	public void deleteStaffSkill(long staffSkillId);
}
