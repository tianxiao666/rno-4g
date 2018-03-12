package com.iscreate.op.dao.networkresourcemanage;

import java.util.List;
import java.util.Map;

public interface NetworkResourceQueryDao {

	/**
	 * @author du.hw
	 * @create_time 2013-05-27
	 * 通过用户标识得到用户有权访问的网络资源
	 * 方法:通过用户组织关联区域，得到与区域关联的网络资源
	 */
	public List<Map<String,Object>> getResourceListByUserId(final long org_user_id,final String resourceType);
	/**
	 * @author du.hw
	 * @create_time 2013-06-13
	 * 通过区域idString得到区域关联的机房
	 * idString:由逗号分割的区域标识字符串
	 * 此查询是通过v_room视图查询
	 */
	public List<Map<String,Object>> getRoomListByAreaIds(String idString);
	/**
	 * 
	 * @description: 通过资源类型 坐标 区域id 获取资源列表（终端使用）（站址 机房 基站）
	 * @author：yuan.yw
	 * @param AetName 资源类型
	 * @param GPSMap 坐标位置map
	 * @param conditionMap 条件
	 * @param areaId 区域id
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @param start 开始记录
	 * @param end 结束记录
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jun 25, 2013 1:48:38 PM
	 */
	public List<Map<String,Object>> getResourceWithPagingByAetNameAndGPSMapAndAreaIds(
			String AetName, Map<String,Object> GPSMap, Map<String, Object> conditionMap,String areaId,double longitude,double latitude,
			int start, int end);
}
