package com.iscreate.op.action.rno;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.mapreduce.Job;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import com.iscreate.op.action.rno.model.G2NcsQueryCond;
import com.iscreate.op.action.rno.vo.EriInfo;
import com.iscreate.op.action.rno.vo.HwInfo;
import com.iscreate.op.action.rno.vo.StructAnaTaskInfo;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.pojo.rno.CellFreqInterferList;
import com.iscreate.op.pojo.rno.NcsCellQueryResult;
import com.iscreate.op.pojo.rno.RnoLteInterferCalcTask;
import com.iscreate.op.pojo.rno.RnoStructureAnalysisTask;
import com.iscreate.op.pojo.rno.RnoTask;
import com.iscreate.op.pojo.rno.RnoThreshold;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.rno.RnoStructureService;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.client.api.JobClient;
import com.iscreate.op.service.rno.job.client.api.JobClientDelegate;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.FileTool;
import com.iscreate.op.service.rno.tool.HttpTools;
import com.iscreate.op.service.rno.tool.RenderTool;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;

public class RnoStructureAction extends RnoCommonAction {
	private static Log log = LogFactory.getLog(RnoStructureAction.class);

	private RnoStructureService rnoStructureService;
	private long ncsId;
	private long clusterId;
	private String ncsIds;// 逗号分隔的ncs id

	private Map<String, String> cond;

	private String cell;

	private String level;
	private long cityId,areaId,provinceId;
	private String cityName,provinceName;
	private String areaName;
	
	private String preFiveDayTime;   //任务开始时间提前5天
	private String taskBeginTime;
	private String taskEndTime;

	private InputStream reportInputStream;// 导出分析报告的输入流
	private long taskId;// 分析任务id
	private String error;
	private String fileSubPath;// 不包含最外层的目录的文件路径
	private String dataType;// 需要获取的报告的数据类型
	private int singleNcs;// 是否是单个ncs
	
	private String taskName;
	private String taskDescription;
	private String taskInfoType;
	private String meaStartTime;
	private String meaEndTime;
	private int ncsFileNum;
	
	private long mrrId;
	private String mrrIds;
	private int mrrFileNum;
	private String lastMonday;
	private String lastSunday;
	private String ncsRendererType;//渲染图的类型
	private Map<String, String> threshold;//阈值门限

	private String sameFreqInterThreshold;
	
	private Map<String, Boolean> busDataType;//业务数据类型
	private Map<String, Boolean> calProcedure;//计算过程
	private long jobId;
	
	private String manufacturers; //BSC厂家
	private String bsc;
	private String ncsDateTime;
	
	//PCI规划
	private String planType;
	private String converType;
	private String cosi;
	private String lteCells;
	private String mrJobId;
	private InputStream exportInputStream;// 导出pci结果文件的输入流

	public InputStream getExportInputStream() {
		return exportInputStream;
	}

	public String getTaskBeginTime() {
		return taskBeginTime;
	}

	public void setTaskBeginTime(String taskBeginTime) {
		this.taskBeginTime = taskBeginTime;
	}

	public String getTaskEndTime() {
		return taskEndTime;
	}

	public void setTaskEndTime(String taskEndTime) {
		this.taskEndTime = taskEndTime;
	}

	public void setExportInputStream(InputStream exportInputStream) {
		this.exportInputStream = exportInputStream;
	}

	public String getMrJobId() {
		return mrJobId;
	}

	public void setMrJobId(String mrJobId) {
		this.mrJobId = mrJobId;
	}

	public String getLteCells() {
		return lteCells;
	}

