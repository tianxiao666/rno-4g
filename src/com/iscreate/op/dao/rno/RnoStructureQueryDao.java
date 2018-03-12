package com.iscreate.op.dao.rno;

import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.Result;

import com.iscreate.op.action.rno.model.G2NcsQueryCond;
import com.iscreate.op.action.rno.vo.Threshold;
import com.iscreate.op.pojo.rno.RnoNcsDescriptor;
import com.iscreate.op.pojo.rno.RnoThreshold;
import com.iscreate.op.pojo.rno.RnoThreshold;
import com.iscreate.op.pojo.rno.RnoStructureAnalysisTask.TaskInfo;
import com.iscreate.op.service.rno.parser.vo.G4PciRec;

/**
 * @author brightming
 * @version 1.0
 * @created 17-һ��-2014 11:19:16
 */
public interface RnoStructureQueryDao {

	/**
	 * 分页查询ncs描述信息
	 * 
	 * @param condition
	 * @param startIndex
	 * startIndex:从0开始
	 * @param cnt
	 * 数量
	 */
	public List<Map<String,Object>> queryNcsDescriptorByPage(
			Map<String, String> condition, long startIndex, long cnt);

	/**
	 * 计算符合某条件的ncs的数量
	 * @param condition
	 * @return
	 * @author brightming
	 * 2014-1-17 下午12:19:20
	 */
	public long getNcsDescriptorCount(Map<String, String> condition);
	/**
	 * 分页查询ncs cell信息
	 * 
	 * @param ncsId
	 * startIndex:从0开始
	 * @param cnt
	 * 数量
	 */
	public List<Map<String, Object>> queryNcsCellDataByPage(long ncsId,
			long startIndex, long cnt);

	/**
	 * 计算ncs下的cell data 数量
	 * @param ncsId
	 * @return
	 * @author brightming
	 * 2014-1-17 下午12:19:52
	 */
	public long getNcsCellCount(long ncsId);
	/**
	 * 分页查询ncs ncell信息
	 * 
	 * @param ncsId
	 * startIndex:从0开始
	 * @param cnt
	 * 数量
	 */
	public List<Map<String, Object>> queryNcsNcellDataByPage(long ncsId,
			long startIndex, long cnt);

	/**
	 * 计算ncs下的ncell data 的数量
	 * @param ncsId
	 * @return
	 * @author brightming
	 * 2014-1-17 下午12:20:23
	 */
	public long getNcsNcellDataCount(long ncsId);
	/**
	 * 分页查询cluster信息
	 * 
	 * @param ncsId
	 * startIndex:从0开始
	 * @param cnt
	 * 数量
	 */
	public List<Map<String, Object>> getNcsClusterByPage(long ncsId,
			long startIndex, long cnt);
	
	/**
	 * 计算ncs下的cluster的数量
	 * @param ncsId
	 * @return
	 * @author brightming
	 * 2014-1-17 下午12:20:51
	 */
	public long getNcsClusterCount(long ncsId);

	/**
	 * 查看cluster下的小区
	 * @param clusterId
	 */
	public List<Map<String, Object>> getNcsClusterCell(long clusterId);

	/**
	 * 分页查询ncs 干扰矩阵信息
	 * 
	 * @param ncsId
	 * startIndex:从0开始
	 * @param cnt
	 * 数量
	 */
	public List<Map<String, Object>> queryNcsInterferMatrixByPage(long ncsId,
			long startIndex, long cnt);
	
	/**
	 * 计算ncs下的干扰矩阵的数量
	 * @param ncsId
	 * @return
	 * @author brightming
	 * 2014-1-17 下午12:21:23
	 */
	public long getNcsInterferMatrixCount(long ncsId);
	
	/**
	 * 统计区域破坏系数 
	 * @param ncsIds
	 * 统计的数据范围
	 * @return
	 * @author brightming
	 * 2014-1-19 下午1:12:29
	 */
	public List<Map<String,Object>> queryAreaDamageFactor(List<Long> ncsIds);

