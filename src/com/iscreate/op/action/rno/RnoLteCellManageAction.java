package com.iscreate.op.action.rno;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.action.rno.model.LteCellUpdateResult;
import com.iscreate.op.service.rno.RnoLteCellManageService;
import com.iscreate.op.service.rno.tool.HttpTools;

public class RnoLteCellManageAction extends RnoCommonAction {

	private static Log log = LogFactory.getLog(RnoLteCellManageAction.class);

	private LteCellQueryCondition queryLteCellCond;
	//邻区参数

	private RnoLteCellManageService rnoLteCellManageService;

	// ------------//
	private long lteCellId;

	private String ids;
	
	private LteCellUpdateResult lteCellUpdateResult;

	public LteCellQueryCondition getQueryLteCellCond() {
		return queryLteCellCond;
	}

	public void setRnoLteCellManageService(
			RnoLteCellManageService rnoLteCellManageService) {
		this.rnoLteCellManageService = rnoLteCellManageService;
	}

	public void setQueryLteCellCond(LteCellQueryCondition queryLteCellCond) {
		this.queryLteCellCond = queryLteCellCond;
	}

	public void setLteCellId(long lteCellId) {
		this.lteCellId = lteCellId;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
	
	public LteCellUpdateResult getLteCellUpdateResult() {
		return lteCellUpdateResult;
	}

	public void setLteCellUpdateResult(LteCellUpdateResult lteCellUpdateResult) {
		this.lteCellUpdateResult = lteCellUpdateResult;
	}

	/**
	 * 进入lte小区管理页面
	 * 
	 * @return
	 * @author brightming 2014-5-15 下午3:41:22
	 */
	public String initLteCellManageAction() {
		initAreaList();
		return "success";
	}
	/**
	 * 
	 * @title 初始化lte邻区管理所需信息
	 * @return
	 * @author chao.xj
	 * @date 2015-6-30下午2:48:16
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public String initLteNCellManagerAction() {
		initAreaList();
		return "success";
	}
	/**
	 * 分页查询lte小区信息
	 * 
	 * @author brightming 2014-5-19 下午1:35:50
	 */
	public void queryLteCellByPageForAjaxAction() {
		log.info("进入：queryLteCellByPageForAjaxAction。queryLteCellCond="
				+ queryLteCellCond + ",page=" + page);
		if (page == null) {
			log.error("方法queryLteCellByPageForAjaxAction的page参数为空！");
			HttpTools.writeToClient("[]");
			return;
		}
		Page newPage = page.copy();
		List<Map<String, Object>> celldatali = rnoLteCellManageService
				.queryLteCellByPage(newPage, queryLteCellCond);
		log.debug("lte cell data list:" + celldatali.size());
		String result1 = gson.toJson(celldatali);

		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String result = "{'page':" + pstr + ",'data':" + result1 + "}";
		log.info("退出queryLteCellByPageForAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}

	/**
	 * 查询指定id的lte小区以及同站的其他小区的详情 返回json队列，第一个为当前选择查看详情的目标小区，后面的为与 该小区同enodeb的其他小区
	 * 
	 * @author brightming 2014-5-19 下午3:51:19
	 */
	public void queryLteCellAndCositeCellsDetailForAjaxAction() {
		log.debug("进入方法：queryLteCellAndCositeCellsDetailForAjaxAction。lteCellId="
				+ lteCellId);
		List<Map<String, Object>> cells = rnoLteCellManageService
				.queryLteCellAndCositeCells(lteCellId);
		//peng.jm加入,格式化日期
		String operation_time = "";
		for (int i = 0; i < cells.size(); i++) {
			if(!("").equals(cells.get(i).get("OPERATION_TIME")) && cells.get(i).get("OPERATION_TIME") != null){
				operation_time =sdf_SIMPLE.format(cells.get(i).get("OPERATION_TIME"));
			}
			cells.get(i).put("OPERATION_TIME", operation_time);
		}
		
		String result = gson.toJson(cells);
		log.debug("queryLteCellAndCositeCellsDetailForAjaxAction输出 ：" + result);
		HttpTools.writeToClient(result);
	}

	/**
	 * 删除指定的lte小区
	 * 
	 * @author brightming 2014-5-21 上午11:22:50
	 */
	public void deleteLteCellByIdsForAjaxAction() {
		if (ids == null || ids.isEmpty()) {
			log.error("未指定需要删除的LTE小区的id！");
			HttpTools.writeToClient("{'flag':false,'msg':'未指定需要删除的LTE小区'}");
			return;
		}
		String[] arr = ids.split(",");
		if (arr == null || arr.length == 0) {
			log.error("未按正确格式指定待删除的LTE小区！");
			HttpTools.writeToClient("{'flag':false,'msg':'未按正确格式指定待删除的LTE小区'}");
			return;
		}
		for (String ar : arr) {
			try {
				Long.parseLong(ar);
			} catch (Exception e) {
				log.error("指定待删除的LTE小区的编号无效");
				HttpTools.writeToClient("{'flag':false,'msg':'指定待删除的LTE小区的编号无效'}");
				return;
			}
		}
		
		boolean ok=rnoLteCellManageService.deleteRnoLteCellByIds(ids);
		if(ok){
			HttpTools.writeToClient("{'flag':true}");
		}else{
			HttpTools.writeToClient("{'flag':false}");
		}
	}
	
	/**
	 * 地图页面的lte信息更新
	 * @return
	 * @author peng.jm
	 * 2014-5-22 14:27:16
	 * @throws UnsupportedEncodingException 
	 */
	public void updateLteCellAndCoSiteCellDetailForAjaxAction() throws UnsupportedEncodingException {
		log.debug("进入方法：updateLteCellAndCoSiteCellDetailForAjaxAction");
		Map<String,Object> lteCell = lteCellUpdateResult.resultToMap();
		boolean resultFromServer = rnoLteCellManageService.modifyLteCellDetail(lteCellId, lteCell);
		log.debug("updateLteCellAndCoSiteCellDetailForAjaxAction ：" + resultFromServer);
		if(resultFromServer){
			HttpTools.writeToClient("{'flag':true}");
		}else{
			HttpTools.writeToClient("{'flag':false}");
		}
	}
	/**
	 * 
	 * @title 分页查询Lte邻区关系信息
	 * @author chao.xj
	 * @date 2015-6-30下午2:54:02
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void queryLteNcellByPageForAjaxAction() {
		log.info("进入：queryLteNcellByPageForAjaxAction。queryLteCellCond="
				+ queryLteCellCond + ",page=" + page);
		if (page == null) {
			log.error("方法queryLteNcellByPageForAjaxAction的page参数为空！");
			HttpTools.writeToClient("[]");
			return;
		}
		Page newPage = page.copy();
		List<Map<String, Object>> ncelldataList = rnoLteCellManageService
				.queryLteNcellByPage(newPage, queryLteCellCond);
		log.debug("lte ncell data list:" + ncelldataList.size());
		String result1 = gson.toJson(ncelldataList);

		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String result = "{'page':" + pstr + ",'data':" + result1 + "}";
		log.info("退出queryLteNcellByPageForAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	
	/**
	 * 
	 * @title 删除指定的Lte邻区关系
	 * @author chao.xj
	 * @date 2015-6-30下午2:54:14
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void deleteLteNcellByPageForAjaxAction() {
		if (ids == null || ids.isEmpty()) {
			log.error("未指定需要删除的LTE邻区的id！");
			HttpTools.writeToClient("{'flag':false,'msg':'未指定需要删除的LTE邻区'}");
			return;
		}
		String[] arr = ids.split(",");
		if (arr == null || arr.length == 0) {
			log.error("未按正确格式指定待删除的LTE小区！");
			HttpTools.writeToClient("{'flag':false,'msg':'未按正确格式指定待删除的LTE邻区'}");
			return;
		}
		for (String ar : arr) {
			try {
				Long.parseLong(ar);
			} catch (Exception e) {
				log.error("指定待删除的LTE邻区的编号无效");
				HttpTools.writeToClient("{'flag':false,'msg':'指定待删除的LTE邻区的编号无效'}");
				return;
			}
		}
		
		boolean ok=rnoLteCellManageService.deleteRnoLteNcellByIds(ids);
		if(ok){
			HttpTools.writeToClient("{'flag':true}");
		}else{
			HttpTools.writeToClient("{'flag':false}");
		}
	}
}
