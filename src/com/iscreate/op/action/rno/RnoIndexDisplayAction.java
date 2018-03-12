package com.iscreate.op.action.rno;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.action.rno.model.Eri2GFasQueryCond;
import com.iscreate.op.action.rno.model.Eri2GMrrQueryCond;
import com.iscreate.op.action.rno.model.LteKpiQueryCond;
import com.iscreate.op.action.rno.model.LteStsIndexQueryCond;
import com.iscreate.op.service.rno.RnoIndexDisplayService;
import com.iscreate.op.service.rno.tool.HttpTools;

public class RnoIndexDisplayAction extends RnoCommonAction {
	private static Log log = LogFactory.getLog(RnoIndexDisplayAction.class);
	private static Gson gson = new GsonBuilder().create();// 线程安全
	//注入
	private RnoIndexDisplayService rnoIndexDisplayService;
	
	//--爱立信 2G MRR cell信息指标查询
	private Eri2GMrrQueryCond eri2gMrrQueryCond;
	//--爱立信 2G FAS cell信息指标查询
	private Eri2GFasQueryCond eri2gFasQueryCond;
	//--LTE KPI 信息指标查询
	private LteKpiQueryCond lteKpiQueryCond;		
	//--LTE STS 信息指标查询条件
	private LteStsIndexQueryCond lteStsIndexQueryCond;
	
	private InputStream exportInputStream;
	
	private String fileName;
	
	public LteStsIndexQueryCond getLteStsIndexQueryCond() {
		return lteStsIndexQueryCond;
	}

	public void setLteStsIndexQueryCond(LteStsIndexQueryCond lteStsIndexQueryCond) {
		this.lteStsIndexQueryCond = lteStsIndexQueryCond;
	}

	public LteKpiQueryCond getLteKpiQueryCond() {
		return lteKpiQueryCond;
	}

	public void setLteKpiQueryCond(LteKpiQueryCond lteKpiQueryCond) {
		this.lteKpiQueryCond = lteKpiQueryCond;
	}

	public Eri2GFasQueryCond getEri2gFasQueryCond() {
		return eri2gFasQueryCond;
	}

	public void setEri2gFasQueryCond(Eri2GFasQueryCond eri2gFasQueryCond) {
		this.eri2gFasQueryCond = eri2gFasQueryCond;
	}

	public Eri2GMrrQueryCond getEri2gMrrQueryCond() {
		return eri2gMrrQueryCond;
	}

	public void setEri2gMrrQueryCond(Eri2GMrrQueryCond eri2gMrrQueryCond) {
		this.eri2gMrrQueryCond = eri2gMrrQueryCond;
	}
	public RnoIndexDisplayService getRnoIndexDisplayService() {
		return rnoIndexDisplayService;
	}

	public void setRnoIndexDisplayService(
			RnoIndexDisplayService rnoIndexDisplayService) {
		this.rnoIndexDisplayService = rnoIndexDisplayService;
	}
	
	public InputStream getExportInputStream() {
		return exportInputStream;
	}
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setExportInputStream(InputStream exportInputStream) {
		this.exportInputStream = exportInputStream;
	}

