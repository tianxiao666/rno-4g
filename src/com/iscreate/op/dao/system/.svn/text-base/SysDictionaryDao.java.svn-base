package com.iscreate.op.dao.system;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.system.SysDictionary;

public interface SysDictionaryDao {
	/**
	 * 
	 * @description: 保存数据字典
	 * @author：yuan.yw
	 * @param entity
	 * @return     
	 * @return Serializable     
	 * @date：Aug 20, 2013 3:09:01 PM
	 */
	public Serializable saveSysDictionary(Object entity);
	/**
	 * 
	 * @description: 更新数据字典
	 * @author：yuan.yw
	 * @param entity
	 * @return     
	 * @return void     
	 * @date：Aug 20, 2013 3:09:06 PM
	 */
	public void updateSysDictionary(Object entity);
	/**
	 * 
	 * @description: 根据字典Id 字符串（“,”隔开）批量更新数据字典状态
	 * @author：yuan.yw
	 * @param dictionaryIds
	 * @param status
	 * @param orgUserId
	 * @return     
	 * @return boolean     
	 * @date：Aug 20, 2013 3:10:40 PM
	 */
	public boolean updateSysDictionaryStatusByIds(String dictionaryIds,String status,long orgUserId);
	/**
	 * 
	 * @description: 根据字典Id获取数据字典信息(包括父级信息)
	 * @author：yuan.yw
	 * @param dictionaryId
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Aug 20, 2013 3:13:12 PM
	 */
	public Map<String,Object> getSysDictionaryDetailById(long dictionaryId);
	/**
	 * 
	 * @description: 根据字典Id获取数据字典信息
	 * @author：yuan.yw
	 * @param dictionaryId
	 * @return     
	 * @return SysDictionary    
	 * @date：Aug 20, 2013 3:13:12 PM
	 */
	public SysDictionary getSysDictionaryById(long dictionaryId);
	/**
	 * 
	 * @description: 根据条件分页获取第一级的数据字典
	 * @author：yuan.yw
	 * @param conditionMap
	 * @param indexStart
	 * @param indexEnd
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Aug 20, 2013 3:15:32 PM
	 */
	public Map<String,Object> getFirstLevelDictionaryByConditionForPage(Map<String,Object> conditionMap,int indexStart,int indexEnd);
	/**
	 * 
	 * @description: 根据上级id,标识flag(获取status判断)获取下级数据字典
	 * @author：yuan.yw
	 * @param parentId
	 * @param flag
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 20, 2013 3:18:35 PM
	 */
	public List<Map<String,Object>> getDictionaryByParId(long parentId,boolean flag);
	/**
	 * 
	 * @description: 根据id（不等于）名称或编码获取数据字典
	 * @author：yuan.yw
	 * @param parentId
	 * @param id
	 * @param name
	 * @param code
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 21, 2013 11:36:02 AM
	 */
	public List<Map<String,Object>> getDictionaryByNameOrCode(String id,String parentId,String name,String code);
	/**
	 * 
	 * @description: 获取自身及子下级字典列表
	 * @author：yuan.yw
	 * @param dicIds
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 22, 2013 11:13:31 AM
	 */
	public List<Map<String,Object>> getSelfAndChildDictionaryListByIds(String dicIds);
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
	 * @param status   为空值获取全部状态数据字典
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
	 * 
	 * @description: 根据id 获取字典标识对应名称map
	 * @author：yuan.yw
	 * @param dicIds
	 * @return     
	 * @return List<Map<String,Object>>
	 * @date：Dec 19, 2013 9:58:16 AM
	 */
	public List<Map<String,Object>> getDictionaryListByIds(String dicIds);
	
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

	
		/**
		 * 
		 * @description: 获取所有系统
		 * @author：zhang.wy1
		 * @return
		 * @return: List<Map<String,Object>>          
		 * @date：2014-1-9 下午4:02:26
		 */
	public List<Map<String,Object>> getAllSystem();
}
