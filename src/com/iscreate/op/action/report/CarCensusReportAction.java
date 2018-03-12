package com.iscreate.op.action.report;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.iscreate.op.action.informationmanage.common.ActionUtil;

import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.report.CarCensusReportService;
import com.iscreate.op.service.system.SysOrganizationService;

/**
 * 车辆统计报表
 * @author andy
 */
@SuppressWarnings({"rawtypes","unchecked","unused","hiding"})
public class CarCensusReportAction {
	
	private final static String defaultBizId = "16";
	private final static String[] defaultCarType = new String[]{"面包车","小轿车","小货车"};
	
	/************ 依赖注入 ***********/
	private CarCensusReportService carCensusReportService;
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	/***************** 属性 *******************/

	
	/****************** action *******************/
	
	
	/** 车辆数量统计 begin *************************************/
	
	/**
	 * 按组织,统计车辆数量
	 * @param carBizId - 车辆组织id
	 */
	public void censusCarCountByBizAction () {
		//获取参数
		HttpServletRequest request = ServletActionContext.getRequest();
		String bizId = request.getParameter("carBizId");
		if ( bizId == null || bizId.isEmpty() ) {
			bizId = defaultBizId;
		}
		Long carBizId = Long.valueOf(bizId);
		String[] carType = request.getParameterValues("carType");
		if ( carType == null || carType.length == 0  ) {
			carType = defaultCarType;
		}
		Map<String, Map<String, Object>> result_map = carCensusReportService.censusCarCountByBiz(carBizId, carType);
		try {
			ActionUtil.responseWrite(result_map,false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 按项目,统计车辆数量
	 */
	public void censusCarCountByProjectAction () {
		//获取参数
		HttpServletRequest request = ServletActionContext.getRequest();
		String[] carType = request.getParameterValues("carType");
		if ( carType == null || carType.length == 0  ) {
			carType = defaultCarType;
		}
		String time = request.getParameter("time");
		Map<String, Map<String, Object>> result_map = carCensusReportService.censusCarCountByProject( carType );
		try {
			ActionUtil.responseWrite(result_map);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/** 车辆数量统计 end *************************************/

	
	/** 车辆里程统计 begin *************************************/
	
	/**
	 * 按组织,统计车辆里程
	 * @param carBizId - 车辆组织id
	 * @param beginTime - 起始时间
	 * @param endTime - 结束时间
	 */
	public void censusCarMileageByBizAction () {
		//获取参数
		HttpServletRequest request = ServletActionContext.getRequest();
		String bizId = request.getParameter("carBizId");
		if ( bizId == null || bizId.isEmpty() ) {
			bizId = defaultBizId;
		}
		Long carBizId = Long.valueOf(bizId);
		String[] carType = request.getParameterValues("carType");
		if ( carType == null || carType.length == 0  ) {
			carType = defaultCarType;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String time = request.getParameter("time");
		if ( time == null || time.isEmpty()  ) {
			time = sdf.format(new Date());
		}
		Map<String, Map<String, Object>> result_map = carCensusReportService.censusCarMileageByBiz( carBizId , carType , time );
		try {
			ActionUtil.responseWrite(result_map,false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 按组织,统计车辆里程
	 * @param carBizId - 车辆组织id
	 * @param beginTime - 起始时间
	 * @param endTime - 结束时间
	 */
	public void censusCarMileageByProjectAction () {
		//获取参数
		HttpServletRequest request = ServletActionContext.getRequest();
		String[] carType = request.getParameterValues("carType");
		if ( carType == null || carType.length == 0  ) {
			carType = defaultCarType;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String time = request.getParameter("time");
		if ( time == null || time.isEmpty()  ) {
			time = sdf.format(new Date());
		}
		Map<String, Map<String, Object>> result_map = carCensusReportService.censusCarMileageByProject( carType , time );
		try {
			ActionUtil.responseWrite(result_map,false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 按组织,统计车辆里程(环比)
	 */
	public void censusCarMileageForRoundInTimesAction () {
		//获取参数
		HttpServletRequest request = ServletActionContext.getRequest();
		String[] carType = request.getParameterValues("carType");
		if ( carType == null || carType.length == 0  ) {
			carType = defaultCarType;
		}
		String months = request.getParameter("months");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		List<String> date_list = gson.fromJson(months, new TypeToken<List<String>>(){}.getType());
		Map<String, Map<String, Object>> result_map = carCensusReportService.censusCarMileageForRoundInTimes(date_list, carType);
		try {
			ActionUtil.responseWrite(result_map,false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** 车辆里程统计 end *************************************/
	
	/** 车辆数量里程综合统计 begin *************************************/
	
	/**
	 * 按组织,统计车辆数量里程(对比)
	 * @param carBizId - 车辆组织id
	 * @param beginTime - 起始时间
	 * @param endTime - 结束时间
	 */
	public void censusCarCountMileageByBizAction () {
		//获取参数
		HttpServletRequest request = ServletActionContext.getRequest();
		String bizId = request.getParameter("carBizId");
		if ( bizId == null || bizId.isEmpty() ) {
			bizId = defaultBizId;
		}
		Long carBizId = Long.valueOf(bizId);
		String[] carType = request.getParameterValues("carType");
		if ( carType == null || carType.length == 0  ) {
			carType = defaultCarType;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String time = request.getParameter("time");
		if ( time == null || time.isEmpty()  ) {
			time = sdf.format(new Date());
		}
		Map<String, Map<String, Object>> result_map = carCensusReportService.censusCarCountMileageByBiz( carBizId , carType , time );
		try {
			ActionUtil.responseWrite(result_map,false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 按组织,统计车辆里程(环比)
	 */
	public void censusCarCountMileageForRoundInTimesAction () {
		//获取参数
		HttpServletRequest request = ServletActionContext.getRequest();
		String[] carType = request.getParameterValues("carType");
		if ( carType == null || carType.length == 0  ) {
			carType = defaultCarType;
		}
		String months = request.getParameter("months");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		List<String> date_list = gson.fromJson(months, new TypeToken<List<String>>(){}.getType());
		Map<String, Map<String, Object>> result_map = carCensusReportService.censusCarCountMileageForRoundInTimes(date_list, carType);
		try {
			ActionUtil.responseWrite(result_map,false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 按项目,统计车辆数量
	 */
	public void censusCarCountMileageByProjectAction () {
		//获取参数
		HttpServletRequest request = ServletActionContext.getRequest();
		String[] carType = request.getParameterValues("carType");
		if ( carType == null || carType.length == 0  ) {
			carType = defaultCarType;
		}
		String time = request.getParameter("time");
		Map<String, Map<String, Object>> result_map = carCensusReportService.censusCarCountByProject( carType );
		try {
			ActionUtil.responseWrite(result_map);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** 车辆数量里程综合统计 end *************************************/
	
	
	/** 车辆油费统计 begin *************************************/
	
	/**
	 * 对比 - 统计车辆油费(根据组织)
	 */
	public void censusCarOilByBizAction () {
		//获取参数
		HttpServletRequest request = ServletActionContext.getRequest();
		String bizId = request.getParameter("carBizId");
		if ( bizId == null || bizId.isEmpty() ) {
			bizId = defaultBizId;
		}
		Long carBizId = Long.valueOf(bizId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String time = request.getParameter("time");
		if ( time == null || time.isEmpty()  ) {
			time = sdf.format(new Date());
		}
		Map<String, Map<String, Object>> result_map = carCensusReportService.censusCarOilByBiz( carBizId , time );
		try {
			ActionUtil.responseWrite(result_map,false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 对比 - 统计车辆油费(根据项目)
	 */
	public void censusCarOilByProjectAction () {
		
	}
	
	/**
	 * 环比 - 统计车辆油费(根据组织)
	 */
	public void censusCarOilForRoundInTimesAction () {
		//获取参数
		HttpServletRequest request = ServletActionContext.getRequest();
		String months = request.getParameter("months");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		List<String> date_list = gson.fromJson(months, new TypeToken<List<String>>(){}.getType());
		Map<String, Map<String, Object>> result_map = carCensusReportService.censusCarOilForRoundInTimes(date_list);
		try {
			ActionUtil.responseWrite(result_map,false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** 车辆油费统计 end *************************************/
	
	
	
	
	/********************* 公共 action **********************/
	
	/**
	 * 根据下级组织id,获取上级组织信息(不递归)
	 * @param orgId - 下级组织id
	 */
	public void findPreOrgByNextOrgId () {
		HttpServletRequest request = ServletActionContext.getRequest();
		String orgId = request.getParameter("orgId");
		if ( orgId == null || orgId.isEmpty() ) {
			orgId = defaultBizId;
		}
		SysOrg pro = this.carCensusReportService.findPreOrgByNextOrgId(Long.valueOf(orgId));
		try {
			ActionUtil.responseWrite(pro,false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取当前登录人id
	 */
	public void getLoginUserBizId () {
		//获取当前登录人的业务单元
		String accountId = (String) SessionService.getInstance().getValueByKey("userId");
		//List<ProviderOrganization> list = providerOrganizationService.getTopLevelOrgByAccount(accountId);
		List<SysOrg> list = this.sysOrganizationService.getTopLevelOrgByAccount(accountId);
		try {
			if ( list != null && list.size() > 0 ) {
				ActionUtil.responseWrite(list.get(0),false);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/***************** getter setter *******************/
	public CarCensusReportService getCarCensusReportService() {
		return carCensusReportService;
	}
	public void setCarCensusReportService(
			CarCensusReportService carCensusReportService) {
		this.carCensusReportService = carCensusReportService;
	}

	
	
	
	
}