	/**
	 * 计算区域归一化干扰水平
	 * @param ncsIds
	 * 统计的数据范围
	 * @return
	 * @author brightming
	 * 2014-1-19 下午1:12:05
	 */
	public List<Map<String,Object>> queryAreaNormalizeFactor(List<Long> ncsIds);
	/**
	 * 
	 * @title 查询连通簇内的小区信息并输出彼此区间干扰值
	 * @param clusterId
	 * @param cell
	 * @param ncell
	 * @return
	 * @author chao.xj
	 * @date 2014-1-20下午12:25:21
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getNcsClusterCellAndOutputEachOtherInterValue(final long clusterId,List<Map<String, Object>> clusterCells);

	/**
	 * 搜索包含指定小区的ncs列表
	 * @param cell
	 * @return
	 * @author brightming
	 * 2014-2-11 下午1:52:42
	 */
	public List<Map<String, Object>> searchNcsContainCell(String cell, long cityId, String manufacturers);

	/**
	 * 获取指定小区在指定 ncs里的测量信息
	 * @param ncsId
	 * @param cell
	 * @return
	 * @author brightming
	 * 2014-2-11 下午2:15:13
	 */
	public List<Map<String, Object>> getCellNcsInfo(long ncsId, String cell, long cityId, String manufacturers);
	
	
	/**
	 * 获取单个ncs的结构指标数据
	 * @param ncsId
	 * @return
	 * @author brightming
	 * 2014-2-14 下午3:33:42
	 */
	public List<Map<String,Object>> getSingleNcsStructAnaRes(long ncsId);
	
	/**
	 * 获取单个ncs的cluster信息
	 * 包括：
	 * ClusterID（簇ID）	Count（小区数）	Trxs（频点数/载波数）	簇约束因子	簇权重	Sectors（小区列表）
	 * @param ncsId
	 * @return
	 * @author brightming
	 * 2014-2-14 下午6:03:35
	 */
	public List<Map<String,Object>> getSingleNcsClusterListInfo(long ncsId);
	
	/**
	 * 获取单个ncs的簇内小区信息
	 * 包括：
	 * 簇ID	小区名	小区中文名	小区载频	簇TCH载频数
	 * @param ncsId
	 * @return
	 * @author brightming
	 * 2014-2-14 下午6:04:33
	 */
	public List<Map<String,Object>> getSingleNcsClusterCellListInfo(long ncsId);
	
	/**
	 * 获取簇内小区的关联信息
	 * 包括：
	 * 主小区	小区层	簇编号	簇内小区载波数	干扰小区
	 * @param ncsId
	 * @return
	 * @author brightming
	 * 2014-2-14 下午6:05:21
	 */
	public List<Map<String,Object>> getSingleNcsClusterCellRelationInfo(long ncsId);

	/**
	 * 查询满足条件的ncs汇总任务数量
	 * @param account
	 * @param condition
	 * @return
	 * @author brightming
	 * 2014-2-18 下午3:40:06
	 */
	public long getNcsTaskCount(String account, Map<String, String> condition);

	/**
	 * 分页获取满足条件的ncs汇总任务
	 * @param account
	 * @param condition
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author brightming
	 * 2014-2-18 下午3:40:32
	 */
	public List<Map<String, Object>> queryNcsTaskByPage(String account,
			Map<String, String> condition, int startIndex, int cnt);
	
	/**
	 * 删除指定的ncs数据
	 * 
	 * @param ncsId
	 * @param isPhysicalDeletion
	 *            是否物理删除 true：从数据库中真正删除数据 false：只是标识删除
	 * @return
	 * @author brightming 2014-2-27 下午5:43:58
	 */
	public boolean deleteNcsDataById(final long ncsId,
			boolean isPhysicalDeletion);
	
