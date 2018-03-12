package com.iscreate.op.service.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.op.dao.system.SysUserRelaPostDao;
import com.iscreate.op.pojo.system.SysUserRelaPost;

public class SysUserRelaPostServiceImpl implements SysUserRelaPostService {
	
	private SysUserRelaPostDao sysUserRelaPostDao;
	/**
	 * 保存用户和岗位关系表
	* @author ou.jh
	* @date Jan 13, 2014 9:27:22 AM
	* @Description: TODO 
	* @param @param sysUserRelaPost
	* @param @return        
	* @throws
	 */
	public int saveSysUserRelaPost(SysUserRelaPost sysUserRelaPost){
		Map<String, Object> infoMap = installedAddSysUserRelaPostToMap(sysUserRelaPost);
		return this.sysUserRelaPostDao.saveSysUserRelaPost(infoMap);
	}
	
	
	public Map<String, Object> installedAddSysUserRelaPostToMap(SysUserRelaPost sysUserRelaPost){
		if(sysUserRelaPost != null){
			Map<String, Object> infoMap = new HashMap<String, Object>();
			infoMap.put("ORG_USER_ID", sysUserRelaPost.getOrg_user_id());
			infoMap.put("POST_CODE", sysUserRelaPost.getPost_code());
			infoMap.put("ORG_ID", sysUserRelaPost.getOrg_id());
			infoMap.put("STATUS", sysUserRelaPost.getStatus());
			infoMap.put("START_TIME", sysUserRelaPost.getStart_time());
			infoMap.put("END_TIME", sysUserRelaPost.getEnd_time());
			return infoMap;
		}else{
			return null;
		}
	}
	
	
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
	public int updateSysUserRelaPostStatus(String status,long orgUserId){
		return this.sysUserRelaPostDao.updateSysUserRelaPostStatus(status, orgUserId);
	}
	
	
	/**
	 * 根据orguserid获取用户和岗位关系
	* @author ou.jh
	* @date Jan 13, 2014 4:06:14 PM
	* @Description: TODO 
	* @param @param orgUserId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getSysUserRelaPostListByorgUserId(long orgUserId){
		return this.sysUserRelaPostDao.getSysUserRelaPostListByorgUserId(orgUserId);
	}

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
	public List<Map<String,Object>> getUserListByPostCodesAndOrgId(String postCodes,long orgId){
		return this.sysUserRelaPostDao.getUserListByPostCodesAndOrgId(postCodes,orgId);
	}
	
	/**
	 * 根据orguserid获取用户和岗位关系
	* @author ou.jh
	* @date Jan 13, 2014 4:06:14 PM
	* @Description: TODO 
	* @param @param orgUserId
	* @param @return        
	* @throws
	 */
	public String getSysUserRelaPostStringByorgUserId(long orgUserId){
		String codes = "";
		List<Map<String, Object>> list = this.sysUserRelaPostDao.getSysUserRelaPostListByorgUserId(orgUserId);
		if(list != null && list.size() > 0){
			for(Map<String, Object> s:list){
				codes = codes + "/" +s.get("POST_CODE");
			}
		}
		codes = codes + "/";
		return codes;
	}

	public SysUserRelaPostDao getSysUserRelaPostDao() {
		return sysUserRelaPostDao;
	}


	public void setSysUserRelaPostDao(SysUserRelaPostDao sysUserRelaPostDao) {
		this.sysUserRelaPostDao = sysUserRelaPostDao;
	}
}
