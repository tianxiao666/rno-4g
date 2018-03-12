package com.iscreate.op.dao.rno;

import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.StsCondition;
import com.iscreate.op.pojo.rno.RnoCityQuality;
import com.iscreate.op.pojo.rno.RnoCityqulDetail;
import com.iscreate.op.pojo.rno.RnoGisCell;
import com.iscreate.op.pojo.rno.RnoStsResult;
import com.iscreate.op.pojo.rno.StsConfig;

public interface RnoTrafficStaticsDao {

	/**
	 * 保存城市质量指标对象
	 * @param rnoCityQuality
	 */
	public void saveRnoCityQuality(RnoCityQuality rnoCityQuality);
	
	/**
	 * 保存城市质量指标详情对象
	 * @param rnoCityQuality
	 */
	public void saveRnoCityQulDetail(RnoCityqulDetail rnoCityqulDetail);
	
	
	/**
	 * 通过地区名得到区域ID
	 * @param name
	 * @return
	 */
	public List<String> getAreaIdByName(String name);
	/**
	 * 从网络质量指标表中得到最新的CityQulId
	 * @return
	 */
	public List<String> getMaxCityQulIdFromRNOCityQuality();
	/**
	 * 通过区域和发生的日期从网络质量指标表中得到CityQulId
	 */
	public List<String> getCityQulIdByAreaAndDate(Long areaId,String staticTime);
	/**
	 * 通过CityQulId删除一条网络质量指标数据
	 * @param CityQulId
	 * @return
	 */
	public int deleteCityQualityById(Long CityQulId);
	/**
	 * 通过CityQulId删除若干条网络质量指标详情数据
	 * @param CityQulId
	 * @return
	 */
	public int deleteCityQualityDetailById(Long CityQulId);
	
	/**
	 * 获取指定分析列表中的小区的gis信息
	 * @param selConfigs
	 * @return
	 * @author brightming
	 * 2013-10-11 下午5:51:35
	 */
	public List<RnoGisCell> getGisCellInfoFromSelectionList(
			List<StsConfig> selConfigs);

	/**
	 * 统计给的字段的情况，统计范围由selConfigs指定
	 * @param fieldName
	 * @param selConfigs
	 * @return
	 * @author brightming
	 * 2013-10-14 上午11:46:28
	 */
	public List<RnoStsResult> stsSpecFieldInSelConfig(String fieldName,
			List<StsConfig> selConfigs);
	
	
	/**
	 * 获取符合指定条件的小区，以及其相应的指标值
	 * @param needCellType
	 * veryidlecell	超闲小区	小区配置大于2载频；小区无线利用率最大时，每线话务量小于0.15Erl，或PDCH承载速率小于3kbps的小区。
       overloadcell	超忙小区	指20点 每线话务量大于0.9erl的小区数。
       highuseradiocell	高无线利用率小区	指每天20点 无线资源利用率90%以上的小区数。
       highcongindatacell	数据高拥塞率小区	下行TBF拥塞率大于5%的小区，取每天21：00-24：00的平均值，剔除小区下行数据业务总流量小于100KB的时段。
       badlyicmcell	高干扰小区	干扰系数=（icm4+icm5）/(ICM1+ICM2+……+ICM5)>30，高干扰小区

	 * @param selConfigs
	 * @return
	 * @author brightming
	 * 2013-10-16 上午11:27:43
	 */
	public List<RnoStsResult> selectSpecialCellInSelConfig(String needCellType,List<StsConfig> selConfigs);
	
	/**
	 * 获取某个区域的繁忙小区
	 * @param areaId
	 * @return
	 * @author brightming
	 * 2013-10-19 下午4:10:48
	 */
	public List<RnoStsResult> staticsHeavyLoadCellWithinArea(long areaId);
	
	
	/**
	 * 获取指定小区的邻区的话务情况
	 * @param cell
	 * @param areaId
	 * @return
	 * @author brightming
	 * 2013-10-19 下午4:19:29
	 */
	public List<RnoStsResult> staticsSpecialCellsNcellStsInfo(String cell,long areaId);
	
	/**
	 * 查询指定条件的时间段统计数据
	 * @param searchType
	 * @param condition
	 * @return
	 * @author brightming
	 * 2013-10-24 下午3:59:39
	 */
	public List<StsConfig> queryStsConfigByCondition(String searchType,
			StsCondition condition);
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
}
