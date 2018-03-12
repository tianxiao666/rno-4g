package com.iscreate.op.dao.rno;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.Eri2GNcsDescQueryCond;
import com.iscreate.op.action.rno.model.Hw2GMrrDescQueryCond;

public interface RnoResourceManageDao {

	/**
	 * 查询符合条件的爱立信ncs的描述记录数
	 * @param eri2gNcsDescQueryCond
	 * @return
	 * @author brightming
	 * 2014-8-22 下午1:39:21
	 */
	public long queryEriNcsDescCnt(Eri2GNcsDescQueryCond eri2gNcsDescQueryCond);

	/**
	 * 分页查询符合条件的爱立信ncs的描述记录
	 * @param eri2gNcsDescQueryCond
	 * @param page
	 * @return
	 * @author brightming
	 * 2014-8-22 下午1:43:26
	 */
	public List<Map<String, Object>> queryEriNcsDescByPage(
			Eri2GNcsDescQueryCond eri2gNcsDescQueryCond,Page page);

	/**
	 * 查询符合条件的华为ncs的描述记录数
	 * @param eri2gNcsDescQueryCond
	 * @return
	 * @author brightming
	 * 2014-8-24 下午5:17:37
	 */
	public long queryHwNcsDescCnt(Eri2GNcsDescQueryCond eri2gNcsDescQueryCond);

	/**
	 * 分页查询符合条件的华为ncs的描述记录
	 * @param eri2gNcsDescQueryCond
	 * @param newPage
	 * @return
	 * @author brightming
	 * 2014-8-24 下午5:17:58
	 */
	public List<Map<String, Object>> queryHwNcsDescByPage(
			Eri2GNcsDescQueryCond eri2gNcsDescQueryCond, Page newPage);
	/**
	 * 计算符合某条件的fas的数量
	 * @param attachParams
	 * @return
	 * @author peng.jm
	 * @date 2015年1月19日18:01:26
	 */
	public long getFasDescriptorCount(Map<String, Object> attachParams);
	/**
	 * 分页查询fas描述信息
	 * @param attachParams
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2015年1月19日18:06:49
	 */
	public List<Map<String, Object>> queryFasDescriptorByPage(
			Map<String, Object> attachParams, int startIndex, int cnt);
	/**
	 * 计算符合某条件的mrr的数量
	 * @param attachParams
	 * @return
	 * @author peng.jm
	 * @date 2014-9-2上午09:51:07
	 */
	public long getMrrDescriptorCount(Map<String, Object> attachParams);

	/**
	 * 分页查询mrr描述信息
	 * @param attachParams
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2014-9-2上午09:51:12
	 */
	public List<Map<String, Object>> queryMrrDescriptorByPage(
			Map<String, Object> attachParams, int startIndex, int cnt);

	/**
	 * 通过描述id获取文件的数据量
	 * @param descId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-2上午11:01:15
	 */
	public long getEriMrrFileRecNumByDescId(long descId);
	/**
	 * 
	 * @title  查询符合条件的华为mrr的描述信息的数量
	 * @param hw2gMrrDescQueryCond
	 * @return
	 * @author chao.xj
	 * @date 2014-9-2下午2:45:09
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long queryHwMrrDescCnt(Hw2GMrrDescQueryCond hw2gMrrDescQueryCond);
	/**
	 * 
	 * @title 分页查询符合条件的华为mrr数据 
	 * @param hw2gMrrDescQueryCond
	 * @param newPage
	 * @return
	 * @author chao.xj
	 * @date 2014-9-2下午2:45:25
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryHwMrrDescByPage(
			Hw2GMrrDescQueryCond hw2gMrrDescQueryCond, Page newPage);

	/**
	 * 获取爱立信mrr文件的小区总数
	 * @param mrrDescId
	 * @param cityId
	 * @param meaTime
	 * @return
	 * @author peng.jm
	 * @date 2014-9-4下午02:23:27
	 */
	public long getEriMrrCellAndBscCntByDescId(long mrrDescId, long cityId, String meaTime);

	/**
	 * 获取爱立信mrr文件的小区和对应的BSC
	 * @param mrrDescId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-4下午02:24:09
	 */
	public List<Map<String, Object>> queryEriMrrCellAndBscByDescId(
			long mrrDescId, long cityId, String meaTime, int startIndex, int cnt);

