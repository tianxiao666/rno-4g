package com.iscreate.op.service.report;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.informationmanage.common.ObjectUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.dao.cardispatch.CardispatchCarDao;
import com.iscreate.op.dao.cardispatch.CardispatchTerminalDao;
import com.iscreate.op.dao.cardispatch.CardispatchWorkorderDao;
import com.iscreate.op.dao.informationmanage.InformationManageNetworkResourceDao;
import com.iscreate.op.dao.report.CarCensusReportDao;
import com.iscreate.op.dao.system.SysOrganizationDao;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.system.SysOrganizationService;



@SuppressWarnings({"rawtypes","unchecked","unused","hiding"})
public class CarCensusReportServiceImpl implements CarCensusReportService {
	
	/************ 依赖注入 ***********/
	private CarCensusReportDao carCensusReportDao;
	private CardispatchWorkorderDao cardispatchWorkorderDao;
	private CardispatchTerminalDao cardispatchTerminalDao;
	private InformationManageNetworkResourceDao informationManageNetworkResourceDao;
	private CardispatchCarDao cardispatchCarDao;
	private SysOrganizationDao  sysOrganizationDao; //组织dao du.hw
	private SysOrganizationService  sysOrganizationService; //组织service 
	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	
	
	/***************** 属性 *******************/
	
	
	/****************** service *******************/
	
	
	/** 车辆数量统计 begin ***************************************/
	
	

	/**
	 * 根据组织id、车辆类型,查询车辆信息
	 * @param carBizId - 车辆组织id
	 * @param carType - 车类型
	 * @return (List<Map<String, String>>) 车辆信息集合
	 */
	public Map<String, Map<String,Object>> censusCarCountByBiz ( Long carBizId , String[] carType ) {
		Map<String , Map<String,Object>> map = new LinkedHashMap<String, Map<String,Object>>();
		//List<ProviderOrganization> nextProviderOrgByOrgIdService = providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(carBizId,"BusinessOrganization");
		//yuan.yw
		List<Map<String,Object>> nextProviderOrgByOrgIdService=this.sysOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(carBizId,"BusinessOrganization");
		if ( nextProviderOrgByOrgIdService == null || nextProviderOrgByOrgIdService.size() == 0 ) {
			System.out.println("没有下级组织");
			return map;
		}
		for (int i = 0; i < nextProviderOrgByOrgIdService.size(); i++) {
			Map<String,Object> pro = nextProviderOrgByOrgIdService.get(i);
			if ( pro != null ) {
				Long orgId = Long.valueOf(pro.get("orgId")+"");
				String orgName = pro.get("name")+"";
				//List<ProviderOrganization> orgListDownwardByOrg = providerOrganizationService.getOrgListDownwardByOrg(orgId);
				List<SysOrg> orgListDownwardByOrg = this.sysOrganizationService.getOrgListDownwardByOrg(orgId);
				
				//统计组织下的车辆数量
				Integer count = 0 ;
				if ( orgListDownwardByOrg != null ) {
					List<String> orgIds = new ArrayList<String>();
					for (int j = 0; j < orgListDownwardByOrg.size(); j++) {
						orgIds.add(orgListDownwardByOrg.get(j).getOrgId()+"");
					}
					List<Map<String, Object>> censusCarCountByBiz = carCensusReportDao.censusCarByBiz( orgIds , carType );
					if ( censusCarCountByBiz != null ) {
						count = censusCarCountByBiz.size();
					}
				}
				Map<String,Object> data_map = new LinkedHashMap<String, Object>();
				Map<String, Object> pro_map = null;
				try {
					pro_map = pro;
				} catch (Exception e) {
					e.printStackTrace();
				}
				//判断是否有下级组织
				boolean isLast = orgListDownwardByOrg == null || orgListDownwardByOrg.size() <= 1 ;
				pro_map.put("isLast", isLast);
				data_map.put("count", count);
				data_map.put("info", pro_map );
				map.put(orgName, data_map);
			}
		}
		return map;
	}
	
	/**
	 * 根据项目 , 统计车辆数量
	 * @param carType - 车辆类型
	 * @return
	 */
	public Map<String,Map<String, Object>> censusCarCountByProject ( String[] carType ) {
		Map<String , Map<String,Object>> map = new LinkedHashMap<String, Map<String,Object>>();
		//获取当前登录人的组织
		String accountId = (String) SessionService.getInstance().getValueByKey("userId");
		//List<ProviderOrganization> org = this.providerOrganizationService.getTopLevelOrgByAccount(accountId);
		List<SysOrg> org = this.sysOrganizationService.getTopLevelOrgByAccount(accountId);
		
		String orgId = "";
		if ( org != null && org.size() > 0 ) {
			orgId = org.get(0).getOrgId()+"";
		}
		if ( StringUtil.isNullOrEmpty(orgId) ) {
			System.out.println("组织为空");
			return map;
		}
		//List<Map<String, String>> project_list = this.providerOrganizationService.getProjectToDownwardProviderOrgByOrgId( Long.valueOf(orgId) );
		//yuan.yw
		List<Map<String, String>> project_list = this.sysOrganizationService.getProjectToDownwardOrgByOrgId( Long.valueOf(orgId) );//组织向下下级组织获取项目
		for (int i = 0; project_list != null && i < project_list.size(); i++) {
			Map<String, String> project_map = project_list.get(i);
			if ( project_map == null || project_map.size() == 0 ) {
				continue;
			}
			String projectId = project_map.get("id");
			Map<String,Object> project_info_map = new HashMap<String, Object>();
			List<Map<String, String>> org_list = this.informationManageNetworkResourceDao.findCarryOutOrgIdByProjectId(projectId);
			//根据组织id,获取车辆数量
			Integer car_size = 0;
			for (int j = 0; org_list != null && j < org_list.size() ; j++) {
				Map<String, String> org_map = org_list.get(j);
				String bizId = org_map.get("orgId");
				List<Map<String, Object>> car_list = cardispatchCarDao.findCarListByBizId(bizId);
				car_size += car_list.size();
			}
			project_info_map.put("info", project_map);
			project_info_map.put("count", car_size);
			map.put(projectId, project_info_map);
		}
		return map;
	}
	
	
	/** 车辆数量统计 end ***************************************/
	
	
	
