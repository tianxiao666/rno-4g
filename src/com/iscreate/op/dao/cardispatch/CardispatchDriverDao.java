package com.iscreate.op.dao.cardispatch;

import java.util.List;
import java.util.Map;

import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.dao.informationmanage.BaseDao;
import com.iscreate.op.pojo.cardispatch.CardispatchCar;
import com.iscreate.op.pojo.cardispatch.CardispatchDriver;

public interface CardispatchDriverDao  extends BaseDao<CardispatchDriver> {

	public List<Map<String,String>> getNotDriverStaffListByNameOrAccount(String accountOrName , List<Long> bizIds);
	
	public abstract List<Map<String,Object>> findDriverList(Map param_map);

	public abstract boolean deleteDriverByIds(List<String> ids);

	public List<Map<String,Object>> findDriverList ( Map param_map , Boolean isFree);

	public abstract boolean saveDriver( Map driver_map);

	public abstract boolean checkDriverAccountId(String accountId);

	public abstract boolean updateDriverById(Map driver_map, String driverId);

	public abstract boolean deleteDriverById(String id);

	public abstract Map<String,String> findDriverById(String driverId);
	
}
