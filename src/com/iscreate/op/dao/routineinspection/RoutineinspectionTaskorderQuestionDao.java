package com.iscreate.op.dao.routineinspection;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.routineinspection.RoutineinspectionTaskorderQuestion;

public interface RoutineinspectionTaskorderQuestionDao {
	/**
	 * 保存巡检问题与任务的关联关系
	 * @param routineinspectionTaskorderQuestion
	 */
	public long saveRoutineinspectionTaskorderQuestionDao(RoutineinspectionTaskorderQuestion routineinspectionTaskorderQuestion);
	
	/**
	 * 更新巡检问题与任务的关联关系
	 * @param routineinspectionTaskorderQuestion
	 */
	public void updateRoutineinspectionTaskorderQuestionDao(RoutineinspectionTaskorderQuestion routineinspectionTaskorderQuestion);
	
	/**
	 * 根据问题Id获取问题
	 * @param id
	 * @return
	 */
	public  List<Map> getRoutineinspectionTaskorderQuestionDao(final String id);
}
