package com.iscreate.op.service.staffduty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.informationmanage.common.ObjectUtil;
import com.iscreate.op.dao.staffduty.StaffSkillDao;
import com.iscreate.op.pojo.organization.Staff;
import com.iscreate.op.pojo.staffduty.Skill;
import com.iscreate.op.pojo.staffduty.Staffskill;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.system.SysOrganizationService;

public class StaffSkillServiceImpl implements StaffSkillService {
	private StaffSkillDao staffSkillDao;
	
	//ou.jh
	private SysOrgUserService sysOrgUserService;
	
	
	
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	public StaffSkillDao getStaffSkillDao() {
		return staffSkillDao;
	}

	public void setStaffSkillDao(StaffSkillDao staffSkillDao) {
		this.staffSkillDao = staffSkillDao;
	}

	
	
	/**
	 * 条件获取人员列表
	 * @param conditions
	 * @return
	 */
	public List<Map> getStaffListByConditionsServiceForStaffSkill(Map conditions) {
		String orgId = (String)conditions.get("orgId");
		//返回结果列表
		List<Map> resList = new ArrayList<Map>();
		//默认获取全部列表
		if(orgId!=null && "all".equals(orgId)){
			String userId = (String)SessionService.getInstance().getValueByKey("userId");
			//List<ProviderOrganization> topOrgList = providerOrganizationService.getTopLevelOrgByAccount(userId);
			List<SysOrg> topOrgList = this.sysOrganizationService.getTopLevelOrgByAccount(userId);

			if(topOrgList!=null && !topOrgList.isEmpty()){
				for (SysOrg topOrg : topOrgList) {
					//获取子级组织架构
					//List<ProviderOrganization> subOrgList = providerOrganizationService.getOrgListDownwardByOrg(topOrg.getId());
					List<SysOrg> subOrgList = this.sysOrganizationService.getOrgListDownwardByOrg(topOrg.getOrgId());
					if(subOrgList!=null && !subOrgList.isEmpty()){
						for (SysOrg subOrg : subOrgList) {
							//ou.jh
							List<Map<String,Object>> userByOrgId = this.sysOrgUserService.getUserByOrgId(subOrg.getOrgId());
//							List<Staff> subStaffList = providerOrganizationService.getStaffListByOrgIdService(subOrg.getOrgId());
							if(userByOrgId!=null && !userByOrgId.isEmpty()){
								for (Map<String, Object> staff : userByOrgId) {
									Map map = new HashMap();
									//获取人员技能信息
									String account = staff.get("account")+"";
									try {
//										Map<String, Object> m = ObjectUtil.object2Map(staff, false);
										map.putAll(staff);
									} catch (Exception e) {
										e.printStackTrace();
									}
									List<Map> staffSkillList = this.getStaffSkillListByAccount(account);
									map.put("staffSkillList", staffSkillList);
									if(!resList.contains(map)){
										map.remove("accountObj");
										resList.add(map);
									}
								}
							}
						}
					}
				}
			}
		}else{
			//符合查询条件集的List
			//List<ProviderOrganization> subOrgList = providerOrganizationService.getOrgListDownwardByOrg(Long.valueOf(orgId));
			//yuan.yw
			List<SysOrg> subOrgList = this.sysOrganizationService.getOrgListDownwardByOrg(Long.valueOf(orgId));
			if(subOrgList!=null && !subOrgList.isEmpty()){
				for (SysOrg subOrg : subOrgList) {
					orgId +=","+subOrg.getOrgId();
				}
			}
			conditions.put("orgId", orgId);
			List<Staff> accountList = staffSkillDao.getStaffListByConditions(conditions);
			if(accountList!=null && !accountList.isEmpty()){
				for (Staff staff : accountList) {
					Map map = new HashMap();
					//获取人员技能信息
					String account = staff.getAccount();
					try {
						Map<String, Object> m = ObjectUtil.object2Map(staff, false);
						map.putAll(m);
					} catch (Exception e) {
						e.printStackTrace();
					}
					List<Map> staffSkillList = this.getStaffSkillListByAccount(account);
					map.put("staffSkillList", staffSkillList);
					if(!resList.contains(map)){
						resList.add(map);
					}
				}
			}
		}
		return resList;
	}

	/**
	 * 根据帐号获取人员技能列表
	 * @param account 人员帐号
	 * @return
	 */
	public List<Map> getStaffSkillListByAccount(String account) {
		List<Map> resList = new ArrayList<Map>();
		//获取人员技能列表
		List<Staffskill> staffSkill = staffSkillDao.getStaffSkillListByAccount(account);
		if(staffSkill!=null && !staffSkill.isEmpty()){
			for (Staffskill ss : staffSkill) {
				Map map = new HashMap();
				try {
					Map<String, Object> m = ObjectUtil.object2Map(ss, false);
					map.putAll(m);
					map.put("staffSkillId",ss.getId());
					Long skillId = ss.getSkillId();
					//获取技能信息
					Skill skill = staffSkillDao.getSkillById(skillId);
					if(skill!=null){
						Map<String, Object> m2 = ObjectUtil.object2Map(skill, false);
						map.putAll(m2);
						map.put("skillId", skillId);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 
				resList.add(map);
			}
		}
		return resList;
	}

	/**
	 * 获取所有技能信息
	 * @return
	 */
	public List<Skill> getAllSkillInfo() {
		return staffSkillDao.getAllSkillInfo();
	}

	/**
	 * 添加人员技能
	 */
	public boolean addStaffSkill(Staffskill sk) {
		boolean checkRes = this.checkIsSameSkill(sk);
		if(checkRes){
			return false;
		}
		staffSkillDao.addStaffSkill(sk);
		return true;
	}

	/**
	 * 判断是否为相同人员技能
	 * @param sk
	 */
	public boolean checkIsSameSkill(Staffskill sk) {
		Staffskill sk2 = staffSkillDao.getStaffSkillByConditions(sk);
		if(sk2!=null){
			return true;
		}
		return false;
	}

	/**
	 * 删除人员技能
	 * @param staffSkillId 人员技能Id
	 */
	public void deleteStaffSkill(long staffSkillId) {
		staffSkillDao.deleteStaffSkill(staffSkillId);
	}

	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}

	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}
	
	
	
	
}