	/** 车辆里程统计 begin ***************************************/
	
	/**
	 * 根据组织id、车辆类型,查询车辆信息
	 * @param carBizId - 车辆组织id
	 * @param carType - 车类型
	 * @param beginTime - 起始时间
	 * @param endTime - 结束时间
	 * @return (List<Map<String, String>>) 车辆信息集合
	 */
	public Map<String, Map<String, Object>> censusCarMileageByBiz ( Long carBizId , String[] carType , String time ) {
		Map<String , Map<String,Object>> map = new LinkedHashMap<String, Map<String,Object>>();
		//获取下级组织
		//List<ProviderOrganization> nextProviderOrgByOrgIdService = providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(carBizId,"BusinessOrganization");
		//yuan.yw
		List<Map<String,Object>> nextProviderOrgByOrgIdService=this.sysOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(carBizId,"BusinessOrganization");

		if ( nextProviderOrgByOrgIdService == null || nextProviderOrgByOrgIdService.size() == 0 ) {
			map = censusCarMileageByCarNumber( carBizId , carType, time );
			return map;
		}
		for (int i = 0; i < nextProviderOrgByOrgIdService.size(); i++) {
			Map<String,Object> pro = nextProviderOrgByOrgIdService.get(i);
			if ( pro != null ) {
				Long orgId = Long.valueOf(pro.get("orgId")+"");
				String orgName = pro.get("name")+"";
				//递归获取下级组织集合
				//List<ProviderOrganization> orgListDownwardByOrg = providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
				List<Map<String,Object>> orgListDownwardByOrg = this.sysOrganizationService.getProviderOrgByOrgIdAndOrgAttrByLevelService(orgId,"BusinessOrganization","downward");

				//统计组织下的车辆数量
				Double totalMileage = 0d ;
				Double totalGpsMileage = 0d ;
				if ( orgListDownwardByOrg != null ) {
					List<String> orgIds = new ArrayList<String>();
					for (int j = 0; j < orgListDownwardByOrg.size(); j++) {
						orgIds.add(orgListDownwardByOrg.get(j).get("orgId")+"");
					}
					List<Map<String, Object>> list = carCensusReportDao.censusCarMileageInTimes(carType, time, orgIds);
					for (int j = 0; list != null && j < list.size(); j++) {
						Map<String, Object> mileage_map = list.get(j);
						String mileage = mileage_map.get("mileage")+"";
						String gpsmileage = mileage_map.get("gpsmileage")+"";
						if ( mileage != null && !mileage.isEmpty() && !mileage.equals("null")) {
							totalMileage += Double.valueOf(mileage);
						}
						if ( gpsmileage != null && !gpsmileage.isEmpty() && !gpsmileage.equals("null")) {
							totalGpsMileage += Double.valueOf(gpsmileage);
						}
					}
				}
				Map<String,Object> data_map = new LinkedHashMap<String, Object>();
				Map<String, Object> pro_map = null;
				try {
					pro_map = pro;
				} catch (Exception e) {
					e.printStackTrace();
				}
				//判断是否有下级组织
				boolean isLast = orgListDownwardByOrg == null || orgListDownwardByOrg.size() <= 1 ;
				
				NumberFormat nf = NumberFormat.getInstance();
				nf.setGroupingUsed(false);
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(1);
				Double totalMileageString  = Double.valueOf(nf.format(totalMileage));
				Double totalGpsMileageString  = Double.valueOf(nf.format(totalGpsMileage));
				
				pro_map.put("isLast", false);
				data_map.put("totalMileage", totalMileageString);
				data_map.put("totalGpsMileage", totalGpsMileageString);
				data_map.put("info", pro_map );
				map.put(orgName, data_map);
			}
		}
		return map;
	}
	
	
	/**
	 * 根据组织id、车辆类型,查询车辆信息
	 * @param carType - 车类型
	 * @param time - 时间
	 * @return (List<Map<String, String>>) 车辆信息集合
	 */
	public Map<String, Map<String, Object>> censusCarMileageByProject ( String[] carType , String time ) {
		Map<String , Map<String,Object>> map = new LinkedHashMap<String, Map<String,Object>>();
		//获取当前登录人所在组织
		String accountId = (String) SessionService.getInstance().getValueByKey("userId");
		//List<ProviderOrganization> org = this.providerOrganizationService.getTopLevelOrgByAccount(accountId);
		List<SysOrg> org = this.sysOrganizationService.getTopLevelOrgByAccount(accountId);
		String orgId = "";
		if ( org != null && org.size() > 0 ) {
			orgId = org.get(0).getOrgId()+"";
		}
		if ( StringUtil.isNullOrEmpty(orgId) ) {
			System.out.println("组织为空");
			return map;
		}
		//根据当前登录人所在组织及下级组织 , 获取相关项目
		//List<Map<String, String>> project_list = this.providerOrganizationService.getProjectToDownwardProviderOrgByOrgId( Long.valueOf(orgId) );
		//yuan.yw
		List<Map<String, String>> project_list = this.sysOrganizationService.getProjectToDownwardOrgByOrgId( Long.valueOf(orgId) );
		//遍历项目集合
		for (int i = 0; project_list != null && i < project_list.size(); i++) {
			Map<String, String> project_map = project_list.get(i);
			if ( project_map == null || project_map.size() == 0 ) {
				continue;
			}
			String projectId = project_map.get("id");
			Map<String,Object> project_info_map = new HashMap<String, Object>();
			//获取项目相关组织集合
			List<Map<String, String>> org_list = this.informationManageNetworkResourceDao.findCarryOutOrgIdByProjectId(projectId);
			//获取组织下在一定时间内的车辆里程读数(不递归组织)
			Integer car_size = 0;
			String startDate = project_map.get("startDate");
			String planEndDate = project_map.get("planEndDate");
			List<String> bizId_list = new ArrayList<String>();
			for (int j = 0; org_list != null && j < org_list.size() ; j++) {
				Map<String, String> org_map = org_list.get(j);
				String bizId = org_map.get("orgId");
				bizId_list.add(bizId);
			}
				
			List<Map<String, Object>> mileage_list = carCensusReportDao.censusCarMileageInTimes(carType, startDate , planEndDate , bizId_list );
			Double mileage = 0d;
			Double gpsMileage = 0d;
			for (int j = 0; mileage_list != null && j < mileage_list.size(); j++) {
				Map<String, Object> mileage_map = mileage_list.get(j);
				String mileage_string = mileage_map.get("mileage")+"";
				String gpsmileage_string = mileage_map.get("gpsmileage")+"";
				if ( !StringUtil.isNullOrEmpty(mileage_string) ) {
					mileage += Double.valueOf(mileage_string);
				}
				if ( !StringUtil.isNullOrEmpty(gpsmileage_string) ) {
					gpsMileage += Double.valueOf(gpsmileage_string);
				}
			}
			project_info_map.put("info", project_map);
			project_info_map.put("mileage", mileage);
			project_info_map.put("gpsMileage", gpsMileage);
			map.put(projectId, project_info_map);
		}
		return map;
	}
	
	
	/**
	 * 根据组织id、车辆类型 , 查询车辆信息
	 * @param carBizId - 车辆组织id
	 * @param carType - 车类型
	 * @param beginTime - 起始时间
	 * @param endTime - 结束时间
	 * @return (List<Map<String, String>>) 车辆信息集合
	 */
	public Map<String, Map<String, Object>> censusCarMileageByCarNumber ( Long carBizId , String[] carType , String time ) {
		Map<String , Map<String,Object>> map = new LinkedHashMap<String, Map<String,Object>>();
		//List<ProviderOrganization> orgListDownwardByOrg = providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(carBizId,"BusinessOrganization");
		List<Map<String,Object>> orgListDownwardByOrg = this.sysOrganizationService.getProviderOrgByOrgIdAndOrgAttrByLevelService(carBizId,"BusinessOrganization","downward");

		if ( orgListDownwardByOrg == null ) {
			return map;
		}
		List<String> orgIds = new ArrayList<String>();
		for (int j = 0; j < orgListDownwardByOrg.size(); j++) {
			orgIds.add(orgListDownwardByOrg.get(j).get("orgId")+"");
		}
		List<Map<String, Object>> cars_list = carCensusReportDao.censusCarByBiz( orgIds , carType);
		if ( cars_list == null || cars_list.size() == 0 ) {
			return map;
		}
		for (int j = 0; j < cars_list.size(); j++) {
			Double totalMileage = 0d ;
			Double totalGpsMileage = 0d ;
			Map<String, Object> car_map = cars_list.get(j);
			String carNumber = car_map.get("carNumber") + "";
			String cdpairId = car_map.get("cdpairId")+"";
			String terminalId = car_map.get("terminalId")+"";
			
			List<Map<String, Object>> list = carCensusReportDao.censusCarMileageByCarNumberInTimes( carNumber, time );
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> car_mileage_map = list.get(i);
				String mileage = car_mileage_map.get("mileage")+"";
				String gpsmileage = car_mileage_map.get("gpsmileage")+"";
				if ( mileage != null && !mileage.isEmpty() && !mileage.equals("null")) {
					totalMileage += Double.valueOf(mileage);
				}
				if ( gpsmileage != null && !gpsmileage.isEmpty() && !gpsmileage.equals("null")) {
					totalGpsMileage += Double.valueOf(gpsmileage);
				}
			}
			Map<String,Object> data_map = new LinkedHashMap<String, Object>();
			NumberFormat nf = NumberFormat.getInstance();
			nf.setGroupingUsed(false);
			nf.setMaximumFractionDigits(2);
			nf.setMinimumFractionDigits(1);
			car_map.put("isLast", true);
			Double totalMileageString  = Double.valueOf(nf.format(totalMileage));
			Double totalGpsMileageString  = Double.valueOf(nf.format(totalGpsMileage));
			data_map.put("totalMileage", totalMileageString);
			data_map.put("totalGpsMileage", totalGpsMileageString);
			data_map.put("info", car_map );
			map.put(carNumber, data_map);
		}
		return map;
	}
	
	
	/**
	 * 根据当前登录人组织、日期范围集合、车辆类型 , 查询车辆信息(环比)
	 * @param date_list - 日期集合
	 * @param carType - 车类型
	 * @return (List<Map<String, String>>) 车辆信息集合
	 */
	public Map<String , Map<String,Object>> censusCarMileageForRoundInTimes ( List<String> date_list , String[] carType ) {
		Map<String , Map<String,Object>> map = new LinkedHashMap<String, Map<String,Object>>();
		if ( date_list == null || date_list.size() == 0 ) {
			return map;
		}
		List<String> userBizId_list = new ArrayList<String>();
		String carBizId_string = "16";
		//获取当前登录人的业务单元
		String accountId = (String) SessionService.getInstance().getValueByKey("userId");
		//List<ProviderOrganization> orgByAccountAndOrgTypes = providerOrganizationService.getTopLevelOrgByAccount( accountId );
		List<SysOrg> orgByAccountAndOrgTypes = this.sysOrganizationService.getTopLevelOrgByAccount(accountId);
		if ( orgByAccountAndOrgTypes != null && orgByAccountAndOrgTypes.size() > 0 ) {
			carBizId_string = orgByAccountAndOrgTypes.get(0).getOrgId()+"";
		} else {
			carBizId_string = "16";
		}
		//递归查询下级组织id
		//List<ProviderOrganization> orgListDownwardByOrg = providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(Long.valueOf(carBizId_string),"BusinessOrganization");
		List<Map<String,Object>> orgListDownwardByOrg = this.sysOrganizationService.getProviderOrgByOrgIdAndOrgAttrByLevelService(Long.valueOf(carBizId_string),"BusinessOrganization","downward");

		for (int i = 0; i < orgListDownwardByOrg.size(); i++) {
			Map<String,Object> providerOrganization = orgListDownwardByOrg.get(i);
			userBizId_list.add(providerOrganization.get("orgId")+"");
		}
		
		for (int i = 0; i < date_list.size(); i++) {
			String date = date_list.get(i);
			List<Map<String, Object>> list = carCensusReportDao.censusCarMileageForRoundInTimes(carType, date, userBizId_list);
			Double totalMileage = 0d;
			Double totalGpsMileage = 0d;
			for (int j = 0; list != null && j < list.size(); j++) {
				Map<String, Object> mil = list.get(j);
				String mileage = mil.get("mileage")+"";
				String gpsmileage = mil.get("gpsmileage")+"";
				if ( mileage != null && !mileage.isEmpty() && !mileage.equals("null")) {
					totalMileage += Double.valueOf(mileage);
				}
				if ( gpsmileage != null && !gpsmileage.isEmpty() && !gpsmileage.equals("null")) {
					totalGpsMileage += Double.valueOf(gpsmileage);
				}
			}
			NumberFormat nf = NumberFormat.getInstance();
			nf.setGroupingUsed(false);
			nf.setMaximumFractionDigits(2);
			nf.setMinimumFractionDigits(1);
			String totalMileageString = nf.format(totalMileage);
			String totalGpsMileageString = nf.format(totalGpsMileage);
			Map<String,Object> data_map = new LinkedHashMap<String, Object>();
			data_map.put("totalMileage", totalMileageString);
			data_map.put("totalGpsMileage", totalGpsMileageString);
			data_map.put("info", list );
			map.put(date, data_map);
		}
		return map;
	}
	
	/** 车辆里程统计 end ***************************************/
	
	
	
	/** 车辆数量里程综合统计 begin ***************************************/
	
	
	/**
	 * 根据组织id、车辆类型,查询车辆信息
	 * @param carBizId - 车辆组织id
	 * @param carType - 车类型
	 * @param beginTime - 起始时间
	 * @param endTime - 结束时间
	 * @return (List<Map<String, String>>) 车辆信息集合
	 */
	public Map<String, Map<String, Object>> censusCarCountMileageByBiz ( Long carBizId , String[] carType , String time ) {
		Map<String , Map<String,Object>> map = new LinkedHashMap<String, Map<String,Object>>();
		//获取下级组织
//		List<ProviderOrganization> nextProviderOrgByOrgIdService = providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(carBizId,"BusinessOrganization");
		//yuan.yw
		List<Map<String,Object>> nextProviderOrgByOrgIdService=this.sysOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(carBizId,"BusinessOrganization");

		for (int i = 0; nextProviderOrgByOrgIdService != null && i < nextProviderOrgByOrgIdService.size(); i++) {
			Map<String,Object> pro = nextProviderOrgByOrgIdService.get(i);
			if ( pro != null ) {
				Long orgId = Long.valueOf(pro.get("orgId")+"");
				String orgName = pro.get("name")+"";
				//递归获取下级组织集合
				//List<ProviderOrganization> orgListDownwardByOrg = providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
				List<Map<String,Object>> orgListDownwardByOrg = this.sysOrganizationService.getProviderOrgByOrgIdAndOrgAttrByLevelService(orgId,"BusinessOrganization","downward");

				//统计组织下的车辆数量
				Double totalMileage = 0d ;
				Double totalGpsMileage = 0d ;
				Integer carCount = 0;
				if ( orgListDownwardByOrg != null ) {
					List<String> orgIds = new ArrayList<String>();
					for (int j = 0; j < orgListDownwardByOrg.size(); j++) {
						orgIds.add(orgListDownwardByOrg.get(j).get("orgId")+"");
					}
					List<Map<String, Object>> list = carCensusReportDao.censusCarMileageInTimes(carType, time, orgIds);
					for (int j = 0; list != null && j < list.size(); j++) {
						Map<String, Object> mileage_map = list.get(j);
						String mileage = mileage_map.get("mileage")+"";
						String gpsmileage = mileage_map.get("gpsmileage")+"";
						if ( mileage != null && !mileage.isEmpty() && !mileage.equals("null")) {
							totalMileage += Double.valueOf(mileage);
						}
						if ( gpsmileage != null && !gpsmileage.isEmpty() && !gpsmileage.equals("null")) {
							totalGpsMileage += Double.valueOf(gpsmileage);
						}
					}
					List<Map<String, Object>> censusCarByBiz = carCensusReportDao.censusCarByBiz(orgIds, carType);
					if ( censusCarByBiz != null ) {
						carCount = censusCarByBiz.size();
					}
				}
				Map<String,Object> data_map = new LinkedHashMap<String, Object>();
				Map<String, Object> pro_map = null;
					pro_map = pro;
				//判断是否有下级组织
				boolean isLast = orgListDownwardByOrg == null || orgListDownwardByOrg.size() <= 1 ;
				
				NumberFormat nf = NumberFormat.getInstance();
				nf.setGroupingUsed(false);
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(1);
				Double totalMileageString  = Double.valueOf(nf.format(totalMileage));
				Double totalGpsMileageString  = Double.valueOf(nf.format(totalGpsMileage));
				
				pro_map.put("isLast", false);
				data_map.put("totalMileage", totalMileageString);
				data_map.put("totalGpsMileage", totalGpsMileageString);
				data_map.put("carCount", carCount);
				data_map.put("info", pro_map );
				Map<String,String> list_map = new LinkedHashMap<String, String>();
				list_map.put("orgName", pro.get("name")+"");
				list_map.put("totalMileage", totalMileageString+"");
				list_map.put("totalGpsMileage", totalGpsMileageString+"");
				list_map.put("carCount", carCount+"");
				data_map.put("data", list_map );
				map.put(orgName, data_map);
			}
		}
		
		//车辆里程
		ArrayList<String> bizIds = new ArrayList<String>();
		bizIds.add(carBizId+"");
		List<Map<String, Object>> cars_list = carCensusReportDao.censusCarByBiz( bizIds , carType);
		for (int j = 0; j < cars_list.size(); j++) {
			Double totalMileage = 0d ;
			Double totalGpsMileage = 0d ;
			Map<String, Object> car_map = cars_list.get(j);
			String carNumber = car_map.get("carNumber") + "";
			String cdpairId = car_map.get("cdpairId")+"";
			String terminalId = car_map.get("terminalId")+"";
			
			List<Map<String, Object>> list = carCensusReportDao.censusCarMileageByCarNumberInTimes( carNumber, time );
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> car_mileage_map = list.get(i);
				String mileage = car_mileage_map.get("mileage")+"";
				String gpsmileage = car_mileage_map.get("gpsmileage")+"";
				if ( mileage != null && !mileage.isEmpty() && !mileage.equals("null")) {
					totalMileage += Double.valueOf(mileage);
				}
				if ( gpsmileage != null && !gpsmileage.isEmpty() && !gpsmileage.equals("null")) {
					totalGpsMileage += Double.valueOf(gpsmileage);
				}
			}
			Map<String,Object> data_map = new LinkedHashMap<String, Object>();
			NumberFormat nf = NumberFormat.getInstance();
			nf.setGroupingUsed(false);
			nf.setMaximumFractionDigits(2);
			nf.setMinimumFractionDigits(1);
			car_map.put("isLast", true);
			Double totalMileageString  = Double.valueOf(nf.format(totalMileage));
			Double totalGpsMileageString  = Double.valueOf(nf.format(totalGpsMileage));
			data_map.put("totalMileage", totalMileageString);
			data_map.put("totalGpsMileage", totalGpsMileageString);
			data_map.put("info", car_map );
			map.put(carNumber, data_map);
		}
		return map;
	}
	
	/**
	 * 根据当前登录人组织、日期范围集合、车辆类型 , 查询车辆信息(环比)
	 * @param date_list - 日期集合
	 * @param carType - 车类型
	 * @return (List<Map<String, String>>) 车辆信息集合
	 */
	public Map<String , Map<String,Object>> censusCarCountMileageForRoundInTimes ( List<String> date_list , String[] carType ) {
		Map<String , Map<String,Object>> map = new LinkedHashMap<String, Map<String,Object>>();
		if ( date_list == null || date_list.size() == 0 ) {
			return map;
		}
		List<String> userBizId_list = new ArrayList<String>();
		String carBizId_string = "16";
		//获取当前登录人的业务单元
		String accountId = (String) SessionService.getInstance().getValueByKey("userId");
		//List<ProviderOrganization> orgByAccountAndOrgTypes = providerOrganizationService.getTopLevelOrgByAccount( accountId );
		List<SysOrg> orgByAccountAndOrgTypes = this.sysOrganizationService.getTopLevelOrgByAccount(accountId);
		if ( orgByAccountAndOrgTypes != null && orgByAccountAndOrgTypes.size() > 0 ) {
			carBizId_string = orgByAccountAndOrgTypes.get(0).getOrgId()+"";
		} else {
			carBizId_string = "16";
		}
		//递归查询下级组织id
		//List<ProviderOrganization> orgListDownwardByOrg = providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(Long.valueOf(carBizId_string),"BusinessOrganization");
		List<Map<String,Object>> orgListDownwardByOrg = this.sysOrganizationService.getProviderOrgByOrgIdAndOrgAttrByLevelService(Long.valueOf(carBizId_string),"BusinessOrganization","downward");

		for (int i = 0; i < orgListDownwardByOrg.size(); i++) {
			Map<String,Object> providerOrganization = orgListDownwardByOrg.get(i);
			userBizId_list.add(providerOrganization.get("OrgId")+"");
		}
		Integer carCount = 0;
		List<Map<String, Object>> censusCarByBiz = carCensusReportDao.censusCarByBiz(userBizId_list, carType);
		if ( censusCarByBiz != null ) {
			carCount = censusCarByBiz.size();
		}
		for (int i = 0; i < date_list.size(); i++) {
			String date = date_list.get(i);
			List<Map<String, Object>> list = carCensusReportDao.censusCarMileageForRoundInTimes(carType, date, userBizId_list);
			Double totalMileage = 0d;
			Double totalGpsMileage = 0d;
			for (int j = 0; list != null && j < list.size(); j++) {
				Map<String, Object> mil = list.get(j);
				String mileage = mil.get("mileage")+"";
				String gpsmileage = mil.get("gpsmileage")+"";
				if ( mileage != null && !mileage.isEmpty() && !mileage.equals("null")) {
					totalMileage += Double.valueOf(mileage);
				}
				if ( gpsmileage != null && !gpsmileage.isEmpty() && !gpsmileage.equals("null")) {
					totalGpsMileage += Double.valueOf(gpsmileage);
				}
			}
			NumberFormat nf = NumberFormat.getInstance();
			nf.setGroupingUsed(false);
			nf.setMaximumFractionDigits(2);
			nf.setMinimumFractionDigits(1);
			String totalMileageString = nf.format(totalMileage);
			String totalGpsMileageString = nf.format(totalGpsMileage);
			Map<String,Object> data_map = new LinkedHashMap<String, Object>();
			data_map.put("totalMileage", totalMileageString);
			data_map.put("totalGpsMileage", totalGpsMileageString);
			data_map.put("carCount", carCount);
			data_map.put("info", list );
			map.put(date, data_map);
		}
		return map;
	}
	
	/** 车辆数量里程综合统计 end ***************************************/
	
	
	
	/** 车辆油费统计 begin ***************************************/
	
	/**
	 * 根据组织id、车辆类型,查询车辆信息
	 * @param carBizId - 车辆组织id
	 * @param carType - 车类型
	 * @param time - 时间
	 * @return (Map<String, Map<String, Object>>) 车辆信息集合
	 */
	public Map<String, Map<String, Object>> censusCarOilByBiz( Long carBizId  , String time ) {
		Map<String , Map<String,Object>> map = new LinkedHashMap<String, Map<String,Object>>();
		//查询标题组织的下级组织id
		//List<ProviderOrganization> orgListDownwardByOrg = providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(carBizId,"BusinessOrganization");
		//yuan.yw
		List<Map<String,Object>> orgListDownwardByOrg=this.sysOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(carBizId,"BusinessOrganization");

		//根据组织查询,车辆信息
		for (int j = 0; orgListDownwardByOrg != null && j < orgListDownwardByOrg.size(); j++) {
			Map<String,Object> pr = orgListDownwardByOrg.get(j);
			Long orgId = Long.valueOf(pr.get("orgId")+"");
			String orgName = pr.get("name")+"";
			if ( orgId == carBizId ) {
				continue;
			}
			//String orgName = pr.getName();
			//递归列名组织的下级组织id
			//List<ProviderOrganization> orgList = providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
			List<Map<String,Object>> orgList = this.sysOrganizationService.getProviderOrgByOrgIdAndOrgAttrByLevelService(orgId,"BusinessOrganization","downward");

			List<String> bizIds_list = new ArrayList<String>();
			if ( orgList == null || orgList.size() == 0 ) {
				continue;
			}
			for (int i = 0; i < orgList.size(); i++) {
				Map<String,Object> pro = orgList.get(i);
				String proId = pro.get("orgId")+"";
				bizIds_list.add(proId);
			}
			//根据组织、时间,查询该组织的费用信息
			List<Map<String, Object>> bizOil_list = carCensusReportDao.censusBizOilByBiz( bizIds_list  , time );
			//获取组织的总里程
			List<Map<String, Object>> mileage_list = carCensusReportDao.censusCarMileageForRoundInTimes(null, time, bizIds_list);
			//计算每百公里油耗
			Map<String, Object> result_map = new LinkedHashMap<String, Object>();
			Double totalOilNum = 0d;
			Double totalOilMoney = 0d;
			Double totalGpsMileage  = 0d;
			String oilUseNum  = "0";
			Map<String, Object> mileage_map = new LinkedHashMap<String, Object>();
			if ( mileage_list != null && mileage_list.size() > 0 ) {
				mileage_map = mileage_list.get(0);
				if ( !StringUtil.isNullOrEmpty(mileage_map.get("gpsmileage")+"") ) {
					totalGpsMileage = Double.valueOf(mileage_map.get("gpsmileage")+"");
				}
			}
			if ( bizOil_list != null && bizOil_list.size() > 0 ) {
				result_map = bizOil_list.get(0);
				//判断是否有下级组织
				if ( bizIds_list != null && bizIds_list.size() > 0 ) {
					result_map.put("isLast", false);
				} else {
					result_map.put("isLast", true);
				}
				//获取组织名
				String bId = result_map.get("carBizId") + "";
				SysOrg pv = sysOrganizationDao.getOrgByOrgId(Long.valueOf(bId));
				if ( pv != null ) {
					result_map.put("carBizName", pv.getName());
				}
				String oilNum = result_map.get("oilNum")+"";
				String oilMoney = result_map.get("oilMoney")+"";
				if ( !StringUtil.isNullOrEmpty(oilNum) && !StringUtil.isNullOrEmpty(oilMoney) ) {
					totalOilNum = Double.valueOf(oilNum);
					totalOilMoney = Double.valueOf(oilMoney);
					Double useNum = totalGpsMileage / totalOilNum * 100 ;
					NumberFormat nf = NumberFormat.getInstance();
					nf.setGroupingUsed(false);
					nf.setMaximumFractionDigits(2);
					nf.setMinimumFractionDigits(1);
					oilUseNum = nf.format(useNum);
					result_map.put("oilUseNum", oilUseNum);
				}
			}
			Map<String,Object> info_map = new LinkedHashMap<String, Object>();
			Map<String, Object> pro_map = new LinkedHashMap<String, Object>();
			try {
				pro_map = pr;
			} catch (Exception e) {
				e.printStackTrace();
			}
			info_map.put("info", pro_map);
			info_map.put("totalOilNum", totalOilNum);
			info_map.put("totalOilMoney", totalOilMoney);
			info_map.put("oilUseNum", oilUseNum);
			info_map.put("data", result_map);
			map.put(orgName, info_map);
		}
		
		//查询该组织下的车辆
		ArrayList<String> bizIds = new ArrayList<String>();
		bizIds.add(carBizId+"");
		List<Map<String, Object>> cars_list = carCensusReportDao.censusCarByBiz( bizIds , null);
		for (int i = 0; cars_list != null && i < cars_list.size() ; i++) {
			Map<String, Object> car_map = cars_list.get(i);
			String carNumber = car_map.get("carNumber") + "";
			List<Map<String, Object>> carOil_list = carCensusReportDao.censusCarOilByBiz(time, carNumber);
			//计算每百公里油耗
			Map<String, Object> result_map = new LinkedHashMap<String, Object>();
			Double totalOilNum = 0d;
			Double totalOilMoney  = 0d;
			Double totalGpsMileage  = 0d;
			String oilUseNum  = "0";
			Map<String, Object> mileage_map = new LinkedHashMap<String, Object>();
			List<Map<String, Object>> censusCarMileageInTime = carCensusReportDao.censusCarMileageInTime(carNumber, time);
			if ( censusCarMileageInTime != null && censusCarMileageInTime.size() > 0 ) {
				mileage_map = censusCarMileageInTime.get(0);
				if ( !StringUtil.isNullOrEmpty(mileage_map.get("gpsmileage")+"") ) {
					totalGpsMileage = Double.valueOf(mileage_map.get("gpsmileage")+"");
				}
			}
			if ( carOil_list != null && carOil_list.size() > 0 ) {
				result_map = carOil_list.get(0);
				//判断是否有下级组织
				result_map.put("isLast", true);
				String oilNum = result_map.get("oilNum")+"";
				String oilMoney = result_map.get("oilMoney")+"";
				if ( !StringUtil.isNullOrEmpty(oilNum) && !StringUtil.isNullOrEmpty(oilMoney) ) {
					totalOilNum = Double.valueOf(oilNum);
					totalOilMoney = Double.valueOf(oilMoney);
					Double useNum =  totalGpsMileage / totalOilNum * 100 ;
					NumberFormat nf = NumberFormat.getInstance();
					nf.setGroupingUsed(false);
					nf.setMaximumFractionDigits(2);
					nf.setMinimumFractionDigits(1);
					oilUseNum = nf.format(useNum);
					result_map.put("oilUseNum", oilUseNum);
				}
			}
			Map<String,Object> info_map = new LinkedHashMap<String, Object>();
			info_map.put("info", result_map);
			info_map.put("totalOilNum", totalOilNum);
			info_map.put("totalOilMoney", totalOilMoney);
			info_map.put("oilUseNum", oilUseNum);
			map.put(carNumber, info_map);
		}
		return map;
	}
	
	
	/**
	 * 根据当前登录人组织、日期范围集合、车辆类型 , 查询车辆信息(环比)
	 * @param date_list - 日期集合
	 * @param carType - 车类型
	 * @return (List<Map<String, String>>) 车辆信息集合
	 */
	public Map<String , Map<String,Object>> censusCarOilForRoundInTimes ( List<String> date_list ) {
		Map<String , Map<String,Object>> map = new LinkedHashMap<String, Map<String,Object>>();
		if ( date_list == null || date_list.size() == 0 ) {
			return map;
		}
		List<String> userBizId_list = new ArrayList<String>();
		String carBizId_string = "16";
		//获取当前登录人的业务单元
		String accountId = (String) SessionService.getInstance().getValueByKey("userId");
		//List<ProviderOrganization> orgByAccountAndOrgTypes = providerOrganizationService.getTopLevelOrgByAccount( accountId );
		List<SysOrg> orgByAccountAndOrgTypes = this.sysOrganizationService.getTopLevelOrgByAccount(accountId);
		if ( orgByAccountAndOrgTypes != null && orgByAccountAndOrgTypes.size() > 0 ) {
			carBizId_string = orgByAccountAndOrgTypes.get(0).getOrgId()+"";
		} else {
			carBizId_string = "16";
		}
		//递归查询下级组织id
		//List<ProviderOrganization> orgListDownwardByOrg = providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(Long.valueOf(carBizId_string),"BusinessOrganization");
		List<Map<String,Object>> orgListDownwardByOrg = this.sysOrganizationService.getProviderOrgByOrgIdAndOrgAttrByLevelService(Long.valueOf(carBizId_string),"BusinessOrganization","downward");

		for (int i = 0; i < orgListDownwardByOrg.size(); i++) {
			Map<String,Object> providerOrganization = orgListDownwardByOrg.get(i);
			userBizId_list.add(providerOrganization.get("orgId")+"");
		}
		
		for (int i = 0; i < date_list.size(); i++) {
			String date = date_list.get(i);
			List<Map<String, Object>> list = carCensusReportDao.censusCarOilForRoundInTimes(userBizId_list, date);
			Map<String,Object> data_map = new LinkedHashMap<String, Object>();
			Double totalOilNum = 0d;
			Double totalGpsMileage  = 0d;
			String oilUseNum  = "0";
			Map<String,Object> result_map = new LinkedHashMap<String, Object>();
			if ( list != null && list.size() > 0 ) {
				result_map = list.get(0);
				//判断是否有下级组织
				result_map.put("isLast", true);
				String oilNum = result_map.get("oilNum")+"";
				String oilMoney = result_map.get("oilMoney")+"";
				if ( !StringUtil.isNullOrEmpty(oilNum) && !StringUtil.isNullOrEmpty(oilMoney) ) {
					totalOilNum = Double.valueOf(oilNum);
					totalGpsMileage = Double.valueOf(oilMoney);
					Double useNum = ( totalGpsMileage / totalOilNum ) * 100;
					NumberFormat nf = NumberFormat.getInstance();
					nf.setGroupingUsed(false);
					nf.setMaximumFractionDigits(2);
					nf.setMinimumFractionDigits(1);
					oilUseNum = nf.format(useNum);
					result_map.put("oilUseNum", oilUseNum);
				}
			}
			data_map.put("totalOilNum", totalOilNum);
			data_map.put("totalOilMoney", totalGpsMileage);
			data_map.put("oilUseNum", oilUseNum);
			data_map.put("info", list );
			map.put(date, data_map);
		}
		return map;
	}
	
	
	
	/** 车辆油费统计 end ***************************************/
	
	
	/**
	 * 根据下级组织id,获取上级组织信息(不递归)
	 * @param orgId - 下级组织id
	 * @return (SysOrg) 上级组织信息
	 */
	public SysOrg findPreOrgByNextOrgId ( Long orgId ) {
		//List<ProviderOrganization> pro_list = this.providerOrganizationService.getUpProviderOrgByOrgIdService(orgId);
		//yuan.yw
		List<SysOrg> pro_list = this.sysOrganizationService.getUpOrNextLevelOrgByOrgIdService(orgId,"parent");

		SysOrg pro = null;
		if ( pro_list != null && pro_list.size() > 0 ) {
			pro = pro_list.get(0);
		}
		return pro;
	}
	
	
	/***************** getter setter *******************/
	public CarCensusReportDao getCarCensusReportDao() {
		return carCensusReportDao;
	}
	public void setCarCensusReportDao(CarCensusReportDao carCensusReportDao) {
		this.carCensusReportDao = carCensusReportDao;
	}
	
	public CardispatchWorkorderDao getCardispatchWorkorderDao() {
		return cardispatchWorkorderDao;
	}
	public void setCardispatchWorkorderDao(
			CardispatchWorkorderDao cardispatchWorkorderDao) {
		this.cardispatchWorkorderDao = cardispatchWorkorderDao;
	}
	public CardispatchTerminalDao getCardispatchTerminalDao() {
		return cardispatchTerminalDao;
	}
	public void setCardispatchTerminalDao(
			CardispatchTerminalDao cardispatchTerminalDao) {
		this.cardispatchTerminalDao = cardispatchTerminalDao;
	}
	public InformationManageNetworkResourceDao getInformationManageNetworkResourceDao() {
		return informationManageNetworkResourceDao;
	}
	public void setInformationManageNetworkResourceDao(
			InformationManageNetworkResourceDao informationManageNetworkResourceDao) {
		this.informationManageNetworkResourceDao = informationManageNetworkResourceDao;
	}
	public CardispatchCarDao getCardispatchCarDao() {
		return cardispatchCarDao;
	}
	public void setCardispatchCarDao(CardispatchCarDao cardispatchCarDao) {
		this.cardispatchCarDao = cardispatchCarDao;
	}

	public SysOrganizationDao getSysOrganizationDao() {
		return sysOrganizationDao;
	}

	public void setSysOrganizationDao(SysOrganizationDao sysOrganizationDao) {
		this.sysOrganizationDao = sysOrganizationDao;
	}
	
	
	
}
