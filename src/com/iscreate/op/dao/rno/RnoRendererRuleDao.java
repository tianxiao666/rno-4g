package com.iscreate.op.dao.rno;

import java.util.List;

import com.iscreate.op.pojo.rno.RnoTrafficRendererConfig;

public interface RnoRendererRuleDao {

	/**
	 * 获取指定名称的渲染规则的细节
	 * @param name
	 * @return
	 * @author brightming
	 * 2013-10-10 下午3:31:05
	 */
	public List<RnoTrafficRendererConfig> getRendererRuleByRuleCode(String name,long areaId);
	
	/**
	 * 修改某个话统渲染的具体规则 
	 * @param ruleId
	 * @param configs
	 * @author brightming
	 * 2013-10-22 下午4:16:48
	 */
	public void modifyStsRendererRuleDetail(long ruleId,List<RnoTrafficRendererConfig> configs);
}
