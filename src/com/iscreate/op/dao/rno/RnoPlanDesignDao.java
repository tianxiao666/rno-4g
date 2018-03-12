package com.iscreate.op.dao.rno;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.Area;
import com.iscreate.op.pojo.rno.Cell;
import com.iscreate.op.pojo.rno.NcellAnalysisCondition;
import com.iscreate.op.pojo.rno.PlanConfig;
import com.iscreate.op.pojo.rno.RedundantNCell;
import com.iscreate.op.pojo.rno.RnoCellDescriptor;
import com.iscreate.op.pojo.rno.RnoCellStructDesc;
import com.iscreate.op.pojo.rno.RnoCellStructDescWrap;
import com.iscreate.op.pojo.rno.RnoGisCell;
import com.iscreate.op.pojo.rno.RnoHandover;
import com.iscreate.op.pojo.rno.RnoHandoverDescriptor;
import com.iscreate.op.pojo.rno.RnoInterferenceDescriptor;
import com.iscreate.op.pojo.rno.RnoNcell;
import com.iscreate.op.pojo.rno.RnoNcsDescriptor;
import com.iscreate.op.pojo.rno.RnoNcsDescriptorWrap;
import com.iscreate.op.pojo.system.SysArea;


public interface RnoPlanDesignDao {

	/**
	 * 根据areaid获取系统配置方案名称
	 * @param areaId
	 * @return
	 * @author chao.xj
	 * @date 2013-11-6上午09:26:34
	 */
	public List<RnoCellDescriptor> getSysCoufigureSchemeFromRnoCellDesc(long areaId);
	/**
	 * 根据areaid获取临时分析方案名称
	 * @param areaId
	 * @return
	 * @author chao.xj
	 * @date 2013-11-6上午09:26:34
	 */
	public List<RnoCellDescriptor> getTempAnalyseSchemeFromRnoCellDesc(long areaId);
	

	/**
	 * 
	 * @description: 统计指定区域范围小区的频率复用情况
	 * @author：yuan.yw
	 * @param selectConfig
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Nov 7, 2013 11:59:55 AM
	 */
	public List<Map<String,Object>> staticsFreqReuseInfoInArea(final PlanConfig selectConfig);
	
	/**
	 * 
	 * @description: 根据类型获取选择的小区配置或小区干扰中分析列表中的小区的gis信息
	 * @author：yuan.yw
	 * @param selectConfig
	 * @return     
	 * @return List<RnoGisCell>     
	 * @date：Nov 8, 2013 10:08:37 AM
	 */
	public List<RnoGisCell> getFreqReuseCellGisInfoFromSelectionList(final PlanConfig selectConfig);

	
	/**
	 * 获取区域下的干扰配置
	 * @param areaId
	 * @return
	 * @author brightming
	 * 2013-11-7 下午6:38:34
	 */
	public List<RnoInterferenceDescriptor> getRnoInterferenceDescriptorInArea(long areaId);
	
	/**
	 * 根据id获取RnoInterferenceDescriptor
	 * @param id
	 * @return
	 * @author brightming
	 * 2013-11-7 下午8:02:26
	 */
	public RnoInterferenceDescriptor getRnoInterferenceDescriptorById(long id);
	/**
	 * 根据configId获取RNO_CELL_DESCRIPTOR
	 * @param areaId
	 * @return
	 * @author chao.xj
	 * @date 2013-11-6上午09:26:34
	 */
	public List<RnoCellDescriptor> getRnoCellDescByConfigId(final long configId);
	/**
	 * 根据areaid,tempname获取临时方案名称
	 * @param areaId
	 * @return
	 * @author chao.xj
	 * @date 2013-11-6上午09:26:34
	 */
	public List<RnoCellDescriptor> getTempAnalyseSchemeFromRnoCellDesc(final long areaId,final String tempname);

