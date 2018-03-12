package com.iscreate.op.dao.report;

import java.util.List;
import java.util.Map;

public interface CarCensusReportDao {

	/**
	 * 根据组织id、车辆类型,查询车辆信息
	 * @param carBizId - 车辆组织id
	 * @param carType - 车类型
	 * @return (List<Map<String, String>>) 车辆信息集合
	 */
	public abstract List<Map<String, Object>>  censusCarByBiz( List<String> carBizId , String[] carType);

	
	/**
	 * 根据车辆类型,查询车辆信息
	 * @param carType - 车类型
	 * @return (List<Map<String, String>>) 车辆信息集合
	 */
	public abstract List<Map<String, Object>> censusCarByCarType(String[] carType);

	/**
	 * 根据组织、日期范围、车辆类型 , 查询车辆里程信息(环比)
	 * @param bizIds - 组织id集合
	 * @param date - 日期
	 * @param carType - 车类型
	 * @return (List<Map<String, String>>) 车辆里程信息集合
	 */
	public abstract List<Map<String,Object>> censusCarMileageForRoundInTimes(String[] carType,
			String date , List<String> bizIds);


	/**
	 * 根据车辆类型、日期范围、组织id集合,查询车辆里程信息
	 * @param carType - 车辆类型
	 * @param date - 日期范围
	 * @param bizIds - 组织id集合
	 * @return (List<Map<String, String>>) 车辆里程信息集合
	 */
	public abstract List<Map<String,Object>> censusCarMileageInTimes(String[] carType, String date,
			List<String> bizIds);

	/**
	 * 根据车牌、日期 , 查询车辆里程信息(对比)
	 * @param bizIds - 组织id集合
	 * @param date - 日期
	 * @param carType - 车类型
	 * @return (List<Map<String, String>>) 车辆里程信息集合
	 */
	public abstract List<Map<String,Object>> censusCarMileageByCarNumberInTimes(String carNumber,
			String date);


	public abstract List<Map<String, Object>> censusBizOilByBiz(List<String> bizIds,
			String time);


	public abstract List<Map<String, Object>> censusCarOilByBiz( String time,
			String carNumber);


	public abstract List<Map<String, Object>> censusCarOilForRoundInTimes(List<String> bizIds, String time);


	public abstract List<Map<String,Object>> censusCarMileageInTime(String carNumber, String time);


	public abstract List<Map<String,Object>> censusCarMileageInTimes(String[] carType, String startDate,
			String endDate, List<String> bizIds);

}