	/**
	 * 获取ncs的描述信息
	 * 
	 * @param ncsId
	 * @return
	 * @author brightming 2014-3-7 上午11:34:13
	 */
	public List<Map<String, Object>> getNcsDescriptorDataById(long ncsId);
	/**
	 * 按逗号分隔的ncs id标记删除ncs记录
	 * @title 
	 * @param ncsId
	 * @param isPhysicalDeletion
	 * 是否物理删除 true：从数据库中真正删除数据 false：只是标识删除
	 * @return
	 * @author chao.xj
	 * @date 2014-3-27上午11:41:15
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean deleteNcsSelectedDataById(final String ncsIds,
			boolean isPhysicalDeletion);

	/**
	 * 更新指定的ncs desc为指定的状态
	 * @param ncsId
	 * @param string
	 * @return
	 * @author brightming
	 * 2014-5-21 下午4:46:16
	 */
	public boolean updateNcsDescStatus(long ncsId, String string);
	
	/**
	 * 查询指定ncs desc的状态
	 * @param ids
	 * @return
	 * @author brightming
	 * 2014-5-21 下午5:22:49
	 */
	public List<Map<String,Object>> queryNcsDescStatus(final String ids);

	/**
	 * 通过任务id获取已存在的ncs分析任务
	 * @author peng.jm 
	 * 2014年6月23日17:48:21
	 */
	/*public List<Map<String, Object>> queryOldNcsTaskByTaskId(long taskId);*/

	/**
	 * 通过NcsIds获取已存在的ncs分析任务
	 * @author peng.jm 
	 */
	public List<Map<String, Object>> queryOldNcsTaskByNcsIds(String ncsIds);
	/**
	 * 
	 * @title 通过ncs或mrr的以逗号分割的id字符串查询其最早测量时间与最后测量时间
	 * @param ids
	 * @param ncsOrMrrFlag
	 * @return
	 * @author chao.xj
	 * @date 2014-7-16上午11:35:19
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryNcsOrMrrTimeSpanByIds(String ids,String ncsOrMrrFlag);
	/**
	 * 
	 * @title 计算符合某条件的mrr的数量
	 * @param condition
	 * @return
	 * @author chao.xj
	 * @date 2014-7-23上午9:36:28
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long getMrrDescriptorCount(Map<String, String> condition);
	/**
	 * 
	 * @title 分页查询mrr描述信息
	 * @param condition
	 * @param startIndex 从0开始
	 * @param cnt
	 * @return
	 * @author chao.xj
	 * @date 2014-7-23上午9:45:17
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryMrrDescriptorByPage(
			Map<String, String> condition, long startIndex, long cnt);
	/**
	 * 
	 * @title 删除指定的mrr数据
	 * @param mrrId
	 * @return
	 * @author chao.xj
	 * @date 2014-7-23上午10:29:35
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean deleteMrrDataById(final long mrrId);

	/**
	 * 通过mrrId获取mrr管理总数
	 * @param mrrId
	 * @return
	 * @author peng.jm
	 * @date 2014年7月24日15:10:24
	 */
	public long getMrrAdmCount(long mrrId);

	/**
	 * 通过mrrId分页获取mrr管理信息
	 * @param mrrId
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:21:35
	 */
	public List<Map<String, Object>> queryMrrAdmByPage(long mrrId,
			int startIndex, int cnt);

	/**
	 * 通过mrrId获取mrr信号强度总数
	 * @param mrrId
	 * @return
	 * @author peng.jm
	 * @date 2014年7月24日15:10:24
	 */
	public long getMrrStrenCount(long mrrId);
	
