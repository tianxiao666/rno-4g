package com.iscreate.op.dao.rno;

import java.util.List;
import java.util.Map;

public interface Rno2GCellAnalysisDao {

	/**
	 * 判断某个日期是否存在爱立信小区数据
	 * @param cityId
	 * @param bscStr
	 * @param paramType
	 * @param date
	 * @return
	 * @author peng.jm
	 * @date 2014-10-22下午04:39:12
	 */
	boolean isEriCellDataExistedOnTheDate(long cityId, String paramType,
			String bscStr, String date);

	/**
	 * 获取对应bscId的BSC信息
	 * @param bscStr
	 * @return
	 * @author peng.jm
	 * @date 2014-10-22下午05:20:32
	 */
	List<Map<String, Object>> getBscByBscIdStr(String bscStr);

	/**
	 * 通过参数类型获取爱立信小区两个日期的参数比较结果
	 * @param cityId
	 * @param paramType
	 * @param paramStr
	 * @param bscStr
	 * @param date1
	 * @param date2
	 * @return
	 * @author peng.jm
	 * @date 2014-10-23下午03:08:21
	 */
	List<Map<String, Object>> getEriCellParamsCompareResByTypeAndDate(
			long cityId, String paramType, String paramStr, String bscStr,
			String date1, String date2);

	/**
	 * 获取爱立信小区某两个日期的参数不一致的详情
	 * @param cityId
	 * @param bsc
	 * @param param
	 * @param paramType
	 * @param date1
	 * @param date2
	 * @return
	 * @author peng.jm
	 * @date 2014-10-23下午06:05:13
	 */
	List<Map<String, Object>> getEriCellParamsDiffDetail(long cityId,
			String bsc, String paramType, String param, String date1, String date2);
	/**
	 * 通过bsc名称字符串获取bsc信息
	 * @param bscStr
	 * @return
	 * @author peng.jm
	 * @date 2014-10-27下午06:17:45
	 */
	List<Map<String, Object>> getBscDetailByBscs(String bscStr);

	/**
	 * 获取爱立信小区功率检查结果
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @return
	 * @author peng.jm
	 * @date 2014-10-27下午02:19:17
	 */
	List<Map<String, Object>> getEriCellPowerCheckResult(String bscStr,
			String date, long cityId);

	/**
	 * 获取爱立信小区跳频检查结果
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author peng.jm
	 * @date 2014-10-27下午06:05:57
	 */
	List<Map<String, Object>> getEriCellFreqHopCheckResult(String bscIdStr,
			String date, long cityId, Map<String,String> settings);

	/**
	 * 获取爱立信小区Nccperm检查结果
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author peng.jm
	 * @date 2014-10-28下午04:16:24
	 */
	List<Map<String, Object>> getEriCellNccpermResult(String bscIdStr,
			String date, long cityId, Map<String, String> settings);
	
	/**
	 * 获取爱立信小区单向邻区检查
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author peng.jm
	 * @date 2014-10-29下午01:54:41
	 */
	List<Map<String, Object>> getEriCellUnidirNcellResult(String bscIdStr,
			String date, long cityId, Map<String, String> settings);
	
	/**
	 * 获取爱立信小区同邻频数据信息
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author peng.jm
	 * @date 2014-10-30下午02:04:06
	 */
	List<Map<String, Object>> getEriCellSameNcellFreqData(String bscIdStr,
			String date, long cityId, Map<String, String> settings);
	
	/**
	 * 获取爱立信小区邻区数据检查
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author peng.jm
	 * @date 2014-10-29下午06:20:45
	 */
	List<Map<String, Object>> getEriCellNcellDataCheckResult(String bscIdStr,
			String date, long cityId, Map<String, String> settings);
	
	/**
	 * 获取爱立信小区测量频点的active，idle以及邻区组成的bcch结果集
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author peng.jm
	 * @date 2014-10-28下午06:06:07
	 */
	List<Map<String, Object>> getEriCellMeaFreqResult(
			String bscIdStr, String date, long cityId,
			Map<String, String> settings);
	/**
	 * 
	 * @title 获取爱立信小区BaNum的结果集
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author chao.xj
	 * @date 2014-10-29上午10:39:57
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getEriCellBaNumCheckResult(
			String bscIdStr, String date, long cityId, Map<String, String> settings);
	/**
	 * 
	 * @title 获取爱立信小区TALIM_MAXTA检查结果集
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author chao.xj
	 * @date 2014-10-29下午1:55:51
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getEriCellTalimAndMaxtaCheckResult(
			String bscIdStr, String date, long cityId, Map<String, String> settings);
	/**
	 * 
	 * @title 获取爱立信小区同频同bsic检查结果集
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author chao.xj
	 * @date 2014-10-29下午1:55:51
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getEriCellCoBsicCheckResult(
			String bscIdStr, String date, long cityId, Map<String, String> settings);
	/**
	 * 
	 * @title 通过源与目标小区得到其经纬度数据113.4087,23.07341,113.27481,23.04926
	 * @param sourcecell
	 * @param targetcell
	 * @return
	 * @author chao.xj
	 * @date 2014-10-29下午3:10:09
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<String> getLnglatsBySourceCellAndTargetCell(
			final String sourcecell, final String targetcell);
	/**
	 * 
	 * @title 获取爱立信小区邻区过多过少检查结果集
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author chao.xj
	 * @date 2014-10-29下午1:55:51
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getEriCellNcellNumCheckResult(
			String bscIdStr, String date, long cityId, Map<String, String> settings);
	/**
	 * 
	 * @title 获取爱立信小区本站邻区漏定义检查结果集
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author chao.xj
	 * @date 2014-10-30下午14:55:51
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getEriCellNcellMomitCheckResult(
			String bscIdStr, String date, long cityId, Map<String, String> settings);

	List<Map<String, Object>> exportEriCellCompareData(long cityId,
			String paramType, String paramStr, String bscStr, String date1,
			String date2);
}
