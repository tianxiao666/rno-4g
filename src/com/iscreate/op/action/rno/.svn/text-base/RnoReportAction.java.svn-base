package com.iscreate.op.action.rno;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.pojo.rno.RnoReportTemplate;
import com.iscreate.op.pojo.rno.RnoRptTemplateDetail;
import com.iscreate.op.pojo.rno.RnoTableDict;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.rno.RnoReportService;
import com.iscreate.op.service.rno.RnoTrafficStaticsService;
import com.iscreate.op.service.rno.tool.HttpTools;

public class RnoReportAction extends RnoCommonAction {
	private static Log log = LogFactory.getLog(RnoReportAction.class);

	private RnoReportService rnoReportService;
	private RnoTrafficStaticsService rnoTrafficStaticsService;

	// ---页面变量---//
	List<RnoTableDict> rnoTableDicts;
	private RnoReportTemplate reportTemplate;
	private List<RnoRptTemplateDetail> rptDetail;
	
	private String searchType;
	private StsCondition queryCondition;
	private long areaId;
	private long rptTemplateId;

	private String reportName;
	private String displayName;
	public List<RnoTableDict> getRnoTableDicts() {
		return rnoTableDicts;
	}

	public void setRnoTableDicts(List<RnoTableDict> rnoTableDicts) {
		this.rnoTableDicts = rnoTableDicts;
	}

	public void setRnoReportService(RnoReportService rnoReportService) {
		this.rnoReportService = rnoReportService;
	}

	public RnoReportTemplate getReportTemplate() {
		return reportTemplate;
	}

	public void setReportTemplate(RnoReportTemplate reportTemplate) {
		this.reportTemplate = reportTemplate;
	}

	public List<RnoRptTemplateDetail> getRptDetail() {
		return rptDetail;
	}

	public void setRptDetail(List<RnoRptTemplateDetail> rptDetail) {
		this.rptDetail = rptDetail;
	}

