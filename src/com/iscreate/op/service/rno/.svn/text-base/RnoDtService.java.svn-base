package com.iscreate.op.service.rno;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.pojo.rno.RnoNcsDescriptorWrap;

public interface RnoDtService {

	/**
	 * 获取符合条件的dt采样点的数量
	 * @param dtDescId
	 * @return
	 * @author chao.xj
	 * @date 2013-12-2上午09:36:11
	 */
	public int getRnoGsmDtSampleCount(final long dtDescId);
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
			Page page,final String mapId);
	/**
	 * 通过区域ID获得某一DT描述ID下的采样点map数据集合
	 * @param dtDescId
	 * @return
	 * @author chao.xj
	 * @date 2013-11-26上午10:48:45
	 */
	public List<Map<String, Object>> getRnoGsmDtSampleListMapsByArea(final long dtDescId);

	/**
	 * 查询符合条件的dt描述项的数量
	 * @param attachParams
	 * @return
	 * @author brightming
	 * 2013-11-27 下午3:12:48
	 */
	public int getDtDescriptorTotalCnt(Map<String, Object> attachParams);

	/**
	 * 分页查询满足条件的dt描述项
	 * @param page
	 * @param attachParams
	 * @return
	 * @author brightming
	 * 2013-11-27 下午3:13:07
	 */
	public List<Map<String,Object>> queryDtDescriptorByPage(Page page,
			Map<String, Object> attachParams);
}
