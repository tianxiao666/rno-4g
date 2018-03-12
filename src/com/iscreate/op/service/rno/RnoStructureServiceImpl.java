package com.iscreate.op.service.rno;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import net.rubyeye.xmemcached.MemcachedClient;

import oracle.jdbc.OracleConnection;
import oracle.sql.CLOB;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.jdbc.support.nativejdbc.C3P0NativeJdbcExtractor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.G2NcsQueryCond;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.rno.RnoStructureAnalysisDao;
import com.iscreate.op.dao.rno.RnoStructureAnalysisDaoImpl.CellInterWithDetailInfo;
import com.iscreate.op.dao.rno.RnoStructureQueryDao;
import com.iscreate.op.dao.rno.RnoTaskDao;
import com.iscreate.op.pojo.rno.CellFreqInterferList;
import com.iscreate.op.pojo.rno.GridStorageCells;
import com.iscreate.op.pojo.rno.NcsCellQueryResult;
import com.iscreate.op.pojo.rno.RenderCellInfo;
import com.iscreate.op.pojo.rno.RenderColor;
import com.iscreate.op.pojo.rno.RnoNcsCell;
import com.iscreate.op.pojo.rno.RnoStructureAnalysisTask.MrrInfo;
import com.iscreate.op.pojo.rno.RnoStructureAnalysisTask.NcsInfo;
import com.iscreate.op.pojo.rno.RnoStructureAnalysisTask.TaskInfo;
import com.iscreate.op.pojo.rno.RnoTask;
import com.iscreate.op.pojo.rno.RnoThreshold;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.client.api.JobClient;
import com.iscreate.op.service.rno.job.client.api.JobClientDelegate;
import com.iscreate.op.service.rno.job.server.JobAddCallback;
import com.iscreate.op.service.rno.task.ExecutorManager;
import com.iscreate.op.service.rno.task.RnoRenderer;
import com.iscreate.op.service.rno.task.RnoTaskWorker;
import com.iscreate.op.service.rno.task.TaskStatus;
import com.iscreate.op.service.rno.tool.CoordinateHelper;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.FileTool;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.iscreate.plat.system.datasourcectl.DataSourceConst;
import com.iscreate.plat.system.datasourcectl.DataSourceContextHolder;

/**
 * @author brightming
 * @version 1.0
 * @created 17-һ��-2014 11:02:57
 */
public class RnoStructureServiceImpl implements RnoStructureService {

	private static Log log = LogFactory.getLog(RnoStructureServiceImpl.class);

	static Object lockObj = new Object();// 锁对象

	// ---注入----//  
	private RnoStructureQueryDao rnoStructureQueryDao;
	private RnoStructureAnalysisDao rnoStructureAnalysisDao;
	private RnoTaskWorker rnoNcsGatherAnalysisWorker;
	private RnoTaskDao rnoTaskDao;
	private MemcachedClient memCached;

	private RnoRenderer rnoRenderer;

	// 临时常量
	public final static int afterShrinkLen = 800;

	public RnoTaskDao getRnoTaskDao() {
		return rnoTaskDao;
	}

	public void setRnoStructureQueryDao(
			RnoStructureQueryDao rnoStructureQueryDao) {
		this.rnoStructureQueryDao = rnoStructureQueryDao;
	}

	public void setRnoStructureAnalysisDao(
			RnoStructureAnalysisDao rnoStructureAnalysisDao) {
		this.rnoStructureAnalysisDao = rnoStructureAnalysisDao;
	}

	public void setRnoNcsGatherAnalysisWorker(
			RnoTaskWorker rnoNcsGatherAnalysisWorker) {
		this.rnoNcsGatherAnalysisWorker = rnoNcsGatherAnalysisWorker;
	}

	public void setRnoTaskDao(RnoTaskDao rnoTaskDao) {
		this.rnoTaskDao = rnoTaskDao;
	}

	public void setMemCached(MemcachedClient memCached) {
		this.memCached = memCached;
	}

	public RnoStructureServiceImpl() {

	}

	public void finalize() throws Throwable {

	}

	public RnoRenderer getRnoRenderer() {
		return rnoRenderer;
	}

	public void setRnoRenderer(RnoRenderer rnoRenderer) {
		this.rnoRenderer = rnoRenderer;
	}

	/**
	 * 
	 * @param condition
	 * @param page
	 */
	public List<Map<String, Object>> queryNcsDescriptorByPage(
			Map<String, String> condition, Page page) {

		log.info("进入方法：queryNcsDescriptorByPage。condition=" + condition
				+ ",page=" + page);
		if (page == null) {
			log.warn("方法：queryNcsDescriptorByPage的参数：page 是空！无法查询!");
			return Collections.EMPTY_LIST;
		}
		long totalCnt = page.getTotalCnt();
		if (totalCnt < 0) {
			totalCnt = rnoStructureQueryDao.getNcsDescriptorCount(condition);
			page.setTotalCnt((int) totalCnt);
		}
		int startIndex = page.calculateStart();
		int cnt = page.getPageSize();
		List<Map<String, Object>> res = rnoStructureQueryDao
				.queryNcsDescriptorByPage(condition, startIndex, cnt);
		return res;
	}

	/**
	 * 
	 * @title 分页查询ncs cell信息
	 * @param ncsId
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2014-1-17下午02:43:54
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryNcsCellDataByPage(long ncsId,
			Page page) {
		log.info("进入queryNcsCellDataByPage(long ncsId," + ncsId + " Page page)"
				+ page);
		if (page == null) {
			log.warn("传入的分页参数为空！");
			return Collections.EMPTY_LIST;
		}
		if (page.getPageSize() <= 0) {
			page.setPageSize(25);
		}
		if (page.getCurrentPage() <= 0) {
			page.setCurrentPage(1);
		}
		final int totalCnt = page.getTotalCnt();
		if (totalCnt <= 0) {
			long count = rnoStructureQueryDao.getNcsCellCount(ncsId);
			log.info("NcsCellCount:" + count);
			page.setTotalCnt((int) count);
		}
		// final int startIndex = (page.getCurrentPage() - 1) *
		// page.getPageSize()+ 1;
		final int startIndex = page.calculateStart();
		// final int endIndex = startIndex + page.getPageSize();
		final int cnt = page.getPageSize();// 页面大小
		List<Map<String, Object>> ncscelldataList = rnoStructureQueryDao
				.queryNcsCellDataByPage(ncsId, startIndex, cnt);
		log.info("退出queryNcsCellDataByPagee ncscelldataList:" + ncscelldataList);
		return ncscelldataList;
	}

	/**
	 * 
	 * @title 分页查询ncs ncell信息
	 * @param ncsId
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2014-1-17下午03:10:51
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryNcsNcellDataByPage(long ncsId,
			Page page) {
		log.info("进入queryNcsNcellDataByPage(long ncsId," + ncsId
				+ " Page page)" + page);
		if (page == null) {
			log.warn("传入的分页参数为空！");
			return Collections.EMPTY_LIST;
		}
		final int totalCnt = page.getTotalCnt();
		if (totalCnt <= 0) {
			long count = rnoStructureQueryDao.getNcsNcellDataCount(ncsId);
			page.setTotalCnt((int) count);
		}
		// final int startIndex = (page.getCurrentPage() - 1) *
		// page.getPageSize()+ 1;
		final int startIndex = page.calculateStart();
		// final int endIndex = startIndex + page.getPageSize();
		final int cnt = page.getPageSize();// 页面大小
		List<Map<String, Object>> ncsncelldataList = rnoStructureQueryDao
				.queryNcsNcellDataByPage(ncsId, startIndex, cnt);
		log.info("退出queryNcsCellDataByPagee ncsncelldataList:"
				+ ncsncelldataList);
		return ncsncelldataList;
	}

	/**
	 * 
	 * @param ncsId
	 * @param page
	 */
	public List<Map<String, Object>> queryNcsClusterByPage(long ncsId, Page page) {
		log.info("进入queryNcsClusterByPage(long ncsId," + ncsId + " Page page)"
				+ page);
		if (page == null) {
			log.warn("传入的分页参数为空！");
			return Collections.EMPTY_LIST;
		}
		final int totalCnt = page.getTotalCnt();
		if (totalCnt <= 0) {
			long count = rnoStructureQueryDao.getNcsClusterCount(ncsId);
			page.setTotalCnt((int) count);
		}
		final int startIndex = page.calculateStart();
		final int cnt = page.getPageSize();// 页面大小
		List<Map<String, Object>> ncsncelldataList = rnoStructureQueryDao
				.getNcsClusterByPage(ncsId, startIndex, cnt);
		log.info("退出queryNcsClusterByPage res:" + ncsncelldataList);
		return ncsncelldataList;
	}

	/**
	 * 
	 * @param clusterId
	 */
	public List<Map<String, Object>> queryNcsClusterCell(long clusterId) {
		log.info("进入方法：queryNcsClusterCell.clusterId=" + clusterId);
		List<Map<String, Object>> clusterCells = rnoStructureQueryDao
				.getNcsClusterCell(clusterId);
		log.info("退出queryNcsClusterCell res:" + clusterCells);
		return clusterCells;

	}

	/**
	 * 
	 * @title 分页查询ncs 干扰矩阵信息
	 * @param ncsId
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2014-1-17下午03:12:23
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryNcsInterferMatrixByPage(long ncsId,
			Page page) {
		log.info("进入queryNcsInterferMatrixByPage(long ncsId," + ncsId
				+ " Page page)" + page);
		if (page == null) {
			log.warn("传入的分页参数为空！");
			return Collections.EMPTY_LIST;
		}
		final int totalCnt = page.getTotalCnt();
		if (totalCnt <= 0) {
			long count = rnoStructureQueryDao.getNcsInterferMatrixCount(ncsId);
			page.setTotalCnt((int) count);
		}
		// final int startIndex = (page.getCurrentPage() - 1) *
		// page.getPageSize()+ 1;
		final int startIndex = page.calculateStart();
		// final int endIndex = startIndex + page.getPageSize();
		final int cnt = page.getPageSize();// 页面大小
		List<Map<String, Object>> ncsmatrixdataList = rnoStructureQueryDao
				.queryNcsInterferMatrixByPage(ncsId, startIndex, cnt);
		log.info("退出queryNcsCellDataByPagee ncsncelldataList:"
				+ ncsmatrixdataList);
		return ncsmatrixdataList;
	}

	/**
	 * 统计区域破坏系数
	 * 
	 * @param ncsIds
	 *            统计的数据范围
	 * @return
	 * @author brightming 2014-1-19 下午1:12:29
	 */
	public List<Map<String, Object>> queryAreaDamageFactor(List<Long> ncsIds) {
		return rnoStructureQueryDao.queryAreaDamageFactor(ncsIds);
	}

	/**
	 * 计算区域归一化干扰水平
	 * 
	 * @param ncsIds
	 *            统计的数据范围
	 * @return
	 * @author brightming 2014-1-19 下午1:12:05
	 */
	public List<Map<String, Object>> queryAreaNormalizeFactor(List<Long> ncsIds) {
		return rnoStructureQueryDao.queryAreaNormalizeFactor(ncsIds);
	}

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
	public List<Map<String, Object>> getNcsClusterCellAndOutputEachOtherInterValue(
			final long clusterId, final List<Map<String, Object>> clusterCells) {
		return rnoStructureQueryDao
				.getNcsClusterCellAndOutputEachOtherInterValue(clusterId,
						clusterCells);
	}

	/**
	 * 包含具有指定bsc的ncs列表
	 * 
	 * @param bsc
	 * @return
	 * @author brightming 2014-2-11 下午1:51:52
	 */
	public List<Map<String, Object>> searchNcsContainCell(String cell, String bsc, long cityId, String manufacturers) {
		
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> res = rnoStructureQueryDao
				.searchNcsContainCell(bsc, cityId, manufacturers);
		String ncsId = "";
		String manuf = "";
		boolean flag = false;
		for (Map<String, Object> map : res) {
			ncsId = map.get("NCS_ID").toString();
			manuf = map.get("MANUFACTURERS").toString();
			flag = rnoStructureQueryDao.isNcsDataExistedByDateAndBsc(cell,ncsId,cityId,manuf);
			if(flag) {
				result.add(map);
			}
		}
		return result;
	}
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
	public List<Map<String, Object>> searchNcsContainCell(G2NcsQueryCond cond)  {
		return rnoStructureQueryDao.searchNcsContainCell(cond);
	}
	/**
	 * 获取指定小区的在指定ncs里的测量信息
	 * 
	 * @param ncsId
	 * @param cell
	 * @return
	 * @author brightming 2014-2-11 下午2:14:24
	 */
	public List<Map<String, Object>> getCellNcsInfo(long ncsId, String cell, long cityId, String manufacturers) {
		return rnoStructureQueryDao.getCellNcsInfo(ncsId, cell, cityId, manufacturers);
	}

	/**
	 * 计算指定ncs范围，指定小区的频点的干扰情况 如果指定小区为null或为空，则计算指定ncs内的所有的小区的频点干扰情况
	 * 
	 * @param ncsIds
	 * @param cells
	 * @return
	 * @author brightming 2014-2-13 下午3:34:51
	 */
	public CellFreqInterferList staticsNcsCellFreqInterfer(List<Long> ncsIds,
			List<String> cells) {

		log.info("进入方法：staticsNcsCellFreqInterfer。ncsIds=" + ncsIds + ",cells="
				+ cells);
		if (ncsIds == null || ncsIds.isEmpty()) {
			log.error("staticsNcsCellFreqInterfer传入的参数ncsIds为空！无法进行计算！");
			return null;
		}
		Connection connection = DataSourceConn.initInstance().getConnection();
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
			return null;
		}
		long cnt = rnoStructureAnalysisDao.prepareFreqInterferMetaData(
				connection, ncsIds, cells, "rno_ncs");
		log.info("准备计算：[" + cnt + "]个小区的频点的干扰情况。");
		CellFreqInterferList result = null;
		if (cnt > 0) {
			result = rnoStructureAnalysisDao
					.calculateFreqInterferResult(connection);
		}
		try {
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			connection.setAutoCommit(true);
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取单个ncs的结构优化数据
	 * 
	 * @param diskPath
	 *            存储文件结果的父目录
	 * @param taskId
	 * @param dataType
	 * @return 如果返回null，表示还没有准备好，正在计算当中
	 * @author brightming 2014-3-6 下午5:58:57
	 */
	public List<Map<String, Object>> getSingleNcsStructData(String diskPath,
			long ncsId, String dataType) {
		log.info("进入方法：getSingleNcsStructData.diskPath=" + diskPath + ",ncsId="
				+ ncsId + ",dataType=" + dataType);
		String key = "RNO_SINGLE_NCS_ANA_RES_" + dataType + "_" + ncsId;
		// String sk = "RNO_SINGLE_NCS_ANA_RES_" + ncsId + "_";
		String saveCachePreKey = "RNO_SINGLE_NCS_ANA_RES_";
		List data = null;
		try {
			data = memCached.get(key);
			if (data != null) {
				log.info("从缓存获取到单个ncs分析结果。");
				// return data;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 如果缓存不存在
		File file = null;
		// 如果不存在，看磁盘有没有数据
		String excelPath = diskPath + "/ncs_cluster_res_" + ncsId + ".xls";
		file = new File(excelPath);
		if (file.exists()) {
			log.info("存在excel文件。从excel[" + excelPath + "]读取数据");
			// 从excel里读取
			// data = getReportFromExcel(excelPath, dataType, saveCachePreKey,
			// ncsId);
			data = getReportFromRoFile(
					excelPath
							+ RnoConstant.ReportConstant.NCS_REPORT_FILE_FOR_PROG_READ_SUFFIX,
					dataType, saveCachePreKey, ncsId);
			return data;
		}
		log.info("ncs[" + ncsId + "]没有excel数据文件.");
		// 没有数据，

		// 判断ncs数据是否存在
		List ncsDesc = rnoStructureQueryDao.getNcsDescriptorDataById(ncsId);
		if (ncsDesc == null || ncsDesc.isEmpty()) {
			log.error("不存在指定的ncs[" + ncsId + "]数据！");
			return null;
		}

		boolean needWork = true;
		synchronized (lockObj) {
			// 判断是否有正在进行的标识
			String flagPath = diskPath + "/ncs_cluster_res_doing_" + ncsId;
			file = new File(flagPath);
			if (file.exists()) {
				// 有该标识，表示正在进行，直接返回，还是循环等待一段时间？
				return null;
			}
			log.info("ncs[" + ncsId + "]没有正在分析的标识。");
			// 进一步检查是否有数据文件
			file = new File(excelPath);
			if (file.exists()) {
				needWork = false;// 已经存在
			}
			if (needWork) {
				// 创建标识
				FileTool.saveInputStream(flagPath,
						new ByteArrayInputStream("1".getBytes()));
			}
		}

		if (needWork) {
			// 启动异步线程
			log.info("启动异步线程计算。。。");
			// ---先将状态改为正在分析中----//
			rnoStructureQueryDao.updateNcsDescStatus(ncsId, "A");
			DoSingleNcsAnalysisWorker work = new DoSingleNcsAnalysisWorker(
					diskPath, rnoStructureAnalysisDao, ncsId);
			ExecutorManager.execute(work);
			return null;
		} else {
			// data = getReportFromExcel(excelPath, dataType, saveCachePreKey,
			// ncsId);
			// 从文件中读取
			data = getReportFromRoFile(
					excelPath
							+ RnoConstant.ReportConstant.NCS_REPORT_FILE_FOR_PROG_READ_SUFFIX,
					dataType, saveCachePreKey, ncsId);
			return data;
		}

	}

	// /**
	// * 获取单个ncs的结构指标数据
	// *
	// * @param ncsId
	// * @return
	// * @author brightming 2014-2-14 下午3:40:04
	// */
	// public List<Map<String, Object>> getSingleNcsStructAnaRes(long ncsId) {
	// return getSingleNcsStructData(ncsId, RnoConstant.ReportConstant.CELLRES);
	// }
	//
	// /**
	// * 获取单个ncs的所有连通簇列表
	// *
	// * @param ncsId
	// * @return
	// * @author brightming 2014-2-15 下午2:17:12
	// */
	// public List<Map<String, Object>> getSingleNcsStructClusterList(long
	// ncsId) {
	// // return rnoStructureQueryDao.getSingleNcsClusterListInfo(ncsId);
	// return getSingleNcsStructData(ncsId, RnoConstant.ReportConstant.CLUSTER);
	// }
	//
	// /**
	// * 获取单个ncs的最大连通簇列表
	// *
	// * @param ncsId
	// * @return
	// * @author brightming 2014-2-15 下午2:17:25
	// */
	// public List<Map<String, Object>> getSingleNcsStructMaxClusterList(long
	// ncsId) {
	// // return rnoStructureQueryDao.getSingleNcsClusterCellListInfo(ncsId);
	// return getSingleNcsStructData(ncsId,
	// RnoConstant.ReportConstant.CLUSTERCELL);
	// }
	//
	// /**
	// * 获取单个ncs的簇内小区列表
	// *
	// * @param ncsId
	// * @return
	// * @author brightming 2014-2-15 下午2:17:36
	// */
	// public List<Map<String, Object>> getSingleNcsStructClusterCellList(
	// long ncsId) {
	// // return
	// rnoStructureQueryDao.getSingleNcsClusterCellRelationInfo(ncsId);
	// return getSingleNcsStructData(ncsId,
	// RnoConstant.ReportConstant.CLUSTERCELLRELA);
	// }

	/**
	 * 提交ncs汇总分析任务
	 * 
	 * @param creator
	 *            提交者账号
	 * @param ncsIds
	 * @param savePath
	 *            结果输出的保存路径
	 * @param cityId
	 *            市区id
	 * @param areaLevel
	 *            统计级别：市、区/县
	 * @param areaId
	 *            统计级别所在区域的id。当统计级别为市的时候，等同于cityId
	 * @param areaName
	 *            统计区域的名称
	 * @param taskName
	 *            分析任务的名称
	 * @param taskDescription
	 *            分析任务的描述
	 * 
	 * @return key:flag true/false key:msg 消息
	 * 
	 * @author brightming 2014-2-17 上午9:44:27
	 */
	public Map<String, Object> submitRnoNcsAnalysisTask(String creator,
			String ncsIds, String savePath, long cityId, String areaLevel,
			long areaId, String areaName, String taskName,
			String taskDescription) {

		Map<String, Object> result = new HashMap<String, Object>();

		RnoTask task = new RnoTask();
		task.setCreator(creator);
		task.setDataLevel(areaLevel);
		task.setLevelId(areaId + "");
		task.setLevelName(areaName);
		task.setCreateTime(new java.sql.Timestamp(new Date().getTime()));
		task.setTaskGoingStatus(TaskStatus.DOING.getCode());
		task.setTaskType("NCS_ANALYSIS");
		task.setTaskName(taskName);
		task.setDescription(taskDescription);

		// 保存任务
		final Long taskId = rnoTaskDao.saveTask(task);
		// 保存任务与ncsIds的关联信息
		String ncsIdsStr = sortNcsIds(ncsIds);
		boolean flag = rnoTaskDao.saveTaskNcsIdList(taskId, ncsIdsStr);
		if (taskId == null) {
			log.error("ncs分析任务提交失败！参数ncsIds=" + ncsIds);
			if (flag == false) {
				log.error("该分析任务d ncsIds关联信息已存在，不再提交！参数ncsIds=" + ncsIdsStr);
			}
			result.put("flag", false);
			result.put("msg", "需提供有效的ncs");
			return result;
		}
		// 保存任务参数
		List<Map<String, String>> paramss = new ArrayList<Map<String, String>>();
		String[] ncss = ncsIds.split(",");
		Map<String, String> params = null;
		for (String ncs : ncss) {
			if (ncs != null && !"".equals(ncs.trim())) {
				params = new HashMap<String, String>();
				params.put("NCSID", ncs.trim());
				paramss.add(params);
			}
		}
		if (paramss.isEmpty()) {
			log.error("ncs分析任务所需要的ncsid列表无效！任务无法提交！");
			// 删除任务
			rnoTaskDao.deleteTask(taskId);
			// 删除任务与ncsIds的关联信息
			rnoTaskDao.deleteTaskNcsIdListByTaskId(taskId);
			result.put("flag", false);
			result.put("msg", "需提供有效的ncs");
			return result;
		}

		Map<String, String> one = new HashMap<String, String>();
		one.put("AREALEVEL", areaLevel);
		paramss.add(one);
		one = new HashMap<String, String>();
		one.put("AREAID", areaId + "");
		paramss.add(one);
		one = new HashMap<String, String>();
		one.put("AREANAME", areaName);
		paramss.add(one);
		one = new HashMap<String, String>();
		one.put("CITYID", cityId + "");
		paramss.add(one);

		int cnt = rnoTaskDao.saveTaskParam(taskId, paramss);
		if (cnt == 0) {
			log.error("ncs分析任务所需要的ncsid列表无效！任务无法提交！");
			// 删除任务
			rnoTaskDao.deleteTask(taskId);
			// 删除任务与ncsIds的关联信息
			rnoTaskDao.deleteTaskNcsIdListByTaskId(taskId);
			result.put("flag", false);
			result.put("msg", "任务提交出错！");
			return result;
		}
		// 异步启动任务
		final Map<String, String> extra = new HashMap<String, String>();
		extra.put("SAVE_PATH", savePath);
		extra.put("creator", creator);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 保存运行的线程信息
				long threadId = Thread.currentThread().getId();
				ExecutorManager.saveTaskThreadInfo(taskId, threadId);
				// ExecutorManager.printMap();
				rnoNcsGatherAnalysisWorker.doWork(taskId, extra);

				// 删除运行结束的线程信息
				ExecutorManager.deleteTaskThreadInfoByTaskId(taskId);
				// ExecutorManager.printMap();
			}
		}).start();

		result.put("flag", true);
		result.put("msg", "任务正在执行。");
		return result;
	}

