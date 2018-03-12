package com.iscreate.op.service.cardispatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.cardispatch.CardispatchCar;
import com.iscreate.op.service.informationmanage.BaseService;

public interface CardispatchManageServiceForMobile  extends BaseService<CardispatchCar>{

	/****************** service *******************/
	public List<Map<String, Object>> findCarDriverPairList(Map param_map);

	public List<Map<String, Object>> findCarList(Map param_map);

	public List<Map<String, Object>> findDriverList(Map param_map, Boolean isFree);

	
	public List<Map<String,Object>> findCarDriverPairListIsDuty(Map param_map, Map duty_param_Map  , String isArranged);

	public List<Map<String, String>> getNotDriverStaffAutoComplete(String driverName);

	public abstract boolean txdeleteDriverByIds(String ids);

	public abstract boolean txdeleteCarByIds(String ids);

	public abstract List<Map<String, Object>> findTerminalList(Map param_map, Boolean isFree);

	public abstract boolean txdeleteTerminalByIds(String ids);

	public abstract Long saveCar(Map param_map);

	public abstract boolean bindCarAndDriver(String carId, String driverId);

	public abstract boolean saveDriverAndBindCar(Map staff_map, Map driver_map,
			String carId, String orgId);

	public abstract boolean saveTerminalAndBindCar(Map terminal_map, String carId);

	public abstract boolean bindCarAndTerminal(String carId, String driverId);

	public abstract Map<String,Object> getCarGpsInTime(String carNumber, String startTime,
			String endTime);

	public abstract boolean updateCar(Map param_map);

	public abstract Boolean checkCarNumberExists(String carNumber, String carId);

	public abstract boolean checkAccountId(String accountId);

	public abstract boolean checkTerminalImeiIsExists(String clientImei);

	public abstract Map<String,String> findCarInfoById(String carId);

	public abstract Map<String,String> getCarTopGps(String carNumber);

	public abstract boolean txSaveDriver(Map staff_map, Map driver_map, String orgId);

	public abstract boolean txUpdateDriverById(Map staff_map, Map driver_map,
			String orgId);

	public abstract boolean txSaveTerminal(Map param_map);

	public abstract boolean txUpdateTerminalById(Map param_map);

	public abstract Map<String,String> getCarByCarDriverPairId(String cardriverpairId);

	public abstract Map<String,String> getCarStateByCarId(String carId);
	
	

}
