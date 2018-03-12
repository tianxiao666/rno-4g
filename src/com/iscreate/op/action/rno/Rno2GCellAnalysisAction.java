package com.iscreate.op.action.rno;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.action.rno.model.Area;
import com.iscreate.op.service.rno.Rno2GCellAnalysisService;
import com.iscreate.op.service.rno.RnoResourceManagerService;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.HttpTools;

public class Rno2GCellAnalysisAction extends RnoCommonAction {
	
	private static Log log = LogFactory.getLog(Rno2GEriCellManageAction.class);
	private static Gson gson = new GsonBuilder().create();// 线程安全
	
	// -------注入-------------//
	private RnoResourceManagerService rnoResourceManagerService;
	private Rno2GCellAnalysisService rno2GCellAnalysisService;
	//参数对比
	private String dayBefore;
	private long cityId;
	private String paramType;
	private String paramStr;
	private String bscStr;
	private String date1;
	private String date2;
	//参数对比详情
	private String bsc;
	private String param;
	//参数一致性检查
	private String checkType;
	private Map<String,String> settings;
	
	//导出结果文件
	private String token;
	private InputStream exportInputStream;// 导出分析文件的输入流
	private String fileName;// 下载文件名
	private String items;
	
	
	
	public String getItems() {
		return items;
	}
	public void setItems(String items) {
		this.items = items;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public InputStream getExportInputStream() 
		throws UnsupportedEncodingException {
		return exportInputStream;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Map<String, String> getSettings() {
		return settings;
	}
	public void setSettings(Map<String, String> settings) {
		this.settings = settings;
	}
	public String getCheckType() {
		return checkType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	public String getBsc() {
		return bsc;
	}
	public void setBsc(String bsc) {
		this.bsc = bsc;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public long getCityId() {
		return cityId;
	}
	public void setCityId(long cityId) {
		this.cityId = cityId;
	}
	public String getParamType() {
		return paramType;
	}
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	public String getParamStr() {
		return paramStr;
	}
	public void setParamStr(String paramStr) {
		this.paramStr = paramStr;
	}
	public String getBscStr() {
		return bscStr;
	}
	public void setBscStr(String bscStr) {
		this.bscStr = bscStr;
	}
	public String getDate1() {
		return date1;
	}
	public void setDate1(String date1) {
		this.date1 = date1;
	}
	public String getDate2() {
		return date2;
	}
	public void setDate2(String date2) {
		this.date2 = date2;
	}
	public String getDayBefore() {
		return dayBefore;
	}
	public void setDayBefore(String dayBefore) {
		this.dayBefore = dayBefore;
	}
	
	public Rno2GCellAnalysisService getRno2GCellAnalysisService() {
		return rno2GCellAnalysisService;
	}
	public void setRno2GCellAnalysisService(
			Rno2GCellAnalysisService rno2GCellAnalysisService) {
		this.rno2GCellAnalysisService = rno2GCellAnalysisService;
	}
	public RnoResourceManagerService getRnoResourceManagerService() {
		return rnoResourceManagerService;
	}
	public void setRnoResourceManagerService(
			RnoResourceManagerService rnoResourceManagerService) {
		this.rnoResourceManagerService = rnoResourceManagerService;
	}
	
	/**
	 * 初始化爱立信小区参数对比页面
	 * @return
	 * @author peng.jm
	 * @date 2014-10-17下午03:37:59
	 */
	public String init2GEriCellParamComparePageAction() {
		initAreaList();
		//保存当前日期的前一天日期
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -2);
		date = calendar.getTime();
		DateUtil dateUtil = new DateUtil();
		dayBefore = dateUtil.format_yyyyMMdd(date);
		
		return "success";
	}
	
	/**
	 * 获取爱立信小区某两天的参数对比数据
	 * @author peng.jm
	 * @date 2014-10-22下午04:03:21
	 */
	public void eriCellParamsCompareForAjaxAction() {
		log.info("进入方法eriCellParamsCompareForAjaxAction。cityId=" + cityId + ",paramType="
				+ paramType + ",paramStr=" + paramStr + ",bscStr=" + bscStr
				+ ",date1=" + date1 + ",date2=" + date2);
		boolean flag = false;
		String result = "";
		//爱立信小区参数对比结果集对象
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		
		//判断第一个日期是否存在小区数据
		boolean flag1 = rno2GCellAnalysisService
					.isEriCellDataExistedOnTheDate(cityId, paramType, bscStr, date1);
		if(flag1) {
			//判断第二个日期是否存在小区数据
			boolean flag2 = rno2GCellAnalysisService
						.isEriCellDataExistedOnTheDate(cityId, paramType, bscStr, date2);
			if(flag2) {
				//获取对比差异
				res = rno2GCellAnalysisService
					.eriCellParamsCompare(cityId, paramType, paramStr, bscStr,
							date1, date2);
				flag = true;
				result = gson.toJson(res);
			} else {
				log.debug(date2 + " 日期不存在小区数据，无法进行比较");
				result = "\"" + date2 + " 日期不存在小区数据，无法进行比较\"";
			}
		} else {
			log.debug( date1 + " 日期不存在小区数据，无法进行比较");
			result = "\"" + date1 + " 日期不存在小区数据，无法进行比较\"";
		}
		String resultStr = "{'flag':"+flag+",'result':"+result+"}";
		HttpTools.writeToClient(resultStr);
	}
	
	/**
	 * 导出爱立信小区数据对比结果到文件
	 * @author peng.jm
	 * @date 2014-11-6上午09:50:54
	 */
	public void exportEriCellCompareDataAjaxForAction() {
		log.info("进入方法exportEriCellCompareDataAjaxForAction。cityId=" + cityId + ",paramType="
				+ paramType + ",paramStr=" + paramStr + ",bscStr=" + bscStr
				+ ",date1=" + date1 + ",date2=" + date2);
		
		String token = "-1";
		String msg = "";
		//判断第一个日期是否存在小区数据
		boolean flag1 = rno2GCellAnalysisService
					.isEriCellDataExistedOnTheDate(cityId, paramType, bscStr, date1);
		if(flag1) {
			//判断第二个日期是否存在小区数据
			boolean flag2 = rno2GCellAnalysisService
						.isEriCellDataExistedOnTheDate(cityId, paramType, bscStr, date2);
			if(flag2) {
				//获取根目录全路径
				String path = ServletActionContext.getServletContext().getRealPath(
					"/op/rno/");
				
				//获取对比差异
				token = rno2GCellAnalysisService
					.exportEriCellCompareData(cityId, paramType, paramStr, bscStr, date1, date2, path);
				if(token == null) {
					msg = "爱立信小区参数对比文件导出失败";
				}
			} else {
				log.debug(date2 + " 日期不存在小区数据，无法导出数据");
				msg = date2 + " 日期不存在小区数据，无法导出数据";
			}
		} else {
			log.debug( date1 + " 日期不存在小区数据，无法导出数据");
			msg = date1 + " 日期不存在小区数据，无法导出数据";
		}
		String resultStr = "{'token':'"+token+"','msg':'"+msg+"'}";
		HttpTools.writeToClient(resultStr);
	}
	
	/**
	 * 查询文件的导出进度
	 * @author peng.jm
	 * @date 2014-10-23下午05:56:13
	 */
	public void queryExportProgressAjaxForAction() {
		log.info("进入方法queryExportProgressAjaxForAction。token=" + token);
		Map<String,Object> res = rno2GCellAnalysisService.queryExportProgress(token);
		String result = gson.toJson(res);
		HttpTools.writeToClient(result);
	}
	
	/**
	 * 下载结果文件
	 * @return
	 * @author peng.jm
	 * @date 2014-11-10下午05:37:06
	 */
	public String downloadEriCellAnalysisDataFileAction() {
		log.info("下载爱立信小区参数分析结果文件。token=" + token);
		// 获取任务的信息。
		String filePath = rno2GCellAnalysisService.queryExportTokenFilePath(token);
		
		if(filePath == null) {
			log.error("找不到对应token="+token+"的导出任务！");
			return "error"; 
		} else {
			log.info("结果文件路径：" + filePath);
			File f = new File(filePath);
			if (!f.exists()) {
				log.error("结果文件不存在！token="+token);
				return "error"; 
			} else {
				log.info("文件名称："+f.getName());
				try {
					fileName = new String(f.getName().getBytes(), "ISO8859-1");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
					return "error"; 
				} 
				setFileName(fileName);
				try {
					exportInputStream = new FileInputStream(filePath);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return "error"; 
				}
			}
		}
		return "success";
	}
	
	/**
	 * 获取爱立信小区某两个日期的参数不一致的详情
	 * @author peng.jm
	 * @date 2014-10-23下午05:56:13
	 */
	public void getEriCellParamsDiffDetailForAjaxAction() {
		log.info("进入方法getEriCellParamsDiffDetailForAjaxAction。cityId=" + cityId
				+ ",bsc=" + bsc + ",paramType=" + paramType + ",param=" + param 
				+ ",date1=" + date1 + ",date2=" + date2);
		boolean flag = false;
		String result = "";
		if(("").equals(bsc) || ("").equals(param) 
				|| ("").equals(date1) || ("").equals(date2) ) {
			log.error("getEriCellParamsDiffDetailForAjaxAction的参数为空！");
		} else {
			List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
			res = rno2GCellAnalysisService
						.getEriCellParamsDiffDetail(cityId, bsc, paramType, param, date1, date2);
			flag = true;
			result = gson.toJson(res);
		}
		String resultStr = "{'flag':"+flag+",'result':"+result+"}";
		HttpTools.writeToClient(resultStr);
	}
	
	/**
	 * 初始化爱立信小区参数一致性检查页面
	 * @return
	 * @author peng.jm
	 * @date 2014-10-25上午10:56:36
	 */
	public String init2GEriCellParamCheckPageAction() {
		initAreaList();
		return "success";
	}
	
	/**
	 * 通过检查类型获取爱立信小区的参数一致性结果
	 * @author peng.jm
	 * @date 2014-10-27下午02:03:04
	 */
	public void getEriCellParamCheckByTypeForAjaxAction() {
		log.info("进入方法getEriCellParamCheckByTypeForAjaxAction。checkType=" + checkType
				+ ",bscStr=" + bscStr + ",date1=" + date1 + ",cityId="+cityId+",settings="+settings);
		boolean flag = false;
		String result = "";
		if(("").equals(bscStr) || ("").equals(checkType) 
				|| ("").equals(date1) || ("").equals(cityId)) {
			log.error("getEriCellParamsDiffDetailForAjaxAction的参数为空！");
		} else {
			List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
			res = rno2GCellAnalysisService
						.getEriCellParamCheckByType(checkType, bscStr, date1, cityId, settings);
			flag = true;
			result = gson.toJson(res);
		}
		String resultStr = "{'flag':"+flag+",'result':"+result+"}";
		HttpTools.writeToClient(resultStr);
	}
	
	/**
	 * 导出爱立信小区参数一致性检查的结果到文件中
	 * @author peng.jm
	 * @date 2014-11-10下午05:42:21
	 */
	public void exportEriCellCheckDataAjaxForAction() {
		log.info("进入方法exportEriCellCheckDataAjaxForAction。items=" + items
				+ ",bscStr=" + bscStr + ",date=" + date1 + ",cityId="+cityId+",settings="+settings);
		String token = "-1";
		String msg = "";

		if(("").equals(items) || ("").equals(bscStr) 
				|| ("").equals(date1) || ("").equals(cityId) ) {
			log.error("exportEriCellCheckDataAjaxForAction的参数为空！");
			msg = "查询参数为空！";
		} else {
			String bscs[] = bscStr.split(",");
			String bscStr2 = "";
			for (String bsc : bscs) {
				bscStr2 += "'"+bsc+"',";
			}
			bscStr2 = bscStr2.substring(0,bscStr2.length()-1);
			//通过bsc名称字符串获取bscId字符串
			List<Map<String, Object>> bscList = rno2GCellAnalysisService.getBscDetailByBscs(bscStr2);
			if(bscList.size() == 0) {
				log.debug("bscIdStr="+bscStr+"条件下，查询不到BSC！");
			}
			//获取bscId列
			String bscIdStr = "";
			for (Map<String, Object> bsc : bscList) {
				if(bsc.get("BSC_ID") != null) {
					bscIdStr += bsc.get("BSC_ID").toString() + ",";
				}
			}
			//bscId列
			bscIdStr = bscIdStr.substring(0,bscIdStr.length()-1);
			
			boolean flag = rno2GCellAnalysisService
						.isEriCellDataExistedOnTheDate(cityId, "cell", bscIdStr, date1);
			if(flag) {
				//获取根目录全路径
				String path = ServletActionContext.getServletContext().getRealPath(
					"/op/rno/");
				//输出一致性检查结果数据到文件
				token = rno2GCellAnalysisService
					.exportEriCellCheckData(items, bscIdStr, date1, cityId, settings, path);
				if(token == null) {
					msg = "爱立信小区参数一致性检查文件导出失败";
				}
			} else {
				log.debug( date1 + " 日期不存在小区数据，无法导出数据");
				msg = date1 + " 日期不存在小区数据，无法导出数据";
			}
		}
	
		String resultStr = "{'token':'"+token+"','msg':'"+msg+"'}";
		HttpTools.writeToClient(resultStr);
	}
	
}
