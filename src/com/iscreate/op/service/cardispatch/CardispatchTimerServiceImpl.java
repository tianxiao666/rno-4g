package com.iscreate.op.service.cardispatch;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.action.informationmanage.common.DateUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.dao.cardispatch.CardispatchTimerDao;
import com.iscreate.op.pojo.cardispatch.CardispatchGpsmileage;
import com.iscreate.op.service.informationmanage.BaseServiceImpl;

public class CardispatchTimerServiceImpl extends BaseServiceImpl<CardispatchGpsmileage> implements CardispatchTimerService {
	/** 依赖注入 *********************/
	private CardispatchTimerDao cardispatchTimerDao;
	
	/** 属性 **********************************/
	private Log log = LogFactory.getLog(this.getClass());
	/** service ***********************************/
	
	/**
	 * 定时更新gps里程
	 */
	public void updateDayGpsMileageTimer ( ) {
		//查询最新的数据日期
		String maxDate = cardispatchTimerDao.findGpsMileageMaxDate();
		Date now = new Date();
		if ( StringUtil.isNullOrEmpty(maxDate) ) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			maxDate = sdf.format(now) + "-01-01";
		}
		String nowString = DateUtil.formatDate(now, "yyyy-MM-dd");
		//跟现在日期对比
		try {
			List<String> date_list = DateUtil.getDateListByFromEndDateListWithString( maxDate , nowString );
			for (int i = 0; i < date_list.size() ; i++) {
				String date_string = date_list.get(i);
				this.cardispatchTimerDao.updateGpsMileageInTimeByProcedure(date_string);
			}
		} catch (ParseException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	
	
	

	

	

	/** getter setter **********************************/
	public CardispatchTimerDao getCardispatchTimerDao() {
		return cardispatchTimerDao;
	}

	public void setCardispatchTimerDao(CardispatchTimerDao cardispatchTimerDao) {
		this.cardispatchTimerDao = cardispatchTimerDao;
	}
	
	
	
}
