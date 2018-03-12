package com.iscreate.op.dao.rno;

import java.util.List;
import java.util.Map;

public interface RnoNcsDynaCoverageDao {

	/**
	 * 检查小区是华为还是爱立信
	 * @title
	 * @return
	 * @author peng.jm
	 * @date 2015年3月10日10:59:56
	 * @company 怡创科技
	 */
	String checkCellIsHwOrEri(String cell);

	/**
	 * 查询爱立信ncs数据，并整理得到需要结果
	 * @title
	 * @return
	 * @author peng.jm
	 * @date 2015年3月10日10:59:56
	 * @company 怡创科技
	 */
	List<Map<String, Object>> queryEriDataFromOracle(long cityId, String cell,
			String startDate, String endDate, String RELSS);

	/**
	 * 查询华为ncs数据，并整理得到需要结果
	 * @title
	 * @return
	 * @author peng.jm
	 * @date 2015年3月10日10:59:56
	 * @company 怡创科技
	 */
	List<Map<String, Object>> queryHwDataFromOracle(long cityId, String cell,
			String startDate, String endDate, String RELSS);

	/**
	 * 按条件获取2g小区方向角计算任务信息总数
	 * @title
	 * @return
	 * @author peng.jm
	 * @date 2015年5月4日14:04:38
	 * @company 怡创科技
	 */
	long get2GDirectionAngleTaskCount(Map<String, String> cond);

	/**
	 * 按条件分页获取2g小区方向角计算任务信息
	 * @title
	 * @return
	 * @author peng.jm
	 * @date 2015年5月4日14:04:38
	 * @company 怡创科技
	 */
	List<Map<String, Object>> query2GDirectionAngleTaskByPage(
			Map<String, String> cond, int startIndex, int cnt);

	/**
	 * 查询对应条件的NCS数据记录数量
	 *
	 * @param cond
	 * @return
	 * @author peng.jm
	 * @date 2014-8-16下午05:02:24
	 */
	long getNcsDataCount(Map<String, String> cond);
	/**
	 * 获取seq的值
	 * @param seq  seq名称
	 * @return
	 * @author peng.jm
	 * @date 2014-8-16下午06:28:31
	 */
	long getNextSeqValue(String string);
	/**
	 * 根据条件获取NCS的数据记录总数量
	 * @param cityId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author peng.jm
	 * @date 2014-8-16下午05:57:59
	 */
	long queryNcsDataRecordsCount(long cityId, String startMeaDate,
			String endMeaDate);

	/**
	 * 创建2g小区方向角计算任务
	 * @param cityId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author peng.jm
	 * @date 2015年5月4日17:44:39
	 */
	boolean create2GDirectionAngleTask(Map<String, Object> interMartix);

	/**
	 * 从Hbase查询MR数据，并整理得到需要结果
	 * 两种比例所对应的数据集 {'res1':[],'res2':[]}
	 * @title
	 * @return
	 * @author peng.jm
	 * @date 2015年3月10日10:59:56
	 * @company 怡创科技
	 */
	Map<String,List<Map<String,Object>>> queryDynaCoverDataFromHbaseMrTable(long cityId,
			String lteCellId, String startDate, String endDate);
	/**
	 *
	 * @title　通过表索引从Hbase查询４gMR指标数据
	 * @param tableName
	 * 表索引为LTE4G_CELL_MEATIME 或  LTE4G_NCELL_MEATIME
	 * @param cityId
	 * @param lteCellId
	 * @param startDate
	 * @param endDate
	 * @return
	 * @author chao.xj
	 * @date 2015-5-27上午11:04:46
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, String>> query4GCellMrIndexFromHBase(
			String dataTabName,long cityId,String lteCellId,String startDate, String endDate,String indexTabName);
	/**
	 *
	 * @title 获取lte小区形状数据通过区域 即三角形的三点坐标
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2015-6-15下午1:48:38
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public Map<String, Object> queryLteCellShapeDataByCityId(final long cityId);
	/**
	 *
	 * @title 获取某个地市 的小区频段类型信息
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2015-6-15下午3:06:41
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public Map<String, Object> queryCellBandTypeByCityId(final long cityId);
}
