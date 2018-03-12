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
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.action.rno.model.Eri2GCellDescQueryCond;
import com.iscreate.op.action.rno.model.Eri2GCellQueryCond;
import com.iscreate.op.action.rno.model.Eri2GNcellQueryCond;
import com.iscreate.op.action.rno.model.Hw2GCellDescQueryCond;
import com.iscreate.op.action.rno.model.Hw2GCellQueryCond;
import com.iscreate.op.action.rno.model.Hw2GNcellQueryCond;
import com.iscreate.op.service.rno.Rno2GHwCellManageService;
import com.iscreate.op.service.rno.RnoResourceManagerService;
import com.iscreate.op.service.rno.parser.jobrunnable.Eri2GCellParserJobRunnable.DBFieldToLogTitle;
import com.iscreate.op.service.rno.parser.jobrunnable.HwNcsParserJobRunnable.DBFieldToTitle;
import com.iscreate.op.service.rno.tool.HttpTools;

public class Rno2GHwCellManageAction extends RnoCommonAction {

	private static Log log = LogFactory.getLog(Rno2GHwCellManageAction.class);
	private static Gson gson = new GsonBuilder().create();// 线程安全
	// -------注入-------------//
	private RnoResourceManagerService rnoResourceManagerService;
	private Rno2GHwCellManageService rno2gHwCellManageService;
	
	private List<String> coverareas;//覆盖区域
	private List<String> covertypes;//覆盖类型
	private List<String> importancegrade;//重要等级
	
