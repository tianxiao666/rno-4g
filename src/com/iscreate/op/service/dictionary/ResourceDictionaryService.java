package com.iscreate.op.service.dictionary;



import java.util.List;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;

public interface ResourceDictionaryService {
	/**
	 * 
	 * @description: 获取资源字典
	 * @author：
	 * @return     
	 * @return List<BasicEntity>     
	 * @date：Jun 19, 2012 1:24:26 PM
	 */
	public List<BasicEntity> getResourceDictionary(String attributeName1,String attributeValue1,String attributeName2,String attributeValue2);
}
