package com.iscreate.op.service.rno;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.StsCondition;
import com.iscreate.op.dao.rno.RnoCellDaoImpl;
import com.iscreate.op.dao.rno.RnoReportDao;
import com.iscreate.op.pojo.rno.RnoReportTemplate;
import com.iscreate.op.pojo.rno.RnoRptTemplateDetail;
import com.iscreate.op.pojo.rno.RnoTableDict;

public class RnoReportServiceImpl implements RnoReportService {
	private static Log log = LogFactory.getLog(RnoReportServiceImpl.class);
	// ---注入----//

	private RnoReportDao rnoReportDao;

	public void setRnoReportDao(RnoReportDao rnoReportDao) {
		this.rnoReportDao = rnoReportDao;
	}

	/**
	 * 
	 */
	public List<RnoTableDict> getStsReportDictInfo() {
		return rnoReportDao.getDictFieldsByTabName("RNO_STS");
	}

	/**
	 * 保存话务统计报表模板
	 * 
	 * @param rptTemplate
	 * @param rptDetails
	 * @return
	 * @author brightming 2014-1-3 上午11:41:46
	 */
	public boolean saveStsReportTemplate(RnoReportTemplate rptTemplate,
			List<RnoRptTemplateDetail> rptDetails) {
		return rnoReportDao.saveReportTemplate(rptTemplate, rptDetails);
	}

	/**
	 * 获取报表模板
	 * 
	 * @param tabName
	 * @param scope
	 * @param value
	 * @return
	 * @author brightming 2014-1-6 上午10:32:13
	 */
	public List<RnoReportTemplate> getReportTemplateByScopeAndValue(
			String tabName, String scope, String value) {
		return rnoReportDao.getReportTemplateByScopeAndValue(tabName, scope,
				value);
	}

	/**
	 * 按报表模板分页查询话统数据
	 * 
	 * @param rptTemplateId
	 * @param page
	 * @param queryCondition
	 * @param searchType
	 * @return
	 * @author brightming 2014-1-6 上午10:48:00
	 */
	public List<Map<String, Object>> queryStsByRptTemplatePage(
			long rptTemplateId, Page page, StsCondition queryCondition,
			String searchType) {
		log.info("进入 service 方法：queryStsByRptTemplatePage。rptTemplateId="
				+ rptTemplateId + ",page=" + page + ",queryCondition="
				+ queryCondition + ",searchType=" + searchType);
		if (page == null || queryCondition == null) {
			log.error("queryStsByRptTemplatePage(Page page,StsCondition queryCondition, String searchType)传递的参数不正确！");
			return Collections.emptyList();
		}
		List<Map<String, Object>> stsList = null;

		boolean isAudio = false;
		if ("CELL_VIDEO".equals(searchType)) {
			isAudio = true;
		} else if ("CELL_DATA".equals(searchType)) {
			isAudio = false;
		} else {
			log.error("searchType=" + searchType + "是未知类型，不予查询！");
			return Collections.EMPTY_LIST;
		}
		stsList = rnoReportDao.queryStsByRptTemplateAndPage(rptTemplateId, page, queryCondition, isAudio);
	    // 小区语音业务指标或小区数据业务指标获取数据
		// this.setCellDataToMemCached(queryCondition, false);//
		// 小区语音业务指标或小区数据业务指标查询话务数据，将小区英文信息缓存入MemCached(加载分析列表使用)
		if (stsList == null) {
			log.info("退出 queryStsByPage(Page page,StsCondition queryCondition, String searchType)。获取结果数量：" + 0);
		} else {
			log.info("退出 queryStsByPage(Page page,StsCondition queryCondition, String searchType)。获取结果数量："
					+ stsList.size());
		}

		return stsList;
	}
	/**
	 * 通过模板ID获取话统报表详情字段信息
	 * @param id
	 * @return
	 * @author chao.xj
	 * @date 2014-1-7下午04:40:23
	 */
	public List<Map<String,Object>> getStsRptTemplateFiledByTemplateId(final long id){
		
		return rnoReportDao.getStsRptTemplateFiledByTemplateId(id);
	}
	/**
	 * 
	 * @title 通过分页方式查询报表模板列表
	 * @param reportName
	 * @param displayName
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2014-1-24上午09:56:51
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<RnoReportTemplate> queryReportTemplateListByPage(String reportName,String displayName,Page page)
	{
		log.info("进入方法：queryReportTemplateListByPage。reportName="+reportName+",displayName="+displayName+",page="+page);
		if(page==null){
			log.warn("方法：queryReportTemplateListByPage的参数：page 是空！无法查询!");
			return Collections.EMPTY_LIST;
		}
		long totalCnt=page.getTotalCnt();
		if(totalCnt<0){
			totalCnt=rnoReportDao.getRnoRptTemplateCount(reportName, displayName);
			page.setTotalCnt((int)totalCnt);
			log.info("page.getTotalCnt():"+page.getTotalCnt());
		}
		int startIndex=page.calculateStart();//起始页
		int recordNum=page.getPageSize();//页面大小
		List<RnoReportTemplate> res=rnoReportDao.queryReportTemplateListByPage(reportName, displayName, startIndex, recordNum);
		log.info("退出queryReportTemplateListByPage res:"+res);
		return res;
	}
	/**
	 * 
	 * @title 通过报表模板ID查看字段详情
	 * @param rptTemplateId
	 * @return
	 * @author chao.xj
	 * @date 2014-1-24下午12:02:57
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<RnoRptTemplateDetail> queryRptTemplateDetailListByRptTempId(long rptTemplateId)
	{
		return rnoReportDao.queryRptTemplateDetailListByRptTempId(rptTemplateId);
	}
	/**
	 * 
	 * @title 根据报表模板ID删除报表模板及其详情信息
	 * @param rptTemplateId
	 * @return
	 * @author chao.xj
	 * @date 2014-1-24下午02:36:30
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean rmvRptTemplateAndDetailListByRptTempId(long rptTemplateId)
	{
		return rnoReportDao.rmvRptTemplateAndDetailListByRptTempId(rptTemplateId);
	}
}
