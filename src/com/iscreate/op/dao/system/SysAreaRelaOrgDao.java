package com.iscreate.op.dao.system;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.system.SysOrgRelaArea;

public interface SysAreaRelaOrgDao {
/**
 * @author:du.hw
 * @create_time:2013-05-30
 * 通过区域标识获取关联的组织
 * 
 */
	public List<SysOrgRelaArea> getOrgListByAreaId(Long area_id);
	/**
	 * 
	 * @description: 根据组织id删除相关关联区域关系
	 * @author：yuan.yw
	 * @param orgId 组织id
	 * @return 操作成功标志flag    
	 * @return boolean     
	 * @date：Jun 20, 2013 9:19:23 AM
	 */
	public boolean deleteSysOrgRelaAreaByOrgId(long orgId);
}
