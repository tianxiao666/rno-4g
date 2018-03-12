package com.iscreate.op.action.rno;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.action.rno.model.Area;
import com.iscreate.op.action.rno.model.Point;
import com.iscreate.op.action.rno.upload.DataUploadStatus;
import com.iscreate.op.action.rno.upload.FileAcceptStatus;
import com.iscreate.op.action.rno.upload.accept.FileAcceptable;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.pojo.rno.PlanConfig;
import com.iscreate.op.pojo.rno.RnoDataCollectRec;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.rno.RnoCommonService;
import com.iscreate.op.service.rno.parser.FileParserManager.ParserToken;
import com.iscreate.op.service.rno.tool.FileTool;
import com.iscreate.op.service.rno.tool.HttpTools;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.plat.system.datasourcectl.DataSourceConst;
import com.iscreate.plat.system.datasourcectl.DataSourceContextHolder;
import com.opensymphony.xwork2.ActionContext;

public class RnoCommonAction {

	private static Log log = LogFactory.getLog(RnoCommonAction.class);
	protected static Gson gson = new GsonBuilder().create();// 线程安全

	protected static SimpleDateFormat sdf_SIMPLE = new SimpleDateFormat(
			"yyyy-MM-dd");
	protected static SimpleDateFormat sdf_full = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	// --------注入--------------------//
	protected RnoCommonService rnoCommonService;

	// ------------页面变量--------------//
	private long parentAreaId;
	private String subAreaLevel;

	// ---------页面变量----------------//
	private long fileSize;
	private File file;// 文件输入流
	// 上传文件名
	private String fileFileName;
	// 上传文件类型
	private String fileContentType;
	private String fileCode;// 文件编码
	private boolean needPersist;// 是否需要持久化
	private boolean autoload;// 是否要自动加载到列表
	private boolean update;// 是否更新
	private long oldConfigId;// 原配置方案id
	private long areaId;
	private boolean systemConfig;// 是否系统配置
	protected HashMap<String, Object> attachParams;
	private String token;// 文件解析任务对应的token

	private String fileName;// 下载文件名 （包括后缀名：如 cell.xlsx,test.jpg） yuan.yw add

	protected Page page;

	private List<Area> provinceAreas;
	private List<Area> cityAreas;
	private List<Area> countryAreas;
	private Point centerPoint;// 中心点

	private String areaName;

	private long cityId;// 城市id
	private long bscId;// bscid
	private String searchCellStr;// 搜索小区关键词

	private String mapId;

	private String error;// 出错信息

	private String meaTime;// 数据所属日期

	public List<Area> getProvinceAreas() {
		return provinceAreas;
	}

	public void setProvinceAreas(List<Area> provinceAreas) {
		this.provinceAreas = provinceAreas;
	}

	public List<Area> getCityAreas() {
		return cityAreas;
	}

	public void setCityAreas(List<Area> cityAreas) {
		this.cityAreas = cityAreas;
	}

	public List<Area> getCountryAreas() {
		return countryAreas;
	}

	public void setCountryAreas(List<Area> countryAreas) {
		this.countryAreas = countryAreas;
	}

	public Point getCenterPoint() {
		return centerPoint;
	}


	public void setCenterPoint(Point centerPoint) {
		this.centerPoint = centerPoint;
	}