	public void setRnoTrafficStaticsService(
			RnoTrafficStaticsService rnoTrafficStaticsService) {
		this.rnoTrafficStaticsService = rnoTrafficStaticsService;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public StsCondition getQueryCondition() {
		return queryCondition;
	}

	public void setQueryCondition(StsCondition queryCondition) {
		this.queryCondition = queryCondition;
	}

	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public long getRptTemplateId() {
		return rptTemplateId;
	}

	public void setRptTemplateId(long rptTemplateId) {
		this.rptTemplateId = rptTemplateId;
	}
	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	/**
	 * 初始化报表模板管理页面
	 * 
	 * @return
	 * @author brightming 2014-1-2 下午2:49:39
	 */
	public String initRnoReportTemplateManagePageAction() {
		rnoTableDicts = rnoReportService.getStsReportDictInfo();
		return "success";
	}

	/**
	 * 保存用户自定义报表模板
	 * 
	 * @author brightming 2014-1-2 下午5:52:00
	 */
	public void saveReportTemplateForAjaxAction() {
		log.info("进入 action：saveReportTemplateForAjaxAction.reportTemplate="
				+ reportTemplate + ",rptDetail="
				+ rptDetail );

		if (reportTemplate == null || reportTemplate.getReportName()== null
				|| "".equals(reportTemplate.getReportName().trim())) {
			log.error("未提供报表模板的名称信息！");
			HttpTools.writeToClient("{\"flag\":false,\"msg\":\"请提供报表模板的名称！\"}");
			return;
		}

		if (rptDetail == null || rptDetail.size() == 0) {
			log.error("请选择报表模板需要的模板字段信息！");
			HttpTools.writeToClient("{\"flag\":false,\"msg\":\"请选择报表模板需要的信息字段！\"}");
			return;
		}
		int fieldCnt = rptDetail.size();
		log.info("拟新增的报表模板包含字段：[" + fieldCnt + "]个");

		String reportName = reportTemplate.getReportName().trim();
		String account = (String) SessionService.getInstance().getValueByKey(
				"userId");

		// 报表模板信息
		RnoReportTemplate rpt = new RnoReportTemplate();
		rpt.setApplyScope(RnoConstant.ApplyScope.PERSONAL.getCode());
		rpt.setReportName(reportName);
		rpt.setStatus("N");
		Date now = new Date();
		rpt.setCreateTime(now);
		rpt.setCreator(account);
		rpt.setModTime(now);
		rpt.setTableName("RNO_STS");
		rpt.setScopeValue(account);

		// 报表模板详情
		List<RnoRptTemplateDetail> details=new ArrayList<RnoRptTemplateDetail>();
		RnoRptTemplateDetail one=null;
		for(RnoRptTemplateDetail det:rptDetail){
			if(det==null){
				continue;
			}
			one=new RnoRptTemplateDetail();
			one.setDisplayName(det.getDisplayName());
			one.setDisplayOrder(det.getDisplayOrder());
			one.setTableFields(det.getTableFields());
			details.add(one);
		}
		
		boolean ok=rnoReportService.saveStsReportTemplate(rpt,details);
		if(ok){
			
			HttpTools.writeToClient("{\"flag\":true,\"msg\":\"报表模板保存成功。\"}");
		}else{
			HttpTools.writeToClient("{\"flag\":false,\"msg\":\"报表模板保存失败，请检查数据并重试。\"}");
		}
		
	}
	
	/**
	 * 获取适用于指定用户的统计报表模板
	 * 
	 * @author brightming
	 * 2014-1-6 上午10:22:37
	 */
	public void getPersonalReportTemplateForAjaxAction(){
		log.info("进入Action方法getReportTemplateForAjaxAction。");
		String account = (String) SessionService.getInstance().getValueByKey(
				"userId");
		List<RnoReportTemplate> tmps=rnoReportService.getReportTemplateByScopeAndValue("RNO_STS", RnoConstant.ApplyScope.PERSONAL.getCode(), account);
		log.info("user=["+account+"]的自定义模板为："+tmps);
		String result=gson.toJson(tmps);
		HttpTools.writeToClient(result);
	}
	
	/**
	 * 按照模板分页查询话统数据
	 * 
	 * @author brightming
	 * 2014-1-6 上午10:39:22
	 */
	public void queryStsByRptTemplateForAjaxAction(){
		log.info("进入action方法：queryStsByRptTemplateForAjaxAction。rptTemplateId="+rptTemplateId+",page="+page+",areaId="+areaId+",searchType="+searchType+",queryCondition="+queryCondition);
		if (page.getCurrentPage() <= 0) {
			page.setCurrentPage(1);
		}
		boolean isAudio=false;
		if("CELL_VIDEO".equals(searchType)){
			isAudio=true;
		}
		//获取总数量，
		int totalCnt = this.rnoTrafficStaticsService.getTotalStsCount(
				this.queryCondition, this.searchType, false,isAudio);
		//获取话统列表
		List<Map<String, Object>> stsList = this.rnoReportService.queryStsByRptTemplatePage(rptTemplateId, page, queryCondition, searchType);
		
		Page newPage = new Page();
		newPage.setCurrentPage(page.getCurrentPage());
		newPage.setPageSize(page.getPageSize());
		newPage.setTotalCnt(totalCnt);
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		Gson gson = new GsonBuilder().create();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("stsList", stsList);
		resultMap.put("newPage", newPage);
		String result = gson.toJson(resultMap);
		log.info("退出方法：queryStsByRptTemplateForAjaxAction。 返回：" + result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 通过模板ID获取话统报表详情字段信息
	 * 
	 * @author chao.xj
	 * @date 2014-1-7下午04:55:48
	 */
	public void getStsRptTemplateFiledForAjaxAction(){
		log.info("进入getStsRptTemplateFiledForAjaxAction　rptTemplateId:"+rptTemplateId);
		List<Map<String, Object>> filedList=rnoReportService.getStsRptTemplateFiledByTemplateId(rptTemplateId);
		Gson gson = new GsonBuilder().create();
		String result=gson.toJson(filedList);
		log.info("退出getStsRptTemplateFiledForAjaxAction　result:"+result);
		HttpTools.writeToClient(result);
	}
	
	/**
	 * 获取所有字段和标题的关系
	 * @author Liang YJ
	 * @date 2014-1-14
	 *
	 */
	public void getRnoTableDictsAction()
	{
		log.info("进入getRnoTableDictsAction");
		rnoTableDicts = rnoReportService.getStsReportDictInfo();
		Gson gson = new GsonBuilder().create();
		String result=gson.toJson(rnoTableDicts);
		log.info("退出getRnoTableDictsAction");
		HttpTools.writeToClient(result);
	}
	/**
	 * 
	 * @title 通过分页方式查询报表模板列表
	 * @author chao.xj
	 * @date 2014-1-24上午10:04:26
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void queryReportTemplateListByPageForAjaxAction(){
		log.info("进入：queryReportTemplateListByPageForAjaxAction。reportName=" + reportName
				+ ",displayName="+displayName+",page=" + page);

		if (page == null) {
			log.error("方法queryReportTemplateListByPageForAjaxAction的page参数为空！");
			HttpTools.writeToClient("[]");
			return;
		}
		
		List<RnoReportTemplate> rnoreporttempList=rnoReportService.queryReportTemplateListByPage(reportName, displayName, page);
		String result1 = gson.toJson(rnoreporttempList);
		//page已经发生改变
//		log.info("action 原生page.getTotalCnt():"+page.getTotalCnt());
		Page newPage = page.copy();
		int totalCnt = newPage.getTotalCnt();
//		log.info("newPage.getTotalCnt():"+totalCnt);
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String result = "{'page':" + pstr + ",'data':" + result1 + "}";
		log.info("退出queryReportTemplateListByPageForAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 
	 * @title 通过报表模板ID查看字段详情
	 * @author chao.xj
	 * @date 2014-1-24下午12:10:04
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void queryRptTemplateDetailListByRptTempIdForAjaxAction() {
		log.info("进入queryRptTemplateDetailListByRptTempIdForAjaxAction rptTemplateId:"+rptTemplateId);
		List<RnoRptTemplateDetail> rpttemplateList=rnoReportService.queryRptTemplateDetailListByRptTempId(rptTemplateId);
		String result=gson.toJson(rpttemplateList);
		log.info("退出queryRptTemplateDetailListByRptTempIdForAjaxAction result:"+result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 
	 * @title 根据报表模板ID删除报表模板及其详情信息
	 * @author chao.xj
	 * @date 2014-1-24下午02:38:53
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void rmvRptTemplateAndDetailListByRptTempIdForAjaxAction() {
		log.info("进入rmvRptTemplateAndDetailListByRptTempIdForAjaxAction rptTemplateId:"+rptTemplateId);
		boolean flag=rnoReportService.rmvRptTemplateAndDetailListByRptTempId(rptTemplateId);
		String result=gson.toJson(flag);
		log.info("退出rmvRptTemplateAndDetailListByRptTempIdForAjaxAction result:"+result);
		HttpTools.writeToClient(result);
	}
}
