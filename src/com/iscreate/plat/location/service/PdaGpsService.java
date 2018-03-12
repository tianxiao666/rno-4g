package com.iscreate.plat.location.service;

import java.util.Date;
import java.util.List;

import com.iscreate.plat.location.pojo.PdaGpsLocation;

public interface PdaGpsService {

	/**
	 * 保存GPS
	 * @param gpsLocation
	 */
	public void saveGpsLocationService(String gpsLocation);
	
	/**
	 * 根据用户Id获取GPS
	 * @param userId
	 * @return
	 */
	public List<PdaGpsLocation> getGpsLocationByUserIdService(String userId);
	
	/**
	 * 保存客户端登陆信息
	 * @param gpsLocation
	 */
	public void saveClientLoginService(String gpsLocation);
	
	/**
	 * 保存客户端登出信息
	 * @param gpsLocation
	 */
	public void saveClientExitService(String gpsLocation);
	
	/**
	 * 根据用户Id获取最近的一个GPS位置信息
	 * @param userId
	 * @return
	 */
	public PdaGpsLocation getLastGpsLocationByUserIdService(String userId);
}
