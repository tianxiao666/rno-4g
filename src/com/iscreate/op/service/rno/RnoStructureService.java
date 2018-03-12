package com.iscreate.op.service.rno;

import java.awt.Polygon;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.G2NcsQueryCond;
import com.iscreate.op.action.rno.vo.Threshold;
import com.iscreate.op.dao.rno.RnoStructureAnalysisDaoImpl.CellInterWithDetailInfo;
import com.iscreate.op.pojo.rno.CellFreqInterferList;
import com.iscreate.op.pojo.rno.NcsCellQueryResult;
import com.iscreate.op.pojo.rno.RenderCellInfo;
import com.iscreate.op.pojo.rno.RnoThreshold;
import com.iscreate.op.pojo.rno.RnoStructureAnalysisTask.MrrInfo;
import com.iscreate.op.pojo.rno.RnoStructureAnalysisTask.NcsInfo;
import com.iscreate.op.pojo.rno.RnoStructureAnalysisTask.TaskInfo;
import com.iscreate.op.pojo.rno.RnoTask;
import com.iscreate.op.service.rno.task.TaskStatus;



/**
 * @author brightming
 * @version 1.0
 * @created 17-һ��-2014 11:02:42
 */
public interface RnoStructureService {

	/**
	 * 
	 * @param condition
	 * @param page
	 */
	public List<Map<String,Object>> queryNcsDescriptorByPage(Map<String,String> condition, Page page);

	/**
	 * 
	 * @param ncsId
	 * @param page
	 */
	public List<Map<String,Object>> queryNcsCellDataByPage(long ncsId, Page page);

	/**
	 * 
	 * @param ncsId
	 * @param page
	 */
	public List<Map<String,Object>> queryNcsNcellDataByPage(long ncsId, Page page);

	/**
	 * 
	 * @param ncsId
	 * @param page
	 */
	public List<Map<String,Object>> queryNcsClusterByPage(long ncsId, Page page);

	/**
	 * 
	 * @param clusterId
	 */
	public List<Map<String,Object>> queryNcsClusterCell(long clusterId);

	/**
	 * 
	 * @param ncsId
	 * @param page
	 */
	public List<Map<String,Object>> queryNcsInterferMatrixByPage(long ncsId, Page page);

	
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
	 * 包含具有指定bsc的ncs列表
	 * @param bsc
	 * @return
	 * @author brightming
	 * 2014-2-11 下午1:51:52
	 */
	public List<Map<String, Object>> searchNcsContainCell(String cell, String bsc, long cityId, String manufacturers);

	/**
	 * 获取指定小区的在指定ncs里的测量信息
	 * @param ncsId
	 * @param cell
	 * @return
	 * @author brightming
	 * 2014-2-11 下午2:14:24
	 */
	public List<Map<String, Object>> getCellNcsInfo(long ncsId, String cell, long cityId, String manufacturers);
	