	public void setLteCells(String lteCells) {
		this.lteCells = lteCells;
	}

	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}

	public String getConverType() {
		return converType;
	}

	public void setConverType(String converType) {
		this.converType = converType;
	}

	public String getCosi() {
		return cosi;
	}

	public void setCosi(String cosi) {
		this.cosi = cosi;
	}

	public String getBsc() {
		return bsc;
	}

	public void setBsc(String bsc) {
		this.bsc = bsc;
	}

	public String getNcsDateTime() {
		return ncsDateTime;
	}

	public void setNcsDateTime(String ncsDateTime) {
		this.ncsDateTime = ncsDateTime;
	}

	public String getManufacturers() {
		return manufacturers;
	}

	public void setManufacturers(String manufacturers) {
		this.manufacturers = manufacturers;
	}

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getSameFreqInterThreshold() {
		return sameFreqInterThreshold;
	}

	public void setSameFreqInterThreshold(String sameFreqInterThreshold) {
		this.sameFreqInterThreshold = sameFreqInterThreshold;
	}

	public long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(long provinceId) {
		this.provinceId = provinceId;
	}

	public String getPreFiveDayTime() {
		return preFiveDayTime;
	}

	public void setPreFiveDayTime(String preFiveDayTime) {
		this.preFiveDayTime = preFiveDayTime;
	}

	public String getLastMonday() {
		return lastMonday;
	}

	public void setLastMonday(String lastMonday) {
		this.lastMonday = lastMonday;
	}

	public String getLastSunday() {
		return lastSunday;
	}

	public void setLastSunday(String lastSunday) {
		this.lastSunday = lastSunday;
	}

	public long getNcsId() {
		return ncsId;
	}

	public void setNcsId(long ncsId) {
		this.ncsId = ncsId;
	}

	public long getClusterId() {
		return clusterId;
	}

	public void setClusterId(long clusterId) {
		this.clusterId = clusterId;
	}

	public Map<String, String> getCond() {
		return cond;
	}

	public void setCond(Map<String, String> cond) {
		this.cond = cond;
	}

	public void setRnoStructureService(RnoStructureService rnoStructureService) {
		this.rnoStructureService = rnoStructureService;
	}

	public String getNcsIds() {
		return ncsIds;
	}

	public void setNcsIds(String ncsIds) {
		this.ncsIds = ncsIds;
	}

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getFileSubPath() {
		return fileSubPath;
	}

	public void setFileSubPath(String fileSubPath) {
		this.fileSubPath = fileSubPath;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public int getSingleNcs() {
		return singleNcs;
	}

	public void setSingleNcs(int singleNcs) {
		this.singleNcs = singleNcs;
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	
	public String getNcsRendererType() {
		return ncsRendererType;
	}

	public void setNcsRendererType(String ncsRendererType) {
		this.ncsRendererType = ncsRendererType;
	}
	public String getTaskInfoType() {
		return taskInfoType;
	}

	public void setTaskInfoType(String taskInfoType) {
		this.taskInfoType = taskInfoType;
	}

	public int getNcsFileNum() {
		return ncsFileNum;
	}

	public void setNcsFileNum(int ncsFileNum) {
		this.ncsFileNum = ncsFileNum;
	}

	public String getMrrIds() {
		return mrrIds;
	}
	public void setMrrIds(String mrrIds) {
		this.mrrIds = mrrIds;
	}

	public int getMrrFileNum() {
		return mrrFileNum;
	}

	public void setMrrFileNum(int mrrFileNum) {
		this.mrrFileNum = mrrFileNum;
	}
	public long getMrrId() {
		return mrrId;
	}

	public void setMrrId(long mrrId) {
		this.mrrId = mrrId;
	}
	public String getMeaStartTime() {
		return meaStartTime;
	}

	public void setMeaStartTime(String meaStartTime) {
		this.meaStartTime = meaStartTime;
	}

	public String getMeaEndTime() {
		return meaEndTime;
	}

	public void setMeaEndTime(String meaEndTime) {
		this.meaEndTime = meaEndTime;
	}
	public Map<String, String> getThreshold() {
		return threshold;
	}

	public void setThreshold(Map<String, String> threshold) {
		this.threshold = threshold;
	}
	public Map<String, Boolean> getBusDataType() {
		return busDataType;
	}

	public void setBusDataType(Map<String, Boolean> busDataType) {
		this.busDataType = busDataType;
	}

	public Map<String, Boolean> getCalProcedure() {
		return calProcedure;
	}

	public void setCalProcedure(Map<String, Boolean> calProcedure) {
		this.calProcedure = calProcedure;
	}
	/**
	 * 初始化ncs原始数据导入页面
	 * 
	 * @return
	 * @author brightming 2014-1-15 下午4:38:55
	 */
	public String initRnoStructNcsOriFileImportAction() {
		initAreaList();// 加载区域相关信息
		return "success";
	}

	/**
	 * 初始化ncs 小区结果查看页面
	 */
	public String initNcsCellDetailQueryPageAction() {
		initAreaList();// 加载区域相关信息
		return "success";
	}

	/**
	 * 
	 * @title 分页查询ncs的小区测量信息
	 * @author chao.xj
	 * @date 2014-1-17下午03:58:49
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void queryNcsCellDataByPageForAjaxAction() {
		log.info("进入：queryNcsCellDataByPageForAjaxAction。ncsId=" + ncsId
				+ ",page=" + page);
		if (page == null) {
			log.error("方法queryNcsCellDataByPageForAjaxAction的page参数为空！");
			HttpTools.writeToClient("[]");
			return;
		}
		Page newPage = page.copy();
		List<Map<String, Object>> celldatali = rnoStructureService
				.queryNcsCellDataByPage(ncsId, newPage);
		log.info("celldatali:" + celldatali.size());
		String result1 = gson.toJson(celldatali);

		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String result = "{'page':" + pstr + ",'data':" + result1 + "}";
		log.info("退出queryNcsCellDataByPageForAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}

	/**
	 * 
	 * @title 分页查询ncs邻区测量信息
	 * @author chao.xj
	 * @date 2014-1-17下午03:59:08
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void queryNcsNcellDataByPageForAjaxAction() {
		log.info("进入：queryNcsNcellDataByPageForAjaxAction。ncsId=" + ncsId
				+ ",page=" + page);

		if (page == null) {
			log.error("方法queryNcsNcellDataByPageForAjaxAction的page参数为空！");
			HttpTools.writeToClient("[]");
			return;
		}
		Page newPage = page.copy();
		List<Map<String, Object>> ncelldatali = rnoStructureService
				.queryNcsNcellDataByPage(ncsId, newPage);
		String result1 = gson.toJson(ncelldatali);

		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String result = "{'page':" + pstr + ",'data':" + result1 + "}";
		log.info("退出queryNcsNcellDataByPageForAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}

	/**
	 * 查询连通簇信息
	 */
	public void queryNcsClusterByPageForAjaxAction() {
		log.info("进入方法：queryNcsClusterByPageForAjaxAction.page=" + page
				+ ",cond=" + cond);
		Map<String, Object> result = new HashMap<String, Object>();
		if (page == null) {
			log.error("方法queryNcsClusterByPageForAjaxAction的page参数为空！");
			HttpTools.writeToClient(gson.toJson(result));
			return;
		}

		Page newPage = page.copy();
		List<Map<String, Object>> descs = rnoStructureService
				.queryNcsClusterByPage(ncsId, newPage);

		log.info("计算以后，page=" + newPage);
		// /
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		result.put("page", newPage);
		result.put("data", descs);

		HttpTools.writeToClient(gson.toJson(result));
	}

	/**
	 * 查询连通簇内的小区信息
	 */
	public void queryNcsClusterCellForAjaxAction() {
		log.info("进入方法：queryNcsClusterCellForAjaxAction.clusterId=" + clusterId);
		if (clusterId <= 0) {
			log.error("方法queryNcsClusterCellForAjaxAction的page参数为空！");
			HttpTools.writeToClient("[]");
			return;
		}
		List<Map<String, Object>> clusterCells = rnoStructureService
				.queryNcsClusterCell(clusterId);
		HttpTools.writeToClient(gson.toJson(clusterCells));
	}

	/**
	 * 
	 * @title 分页查询干扰矩阵情况
	 * @author chao.xj
	 * @date 2014-1-17下午04:00:36
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void queryNcsInterferMatrixByPageForAjaxAction() {
		log.info("进入：queryNcsInterferMatrixByPageForAjaxActin。ncsId=" + ncsId
				+ ",page=" + page);

		if (page == null) {
			log.error("方法queryNcsInterferMatrixByPageForAjaxAction的page参数为空！");
			HttpTools.writeToClient("[]");
			return;
		}

		Page newPage = page.copy();
		List<Map<String, Object>> matrixdatali = rnoStructureService
				.queryNcsInterferMatrixByPage(ncsId, newPage);
		String result1 = gson.toJson(matrixdatali);

		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String result = "{'page':" + pstr + ",'data':" + result1 + "}";
		log.info("退出queryNcsInterferMatrixByPageForAjaxActin。输出：" + result);
		HttpTools.writeToClient(result);
	}

	/**
	 * 查询ncs记录的情况
	 * 
	 * @author brightming 2014-1-17 下午2:55:26
	 */
	public void queryNcsDescpByPageForAjaxAction() {
		log.info("进入方法：queryNcsDescpByPageForAjaxAction.page=" + page
				+ ",cond=" + cond);
		Map<String, Object> result = new HashMap<String, Object>();
		if (page == null) {
			log.error("方法queryNcsDescpByPageForAjaxAction的page参数为空！");
			HttpTools.writeToClient(gson.toJson(result));
			return;
		}

		Page newPage = page.copy();
		List<Map<String, Object>> descs = rnoStructureService
				.queryNcsDescriptorByPage(cond, newPage);

		log.info("计算以后，page=" + newPage);
		// /
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		result.put("page", newPage);
		result.put("data", descs);

		HttpTools.writeToClient(gson.toJson(result));
	}

	/**
	 * 查询区域相关的系数 1、区域结构破坏系数 2、区域归一化干扰水平
	 * 
	 * @author brightming 2014-1-19 下午1:40:40
	 */
	public void queryAreaDamageDataForAjaxAction() {
		log.info("进入方法：queryAreaDamageDataForAjaxAction.ncsIds=" + ncsIds);
		if (ncsIds == null || "".equals(ncsIds.trim())) {
			log.error("方法queryAreaDamageDataForAjaxAction 未传入ncsid值");
			HttpTools.writeToClient("{}");
			return;
		}
		String[] strarr = ncsIds.split(",");
		List<Long> ncs = new ArrayList<Long>();
		Long l;
		for (String s : strarr) {
			try {
				l = Long.parseLong(s);
				ncs.add(l);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 区域破坏系数
		List<Map<String, Object>> damage = rnoStructureService
				.queryAreaDamageFactor(ncs);
		// 归一化干扰值
		List<Map<String, Object>> normalize = rnoStructureService
				.queryAreaNormalizeFactor(ncs);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("damage", damage);
		result.put("normalize", normalize);

		List<Map<String, Object>> merge = new ArrayList<Map<String, Object>>();

		long areaid, area2;
		String areaname;
		BigDecimal bd;
		String damageval;
		String normalizeval;

		Map<String, Object> ret = null;
		for (Map<String, Object> one : damage) {

			ret = new HashMap<String, Object>();

			bd = (BigDecimal) one.get("AREA_ID");
			areaid = bd.longValue();
			areaname = (String) one.get("AREA_NAME");
			bd = (BigDecimal) one.get("VAL");
			if (bd == null) {
				damageval = "";
			} else {
				damageval = bd.floatValue() + "";
			}

			ret.put("AREA_ID", areaid);
			ret.put("AREA_NAME", areaname);
			ret.put("DAMAGE", damageval);

			for (Map<String, Object> two : normalize) {
				bd = (BigDecimal) two.get("AREA_ID");
				area2 = bd.longValue();
				if (areaid == area2) {
					bd = (BigDecimal) two.get("VAL");
					if (bd == null) {
						normalizeval = "";
					} else {
						normalizeval = bd.floatValue() + "";
					}
					ret.put("NORMALIZE", normalizeval);
					break;
				}

			}

			merge.add(ret);
		}

		String res = gson.toJson(merge);
		log.info("退出方法:queryAreaDamageDataForAjaxAction.输出：" + res);
		HttpTools.writeToClient(res);
	}

	/**
	 * 
	 * @title 查询连通簇内的小区信息并输出彼此区间干扰值
	 * @author chao.xj
	 * @date 2014-1-20上午11:28:07
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void queryNcsClusterCellAndOutputEachOtherInterValueForAjaxAction() {
		log.info("进入方法：queryNcsClusterCellAndOutputEachOtherInterValueForAjaxAction.clusterId="
				+ clusterId);
		if (clusterId <= 0) {
			log.error("方法queryNcsClusterCellAndOutputEachOtherInterValueForAjaxAction的page参数为空！");
			HttpTools.writeToClient("[]");
			return;
		}
		List<Map<String, Object>> clusterCells = rnoStructureService
				.queryNcsClusterCell(clusterId);
		List<Map<String, Object>> ncsCellInterVal = rnoStructureService
				.getNcsClusterCellAndOutputEachOtherInterValue(clusterId,
						clusterCells);
		/*
		 * for (int i = 0; i < aaList.size(); i++) {
		 * log.info(aaList.get(i).get("CELL"
		 * ).toString()+aaList.get(i).get("NCELL"
		 * )+aaList.get(i).get("INTERFER")); }
		 */
		String result = "{'clustercells':" + gson.toJson(clusterCells)
				+ ",'ncscellinter':" + gson.toJson(ncsCellInterVal) + "}";
		log.info("退出queryNcsClusterCellAndOutputEachOtherInterValueForAjaxAction: result："
				+ result);
		HttpTools.writeToClient(result);
	}

	/**
	 * 搜索包含有指定的小区的nc是列表
	 * 
	 * @author brightming 2014-2-11 下午1:49:46
	 */
	public void searchNcsContainsCellForAjaxAction() {
		log.info("进入方法：searchNcsContainsCellForAjaxAction。cell="+cell+",bsc=" + bsc
				+ ",cityId=" + cityId + ",manufacturers=" + manufacturers);
		/*List<Map<String, Object>> ncss = rnoStructureService
				.searchNcsContainCell(cell, cityId, manufacturers);*/
		G2NcsQueryCond cond=new G2NcsQueryCond();
		cond.setCell(cell);
		cond.setCityId(cityId);
		cond.setVendor(manufacturers);
		List<Map<String, Object>> ncss = rnoStructureService
				.searchNcsContainCell(cond);
		String result = gson.toJson(ncss);
		HttpTools.writeToClient(result);
	}

	/**
	 * 获取指定cell在指定ncs里得到的测量信息
	 * 
	 * @author brightming 2014-2-11 下午2:11:33
	 */
	public void getNcsCellInfoForAjaxAction() {
		log.info("进入方法：getNcsCellInfoForAjaxAction。ncsId=" + ncsId + ",cell="
				+ cell + ",cityId=" + cityId + ",manufacturers=" + manufacturers);
		G2NcsQueryCond cond=new G2NcsQueryCond();
		cond.setCell(cell);
		cond.setCityId(cityId);
		cond.setVendor(manufacturers);
		cond.setNcsDateTime(ncsDateTime);
		/*List<Map<String, Object>> ncss = rnoStructureService.getCellNcsInfo(
				ncsId, cell, cityId, manufacturers);*/
		List<Map<String, Object>> ncss = rnoStructureService.getCellNcsInfo(cond);
		/*Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> ncells=rnoStructureService.getNcellInfoByCell(cond.getCell());
		result.put("ncells", ncells);
		result.put("data", ncss);*/
		String result = gson.toJson(ncss);
		HttpTools.writeToClient(result);
//		HttpTools.writeToClient(gson.toJson(result));
	}

	/**
	 * 计算指定小区在指定ncs的频点干扰情况
	 * 
	 * @author brightming 2014-2-13 下午3:42:24
	 */
	public void getCellFreqInterferInNcsForAjaxAction() {
		log.info("进入方法：getCellFreqInterferInNcsForAjaxAction。ncsId=" + ncsId
				+ ",cell=" + cell);
		List<Long> tmpNcsIds = new ArrayList<Long>();
		tmpNcsIds.add(ncsId);
		List<String> tmpCells = new ArrayList<String>();
		tmpCells.add(cell);
		CellFreqInterferList res = rnoStructureService
				.staticsNcsCellFreqInterfer(tmpNcsIds, tmpCells);
		String result = gson.toJson(res);
		HttpTools.writeToClient(result);
	}

	/**
	 * ncs结果分析页面
	 * 
	 * @return
	 * @author brightming 2014-2-13 下午6:14:30
	 */
	public String initNcsAnalysisPageAction() {
		//@author chao.xj  2014-7-24 下午4:07:07 取消任务时
		//清空session
		SessionService.getInstance().rmvValueByKey("NCSTASKINFO");
		initAreaList();// 加载区域相关信息
		return "success";
	}

	/**
	 * NCS的结果查看
	 * 
	 * @return
	 * @author brightming 2014-2-14 上午10:25:36
	 */
	public String initRnoStructNcsDetailDisplayAction() {
		initAreaList();// 加载区域相关信息
		return "success";
	}

//	/**
//	 * 获取单个ncs的结构指标数据，包括： 被干扰系数 网络结构指数 冗余覆盖指数 重叠覆盖度 干扰源系数 过覆盖系数 小区检测次数 理想覆盖距离
//	 * 关联邻小区数 小区覆盖分量 小区容量分量
//	 * 
//	 * @author brightming 2014-2-14 下午3:32:13
//	 */
//	public void getSingleNcsStructAnaResForAjaxAction() {
//		log.info("进入方法：getSingleNcsStructAnaResForAjaxAction。ncsId=" + ncsId);
//		List<Map<String, Object>> ncss = rnoStructureService
//				.getSingleNcsStructAnaRes(ncsId);
//		String result = gson.toJson(ncss);
//		HttpTools.writeToClient(result);
//	}
//
//	/**
//	 * 获取单个ncs的所有连通簇数据 包括： Count（小区数） Trxs（频点数/载波数） 簇约束因子 簇权重 Sectors（小区列表）
//	 * 
//	 * @author brightming 2014-2-15 下午2:14:22
//	 */
//	public void getSingleNcsStructClusterListForAjaxAction() {
//		log.info("进入方法：getSingleNcsStructClusterListForAjaxAction。ncsId="
//				+ ncsId);
//		List<Map<String, Object>> ncss = rnoStructureService
//				.getSingleNcsStructClusterList(ncsId);
//		String result = gson.toJson(ncss);
//		HttpTools.writeToClient(result);
//	}
//
//	/**
//	 * 获取单个ncs的最大连通簇小区列表 包括： 簇ID 小区名 小区中文名 小区载频 簇TCH载频数
//	 * 
//	 * @author brightming 2014-2-15 下午2:14:22
//	 */
//	public void getSingleNcsStructMaxClusterCellListForAjaxAction() {
//		log.info("进入方法：getSingleNcsStructClusterListForAjaxAction。ncsId="
//				+ ncsId);
//		List<Map<String, Object>> ncss = rnoStructureService
//				.getSingleNcsStructMaxClusterList(ncsId);
//		String result = gson.toJson(ncss);
//		HttpTools.writeToClient(result);
//	}
//
//	/**
//	 * 获取单个ncs的连通簇小区干扰关系数据 包括： 主小区 小区层 簇编号 簇内小区载波数 干扰小区
//	 * 
//	 * @author brightming 2014-2-15 下午2:14:22
//	 */
//	public void getSingleNcsStructClusterCellInterListForAjaxAction() {
//		log.info("进入方法：getSingleNcsStructClusterListForAjaxAction。ncsId="
//				+ ncsId);
//		List<Map<String, Object>> ncss = rnoStructureService
//				.getSingleNcsStructClusterCellList(ncsId);
//		String result = gson.toJson(ncss);
//		HttpTools.writeToClient(result);
//	}

	/**
	 * 
	 * 提交ncs汇总计算任务
	 * 
	 * 传入参数为：ncsId列表。 返回任务提交的状态。 level:市或区 areaId:对应市或区的id
	 * 
	 * @author brightming 2014-2-17 上午9:39:58
	 */
	public void submitNcsAnalysisTaskAction() {
		log.info("进入：submitNcsAnalysisTaskAction.ncsIds=" + ncsIds + ",cityId="+cityId+",level="
				+ level + ",areaId=" + areaId + ",areaName=" + areaName + ",taskName=" + taskName + ",taskDescription=" + taskDescription);
		String account = (String) SessionService.getInstance().getValueByKey(
				"userId");
		String path = ServletActionContext.getServletContext().getRealPath(
				"/op/rno/ana_result/");

		log.info("creator=" + account + ",savepath=" + path);
		Map<String, Object> res = rnoStructureService.submitRnoNcsAnalysisTask(
				account, ncsIds, path,cityId, level, areaId, areaName, taskName, taskDescription);
		String result = gson.toJson(res);
		HttpTools.writeToClient(result);
	}

	/**
	 * ncs汇总计算任务查询
	 * 
	 * @author brightming 2014-2-18 下午3:35:45
	 */
	public void queryNcsAnalysisTaskForAjaxAction() {
		log.info("进入方法： queryNcsAnalysisTaskAction。cond=" + cond);
		String account = (String) SessionService.getInstance().getValueByKey(
				"userId");
		Map<String, Object> result = new HashMap<String, Object>();
		Page newPage = page.copy();
		List<Map<String, Object>> descs = rnoStructureService
				.queryNcsTaskByPage(account, cond, newPage);

		log.info("计算以后，page=" + newPage);
		// /
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		result.put("page", newPage);
		result.put("data", descs);

		HttpTools.writeToClient(gson.toJson(result));
	}
	
	/**
	 * 通过ncsIds获取已存在的分析任务
	 * @author peng.jm 
	 * 2014年6月23日17:48:21
	 */
	public void queryOldNcsTaskByTaskIdForAjaxAction() {
		log.info("进入方法： queryOldNcsTaskByTaskIdForAjaxAction。ncsIds=" + ncsIds);

		List<Map<String, Object>> res = rnoStructureService
				.queryOldNcsTaskByNcsIds(ncsIds);
		String result = gson.toJson(res);
		HttpTools.writeToClient(result);
	}

	/**
	 * 导出分析报告
	 * 
	 * @return
	 * @author brightming 2014-2-19 上午10:00:57
	 */
	public String exportNcsAnalysisReportAction() {
		log.info("导出分析任务的报告。任务taskId=" + taskId);
		// 获取任务的信息。
		RnoTask task = null;
		task = rnoStructureService.getTaskById(taskId);
		if (task == null) {
			error = "<h>不存在指定的分析任务!无法导出其分析报告！</h>";
			return "fail";
		}
		if(!com.iscreate.op.service.rno.task.TaskStatus.FINISH.getCode().equals(task.getTaskGoingStatus())){
			//状态不对
			log.error("任务taskId[" + taskId + "]尚未完成，请等完成后再尝试。");
			error = "任务taskId[" + taskId + "]尚未完成，请等完成后再尝试。";
			return "fail";
		}
//	    task.getResult()
		Timestamp ts = task.getCompleteTime();
		if(ts==null){
			log.error("任务taskId[" + taskId + "]尚未正常完成，请等完成后再尝试。");
            error = "任务taskId[" + taskId + "]尚未正常完成，请等完成后再尝试。";
			return "fail";
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date(ts.getTime()));

		fileSubPath = calendar.get(Calendar.YEAR) + "/"
				+ (calendar.get(Calendar.MONTH) + 1) + "/ncs_res_" + taskId
				+ ".xls";
		String name = (task.getLevelName() == null ? "" : task.getLevelName())
				+ "Ncs汇总分析报告("
				+ new DateUtil().format_yyyyMMddHHmmss(new Date(ts.getTime()))
				+ ").xls";
		log.info("报告所在的文件：" + fileSubPath + ",将输出的文件名：" + name);
		super.setFileName(name);

		// 判断文件是否存在
		String file = "/op/rno/ana_result/" + fileSubPath;// 文件下载路径 hardcode
		String realPath = ServletActionContext.getServletContext().getRealPath(
				file);
		File f = new File(realPath);
		if (!f.exists()) {
			log.error("任务taskId[" + taskId + "]对应的报告文件：" + file + "不存在！");
			error = "所需要的任务对应的报告不存在！是否重新计算？"
					+ "<form action='recalculateRnoNcsTaskAction' type='post'>"
					+ "<input type='hidden' name='taskId' value='" + taskId
					+ "'/>" + "<input type='submit' value='重新计算' />"
					+ "</form>";
			return "fail";
		}
		return "success";
	}
	
	public InputStream getReportInputStream()
			throws UnsupportedEncodingException {
//		String file = "/op/rno/ana_result/" + fileSubPath;// 文件下载路径 hardcode
////		super.setFileName(new String(super.getFileName().getBytes("GBK"),
////				"ISO-8859-1"));
//		return ServletActionContext.getServletContext().getResourceAsStream(
//				file);
		return reportInputStream;
	}

	/**
	 * 重新计算某个task的结果
	 * 
	 * @author brightming 2014-2-19 上午10:53:28
	 */
	public void recalculateRnoNcsTaskAction() {
		log.info("进入方法：recalculateRnoNcsTaskAction.taskId=" + taskId + 
				",taskName=" + taskName + ",taskDescription=" + taskDescription);
		String path = ServletActionContext.getServletContext().getRealPath(
				"/op/rno/ana_result/");
		String account = (String) SessionService.getInstance().getValueByKey(
				"userId");
		log.info("creator=" + account + ",savepath=" + path);
		Map<String, Object> res = rnoStructureService
				.recalculateRnoNcsAnalysisTask(account, taskId, path, taskName, taskDescription);
		String result = gson.toJson(res.get("msg") + "");
		HttpTools.writeToClient(result);
	}

	/**
	 * 获取ncs分析报告的数据
	 * 
	 * @author brightming 2014-2-20 上午11:24:36
	 */
	public void getNcsReportDataForAjaxAction() {
		log.info("进入方法：getNcsReportDataForAjaxAction.singleNcs=" + singleNcs
				+ ",taskId=" + taskId + ",dataType=" + dataType);

		if (singleNcs == 0) {
			log.info("获取多个ncs的分析报告。。。");
			// 获取任务的信息。
			RnoTask task = null;
			task = rnoStructureService.getTaskById(taskId);
			if (task == null) {
				HttpTools.writeToClient("[]");
				return;
			}
			Timestamp ts = task.getStartTime();
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(new Date(ts.getTime()));

			fileSubPath = calendar.get(Calendar.YEAR) + "/"
					+ (calendar.get(Calendar.MONTH) + 1) + "/ncs_res_" + taskId
					+ ".xls";
			String name = (task.getLevelName() == null ? "" : task
					.getLevelName())
					+ "Ncs汇总分析报告("
					+ new DateUtil().format_yyyyMMddHHmmss(new Date(ts.getTime()))
					+ ").xls";
			log.info("报告所在的文件：" + fileSubPath + ",将输出的文件名：" + name);
			super.setFileName(name);

			// 判断文件是否存在
			String file = "/op/rno/ana_result/" + fileSubPath;// 文件下载路径 hardcode
			String realPath = ServletActionContext.getServletContext()
					.getRealPath(file);

			List<Object> res = rnoStructureService.getNcsTaskReport(taskId, realPath,
					dataType);
			String result = gson.toJson(res);
			HttpTools.writeToClient(result);
			log.info("退出方法：getNcsReportDataForAjaxAction。返回数据："
					+ (res == null ? 0 : res.size()));
		} else {
			log.info("获取单个ncs的分析报告...");
			String subpath=(1000*(taskId/1000)+1)+"_"+(1000*(taskId/1000+1));
			String file = "/op/rno/ana_result/single/" + subpath+"/";// 文件下载路径 hardcode
			String realPath = ServletActionContext.getServletContext()
					.getRealPath(file);
			List<Map<String, Object>> data=rnoStructureService.getSingleNcsStructData(realPath,taskId,dataType);
			if(data==null){
				log.info("ncs["+taskId+"]的结构信息正在分析中");
				HttpTools.writeToClient("doing");
			}else{
				String result = gson.toJson(data);
				HttpTools.writeToClient(result);
			}
		}
	}

	/**
	 * 根据taskId停止对应的ncs分析任务
	 * @author peng.jm  2014年6月20日14:14:37
	 */
	public void stopNcsAnalysisTaskForAjaxAction() {
		log.info("进入方法：stopNcsAnalysisTaskForAjaxAction。taskId=" + taskId);
		Map<String, Object> res = rnoStructureService.stopNcsAnalysisTask(taskId);
		String result = gson.toJson(res);
		HttpTools.writeToClient(result);
	}
	
	/**
	 * 根据ncsDescId获取指定小区
	 * 
	 * @author Liang YJ
	 * @date 2014-2-19 上午10:24:50
	 */
	public void getNcsCellByPageForAjaxAction() {
		// 分页参数：page
		// ncs描述id:ncsId
		log.info("进入方法：getNcsCellByPageForAjaxAction.ncsId" + ncsId + ",page"
				+ page);
		// String loginPerson = (String)
		// SessionService.getInstance().getValueByKey("userId");
		NcsCellQueryResult ncsCellQueryResult = null;
		int totalCnt = 0;
		ncsCellQueryResult = rnoStructureService.getNcsCellByPage(ncsId, page);
		totalCnt = ncsCellQueryResult.getTotalCnt();

		Page newPage = new Page();
		newPage.setCurrentPage(page.getCurrentPage());
		newPage.setPageSize(page.getPageSize());
		newPage.setTotalCnt(totalCnt);
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(page.getForcedStartIndex());
		ncsCellQueryResult.setPage(newPage);

		String result = gson.toJson(ncsCellQueryResult);
		log.info("离开方法：getNcsCellByPageForAjaxAction。输出小区数：" + totalCnt
				+ ",page" + newPage);
		// System.out.println("离开方法：getNcsCellByPageForAjaxAction。输出小区数："+totalCnt+",page"+newPage);
		HttpTools.writeToClient(result);
	}

	/**
	 * 初始化ncs数据渲染展示的地图
	 * 
	 * @author Liang YJ
	 * @date 2014-2-18 16:20
	 */
	public String initNcsCellMapAction() {
		return "success";
	}
	
	/**
	 * 根据id删除ncs记录
	 * 
	 * @author brightming
	 * 2014-3-13 下午4:59:56
	 */
	public void deleteNcsRecByIdForAjaxAction(){
		log.info("进入方法：deleteNcsRecByIdForAjaxAction.ncsId="+ncsId);
		rnoStructureService.deleteNcsRecById(ncsId);
		HttpTools.writeToClient("success");
	}
	/**
	 * 根据复选框选择项删除若干项ncs记录
	 * @title 
	 * @author chao.xj
	 * @date 2014-3-27上午11:32:24
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void deleteNcsSelectedRecByIdForAjaxAction(){
		log.info("进入方法：deleteNcsSelectedRecByIdForAjaxAction.ncsId="+ncsIds);
		rnoStructureService.deleteNcsSelectedRecById(ncsIds);
		HttpTools.writeToClient("success");
	}
	
	/**
	 * 查询指定的ncs desc的状态
	 * 
	 * @author brightming
	 * 2014-5-21 下午5:23:45
	 */
	public void queryNcsDescStatusForAjaxAction(){
		log.debug("进入方法: queryNcsDescStatusForAjaxAction.ncsIds="+ncsIds);
		List<Map<String,Object>> res=rnoStructureService.queryNcsDescStatus(ncsIds);
		String result=gson.toJson(res);
		HttpTools.writeToClient(result);
	}
	
	/**
	 * 检查是否存在已保存的ncs分析任务
	 * @author peng.jm
	 * 2014年6月23日9:49:58
	 */
	public void checkNcsTaskByNcsIdsForAjaxAction() {
		log.debug("进入方法: checkNcsTaskByNcsIdsForAjaxAction。ncsIds="+ncsIds);
		Map<String, Object> res = rnoStructureService.checkNcsTaskByNcsIds(ncsIds);
		String result = gson.toJson(res);
		HttpTools.writeToClient(result);
	}
	
	/**
	 * 检查是否存在对应的渲染图，不存在则生成该参数的渲染图
	 * @author peng.jm
	 * 2014年7月9日12:16:03
	 */
	public void getRenderImgByParamAndTaskIdForAjaxAction() {
		log.debug("进入方法: getRenderImgByParamAndTaskIdForAjaxAction。ncsRendererType=" + ncsRendererType + ",taskId=" + taskId);
		
//		String filePath = ServletActionContext.getServletContext().getRealPath(
//					"/op/rno/ana_result/");
		// 获取任务的信息。
		List<Map<String,Object>> res = rnoStructureService.getStructureTaskByJobId(taskId);
		if(res.size() <= 0) {
			log.error("找不到对应taskId="+taskId+"的任务！");
			return;
		}
		String resultDir = "";
		String rdFileName = "";
		if(res.get(0).get("RESULT_DIR") != null) {
			resultDir = res.get(0).get("RESULT_DIR").toString();
		} 
		if(res.get(0).get("RD_FILE_NAME") != null) {
			rdFileName = res.get(0).get("RD_FILE_NAME").toString();
		} 
		log.info("ro文件所在路径：" + resultDir + ", 文件名：" + rdFileName);
		String filePath = resultDir + rdFileName; //rdFileName;
		log.info("ncsRendererType=" + ncsRendererType + ",taskId=" + taskId + ",filePath=" + filePath);
		Map<String, Object> res2 = rnoStructureService
				.getRenderImgByParamAndTaskId(ncsRendererType, taskId, filePath);
		String result = gson.toJson(res2);
		HttpTools.writeToClient(result);
	}
	
	/**
	 * 渲染进度查询
	 * @author peng.jm
	 * 2014年7月11日16:26:55
	 */
	public void queryRenderProgressAjaxForAction() {
		log.debug("进入方法: queryRenderProgressAjaxForAction。ncsRendererType=" + ncsRendererType + ",taskId=" + taskId);
		Map<String, Object> res = RenderTool.getRenderProgressFromTaskIdAndType(
				taskId, ncsRendererType);
		if(res.size() == 0 ) {
			res.put("flag", false);
		} else {
			res.put("flag", true);
		}
		String result = gson.toJson(res);
		HttpTools.writeToClient(result);
	}
	
	/**
	 * 获取渲染规则
	 * @author peng.jm
	 * 2014年7月16日14:53:28
	 */
	public void getRenderColorRuleAjaxForAction() {
		log.debug("进入方法: queryRenderProgressAjaxForAction。ncsRendererType="
				+ ncsRendererType + ",taskId=" + taskId);		
		// 获取任务的信息。
		List<Map<String,Object>> res = rnoStructureService.getStructureTaskByJobId(taskId);
		if(res.size() <= 0) {
			log.error("找不到对应taskId="+taskId+"的任务！");
			return ;
		}
		String resultDir = "";
		String rdFileName = "";
		if(res.get(0).get("RESULT_DIR") != null) {
			resultDir = res.get(0).get("RESULT_DIR").toString();
		} 
		if(res.get(0).get("RD_FILE_NAME") != null) {
			rdFileName = res.get(0).get("RD_FILE_NAME").toString();
		} 
		
		log.info("结果文件所在路径：" + resultDir + ", 文件名：" + rdFileName);
		
//		String filePath = ServletActionContext.getServletContext().getRealPath(
//				"/op/rno/ana_result/");

		String filePath = resultDir + rdFileName;
		
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();

		result = rnoStructureService.getRenderColorRule(ncsRendererType, taskId, filePath);
		String resultStr = gson.toJson(result);
		HttpTools.writeToClient(resultStr);
	}

	/**
	 * 初始化Mrr数据导入页面
	 * @return
	 * @author peng.jm
	 * @date 2014-7-18上午09:32:30
	 */
	public String initRnoStructMrrOriFileImportAction() {
		initAreaList();// 加载区域相关信息
		return "success";
	}
	
	/**
	 * 检查区域是否正在导入mrr数据
	 * 
	 * @author peng.jm
	 * @date 2014-7-24上午10:39:45
	 */
	@SuppressWarnings("unchecked")
	public void checkAreaLockedForImportMrrAction() {
		log.info("进入方法：checkAreaLockForImportMrrAction. cityId=" + cityId);
		boolean flag = false;
		ServletContext context = ServletActionContext.getServletContext();
		Set<Long> lcAreaIds = (Set<Long>) context
				.getAttribute(RnoConstant.ApplicationConstant.LockAreaForImportEriMrr);
		log.debug("正在导入mrr数据的区域：" + lcAreaIds);
		if (lcAreaIds == null || lcAreaIds.isEmpty()) {
			flag = false;
		} else {
			for (Long id : lcAreaIds) {
				if(id == cityId) {
					log.debug("检查到正在导入mrr数据的区域：" + id);
					flag = true;
				}
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		HttpTools.writeToClient(gson.toJson(result));
	}
	
	/**
	 * 初始化MRR描述查看页面
	 * @return
	 * @author peng.jm
	 * @date 2014-7-23下午03:06:42
	 */
	public String initRnoStructMrrDetailDisplayAction() {
		initAreaList();// 加载区域相关信息
		return "success";
	}
	
	/**
	 * 查询mrr描述信息
	 * 
	 * @author peng.jm
	 * @date 2014-7-23下午04:07:47
	 */
	public void queryMrrDescriptorByPageForAjaxAction() {
		log.info("进入方法：queryMrrDescriptorByPageForAjaxAction.page=" + page
				+ ",cond=" + cond);
		Map<String, Object> result = new HashMap<String, Object>();
		if (page == null) {
			log.error("方法queryMrrDescriptorByPageForAjaxAction的page参数为空！");
			HttpTools.writeToClient(gson.toJson(result));
			return;
		}

		Page newPage = page.copy();
		List<Map<String, Object>> descs = rnoStructureService
				.queryMrrDescByPage(cond, newPage);

		log.info("计算以后，page=" + newPage);
		// /
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		result.put("page", newPage);
		result.put("data", descs);

		HttpTools.writeToClient(gson.toJson(result));
	}

	/**
	 * 查询mrr管理信息
	 * 
	 * @author peng.jm
	 * @date 2014-7-24下午03:04:22
	 */
	public void queryMrrAdmDataByPageForAjaxAction() {
		log.info("进入方法queryMrrAdmDataByPageForAjaxAction。 mrrId=" + mrrId
				+ ", page=" + page);
		Map<String, Object> result = new HashMap<String, Object>();
		if (page == null) {
			log.error("方法queryMrrAdmDataByPageForAjaxAction的page参数为空！");
			HttpTools.writeToClient(gson.toJson(result));
			return;
		}

		Page newPage = page.copy();
		List<Map<String, Object>> mrrAdms = rnoStructureService
				.queryMrrAdmDataByPage(mrrId, newPage);

		log.info("计算以后，page=" + newPage);
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		result.put("page", newPage);
		result.put("data", mrrAdms);
		HttpTools.writeToClient(gson.toJson(result));
	}
	
	/**
	 * 查询mrr信号强度信息
	 * 
	 * @author peng.jm
	 * @date 2014-7-24下午03:04:22
	 */
	public void queryMrrStrenDataByPageForAjaxAction() {
		log.info("进入方法queryMrrStrenDataByPageForAjaxAction。 mrrId=" + mrrId
				+ ", page=" + page);
		Map<String, Object> result = new HashMap<String, Object>();
		if (page == null) {
			log.error("方法queryMrrStrenDataByPageForAjaxAction的page参数为空！");
			HttpTools.writeToClient(gson.toJson(result));
			return;
		}

		Page newPage = page.copy();
		List<Map<String, Object>> mrrStrens = rnoStructureService
				.queryMrrStrenDataByPage(mrrId, newPage);

		log.info("计算以后，page=" + newPage);
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		result.put("page", newPage);
		result.put("data", mrrStrens);
		HttpTools.writeToClient(gson.toJson(result));
	}
	
	/**
	 * 查询mrr信号质量信息
	 * 
	 * @author peng.jm
	 * @date 2014-7-24下午03:04:22
	 */
	public void queryMrrQualiDataByPageForAjaxAction() {
		log.info("进入方法queryMrrQualiDataByPageForAjaxAction。 mrrId=" + mrrId
				+ ", page=" + page);
		Map<String, Object> result = new HashMap<String, Object>();
		if (page == null) {
			log.error("方法queryMrrQualiDataByPageForAjaxAction的page参数为空！");
			HttpTools.writeToClient(gson.toJson(result));
			return;
		}

		Page newPage = page.copy();
		List<Map<String, Object>> mrrQualis = rnoStructureService
				.queryMrrQualiDataByPage(mrrId, newPage);

		log.info("计算以后，page=" + newPage);
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		result.put("page", newPage);
		result.put("data", mrrQualis);
		HttpTools.writeToClient(gson.toJson(result));
	}
	
	/**
	 * 查询mrr传输功率信息
	 * 
	 * @author peng.jm
	 * @date 2014-7-24下午03:04:22
	 */
	public void queryMrrPowerDataByPageForAjaxAction() {
		log.info("进入方法queryMrrPowerDataByPageForAjaxAction。 mrrId=" + mrrId
				+ ", page=" + page);
		Map<String, Object> result = new HashMap<String, Object>();
		if (page == null) {
			log.error("方法queryMrrPowerDataByPageForAjaxAction的page参数为空！");
			HttpTools.writeToClient(gson.toJson(result));
			return;
		}

		Page newPage = page.copy();
		List<Map<String, Object>> mrrQualis = rnoStructureService
				.queryMrrPowerDataByPage(mrrId, newPage);

		log.info("计算以后，page=" + newPage);
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		result.put("page", newPage);
		result.put("data", mrrQualis);
		HttpTools.writeToClient(gson.toJson(result));
	}
	
	/**
	 * 查询mrr实时预警信息
	 * 
	 * @author peng.jm
	 * @date 2014-7-24下午03:04:22
	 */
	public void queryMrrTaDataByPageForAjaxAction() {
		log.info("进入方法queryMrrTaDataByPageForAjaxAction。 mrrId=" + mrrId
				+ ", page=" + page);
		Map<String, Object> result = new HashMap<String, Object>();
		if (page == null) {
			log.error("方法queryMrrTaDataByPageForAjaxAction的page参数为空！");
			HttpTools.writeToClient(gson.toJson(result));
			return;
		}

		Page newPage = page.copy();
		List<Map<String, Object>> mrrTas = rnoStructureService
				.queryMrrTaDataByPage(mrrId, newPage);

		log.info("计算以后，page=" + newPage);
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		result.put("page", newPage);
		result.put("data", mrrTas);
		HttpTools.writeToClient(gson.toJson(result));
	}
	
	/**
	 * 查询mrr路径损耗信息
	 * 
	 * @author peng.jm
	 * @date 2014-7-24下午03:04:22
	 */
	public void queryMrrPlDataByPageForAjaxAction() {
		log.info("进入方法queryMrrPlDataByPageForAjaxAction。 mrrId=" + mrrId
				+ ", page=" + page);
		Map<String, Object> result = new HashMap<String, Object>();
		if (page == null) {
			log.error("方法queryMrrPlDataByPageForAjaxAction的page参数为空！");
			HttpTools.writeToClient(gson.toJson(result));
			return;
		}

		Page newPage = page.copy();
		List<Map<String, Object>> mrrPls = rnoStructureService
				.queryMrrPlDataByPage(mrrId, newPage);

		log.info("计算以后，page=" + newPage);
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		result.put("page", newPage);
		result.put("data", mrrPls);
		HttpTools.writeToClient(gson.toJson(result));
	}
	
	/**
	 * 查询mrr路径损耗差异信息
	 * 
	 * @author peng.jm
	 * @date 2014-7-24下午03:04:22
	 */
	public void queryMrrPldDataByPageForAjaxAction() {
		log.info("进入方法queryMrrPldDataByPageForAjaxAction。 mrrId=" + mrrId
				+ ", page=" + page);
		Map<String, Object> result = new HashMap<String, Object>();
		if (page == null) {
			log.error("方法queryMrrPldDataByPageForAjaxAction的page参数为空！");
			HttpTools.writeToClient(gson.toJson(result));
			return;
		}

		Page newPage = page.copy();
		List<Map<String, Object>> mrrPlds = rnoStructureService
				.queryMrrPldDataByPage(mrrId, newPage);

		log.info("计算以后，page=" + newPage);
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		result.put("page", newPage);
		result.put("data", mrrPlds);
		HttpTools.writeToClient(gson.toJson(result));
	}
	
	/**
	 * 查询mrr测量结果信息
	 * 
	 * @author peng.jm
	 * @date 2014-7-24下午03:04:22
	 */
	public void queryMrrMeaDataByPageForAjaxAction() {
		log.info("进入方法queryMrrMeaDataByPageForAjaxAction。 mrrId=" + mrrId
				+ ", page=" + page);
		Map<String, Object> result = new HashMap<String, Object>();
		if (page == null) {
			log.error("方法queryMrrMeaDataByPageForAjaxAction的page参数为空！");
			HttpTools.writeToClient(gson.toJson(result));
			return;
		}

		Page newPage = page.copy();
		List<Map<String, Object>> mrrMeas = rnoStructureService
				.queryMrrMeaDataByPage(mrrId, newPage);

		log.info("计算以后，page=" + newPage);
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		result.put("page", newPage);
		result.put("data", mrrMeas);
		HttpTools.writeToClient(gson.toJson(result));
	}
	
	/**
	 * 查询mrr的上下行FER信息
	 * 
	 * @author peng.jm
	 * @date 2014-7-24下午03:04:22
	 */
	public void queryMrrFerDataByPageForAjaxAction() {
		log.info("进入方法queryMrrFerDataByPageForAjaxAction。 mrrId=" + mrrId
				+ ", page=" + page);
		Map<String, Object> result = new HashMap<String, Object>();
		if (page == null) {
			log.error("方法queryMrrFerDataByPageForAjaxAction的page参数为空！");
			HttpTools.writeToClient(gson.toJson(result));
			return;
		}

		Page newPage = page.copy();
		List<Map<String, Object>> mrrFers = rnoStructureService
				.queryMrrFerDataByPage(mrrId, newPage);

		log.info("计算以后，page=" + newPage);
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		result.put("page", newPage);
		result.put("data", mrrFers);
		HttpTools.writeToClient(gson.toJson(result));
	}
	
	/**
	 * 初始化干扰矩阵管理页面
	 * @author peng.jm
	 * @date 2014-8-15上午10:35:20
	 */
	public String initInterferMartixManageAction() {
		initAreaList();// 加载区域相关信息
		return "success";
	}
	
	/**
	 * 分页查询干扰矩阵信息
	 * @author peng.jm
	 * @date 2014-8-15下午04:14:09
	 */
	public void queryInterferMartixByPageForAjaxAction() {
		log.info("进入方法queryInterferMartixByPageForAjaxAction. page=" + page
			+ ",cond=" + cond);
		
		Map<String, Object> result = new HashMap<String, Object>();
		if (page == null) {
			log.error("方法queryInterferMartixByPageForAjaxAction的page参数为空！");
			HttpTools.writeToClient(gson.toJson(result));
			return;
		}

		Page newPage = page.copy();
		List<Map<String, Object>> res = rnoStructureService
				.queryInterferMartixByPage(cond, newPage);

		log.info("计算以后，page=" + newPage);
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		result.put("page", newPage);
		result.put("data", res);

		HttpTools.writeToClient(gson.toJson(result));
	}
	
	/**
	 * 初始化新增NCS干扰矩阵页面
	 * @author peng.jm
	 * @date 2014-8-15下午06:14:09
	 */
	public String initInterferMartixAddForAjaxAction() {
		
		initAreaList();// 加载区域相关信息
		
		Calendar cal = Calendar.getInstance();
		//n为推迟的周数，1本周，-1向前推迟一周，2下周，依次类推
		int n = -1;
		cal.add(Calendar.DATE, n*7);
		cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		lastMonday = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(cal.getTime());
		//System.out.println(monday);
		  
		n = 1;
		cal.add(Calendar.DATE, n*7);
		cal.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
		lastSunday = new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(cal.getTime());
		//System.out.println(sunday);
		
		return "success";
	}
	
	/**
	 * 检查这周是否计算过NCS干扰矩阵
	 * @author peng.jm
	 * @date 2014-8-16上午11:01:47
	 */
	public void isExistedInterMartixThisWeekAction() {
		log.info("进入方法isExistedInterMartixThisWeekAction. cond=" + cond);
		if (cond == null || cond.isEmpty()) {
			log.info("未传入查询条件");
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		long areaId = Long.parseLong(cond.get("cityId").toString());
		Calendar cal = Calendar.getInstance();
		//获取这个周一
		cal.add(Calendar.DATE, 0*7);
		cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		String thisMonday = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(cal.getTime());
		//获取今天
	    cal = Calendar.getInstance();
	    String today = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
		//查询结果
		List<Map<String, Object>> res = rnoStructureService
				.checkInterMartixThisWeek(areaId, thisMonday, today);
		//转为星期命名
		String desc = "";
		boolean flag = false;
		if(res != null && res.size() > 0) {
			String createTime = res.get(0).get("CREATE_DATE").toString();
			String timeArray[] = createTime.split(" ");
			Date date = null;
			try {
				date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createTime);
			} catch (ParseException e) {
				e.printStackTrace();
			} 
			cal = Calendar.getInstance();
			cal.setTime(date);
			int weekNum = cal.get(Calendar.DAY_OF_WEEK);
			
			switch (weekNum) {
				case 1:
					desc = "本周周日 " + timeArray[1] + " 曾经计算过干扰矩阵";
					flag = true;
					break;
				case 2:
					desc = "本周周一 " + timeArray[1] + " 曾经计算过干扰矩阵";
					flag = true;
					break;
				case 3:
					desc = "本周周二 " + timeArray[1] + " 曾经计算过干扰矩阵";
					flag = true;
					break;
				case 4:
					desc = "本周周三 " + timeArray[1] + " 曾经计算过干扰矩阵";
					flag = true;
					break;
				case 5:
					desc = "本周周四 " + timeArray[1] + " 曾经计算过干扰矩阵";
					flag = true;
					break;
				case 6:
					desc = "本周周五 " + timeArray[1] + " 曾经计算过干扰矩阵";
					flag = true;
					break;
				case 7:
					desc = "本周周六 " + timeArray[1] + " 曾经计算过干扰矩阵";
					flag = true;
					break;
				default:
					break;
			}
		}
		
		result.put("flag", flag);
		result.put("desc", desc);
		
		HttpTools.writeToClient(gson.toJson(result));
	}
	
	/**
	 * 分页加载NCS数据
	 * @author peng.jm
	 * @date 2014-8-16下午02:05:38
	 */
	public void queryNcsDataByPageForAjaxAction() {
		log.info("进入方法queryNcsDataByPageForAjaxAction. page=" + page
			+ ",cond=" + cond);
		if (cond == null || cond.isEmpty()) {
			log.info("未传入查询条件");
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		if (page == null) {
			log.error("方法queryNcsDataByPageForAjaxAction的page参数为空！");
			HttpTools.writeToClient(gson.toJson(result));
			return;
		}

		Page newPage = page.copy();
		List<Map<String, Object>> res = rnoStructureService
				.queryNcsDataByPageAndCond(cond, newPage);

		log.info("计算以后，page=" + newPage);
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		result.put("page", newPage);
		result.put("data", res);

		HttpTools.writeToClient(gson.toJson(result));
	}
	
	/**
	 * 新增NCS干扰矩阵
	 * 
	 * @author peng.jm
	 * @date 2014-8-16下午04:10:48
	 */
	public void addInterMartixByNcsForAjaxAction() {
		log.info("进入方法addInterMartixByNcsForAjaxAction。cond=" + cond);
		if (cond == null || cond.isEmpty()) {
			log.info("未传入查询条件");
		}
//		long s = System.currentTimeMillis();
		Calendar cal = Calendar.getInstance();
		//获取上周周一
		cal.add(Calendar.DATE, -1 * 7);
		cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		lastMonday = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(cal.getTime());
		//获取上周周日
		cal.add(Calendar.DATE, 1 * 7);
		cal.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
		lastSunday = new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(cal.getTime());
		
		DateUtil dateUtil = new DateUtil();
		Date startTime = dateUtil.parseDateArbitrary(cond.get("begTime").toString());
		Date endTime = dateUtil.parseDateArbitrary(cond.get("endTime").toString());
		Date lastMon = dateUtil.parseDateArbitrary(lastMonday);
		Date lastSun = dateUtil.parseDateArbitrary(lastSunday);
		
		boolean isDateRight = false;
		boolean isNcsExist = false;
		//判断日期是否符合要求
		if((endTime.after(startTime) || endTime.equals(startTime)) 
				&& (lastSun.after(endTime) || lastSun.equals(endTime))
				&& (lastSun.after(startTime) || lastSun.equals(startTime))
				&& (startTime.after(lastMon) || startTime.equals(lastMon))
				&& (endTime.after(lastMon) || endTime.equals(lastMon))) {
			
			isDateRight = true;
		}
		//判断日期范围是否存在NCS数据
		long ncsCnt = rnoStructureService.queryNcsDataCountByCond(cond);
		if(ncsCnt > 0) {
			isNcsExist = true;
		}
//		System.out.println("数据验证耗时："+((System.currentTimeMillis()-s)/1000));
//		s = System.currentTimeMillis();
		//检查当前区域是否正在计算干扰矩阵
	/*	long areaId = Long.parseLong(cond.get("cityId").toString());
		cal = Calendar.getInstance();
		//获取这个周一
		cal.add(Calendar.DATE, 0*7);
		cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		String thisMonday = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(cal.getTime());
		//获取今天
	    cal = Calendar.getInstance();
	    String today = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
		boolean isCalculating = rnoStructureService
				.isCalculatingInterMartixThisArea(areaId, thisMonday, today);*/
		
		if(/*isDateRight && */isNcsExist /*&& !isCalculating*/) {
			//提交NCS干扰矩阵计算任务
			String account = (String) SessionService.getInstance().getValueByKey("userId");
			rnoStructureService.addInterMartixByNcs(cond, account);
		}
//		System.out.println("任务提交耗时："+((System.currentTimeMillis()-s)/1000));

		//测试
		isDateRight=true;  
//		isNcsExist=true;
//		isCalculating=false;
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("isDateRight", isDateRight);
		result.put("isNcsExist", isNcsExist);
//		result.put("isCalculating", isCalculating);
		HttpTools.writeToClient(gson.toJson(result));
	}
	
	/**
	 * 获取结构分析任务保存的文件路径
	 * 
	 * @author peng.jm
	 * @date 2014-8-28下午02:39:04
	 */
	public void getRenderImgDetailByjobIdAjaxForAction() {
		log.info("进入：getRenderImgDetailByjobIdAjaxForAction。jobId="+jobId+",ncsRendererType+"+ncsRendererType);
		
		Map<String,Object> result = new HashMap<String, Object>();
		
		// 获取任务的信息。
		List<Map<String,Object>> res = rnoStructureService.getStructureTaskByJobId(jobId);
		if(res.size() > 0) {
			String resultDir = "";
			if(res.get(0).get("RESULT_DIR") != null) {
				resultDir = res.get(0).get("RESULT_DIR").toString();
			} 
			log.info("结果文件所在路径：" + resultDir);
			resultDir=resultDir.replaceAll("\\\\", "/");
			String fixedStr="/op/rno/ana_result/";
			int stIdx=resultDir.indexOf(fixedStr);
			String imgPath=resultDir.substring(stIdx)+ "/image/" + jobId + "_" + ncsRendererType + ".png";
			
			//读取渲染图的数据文件
			String filePath = resultDir + "image/" + jobId + "_" + ncsRendererType + ".data";
			File dataFile = new File(filePath);		
			if (dataFile.isDirectory()) {
				log.error("请传入文件名称，不要传入路径！");
				result.put("flag", false);
			} else if (!dataFile.exists()) {
				log.error("渲染图对应的位置数据文件不存在！ path="+filePath);
				result.put("flag", false);
			} else {
				BufferedReader br = null;
				try {
					br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
				} catch (FileNotFoundException e1) {
					log.error("渲染图对应的位置数据文件不存在！path="+filePath);
					result.put("flag", false);
				}
				try {
					if(br.readLine() == null){//忽略第一行
						log.error("解析数据为空！path="+filePath);
						result.put("flag", false);
					} 
					String line;
					if((line = br.readLine()) != null) {
						String[] vs = line.split("##");
						if(vs.length < 4) {
							log.debug(line);
						}
						result.put("minLng", vs[0]);
						result.put("minLat", vs[1]);
						result.put("maxLng", vs[2]);
						result.put("maxLat", vs[3]);
					}
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
					log.error("解析出错！path="+filePath);
					result.put("flag", false);
				}
				if(result.get("flag") == null) {
					result.put("flag", true);
					result.put("filePath", imgPath);
				}
			}
		} else {
			log.error("找不到对应jobId="+jobId+"的任务！");
			result.put("flag", false);
		}
		HttpTools.writeToClient(gson.toJson(result));
	}
	/**
	 * 提交结构分析计算任务
	 * @author peng.jm
	 * @date 2014-8-26下午04:30:09
	 */
	public void submitEriAndHwStructureTaskAction() {
		log.info("进入：submitEriAndHwStructureTaskAction");
		String account = (String) SessionService.getInstance().getValueByKey(
					"userId");
		String path = ServletActionContext.getServletContext().getRealPath(
				"/op/rno/ana_result/");
		log.info("提交者=" + account + ",文件存储=" + path);
		
		RnoStructureAnalysisTask taskobj = (RnoStructureAnalysisTask)SessionService.getInstance().getValueByKey("NCSTASKINFO");
		Map<String, Object> res =null;
		if (taskobj != null) {
			//取出session里保存的任务所需信息
//			Threshold threshold = taskobj.getThreshold(); //门限值
			List<RnoThreshold> rnoThresholds=taskobj.getRnoThresholds();
			RnoStructureAnalysisTask.TaskInfo taskInfo=taskobj.getTaskInfo(); //任务信息
			
			res = rnoStructureService.submitEriAndHwStructureTask(account,
					path, rnoThresholds, taskInfo);
			//清空session
			SessionService.getInstance().rmvValueByKey("NCSTASKINFO");
		}
		String result = gson.toJson(res);
		HttpTools.writeToClient(result);	
	}
	/**
	 * 分页查询结构分析计算任务信息
	 * 
	 * @author peng.jm
	 * @date 2014-8-26下午06:38:32
	 */
	public void queryStructureAnalysisTaskByPageForAjaxAction() {
		log.info("进入：queryStructureAnalysisTaskByPageForAjaxAction。cond=" + cond
				+ ",page=" + page);
		String account = (String) SessionService.getInstance().getValueByKey("userId");
		if (page == null) {
			log.error("方法queryStructureAnalysisTaskByPageForAjaxAction的page参数为空！");
			HttpTools.writeToClient("[]");
			return;
		}

		Page newPage = page.copy();
		List<Map<String, Object>> structAnaTasks = rnoStructureService
				.queryStructureAnalysisTaskByPage(cond, newPage, account);
		String res = gson.toJson(structAnaTasks);

		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String result = "{'page':" + pstr + ",'data':" + res + "}";
		log.info("退出queryStructureAnalysisTaskByPageForAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	
	/**
	 * 停止对应jobId的执行任务
	 * 
	 * @author peng.jm
	 * @date 2014-8-27下午05:15:46
	 */
	public void stopJobByJobIdForAjaxAction() {
		log.info("进入：stopJobByJobIdForAjaxAction。jobId=" + jobId);
		String account = (String) SessionService.getInstance().getValueByKey("userId");
		boolean flag = rnoStructureService.stopJobByJobId(jobId, account);
		String result = "{'flag':" + flag +"}";
		log.info("退出stopJobByJobIdForAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	
	/**
	 * 下载结构分析结果文件
	 * @return
	 * @author peng.jm
	 * @date 2014-8-27下午05:46:22
	 */
	public String downloadStructureFileAction() {
		log.info("下载结构分析结果文件。任务jobId=" + jobId);
		// 获取任务的信息。
		List<Map<String,Object>> res = rnoStructureService.getStructureTaskByJobId(jobId);
		if(res.size() <= 0) {
			log.error("找不到对应jobId="+jobId+"的任务！");
			error = "<h>不存在指定的分析任务!无法导出其分析报告！</h>";
			return "fail";
		}
		String resultDir = "";
		String dlFileName = "";
		if(res.get(0).get("RESULT_DIR") != null) {
			resultDir = res.get(0).get("RESULT_DIR").toString();
		} 
		if(res.get(0).get("DL_FILE_NAME") != null) {
			dlFileName = res.get(0).get("DL_FILE_NAME").toString();
		} 
		
		log.info("结果文件所在路径：" + resultDir + ", 文件名：" + dlFileName);
		try {
			super.setFileName(new String(dlFileName.getBytes(),"iso-8859-1"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		// 判断文件是否存在
		String realPath = resultDir + dlFileName;
		File f = new File(realPath);
		if (!f.exists()) {
			log.error("任务jobId[" + jobId + "]对应的结果文件：" + dlFileName + "不存在！");
			error = "所需要的任务结果文件不存在！是否重新计算？"
					+ "<form action='' type='post'>"
					+ "<input type='hidden' name='jobId' value='" + jobId
					+ "'/>" + "<input type='submit' value='重新计算' />"
					+ "</form>";
			return "fail";
		}
		try {
			reportInputStream=new FileInputStream(realPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "fail";
		}
		return "success";
	}
	/**
* 
* @title 在创建新任务消息过程中主导页面跳转
* @author chao.xj
* @date 2014-7-15上午11:22:02
* @company 怡创科技
* @version 1.2
* goNcsNewTaskInfoPageAndOperateSessionForAjaxAction
*/
public String stepByStepOperateStructureTaskInfoPageForAjaxAction() {
log.debug("进入方法: stepByStepOperateStructureTaskInfoPageForAjaxAction taskInfoType="+taskInfoType);
if(taskInfoType!=null&&"taskInfoForward".equals(taskInfoType)){
	// 加载区域相关信息
	initAreaList();
	//STRUCTANA默认值保存到session
//	Threshold thresholdObj = rnoStructureService.getThresholdByModType("STRUCTANA");

	//List<RnoThreshold> rnoThresholds=rnoStructureService.getThresholdsByModuleType("STRUCTANA");
	RnoStructureAnalysisTask taskobj=(RnoStructureAnalysisTask)SessionService.getInstance().getValueByKey("NCSTASKINFO");
//	taskobj.setThresholdDefault(thresholdObj);
	List<RnoThreshold> rnoThresholds;
	if(taskobj.getRnoThresholds() == null) {
		rnoThresholds=rnoStructureService.getThresholdsByModuleType("STRUCTANA");
		taskobj.setRnoThresholds(rnoThresholds);
	} else {
		rnoThresholds = taskobj.getRnoThresholds();
	}
	//获取不重复的分组条件
	HashSet<String> conditionGroups = new HashSet<String>();
	for (RnoThreshold one : rnoThresholds) {
		conditionGroups.add(one.getConditionGroup());
	}
//	List<List<RnoThreshold>> groupRnoThresholds = new ArrayList<List<RnoThreshold>>();
	Map<Long,List<RnoThreshold>> groupRnoThresholds=new TreeMap<Long, List<RnoThreshold>>();
	List<RnoThreshold> groupRnoThreshold;
	long orderNum=0;
	//循环分组条件，加入参数对象
	for (String cond : conditionGroups) {
		groupRnoThreshold = new ArrayList<RnoThreshold>();
		for (RnoThreshold rnoThreshold : rnoThresholds) {
			if(cond.equals(rnoThreshold.getConditionGroup())) {
				groupRnoThreshold.add(rnoThreshold);
				orderNum=rnoThreshold.getOrderNum();
			}
		}
//		groupRnoThresholds.add(groupRnoThreshold);
		groupRnoThresholds.put(orderNum, groupRnoThreshold);
		
	}
	
	taskobj.setGroupThresholds(groupRnoThresholds); //用于分组显示
	return "taskInfoForward";
	
}else if(taskInfoType!=null&&"paramInfoBack".equals(taskInfoType)){
	initAreaList();// 加载区域相关信息
	return "paramInfoBack";
	
}else if(taskInfoType!=null&&"paramInfoForward".equals(taskInfoType)){

	RnoStructureAnalysisTask taskobj = (RnoStructureAnalysisTask)SessionService.getInstance().getValueByKey("NCSTASKINFO");
	long cityId = taskobj.getTaskInfo().getCityId();
	String startTime = taskobj.getTaskInfo().getStartTime();
	String endTime = taskobj.getTaskInfo().getEndTime();
	//查询爱立信和华为在任务的数据详情
	List<Map<String, Object>> eriInfo = rnoStructureService
				.getEriDataDetailsByCityIdAndDate(cityId, startTime, endTime);
	List<Map<String, Object>> hwInfo = rnoStructureService
				.getHWDataDetailsByCityIdAndDate(cityId, startTime, endTime);
	//保存进session
	taskobj.setEriInfo(eriInfo);
	taskobj.setHwInfo(hwInfo);
	return "paramInfoForward";
	
}else if(taskInfoType!=null&&"overviewInfoBack".equals(taskInfoType)){
	initAreaList();// 加载区域相关信息
	return "overviewInfoBack";
/*}else if(taskInfoType!=null&&"mrrInfoForward".equals(taskInfoType)){
	return "mrrInfoForward";
}else if(taskInfoType!=null&&"mrrInfoBack".equals(taskInfoType)){
	initAreaList();// 加载区域相关信息
	return "mrrInfoBack";
}else if(taskInfoType!=null&&"submitTask".equals(taskInfoType)){
	return "submitTask";*/
}else{
	//先清空该 key下的属性值
	SessionService.getInstance().rmvValueByKey("NCSTASKINFO");
	RnoStructureAnalysisTask taskobj=new RnoStructureAnalysisTask();
	SessionService.getInstance().setValueByKey("NCSTASKINFO", taskobj);

	initAreaList();// 加载区域相关信息
	Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE,-5);
    preFiveDayTime = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(cal.getTime());
	return "goTaskInfoPage";
}

}
/**
 * 
 * @title 在创建新任务消息过程中主导存储结构分析任务对象信息（下一步操作和提交任务操作）
 * @author chao.xj
 * @date 2014-7-15下午3:57:49
 * @company 怡创科技
 * @version 1.2
 */
public void storageStructureAnalysisTaskObjInfoForAjaxAction() {
	log.debug("进入方法: storageStructureAnalysisTaskObjInfoForAjaxAction。");
	RnoStructureAnalysisTask taskobj=(RnoStructureAnalysisTask)SessionService.getInstance().getValueByKey("NCSTASKINFO");
	String result ="";
	if(taskobj!=null&&"taskInfoForward".equals(taskInfoType)){
		try {
			//仅仅是存储任务消息内容
			RnoStructureAnalysisTask.TaskInfo ti =taskobj.getTaskInfo();
			ti.setTaskName(taskName);
			ti.setTaskDesc(taskDescription);
			ti.setStartTime(meaStartTime);
			ti.setEndTime(meaEndTime);
			ti.setProvinceId(provinceId);
			ti.setCityId(cityId);
			ti.setCityName(cityName);
			ti.setProvinceName(provinceName);
			ti.setBusDataType(busDataType);
			ti.setCalProcedure(calProcedure);
			result = "{'state':'true'}";
		} catch (Exception e) {
			result = "{'state':'false'}";
			e.printStackTrace();
		}
		HttpTools.writeToClient(result);
	}
	if(taskobj != null && "paramInfoBack".equals(taskInfoType)){
		try {
			/*Threshold thresholdObj = taskobj.getThreshold();
			//System.out.println(threshold);
			thresholdObj.setSameFreqInterThreshold(threshold.get("sameFreqInterThreshold").toString());
			thresholdObj.setOverShootingIdealDisMultiple(threshold.get("overShootingIdealDisMultiple").toString());
			thresholdObj.setBetweenCellIdealDisMultiple(threshold.get("betweenCellIdealDisMultiple").toString());
			thresholdObj.setCellCheckTimesIdealDisMultiple(threshold.get("cellCheckTimesIdealDisMultiple").toString());
			thresholdObj.setCellCheckTimesSameFreqInterThreshold(threshold.get("cellCheckTimesSameFreqInterThreshold").toString());
			thresholdObj.setCellIdealDisReferenceCellNum(threshold.get("cellIdealDisReferenceCellNum").toString());
			thresholdObj.setGsm900CellFreqNum(threshold.get("gsm900CellFreqNum").toString());
			thresholdObj.setGsm1800CellFreqNum(threshold.get("gsm1800CellFreqNum").toString());
			thresholdObj.setGsm900CellIdealCapacity(threshold.get("gsm900CellIdealCapacity").toString());
			thresholdObj.setGsm1800CellIdealCapacity(threshold.get("gsm1800CellIdealCapacity").toString());
			thresholdObj.setDlCoverMinimumSignalStrengthThreshold(threshold.get("dlCoverMinimumSignalStrengthThreshold").toString());
			thresholdObj.setUlCoverMinimumSignalStrengthThreshold(threshold.get("ulCoverMinimumSignalStrengthThreshold").toString());
			thresholdObj.setInterFactorMostDistant(threshold.get("interFactorMostDistant").toString());
			thresholdObj.setInterFactorSameAndAdjFreqMinimumThreshold(threshold.get("interFactorSameAndAdjFreqMinimumThreshold").toString());
			taskobj.setThreshold(thresholdObj);*/
			//List<RnoThreshold> rnoThresholds=new ArrayList<RnoThreshold>();
			List<RnoThreshold> rnoThresholds = taskobj.getRnoThresholds();
		/*	Iterator<Entry<String, String>> thresholdIterator=threshold.entrySet().iterator();
			while (thresholdIterator.hasNext()) {
				RnoThreshold rnoThreshold=new RnoThreshold();
				Entry<String, String> entry=thresholdIterator.next();
				rnoThreshold.setCode(entry.getKey());
				rnoThreshold.setDefaultVal(entry.getValue());
				rnoThresholds.add(rnoThreshold);
				System.out.println(rnoThreshold.getCode()+":"+rnoThreshold.getDefaultVal());
			}
			taskobj.setRnoThresholds(rnoThresholds);*/
			for (RnoThreshold rnoThreshold : rnoThresholds) {
				String code=rnoThreshold.getCode();
				for(String key:threshold.keySet()){
					if(key.toUpperCase().equals(code)){
						rnoThreshold.setDefaultVal(threshold.get(key));
						//System.out.println(key+" : "+threshold.get(key));
					}
				}
			}
			//获取不重复的分组条件
			HashSet<String> conditionGroups = new HashSet<String>();
			for (RnoThreshold one : rnoThresholds) {
				conditionGroups.add(one.getConditionGroup());
			}
//			List<List<RnoThreshold>> groupRnoThresholds = new ArrayList<List<RnoThreshold>>();
			Map<Long,List<RnoThreshold>> groupRnoThresholds=new TreeMap<Long, List<RnoThreshold>>();
			long orderNum=0;
			List<RnoThreshold> groupRnoThreshold;
			//循环分组条件，加入参数对象
			for (String cond : conditionGroups) {
				groupRnoThreshold = new ArrayList<RnoThreshold>();
				for (RnoThreshold rnoThreshold : rnoThresholds) {
					if(cond.equals(rnoThreshold.getConditionGroup())) {
						groupRnoThreshold.add(rnoThreshold);
						orderNum=rnoThreshold.getOrderNum();
					}
				}
//				groupRnoThresholds.add(groupRnoThreshold);
				groupRnoThresholds.put(orderNum, groupRnoThreshold);
			}
			taskobj.setGroupThresholds(groupRnoThresholds);
			
			result = "{'state':'true'}";
		} catch (Exception e) {
			result = "{'state':'false'}";
			e.printStackTrace();
		}
		HttpTools.writeToClient(result);
	}
	if(taskobj != null && "paramInfoForward".equals(taskInfoType)){
		try {
			/*Threshold thresholdObj = taskobj.getThreshold();
			
			//System.out.println(threshold);
			thresholdObj.setSameFreqInterThreshold(threshold.get("sameFreqInterThreshold").toString());
			thresholdObj.setOverShootingIdealDisMultiple(threshold.get("overShootingIdealDisMultiple").toString());
			thresholdObj.setBetweenCellIdealDisMultiple(threshold.get("betweenCellIdealDisMultiple").toString());
			thresholdObj.setCellCheckTimesIdealDisMultiple(threshold.get("cellCheckTimesIdealDisMultiple").toString());
			thresholdObj.setCellCheckTimesSameFreqInterThreshold(threshold.get("cellCheckTimesSameFreqInterThreshold").toString());
			thresholdObj.setCellIdealDisReferenceCellNum(threshold.get("cellIdealDisReferenceCellNum").toString());
			thresholdObj.setGsm900CellFreqNum(threshold.get("gsm900CellFreqNum").toString());
			thresholdObj.setGsm1800CellFreqNum(threshold.get("gsm1800CellFreqNum").toString());
			thresholdObj.setGsm900CellIdealCapacity(threshold.get("gsm900CellIdealCapacity").toString());
			thresholdObj.setGsm1800CellIdealCapacity(threshold.get("gsm1800CellIdealCapacity").toString());
			thresholdObj.setDlCoverMinimumSignalStrengthThreshold(threshold.get("dlCoverMinimumSignalStrengthThreshold").toString());
			thresholdObj.setUlCoverMinimumSignalStrengthThreshold(threshold.get("ulCoverMinimumSignalStrengthThreshold").toString());
			thresholdObj.setInterFactorMostDistant(threshold.get("interFactorMostDistant").toString());
			thresholdObj.setInterFactorSameAndAdjFreqMinimumThreshold(threshold.get("interFactorSameAndAdjFreqMinimumThreshold").toString());
			taskobj.setThreshold(thresholdObj);
			*/
			List<RnoThreshold> rnoThresholds=taskobj.getRnoThresholds();
			/*Iterator<Entry<String, String>> thresholdIterator=threshold.entrySet().iterator();
			while (thresholdIterator.hasNext()) {
				RnoThreshold rnoThreshold=new RnoThreshold();
				Entry<String, String> entry=thresholdIterator.next();
				rnoThreshold.setCode(entry.getKey());
				rnoThreshold.setDefaultVal(entry.getValue());
				rnoThresholds.add(rnoThreshold);
			}*/
			for (RnoThreshold rnoThreshold : rnoThresholds) {
				String code=rnoThreshold.getCode();
				for(String key:threshold.keySet()){
					if(key.toUpperCase().equals(code)){
						rnoThreshold.setDefaultVal(threshold.get(key));
						//System.out.println(key+" : "+threshold.get(key));
					}
				}
			}
			//获取不重复的分组条件
			HashSet<String> conditionGroups = new HashSet<String>();
			for (RnoThreshold one : rnoThresholds) {
				conditionGroups.add(one.getConditionGroup());
			}
//			List<List<RnoThreshold>> groupRnoThresholds = new ArrayList<List<RnoThreshold>>();
			Map<Long,List<RnoThreshold>> groupRnoThresholds=new TreeMap<Long, List<RnoThreshold>>();
			long orderNum=0;
			List<RnoThreshold> groupRnoThreshold;
			//循环分组条件，加入参数对象
			for (String cond : conditionGroups) {
				groupRnoThreshold = new ArrayList<RnoThreshold>();
				for (RnoThreshold rnoThreshold : rnoThresholds) {
					if(cond.equals(rnoThreshold.getConditionGroup())) {
						groupRnoThreshold.add(rnoThreshold);
						orderNum=rnoThreshold.getOrderNum();
					}
				}
//				groupRnoThresholds.add(groupRnoThreshold);
				groupRnoThresholds.put(orderNum, groupRnoThreshold);
			}
			taskobj.setGroupThresholds(groupRnoThresholds);
			taskobj.setRnoThresholds(rnoThresholds);
			result = "{'state':'true'}";
		} catch (Exception e) {
			result = "{'state':'false'}";
			e.printStackTrace();
		}
		HttpTools.writeToClient(result);
	}
	
/*	if(taskobj!=null&&"ncsInfoForward".equals(taskInfoType)){
		List<Map<String, Object>> ncstimespanlist=rnoStructureService.queryNcsOrMrrTimeSpanByIds(ncsIds, "ncs");
		String ncsTimeSpan=ncstimespanlist.get(0).get("TIMESPAN").toString();
		try {
			//仅仅是存储NCS消息内容
			RnoStructureAnalysisTask.NcsInfo ni=taskobj.getNcsInfo();
			ni.setNcsIds(ncsIds);
			ni.setNcsFileNum(ncsFileNum);
			ni.setNcsAreaCoverage(areaName);
			ni.setNcsTimeSpan(ncsTimeSpan);
			ni.setNcsAreaId(areaId);
			ni.setNcsCityId(cityId);
			ni.setNcsLevel(level);
			result = "{'state':'true'}";
		} catch (Exception e) {
			result = "{'state':'false'}";
			e.printStackTrace();
		}
		HttpTools.writeToClient(result);
	}
	if(taskobj!=null&&"mrrInfoForward".equals(taskInfoType)){
		log.debug("mrrIds:"+mrrIds);
		List<Map<String, Object>> mrrtimespanlist=rnoStructureService.queryNcsOrMrrTimeSpanByIds(mrrIds, "mrr");
		String mrrTimeSpan=mrrtimespanlist.get(0).get("TIMESPAN").toString();
		try {
			//仅仅是存储MRR消息内容
			RnoStructureAnalysisTask.MrrInfo mi=taskobj.getMrrInfo();
			mi.setMrrIds(mrrIds);
			mi.setMrrFileNum(mrrFileNum);
			mi.setMrrAreaCoverage(areaName);
			mi.setMrrTimeSpan(mrrTimeSpan);
			mi.setMrrAreaId(areaId);
			mi.setMrrCityId(cityId);
			mi.setMrrLevel(level);
			//存储成功后就该 跳转至所有信息总预览页面，然后在页面中将session存储的对象取出来呈现至页面中去
			result = "{'state':'true'}";
		} catch (Exception e) {
			result = "{'state':'false'}";
			e.printStackTrace();
		}
		HttpTools.writeToClient(result);
	}
	if(taskobj!=null&&"submitTask".equals(taskInfoType)){
		RnoStructureAnalysisTask.NcsInfo ni=taskobj.getNcsInfo();
		RnoStructureAnalysisTask.MrrInfo mi=taskobj.getMrrInfo();
		ncsIds=ni.getNcsIds();
		mrrIds=mi.getMrrIds();
		//真正提交后台运算
		Map<String, Object> res = rnoStructureService.checkNcsTaskByNcsIds(ncsIds);
		result = gson.toJson(res);
		HttpTools.writeToClient(result);	
	}
	*/
}
/**
 * 
 * @title 查询mrr记录的情况
 * @author chao.xj
 * @date 2014-7-23上午9:30:38
 * @company 怡创科技
 * @version 1.2
 */
public void queryMrrDescpByPageForAjaxAction() {
	log.info("进入方法：queryMrrDescpByPageForAjaxAction.page=" + page
			+ ",cond=" + cond);
	Map<String, Object> result = new HashMap<String, Object>();
	if (page == null) {
		log.error("方法queryMrrDescpByPageForAjaxAction的page参数为空！");
		HttpTools.writeToClient(gson.toJson(result));
		return;
	}

	Page newPage = page.copy();
	List<Map<String, Object>> descs = rnoStructureService
			.queryMrrDescriptorByPage(cond, newPage);

	log.info("计算以后，page=" + newPage);
	// /
	int totalCnt = newPage.getTotalCnt();
	newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
			+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
	newPage.setForcedStartIndex(-1);

	result.put("page", newPage);
	result.put("data", descs);
	//@author chao.xj  2014-7-24 下午5:45:22
	RnoStructureAnalysisTask analysisTask=(RnoStructureAnalysisTask)SessionService.getInstance().getValueByKey("NCSTASKINFO");
	RnoStructureAnalysisTask.NcsInfo ncsInfo=analysisTask.getNcsInfo();
	String ncsarea=ncsInfo.getNcsAreaCoverage();
	result.put("area", ncsarea);
	HttpTools.writeToClient(gson.toJson(result));
}
/**
 * 
 * @title 根据id删除mrr记录
 * @author chao.xj
 * @date 2014-7-23上午10:13:38
 * @company 怡创科技
 * @version 1.2
 */
public void deleteMrrRecByIdForAjaxAction(){
	log.info("进入方法：deleteMrrRecByIdForAjaxAction.mrrId="+mrrId);
	rnoStructureService.deleteMrrRecById(mrrId);
	HttpTools.writeToClient("success");
}
/**
 * 
 * @title 提交ncs与mrr汇总计算任务
 * @author chao.xj
 * @date 2014-7-23下午12:08:40
 * @company 怡创科技
 * @version 1.2
 */
public void submitNcsAndMrrAnalysisTaskAction() {
	log.info("进入：submitNcsAndMrrAnalysisTaskAction");
	String account = (String) SessionService.getInstance().getValueByKey(
			"userId");
	String path = ServletActionContext.getServletContext().getRealPath(
			"/op/rno/ana_result/");

	log.info("creator=" + account + ",savepath=" + path);
	//@author chao.xj  2014-7-18 下午12:19:45
	RnoStructureAnalysisTask taskobj=(RnoStructureAnalysisTask)SessionService.getInstance().getValueByKey("NCSTASKINFO");
	Map<String, Object> res =null;
	if (taskobj!=null) {
		
		RnoStructureAnalysisTask.NcsInfo ncsInfo=taskobj.getNcsInfo();
		RnoStructureAnalysisTask.MrrInfo mrrInfo=taskobj.getMrrInfo();
		RnoStructureAnalysisTask.TaskInfo taskInfo=taskobj.getTaskInfo();
		res = rnoStructureService.submitRnoNcsAndMrrAnalysisTask(
				account,path,ncsInfo, mrrInfo, taskInfo);
		//清空session
		SessionService.getInstance().rmvValueByKey("NCSTASKINFO");
	}
	String result = gson.toJson(res);
	HttpTools.writeToClient(result);
}
/**
 * 
 * @title 在创建新任务消息过程中主导存储结构分析任务对象信息（填写任务信息及参数配置即阈值门限存储共两步）
 * @author chao.xj
 * @date 2014-8-16下午6:20:08
 * @company 怡创科技
 * @version 1.2
 */
public void storageStructAnaTaskInfoToSessionForAjaxAction() {
	log.debug("进入方法: storageStructAnaTaskInfoToSessionForAjaxAction cityid="+cityId);
	
	StructAnaTaskInfo taskobj=(StructAnaTaskInfo)SessionService.getInstance().getValueByKey("STRUCTTASKINFO");
	SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	String result ="";
	if(taskobj!=null&&"taskInfoForward".equals(taskInfoType)){
		//判断HW与ERI数据是否为空
		boolean eriFlag=true;//eri是否满足要求
		boolean hwFlag=true;//hw是否满足要求
		Map<String, Object> eriTimeRangeMap=rnoStructureService.getStructAnaSummaryInfoForTimeRange(cityId, meaStartTime, meaEndTime, "ERI");
		Map<String, Object> hwTimeRangeMap=rnoStructureService.getStructAnaSummaryInfoForTimeRange(cityId, meaStartTime, meaEndTime, "HW");
		Iterator<Entry<String, Object>> eriTimeRangeIterator=eriTimeRangeMap.entrySet().iterator();
		while (eriTimeRangeIterator.hasNext()) {
			Entry<String, Object> entry=eriTimeRangeIterator.next();
			String val=entry.getValue().toString();
			if (val.equals("0")) {
				eriFlag=false;
				break;
			}
		}
		Iterator<Entry<String, Object>> hwTimeRangeIterator=hwTimeRangeMap.entrySet().iterator();
		while (hwTimeRangeIterator.hasNext()) {
			Entry<String, Object> entry=hwTimeRangeIterator.next();
			String val=entry.getValue().toString();
			if (val.equals("0")) {
				hwFlag=false;
				break;
			}
		}
		if (!hwFlag&&!eriFlag) {
			result = "{'state':'false'}";
			HttpTools.writeToClient(result);
			return;
		}
		//设置是否存储满足条件 的华为与爱立信数据
		taskobj.setExistEriDate(eriFlag);
		taskobj.setExistHwDate(hwFlag);
		
		Calendar ca = Calendar.getInstance();
		Date startDate;
		Date endDate;
		try {
			//将起始时间存储起来
			taskobj.setStartDate(formatter.parse(meaStartTime));
			taskobj.setEndDate(formatter.parse(meaEndTime));
			
			List<EriInfo> eriInfos=new ArrayList<EriInfo>();
			List<HwInfo> hwInfos=new ArrayList<HwInfo>();
			startDate = formatter.parse(meaStartTime);
			endDate =formatter.parse(meaEndTime);
			Date curDate = startDate;
			//日期循环取数据
			while(curDate.compareTo(endDate)<=0){
			      ca.setTime(curDate);
			      //业务处理...
			      ca.add(Calendar.DATE, 1);
			      curDate = ca.getTime();
			      java.sql.Date date=new java.sql.Date(curDate.getTime());
			      //处理eri数据
			      if(eriFlag){
			    	  Map<String, Object> eriOneDayMap=rnoStructureService.getStructAnaSummaryInfoForOneDay(cityId, date.toString(), "ERI");
				      Iterator<Entry<String, Object>> eriIterator=eriOneDayMap.entrySet().iterator();
				      EriInfo eriInfo=new EriInfo();
						while (eriIterator.hasNext()) {
							Entry<String, Object> entry=eriIterator.next();
							String key=entry.getKey();
							if(key.equals("NCSCELLCOUNT")){
								eriInfo.setNcsCellNum(Integer.parseInt(entry.getValue().toString()));
								}
							if(key.equals("SYSCELLCOUNT")){
								eriInfo.setAreaCfgCellNum(Integer.parseInt(entry.getValue().toString()));
								}
							if(key.equals("NCSOFBSCCOUNT")){
								eriInfo.setNcsOfBscNum(Integer.parseInt(entry.getValue().toString()));
								}
							if(key.equals("MRRFILECOUNT")){
								eriInfo.setMrrFileNum(Integer.parseInt(entry.getValue().toString()));
								}
							if(key.equals("MRROFBSCCOUNT")){
								eriInfo.setMrrOfBscNum(Integer.parseInt(entry.getValue().toString()));
								}
							if(key.equals("MRRCELLCOUNT")){
								eriInfo.setMrrCellNum(Integer.parseInt(entry.getValue().toString()));
								}
							if(key.equals("SYSOFBSCCOUNT")){
								eriInfo.setAreaCfgBscNum(Integer.parseInt(entry.getValue().toString()));
								}
							if(key.equals("NCSFILECOUNT")){
								eriInfo.setNcsFileNum(Integer.parseInt(entry.getValue().toString()));
								}
						}
						eriInfos.add(eriInfo);
			      }
				//处理hw数据
			    if (hwFlag) {
			    	 Map<String, Object> hwOneDayMap=rnoStructureService.getStructAnaSummaryInfoForOneDay(cityId, date.toString(), "HW");
						Iterator<Entry<String, Object>> hwIterator=hwOneDayMap.entrySet().iterator();
							HwInfo hwInfo=new HwInfo();
							while (hwIterator.hasNext()) {
								Entry<String, Object> entry=hwIterator.next();
								String key=entry.getKey();
								if(key.equals("NCSCELLCOUNT")){
									hwInfo.setNcsCellNum(Integer.parseInt(entry.getValue().toString()));
									}
								if(key.equals("SYSCELLCOUNT")){
									hwInfo.setAreaCfgCellNum(Integer.parseInt(entry.getValue().toString()));
									}
								if(key.equals("NCSOFBSCCOUNT")){
									hwInfo.setNcsOfBscNum(Integer.parseInt(entry.getValue().toString()));
									}
								if(key.equals("MRROFBSCCOUNT")){
									hwInfo.setMrrOfBscNum(Integer.parseInt(entry.getValue().toString()));
									}
								if(key.equals("MRRCELLCOUNT")){
									hwInfo.setMrrCellNum(Integer.parseInt(entry.getValue().toString()));
									}
								if(key.equals("SYSOFBSCCOUNT")){
									hwInfo.setAreaCfgBscNum(Integer.parseInt(entry.getValue().toString()));
									}
							}
							hwInfos.add(hwInfo);
				}
			}
			taskobj.setEriInfos(eriInfos);
			taskobj.setHwInfos(hwInfos);
			result = "{'state':'true'}";
			HttpTools.writeToClient(result);
		} catch (ParseException e) {
			result = "{'state':'false'}";
			HttpTools.writeToClient(result);
			e.printStackTrace();
		}
	}else if(taskobj!=null&&"thresholdForward".equals(taskInfoType)){
		/*Iterator<Entry<String, String>> thresholdIterator=threshold.entrySet().iterator();
		Threshold threshold=new Threshold();
		try {
			while (thresholdIterator.hasNext()) {
				Entry<String, String> entry=thresholdIterator.next();
				String key=entry.getKey();
				String val=entry.getValue();
				if("betweencellidealdismultiple".toUpperCase().equals(key)){
					threshold.setBetweenCellIdealDisMultiple(val);
				}
				if("cellCheckTimesIdealDisMultiple".toUpperCase().equals(key)){
					threshold.setCellCheckTimesIdealDisMultiple(val);
				}
				if("cellCheckTimesSameFreqInterThreshold".toUpperCase().equals(key)){
					threshold.setCellCheckTimesSameFreqInterThreshold(val);
				}
				if("cellIdealDisReferenceCellNum".toUpperCase().equals(key)){
					threshold.setCellIdealDisReferenceCellNum(val);
				}
				if("dlCoverMinimumSignalStrengthThreshold".toUpperCase().equals(key)){
					threshold.setDlCoverMinimumSignalStrengthThreshold(val);
				}
				if("gsm1800CellFreqNum".toUpperCase().equals(key)){
					threshold.setGsm1800CellFreqNum(val);
				}
				if("gsm1800CellIdealCapacity".toUpperCase().equals(key)){
					threshold.setGsm1800CellIdealCapacity(val);
				}
				if("gsm900CellFreqNum".toUpperCase().equals(key)){
					threshold.setGsm900CellFreqNum(val);
				}
				if("gsm900CellIdealCapacity".toUpperCase().equals(key)){
					threshold.setGsm900CellIdealCapacity(val);
				}
				if("interFactorMostDistant".toUpperCase().equals(key)){
					threshold.setInterFactorMostDistant(val);
				}
				if("interFactorSameAndAdjFreqMinimumThreshold".toUpperCase().equals(key)){
					threshold.setInterFactorSameAndAdjFreqMinimumThreshold(val);
				}
				if("overShootingIdealDisMultiple".toUpperCase().equals(key)){
					threshold.setOverShootingIdealDisMultiple(val);
				}
				if("sameFreqInterThreshold".toUpperCase().equals(key)){
					threshold.setSameFreqInterThreshold(val);
				}
				if("ulCoverMinimumSignalStrengthThreshold".toUpperCase().equals(key)){
					threshold.setUlCoverMinimumSignalStrengthThreshold(val);
				}
			}
			taskobj.setThreshold(threshold);*/
		try {
		List<RnoThreshold> rnoThresholds=new ArrayList<RnoThreshold>();
		Iterator<Entry<String, String>> thresholdIterator=threshold.entrySet().iterator();
		while (thresholdIterator.hasNext()) {
			RnoThreshold rnoThreshold=new RnoThreshold();
			Entry<String, String> entry=thresholdIterator.next();
			rnoThreshold.setCode(entry.getKey());
			rnoThreshold.setDefaultVal(entry.getValue());
			rnoThresholds.add(rnoThreshold);
		}
		taskobj.setThresholds(rnoThresholds);
			result = "{'state':'true'}";
			HttpTools.writeToClient(result);
		} catch (Exception e) {
			result = "{'state':'false'}";
			HttpTools.writeToClient(result);
			e.printStackTrace();
		}
	}
}
/**
 * 
 * @title 在创建结构分析新任务消息过程中主导页面跳转
 * @return
 * @author chao.xj
 * @date 2014-8-16下午6:29:32
 * @company 怡创科技
 * @version 1.2
 */
public String stepByStepOperateStructAnaTaskInfoPageForAjaxAction() {
	log.debug("进入方法: stepByStepOperateStructAnaTaskInfoPageForAjaxAction taskInfoType="+taskInfoType);
	if(taskInfoType!=null&&"taskInfoForward".equals(taskInfoType)){
		initAreaList();// 加载区域相关信息
		return "taskInfoForward";
	}else if(taskInfoType!=null&&"thresholdForward".equals(taskInfoType)){
		return "thresholdForward";
	}else if(taskInfoType!=null&&"thresholdBack".equals(taskInfoType)){
		initAreaList();// 加载区域相关信息
		return "thresholdBack";
	}else if(taskInfoType!=null&&"overviewInfoBack".equals(taskInfoType)){
		return "overviewInfoBack";
	}else if(taskInfoType!=null&&"submitTask".equals(taskInfoType)){
		return "submitTask";
	}else{
		//先清空该 key下的属性值
		SessionService.getInstance().rmvValueByKey("STRUCTTASKINFO");
		StructAnaTaskInfo taskobj=new StructAnaTaskInfo();
		SessionService.getInstance().setValueByKey("STRUCTTASKINFO", taskobj);
		return "goTaskInfoPage";
	}
	
}

	/**
	 * 
	 * @title 初始化lte干扰计算页面
	 * @return
	 * @author chao.xj
	 * @date 2015-3-26下午1:51:10
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public String initLteInterferCalcPageAction() {
		// 取消任务时
		// 清空session
		// SessionService.getInstance().rmvValueByKey("NCSTASKINFO");
		initAreaList();// 加载区域相关信息
		return "success";
	}
	
	/**
	 * 
	 * @title 在创建lte干扰计算新任务消息过程中主导页面跳转
	 * @return
	 * @author chao.xj
	 * @date 2015-3-26下午2:29:35
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public String stepByStepOperateLteInterCalcTaskInfoPageForAjaxAction() {
		log.debug("进入方法: stepByStepOperateLteInterCalcTaskInfoPageForAjaxAction taskInfoType="
				+ taskInfoType);
		if (taskInfoType != null && "taskInfoForward".equals(taskInfoType)) {
			// 加载区域相关信息
			initAreaList();
		
			RnoLteInterferCalcTask taskobj = (RnoLteInterferCalcTask) SessionService
					.getInstance().getValueByKey("MRTASKINFO");
			// taskobj.setThresholdDefault(thresholdObj);
			List<RnoThreshold> rnoThresholds;
			if (taskobj.getRnoThresholds() == null) {
				rnoThresholds = rnoStructureService
						.getThresholdsByModuleType("LTEINTERFERCALC");
				taskobj.setRnoThresholds(rnoThresholds);
			} else {
				rnoThresholds = taskobj.getRnoThresholds();
			}
			// 获取不重复的分组条件
			HashSet<String> conditionGroups = new HashSet<String>();
			for (RnoThreshold one : rnoThresholds) {
				conditionGroups.add(one.getConditionGroup());
			}
			// List<List<RnoThreshold>> groupRnoThresholds = new
			// ArrayList<List<RnoThreshold>>();
			Map<Long, List<RnoThreshold>> groupRnoThresholds = new TreeMap<Long, List<RnoThreshold>>();
			List<RnoThreshold> groupRnoThreshold;
			long orderNum = 0;
			// 循环分组条件，加入参数对象
			for (String cond : conditionGroups) {
				groupRnoThreshold = new ArrayList<RnoThreshold>();
				for (RnoThreshold rnoThreshold : rnoThresholds) {
					if (cond.equals(rnoThreshold.getConditionGroup())) {
						groupRnoThreshold.add(rnoThreshold);
						orderNum = rnoThreshold.getOrderNum();
					}
				}
				groupRnoThresholds.put(orderNum, groupRnoThreshold);
			}

			taskobj.setGroupThresholds(groupRnoThresholds); // 用于分组显示
			return "taskInfoForward";

		} else if (taskInfoType != null && "paramInfoBack".equals(taskInfoType)) {
			initAreaList();// 加载区域相关信息
			return "paramInfoBack";

		} else if (taskInfoType != null		&& "paramInfoForward".equals(taskInfoType)) {

			RnoLteInterferCalcTask taskobj = (RnoLteInterferCalcTask) SessionService
					.getInstance().getValueByKey("MRTASKINFO");
			long cityId = taskobj.getTaskInfo().getCityId();
			String startTime = taskobj.getTaskInfo().getStartTime();
			String endTime = taskobj.getTaskInfo().getEndTime();
			// 查询爱立信和中兴在任务的数据详情
			List<Map<String, Object>> eriInfo = rnoStructureService
					.getDataRecordFromHbase(cityId, startTime, endTime, "ERI");
			List<Map<String, Object>> zteInfo = rnoStructureService
					.getDataRecordFromHbase(cityId, startTime, endTime, "ZTE");
			// 保存进session
			taskobj.setEriInfo(eriInfo);
			taskobj.setZteInfo(zteInfo);
			return "paramInfoForward";

		} else if (taskInfoType != null		&& "overviewInfoBack".equals(taskInfoType)) {
			initAreaList();// 加载区域相关信息
			return "overviewInfoBack";
		} else {
			// 先清空该 key下的属性值
			SessionService.getInstance().rmvValueByKey("MRTASKINFO");
			RnoLteInterferCalcTask taskobj = new RnoLteInterferCalcTask();
			SessionService.getInstance().setValueByKey("MRTASKINFO", taskobj);

			initAreaList();// 加载区域相关信息
			
			Calendar calbeg = Calendar.getInstance();
			Calendar calend = Calendar.getInstance();
/*			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -5);
			preFiveDayTime = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(cal.getTime());*/
			
			//n为调整的天数，0为今天，1为明天，2为后天，-1为昨天，-2为前天，依次类推
			
			int n = -5; //开始时间默认设置为5天前
			calbeg.add(Calendar.DATE, n);
			taskBeginTime = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(calbeg.getTime());		  
			n = -1;  //结束时间默认为昨天
			calend.add(Calendar.DATE, n);
			taskEndTime = new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(calend.getTime());
			
/*			DateUtil dateUtil = new DateUtil();
			Date beginTime = dateUtil.parseDateArbitrary(cond.get("begTime").toString());
			Date endTime = dateUtil.parseDateArbitrary(cond.get("endTime").toString());
			Date tBeginTime = dateUtil.parseDateArbitrary(taskBeginTime);
			Date tEndTime = dateUtil.parseDateArbitrary(taskEndTime);
			
			boolean isDateRight = false;
			//判断日期是否符合要求
			if((endTime.after(beginTime) || endTime.equals(beginTime)) 
					&& (tEndTime.after(endTime) || tEndTime.equals(endTime))
					&& (tEndTime.after(beginTime) || tEndTime.equals(beginTime))
					&& (beginTime.after(tBeginTime) || beginTime.equals(tBeginTime))
					&& (endTime.after(tBeginTime) || endTime.equals(tBeginTime)))	isDateRight = true;
		//	System.out.println(isDateRight); */			
			return "goTaskInfoPage";
		}

	}
	/**
	 * 
	 * @title 在创建新任务消息过程中主导存储LTE干扰计算任务对象信息（下一步操作和提交任务操作）
	 * @author chao.xj
	 * @date 2015-3-26下午3:02:57
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void storageLteInterferCalcTaskObjInfoForAjaxAction() {
		log.debug("进入方法: storageLteInterferCalcTaskObjInfoForAjaxAction。");
		RnoLteInterferCalcTask taskobj=(RnoLteInterferCalcTask)SessionService.getInstance().getValueByKey("MRTASKINFO");
		String result ="";
		if(taskobj!=null&&"taskInfoForward".equals(taskInfoType)){
			try {
				//仅仅是存储任务消息内容
				RnoLteInterferCalcTask.TaskInfo ti =taskobj.getTaskInfo();
				ti.setTaskName(taskName);
				ti.setTaskDesc(taskDescription);
				ti.setStartTime(meaStartTime);
				ti.setEndTime(meaEndTime);
				ti.setProvinceId(provinceId);
				ti.setCityId(cityId);
				ti.setCityName(cityName);
				ti.setProvinceName(provinceName);
				ti.setPlanType(planType);
				ti.setConverType(converType);
				ti.setRelaNumerType(cosi);
				result = "{'state':'true'}";
			} catch (Exception e) {
				result = "{'state':'false'}";
				e.printStackTrace();
			}
			HttpTools.writeToClient(result);
		}
		if(taskobj != null && "paramInfoBack".equals(taskInfoType)){
			try {
				List<RnoThreshold> rnoThresholds = taskobj.getRnoThresholds();
				for (RnoThreshold rnoThreshold : rnoThresholds) {
					String code=rnoThreshold.getCode();
					for(String key:threshold.keySet()){
						if(key.toUpperCase().equals(code)){
							rnoThreshold.setDefaultVal(threshold.get(key));
							//System.out.println(key+" : "+threshold.get(key));
						}
					}
				}
				//获取不重复的分组条件
				HashSet<String> conditionGroups = new HashSet<String>();
				for (RnoThreshold one : rnoThresholds) {
					conditionGroups.add(one.getConditionGroup());
				}
//				List<List<RnoThreshold>> groupRnoThresholds = new ArrayList<List<RnoThreshold>>();
				Map<Long,List<RnoThreshold>> groupRnoThresholds=new TreeMap<Long, List<RnoThreshold>>();
				long orderNum=0;
				List<RnoThreshold> groupRnoThreshold;
				//循环分组条件，加入参数对象
				for (String cond : conditionGroups) {
					groupRnoThreshold = new ArrayList<RnoThreshold>();
					for (RnoThreshold rnoThreshold : rnoThresholds) {
						if(cond.equals(rnoThreshold.getConditionGroup())) {
							groupRnoThreshold.add(rnoThreshold);
							orderNum=rnoThreshold.getOrderNum();
						}
					}
//					groupRnoThresholds.add(groupRnoThreshold);
					groupRnoThresholds.put(orderNum, groupRnoThreshold);
				}
				taskobj.setGroupThresholds(groupRnoThresholds);
				
				result = "{'state':'true'}";
			} catch (Exception e) {
				result = "{'state':'false'}";
				e.printStackTrace();
			}
			HttpTools.writeToClient(result);
		}
		if(taskobj != null && "paramInfoForward".equals(taskInfoType)){
			try {
				List<RnoThreshold> rnoThresholds=taskobj.getRnoThresholds();
				log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>rnoThresholds size="+rnoThresholds.size());
				for (RnoThreshold rnoThreshold : rnoThresholds) {
					String code=rnoThreshold.getCode();
					for(String key:threshold.keySet()){
						if(key.toUpperCase().equals(code)){
							rnoThreshold.setDefaultVal(threshold.get(key));
							//System.out.println(key+" : "+threshold.get(key));
						}
					}
				}
				//获取不重复的分组条件
				HashSet<String> conditionGroups = new HashSet<String>();
				for (RnoThreshold one : rnoThresholds) {
					conditionGroups.add(one.getConditionGroup());
				}
//				List<List<RnoThreshold>> groupRnoThresholds = new ArrayList<List<RnoThreshold>>();
				Map<Long,List<RnoThreshold>> groupRnoThresholds=new TreeMap<Long, List<RnoThreshold>>();
				long orderNum=0;
				List<RnoThreshold> groupRnoThreshold;
				//循环分组条件，加入参数对象
				for (String cond : conditionGroups) {
					groupRnoThreshold = new ArrayList<RnoThreshold>();
					for (RnoThreshold rnoThreshold : rnoThresholds) {
						if(cond.equals(rnoThreshold.getConditionGroup())) {
							groupRnoThreshold.add(rnoThreshold);
							orderNum=rnoThreshold.getOrderNum();
						}
					}
//					groupRnoThresholds.add(groupRnoThreshold);
					groupRnoThresholds.put(orderNum, groupRnoThreshold);
				}
				taskobj.setGroupThresholds(groupRnoThresholds);
				taskobj.setRnoThresholds(rnoThresholds);
				result = "{'state':'true'}";
			} catch (Exception e) {
				result = "{'state':'false'}";
				e.printStackTrace();
			}
			HttpTools.writeToClient(result);
		}
		if(taskobj != null && "overViewBack".equals(taskInfoType)){
			try {
				//缓存需要优化的小区列
				taskobj.getTaskInfo().setLteCells(lteCells);
				
				result = "{'state':'true'}";
			} catch (Exception e) {
				result = "{'state':'false'}";
				e.printStackTrace();
			}
			HttpTools.writeToClient(result);
		}
	}
	
	/**
	 * 提交pci自动规划任务
	 * 
	 * @author peng.jm
	 * @date 2015年3月28日15:09:34
	 */
	public void submitPciPlanAnalysisTaskAction() {
		log.info("进入：submitPciPlanAnalysisTaskAction");
		String account = (String) SessionService.getInstance().getValueByKey(
					"userId");
		log.info("提交者=" + account);
		
		RnoLteInterferCalcTask taskobj = (RnoLteInterferCalcTask) SessionService
				.getInstance().getValueByKey("MRTASKINFO");
		
		//保存需要优化的小区列
		taskobj.getTaskInfo().setLteCells(lteCells);
		
		Map<String, Object> res =null;
		if (taskobj != null) {
			//取出session里保存的任务所需信息
//			Threshold threshold = taskobj.getThreshold(); //门限值
			List<RnoThreshold> rnoThresholds=taskobj.getRnoThresholds();
			RnoLteInterferCalcTask.TaskInfo taskInfo=taskobj.getTaskInfo(); //任务信息
			
			res = rnoStructureService.submitPciPlanAnalysisTask(account,
					rnoThresholds, taskInfo);
			//清空session
			SessionService.getInstance().rmvValueByKey("MRTASKINFO");
		}
		String result = gson.toJson(res);
		log.debug("退出submitPciPlanAnalysisTaskAction result="+result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 查询pci自动规划任务
	 * 
	 * @author peng.jm
	 * @date 2015年3月31日15:23:10
	 */
	public void queryPciPlanAnalysisTaskByPageForAjaxAction() {
		log.info("进入：queryPciPlanAnalysisTaskByPageForAjaxAction。cond=" + cond
				+ ",page=" + page);
		String account = (String) SessionService.getInstance().getValueByKey("userId");
		if (page == null) {
			log.error("方法queryPciPlanAnalysisTaskByPageForAjaxAction的page参数为空！");
			HttpTools.writeToClient("[]");
			return;
		}

		Page newPage = page.copy();
		List<Map<String, Object>> pciTasks = rnoStructureService
				.queryPciPlanTaskByPage(cond, newPage, account);
		String res = gson.toJson(pciTasks);

		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String result = "{'page':" + pstr + ",'data':" + res + "}";
		log.info("退出queryPciPlanAnalysisTaskByPageForAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	
	/**
	 * 终止Pci规划任务
	 * 
	 * @author peng.jm
	 * @date 2015年3月31日15:23:10
	 */
	public void stopPciJobByJobIdAndMrJobIdForAjaxAction() {
		log.info("进入方法：stopPciJobByJobIdAndMrJobIdForAjaxAction。jobId=" + jobId 
				+ ", mrJobId="+mrJobId);
		String account = (String) SessionService.getInstance().getValueByKey(
				"userId");
		Map<String, Object> res = new HashMap<String, Object>();
		//停止runnable的job
		JobClient jobClient = JobClientDelegate.getJobClient();
		JobProfile job = new JobProfile(jobId);
		jobClient.killJob(job, account, "用户主动停止");
		//停止Hadoop的mapreduce的job
		/*org.apache.hadoop.mapred.JobClient mrJobClient 
					= new org.apache.hadoop.mapred.JobClient();*/
		//@author chao.xj  2015-4-22 下午3:34:11
		log.debug("mrJobId是:"+mrJobId);
		boolean flag=true;
	   Job mrJob=(Job)SessionService.getInstance().getValueByKey(
				mrJobId);
	   log.debug("通过mrJobId从session获取的job是:"+mrJob);
		   try {
			  if(mrJob!=null){
				  
				  mrJob.killJob();
				  log.debug(mrJob+"该mr工作状态为："+mrJob.getStatus().getState());
			  }
			
		} catch (IOException e) {
			log.error(mrJob+"该mrjob停止失败!");
			flag=false;
			e.printStackTrace();
		}catch (InterruptedException e) {
			log.error(mrJob+"该mrjob停止失败!");
			flag=false;
			e.printStackTrace();
		}finally{
			if(!"undefined".equals(mrJobId)){
				SessionService.getInstance().rmvValueByKey(mrJobId);
			}
		}
		res.put("flag", flag);
		String result = gson.toJson(res);
		HttpTools.writeToClient(result);
	}
	
	/**
	 * 下载pci自动规划结果文件
	 * 
	 * @author peng.jm
	 * @date 2015年3月31日15:23:10
	 */
	public String downloadPciFileAction() {
		log.info("下载PCI规划结果文件， jobId=" + jobId + ", mrJobId=" + mrJobId);
		// 获取任务的信息。
		Connection connection = DataSourceConn.initInstance().getConnection();
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String sql = "select * from rno_lte_pci_job where job_id=" + jobId;
		List<Map<String, Object>> pciTaksInfo = RnoHelper.commonQuery(stmt, sql);

		if (pciTaksInfo.size() <= 0) {
			error = "不存在该" + jobId + "任务信息";
			return "fail";
		}
		//区域ID
		long cityId = Long.parseLong(pciTaksInfo.get(0).get("CITY_ID").toString());
		Map<String, List<String>> cellIdToCessInfo = rnoStructureService.getLteCellInfoByCellId(stmt, cityId);

		Map<String, String> cacheMrCell = new HashMap<String, String>();
		if (cellIdToCessInfo.size() <= 0) {
			error = "该区域" + cityId + "不存在系统小区信息";
			return "fail";
		}
		try {
			stmt.close();
			connection.close();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		//获取pci规划任务中的待优化小区字段
		Clob cellsClob = (Clob) pciTaksInfo.get(0).get("OPTIMIZE_CELLS");
		Reader inStream = null;
		Map<String, String> cellsMap = new HashMap<String, String>();
		try {
			inStream = cellsClob.getCharacterStream();
			char[] cellsChar = new char[(int) cellsClob.length()];
			inStream.read(cellsChar);
			String[] cellStrs = new String(cellsChar).trim().split(",");
			for (String c : cellStrs) {
				if (!"".equals(c.trim())) {
					cellsMap.put(c, "");
				}
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//下载的Pci规划文件名称
		String dlFileName = pciTaksInfo.get(0).get("DL_FILE_NAME").toString();
		
		//下载的Pci规划文件全路径
		DateUtil dateUtil = new DateUtil();
		Calendar calendar = new GregorianCalendar();
		Date createDate = dateUtil.parseDateArbitrary(pciTaksInfo.get(0).get("CREATE_TIME").toString());
		calendar.setTime(createDate);
		String path = ServletActionContext.getServletContext().getRealPath("/op/rno/ana_result/");
		String dlFileRealdir = path + "/" + calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1)
				+ "/";
		String dlFileRealPath = dlFileRealdir + dlFileName;
		File realdir = new File(dlFileRealdir);
		if (!realdir.exists()) {
			realdir.mkdirs();
		}
		File realfile = new File(dlFileRealPath);
		if (!realfile.exists()) {
			try {
				realfile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//Pci规划数据源文件的hdfs路径
		String resFilePath = pciTaksInfo.get(0).get("RESULT_DIR").toString();
		//Pci规划数据源文件的名称,源数据文件有两个，一个是current后缀，一个是backup后缀
		String currentFileName = pciTaksInfo.get(0).get("RD_FILE_NAME").toString() + ".current";
		String backupFileName = pciTaksInfo.get(0).get("RD_FILE_NAME").toString() + ".backup";

		File currentFile = FileTool.getFile(resFilePath + "/" + currentFileName);
		File backupFile = FileTool.getFile(resFilePath + "/" + backupFileName);

		//两个源文件不存在，不能提供下载
		if (currentFile == null && backupFile == null) {
			log.info("Pci规划计算的源数据current文件不存在！currentFileName=" + resFilePath + "/" + currentFileName);
			log.info("Pci规划计算的源数据backup文件不存在！backupFileName=" + resFilePath + "/" + backupFileName);
			error = "Pci规划计算的源数据backup文件不存在！backupFileName=" + resFilePath + "/" + backupFileName;
			return "fail";
		}

		FileInputStream fsBak = null;
		FileInputStream fsCur = null;
		DataInputStream disBak = null;
		DataInputStream disCur = null;
		@SuppressWarnings("unused")
		double totInterVal = 0;
		int totDataNum = 0;
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		Map<String, Object> one = null;
		String isFinished = "";

		String result = "success";
		String cellId = "";
		String cellName = "";
		String oldPci = "";
		String earfcn = "";
		List<String> cells = null;
		try {
			if (currentFile != null) {
				//当前数据文件存在，则选择current文件
				fsCur = new FileInputStream(currentFile);
				disCur = new DataInputStream(fsCur);
				totInterVal = disCur.readDouble();
				totDataNum = disCur.readInt();
				for (int i = 0; i < totDataNum; i++) {
					one = new HashMap<String, Object>();
					cellId = disCur.readUTF();
					//缓存MR小区
					cacheMrCell.put(cellId, cellId);
					cells = cellIdToCessInfo.get(cellId);
					if (cells == null) {
						log.error("小区找不到对应工参数据：cellId = " + cellId);
						one.put("oldPci", -1);
						one.put("earfcn", -1);
						one.put("cellName", "未知小区");
					} else {
						//小区名，pci,频点
						cellName = cells.get(0);
						oldPci = cells.get(1);
						earfcn = cells.get(2);
						one.put("oldPci", oldPci);
						one.put("earfcn", earfcn);
						one.put("cellName", cellName);
					}
					one.put("cellId", cellId);
					int newPci = disCur.readInt();
					if (newPci == -1) {
						if (cells == null) {
							one.put("newPci", "找不到对应工参数据");
						} else {
							one.put("newPci", "找不到对应MR数据");
						}
					} else {
						one.put("newPci", newPci);
					}
					one.put("oriInterVal", disCur.readDouble());
					one.put("interVal", disCur.readDouble());

					if (cellsMap.containsKey(cellId)) {
						one.put("remark", "修改小区");
					} else {
						one.put("remark", "MR其他小区");
					}
					res.add(one);
				}

				isFinished = disCur.readUTF();
				if (isFinished.equals("finished")) {
					//可以生成excel结果文件
					setFileName(new String(dlFileName.getBytes(), "iso-8859-1"));
					createExcelFile(dlFileRealPath, res);
					File f = new File(dlFileRealPath);
					if (!f.exists()) {
						log.error("Pci规划结果文件不存在！");
						error = "Pci规划结果文件不存在！,文件路径：" + dlFileRealPath;
						if (disCur != null) {
							disCur.close();
						}
						return "fail";
					} else {
						try {
							exportInputStream = new FileInputStream(dlFileRealPath);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
							result = "error";
							error = exportInputStream + "获取文件流异常,文件路径：" + dlFileRealPath;
						}
					}
				} else {
					//当前文件没有正常结束，读取backup文件
					res.clear();

					if (backupFile != null) {
						fsBak = new FileInputStream(backupFile);
						disBak = new DataInputStream(fsBak);
						totInterVal = disBak.readDouble();
						totDataNum = disBak.readInt();
						for (int i = 0; i < totDataNum; i++) {
							one = new HashMap<String, Object>();
							cellId = disBak.readUTF();

							//缓存MR小区
							cacheMrCell.put(cellId, cellId);
							cells = cellIdToCessInfo.get(cellId);
							if (cells == null) {
								log.error("小区找不到对应工参数据：cellId = " + cellId);
								one.put("oldPci", -1);
								one.put("earfcn", -1);
								one.put("cellName", "未知小区");
							} else {
								//小区名，pci,频点
								cellName = cells.get(0);
								oldPci = cells.get(1);
								earfcn = cells.get(2);
								one.put("oldPci", oldPci);
								one.put("earfcn", earfcn);
								one.put("cellName", cellName);
							}
							one.put("cellId", cellId);
							int newPci = disBak.readInt();
							if (newPci == -1) {
								if (cells == null) {
									one.put("newPci", "找不到对应工参数据");
								} else {
									one.put("newPci", "找不到对应MR数据");
								}
							} else {
								one.put("newPci", newPci);
							}
							one.put("oriInterVal", disBak.readDouble());
							one.put("interVal", disBak.readDouble());

							if (cellsMap.containsKey(cellId)) {
								one.put("remark", "修改小区");
							} else {
								one.put("remark", "MR其他小区");
							}
							res.add(one);
						}

						isFinished = disBak.readUTF();
						if (isFinished.equals("finished")) {
							//可以生成excel结果文件
							setFileName(new String(dlFileName.getBytes(), "iso-8859-1"));
							createExcelFile(dlFileRealPath, res);
							File f = new File(dlFileRealPath);
							if (!f.exists()) {
								log.error("Pci规划结果文件不存在！");
								error = "Pci规划结果文件不存在！,文件路径：" + dlFileRealPath;
								result = "error";
							} else {
								try {
									exportInputStream = new FileInputStream(dlFileRealPath);
								} catch (FileNotFoundException e) {
									e.printStackTrace();
									error = exportInputStream + "获取文件流异常,文件路径：" + dlFileRealPath;
									result = "error";
								}
							}
						} else {
							log.info("Pci规划结果源文件中，current和backup文件都没有以finished结尾");
							result = "error";
							error = "Pci规划结果源文件中，current和backup文件都没有以finished结尾";
						}
					} else {
						log.info("Pci规划结果源文件中，current文件没有正常结尾，backup文件则不存在");
						error = "Pci规划结果源文件中，current文件没有正常结尾，backup文件则不存在";
						result = "error";
					}
				}
			} else {
				//current为null，读取backup文件
				fsBak = new FileInputStream(backupFile);
				disBak = new DataInputStream(fsBak);
				totInterVal = disBak.readDouble();
				totDataNum = disBak.readInt();
				for (int i = 0; i < totDataNum; i++) {
					one = new HashMap<String, Object>();
					cellId = disBak.readUTF();

					//缓存MR小区
					cacheMrCell.put(cellId, cellId);
					cells = cellIdToCessInfo.get(cellId);
					if (cells == null) {
						log.error("小区找不到对应工参数据：cellId = " + cellId);
						one.put("oldPci", -1);
						one.put("earfcn", -1);
						one.put("cellName", "未知小区");
					} else {
						//小区名，pci,频点
						cellName = cells.get(0);
						oldPci = cells.get(1);
						earfcn = cells.get(2);
						one.put("oldPci", oldPci);
						one.put("earfcn", earfcn);
						one.put("cellName", cellName);
					}
					one.put("cellId", cellId);
					int newPci = disBak.readInt();
					if (newPci == -1) {
						if (cells == null) {
							one.put("newPci", "找不到对应工参数据");
						} else {
							one.put("newPci", "找不到对应MR数据");
						}
					} else {
						one.put("newPci", newPci);
					}
					one.put("oriInterVal", disBak.readDouble());
					one.put("interVal", disBak.readDouble());

					if (cellsMap.containsKey(cellId)) {
						one.put("remark", "修改小区");
					} else {
						one.put("remark", "MR其他小区");
					}
					res.add(one);
				}
				isFinished = disBak.readUTF();
				if (isFinished.equals("finished")) {
					//可以生成excel结果文件
					setFileName(new String(dlFileName.getBytes(), "iso-8859-1"));
					createExcelFile(dlFileRealPath, res);
					File f = new File(dlFileRealPath);
					if (!f.exists()) {
						log.error("Pci规划结果文件不存在！");
						result = "error";
						error = "Pci规划结果文件不存在！,文件路径：" + dlFileRealPath;
					} else {
						try {
							exportInputStream = new FileInputStream(dlFileRealPath);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
							error = exportInputStream + "获取文件流异常,文件路径：" + dlFileRealPath;
							result = "error";
						}
					}
				} else {
					log.info("Pci规划结果源文件中，backup文件没有正常结尾，current文件则不存在");
					error = "Pci规划结果源文件中，backup文件没有正常结尾，current文件则不存在";
					result = "error";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (disBak != null) {
					disBak.close();
				}
				if (disCur != null) {
					disCur.close();
				}
				if (fsBak != null) {
					fsBak.close();
				}
				if (fsCur != null) {
					fsCur.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
				log.info("获取Pci规划结果源文件中，读取数据出错");
				error = "获取Pci规划结果源文件中，读取数据出错";
				result = "error";
			}
			log.info("获取Pci规划结果源文件中，读取数据出错");
			error = "获取Pci规划结果源文件中，读取数据出错";
			result = "error";
		}
		if (result.equals("error")) {
			return "fail";
		}
		return result;
	}
	
	/**
	 * 在项目的数据目录创建Pci结果excel文件
	 * @param fileRealPath
	 * @param res
	 * @return
	 */
	public boolean createExcelFile(String fileRealPath, List<Map<String,Object>> res) {
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileRealPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		
		Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet();
		Row row;
		Cell cell;
		row = sheet.createRow((short)0);
		cell = row.createCell(0);
		cell.setCellValue("小区名称");
		cell = row.createCell(1);
		cell.setCellValue("小区标识");
		cell = row.createCell(2);
		cell.setCellValue("频点");
		cell = row.createCell(3);
		cell.setCellValue("原PCI");
		cell = row.createCell(4);
		cell.setCellValue("新PCI");
		cell = row.createCell(5);
		cell.setCellValue("原干扰值");	
		cell = row.createCell(6);
		cell.setCellValue("干扰值");
		cell = row.createCell(7);
		cell.setCellValue("备注");
		for (int i = 0; i < res.size(); i++) {
			row = sheet.createRow((short)i + 1);
			cell = row.createCell(0);
			cell.setCellValue(res.get(i).get("cellName").toString());
			cell = row.createCell(1);
			cell.setCellValue(res.get(i).get("cellId").toString());
			cell = row.createCell(2);
			cell.setCellValue(Integer.parseInt(res.get(i).get("earfcn").toString()));
			cell = row.createCell(3);
			cell.setCellValue(Double.parseDouble(res.get(i).get("oldPci").toString()));
			cell = row.createCell(4);
			cell.setCellValue(res.get(i).get("newPci").toString());
			cell = row.createCell(5);
			cell.setCellValue(Double.parseDouble(res.get(i).get("oriInterVal").toString()));
			cell = row.createCell(6);
			cell.setCellValue(Double.parseDouble(res.get(i).get("interVal").toString()));
			cell = row.createCell(7);
			cell.setCellValue(res.get(i).get("remark").toString());
		}
        //最终写入文件
        try {
			workbook.write(fos);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
        try {
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
        return true;
	}
	
}
