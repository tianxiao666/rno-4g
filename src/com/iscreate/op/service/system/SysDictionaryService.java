package com.iscreate.op.service.system;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.system.SysDictionary;

public interface SysDictionaryService {
	/**
	 * 
	 * @description: 保存或更新字典
	 * @author：yuan.yw
	 * @param sysDictionary
	 * @return     
	 * @return Long     
	 * @date：Aug 21, 2013 10:24:21 AM
	 */
	public Long saveOrUpdateSysDictionary(SysDictionary sysDictionary);
	/**
	 * 
	 * @description: 根据id或名称或编码验证数据字典是否存在
	 * @author：yuan.yw
	 * @param id
	 * @param parentId
	 * @param code
	 * @param name
	 * @return     
	 * @return boolean     
	 * @date：Aug 21, 2013 11:33:54 AM
	 */
	public boolean checkDictionaryIsExists(String id,String parentId,String code,String name);
	/**
	 * 
	 * @description: 根据上级id获取数据字典list
	 * @author：yuan.yw
	 * @param dicId
	 * @param statusFlag
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 21, 2013 2:18:49 PM
	 */
	public List<Map<String,Object>> getDictionaryListByParId(long dicId,boolean statusFlag);
	/**
	 * 
	 * @description:根据Id获取字典信息
	 * @author：yuan.yw
	 * @param dicId
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Aug 21, 2013 2:36:06 PM
	 */
	public Map<String,Object> getDictionaryDetailById(long dicId);
	/**
	 * 
	 * @description: 更新字典状态
	 * @author：yuan.yw
	 * @param dicIds
	 * @param status
	 * @return     
	 * @return boolean     
	 * @date：Aug 22, 2013 11:09:59 AM
	 */
	public boolean updateDictionaryStatus(String dicIds,String status);
	/**
	 * 
	 * @description: 分页获取第一级字典
	 * @author：yuan.yw
	 * @param conditionMap
	 * @param currentPage
	 * @param pageSize
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Aug 22, 2013 3:00:23 PM
	 */
	public Map<String,Object> getRootDictionaryList(Map<String,Object> conditionMap,int currentPage,int pageSize);
	/**
	 * 
	 * @description: 根据编码获取下（子）级数据字典
	 * @author：yuan.yw
	 * @param code
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 26, 2013 10:10:43 AM
	 */
	public List<Map<String,Object>> getChildDictionaryListByCode(String code);
	/**
	 * 
	 * @description: 根据编码获取其下全部下级字典（或者包括自身 selfFlag判断）
	 * @author：yuan.yw
	 * @param code
	 * @param selfFlag 是否获取自身
	 * @param status   为空值获取全部
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 26, 2013 10:10:43 AM
	 */
	public List<Map<String,Object>> getAllChildDictionaryListByCode(String code,boolean selfFlag,String status);
	/**
	 * 
	 * @description: 根据自身编码及父编码获取下（子）级数据字典
	 * @author：yuan.yw
	 * @param code
	 * @param parentCode
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 26, 2013 10:19:37 AM
	 */
	public List<Map<String,Object>> getChildDictionaryListByCodeAndParCode(String code,String parentCode);
	/**
	 * 
	 * @description: 根据自身编码及父编码获取其下全部下级字典（或者包括自身 selfFlag判断）
	 * @author：yuan.yw
	 * @param code
	 * @param parentCode
	 * @param selfFlag 是否获取自身
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 26, 2013 10:20:08 AM
	 */
	public List<Map<String,Object>> getAllChildDictionaryListByCodeAndParCode(String code,String parentCode,boolean selfFlag);
	/**
	 * 
	 * @description: 根据自身编码及父编码获取数据字典
	 * @author：yuan.yw
	 * @param code
	 * @param parentCode
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 26, 2013 10:19:37 AM
	 */
	public Map<String,Object> getDictionaryMapByCodeAndParCode(String code,String parentCode);
	
	/**
	 * 
	 * @description: 获取自身及子下级字典列表
	 * @author：yuan.yw
	 * @param dicIds
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 22, 2013 11:13:31 AM
	 */
	public List<Map<String,Object>> getSelfAndChildDictionaryListByCode(String code);
	
	/**
	 * 
	 * @description: 根据自身编码及父编码获取数据字典
	 * @author：yuan.yw
	 * @param code
	 * @param parentCode
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 26, 2013 10:19:37 AM
	 */
	public Map<String,Object> getDictionaryMapByCode(String code);
	
	/**
	 * 根据code集合其孙子级数据字典(in)
	* @author ou.jh
	* @date Jan 10, 2014 2:27:24 PM
	* @Description: TODO 
	* @param @param code
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getSecondSysDictionaryListByCode(String code);


}
