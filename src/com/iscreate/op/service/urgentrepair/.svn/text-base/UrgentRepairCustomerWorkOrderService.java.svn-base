package com.iscreate.op.service.urgentrepair;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.publicinterface.Workorderassnetresource;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairCustomerworkorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairDevice2gworkorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairDevicetdworkorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairWorkorder;
import com.iscreate.op.pojo.workmanage.WorkmanageTaskorder;
import com.iscreate.op.pojo.workmanage.WorkmanageWorkorder;

public interface UrgentRepairCustomerWorkOrderService {
	/**
	 * 保存客户工单
	 * @param urgentrepairCustomerworkorder
	 */
	public Serializable saveUrgentRepairCustomerWorkOrder(UrgentrepairCustomerworkorder urgentrepairCustomerworkorder);
	
	/**
	 * 更新客户工单
	 * @param urgentrepairCustomerworkorder
	 */
	public void updateUrgentRepairCustomerWorkOrder(UrgentrepairCustomerworkorder urgentrepairCustomerworkorder);
	
	/**
	 * 根据IOSM工单号获取客户工单类表
	 */
	public List<UrgentrepairCustomerworkorder> getUrgentrepairCustomerWorkorderByWoId(final String woId);
	
	/**
	 * 根据id获取客户工单
	 * @param Id
	 * @return
	 */
	public UrgentrepairCustomerworkorder getUrgentrepairCustomerWorkorderById(long id);
	
	/**
	 * 根据条件集合获取2g工单
	 * 网优之家的2g工单信息列表
	 */
	public List<UrgentrepairDevice2gworkorder> getUrgentrepairDevice2gWorkorderByParams(final Map<String,String> params);
	
	/**
	 * 根据id获取网优之家2g工单
	 * @param Id
	 * @return
	 */
	public UrgentrepairDevice2gworkorder getUrgentrepairDevice2gWorkorderById(long id);
	
	
	
	/**
	 * 根据工单号获取网优之家td工单列表
	 */
	public List<UrgentrepairDevicetdworkorder> getUrgentrepairDevicetdWorkorderByParams(final Map<String,String> params);
	
	/**
	 * 根据id获取网优之家td工单
	 * @param Id
	 * @return
	 */
	public UrgentrepairDevicetdworkorder getUrgentrepairDevicetdWorkorderById(long id);
	
}