	/**
	 * 通过mrrId分页获取mrr信号强度信息
	 * @param mrrId
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:21:35
	 */
	public List<Map<String, Object>> queryMrrStrenByPage(long mrrId,
			int startIndex, int cnt);
	/**
	 * 通过mrrId获取mrr信号质量总数
	 * @param mrrId
	 * @return
	 * @author peng.jm
	 * @date 2014年7月24日15:10:24
	 */
	public long getMrrQualiCount(long mrrId);
	/**
	 * 通过mrrId分页获取mrr信号质量信息
	 * @param mrrId
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:21:35
	 */
	public List<Map<String, Object>> queryMrrQualiByPage(long mrrId,
			int startIndex, int cnt);
	/**
	 * 通过mrrId获取mrr传输功率信息总数
	 * @param mrrId
	 * @return
	 * @author peng.jm
	 * @date 2014年7月24日15:10:24
	 */
	public long getMrrPowerCount(long mrrId);
	/**
	 * 通过mrrId分页获取mrr传输功率信息
	 * @param mrrId
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:21:35
	 */
	public List<Map<String, Object>> queryMrrPowerByPage(long mrrId,
			int startIndex, int cnt);
	/**
	 * 通过mrrId获取mrr实时预警信息总数
	 * @param mrrId
	 * @return
	 * @author peng.jm
	 * @date 2014年7月24日15:10:24
	 */
	public long getMrrTaCount(long mrrId);
	/**
	 * 通过mrrId分页获取mrr实时预警信息
	 * @param mrrId
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:21:35
	 */
	public List<Map<String, Object>> queryMrrTaByPage(long mrrId,
			int startIndex, int cnt);
	/**
	 * 通过mrrId获取mrr路径损耗信息总数
	 * @param mrrId
	 * @return
	 * @author peng.jm
	 * @date 2014年7月24日15:10:24
	 */
	public long getMrrPlCount(long mrrId);
	/**
	 * 通过mrrId分页获取mrr路径损耗信息
	 * @param mrrId
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:21:35
	 */
	public List<Map<String, Object>> queryMrrPlByPage(long mrrId,
			int startIndex, int cnt);
	/**
	 * 通过mrrId获取mrr路径损耗差异信息总数
	 * @param mrrId
	 * @return
	 * @author peng.jm
	 * @date 2014年7月24日15:10:24
	 */
	public long getMrrPldCount(long mrrId);
	/**
	 * 通过mrrId分页获取mrr路径损耗差异信息
	 * @param mrrId
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:21:35
	 */
	public List<Map<String, Object>> queryMrrPldByPage(long mrrId,
			int startIndex, int cnt);
	/**
	 * 通过mrrId获取mrr测量结果信息总数
	 * @param mrrId
	 * @return
	 * @author peng.jm
	 * @date 2014年7月24日15:10:24
	 */
	public long getMrrMeaCount(long mrrId);
	/**
	 * 通过mrrId分页获取mrr测量结果信息
	 * @param mrrId
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:21:35
	 */
	public List<Map<String, Object>> queryMrrMeaByPage(long mrrId,
			int startIndex, int cnt);
	/**
	 * 通过mrrId获取mrr的上下行FER信息总数
	 * @param mrrId
	 * @return
	 * @author peng.jm
	 * @date 2014年7月24日15:10:24
	 */
	public long getMrrFerCount(long mrrId);
	/**
	 * 通过mrrId分页获取mrr的上下行FER信息
	 * @param mrrId
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:21:35
	 */
	public List<Map<String, Object>> queryMrrFerByPage(long mrrId,
			int startIndex, int cnt);

	/**
	 * 通过区域id获取区域的经纬度范围
	 * @param areaId
	 * @return 
	 * @author peng.jm
	 * @date 2014-7-29上午10:48:33
	 */
	public Map<String, Object> getAreaRangeByAreaId(String areaId);
		/**
	 * 获取符合条件的干扰矩阵数量
	 * @param condition
	 * @return
	 * @author peng.jm
	 * @date 2014-8-15下午04:33:46
	 */
	public long getInterferMartixCount(Map<String, String> condition);

	/**
	 * 分页获取符合条件的干扰矩阵的详情
	 * @param condition
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2014-8-15下午04:34:06
	 */
	public List<Map<String, Object>> queryInterferMartixByPage(
			Map<String, String> condition, int startIndex, int cnt);

	/**
	 * 检查这周是否计算过NCS干扰矩阵
	 * @author peng.jm
	 * @date 2014-8-16上午11:01:47
	 */
	public List<Map<String, Object>> checkInterMartixThisWeek(long areaId,
			String thisMonday, String today);

	/**
	 * 获取符合条件的ncs文件数量
	 * @param condition
	 * @return
	 * @author peng.jm
	 * @date 2014-8-16下午02:17:20
	 */
	public long getNcsDataCount(Map<String, String> condition);

