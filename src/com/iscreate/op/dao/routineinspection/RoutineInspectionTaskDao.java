package com.iscreate.op.dao.routineinspection;

import java.util.List;

import com.iscreate.op.pojo.routineinspection.RoutineinspectionExample;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionTaskorder;

public interface RoutineInspectionTaskDao {
	
	/**
	 * 根据TOID获取任务单信息
	 * @param toId
	 */
	public RoutineinspectionTaskorder getRoutineInspectionTaskByToId(String toId);
	
	/**
	 * 保存任务单信息
	 * @param routineinspectionTaskorder
	 * @return
	 */
	public boolean saveRoutineInspectionTaskOrder(RoutineinspectionTaskorder routineinspectionTaskorder);
	
	/**
	 * 修改任务单信息
	 * @param routineinspectionTaskorder
	 * @return
	 */
	public boolean updateRoutineInspectionTaskOrder(RoutineinspectionTaskorder routineinspectionTaskorder);
	
	/**
	 * 根据WOID获取任务单信息
	 * @param toId
	 */
	public List<RoutineinspectionTaskorder> getRoutineInspectionTaskByWoId(String woId);
	
	/**
	 * 根据工单号获取平均偏离距离数
	 * @param orgId
	 * @return
	 */
	public float getTaskOrderAvgDeviateByWoId(String woId);
}
