package com.iscreate.op.service.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.system.SysUserRelaPost;

public interface SysUserRelaPostService {
	/**
	 * 保存用户和岗位关系表
	* @author ou.jh
	* @date Jan 13, 2014 9:27:22 AM
	* @Description: TODO 
	* @param @param sysUserRelaPost
	* @param @return        
	* @throws
	 */
	public int saveSysUserRelaPost(SysUserRelaPost sysUserRelaPost);
	

	
	
	/**
	 * 更新用户和岗位关系状态
	* @author ou.jh
	* @date Sep 3, 2013 2:28:34 PM
	* @Description: TODO 
	* @param @param infoMap
	* @param @param id
	* @param @return        
	* @throws
	 */
	public int updateSysUserRelaPostStatus(String status,long orgUserId);
	
	
	/**
	 * 根据orguserid获取用户和岗位关系
	* @author ou.jh
	* @date Jan 13, 2014 4:06:14 PM
	* @Description: TODO 
	* @param @param orgUserId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getSysUserRelaPostListByorgUserId(long orgUserId);
	/**
	 * 
	 * @description: 根据岗位code字符串(","分隔) 部门id 获取人员信息
	 * @author：yuan.yw
	 * @param postCodes
	 * @param orgId
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jan 15, 2014 9:12:41 AM
	 */
	public List<Map<String,Object>> getUserListByPostCodesAndOrgId(String postCodes,long orgId);
	
	/**
	 * 根据orguserid获取用户和岗位关系
	* @author ou.jh
	* @date Jan 13, 2014 4:06:14 PM
	* @Description: TODO 
	* @param @param orgUserId
	* @param @return        
	* @throws
	 */
	public String getSysUserRelaPostStringByorgUserId(long orgUserId);
}
