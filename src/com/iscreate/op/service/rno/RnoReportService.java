package com.iscreate.op.service.rno;

import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.StsCondition;
import com.iscreate.op.pojo.rno.RnoReportTemplate;
import com.iscreate.op.pojo.rno.RnoRptTemplateDetail;
import com.iscreate.op.pojo.rno.RnoTableDict;

public interface RnoReportService {

	/**
	 * 获取话统表的字典信息
	 * @param tableName
	 * @return
	 * @author brightming
	 * 2014-1-2 下午2:51:26
	 */
	public List<RnoTableDict> getStsReportDictInfo();

	/**
	 * 保存话务统计报表模板
	 * @param rptTemplate
	 * @param rptDetails
	 * @return
	 * @author brightming
	 * 2014-1-3 上午11:41:46
	 */
	public boolean saveStsReportTemplate(RnoReportTemplate rptTemplate,
			List<RnoRptTemplateDetail> rptDetails);
	/**
	 * 获取报表模板
	 * @param tabName
	 * @param scope
	 * @param value
	 * @return
	 * @author brightming
	 * 2014-1-6 上午10:32:13
	 */
	public List<RnoReportTemplate> getReportTemplateByScopeAndValue(String tabName,String scope,String value);
	
	/**
	 * 按报表模板分页查询话统数据
	 * @param rptTemplateId
	 * @param page
	 * @param queryCondition
	 * @param searchType
	 * @return
	 * @author brightming
	 * 2014-1-6 上午10:48:00
	 */
	public List<Map<String,Object>> queryStsByRptTemplatePage(long rptTemplateId,Page page, StsCondition queryCondition,String searchType);
	/**
	 * 通过模板ID获取话统报表详情字段信息
	 * @param id
	 * @return
	 * @author chao.xj
	 * @date 2014-1-7下午04:40:23
	 */
	public List<Map<String,Object>> getStsRptTemplateFiledByTemplateId(final long id);
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
	public List<RnoReportTemplate> queryReportTemplateListByPage(String reportName,String displayName,Page page);
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
	public List<RnoRptTemplateDetail> queryRptTemplateDetailListByRptTempId(long rptTemplateId);
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
	public boolean rmvRptTemplateAndDetailListByRptTempId(long rptTemplateId);
}
