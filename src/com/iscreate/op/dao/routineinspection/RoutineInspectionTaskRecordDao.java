package com.iscreate.op.dao.routineinspection;

import java.util.List;

import com.iscreate.op.pojo.routineinspection.RoutineinspectionExample;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionRecordExample;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionRecordTemplate;

public interface RoutineInspectionTaskRecordDao {

	/**
	 * 保存巡检实例
	 * @param re
	 */
	public boolean saveRoutineInspectionExample(RoutineinspectionExample routineinspectionExample);
	
	/**
	 * 根据资源Id、资源类型、任务单Id获取巡检实例
	 * @param reId
	 * @param reType
	 * @param toId
	 * @return
	 */
	public RoutineinspectionExample getRoutineInspectionExampleByReIdAndReTypeAndToId(String reId,String reType,String toId);
	
	/**
	 * 根据资源Id、资源类型、模板Id和终端类型获取巡检记录模板
	 * @param reId
	 * @param reType
	 * @param templateId
	 * @param terminalType
	 * @return
	 */
	public List<RoutineinspectionRecordTemplate> getRoutineInspectionTemplateByReIdAndReTypeAndTemplateIdAndTerminalType(String reId,String reType,long templateId,String terminalType);
	
	/**
	 * 保存巡检内容
	 * @param re
	 */
	public boolean saveRoutineInspectionRecordExample(RoutineinspectionRecordExample routineinspectionRecordExample);
	
	/**
	 * 根据资源Id、资源类型、任务单Id和巡检内容模板Id获取巡检内容实例
	 * @param reId
	 * @param reType
	 * @param toId
	 * @param templateId
	 * @return
	 */
	public RoutineinspectionRecordExample getRoutineinspectionRecordExampleByReIdAndReTypeAndToIdAndTemplateId(String reId,String reType,String toId,long templateId);
	
	/**
	 * 修改巡检内容
	 * @param re
	 */
	public boolean updateRoutineInspectionRecordExample(RoutineinspectionRecordExample routineinspectionRecordExample);
	
	/**
	 * 根据资源Id、资源类型、任务单Id获取巡检内容实例
	 * @param reId
	 * @param reType
	 * @param toId
	 * @return
	 */
	public List<RoutineinspectionRecordExample> getRoutineinspectionRecordExampleByReIdAndReTypeAndToId(String reId,String reType,String toId);
}
