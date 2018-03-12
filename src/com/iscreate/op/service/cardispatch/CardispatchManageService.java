package com.iscreate.op.service.cardispatch;

import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.cardispatch.CardispatchCar;
import com.iscreate.op.pojo.cardispatch.CardispatchFuelBills;
import com.iscreate.op.pojo.cardispatch.CardispatchInstrumentReading;
import com.iscreate.op.service.informationmanage.BaseService;

public interface CardispatchManageService  extends BaseService<CardispatchCar>{

	
	/****************** service *******************/
	public List<Map<String, Object>> findCarDriverPairList(Map param_map);

	public List<Map<String, Object>> findCarList(Map param_map);

	public List<Map<String, Object>> findDriverList(Map param_map, Boolean isFree);

	
	public List<Map<String,Object>> findCarDriverPairListIsDuty(Map param_map, Map duty_param_Map  , String isArranged);

	public List<Map<String, String>> getNotDriverStaffAutoComplete( String driverName , String bizId);

	public abstract boolean txdeleteDriverByIds(String ids);

	public abstract boolean txdeleteCarByIds(String ids);

	public abstract List<Map<String, Object>> findTerminalList(Map param_map, Boolean isFree);
	
	public abstract List<Map<String, Object>> findTerminalBindingList(Map param_map, Boolean isFree);

	public abstract boolean txdeleteTerminalByIds(String ids);

	public abstract Long saveCar(Map param_map);

	public abstract boolean bindCarAndDriver(String carId, String driverId);

	public abstract boolean saveDriverAndBindCar( Map driver_map,
			String carId);

	public abstract boolean saveTerminalAndBindCar(Map terminal_map, String carId);

	public abstract boolean bindCarAndTerminal(String carId, String driverId);

	public abstract Map<String,Object> getCarGpsInTime(String carNumber, String startTime,
			String endTime,String orgId);
	
	public abstract Map<String,Object> getCarHeartbeat(String carNumber, String startTime,String endTime);

	public abstract boolean updateCar(Map param_map);

	public abstract Boolean checkCarNumberExists(String carNumber, String carId);

	public abstract boolean checkAccountId(String accountId);

	public abstract boolean checkTerminalImeiIsExists(String clientImei);

	public abstract Map<String,String> findCarInfoById(String carId);

	public abstract Map<String,String> getCarTopGps(String carNumber);

	public abstract boolean txSaveDriver( Map driver_map);

	public abstract boolean txUpdateDriverById( Map driver_map);

	public abstract boolean txSaveTerminal(Map param_map);

	public abstract boolean txUpdateTerminalById(Map param_map);

	public abstract Map<String,String> getCarByCarDriverPairId(String cardriverpairId);

	public abstract Map<String,String> getCarStateByCarId(String carId);

	public abstract Double findCarLastMileage(String carId);

	public abstract List<Map<String,String>> findCarMileageByBizInTime(String bizId,List<String> date_list,
			String startHour, String endHour);
	

	/**
	 * 根据组织id集合,获取车辆信息(包括最新gps、车辆状态)
	 * @param orgIds - 组织id集合
	 * @return
	 */
	public abstract List<Map<String,Object>> findCarListWithTopGpsAndStateByOrgIds(Collection<String> orgIds);
	
	public abstract List<String> findCarNumber(String orgId);

	
	public abstract List<Map<String,Object>> findCarDriverPairListIsDutyByGis(Map param_map,
			Map duty_param_Map, String isArranged);
	
	
	public abstract Map<String,Double> findCurrentDayMileage(String carId,String sTime,String eTime);
	
	
	/**
	 * 根据车牌,查询车辆最新GPS信息
	 * @param carNumber
	 * @return
	 */
	public abstract Map<String,String> findGPSLoactionByCarNumber ( String carNumber);
	/**
	 * 
	 * @description: 车辆状态监控获取信息 
	 * @author：yuan.yw
	 * @param param_map
	 * @param duty_param_Map
	 * @param isArranged
	 * @param currentPage
	 * @param pageSize
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jul 23, 2013 10:14:04 AM
	 */
	public List<Map<String,Object>> findCarDriverPairListForMonitor(Map<String,Object> param_map, Map<String,Object> duty_param_Map  , String isArranged,int currentPage,int pageSize);
	/***
	 * 
	 * @description: 根据车辆id获取当前车辆最新Gps（若无记录 查询全部时间范围）
	 * @author：yuan.yw
	 * @param carId
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Jul 26, 2013 4:23:14 PM
	 */
	public Map<String,Object> getCarCurrentGpsInfoByCarId(String carId);
	
