package com.iscreate.op.service.cardispatch;

import java.util.List;
import java.util.Map;

public interface CardispatchCommonService {

	/****************** service *******************/
	public List<Map<String, String>> getAllProvince();

	public List<Map<String, String>> getSubArea(String areaId);

	public abstract List<String> getAreaIdListByStationId(String stationId);

	public abstract List<String> getAreaIdListByBaseStationIdAndType(String baseStationId, String baseStationType);

	public abstract List<String> getAreaIdByLoginPerson();

	public abstract Map<String,String> getResoueceInfo(String resId, String resType);

	/**
	 * 获取当前登录人的下级组织(不递归)
	 * @return (List<Map<String, String>>) 当前登录人的下级组织
	 */
	public abstract List<Map<String, String>> getUserDownOrg();
	
	/**
	 * 
	 * @description: 根据组织id获取子组织列表 (组织id为空，获取当前登录人所在组织)
	 * @author：yuan.yw
	 * @param orgId
	 * @param account
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jul 23, 2013 4:38:35 PM
	 */
	
	public abstract List<Map<String,Object>> getChildOrgListByOrgId(String orgId,String account);
	
}
