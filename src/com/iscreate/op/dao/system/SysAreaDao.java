package com.iscreate.op.dao.system;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.system.SysArea;

public interface SysAreaDao {
	/**
	 * @author:duhw
	 * @create_time:2013-05-29
	 * 通过区域标识得到所有父区域的信息
	 * area_id：区域标识(此标识等于0时，此方法可以得到所有区域信息)
	 * return:区域列表
	 */
	public List<Map<String,Object>> getParentAreaListByAreaId(long area_id);
	/**
	 * @author:du.hw
	 * @create_time:2013-05-29
	 * 通过区域标识得到上一级区域信息
	 * areaId:区域标识（字符串形式）
	 */
	 public Map<String,Object> getParentAreaByAreaId(long area_id);
	 
	/**
	 * @author:duhw
	 * @create_time:2013-05-11
	 * 通过区域标识得到所有子区域的信息
	 * area_id：区域标识(此标识等于0时，此方法可以得到所有区域信息)
	 * deepth:树的深度
	 * return:区域列表
	 */
	public List<Map<String,Object>> getAreaList(int area_id,int deepth);
	/**
	 * @author:duhw
	 * @create_time:2013-05-13
	 * 通过用户标识得到用户关联的区域列表(查询用户关联的组织所关联的区域)
	 * org_user_id：用户标识
	 * return:区域列表
	 */
	public List<Map<String,Object>> getAreaListByOrgUserId(long org_user_id);
	
	
	/**
	 * 根据人员账号获取人员对应区域
	* @author ou.jh
	* @date May 28, 2013 10:16:55 AM
	* @Description: TODO 
	* @param @param account        
	* @throws
	 */
	public List<Map<String,Object>> getAreaByAccount(String account);
	/**
	 * 
	 * @description: 通过区域标识字符串得到所有父区域的信息
	 * @author：yuan.yw
	 * @param area_ids 区域标识字符串 (逗号隔开)
	 * @return     区域列表
	 * @return List<Map<String,Object>>     
	 * @date：Jun 19, 2013 1:48:47 PM
	 */
	public List<Map<String,Object>> getParentAreaListByAreaIds(String area_ids);
	/**
	 * 
	 * @description: 通过组织id标识得到关联区域的信息
	 * @author：yuan.yw
	 * @param orgId 组织id标识 
	 * @return     区域列表
	 * @return List<Map<String,Object>>     
	 * @date：Jun 19, 2013 1:48:47 PM
	 */
	public List<Map<String,Object>> getAreaListByOrgId(String orgId);
	/**
	 * 
	 * @description: 通过账户名account获取关联区域的市级别(level)区域 比如广州市
	 * @author：yuan.yw
	 * @param account
	 * @return     
	 * @return Map<String,Object>    
	 * @date：Jun 25, 2013 11:05:39 AM
	 */
	public Map<String,Object> getAreaListInCityLevelByAccount(String account);
	
	/**
	 * 获取指定区域下的指定类型的子区域
	 * @param area_ids
	 * @param area_level
	 * @return
	 * @author brightming
	 * 2013-9-26 下午2:56:06
	 */
	public List<Map<String,Object>> getSubAreasInSpecAreaLevel(long[] area_ids,String area_level);
	/**
	 * @author:duhw
	 * @create_time:2013-05-11
	 * 查询sql
	 */
	public List executeSqlForObject(final String sqlString);
	
	/**
	 * 根据id获取area
	 * @param areaId
	 * @return
	 * @author brightming
	 * 2013-10-29 下午4:39:57
	 */
	public SysArea getAreaById(long areaId);
	/**
	 * 
	 * @title 获取指定区域下的BSC信息
	 * @param areaId
	 * @return
	 * @author chao.xj
	 * @date 2014-8-7下午5:11:52
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String,Object>> getBscInSpecAreaLevel(long areaId);
	/**
	 * @title 获取指定区域名称的区域信息
	 * @param areaName
	 * @return
	 * @author peng.jm
	 * @date 2014-10-8下午04:57:59
	 */
	public Map<String,Object> getAreaByAreaName(String value);
}
