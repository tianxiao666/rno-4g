package com.iscreate.op.service.system;

import java.util.List;
import java.util.Map;

public interface SysAreaService {
	/**
	 * @author:duhw
	 * @create_time:2013-05-11
	 * @param area_id:区域标识
	 * @param deepth:树的深度
	 * @return 区域的子区域树形结构
	 */
	public List<Map<String, Object>> getAreaTreeList(int area_id,int deepth);
	/**
	 * @author:duhw
	 * @create_time:2013-05-13
	 * 通过用户标识得到用户关联的区域标识
	 * （用户关联组织，组织关联区域）
	 * @param org_user_id:用户标识(不是账号标识，注意区分)
	 * @return 区域列表
	 */
	public List<Map<String,Object>> getRelaAreaListByOrgUserId(long org_user_id);
	
	
	/**
	 * 根据人员账号获取人员对应区域
	* @author ou.jh
	* @date May 28, 2013 10:16:55 AM
	* @Description: TODO 
	* @param @param account        
	* @throws
	 */
	public List<Map<String,Object>> getAreaByAccount(String account);
}
