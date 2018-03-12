package com.iscreate.op.service.rno;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.Area;
import com.iscreate.op.action.rno.model.GisCellQueryResult;
import com.iscreate.op.pojo.rno.Cell;
import com.iscreate.op.pojo.rno.CobsicCells;
import com.iscreate.op.pojo.rno.FreqInterIndex;
import com.iscreate.op.pojo.rno.NcellAnalysisCondition;
import com.iscreate.op.pojo.rno.PlanConfig;
import com.iscreate.op.pojo.rno.RedundantNCell;
import com.iscreate.op.pojo.rno.RnoCellDescriptor;
import com.iscreate.op.pojo.rno.RnoCellStructDesc;
import com.iscreate.op.pojo.rno.RnoCellStructDescWrap;
import com.iscreate.op.pojo.rno.RnoHandoverDescriptor;
import com.iscreate.op.pojo.rno.RnoInterferenceDescriptor;
import com.iscreate.op.pojo.rno.RnoNcell;
import com.iscreate.op.pojo.rno.RnoNcsDescriptor;
import com.iscreate.op.pojo.rno.RnoNcsDescriptorWrap;
import com.iscreate.op.pojo.system.SysArea;

public interface RnoPlanDesignService {

	/**
	 * 根据areaid获取系统配置方案名称
	 * @param areaId
	 * @return
	 * @author chao.xj
	 * @date 2013-11-6上午09:26:34
	 */
	public List<RnoCellDescriptor> getSysCoufigureSchemeFromRnoCellDesc(final Long areaId);
	/**
	 * 根据areaid获取临时方案名称
	 * @param areaId
	 * @return
	 * @author chao.xj
	 * @date 2013-11-6上午09:26:34
	 */
	public List<RnoCellDescriptor> getTempAnalyseSchemeFromRnoCellDesc(final long areaId);

	/**
	 * 根据configId获取RNO_CELL_DESCRIPTOR
	 * @param areaId
	 * @return
	 * @author chao.xj
	 * @date 2013-11-6上午09:26:34
	 */
	public List<RnoCellDescriptor> getRnoCellDescByConfigId(final long configId);


	/**
	 * 
	 * @description: 统计指定区域范围小区的频率复用情况
	 * @author：yuan.yw
	 * @param selectConfig
	 * @return     
	 * @return Map<Integer,Object>    
	 * @date：Nov 7, 2013 11:59:55 AM
	 */
	public Map<Integer,Object> staticsFreqReuseInfoInArea(PlanConfig selectConfig);
	/**
	 * 
	 * @description: 根据类型获取选择的小区配置或小区干扰中分析列表中的小区的gis信息
	 * @author：yuan.yw
	 * @param selectConfig
	 * @param page
	 * @return     
	 * @return GisCellQueryResult     
	 * @date：Nov 8, 2013 10:08:37 AM
	 */
	public GisCellQueryResult getFreqReuseCellGisInfoFromSelectionList(PlanConfig selectConfig,Page page);

	
	/**
	 * 获取区域下的干扰配置
	 * @param areaId
	 * @return
	 * @author brightming
	 * 2013-11-7 下午6:42:49
	 */
	public List<RnoInterferenceDescriptor> getRnoInterferenceDescriptorInArea(long areaId); 
	
	/**
	 * 根据获取RnoInterferenceDescriptor
	 * @param id
	 * @return
	 * @author brightming
	 * 2013-11-7 下午8:01:44
	 */
	public RnoInterferenceDescriptor getRnoInterferenceDescriptorById(long id);
	/**
	 * 通过相同bcchbsic的组合cobsic下有两个或多个label,从而保存CobsicCells对象集合数据
	 * @title 
	 * @param reSelected
	 * @param areaIds
	 * @param configIds
	 * @param bcch
	 * @param bsic
	 * @return
	 * @author chao.xj
	 * @date 2014-4-16上午10:14:20
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<CobsicCells> saveCobsicCellsByCoBsicKeys(boolean reSelected,String areaIds,String configIds,int bcch,String bsic);
	/**
	 * 全网范围内：通过相同bcchbsic的组合cobsic下有两个或多个label,从而保存CobsicCells对象集合数据
	 * @title 
	 * @param reSelected
	 * @param areaIds
	 * @param configIds
	 * @return
	 * @author chao.xj
	 * @date 2014-4-16下午04:18:08
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<CobsicCells> saveCobsicCellsByCoBsicKeys(boolean reSelected,String areaIds,String configIds);
	
	/**
	 * 获取指定id的RnoNcsDescriptor对象
	 * @param configIds
	 * @param areaids 用户允许查看的area id
	 * @return
	 * @author brightming
	 * 2013-11-14 下午7:17:06
	 */
	public List<RnoNcsDescriptor> getRnoNcsDescriptorByIds(List<Long> configIds,List<Long> areaIds);
	
	/**
	 * 分页查询ncs描述信息
	 * @param page
	 * @param attachParams
	 * @return
	 * @author brightming
	 * 2013-11-15 上午11:08:48
	 */
	public List<RnoNcsDescriptorWrap> queryNcsDescriptorByPage(Page page,
			HashMap<String, Object> attachParams);
	
	/**
	 * 获取满足条件的ncs desc的数量
	 * @param attachParams
	 * @return
	 * @author brightming
	 * 2013-11-15 下午12:03:13
	 */
	public int  getNcsDescriptorTotalCnt(HashMap<String, Object> attachParams);
	
	/**
	 * 获取指定id的RnoCellStructDesc对象
	 * @param cfids
	 * @param areaIds
	 * @return
	 * @author brightming
	 * 2013-11-16 下午12:07:05
	 */
	public List<RnoCellStructDesc> getRnoCellStructDescriptorByIds(
			List<Long> cfids, List<Long> areaIds);
	
