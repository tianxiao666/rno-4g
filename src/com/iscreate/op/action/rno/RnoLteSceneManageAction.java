package com.iscreate.op.action.rno;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import com.iscreate.op.action.rno.model.Eri2GNcsDescQueryCond;
import com.iscreate.op.action.rno.model.Line;
import com.iscreate.op.action.rno.model.LteCellUpdateResult;
import com.iscreate.op.action.rno.model.Point;
import com.iscreate.op.service.rno.RnoLteCellManageService;
import com.iscreate.op.service.rno.RnoLteSceneManageService;
import com.iscreate.op.service.rno.tool.HttpTools;

public class RnoLteSceneManageAction extends RnoCommonAction {

	private static Log log = LogFactory.getLog(RnoLteSceneManageAction.class);
	
	private RnoLteSceneManageService rnoLteSceneManageService;
	private long cityId;
	private long ctId;
	private Map<String,String> sceneParam;
	private InputStream exportInputStream;
    private String name;
    public InputStream getExportInputStream() {
		return exportInputStream;
	}

	public void setExportInputStream(InputStream exportInputStream) {
		this.exportInputStream = exportInputStream;
	}

	//	private String sceneType; //场景类型
	private String sceneId; //场景ID
	
	public String getSceneId() {
		return sceneId;
	}

	public void setSceneId(String sceneId) {
		this.sceneId = sceneId;
	}
	
/*	public String getSceneType() {
		return sceneType;
	}

	public void setSceneType(String sceneType) {
		this.sceneType = sceneType;
	}
*/
	public Map<String, String> getSceneParam() {
		return sceneParam;
	}

