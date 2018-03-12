package com.iscreate.op.service.cardispatch;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface CarFuelReportService {
	/**
	 * 
	 * @description: 根据组织id分页获取时间月份的车辆费用信息
	 * @author：yuan.yw
	 * @param orgId
	 * @param currentPage
	 * @param pageSize
	 * @param date
	 * @return     
	 * @return Map<String,Object>    
	 * @date：Aug 2, 2013 11:42:42 AM
	 */
	public Map<String,Object> getCarFuelInfoForPage(String orgId,int currentPage,int pageSize,String date);
	/**
	 * 
	 * @description: 导出油费报表
	 * @author：yuan.yw
	 * @param orgId
	 * @param orgName
	 * @param date
	 * @return     
	 * @return InputStream     
	 * @date：Aug 5, 2013 10:28:20 AM
	 */
	public InputStream exportCarFuelBillsService(String orgId,String orgName,String date);
}
