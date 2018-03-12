package com.iscreate.op.service.networkresourcemanage;

import java.util.List;
import java.util.Map;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;

public interface StaffOrganizationService {
	/**
	 * 根据人员ID获取人员所在组织的区域
	 * @param userId
	 * @return
	 */
	public List<BasicEntity> getAreaByUserId(String userId);
	
	
	/**
	 * 获取顶级区域节点集合
	 * @return
	 */
	public List<BasicEntity> getTopLevelAreaList();
	
	public Map<String,String> getUserByUserId(String userId);
}
