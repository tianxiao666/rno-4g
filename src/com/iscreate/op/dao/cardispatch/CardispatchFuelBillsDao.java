package com.iscreate.op.dao.cardispatch;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.cardispatch.CardispatchFuelBills;

public interface CardispatchFuelBillsDao {
	/**
	 * 添加车辆油费
	* @author ou.jh
	* @date Aug 1, 2013 10:51:40 AM
	* @Description: TODO 
	* @param @param cardispatchFuelBills        
	* @throws
	 */
	public void addCardispatchFuelBills(CardispatchFuelBills cardispatchFuelBills);
	
	/**
	 * 更新车辆油费
	* @author ou.jh
	* @date Aug 1, 2013 10:52:48 AM
	* @Description: TODO 
	* @param @param cardispatchFuelBills        
	* @throws
	 */
	public void updateCardispatchFuelBills(CardispatchFuelBills cardispatchFuelBills);
	
	/**
	 * 根据ID获取车辆油费读数
	* @author ou.jh
	* @date Aug 5, 2013 2:47:30 PM
	* @Description: TODO 
	* @param @param id
	* @param @return        
	* @throws
	 */
	public CardispatchFuelBills getCardispatchFuelBillsBy(long id);
	
	/**
	 * 根据ID获取车辆油费
	* @author ou.jh
	* @date Aug 1, 2013 11:08:01 AM
	* @Description: TODO 
	* @param @param id        
	* @throws
	 */
	public Map<String,Object> getCardispatchFuelBillsById(long id);
	
	/**
	 * 根据年月获取最大油费
	* @author ou.jh
	* @date Aug 1, 2013 11:08:01 AM
	* @Description: TODO 
	* @param @param id        
	* @throws
	 */
	public double getMaxBillsByDate(String date,long carId);
	
	/**
	 * 根据车辆油费（分页）
	* @author ou.jh
	* @date Aug 1, 2013 11:08:01 AM
	* @Description: TODO 
	* @param @param id        
	* @throws
	 */
	public List<Map<String,Object>> getCardispatchFuelBillsPaging(String startTime,String endTime,long orgId,String carId,int currentPage, int pageSize);
	
	/**
	 * 根据车辆油费总数
	* @author ou.jh
	* @date Aug 1, 2013 11:08:01 AM
	* @Description: TODO 
	* @param @param id        
	* @throws
	 */
	public int getCardispatchFuelBillsCount(String startTime,String endTime,long orgId,String carId);
	
	/**
	 * 根据车辆油费
	* @author ou.jh
	* @date Aug 1, 2013 11:08:01 AM
	* @Description: TODO 
	* @param @param id        
	* @throws
	 */
	public List<Map<String,Object>> getCardispatchFuelBills(String startTime,String endTime,long orgId,String carId);
}
