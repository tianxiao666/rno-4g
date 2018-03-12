package com.iscreate.op.service.staffduty;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.iscreate.op.action.informationmanage.common.ObjectUtil;
import com.iscreate.op.constant.WorkManageConstant;
import com.iscreate.op.dao.staffduty.StaffDao;
import com.iscreate.op.pojo.organization.Staff;
import com.iscreate.op.pojo.system.SysAccount;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.pojo.system.SysRole;
import com.iscreate.op.service.system.SysAccountService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.op.service.system.SysRoleService;
import com.iscreate.op.service.workmanage.WorkManageService;
import com.iscreate.plat.location.pojo.PdaGpsLocation;
import com.iscreate.plat.location.service.PdaGpsService;
import com.iscreate.plat.tools.TimeFormatHelper;

public class StaffServiceImpl implements StaffService{
	
	private StaffDao staffDao;
	private WorkManageService workManageService;
	private PdaGpsService pdaGpsService;
	private StaffSkillService staffSkillService;
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	private SysRoleService sysRoleService;
	
	//ou.jh
	private SysOrgUserService sysOrgUserService;
	
	private SysAccountService sysAccountService;
	
	

	public SysAccountService getSysAccountService() {
		return sysAccountService;
	}

	public void setSysAccountService(SysAccountService sysAccountService) {
		this.sysAccountService = sysAccountService;
	}

	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}

	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}

	public SysRoleService getSysRoleService() {
		return sysRoleService;
	}

	public void setSysRoleService(SysRoleService sysRoleService) {
		this.sysRoleService = sysRoleService;
	}

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	
	public StaffDao getStaffDao() {
		return staffDao;
	}
	public void setStaffDao(StaffDao staffDao) {
		this.staffDao = staffDao;
	}
	
	public WorkManageService getWorkManageService() {
		return workManageService;
	}

	public void setWorkManageService(WorkManageService workManageService) {
		this.workManageService = workManageService;
	}
	public PdaGpsService getPdaGpsService() {
		return pdaGpsService;
	}
	public void setPdaGpsService(PdaGpsService pdaGpsService) {
		this.pdaGpsService = pdaGpsService;
	}
	public StaffSkillService getStaffSkillService() {
		return staffSkillService;
	}
	public void setStaffSkillService(StaffSkillService staffSkillService) {
		this.staffSkillService = staffSkillService;
	}
	
	static List<String> allStaffList = new ArrayList<String>();
	
	/**
	 * 根据条件获取人员列表
	 * @param conditions 查询条件集合
	 * @return
	 */
	public List<Map> getStaffListByConditionsService(Map conditions) {
		HttpServletRequest request = ServletActionContext.getRequest();
		List<Map> resultList = new ArrayList<Map>();
		String searchType = (String)conditions.get("searchType");
		if(searchType!=null && searchType.equals("all")){
			List<Staff> resList = new ArrayList<Staff>();
			//默认查询全部人员信息
			String userId = (String) request.getSession().getAttribute("userId");
			//List<ProviderOrganization> topOrgList = providerOrganizationService.getTopLevelOrgByAccount(userId);
			List<SysOrg> topOrgList = this.sysOrganizationService.getTopLevelOrgByAccount(userId);

			if(topOrgList!=null && !topOrgList.isEmpty()){
				for (SysOrg org : topOrgList) {
					//List<ProviderOrganization> subOrgList = providerOrganizationService.getOrgListDownwardByOrg(org.getId());
					//yuan.yw
					List<SysOrg> subOrgList = this.sysOrganizationService.getOrgListDownwardByOrg(org.getOrgId());
					if(subOrgList!=null && !subOrgList.isEmpty()){
						for (SysOrg subOrg : subOrgList) {
							//ou.jh
							List<Map<String,Object>> userByOrgId = this.sysOrgUserService.getUserByOrgId(subOrg.getOrgId());
							//List<Staff> staffList = providerOrganizationService.getStaffListByOrgIdService(subOrg.getOrgId());
							if(userByOrgId!=null && !userByOrgId.isEmpty()){
								/*获取人员技能信息&任务数*/
								for (Map<String,Object> staff : userByOrgId) {
									Map staffMap = this.getStaffInfo(staff);
									if(!resultList.contains(staffMap)){
										resultList.add(staffMap);
										allStaffList.add(staff.get("account")+"");
									}
								}
							}
						}
					}
				}
			}
			
		}else if(searchType!=null && searchType.equals("simple")){
			//普通查询
			String orgId = (String)conditions.get("orgId");
			String staffName = (String)conditions.get("staffName");
			//List<ProviderOrganization> subOrgList = providerOrganizationService.getOrgListDownwardByOrg(Long.valueOf(orgId));
			//yuan.yw
			List<SysOrg> subOrgList = this.sysOrganizationService.getOrgListDownwardByOrg(Long.valueOf(orgId));
			if(subOrgList!=null && !subOrgList.isEmpty()){
				for (SysOrg subOrg : subOrgList) {
					orgId +=","+subOrg.getOrgId();
				}
			}
			List<Staff> staffList = staffDao.getStaffListByConditions(orgId, staffName);
			if(staffList!=null && !staffList.isEmpty()){
				for (Staff staff : staffList) {
					Map<String, Object> map2 = null;
					try {
						map2 = ObjectUtil.object2Map(staff, false);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					Map staffMap = this.getStaffInfo(map2);
					if(!resultList.contains(staffMap)){
						resultList.add(staffMap);
					}
				}
			}
		}else{
			//高级搜索
			String skillId = (String)conditions.get("skillId");
			String experienceAge = (String)conditions.get("experienceAge");
			String sex = (String)conditions.get("sex");
			String startDutyTime = (String)conditions.get("startDutyTime");
			String endDutyTime = (String)conditions.get("endDutyTime");
			List<Staff> staffList = staffDao.getStaffListByConditions(skillId, experienceAge, sex, startDutyTime, endDutyTime);
			if(staffList!=null && !staffList.isEmpty()){
				for (Staff staff : staffList) {
					if(allStaffList.contains(staff.getAccount())){
						Map<String, Object> map2 = null;
						try {
							map2 = ObjectUtil.object2Map(staff, false);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
						Map staffMap = this.getStaffInfo(map2);
						if(!resultList.contains(staffMap)){
							resultList.add(staffMap);
						}
					}
					
				}
			}
		}
		return resultList;
	}
	

	
	/**
	 * 获取人员信息（技能、任务数、经纬度）
	 * @param staff
	 * @return
	 */
	private Map getStaffInfo(Map<String, Object> staff){
		Map staffMap = new HashMap();
		String staffId = staff.get("account")+"";
		try {
			//Map<String, Object> map2 = ObjectUtil.object2Map(staff, false);
			staffMap.putAll(staff);
			staffMap.remove("accountObj");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		//获取人员技能信息
		List<Map> staffSkillList = staffSkillService.getStaffSkillListByAccount(staffId);
		if(staffSkillList!=null && !staffSkillList.isEmpty()){
			String skillStr = "";
			for (Map map : staffSkillList) {
				String str = (String)map.get("skillType");
				skillStr+=str+"/";
			}
			if(skillStr!=""){
				skillStr = skillStr.substring(0,skillStr.length()-1);
			}
			staffMap.put("skillStr", skillStr);
		}
		//获取人员任务信息
		long taskCount = workManageService.getTaskOrderCountByObjectTypeAndObjectId("people", staffId);
		//获取人员经纬度
		List<PdaGpsLocation> gpsList = pdaGpsService.getGpsLocationByUserIdService(staffId);
		if(gpsList!=null && !gpsList.isEmpty()){
			PdaGpsLocation gps = gpsList.get(0);
			staffMap.put("latitude", gps.getLatitude());
			staffMap.put("longitude", gps.getLongitude());
		}
		staffMap.put("taskCount", taskCount);
		return staffMap;
	}
	
	/**
	 * 获取人员基本信息（经纬度、信息、所属组织等）
	 * @param account 人员帐号
	 * @return
	 */
	public Map getStaffBaseInfoService(String account) {
		Map staffMap = new HashMap();
		//获取人员基本信息
		//ou.jh
		SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(account);
		SysAccount sysAccountByAccount = this.sysAccountService.getSysAccountByAccount(account);

//		Staff staff = providerOrganizationService.getStaffByAccount(account);
		if(sysOrgUserByAccount!=null && sysAccountByAccount != null){
//			staffMap.put("sex", staff.getSex());
			staffMap.put("name", sysOrgUserByAccount.getName());
			staffMap.put("account", sysAccountByAccount.getAccount());
			staffMap.put("identityCard", sysOrgUserByAccount.getIdcard());
			staffMap.put("birthday", sysOrgUserByAccount.getBirthday());
			staffMap.put("cellPhoneNumber", sysOrgUserByAccount.getMobile());
//			Account accountObj = staff.getAccountObj();
//			if(accountObj!=null){
				staffMap.put("email", sysOrgUserByAccount.getEmail());
//			}
			//获取用户所属组织信息
			//List<ProviderOrganization> topOrgList = providerOrganizationService.getTopLevelOrgByAccount(account);
			List<SysOrg> topOrgList = this.sysOrganizationService.getTopLevelOrgByAccount(account);

			if(topOrgList!=null && !topOrgList.isEmpty()){
				SysOrg org = topOrgList.get(0);
				staffMap.put("orgId", org.getOrgId());
				staffMap.put("orgName", org.getName());
				staffMap.put("orgAddress", org.getAddress());
			}
			//获取人员拥有角色
			List<SysRole> userRoles = this.sysRoleService.getUserRoles(account);
//			List<Map<String, Object>> staffRoleList = providerOrganizationService.getOrgBizRoleByAccountAndRoleTypeCodeService(account);
			staffMap.put("staffRoleList", userRoles);
			//获取人员技能信息
			List<Map> staffSkillList = staffSkillService.getStaffSkillListByAccount(account);
			if(staffSkillList!=null && !staffSkillList.isEmpty()){
				String skillStr = "";
				for (Map map : staffSkillList) {
					String str = (String)map.get("skillType");
					skillStr+=str+"|";
				}
				if(skillStr!=""){
					skillStr = skillStr.substring(0,skillStr.length()-1);
				}
				staffMap.put("skillStr", skillStr);
			}
		}
		//获取人员GPS位置
		List<PdaGpsLocation> gpsList = pdaGpsService.getGpsLocationByUserIdService(account);
		if(gpsList!=null && !gpsList.isEmpty()){
			PdaGpsLocation gps = gpsList.get(0);
			staffMap.put("latitude", gps.getLatitude());
			staffMap.put("longitude", gps.getLongitude());
		}
		return staffMap;
	}
	/**
	 * 获取人员任务信息(“完成 未完成”，”抢修、巡检、车辆“)
	 * @param account 人员帐号
	 * @param conditions 查询条件集
	 * @return
	 */
	public List<Map> getStaffTaskInfoService(String account, Map conditions) {
		List<Map> resList = new ArrayList<Map>();
		String taskStatus = (String)conditions.get("taskStatus");
		String taskType = (String)conditions.get("taskType");
		String beginTime = (String)conditions.get("beginTime");
		String endTime = (String)conditions.get("endTime");
		String toTitle = (String) conditions.get("toTitle");
		String conditionString = "";
		
		if(taskType.equals("cardispatch")){
			if(taskStatus.equals("pending")){
				//获取未 还车工单 woTitle
				conditionString+=" and \"status\" = "+WorkManageConstant.WORKORDER_WAIT4RETURNCAR;
			}else if(taskStatus.equals("hasDone")){
				conditionString+=" and \"status\" = "+WorkManageConstant.WORKORDER_END;
			}
			if(beginTime!=null && !"".equals(beginTime)){
				if(endTime==null || "".equals(endTime)){
					endTime = TimeFormatHelper.getTimeFormatByFree(new Date(), "yyyy-MM-dd HH:mm:ss");
				}
				conditionString+= " and \"createTime\"  > to_date('"+beginTime+ "','yyyy-mm-dd hh24:mi:ss') and \"createTime\" < to_date('"+endTime+ "','yyyy-mm-dd hh24:mi:ss')";
			}
			if(toTitle!=null && !"".equals(toTitle)){
				conditionString+=" and \"woTitle\" like '%"+toTitle+"%'";
			}
		}else if(taskType.equals("urgentrepair")){
			if(taskStatus.equals("pending")){
				conditionString+=" and \"status\" <> "+WorkManageConstant.TASKORDER_CLOSED ;
			}else if(taskStatus.equals("hasDone")){
				conditionString+=" and \"status\" = "+WorkManageConstant.TASKORDER_CLOSED;
			}
			if(beginTime!=null && !"".equals(beginTime)){
				if(endTime==null || "".equals(endTime)){
					endTime = TimeFormatHelper.getTimeFormatByFree(new Date(), "yyyy-MM-dd HH:mm:ss");
				}
				conditionString+= " and \"createTime\"  > to_date('"+beginTime+ "','yyyy-mm-dd hh24:mi:ss') and \"createTime\" < to_date('"+endTime+ "','yyyy-mm-dd hh24:mi:ss')";
			}
			if(toTitle!=null && !"".equals(toTitle)){
				conditionString+=" and \"toTitle\" like '%"+toTitle+"%'";
			}
		}else if(taskType.equals("xunjian")){
			
		}else if(taskType.equals("all")){
			String conditionsStr = "";
			
			if(taskStatus.equals("pending")){
				conditionString+=" and \"status\" <> "+WorkManageConstant.TASKORDER_CLOSED ;
				conditionsStr+=" and \"status\" = "+WorkManageConstant.WORKORDER_WAIT4RETURNCAR;
			}else if(taskStatus.equals("hasDone")){
				conditionString+=" and \"status\" = "+WorkManageConstant.TASKORDER_CLOSED;
				conditionsStr+=" and \"status\" = "+WorkManageConstant.WORKORDER_END;
			}
			if(beginTime!=null && !"".equals(beginTime)){
				if(endTime==null || "".equals(endTime)){
					endTime = TimeFormatHelper.getTimeFormatByFree(new Date(), "yyyy-MM-dd HH:mm:ss");
				}
				conditionString+= " and \"assignTime\"  > to_date('"+beginTime+ "','yyyy-mm-dd hh24:mi:ss') and \"assignTime\" < to_date('"+endTime+ "','yyyy-mm-dd hh24:mi:ss')";
				conditionsStr += " and \"createTime\"  > to_date('"+beginTime+ "','yyyy-mm-dd hh24:mi:ss') and \"createTime\" < to_date('"+endTime+ "','yyyy-mm-dd hh24:mi:ss')";
			}
			if(toTitle!=null && !"".equals(toTitle)){
				conditionString+=" and \"toTitle\" like '%"+toTitle+"%'";
				conditionsStr+=" and \"woTitle\" like '%"+toTitle+"%'";
			}
			//获取用户车辆工单信息
			Map<String, Object> userTaskOrders = workManageService.getUserTaskOrdersForStaffInfo(account, "cardispatch", null, null, conditionsStr);
			if(userTaskOrders!=null){
				List<Map> list = (List<Map>)userTaskOrders.get("entityList");
				if(list!=null && !list.isEmpty()){
					resList.addAll(list);
				}
			}
		}
		
		
		//获取用户任务单信息
		Map<String, Object> userTaskOrders = workManageService.getUserTaskOrdersForStaffInfo(account, taskType, null, null, conditionString);
		if(userTaskOrders!=null){
			List<Map> list = (List<Map>)userTaskOrders.get("entityList");
			if(list!=null && !list.isEmpty()){
				resList.addAll(list);
			}
		}
		if(resList != null && !resList.isEmpty()){
			for(Map m:resList){
				Date assignTime = null;
				if(m.get("assignTime") != null){
					assignTime = TimeFormatHelper.setTimeFormat(m.get("assignTime"));
					m.put("assignTime", TimeFormatHelper.getTimeFormatBySecond(assignTime));
				}if(m.get("createTime") != null){
					assignTime = TimeFormatHelper.setTimeFormat(m.get("createTime"));
					m.put("createTime", TimeFormatHelper.getTimeFormatBySecond(assignTime));
				}
				Date finalCompleteTime = null;
				if(m.get("finalCompleteTime") != null){
					finalCompleteTime = TimeFormatHelper.setTimeFormat(m.get("finalCompleteTime"));
					m.put("finalCompleteTime", TimeFormatHelper.getTimeFormatBySecond(finalCompleteTime));
				}Date requireCompleteTime = null;
				if(m.get("requireCompleteTime") != null){
					requireCompleteTime = TimeFormatHelper.setTimeFormat(m.get("requireCompleteTime"));
					m.put("requireCompleteTime", TimeFormatHelper.getTimeFormatBySecond(requireCompleteTime));
				}
				if(assignTime != null && finalCompleteTime != null){
					
					float diff = finalCompleteTime.getTime() - assignTime.getTime();
					float hour = diff / (1000 * 60);
					 DecimalFormat df = new DecimalFormat("0.0");//这里是设定小数位数
					 String e = df.format(1 * hour);
					 Float valueOf = Float.valueOf(e);
					 m.put("takeTime", valueOf);
				}
				
			}
		}
		return resList;
	}
	
}