	/**
	 * 计算指定ncs范围，指定小区的频点的干扰情况
	 * 如果指定小区为null或为空，则计算指定ncs内的所有的小区的频点干扰情况
	 * @param ncsIds
	 * @param cells
	 * @return
	 * @author brightming
	 * 2014-2-13 下午3:34:51
	 */
	public CellFreqInterferList staticsNcsCellFreqInterfer(List<Long> ncsIds,List<String> cells);
	
//	/**
//	 * 获取单个ncs的结构指标数据
//	 * @param ncsId
//	 * @return
//	 * @author brightming
//	 * 2014-2-14 下午3:40:04
//	 */
//	public List<Map<String,Object>> getSingleNcsStructAnaRes(long ncsId);
//
//	/**
//	 * 获取单个ncs的所有连通簇列表
//	 * @param ncsId
//	 * @return
//	 * @author brightming
//	 * 2014-2-15 下午2:17:12
//	 */
//	public List<Map<String, Object>> getSingleNcsStructClusterList(long ncsId);
//
//	/**
//	 * 获取单个ncs的最大连通簇列表
//	 * @param ncsId
//	 * @return
//	 * @author brightming
//	 * 2014-2-15 下午2:17:25
//	 */
//	public List<Map<String, Object>> getSingleNcsStructMaxClusterList(long ncsId);
//
//	/**
//	 * 获取单个ncs的簇内小区列表
//	 * @param ncsId
//	 * @return
//	 * @author brightming
//	 * 2014-2-15 下午2:17:36
//	 */
//	public List<Map<String, Object>> getSingleNcsStructClusterCellList(
//			long ncsId);
//	
	/**
	 * 提交ncs汇总分析任务
	 * 
	 * @param creator
	 *            提交者账号
	 * @param ncsIds
	 * @param savePath
	 *            结果输出的保存路径
	 * @param cityId
	 *        市区id
	 * @param areaLevel
	 *        统计级别：市、区/县
	 * @param areaId
	 *        统计级别所在区域的id。当统计级别为市的时候，等同于cityId
	 * @param areaName
	 *        统计区域的名称 
	 * @param taskName
	 *        分析任务的名称
	 * @param taskDescription
	 *        分析任务的描述
	 *    
	 * @return key:flag true/false key:msg 消息
	 * 
	 * @author brightming 2014-2-17 上午9:44:27
	 */
	public Map<String, Object> submitRnoNcsAnalysisTask(String creator,
			String ncsIds, String savePath, long cityId,String areaLevel, long areaId,
			String areaName, String taskName, String taskDescription);
	/**
	 * 分页查询由account启动的任务
	 * @param account
	 * @param cond
	 * @param newPage
	 * @return
	 * @author brightming
	 * 2014-2-18 下午3:37:55
	 */
	public List<Map<String, Object>> queryNcsTaskByPage(String account,
			Map<String, String> cond, Page newPage);

	/**
	 * 获取任务详情
	 * @param taskId
	 * @return
	 * @author brightming
	 * 2014-2-19 上午10:04:32
	 */
	public RnoTask getTaskById(long taskId);

	/**
	 * 重新计算指定的ncs的汇总任务
	 * @param account
	 * @param taskId
	 * @param path
	 * @param taskName
	 * @param taskDescription
	 * @return
	 * @author brightming
	 * 2014-2-19 上午10:55:32
	 */
	public Map<String, Object> recalculateRnoNcsAnalysisTask(String account,
			long taskId, String path, String taskName, String taskDescription);
	
	/**
	 * 获取存储在excel中的报告
	 * @param excelPath
	 * @param dataType
	 * cellres
	 * cluster
	 * clustercell
	 * clustercellrela
	 * @return
	 * @author brightming
	 * 2014-2-20 上午10:11:51
	 */
	public List<Object> getNcsTaskReport(long taskId,String excelPath,String dataType);
	
	
	/**
	 * 分页获取指定ncsId的ncsCell
	 * @author Liang YJ
	 * @date 2014-2-19 上午9:29:53
	 * @param ncsDescId
	 * 数据源表中的RNO_NCS_DESC_ID字段
	 * @param page
	 * 分页参数
	 * @return
	 */
	public NcsCellQueryResult getNcsCellByPage(long ncsDescId, Page page);

	/**
	 * 获取单个ncs的结构优化数据
	 * @param diskPath
	 * 存储文件结果的父目录
	 * @param ncsId
	 * @param dataType
	 * @return
	 * 如果返回null，表示还没有准备好，正在计算当中
	 * @author brightming
	 * 2014-3-6 下午5:58:57
	 */
	public List<Map<String, Object>> getSingleNcsStructData(String diskPath,long ncsId,
			String dataType) ;

