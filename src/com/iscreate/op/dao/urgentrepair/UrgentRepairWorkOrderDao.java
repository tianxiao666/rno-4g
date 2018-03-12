package com.iscreate.op.dao.urgentrepair;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.urgentrepair.UrgentrepairWorkorder;

public interface UrgentRepairWorkOrderDao {
	/**
	 * 保存工单
	 * @param urgentrepairWorkorder
	 */
	public Serializable saveUrgentRepairWorkOrder(UrgentrepairWorkorder urgentrepairWorkorder);
	
	/**
	 * 更新工单
	 * @param urgentrepairWorkorder
	 */
	public void updateUrgentRepairWorkOrder(UrgentrepairWorkorder urgentrepairWorkorder);
	
	/**
	 * 根据工单号获取工单
	 * @param woId
	 * @return
	 */
	public List<UrgentrepairWorkorder> getUrgentrepairWorkorderByWoId(String woId);
	
	/**
	 * 根据id获取工单
	 * @param woId
	 * @return
	 */
	public UrgentrepairWorkorder getUrgentrepairWorkorderById(long id);
	
	/**
	 * 根据工单责任人账号和工单受理专业获取工单列表
	 * @param account
	 * @param acceptProfessional
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<Map> getWorkOrderListByAccountAndAcceptProfessionalDao(final String account,final String acceptProfessional,final String startTime,final String endTime);
	
	/**
	 * 根据组织id获取工单列表
	 * @param account
	 * @param acceptProfessional
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<Map> getWorkOrderListByOrgIdsDao(final List<String> orgIds ,final String startTime,final String endTime);
	
}
