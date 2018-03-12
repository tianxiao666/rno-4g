package com.iscreate.op.action.rno;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.action.rno.model.Eri2GNcsDescQueryCond;
import com.iscreate.op.action.rno.model.G4DtDescQueryCond;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.pojo.rno.AreaRectangle;
import com.iscreate.op.pojo.rno.RnoMapLnglatRelaGps;
import com.iscreate.op.service.rno.RnoLteDtAnaService;
import com.iscreate.op.service.rno.tool.HttpTools;

public class RnoLteDtAnaAction extends RnoCommonAction {

	private static Log log = LogFactory.getLog(RnoLteDtAnaAction.class);
	
	private RnoLteDtAnaService rnoLteDtAnaService;

	private long cityId;
	
	/* 数据查询条件 */
	private G4DtDescQueryCond g4DtDescQueryCond;
	
	private String descIdStr;//描述ID字符串
	


	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}
	public RnoLteDtAnaService getRnoLteDtAnaService() {
		return rnoLteDtAnaService;
	}

	public void setRnoLteDtAnaService(RnoLteDtAnaService rnoLteDtAnaService) {
		this.rnoLteDtAnaService = rnoLteDtAnaService;
	}
	public String getDescIdStr() {
		return descIdStr;
	}

	public void setDescIdStr(String descIdStr) {
		this.descIdStr = descIdStr;
	}

	/**
	 * 
	 * @title 进入lte路测数据管理页面 
	 * @return
	 * @author chao.xj
	 * @date 2015-7-7下午1:47:12
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public String initLteDtAnaManageAction() {
		initAreaList();
		return "success";
	}
	/**
	 * 
	 * @title 分頁查询4g 路测的描述信息
	 * @author chao.xj
	 * @date 2015-7-8下午8:28:09
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void queryG4DtDescAjaxAction(){

		g4DtDescQueryCond=new G4DtDescQueryCond();
		g4DtDescQueryCond.setDataType((String)attachParams.get("dataType"));
		g4DtDescQueryCond.setCityId(Long.parseLong((String) attachParams.get("cityId")));
		g4DtDescQueryCond.setMeaBegTime((String) attachParams.get("dtMeaBegDate"));
		g4DtDescQueryCond.setMeaEndTime((String) attachParams.get("dtMeaEndDate"));
		log.debug("queryG4DtDescAjaxAction.page="+page+",attmap="+attachParams+",queryCond="+g4DtDescQueryCond);
		
		int cnt=page.getTotalCnt();
		if(cnt<0){
			cnt=(int)rnoLteDtAnaService.queryG4DtDescCnt(g4DtDescQueryCond);
		}
		
		Page newPage = page.copy();
		newPage.setTotalCnt(cnt);
		
		List<Map<String,Object>> dataRecs = rnoLteDtAnaService.queryG4DtDescByPage(g4DtDescQueryCond, newPage);
		log.debug("size:" + dataRecs==null?0:dataRecs.size());
		String datas = gson.toJson(dataRecs);

		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String result = "{'page':" + pstr + ",'data':" + datas + "}";
		log.debug("退出queryG4DtDescAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 
	 * @title 进入lte路测分析地图页面 
	 * @return
	 * @author chao.xj
	 * @date 2015-7-9上午10:20:39
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public String initLteDtAnaMapAction() {
		initAreaList();
		return "success";
	}
	/**
	 * 
	 * @title 通过条件查询某一天的DT描述信息返回分析页面
	 * @author chao.xj
	 * @date 2015-7-8下午8:28:09
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void queryG4DtDescForMapAjaxAction(){

		g4DtDescQueryCond = new G4DtDescQueryCond();
		g4DtDescQueryCond.setDataType((String) attachParams.get("dataType"));
		g4DtDescQueryCond.setAreaType((String) attachParams.get("areaType"));
		g4DtDescQueryCond.setCityId(Long.parseLong((String) attachParams
				.get("cityId")));
		g4DtDescQueryCond.setMeaBegTime((String) attachParams
				.get("dtMeaBegDate"));
		log.debug("queryG4DtDescForMapAjaxAction.attmap=" + attachParams
				+ ",queryCond=" + g4DtDescQueryCond);
		List<Map<String, Object>> dataRecs = rnoLteDtAnaService
				.queryG4DtDescInfoForMap(g4DtDescQueryCond);
		log.debug("size:" + dataRecs == null ? 0 : dataRecs.size());
		String result = gson.toJson(dataRecs);
		log.debug("退出queryG4DtDescForMapAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 
	 * @title 通过条件查询某一天的DT詳情信息返回分析页面
	 * @author chao.xj
	 * @date 2015-7-8下午8:28:09
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void queryG4DtDetailInfoForMapAjaxAction(){

		log.debug("queryG4DtDetailInfoForMapAjaxAction.descIdStr=" + descIdStr);
		List<Map<String, Object>> dataRecs = rnoLteDtAnaService
				.queryG4DtDetailInfoForMap(descIdStr);
		log.debug("size:" + dataRecs == null ? 0 : dataRecs.size());
		String result = gson.toJson(dataRecs);
		log.debug("退出queryG4DtDetailInfoForMapAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 
	 * @title 分頁查询4g 路测的詳情信息
	 * @author chao.xj
	 * @date 2015-7-8下午8:28:09
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void queryG4DtDetailDataInDescIdByPageAjaxAction(){

		
		log.debug("queryG4DtDetailDataInDescIdByPageAjaxAction.page="+page+",descIdStr="+descIdStr);
		
		int cnt=page.getTotalCnt();
		if(cnt<0){
			cnt=(int)rnoLteDtAnaService.queryG4DtDetailDataCnt(descIdStr);
		}
		// 基准点
		Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints = null;
		standardPoints = rnoCommonService
				.getSpecialAreaRnoMapLnglatRelaGpsMapList(cityId,
						RnoConstant.DBConstant.MAPTYPE_BAIDU);
		
		Page newPage = page.copy();
		newPage.setTotalCnt(cnt);
		
		List<Map<String,Object>> dataRecs = rnoLteDtAnaService.queryG4DtDetailDataByPage(descIdStr, newPage,standardPoints);
		log.debug("size:" + dataRecs==null?0:dataRecs.size());
		String datas = gson.toJson(dataRecs);

		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		boolean hasMore=false;
		
		if (cnt % page.getPageSize() == 0) {
			newPage.setTotalPageCnt(cnt / page.getPageSize());//总页码数
		} else {
			newPage.setTotalPageCnt(cnt / page.getPageSize() + 1);
		}
//		System.out.println("getTotalPageCnt="+newPage.getTotalPageCnt());
		if (newPage.getCurrentPage()!=newPage.getTotalPageCnt()) {
			hasMore=true;
		}
		String pstr = gson.toJson(newPage);
		String result = "{'page':" + pstr + ",'data':" + datas + ",'hasMore':" + hasMore + "}";
		log.debug("退出queryG4DtDetailDataInDescIdByPageAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
}
