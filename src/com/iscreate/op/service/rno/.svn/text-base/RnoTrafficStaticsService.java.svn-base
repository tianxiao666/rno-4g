package com.iscreate.op.service.rno;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.StsCondition;
import com.iscreate.op.action.rno.model.Area;
import com.iscreate.op.action.rno.model.GisCellQueryResult;
import com.iscreate.op.pojo.rno.RnoStsResult;
import com.iscreate.op.pojo.rno.StsConfig;

public interface RnoTrafficStaticsService {

	/**
	 * 获取选定的分析列表的小区的gis信息
	 * @param selConfigs
	 * 选定的分析列表
	 * @param page
	 * 分页参数
	 * @return
	 * @author brightming
	 * 2013-10-11 下午5:33:38
	 */
	public GisCellQueryResult getGisCellInfoFromSelectionList(
			List<StsConfig> selConfigs, Page page);

	/**
	 * 统计指定类型的利用率或数量
	 * @param stsCode
	 * @param selConfigs
	 * @return
	 * @author brightming
	 * 2013-10-14 上午11:09:38
	 */
	public List<RnoStsResult> staticsResourceUtilizationRateInSelList(
			String stsCode, List<StsConfig> selConfigs);
	
	/**
	 * 
	 * @description: 分页查询话务数据
	 * @author：yuan.yw
	 * @param page 分页vo
	 * @param queryCondition 条件vo
	 * @param searchType 指标类型
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Oct 10, 2013 2:01:35 PM
	 */
	public List<Map<String,Object>> queryStsByPage(Page page,StsCondition queryCondition, String searchType);
	/**
	 * 
	 * @description: 获取符合条件话务数据数量
	 * @author：yuan.yw
	 * @param queryCondition 条件vo
	 * @param searchType 指标类型
	 * @param rebuildCache 是否重新生成缓存
	 * @return     
	 * @return int     
	 * @date：Oct 10, 2013 2:02:07 PM
	 */
	public int getTotalStsCount(StsCondition queryCondition, String searchType,boolean rebuildCache,boolean isAudio);
	/**
	 * 
	 * @description: 小区语音业务指标或小区数据业务指标查询话务数据，将小区英文信息缓存入MemCached(加载分析列表使用)
	 * @author：yuan.yw
	 * @param queryCondition
	 * @param rebuildCache     是否重新生成缓存
	 * @return void     
	 * @date：Oct 11, 2013 9:40:04 AM
	 */
	public List<Map<String,Object>> setCellDataToMemCached(StsCondition queryCondition,boolean rebuildCache,boolean isAudio);
	/**
	 * 得到用户权限内的省份数据集合
	 * @return　areaList
	 */
	public List<Map<String,Object>> getUserSpecLevelProvinces(long[] area_ids,String area_level);
	/**
	 * 获取用户可访问的指定级别的省份区域
	 */
	public List<Area> getProvincesInSpecLevelListByUserId(String accountId,String areaLevel);
	/**
	 * 得到用户权限内的市区数据集合
	 * @return　areaList
	 */
	public List<Map<String,Object>> getUserSpecLevelCityAreas(long[] area_ids,String area_level,long area_id);
	/**
	 * 获取用户可访问的指定级别的市区区域
	 */
	public String getCityAreasInSpecLevelListByUserId(String accountId,String areaLevel,long areaId);
	/**
	 * 得到用户权限内的县/区数据集合
	 * @param area_ids
	 * @param area_level
	 * @return
	 * @author chao.xj
	 * 2013-10-15下午02:45:00
	 */
	public List<Map<String,Object>> getUserSpecLevelCountys(long[] area_ids,String area_level);
	/**
	 * 获取用户可访问的指定级别的县/区区域
	 * @param accountId
	 * @param areaLevel
	 * @return
	 * @author chao.xj
	 * 2013-10-15下午02:51:20
	 */
	public List<Area> getCountysInSpecLevelListByUserId(String accountId,String areaLevel);

	/**
	 * 获取某类型的小区
	 * @param cellType
	 * @param selConfigs
	 * @return
	 * @author brightming
	 * 2013-10-16 下午4:17:34
	 */
	public List<RnoStsResult> staticsSpecialCellInSelList(String cellType,
			List<StsConfig> selConfigs);

	/**
	 * 获取指定区域的忙小区
	 * @param areaId
	 * @return
	 * @author brightming
	 * 2013-10-21 下午2:29:13
	 */
	public List<RnoStsResult> staticsHeavyLoadCellInArea(long areaId);

	/**
	 * 统计某指定小区的邻区的情况
	 * @param cell
	 * @param areaId
	 * @return
	 * @author brightming
	 * 2013-10-21 下午2:42:39
	 */
	public List<RnoStsResult> staticsSpecialCellsNcellStsInfo(String cell,
			long areaId);

	/**
	 * 查询符合条件的话务统计时间段数据
	 * @param page
	 * @param searchType
	 * @param queryCondition
	 * @return
	 * @author brightming
	 * 2013-10-24 下午3:14:32
	 */
	public List<StsConfig> queryStsPeriodByQueryCondition(Page page,
			String searchType, StsCondition queryCondition);
	/**
	 * 
	 * @description:获取 导出查询话务数据 输入流 
	 * @author：yuan.yw
	 * @param queryCondition
	 * @return     
	 * @return InputStream     
	 * @date：Oct 29, 2013 9:43:28 AM
	 */
	public InputStream exportQueryRnoStsList(StsCondition queryCondition,boolean isAudio);
	/**
	 * 通过RNO_STS表中的BSC_ID获取RNO_BSC的ENGNAME
	 * @return
	 * @author chao.xj
	 * @date 2013-11-4下午02:00:59
	 */
	public List<Map<String, Object>> getRnoBscEngName();
	/**
	 * 改造：查询小区语音或数据业务话统通过分页方式
	 * @param page
	 * @param queryCondition
	 * @param searchType
	 * @return
	 * @author chao.xj
	 * @date 2013-12-12下午07:03:17
	 */
	public List<Map<String,Object>> queryCellAudioOrDataStsByPage(final Page page, final StsCondition queryCondition,final String searchType);
	/**
	 * 改造：获取符合条件的小区语音或小区数据的数量
	 * @param searchType
	 * @param queryCondition
	 * @return
	 * @author chao.xj
	 * @date 2013-12-12下午06:18:25
	 */
	public int getTotalCellAudioOrDataCount(final String searchType,
			final StsCondition queryCondition);
	/**
	 * 改造：通过小区语音和数据话统配置ID的字符串获取话统指标描述数据
	 * @param configIds
	 * @return
	 * @author chao.xj
	 * @date 2013-12-10下午05:46:52
	 */
	public List<Map<String, Object>> getCellAudioOrDataDescByConfigIds(final String configIds);
	/**
	 * 通过小区查询邻区
	 * @param cell
	 * @return
	 * @author chao.xj
	 * @date 2013-12-26下午03:16:28
	 */
	public List<Map<String,Object>> queryNcellByCell(final String cell);
	/**
	 * 获取 导出查询话统指标数据 输入流 
	 * @param queryCondition
	 * @param isAudio
	 * @param rptTemplateId 通过模板ID判断缺省还是自主定义选择
	 * @return
	 * @author chao.xj
	 * @date 2014-1-9上午09:42:29
	 */
	public InputStream exportQueryRnoStsList(StsCondition queryCondition,boolean isAudio,long rptTemplateId);
}
