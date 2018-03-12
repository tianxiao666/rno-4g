package com.iscreate.op.service.networkresourcemanage;



import java.util.List;
import java.util.Map;

public interface GisDispatchAreaResourceService {

	/**
	 * 城市列表(按省份)
	 * @param p_name - 区域名
	 */
	public abstract Map<String,Map<String,Object>> cityListByProvince( String province_name );

	/**
	 * 城市列表(按城市)
	 * @param p_name - 区域名
	 */
	public abstract Map<String,Map<String,Object>> cityListByCity(String p_name);

	/**
	 * 根据区域id,获取下级区域id
	 * @param areaId - 区域id
	 * @return
	 */
	public abstract List<Map<String, Object>> gisSubAreaList(String areaId);

	/**
	 * 根据用户账号,获取区域列表(省->...->街)
	 * @param accountId
	 * @return
	 */
	public abstract List<Map<String,Object>> gisfindAreaListByUserId(String accountId);
	/**
	 * 
	 * @description: 获取用户所属区域及其父区域id String List
	 * @author：yuan.yw
	 * @param userId
	 * @return     
	 * @return List<String>     
	 * @date：Mar 6, 2013 10:01:12 AM
	 */
	public abstract List<String> getUserParentAreaAndAreaIdsList(String userId);
}
