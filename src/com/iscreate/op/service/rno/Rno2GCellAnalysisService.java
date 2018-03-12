package com.iscreate.op.service.rno;

import java.util.List;
import java.util.Map;


public interface Rno2GCellAnalysisService {

	/**
	 * 获取爱立信小区某两天的参数对比数据
	 * @param cityId
	 * @param paramType
	 * @param paramStr
	 * @param bscStr
	 * @param date1
	 * @param date2
	 * @return
	 * @author peng.jm
	 * @date 2014-10-22下午04:03:12
	 */
	List<Map<String,Object>> eriCellParamsCompare(long cityId,
			String paramType, String paramStr, String bscStr, String date1,
			String date2);

	/**
	 * 判断某个日期是否存在爱立信小区数据
	 * @param cityId
	 * @param bscIdStr
	 * @param paramType
	 * @param date
	 * @return
	 * @author peng.jm
	 * @date 2014-10-22下午04:39:12
	 */
	boolean isEriCellDataExistedOnTheDate(long cityId, String paramType, String bscIdStr,
			String date);

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
	 * @date 2014-10-23下午06:03:45
	 */
	List<Map<String, Object>> getEriCellParamsDiffDetail(long cityId,
			String bsc, String paramType, String param, String date1, String date2);

	/**
	 * 通过检查类型获取爱立信小区的参数一致性结果
	 * @param checkType
	 * @param bscStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author peng.jm
	 * @date 2014-10-27下午02:11:19
	 */
	List<Map<String, Object>> getEriCellParamCheckByType(String checkType,
			String bscStr, String date, long cityId, Map<String,String> settings);

	/**
	 * 导出爱立信小区参数对比结果数据
	 * @param cityId
	 * @param paramType
	 * @param paramStr
	 * @param bscStr
	 * @param date1
	 * @param date2
	 * @param path
	 * @return
	 * @author peng.jm
	 * @date 2014-11-5下午06:14:24
	 */
	String exportEriCellCompareData(long cityId, String paramType,
			String paramStr, String bscStr, String date1, String date2,String path);

	/**
	 * 查询爱立信小区参数分析结果文件进度
	 * @param token
	 * @return
	 * @author peng.jm
	 * @date 2014-11-6下午03:41:26
	 */
	Map<String, Object> queryExportProgress(String token);

	/**
	 * 获取爱立信小区参数分析导出任务的文件路径
	 * @param token
	 * @return
	 * @author peng.jm
	 * @date 2014-11-6下午05:57:55
	 */
	String queryExportTokenFilePath(String token);

	/**
	 * 导出爱立信小区参数一致性检查的结果到文件中
	 * @param items
	 * @param bscStr
	 * @param date1
	 * @param cityId
	 * @param settings
	 * @param path
	 * @return
	 * @author peng.jm
	 * @date 2014-11-10下午06:05:29
	 */
	String exportEriCellCheckData(String items, String bscStr, String date1,
			long cityId, Map<String, String> settings, String path);

	/**
	 * 通过BSC名称获取BSC信息
	 * @param bscStr2
	 * @return
	 * @author peng.jm
	 * @date 2014-11-11上午11:22:12
	 */
	List<Map<String, Object>> getBscDetailByBscs(String bscStr2);

}