	/**
	 * 获取爱立信mrr文件的6,7级信号上行质量占比
	 * @param mrrDescId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-4下午02:24:42
	 */
	public List<Map<String, Object>> queryEriMrrUlQua6t7RateByDescId(
			long mrrDescId, long cityId, String meaTime, int startIndex, int cnt);

	/**
	 * 获取爱立信mrr文件的6,7级信号下行质量占比
	 * @param mrrDescId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-4下午02:24:42
	 */
	public List<Map<String, Object>> queryEriMrrDlQua6t7RateByDescId(
			long mrrDescId, long cityId, String meaTime, int startIndex, int cnt);
	/**
	 * 获取爱立信mrr文件的上行平均信号强度
	 * @param mrrDescId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-4下午02:24:42
	 */
	public List<Map<String, Object>> queryEriMrrUlStrenRateByDescId(
			long mrrDescId, long cityId, String meaTime, int startIndex, int cnt);
	/**
	 * 获取爱立信mrr文件的下行平均信号强度
	 * @param mrrDescId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-4下午02:24:42
	 */
	public List<Map<String, Object>> queryEriMrrDlStrenRateByDescId(
			long mrrDescId, long cityId, String meaTime, int startIndex, int cnt);
	/**
	 * 获取爱立信mrr文件的下行弱信号比例
	 * @param mrrDescId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-4下午02:24:42
	 */
	public List<Map<String, Object>> queryEriMrrDlWeekSignalByDescId(
			long mrrDescId, long cityId, String meaTime, int startIndex, int cnt);
	/**
	 * 获取爱立信mrr文件的平均TA
	 * @param mrrDescId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-4下午02:24:42
	 */
	public List<Map<String, Object>> queryEriMrrAverTaByDescId(
			long mrrDescId, long cityId, String meaTime, int startIndex, int cnt);
	/**
	 * 获取爱立信mrr文件的最大TA
	 * @param mrrDescId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-4下午02:24:42
	 */
	public List<Map<String, Object>> queryEriMrrMaxTaByDescId(
			long mrrDescId, long cityId, String meaTime, int startIndex, int cnt);
	/**
	 * 获取爱立信mrr文件的上行通好率
	 * @param mrrDescId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-4下午02:24:42
	 */
	public List<Map<String, Object>> queryEriMrrUlQua0t5RateByDescId(
			long mrrDescId, long cityId, String meaTime, int startIndex, int cnt);
	/**
	 * 获取爱立信mrr文件的下行通好率
	 * @param mrrDescId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-4下午02:24:42
	 */
	public List<Map<String, Object>> queryEriMrrDlQua0t5RateByDescId(
			long mrrDescId, long cityId, String meaTime, int startIndex, int cnt);

	/**
	 * 通过描述Id获取爱立信mrr描述信息
	 * @param mrrDescId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-11下午05:12:23
	 */
	public List<Map<String, Object>> getEriMrrDetailByDescId(long mrrDescId);

	/**
	 * 通过条件获取bsc总数
	 * @param bscQuery
	 * @return
	 * @author peng.jm
	 * @date 2014-9-30下午03:18:45
	 */
	public long queryBscCntByCond(Map<String, String> bscQuery);

	/**
	 * 分页获取bsc信息
	 * @param bscQuery
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2014-9-30下午03:19:03
	 */
	public List<Map<String, Object>> queryBscByPage(
			Map<String, String> bscQuery, int startIndex, int cnt);

	/**
	 * 判断bsc是否存在与小区有关联关系
	 * @param bscEngName
	 * @return
	 * @author peng.jm
	 * @date 2014-10-10下午03:32:12
	 */
	public boolean isBscRelaToCell(String bscEngName);

	/**
	 * 通过名称删除BSC
	 * @param bscEngName
	 * @param areaStr
	 * @return
	 * @author peng.jm
	 * @date 2014-10-10上午10:52:19
	 */
	public boolean deleteBscRelatoAreaByName(String bscEngName, String areaStr);
	/**
	 * 新增单个BSC
	 * @param bscEngName
	 * @param manufacturers
	 * @param cityId
	 * @return
	 * @author peng.jm
	 * @date 2014-10-16下午06:12:15
	 */
	public boolean addSingleBsc(String bscEngName,String manufacturers, long cityId);
}
