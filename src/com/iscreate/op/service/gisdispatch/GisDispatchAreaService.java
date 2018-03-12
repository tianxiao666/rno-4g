package com.iscreate.op.service.gisdispatch;

import java.util.List;
import java.util.Map;

public interface GisDispatchAreaService {

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
	public abstract List<Map<String, String>> gisSubAreaList(String areaId);

	/**
	 * 根据用户账号,获取区域列表(省->...->街)
	 * @param accountId
	 * @return
	 */
	public abstract List<Map<String,String>> gisfindAreaListByUserId(String accountId);

	
}
