package com.iscreate.plat.gantt.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.op.constant.WorkManageConstant;
import com.iscreate.op.service.workmanage.WorkManageService;
import com.iscreate.plat.tools.TimeFormatHelper;

public class GanttServiceImpl implements GanttService {
	
	private WorkManageService workManageService;	//工作管理接口
	
	
	
	
	public WorkManageService getWorkManageService() {
		return workManageService;
	}

	public void setWorkManageService(WorkManageService workManageService) {
		this.workManageService = workManageService;
	}

	/**
	 * 获取车辆某一天工单甘特列表
	 * @param carId 车辆Id
	 * @param taskDate 任务日期
	 * @return
	 */
	public List<Map> getCarWorkOrderGantt(String carId, String taskDate) {
		List<Map> resultList = new ArrayList<Map>();
		String beginTime = taskDate+" 00:00:00";
		String endTime = taskDate+" 23:59:59";
		String conditionString = "and \"createTime\" between to_date('"+beginTime+"','yyyy-mm-dd hh24:mi:ss') and to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss')";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//获取车辆待办车辆调度工单
		List<Map> taskList = workManageService.getWorkOrderListByResourceTypeAndResourceId("car", carId, "cardispatch", "pendingWorkOrder", conditionString);
		if(taskList!=null && !taskList.isEmpty()){
			for (Map map : taskList) {
				Map m = new HashMap();
				beginTime = (String)map.get("planUseCarTime");
				endTime = (String)map.get("planReturnCarTime");
				String title = (String)map.get("woTitle");
				
				int bTime = 0;
				int eTime = 0;
				if(beginTime!=null && !"".equals(beginTime)){
					Date begin_time;
					try {
						begin_time = sdf.parse(beginTime);
						beginTime = TimeFormatHelper.getTimeFormatByFree(beginTime, "yyyy-MM-dd HH:mm:ss");
						if(begin_time!=null){
							bTime = begin_time.getHours();
							double judgeMinutes = this.judgeMinutes(begin_time.getMinutes());
							bTime+=judgeMinutes;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(endTime!=null && !"".equals(endTime)){
					Date end_time;
					try {
						end_time = sdf.parse(endTime);
						endTime = TimeFormatHelper.getTimeFormatByFree(endTime, "yyyy-MM-dd HH:mm:ss");
						if(end_time!=null){
							eTime = end_time.getHours();
							double judgeMinutes = this.judgeMinutes(end_time.getMinutes());
							eTime+=judgeMinutes;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				m.put("begin_time", bTime);
				m.put("end_time", eTime);
				m.put("title", title+"，"+beginTime+"，"+endTime);
				resultList.add(m);
			}
		}
		return resultList;
	}

	/**
	 * 获取人员某一天任务甘特列表
	 * @param staffId 人员Id
	 * @param taskDate 任务日期
	 * @return
	 */
	public List<Map> getStaffTaskGantt(String staffId, String taskDate) {
		List<Map> resultList = new ArrayList<Map>();
		String beginTime = taskDate+" 00:00:00";
		String endTime = taskDate+" 23:59:59";
		String conditionString = "and \"assignTime\" between to_date('"+beginTime+"','yyyy-mm-dd hh24:mi:ss') and to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss')";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//获取人员待办抢修任务单
		Map<String, Object> userTaskOrders = workManageService.getUserTaskOrders(staffId, "urgentrepair", "pendingTaskOrder", null, null,conditionString);
		List<Map> taskList = null;
		if(userTaskOrders!=null){
			taskList = (List<Map>) userTaskOrders.get("entityList");
		}
		if(taskList!=null && !taskList.isEmpty()){
			for (Map map : taskList) {
				Map m = new HashMap();
				beginTime = sdf.format(map.get("assignTime"));//(String)map.get("assignTime");
				endTime = sdf.format(map.get("requireCompleteTime"));//(String)map.get("requireCompleteTime");
				String title = (String)map.get("toTitle");
				
				int bTime = 0;
				int eTime = 0;
				if(beginTime!=null && !"".equals(beginTime)){
					Date begin_time;
					try {
						begin_time = sdf.parse(beginTime);
						beginTime = TimeFormatHelper.getTimeFormatByFree(beginTime, "yyyy-MM-dd HH:mm:ss");
						if(begin_time!=null){
							bTime = begin_time.getHours();
							double judgeMinutes = this.judgeMinutes(begin_time.getMinutes());
							bTime+=judgeMinutes;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(endTime!=null && !"".equals(endTime)){
					Date end_time;
					try {
						end_time = sdf.parse(endTime);
						endTime = TimeFormatHelper.getTimeFormatByFree(endTime, "yyyy-MM-dd HH:mm:ss");
						if(end_time!=null){
							eTime = end_time.getHours();
							double judgeMinutes = this.judgeMinutes(end_time.getMinutes());
							eTime+=judgeMinutes;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				m.put("begin_time", bTime);
				m.put("end_time", eTime);
				m.put("title", title+"，"+beginTime+"，"+endTime);
				resultList.add(m);
			}
		}
		return resultList;
	}
	
	/**
	 * 获取站址某一天工单甘特列表
	 * @param stationId 站址Id
	 * @param taskDate 任务日期
	 * @return
	 */
	public List<Map> getStationWorkOrderGantt(String stationId, String taskDate) {
		List<Map> resultList = new ArrayList<Map>();
		String beginTime = taskDate+" 00:00:00";
		String endTime = taskDate+" 23:59:59";
		String conditionString = "and \"assignTime\" between to_date('"+beginTime+"','yyyy-mm-dd hh24:mi:ss') and to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss')";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//获取站址待办工单
		List<Map> taskList = workManageService.getWorkOrderListByResourceTypeAndResourceId("station", stationId, "urgentrepair", "pendingWorkOrder", conditionString);
		if(taskList!=null && !taskList.isEmpty()){
			for (Map map : taskList) {
				Map m = new HashMap();
				beginTime = (String)map.get("assignTime");
				endTime = (String)map.get("requireCompleteTime");
				String title = (String)map.get("toTitle");
				
				int bTime = 0;
				int eTime = 0;
				if(beginTime!=null && !"".equals(beginTime)){
					Date begin_time;
					try {
						begin_time = sdf.parse(beginTime);
						beginTime = TimeFormatHelper.getTimeFormatByFree(beginTime, "yyyy-MM-dd HH:mm:ss");
						if(begin_time!=null){
							bTime = begin_time.getHours();
							double judgeMinutes = this.judgeMinutes(begin_time.getMinutes());
							bTime+=judgeMinutes;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(endTime!=null && !"".equals(endTime)){
					Date end_time;
					try {
						end_time = sdf.parse(endTime);
						endTime = TimeFormatHelper.getTimeFormatByFree(endTime, "yyyy-MM-dd HH:mm:ss");
						if(end_time!=null){
							eTime = end_time.getHours();
							double judgeMinutes = this.judgeMinutes(end_time.getMinutes());
							eTime+=judgeMinutes;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				m.put("begin_time", bTime);
				m.put("end_time", eTime);
				m.put("title", title+"，"+beginTime+"，"+endTime);
				resultList.add(m);
			}
		}
		return resultList;
	}

	/**
	 * 获取资源任务甘特列表
	 * @param resourceId 资源Id
	 * @param resourceType 资源类型：car、people、station、baseStation
	 * @param taskDate 任务日期
	 * @param isOverTime 是否超时
	 * @return
	 */
	public List<Map> getResourceTaskGanttService(String resourceId,String resourceType, String taskDate,int isOverTime) {
		//获取不同类型资源的任务甘特信息
		if(resourceType.toLowerCase().equals("car")){
			return this.getCarWorkOrderGantt(resourceId, taskDate);
		}else if(resourceType.toLowerCase().equals("people")){
			return this.getStaffTaskGantt(resourceId, taskDate);
		}else if(resourceType.toLowerCase().equals("station")){
			return this.getStationWorkOrderGantt(resourceId, taskDate);
		}else{
			return this.getBaseStationWorkOrderGantt(resourceId, resourceType, taskDate ,isOverTime);
		}
	}

	/**
	 * 获取资源某月任务甘特信息
	 * @param resourceId 资源Id
	 * @param resourceType 资源类型
	 * @param taskDate 格式：2012-11 
	 * @param endDate 某月最后一天
	 * @param isOverTime 是否超时
	 * @return
	 */
	public Map getResourceMonthTaskGanttService(String resourceId,String resourceType, String taskDate, String endDate,int isOverTime) {
		Map resMap = new HashMap();	//按日期存储
		int beginDate = 1;	//每个月第一天为1号
		int endNum = Integer.valueOf(endDate);
		for(int i=beginDate;i<=endNum;i++){
			Map m = new HashMap();
			String tDate = taskDate+i;
			boolean res = false;
			//获取不同资源任务信息
			if(resourceType.toLowerCase().equals("car")){
				List<Map> taskList = this.getCarWorkOrderGantt(resourceId, tDate);
				if(taskList!=null && !taskList.isEmpty()){
					res = true;
				}
			}else if(resourceType.toLowerCase().equals("people")){
				List<Map> taskList = this.getStaffTaskGantt(resourceId, tDate);
				if(taskList!=null && !taskList.isEmpty()){
					res = true;
				}
			}else if(resourceType.toLowerCase().equals("station")){
				List<Map> taskList = this.getStationWorkOrderGantt(resourceId, tDate);
				if(taskList!=null && !taskList.isEmpty()){
					res = true;
				}
			}else{
				List<Map> taskList = this.getBaseStationWorkOrderGantt(resourceId, resourceType, taskDate, isOverTime);
				if(taskList!=null && !taskList.isEmpty()){
					res = true;
				}
			}
			m.put("taskDate", tDate);
			m.put("date", i);
			m.put("hasTask", ""+res);
			resMap.put(i, m);
		}
		return resMap;
	}

	/**
	 * 获取基站某天工单甘特列表
	 * @param baseStationId 基站Id
	 * @param baseStationType 基站类型
	 * @param taskDate 任务日期
	 * @param isOverTime 是否超时
	 * @return
	 */
	public List<Map> getBaseStationWorkOrderGantt(String baseStationId,String baseStationType,String taskDate,int isOverTime) {
		List<Map> resultList = new ArrayList<Map>();
		String beginTime = taskDate+" 00:00:00";
		String endTime = taskDate+" 23:59:59";
		String conditionString = "and \"assignTime\" between to_date('"+beginTime+"','yyyy-mm-dd hh24:mi:ss') and to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss')";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(isOverTime == 1){
			conditionString = conditionString + " and "+isOverTime;
		}
		//获取基站办工单
		List<Map> taskList = workManageService.getWorkOrderListByResourceTypeAndResourceId(baseStationType, baseStationType, "urgentrepair", "pendingWorkOrder", conditionString);
		if(taskList!=null && !taskList.isEmpty()){
			for (Map map : taskList) {
				Map m = new HashMap();
				beginTime = (String)map.get("assignTime");
				endTime = (String)map.get("requireCompleteTime");
				String title = (String)map.get("woTitle");
				
				int bTime = 0;
				int eTime = 0;
				if(beginTime!=null && !"".equals(beginTime)){
					Date begin_time;
					try {
						begin_time = sdf.parse(beginTime);
						beginTime = TimeFormatHelper.getTimeFormatByFree(beginTime, "yyyy-MM-dd HH:mm:ss");
						if(begin_time!=null){
							bTime = begin_time.getHours();
							double judgeMinutes = this.judgeMinutes(begin_time.getMinutes());
							bTime+=judgeMinutes;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(endTime!=null && !"".equals(endTime)){
					Date end_time;
					try {
						end_time = sdf.parse(endTime);
						endTime = TimeFormatHelper.getTimeFormatByFree(endTime, "yyyy-MM-dd HH:mm:ss");
						if(end_time!=null){
							eTime = end_time.getHours();
							double judgeMinutes = this.judgeMinutes(end_time.getMinutes());
							eTime+=judgeMinutes;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				m.put("begin_time", bTime);
				m.put("end_time", eTime);
				m.put("title", title+"，"+beginTime+"，"+endTime);
				resultList.add(m);
			}
		}
		return resultList;
	}
	
	/**
	 * 判断 分钟数 是否大于30
	 * @return
	 */
	private double judgeMinutes(int minutes){
		if(minutes>=30){
			return 0.5;
		}
		return 0;
	}

}