	/**
	 * 分页获取符合条件的ncs记录
	 * @param condition
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2014-8-16下午02:17:40
	 */
	public List<Map<String, Object>> queryNcsDataByPageAndCond(
			Map<String, String> condition, int startIndex, int cnt);

	/**
	 * 根据条件获取NCS的数据记录总数量
	 * @param cityId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author peng.jm
	 * @date 2014-8-16下午05:57:59
	 */
	public long queryNcsDataRecordsCount(long cityId, String startTime,
			String endTime);

	/**
	 * 获取seq的值
	 * @param seq  seq名称
	 * @return
	 * @author peng.jm
	 * @date 2014-8-16下午06:28:31
	 */
	public long getNextSeqValue(String seq);
	/**
	 * 检查是否存在正在计算的干扰矩阵
	 * @param areaId
	 * @param thisMonday
	 * @param today
	 * @return
	 * @author peng.jm
	 * @date 2014-8-19下午03:57:13
	 */
	public boolean isCalculatingInterMartixThisArea(long areaId, String thisMonday, String today);
	/**
	 * 创建NCS干扰矩阵计算任务
	 * @param interMartix
	 * @return
	 * @author peng.jm
	 * @date 2014-8-16下午06:38:05
	 */
	public boolean createNcsInterMartixRec(Map<String, Object> interMartix);
	/**
	 * 更新干扰矩阵的记录状态
	 * @param martixRecId
	 * @param workStatus
	 * @author peng.jm
	 * @date 2014-8-18下午02:57:10
	 */
	public boolean updateInterMartixWorkStatus(long martixRecId, String workStatus);
	/***
	 * 获取爱立信数据的BSC个数
	 * @param date
	 * @return
	 * @author peng.jm
	 * @date 2014-9-2下午03:42:33
	 */
	public List<Map<String, Object>> getEriDataBscNumByDate(long cityId, String startTime, String endTime);

	/**
	 * 获取爱立信Ncs的文件数量
	 * @param date
	 * @return
	 * @author peng.jm
	 * @date 2014-9-2下午03:42:38
	 */
	public List<Map<String, Object>> getEriNcsFileNumByDate(long cityId, String startTime, String endTime);

	/**
	 * 获取爱立信Mrr的文件数量
	 * @param date
	 * @return
	 * @author peng.jm
	 * @date 2014-9-2下午03:42:41
	 */
	public List<Map<String, Object>> getEriMrrFileNumByDate(long cityId, String startTime, String endTime);
	/***
	 * 获取华为数据的BSC个数
	 * @param date
	 * @return
	 * @author peng.jm
	 * @date 2014-9-2下午03:42:33
	 */
	public List<Map<String, Object>> getHWDataBscNumByDate(long cityId, String startTime, String endTime);
	/**
	 * 通过城市id和日期范围查询爱立信用于结构分析的数据详情
	 * @param cityId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author peng.jm
	 * @date 2014-8-26下午02:52:34
	 */
	public List<Map<String, Object>> getEriDataDetailsByCityIdAndDate(
			long cityId, String startTime, String endTime);
	/**
	 * 通过城市id和日期范围查询华为用于结构分析的数据详情
	 * @param cityId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author peng.jm
	 * @date 2014-8-26下午02:52:34
	 */
	public List<Map<String, Object>> getHWDataDetailsByCityIdAndDate(
			long cityId, String startTime, String endTime);
	/**
	 * 保存对应jobId的结构分析任务信息
	 * @param jobId
	 * @param dlFileName
	 * @param rdFileName
	 * @param resultDir
	 * @param finishState
	 * @param createTime
	 * @param modTime
	 * @param taskInfo
	 * @return
	 * @author peng.jm
	 * @date 2014-8-26下午05:26:13
	 */
	public boolean saveStructureAnalysisTask(long jobId, String dlFileName,
			String rdFileName, String resultDir, String finishState,
			String createTime, String modTime, TaskInfo taskInfo);