	/**
	 * 根据id删除ncs相关记录。
	 * @param ncsId
	 * @author brightming
	 * 2014-3-13 下午5:01:13
	 */
	public void deleteNcsRecById(long ncsId);
	/**
	 * 按逗号分隔的ncs id标记删除ncs记录
	 * @title 
	 * @param ncsIds
	 * @author chao.xj
	 * @date 2014-3-27上午11:38:34
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void deleteNcsSelectedRecById(String ncsIds);

	/**
	 * 查询指定的ncs desc的状态
	 * @param ncsIds
	 * @return
	 * @author brightming
	 * 2014-5-21 下午5:26:15
	 */
	public List<Map<String, Object>> queryNcsDescStatus(String ncsIds);
	/**
	 * 
	 * @title 获取所有的小区平面坐标数据集合
	 * @param cellresList 结构指标数据
	 * @param leftTLng    区域左上经度
	 * @param leftTLat    区域左上纬度
	 * @param rightBLng   区域右下经度
	 * @param rightBLat   区域右下纬度
	 * @return 小区平面坐标集合
	 * @author chao.xj
	 * @date 2014-5-16上午10:14:12
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<RenderCellInfo> getCellXYPlaneCoordinateInfos(
			List<Map<String, Object>> cellresList, double leftTLng,
			double leftTLat, double rightBLng, double rightBLat);
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
	public double[] getAreaActualLenWidAndAfterShrinkLenWid(double leftTLng,double leftTLat,double rightBLng,double rightBLat,int afterShrinkLen);
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
	public Map<String, Object> getRowColumnToCellMap(List<RenderCellInfo> renderCellList,double actualAreaLenght,double actualAreaWidth,int gridMeter,int expandMeter);
	/**
	 * 
	 * @title 通过传入不同的Code 生成图像并存储
	 * @param rowColumnToCellMap 网格行列到小区集合映射
	 * @param taskId 任务ID
	 * @param dataType 数据类型 如 cellres
	 * @param afterShrinkLen 收缩后的画布长度
	 * @param afterShrinkWid 收缩后的画布宽度
	 * @param gridMeter 一个网格实际多少米
	 * @param actualAreaLength 实际矩形区域长度米
	 * @param Code 0代表BE_INTERFER被干扰系数，1代表NET_STRUCT_FACTOR网络结构指数
	 * @author chao.xj
	 * @date 2014-5-16下午05:56:16
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void drawImage(Map<String, Object> rowColumnToCellMap,long taskId,String dataType,int afterShrinkLen,int afterShrinkWid,int gridMeter,double actualAreaLength,int Code);
	/**
	 * 
	 * @title 通过传入不同的Code 生成图像并存储
	 * @param rowColumnToCellMap 网格行列到小区集合映射
	 * @param taskId 任务ID
	 * @param dataType 数据类型 如 cellres
	 * @param afterShrinkLen 收缩后的画布长度
	 * @param afterShrinkWid 收缩后的画布宽度
	 * @param gridMeter 一个网格实际多少米
	 * @param actualAreaLength 实际矩形区域长度米
	 * @param actualAreaWidth 实际矩形区域宽度米
	 * @param Code 0代表BE_INTERFER被干扰系数，1代表NET_STRUCT_FACTOR网络结构指数
	 * @author chao.xj
	 * @date 2014-5-16下午05:56:16
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void drawImage(Map<String, Object> rowColumnToCellMap,long taskId,String dataType,int afterShrinkLen,int afterShrinkWid,int gridMeter,double actualAreaLength,double actualAreaWidth,int Code);
	/**
	 * 
	 * @title 获取十六进制渐变颜色字符串
	 * @param hexcolor 363eeb
	 * @param MAX_VALUE 0.08 颜色值区间
	 * @param MIN_VALUE 0.06 颜色值区间
	 * @param cellCoeff 0.065 区间点
	 * @return 在此区间点的十六进制颜色字符串
	 * @author chao.xj
	 * @date 2014-5-24下午04:08:32
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String getHexColor(String hexcolor, double MAX_VALUE,
			double MIN_VALUE, double cellCoeff);
	/**
	 * 
	 * @title 转换映射至画布收缩后的小区平面坐标
	 * @param leftTLng　区域左上经度
	 * @param leftTLat　区域左上纬度
	 * @param rightBLng　区域右下经度
	 * @param rightBLat　区域右下纬度
	 * @param cellLng　小区经度
	 * @param cellLat　小区纬度
	 * @return　收缩后站点画布平面坐标
	 * @author chao.xj
	 * @date 2014-5-9上午11:19:51
	 * @company 怡创科技
	 * @version 1.2
	 */
	public double[] getRelaCoordinateForMappingCanvas(double leftTLng,double leftTLat,double rightBLng,double rightBLat,double cellLng,double cellLat,int afterShrinkLen);
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
	public List<Polygon> getBoundPolygon(double leftTLng,double leftTLat,double rightBLng,double rightBLat,int afterShrinkLen,String allBoundary);
	/**
	 * 
	 * @title 通过传入不同的Code 生成图像并存储
	 * @param rowColumnToCellMap 网格行列到小区集合映射
	 * @param taskId 任务ID
	 * @param dataType 数据类型 如 cellres
	 * @param afterShrinkLen 收缩后的画布长度
	 * @param afterShrinkWid 收缩后的画布宽度
	 * @param gridMeter 一个网格实际多少米
	 * @param actualAreaLength 实际矩形区域长度米
	 * @param actualAreaWidth 实际矩形区域宽度米
	 * @param Code 0代表BE_INTERFER被干扰系数，1代表NET_STRUCT_FACTOR网络结构指数
	 * @param polygonList 多边形集合
	 * @author chao.xj
	 * @date 2014-5-16下午05:56:16
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void drawImage(Map<String, Object> rowColumnToCellMap,long taskId,String dataType,int afterShrinkLen,int afterShrinkWid,int gridMeter,double actualAreaLength,double actualAreaWidth,int Code,List<Polygon> polygonList);


	/**
	 * 根据taskId停止对应的ncs分析任务
	 * @author peng.jm  2014年6月20日14:14:37
	 */
	public Map<String, Object> stopNcsAnalysisTask(long taskId);
	
