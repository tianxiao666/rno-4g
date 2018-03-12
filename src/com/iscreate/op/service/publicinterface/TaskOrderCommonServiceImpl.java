package com.iscreate.op.service.publicinterface;

import java.util.List;
import java.util.Map;

import com.iscreate.op.service.workmanage.WorkManageService;

public class TaskOrderCommonServiceImpl implements TaskOrderCommonService {
	private WorkManageService workManageService;	
	/**
	 * 检查任务单的下级任务是否已全部结束
	 * @param toId
	 * @return
	 */
	public boolean hasAllSubTasksFinishedByToId(String toId, String currentToId){
		//获取任务单的下级任务
		List<Map> subToMaps = workManageService.getChildTaskOrderListByToId(toId);
		//当下级任务不为空
		if(subToMaps!=null&&!subToMaps.isEmpty()){
			//遍历下级任务
			for(Map subToMap : subToMaps){
				if(!currentToId.equals(subToMap.get("toId"))){
					//当发现下级任务有状态为未结束的任务时，返回false
					if(!"已结束".equals(subToMap.get("statusName"))&&!"已撤销".equals(subToMap.get("statusName"))){
						return false;
					}
				}
				
			}
		}
		return true;
	}
	
	/**
	 * 检查任务单的下级任务是否已全部结束
	 * @param toId
	 * @return
	 */
	public boolean hasAllSubTasksFinishedByToId(String toId){
		//获取任务单的下级任务
		List<Map> subToMaps = workManageService.getChildTaskOrderListByToId(toId);
		//当下级任务不为空
		if(subToMaps!=null&&!subToMaps.isEmpty()){
			//遍历下级任务
			for(Map subToMap : subToMaps){
				//当发现下级任务有状态为未结束的任务时，返回false
				if(!"已结束".equals(subToMap.get("statusName"))&&!"已撤销".equals(subToMap.get("statusName"))){
					return false;
				}
				
			}
		}
		return true;
	}
	public WorkManageService getWorkManageService() {
		return workManageService;
	}
	public void setWorkManageService(WorkManageService workManageService) {
		this.workManageService = workManageService;
	}
	
	
}
