package com.iscreate.op.service.publicinterface;

import java.util.Map;

import com.iscreate.op.pojo.publicinterface.LoginRecord;

public interface LoginRecordService {

	/**
	 * 获取最后一次的登陆记录
	 * @param userId
	 * @return
	 */
	public LoginRecord getLastLoginRecordService(String userId);
	
	/**
	 * 保存登陆记录（PC）
	 * @param userId
	 * @return
	 */
	public boolean saveLoginRecordToPCByUserIdService(String userId);
	
	/**
	 * 保存登陆记录（终端）
	 * @param userId
	 * @param longitude
	 * @param latitude
	 * @return
	 */
	public boolean saveLoginRecordToMobileByUserIdService(String userId,double longitude,double latitude);
	
	/**
	 * 修改登出时间
	 * @param userId
	 * @return
	 */
	public boolean updateLogoutRecordByUserIdService(String userId);
	
	/**
	 * 获取登陆记录信息
	 * @param userId
	 * @return
	 */
	public Map<String,String> getLoginRecordInfoService(String userId);
}
