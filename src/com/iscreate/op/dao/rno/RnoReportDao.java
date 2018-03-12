package com.iscreate.op.dao.rno;

import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.StsCondition;
import com.iscreate.op.pojo.rno.RnoReportTemplate;
import com.iscreate.op.pojo.rno.RnoRptTemplateDetail;
import com.iscreate.op.pojo.rno.RnoTableDict;

public interface RnoReportDao {

	/**
	 * 获取指定表的字典信息
	 * @param tableName
	 * @return
	 * @author brightming
	 * 2014-1-2 下午2:53:24
	 */
	public List<RnoTableDict> getDictFieldsByTabName(String tableName);

	/**
	 * 保存统计模板
	 * @param rptTemplate
	 * @param rptDetails
	 * @return
	 * @author brightming
	 * 2014-1-3 上午11:43:45
	 */
	public boolean saveReportTemplate(RnoReportTemplate rptTemplate,
			List<RnoRptTemplateDetail> rptDetails);
	
	/**
	 * 获取指定报表模板的报表信息
	 * @param tabAlias  表别名
	 * @param templateId
	 * @return
	 * @author brightming
	 * 2014-1-3 下午2:42:33
	 */
	public String getReportFieldsAsSqlByTemplateId(String tabAlias,long templateId);
	
	/**
	 * 根据模板的应用范围和值获取模板
	 * @param tabName 适应表名
	 * @param scope
	 * @param value
	 * @return
	 * @author brightming
	 * 2014-1-3 下午2:50:43
	 */
	public List<RnoReportTemplate> getReportTemplateByScopeAndValue(String tabName,String scope,String value);
	
	
	/**
	 * 获取自定义统计模板指定的话统信息
	 * @param reportTemplateId
	 * @param page
	 * @param queryCondition
	 * @param isAudio
	 * @return
	 * @author brightming
	 * 2014-1-6 上午10:05:27
	 */
	public List<Map<String,Object>> queryStsByRptTemplateAndPage(final long reportTemplateId,final Page page, final StsCondition queryCondition,boolean isAudio);
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
	 * @author Liang YJ
	 * @date 2014-1-21 下午7:07:59
	 * @param tableShortcut
	 * @param statisticType
	 * @return statisticField
	 * @description 获取统计字段
	 */
	public Map<String, String> getstatisticField(String tableName,String tableShortcut, String statisticType);
	/**
	 * 
	 * @title 通过分页方式查询报表模板列表
	 * @param reportName
	 * @param displayName
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author chao.xj
	 * @date 2014-1-24上午09:35:23
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<RnoReportTemplate> queryReportTemplateListByPage(String reportName,String displayName,long startIndex, long recordNum);
	/**
	 * 
	 * @title 查询报表模板总量
	 * @param reportName
	 * @param displayName
	 * @return
	 * @author chao.xj
	 * @date 2014-1-23下午06:53:55
	 * @company 怡创科技
	 * @version 1.2
	 */
	public int getRnoRptTemplateCount(String reportName,String displayName);
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
	
	
	/**
	 * 
	 * @author Liang YJ
	 * @date2014-1-20 下午2:20:21
	 * @param queryCondition
	 * @param isAudio
	 * @param rptTemplateId
	 * @return statisticList;
	 * @description 根据查询条件返回统计结果
	 */
	public List<Map<String,Object>> getRnoStsResultList(StsCondition queryCondition,boolean isAudio,long rptTemplateId,List<String> statisticFieldList, List<String> statisticTitleList);
}
