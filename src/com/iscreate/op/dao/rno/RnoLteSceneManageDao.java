package com.iscreate.op.dao.rno;

import java.sql.Statement;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.LteCellQueryCondition;
import com.iscreate.op.action.rno.Page;
import com.iscreate.op.pojo.rno.AreaRectangle;
import com.iscreate.op.pojo.rno.RnoMapLnglatRelaGps;

public interface RnoLteSceneManageDao {
	
	/**
	 * 
	 * 
	 * @title 查询lte网格数据数量通过城市ID
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2015-7-5上午11:56:14
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public long queryLteGridDataCntByCityId(final long cityId);
	/**
	 * 
	 * @title 分页查询符合条件的LTE网格数据记录
	 * @param cityId
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2015-7-5下午12:05:29
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> queryLteGridDataByPage(final 
			long cityId,final Page page);
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
	 * @date 2015-9-14上午16:38:01
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> exportLteGridDataByCityId(final 
			long cityId);
	/**
	 * 
	 * @title 通过区域ID获取指定区域的地图经纬度纠偏map集合
	 * @param areaid
	 * @param mapType
	 * @return
	 * @author chao.xj
	 * @date 2015-7-6下午5:07:53
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public Map<AreaRectangle, List<RnoMapLnglatRelaGps>> getSpecialAreaRnoMapLnglatRelaGpsMapList(Statement stmt,
			long areaid, String mapType);
	/**
	 * 通过场景ID查询地理场景信息
	 * @return
	 * @author chen.c10
	 * @date 2015-7-7 上午10:55:49
	 * @company 怡创科技
	 * @version V1.0
	 */
	public List<Map<String, Object>> queryLteRegionSceneInfoBySceneId(String sceneId);
	/**
	 * 更新地理场景信息
	 * @param sceneDataMap
	 * @return
	 * @author chen.c10
	 * @date 2015-7-8 下午6:18:54
	 * @company 怡创科技
	 * @version V1.0
	 */
	public boolean updateLteRegionSceneDataBySceneId(Map<String, String> sceneParam);
	/**
	 * 通过场景类型查询地理场景名称列表
	 * @param sceneType
	 * @return
	 * @author chen.c10
	 * @date 2015-7-9 下午12:20:35
	 * @company 怡创科技
	 * @version V1.0
	 */
	public List<Map<String, Object>> queryLteRegionSceneNameList();
	
	/**
	 * 通过场景ID删除地理场景数据
	 * @param sceneId
	 * @return
	 * @author chen.c10
	 * @date 2015-7-10 上午11:11:03
	 * @company 怡创科技
	 * @version V1.0
	 */
	public boolean deleteLteRegionSceneData(String sceneId);
	/**
	 * 插入新地理场景管理数据
	 * @param sceneParam
	 * @return
	 * @author chen.c10
	 * @date 2015-7-10 下午1:46:12
	 * @company 怡创科技
	 * @version V1.0
	 */
	public boolean insertLteRegionSceneData(Map<String, String> sceneParam);
	/**
	 * 通过场景ID查询时间场景信息
	 * @return
	 * @author chen.c10
	 * @date 2015-7-7 上午10:55:49
	 * @company 怡创科技
	 * @version V1.0
	 */
	public List<Map<String, Object>> queryLteTimeSceneInfoBySceneId(String sceneId);
	/**
	 * 更新时间场景信息
	 * @param sceneDataMap
	 * @return
	 * @author chen.c10
	 * @date 2015-7-8 下午6:18:54
	 * @company 怡创科技
	 * @version V1.0
	 */
	public boolean updateLteTimeSceneDataBySceneId(Map<String, String> sceneParam);
	/**
	 * 通过场景类型查询时间场景名称列表
	 * @param sceneType
	 * @return
	 * @author chen.c10
	 * @date 2015-7-9 下午12:20:35
	 * @company 怡创科技
	 * @version V1.0
	 */
	public List<Map<String, Object>> queryLteTimeSceneNameList();
	
	/**
	 * 通过场景ID删除时间场景数据
	 * @param sceneId
	 * @return
	 * @author chen.c10
	 * @date 2015-7-10 上午11:11:03
	 * @company 怡创科技
	 * @version V1.0
	 */
	public boolean deleteLteTimeSceneData(String sceneId);
	/**
	 * 插入新时间场景管理数据
	 * @param sceneParam
	 * @return
	 * @author chen.c10
	 * @date 2015-7-10 下午1:46:12
	 * @company 怡创科技
	 * @version V1.0
	 */
	public boolean insertLteTimeSceneData(Map<String, String> sceneParam);
}
