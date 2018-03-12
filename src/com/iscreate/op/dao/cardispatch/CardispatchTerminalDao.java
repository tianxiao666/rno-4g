package com.iscreate.op.dao.cardispatch;

import java.util.List;
import java.util.Map;

import com.iscreate.op.action.informationmanage.common.DBUtil.DBWhereCallBack;
import com.iscreate.op.dao.informationmanage.BaseDao;
import com.iscreate.op.pojo.cardispatch.CardispatchCar;
import com.iscreate.op.pojo.cardispatch.CardispathTerminal;

public interface CardispatchTerminalDao  extends BaseDao<CardispathTerminal> {

	public List<Map<String,Object>> findTerminalList( Map param_map , Boolean isFree );
	public List<Map<String,Object>> findTerminalBindingList( Map param_map , Boolean isFree );
	public abstract boolean deleteTerminalByIds(List<String> ids);
	public abstract boolean saveTerminal(Map param_map);
	public abstract List<Map<String,Object>> findTerminalList(Map param_map);
	public abstract boolean checkTerminalImeiIsExists(String clientimei);
	public abstract boolean updateTerminalById(Map param_map, String terminalId);
	public abstract Map<String,String> findMaxGpsByClientId(String clientId);
	public abstract Map<String,String> findMinGpsByCarId(String car_Id,String time);
	
	public abstract List<Map<String,String>> findCarHeartbeat(String carNumber, String startTime,String endTime);
	
	
	public abstract List<Map<String,String>> findGpsByParam(Map param_map);
	public abstract List<Map<String,String>> findTerminalGpsList(String terminalId, String startTime,
			String endTime);
	
	/**
	 * 根据终端id,获取小于pickTime参数的最新gps数据
	 * @param terminalId - 终端id
	 * @param pickTime - 时间区间
	 * @return (Map<String,String>) gps数据
	 */
	public abstract Map<String,String> findGpsByClientIdInLowerTime(String terminalId,
			String beginTime,String endTime,String asc);
	
	
	/**
	 * 根据终端id,获取终端信息
	 * @param terminalId - 终端id
	 * @return 终端信息
	 */
	public abstract Map<String,String> findTerminalByTerminalId(String terminalId);
}