	private long cityId;
	private String serviceType;
	//----------资源查询的条件--------------//
	//--华为2g cell 描述信息
	private Hw2GCellDescQueryCond hw2gCellDescQueryCond;
	//--华为 2G cell信息查询
	private Hw2GCellQueryCond hw2gCellQueryCond;
	private Hw2GNcellQueryCond hw2gNcellQueryCond;
	//导出结果文件
	private String token;
	private InputStream exportInputStream;// 导出分析文件的输入流
	private String fileName;// 下载文件名
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public InputStream getExportInputStream() {
		return exportInputStream;
	}
	public void setExportInputStream(InputStream exportInputStream) {
		this.exportInputStream = exportInputStream;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	//多少月
	private int monthNum;
	
	public int getMonthNum() {
		return monthNum;
	}
	public void setMonthNum(int monthNum) {
		this.monthNum = monthNum;
	}
	public Hw2GNcellQueryCond getHw2gNcellQueryCond() {
		return hw2gNcellQueryCond;
	}
	public void setHw2gNcellQueryCond(Hw2GNcellQueryCond hw2gNcellQueryCond) {
		this.hw2gNcellQueryCond = hw2gNcellQueryCond;
	}
	public RnoResourceManagerService getRnoResourceManagerService() {
		return rnoResourceManagerService;
	}
	public void setRnoResourceManagerService(
			RnoResourceManagerService rnoResourceManagerService) {
		this.rnoResourceManagerService = rnoResourceManagerService;
	}
	public Rno2GHwCellManageService getRno2gHwCellManageService() {
		return rno2gHwCellManageService;
	}
	public void setRno2gHwCellManageService(
			Rno2GHwCellManageService rno2gHwCellManageService) {
		this.rno2gHwCellManageService = rno2gHwCellManageService;
	}
	public List<String> getCoverareas() {
		return coverareas;
	}
	public void setCoverareas(List<String> coverareas) {
		this.coverareas = coverareas;
	}
	public List<String> getCovertypes() {
		return covertypes;
	}
	public void setCovertypes(List<String> covertypes) {
		this.covertypes = covertypes;
	}
	public List<String> getImportancegrade() {
		return importancegrade;
	}
	public void setImportancegrade(List<String> importancegrade) {
		this.importancegrade = importancegrade;
	}
	public long getCityId() {
		return cityId;
	}
	public void setCityId(long cityId) {
		this.cityId = cityId;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public Hw2GCellDescQueryCond getHw2gCellDescQueryCond() {
		return hw2gCellDescQueryCond;
	}
	public void setHw2gCellDescQueryCond(Hw2GCellDescQueryCond hw2gCellDescQueryCond) {
		this.hw2gCellDescQueryCond = hw2gCellDescQueryCond;
	}
	public Hw2GCellQueryCond getHw2gCellQueryCond() {
		return hw2gCellQueryCond;
	}
	public void setHw2gCellQueryCond(Hw2GCellQueryCond hw2gCellQueryCond) {
		this.hw2gCellQueryCond = hw2gCellQueryCond;
	}
	
	/**
	 * 
	 * @title 初始化hw ２Ｇ小区管理页面
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午9:56:40
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String init2GHwCellManagerPageAction() {
		initAreaList();
		Map<String,List<String>> mid=rnoResourceManagerService.getAllCoverareaCovertypeAndImportances();
		coverareas=mid.get("coverarea");
		covertypes=mid.get("covertype");
		importancegrade=mid.get("importancegrade");
		return "success";
	}
	/**
	 * 
	 * @title 华为2G小区查询描述信息的方法
	 * @author chao.xj
	 * @date 2014-10-27上午9:58:08
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void queryHwCellDescAjaxAction(){

		hw2gCellDescQueryCond=new Hw2GCellDescQueryCond();
		hw2gCellDescQueryCond.setDataType((String)attachParams.get("dataType"));
		hw2gCellDescQueryCond.setCityId(Long.parseLong((String) attachParams.get("cityId")));
		hw2gCellDescQueryCond.setMeaBegTime((String) attachParams.get("cellMeaBegDate"));
		hw2gCellDescQueryCond.setMeaEndTime((String) attachParams.get("cellMeaEndDate"));
		log.debug("queryEriCellDescAjaxAction.page="+page+",attmap="+attachParams+",queryCond="+hw2gCellDescQueryCond);
		
		int cnt=page.getTotalCnt();
		if(cnt<0){
			cnt=(int)rno2gHwCellManageService.queryHwCellDescCnt(hw2gCellDescQueryCond);
		}
		
		Page newPage = page.copy();
		newPage.setTotalCnt(cnt);
		
		List<Map<String,Object>> dataRecs = rno2gHwCellManageService.queryHwCellDescByPage(hw2gCellDescQueryCond, newPage);
		log.debug("HwcellDesc size:" + dataRecs==null?0:dataRecs.size());
		String datas = gson.toJson(dataRecs);

		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String result = "{'page':" + pstr + ",'data':" + datas + "}";
		log.debug("退出queryHwCellDescAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}	
	/**
	 * 
	 * @title 初始化华为２Ｇ小区 查询 页面
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午10:45:30
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String init2GHwCellQueryPageAction() {
		initAreaList();
//		cityId=getCityAreas().get(0).getArea_id();
		Map<String,List<String>> mid=rnoResourceManagerService.getAllCoverareaCovertypeAndImportances();
		coverareas=mid.get("coverarea");
		covertypes=mid.get("covertype");
		importancegrade=mid.get("importancegrade");
		return "success";
	}
	/**
	 * 
	 * @title 获取2G华为小区查询页面所有列表填充信息
	 * @author chao.xj
	 * @date 2014-10-27上午10:47:15
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void get2GHwCellQueryPageAllListInfoAction() {
		log.debug("进入get2GHwCellQueryPageAllListInfoAction");
		Map<String, Map<String,DBFieldToTitle>> cellNcellParams=rno2gHwCellManageService.getHw2GCellAndNcellParams();
		List<Map<String, Object>> bscList=rno2gHwCellManageService.queryBscListByCityId(cityId);
		List<Map<String, Object>> dateList=rno2gHwCellManageService.queryLatelyOneMonthOfHw2GCellDateInfo(cityId);
		String cellChannelNcellParamstr = gson.toJson(cellNcellParams);
		String datestr = gson.toJson(dateList);
		String bscstr = gson.toJson(bscList);
		String result = "{'paramInfo':" + cellChannelNcellParamstr + ",'dateInfo':" + datestr + ",'bscInfo':" + bscstr + "}";
		log.debug("退出get2GHwCellQueryPageAllListInfoAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 
	 * @title 获取2G华为小区查询页面日期及BSC列表填充信息
	 * @author chao.xj
	 * @date 2014-10-27上午10:48:11
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void get2GHwCellQueryPageDateAndBscListInfoAction() {
		log.debug("进入get2GHwCellQueryPageDateAndBscListInfoAction");
		List<Map<String, Object>> bscList=rno2gHwCellManageService.queryBscListByCityId(cityId);
		List<Map<String, Object>> dateList=rno2gHwCellManageService.queryLatelyOneMonthOfHw2GCellDateInfo(cityId);
		String datestr = gson.toJson(dateList);
		String bscstr = gson.toJson(bscList);
		String result = "{'dateInfo':" + datestr + ",'bscInfo':" + bscstr + "}";
		log.debug("退出get2GHwCellQueryPageDateAndBscListInfoAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 
	 * @title 华为2G小区分页查询信息的方法
	 * @author chao.xj
	 * @date 2014-10-27上午10:48:49
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void queryHw2GCellByPageAjaxAction(){

		hw2gCellQueryCond=new Hw2GCellQueryCond();
		hw2gCellQueryCond.setBsc((String)attachParams.get("cellBsc"));
		hw2gCellQueryCond.setCell((String)attachParams.get("cellForCell"));
		hw2gCellQueryCond.setCityId(Long.parseLong((String) attachParams.get("cityId")));
		hw2gCellQueryCond.setMeaDate((String)attachParams.get("cellDate"));
		hw2gCellQueryCond.setParam((String)attachParams.get("cellParam"));
		
		log.debug("queryHw2GCellByPageAjaxAction.page="+page+",attmap="+attachParams+",queryCond="+hw2gCellQueryCond);
		
		int cnt=page.getTotalCnt();
		if(cnt<0){
			cnt=(int)rno2gHwCellManageService.queryHw2GCellCnt(hw2gCellQueryCond);
		}
		
		Page newPage = page.copy();
		newPage.setTotalCnt(cnt);
		
		List<Map<String,Object>> dataRecs = rno2gHwCellManageService.queryHw2GCellByPage(hw2gCellQueryCond, newPage);
		log.debug("hw2gCellQueryCond size:" + dataRecs==null?0:dataRecs.size());
		String datas = gson.toJson(dataRecs);

		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String result = "{'page':" + pstr + ",'data':" + datas + "}";
		log.debug("退出queryHw2GCellByPageAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 
	 * @title 华为2G邻区分页查询信息的方法
	 * @author chao.xj
	 * @date 2014-10-27上午10:52:35
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void queryHw2GNcellByPageAjaxAction(){

		hw2gNcellQueryCond=new Hw2GNcellQueryCond();
		hw2gNcellQueryCond.setBsc((String)attachParams.get("ncellBsc"));
		hw2gNcellQueryCond.setCell((String)attachParams.get("ncellForCell"));
		hw2gNcellQueryCond.setCityId(Long.parseLong((String) attachParams.get("cityId")));
		hw2gNcellQueryCond.setMeaDate((String)attachParams.get("ncellDate"));
		hw2gNcellQueryCond.setParam((String)attachParams.get("ncellParam"));
		hw2gNcellQueryCond.setNcell((String)attachParams.get("ncellForNcell"));
		log.debug("queryHw2GNcellByPageAjaxAction.page="+page+",attmap="+attachParams+",queryCond="+hw2gNcellQueryCond);
		
		int cnt=page.getTotalCnt();
		if(cnt<0){
			cnt=(int)rno2gHwCellManageService.queryHw2GNcellCnt(hw2gNcellQueryCond);
		}
		
		Page newPage = page.copy();
		newPage.setTotalCnt(cnt);
		
		List<Map<String,Object>> dataRecs = rno2gHwCellManageService.queryHw2GNcellByPage(hw2gNcellQueryCond, newPage);
		log.debug("hw2gNcellQueryCond size:" + dataRecs==null?0:dataRecs.size());
		String datas = gson.toJson(dataRecs);

		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String result = "{'page':" + pstr + ",'data':" + datas + "}";
		log.debug("退出queryHw2GNcellByPageAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 
	 * @title 导出华为小区数据结果到文件
	 * @author chao.xj
	 * @date 2014-11-8下午3:57:52
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void exportHw2GCellDataAjaxForAction() {
		
		hw2gCellQueryCond=new Hw2GCellQueryCond();
		hw2gCellQueryCond.setBsc((String)attachParams.get("cellBsc"));
		hw2gCellQueryCond.setCell((String)attachParams.get("cellForCell"));
		hw2gCellQueryCond.setCityId(Long.parseLong((String) attachParams.get("cityId")));
		hw2gCellQueryCond.setMeaDate((String)attachParams.get("cellDate"));
		hw2gCellQueryCond.setParam((String)attachParams.get("cellParam"));
		log.info("进入方法exportHw2GCellDataAjaxForAction。hw2gCellQueryCond=" + hw2gCellQueryCond );
		String token = null;
		String msg = "";
		//判断是否存在小区数据
		long cnt=rno2gHwCellManageService.queryHw2GCellCnt(hw2gCellQueryCond);
		if(cnt>0) {
			//获取根目录全路径
			String path = ServletActionContext.getServletContext().getRealPath(
				"/op/rno/");
			//获取对比差异
			token = rno2gHwCellManageService.exportHw2GCellData(path, hw2gCellQueryCond,cnt);
			if(token == null) {
				msg = "创建任务失败";
			}else{
				msg="创建任务成功";
			}
		} else {
			log.debug( hw2gCellQueryCond + " 该条件下不存在小区数据，无法导出数据");
			msg =  " 该条件下不存在小区数据，无法导出数据";
		}
		String resultStr = "{'token':'"+token+"','msg':'"+msg+"'}";
		HttpTools.writeToClient(resultStr);
	}
	/**
	 * 
	 * @title 导出华为小区邻区数据结果到文件
	 * @author chao.xj
	 * @date 2014-11-8下午3:57:52
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void exportHw2GCellNcellDataAjaxForAction() {
		
		hw2gNcellQueryCond=new Hw2GNcellQueryCond();
		hw2gNcellQueryCond.setBsc((String)attachParams.get("ncellBsc"));
		hw2gNcellQueryCond.setCell((String)attachParams.get("ncellForCell"));
		hw2gNcellQueryCond.setCityId(Long.parseLong((String) attachParams.get("cityId")));
		hw2gNcellQueryCond.setMeaDate((String)attachParams.get("ncellDate"));
		hw2gNcellQueryCond.setParam((String)attachParams.get("ncellParam"));
		hw2gNcellQueryCond.setNcell((String)attachParams.get("ncellForNcell"));
		log.info("进入方法exportHw2GCellNcellDataAjaxForAction。hw2gNcellQueryCond=" + hw2gNcellQueryCond );
		String token = null;
		String msg = "";
		//判断是否存在小区数据
		long cnt=rno2gHwCellManageService.queryHw2GNcellCnt(hw2gNcellQueryCond);
		if(cnt>0) {
			//获取根目录全路径
			String path = ServletActionContext.getServletContext().getRealPath(
				"/op/rno/");
			//获取对比差异
			token = rno2gHwCellManageService.exportHw2GCellNcellData(path, hw2gNcellQueryCond,cnt);
			if(token == null) {
				msg = "创建任务失败";
			}else{
				msg="创建任务成功";
			}
		} else {
			log.debug( hw2gNcellQueryCond + " 该条件下不存在小区邻区数据，无法导出数据");
			msg =  " 该条件下不存在小区邻区数据，无法导出数据";
		}
		String resultStr = "{'token':'"+token+"','msg':'"+msg+"'}";
		HttpTools.writeToClient(resultStr);
	}
	/**
	 * 
	 * @title 查询华为小区数据结果文件的导出进度
	 * @author chao.xj
	 * @date 2014-11-8下午3:07:42
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void queryExportProgressAjaxForAction() {
		log.info("进入方法queryExportProgressAjaxForAction。token=" + token);
		Map<String,Object> res = rno2gHwCellManageService.queryExportProgress(token);
		String result = gson.toJson(res);
		HttpTools.writeToClient(result);
	}
	/**
	 * 
	 * @title 下载华为2G小区数据文件
	 * @return
	 * @author chao.xj
	 * @date 2014-11-8下午3:08:52
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String downloadHw2GCellDataFileAction() {
		log.info("下载华为小区数据结果文件。token=" + token);
		boolean flag = false;
		String msg = "";
		// 获取任务的信息。
		String filePath = rno2gHwCellManageService.queryExportTokenFilePath(token);
		
		if(filePath == null) {
			log.error("找不到对应token="+token+"的导出任务！");
			msg = "文件导出失败";
		} else {
			log.info("结果文件路径：" + filePath);
			File f = new File(filePath);
			if (!f.exists()) {
				log.error("结果文件不存在！token="+token);
				msg = "文件导出失败";
			} else {
				log.info("文件名称："+f.getName());
				try {
					fileName = new String(f.getName().getBytes(), "ISO8859-1");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				} 
				setFileName(fileName);
				try {
					exportInputStream = new FileInputStream(filePath);
					flag = true;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		String result = "{'flag':"+flag+",'msg':'"+msg+"'}";
		return "success";
	}
	/**
	 * 
	 * @title 获取2G华为小区查询页面所有列表填充信息
	 * @author chao.xj
	 * @date 2014-10-27上午10:47:15
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void get2GHwCellQueryPageMoreDateInfoAction() {
		log.debug("进入get2GHwCellQueryPageMoreDateInfoAction");
		List<Map<String, Object>> dateList=rno2gHwCellManageService.queryLatelySeveralMonthOfHw2GCellDateInfo(cityId, monthNum);
		String datestr = gson.toJson(dateList);
		String result = "{'dateInfo':" + datestr + "}";
		log.debug("退出get2GEriCellQueryPageMoreDateInfoAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
}
