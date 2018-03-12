package com.iscreate.op.action.cardispatch;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.service.cardispatch.CardispatchTimerService;


public class CardispatchTimerAction {

	
	
	/** 依赖注入 *********************/
	private CardispatchTimerService cardispatchTimerService;
	
	/** 属性 **********************************/
	private Log log = LogFactory.getLog(this.getClass());

	
	
	
	/** Action **************************************/
	
	/**
	 * 定时更新gps里程
	 */
	public void updateDayGpsMileageTimer () {
		cardispatchTimerService.updateDayGpsMileageTimer();
	}

	public void init () {
		new Thread(new Runnable() {
			public void run() {
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("HH");
				Integer hour = Integer.valueOf(sdf.format(date));
				if ( hour >= 22 || hour == 0 ) {
					return;
				}
				updateDayGpsMileageTimer();
			}
		}).start();
	}
	
	
	
	/** getter setter **********************************/
	public CardispatchTimerService getCardispatchTimerService() {
		return cardispatchTimerService;
	}
	public void setCardispatchTimerService(
			CardispatchTimerService cardispatchTimerService) {
		this.cardispatchTimerService = cardispatchTimerService;
	}


	

	
	
	
}
