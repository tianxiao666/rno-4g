package com.iscreate.op.service.urgentrepair;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.publicinterface.Workorderassnetresource;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairCustomerworkorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairWorkorder;
import com.iscreate.op.pojo.workmanage.WorkmanageTaskorder;
import com.iscreate.op.pojo.workmanage.WorkmanageWorkorder;

public interface UrgentRepairWorkOrderService {
	/**
	 * 创建工单
	 * @param Creator
	 * @param woId
	 * @param urgentrepairWorkorder
	 */
	public String txCreateUrgentRepairWorkOrder(String Creator,UrgentrepairWorkorder urgentrepairWorkorder,UrgentrepairCustomerworkorder urgentrepairCustomerworkorder,Workorderassnetresource workorderassnetresource,WorkmanageWorkorder workmanageWorkorder);
	
	/**
	 * 更改工单信息
	 * 一般都是从网优之家对接回来的工单，发现若信息不全，则使用更改工单功能将信息补全
	 * @param woId
	 * @param urgentrepairWorkorder
	 * @param workorderassnetresource
	 */
	public String txModifytUrgentRepairWorkOrder(String woId,UrgentrepairWorkorder urgentrepairWorkorder,Workorderassnetresource workorderassnetresource);
	
	/**
	 * 工单阶段回复
	 * 记录反馈工单受理后至结束前的一些信息
	 * 若果是网优之家对接过来的工单，阶段回复工单后需要调用网优之家接口更新网优之家工单的状态信息
	 * @param actor
	 * @param woId
	 * @param stepReply_desc
	 * @param stepReply_processingProgress
	 * @param stepReply_faultType
	 * @param stepReply_faultResult
	 * @return
	 */
	public String txStepReplyUrgentRepairWorkOrder(String actor,String woId,String stepReply_desc,String stepReply_processingProgress,String stepReply_faultType,String stepReply_faultResult,String customerWoType);
	
	/**
	 * 工单最终回复
	 * 若果是网优之家对接过来的工单，回复工单后需要调用网优之家接口更新网优之家工单的状态信息
	 * @param reply
	 * @param woId
	 * @param urgentrepairWorkorder
	 */
	public String txFinalReplyUrgentRepairWorkOrder(String reply,String woId,UrgentrepairWorkorder urgentrepairWorkorder);
	
	
	/**
	 * 受理工单
	 * 创建故障抢修工单后，必须先对其受理才能进行后续业务功能
	 * 若果是网优之家对接过来的工单，受理工单后需要调用网优之家接口更新网优之家工单的状态信息
	 * @param accept
	 *            受理人
	 * @param woId
	 *            工单号
	 * @param urgentrepairWorkorder
	 *            工单实例
	 */
	public String txAcceptUrgentRepairWorkOrder(String accept,String woId,UrgentrepairWorkorder urgentrepairWorkorder);
	
	/**
	 * 派发现场任务单
	 * @param sendor
	 * @param recipient
	 * @param woId
	 * @param toId
	 * @param urgentrepairSencetaskorder
	 */
	public void txCreateUrgentRepairSceneTaskOrder(String sendor,
			String recipient,String woId,WorkmanageTaskorder workmanageTaskorder);
	
	/**
	 * 撤销任务
	 * 在派发任务的下级任务管理列表里，可以对工单下的状态不为“已撤销”或“已结束”的子任务作撤销操作，
	 * @param actor
	 * @param woId
	 * @param toId
	 * @param workmanageTaskorder
	 */
	public boolean txCancelUrgentRepairSceneTaskOrder(String actor,String woId,String toId,
			WorkmanageTaskorder workmanageTaskorder);
	
	/**
	 * 重派任务
	 * 在派发任务的下级任务管理列表里，可以对工单下的状态为“待撤销”的子任务作重派操作，
	 * @param actor
	 * @param woId
	 * @param toId
	 * @param workmanageTaskorder
	 */
	public boolean txReAssignUrgentRepairSceneTaskOrder(String actor,String woId,String toId,
			WorkmanageTaskorder workmanageTaskorder);
	
	/**
	 * 催办任务
	 * 在派发任务的下级任务管理列表里，可以对工单下的子任务作催办操作，催办后会对催办对象任务发送一条催办消息
	 * @param actor
	 * @param woId
	 * @param toId
	 * @param workmanageTaskorder
	 */
	public boolean txHastenUrgentRepairSceneTaskOrder(String actor,String woId,String toId,
			WorkmanageTaskorder workmanageTaskorder);
	
	/**
	 * 获取维护队长
	 * 派发现场任务单，需要根据登录人所在组织获取其下的维护队，维护队队长是派发对象
	 * @param providerOrgId
	 */
	public List<Map>  loadTeamLeadersInfoService(String providerOrgId);
	

	
	/**
	 * 根据工单号获取工单
	 * @param toId
	 */
	public UrgentrepairWorkorder getUrgentRepairWorkOrder(String woId);
	
	
	
}
