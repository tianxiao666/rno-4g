package com.iscreate.op.dao.publicinterface;

import java.util.List;

import com.iscreate.op.pojo.publicinterface.LoginRecord;

public interface LoginRecordDao {

	/**
	 * 根据账号获取登陆记录
	 * @param userId
	 * @return
	 */
	public List<LoginRecord> getLoginRecordByUserId(String userId);
	
	/**
	 * 保存登陆记录
	 * @param loginRecord
	 */
	public void saveLoginRecord(LoginRecord loginRecord);
	
	/**
	 * 修改登陆记录
	 * @param loginRecord
	 */
	public void updateLoginRecord(LoginRecord loginRecord);
}
