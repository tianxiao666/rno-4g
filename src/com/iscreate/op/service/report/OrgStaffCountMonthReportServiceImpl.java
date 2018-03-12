package com.iscreate.op.service.report;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.action.informationmanage.common.ObjectUtil;
import com.iscreate.op.constant.OrganizationConstant;
import com.iscreate.op.dao.report.OrgStaffCountMonthReportDao;
import com.iscreate.op.dao.system.SysOrganizationDao;
import com.iscreate.op.pojo.organization.Staff;
import com.iscreate.op.pojo.report.ReportOrgStaffCountMonth;
import com.iscreate.op.pojo.report.ReportOrgStaffSkillMonth;
import com.iscreate.op.pojo.report.ReportOrgWorkOrderCountMonth;
import com.iscreate.op.pojo.report.ReportStaffTaskCountMonth;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.service.system.SysOrganizationService;


public class OrgStaffCountMonthReportServiceImpl implements OrgStaffCountMonthReportService{
	
	private OrgStaffCountMonthReportDao orgStaffCountMonthReportDao;
	
	
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	
	private SysOrganizationDao sysOrganizationDao;//du.hw添加

	public SysOrganizationDao getSysOrganizationDao() {
		return sysOrganizationDao;
	}

	public void setSysOrganizationDao(SysOrganizationDao sysOrganizationDao) {
		this.sysOrganizationDao = sysOrganizationDao;
	}

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	
	public OrgStaffCountMonthReportDao getOrgStaffCountMonthReportDao() {
		return orgStaffCountMonthReportDao;
	}

	public void setOrgStaffCountMonthReportDao(
			OrgStaffCountMonthReportDao orgStaffCountMonthReportDao) {
		this.orgStaffCountMonthReportDao = orgStaffCountMonthReportDao;
	}

	

	
	
	/**
	 * 根据组织Id获取人员数量
	 * @param orgId 组织Id
	 * @param beginTime 统计开始时间
	 * @param endTime 统计结束时间
	 * @return
	 */
	public List<Map> getOrgStaffCountByOrgIdService(long orgId,String beginTime, String endTime) {
		List<Map> resultList = new ArrayList<Map>();
		//TODO 组织工单数 应该在数据库统计好，在Service循环调用下级会消耗性能
		int staffCount = 0;
		//List<ProviderOrganization> subOrgList = providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
		List<Map<String,Object>> subOrgList = this.sysOrganizationService.getProviderOrgByOrgIdAndOrgAttrByLevelService(orgId,"BusinessOrganization","downward");

		if(subOrgList!=null && !subOrgList.isEmpty()){
			for (Map<String,Object> subOrg : subOrgList) {
				//获取数据
				List<Map<String, Object>> orgReportList = orgStaffCountMonthReportDao.getOrgStaffCountByOrgId(Long.valueOf(subOrg.get("orgId")+""), beginTime, endTime);
				if(orgReportList!=null && !orgReportList.isEmpty()){
					for (Map<String, Object> org : orgReportList) {
						if(org != null){
							if(org.get("staffCount") != null){
								
								Long count = Long.valueOf(org.get("staffCount").toString());
								staffCount+=count;
							}
						}
					}
				}
			}
		}
		Map map = new HashMap();
		map.put("orgId",orgId);
		map.put("staffCount", staffCount);
		Map<String,Object> orgInfo = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
		
		String orgName = "";
		if(orgInfo!=null){
			orgName = (String) orgInfo.get("name");
		}
		map.put("orgName", orgName);
		resultList.add(map);
		return resultList;
	}

