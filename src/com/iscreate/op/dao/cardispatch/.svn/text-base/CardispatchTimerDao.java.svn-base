package com.iscreate.op.dao.cardispatch;

import com.iscreate.op.dao.informationmanage.BaseDao;
import com.iscreate.op.pojo.cardispatch.CardispatchGpsmileage;

public interface CardispatchTimerDao extends BaseDao<CardispatchGpsmileage> {

	/**
	 * 查询gpsmileage表,最新日期
	 * @return 日期
	 */
	public String findGpsMileageMaxDate ();
	
	/**
	 * 根据日期更新gps里程表
	 * @param date - 日期
	 * @return 是否操作成功
	 */
	public boolean updateGpsMileageInTimeByProcedure ( String date );
	
}
