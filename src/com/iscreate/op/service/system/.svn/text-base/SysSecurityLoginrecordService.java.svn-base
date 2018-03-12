package com.iscreate.op.service.system;

import java.util.Map;

import com.iscreate.op.pojo.system.SysSecurityLoginrecord;


public interface SysSecurityLoginrecordService {

	/**
	 * 获取最后一次的登陆记录
	 * @param userId
	 * @return
	 */
	public SysSecurityLoginrecord getSysSecurityLoginrecordService(String userId);
	
	/**
	 * 保存登陆记录（PC）
	 * @param userId
	 * @return
	 */
	public boolean saveSysSecurityLoginrecordToPCByUserIdService(String userId);
	
	/**
	 * 保存登陆记录（终端）
	 * @param userId
	 * @param longitude
	 * @param latitude
	 * @return
	 */
	public boolean saveSysSecurityLoginrecordToMobileByUserIdService(String userId,double longitude,double latitude);
	
	/**
	 * 修改登出时间
	 * @param userId
	 * @return
	 */
	public boolean updateSysSecurityLoginrecordByUserIdService(String userId);
	
	/**
	 * 获取登陆记录信息
	 * @param userId
	 * @return
	 */
	public Map<String,String> getSysSecurityLoginrecordInfoService(String userId);
}
