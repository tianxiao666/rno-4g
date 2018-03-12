package com.iscreate.op.service.routineinspection;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.routineinspection.RoutineinspectionExample;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionRecordExample;

public interface RoutineInspectionTaskRecordService {

	/**
	 * 根据专业和任务单ID获取巡检资源
	 * @param profession
	 * @return
	 */
	public Map<String, List<Map<String, String>>> getRoutineInspectionResourceByProfessionAndToIdService(String profession,String toId,String terminalType);
	
	/**
	 * 根据资源Id资源类型工单Id获取巡检记录模板
	 * @param reId
	 * @param reType
	 * @param woId
	 * @return
	 */
	public List<Map<String,String>> getRoutineInspectionRecordTemplateByReIdAndReTypeAndWoIdService(String reId,String reType,String woId,String terminalType);
	
	/**
	 * 保存巡检实例
	 * @param routineinspectionExample
	 * @return
	 */
	public boolean txSaveRoutineInspectionExampleService(RoutineinspectionExample routineinspectionExample);
	
	/**
	 * 保存巡检内容
	 * @param routineinspectionExample
	 * @return
	 */
	public boolean txSaveRoutineInspectionRecordService(RoutineinspectionRecordExample routineinspectionRecordExample);
	
	/**
	 * 根据资源Id、资源类型、任务单Id和巡检内容模板Id获取巡检内容实例
	 * @param reId
	 * @param reType
	 * @param toId
	 * @param templateId
	 * @return
	 */
	public RoutineinspectionRecordExample getRoutineinspectionRecordExampleByReIdAndReTypeAndToIdAndTemplateIdService(String reId,String reType,String toId,long templateId);
	
	/**
	 * 修改巡检内容
	 * @param re
	 */
	public boolean txUpdateRoutineInspectionRecordExampleService(RoutineinspectionRecordExample routineinspectionRecordExample);
	
	/**
	 * 根据资源Id、资源类型、任务单Id获取巡检实例
	 * @param reId
	 * @param reType
	 * @param toId
	 * @return
	 */
	public RoutineinspectionExample getRoutineInspectionExampleByReIdAndReTypeAndToIdService(String reId,String reType,String toId);
	
	/**
	 * 根据资源Id、资源类型、任务单Id获取巡检模板实例
	 * @param reId
	 * @param reType
	 * @param toId
	 * @return
	 */
	public List<Map<String,String>> getRoutineinspectionRecordExampleByReIdAndReTypeAndToIdService(String reId,String reType,String toId);
	
	/**
	 * 保存巡检内容
	 * @param reId
	 * @param reType
	 * @param woId
	 * @param toId
	 * @param tempIdJsonStr
	 * @param valueJsonStr
	 * @param remarkJsonStr
	 * @return
	 */
	public boolean txSaveRoutineInspectionRecordService(String reId,String reType,String woId,String toId,String tempIdJsonStr,String valueJsonStr,String remarkJsonStr);
}
