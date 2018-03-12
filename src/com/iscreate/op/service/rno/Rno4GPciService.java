package com.iscreate.op.service.rno;

import java.sql.Statement;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.G4MrDescQueryCond;

public interface Rno4GPciService {

	/**
	 * 
	 * @title 分页查询4g干扰矩阵信息
	 * @param condition
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15上午10:52:43
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> query4GInterferMartixByPage(
			Map<String, String> condition, Page page);
	/**
	 * 
	 * @title 分页获取Hbase的Mr数据描述表的数据
	 * @param cond
	 * @param newPage
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15上午11:35:13
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, String>> queryMrDataFromHbaseByPage(
			G4MrDescQueryCond cond, Page newPage);
	/**
	 * 
	 * @title 检查这周是否计算过4g pci干扰矩阵
	 * @param areaId
	 * @param thisMonday
	 * @param today
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15下午1:59:46
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> check4GInterMartixThisWeek(long areaId,
			String thisMonday, String today);
	/**
	 * 
	 * @title 新增4g pci 干扰矩阵
	 * @param condition
	 * @param account
	 * @author chao.xj
	 * @date 2015-4-15下午2:58:38
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void add4GInterMartixByMr(Map<String, String> condition,
			String account) ;
	/**
	 * 
	 * @title 查询对应条件的MR数据记录数量
	 * @param condition
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15下午3:06:30
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public int queryMrDataCountByCond(Map<String, String> condition);
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
	 * @param cell1
	 * @param pci1
	 * @param cell2
	 * @param pci2
	 * @return
	 */
	public boolean changeLteCellPci(String cell1, String pci1, String cell2,
			String pci2);
	/**
	 * 
	 * @title 提交4g方位角计算任务
	 * @param account
	 * @param taskInfo
	 * @return
	 * @author chao.xj
	 * @date 2015-4-29下午3:22:32
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public Map<String, Object> submit4GAzimuthCalcTask(String account,
			
			final Map<String, Object> taskInfo);
	/**
	 * 
	 * @title 查询4g方位角计算任务
	 * @param cond
	 * @param page
	 * @param account
	 * @return
	 * @author chao.xj
	 * @date 2015-4-29下午3:40:24
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> query4GAzimuthCalcTaskByPage(
			Map<String, String> cond, Page page, String account);
	/**
	 * 
	 * @title 通过区域ID获取LTE小区标识对应的小区信息
	 * @param stmt
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2015-4-29下午3:40:24
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public Map<String, List<String>> getLteCellInfoByCellId(Statement stmt,long cityId);
}