	public File getFile() {
		return file;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	public String getFileCode() {
		return fileCode;
	}

	public void setFileCode(String fileCode) {
		this.fileCode = fileCode;
	}

	public boolean isNeedPersist() {
		return needPersist;
	}

	public void setNeedPersist(boolean needPersist) {
		this.needPersist = needPersist;
	}

	public boolean isAutoload() {
		return autoload;
	}

	public void setAutoload(boolean autoload) {
		this.autoload = autoload;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public long getOldConfigId() {
		return oldConfigId;
	}

	public void setOldConfigId(long oldConfigId) {
		this.oldConfigId = oldConfigId;
	}

	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public boolean isSystemConfig() {
		return systemConfig;
	}

	public void setSystemConfig(boolean systemConfig) {
		this.systemConfig = systemConfig;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getParentAreaId() {
		return parentAreaId;
	}

	public void setParentAreaId(long parentAreaId) {
		this.parentAreaId = parentAreaId;
	}

	public HashMap<String, Object> getAttachParams() {
		return attachParams;
	}

	public void setAttachParams(HashMap<String, Object> attachParams) {
		this.attachParams = attachParams;
	}

	public String getSubAreaLevel() {
		return subAreaLevel;
	}

	public void setSubAreaLevel(String subAreaLevel) {
		this.subAreaLevel = subAreaLevel;
	}

	public void setRnoCommonService(RnoCommonService rnoCommonService) {
		this.rnoCommonService = rnoCommonService;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public long getBscId() {
		return bscId;
	}

	public void setBscId(long bscId) {
		this.bscId = bscId;
	}

	public String getSearchCellStr() {
		return searchCellStr;
	}

	public void setSearchCellStr(String searchCellStr) {
		this.searchCellStr = searchCellStr;
	}

	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMeaTime() {
		return meaTime;
	}

	public void setMeaTime(String meaTime) {
		this.meaTime = meaTime;
	}

	/**
	 * 上传文件
	 * 
	 * @return Sep 9, 2013 11:57:49 AM gmh
	 */
	public void uploadFileAjaxAction() {
		// System.out.println(areaId+fileCode+needPersist+update+oldConfigId);
		log.info("Rno进入 uploadFileAjaxAction . fileCode=" + fileCode
				+ ",needPersist=" + needPersist + ",autoload=" + autoload
				+ ",update=" + update + ",oldConfigId=" + oldConfigId
				+ ",areaId=" + areaId + ",systemConfig=" + systemConfig
				+ ",attachParams=" + attachParams);
		boolean valid = true;
		String msg = "";
		if (file == null) {
			log.error("未上传文件！");
			msg += "未上传文件！";
			valid = false;
		}
		if (fileCode == null || fileCode.trim().isEmpty()) {
			log.error("未带文件类型编码！");
			msg += "文件类型不明！";
			valid = false;
		}
		if (areaId == 0) {
			log.error("未指定区域！");
			msg += "未指定区域！";
			valid = false;
		}

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/json");
		response.setCharacterEncoding("utf-8");

		String token = null;
		if (valid) {
			// 将文件转移到存储路径
			String path = ServletActionContext.getServletContext().getRealPath(
					"/op/rno/upload");
			// System.out.println("only path==" + path);
			Date now = new Date();
			String day = sdf_SIMPLE.format(now);
			path += "/" + day + "/" + file.getName();
			// System.out.println("with file path==" + path);
			File anotherFile = new File(path);
			if (!anotherFile.getParentFile().exists()) {
				anotherFile.getParentFile().mkdirs();
			}

			FileTool.copy(file, anotherFile);
			if (attachParams == null) {
				attachParams = new HashMap<String, Object>();
			}
			attachParams.put("fileName", fileFileName);
			attachParams.put("contentType", fileContentType);
			attachParams.put("session", ServletActionContext.getRequest()
					.getSession());
			token = rnoCommonService.importData(anotherFile, fileCode,
					needPersist, autoload, update, oldConfigId, areaId,
					systemConfig, attachParams);
			if (token == null) {
				msg += "当前不支持该类型文件的解析！";
			}
		}

		String result = "{'token':'" + token + "','msg':'" + msg + "'}";

		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("写uploadFileAjaxAction回客户端失败！");
		}
		log.debug("uploadFileAjaxAction结果：" + result);
		log.debug("Rno 退出uploadFileAjaxAction");
	}

	/**
	 * 查询token对应的进度情况
	 * 
	 * Sep 12, 2013 5:19:42 PM gmh
	 */
	public void queryUploadStatusAjaxAction() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/json");
		response.setCharacterEncoding("utf-8");

		ParserToken pt = rnoCommonService.getToken(token);
		String result = "";
		if (pt == null) {
			result = "{'flag':false,'msg':'无进度信息！'}";
		} else {
			if (pt.isFail()) {
				result = "{'flag':false,'msg':'文件解析失败！'}";
			} else {
				result = "{'flag':true,'progress':" + pt.getProgress() * 100
						+ ",'msg':'" + pt.getMsg() + "'}";
			}
			//
			// result=gson.toJson(pt);
		}
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("写uploadFileAjaxAction回客户端失败！");
		}
	}

	/**
	 * 获取上传结果
	 * 
	 * Sep 13, 2013 11:37:02 AM gmh
	 */
	public void getUploadResultAjaxAction() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");
		log.info("进入getUploadResultAjaxAction。token=" + token);
		Object result = rnoCommonService.popUploadResult(token);
		log.info("获取到的进度情况。result=" + result);
		if (result == null) {
			result = "";
		}
		try {
			if (result instanceof String) {
				response.getWriter().write((String) result);
			} else {
				String st = gson.toJson(result);
				response.getWriter().write(st);
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.error("getUploadResultAjaxAction返回客户端时失败！");
		}
	}

	/**
	 * 根据父区域id，获取指定类型的子区域列表
	 * 
	 * @author brightming 2013-10-17 下午2:07:06
	 */
	public void getSubAreaByParentAreaForAjaxAction() {
		log.info("进入方法：getSubAreaByParentAreaForAjaxAction.parentAreaId="
				+ parentAreaId + ",subAreaLevel=" + subAreaLevel);
		String account = (String) SessionService.getInstance().getValueByKey(
				"userId");
		List<Area> areas = rnoCommonService
				.getSpecialSubAreasByAccountAndParentArea(account,
						parentAreaId, subAreaLevel);
		String result = gson.toJson(areas);
		log.info("退出getSubAreaByParentAreaForAjaxAction。输出：" + result);
		writeToClient(result);
	}

	public void writeToClient(String result) {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");

		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("writeToClient 出错！");
		}
	}

