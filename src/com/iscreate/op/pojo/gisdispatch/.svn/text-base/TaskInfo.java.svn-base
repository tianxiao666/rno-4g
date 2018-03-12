package com.iscreate.op.pojo.gisdispatch;

import java.util.List;

public class TaskInfo {

	private int totalTaskCount;
	private List<TaskDetailInfo> taskDetailInfos;

	public int getTotalTaskCount() {
		return totalTaskCount;
	}

	public void setTotalTaskCount(int totalTaskCount) {
		this.totalTaskCount = totalTaskCount;
	}

	

	public List<TaskDetailInfo> getTaskDetailInfo() {
		return taskDetailInfos;
	}

	public void setTaskDetailInfo(List<TaskDetailInfo> taskDetailInfos) {
		this.taskDetailInfos = taskDetailInfos;
	}

	/**
	 * 返回json格式
	 * @return
	 * Author gmh
	 * 2012-3-22 下午03:52:54
	 */
	public String toJson() {
		String result = "{totalTaskCount:" + totalTaskCount; 
		if(taskDetailInfos!=null && taskDetailInfos.size()>0){
			result+= ",taskDetailInfos:[";
			for(TaskDetailInfo taskDetailInfo:taskDetailInfos){
				 result+= taskDetailInfo.toJson() +",";
			}
			result=result.substring(0,result.length()-1);
			result+="]";
		}
		result+="}";
		return result;
	}
}
