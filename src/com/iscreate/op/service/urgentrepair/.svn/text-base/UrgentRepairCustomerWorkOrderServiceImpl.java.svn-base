package com.iscreate.op.service.urgentrepair;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.iscreate.op.dao.urgentrepair.UrgentRepairCustomerWorkOrderDao;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairCustomerworkorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairDevice2gworkorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairDevicetdworkorder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UrgentRepairCustomerWorkOrderServiceImpl implements
		UrgentRepairCustomerWorkOrderService {
	// 注入Dao层
	private UrgentRepairCustomerWorkOrderDao urgentRepairCustomerWorkOrderDao;

	private  static final  Log log = LogFactory.getLog(UrgentRepairCustomerWorkOrderServiceImpl.class);

	/**
	 * 保存客户工单
	 * 在创建故障抢修工单时，创建客户工单信息，不是必须创建
	 * @param urgentrepairCustomerworkorder
	 */
	public Serializable saveUrgentRepairCustomerWorkOrder(UrgentrepairCustomerworkorder urgentrepairCustomerworkorder){
		return urgentRepairCustomerWorkOrderDao.saveUrgentRepairCustomerWorkOrder(urgentrepairCustomerworkorder);
	}
	
	/**
	 * 更新客户工单
	 * @param urgentrepairCustomerworkorder
	 */
	public void updateUrgentRepairCustomerWorkOrder(UrgentrepairCustomerworkorder urgentrepairCustomerworkorder){
		urgentRepairCustomerWorkOrderDao.updateUrgentRepairCustomerWorkOrder(urgentrepairCustomerworkorder);
	}
	
	
	/**
	 * 根据id获取客户工单
	 * @param Id
	 * @return
	 */
	public UrgentrepairCustomerworkorder getUrgentrepairCustomerWorkorderById(long id){
		return urgentRepairCustomerWorkOrderDao.getUrgentrepairCustomerWorkorderById(id);
	}
	
	/**
	 * 根据IOSM工单号获取客户工单类表
	 */
	public List<UrgentrepairCustomerworkorder> getUrgentrepairCustomerWorkorderByWoId(final String woId){
		return urgentRepairCustomerWorkOrderDao.getUrgentrepairCustomerWorkorderByWoId(woId);
	}
	
	/**
	 * 根据条件集合获取2g工单
	 * 网优之家的2g工单信息列表
	 */
	public List<UrgentrepairDevice2gworkorder> getUrgentrepairDevice2gWorkorderByParams(final Map<String,String> params){
		return urgentRepairCustomerWorkOrderDao.getUrgentrepairDevice2gWorkorderByParams(params);
	}
	
	/**
	 * 根据id获取网优之家2g工单
	 * @param Id
	 * @return
	 */
	public UrgentrepairDevice2gworkorder getUrgentrepairDevice2gWorkorderById(long id){
		return urgentRepairCustomerWorkOrderDao.getUrgentrepairDevice2gWorkorderById(id);
	}
	
	
	/**
	 * 根据工单号获取网优之家td工单列表
	 */
	public List<UrgentrepairDevicetdworkorder> getUrgentrepairDevicetdWorkorderByParams(final Map<String,String> params){
		return urgentRepairCustomerWorkOrderDao.getUrgentrepairDevicetdWorkorderByParams(params);
	}
	
	/**
	 * 根据id获取网优之家td工单
	 * @param Id
	 * @return
	 */
	public UrgentrepairDevicetdworkorder getUrgentrepairDevicetdWorkorderById(long id){
		return urgentRepairCustomerWorkOrderDao.getUrgentrepairDevicetdWorkorderById(id);
	}
	

	public UrgentRepairCustomerWorkOrderDao getUrgentRepairCustomerWorkOrderDao() {
		return urgentRepairCustomerWorkOrderDao;
	}

	public void setUrgentRepairCustomerWorkOrderDao(
			UrgentRepairCustomerWorkOrderDao urgentRepairCustomerWorkOrderDao) {
		this.urgentRepairCustomerWorkOrderDao = urgentRepairCustomerWorkOrderDao;
	}
	
	
	
}
