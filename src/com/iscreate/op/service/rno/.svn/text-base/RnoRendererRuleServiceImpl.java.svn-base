package com.iscreate.op.service.rno;

import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.dao.rno.RnoRendererRuleDao;
import com.iscreate.op.pojo.rno.RnoTrafficRendererConfig;

public class RnoRendererRuleServiceImpl implements RnoRendererRuleService {

	private static Log log = LogFactory
			.getLog(RnoRendererRuleServiceImpl.class);

	private RnoRendererRuleDao rnoRendererRuleDao;

	public RnoRendererRuleDao getRnoRendererRuleDao() {
		return rnoRendererRuleDao;
	}

	public void setRnoRendererRuleDao(RnoRendererRuleDao rnoRendererRuleDao) {
		this.rnoRendererRuleDao = rnoRendererRuleDao;
	}

	/**
	 * 获取指定名称的渲染规则对应的渲染细节
	 * 
	 * @param code
	 * @return
	 * @author brightming 2013-10-10 下午3:26:04
	 */
	public List<RnoTrafficRendererConfig> getRendererRuleByRuleCode(String code,long areaId) {
		log.info("进入getRendererRuleByRuleCode，code=" + code+",areaId="+areaId);
		if (code == null || "".equals(code.trim())) {
			return Collections.EMPTY_LIST;
		}

		List<RnoTrafficRendererConfig> configs= rnoRendererRuleDao.getRendererRuleByRuleCode(code,areaId);
		log.info("退出getRendererRuleByRuleName。结果："+configs);
		return configs;
	}

}
