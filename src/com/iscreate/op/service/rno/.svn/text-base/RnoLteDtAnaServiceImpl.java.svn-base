package com.iscreate.op.service.rno;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.action.rno.LteCellQueryCondition;
import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.Eri2GNcsDescQueryCond;
import com.iscreate.op.action.rno.model.G4DtDescQueryCond;
import com.iscreate.op.dao.rno.RnoLteCellDao;
import com.iscreate.op.dao.rno.RnoLteDtAnaDao;
import com.iscreate.op.dao.rno.RnoLteSceneManageDao;
import com.iscreate.op.pojo.rno.AreaRectangle;
import com.iscreate.op.pojo.rno.RnoMapLnglatRelaGps;

public class RnoLteDtAnaServiceImpl implements RnoLteDtAnaService {
	private static Log log = LogFactory
			.getLog(RnoLteDtAnaServiceImpl.class);

	private RnoLteDtAnaDao rnoLteDtAnaDao;


	public RnoLteDtAnaDao getRnoLteDtAnaDao() {
		return rnoLteDtAnaDao;
	}
	public void setRnoLteDtAnaDao(RnoLteDtAnaDao rnoLteDtAnaDao) {
		this.rnoLteDtAnaDao = rnoLteDtAnaDao;
	}
	/**
	 * 
	 * @title 查询符合条件的4g 路测描述记录数
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2015-7-8下午8:17:07
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public long queryG4DtDescCnt(final G4DtDescQueryCond cond){
		return rnoLteDtAnaDao.queryG4DtDescCnt(cond);
	}
	/**
	 * 
	 * @title 分页查询符合条件的4G 路测的描述记录
	 * @param cond
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2015-7-8下午8:23:59
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> queryG4DtDescByPage(final 
			G4DtDescQueryCond cond,final Page page){
		return rnoLteDtAnaDao.queryG4DtDescByPage(cond, page);
	}
	/**
	 * 
	 * @title 通过条件查询某一天的DT描述信息返回分析页面
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2015-7-10上午9:48:19
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> queryG4DtDescInfoForMap(final 
			G4DtDescQueryCond cond){
		return rnoLteDtAnaDao.queryG4DtDescInfoForMap(cond);
	}
	/**
	 * 
	 * @title 通过条件查询某一天的DT详情信息返回分析页面
	 * @param descIdStr
	 * @return
	 * @author chao.xj
	 * @date 2015-7-10上午11:55:39
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> queryG4DtDetailInfoForMap(final 
			String descIdStr){
		return rnoLteDtAnaDao.queryG4DtDetailInfoForMap(descIdStr);
	}
	/**
	 * 
	 * @title 查询符合条件的4g 路测描述记录数
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2015-7-8下午8:17:07
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public long queryG4DtDetailDataCnt(final String descIdStr){
		return rnoLteDtAnaDao.queryG4DtDetailDataCnt(descIdStr);
	}
	/**
	 * 
	 * @title 分页查询符合条件的4G 路测的詳情记录
	 * @param cond
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2015-7-8下午8:23:59
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> queryG4DtDetailDataByPage(final String descIdStr,final Page page,Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints){
		return rnoLteDtAnaDao.queryG4DtDetailDataByPage(descIdStr, page,standardPoints);
	}
}