	/**
	 * 通过小区ID查询一条小区数据
	 */
	public SysArea queryAreaById(final long id);
	/**
	 * 获取指定区域下的所有临时小区名
	 */
	@SuppressWarnings("unchecked")
	public List<String> getTempCellNameByAreaId(final Long areaId);
	/**
	 * 通过areaId/configids获得BCCH/BSIC相同，ID和label不同的小区集合数据
	 * @title 
	 * @param reSelected
	 * @param areaIds
	 * @param configIds
	 * @param bcch
	 * @param bsic
	 * @return
	 * @author chao.xj
	 * @date 2014-4-16上午10:16:07
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String,Object>> getCoBsicCellsByAreaId(boolean reSelected,String areaIds,String configIds,final int bcch,final String bsic);
	/**
	 * 全网范围内：通过areaId/configids获得BCCH/BSIC相同，ID和label不同的小区集合数据
	 * @title 
	 * @param reSelected
	 * @param areaIds
	 * @param configIds
	 * @return
	 * @author chao.xj
	 * @date 2014-4-16下午04:20:30
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String,Object>> getCoBsicCellsByAreaId(boolean reSelected,String areaIds,String configIds);
	
	/**
	 * 查找指定区域下，id为参数指定的rnoncsdescriptor列表
	 * @param configIds
	 * @param areaIds
	 * @return
	 * @author brightming
	 * 2013-11-15 上午11:11:03
	 */
	public List<RnoNcsDescriptor> getRnoNcsDescriptorByIdsWithInAreas(List<Long> configIds,
			List<Long> areaIds);
	
	/**
	 * 分页查询ncs描述信息
	 * @param page
	 * @param attachParams
	 * @return
	 * @author brightming
	 * 2013-11-15 上午11:19:55
	 */
	public List<RnoNcsDescriptorWrap> queryNcsDescriptorByPage(Page page,
			Map<String, Object> attachParams);
	
	/**
	 * 查询指定条件的ncs 描述数量
	 * @param attachParams
	 * @return
	 * @author brightming
	 * 2013-11-15 上午11:20:39
	 */
	public int getNcsDescriptorCount(Map<String, Object> attachParams);
	
	/**
	 *  查找指定区域下，id为参数指定的RnoCellStructDesc列表
	 * @param configIds
	 * @param areaIds
	 * @return
	 * @author brightming
	 * 2013-11-16 下午12:08:56
	 */
	public List<RnoCellStructDesc> getRnoCellStructDescByIdsWithInAreas(
			List<Long> configIds, List<Long> areaIds);
	/**
	 * 获取符合条件的小区结构指标数量
	 * @param attachParams
	 * @return
	 * @author brightming
	 * 2013-11-16 下午12:15:31
	 */
	public int getCellStructDescriptorCount(Map<String, Object> attachParams);
	
	/**
	 * 分页查询符合条件的小区结构指标数据
	 * @param page
	 * @param attachParams
	 * @return
	 * @author brightming
	 * 2013-11-16 下午12:15:57
	 */
	public List<RnoCellStructDescWrap> queryCellStructDescriptorByPage(
			Page page, Map<String, Object> attachParams);
	
	/**
	 * 结合参数获取冗余邻区
	 * @param cell
	 * @param ncsDescId
	 * @param handoverDescId
	 * @param cellStructDescId
	 * @param condition
	 * @return
	 * @author brightming
	 * 2013-11-16 下午4:33:17
	 */
	/*public List<RedundantNCell> getRedundantNcell(String cell,long ncsDescId,long handoverDescId,long cellStructDescId,NcellAnalysisCondition condition);*/
	
