package com.iscreate.op.dao.cardispatch;

import java.util.List;
import java.util.Map;

public interface CarFuelReportDao {
	/**
	 * 
	 * @description: 根据组织id查询组织的车辆信息（包括下级组织）(分页)
	 * @author：yuan.yw
	 * @param orgId
	 * @param indexStart
	 * @param indexEnd
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 2, 2013 11:29:09 AM
	 */
	public List<Map<String,Object>> getCarInfoByOrgId(String orgId,int indexStart,int indexEnd);
	/**
	 * 
	 * @description: 根据时间（2013-07-12），组织id，车辆id字符串获取车辆费用相关信息
	 * @author：yuan.yw
	 * @param dateString
	 * @param carIds
	 * @param orgId
	 * @return
	 * @return List<Map<String,Object>>
	 * @date：Aug 2, 2013 11:08:25 AM
	 */
	public List<Map<String, Object>> getCarFuelInfoByDateAndCarId(
			String[] dateString, String carIds,String orgId);
	/**
	 * 
	 * @description: 根据时间（2013-07-12），组织id获取车辆费用相关信息
	 * @author：yuan.yw
	 * @param dateString
	 * @param orgId
	 * @return
	 * @return List<Map<String,Object>>
	 * @date：Aug 2, 2013 11:08:25 AM
	 */
	public List<Map<String, Object>> getCarFuelInfoByDateAndOrgId(
			String[] dateString, String orgId);
}
