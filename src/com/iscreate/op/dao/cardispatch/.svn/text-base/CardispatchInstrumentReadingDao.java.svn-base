package com.iscreate.op.dao.cardispatch;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.cardispatch.CardispatchInstrumentReading;

public interface CardispatchInstrumentReadingDao {
	/**
	 * 添加车辆仪表读数
	* @author ou.jh
	* @date Aug 1, 2013 10:56:49 AM
	* @Description: TODO 
	* @param @param cardispatchInstrumentReading        
	* @throws
	 */
	public void addCardispatchInstrumentReading(CardispatchInstrumentReading cardispatchInstrumentReading);
	
	/**
	 * 更新车辆仪表读数
	* @author ou.jh
	* @date Aug 1, 2013 10:57:03 AM
	* @Description: TODO 
	* @param @param cardispatchInstrumentReading        
	* @throws
	 */
	public void updateCardispatchInstrumentReading(CardispatchInstrumentReading cardispatchInstrumentReading);
	
	/**
	 * 根据ID获取车辆仪表读数
	* @author ou.jh
	* @date Aug 5, 2013 2:47:30 PM
	* @Description: TODO 
	* @param @param id
	* @param @return        
	* @throws
	 */
	public CardispatchInstrumentReading getCardispatchInstrumentReading(long id);
	
	/**
	 * 根据ID获取车辆仪表读数
	* @author ou.jh
	* @date Aug 1, 2013 11:08:01 AM
	* @Description: TODO 
	* @param @param id        
	* @throws
	 */
	public Map<String,Object> getCardispatchInstrumentReadingById(long id);
	
	/**
	 * 根据年月获取最大仪表读数
	* @author ou.jh
	* @date Aug 1, 2013 11:08:01 AM
	* @Description: TODO 
	* @param @param id        
	* @throws
	 */
	public double getMaxReadingByDate(String date,long carId);
	
	/**
	 * 根据车辆仪表读数（分页）
	* @author ou.jh
	* @date Aug 1, 2013 11:08:01 AM
	* @Description: TODO 
	* @param @param id        
	* @throws
	 */
	public List<Map<String,Object>> getCardispatchInstrumentReadingPaging(String startTime,String endTime,long orgId,String carId,int currentPage, int pageSize);
	
	/**
	 * 根据车辆仪表读数总数
	* @author ou.jh
	* @date Aug 1, 2013 11:08:01 AM
	* @Description: TODO 
	* @param @param id        
	* @throws
	 */
	public int getCardispatchInstrumentReadingCount(String startTime,String endTime,long orgId,String carId);
	
	/**
	 * 根据车辆仪表读数
	* @author ou.jh
	* @date Aug 1, 2013 11:08:01 AM
	* @Description: TODO 
	* @param @param id        
	* @throws
	 */
	public List<Map<String,Object>> getCardispatchInstrumentReading(String startTime,String endTime,long orgId,String carId);
	
	/**
	 * 获取车辆当前日期仪表数据
	* @author ou.jh
	* @date Aug 1, 2013 10:47:22 AM
	* @Description: TODO 
	* @param @param date
	* @param @param carId
	* @param @return        
	* @throws
	 */
	public int getReadingCountByDate(String date,long carId);
	
	/**
	 * 根据年月获取最小仪表读数
	* @author ou.jh
	* @date Aug 1, 2013 11:08:01 AM
	* @Description: TODO 
	* @param @param id        
	* @throws
	 */
	public double getMinReadingByDate(String date,long carId);
}
