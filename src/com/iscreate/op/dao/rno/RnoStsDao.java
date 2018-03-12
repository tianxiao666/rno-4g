package com.iscreate.op.dao.rno;

import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.StsCondition;
import com.iscreate.op.pojo.rno.RnoSts;

public interface RnoStsDao {
	/**
	 * 
	 * @description: 小区语音业务指标或小区数据业务指标分页获取数据
	 * @author：yuan.yw
	 * @param page
	 * @param queryCondition
	 * @param isAudio是否查语音
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Oct 10, 2013 2:22:52 PM
	 */
	public List<Map<String,Object>> queryStsByCellVideoOrDataByPage(final Page page, final StsCondition queryCondition,boolean isAudio);
	/**
	 * 
	 * @description: 小区语音业务指标或小区数据业务指标条件获取数据数量
	 * @author：yuan.yw
	 * @param queryCondition
	 * @return     
	 * @return int     
	 * @date：Oct 10, 2013 3:50:36 PM
	 */
	public int getTotalQueryStsByCellVideoOrData(
			final StsCondition queryCondition,boolean isAudio);
	/**
	 * 
	 * @description: 小区语音业务指标或小区数据业务指标条件获取小区信息（加载分析列表使用）
	 * @author：yuan.yw
	 * @param queryCondition
	 * @return     
	 * @return int     
	 * @date：Oct 10, 2013 3:50:36 PM
	 */
	public List<Map<String,Object>> getCellQueryByCellVideoOrData(
			final StsCondition queryCondition,boolean isAudio);
	/**
	 * 
	 * @description: 城市网络质量指标分页查询话务数据
	 * @author：yuan.yw
	 * @param page
	 * @param queryCondition
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Oct 10, 2013 2:22:52 PM
	 */
	public List<Map<String,Object>> queryStsByCityQuaByPage(final Page page, final StsCondition queryCondition);
	/**
	 * 
	 * @description: 城市网络质量指标分页查询话务数据数量
	 * @author：yuan.yw
	 * @param queryCondition
	 * @return     
	 * @return int     
	 * @date：Oct 10, 2013 3:50:36 PM
	 */
	public int getTotalQueryStsByCityQua(
			final StsCondition queryCondition);
	
	
	/**
	 * 在指定区域插入新的RnoSts对象
	 */
	public Long insertRnoSts(RnoSts sts);
	
	/**
	 * 根据描述ID获取话统数据
	* @author ou.jh
	* @date Oct 10, 2013 4:00:44 PM
	* @Description: TODO 
	* @param @param descriptorId
	* @param @return        
	* @throws
	 */
	public List<RnoSts> getRnoStsBydescriptorId(final long descriptorId);
	
	/**
	 * 
	 * @return
	 * @author ou.jh
	 * 
	 */
	public List<RnoSts> getAllRnoSts();
	/**
	 * 
	 * @description: 小区语音业务指标或小区数据业务指标条件获取话务数据
	 * @author：yuan.yw
	 * @param queryCondition
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Oct 29, 2013 10:28:05 AM
	 */
	public List<Map<String,Object>> queryStsByVideoOrDataCondition(final StsCondition queryCondition,boolean isAudio);
	
	/**
	 * 
	 * @author Liang YJ
	 * @date 2014-1-20 下午3:40:47
	 * @param field
	 * @param queryCondition
	 * @param isAudio
	 * @return List<Map<String,Object>>
	 * @description 小区语音业务指标或小区数据业务指标条件获取话务数据汇总后的结果
	 */
	public List<Map<String,Object>> queryStsByVideoOrDataCondition(String field, final StsCondition queryCondition,final boolean isAudio);
	/**
	 * 
	 * @description: 获取小区语音业务指标或小区数据业务指标获取数据sql
	 * @author：yuan.yw
	 * @param field
	 * @param queryCondition
	 * @pramm sqlForWhat  sql查询目的（数量 count  话务数据 sts  小区数据 cell）
	 * @return     
	 * @return String     
	 * @date：Oct 10, 2013 3:15:48 PM
	 */
	public String getQueryStsByCellVideoOrDataSql(String field,StsCondition queryCondition,String sqlForWhat,boolean isAudio);
}
