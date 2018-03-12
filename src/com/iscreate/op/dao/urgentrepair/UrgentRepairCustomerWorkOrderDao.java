package com.iscreate.op.dao.urgentrepair;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.iscreate.op.pojo.urgentrepair.UrgentrepairCustomerworkorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairDevice2gworkorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairDevicetdworkorder;

public interface UrgentRepairCustomerWorkOrderDao {
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
	 * 根据工单号获取工单
	 */
	public List<UrgentrepairCustomerworkorder> getUrgentrepairCustomerWorkorderByWoId(final String woId);
	
	/**
	 * 根据id获取客户工单
	 * @param Id
	 * @return
	 */
	public UrgentrepairCustomerworkorder getUrgentrepairCustomerWorkorderById(long id);
	
	/**
	 * 保存2g客户工单
	 * @param urgentrepairCustomerworkorder
	 */
	public void saveUrgentRepairDevice2gWorkOrder(UrgentrepairDevice2gworkorder urgentrepairDevice2gworkorder);
	
	/**
	 * 更新2g客户工单
	 * @param urgentrepairCustomerworkorder
	 */
	public void updateUrgentRepairDevice2gWorkOrder(UrgentrepairDevice2gworkorder urgentrepairDevice2gworkorder);
	
	/**
	 * 根据工单号获取2g工单
	 */
	public List<UrgentrepairDevice2gworkorder> getUrgentrepairDevice2gWorkorderByParams(final Map<String,String> params);
	
	/**
	 * 根据id获取2g工单
	 * @param Id
	 * @return
	 */
	public UrgentrepairDevice2gworkorder getUrgentrepairDevice2gWorkorderById(long id);
	
	
	
	/**
	 * 保存td客户工单
	 * @param urgentrepairCustomerworkorder
	 */
	public void saveUrgentRepairDevicetdWorkOrder(UrgentrepairDevicetdworkorder urgentrepairDevicetdworkorder);
	
	/**
	 * 更新td客户工单
	 * @param urgentrepairCustomerworkorder
	 */
	public void updateUrgentRepairDevicetdWorkOrder(UrgentrepairDevicetdworkorder urgentrepairDevicetdworkorder);
	
	/**
	 * 根据条件集合获取td工单
	 */
	public List<UrgentrepairDevicetdworkorder> getUrgentrepairDevicetdWorkorderByParams(final Map<String,String> params);
	
	/**
	 * 根据id获取2g工单
	 * @param Id
	 * @return
	 */
	public UrgentrepairDevicetdworkorder getUrgentrepairDevicetdWorkorderById(long id);
	
}
