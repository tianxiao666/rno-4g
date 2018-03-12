package com.iscreate.op.service.networkresourcemanage;

import java.util.List;
import java.util.Map;



public interface ImportService {
	/**
	 * 
	 * @description: 导入新增
	 * @author：
	 * @param userId
	 * @param userName
	 * @param importEntityType
	 * @param importModel
	 * @param opCause
	 * @param opScene
	 * @param rdoAssModel
	 * @param assType
	 * @param assEntityType
	 * @param listMap
	 * @param rows
	 * @param assIds
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jan 30, 2013 2:41:44 PM
	 */
	public List<Map<String,Object>> importAddResource(String userId,String userName,String importEntityType,String importModel,String opCause,String opScene,String rdoAssModel,String assType,String assEntityType,List<Map<String,String>> listMap,List<String>  rows,List<String> assIds);
	/**
	 * 
	 * @description: 导入更新
	 * @author：
	 * @param userId
	 * @param userName
	 * @param importEntityType
	 * @param importModel
	 * @param opCause
	 * @param opScene
	 * @param rdoAssModel
	 * @param assType
	 * @param assEntityType
	 * @param listMap
	 * @param rows
	 * @param assIds
	 * @param updateIds
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jan 30, 2013 2:41:52 PM
	 */
	public List<Map<String,Object>> importUpdateResource(String userId,String userName,String importEntityType,String importModel,String opCause,String opScene,String rdoAssModel,String assType,String assEntityType,List<Map<String,String>> listMap,List<String>  rows,List<String> assIds,List<String> updateIds);
	/**
	 * 
	 * @description: 导入删除新增
	 * @author：
	 * @param userId
	 * @param userName
	 * @param importEntityType
	 * @param importModel
	 * @param opCause
	 * @param opScene
	 * @param rdoAssModel
	 * @param assType
	 * @param assEntityType
	 * @param listMap
	 * @param rows
	 * @param assIds
	 * @param updateIds
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jan 30, 2013 2:42:01 PM
	 */
	public List<Map<String,Object>> importDeleteAddResource(String userId,String userName,String importEntityType,String importModel,String opCause,String opScene,String rdoAssModel,String assType,String assEntityType,List<Map<String,String>> listMap,List<String>  rows,List<String> assIds,List<String> updateIds);

}
