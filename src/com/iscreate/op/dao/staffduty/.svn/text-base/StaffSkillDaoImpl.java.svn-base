package com.iscreate.op.dao.staffduty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.pojo.organization.Staff;
import com.iscreate.op.pojo.staffduty.Skill;
import com.iscreate.op.pojo.staffduty.Staffskill;

public class StaffSkillDaoImpl implements StaffSkillDao {
	private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * 根据帐号获取人员技能列表
	 * @param account 人员帐号
	 * @return
	 */
	public List<Staffskill> getStaffSkillListByAccount(String account) {
		String hql = "from Staffskill where staffAccount=?";
		return hibernateTemplate.find(hql, account);
	}

	/**
	 * 根据Id获取技能信息
	 * @param skillId
	 * @return
	 */
	public Skill getSkillById(long skillId) {
		return hibernateTemplate.get(Skill.class, skillId);
	}

	/**
	 * 获取所有技能信息
	 * @return
	 */
	public List<Skill> getAllSkillInfo() {
		return hibernateTemplate.find("from Skill");
	}

	/**
	 * 添加人员技能
	 * @param sk
	 */
	public void addStaffSkill(Staffskill sk) {
		hibernateTemplate.save(sk);
	}

	/**
	 * 条件获取人员技能配对
	 * @param sk
	 * @return
	 */
	public Staffskill getStaffSkillByConditions(Staffskill sk) {
		Staffskill res = null;
		String hql = "from Staffskill where staffAccount=? and skillId=? and experienceYear=? and  skillGrade=?";
		List<Staffskill> resList = hibernateTemplate.find(hql, sk.getStaffAccount(),sk.getSkillId(),sk.getExperienceYear(),sk.getSkillGrade());
		if(resList!=null && !resList.isEmpty()){
			res = resList.get(0); 
		}
		return res;
	}

	/**
	 * 删除人员技能
	 * @param staffSkillId 人员技能Id
	 */
	public void deleteStaffSkill(long staffSkillId) {
		String hql = "from Staffskill where  id=? ";
		List<Staffskill> resList = hibernateTemplate.find(hql,staffSkillId);
		if(resList!=null && !resList.isEmpty()){
			for (Staffskill staffskill : resList) {
				hibernateTemplate.delete(staffskill);
			}
		}
	}

	/**
	 * 按条件获取人员列表 
	 * @param conditions 条件集
	 * @return
	 */
	public List<Staff> getStaffListByConditions(Map conditions) {
		String hql = "select s from Staff s,SysUserRelaOrg r,SysAccount sa ";
		String staffName = (String)conditions.get("staffName");
		String staffSex = (String)conditions.get("staffSex");
		String skillId = (String)conditions.get("skillId");
		String skillGrade = (String)conditions.get("skillGrade");
		String skillYear = (String)conditions.get("skillYear");
		String contactPhone = (String)conditions.get("contactPhone");
		String orgId = (String)conditions.get("orgId");
		if(!StringUtil.isNullOrEmpty(skillId) || !StringUtil.isNullOrEmpty(skillGrade) || !StringUtil.isNullOrEmpty(skillYear) ){
			hql+=",Staffskill ss where s.account=ss.staffAccount ";
		}else{
			hql+="where 1=1 ";
		}
		hql+="and  s.account=sa.account and r.orgUserId = sa.orgUserId ";
		if(!StringUtil.isNullOrEmpty(orgId)){
			hql+="and r.orgId in ("+orgId+") ";
		}
		if(!StringUtil.isNullOrEmpty(staffName)){
			if(!StringUtil.isNullOrEmpty(staffName.trim())){
				hql+="and instr(s.name,'"+staffName+"')>0 ";
			}
			
		}
		if(!StringUtil.isNullOrEmpty(staffSex)){
			hql+="and s.sex='"+staffSex+"' ";
		}
		if(!StringUtil.isNullOrEmpty(skillId)){
			hql+="and ss.skillId="+Long.valueOf(skillId)+" ";
		}
		if(!StringUtil.isNullOrEmpty(skillGrade)){
			hql+="and ss.skillGrade='"+skillGrade+"' ";
		}
		if(!StringUtil.isNullOrEmpty(skillYear)){
			hql+="and ss.experienceYear in("+skillYear+") ";
		}
		if(!StringUtil.isNullOrEmpty(contactPhone)){
			if(!StringUtil.isNullOrEmpty(contactPhone.trim())){
				hql+="and instr(s.contactPhone,'"+contactPhone+"')>0  ";
			}
			
		}
		return hibernateTemplate.find(hql);
	}
	
	
	
	
	
}