	/**
	 * 分页查询由account启动的任务
	 * 
	 * @param account
	 * @param cond
	 * @param newPage
	 * @return
	 * @author brightming 2014-2-18 下午3:37:55
	 */
	public List<Map<String, Object>> queryNcsTaskByPage(String account,
			Map<String, String> condition, Page page) {
		log.info("进入方法：queryNcsTaskByPage。condition=" + condition + ",page="
				+ page);
		if (page == null) {
			log.warn("方法：queryNcsTaskByPage的参数：page 是空！无法查询!");
			return Collections.EMPTY_LIST;
		}
		long totalCnt = page.getTotalCnt();
		if (totalCnt < 0) {
			totalCnt = rnoStructureQueryDao.getNcsTaskCount(account, condition);
			page.setTotalCnt((int) totalCnt);
		}
		int startIndex = page.calculateStart();
		int cnt = page.getPageSize();
		List<Map<String, Object>> res = rnoStructureQueryDao
				.queryNcsTaskByPage(account, condition, startIndex, cnt);
		return res;
	}

	/**
	 * 获取任务详情
	 * 
	 * @param taskId
	 * @return
	 * @author brightming 2014-2-19 上午10:04:32
	 */
	public RnoTask getTaskById(long taskId) {
		return rnoTaskDao.getTaskById(taskId);
	}