	/**
	 * 添加车辆仪表读数与油费
	* @author ou.jh
	* @date Aug 1, 2013 2:06:56 PM
	* @Description: TODO 
	* @param @param cardispatchInstrumentReading
	* @param @param cardispatchFuelBills        
	* @throws
	 */
	public boolean addCardispatchInstrumentReadingAndFuelBills(CardispatchInstrumentReading cardispatchInstrumentReading,CardispatchFuelBills cardispatchFuelBills);
	
	/**
	 * 根据组织ID查找车辆 
	* @author ou.jh
	* @date Aug 2, 2013 10:09:59 AM
	* @Description: TODO 
	* @param @param orgId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> findCarByOrgIdService(long orgId);
	
	/**
	 * 根据年月获取最大油费 
	* @author ou.jh
	* @date Aug 2, 2013 10:39:57 AM
	* @Description: TODO 
	* @param @param date
	* @param @return        
	* @throws
	 */
	public double getmaxBillsByDateService(String date,long carId);
	
	/**
	 * 根据年月获取最大仪表读数 
	* @author ou.jh
	* @date Aug 2, 2013 10:39:57 AM
	* @Description: TODO 
	* @param @param date
	* @param @return        
	* @throws
	 */
	public double getmaxReadingByDateService(String date,long carId);
	
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
	 * 删除仪表读数状态
	* @author ou.jh
	* @date Aug 5, 2013 2:51:17 PM
	* @Description: TODO 
	* @param @param id
	* @param @param deleteUserId        
	* @throws
	 */
	public void deleteCardispatchInstrumentReading(long id,long deleteUserId);
	
	/**
	 * 删除油费状态
	* @author ou.jh
	* @date Aug 5, 2013 2:51:22 PM
	* @Description: TODO 
	* @param @param id
	* @param @param deleteUserId        
	* @throws
	 */
	public void deleteCardispatchFuelBills(long id,long deleteUserId);
	
	/**
	 * 根据ID获取车辆油费 
	* @author ou.jh
	* @date Aug 5, 2013 3:39:15 PM
	* @Description: TODO 
	* @param @param id
	* @param @return        
	* @throws
	 */
	public Map<String, Object> getCardispatchFuelBillsById(long id);
	
	/**
	 * 根据ID获取车辆仪表读数 
	* @author ou.jh
	* @date Aug 5, 2013 3:39:15 PM
	* @Description: TODO 
	* @param @param id
	* @param @return        
	* @throws
	 */
	public Map<String, Object> getCardispatchInstrumentReadingById(long id);
	/***
	 * 
	 * @description: 根据车辆id获取当前车辆信息
	 * @author：yuan.yw
	 * @param carId
	 * @return     
	 * @return Map<String,String>     
	 * @date：Jul 26, 2013 4:23:14 PM
	 */
	public Map<String,String> getCarInfoByCarId(String carId);
	
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
	 * 导出仪表读数或油费
	* @author ou.jh
	* @date Aug 13, 2013 1:42:23 PM
	* @Description: TODO 
	* @param @param distinction
	* @param @param startTime
	* @param @param endTime
	* @param @param orgId
	* @param @param carId
	* @param @return        
	* @throws
	 */
	public InputStream exportCarReadingOrBillsService(String distinction,String startTime,String endTime,long orgId,String carId);
	
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