	/**
	 * 结合参数获取冗余邻区
	 * @param cell
	 * @param ncsDescId
	 * @param cellStructDescId
	 * @param condition
	 * @return
	 * @author brightming
	 * 2013-11-16 下午4:33:17
	 */
	/*public List<RedundantNCell> getOmitNcell(String cell,long ncsDescId,long cellStructDescId,NcellAnalysisCondition condition);*/
	/**
	 * 通过区域和话统时间返回切换描述数据的多少
	 * @param areaId
	 * @param staticTime
	 * @return
	 * @author chao.xj
	 * @date 2013-11-14上午11:07:45
	 */
	public List<RnoHandoverDescriptor> queryRnoHandoverDescByAreaAndStaticTime(long areaId,String staticTime);
	/**
	 * 保存一个切换统计描述记录对象
	 * @param rnoHandoverDescriptor
	 * @author chao.xj
	 * @date 2013-11-14下午12:01:23
	 */
	public void saveOneRnoHandOverDesc(RnoHandoverDescriptor rnoHandoverDescriptor);
	/**
	 * 保存一个切换统计记录对象
	 * @param rnoHandover
	 * @author chao.xj
	 * @date 2013-11-14下午12:01:23
	 */
	public void saveOneRnoHandOver(RnoHandover rnoHandover);
	/**
	 * 删除一个切换统计描述数据
	 * @param rnoHandoverDescriptor
	 * @author chao.xj
	 * @date 2013-11-14上午11:25:21
	 */
	public void deleteOneRnoHandoverDesc(RnoHandoverDescriptor rnoHandoverDescriptor);
	/**
	 * 通过rnohanddescID删除其下所有的切换统计数据
	 * @param rnoHandoverDescriptor
	 * @author chao.xj
	 * @date 2013-11-14上午11:53:15
	 */
	public void deleteRnoHandoverByRnoHandDescId(RnoHandoverDescriptor rnoHandoverDescriptor);
	/**
	 * 通过区域和日期返回切换描述数据的多少
	 * @param areaId
	 * @param date
	 * @return
	 * @author chao.xj
	 * @date 2013-11-14上午11:07:45
	 */
	public List<Map<String,Object>> queryRnoHandoverDescByAreaAndDate(Page page,long areaId,String date[],boolean sqlForCount);
	/**
	 * 通过日期等条件语句查找返回不同总数量记录
	 * @param date
	 * @param sqlForCount
	 * @return
	 * @author chao.xj
	 * @date 2013-11-16下午09:58:18
	 */
	public int getTotalHODescCount(String date[],boolean sqlForCount,long areaId);
	/**
	 * 通过日期等条件语句查找返回不同sql语句
	 * @param date
	 * @param sqlForCount
	 * @return
	 * @author chao.xj
	 * @date 2013-11-16下午10:00:17
	 */
	public String getHODescSql(String date[],boolean sqlForCount,long areaId);
	/**
	 * 根据HoDescId获取切换统计描述记录
	 * @param HoDescId
	 * @return
	 */
	public List<RnoHandoverDescriptor> getRnoHandoverDescriptorByHoDescId(final String HoDescId[]);
	/**
	 * 通过区域ID获取一个area对象
	 * @param areaid
	 * @return
	 * @author chao.xj
	 * @date 2013-11-19下午02:41:51
	 */
	public SysArea getAreaobjByareaId(long areaid);
	/**
	 * 通过源区和目标小区确认是否是邻区关系
	 * @param sourcecell
	 * @param targetcell
	 * @return
	 * @author chao.xj
	 * @date 2013-11-19下午03:47:15
	 */
	public boolean queryNcellByoCell(String sourcecell,String targetcell);
	/**
	 * 通过传入小区label得到一个小区对象
	 * @param label
	 * @return
	 * @author chao.xj
	 * @date 2013-11-19下午03:59:20
	 */
	public Cell queryCellobjByLabel(String label);
	
	/**
	 * 获取指定小区配置表的所有小区的冗余邻区
	 * 
	 * @param cellConfig
	 * @param ncsDescId
	 * @param handoverDescId
	 * @param cellStructDescId
	 * @param condition
	 * @return
	 * @author brightming 2013-11-19 下午8:42:39
	 */
/*	public List<RedundantNCell> getAllRedundantCellsByCondition(
			final PlanConfig cellConfig, final long ncsDescId,
			final long handoverDescId, final long cellStructDescId,
			final NcellAnalysisCondition condition) ;*/
	
