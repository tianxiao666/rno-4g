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
import com.iscreate.op.action.rno.model.Eri2GCellChannelQueryCond;
import com.iscreate.op.action.rno.model.Eri2GCellDescQueryCond;
import com.iscreate.op.action.rno.model.Eri2GCellQueryCond;
import com.iscreate.op.action.rno.model.Eri2GNcellQueryCond;
import com.iscreate.op.action.rno.model.Eri2GNcsDescQueryCond;
import com.iscreate.op.service.rno.Rno2GEriCellManageService;
import com.iscreate.op.service.rno.RnoResourceManagerService;
import com.iscreate.op.service.rno.parser.jobrunnable.Eri2GCellParserJobRunnable.DBFieldToLogTitle;
import com.iscreate.op.service.rno.tool.HttpTools;

public class Rno2GEriCellManageAction extends RnoCommonAction {

	private static Log log = LogFactory.getLog(Rno2GEriCellManageAction.class);
	private static Gson gson = new GsonBuilder().create();// 线程安全
	// -------注入-------------//
	private RnoResourceManagerService rnoResourceManagerService;
	private Rno2GEriCellManageService rno2gEriCellManageService;
	
	private List<String> coverareas;//覆盖区域
	private List<String> covertypes;//覆盖类型
	private List<String> importancegrade;//重要等级
	
	private long cityId;
	private String serviceType;
	
	//----------资源查询的条件--------------//
	//--爱立信2g cell 描述信息
	private Eri2GCellDescQueryCond eri2GCellDescQueryCond;
	
	//--爱立信 2G cell信息查询
	private Eri2GCellQueryCond eri2gCellQueryCond;
	private Eri2GCellChannelQueryCond eri2gCellChannelQueryCond;
	private Eri2GNcellQueryCond eri2gNcellQueryCond;
	//导出结果文件
	private String token;
	private InputStream exportInputStream;// 导出分析文件的输入流
	private String fileName;// 下载文件名
	//多少月
	private int monthNum;
		
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

	public int getMonthNum() {
		return monthNum;
	}

	public void setMonthNum(int monthNum) {
		this.monthNum = monthNum;
	}

	public Eri2GCellQueryCond getEri2gCellQueryCond() {
		return eri2gCellQueryCond;
	}

	public void setEri2gCellQueryCond(Eri2GCellQueryCond eri2gCellQueryCond) {
		this.eri2gCellQueryCond = eri2gCellQueryCond;
	}

	public Eri2GCellChannelQueryCond getEri2gCellChannelQueryCond() {
		return eri2gCellChannelQueryCond;
	}

	public void setEri2gCellChannelQueryCond(
			Eri2GCellChannelQueryCond eri2gCellChannelQueryCond) {
		this.eri2gCellChannelQueryCond = eri2gCellChannelQueryCond;
	}

	public Eri2GNcellQueryCond getEri2gNcellQueryCond() {
		return eri2gNcellQueryCond;
	}

	public void setEri2gNcellQueryCond(Eri2GNcellQueryCond eri2gNcellQueryCond) {
		this.eri2gNcellQueryCond = eri2gNcellQueryCond;
	}

	public RnoResourceManagerService getRnoResourceManagerService() {
		return rnoResourceManagerService;
	}

	public void setRnoResourceManagerService(
			RnoResourceManagerService rnoResourceManagerService) {
		this.rnoResourceManagerService = rnoResourceManagerService;
	}
	public Rno2GEriCellManageService getRno2gEriCellManageService() {
		return rno2gEriCellManageService;
	}