	/**
	 * 重新计算指定的ncs的汇总任务
	 * 
	 * @param account
	 * @param taskId
	 * @param savePath
	 * @param taskName
	 * @param taskDescription
	 * @return
	 * @author brightming 2014-2-19 上午10:55:32
	 */
	public Map<String, Object> recalculateRnoNcsAnalysisTask(String account,
			final long taskId, String savePath, String taskName,
			String taskDescription) {
		Map<String, Object> result = new HashMap<String, Object>();

		RnoTask task = rnoTaskDao.getTaskById(taskId);
		if (task == null || !account.equals(task.getCreator())) {
			log.error("用户[" + account + "]不存在taskId=" + taskId + "的任务！");
			result.put("flag", false);
			result.put("msg", "用户[" + account + "]不存在taskId=" + taskId + "的任务！");
			return result;
		}

		// 正在执行中
		if (TaskStatus.DOING.getCode().equals(task.getTaskGoingStatus())) {
			log.error("任务[" + taskId + "]正在执行中，当前不允许重新执行!");
			result.put("flag", false);
			result.put("msg", "任务[" + taskId + "]正在执行中，当前不允许重新执行!");
			return result;
		}

		// 更新任务状态
		task.setTaskName(taskName);
		task.setDescription(taskDescription);
		task.setTaskGoingStatus(TaskStatus.DOING.getCode());
		task.setResult("");
		rnoTaskDao.updateTask(task);

		// 异步启动任务
		final Map<String, String> extra = new HashMap<String, String>();
		extra.put("SAVE_PATH", savePath);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 保存运行的线程信息
				long threadId = Thread.currentThread().getId();
				ExecutorManager.saveTaskThreadInfo(taskId, threadId);
				rnoNcsGatherAnalysisWorker.doWork(taskId, extra);

				// 删除运行结束的线程信息
				ExecutorManager.deleteTaskThreadInfoByTaskId(taskId);
			}
		}).start();

		result.put("flag", true);
		result.put("msg", "任务正在执行。");
		return result;
	}

	/**
	 * 从文件中读取内容
	 * 
	 * @param fileFullName
	 * @param dataType
	 * @param saveCachePreKey
	 * @param taskId
	 * @return
	 * @author brightming 2014-6-13 下午12:06:12
	 */
	public List<Map<String, Object>> getReportFromRoFile(String fileFullName,
			String dataType, String saveCachePreKey, long taskId) {

		// 整理返回的数据为需要的格式
		String code = "";

		List<Map<String, Object>> result = null;
		result = FileTool.readDataFromTxt(fileFullName, dataType);
		String sk = saveCachePreKey + code + "_" + taskId;
		try {
			memCached.set(sk, 6 * 60 * 60,// 6个小时
					result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 从excel读取数据效率太低，而且容易造成内存溢出
	 * 
	 * @param excelPath
	 * @param dataType
	 * @param saveCachePreKey
	 * @param taskId
	 * @return
	 * @author brightming 2014-6-13 下午12:00:45
	 */
	@Deprecated
	public List<Object> getReportFromExcel(String excelPath, String dataType,
			String saveCachePreKey, long taskId) {
		// 获取excel文件，读取内容，放入缓存服务器
		// --2014-6-12 修改 ，防止excel文件过大，一次性读入太多内容到内存造成溢出-- begin----//
		// List<String> sheets = Arrays.asList(
		// RnoConstant.ReportConstant.NCS_REPORT_CELL_RES_SHEET,
		// RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_SHEET,
		// RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_CELL_SHEET,
		// RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_CELL_RELA_SHEET,
		// RnoConstant.ReportConstant.NCS_REPORT_OPT_SUGGESTION_SHEET);

		List<String> sheets = new ArrayList<String>();
		if (RnoConstant.ReportConstant.CELLRES.equals(dataType)) {
			sheets.add(RnoConstant.ReportConstant.NCS_REPORT_CELL_RES_SHEET);
		} else if (RnoConstant.ReportConstant.CLUSTER.equals(dataType)) {
			sheets.add(RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_SHEET);
		} else if (RnoConstant.ReportConstant.CLUSTERCELL.equals(dataType)) {
			sheets.add(RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_CELL_SHEET);
		} else if (RnoConstant.ReportConstant.CLUSTERCELLRELA.equals(dataType)) {
			sheets.add(RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_CELL_RELA_SHEET);
		} else if (RnoConstant.ReportConstant.OPTSUGGESTION.equals(dataType)) {
			sheets.add(RnoConstant.ReportConstant.NCS_REPORT_OPT_SUGGESTION_SHEET);
		}
		// --2014-6-12 修改 ，防止excel文件过大，一次性读入太多内容到内存造成溢出-- end----//

		Map<String, List> allDatas = FileTool.getListRows(excelPath, sheets);
		if (allDatas == null) {
			log.error("未获取到相应的数据。excelpath=" + excelPath);
			return null;
		}
		// 整理返回的数据为需要的格式
		List shdat = null;
		String code = "";
		Map<String, String> titleToKeys = new HashMap<String, String>();
		String[] titles = null;
		String[] keys = null;

		List data = null;

		List<Map<String, Object>> result = null;

		for (String sh : sheets) {
			shdat = allDatas.get(sh);
			result = new ArrayList<Map<String, Object>>();// 每个sheet转为List<Map>结构的存储变量
			if (sh.equals(RnoConstant.ReportConstant.NCS_REPORT_CELL_RES_SHEET)) {
				code = RnoConstant.ReportConstant.CELLRES;// "cellres";
				titles = RnoConstant.ReportConstant.NCS_REPORT_CELL_RES_SHEET_TITLES;
				keys = RnoConstant.ReportConstant.NCS_REPORT_CELL_RES_SHEET_KEYS;
			} else if (sh
					.equals(RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_SHEET)) {
				code = RnoConstant.ReportConstant.CLUSTER;// "cluster";
				titles = RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_SHEET_TITLES;
				keys = RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_SHEET_KEYS;
			} else if (sh
					.equals(RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_CELL_SHEET)) {
				code = RnoConstant.ReportConstant.CLUSTERCELL;// "clustercell";
				titles = RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_CELL_SHEET_TITLES;
				keys = RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_CELL_SHEET_KEYS;
			} else if (sh
					.equals(RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_CELL_RELA_SHEET)) {
				code = RnoConstant.ReportConstant.CLUSTERCELLRELA;// "clustercellrela";
				titles = RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_CELL_RELA_SHEET_TITLES;
				keys = RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_CELL_RELA_SHEET_KEYS;
			} else if (sh
					.equals(RnoConstant.ReportConstant.NCS_REPORT_OPT_SUGGESTION_SHEET)) {
				code = RnoConstant.ReportConstant.OPTSUGGESTION;// "optsuggestion";
				titles = RnoConstant.ReportConstant.NCS_REPORT_OPT_SUGGESTION_TITLES;
				keys = RnoConstant.ReportConstant.NCS_REPORT_OPT_SUGGESTION_KEYS;
			}

			titleToKeys.clear();
			for (int i = 0; i < titles.length; i++) {
				titleToKeys.put(titles[i], keys[i]);
			}

			// 将List<List>转为List<Map>
			List sheetTitle = (List) shdat.get(0);// 该sheet的第一行数据，即标题
			Map<String, Object> one = null;
			List row = null;
			for (int j = 1; j < shdat.size(); j++) {
				one = new HashMap<String, Object>();
				row = (List) shdat.get(j);// 每一行的数据
				for (int k = 0; k < row.size(); k++) {
					one.put(titleToKeys.get(sheetTitle.get(k) + ""), row.get(k));
				}
				result.add(one);
			}

			allDatas = null;
			shdat = null;
			System.gc();
			// 正是要返回的那个数据
			if (code.equals(dataType)) {
				data = result;
			}

			String sk = saveCachePreKey + code + "_" + taskId;
			try {
				memCached.set(sk, 6 * 60 * 60,// 6个小时
						result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return data;
	}

	/**
	 * 获取存储在excel中的报告
	 * 
	 * @param excelPath
	 * @param dataType
	 *            cellres cluster clustercell clustercellrela
	 * @return
	 * @author brightming 2014-2-20 上午10:11:51
	 */
	public List<Object> getNcsTaskReport(long taskId, String excelPath,
			String dataType) {
		log.info("进入方法：getNcsTaskReport。taskId=" + taskId + ",excelpath="
				+ excelPath + ",dataType=" + dataType);
		// 检查缓存服务器是否有相应的数据
		String key = "RNO_NCS_ANA_RES_" + dataType + "_" + taskId;
		List data = null;
		try {
			data = memCached.get(key);
			if (data != null) {
				log.info("从缓存获取到ncs分析结果。");
				return data;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String cachePreKey = "RNO_NCS_ANA_RES_";
		// 修改为从文本中读取
		// data = getReportFromExcel(excelPath, dataType, cachePreKey, taskId);
		data = getReportFromRoFile(
				excelPath
						+ RnoConstant.ReportConstant.NCS_REPORT_FILE_FOR_PROG_READ_SUFFIX,
				dataType, cachePreKey, taskId);
		if (data == null) {
			log.error("读取ncs汇总分析任务：" + taskId + "的报告：" + excelPath
					+ " 失败！未得到数据!");
		}
		// log.info("退出方法getNcsTaskReport:"+data.size());
		return data;
	}

	/**
	 * 分页获取指定ncsId的ncsCell
	 * 
	 * @author Liang YJ
	 * @date 2014-2-19 上午9:29:53
	 * @param ncsId
	 *            数据源表中的RNO_NCS_DESC_ID字段
	 * @param page
	 *            分页参数
	 * @return
	 */
	public NcsCellQueryResult getNcsCellByPage(long ncsDescId, Page page) {
		log.info("进入方法：getNcsCellByPage.ncsDescId=" + ncsDescId + ",page="
				+ page);
		List<RnoNcsCell> ncsCellList = null;
		// 从缓存服务器获取
		// 从数据库查询
		if (null == ncsCellList) {
			log.info("从数据库中获取->");
			ncsCellList = rnoStructureAnalysisDao
					.getNcsCellByNcsDescId(ncsDescId);
			log.info("从数据库中获取结果数为： " + ncsCellList == null ? 0 : ncsCellList
					.size());
			// 将查询得到的结果放入缓存中
			log.info("放入缓存->");

		}

		// 根据page截取所需的结果
		Integer totalCnt = 0;
		List<RnoNcsCell> resultNcsCellList = new ArrayList<RnoNcsCell>();
		if (null != ncsCellList && ncsCellList.size() > 0) {
			totalCnt = ncsCellList.size();
			int start = page.getForcedStartIndex() == -1 ? (page
					.getCurrentPage() - 1) * page.getPageSize() : page
					.getForcedStartIndex();
			// int end = page.getCurrentPage() * page.getPageSize();
			int end = start + page.getPageSize();
			// /范围[start,end)
			int size = ncsCellList.size();
			if (start < 0) {
				start = 0;
			}
			if (start > size) {
				start = size;
			}
			if (end < 1) {
				end = 1;
			}
			if (end > size) {
				end = size;
			}

			log.info("start=" + start + ",end=" + end);
			// 截取
			resultNcsCellList = ncsCellList.subList(start, end);
		}
		NcsCellQueryResult result = new NcsCellQueryResult();
		result.setTotalCnt(totalCnt);
		result.setNcsCellList(resultNcsCellList);
		return result;
	}

	/**
	 * 根据id删除ncs相关记录。
	 * 
	 * @param ncsId
	 * @author brightming 2014-3-13 下午5:01:13
	 */
	public void deleteNcsRecById(long ncsId) {
		// 删除相关的缓存记录
		// 进行标记删除
		rnoStructureQueryDao.deleteNcsDataById(ncsId, false);
	}

	/**
	 * 按逗号分隔的ncs id标记删除ncs记录
	 * 
	 * @title
	 * @param ncsIds
	 * @author chao.xj
	 * @date 2014-3-27上午11:38:34
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void deleteNcsSelectedRecById(String ncsIds) {
		// 删除相关的缓存记录
		// 进行标记删除
		rnoStructureQueryDao.deleteNcsSelectedDataById(ncsIds, false);
	}

	/**
	 * 
	 * @title 获取所有的小区平面坐标数据集合
	 * @param cellresList
	 *            结构指标数据
	 * @param leftTLng
	 *            区域左上经度
	 * @param leftTLat
	 *            区域左上纬度
	 * @param rightBLng
	 *            区域右下经度
	 * @param rightBLat
	 *            区域右下纬度
	 * @return 小区平面坐标集合
	 * @author chao.xj
	 * @date 2014-5-16上午10:14:12
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<RenderCellInfo> getCellXYPlaneCoordinateInfos(
			List<Map<String, Object>> cellresList, double leftTLng,
			double leftTLat, double rightBLng, double rightBLat) {
		log.info("进入getCellXYPlaneCoordinateInfos List<Map<String, Object>> cellresList, double leftTLng,double leftTLat, double rightBLng, double rightBLat："
				+ cellresList
				+ ","
				+ leftTLng
				+ ","
				+ leftTLat
				+ ","
				+ rightBLng + "," + rightBLat);
		List<RenderCellInfo> renderCellList = new ArrayList<RenderCellInfo>();
		log.info("cellresList 大小:" + cellresList.size());
		for (Map<String, Object> cellMap : cellresList) {
			RenderCellInfo renderCellInfo = new RenderCellInfo();
			// Map<String, Object> map=(Map<String, Object>)object;
			// System.out.println("object:"+cellMap.get("cell"));
			log.info("cellMap.get(\"lng\").toString():"
					+ cellMap.get("lng").toString());
			renderCellInfo.setCell(cellMap.get("cell").toString());
			double interedCoeff = 0;
			double netStructureIndex = 0;
			if (!"".equals(cellMap.get("BE_INTERFER").toString())) {
				interedCoeff = Double.parseDouble(cellMap.get("BE_INTERFER")
						.toString());
			}
			if (!"".equals(cellMap.get("NET_STRUCT_FACTOR").toString())) {
				netStructureIndex = Double.parseDouble(cellMap.get(
						"NET_STRUCT_FACTOR").toString());
			}
			renderCellInfo.setInteredCoeff(interedCoeff);
			renderCellInfo.setNetStructureIndex(netStructureIndex);
			double cellLng = 0;
			double cellLat = 0;
			if (!"".equals(cellMap.get("lng").toString())) {
				cellLng = Double.parseDouble(cellMap.get("lng").toString());
			} else {
				log.info("lng为空!!!!!!");
				continue;
			}
			if (!"".equals(cellMap.get("lat").toString())) {
				cellLat = Double.parseDouble(cellMap.get("lat").toString());
			} else {
				continue;
			}

			// 转换得到缩放后的小区平面坐标
			double[] afterShrinkXY = getRelaCoordinateForMappingCanvas(
					leftTLng, leftTLat, rightBLng, rightBLat, cellLng, cellLat,
					RnoStructureServiceImpl.afterShrinkLen);
			renderCellInfo.setCellX(afterShrinkXY[0]);
			renderCellInfo.setCellY(afterShrinkXY[1]);
			renderCellList.add(renderCellInfo);
		}
		log.info("退出getCellXYPlaneCoordinateInfos renderCellList："
				+ renderCellList.size());
		return renderCellList;
	}

	/**
	 * 
	 * @title 转换映射至画布收缩后的小区平面坐标
	 * @param leftTLng
	 *            　区域左上经度
	 * @param leftTLat
	 *            　区域左上纬度
	 * @param rightBLng
	 *            　区域右下经度
	 * @param rightBLat
	 *            　区域右下纬度
	 * @param cellLng
	 *            　小区经度
	 * @param cellLat
	 *            　小区纬度
	 * @return　收缩后站点画布平面坐标
	 * @author chao.xj
	 * @date 2014-5-9上午11:19:51
	 * @company 怡创科技
	 * @version 1.2
	 */
	public double[] getRelaCoordinateForMappingCanvas(double leftTLng,
			double leftTLat, double rightBLng, double rightBLat,
			double cellLng, double cellLat, int afterShrinkLen) {
		log.info("进入getRelaCoordinateForMappingCanvas(double leftTLng,double leftTLat,double rightBLng,double rightBLat,double cellLng,double cellLat)"
				+ leftTLng
				+ ","
				+ leftTLat
				+ ","
				+ rightBLng
				+ ","
				+ rightBLat
				+ "," + cellLng + "," + cellLat);
		// int FIXEDLEN=800;
		// bts原点坐标经纬度转换平面坐标
		double[] leftTxyarr;
		double actualLeftTX;
		double actualLeftTY;

		double[] rightBxyarr;
		double actualRightBX;
		double actualRightBY;

		double actualAreaLenght;
		double actualAreaWidth;

		double[] cellxyarr;
		double actualCellX;
		double actualCellY;

		double differX;
		double differY;

		double shrinkTimes;

		double afterShrinkX;
		double afterShrinkY;

		leftTxyarr = CoordinateHelper.lonLat2Mercator(leftTLng, leftTLat);
		actualLeftTX = leftTxyarr[0];
		actualLeftTY = leftTxyarr[1];

		rightBxyarr = CoordinateHelper.lonLat2Mercator(rightBLng, rightBLat);
		actualRightBX = rightBxyarr[0];
		actualRightBY = rightBxyarr[1];
		// 实际区域长度＝实际右下坐标X－左上坐标X
		actualAreaLenght = actualRightBX - actualLeftTX;
		// 实际区域宽度＝实际左上坐标Y－右下坐标Y
		actualAreaWidth = actualLeftTY - actualRightBY;

		cellxyarr = CoordinateHelper.lonLat2Mercator(cellLng, cellLat);
		actualCellX = cellxyarr[0];
		actualCellY = cellxyarr[1];
		// 点坐标实际X－左上实际X=相差X
		// 站点实际坐标X减去区域左上实际X坐标得到X轴距离差
		// X轴距离差：differX
		differX = actualCellX - actualLeftTX;
		// 左上纬度-点坐标纬度=相差Y
		// 区域右上实际Y坐标减去站点实际Y坐标得到Y轴距离差
		// Y轴距离差：differY
		differY = actualLeftTY - actualCellY;
		// 收缩倍数=区域实际长度/映射虚拟像素长度
		shrinkTimes = actualAreaLenght / afterShrinkLen;
		// 相差X/缩放倍数=缩放后的X
		afterShrinkX = differX / shrinkTimes;
		// 相差Y/缩放倍数=缩放后的Y
		afterShrinkY = differY / shrinkTimes;
		log.info("退出getRelaCoordinateForMappingCanvas afterShrinkX,afterShrinkY："
				+ afterShrinkX + "," + afterShrinkY);
		return new double[] { afterShrinkX, afterShrinkY };
	}

	/**
	 * 
	 * @title 获取矩形区域长度与宽度
	 * @param leftTLng
	 * @param leftTLat
	 * @param rightBLng
	 * @param rightBLat
	 * @param afterShrinkLen
	 * @return 0,1索引值代表实际的长宽_米，2,3索引值代表收缩后的长宽_像素PX
	 * @author chao.xj
	 * @date 2014-5-16下午06:39:46
	 * @company 怡创科技
	 * @version 1.2
	 */
	public double[] getAreaActualLenWidAndAfterShrinkLenWid(double leftTLng,
			double leftTLat, double rightBLng, double rightBLat,
			int afterShrinkLen) {
		// bts原点坐标经纬度转换平面坐标
		double[] leftTxyarr;
		double actualLeftTX;
		double actualLeftTY;

		double[] rightBxyarr;
		double actualRightBX;
		double actualRightBY;

		double actualAreaLenght;
		double actualAreaWidth;

		leftTxyarr = CoordinateHelper.lonLat2Mercator(leftTLng, leftTLat);
		actualLeftTX = leftTxyarr[0];
		actualLeftTY = leftTxyarr[1];

		rightBxyarr = CoordinateHelper.lonLat2Mercator(rightBLng, rightBLat);
		actualRightBX = rightBxyarr[0];
		actualRightBY = rightBxyarr[1];
		// 区域长度＝实际右下经度X－左上经度X
		actualAreaLenght = actualRightBX - actualLeftTX;
		// 实际区域宽度＝实际左上坐标Y－右下坐标Y
		actualAreaWidth = actualLeftTY - actualRightBY;
		int afterShrinkWid = (int) Math.round(actualAreaWidth * afterShrinkLen
				/ actualAreaLenght);
		return new double[] { actualAreaLenght, actualAreaWidth,
				afterShrinkLen, afterShrinkWid };
	}

	/**
	 * 
	 * @title 获取网格存储小区MAP集合数据
	 * @param renderCellList
	 * @param actualAreaLenght
	 * @param actualAreaWidth
	 * @param gridMeter
	 * @param expandMeter
	 * @return
	 * @author chao.xj
	 * @date 2014-5-16下午12:29:52
	 * @company 怡创科技
	 * @version 1.2
	 */
	public Map<String, Object> getRowColumnToCellMap(
			List<RenderCellInfo> renderCellList, double actualAreaLength,
			double actualAreaWidth, int gridMeter, int expandMeter) {
		log.info("进入getRowColumnToCellMap List<RenderCellInfo> renderCellList,double actualAreaLength,double actualAreaWidth,int gridMeter,int expandMeter:"
				+ renderCellList.size()
				+ ","
				+ actualAreaLength
				+ ","
				+ actualAreaWidth + "," + gridMeter + "," + expandMeter);
		Map<String, Object> rowColumnToCellMap = new HashMap<String, Object>();
		List<RenderCellInfo> cellList;
		GridStorageCells gridStorageCells;
		int phylen = (int) Math.round(actualAreaLength);
		int phywid = (int) Math.round(actualAreaWidth);
		// System.out.println("phylen:"+phylen+"--phywid:"+phywid);
		// 缩放后的画布长度
		int changwidth = phywid * RnoStructureServiceImpl.afterShrinkLen
				/ phylen;
		// System.out.println(1.0f*gridMeter*RnoStructureServiceImpl.afterShrinkLen/phylen);
		// 从米转换至像素的网格大小gridSize*gridSize
		int gridSize = Math.round(1.0f * gridMeter
				* RnoStructureServiceImpl.afterShrinkLen / phylen);// 网格大小
		// 将扩展多少米转换成像素
		int expandPx = Math.round(1.0f * expandMeter
				* RnoStructureServiceImpl.afterShrinkLen / phylen);// 扩展多少米转换成扩展多少像素
		// 迭代所有小区数据
		for (RenderCellInfo cellInfo : renderCellList) {
			int cellY = (int) Math.round(cellInfo.getCellY());
			int cellX = (int) Math.round(cellInfo.getCellX());
			String cellString = cellInfo.getCell();
			log.info("cell,cellX,cellY:" + cellString + "," + cellX + ","
					+ cellY);
			// 映射缩放后画布宽changwidth即为行gridSize网格大小
			for (int i = 0; i < changwidth / gridSize; i++) {
				boolean flag = false;
				if (i * gridSize <= cellY && (i + 1) * gridSize >= cellY) {
					// 映射缩放后画布长RnoStructureServiceImpl.afterShrinkLen即为列
					for (int j = 0; j < RnoStructureServiceImpl.afterShrinkLen
							/ gridSize; j++) {
						if (j * gridSize <= cellX
								&& (j + 1) * gridSize >= cellX) {
							// 扩展像素值/一网格大小像素值＝行列移动大小
							// 由像素转换为行列大小
							int expandValue = Math.round(1.0f * expandPx
									/ gridSize);
							// 0.5如何办待优化

							int mMax = (int) Math
									.round(1.0f * (i + expandValue));// 小于等于最大行
							// log.info("mMax:"+mMax);
							int mValue = i - expandValue;// 满足大于等于０
							// log.info("mValue:"+mValue);
							mMax = mMax <= changwidth / gridSize ? mMax
									: changwidth / gridSize;
							mValue = mValue >= 0 ? mValue : 0;
							int nMax = Math.round(1.0f * (j + expandValue));// 小于等于最大列
							// log.info("nMax:"+nMax);
							int nValue = j - expandValue;// 满足大于等于０
							// log.info("nValue:"+nValue);
							nMax = nMax <= RnoStructureServiceImpl.afterShrinkLen
									/ gridSize ? nMax
									: RnoStructureServiceImpl.afterShrinkLen
											/ gridSize;
							nValue = nValue >= 0 ? nValue : 0;
							log.info("mValue,mMax,nValue,nMax:" + mValue + ","
									+ mMax + "," + nValue + "," + nMax);
							for (int m = mValue; m <= mMax; m++) {
								for (int n = nValue; n <= nMax; n++) {
									// log.info("m,n行列存储小区:"+m+","+n+"存储小区-"+cellString);
									if (rowColumnToCellMap.containsKey(m + ","
											+ n)) {
										// log.info("存在的m,n"+m+","+n);
										// map集合已经有行列KEY键则取出来
										gridStorageCells = (GridStorageCells) rowColumnToCellMap
												.get(m + "," + n);
										cellList = gridStorageCells
												.getRenderCellList();
										cellList.add(cellInfo);
										gridStorageCells
												.setRenderCellList(cellList);
										rowColumnToCellMap.put(m + "," + n,
												gridStorageCells);
									} else {
										// log.info("不存在的m,n"+m+","+n);
										// map集合没有行列KEY键则新建
										gridStorageCells = new GridStorageCells();
										gridStorageCells.setRowIndex(m);
										gridStorageCells.setColIndex(n);
										cellList = new ArrayList<RenderCellInfo>();
										cellList.add(cellInfo);
										gridStorageCells
												.setRenderCellList(cellList);
										rowColumnToCellMap.put(m + "," + n,
												gridStorageCells);
									}
								}
							}
							flag = true;
							break;
						}
					}
					if (flag) {
						break;
					}
				}
			}
		}
		log.info("退出getRowColumnToCellMap rowColumnToCellMap:"
				+ rowColumnToCellMap.size());
		return rowColumnToCellMap;
	}

	/**
	 * 
	 * @title 通过传入不同的Code 生成图像并存储
	 * @param rowColumnToCellMap
	 *            网格行列到小区集合映射
	 * @param taskId
	 *            任务ID
	 * @param dataType
	 *            数据类型 如 cellres
	 * @param afterShrinkLen
	 *            收缩后的画布长度
	 * @param afterShrinkWid
	 *            收缩后的画布宽度
	 * @param gridMeter
	 *            一个网格实际多少米
	 * @param actualAreaLength
	 *            实际矩形区域长度米
	 * @param Code
	 *            0代表BE_INTERFER被干扰系数，1代表NET_STRUCT_FACTOR网络结构指数
	 * @author chao.xj
	 * @date 2014-5-16下午05:56:16
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void drawImage(Map<String, Object> rowColumnToCellMap, long taskId,
			String dataType, int afterShrinkLen, int afterShrinkWid,
			int gridMeter, double actualAreaLength, int Code) {
		log.info("进入drawImageMap<String, Object> rowColumnToCellMap,long taskId,String dataType,int afterShrinkLen,int afterShrinkWid,int gridMeter,double actualAreaLength,int Code");
		// 建立映射关系为了更好的从表中取得该code的颜色
		// RNO_TRAFFIC_INDEX_TYPE RNO_TRAFFIC_RENDERER_CONFIG
		Map<String, String> titleToKeys = new HashMap<String, String>();
		titleToKeys.put(String.valueOf(0), "BE_INTERFER");
		titleToKeys.put(String.valueOf(1), "NET_STRUCT_FACTOR");

		String codeString = titleToKeys.get(String.valueOf(Code)).toString();
		// 从米转换至像素的网格大小gridSize*gridSize
		int gridSize = (int) Math.round(1.0f * gridMeter
				* RnoStructureServiceImpl.afterShrinkLen / actualAreaLength);// 网格大小

		String folderString = "/op/rno/ana_result/image/";
		folderString = ServletActionContext.getServletContext().getRealPath(
				folderString);
		File folder = new File(folderString);
		if (!folder.exists() && !folder.isDirectory()) {
			log.info("不存在此文件夹需创建folder:" + folderString);
			folder.mkdir();
		}
		String pathString = "/op/rno/ana_result/image/" + codeString + "_"
				+ taskId + ".png";
		String realPath = ServletActionContext.getServletContext().getRealPath(
				pathString);
		log.info("realPath:" + realPath);
		// File file = new File("D:/image.png");
		File file = new File(realPath);
		// 1.在内存中建一张图片(得到画布的感觉)
		BufferedImage image = new BufferedImage(afterShrinkLen, afterShrinkWid,
				BufferedImage.TYPE_INT_RGB);
		// 获取画布的画笔
		Graphics2D g2 = (Graphics2D) image.getGraphics();
		// ---------- 增加下面的代码使得画布背景透明 -----------------
		image = g2.getDeviceConfiguration().createCompatibleImage(
				afterShrinkLen, afterShrinkWid, Transparency.TRANSLUCENT);
		// g2.dispose();
		g2 = image.createGraphics();
		// ---------- 背景透明代码结束 -----------------
		// 开始画出黑色网格线
		g2.setColor(Color.black);
		for (int i = 0; i < afterShrinkWid / gridSize + 1; i++) {
			// 行：
			g2.drawLine(0, i * gridSize, afterShrinkLen, i * gridSize);// 行
		}
		for (int j = 0; j < afterShrinkLen / gridSize + 1; j++) {
			// 列
			g2.drawLine(j * gridSize, 0, j * gridSize, afterShrinkWid);
			// g2.fillRect(j*gridSize, i*gridSize, gridSize, gridSize);
		}
		// 结束画出黑色网格线
		List<Map<String, Object>> interedCoeffConfigList = null;
		for (Entry<String, Object> entry : rowColumnToCellMap.entrySet()) {
			GridStorageCells gridStorageCells = (GridStorageCells) entry
					.getValue();
			int rowIndex = gridStorageCells.getRowIndex();
			int colIndex = gridStorageCells.getColIndex();
			List<RenderCellInfo> rendcellLists = gridStorageCells
					.getRenderCellList();
			// double interedCoeff=0;
			// double netStructureIndex=0;
			double cellCoeff = 0;
			String curCodeValue = "";
			for (RenderCellInfo renderInfo : rendcellLists) {
				// 每个网格中的小区的累加指标数据
				// interedCoeff+=renderInfo.getInteredCoeff();
				// netStructureIndex+=renderInfo.getNetStructureIndex();
				switch (Code) {
				case 0:
					curCodeValue = titleToKeys.get(0 + "").toString();
					cellCoeff += renderInfo.getInteredCoeff();
					continue;
				case 1:
					curCodeValue = titleToKeys.get(1 + "").toString();
					cellCoeff += renderInfo.getNetStructureIndex();
					continue;
				default:
					break;
				}
			}
			// 生成不同类型的图片如：被干扰系数，网络结构指标等将将生成的图片按一定的命名规则命名，
			// 并存储在磁盘文件中将所需图片返回客户端渲染至地图
			// 真正的画图开始
			// 获取渲染配置
			if (interedCoeffConfigList == null) {

				interedCoeffConfigList = rnoStructureAnalysisDao
						.getRnoTrafficRendererConfig(curCodeValue);

			}

			for (Map<String, Object> renderMap : interedCoeffConfigList) {
				// MIN_VALUE ,MAX_VALUE,STYLE {color : '#FA07E9'}
				double MIN_VALUE = Double.parseDouble(renderMap
						.get("MIN_VALUE").toString());
				double MAX_VALUE = 100000;
				// log.info("MAX_VALUE:"+renderMap.get("MAX_VALUE")+"--"+renderMap.get("MAX_VALUE")==null);
				if (renderMap.get("MAX_VALUE") != null) {
					MAX_VALUE = Double.parseDouble(renderMap.get("MAX_VALUE")
							.toString());
				}
				String STYLE = renderMap.get("STYLE").toString();
				// log.info("MIN_VALUE,MAX_VALUE,STYLE"+MIN_VALUE+","+MAX_VALUE+","+STYLE);
				GsonBuilder gsonBuilder = new GsonBuilder();
				Gson gson = gsonBuilder.create();
				// RenderColor renderColor=gson.fromJson(STYLE,new
				// TypeToken<RenderColor>(){}.getType());
				RenderColor renderColor = gson.fromJson(STYLE,
						RenderColor.class);
				String colorString = renderColor.getColor().substring(1);
				// String colorString=listObj.get(0).toString().substring(1);
				// java.lang.NumberFormatException: For input string: "0xFA07E9"
				int color = Integer.parseInt(colorString, 16);
				// log.info("colorString,color:"+colorString+","+color);
				if (MIN_VALUE < cellCoeff && cellCoeff <= MAX_VALUE) {
					// 16进制表达方式：0x 0xFA07E9
					g2.setColor(new Color(color));
					break;
				}
			}

			g2.fillRect(colIndex * gridSize, rowIndex * gridSize, gridSize,
					gridSize);
		}
		try {
			// 将生成的图片保存为jpg格式的文件。ImageIO支持jpg、png、gif等格式
			g2.dispose();
			ImageIO.write(image, "png", file);
			log.info("退出drawImage：画图完成" + file);
		} catch (IOException e) {
			log.info("生成图片出错........");
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @title 通过传入不同的Code 生成图像并存储
	 * @param rowColumnToCellMap
	 *            网格行列到小区集合映射
	 * @param taskId
	 *            任务ID
	 * @param dataType
	 *            数据类型 如 cellres
	 * @param afterShrinkLen
	 *            收缩后的画布长度
	 * @param afterShrinkWid
	 *            收缩后的画布宽度
	 * @param gridMeter
	 *            一个网格实际多少米
	 * @param actualAreaLength
	 *            实际矩形区域长度米
	 * @param actualAreaWidth
	 *            实际矩形区域宽度米
	 * @param Code
	 *            0代表BE_INTERFER被干扰系数，1代表NET_STRUCT_FACTOR网络结构指数
	 * @author chao.xj
	 * @date 2014-5-16下午05:56:16
	 * @company 怡创科技
	 * @version 1.2
	 */
	/*
	 * public void drawImage(Map<String, Object> rowColumnToCellMap,long
	 * taskId,String dataType,int afterShrinkLen,int afterShrinkWid,int
	 * gridMeter,double actualAreaLength,double actualAreaWidth,int Code) {
	 * log.info(
	 * "进入drawImageMap<String, Object> rowColumnToCellMap,long taskId,String dataType,int afterShrinkLen,int afterShrinkWid,int gridMeter,double actualAreaLength,int Code"
	 * ); //建立映射关系为了更好的从表中取得该code的颜色 //RNO_TRAFFIC_INDEX_TYPE
	 * RNO_TRAFFIC_RENDERER_CONFIG Map<String, String> titleToKeys = new
	 * HashMap<String, String>(); titleToKeys.put(String.valueOf(0),
	 * "BE_INTERFER"); titleToKeys.put(String.valueOf(1), "NET_STRUCT_FACTOR");
	 * 
	 * String codeString=titleToKeys.get(String.valueOf(Code)).toString();
	 * //从米转换至像素的网格大小gridSize*gridSize int
	 * gridSize=(int)Math.round(1.0f*gridMeter
	 * *RnoStructureServiceImpl.afterShrinkLen/actualAreaLength);//网格大小
	 * 
	 * String folderString="/op/rno/ana_result/image/";
	 * folderString=ServletActionContext
	 * .getServletContext().getRealPath(folderString); File folder=new
	 * File(folderString); if (!folder.exists()&&!folder.isDirectory()) {
	 * log.info("不存在此文件夹需创建folder:"+folderString); folder.mkdir(); } String
	 * pathString="/op/rno/ana_result/image/"+codeString+"_"+taskId+".png";
	 * String realPath = ServletActionContext.getServletContext()
	 * .getRealPath(pathString); log.info("realPath:"+realPath); //File file =
	 * new File("D:/image.png"); File file = new File(realPath);
	 * //1.在内存中建一张图片(得到画布的感觉) BufferedImage image=new
	 * BufferedImage(afterShrinkLen,afterShrinkWid,BufferedImage.TYPE_INT_RGB);
	 * //获取画布的画笔 Graphics2D g2 = (Graphics2D)image.getGraphics(); // ----------
	 * 增加下面的代码使得画布背景透明 ----------------- image =
	 * g2.getDeviceConfiguration().createCompatibleImage
	 * (afterShrinkLen,afterShrinkWid, Transparency.TRANSLUCENT);
	 * //g2.dispose(); g2 = image.createGraphics(); // ---------- 背景透明代码结束
	 * ----------------- //开始画出黑色网格线 g2.setColor(Color.black); for (int i = 0; i
	 * < afterShrinkWid/gridSize+1; i++) { //行： g2.drawLine(0, i*gridSize,
	 * afterShrinkLen, i*gridSize);//行 } for (int j = 0; j <
	 * afterShrinkLen/gridSize+1; j++) { //列 g2.drawLine(j*gridSize,
	 * 0,j*gridSize , afterShrinkWid); //g2.fillRect(j*gridSize, i*gridSize,
	 * gridSize, gridSize); } //结束画出黑色网格线 List<Map<String, Object>>
	 * interedCoeffConfigList=null; int
	 * phylen=(int)Math.round(actualAreaLength); int
	 * phywid=(int)Math.round(actualAreaWidth);
	 * //System.out.println("phylen:"+phylen+"--phywid:"+phywid); //缩放后的画布长度 int
	 * changwidth=phywid*RnoStructureServiceImpl.afterShrinkLen/phylen;
	 * //开始循环行列网格 //映射缩放后画布宽changwidth即为行gridSize网格大小 for (int i = 0; i <
	 * changwidth/gridSize; i++) {
	 * //映射缩放后画布长RnoStructureServiceImpl.afterShrinkLen即为列 for (int j = 0; j <
	 * RnoStructureServiceImpl.afterShrinkLen/gridSize; j++) { boolean
	 * flag=false; for (Entry<String, Object>
	 * entry:rowColumnToCellMap.entrySet()) { GridStorageCells
	 * gridStorageCells=(GridStorageCells)entry.getValue(); int
	 * rowIndex=gridStorageCells.getRowIndex(); int
	 * colIndex=gridStorageCells.getColIndex(); if (i==rowIndex&&j==colIndex) {
	 * flag=true; List<RenderCellInfo>
	 * rendcellLists=gridStorageCells.getRenderCellList(); //double
	 * interedCoeff=0; //double netStructureIndex=0; double cellCoeff=0; String
	 * curCodeValue=""; for (RenderCellInfo renderInfo:rendcellLists) {
	 * //每个网格中的小区的累加指标数据 //interedCoeff+=renderInfo.getInteredCoeff();
	 * //netStructureIndex+=renderInfo.getNetStructureIndex(); switch (Code) {
	 * case 0: curCodeValue=titleToKeys.get(0+"").toString();
	 * cellCoeff+=renderInfo.getInteredCoeff(); continue; case 1:
	 * curCodeValue=titleToKeys.get(1+"").toString();
	 * cellCoeff+=renderInfo.getNetStructureIndex(); continue; default: break; }
	 * } // 生成不同类型的图片如：被干扰系数，网络结构指标等将将生成的图片按一定的命名规则命名，
	 * //并存储在磁盘文件中将所需图片返回客户端渲染至地图 //真正的画图开始 //获取渲染配置 if
	 * (interedCoeffConfigList==null) {
	 * 
	 * interedCoeffConfigList=rnoStructureAnalysisDao.getRnoTrafficRendererConfig
	 * (curCodeValue);
	 * 
	 * }
	 * 
	 * for (Map<String, Object> renderMap:interedCoeffConfigList) { //MIN_VALUE
	 * ,MAX_VALUE,STYLE {color : '#FA07E9'} double
	 * MIN_VALUE=Double.parseDouble(renderMap.get("MIN_VALUE").toString());
	 * double MAX_VALUE=100000;
	 * log.info("MAX_VALUE:"+renderMap.get("MAX_VALUE")+
	 * "--"+renderMap.get("MAX_VALUE")==null); if
	 * (renderMap.get("MAX_VALUE")!=null) {
	 * MAX_VALUE=Double.parseDouble(renderMap.get("MAX_VALUE").toString()); }
	 * String STYLE=renderMap.get("STYLE").toString();
	 * log.info("MIN_VALUE,MAX_VALUE,STYLE"+MIN_VALUE+","+MAX_VALUE+","+STYLE);
	 * GsonBuilder gsonBuilder=new GsonBuilder(); Gson
	 * gson=gsonBuilder.create(); //RenderColor
	 * renderColor=gson.fromJson(STYLE,new
	 * TypeToken<RenderColor>(){}.getType()); RenderColor
	 * renderColor=gson.fromJson(STYLE,RenderColor.class); String
	 * colorString=renderColor.getColor().substring(1); //String
	 * colorString=listObj.get(0).toString().substring(1);
	 * //java.lang.NumberFormatException: For input string: "0xFA07E9" int
	 * color=Integer.parseInt(colorString,16);
	 * log.info("colorString,color:"+colorString+","+color); if
	 * (MIN_VALUE<cellCoeff&&cellCoeff<=MAX_VALUE) { //16进制表达方式：0x 0xFA07E9
	 * g2.setColor(new Color (color)); break; } }
	 * 
	 * g2.fillRect(colIndex*gridSize, rowIndex*gridSize, gridSize, gridSize);
	 * for (RenderCellInfo renderInfo:rendcellLists) {
	 * g2.setColor(Color.black);//new Color(0,255,0)
	 * //System.out.println("renderInfo.getCellX():"
	 * +renderInfo.getCellX()+"---"+renderInfo.getCellY());
	 * g2.fillArc((int)Math.
	 * round(renderInfo.getCellX()),(int)Math.round(renderInfo.getCellY()), 10,
	 * 10, 0, 360);
	 * //g2.drawArc((int)renderInfo.getCellX(),(int)renderInfo.getCellY(), 10,
	 * 10, 0, 360); } break; } } if (!flag) { String curCodeValue=""; switch
	 * (Code) { case 0: curCodeValue=titleToKeys.get(0+"").toString(); case 1:
	 * curCodeValue=titleToKeys.get(1+"").toString(); } if
	 * (interedCoeffConfigList==null) {
	 * interedCoeffConfigList=rnoStructureAnalysisDao
	 * .getRnoTrafficRendererConfig(curCodeValue); } String
	 * STYLE=interedCoeffConfigList.get(0).get("STYLE").toString(); GsonBuilder
	 * gsonBuilder=new GsonBuilder(); Gson gson=gsonBuilder.create();
	 * //RenderColor renderColor=gson.fromJson(STYLE,new
	 * TypeToken<RenderColor>(){}.getType()); RenderColor
	 * renderColor=gson.fromJson(STYLE,RenderColor.class); String
	 * colorString=renderColor.getColor().substring(1); //String
	 * colorString=listObj.get(0).toString().substring(1);
	 * //java.lang.NumberFormatException: For input string: "0xFA07E9" int
	 * color=Integer.parseInt(colorString,16); //16进制表达方式：0x 0xFA07E9
	 * g2.setColor(new Color (color)); g2.fillRect(j*gridSize, i*gridSize,
	 * gridSize, gridSize); } } } //结束循环行列网格 for (Entry<String, Object>
	 * entry:rowColumnToCellMap.entrySet()) { GridStorageCells
	 * gridStorageCells=(GridStorageCells)entry.getValue();
	 * 
	 * List<RenderCellInfo> rendcellLists=gridStorageCells.getRenderCellList();
	 * for (RenderCellInfo renderInfo:rendcellLists) {
	 * g2.setColor(Color.black);//new Color(0,255,0)
	 * //System.out.println("renderInfo.getCellX():"
	 * +renderInfo.getCellX()+"---"+renderInfo.getCellY());
	 * g2.fillArc((int)Math.
	 * round(renderInfo.getCellX()),(int)Math.round(renderInfo.getCellY()), 10,
	 * 10, 0, 360);
	 * //g2.drawArc((int)renderInfo.getCellX(),(int)renderInfo.getCellY(), 10,
	 * 10, 0, 360); } } try { //将生成的图片保存为jpg格式的文件。ImageIO支持jpg、png、gif等格式
	 * g2.dispose(); ImageIO.write(image, "png", file);
	 * log.info("退出drawImage：画图完成"+file); } catch (IOException e) {
	 * log.info("生成图片出错........"); e.printStackTrace(); }
	 * 
	 * 
	 * }
	 */
	/**
	 * 
	 * @title 通过传入不同的Code 生成图像并存储
	 * @param rowColumnToCellMap
	 *            网格行列到小区集合映射
	 * @param taskId
	 *            任务ID
	 * @param dataType
	 *            数据类型 如 cellres
	 * @param afterShrinkLen
	 *            收缩后的画布长度
	 * @param afterShrinkWid
	 *            收缩后的画布宽度
	 * @param gridMeter
	 *            一个网格实际多少米
	 * @param actualAreaLength
	 *            实际矩形区域长度米
	 * @param actualAreaWidth
	 *            实际矩形区域宽度米
	 * @param Code
	 *            0代表BE_INTERFER被干扰系数，1代表NET_STRUCT_FACTOR网络结构指数
	 * @author chao.xj
	 * @date 2014-5-16下午05:56:16
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void drawImage(Map<String, Object> rowColumnToCellMap, long taskId,
			String dataType, int afterShrinkLen, int afterShrinkWid,
			int gridMeter, double actualAreaLength, double actualAreaWidth,
			int Code) {
		log.info("进入drawImageMap<String, Object> rowColumnToCellMap,long taskId,String dataType,int afterShrinkLen,int afterShrinkWid,int gridMeter,double actualAreaLength,int Code");
		// 建立映射关系为了更好的从表中取得该code的颜色
		// RNO_TRAFFIC_INDEX_TYPE RNO_TRAFFIC_RENDERER_CONFIG
		Map<String, String> titleToKeys = new HashMap<String, String>();
		titleToKeys.put(String.valueOf(0), "BE_INTERFER");
		titleToKeys.put(String.valueOf(1), "NET_STRUCT_FACTOR");

		String codeString = titleToKeys.get(String.valueOf(Code)).toString();
		// 从米转换至像素的网格大小gridSize*gridSize
		int gridSize = (int) Math.round(1.0f * gridMeter
				* RnoStructureServiceImpl.afterShrinkLen / actualAreaLength);// 网格大小

		String folderString = "/op/rno/ana_result/image/";
		folderString = ServletActionContext.getServletContext().getRealPath(
				folderString);
		File folder = new File(folderString);
		if (!folder.exists() && !folder.isDirectory()) {
			log.info("不存在此文件夹需创建folder:" + folderString);
			folder.mkdir();
		}
		String pathString = "/op/rno/ana_result/image/" + codeString + "_"
				+ taskId + ".png";
		String realPath = ServletActionContext.getServletContext().getRealPath(
				pathString);
		log.info("realPath:" + realPath);
		// File file = new File("D:/image.png");
		File file = new File(realPath);
		// 1.在内存中建一张图片(得到画布的感觉)
		BufferedImage image = new BufferedImage(afterShrinkLen, afterShrinkWid,
				BufferedImage.TYPE_INT_RGB);
		// 获取画布的画笔
		Graphics2D g2 = (Graphics2D) image.getGraphics();
		// ---------- 增加下面的代码使得画布背景透明 -----------------
		image = g2.getDeviceConfiguration().createCompatibleImage(
				afterShrinkLen, afterShrinkWid, Transparency.TRANSLUCENT);
		// g2.dispose();
		g2 = image.createGraphics();
		// ---------- 背景透明代码结束 -----------------
		// 开始画出黑色网格线
		/*
		 * g2.setColor(Color.black); for (int i = 0; i <
		 * afterShrinkWid/gridSize+1; i++) { //行： g2.drawLine(0, i*gridSize,
		 * afterShrinkLen, i*gridSize);//行 } for (int j = 0; j <
		 * afterShrinkLen/gridSize+1; j++) { //列 g2.drawLine(j*gridSize,
		 * 0,j*gridSize , afterShrinkWid); //g2.fillRect(j*gridSize, i*gridSize,
		 * gridSize, gridSize); }
		 */
		// 结束画出黑色网格线
		List<Map<String, Object>> interedCoeffConfigList = null;
		int phylen = (int) Math.round(actualAreaLength);
		int phywid = (int) Math.round(actualAreaWidth);
		// System.out.println("phylen:"+phylen+"--phywid:"+phywid);
		// 缩放后的画布长度
		int changwidth = phywid * RnoStructureServiceImpl.afterShrinkLen
				/ phylen;
		// 开始循环行列网格
		// 映射缩放后画布宽changwidth即为行gridSize网格大小
		for (int i = 0; i < changwidth / gridSize; i++) {
			// 映射缩放后画布长RnoStructureServiceImpl.afterShrinkLen即为列
			for (int j = 0; j < RnoStructureServiceImpl.afterShrinkLen
					/ gridSize; j++) {
				String curCodeValue = "";
				switch (Code) {
				case 0:
					curCodeValue = titleToKeys.get(0 + "").toString();
					break;
				case 1:
					curCodeValue = titleToKeys.get(1 + "").toString();
					break;
				default:
					continue;
				}
				if (interedCoeffConfigList == null) {
					interedCoeffConfigList = rnoStructureAnalysisDao
							.getRnoTrafficRendererConfig(curCodeValue);
				}
				String STYLE = interedCoeffConfigList.get(0).get("STYLE")
						.toString();
				GsonBuilder gsonBuilder = new GsonBuilder();
				Gson gson = gsonBuilder.create();
				// RenderColor renderColor=gson.fromJson(STYLE,new
				// TypeToken<RenderColor>(){}.getType());
				RenderColor renderColor = gson.fromJson(STYLE,
						RenderColor.class);
				String colorString = renderColor.getColor().substring(1);
				// String colorString=listObj.get(0).toString().substring(1);
				// java.lang.NumberFormatException: For input string: "0xFA07E9"
				int color = Integer.parseInt(colorString, 16);
				// 16进制表达方式：0x 0xFA07E9
				g2.setColor(new Color(color));
				g2.fillRect(j * gridSize, i * gridSize, gridSize, gridSize);
			}
		}
		// 结束循环行列网格
		for (Entry<String, Object> entry : rowColumnToCellMap.entrySet()) {
			GridStorageCells gridStorageCells = (GridStorageCells) entry
					.getValue();
			int rowIndex = gridStorageCells.getRowIndex();
			int colIndex = gridStorageCells.getColIndex();
			List<RenderCellInfo> rendcellLists = gridStorageCells
					.getRenderCellList();
			// double interedCoeff=0;
			// double netStructureIndex=0;
			double cellCoeff = 0;
			double count = 0;
			String curCodeValue = "";
			// int i=0;
			for (RenderCellInfo renderInfo : rendcellLists) {
				// i++;
				// 每个网格中的小区的累加指标数据
				// interedCoeff+=renderInfo.getInteredCoeff();
				// netStructureIndex+=renderInfo.getNetStructureIndex();
				// g2.setColor(Color.red);
				log.info("renderInfo.getCell():" + renderInfo.getCell());

				switch (Code) {
				case 0:
					curCodeValue = titleToKeys.get(0 + "").toString();
					cellCoeff += renderInfo.getInteredCoeff();
					// System.out.println(renderInfo.getCell()+"的"+curCodeValue+"影响的行,列:"+gridStorageCells.getRowIndex()+","+gridStorageCells.getColIndex()+"----小区坐标X,Y:"+(int)Math.round(renderInfo.getCellX())+","+(int)Math.round(renderInfo.getCellY())+"---网格大小:"+gridSize+"--系数:"+cellCoeff);
					count++;
					continue;
				case 1:
					curCodeValue = titleToKeys.get(1 + "").toString();
					cellCoeff += renderInfo.getNetStructureIndex();
					// System.out.println(renderInfo.getCell()+"的"+curCodeValue+"影响的行,列:"+gridStorageCells.getRowIndex()+","+gridStorageCells.getColIndex()+"----小区坐标X,Y:"+(int)Math.round(renderInfo.getCellX())+","+(int)Math.round(renderInfo.getCellY())+"---网格大小:"+gridSize+"--系数:"+cellCoeff);
					count++;
					continue;
				default:
					break;
				}
			}
			// 生成不同类型的图片如：被干扰系数，网络结构指标等将将生成的图片按一定的命名规则命名，
			// 并存储在磁盘文件中将所需图片返回客户端渲染至地图
			// 真正的画图开始
			// 获取渲染配置
			if (interedCoeffConfigList == null) {

				interedCoeffConfigList = rnoStructureAnalysisDao
						.getRnoTrafficRendererConfig(curCodeValue);

			}

			for (Map<String, Object> renderMap : interedCoeffConfigList) {
				// MIN_VALUE ,MAX_VALUE,STYLE {color : '#FA07E9'}
				double MIN_VALUE = Double.parseDouble(renderMap
						.get("MIN_VALUE").toString());
				double MAX_VALUE = 100000;
				// log.info("MAX_VALUE:"+renderMap.get("MAX_VALUE")+"--"+renderMap.get("MAX_VALUE")==null);
				if (renderMap.get("MAX_VALUE") != null) {
					MAX_VALUE = Double.parseDouble(renderMap.get("MAX_VALUE")
							.toString());
				}
				String STYLE = renderMap.get("STYLE").toString();
				// log.info("MIN_VALUE,MAX_VALUE,STYLE"+MIN_VALUE+","+MAX_VALUE+","+STYLE);
				GsonBuilder gsonBuilder = new GsonBuilder();
				Gson gson = gsonBuilder.create();
				// RenderColor renderColor=gson.fromJson(STYLE,new
				// TypeToken<RenderColor>(){}.getType());
				RenderColor renderColor = gson.fromJson(STYLE,
						RenderColor.class);
				String colorString = renderColor.getColor().substring(1);
				// String colorString=listObj.get(0).toString().substring(1);
				// java.lang.NumberFormatException: For input string: "0xFA07E9"
				// int color=Integer.parseInt(colorString,16);//改进渐变颜色
				// Color.getColor("").get
				// log.info("colorString,color,count:"+colorString+","+color+","+count);
				if (MIN_VALUE < cellCoeff / count
						&& cellCoeff / count <= MAX_VALUE) {
					// 16进制表达方式：0x 0xFA07E9
					String colorstr = getHexColor(colorString, MAX_VALUE,
							MIN_VALUE, cellCoeff / count);
					log.info("colorString:" + colorString);
					int color = Integer.parseInt(colorstr, 16);
					g2.setColor(new Color(color));
					break;
				}
			}

			g2.fillRect(colIndex * gridSize, rowIndex * gridSize, gridSize,
					gridSize);
			// break;
		}

		/*
		 * for (Entry<String, Object> entry:rowColumnToCellMap.entrySet()) {
		 * GridStorageCells gridStorageCells=(GridStorageCells)entry.getValue();
		 * 
		 * List<RenderCellInfo>
		 * rendcellLists=gridStorageCells.getRenderCellList(); for
		 * (RenderCellInfo renderInfo:rendcellLists) {
		 * //System.out.println("renderInfo.getCell()"+renderInfo.getCell()); if
		 * (renderInfo.getCell().equals("S9BCBS1")) { g2.setColor(Color.red);
		 * g2.fillRect(gridStorageCells.getColIndex()*gridSize,
		 * gridStorageCells.getRowIndex()*gridSize, gridSize, gridSize);
		 * //g2.drawArc((int)renderInfo.getCellX(),(int)renderInfo.getCellY(),
		 * 10, 10, 0, 360);
		 * System.out.println(renderInfo.getCell()+"影响的行,列:"+gridStorageCells
		 * .getRowIndex
		 * ()+","+gridStorageCells.getColIndex()+"----小区坐标X,Y:"+(int)
		 * Math.round(renderInfo
		 * .getCellX())+","+(int)Math.round(renderInfo.getCellY
		 * ())+"---网格大小:"+gridSize); g2.setColor(Color.green);
		 * 
		 * g2.fillArc((int)Math.round(renderInfo.getCellX()),(int)Math.round(
		 * renderInfo.getCellY()), 2, 2, 0, 360); }
		 * //g2.setColor(Color.black);//new Color(0,255,0)
		 * //System.out.println("renderInfo.getCellX():"
		 * +renderInfo.getCellX()+"---"+renderInfo.getCellY());
		 * //g2.fillArc((int
		 * )Math.round(renderInfo.getCellX()),(int)Math.round(renderInfo
		 * .getCellY()), 2, 2, 0, 360); //g2.setFont(new
		 * Font("宋体",Font.PLAIN,12)); //g2.drawString(renderInfo.getCell(),
		 * (int)
		 * Math.round(renderInfo.getCellX()),(int)Math.round(renderInfo.getCellY
		 * ()));
		 * //g2.drawArc((int)renderInfo.getCellX(),(int)renderInfo.getCellY(),
		 * 10, 10, 0, 360); } }
		 */

		try {
			// 将生成的图片保存为jpg格式的文件。ImageIO支持jpg、png、gif等格式
			g2.dispose();
			ImageIO.write(image, "png", file);
			log.info("退出drawImage：画图完成" + file);
		} catch (IOException e) {
			log.info("生成图片出错........");
			e.printStackTrace();
		}

	}

	/**
	 * 查询指定的ncs desc的状态
	 * 
	 * @param ncsIds
	 * @return
	 * @author brightming 2014-5-21 下午5:26:15
	 */
	public List<Map<String, Object>> queryNcsDescStatus(String ncsIds) {
		return rnoStructureQueryDao.queryNcsDescStatus(ncsIds);
	}

	/**
	 * 
	 * @title 通过传入不同的Code 生成图像并存储
	 * @param rowColumnToCellMap
	 *            网格行列到小区集合映射
	 * @param taskId
	 *            任务ID
	 * @param dataType
	 *            数据类型 如 cellres
	 * @param afterShrinkLen
	 *            收缩后的画布长度
	 * @param afterShrinkWid
	 *            收缩后的画布宽度
	 * @param gridMeter
	 *            一个网格实际多少米
	 * @param actualAreaLength
	 *            实际矩形区域长度米
	 * @param actualAreaWidth
	 *            实际矩形区域宽度米
	 * @param Code
	 *            0代表BE_INTERFER被干扰系数，1代表NET_STRUCT_FACTOR网络结构指数
	 * @param polygonList
	 *            多边形集合
	 * @author chao.xj
	 * @date 2014-5-16下午05:56:16
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void drawImage(Map<String, Object> rowColumnToCellMap, long taskId,
			String dataType, int afterShrinkLen, int afterShrinkWid,
			int gridMeter, double actualAreaLength, double actualAreaWidth,
			int Code, List<Polygon> polygonList) {
		log.info("进入drawImageMap<String, Object> rowColumnToCellMap,long taskId,String dataType,int afterShrinkLen,int afterShrinkWid,int gridMeter,double actualAreaLength,int Code");
		// 建立映射关系为了更好的从表中取得该code的颜色
		// RNO_TRAFFIC_INDEX_TYPE RNO_TRAFFIC_RENDERER_CONFIG
		Map<String, String> titleToKeys = new HashMap<String, String>();
		titleToKeys.put(String.valueOf(0), "BE_INTERFER");
		titleToKeys.put(String.valueOf(1), "NET_STRUCT_FACTOR");

		String codeString = titleToKeys.get(String.valueOf(Code)).toString();
		// 从米转换至像素的网格大小gridSize*gridSize
		int gridSize = (int) Math.round(1.0f * gridMeter
				* RnoStructureServiceImpl.afterShrinkLen / actualAreaLength);// 网格大小

		String folderString = "/op/rno/ana_result/image/";
		folderString = ServletActionContext.getServletContext().getRealPath(
				folderString);
		File folder = new File(folderString);
		if (!folder.exists() && !folder.isDirectory()) {
			log.info("不存在此文件夹需创建folder:" + folderString);
			folder.mkdir();
		}
		String pathString = "/op/rno/ana_result/image/" + codeString + "_"
				+ taskId + ".png";
		String realPath = ServletActionContext.getServletContext().getRealPath(
				pathString);
		log.info("realPath:" + realPath);
		// File file = new File("D:/image.png");
		File file = new File(realPath);
		if (file.exists()) {
			return;
		}
		// 1.在内存中建一张图片(得到画布的感觉)
		BufferedImage image = new BufferedImage(afterShrinkLen, afterShrinkWid,
				BufferedImage.TYPE_INT_RGB);
		// 获取画布的画笔
		Graphics2D g2 = (Graphics2D) image.getGraphics();
		// ---------- 增加下面的代码使得画布背景透明 -----------------
		image = g2.getDeviceConfiguration().createCompatibleImage(
				afterShrinkLen, afterShrinkWid, Transparency.TRANSLUCENT);
		// g2.dispose();
		g2 = image.createGraphics();
		// ---------- 背景透明代码结束 -----------------

		List<Map<String, Object>> interedCoeffConfigList = null;
		int phylen = (int) Math.round(actualAreaLength);
		int phywid = (int) Math.round(actualAreaWidth);
		// System.out.println("phylen:"+phylen+"--phywid:"+phywid);
		// 缩放后的画布长度
		int changwidth = phywid * RnoStructureServiceImpl.afterShrinkLen
				/ phylen;
		// 开始循环行列网格
		// 映射缩放后画布宽changwidth即为行gridSize网格大小
		String CodeValue = "";
		switch (Code) {
		case 0:
			CodeValue = titleToKeys.get(0 + "").toString();
			break;
		case 1:
			CodeValue = titleToKeys.get(1 + "").toString();
			break;
		default:
			break;
		}
		if (interedCoeffConfigList == null) {
			interedCoeffConfigList = rnoStructureAnalysisDao
					.getRnoTrafficRendererConfig(CodeValue);
		}
		String boundSTYLE = "{color : '#FFFFFF'}";
		double intelval = 100;
		for (Map<String, Object> renderMap : interedCoeffConfigList) {

			double MIN_VALUE = Double.parseDouble(renderMap.get("MIN_VALUE")
					.toString());
			if (intelval > MIN_VALUE) {
				intelval = MIN_VALUE;
				boundSTYLE = renderMap.get("STYLE").toString();
			}
		}
		// String
		// boundSTYLE=interedCoeffConfigList.get(0).get("STYLE").toString();
		GsonBuilder boundgsonBuilder = new GsonBuilder();
		Gson boundgson = boundgsonBuilder.create();
		// RenderColor renderColor=gson.fromJson(STYLE,new
		// TypeToken<RenderColor>(){}.getType());
		RenderColor boundrenderColor = boundgson.fromJson(boundSTYLE,
				RenderColor.class);
		String boundcolorString = boundrenderColor.getColor().substring(1);
		// String colorString=listObj.get(0).toString().substring(1);
		// java.lang.NumberFormatException: For input string: "0xFA07E9"
		int boundcolor = Integer.parseInt(boundcolorString, 16);
		// 16进制表达方式：0x 0xFA07E9
		g2.setColor(new Color(boundcolor));
		// g2.setColor(Color.red);
		for (Polygon polygon : polygonList) {

			/*
			 * for (int i = 0; i < polygon.xpoints.length; i++) {
			 * log.info("polygon.xpoints["+i+"]:"+polygon.xpoints[i]); } for
			 * (int k = 0; k < polygon.ypoints.length; k++) {
			 * log.info("polygon.ypoints["+k+"]:"+polygon.ypoints[k]); }
			 */
			g2.fillPolygon(polygon);
		}
		// 结束循环行列网格
		for (Entry<String, Object> entry : rowColumnToCellMap.entrySet()) {
			GridStorageCells gridStorageCells = (GridStorageCells) entry
					.getValue();
			int rowIndex = gridStorageCells.getRowIndex();
			int colIndex = gridStorageCells.getColIndex();
			List<RenderCellInfo> rendcellLists = gridStorageCells
					.getRenderCellList();
			// double interedCoeff=0;
			// double netStructureIndex=0;
			double cellCoeff = 0;
			double count = 0;
			String curCodeValue = "";
			// int i=0;
			for (RenderCellInfo renderInfo : rendcellLists) {
				// i++;
				// 每个网格中的小区的累加指标数据
				// interedCoeff+=renderInfo.getInteredCoeff();
				// netStructureIndex+=renderInfo.getNetStructureIndex();
				// g2.setColor(Color.red);
				// log.info("renderInfo.getCell():"+renderInfo.getCell());

				switch (Code) {
				case 0:
					curCodeValue = titleToKeys.get(0 + "").toString();
					cellCoeff += renderInfo.getInteredCoeff();
					// System.out.println(renderInfo.getCell()+"的"+curCodeValue+"影响的行,列:"+gridStorageCells.getRowIndex()+","+gridStorageCells.getColIndex()+"----小区坐标X,Y:"+(int)Math.round(renderInfo.getCellX())+","+(int)Math.round(renderInfo.getCellY())+"---网格大小:"+gridSize+"--系数:"+cellCoeff);
					count++;
					continue;
				case 1:
					curCodeValue = titleToKeys.get(1 + "").toString();
					cellCoeff += renderInfo.getNetStructureIndex();
					// System.out.println(renderInfo.getCell()+"的"+curCodeValue+"影响的行,列:"+gridStorageCells.getRowIndex()+","+gridStorageCells.getColIndex()+"----小区坐标X,Y:"+(int)Math.round(renderInfo.getCellX())+","+(int)Math.round(renderInfo.getCellY())+"---网格大小:"+gridSize+"--系数:"+cellCoeff);
					count++;
					continue;
				default:
					break;
				}
			}
			// 生成不同类型的图片如：被干扰系数，网络结构指标等将将生成的图片按一定的命名规则命名，
			// 并存储在磁盘文件中将所需图片返回客户端渲染至地图
			// 真正的画图开始
			// 获取渲染配置
			if (interedCoeffConfigList == null) {

				interedCoeffConfigList = rnoStructureAnalysisDao
						.getRnoTrafficRendererConfig(curCodeValue);

			}

			for (Map<String, Object> renderMap : interedCoeffConfigList) {
				// MIN_VALUE ,MAX_VALUE,STYLE {color : '#FA07E9'}
				double MIN_VALUE = Double.parseDouble(renderMap
						.get("MIN_VALUE").toString());
				double MAX_VALUE = 100000;
				// log.info("MAX_VALUE:"+renderMap.get("MAX_VALUE")+"--"+renderMap.get("MAX_VALUE")==null);
				if (renderMap.get("MAX_VALUE") != null) {
					MAX_VALUE = Double.parseDouble(renderMap.get("MAX_VALUE")
							.toString());
				}
				String STYLE = renderMap.get("STYLE").toString();
				// log.info("MIN_VALUE,MAX_VALUE,STYLE"+MIN_VALUE+","+MAX_VALUE+","+STYLE);
				GsonBuilder gsonBuilder = new GsonBuilder();
				Gson gson = gsonBuilder.create();
				// RenderColor renderColor=gson.fromJson(STYLE,new
				// TypeToken<RenderColor>(){}.getType());
				RenderColor renderColor = gson.fromJson(STYLE,
						RenderColor.class);
				String colorString = renderColor.getColor().substring(1);
				// String colorString=listObj.get(0).toString().substring(1);
				// java.lang.NumberFormatException: For input string: "0xFA07E9"
				// int color=Integer.parseInt(colorString,16);//改进渐变颜色
				// Color.getColor("").get
				// log.info("colorString,color,count:"+colorString+","+color+","+count);
				if (MIN_VALUE < cellCoeff / count
						&& cellCoeff / count <= MAX_VALUE) {
					// 16进制表达方式：0x 0xFA07E9
					String colorstr = getHexColor(colorString, MAX_VALUE,
							MIN_VALUE, cellCoeff / count);
					// log.info("colorString:"+colorString);
					int color = Integer.parseInt(colorstr, 16);
					g2.setColor(new Color(color));
					break;
				}
			}

			g2.fillRect(colIndex * gridSize, rowIndex * gridSize, gridSize,
					gridSize);
			// break;
		}

		/*
		 * for (Entry<String, Object> entry:rowColumnToCellMap.entrySet()) {
		 * GridStorageCells gridStorageCells=(GridStorageCells)entry.getValue();
		 * 
		 * List<RenderCellInfo>
		 * rendcellLists=gridStorageCells.getRenderCellList(); for
		 * (RenderCellInfo renderInfo:rendcellLists) {
		 * //System.out.println("renderInfo.getCell()"+renderInfo.getCell()); if
		 * (renderInfo.getCell().equals("S9BCBS1")) { g2.setColor(Color.red);
		 * g2.fillRect(gridStorageCells.getColIndex()*gridSize,
		 * gridStorageCells.getRowIndex()*gridSize, gridSize, gridSize);
		 * //g2.drawArc((int)renderInfo.getCellX(),(int)renderInfo.getCellY(),
		 * 10, 10, 0, 360);
		 * System.out.println(renderInfo.getCell()+"影响的行,列:"+gridStorageCells
		 * .getRowIndex
		 * ()+","+gridStorageCells.getColIndex()+"----小区坐标X,Y:"+(int)
		 * Math.round(renderInfo
		 * .getCellX())+","+(int)Math.round(renderInfo.getCellY
		 * ())+"---网格大小:"+gridSize); g2.setColor(Color.green);
		 * 
		 * g2.fillArc((int)Math.round(renderInfo.getCellX()),(int)Math.round(
		 * renderInfo.getCellY()), 2, 2, 0, 360); }
		 * //g2.setColor(Color.black);//new Color(0,255,0)
		 * //System.out.println("renderInfo.getCellX():"
		 * +renderInfo.getCellX()+"---"+renderInfo.getCellY());
		 * //g2.fillArc((int
		 * )Math.round(renderInfo.getCellX()),(int)Math.round(renderInfo
		 * .getCellY()), 2, 2, 0, 360); //g2.setFont(new
		 * Font("宋体",Font.PLAIN,12)); //g2.drawString(renderInfo.getCell(),
		 * (int)
		 * Math.round(renderInfo.getCellX()),(int)Math.round(renderInfo.getCellY
		 * ()));
		 * //g2.drawArc((int)renderInfo.getCellX(),(int)renderInfo.getCellY(),
		 * 10, 10, 0, 360); } }
		 */

		try {
			// 将生成的图片保存为jpg格式的文件。ImageIO支持jpg、png、gif等格式
			g2.dispose();
			ImageIO.write(image, "png", file);
			log.info("退出drawImage：画图完成" + file);
		} catch (IOException e) {
			log.info("生成图片出错........");
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @title 获取十六进制渐变颜色字符串
	 * @param hexcolor
	 *            363eeb
	 * @param MAX_VALUE
	 *            0.08 颜色值区间
	 * @param MIN_VALUE
	 *            0.06 颜色值区间
	 * @param cellCoeff
	 *            0.065 区间点
	 * @return 在此区间点的十六进制颜色字符串
	 * @author chao.xj
	 * @date 2014-5-24下午04:08:32
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String getHexColor(String hexcolor, double MAX_VALUE,
			double MIN_VALUE, double cellCoeff) {
		// log.info("进入getHexColor(String hexcolor, double MAX_VALUE,double MIN_VALUE, double cellCoeff)"+hexcolor+","+MAX_VALUE+","+MIN_VALUE+","+cellCoeff);
		Color color = Color.ORANGE;
		try {
			color = new Color(Integer.parseInt(hexcolor, 16));
		} catch (NumberFormatException e) {
			// codes to deal with this exception
			e.printStackTrace();
		}
		// 只要把rgb等比例向255靠拢，颜色就会逐渐变淡
		// 看离最大值有多远
		double gradientRange = MAX_VALUE - MIN_VALUE;// 总的梯度区间
		// 离最大值越近则RGB等比例向255靠拢的越小
		// 离最大值越远则RGB等比例向255靠拢的越大
		double differToMax = MAX_VALUE - cellCoeff;// 与最大值相差
		// double differToMax=cellCoeff-MIN_VALUE;//与最小值相差
		double weightRatio = differToMax / gradientRange;// 权重比:所占权重越大则离最大值越远
		// System.out.println("weightRatio:"+weightRatio);
		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();
		double redWeightVal = (255 - red) * weightRatio;
		double greWeightVal = (255 - green) * weightRatio;
		double bluWeightVal = (255 - blue) * weightRatio;
		red = red + (int) Math.round(redWeightVal);
		green = green + (int) Math.round(greWeightVal);
		blue = blue + (int) Math.round(bluWeightVal);
		StringBuffer sb = new StringBuffer();
		sb.append(Integer.toHexString(red));
		sb.append(Integer.toHexString(green));
		sb.append(Integer.toHexString(blue));
		// log.info("退出getHexColor"+sb.toString());
		return sb.toString();
	}

	/**
	 * 
	 * @title 获取边界多边形
	 * @param leftTLng
	 * @param leftTLat
	 * @param rightBLng
	 * @param rightBLat
	 * @param afterShrinkLen
	 * @param allBoundary
	 * @return
	 * @author chao.xj
	 * @date 2014-5-26下午04:53:36
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Polygon> getBoundPolygon(double leftTLng, double leftTLat,
			double rightBLng, double rightBLat, int afterShrinkLen,
			String allBoundary) {
		log.info("进入getBoundPolygon(double leftTLng,double leftTLat,double rightBLng,double rightBLat,double boundLng,double boundLat,int afterShrinkLen,String allBoundary)");
		List<Polygon> polygonList = new ArrayList<Polygon>();
		String[] lnglats = allBoundary.split("#");// 133,33;144,23||122,22;133,45
		for (int i = 0; i < lnglats.length; i++) {
			String[] lnglat = lnglats[i].split(";");
			Polygon polygon = new Polygon();
			for (int j = 0; j < lnglat.length; j++) {
				log.info("lnglat.length:" + lnglat.length);
				String[] lng_lat = lnglat[j].split(",");
				// 增加point点
				double[] afterShrinkXY = getRelaCoordinateForMappingCanvas(
						leftTLng, leftTLat, rightBLng, rightBLat,
						Double.parseDouble(lng_lat[0]),
						Double.parseDouble(lng_lat[1]),
						RnoStructureServiceImpl.afterShrinkLen);
				polygon.addPoint((int) Math.round(afterShrinkXY[0]),
						(int) Math.round(afterShrinkXY[1]));
			}
			// 画一次
			polygonList.add(polygon);
		}
		log.info("退出getBoundPolygon polygonList：" + polygonList.size());
		return polygonList;
	}

	/**
	 * 根据taskId停止对应的ncs分析任务
	 * 
	 * @author peng.jm 2014年6月20日14:14:37
	 */
	public Map<String, Object> stopNcsAnalysisTask(long taskId) {
		Map<String, Object> result = new HashMap<String, Object>();
		long threadId = ExecutorManager.getThreadIdByTaskId(taskId);
		if (threadId != -1) {
			try {
				ExecutorManager.getThread(threadId).interrupt();
				result.put("flag", true);
				result.put("msg", "成功取消分析任务");
				// 同时删除已记录的任务线程信息
				ExecutorManager.deleteTaskThreadInfoByTaskId(taskId);
			} catch (NullPointerException e) {
				log.error("任务已经输出计算结果或者无法找到");
				result.put("flag", false);
				result.put("msg", "任务已经输出计算结果或者无法找到");
			}
		} else {
			log.info("任务状态已经过时，所以无法停止");
			result.put("flag", false);
			result.put("msg", "任务状态已经过时，所以无法停止");
		}
		return result;
	}

	/**
	 * 检查是否存在已保存的ncs分析任务
	 * 
	 * @author peng.jm 2014年6月23日9:49:58
	 */
	public Map<String, Object> checkNcsTaskByNcsIds(String ncsIds) {
		// 对ncsIds列进行排序
		String ncsIdsStr = sortNcsIds(ncsIds);
		// 获取对应的taskId
		List<Map<String, Object>> resList = rnoTaskDao
				.checkNcsTaskByNcsIds(ncsIdsStr);

		boolean flag = false;
		Map<String, Object> res = new HashMap<String, Object>();
		if (resList.size() > 0) {
			flag = true;
			// long taskId =
			// Long.parseLong(resList.get(0).get("TASK_ID").toString());
			// 通过taskId获取task任务状态
			// RnoTask task = rnoTaskDao.getTaskById(taskId);
			// String taskGoingStatus = task.getTaskGoingStatus();

			res.put("flag", flag);
			// res.put("oldNcsTaskId", taskId);
			// res.put("taskGoingStatus", taskGoingStatus);
		} else {
			flag = false;
			res.put("flag", flag);
		}
		return res;
	}

	/**
	 * 通过任务id获取已成功完成的ncs分析任务
	 * 
	 * @author peng.jm 2014年6月23日17:48:21
	 */
	/*
	 * public Map<String, Object> queryOldNcsTaskByTaskId(long taskId) {
	 * Map<String, Object> result = new HashMap<String, Object>();
	 * List<Map<String, Object>> res = rnoStructureQueryDao
	 * .queryOldNcsTaskByTaskId(taskId); if(res.size() > 0) { result =
	 * res.get(0); } return result; }
	 */

	/**
	 * 对ncsIds进行升序排序
	 * 
	 * @param ncsIds
	 * @author peng.jm
	 * @return
	 */
	public String sortNcsIds(String ncsIds) {
		// 对ncsIds列进行排序
		String[] ncss = ncsIds.split(",");
		List<Long> ncsList = new ArrayList<Long>();
		for (String ncs : ncss) {
			long ncsLong = Long.parseLong(ncs);
			ncsList.add(ncsLong);
		}
		Collections.sort(ncsList); // 升序

		// 转成String
		StringBuffer str = new StringBuffer();
		// for (Long mrr : mrrList) {
		// str.append(mrr+",");
		// }

		// 2014-8-10修改为1 to 100这种格式
		if (ncsList.size() >= 2) {
			str.append(ncsList.get(0)).append(" to ")
					.append(ncsList.get(ncsList.size() - 1));
		} else if (ncsList.size() == 1) {
			str.append(ncsList.get(0));
		} else {
			str.append("-1");
		}

		return str.toString();
	}

	/**
	 * 通过NcsIds获取已存在的ncs分析任务
	 * 
	 * @author peng.jm
	 */
	public List<Map<String, Object>> queryOldNcsTaskByNcsIds(String ncsIds) {
		// 排序
		String ncsIdsStr = sortNcsIds(ncsIds);
		List<Map<String, Object>> res = rnoStructureQueryDao
				.queryOldNcsTaskByNcsIds(ncsIdsStr);
		return res;
	}

	/**
	 * 
	 * @title 获取小区干扰矩阵数据详情信息
	 * @param realpath
	 * @param cellName
	 * @return
	 * @author chao.xj
	 * @date 2014-6-30上午10:34:31
	 * @company 怡创科技
	 * @version 1.2
	 */
	public CellInterWithDetailInfo getCellInterDetailInfo(String realpath,
			String cellName) {
		return rnoStructureAnalysisDao.getCellInterDetailInfo(realpath,
				cellName);
	}

	/**
	 * 检查是否存在对应的渲染图，不存在则生成该参数的渲染图
	 * 
	 * @author peng.jm 2014年7月9日12:16:03
	 */
	public Map<String, Object> getRenderImgByParamAndTaskId(
			String ncsRendererType, long taskId, String filePath) {
		Map<String, Object> res = new HashMap<String, Object>();
		log.debug("ncsRendererType=" + ncsRendererType + ", taskId=" + taskId);
		res = rnoRenderer.getRenderImg(ncsRendererType, taskId, filePath);
		return res;
	}

	/**
	 * 获取指标参数对应的颜色渲染规则
	 * 
	 * @param ncsRendererType
	 *            指标参数
	 * @param taskId
	 * @param filePath
	 * @return
	 * @author peng.jm
	 */
	public List<Map<String, Object>> getRenderColorRule(String ncsRendererType,
			long taskId, String filePath) {
		log.debug("ncsRendererType=" + ncsRendererType + ", taskId=" + taskId);

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		// 获取cellres数据
		List<Map<String, Object>> cellres = new ArrayList<Map<String, Object>>();
		cellres = rnoRenderer.getRoFileDataByTaskId(taskId, filePath,
				"cellres", ncsRendererType);
		if (cellres.size() <= 0) {
			log.error("获取ro文件数据出错！");
			return result;
		}
		Map<String, Object> oneCellres = null;
		// 获取对应的指标值
		List<Double> vals = new ArrayList<Double>();
		double val = 0;
		String strVal = "";
		for (int i = 0; i < cellres.size(); i++) {
			oneCellres = cellres.get(i);
			strVal = oneCellres.get(ncsRendererType) == null ? "" : oneCellres
					.get(ncsRendererType).toString();                                
			strVal = strVal.trim();
			if ("".equals(strVal)) {
				continue;
			}
			try {
				// 获取对应的样本数据值
				val = Double.parseDouble(oneCellres.get(ncsRendererType)
						.toString());
				vals.add(val);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 通过指标值获取渲染颜色配置
		result = rnoRenderer.getColorConfigList(vals);
		if (result.size() <= 0) {
			log.error(ncsRendererType + ":渲染规则不存在！");
			return result;
		}
		return result;
	}

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
	public List<Map<String, Object>> queryNcsOrMrrTimeSpanByIds(String ids,
			String ncsOrMrrFlag) {
		return rnoStructureQueryDao.queryNcsOrMrrTimeSpanByIds(ids,
				ncsOrMrrFlag);
	}

	/**
	 * 
	 * @title 查询mrr记录的情况
	 * @param condition
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2014-7-23上午9:33:07
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryMrrDescriptorByPage(
			Map<String, String> condition, Page page) {

		log.info("进入方法：queryMrrDescriptorByPage。condition=" + condition
				+ ",page=" + page);
		if (page == null) {
			log.warn("方法：queryMrrDescriptorByPage的参数：page 是空！无法查询!");
			return Collections.EMPTY_LIST;
		}
		long totalCnt = page.getTotalCnt();
		if (totalCnt < 0) {
			totalCnt = rnoStructureQueryDao.getMrrDescriptorCount(condition);
			page.setTotalCnt((int) totalCnt);
		}
		int startIndex = page.calculateStart();
		int cnt = page.getPageSize();
		List<Map<String, Object>> res = rnoStructureQueryDao
				.queryMrrDescriptorByPage(condition, startIndex, cnt);
		return res;
	}

	/**
	 * 
	 * @title 根据id删除mrr相关记录
	 * @param mrrId
	 * @author chao.xj
	 * @date 2014-7-23上午10:18:34
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void deleteMrrRecById(long mrrId) {
		// 删除相关的缓存记录
		// 进行标记删除
		rnoStructureQueryDao.deleteMrrDataById(mrrId);
	}

	/**
	 * 
	 * @title 提交ncs与mrr汇总分析任务
	 * @param creator
	 *            帐户名
	 * @param savePath
	 *            保存路径
	 * @param ncsInfo
	 * @param mrrInfo
	 * @param taskInfo
	 * @return
	 * @author chao.xj
	 * @date 2014-7-23上午11:46:11
	 * @company 怡创科技
	 * @version 1.2
	 */
	public Map<String, Object> submitRnoNcsAndMrrAnalysisTask(String creator,
			String savePath, NcsInfo ncsInfo, MrrInfo mrrInfo, TaskInfo taskInfo) {

		String areaLevel = ncsInfo.getNcsLevel();
		long areaId = ncsInfo.getNcsAreaId();
		String areaName = ncsInfo.getNcsAreaCoverage();
		String ncsIds = ncsInfo.getNcsIds();
		long cityId = ncsInfo.getNcsCityId();

		String mrrIds = mrrInfo.getMrrIds();

		String taskName = taskInfo.getTaskName();
		String taskDescription = taskInfo.getTaskDesc();

		Map<String, Object> result = new HashMap<String, Object>();

		RnoTask task = new RnoTask();
		task.setCreator(creator);
		task.setDataLevel(areaLevel);
		task.setLevelId(areaId + "");
		task.setLevelName(areaName);
		task.setCreateTime(new java.sql.Timestamp(new Date().getTime()));
		task.setTaskGoingStatus(TaskStatus.DOING.getCode());
		task.setTaskType("NCS_ANALYSIS");
		task.setTaskName(taskName);
		task.setDescription(taskDescription);

		// 保存任务
		final Long taskId = rnoTaskDao.saveTask(task);
		// 保存任务与ncsIds的关联信息
		String ncsIdsStr = sortNcsIds(ncsIds);
		boolean flag = rnoTaskDao.saveTaskNcsIdList(taskId, ncsIdsStr);
		// @author chao.xj 2014-7-23 下午2:31:19 保存mrr任务ID列表
		String mrrIdsStr = sortMrrIds(mrrIds);
		rnoTaskDao.saveTaskMrrIdList(taskId, mrrIdsStr);
		if (taskId == null) {
			log.error("ncs分析任务提交失败！参数ncsIds=" + ncsIds);
			if (flag == false) {
				log.error("该分析任务d ncsIds关联信息已存在，不再提交！参数ncsIds=" + ncsIdsStr);
			}
			result.put("flag", false);
			result.put("msg", "需提供有效的ncs");
			return result;
		}
		// 保存任务参数
		List<Map<String, String>> paramss = new ArrayList<Map<String, String>>();
		String[] ncss = ncsIds.split(",");
		Map<String, String> params = null;
		for (String ncs : ncss) {
			if (ncs != null && !"".equals(ncs.trim())) {
				params = new HashMap<String, String>();
				params.put("NCSID", ncs.trim());
				paramss.add(params);
			}
		}
		// @author chao.xj 2014-7-23 上午11:57:41 加入mrr参数
		String[] mrrs = mrrIds.split(",");
		for (String mrr : mrrs) {
			if (mrr != null && !"".equals(mrr.trim())) {
				params = new HashMap<String, String>();
				params.put("MRRID", mrr.trim());
				paramss.add(params);
			}
		}
		if (paramss.isEmpty()) {
			log.error("ncs分析任务所需要的ncsid列表无效！任务无法提交！");
			// 删除任务
			rnoTaskDao.deleteTask(taskId);
			// 删除任务与ncsIds的关联信息
			rnoTaskDao.deleteTaskNcsIdListByTaskId(taskId);
			result.put("flag", false);
			result.put("msg", "需提供有效的ncs");
			return result;
		}
		Map<String, String> one = new HashMap<String, String>();
		one.put("AREALEVEL", areaLevel);
		paramss.add(one);
		one = new HashMap<String, String>();
		one.put("AREAID", areaId + "");
		paramss.add(one);
		one = new HashMap<String, String>();
		one.put("AREANAME", areaName);
		paramss.add(one);
		one = new HashMap<String, String>();
		one.put("CITYID", cityId + "");
		paramss.add(one);

		int cnt = rnoTaskDao.saveTaskParam(taskId, paramss);
		if (cnt == 0) {
			log.error("ncs分析任务所需要的ncsid列表无效！任务无法提交！");
			// 删除任务
			rnoTaskDao.deleteTask(taskId);
			// 删除任务与ncsIds的关联信息
			// @author chao.xj 2014-7-18 上午11:05:56 此方法已增加 删除mrridlist表的语句
			rnoTaskDao.deleteTaskNcsIdListByTaskId(taskId);
			result.put("flag", false);
			result.put("msg", "任务提交出错！");
			return result;
		}
		// 异步启动任务
		final Map<String, String> extra = new HashMap<String, String>();
		extra.put("SAVE_PATH", savePath);
		extra.put("creator", creator);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 保存运行的线程信息
				long threadId = Thread.currentThread().getId();
				ExecutorManager.saveTaskThreadInfo(taskId, threadId);
				// ExecutorManager.printMap();
				rnoNcsGatherAnalysisWorker.doWork(taskId, extra);

				// 删除运行结束的线程信息
				ExecutorManager.deleteTaskThreadInfoByTaskId(taskId);
				// ExecutorManager.printMap();
			}
		}).start();

		result.put("flag", true);
		result.put("msg", "任务正在执行。");
		return result;
	}

	/**
	 * 
	 * @title 对mrrIds进行升序排序
	 * @param mrrIds
	 * @return
	 * @author chao.xj
	 * @date 2014-7-23下午2:35:17
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String sortMrrIds(String mrrIds) {
		// 对ncsIds列进行排序
		String[] mrrs = mrrIds.split(",");
		List<Long> mrrList = new ArrayList<Long>();
		for (String mrr : mrrs) {
			long ncsLong = Long.parseLong(mrr);
			mrrList.add(ncsLong);
		}
		Collections.sort(mrrList); // 升序
		// 转成String
		StringBuffer str = new StringBuffer();
		// for (Long mrr : mrrList) {
		// str.append(mrr+",");
		// }

		// 2014-8-8 修改为1 to 100这种格式
		if (mrrList.size() >= 2) {
			str.append(mrrList.get(0)).append(" to ")
					.append(mrrList.get(mrrList.size() - 1));
		} else if (mrrList.size() == 1) {
			str.append(mrrList.get(0));
		} else {
			str.append("-1");
		}

		return str.toString();
	}

	/**
	 * 查询mrr描述信息
	 * 
	 * @author peng.jm
	 * @date 2014-7-23下午04:07:47
	 */
	public List<Map<String, Object>> queryMrrDescByPage(
			Map<String, String> condition, Page page) {

		log.info("进入方法：queryMrrDescByPage。condition=" + condition + ",page="
				+ page);
		if (page == null) {
			log.warn("方法：queryMrrDescByPage的参数：page 是空！无法查询!");
			return Collections.EMPTY_LIST;
		}
		long totalCnt = page.getTotalCnt();
		if (totalCnt < 0) {
			totalCnt = rnoStructureQueryDao.getMrrDescriptorCount(condition);
			page.setTotalCnt((int) totalCnt);
		}
		int startIndex = page.calculateStart();
		int cnt = page.getPageSize();
		List<Map<String, Object>> res = rnoStructureQueryDao
				.queryMrrDescriptorByPage(condition, startIndex, cnt);
		return res;
	}

	/**
	 * 查询mrr管理信息
	 * 
	 * @param mrrId
	 * @param page
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:05:05
	 */
	public List<Map<String, Object>> queryMrrAdmDataByPage(long mrrId, Page page) {
		log.info("进入方法：queryMrrAdmDataByPage。mrrId=" + mrrId + ",page=" + page);
		if (page == null) {
			log.warn("方法：queryMrrAdmDataByPage的参数：page 是空！无法查询!");
			return Collections.EMPTY_LIST;
		}
		long totalCnt = page.getTotalCnt();
		if (totalCnt < 0) {
			totalCnt = rnoStructureQueryDao.getMrrAdmCount(mrrId);
			page.setTotalCnt((int) totalCnt);
		}
		int startIndex = page.calculateStart();
		int cnt = page.getPageSize();
		List<Map<String, Object>> res = rnoStructureQueryDao.queryMrrAdmByPage(
				mrrId, startIndex, cnt);
		return res;
	}

	/**
	 * 查询mrr信号强度信息
	 * 
	 * @param mrrId
	 * @param page
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:05:05
	 */
	public List<Map<String, Object>> queryMrrStrenDataByPage(long mrrId,
			Page page) {
		log.info("进入方法：queryMrrStrenDataByPage。mrrId=" + mrrId + ",page="
				+ page);
		if (page == null) {
			log.warn("方法：queryMrrStrenDataByPage的参数：page 是空！无法查询!");
			return Collections.EMPTY_LIST;
		}
		long totalCnt = page.getTotalCnt();
		if (totalCnt < 0) {
			totalCnt = rnoStructureQueryDao.getMrrStrenCount(mrrId);
			page.setTotalCnt((int) totalCnt);
		}
		int startIndex = page.calculateStart();
		int cnt = page.getPageSize();
		List<Map<String, Object>> res = rnoStructureQueryDao
				.queryMrrStrenByPage(mrrId, startIndex, cnt);
		return res;
	}

	/**
	 * 查询mrr信号质量信息
	 * 
	 * @param mrrId
	 * @param page
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:05:05
	 */
	public List<Map<String, Object>> queryMrrQualiDataByPage(long mrrId,
			Page page) {
		log.info("进入方法：queryMrrQualiDataByPage。mrrId=" + mrrId + ",page="
				+ page);
		if (page == null) {
			log.warn("方法：queryMrrQualiDataByPage的参数：page 是空！无法查询!");
			return Collections.EMPTY_LIST;
		}
		long totalCnt = page.getTotalCnt();
		if (totalCnt < 0) {
			totalCnt = rnoStructureQueryDao.getMrrQualiCount(mrrId);
			page.setTotalCnt((int) totalCnt);
		}
		int startIndex = page.calculateStart();
		int cnt = page.getPageSize();
		List<Map<String, Object>> res = rnoStructureQueryDao
				.queryMrrQualiByPage(mrrId, startIndex, cnt);
		return res;
	}

	/**
	 * 查询mrr传输功率信息
	 * 
	 * @param mrrId
	 * @param page
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:05:05
	 */
	public List<Map<String, Object>> queryMrrPowerDataByPage(long mrrId,
			Page page) {
		log.info("进入方法：queryMrrPowerDataByPage。mrrId=" + mrrId + ",page="
				+ page);
		if (page == null) {
			log.warn("方法：queryMrrPowerDataByPage的参数：page 是空！无法查询!");
			return Collections.EMPTY_LIST;
		}
		long totalCnt = page.getTotalCnt();
		if (totalCnt < 0) {
			totalCnt = rnoStructureQueryDao.getMrrPowerCount(mrrId);
			page.setTotalCnt((int) totalCnt);
		}
		int startIndex = page.calculateStart();
		int cnt = page.getPageSize();
		List<Map<String, Object>> res = rnoStructureQueryDao
				.queryMrrPowerByPage(mrrId, startIndex, cnt);
		return res;
	}

	/**
	 * 查询mrr实时预警信息
	 * 
	 * @param mrrId
	 * @param newPage
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:05:05
	 */
	public List<Map<String, Object>> queryMrrTaDataByPage(long mrrId, Page page) {
		log.info("进入方法：queryMrrTaDataByPage。mrrId=" + mrrId + ",page=" + page);
		if (page == null) {
			log.warn("方法：queryMrrTaDataByPage的参数：page 是空！无法查询!");
			return Collections.EMPTY_LIST;
		}
		long totalCnt = page.getTotalCnt();
		if (totalCnt < 0) {
			totalCnt = rnoStructureQueryDao.getMrrTaCount(mrrId);
			page.setTotalCnt((int) totalCnt);
		}
		int startIndex = page.calculateStart();
		int cnt = page.getPageSize();
		List<Map<String, Object>> res = rnoStructureQueryDao.queryMrrTaByPage(
				mrrId, startIndex, cnt);
		return res;
	}

	/**
	 * 查询mrr路径损耗信息
	 * 
	 * @param mrrId
	 * @param page
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:05:05
	 */
	public List<Map<String, Object>> queryMrrPlDataByPage(long mrrId, Page page) {
		log.info("进入方法：queryMrrPlDataByPage。mrrId=" + mrrId + ",page=" + page);
		if (page == null) {
			log.warn("方法：queryMrrPlDataByPage的参数：page 是空！无法查询!");
			return Collections.EMPTY_LIST;
		}
		long totalCnt = page.getTotalCnt();
		if (totalCnt < 0) {
			totalCnt = rnoStructureQueryDao.getMrrPlCount(mrrId);
			page.setTotalCnt((int) totalCnt);
		}
		int startIndex = page.calculateStart();
		int cnt = page.getPageSize();
		List<Map<String, Object>> res = rnoStructureQueryDao.queryMrrPlByPage(
				mrrId, startIndex, cnt);
		return res;
	}

	/**
	 * 查询mrr路径损耗差异信息
	 * 
	 * @param mrrId
	 * @param page
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:05:05
	 */
	public List<Map<String, Object>> queryMrrPldDataByPage(long mrrId, Page page) {
		log.info("进入方法：queryMrrPldDataByPage。mrrId=" + mrrId + ",page=" + page);
		if (page == null) {
			log.warn("方法：queryMrrPldDataByPage的参数：page 是空！无法查询!");
			return Collections.EMPTY_LIST;
		}
		long totalCnt = page.getTotalCnt();
		if (totalCnt < 0) {
			totalCnt = rnoStructureQueryDao.getMrrPldCount(mrrId);
			page.setTotalCnt((int) totalCnt);
		}
		int startIndex = page.calculateStart();
		int cnt = page.getPageSize();
		List<Map<String, Object>> res = rnoStructureQueryDao.queryMrrPldByPage(
				mrrId, startIndex, cnt);
		return res;
	}

	/**
	 * 查询mrr测量结果信息
	 * 
	 * @param mrrId
	 * @param page
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:05:05
	 */
	public List<Map<String, Object>> queryMrrMeaDataByPage(long mrrId, Page page) {
		log.info("进入方法：queryMrrMeaDataByPage。mrrId=" + mrrId + ",page=" + page);
		if (page == null) {
			log.warn("方法：queryMrrMeaDataByPage的参数：page 是空！无法查询!");
			return Collections.EMPTY_LIST;
		}
		long totalCnt = page.getTotalCnt();
		if (totalCnt < 0) {
			totalCnt = rnoStructureQueryDao.getMrrMeaCount(mrrId);
			page.setTotalCnt((int) totalCnt);
		}
		int startIndex = page.calculateStart();
		int cnt = page.getPageSize();
		List<Map<String, Object>> res = rnoStructureQueryDao.queryMrrMeaByPage(
				mrrId, startIndex, cnt);
		return res;
	}

	/**
	 * 查询mrr的上下行FER信息
	 * 
	 * @param mrrId
	 * @param page
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:05:05
	 */
	public List<Map<String, Object>> queryMrrFerDataByPage(long mrrId, Page page) {
		log.info("进入方法：queryMrrFerDataByPage。mrrId=" + mrrId + ",page=" + page);
		if (page == null) {
			log.warn("方法：queryMrrFerDataByPage的参数：page 是空！无法查询!");
			return Collections.EMPTY_LIST;
		}
		long totalCnt = page.getTotalCnt();
		if (totalCnt < 0) {
			totalCnt = rnoStructureQueryDao.getMrrFerCount(mrrId);
			page.setTotalCnt((int) totalCnt);
		}
		int startIndex = page.calculateStart();
		int cnt = page.getPageSize();
		List<Map<String, Object>> res = rnoStructureQueryDao.queryMrrFerByPage(
				mrrId, startIndex, cnt);
		return res;
	}

	/**
	 * 分页查询干扰矩阵信息
	 * 
	 * @author peng.jm
	 * @date 2014-8-15下午04:14:09
	 */
	public List<Map<String, Object>> queryInterferMartixByPage(
			Map<String, String> condition, Page page) {
		log.info("进入方法：queryInterferMartixByPage。condition=" + condition
				+ ",page=" + page);
		if (page == null) {
			log.warn("方法：queryInterferMartixByPage的参数：page 是空！无法查询!");
			return Collections.EMPTY_LIST;
		}
		long totalCnt = page.getTotalCnt();
		if (totalCnt < 0) {
			totalCnt = rnoStructureQueryDao.getInterferMartixCount(condition);
			page.setTotalCnt((int) totalCnt);
		}
		int startIndex = page.calculateStart();
		int cnt = page.getPageSize();
		List<Map<String, Object>> res = rnoStructureQueryDao
				.queryInterferMartixByPage(condition, startIndex, cnt);
		return res;
	}

	/**
	 * 检查这周是否计算过NCS干扰矩阵
	 * 
	 * @author peng.jm
	 * @date 2014-8-16上午11:01:47
	 */
	public List<Map<String, Object>> checkInterMartixThisWeek(long areaId,
			String thisMonday, String today) {
		return rnoStructureQueryDao.checkInterMartixThisWeek(areaId,
				thisMonday, today);
	}

	/**
	 * 分页加载NCS数据
	 * 
	 * @author peng.jm
	 * @date 2014-8-16下午02:05:38
	 */
	public List<Map<String, Object>> queryNcsDataByPageAndCond(
			Map<String, String> condition, Page page) {
		log.info("进入方法：queryNcsDataByPageAndCond。condition=" + condition
				+ ",page=" + page);
		if (page == null) {
			log.warn("方法：queryNcsDataByPageAndCond的参数：page 是空！无法查询!");
			return Collections.EMPTY_LIST;
		}
		long totalCnt = page.getTotalCnt();
		if (totalCnt < 0) {
			totalCnt = rnoStructureQueryDao.getNcsDataCount(condition);
			page.setTotalCnt((int) totalCnt);
		}
		int startIndex = page.calculateStart();
		int cnt = page.getPageSize();
		List<Map<String, Object>> res = rnoStructureQueryDao
				.queryNcsDataByPageAndCond(condition, startIndex, cnt);
		return res;
	}

	/**
	 * 查询对应条件的NCS数据记录数量
	 * 
	 * @param cond
	 * @return
	 * @author peng.jm
	 * @date 2014-8-16下午05:02:24
	 */
	public int queryNcsDataCountByCond(Map<String, String> condition) {
		return (int) rnoStructureQueryDao.getNcsDataCount(condition);
	}

	/**
	 * 检查是否存在正在计算的干扰矩阵
	 * 
	 * @param areaId
	 * @param thisMonday
	 * @param today
	 * @return
	 * @author peng.jm
	 * @date 2014-8-19下午03:57:13
	 */
	public boolean isCalculatingInterMartixThisArea(long areaId,
			String thisMonday, String today) {
		return rnoStructureQueryDao.isCalculatingInterMartixThisArea(areaId,
				thisMonday, today);
	}

	/**
	 * 新增NCS干扰矩阵
	 * 
	 * @param cond
	 * @param account
	 * @author peng.jm
	 * @date 2014-8-16下午04:40:41
	 */
	public void addInterMartixByNcs(Map<String, String> condition,
			String account) {
		log.info("进入方法：addInterMartixByNcs。condition=" + condition
				+ ", account=" + account);
//		long s = System.currentTimeMillis();
		// 创建主键
		final long martixRecId = rnoStructureQueryDao
				.getNextSeqValue("SEQ_INTER_MARTIX_DESC_ID");
		// 区域id
		final long cityId = Long.parseLong(condition.get("cityId").toString());
		// 创建日期
		Calendar cal = Calendar.getInstance();
		cal = Calendar.getInstance();
		final String createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(cal.getTime());
		// NCS数据的开始测量日期
		final String startMeaDate = condition.get("begTime").toString();
		// NCS数据的结束测量日期
		final String endMeaDate = condition.get("endTime").toString();
		// 数据量
		final long recordNum = rnoStructureQueryDao.queryNcsDataRecordsCount(
				cityId, startMeaDate, endMeaDate);
		
//		System.out.println("统计数据量耗时："+((System.currentTimeMillis()-s)/1000));
//		s = System.currentTimeMillis();
		// 结果文件路径
		final String filePath = "hdfs:///rno_data/matrix/" + cal.get(Calendar.YEAR) + "/"
				+ (cal.get(Calendar.MONTH) + 1);
//		final String realDiskPath = ServletActionContext.getServletContext()
//				.getRealPath(filePath);
		// 创建job
		JobProfile job = new JobProfile();
		job.setAccount(account);
		job.setJobName("计算干扰矩阵");
		String jobType = condition.get("jobType").toString();
		log.debug("jobType=" + jobType);
		job.setJobType(jobType);
		job.setSubmitTime(new Date());
		job.setDescription("干扰矩阵job");

		JobClient jobClient=JobClientDelegate.getJobClient();
		jobClient.submitJob(job, new JobAddCallback<Boolean>() {
			@Override
			public Boolean doWhenJobAdded(JobProfile job) {
				// 获取jobId
				long jobId = job.getJobId();
				// 新建一个干扰矩阵计算任务
				Map<String, Object> interMartix = new HashMap<String, Object>();
				interMartix.put("martix_rec_id", martixRecId);
				interMartix.put("city_id", cityId);
				interMartix.put("create_date", createDate);
				interMartix.put("start_mea_date", startMeaDate);
				interMartix.put("end_mea_date", endMeaDate);
				interMartix.put("record_num", recordNum);
				interMartix.put("file_path", filePath);
				interMartix.put("type", "NCS"); // “NCS”表示干扰矩阵是通过计算ncs数据得出
				interMartix.put("work_status", "排队中");
				interMartix.put("job_id", jobId); // 系统分配的工作id
				interMartix.put("status", "Y"); // 是否在系统中删除

				// 在数据库创建干扰矩阵计算任务
				boolean flag = rnoStructureQueryDao
						.createNcsInterMartixRec(interMartix);
				if (!flag) {
					log.info(martixRecId + "的NCS干扰矩阵任务创建失败");
					return false;
				}
				return true;
			}

		});
//		System.out.println("提交完成耗时："+((System.currentTimeMillis()-s)/1000));
//		s = System.currentTimeMillis();
	}

	/**
	 * 提交结构分析计算任务
	 * 
	 * @param account
	 * @param path
	 * @param threshold
	 * @param taskInfo
	 * @return
	 * @author peng.jm
	 * @date 2014-8-26下午04:44:25
	 */
	public Map<String, Object> submitEriAndHwStructureTask(String account,
			final String path, final List<RnoThreshold> rnoThresholds,
			final TaskInfo taskInfo) {

		Map<String, Object> result = new HashMap<String, Object>();
		// 创建job
		JobProfile job = new JobProfile();
		job.setAccount(account);
		job.setJobName(taskInfo.getTaskName());
		job.setJobType("RNO_STRUCT_ANA");
		;
		job.setSubmitTime(new Date());
		job.setDescription(taskInfo.getTaskDesc());
		// jobClient.submitJob(job);
		JobClient jobClient=JobClientDelegate.getJobClient();
		result = jobClient.submitJob(job,
				new JobAddCallback<Map<String, Object>>() {
					@Override
					public Map<String, Object> doWhenJobAdded(JobProfile job) {
						Map<String, Object> result = new HashMap<String, Object>();
						// 获取jobId
						long jobId = job.getJobId();
						if (jobId == 0) {
							log.error("创建jobId失败！");
							result.put("flag", false);
							result.put("desc", "提交任务失败！");
						}
						// 下载文件名
						String dlFileName = taskInfo.getCityName().trim() + "_"
								+ jobId + "_结构分析指标结果.zip";
						// 读取文件名
						String rdFileName = jobId + "_rdFile.ro";
						// 创建日期
						Calendar cal = Calendar.getInstance();
						cal = Calendar.getInstance();
						String createTime = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss").format(cal.getTime());
						// 文件保存路径
						String resultDir = path + File.separator
								+ cal.get(Calendar.YEAR) + "/"
								+ (cal.get(Calendar.MONTH) + 1) + "/";
						String finishState = "排队中";
						// 更新日期
						String modTime = createTime;

						// 保存对应的门限值
						// Map<String, String> map =
						// threshold.getMap(threshold);
						String paramVal;
						String paramCode;
						boolean thresholdFlag = false;
						/*
						 * for(String paramCode : map.keySet()) { paramVal =
						 * map.get(paramCode); if(paramVal == null) {
						 * log.error(paramCode + "的值为null!"); thresholdFlag =
						 * false; break; } thresholdFlag =
						 * rnoStructureQueryDao.saveThresholdWithJobId(jobId,
						 * "STRUCTANA", paramCode, paramVal); }
						 */

						String oriDs = DataSourceContextHolder
								.getDataSourceType();
						DataSourceContextHolder
								.setDataSourceType(DataSourceConst.rnoDS);
						Connection connection = DataSourceConn.initInstance()
								.getConnection();
						DataSourceContextHolder.setDataSourceType(oriDs);
						PreparedStatement pstmt = null;
						String threshHoldSql = "insert into RNO_STRUCANA_JOB_PARAM (JOB_ID,"
								+ "PARAM_TYPE,"
								+ "PARAM_CODE,"
								+ "PARAM_VAL) values("
								+ jobId
								+ ",?,"
								+ "?,"
								+ "?)";
						try {
							pstmt = connection.prepareStatement(threshHoldSql);
						} catch (SQLException e) {
							e.printStackTrace();
							result.put("flag", false);
							try {
								connection.close();
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
							return result;
						}

						try {
							for (RnoThreshold rnoThreshold : rnoThresholds) {
								paramCode = rnoThreshold.getCode();
								paramVal = rnoThreshold.getDefaultVal();

								if (!StringUtils.isBlank(paramCode)
										&& !StringUtils.isBlank(paramVal)) {
									try {
										pstmt.setString(1,
												"STRUCTANA_THRESHOLD");
										pstmt.setString(2, paramCode);
										pstmt.setString(3, paramVal);
										pstmt.addBatch();
									} catch (SQLException e) {
										e.printStackTrace();
									}
								}
								// thresholdFlag = rnoStructureQueryDao
								// .saveThresholdWithJobId(jobId,
								// "STRUCTANA_THRESHOLD", paramCode,
								// paramVal);
							}
							// @author chao.xj 2014-9-10 上午11:50:20
							// 业务数据类型：华为与爱立信
							Map<String, Boolean> busDataTypes = taskInfo
									.getBusDataType();
							for (String key : busDataTypes.keySet()) {
								boolean flag = busDataTypes.get(key);
								if (flag) {
									paramVal = "Y";
								} else {
									paramVal = "N";
								}

								if (!StringUtils.isBlank(key)
										&& !StringUtils.isBlank(paramVal)) {
									try {
										pstmt.setString(1, "STRUCTANA_DATATYPE");
										pstmt.setString(2, key);
										pstmt.setString(3, paramVal);
										pstmt.addBatch();
									} catch (SQLException e) {
										e.printStackTrace();
									}
								}

								// thresholdFlag = rnoStructureQueryDao
								// .saveThresholdWithJobId(jobId,
								// "STRUCTANA_DATATYPE", key, paramVal);
							}
							// 结构指标计算过程
							Map<String, Boolean> calProcedures = taskInfo
									.getCalProcedure();
							for (String key : calProcedures.keySet()) {
								boolean flag = calProcedures.get(key);
								if (flag) {
									paramVal = "Y";
								} else {
									paramVal = "N";
								}
								if (!StringUtils.isBlank(key)
										&& !StringUtils.isBlank(paramVal)) {
									try {
										pstmt.setString(1, "STRUCTANA_CALPROCE");
										pstmt.setString(2, key);
										pstmt.setString(3, paramVal);
										pstmt.addBatch();
									} catch (SQLException e) {
										e.printStackTrace();
									}
								}

								// thresholdFlag = rnoStructureQueryDao
								// .saveThresholdWithJobId(jobId,
								// "STRUCTANA_CALPROCE", key, paramVal);
							}

							// 执行
							try {
								pstmt.executeBatch();
							} catch (SQLException e) {
								e.printStackTrace();
							}

							// 保存结构分析任务
							boolean taskFlag = false;
//							taskFlag = rnoStructureQueryDao
//									.saveStructureAnalysisTask(jobId,
//											dlFileName, rdFileName, resultDir,
//											finishState, createTime, modTime,
//											taskInfo);
							String begMeaTime = taskInfo.getStartTime();
							String endMeaTime = taskInfo.getEndTime();
							long cityId = taskInfo.getCityId();

							String insertSql = "insert into rno_strucana_job	(JOB_ID,"
									+ "BEG_MEA_TIME,"
									+ "END_MEA_TIME,"
									+ "CITY_ID,"
									+ "DL_FILE_NAME,"
									+ "RD_FILE_NAME,"
									+ "RESULT_DIR,"
									+ "FINISH_STATE,"
									+ "STATUS,"
									+ "CREATE_TIME,"
									+ "MOD_TIME)"
									+ "	    values											" + "		  ("
									+ jobId
									+ ",											"
									+ "		   to_date('"
									+ begMeaTime
									+ "', 'yyyy-MM-dd HH24:mi:ss'),	"
									+ "		   to_date('"
									+ endMeaTime
									+ "', 'yyyy-MM-dd HH24:mi:ss'),	"
									+ "		   "
									+ cityId
									+ ",											"
									+ "		   '"
									+ dlFileName
									+ "',											"
									+ "		   '"
									+ rdFileName
									+ "',											"
									+ "		   '"
									+ resultDir
									+ "',											"
									+ "		   '"
									+ finishState
									+ "',											"
									+ "		   'N',												"
									+ "		   to_date('"
									+ createTime
									+ "', 'yyyy-MM-dd HH24:mi:ss'),"
									+ "		   to_date('"
									+ modTime
									+ "', 'yyyy-MM-dd HH24:mi:ss'))";

							Statement stmt = null;
							try {
								stmt = connection.createStatement();
								int cnt = stmt.executeUpdate(insertSql);
								if (cnt == 0) {
									log.error("保存结构分析任务的数量为0!失败！");
									result.put("flag", false);
								}
							} catch (Exception eee) {
								eee.printStackTrace();
								log.error("jobId=" + jobId + "，保存结构分析任务失败！");
								result.put("flag", false);
								result.put("desc", "提交任务失败！");
							} finally {
								stmt.close();
							}

						} catch (Exception ee) {
							ee.printStackTrace();
						} finally {
							try {
								if (pstmt != null)
									pstmt.clearBatch();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							try {
								if (pstmt != null)
									pstmt.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							try {
								connection.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						return result;
					}

				});
		return result;
	}

	/**
	 * 提交PCI规划任务
	 * 
	 * @param account
	 * @param threshold
	 * @param taskInfo
	 * @return
	 * @author peng.jm
	 * @date 2015年3月30日11:36:44
	 */
	public Map<String, Object> submitPciPlanAnalysisTask(String account,
			final List<RnoThreshold> rnoThresholds,
			final com.iscreate.op.pojo.rno.RnoLteInterferCalcTask.TaskInfo taskInfo) {
		log.debug("进入submitPciPlanAnalysisTask account=" + account
				+ ",rnoThresholds=" + rnoThresholds + ",taskInfo=" + taskInfo);
		Map<String, Object> result = new HashMap<String, Object>();
		// 创建job
		JobProfile job = new JobProfile();
		job.setAccount(account);
		job.setJobName(taskInfo.getTaskName());
		job.setJobType("RNO_PCI_PLAN");
		
		job.setSubmitTime(new Date());
		job.setDescription(taskInfo.getTaskDesc());
		// jobClient.submitJob(job);
		JobClient jobClient =JobClientDelegate.getJobClient();
		SessionService.getInstance().saveSession("session", ServletActionContext.getRequest()
				.getSession());
		result = jobClient.submitJob(job,
				new JobAddCallback<Map<String, Object>>() {
					@Override
					public Map<String, Object> doWhenJobAdded(JobProfile job) {
						Map<String, Object> result = new HashMap<String, Object>();
						// 获取jobId
						long jobId = job.getJobId();
						if (jobId == 0) {
							log.error("创建jobId失败！");
							result.put("flag", false);
							result.put("desc", "提交任务失败！");
						}
						// 下载文件名
						String dlFileName = jobId + "_PCI优化方案.xlsx";
						// 读取文件名
						String rdFileName = jobId + "_pci_data";
						// 创建日期
						Calendar cal = Calendar.getInstance();
						cal = Calendar.getInstance();
						String createTime = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss").format(cal.getTime());
						// 文件保存路径
						String resultDir = "hdfs:///rno_data/pci/" + cal.get(Calendar.YEAR) 
								+ "/" + (cal.get(Calendar.MONTH) + 1);
						String finishState = "排队中";
						// 更新日期
						String modTime = createTime;

						// 保存对应的门限值
						String paramVal;
						String paramCode;

						String oriDs = DataSourceContextHolder
								.getDataSourceType();
						DataSourceContextHolder
								.setDataSourceType(DataSourceConst.rnoDS);
						Connection connection = DataSourceConn.initInstance()
								.getConnection();
						DataSourceContextHolder.setDataSourceType(oriDs);
						PreparedStatement pstmt = null;
						String threshHoldSql = "insert into rno_lte_pci_job_param (JOB_ID,"
								+ "PARAM_TYPE,"
								+ "PARAM_CODE,"
								+ "PARAM_VAL) values("
								+ jobId
								+ ",?,"
								+ "?,"
								+ "?)";
						try {
							pstmt = connection.prepareStatement(threshHoldSql);
						} catch (SQLException e) {
							e.printStackTrace();
							result.put("flag", false);
							try {
								connection.close();
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
							return result;
						}

						try {
							for (RnoThreshold rnoThreshold : rnoThresholds) {
								paramCode = rnoThreshold.getCode();
								paramVal = rnoThreshold.getDefaultVal();

								if (!StringUtils.isBlank(paramCode)
										&& !StringUtils.isBlank(paramVal)) {
									try {
										pstmt.setString(1,
												"PCI_THRESHOLD");
										pstmt.setString(2, paramCode);
										pstmt.setString(3, paramVal);
										pstmt.addBatch();
									} catch (SQLException e) {
										e.printStackTrace();
									}
								}
							}
							
							// 执行
							try {
								pstmt.executeBatch();
							} catch (SQLException e) {
								e.printStackTrace();
								log.error("jobId=" + jobId + "，保存PCI规划参数失败！");
								result.put("flag", false);
								result.put("desc", "提交任务失败！");
								return result;
							}

							// 保存PCI任务
							String begMeaTime = taskInfo.getStartTime();
							String endMeaTime = taskInfo.getEndTime();
							long cityId = taskInfo.getCityId();
							String optimizeCells = taskInfo.getLteCells();
							String planType = taskInfo.getPlanType();
							String converType = taskInfo.getConverType();
							String relaNumType = taskInfo.getRelaNumerType();

							String insertSql = "insert into rno_lte_pci_job	(JOB_ID,"
									+ "BEG_MEA_TIME,"
									+ "END_MEA_TIME,"
									+ "CITY_ID,"
									+ "DL_FILE_NAME,"
									+ "RD_FILE_NAME,"
									+ "RESULT_DIR,"
									+ "FINISH_STATE,"
									+ "STATUS,"
									+ "CREATE_TIME,"
									+ "MOD_TIME,OPTIMIZE_CELLS,PLAN_TYPE,CONVER_TYPE,RELA_NUM_TYPE)"
									+ "	    values											" + "		  ("
									+ jobId
									+ ",											"
									+ "		   to_date('"
									+ begMeaTime
									+ "', 'yyyy-MM-dd HH24:mi:ss'),	"
									+ "		   to_date('"
									+ endMeaTime
									+ "', 'yyyy-MM-dd HH24:mi:ss'),	"
									+ "		   "
									+ cityId
									+ ",											"
									+ "		   '"
									+ dlFileName
									+ "',											"
									+ "		   '"
									+ rdFileName
									+ "',											"
									+ "		   '"
									+ resultDir
									+ "',											"
									+ "		   '"
									+ finishState
									+ "',											"
									+ "		   'N',												"
									+ "		   to_date('"
									+ createTime
									+ "', 'yyyy-MM-dd HH24:mi:ss'),"
									+ "		   to_date('"
									+ modTime
									+ "', 'yyyy-MM-dd HH24:mi:ss'),?,?,?,?)";

							pstmt = connection.prepareStatement(insertSql);

							//将conn转为oracle的conn
							C3P0NativeJdbcExtractor cp30NativeJdbcExtractor = new C3P0NativeJdbcExtractor();  
		                    OracleConnection conn = (OracleConnection) cp30NativeJdbcExtractor
		                    		.getNativeConnection(connection);  
		                    //获取clob对象
							oracle.sql.CLOB clob = oracle.sql.CLOB.createTemporary(
									conn, false, oracle.sql.CLOB.DURATION_SESSION); 
							clob.open(oracle.sql.CLOB.MODE_READWRITE); 
							clob.setString(3, optimizeCells); 
							
							pstmt.setClob(1, clob);
							pstmt.setString(2, planType);
							pstmt.setString(3, converType);
							pstmt.setString(4, relaNumType);
							pstmt.addBatch();
							
							// 执行
							try {
								pstmt.executeBatch();
							} catch (SQLException e) {
								e.printStackTrace();
								log.error("jobId=" + jobId + "，保存PCI规划任务失败！");
								result.put("flag", false);
								result.put("desc", "提交任务失败！");
							}
						} catch (Exception ee) {
							ee.printStackTrace();
							log.error("jobId=" + jobId + "，保存PCI规划任务失败！");
							result.put("flag", false);
							result.put("desc", "提交任务失败！");
						} finally {
							try {
								if (pstmt != null)
									pstmt.clearBatch();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							try {
								if (pstmt != null)
									pstmt.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							try {
								connection.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						return result;
					}
				});
		log.debug("退出submitPciPlanAnalysisTask result=" + result);
		return result;
	}
	
	/**
	 * 查询pci自动规划任务
	 * 
	 * @author peng.jm
	 * @date 2015年3月31日15:23:10
	 */
	public List<Map<String, Object>> queryPciPlanTaskByPage(
			Map<String, String> cond, Page page, String account) {
		log.info("进入方法：queryPciPlanTaskByPage。condition=" + cond
				+ ",page=" + page);
		if (page == null) {
			log.warn("方法：queryPciPlanTaskByPage的参数：page 是空！无法查询!");
			return Collections.EMPTY_LIST;
		}
		long totalCnt = page.getTotalCnt();
		if (totalCnt < 0) {
			totalCnt = rnoStructureQueryDao.getPciAnalysisTaskCount(
					cond, account);
			page.setTotalCnt((int) totalCnt);
		}
		int startIndex = page.calculateStart();
		int cnt = page.getPageSize();
		List<Map<String, Object>> res = rnoStructureQueryDao
				.queryPciPlanTaskByPage(cond, account,
						startIndex, cnt);
		return res;
	}

	
	/**
	 * 分页查询结构分析任务信息
	 * 
	 * @param cond
	 * @param newPage
	 * @param account
	 * @return
	 * @author peng.jm
	 * @date 2014-8-26下午06:45:43
	 */
	public List<Map<String, Object>> queryStructureAnalysisTaskByPage(
			Map<String, String> condition, Page page, String account) {
		log.info("进入方法：queryStructureAnalysisTaskByPage。condition=" + condition
				+ ",page=" + page);
		if (page == null) {
			log.warn("方法：queryStructureAnalysisTaskByPage的参数：page 是空！无法查询!");
			return Collections.EMPTY_LIST;
		}
		long totalCnt = page.getTotalCnt();
		if (totalCnt < 0) {
			totalCnt = rnoStructureQueryDao.getStructureAnalysisTaskCount(
					condition, account);
			page.setTotalCnt((int) totalCnt);
		}
		int startIndex = page.calculateStart();
		int cnt = page.getPageSize();
		List<Map<String, Object>> res = rnoStructureQueryDao
				.queryStructureAnalysisTaskByPage(condition, account,
						startIndex, cnt);
		return res;
	}
	
	

	/**
	 * 通过城市id和日期范围查询爱立信用于结构分析的数据详情
	 * 
	 * @param cityId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author peng.jm
	 * @date 2014-8-26下午02:52:34
	 */
	public List<Map<String, Object>> getEriDataDetailsByCityIdAndDate(
			long cityId, String startTime, String endTime) {

		DateUtil dateUtil=new DateUtil();
		
		if(StringUtils.isBlank(startTime)||StringUtils.isBlank(endTime)){
			log.error("getEriDataDetailsByCityIdAndDate未指明时间范围！");
			return Collections.EMPTY_LIST;
		}
		Date startDate = dateUtil.to_yyyyMMddDate(startTime);
		Date endDate = dateUtil.to_yyyyMMddDate(endTime);
		
		if(startDate==null||endDate==null){
			log.error("getEriDataDetailsByCityIdAndDate指定的时间有错！startTime="+startTime+",endTime="+endTime);
			return Collections.EMPTY_LIST;
		}
		List<Date> dateList = RnoHelper.findDates(startDate, endDate);

		List<Map<String, Object>> bscNumList = rnoStructureQueryDao
				.getEriDataBscNumByDate(cityId, startTime, endTime);
		List<Map<String, Object>> ncsFileNumList = rnoStructureQueryDao
				.getEriNcsFileNumByDate(cityId, startTime, endTime);
		List<Map<String, Object>> mrrFileNumList = rnoStructureQueryDao
				.getEriMrrFileNumByDate(cityId, startTime, endTime);

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		String date = "";
		long cellNum;
		long ncsCellNum;
		long mrrCellNum;
		long bscNum;
		long ncsFileNum;
		long mrrFileNum;
		String meaTime = "";

		for (Date d : dateList) {
			date = dateUtil.format_yyyyMMdd(d);
			map = new HashMap<String, Object>();
			map.put("DATETIME", date);
			map.put("CELL_NUM", "--");
			map.put("NCS_NUM", "--");
			map.put("MRR_NUM", "--");
			map.put("BSC_NUM", "--");
			map.put("NCSFILE_NUM", "--");
			map.put("MRRFILE_NUM", "--");
			for (Map<String, Object> one : bscNumList) {
				meaTime = one.get("MEA_TIME").toString();
				if (meaTime.equals(date)) {
					map.put("BSC_NUM",
							Long.parseLong(one.get("BSC_NUM").toString()));
				}
			}
			for (Map<String, Object> one : ncsFileNumList) {
				meaTime = one.get("MEA_TIME").toString();
				if (meaTime.equals(date)) {
					map.put("NCSFILE_NUM",
							Long.parseLong(one.get("NCSFILE_NUM").toString()));
				}
			}
			for (Map<String, Object> one : mrrFileNumList) {
				meaTime = one.get("MEA_TIME").toString();
				if (meaTime.equals(date)) {
					map.put("MRRFILE_NUM",
							Long.parseLong(one.get("MRRFILE_NUM").toString()));
				}
			}
			result.add(map);
		}
		return result;
		// return rnoStructureQueryDao.getEriDataDetailsByCityIdAndDate(cityId,
		// startTime, endTime);
	}

	/**
	 * 通过城市id和日期范围查询华为用于结构分析的数据详情
	 * 
	 * @param cityId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author peng.jm
	 * @date 2014-8-26下午02:52:34
	 */
	public List<Map<String, Object>> getHWDataDetailsByCityIdAndDate(
			long cityId, String startTime, String endTime) {

		DateUtil dateUtil=new DateUtil();
		Date startDate = dateUtil.to_yyyyMMddDate(startTime);
		Date endDate = dateUtil.to_yyyyMMddDate(endTime);
		List<Date> dateList = RnoHelper.findDates(startDate, endDate);

		List<Map<String, Object>> bscNumList = rnoStructureQueryDao
				.getHWDataBscNumByDate(cityId, startTime, endTime);

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		String date = "";
		long cellNum;
		long ncsCellNum;
		long mrrCellNum;
		long bscNum;
		long ncsFileNum;
		long mrrFileNum;
		String meaTime = "";

		
		for (Date d : dateList) {
			date = dateUtil.format_yyyyMMdd(d);
			map = new HashMap<String, Object>();
			map.put("DATETIME", date);
			map.put("CELL_NUM", "--");
			map.put("NCS_NUM", "--");
			map.put("MRR_NUM", "--");
			map.put("BSC_NUM", "--");
			map.put("NCSFILE_NUM", "--");
			map.put("MRRFILE_NUM", "--");
			for (Map<String, Object> one : bscNumList) {
				meaTime = one.get("MEA_TIME").toString();
				if (meaTime.equals(date)) {
					map.put("BSC_NUM",
							Long.parseLong(one.get("BSC_NUM").toString()));
				}
			}
			result.add(map);
		}
		return result;
		// return rnoStructureQueryDao.getHWDataDetailsByCityIdAndDate(cityId,
		// startTime, endTime);
	}

	/**
	 * 停止对应jobId的执行任务
	 * 
	 * @param jobId
	 * @param account
	 * @return
	 * @author peng.jm
	 * @date 2014-8-27下午05:22:32
	 */
	public boolean stopJobByJobId(long jobId, String account) {
		JobProfile job = new JobProfile();
		job.setJobId(jobId);
		JobClient jobClient=JobClientDelegate.getJobClient();
		JobStatus status = jobClient.killJob(job, account, "用户主动停止任务执行");
		if (status == null) {
			return false;
		}
		return true;
	}

	/**
	 * 通过jobId查询对应的结果分析信息
	 * 
	 * @param jobId
	 * @return
	 * @author peng.jm
	 * @date 2014-8-27下午06:17:47
	 */
	public List<Map<String, Object>> getStructureTaskByJobId(long jobId) {
		return rnoStructureQueryDao.getStructureTaskByJobId(jobId);
	}

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
	/*
	 * public Threshold getThresholdByModType(String modType){ return
	 * rnoStructureQueryDao.getThresholdByModType(modType); }
	 */
	/**
	 * 
	 * @title 获取结构分析汇总信息
	 * @param cityId
	 * @param meaDate
	 * @param dateType
	 *            HW 华为 ERI 爱立信
	 * @return
	 * @author chao.xj
	 * @date 2014-8-16下午2:31:52
	 * @company 怡创科技
	 * @version 1.2
	 */
	public Map<String, Object> getStructAnaSummaryInfoForOneDay(long cityId,
			String meaDate, String dateType) {
		return rnoStructureQueryDao.getStructAnaSummaryInfoForOneDay(cityId,
				meaDate, dateType);
	}

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
	public Map<String, Object> getStructAnaSummaryInfoForTimeRange(long cityId,
			String startDate, String endDate, String dateType) {
		return rnoStructureQueryDao.getStructAnaSummaryInfoForTimeRange(cityId,
				startDate, endDate, dateType);
	}

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
	public List<RnoThreshold> getThresholdsByModuleType(String moduleType) {
		return rnoStructureQueryDao.getThresholdsByModuleType(moduleType);
	}
	/**
	 * 
	 * @title 获取指定小区的在指定ncs里的测量信息
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-12-8下午5:10:01
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getCellNcsInfo(G2NcsQueryCond cond) {
		return rnoStructureQueryDao.getCellNcsInfo(cond);
	}

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
	public List<Map<String, Object>> getNcellInfoByCell(String cell) {
		// TODO Auto-generated method stub
		return rnoStructureQueryDao.getNcellInfoByCell(cell);
	}
	/**
	 * 
	 * @title 通过区域及起始时间和厂家,及数据类型从Hbase获取MR数据描述记录情况
	 * @param cityId
	 * @param startTime
	 * @param endTime
	 * @param factory
	 * ERI,ZTE
	 * @return
	 * @author chao.xj
	 * @date 2015-3-27下午4:33:02
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> getDataRecordFromHbase(
			long cityId, String startTime, String endTime,String factory) {

		DateUtil dateUtil=new DateUtil();
		
		if(StringUtils.isBlank(startTime)||StringUtils.isBlank(endTime)){
			log.error("getDataRecordFromHbase未指明时间范围！");
			return Collections.EMPTY_LIST;
		}
		Date startDate = dateUtil.to_yyyyMMddDate(startTime);
		Date endDate = dateUtil.to_yyyyMMddDate(endTime);
		
		if(startDate==null||endDate==null){
			log.error("getDataRecordFromHbase指定的时间有错！startTime="+startTime+",endTime="+endTime);
			return Collections.EMPTY_LIST;
		}
		List<Date> dateList = RnoHelper.findDates(startDate, endDate);
		//MR
		List<Map<String, Object>> mrRecordNumList = rnoStructureQueryDao.getDataDescRecordFromHbase(cityId, startTime, endTime, factory, "MR");
		//HO
		List<Map<String, Object>> hoRecordNumList = rnoStructureQueryDao.getDataDescRecordFromHbase(cityId, startTime, endTime, factory, "HO");
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		String date = "";
		long recordNum;
		String meaTime = "";

		Calendar now=Calendar.getInstance();
		
		for (Date d : dateList) {
			date = dateUtil.format_yyyyMMdd(d);
			map = new HashMap<String, Object>();
			map.put("DATETIME", date);
			map.put("MR_RECORD_NUM", "--");
			map.put("HO_RECORD_NUM", "--");
			for (Map<String, Object> one : mrRecordNumList) {
				meaTime = one.get("MEA_TIME").toString();
				meaTime=dateUtil.format_yyyyMMdd(dateUtil.parseDateArbitrary(meaTime));
				if (meaTime.equals(date)) {
					map.put("MR_RECORD_NUM",
							Long.parseLong(one.get("RECORD_COUNT").toString()));
				}
			}
			for (Map<String, Object> one : hoRecordNumList) {
				meaTime = one.get("MEA_TIME").toString();
				meaTime=dateUtil.format_yyyyMMdd(dateUtil.to_yyyyMMddDate(meaTime));
				if (meaTime.equals(date)) {
					map.put("HO_RECORD_NUM",
							Long.parseLong(one.get("RECORD_COUNT").toString()));
				}
			}
			result.add(map);
		}
		return result;
	}
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
	public Map<String, List<String>> getLteCellInfoByCellId(Statement stmt,long cityId) {
		return rnoStructureQueryDao.getLteCellInfoByCellId(stmt, cityId);
	}
}

class DoSingleNcsAnalysisWorker extends Thread {
	private static Log log = LogFactory.getLog(DoSingleNcsAnalysisWorker.class);

	private String diskPath;// 文件的存储目录
	private long ncsId;
	private RnoStructureAnalysisDao rnoStructureAnalysisDao;

	public DoSingleNcsAnalysisWorker(String diskPath,
			RnoStructureAnalysisDao rnoStructureAnalysisDao, long ncsId) {
		this.diskPath = diskPath;
		this.rnoStructureAnalysisDao = rnoStructureAnalysisDao;
		this.ncsId = ncsId;
	}

	public void run() {
		log.info(Thread.currentThread().getName() + "正在执行分析单个ncs的结构指标数据。。。");

		// 保存运行的线程信息
		long threadId = Thread.currentThread().getId();
		ExecutorManager.saveTaskThreadInfo(ncsId, threadId);
		// ExecutorManager.printMap();

		String flagPath = diskPath + "/"
				+ RnoConstant.ReportConstant.SINGLE_NCS_DOING_FILE + ncsId;
		String excelResPath = diskPath + "/"
				+ RnoConstant.ReportConstant.SINGLE_NCS_FILE + ncsId + ".xls";
		// 如果不存在，启动异步线程，进行计算，同时，返回用户正在计算的信息
		Connection connection = DataSourceConn.initInstance().getConnection();
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
			return;
		}
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				connection.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// 删除标志
			synchronized (RnoStructureServiceImpl.lockObj) {
				FileTool.delete(new File(flagPath));
			}
			return;
		}

		boolean exeFlag = true;// 分析成功与否的标识
		// try {
		long t1, t2 = System.currentTimeMillis();
		List<Long> ncsIds = new ArrayList<Long>();
		ncsIds.add(ncsId);

		// ---获取市区id---//
		String sql = "select city_Id from RNO_NCS_DESCRIPTOR where rno_ncs_desc_id="
				+ ncsId;
		List<Map<String, Object>> info = RnoHelper.commonQuery(stmt, sql);
		long cityId = ((BigDecimal) info.get(0).get("CITY_ID")).longValue();
		log.info("cityId=" + cityId);

		// ---执行分析----//
		Map<String, Object> result = rnoStructureAnalysisDao.doNcsAnalysis(
				connection, stmt, excelResPath, cityId, ncsIds, ncsId);
		exeFlag = (Boolean) result.get("flag");

		synchronized (RnoStructureServiceImpl.lockObj) {
			FileTool.delete(new File(flagPath));
		}
		if (exeFlag) {
			// --2014-3-21--更新descriptor的状态----//
			log.info("单个ncs汇总分析成功。");
			try {
				stmt.executeUpdate("update RNO_NCS_DESCRIPTOR  SET STATUS='H' WHERE RNO_NCS_DESC_ID="
						+ ncsId);
				// 删除运行结束的线程信息
				ExecutorManager.deleteTaskThreadInfoByTaskId(ncsId);
			} catch (SQLException e3) {
				e3.printStackTrace();
			}
			try {
				connection.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			log.error("单个ncs汇总分析失败！原因：" + result.get("msg"));
			// 删除运行结束的线程信息
			ExecutorManager.deleteTaskThreadInfoByTaskId(ncsId);
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			stmt.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			connection.setAutoCommit(true);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	};
}