	/**
	 * 
	 * @title 初始化MRR指标图表展现小区查看页面
	 * @return
	 * @author chao.xj
	 * @date 2014-11-13上午11:46:14
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String initEri2GMrrIndexDisplayCellQueryPageAction() {
		initAreaList();//  加载区域相关信息
		return "success";
	}
	/**
	 * 
	 * @title 搜索爱立信2G小区MRR指标
	 * @author chao.xj
	 * @date 2014-11-13下午2:51:19
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void searchEri2GMrrCellIndexForAjaxAction() {
		
		eri2gMrrQueryCond=new Eri2GMrrQueryCond();
		eri2gMrrQueryCond.setCell((String)attachParams.get("cell"));
		eri2gMrrQueryCond.setCityId(Long.parseLong((String) attachParams.get("cityId")));
		eri2gMrrQueryCond.setMeaBegTime((String)attachParams.get("meaBegTime"));
		eri2gMrrQueryCond.setMeaEndTime((String)attachParams.get("meaEndTime"));
		eri2gMrrQueryCond.setDataType((String)attachParams.get("dataType"));
		eri2gMrrQueryCond.setChgr((String)attachParams.get("chgr"));
		
		log.debug("searchEri2GMrrCellIndexForAjaxAction.attmap="+attachParams+",queryCond="+eri2gMrrQueryCond);
		
		List<Map<String, Object>> obj=rnoIndexDisplayService.queryEri2GCellMrrIndex(eri2gMrrQueryCond);
		String datas = gson.toJson(obj);
		String result = "{'data':" + datas + "}";
		log.debug("退出searchEri2GMrrCellIndexForAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 
	 * @title 初始化FAS指标图表展现小区查看页面
	 * @return
	 * @author chao.xj
	 * @date 2015-2-2上午9:40:58
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public String initEri2GFasIndexDisplayCellQueryPageAction() {
		initAreaList();//  加载区域相关信息
		return "success";
	}
	/**
	 * 
	 * @title 搜索爱立信2G小区Fas指标
	 * @author chao.xj
	 * @date 2015-2-2上午10:10:15
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void searchEri2GFasCellIndexForAjaxAction() {
		
		eri2gFasQueryCond=new Eri2GFasQueryCond();
		eri2gFasQueryCond.setCell((String)attachParams.get("cell"));
		eri2gFasQueryCond.setCityId(Long.parseLong((String) attachParams.get("cityId")));
		eri2gFasQueryCond.setMeaBegTime((String)attachParams.get("meaBegTime"));
		eri2gFasQueryCond.setMeaEndTime((String)attachParams.get("meaEndTime"));
		
		
		log.debug("searchEri2GFasCellIndexForAjaxAction.attmap="+attachParams+",queryCond="+eri2gFasQueryCond);
		
		List<Map<String, Object>> obj=rnoIndexDisplayService.queryEri2GCellFasIndex(eri2gFasQueryCond);
		String datas = gson.toJson(obj);
		String result = "{'data':" + datas + "}";
		log.debug("退出searchEri2GFasCellIndexForAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 
	 * @title 初始化LTE KPI 指标图表展现小区查看页面
	 * @return
	 * @author chao.xj
	 * @date 2015-9-14下午1:52:19
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public String initLteKpiIndexDisplayCellQueryPageAction() {
		initAreaList();//  加载区域相关信息
		return "success";
	}
	/**
	 * 
	 * @title 搜索LTE小区 KPI 指标
	 * @author chao.xj
	 * @date 2015-9-14下午1:54:17
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void searchLteKpiCellIndexForAjaxAction() {
		
		lteKpiQueryCond=new LteKpiQueryCond();
		lteKpiQueryCond.setCell((String)attachParams.get("cell"));
		lteKpiQueryCond.setCityId(Long.parseLong((String) attachParams.get("cityId")));
		lteKpiQueryCond.setMeaBegTime((String)attachParams.get("meaBegTime"));
		lteKpiQueryCond.setMeaEndTime((String)attachParams.get("meaEndTime"));
		lteKpiQueryCond.setDataType((String)attachParams.get("dataType"));
		
		log.debug("searchLteKpiCellIndexForAjaxAction.attmap="+attachParams+",queryCond="+eri2gMrrQueryCond);
		
		List<Map<String, String>> obj=rnoIndexDisplayService.queryLteKpiIndexFromHBase(lteKpiQueryCond);
		String datas = gson.toJson(obj);
		String result = "{'data':" + datas + "}";
		log.debug("退出searchLteKpiCellIndexForAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 初始化4G话统指标展示页面
	 * @return
	 * @author chen.c10
	 * @date 2015-9-16 下午2:56:40
	 * @company 怡创科技
	 * @version V1.0
	 */
	public String initLteStsIndexDisplayQueryPageAction() {
		initAreaList();
		return "success";
	}
	public void searchLteStsCellIndexForAjaxAction() {
		log.debug("进入searchLteStsCellIndexForAjaxAction。" );		
		
		lteStsIndexQueryCond=new LteStsIndexQueryCond();
		lteStsIndexQueryCond.setCityId(Long.parseLong((String) attachParams.get("cityId")));
		lteStsIndexQueryCond.setCellNameList((String) attachParams.get("cellNameStr"));
		lteStsIndexQueryCond.setColumnList((String) attachParams.get("indexColumnStr"));
		lteStsIndexQueryCond.setMeaBegTime((String) attachParams.get("begTime"));
		lteStsIndexQueryCond.setMeaEndTime((String) attachParams.get("endTime"));
		
		int cnt=page.getTotalCnt();
		if(cnt<0){
			cnt=(int)rnoIndexDisplayService.queryLteStsIndexCnt(lteStsIndexQueryCond);
		}
		Page newPage = page.copy();
		newPage.setTotalCnt(cnt);
		
		log.debug("searchLteStsCellIndexForAjaxAction.page="+page+",attmap="+attachParams+",queryCond="+lteStsIndexQueryCond);
		
		//List<Map<String, Object>> obj=rnoIndexDisplayService.queryLteStsIndex(lteStsIndexQueryCond);
		List<Map<String, Object>> obj=rnoIndexDisplayService.queryLteStsIndexByPage(lteStsIndexQueryCond,newPage);
		log.debug("size:" + (obj==null?0:obj.size()));
		String datas = gson.toJson(obj);
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);
		String pstr = gson.toJson(newPage);
		String result = "{'page':" + pstr + ",'data':" + datas + "}";
/*		List<Map<String, Object>> obj=rnoIndexDisplayService.queryLteStsIndex(lteStsIndexQueryCond);
		log.debug("size:" + (obj==null?0:obj.size()));
		String datas = gson.toJson(obj);
		String result = "{'data':" + datas + "}";*/
		log.debug("退出searchLteStsCellIndexForAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	public String downloadLteStsCellIndexFileAction(){
		log.debug("进入方法：downloadLteStsCellIndexFileAction。");

		lteStsIndexQueryCond=new LteStsIndexQueryCond();
		lteStsIndexQueryCond.setCityId(Long.parseLong((String) attachParams.get("cityIdForDownload")));
		lteStsIndexQueryCond.setCellNameList((String) attachParams.get("cellNameStrForDownload"));
		lteStsIndexQueryCond.setColumnList((String) attachParams.get("indexStrForDownload"));
		lteStsIndexQueryCond.setColumnNameList((String) attachParams.get("indexNameStrForDownload"));
		lteStsIndexQueryCond.setMeaBegTime((String) attachParams.get("begTimeForDownload"));
		lteStsIndexQueryCond.setMeaEndTime((String) attachParams.get("endTimeForDownload"));
		
		log.debug("downloadLteStsCellIndexFileAction.attmap="+attachParams+",queryCond="+lteStsIndexQueryCond);
		
		String dlFileRealPath = rnoIndexDisplayService.downloadLteStsCellIndexFile(lteStsIndexQueryCond);
		if("error".equals(dlFileRealPath)){
			return "fail";
		}
		String dlFileName = dlFileRealPath.substring(dlFileRealPath.lastIndexOf("/")+1);
		log.info(dlFileName);
		try {
			setFileName(new String(dlFileName.getBytes(),"iso-8859-1"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return "fail";
		}
		File file = new File(dlFileRealPath);
		if (!file.exists()) {
			log.error("文件不存在");
			return "fail";
		} else {
			try {
				exportInputStream = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return "fail";
			}
		}
		log.debug("退出方法：downloadLteStsCellIndexFileAction。");
		return "success";
		/*String result=gson.toJson(grids);
		log.debug("退出exportLteGridDataByCityAjaxAction.result="+result);
		System.out.println(result);
		HttpTools.writeToClient(result);*/
		
	}
}
