package com.iscreate.op.service.cardispatch;

import java.util.Map;

import com.iscreate.op.dao.cardispatch.CardispatchCarDao;
import com.iscreate.op.dao.cardispatch.CardispatchTerminalDao;
import com.iscreate.op.dao.cardispatch.CardispatchWorkorderDao;
import com.iscreate.op.pojo.cardispatch.CardispatchWorkorder;
import com.iscreate.op.service.informationmanage.BaseServiceImpl;

public class CardispatchCarLocusServiceImpl extends BaseServiceImpl<CardispatchWorkorder> implements CardispatchCarLocusService {
	
	/************ 依赖注入 ***********/
	CardispatchWorkorderDao cardispatchWorkorderDao;
	CardispatchCarDao cardispatchCarDao;
	CardispatchTerminalDao cardispatchTerminalDao;
	
	public CardispatchWorkorderDao getCardispatchWorkorderDao() {
		return cardispatchWorkorderDao;
	}
	public void setCardispatchWorkorderDao(
			CardispatchWorkorderDao cardispatchWorkorderDao) {
		this.cardispatchWorkorderDao = cardispatchWorkorderDao;
	}
	public CardispatchCarDao getCardispatchCarDao() {
		return cardispatchCarDao;
	}
	public void setCardispatchCarDao(CardispatchCarDao cardispatchCarDao) {
		this.cardispatchCarDao = cardispatchCarDao;
	}
	public CardispatchTerminalDao getCardispatchTerminalDao() {
		return cardispatchTerminalDao;
	}
	public void setCardispatchTerminalDao(
			CardispatchTerminalDao cardispatchTerminalDao) {
		this.cardispatchTerminalDao = cardispatchTerminalDao;
	}
	
	
	/**
	 * 根据WOID获取车辆工单信息
	 * @param woId 
	 * @return
	 */
	public Map getCarWorkOrderInfoService(String woId) {
		return cardispatchWorkorderDao.findUseReturnCarTimeByWoId(woId);
	}
	/**
	 * 根据车辆牌照&采集时间获取轨迹信息
	 * @param carNumber 车辆牌照
	 * @param pickTime 采集时间
	 * @return
	 */
	public Map getGpsInfoByPickTime(String carNumber,String beginTime,String endTime,String asc){
		Map resMap = null;
		Map<String, String> carInfo = cardispatchCarDao.findTerminalByCarNumber(carNumber);
		if(carInfo!=null){
			String terminalId = carInfo.get("id");
			resMap = cardispatchTerminalDao.findGpsByClientIdInLowerTime(terminalId, beginTime,endTime,asc);
		}
		return resMap;
	}
	
}
