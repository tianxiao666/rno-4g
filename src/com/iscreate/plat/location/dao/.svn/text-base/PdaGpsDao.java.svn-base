package com.iscreate.plat.location.dao;

import java.util.List;

import com.iscreate.plat.location.pojo.PdaClient;
import com.iscreate.plat.location.pojo.PdaGpsLocation;

public interface PdaGpsDao {

	/**
	 * 保存GPS
	 * @param pgl
	 */
	public void saveGpsLocation(PdaGpsLocation pgl);
	
	/**
	 * 保存客户端
	 * @param pc
	 */
	public void saveClient(PdaClient pc);
	
	/**
	 * 根据用户ID获取GPS
	 * @param userId
	 * @return
	 */
	public List<PdaGpsLocation> getGpsLocationByUserId(String userId);
	
	/**
	 * 根据用户Id获取最近的一个GPS位置信息
	 * @param userId
	 * @return
	 */
	public PdaGpsLocation getLastGpsLocationByUserId(String userId);
}