	/**
	 * 保存对应jobId的门限值信息
	 * @param jobId
	 * @param paramType
	 * @param paramCode
	 * @param paramVal
	 * @return
	 * @author peng.jm
	 * @date 2014-8-26下午05:26:38
	 */
	public boolean saveThresholdWithJobId(long jobId, String paramType, String paramCode, String paramVal);
	/**
	 * 获取结构分析任务的总数
	 * @param condition
	 * @param account
	 * @return
	 * @author peng.jm
	 * @date 2014-8-26下午06:51:38
	 */
	public long getStructureAnalysisTaskCount(Map<String, String> condition, String account);
	/**
	 * 分页获取结构分析任务的信息
	 * @param condition
	 * @param account
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2014-8-26下午06:51:59
	 */
	public List<Map<String, Object>> queryStructureAnalysisTaskByPage(
			Map<String, String> condition, String account, int startIndex, int cnt);
	/**
	 * 通过jobId查询对应的结果分析信息
	 * @param jobId
	 * @return
	 * @author peng.jm
	 * @date 2014-8-27下午06:19:50
	 */
	public List<Map<String, Object>> getStructureTaskByJobId(long jobId);
	/**
	 * 
	 * @title 通过模块类型获取阈值门限对象
	 * @param modType
	 * @return
	 * @author chao.xj
	 * @date 2014-8-15下午4:41:48
	 * @company 怡创科技
	 * @version 1.2
	 */
//	public Threshold getThresholdByModType(String modType);
	/**
	 * 
	 * @title 通过城市ID获取该区域下的系统小区数量
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2014-8-16上午10:37:52
	 * @company 怡创科技
	 * @version 1.2
	 */
//	public int getAreaCellNumByCityId(long cityId);
	/**
	 * 
	 * @title 获取结构分析汇总信息
	 * @param cityId
	 * @param meaDate
	 * @param dateType HW 华为  ERI 爱立信
	 * @return
	 * @author chao.xj
	 * @date 2014-8-16下午2:31:52
	 * @company 怡创科技
	 * @version 1.2
	 */
	public Map<String, Object> getStructAnaSummaryInfoForOneDay(long cityId,String meaDate,String dateType);
	/**
	 * 
	 * @title 获取结构分析汇总信息为某时间段
	 * @param cityId
	 * @param startDate
	 * @param endDate
	 * @param dateType
	 * @return
	 * @author chao.xj
	 * @date 2014-8-16下午5:28:53
	 * @company 怡创科技
	 * @version 1.2
	 */
	public Map<String, Object> getStructAnaSummaryInfoForTimeRange(long cityId,String startDate,String endDate,String dateType);
	/**
	 * 
	 * @title 通过模块类型获取阈值门限对象集合
	 * @param moduleType
	 * @return
	 * @author chao.xj
	 * @date 2014-8-28上午10:52:09
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<RnoThreshold> getThresholdsByModuleType(String moduleType);

	/**
	 * 判断在指定的bsc和日期中是否存在ncs数据
	 * @param cell
	 * @param ncsId
	 * @param cityId
	 * @param manuf
	 * @return
	 * @author peng.jm
	 * @date 2014-12-2上午11:06:29
	 */
	public boolean isNcsDataExistedByDateAndBsc(String cell, String ncsId,
			long cityId, String manuf);
	/**
	 * 
	 * @title 获取指定小区在指定 ncs里的测量信息
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-12-8下午5:06:02
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getCellNcsInfo(G2NcsQueryCond cond);
	/**
	 * 
	 * @title 搜索包含指定小区的ncs 的时间列表 hbase
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-12-9下午6:06:54
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> searchNcsContainCell(G2NcsQueryCond cond);
	/**
	 * 
	 * @title ncs指标查看中，获取某小区的邻区信息
	 * @param cell
	 * @return
	 * @author chao.xj
	 * @date 2015-3-10上午9:39:27
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> getNcellInfoByCell(
			final String cell);
	/**
	 * 
	 * @title 通过区域及起始时间和厂家,及数据类型从Hbase获取MR数据描述记录情况
	 * @param cityId
	 * @param startTime
	 * @param endTime
	 * @param factory
	 * ERI,ZTE
	 * @param dataType
	 * HO,MR
	 * @return
	 * @author chao.xj
	 * @date 2015-3-27下午4:33:02
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> getDataDescRecordFromHbase(long cityId,
			String startTime, String endTime, String factory,String dataType);
	/**
	 * 
	 * @title 通过jobId获取pci规划记录任务信息
	 * @param stmt
	 * @param jobId
	 * @return
	 * @author chao.xj
	 * @date 2015-3-30上午10:14:31
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> queryPciPlanJobRecByJobId(Statement stmt, long jobId);

	/**
	 * 统计pci自动规划任务数量
	 * 
	 * @author peng.jm
	 * @date 2015年3月31日15:23:10
	 */
	public long getPciAnalysisTaskCount(Map<String, String> cond, String account);

