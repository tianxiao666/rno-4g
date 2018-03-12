package com.iscreate.op.service.rno;

import java.util.List;

import com.iscreate.op.pojo.rno.RnoTrafficRendererConfig;

public interface RnoRendererRuleService {

	/**
	 * 获取指定名称的渲染规则对应的渲染细节
	 * @param code
	 * @return
	 * @author brightming
	 * 2013-10-10 下午3:26:04
	 */
	public List<RnoTrafficRendererConfig> getRendererRuleByRuleCode(String code,long areaId);
}
