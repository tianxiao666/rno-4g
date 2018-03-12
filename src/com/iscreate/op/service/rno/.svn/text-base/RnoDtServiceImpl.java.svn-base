package com.iscreate.op.service.rno;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.action.rno.Page;
import com.iscreate.op.dao.rno.RnoDtDao;
import com.iscreate.op.pojo.rno.RnoNcsDescriptorWrap;

public class RnoDtServiceImpl implements RnoDtService {

	//－－－－－－－－－－－－－类静态－－－－－－－－－－－－－
	private static Log log = LogFactory.getLog(RnoDtServiceImpl.class);
	private static Gson gson = new GsonBuilder().create();// 线程安全
	//－－－－－－－－－－－注入－－－－－－－－－－－－－
	private RnoDtDao rnoDtDao;
	public RnoDtDao getRnoDtDao() {
		return rnoDtDao;
	}
	public void setRnoDtDao(RnoDtDao rnoDtDao) {
		this.rnoDtDao = rnoDtDao;
	}
	/**
	 * 获取符合条件的dt采样点的数量
	 * @param dtDescId
	 * @return
	 * @author chao.xj
	 * @date 2013-12-2上午09:36:11
	 */
	public int getRnoGsmDtSampleCount(final long dtDescId){
		
		return rnoDtDao.getRnoGsmDtSampleCount(dtDescId);
	}
	/**
	 * 通过区分页查询采样点集合MAP数据
	 * @param dtDescId
	 * @param page
	 * @param attachParams
	 * @return
	 * @author chao.xj
	 * @date 2013-12-2上午10:05:58
	 */
	public List<Map<String, Object>> queryRnoGsmDtSampleListMapsByDescIdAndPage(final long dtDescId,
			Page page,final String mapId){
		
		return rnoDtDao.queryRnoGsmDtSampleListMapsByDescIdAndPage(dtDescId, page, mapId);
	}
	/**
	 * 通过区域ID获得某一DT描述ID下的采样点map数据集合
	 * @param dtDescId
	 * @return
	 * @author chao.xj
	 * @date 2013-11-26上午10:48:45
	 */
	public List<Map<String, Object>> getRnoGsmDtSampleListMapsByArea(final long dtDescId){
		
		return rnoDtDao.getRnoGsmDtSampleListMapsByArea(dtDescId);
	}
	
	/**
	 * 查询符合条件的dt描述项的数量
	 * @param attachParams
	 * @return
	 * @author brightming
	 * 2013-11-27 下午3:12:48
	 */
	public int getDtDescriptorTotalCnt(Map<String, Object> attachParams){
		log.info("进入方法：getDtDescriptorTotalCnt.attachParams="+attachParams);
		if(attachParams==null || attachParams.isEmpty()){
			return 0;
		}
		return rnoDtDao.getDtDescriptorTotalCnt(attachParams);
	}

	/**
	 * 分页查询满足条件的dt描述项
	 * @param page
	 * @param attachParams
	 * @return
	 * @author brightming
	 * 2013-11-27 下午3:13:07
	 */
	public List<Map<String,Object>> queryDtDescriptorByPage(Page page,
			Map<String, Object> attachParams){
		log.info("进入方法：queryDtDescriptorByPage.page="+page+",attachParams="+attachParams);
		if(page==null || attachParams==null || attachParams.isEmpty()){
			return Collections.EMPTY_LIST;
		}
		return rnoDtDao.queryDtDescriptorByPage(page,attachParams);
	}
}