	/**
	 * 根据组织Id获取下级组织人员数量
	 * @param orgId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map> getNextOrgStaffCountReportService(long orgId,String beginTime, String endTime) {
		List<Map> resultList = new ArrayList<Map>();
		//List<ProviderOrganization> subOrgList = providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
		//yuan.yw
		List<Map<String,Object>> subOrgList=this.sysOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
	
		if(subOrgList!=null && !subOrgList.isEmpty()){
			for (Map<String,Object> subOrg : subOrgList) {
				List<Map> subReportList = this.getOrgStaffCountByOrgIdService(Long.valueOf(subOrg.get("orgId")+""), beginTime, endTime);
				if(subReportList!=null && !subReportList.isEmpty()){
					resultList.addAll(subReportList);
				}
			}
		}
		return resultList;
	}

	/**
	 * 获取当前用户组织下人员数统计
	 * @param userId 用户Id
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	public Map getUserOrgStaffrCountReportService(String userId,String beginTime, String endTime) {
		Map resMap = new HashMap();
		List<String> orgNameList = new ArrayList<String>();
		List<Integer> dataList = new ArrayList<Integer>();
		List<String> orgIdList = new ArrayList<String>();
		String name = "人员数";
		boolean canClick = false;
		String orgName = "";
		//获取用户顶级组织架构
		//List<ProviderOrganization> topOrgList = providerOrganizationService.getTopLevelOrgByAccount(userId);
		List<SysOrg> topOrgList = this.sysOrganizationService.getTopLevelOrgByAccount(userId);

		if(topOrgList!=null && !topOrgList.isEmpty()){
			for (SysOrg org : topOrgList) {
				orgName = org.getName();
				List<Map> nextOrgReportList = this.getNextOrgStaffCountReportService(org.getOrgId(), beginTime, endTime);
				if(nextOrgReportList!=null && !nextOrgReportList.isEmpty()){
					for (Map map : nextOrgReportList) {
						dataList.add(Integer.valueOf(map.get("staffCount")+""));
						orgNameList.add(map.get("orgName")+"");
						orgIdList.add(map.get("orgId")+"");
						canClick = true;
					}
				}else{
					//判断当前组织架构，若组织架构为维护队，获取维护队人员数量，不可点击向下钻取
					Map<String,Object> orgInfo = sysOrganizationDao.getOrganizationMessageByOrgId(org.getOrgId());
					
					if(orgInfo!=null && orgInfo.get("type").equals(OrganizationConstant.ORGANIZATION_MAINTENANCETEAM)){
						List<Map> staffCountList = this.getOrgStaffCountByOrgIdService(org.getOrgId(), beginTime, endTime);
						if(staffCountList!=null && !staffCountList.isEmpty()){
							for (Map map : staffCountList) {
								dataList.add(Integer.valueOf(map.get("staffCount")+""));
								orgNameList.add(map.get("orgName")+"");
								orgIdList.add(map.get("orgId")+"");
							}
						}
					}
				}
				resMap.put("userOrgId", org.getOrgId());
			}
		}
		//报表数据
		Map reportMap = new HashMap();
		reportMap.put("name", name);
		reportMap.put("data", dataList);
		//放到返回结果
		resMap.put("reportMap", reportMap);
		resMap.put("orgNameList", orgNameList);
		resMap.put("orgIdList", orgIdList);
		resMap.put("canClick", canClick);
		resMap.put("orgName", orgName);
		return resMap;
	}

	/**
	 * 根据组织Id获取下级组织人员数量报表信息
	 * @param orgId 组织Id
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	public Map getNextOrgStaffCountReportInfoService(long orgId,String beginTime, String endTime) {
		Map resMap = new HashMap();
		List<String> orgNameList = new ArrayList<String>();
		List<Integer> dataList = new ArrayList<Integer>();
		List<String> orgIdList = new ArrayList<String>();
		String name = "人员数";
		boolean canClick = false;
		//默认获取当前组织架构工单数
		List<Map> nextOrgReportList = this.getNextOrgStaffCountReportService(orgId, beginTime, endTime);
		if(nextOrgReportList!=null && !nextOrgReportList.isEmpty()){
			for (Map map : nextOrgReportList) {
				dataList.add(Integer.valueOf(map.get("staffCount")+""));
				orgNameList.add(map.get("orgName")+"");
				orgIdList.add(map.get("orgId")+"");
				canClick = true;
			}
		}
		//报表数据
		Map reportMap = new HashMap();
		reportMap.put("name", name);
		reportMap.put("data", dataList);
		//放到返回结果
		resMap.put("reportMap", reportMap);
		resMap.put("orgNameList", orgNameList);
		resMap.put("orgIdList", orgIdList);
		resMap.put("canClick", canClick);
		resMap.put("userOrgId", orgId);
		String orgName = "";
		Map<String,Object> orgInfo = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
		
		if(orgInfo!=null){
			orgName = (String) orgInfo.get("name");
		}
		resMap.put("orgName",orgName);
		return resMap;
	}

	/**
	 * 获取组织人员数量环比数据
	 * @param orgId 组织Id
	 * @param yearMonthList 年月List
	 * @return
	 */
	public Map getOrgStaffCountChainReportService(long orgId,List<String> yearMonthList) {
		Map resMap = new HashMap();
		List<Integer> dataList = new ArrayList<Integer>();
		List<String> nameList = new ArrayList<String>();
		Map<String,Object> orgInfo = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
		
		String orgName = "";
		if(orgInfo!=null){
			orgName = (String) orgInfo.get("name");
		}
		//根据年月List获取环比数据
		if(yearMonthList!=null && !yearMonthList.isEmpty()){
			int size = yearMonthList.size()-1;
			for(int i=size;i>=0;i--){
				String str = yearMonthList.get(i);
				String beginTime = str+"-01 00:00:00";
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date parse = null;
				try {
					parse = sf.parse(beginTime);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 Calendar calender = Calendar.getInstance();
		          calender.setTime(parse);
		          calender.add(Calendar.MONTH, 1);
				String endTime = sf.format(calender.getTime());
				int staffCount = 0;
				//获取数据
				List<Map> orgStaffCountList = this.getOrgStaffCountByOrgIdService(orgId, beginTime, endTime);
				if(orgStaffCountList!=null && !orgStaffCountList.isEmpty()){
					Map map = orgStaffCountList.get(0);
					staffCount = Integer.valueOf(map.get("staffCount")+"");
				}
				nameList.add(str);
				dataList.add(staffCount);
			}
		}
		//报表数据
		Map reportMap = new HashMap();
		reportMap.put("name", "人员数");
		reportMap.put("data", dataList);
		reportMap.put("type", "line");
		//保存数据
		resMap.put("orgId", orgId);
		resMap.put("orgName", orgName);
		resMap.put("reportMap", reportMap);
		resMap.put("nameList", nameList);
		return resMap;
	}

	/**
	 * 获取组织架构人员技能分类统计信息
	 * @param orgId 组织Id
	 * @param beginTime 统计开始时间
	 * @param endTime 统计结束时间
	 * @return
	 */
	public Map getUserOrgStaffSkillReportService(long orgId,String beginTime, String endTime) {
		Map resMap = new HashMap();
		Map<String,Object> orgInfo = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
		
		String orgName = "";
		if(orgInfo!=null){
			orgName = (String) orgInfo.get("name");
		}
		//获取用户顶级组织架构
		String pieJson = "[";
		List<Map> orgStaffSkillList = this.getOrgStaffSkillReportByOrgIdService(orgId, beginTime, endTime);
		if(orgStaffSkillList!=null && !orgStaffSkillList.isEmpty()){
			for (Map map : orgStaffSkillList) {
				String skillName = map.get("skillName")+"";
				pieJson+="['"+skillName+"',"+map.get("staffCount")+"],";
			}
			pieJson = pieJson.substring(0,pieJson.length()-1);
		}
		resMap.put("userOrgId", orgId);
		pieJson+="]";
		//放到返回结果
		resMap.put("pieJson", pieJson); 
		resMap.put("orgName", orgName);
		return resMap;
	}

	/**
	 * 根据组织架构Id获取人员技能分类统计信息
	 * @param orgId 组织Id
	 * @param beginTime 统计开始时间
	 * @param endTime 统计结束时间
	 * @return
	 */
	public List<Map> getOrgStaffSkillReportByOrgIdService(long orgId,String beginTime, String endTime) {
		List<Map> resList = new ArrayList<Map>();
		List<Map<String, Object>> orgStaffSkillList = orgStaffCountMonthReportDao.getOrgStaffSkillByOrgId(orgId, beginTime, endTime);

		return resList;
	}

	/**
	 * 获取上级组织人员数量环比数据
	 * @param orgId 组织Id
	 * @param yearMonthList 年月List
	 * @return
	 */
	public Map getUpOrgStaffCountChainReportService(long orgId,List<String> yearMonthList) {
		Map resMap = null;
		//yuan.yw
		List<SysOrg> upOrgList = this.sysOrganizationService.getUpOrNextLevelOrgByOrgIdService(orgId,"parent");

		//List<ProviderOrganization> upOrgList = providerOrganizationService.getUpProviderOrgByOrgIdService(orgId);
		if(upOrgList!=null && !upOrgList.isEmpty()){
			SysOrg upOrg = upOrgList.get(0);
			orgId = upOrg.getOrgId();
			resMap = this.getOrgStaffCountChainReportService(orgId, yearMonthList);
		}
		return resMap;
	}

	/**
	 * 获取上级组织人员数量报表信息
	 * @param orgId 组织Id
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	public Map getUpOrgStaffCountReportInfoService(long orgId,String beginTime, String endTime) {
		Map resMap = null;
		//List<ProviderOrganization> upOrgList = providerOrganizationService.getUpProviderOrgByOrgIdService(orgId);
		//yuan.yw
		List<SysOrg> upOrgList = this.sysOrganizationService.getUpOrNextLevelOrgByOrgIdService(orgId,"parent");

		if(upOrgList!=null && !upOrgList.isEmpty()){
			SysOrg upOrg = upOrgList.get(0);
			orgId = upOrg.getOrgId();
			resMap = this.getNextOrgStaffCountReportInfoService(orgId, beginTime, endTime);
		}
		return resMap;
	}

	/**
	 * 获取组织架构下项目人员数统计
	 * @param orgId 组织Id
	 * @param beginTime 统计开始时间
	 * @param endTime 统计结束时间
	 * @return
	 */
	public Map getOrgProjectStaffCountReportService(long orgId,String beginTime, String endTime) {
		Map resMap = new HashMap();	//返回结果
		List<Integer> dataList = new ArrayList<Integer>();
		List<String> orgNameList = new ArrayList<String>();
		List<String> orgIdList =  new ArrayList<String>();
		String orgName = "";
		Map<String,Object> orgInfo = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
		
		if(orgInfo!=null){
			orgName = (String) orgInfo.get("name");
		}
		//获取当前登录用户组织架构下的 项目
		//List<Map<String, String>> projectList = organizationService.getProjectToDownwardOrgByOrgIdService(orgId);
		//yuan.yw
		List<Map<String, String>> projectList = this.sysOrganizationService.getProjectToDownwardOrgByOrgId(orgId);
		if(projectList!=null && !projectList.isEmpty()){
			for (Map<String, String> map : projectList) {
				if(map != null){
					if(map.get("id") != null){
						
						long poId = Long.valueOf(map.get("id"));
						String poName = map.get("NAME");
						int staffCount = 0;
						//List<Map<String, String>> orgList = organizationService.getOrgByProjectIdService(poId+"");
						//yuan.yw
						List<Map<String, String>> orgList = this.sysOrganizationService.getOrgByProjectIdService(poId+"");
						if(orgList!=null && !orgList.isEmpty()){
							for (Map<String, String> map2 : orgList) {
								poId = Long.valueOf(map2.get("id"));
								List<Map> poReportInfoList = this.getOrgStaffCountByOrgIdService(poId, beginTime, endTime);
								if(poReportInfoList!=null && !poReportInfoList.isEmpty()){
									Map m = poReportInfoList.get(0);
									staffCount += Integer.valueOf(m.get("staffCount")+"");
								}
							}
						}
						orgNameList.add(poName);
						dataList.add(staffCount);
						orgIdList.add(poId+"");
					}
				}
			}
		}
		//报表数据
		Map reportMap = new HashMap();
		reportMap.put("name", "人员数");
		reportMap.put("data", dataList);
		//放到返回结果
		resMap.put("reportMap", reportMap);
		resMap.put("orgIdList", orgIdList);
		resMap.put("orgNameList", orgNameList);
		resMap.put("orgName", orgName);
		resMap.put("userOrgId", orgId);
		return resMap;
	}


	
}
