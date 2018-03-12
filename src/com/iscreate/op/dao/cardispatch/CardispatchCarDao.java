package com.iscreate.op.dao.cardispatch;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.iscreate.op.dao.informationmanage.BaseDao;
import com.iscreate.op.pojo.cardispatch.CardispatchCar;

public interface CardispatchCarDao  extends BaseDao<CardispatchCar> {
	
	/**
	 * @author:du.hw
	 * @craete_time:2013-05-27
	 * 通过用户标识得到用户有权访问的车辆列表
	 * @param org_user_id
	 * @return
	 */
	public List<Map<String,Object>> getCarListByUserId(long org_user_id);

	public List<Map<String,Object>> findCarList(Map param_map);

	public List<Map<String,Object>> findCarDriverPairList(Map param_map);
	
	
	public List<String> findCarNumber(String orgId);

	public List<Map<String,Object>> findCarDriverPairListIsDuty(Map param_map, Map duty_param_Map);
	
	
	public List<Map<String,String>>  findCurrentDayLatLngList (String carId,String sTime,String eTime);
	

	public List<Map<String,Object>> findCarListByBizId(String bizId);

	public Map<String, Object> findCarInfoById(String carId);

	public abstract boolean deleteCarByIds(List<String> ids);

	public abstract boolean clearCarBindDriver(String carId, String driverId);

	public abstract boolean carBindDriver(Map param_map);
	
	public abstract boolean updateCarBindTerminalId(String carId,String terminalId);
	
	public abstract boolean isCarBindTerminal(String carId , String terminalId);

	public abstract boolean clearCarBindTerminal(String carId, String terminalId);

	public abstract boolean carBindTerminal(Map param_map);

	public abstract Map<String,String> checkCarNumber(String carNumber);


	public abstract Map<String,String> findCarAllInfoById(String carId);


	public abstract Map<String,String> getCarByCarDriverPairId(String cardriverpairId);

	/**
	 * 根据车辆id,查询车辆状态
	 * @parma carId 车辆状态
	 * @return 车辆信息
	 */
	public abstract Map<String,String> getCarStateByCarId(String carId);

	public abstract String findCarLastMileage(String carId);

	public abstract Map<String,String> findTerminalByCarNumber(String carNumber);

	public abstract Map<String,String> findByCarNumber(String carNumber);
	
	public abstract Map<String,String> findByCarNumberForOrgId(String carNumber,String orgId);

	public abstract String findTerminalIdByCarId(String carId);

	public abstract Map<String,String> findByCarId(String carId);

	public abstract String findDriverIdByCarId(String carId);
	

	/**
	 * 根据车辆牌照,查询配对终端id
	 * @param carNumber -车辆牌照
	 * @return (String) 终端id
	 */
	public abstract String findTerminalIdByCarNumber(String carNumber);

	public abstract List<Map<String,String>> findCarInBiz(List<Long> bizId);

	/**
	 * 根据组织集合 , 查询车辆信息
	 * @param orgIds - 组织集合
	 * @return 车辆信息
	 */
	public abstract List<Map<String, Object>> findCarListByOrgIdList(Collection<String> orgIds);

	/**
	 * 根据组织id集合 , 获取车辆终端信息
	 * @parma carId 车辆状态
	 * @return 车辆信息
	 */
	public abstract List<Map<String,Object>> findCarTerminalListByOrgIdList(Collection<String> orgIds);

	
	/**
	 * 根据车辆id , 删除车辆司机关系
	 * @param carId - 车辆id
	 * @return
	 */
	public abstract boolean deleteCarTerminalPairByCarId(String carId);

	
	/**
	 * 根据车辆id , 删除车辆司机关系
	 * @param carId - 车辆id
	 * @return
	 */
	public abstract boolean deleteCarDirverPairByCarId(String carId);
	/**
	 * 根据车牌,查询车辆最新GPS信息
	 * @param carNumber
	 * @return
	 */
	public Map<String,String> findGPSLoactionByCarNumber ( String carNumber);

	/**
	 * 
	 * @description:车辆状态监控查询相关信息
	 * @author：yuan.yw
	 * @param param_map
	 * @param duty_param_Map
	 * @param indexStart
	 * @param indexEnd
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jul 22, 2013 5:53:20 PM
	 */
	public abstract List<Map<String,Object>> findCarDriverPairListForMonitor ( Map<String,Object> param_map , Map<String,Object> duty_param_Map,int indexStart,int indexEnd);
	
	/**
	 * 
	 * @description: 根据条件获取车辆相关信息（分页）
	 * @author：yuan.yw
	 * @param conditionMap
	 * @param GPSMap
	 * @param indexStart
	 * @param indexEnd
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jul 23, 2013 5:09:03 PM
	 */
	public  abstract List<Map<String,Object>> getCarStateMonitorListByCondition(Map<String,Object> conditionMap,Map<String, Object> GPSMap,String indexStart,String indexEnd);
	/**
	 * 
	 * @description: 根据车辆id获取相关信息
	 * @author：yuan.yw
	 * @param carId
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Jul 24, 2013 10:19:48 AM
	 */
	public  abstract Map<String,Object> getCarRelatedInformationByCarId(String carId);
	/***
	 * 
	 * @description: 根据车辆id 时间范围 获取当前车辆最新Gps（若无记录 查询全部时间范围）
	 * @author：yuan.yw
	 * @param carId
	 * @param dateString
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Jul 26, 2013 4:23:14 PM
	 */
	public abstract Map<String,Object> getCarCurrentGpsInfoByCarId(String carId,String dateString );
	
	/**
	 * 根据组织ID查找车辆
	 * @param orgId
	 * @return
	 */
	public List<Map<String, Object>> findCarByOrgId(long orgId);
}
