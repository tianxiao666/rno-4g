package com.iscreate.op.dao.cardispatch;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.iscreate.op.dao.informationmanage.BaseDaoImpl;
import com.iscreate.op.pojo.cardispatch.CardispatchGpsmileage;

public class CardispatchTimerDaoImpl extends BaseDaoImpl<CardispatchGpsmileage> implements CardispatchTimerDao {
	
	private static final String gpsmileage_Table = " (SELECT * , id as carId FROM cardispatch_gpsmileage) car "; 
	
	
	
	/**
	 * 查询gpsmileage表,最新日期
	 * @return 日期
	 */
	@Deprecated
	public String findGpsMileageMaxDate () {
		String sql = " SELECT Max(gpsDate) as gpsDate FROM " + gpsmileage_Table + " LIMIT 0 , 1 ";
		String date = null;
		List<Map<String, String>> list = super.executeFindList(sql);
		if ( list != null && list.size() > 0 ) {
			date = list.get(0).get("gpsDate");
		}
		return date;
	}
	
	
	/**
	 * 根据日期更新gps里程表
	 * @param date - 日期
	 * @return 是否操作成功
	 */
	@Deprecated
	public boolean updateGpsMileageInTimeByProcedure ( String date ) {
		Boolean flag = false;
		String sql = " CALL pro_updateGpsMileage('" + date + "')";
//		String sql = " CALL a('" + date + "')";
		return flag;
	}
	
	
	
}
