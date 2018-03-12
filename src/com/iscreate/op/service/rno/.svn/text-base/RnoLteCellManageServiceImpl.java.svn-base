package com.iscreate.op.service.rno;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.action.rno.LteCellQueryCondition;
import com.iscreate.op.action.rno.Page;
import com.iscreate.op.dao.rno.RnoLteCellDao;

public class RnoLteCellManageServiceImpl implements RnoLteCellManageService {
	private static Log log = LogFactory
			.getLog(RnoLteCellManageServiceImpl.class);

	private RnoLteCellDao rnoLteCellDao;

	public void setRnoLteCellDao(RnoLteCellDao rnoLteCellDao) {
		this.rnoLteCellDao = rnoLteCellDao;
	}

	/**
	 * 分页查询LTE小区信息
	 * 
	 * @param page
	 * @param cond
	 * @return
	 * @author brightming 2014-5-19 下午1:40:21
	 */
	public List<Map<String, Object>> queryLteCellByPage(Page page,
			LteCellQueryCondition cond) {
		log.info("进入方法：queryLteCellByPage。condition=" + cond + ",page=" + page);
		if (page == null) {
			log.warn("方法：queryLteCellByPage的参数：page 是空！无法查询!");
			return Collections.EMPTY_LIST;
		}
		long totalCnt = page.getTotalCnt();
		if (totalCnt < 0) {
			totalCnt = rnoLteCellDao.getLteCellCount(cond);
			page.setTotalCnt((int) totalCnt);
		}
		int startIndex = page.calculateStart();
		int cnt = page.getPageSize();
		List<Map<String, Object>> res = rnoLteCellDao
				.queryLteCellByPage(cond, startIndex, cnt);
		return res;
	}

	/**
	 * 获取小区详情
	 * 
	 * @param lteCellId
	 * @return
	 * @author brightming 2014-5-19 下午1:41:05
	 */
	public Map<String, Object> getLteCellDetail(long lteCellId) {
		return rnoLteCellDao.getLteCellDetail(lteCellId);
	}

	/**
	 * 修改lte小区信息
	 * 
	 * @param lteCellId
	 * @param lteCell
	 * @return
	 * @author brightming 2014-5-19 下午1:42:05
	 */
	public boolean modifyLteCellDetail(long lteCellId,
			Map<String, Object> lteCell) {
		boolean result = rnoLteCellDao.modifyLteCellDetail(lteCellId, lteCell);
		return result;
	}
	
	/**
	 * 查询指定小区的详情，以及与该小区同站的其他小区的详情。
	 * 第一个为目标小区，后面的为其他的小区。
	 * @param lteCellId
	 * @return
	 * @author brightming
	 * 2014-5-19 下午3:54:38
	 */
	public List<Map<String, Object>> queryLteCellAndCositeCells(long lteCellId){
	
		return rnoLteCellDao.queryLteCellAndCositeCells(lteCellId);
	}
	
	/**
	 * 删除指定的lte id
	 * @param ids
	 * @return
	 * @author brightming
	 * 2014-5-21 上午11:24:55
	 */
	public boolean deleteRnoLteCellByIds(String ids){
		return rnoLteCellDao.deleteRnoLteCellByIds(ids);
	}
	/**
	 * 
	 * @title 分页查询Lte邻区关系信息
	 * @param page
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2015-6-30下午2:56:38
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> queryLteNcellByPage(Page page,
			LteCellQueryCondition cond) {
		log.info("进入方法：queryLteNcellByPage。condition=" + cond + ",page=" + page);
		if (page == null) {
			log.warn("方法：queryLteNcellByPage的参数：page 是空！无法查询!");
			return Collections.EMPTY_LIST;
		}
		long totalCnt = page.getTotalCnt();
		if (totalCnt < 0) {
			totalCnt = rnoLteCellDao.getLteNcellCount(cond);
			page.setTotalCnt((int) totalCnt);
		}
		int startIndex = page.calculateStart();
		int cnt = page.getPageSize();
		List<Map<String, Object>> res = rnoLteCellDao
				.queryLteNcellByPage(cond, startIndex, cnt);
		return res;
	}

	/**
	 * 
	 * @title 删除指定的lte邻区 id
	 * @param ids
	 * @return
	 * @author chao.xj
	 * @date 2015-6-30下午2:56:48
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public boolean deleteRnoLteNcellByIds(String ids) {
		return rnoLteCellDao.deleteRnoLteNcellByIds(ids);
	}
}
