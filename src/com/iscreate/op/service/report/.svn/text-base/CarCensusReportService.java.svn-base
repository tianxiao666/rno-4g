package com.iscreate.op.service.report;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.system.SysOrg;

public interface CarCensusReportService {

	/**
	 * 根据组织id、车辆类型,查询车辆信息
	 * @param carBizId - 车辆组织id
	 * @param carType - 车类型
	 * @return (List<Map<String, String>>) 车辆信息集合
	 */
	public abstract Map<String, Map<String,Object>> censusCarCountByBiz(Long carBizId, String[] carType);

	
	/**
	 * 根据组织id、车辆类型,查询车辆信息
	 * @param carBizId - 车辆组织id
	 * @param carType - 车类型
	 * @param beginTime - 起始时间
	 * @param endTime - 结束时间
	 * @return 
	 * @return (List<Map<String, String>>) 车辆信息集合
	 */
	public abstract Map<String, Map<String, Object>> censusCarMileageByBiz(Long carBizId, String[] carType,
			String time);

	/**
	 * 根据下级组织id,获取上级组织信息(不递归)
	 * @param orgId - 下级组织id
	 * @return (SysOrg) 上级组织信息
	 */
	public abstract SysOrg findPreOrgByNextOrgId(Long orgId);

	/**
	 * 根据当前登录人组织、日期范围集合、车辆类型 , 查询车辆信息(环比)
	 * @param date_list - 日期集合
	 * @param carType - 车类型
	 * @return (List<Map<String, String>>) 车辆信息集合
	 */
	public abstract Map<String , Map<String,Object>> censusCarMileageForRoundInTimes(List<String> date_list,
			String[] carType);


	public abstract Map<String, Map<String, Object>> censusCarOilByBiz(Long carBizId, String time);


	public abstract Map<String , Map<String,Object>> censusCarOilForRoundInTimes(List<String> date_list);


	public abstract Map<String, Map<String, Object>> censusCarCountMileageByBiz(Long carBizId,
			String[] carType, String time);


	public abstract Map<String , Map<String,Object>> censusCarCountMileageForRoundInTimes(List<String> date_list,
			String[] carType);

	/**
	 * 根据项目 , 统计车辆数量
	 * @param carType - 车辆类型
	 * @return
	 */
	public abstract Map<String,Map<String, Object>> censusCarCountByProject(String[] carType);


	public abstract Map<String, Map<String, Object>> censusCarMileageByProject(String[] carType, String time);
	
}