	/**
	 * 获取指定配置下的所有漏定邻区
	 * 
	 * @param cellConfig
	 * @param ncsDescId
	 * @param cellStructDescId
	 * @param condition
	 * @return
	 * @author brightming 2013-11-20 上午10:37:13
	 */
	@SuppressWarnings("unchecked")
/*	public List<RedundantNCell> getAllOmitCellsByCondition(
			PlanConfig cellConfig, long ncsDescId, long cellStructDescId,
			NcellAnalysisCondition condition);*/
	/**
	 * 通过源与目标小区得到其经纬度数据113.4087,23.07341,113.27481,23.04926
	 * @param sourcecell
	 * @param targetcell
	 * @return
	 * @author chao.xj
	 * @date 2013-11-20上午10:51:35
	 */
	public List<String> getLnglatsBySourceCellAndTargetCell(final String sourcecell,final String targetcell);
	/**
	 * 通过两个小区查询其共有的邻区
	 * @param sourcecell
	 * @param targetcell
	 * @return
	 * @author chao.xj
	 * @date 2013-11-20上午11:31:22
	 */
	public List<RnoNcell> queryCommonNcellByTwoCell(final String sourcecell,final String targetcell);
	/**
	 * 获取指定区域和模糊查询条件的小区配置描述的总量
	 * @return
	 * @author chao.xj
	 * @date 2013-12-10下午02:06:15
	 */
	public int getTotalCellDescCountByArea(final long areaId,final String schemeName,final boolean sysDefault);
	/**
	 * 通过区域和方案名分页查询小区配置描述数据
	 * @param page
	 * @param areaId
	 * @param schemeName
	 * @return
	 * @author chao.xj
	 * @date 2013-12-10下午02:30:01
	 */
	public List<Map<String, Object>> queryCellConfigureDescByAreaAndScheme(
			Page page, final String areaIds,final String schemeName,final boolean sysDefault);
	/**
	 * 通过小区配置ID的字符串获取小区配置描述数据
	 * @param configIds
	 * @return
	 * @author chao.xj
	 * @date 2013-12-10下午05:46:52
	 */
	public List<Map<String, Object>> getCellConfigureDescByConfigIds(final String configIds);
	/**
	 * 改造：获取符合条件的干扰数据的数量
	 * @param areaId
	 * @param attachParams
	 * @param isDefault
	 * @param schemeName
	 * @param forcount
	 * @return
	 * @author chao.xj
	 * @date 2013-12-13下午03:54:34
	 */
	public int getTotalInterferenceCount(final long areaId,final HashMap<String, Object> attachParams,final boolean isDefault,
			final String schemeName,final boolean forcount);
	/**
	 * 改造：查询干扰数据通过分页方式
	 * @param page
	 * @param queryCondition
	 * @param searchType
	 * @return
	 * @author chao.xj
	 * @date 2013-12-12下午07:03:17
	 */
	public List<Map<String,Object>> queryInterferenceDataByPage(final Page page,final long areaId,final HashMap<String, Object> attachParams,final boolean isDefault,
			final String schemeName,final boolean forcount);
	/**
	 * 改造：通过干扰配置ID的字符串获取干扰指标描述数据
	 * @param configIds
	 * @return
	 * @author chao.xj
	 * @date 2013-12-10下午05:46:52
	 */
	public List<Map<String, Object>> getInterferenceDataDescByConfigIds(final String interDescIds);
	/**
	 * 通过城市id获取其下相应的区域集合
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2013-12-23上午11:46:11
	 */
	public List<Map<String, Object>> getSpecialLevalAreaByCityId(final long cityId);
	/**
	 * 通过小区名获取NCS干扰数据的集合
	 * @title 
	 * @param label
	 * @return
	 * @author chao.xj
	 * @date 2014-4-24上午10:33:49
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getSpecifyCellInterferenceAnalysis(final String label,final long selectId);
	/**
	 * 通过ncs描述ID获取NCS下的干扰数据集合
	 * @title 
	 * @param ncsDescId
	 * @return
	 * @author chao.xj
	 * @date 2014-4-24下午05:46:02
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getTotalAnalysisGisCellByNcsDescId(final long ncsDescId,long startIndex, long cnt);
	/**
	 * 通过ncs描述ID获取NCS干扰总数量
	 * @title 
	 * @param ncsDescId
	 * @return
	 * @author chao.xj
	 * @date 2014-4-24下午06:06:36
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long getNcsInfoCount(final long ncsDescId);
	/**
	 * 通过ncsid获取ncs描述表的区域信息[city,area]
	 * @title 
	 * @param ncsDescId
	 * @return
	 * @author chao.xj
	 * @date 2014-4-29下午01:40:02
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getNcsAreaInfoByNcsId(final long ncsDescId);
	
	/**
	 * 
	 * @author Liang YJ
	 * 2014-4-21 下午3:09:03
	 * @param cell
	 * @param ncsDescIds
	 * @param handoverDescIds
	 * @param condition
	 * @param areaId
	 * @param cityId
	 * @return result
	 * @description 根据参数查询小区的冗余邻区和漏定邻区
	 */
	public Map<String, List<RedundantNCell>> getRedundantAndOmitNcells(String cell, List<Long> ncsDescIds, List<Long> handoverDescIds, NcellAnalysisCondition condition, long areaId);
	/**
	 * 
	 * @author Liang YJ
	 * 2014-4-21 下午3:09:03
	 * @param cell
	 * @param ncsDescIds
	 * @param cellResult
	 * @param handoverDescIds
	 * @param condition
	 * @param areaId
	 * @return result
	 * @description 根据参数查询小区的冗余邻区和漏定邻区
	 */
	public Map<String, List<RedundantNCell>> getRedundantAndOmitNcells(String cell, List<Long> ncsDescIds, List<Object> cellResult, List<Long> handoverDescIds, NcellAnalysisCondition condition, long areaId);
	
	
	/**
	 * 
	 * @author Liang YJ
	 * 2014-4-21 下午5:01:37
	 * @param ncsDescIds
	 * @param handoverDescIds
	 * @param condition
	 * @param areaId
	 * @param cityId
	 * @return result
	 * @description 根据条件全网分析冗余邻区和漏定邻区
	 */
	//public Map<String, List<RedundantNCell>> getAllRedundantAndOmitCellsByCondition(List<Long> ncsDescIds, List<Long> handoverDescIds, NcellAnalysisCondition condition, long areaId, long cityId);
	/**
	 * 
	 * @author Liang YJ
	 * 2014-4-21 下午5:01:37
	 * @param ncsDescIds
	 * @param cellResult
	 * @param handoverDescIds
	 * @param condition
	 * @param areaId
	 * @return result
	 * @description 根据条件全网分析冗余邻区和漏定邻区
	 */
	//public Map<String, List<RedundantNCell>> getAllRedundantAndOmitCellsByCondition(List<Long> ncsDescIds,List<Object> cellResult, List<Long> handoverDescIds, NcellAnalysisCondition condition, long areaId);
	