	/**
	 * 分页获取pci自动规划任务信息
	 * 
	 * @author peng.jm
	 * @date 2015年3月31日15:23:10
	 */
	public List<Map<String, Object>> queryPciPlanTaskByPage(
			Map<String, String> cond, String account, int startIndex, int cnt);

	/**
	 * 
	 * @title 通过statement更新PCI规划的job状态
	 * @param stmt
	 * @param jobId
	 * @param workStatus
	 * @return
	 * @author chao.xj
	 * @date 2015-3-30上午10:40:40
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public boolean updatePciPlanWorkStatusByStmt(Statement stmt, long jobId, String workStatus);
	/**
	 * 
	 * @title 通过城市ID获取从基站标识到lte小区的映射集合
	 * @param stmt
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2015-3-31下午3:23:10
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public Map<String, String> getEnodebIdForCellsMap(Statement stmt,long cityId);
	/**
	 * 
	 * @title 获取lte MR测量数据的结果集
	 * @param tableName
	 * @param startRow
	 * @param stopRow
	 * @return
	 * @author chao.xj
	 * @date 2015-3-31下午4:01:13
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public Result[] getMrResults(String tableName,String startRow,String stopRow);
	/**
	 * 
	 * @title 获取lte HO切换数据的结果集
	 * @param tableName
	 * @param startRow
	 * @param stopRow
	 * @param factory
	 * ZTE,ERI
	 * @return
	 * @author chao.xj
	 * @date 2015-3-31下午4:00:27
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public Result[] getHoResults(String tableName,String startRow,String stopRow,String factory);
	/**
	 * 
	 * @title MR子关联度
	 * @param results
	 * @return
	 * @author chao.xj
	 * @date 2015-3-31上午10:42:43
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public  Map<Integer, List<G4PciRec>> getMrRelaDegree(Result[] results,String numerator);
	/**
	 * 
	 * @title HO子关联度
	 * @param results
	 * @param factory
	 * ZTE,ERI
	 * @return
	 * @author chao.xj
	 * @date 2015-3-31下午2:12:58
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public  Map<Integer, List<G4PciRec>> getHoRelaDegree(Result[] results,String factory);
	/**
	 * 
	 * @title 添加mapreduce的jobid
	 * @param stmt
	 * @param jobId
	 * @param mrJobId
	 * @author chao.xj
	 * @date 2015-3-31下午5:05:17
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void addMapReduceJobId(Statement stmt,long jobId,String mrJobId);
	/**
	 * 
	 * @title 保存原始PCI文件到hdfs文件系统上
	 * @param filePath
	 * @param sumMrRelaDegre
	 * @author chao.xj
	 * @date 2015-3-31下午7:36:37
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void saveOriPciInHdfs(String filePath, Map<Integer, List<G4PciRec>> sumMrRelaDegre);
	/**
	 * 
	 * @title 通过区域ID获取LTE小区标识对应的小区信息
	 * @param stmt
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2015-4-9下午2:34:39
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public Map<String, List<String>> getLteCellInfoByCellId(Statement stmt,long cityId);
}