	/**
	 * 
	 * @description: 下载文件输入流
	 * @author：yuan.yw
	 * @return
	 * @return InputStream
	 * @throws UnsupportedEncodingException
	 * @date：Oct 25, 2013 10:43:55 AM
	 */
	public InputStream getInputStream() throws UnsupportedEncodingException {
		String fn = new String(this.fileName.getBytes("ISO-8859-1"), "utf-8");// 中文乱码转码
		String file = "/op/rno/download/" + fn;// 文件下载路径 hardcode
		// this.fileName = new String(this.fileName.getBytes("utf-8"),
		// "ISO-8859-1");
		return ServletActionContext.getServletContext().getResourceAsStream(
				file);
	}

	/**
	 * 
	 * @description: 文件下载
	 * @author：yuan.yw
	 * @return
	 * @return String
	 * @date：Oct 25, 2013 10:29:28 AM
	 */
	public String fileDownloadAction() {
		String fn = "";
		try {
			fn = new String(this.fileName.getBytes("ISO-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error("无法正确解析待下载的文件名！");
			error = "无法正确解析待下载的文件名！";
			return "fail";
		}// 中文乱码转码
		String filePath = "/op/rno/download/" + fn;// 文件下载路径 hardcode

		InputStream is = ServletActionContext.getServletContext()
				.getResourceAsStream(filePath);
		File file = new File(filePath);
		if (is == null) {
			log.error("待下载的文件：" + fn + "不存在！");
			error = "待下载的文件：" + fn + "不存在！";
			return "fail";
		} else {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return "success";
		}
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 获取纠偏GPS的action
	 * 
	 * @return
	 * @author chao.xj
	 * @date 2013-10-28上午10:15:58
	 */
	public String getLngLatCorrectValueAction() {
		// Map<String, List<RnoMapLnglatRelaGps>>
		// map=rnoCommonService.getSpecialAreaRnoMapLnglatRelaGpsMapList(88);
		// String bb=rnoCommonService.getLngLatCorrectValue("114.05405877154",
		// "23.578861559190003",map);
		// System.out.println(bb);
		return "success";
	}

	/**
	 * 
	 * 公用定时任务：完成定时刷新页面功能commontimedtask.js
	 * 
	 * @author chao.xj
	 * @date 2013-10-29上午11:18:58
	 */
	public void commonTimedRefreshTaskAction() {

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/json");
		response.setCharacterEncoding("utf-8");
		String result = "[{'flag':'success'}]";
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	protected void initAreaList() {
		String account = (String) SessionService.getInstance().getValueByKey(
				"userId");
		provinceAreas = rnoCommonService.getSpecialLevalAreaByAccount(account,
				"省");
		cityAreas = new ArrayList<Area>();
		countryAreas = new ArrayList<Area>();
		//通过获取默认城市，然后置顶
		long cfgCityId = rnoCommonService.getUserConfigAreaId(account);
//		long cfgProvinceId=rnoCommonService.getParentIdByCityId(cfgCityId);
		long cfgProvinceId=0;
		if(cfgCityId!=-1){
			cfgProvinceId=rnoCommonService.getParentIdByCityId(cfgCityId);
		}
		//再恢复数据源
		DataSourceContextHolder.setDataSourceType(DataSourceConst.authDs);
		provinceAreas = rnoCommonService.getSpecialLevalAreaByAccount(account,
				"省");
		//如果该帐户没有设定过默认区域，哪就默认第一个省份为默认区域
		if(cfgProvinceId==0){
			cfgProvinceId=provinceAreas.get(0).getArea_id();
		}
		//先保存第一个省的对象信息:为了使默认的省排在首位
		Area tmp=provinceAreas.get(0);
		for (int i = 0; i < provinceAreas.size(); i++) {
			//替换次序
			if(provinceAreas.get(i).getArea_id()==cfgProvinceId){
				provinceAreas.set(0, provinceAreas.get(i));
				provinceAreas.set(i, tmp);
				break;
			}
		}
		if (provinceAreas != null && provinceAreas.size() > 0) {
			cityAreas = rnoCommonService
					.getSpecialSubAreasByAccountAndParentArea(account,
							cfgProvinceId, "市");
			
			Area areaTemp = new Area();
			for (int i = 0; i < cityAreas.size(); i++) {
				if(cityAreas.get(i).getArea_id() == cfgCityId) {
					areaTemp  = cityAreas.get(0);
					cityAreas.set(0, cityAreas.get(i));
					cityAreas.set(i, areaTemp);
				}
			}
			if (cityAreas != null && cityAreas.size() > 0) {
				countryAreas = rnoCommonService
						.getSpecialSubAreasByAccountAndParentArea(account,
								cityAreas.get(0).getArea_id(), "区/县");
				if (countryAreas != null && countryAreas.size() > 0) {
					areaName = countryAreas.get(0).getName();
					centerPoint = new Point();
					centerPoint.setLng(countryAreas.get(0).getLongitude());
					centerPoint.setLat(countryAreas.get(0).getLatitude());
				}
			}
		}

	}

	/**
	 * 从列表移除
	 * 
	 * @param code
	 * @param configIds
	 * @return
	 * @author brightming 2013-11-27 下午2:42:02
	 */
	protected int removeFromAnalysis(String code, String configIds) {
		log.info("进入方法 ：removeFromAnalysisForAjaxAction。code=" + code
				+ ",configIds=" + configIds);
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		HttpSession session = request.getSession();
		List<PlanConfig> planConfigs = (List<PlanConfig>) session
				.getAttribute(code);
		if (configIds == null) {
			log.error("未指明需要删除的ncs id");
			HttpTools.writeToClient("false");
			return 0;
		}
		String[] ids = configIds.split(",");
		if (ids.length == 0) {
			log.error("未指明需要删除的ncs id");
			HttpTools.writeToClient("false");
			return 0;
		}
		int cnt = 0;
		if (planConfigs != null && planConfigs.size() > 0) {
			int i = 0;
			String inid = "";
			List<PlanConfig> del = new ArrayList<PlanConfig>();
			for (String id : ids) {
				for (i = 0; i < planConfigs.size(); i++) {
					inid = planConfigs.get(i).getConfigId() + "";
					if (id.equals(inid)) {
						del.add(planConfigs.get(i));
						break;
					}
				}
			}
			if (del.size() > 0) {
				cnt = del.size();
				log.info("移除指定的加载项。");
				for (int j = 0; j < del.size(); j++) {
					planConfigs.remove(del.get(j));
				}

				session.setAttribute(code, planConfigs);
			}
		}
		log.info("退出方法：removeFromAnalysisForAjaxAction。");
		return cnt;
	}

	/**
	 * 获取城市下的所有bsc
	 * 
	 * @author brightming 2014-2-10 下午3:50:39
	 */
	public void getAllBscInCityForAjaxAction() {
		log.info("进入方法：getAllBscInCityForAjaxAction。cityId=" + cityId);
		List<Map<String, Object>> bscs = rnoCommonService
				.getAllBscsInCity(cityId);
		log.info("城市：" + cityId + "下有" + bscs.size() + "个bsc");
		String result = gson.toJson(bscs);
		HttpTools.writeToClient(result);
		return;
	}

	/**
	 * 获取bsc下的所有小区
	 * 
	 * @author brightming 2014-2-10 下午4:26:15
	 */
	public void getAllCellsInBscForAjaxAction() {
		log.info("进入方法：getAllCellsInBscForAjaxAction。bscId=" + bscId);
		List<Map<String, Object>> cells = rnoCommonService
				.getAllCellsInBsc(bscId);
		log.info("bsc：" + bscId + "下有" + cells.size() + "个cell");
		String result = gson.toJson(cells);
		HttpTools.writeToClient(result);
		return;
	}

	/**
	 * 获取城市下的所有的bsc和对应的cell
	 * 
	 * @author brightming 2014-2-11 下午12:06:10
	 */
	public void getAllBscCellsInCityForAjaxAction() {
		log.info("进入方法：getAllBscCellsInCityForAjaxAction。cityId=" + cityId);
		List<Map<String, Object>> cells = rnoCommonService
				.getAllBscCellsInCity(cityId);
		log.info("city：" + cityId + "下有" + cells.size() + "个cell");
		// 整理
		String prebsc = "", curbsc = "";
		List<Map<String, Object>> line = null;
		StringBuilder buf = new StringBuilder();
		for (Map<String, Object> one : cells) {
			if (one == null) {
				continue;
			}
			if (one.get("ENGNAME") == null) {
				curbsc = "未知bsc";
			} else {
				curbsc = "" + one.get("ENGNAME");
			}
			if (!prebsc.equals(curbsc)) {
				if (line != null && !line.isEmpty()) {
					buf.append(generateTree(prebsc, line));
				}
				line = new ArrayList<Map<String, Object>>();
				prebsc = curbsc;
			}
			line.add(one);
		}

		if (!line.isEmpty()) {
			buf.append(generateTree(prebsc, line));
		}

		String result = buf.toString();
		HttpTools.writeToClient(result);
		return;
	}

	private String generateTree(String bsc, List<Map<String, Object>> data) {
		String res = "<li><span class='bscCls'>" + bsc + "（小区数量:" + data.size()
				+ "）</span><ul>";
		String label, name, manufacturers;
		for (Map<String, Object> one : data) {
			label = one.get("LABEL").toString();
			name = one.get("NAME").toString();
			manufacturers = one.get("MANUFACTURERS").toString();
			res += "<li><span class='cellCls' data='" + label + "' bsc='" + bsc
					+ "' manufacturers='"+manufacturers+"'>" + label + "(" + name + ")</span></li>";
		}
		res += "</ul>";
		return res;
	}

	/**
	 * 通过城市id获取bsc和小区信息，并以map形式保存
	 * @author peng.jm
	 * @date 2014-11-17下午03:20:12
	 */
	public void getAllBscCellsByCityIdInMapForAjaxAction() {
		log.info("进入方法：getAllBscCellsByCityIdInMapForAjaxAction。cityId=" + cityId);
		
		Map<String, List<Map<String, Object>>> res = new HashMap<String, List<Map<String,Object>>>();
		List<Map<String, Object>> cells = rnoCommonService
				.getAllBscCellsInCity(cityId);
		log.info("city：" + cityId + "下有" + cells.size() + "个cell");
		// 整理
		String prebsc = "", curbsc = "";
		List<Map<String, Object>> line = null;
		StringBuilder buf = new StringBuilder();
		for (Map<String, Object> one : cells) {
			if (one == null) {
				continue;
			}
			if (one.get("ENGNAME") == null) {
				curbsc = "未知bsc";
			} else {
				curbsc = "" + one.get("ENGNAME");
			}
			if (!prebsc.equals(curbsc)) {
				if (line != null && !line.isEmpty()) {
					res.put(prebsc, line);
				}
				line = new ArrayList<Map<String, Object>>();
				prebsc = curbsc;
			}
			line.add(one);
		}
		
		if (!line.isEmpty()) {
			res.put(prebsc, line);
		}

		String result = gson.toJson(res);
		HttpTools.writeToClient(result);
		return;
	}
	
	
	/**
	 * 获取城市下的所有的bsc
	 * @author peng.jm
	 * @date 2014-10-22下午01:56:51
	 */
	public void getAllBscByCityIdForAjaxAction() {
		log.info("进入方法：getAllBscByCityIdForAjaxAction。cityId=" + cityId);
		List<Map<String, Object>> bscList = rnoCommonService
				.getAllBscByCityId(cityId);
		log.info("city：" + cityId + "下有" + bscList.size() + "个bsc");
		boolean flag = false;
		if(bscList.size() > 0) {
			flag = true;
		}
		String bscs = gson.toJson(bscList);
		String result = "{'flag':"+flag+",'bscList':"+bscs+"}";
		HttpTools.writeToClient(result);
	}

	/**
	 * 指定城市下搜索小区
	 * 
	 * @author brightming 2014-2-10 下午4:28:45
	 */
	public void searchCellWithKeyWordForAjaxAction() {
		log.info("进入方法：searchCellWithKeyWordForAjaxAction。cityId=" + cityId
				+ ",searchCellStr=" + searchCellStr);
		List<Map<String, Object>> cells = rnoCommonService
				.findCellWithPartlyWords(cityId, searchCellStr);
		log.info("找到有" + cells.size() + "个cell");
		String result = gson.toJson(cells);
		HttpTools.writeToClient(result);
		return;
	}

	/**
	 * 通过session存储mapid
	 * 
	 * @title
	 * @author chao.xj
	 * @date 2014-3-10上午11:02:47
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void storageMapIdBySessForAjaxAction() {
		// null,baidu,google
		String map = (String) SessionService.getInstance().getValueByKey(
				"mapId");
		log.info("mapid为空时:" + map);
		SessionService.getInstance().setValueByKey("mapId", mapId);
		log.info("mapid存储后:"
				+ (String) SessionService.getInstance().getValueByKey("mapId"));
		String result = gson.toJson(mapId);
		writeToClient(result);
		// SessionService.getInstance().rmvValueByKey("mapId");
		// log.info("删除mapid后:"+(String)
		// SessionService.getInstance().getValueByKey("mapId"));

	}
	
	/**
	 * 保存用户的配置
	 * 
	 * @author peng.jm
	 * @date 2014-9-25下午02:51:15
	 */
	public void saveUserConfigAction() {
		log.info("进入方法：saveUserConfigAction。cityId=" + cityId);
		String account = (String) SessionService.getInstance().getValueByKey(
			"userId");
		boolean flag = false;
		if(cityId != 0) {
			flag = rnoCommonService.saveUserConfig(account, cityId);
		}
		String result = "{'flag':" + flag +"}";
		writeToClient(result);
	}
	/**
	 * 
	 * @title 通过城市id获取lte enodeb和小区信息，并以map形式保存
	 * @author chao.xj
	 * @date 2015-9-14下午2:57:22
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void getAllLteCellsByCityIdInMapForAjaxAction() {
		log.info("进入方法：getAllLteCellsByCityIdInMapForAjaxAction。cityId=" + cityId);
		
		Map<String, List<Map<String, Object>>> res = new HashMap<String, List<Map<String,Object>>>();
		List<Map<String, Object>> cells = rnoCommonService
				.getAllLteCellsInCity(cityId);
		log.info("city：" + cityId + "下有" + cells.size() + "个cell");
		// 整理
		String prebsc = "", curbsc = "";
		List<Map<String, Object>> line = null;
		StringBuilder buf = new StringBuilder();
		for (Map<String, Object> one : cells) {
			if (one == null) {
				continue;
			}
			if (one.get("ENGNAME") == null) {
				curbsc = "未知bsc";
			} else {
				curbsc = "" + one.get("ENGNAME");
			}
			if(res.containsKey(curbsc)){
				res.get(curbsc).add(one);
			}else{
				line = new ArrayList<Map<String, Object>>();
				line.add(one);
				res.put(curbsc, line);
			}
		}
		String result = gson.toJson(res);
		HttpTools.writeToClient(result);
		return;
	}
}
