package com.iscreate.op.service.rno;

import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.LteCellQueryCondition;
import com.iscreate.op.action.rno.Page;

public interface RnoLteSceneManageService {

	/**
	 * 
	 * @title 查询lte网格数据数量通过城市ID
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2015-7-5下午12:07:41
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public long queryLteGridDataCntByCityId(long cityId);
	/**
	 * 
	 * @title 分页查询符合条件的LTE网格数据记录
	 * @param cityId
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2015-7-5下午12:08:06
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> queryLteGridDataByPage(long cityId,Page page);
	/**
	 * 
	 * @title 通过城市ID获取符合条件的LTE网格数据记录
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2015-7-6上午9:49:00
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> queryLteGridDataByCityId(final 
			long cityId);
	/**
	 * 
	 * @title 通过城市ID导出符合条件的LTE网格数据记录
	 * @param cityId
	 * @return
	 * @author li.tf
	 * @date 2015-9-14下午16:35:01
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> exportLteGridDataByCityId(final 
			long cityId);
	/**
	 * 创建网格小区数据excel文件
	 * @param fileRealPath
	 * @param res 小区数据
	 * @param grid 网格
	 * @return
	 */
	public Boolean createExcelFile(List<Map<String,Object>> res,List<Map<String,Object>> grid,String fileRealPath);
	/**
	 * 通过场景ID获取地理场景信息
	 * @return
	 * @author chen.c10
	 * @date 2015-7-7 上午10:49:21
	 * @company 怡创科技
	 * @version V1.0
	 */
	public List<Map<String, Object>> queryLteRegionSceneInfoBySceneId(String sceneId);
	/**
	 *更新地理场景管理信息
	 * @param sceneDataMap
	 * @return
	 * @author chen.c10
	 * @date 2015-7-8 下午6:16:48
	 * @company 怡创科技
	 * @version V1.0
	 */
	public boolean updateLteRegionSceneDataBySceneId(Map<String, String> sceneParam);
	/**
	 * 查询地理场景信息名称列表
	 * @return
	 * @author chen.c10
	 * @date 2015-7-9 下午12:16:43
	 * @company 怡创科技
	 * @version V1.0
	 * @param sceneType 
	 */
	public List<Map<String, Object>> queryLteRegionSceneNameList();
	/**
	 * 通过场景ID删除地理场景数据
	 * @param sceneId
	 * @return
	 * @author chen.c10
	 * @date 2015-7-10 上午11:08:26
	 * @company 怡创科技
	 * @version V1.0
	 */
	public boolean deleteLteRegionSceneData(String sceneId);
	/**
	 * 插入新地理场景管理数据
	 * @param sceneParam
	 * @return
	 * @author chen.c10
	 * @date 2015-7-10 下午1:44:18
	 * @company 怡创科技
	 * @version V1.0
	 */
	public boolean insertLteRegionSceneData(Map<String, String> sceneParam);
	
	/**
	 * 通过场景ID获取时间场景信息
	 * @return
	 * @author chen.c10
	 * @date 2015-7-7 上午10:49:21
	 * @company 怡创科技
	 * @version V1.0
	 */
	public List<Map<String, Object>> queryLteTimeSceneInfoBySceneId(String sceneId);
	/**
	 *更新时间场景管理信息
	 * @param sceneDataMap
	 * @return
	 * @author chen.c10
	 * @date 2015-7-8 下午6:16:48
	 * @company 怡创科技
	 * @version V1.0
	 */
	public boolean updateLteTimeSceneDataBySceneId(Map<String, String> sceneParam);
	/**
	 * 查询时间场景信息名称列表
	 * @return
	 * @author chen.c10
	 * @date 2015-7-9 下午12:16:43
	 * @company 怡创科技
	 * @version V1.0
	 * @param sceneType 
	 */
	public List<Map<String, Object>> queryLteTimeSceneNameList();
	/**
	 * 通过场景ID删除时间场景数据
	 * @param sceneId
	 * @return
	 * @author chen.c10
	 * @date 2015-7-10 上午11:08:26
	 * @company 怡创科技
	 * @version V1.0
	 */
	public boolean deleteLteTimeSceneData(String sceneId);
	/**
	 * 插入新时间场景管理数据
	 * @param sceneParam
	 * @return
	 * @author chen.c10
	 * @date 2015-7-10 下午1:44:18
	 * @company 怡创科技
	 * @version V1.0
	 */
	public boolean insertLteTimeSceneData(Map<String, String> sceneParam);
}