	/**
	 * 检查是否存在已存在的ncs分析任务
	 * @author peng.jm
	 * 2014年6月23日9:49:58
	 */
	public Map<String, Object> checkNcsTaskByNcsIds(String ncsIds);

	/**
	 * 通过任务id获取已存在的ncs分析任务
	 * @author peng.jm 
	 * 2014年6月23日17:48:21
	 */
	/*public Map<String, Object> queryOldNcsTaskByTaskId(long taskId);*/

	/**
	 * 通过NcsIds获取已存在的ncs分析任务
	 * @author peng.jm 
	 */
	public List<Map<String, Object>> queryOldNcsTaskByNcsIds(String ncsIds);
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
	public CellInterWithDetailInfo getCellInterDetailInfo(String realpath, String cellName);

	/**
	 * 检查是否存在对应的渲染图，不存在则生成该参数的渲染图
	 * @author peng.jm
	 * 2014年7月9日12:16:03
	 */
	public Map<String, Object> getRenderImgByParamAndTaskId(
			String ncsRendererType, long taskId, String filePath);

	/**
	 * 获取指标参数对应的颜色渲染规则
	 * @param ncsRendererType 指标参数
	 * @param taskId
	 * @param filePath
	 * @return
	 * @author peng.jm
	 */
	public List<Map<String, Object>> getRenderColorRule(String ncsRendererType,
			long taskId, String filePath);
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
			Map<String, String> condition, Page page);
	/**
	 * 
	 * @title 根据id删除mrr相关记录
	 * @param mrrId
	 * @author chao.xj
	 * @date 2014-7-23上午10:18:34
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void deleteMrrRecById(long mrrId);
	/**
	 * 
	 * @title 提交ncs与mrr汇总分析任务
	 * @param creator 帐户名
	 * @param savePath 保存路径
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
			String savePath,NcsInfo ncsInfo,MrrInfo mrrInfo,TaskInfo taskInfo);
	

	/**
	 * 
	 * @param cond
	 * @param newPage
	 * @return
	 * @author peng.jm
	 * @date 2014-7-23下午04:09:55
	 */
	public List<Map<String, Object>> queryMrrDescByPage(
			Map<String, String> cond, Page newPage);

	/**
	 * 查询mrr管理信息
	 * @param mrrId
	 * @param newPage
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:05:05
	 */
	public List<Map<String, Object>> queryMrrAdmDataByPage(
			long mrrId, Page newPage);
	
	/**
	 * 查询mrr信号强度信息
	 * @param mrrId
	 * @param newPage
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:05:05
	 */
	public List<Map<String, Object>> queryMrrStrenDataByPage(long mrrId,
			Page newPage);

	/**
	 * 查询mrr信号质量信息
	 * @param mrrId
	 * @param newPage
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:05:05
	 */
	public List<Map<String, Object>> queryMrrQualiDataByPage(long mrrId,
			Page newPage);

	/**
	 * 查询mrr传输功率信息
	 * @param mrrId
	 * @param newPage
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:05:05
	 */
	public List<Map<String, Object>> queryMrrPowerDataByPage(long mrrId,
			Page newPage);
	/**
	 * 查询mrr实时预警信息
	 * 
	 * @author peng.jm
	 * @date 2014-7-24下午03:04:22
	 */
	public List<Map<String, Object>> queryMrrTaDataByPage(long mrrId,
			Page newPage);
	/**
	 * 查询mrr路径损耗信息
	 * 
	 * @author peng.jm
	 * @date 2014-7-24下午03:04:22
	 */
	public List<Map<String, Object>> queryMrrPlDataByPage(long mrrId,
			Page newPage);
	/**
	 * 查询mrr路径损耗差异信息
	 * 
	 * @author peng.jm
	 * @date 2014-7-24下午03:04:22
	 */
	public List<Map<String, Object>> queryMrrPldDataByPage(long mrrId,
			Page newPage);
	/**
	 * 查询mrr测量结果信息
	 * 
	 * @author peng.jm
	 * @date 2014-7-24下午03:04:22
	 */
	public List<Map<String, Object>> queryMrrMeaDataByPage(long mrrId,
			Page newPage);
	/**
	 * 查询mrr的上下行FER信息
	 * 
	 * @author peng.jm
	 * @date 2014-7-24下午03:04:22
	 */
	public List<Map<String, Object>> queryMrrFerDataByPage(long mrrId,
			Page newPage);
	

	/**
	 * 分页查询干扰矩阵信息
	 * @author peng.jm
	 * @date 2014-8-15下午04:14:09
	 */
	public List<Map<String, Object>> queryInterferMartixByPage(
			Map<String, String> cond, Page page);

	/**
	 * 检查这周是否计算过NCS干扰矩阵
	 * @author peng.jm
	 * @date 2014-8-16上午11:01:47
	 */
	public List<Map<String, Object>> checkInterMartixThisWeek(
			long areaId, String thisMonday, String today);

	/**
	 * 分页加载NCS数据
	 * @author peng.jm
	 * @date 2014-8-16下午02:05:38
	 */
	public List<Map<String, Object>> queryNcsDataByPageAndCond(
			Map<String, String> cond, Page page);

	/**
	 * 查询对应条件的NCS数据记录数量
	 * @param cond
	 * @return
	 * @author peng.jm
	 * @date 2014-8-16下午05:02:24
	 */
	public int queryNcsDataCountByCond(Map<String, String> cond);
	
	/**
	 * 新增NCS干扰矩阵
	 * @param cond
	 * @param account
	 * @author peng.jm
	 * @date 2014-8-16下午04:40:41
	 */
	public void addInterMartixByNcs(Map<String, String> cond, String account);
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
	 * 提交结构分析计算任务
	 * @param account
	 * @param path
	 * @param threshold
	 * @param taskInfo
	 * @return
	 * @author peng.jm
	 * @date 2014-8-26下午04:44:25
	 */
	public Map<String, Object> submitEriAndHwStructureTask(String account,
			String path, List<RnoThreshold> rnoThresholds, TaskInfo taskInfo);
	/**
	 * 分页查询结构分析任务信息
	 * @param cond
	 * @param newPage
	 * @param account
	 * @return
	 * @author peng.jm
	 * @date 2014-8-26下午06:45:43
	 */
	public List<Map<String, Object>> queryStructureAnalysisTaskByPage(
			Map<String, String> cond, Page newPage, String account);
	/**
	 * 停止对应jobId的执行任务
	 * @param jobId
	 * @param account
	 * @return
	 * @author peng.jm
	 * @date 2014-8-27下午05:22:32
	 */
	public boolean stopJobByJobId(long jobId, String account);
	/**
	 * 通过jobId查询对应的结果分析信息
	 * @param jobId
	 * @return
	 * @author peng.jm
	 * @date 2014-8-27下午06:17:47
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
	 * 
	 * @title 获取指定小区的在指定ncs里的测量信息
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-12-8下午5:10:01
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
	 * @return
	 * @author chao.xj
	 * @date 2015-3-27下午4:33:02
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> getDataRecordFromHbase(
			long cityId, String startTime, String endTime,String factory);

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
			List<RnoThreshold> rnoThresholds,
			com.iscreate.op.pojo.rno.RnoLteInterferCalcTask.TaskInfo taskInfo);

	/**
	 * 查询pci自动规划任务
	 * 
	 * @author peng.jm
	 * @date 2015年3月31日15:23:10
	 */
	public List<Map<String, Object>> queryPciPlanTaskByPage(
			Map<String, String> cond, Page newPage, String account);
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