	/**
	 * 
	 * @title 通过区域ID获取区域级别信息
	 * @param areaId
	 * @return
	 * @author chao.xj
	 * @date 2014-6-24下午05:12:47
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getAreaLevelByAreaId(final long areaId);
	/**
	 * 196 2014-06-24 12:12:09　2014\6
	 * @title 获取taskid及成功完成时间信息通过区域id
	 * @param areaId
	 * @return
	 * @author chao.xj
	 * @date 2014-6-24下午06:14:42
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getTaskIdAndCompleteTimeByAreaId(final long areaId);
	/**
	 * 获取区域最新的NCS干扰矩阵
	 * @param areaId
	 * @return
	 * @author peng.jm
	 * @date 2014-8-19下午04:52:44
	 */
	public List<Map<String, Object>> getLatelyInterferMartixRecByAreaId(
			long areaId);
	/**
	 * 
	 * @title 判断该ID是否为市
	 * @param areaId
	 * @return
	 * @author chao.xj
	 * @date 2014-6-24下午05:20:15
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean whetherCity(long areaId);
	/**
	 * 获取小区的bcch与tch
	 * @author peng.jm
	 * @date 2014-8-13下午05:21:58
	 */
	public List<Map<String, Object>> getCellFreqByCellName(String cell);
	/**
	 * 更新小区的bcch与tch
	 * @author peng.jm
	 * @date 2014-8-13下午05:21:58
	 */
	public boolean updateCellFreqThroughCellName(String cell, String bcch, String tch);
	/**
	 * 通过id获取干扰矩阵的目录
	 * @param martixDescId
	 * @return
	 * @author peng.jm
	 * @date 2014-8-19下午05:32:10
	 */
	public List<Map<String, Object>> getMartixDirById(long martixDescId);
}