	public void setSceneParam(Map<String, String> sceneParam) {
		this.sceneParam = sceneParam;
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public long getCtId() {
		return ctId;
	}

	public void setCtId(long ctId) {
		this.ctId = ctId;
	}
	
	public RnoLteSceneManageService getRnoLteSceneManageService() {
		return rnoLteSceneManageService;
	}

	public void setRnoLteSceneManageService(
			RnoLteSceneManageService rnoLteSceneManageService) {
		this.rnoLteSceneManageService = rnoLteSceneManageService;
	}

	/**
	 * 
	 * @title 进入lte網絡管理页面
	 * @return
	 * @author chao.xj
	 * @date 2015-7-3下午6:47:37
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public String initLteGridManageAction() {
		initAreaList();
		return "success";
	}
	/**
	 * 
	 * @title 查询lte网格数据信息
	 * @author chao.xj
	 * @date 2015-7-5上午11:50:29
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void queryLteGridDataByPageAjaxAction(){

		cityId = Long.parseLong((String) attachParams.get("cityId"));
		log.debug("queryLteGridDataAjaxAction.page="+page+",cityId="+cityId);
		
		int cnt=page.getTotalCnt();
		if(cnt<0){
			cnt=(int)rnoLteSceneManageService.queryLteGridDataCntByCityId(cityId);
		}
		
		Page newPage = page.copy();
		newPage.setTotalCnt(cnt);
		
		List<Map<String,Object>> dataRecs = rnoLteSceneManageService.queryLteGridDataByPage(cityId, newPage);
		log.debug("grid size:" + dataRecs==null?0:dataRecs.size());
		String datas = gson.toJson(dataRecs);

		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String result = "{'page':" + pstr + ",'data':" + datas + "}";
		log.debug("退出queryLteGridDataAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 
	 * @title 通過城市ID查询lte网格数据信息
	 * @author chao.xj
	 * @date 2015-7-6上午10:50:29
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void queryLteGridDataByCityAjaxAction(){
		log.debug("進入queryLteGridDataByCityAjaxAction.cityId="+cityId);
		List<Map<String, Object>> grids= rnoLteSceneManageService.queryLteGridDataByCityId(cityId);
		String result=gson.toJson(grids);
		log.debug("退出queryLteGridDataByCityAjaxAction.result="+result);
		HttpTools.writeToClient(result);
	}
	
	/**
	 * 
	 * @title 通过城市ID导出lte网格数据信息
	 * @author li.tf
	 * @date 2015-9-14下午16:29:29
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public String exportLteGridDataByCityAjaxAction(){
		log.debug("进入exportLteGridDataByCityAjaxAction.cityId="+ctId);
		List<Map<String,Object>> gridsData= rnoLteSceneManageService.exportLteGridDataByCityId(ctId);
		List<Map<String, Object>> grids= rnoLteSceneManageService.queryLteGridDataByCityId(ctId);
		Date today=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String filePath = ServletActionContext.getServletContext().getRealPath("/op/rno/download/");
		String FileName = sdf.format(today) + "_网格小区数据.xlsx";
		/*String name = gridsData.get(1).get("CELL_NAME").toString().substring(0, 2) + "网格小区数据.xls";*/
		String dlFileName = ctId+"网格小区数据.xls";
		String fileRealPath = filePath+"/"+dlFileName;
		Boolean result = rnoLteSceneManageService.createExcelFile(gridsData,grids,fileRealPath);
		if(!result){
			return "fail";
		}
		try {
			setFileName(new String(FileName.getBytes(),"iso-8859-1"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return "fail";
		}
		File f = new File(fileRealPath);
		if (!f.exists()) {
			log.error("文件不存在");
			return "fail";
		} else {
			try {
				exportInputStream = new FileInputStream(f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return "fail";
			}
		}
		return "success";
	}
	
	/**
	 * 
	 * @title 进入lte網絡地圖展現页面
	 * @return
	 * @author chao.xj
	 * @date 2015-7-6下午11:13:37
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public String initLteGridMapDisplayAction() {
		initAreaList();
		return "success";
	}
	
	/**
	 * @Title 进入LTE地理场景管理页面
	 * @Author chen.c10
	 * @Date 2015-7-7 上午10:36:25
	 * @Company 怡创科技
	 * @Version V1.0
	 */
	public String initLteRegionSceneManagerAction(){
		initAreaList();
		return "success";
	}
	
	/**
	 * 获取场景名称列表
	 * @author chen.c10
	 * @date 2015-7-9 下午12:11:28
	 * @company 怡创科技
	 * @version V1.0
	 */
	public void getLteRegionSceneNameListForAjaxAction(){
		log.debug("进入getLteRegionSceneNameListForAjaxAction");
		String result="";
		List<Map<String, Object>> sceneNameList= rnoLteSceneManageService.queryLteRegionSceneNameList();
		result=gson.toJson(sceneNameList);
		log.debug("退出getLteRegionSceneNameListForAjaxAction.result="+result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 通过场景ID获取场景的信息
	 * @author chen.c10
	 * @date 2015-7-7 上午10:41:48
	 * @company 怡创科技
	 * @version V1.0
	 */
	public void getLteRegionSceneInfoListForAjaxAction(){
		log.debug("进入getLteRegionSceneInfoListForAjaxAction.sceneId="+sceneId);
		String result="";
		List<Map<String, Object>> scenes= rnoLteSceneManageService.queryLteRegionSceneInfoBySceneId(sceneId);
		Map<String, Object> scene=scenes.get(0);
		result=gson.toJson(scene);
		System.out.println("result="+result);
		log.debug("退出getLteRegionSceneInfoListForAjaxAction.result="+result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 更新地理场景信息
	 * 
	 * @author chen.c10
	 * @date 2015-7-8 下午7:10:34
	 * @company 怡创科技
	 * @version V1.0
	 */
	public void updateLteRegionSceneDataForAjaxAction(){
		log.debug("进入updateLteRegionSceneDataForAjaxAction");
		String result="";
		String sucstr="";
		boolean flag= rnoLteSceneManageService.updateLteRegionSceneDataBySceneId(sceneParam);
		if(flag){
			sucstr="success";
		}else{
			sucstr="failure";
		}
		result =sucstr;
		log.debug("退出updateLteRegionSceneDataForAjaxAction.result="+result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 删除场景信息
	 * 
	 * @author chen.c10
	 * @date 2015-7-10 上午11:20:33
	 * @company 怡创科技
	 * @version V1.0
	 */
	public void deleteLteRegionSceneDataForAjaxAction(){
		log.debug("进入deleteLteRegionSceneDataForAjaxAction.sceneId="+sceneId);
		String result="";
		String sucstr="";
		boolean flag= rnoLteSceneManageService.deleteLteRegionSceneData(sceneId);
		if(flag){
			sucstr="success";
		}else{
			sucstr="failure";
		}
		result =sucstr;
		log.debug("退出deleteLteRegionSceneDataForAjaxAction.result="+result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 插入场景管理数据
	 * 
	 * @author chen.c10
	 * @date 2015-7-10 下午1:43:16
	 * @company 怡创科技
	 * @version V1.0
	 */
	public void insertLteRegionSceneDataForAjaxAction(){
		log.debug("进入insertLteRegionSceneDataForAjaxAction");
		String result="";
		String sucstr="";
		boolean flag= rnoLteSceneManageService.insertLteRegionSceneData(sceneParam);
		if(flag){
			sucstr="success";
		}else{
			sucstr="failure";
		}
		result =sucstr;
		log.debug("退出insertLteRegionSceneDataForAjaxAction.result="+result);
		HttpTools.writeToClient(result);
	}
	
	/**
	 * 进入LTE时间场景管理页面
	 * @return
	 * @author chen.c10
	 * @date 2015-7-13 上午11:38:40
	 * @company 怡创科技
	 * @version V1.0
	 */
	public String initLteTimeSceneManagerAction(){
		initAreaList();
		return "success";
	}
	
	/**
	 * 获取场景名称列表
	 * @author chen.c10
	 * @date 2015-7-9 下午12:11:28
	 * @company 怡创科技
	 * @version V1.0
	 */
	public void getLteTimeSceneNameListForAjaxAction(){
		log.debug("进入getLteTimeSceneNameListForAjaxAction");
		String result="";
		List<Map<String, Object>> sceneNameList= rnoLteSceneManageService.queryLteTimeSceneNameList();
		result=gson.toJson(sceneNameList);
		log.debug("退出getLteTimeSceneNameListForAjaxAction.result="+result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 通过场景ID获取时间场景的信息
	 * @author chen.c10
	 * @date 2015-7-7 上午10:41:48
	 * @company 怡创科技
	 * @version V1.0
	 */
	public void getLteTimeSceneInfoListForAjaxAction(){
		log.debug("进入getLteTimeSceneInfoListForAjaxAction.sceneId="+sceneId);
		String result="";
		List<Map<String, Object>> scenes= rnoLteSceneManageService.queryLteTimeSceneInfoBySceneId(sceneId);
		Map<String, Object> scene=scenes.get(0);
		result=gson.toJson(scene);
		System.out.println("result="+result);
		log.debug("退出getLteTimeSceneInfoListForAjaxAction.result="+result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 更新时间场景信息
	 * 
	 * @author chen.c10
	 * @date 2015-7-8 下午7:10:34
	 * @company 怡创科技
	 * @version V1.0
	 */
	public void updateLteTimeSceneDataForAjaxAction(){
		log.debug("进入updateLteTimeSceneDataForAjaxAction");
		String result="";
		String sucstr="";
		boolean flag= rnoLteSceneManageService.updateLteTimeSceneDataBySceneId(sceneParam);
		if(flag){
			sucstr="success";
		}else{
			sucstr="failure";
		}
		result =sucstr;
		log.debug("退出updateLteTimeSceneDataForAjaxAction.result="+result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 删除时间场景信息
	 * 
	 * @author chen.c10
	 * @date 2015-7-10 上午11:20:33
	 * @company 怡创科技
	 * @version V1.0
	 */
	public void deleteLteTimeSceneDataForAjaxAction(){
		log.debug("进入deleteLteTimeSceneDataForAjaxAction.sceneId="+sceneId);
		String result="";
		String sucstr="";
		boolean flag= rnoLteSceneManageService.deleteLteTimeSceneData(sceneId);
		if(flag){
			sucstr="success";
		}else{
			sucstr="failure";
		}
		result =sucstr;
		log.debug("退出deleteLteTimeSceneDataForAjaxAction.result="+result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 插入时间场景管理数据
	 * 
	 * @author chen.c10
	 * @date 2015-7-10 下午1:43:16
	 * @company 怡创科技
	 * @version V1.0
	 */
	public void insertLteTimeSceneDataForAjaxAction(){
		log.debug("进入insertLteTimeSceneDataForAjaxAction");
		String result="";
		String sucstr="";
		boolean flag= rnoLteSceneManageService.insertLteTimeSceneData(sceneParam);
		if(flag){
			sucstr="success";
		}else{
			sucstr="failure";
		}
		result =sucstr;
		log.debug("退出insertLteTimeSceneDataForAjaxAction.result="+result);
		HttpTools.writeToClient(result);
	}
	
	
	
	public static void main(String[] args) {
		RnoLteSceneManageAction rs = new RnoLteSceneManageAction();
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("BAND_TYPE", "bb");
		res.add(map);
		/*rs.createExcelFile(res);*/

	}
}
