package com.iscreate.op.dao.rno;

import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.Result;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.G4MrDescQueryCond;
import com.iscreate.op.service.rno.parser.vo.G4PciRec;

public interface Rno4GPciDao {

	/**
	 * 
	 * @title 获取符合条件的4g干扰矩阵数量
	 * @param condition
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15上午10:37:05
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public long get4GInterferMartixCount(Map<String, String> condition);
	/**
	 * 
	 * @title 分页获取符合条件的4g干扰矩阵的详情
	 * @param condition
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15上午10:34:05
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> query4GInterferMartixByPage(
			Map<String, String> condition, int startIndex, int cnt);
	/**
	 * 
	 * @title 分页获取Hbase的Mr数据描述表的数据
	 * @param cond
	 * @param newPage
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15上午11:31:59
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, String>> queryMrDataFromHbaseByPage(
			G4MrDescQueryCond cond, Page newPage);
	/**
	 * 
	 * @title 检查这周是否计算过4G MR干扰矩阵
	 * @param cityId
	 * @param thisMonday
	 * @param today
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15下午1:57:05
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> check4GInterMartixThisWeek(long cityId,
			String thisMonday, String today);
	/**
	 * 
	 * @title 获取seq的val值
	 * @param seq
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15下午2:32:50
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public long getNextSeqValue(String seq);
	/**
	 * 
	 * @title 根据条件获取MR的数据记录总量
	 * @param cityId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15下午2:48:15
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public long queryMrDataRecordsCount(long cityId, String startTime,
			String endTime);
	/**
	 * 
	 * @title 创建MR 4g 干扰矩阵计算任务
	 * @param interMartix
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15下午2:52:26
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public boolean createMr4GInterMartixRec(Map<String, Object> interMartix);
	/**
	 * 
	 * @title 获取符合条件的mr文件数量
	 * @param condition
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15下午3:03:15
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public long getMrDataCount(Map<String, String> condition);
	/**
	 * 
	 * @title 通过statement更新4g干扰矩阵的记录状态
	 * @param stmt
	 * @param martixRecId
	 * @param workStatus
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15下午4:19:27
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public boolean update4GInterMatrixWorkStatusByStmt(Statement stmt, long martixRecId, String workStatus) ;
	/**
	 * 
	 * @title 通过jobId获取4g 干扰矩阵的记录任务信息
	 * @param stmt
	 * @param jobId
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15下午4:20:32
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> query4GInterferMatrixRecByJobId(Statement stmt, long jobId) ;
	/**
	 * 
	 * @title 获取lte MR测量数据的结果集
	 * @param tableName
	 * @param startRow
	 * @param stopRow
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15下午5:12:04
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public Result[] getMrResults(String tableName,String startRow,String stopRow);
	/**
	 * 
	 * @title MR子关联度
	 * @param results
	 * @param numerator
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15下午5:24:53
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public  Map<Integer, List<G4PciRec>> getMrRelaDegree(Result[] results,String numerator);
	/**
	 * 
	 * @title 保存原始pci优化 4G 干扰矩阵文件到hdfs文件系统上
	 * @param filePath
	 * @param sumMrRelaDegre
	 * @author chao.xj
	 * @date 2015-4-15下午5:32:07
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void save4GInterferMatrixInHdfs(String filePath, Map<Integer, List<G4PciRec>> sumMrRelaDegre);
	/**
	 * 获取最近十次lte干扰矩阵信息
	 * @title 
	 * @return
	 * @author peng.jm
	 * @date 2015年4月13日18:32:31
	 * @company 怡创科技
	 */
	public List<Map<String, Object>> getLatelyLteMatrixByCityId(long cityId);
	/**
	 * 通过job_id获取干扰矩阵
	 * @title 
	 * @return
	 * @author peng.jm
	 * @date 2015年4月13日18:32:31
	 * @company 怡创科技
	 */
	public List<Map<String, Object>> getLteMatrixById(long jobId);
	/**
	 * 获取同站lte小区和pci
	 * @title 
	 * @return
	 * @author peng.jm
	 * @date 2015年4月13日18:32:31
	 * @company 怡创科技
	 */
	public List<Map<String, String>> getSameStationCellsByLteCellId(
			String lteCell);
	/**
	 * 转换lte小区与某同站小区的pci
	 * @title 
	 * @return
	 * @author peng.jm
	 * @date 2015年4月13日18:32:31
	 * @company 怡创科技
	 */
	public boolean changeLteCellPci(String cell1, String pci1, String cell2,
			String pci2);
	/**
	 * 
	 * @title 统计4g方位角任务数量
	 * @param cond
	 * @param account
	 * @return
	 * @author chao.xj
	 * @date 2015-4-29下午3:32:04
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public long get4GAzimuthCalcTaskCount(Map<String, String> cond, String account);
	/**
	 * 
	 * @title 分页获取4g方位角计算任务信息
	 * @param cond
	 * @param account
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author chao.xj
	 * @date 2015-4-29下午3:36:43
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> query4GAzimuthCalcTaskByPage(
			Map<String, String> cond, String account, int startIndex, int cnt);
	/**
	 * 
	 * @title 通过区域ID获取LTE小区标识对应的小区信息
	 * @param stmt
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2015-4-29下午3:36:43
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public Map<String, List<String>> getLteCellInfoByCellId(Statement stmt,long cityId);
	/**
	 * 
	 * @title 通过jobId获取4g方位角记录任务信息
	 * @param stmt
	 * @param jobId
	 * @return
	 * @author chao.xj
	 * @date 2015-4-29下午4:44:12
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> query4GAzimuthJobRecByJobId(Statement stmt, long jobId);
	/**
	 * 
	 * @title 通过jobid更新4g方位角计算的job状态
	 * @param stmt
	 * @param jobId
	 * @param workStatus
	 * @return
	 * @author chao.xj
	 * @date 2015-4-29下午5:06:29
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public boolean update4GAzimuthCalcWorkStatusByStmt(Statement stmt, long jobId, String workStatus);
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
	 * @title 获取某个地市 的小区方位角信息
	 * @param stmt
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2015-5-11下午1:55:35
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public Map<String, String> queryCellAzimuthByCityId(
			Statement stmt, long cityId);
	/**
	 * 
	 * @title 获取某个地市 的小区频段类型信息
	 * @param stmt
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2015-5-11下午1:55:35
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public Map<String, String> queryCellBandTypeByCityId(
			Statement stmt, long cityId);
}