	public void setRno2gEriCellManageService(
			Rno2GEriCellManageService rno2gEriCellManageService) {
		this.rno2gEriCellManageService = rno2gEriCellManageService;
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

	/**
	 * 
	 * @title 初始化爱立信２Ｇ小区管理页面
	 * @return
	 * @author chao.xj
	 * @date 2014-10-13下午2:53:32
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String init2GEriCellManagerPageAction() {
		initAreaList();
		Map<String,List<String>> mid=rnoResourceManagerService.getAllCoverareaCovertypeAndImportances();
		coverareas=mid.get("coverarea");
		covertypes=mid.get("covertype");
		importancegrade=mid.get("importancegrade");
		return "success";
	}
	/**
	 * 
	 * @title 爱立信2G小区查询描述信息的方法
	 * @author chao.xj
	 * @date 2014-10-16下午10:35:55
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void queryEriCellDescAjaxAction(){

		eri2GCellDescQueryCond=new Eri2GCellDescQueryCond();
		eri2GCellDescQueryCond.setDataType((String)attachParams.get("dataType"));
		eri2GCellDescQueryCond.setCityId(Long.parseLong((String) attachParams.get("cityId")));
		eri2GCellDescQueryCond.setMeaBegTime((String) attachParams.get("cellMeaBegDate"));
		eri2GCellDescQueryCond.setMeaEndTime((String) attachParams.get("cellMeaEndDate"));
		log.debug("queryEriCellDescAjaxAction.page="+page+",attmap="+attachParams+",queryCond="+eri2GCellDescQueryCond);
		
		int cnt=page.getTotalCnt();
		if(cnt<0){
			cnt=(int)rno2gEriCellManageService.queryEriCellDescCnt(eri2GCellDescQueryCond);
		}
		
		Page newPage = page.copy();
		newPage.setTotalCnt(cnt);
		
		List<Map<String,Object>> dataRecs = rno2gEriCellManageService.queryEriCellDescByPage(eri2GCellDescQueryCond, newPage);
		log.debug("EricellDesc size:" + dataRecs==null?0:dataRecs.size());
		String datas = gson.toJson(dataRecs);

		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String result = "{'page':" + pstr + ",'data':" + datas + "}";
		log.debug("退出queryEriCellDescAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 
	 * @title 初始化爱立信２Ｇ小区 查询 页面
	 * @return
	 * @author chao.xj
	 * @date 2014-10-22上午11:08:19
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String init2GEriCellQueryPageAction() {
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
	 * @title 获取2G爱立信小区查询页面所有列表填充信息
	 * @author chao.xj
	 * @date 2014-10-23下午3:31:47
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void get2GEriCellQueryPageAllListInfoAction() {
		log.debug("进入get2GEriCellQueryPageListInfoAction");
		Map<String, Map<String,DBFieldToLogTitle>> cellChannelNcellParams=rno2gEriCellManageService.getEri2GCellChannelNcellParams();
		List<Map<String, Object>> bscList=rno2gEriCellManageService.queryBscListByCityId(cityId);
		List<Map<String, Object>> dateList=rno2gEriCellManageService.queryLatelyOneMonthOfEri2GCellDateInfo(cityId);
		String cellChannelNcellParamstr = gson.toJson(cellChannelNcellParams);
		String datestr = gson.toJson(dateList);
		String bscstr = gson.toJson(bscList);
		String result = "{'paramInfo':" + cellChannelNcellParamstr + ",'dateInfo':" + datestr + ",'bscInfo':" + bscstr + "}";
		log.debug("退出get2GEriCellQueryPageListInfoAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 
	 * @title 获取2G爱立信小区查询页面日期及BSC列表填充信息
	 * @author chao.xj
	 * @date 2014-10-23下午5:06:17
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void get2GEriCellQueryPageDateAndBscListInfoAction() {
		log.debug("进入get2GEriCellQueryPageDateAndBscListInfoAction");
		List<Map<String, Object>> bscList=rno2gEriCellManageService.queryBscListByCityId(cityId);
		List<Map<String, Object>> dateList=rno2gEriCellManageService.queryLatelyOneMonthOfEri2GCellDateInfo(cityId);
		String datestr = gson.toJson(dateList);
		String bscstr = gson.toJson(bscList);
		String result = "{'dateInfo':" + datestr + ",'bscInfo':" + bscstr + "}";
		log.debug("退出get2GEriCellQueryPageDateAndBscListInfoAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 
	 * @title 爱立信2G小区分页查询信息的方法
	 * @author chao.xj
	 * @date 2014-10-24上午11:47:13
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void queryEri2GCellByPageAjaxAction(){

		eri2gCellQueryCond=new Eri2GCellQueryCond();
		eri2gCellQueryCond.setBsc((String)attachParams.get("cellBsc"));
		eri2gCellQueryCond.setCell((String)attachParams.get("cellForCell"));
		eri2gCellQueryCond.setCityId(Long.parseLong((String) attachParams.get("cityId")));
		eri2gCellQueryCond.setMeaDate((String)attachParams.get("cellDate"));
		eri2gCellQueryCond.setParam((String)attachParams.get("cellParam"));
		log.debug("queryEri2GCellByPageAjaxAction.page="+page+",attmap="+attachParams+",queryCond="+eri2gCellQueryCond);
		
		int cnt=page.getTotalCnt();
		if(cnt<0){
			cnt=(int)rno2gEriCellManageService.queryEri2GCellCnt(eri2gCellQueryCond);
		}
		
		Page newPage = page.copy();
		newPage.setTotalCnt(cnt);
		
		List<Map<String,Object>> dataRecs = rno2gEriCellManageService.queryEri2GCellByPage(eri2gCellQueryCond, newPage);
		log.debug("eri2gCellQueryCond size:" + dataRecs==null?0:dataRecs.size());
		String datas = gson.toJson(dataRecs);

		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String result = "{'page':" + pstr + ",'data':" + datas + "}";
		log.debug("退出queryEri2GCellByPageAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 
	 * @title 爱立信2G小区信道分页查询信息的方法
	 * @author chao.xj
	 * @date 2014-10-24上午11:47:49
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void queryEri2GCellChannelByPageAjaxAction(){

		eri2gCellChannelQueryCond=new Eri2GCellChannelQueryCond();
		eri2gCellChannelQueryCond.setBsc((String)attachParams.get("channelBsc"));
		eri2gCellChannelQueryCond.setCell((String)attachParams.get("channelForCell"));
		eri2gCellChannelQueryCond.setCityId(Long.parseLong((String) attachParams.get("cityId")));
		eri2gCellChannelQueryCond.setMeaDate((String)attachParams.get("channelDate"));
		eri2gCellChannelQueryCond.setParam((String)attachParams.get("channelParam"));
		log.debug("queryEri2GCellChannelByPageAjaxAction.page="+page+",attmap="+attachParams+",queryCond="+eri2gCellChannelQueryCond);
		
		int cnt=page.getTotalCnt();
		if(cnt<0){
			cnt=(int)rno2gEriCellManageService.queryEri2GCellChannelCnt(eri2gCellChannelQueryCond);
		}
		
		Page newPage = page.copy();
		newPage.setTotalCnt(cnt);
		
		List<Map<String,Object>> dataRecs = rno2gEriCellManageService.queryEri2GCellChannelByPage(eri2gCellChannelQueryCond, newPage);
		log.debug("eri2gCellChannelQueryCond size:" + dataRecs==null?0:dataRecs.size());
		String datas = gson.toJson(dataRecs);

		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String result = "{'page':" + pstr + ",'data':" + datas + "}";
		log.debug("退出queryEri2GCellChannelByPageAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 
	 * @title 爱立信2G邻区分页查询信息的方法
	 * @author chao.xj
	 * @date 2014-10-24上午11:48:07
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void queryEri2GNcellByPageAjaxAction(){

		eri2gNcellQueryCond=new Eri2GNcellQueryCond();
		eri2gNcellQueryCond.setBsc((String)attachParams.get("ncellBsc"));
		eri2gNcellQueryCond.setCell((String)attachParams.get("ncellForCell"));
		eri2gNcellQueryCond.setCityId(Long.parseLong((String) attachParams.get("cityId")));
		eri2gNcellQueryCond.setMeaDate((String)attachParams.get("ncellDate"));
		eri2gNcellQueryCond.setParam((String)attachParams.get("ncellParam"));
		eri2gNcellQueryCond.setNcell((String)attachParams.get("ncellForNcell"));
		log.debug("queryEri2GNcellByPageAjaxAction.page="+page+",attmap="+attachParams+",queryCond="+eri2gNcellQueryCond);
		
		int cnt=page.getTotalCnt();
		if(cnt<0){
			cnt=(int)rno2gEriCellManageService.queryEri2GNcellCnt(eri2gNcellQueryCond);
		}
		
		Page newPage = page.copy();
		newPage.setTotalCnt(cnt);
		
		List<Map<String,Object>> dataRecs = rno2gEriCellManageService.queryEri2GNcellByPage(eri2gNcellQueryCond, newPage);
		log.debug("eri2gNcellQueryCond size:" + dataRecs==null?0:dataRecs.size());
		String datas = gson.toJson(dataRecs);

		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String result = "{'page':" + pstr + ",'data':" + datas + "}";
		log.debug("退出queryEri2GNcellByPageAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 
	 * @title 导出爱立信小区数据结果到文件
	 * @author chao.xj
	 * @date 2014-11-8下午3:57:52
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void exportEri2GCellDataAjaxForAction() {
		
		eri2gCellQueryCond=new Eri2GCellQueryCond();
		eri2gCellQueryCond.setBsc((String)attachParams.get("cellBsc"));
		eri2gCellQueryCond.setCell((String)attachParams.get("cellForCell"));
		eri2gCellQueryCond.setCityId(Long.parseLong((String) attachParams.get("cityId")));
		eri2gCellQueryCond.setMeaDate((String)attachParams.get("cellDate"));
		eri2gCellQueryCond.setParam((String)attachParams.get("cellParam"));
		log.info("进入方法exportEri2GCellDataAjaxForAction。eri2gCellQueryCond=" + eri2gCellQueryCond );
		String token = null;
		String msg = "";
		//判断是否存在小区数据
		long cnt=rno2gEriCellManageService.queryEri2GCellCnt(eri2gCellQueryCond);
		if(cnt>0) {
			//获取根目录全路径
			String path = ServletActionContext.getServletContext().getRealPath(
				"/op/rno/");
			//获取对比差异
			token = rno2gEriCellManageService.exportEri2GCellData(path, eri2gCellQueryCond,cnt);
			if(token == null) {
				msg = "创建任务失败";
			}else{
				msg="创建任务成功";
			}
		} else {
			log.debug( eri2gCellQueryCond + " 该条件下不存在小区数据，无法导出数据");
			msg =  " 该条件下不存在小区数据，无法导出数据";
		}
		String resultStr = "{'token':'"+token+"','msg':'"+msg+"'}";
		HttpTools.writeToClient(resultStr);
	}
	/**
	 * 
	 * @title 导出爱立信小区信道数据结果到文件
	 * @author chao.xj
	 * @date 2014-11-8下午3:57:52
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void exportEri2GCellChannelDataAjaxForAction() {
		
		eri2gCellChannelQueryCond=new Eri2GCellChannelQueryCond();
		eri2gCellChannelQueryCond.setBsc((String)attachParams.get("channelBsc"));
		eri2gCellChannelQueryCond.setCell((String)attachParams.get("channelForCell"));
		eri2gCellChannelQueryCond.setCityId(Long.parseLong((String) attachParams.get("cityId")));
		eri2gCellChannelQueryCond.setMeaDate((String)attachParams.get("channelDate"));
		eri2gCellChannelQueryCond.setParam((String)attachParams.get("channelParam"));
		
		log.info("进入方法exportEri2GCellDataAjaxForAction。eri2gCellChannelQueryCond=" + eri2gCellChannelQueryCond );
		String token = null;
		String msg = "";
		//判断是否存在小区数据
		long cnt=rno2gEriCellManageService.queryEri2GCellChannelCnt(eri2gCellChannelQueryCond);
		if(cnt>0) {
			//获取根目录全路径
			String path = ServletActionContext.getServletContext().getRealPath(
				"/op/rno/");
			//获取对比差异
			token = rno2gEriCellManageService.exportEri2GCellChannelData(path, eri2gCellChannelQueryCond,cnt);
			if(token == null) {
				msg = "创建任务失败";
			}else{
				msg="创建任务成功";
			}
		} else {
			log.debug( eri2gCellChannelQueryCond + " 该条件下不存在小区信道数据，无法导出数据");
			msg =  " 该条件下不存在小区信道数据，无法导出数据";
		}
		String resultStr = "{'token':'"+token+"','msg':'"+msg+"'}";
		HttpTools.writeToClient(resultStr);
	}
	/**
	 * 
	 * @title 导出爱立信小区邻区数据结果到文件
	 * @author chao.xj
	 * @date 2014-11-8下午3:57:52
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void exportEri2GCellNcellDataAjaxForAction() {
		
		eri2gNcellQueryCond=new Eri2GNcellQueryCond();
		eri2gNcellQueryCond.setBsc((String)attachParams.get("ncellBsc"));
		eri2gNcellQueryCond.setCell((String)attachParams.get("ncellForCell"));
		eri2gNcellQueryCond.setCityId(Long.parseLong((String) attachParams.get("cityId")));
		eri2gNcellQueryCond.setMeaDate((String)attachParams.get("ncellDate"));
		eri2gNcellQueryCond.setParam((String)attachParams.get("ncellParam"));
		eri2gNcellQueryCond.setNcell((String)attachParams.get("ncellForNcell"));
		log.info("进入方法exportEri2GCellDataAjaxForAction。eri2gNcellQueryCond=" + eri2gNcellQueryCond );
		String token = null;
		String msg = "";
		//判断是否存在小区数据
		long cnt=rno2gEriCellManageService.queryEri2GNcellCnt(eri2gNcellQueryCond);
		if(cnt>0) {
			//获取根目录全路径
			String path = ServletActionContext.getServletContext().getRealPath(
				"/op/rno/");
			//获取对比差异
			token = rno2gEriCellManageService.exportEri2GCellNcellData(path, eri2gNcellQueryCond,cnt);
			if(token == null) {
				msg = "创建任务失败";
			}else{
				msg="创建任务成功";
			}
		} else {
			log.debug( eri2gNcellQueryCond + " 该条件下不存在小区邻区数据，无法导出数据");
			msg =  " 该条件下不存在小区邻区数据，无法导出数据";
		}
		String resultStr = "{'token':'"+token+"','msg':'"+msg+"'}";
		HttpTools.writeToClient(resultStr);
	}	
	/**
	 * 
	 * @title 查询爱立信小区数据结果文件的导出进度
	 * @author chao.xj
	 * @date 2014-11-8下午3:07:42
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void queryExportProgressAjaxForAction() {
		log.info("进入方法queryExportProgressAjaxForAction。token=" + token);
		Map<String,Object> res = rno2gEriCellManageService.queryExportProgress(token);
		String result = gson.toJson(res);
		HttpTools.writeToClient(result);
	}
	/**
	 * 
	 * @title 下载爱立信2G小区数据文件
	 * @return
	 * @author chao.xj
	 * @date 2014-11-8下午3:08:52
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String downloadEri2GCellDataFileAction() {
		log.info("下载爱立信小区数据结果文件。token=" + token);
		boolean flag = false;
		String msg = "";
		// 获取任务的信息。
		String filePath = rno2gEriCellManageService.queryExportTokenFilePath(token);
		
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
	 * @title 通过日期和市ID确认爱立信2G小区数据的数量
	 * @author chao.xj
	 * @date 2014-11-7上午10:40:27
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void confirmEri2GCellCntAjaxAction(){

		eri2gCellQueryCond = new Eri2GCellQueryCond();
		eri2gCellQueryCond.setCityId(Long.parseLong((String) attachParams
				.get("cityId")));
		eri2gCellQueryCond.setMeaDate((String) attachParams.get("cellDate"));
		eri2gCellQueryCond.setParam((String)attachParams.get("cellParam"));
		log.debug("confirmEri2GCellCntAjaxAction.attmap="
				+ attachParams + ",queryCond=" + eri2gCellQueryCond);
		int cnt = (int) rno2gEriCellManageService
				.confirmEri2GCellCnt(eri2gCellQueryCond);
		String result = "{'CNT':" + cnt + "}";
		log.debug("退出confirmEri2GCellCntAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 
	 * @title 获取2G爱立信小区查询页面更多日期填充信息
	 * @author chao.xj
	 * @date 2014-10-23下午3:31:47
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void get2GEriCellQueryPageMoreDateInfoAction() {
		log.debug("进入get2GEriCellQueryPageMoreDateInfoAction");
		
		List<Map<String, Object>> dateList=rno2gEriCellManageService.queryLatelySeveralMonthOfEri2GCellDateInfo(cityId, monthNum);
		String datestr = gson.toJson(dateList);
		String result = "{'dateInfo':" + datestr + "}";
		log.debug("退出get2GEriCellQueryPageMoreDateInfoAction。输出：" + result);
		HttpTools.writeToClient(result);
}
}