	/**
	 * 查询符合条件的小区结构指标数据的数量
	 * @param attachParams
	 * @return
	 * @author brightming
	 * 2013-11-16 下午12:13:55
	 */
	public int getCellStructDescTotalCnt(HashMap<String, Object> attachParams);
	
	/**
	 * 分页获取符合条件的小区结构指标数据
	 * @param page
	 * @param attachParams
	 * @return
	 * @author brightming
	 * 2013-11-16 下午12:14:15
	 */
	public List<RnoCellStructDescWrap> queryCellStructDescByPage(Page page,
			HashMap<String, Object> attachParams);
	
	/**
	 * 获取指定小区的冗余邻区
	 * @param cell
	 * @param cellConfig
	 * @param handoverConfig
	 * @param cellStructConfig
	 * @param ncsConfig
	 * @param condition
	 * @return
	 * @author brightming
	 * 2013-11-18 下午4:49:13
	 */
/*	public List<RedundantNCell> getRedundantNcellOfCell(String cell,
			PlanConfig cellConfig, PlanConfig handoverConfig,
			PlanConfig cellStructConfig, PlanConfig ncsConfig,
			NcellAnalysisCondition condition);*/
	
	/**
	 * 获取指定小区的漏定邻区
	 * @param cell
	 * @param cellConfig
	 * @param handoverConfig
	 * @param cellStructConfig
	 * @param ncsConfig
	 * @param condition
	 * @return
	 * @author brightming
	 * 2013-11-18 下午4:55:16
	 */
/*	public List<RedundantNCell> getOmitNcellOfCell(String cell,
			PlanConfig cellConfig, PlanConfig handoverConfig,
			PlanConfig cellStructConfig, PlanConfig ncsConfig,
			NcellAnalysisCondition condition);*/
	
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
	 * 获取小区间的距离
	 * @param lnglats
	 * @return
	 * @author chao.xj
	 * @date 2013-11-20上午10:56:40
	 */
	public double getDistanceBetweenTheCells(String sourcecell, String targetcell);
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
	 * 获取全网冗余邻区
	 * @param cellConfig
	 * @param handoverConfig
	 * @param cellStructConfig
	 * @param ncsConfig
	 * @param condition
	 * @return
	 * @author brightming
	 * 2013-11-20 上午11:24:13
	 */
/*	public List<RedundantNCell> getAllRedundantCellsByCondition(
			PlanConfig cellConfig, PlanConfig handoverConfig,
			PlanConfig cellStructConfig, PlanConfig ncsConfig,
			NcellAnalysisCondition condition);*/
	
	/**
	 * 获取全网漏定邻区
	 * @param cellConfig
	 * @param cellStructConfig
	 * @param ncsConfig
	 * @param condition
	 * @return
	 * @author brightming
	 * 2013-11-20 上午11:24:31
	 */
/*	public List<RedundantNCell> getAllOmitCellsByCondition(
			PlanConfig cellConfig, PlanConfig cellStructConfig,
			PlanConfig ncsConfig, NcellAnalysisCondition condition);*/
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
	 * 通过小区名获取NCS干扰指标数据的集合
	 * @title 
	 * @param label
	 * @return
	 * @author chao.xj
	 * @date 2014-4-24上午10:33:49
	 * @company 怡创科技
	 * @version 1.2
	 */
	public Map<String, FreqInterIndex> getSpecifyCellInterferenceAnalysis(final String label,final long selectId);
	/**
	 * 分页查询总分析ncs cell信息通过NCS描述ID
	 * @title 
	 * @param ncsDescId
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2014-4-24下午06:15:09
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getTotalAnalysisGisCellByNcsDescIdForPage(long ncsDescId,
			Page page);
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
	 * 2014-4-21 下午3:09:03
	 * @param cell
	 * @param ncsDescIds
	 * @param handoverDescIds
	 * @param condition
	 * @param cityId
	 * @return result
	 * @description 根据参数查询小区的冗余邻区和漏定邻区
	 */
	public Map<String, List<RedundantNCell>>getRedundantAndOmitNcells(String cell, List<Long> ncsDescIds, List<Long> handoverDescIds, NcellAnalysisCondition condition, long areaId);
	
	/**
	 * 
	 * @author Liang YJ
	 * 2014-4-21 下午3:09:03
	 * @param cell
	 * @param ncsDescIds
	 * @param cellResult
	 * @param handoverDescIds
	 * @param condition
	 * @return result
	 * @description 根据参数查询小区的冗余邻区和漏定邻区
	 */
	public Map<String, List<RedundantNCell>>getRedundantAndOmitNcells(String cell, List<Long> ncsDescIds, List<Object> cellResult, List<Long> handoverDescIds, NcellAnalysisCondition condition, long areaId);
	
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
	//public Map<String, List<RedundantNCell>> getAllRedundantAndOmitCellsByCondition(List<Long> ncsIds,List<Object> cellResult, List<Long> handoverIds, NcellAnalysisCondition condition, long areaId);

	/**
	 * 
	 * @title 
	 * @param taskId
	 * @param dir
	 * @return 获取干扰矩阵文件索引数据目录
	 * @author chao.xj
	 * @date 2014-6-24下午06:35:44
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String getInterferMatrixDir(long taskId,String dir);
	/**
	 * 获取干扰矩阵文件的目录
	 * @param martixDescId
	 * @return
	 * @author peng.jm
	 * @date 2014-8-19下午05:28:05
	 */
	public String getInterferMatrixDirById(long martixDescId);
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
}
