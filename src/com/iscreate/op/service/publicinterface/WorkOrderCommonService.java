package com.iscreate.op.service.publicinterface;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.urgentrepair.UrgentrepairWorkorder;

public interface WorkOrderCommonService {
	
	
	/**
	 * 获取抢修工单涉及的所有信息
	 * */
	public String getAllUrgentRepairWorkOrderInfoService();
	
	
	/**
	 * 获取工单信息
	 * */
	public Map getUrgentRepairWorkOrderService(String woId);
	
	/**
	 * 获取客户工单信息
	 * */
	public Map getUrgentRepairCustomerWorkOrderService(String woId) ;
	
	/**
	 * 获取网优之家2g工单信息
	 * */
	public Map getUrgentRepairHome2GWorkOrderService(String woId);
	
	/**
	 * 获取网优之家Td工单信息
	 * */
	public Map getUrgentRepairHomeTdWorkOrderService(String woId);
	
	/**
	 * 获取任务单信息
	 * @param toId
	 * @return
	 */
	public Map getUrgentRepairTaskOrderService(String toId);
	
	/**
	 * 获工单流转过程
	 * */
	public List<Map> getUrgentRepairWorkOrderProcessInfoService(String woId);
	
	/**
	 * 检查工单的下级任务是否已全部结束
	 * @param toId
	 * @return
	 */
	public boolean hasAllSubTasksFinishedByWoId(String woId);
	
	/**
	 * 检查工单的下级任务是否已全部结束(当前任务除外)
	 * @param woId
	 * @param subToId  检查的下级任务中排除当前任务
	 * @return
	 */
	public boolean hasAllSubTasksFinishedByWoId(String woId,String subToId);
	
	/**
	 * 检查任务单的下级任务是否已全部结束
	 * @param toId
	 * @return
	 */
	public boolean hasAllSubTasksFinishedByToId(String toId);
	
	/**
	 * 根据工单责任人账号和工单受理专业获取工单列表
	 * @param account
	 * @param acceptProfessional
	 * @param startTime
	 * @param endTime
	 */
	public List<Map> getWorkOrderListByAccountAndAcceptProfessionalService(String account,String acceptProfessional,String startTime,String endTime);

	/**
	 * 根据组织id集合获取工单列表
	 * @param account
	 * @param acceptProfessional
	 * @param startTime
	 * @param endTime
	 */
	public List<Map> getWorkOrderListByOrgIdsService(List<String> orgIds,String startTime,String endTime);
	
	/**
	 * 通过webserver接口受理网优之家工单
	 */
	public boolean callWebServerAcceptWorkOrder(String woId,String customerWoType);
	
	/**
	 * 通过webserver接口回复网优之家工单
	 */
	public boolean callWebServerReplyWorkOrder(String woId,UrgentrepairWorkorder urgentrepairWorkorder,String acceptedPeople);
	
	/**
	 * 通过webserver接口阶段回复网优之家工单
	 */
	public boolean callWebServerStepReplyWorkOrder(String woId,String customerWoType,String processingProgress,String faultType,String faultResult);
}
