package com.iscreate.op.action.rno;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.action.rno.model.G4MrDescQueryCond;
import com.iscreate.op.pojo.rno.RnoLteInterferCalcTask;
import com.iscreate.op.pojo.rno.RnoThreshold;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.rno.Rno4GPciService;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.client.api.JobClient;
import com.iscreate.op.service.rno.job.client.api.JobClientDelegate;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.FileTool;
import com.iscreate.op.service.rno.tool.HttpTools;
import com.iscreate.op.service.rno.tool.RenderTool;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.op.service.rno.vo.Grid;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.iscreate.plat.system.datasourcectl.DataSourceConst;
import com.iscreate.plat.system.datasourcectl.DataSourceContextHolder;

public class Rno4GPciAction  extends RnoCommonAction {
	private static Log log = LogFactory.getLog(Rno4GPciAction.class);
	private static Gson gson = new GsonBuilder().create();// 线程安全
	
	
	private String lastMonday;
	private String lastSunday;
	
	private Map<String, String> cond;

	//注入
	private Rno4GPciService rno4gPciService;
	
	//--4g Mr 描述信息
	private G4MrDescQueryCond g4MrDescQueryCond;
	
	private long cityId;
	private long jobId;
	private String lteCell;
	private String cell1;
	private String cell2;
	private String pci1;
	private String pci2;
	
	//任务消息
	private HashMap<String, Object> taskInfo;
	
	private String error;
	private InputStream exportInputStream;// 导出4g azimuth结果文件的输入流
	/*                  set/get方法开始                                                                                */
	
	public InputStream getExportInputStream() {
		return exportInputStream;
	}
	public void setExportInputStream(InputStream exportInputStream) {
		this.exportInputStream = exportInputStream;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public Map<String, Object> getTaskInfo() {
		return taskInfo;
	}
	public void setTaskInfo(HashMap<String, Object> taskInfo) {
		this.taskInfo = taskInfo;
	}
	public String getLastMonday() {
		return lastMonday;
	}
	public String getCell1() {
		return cell1;
	}
	public void setCell1(String cell1) {
		this.cell1 = cell1;
	}
	public String getCell2() {
		return cell2;
	}
	public void setCell2(String cell2) {
		this.cell2 = cell2;
	}
	public String getPci1() {
		return pci1;
	}
	public void setPci1(String pci1) {
		this.pci1 = pci1;
	}
	public String getPci2() {
		return pci2;
	}
	public void setPci2(String pci2) {
		this.pci2 = pci2;
	}
	public String getLteCell() {
		return lteCell;
	}
	public void setLteCell(String lteCell) {
		this.lteCell = lteCell;
	}
	public long getJobId() {
		return jobId;
	}
	public void setJobId(long jobId) {
		this.jobId = jobId;
	}
	public long getCityId() {
		return cityId;
	}
	public void setCityId(long cityId) {
		this.cityId = cityId;
	}
	public void setLastMonday(String lastMonday) {
		this.lastMonday = lastMonday;
	}
	public String getLastSunday() {
		return lastSunday;
	}
	public void setLastSunday(String lastSunday) {
		this.lastSunday = lastSunday;
	}
	public Map<String, String> getCond() {
		return cond;
	}
	public void setCond(Map<String, String> cond) {
		this.cond = cond;
	}
	public Rno4GPciService getRno4gPciService() {
		return rno4gPciService;
	}
	public void setRno4gPciService(Rno4GPciService rno4gPciService) {
		this.rno4gPciService = rno4gPciService;
	}
	public G4MrDescQueryCond getG4MrDescQueryCond() {
		return g4MrDescQueryCond;
	}
	public void setG4MrDescQueryCond(G4MrDescQueryCond g4MrDescQueryCond) {
		this.g4MrDescQueryCond = g4MrDescQueryCond;
	}
	/*                 set/get方法结束                                                                                */
	/**
	 * 初始化pci分析页面
	 * @title 
	 * @return
	 * @author peng.jm
	 * @date 2015年4月13日18:32:31
	 * @company 怡创科技
	 */
	public String initPciAnalysisPageAction() {
		initAreaList();
		//Calendar cal = Calendar.getInstance();
	    //cal.add(Calendar.DATE,-5);
	    //preFiveDayTime = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(cal.getTime());
		return "success";
	}
	/**
	 * 获取最近十次lte干扰矩阵信息
	 * @title 
	 * @return
	 * @author peng.jm
	 * @date 2015年4月13日18:32:31
	 * @company 怡创科技
	 */
	public void getLatelyLteMatrixByCityIdForAjaxAction() {
		log.info("获取最近十次lte干扰矩阵信息. cityId=" + cityId);
		List<Map<String, Object>> result = rno4gPciService
				.getLatelyLteMatrixByCityId(cityId);
		HttpTools.writeToClient(gson.toJson(result));
	}
	/**
	 * 获取干扰矩阵数据
	 * @title 
	 * @return
	 * @author peng.jm
	 * @date 2015年4月13日18:32:31
	 * @company 怡创科技
	 */
	public void getLteMatrixByIdForAjaxAction() {
		log.info("获取最近十次lte干扰矩阵信息. jobId=" + jobId);
		//获取任务信息
		List<Map<String,Object>> res = rno4gPciService.getLteMatrixById(jobId);
		Map<String,String> result = new HashMap<String,String>();
		
		if(res.size() > 0) {
			List<Map<String,String>> matrixData = new ArrayList<Map<String,String>>();
			
			String matrixPath = res.get(0).get("FILE_PATH").toString();
			String matrixName = res.get(0).get("FILE_NAME").toString();
			File matrixfile = FileTool.getFile(matrixPath+"/"+matrixName);
			if(matrixfile == null) {
				log.debug("文件不存在:"+matrixPath+"/"+matrixName);
				result.put("flag", "false");
				result.put("val", "");
				HttpTools.writeToClient(gson.toJson(result));
				return;
			}
			//读取文件
			BufferedReader br = null;
			Map<String,String> one = null;
			try {
				br = new BufferedReader(new InputStreamReader(
						new FileInputStream(matrixfile)));
			} catch (FileNotFoundException e1) {
				log.debug("文件不存在:"+matrixPath+"/"+matrixName);
				result.put("flag", "false");
				result.put("val", "");
				HttpTools.writeToClient(gson.toJson(result));
				return;
			}
			try {
				String line="";
				while((line = br.readLine()) != null) {
					String[] vs = line.split("#");
					one = new HashMap<String, String>();
					one.put("CELL", vs[0]);
					one.put("NCELL", vs[1]);
					one.put("RELA_VAL", vs[2]);
					one.put("CELL_PCI", vs[3]);
					one.put("NCELL_PCI", vs[4]);
					matrixData.add(one);
				}
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
				log.debug("文件不存在:"+matrixPath+"/"+matrixName);
				result.put("flag", "false");
				result.put("val", "");
				HttpTools.writeToClient(gson.toJson(result));
				return;
			}
			
			//传回页面
			result.put("flag", "true");
			result.put("val", gson.toJson(matrixData));
			HttpTools.writeToClient(gson.toJson(result));
		} else {
			result.put("flag", "false");
			result.put("val", "");
			HttpTools.writeToClient(gson.toJson(result));
		}
	}
	
	/**
	 * 获取同站lte小区和pci
	 * @title 
	 * @return
	 * @author peng.jm
	 * @date 2015年4月13日18:32:31
	 * @company 怡创科技
	 */
	public void getSameStationCellsByLteCellIdForAjaxAction() {
		log.info("getSameStationCellsByLteCellIdForAjaxAction. lteCell=" + lteCell);
		List<Map<String, String>> res = rno4gPciService
				.getSameStationCellsByLteCellId(lteCell);
		HttpTools.writeToClient(gson.toJson(res));
	}
	/**
	 * 转换lte小区与某同站小区的pci
	 * @title 
	 * @return
	 * @author peng.jm
	 * @date 2015年4月13日18:32:31
	 * @company 怡创科技
	 */
	public void changeLteCellPciForAjaxAction() {
		log.info("changeLteCellPciForAjaxAction. cell1=" + cell1
				+ ",pci1="+pci1+",cell2="+cell2+",pci2="+pci2);
		boolean result = rno4gPciService
				.changeLteCellPci(cell1,pci1,cell2,pci2);
		Map<String,Boolean> res = new HashMap<String, Boolean>();
		res.put("flag", result);
		HttpTools.writeToClient(gson.toJson(res));
	}
	/**
	 * 
	 * @title 初始化4G干扰矩阵管理页面
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15上午9:48:28
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public String init4GInterferMartixManageAction() {
		initAreaList();// 加载区域相关信息
		return "success";
	}
	/**
	 * 
	 * @title 初始化新增4G干扰矩阵页面
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15上午9:52:12
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public String init4GInterferMartixAddForAjaxAction() {
		
		initAreaList();// 加载区域相关信息
		
		Calendar cal = Calendar.getInstance();
		//n为推迟的周数，1本周，-1向前推迟一周，2下周，依次类推
		int n = -1;
		cal.add(Calendar.DATE, n*7);
		cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		lastMonday = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(cal.getTime());
		//System.out.println(monday);
		  
		n = 1;
		cal.add(Calendar.DATE, n*7);
		cal.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
		lastSunday = new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(cal.getTime());
		//System.out.println(sunday);
		
		return "success";
	}
	/**
	 * 
	 * @title 分页查询4g干扰矩阵信息
	 * @author chao.xj
	 * @date 2015-4-15上午10:58:16
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void query4GInterferMartixByPageForAjaxAction() {
		log.info("进入方法query4GInterferMartixByPageForAjaxAction. page=" + page
			+ ",cond=" + cond);
		
		Map<String, Object> result = new HashMap<String, Object>();
		if (page == null) {
			log.error("方法query4GInterferMartixByPageForAjaxAction的page参数为空！");
			HttpTools.writeToClient(gson.toJson(result));
			return;
		}

		Page newPage = page.copy();
		List<Map<String, Object>> res = rno4gPciService.query4GInterferMartixByPage(cond, newPage);

		log.info("计算以后，page=" + newPage);
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		result.put("page", newPage);
		result.put("data", res);

		HttpTools.writeToClient(gson.toJson(result));
	}
	/**
	 * 
	 * @title 分页加载MR数据
	 * @author chao.xj
	 * @date 2015-4-15上午11:23:20
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void queryMrDataByPageForAjaxAction() {
		log.info("进入方法queryMrDataByPageForAjaxAction. page=" + page
			+ ",cond=" + cond);
		if (cond == null || cond.isEmpty()) {
			log.info("未传入查询条件");
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		if (page == null) {
			log.error("方法queryMrDataByPageForAjaxAction的page参数为空！");
			HttpTools.writeToClient(gson.toJson(result));
			return;
		}

		Page newPage = page.copy();
		
		long cityId = Long.parseLong(cond.get("cityId").toString());
		String startTime = cond.get("begTime").toString();
		String endTime = cond.get("endTime").toString();
		
		g4MrDescQueryCond = new G4MrDescQueryCond();
		g4MrDescQueryCond.setCityId(cityId);
		g4MrDescQueryCond.setMeaBegTime(startTime);
		g4MrDescQueryCond.setMeaEndTime(endTime);
		List<Map<String, String>> res = rno4gPciService.queryMrDataFromHbaseByPage(g4MrDescQueryCond, newPage);

		log.info("计算以后，page=" + newPage);
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		result.put("page", newPage);
		result.put("data", res);

		HttpTools.writeToClient(gson.toJson(result));
	}
	/**
	 * 
	 * @title 检查这周是否计算过4g pci干扰矩阵
	 * @author chao.xj
	 * @date 2015-4-15下午2:02:05
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void isExisted4GInterMartixThisWeekAction() {
		log.info("进入方法isExisted4GInterMartixThisWeekAction. cond=" + cond);
		if (cond == null || cond.isEmpty()) {
			log.info("未传入查询条件");
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		long areaId = Long.parseLong(cond.get("cityId").toString());
		Calendar cal = Calendar.getInstance();
		//获取这个周一
		cal.add(Calendar.DATE, 0*7);
		cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		String thisMonday = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(cal.getTime());
		//获取今天
	    cal = Calendar.getInstance();
	    String today = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
		//查询结果
		List<Map<String, Object>> res = rno4gPciService.check4GInterMartixThisWeek(areaId, thisMonday, today);
		//转为星期命名
		String desc = "";
		boolean flag = false;
		if(res != null && res.size() > 0) {
			String createTime = res.get(0).get("CREATE_DATE").toString();
			String timeArray[] = createTime.split(" ");
			Date date = null;
			try {
				date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createTime);
			} catch (ParseException e) {
				e.printStackTrace();
			} 
			cal = Calendar.getInstance();
			cal.setTime(date);
			int weekNum = cal.get(Calendar.DAY_OF_WEEK);
			
			switch (weekNum) {
				case 1:
					desc = "本周周日 " + timeArray[1] + " 曾经计算过干扰矩阵";
					flag = true;
					break;
				case 2:
					desc = "本周周一 " + timeArray[1] + " 曾经计算过干扰矩阵";
					flag = true;
					break;
				case 3:
					desc = "本周周二 " + timeArray[1] + " 曾经计算过干扰矩阵";
					flag = true;
					break;
				case 4:
					desc = "本周周三 " + timeArray[1] + " 曾经计算过干扰矩阵";
					flag = true;
					break;
				case 5:
					desc = "本周周四 " + timeArray[1] + " 曾经计算过干扰矩阵";
					flag = true;
					break;
				case 6:
					desc = "本周周五 " + timeArray[1] + " 曾经计算过干扰矩阵";
					flag = true;
					break;
				case 7:
					desc = "本周周六 " + timeArray[1] + " 曾经计算过干扰矩阵";
					flag = true;
					break;
				default:
					break;
			}
		}
		
		result.put("flag", flag);
		result.put("desc", desc);
		
		HttpTools.writeToClient(gson.toJson(result));
	}
	/**
	 * 
	 * @title 新增4g pci  干扰矩阵
	 * @author chao.xj
	 * @date 2015-4-15下午3:09:45
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void add4GInterMartixByMrForAjaxAction() {
		log.info("进入方法add4GInterMartixByMrForAjaxAction。cond=" + cond);
		if (cond == null || cond.isEmpty()) {
			log.info("未传入查询条件");
		}
//		long s = System.currentTimeMillis();
		Calendar cal = Calendar.getInstance();
		//获取上周周一
		cal.add(Calendar.DATE, -1 * 7);
		cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		lastMonday = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(cal.getTime());
		//获取上周周日
		cal.add(Calendar.DATE, 1 * 7);
		cal.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
		lastSunday = new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(cal.getTime());
		
		DateUtil dateUtil = new DateUtil();
		Date startTime = dateUtil.parseDateArbitrary(cond.get("begTime").toString());
		Date endTime = dateUtil.parseDateArbitrary(cond.get("endTime").toString());
		Date lastMon = dateUtil.parseDateArbitrary(lastMonday);
		Date lastSun = dateUtil.parseDateArbitrary(lastSunday);
		
		boolean isDateRight = false;
		boolean isMrExist = false;
		//判断日期是否符合要求
		if((endTime.after(startTime) || endTime.equals(startTime)) 
				&& (lastSun.after(endTime) || lastSun.equals(endTime))
				&& (lastSun.after(startTime) || lastSun.equals(startTime))
				&& (startTime.after(lastMon) || startTime.equals(lastMon))
				&& (endTime.after(lastMon) || endTime.equals(lastMon))) {
			
			isDateRight = true;
		}
		//判断日期范围是否存在MR数据
		long mrCnt = rno4gPciService.queryMrDataCountByCond(cond);
		if(mrCnt > 0) {
			isMrExist = true;
		}

		
		if(/*isDateRight && */isMrExist /*&& !isCalculating*/) {
			//提交mr干扰矩阵计算任务
			String account = (String) SessionService.getInstance().getValueByKey("userId");
			rno4gPciService.add4GInterMartixByMr(cond, account);
		}
//		System.out.println("任务提交耗时："+((System.currentTimeMillis()-s)/1000));
		//测试
		isDateRight=true;  
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("isDateRight", isDateRight);
		result.put("isMrExist", isMrExist);
//		result.put("isCalculating", isCalculating);
		HttpTools.writeToClient(gson.toJson(result));
	}
	/**
	 * 
	 * @title 初始化4G azimuth方位角计算页面
	 * @return
	 * @author chao.xj
	 * @date 2015-4-29上午10:08:20
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public String init4GAzimuthCalcPageAction() {
		
		initAreaList();// 加载区域相关信息
		return "success";
	}
	/**
	 * 
	 * @title 提交4g方位角计算任务
	 * @author chao.xj
	 * @date 2015-4-29下午3:28:07
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void submit4GAzimuthCalcTaskAction() {
		log.info("进入：submit4GAzimuthCalcTaskAction");
		String account = (String) SessionService.getInstance().getValueByKey(
					"userId");
		log.info("提交者=" + account);
		System.out.println("taskInfo:"+taskInfo);
		System.out.println("cityId:"+taskInfo.get("CITYID").toString());
		Map<String, Object> res =null;
		if (taskInfo != null && taskInfo.size()!=0) {
			
			res = rno4gPciService.submit4GAzimuthCalcTask(account, taskInfo);
		}
		String result = gson.toJson(res);
		log.debug("退出submit4GAzimuthCalcTaskAction result="+result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 
	 * @title 查询4g方位角计算任务
	 * @author chao.xj
	 * @date 2015-4-29下午3:43:20
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void query4GAzimuthCalcTaskByPageForAjaxAction() {
		log.info("进入：query4GAzimuthCalcTaskByPageForAjaxAction。cond=" + cond
				+ ",page=" + page);
		String account = (String) SessionService.getInstance().getValueByKey("userId");
		if (page == null) {
			log.error("方法query4GAzimuthCalcTaskByPageForAjaxAction的page参数为空！");
			HttpTools.writeToClient("[]");
			return;
		}

		Page newPage = page.copy();
		List<Map<String, Object>> pciTasks = rno4gPciService.query4GAzimuthCalcTaskByPage(cond, newPage, account);
				
		String res = gson.toJson(pciTasks);

		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String result = "{'page':" + pstr + ",'data':" + res + "}";
		log.info("退出query4GAzimuthCalcTaskByPageForAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 
	 * @title 终止4g方位角计算任务
	 * @author chao.xj
	 * @date 2015-4-29下午3:51:23
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void stop4GAzimuthJobByJobIdForAjaxAction() {
		log.info("进入方法：stop4GAzimuthJobByJobIdForAjaxAction。jobId=" + jobId 
				);
		String account = (String) SessionService.getInstance().getValueByKey(
				"userId");
		Map<String, Object> res = new HashMap<String, Object>();
		boolean flag=true;
		JobClient jobClient;
		JobProfile job=null;
		try {
			// 停止runnable的job
			jobClient = JobClientDelegate.getJobClient();
			job = new JobProfile(jobId);
			log.debug("kill前job状态:" + job.getJobStateStr());
			jobClient.killJob(job, account, "用户主动停止");
			log.debug("kill后job状态:" + job.getJobStateStr());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(job + "该job停止失败!");
			flag = false;
			e.printStackTrace();
		}
		res.put("flag", flag);
		String result = gson.toJson(res);
		HttpTools.writeToClient(result);
	}
	/**
	 * 
	 * @title 下载4g方位角计算任务结果文件
	 * @return
	 * @author chao.xj
	 * @date 2015-4-29下午3:51:23
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public String download4GAzimuthFileAction() {
		log.info("下载4g方位角计算结果文件， jobId=" + jobId 
				);
		// 获取任务的信息。
		Connection connection = DataSourceConn.initInstance().getConnection();
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String sql = "select * from RNO_4G_AZIMUTH_JOB where job_id="+jobId;
		List<Map<String,Object>> pciTaksInfo = RnoHelper.commonQuery(stmt, sql);
		
		if(pciTaksInfo.size() <= 0) {
			error="不存在该"+jobId+"任务信息";
			return "fail";
		}
		//区域ID
		long cityId = Long.parseLong(pciTaksInfo.get(0).get("CITY_ID").toString());
		Map<String, List<String>> cellIdToCessInfo=rno4gPciService.getLteCellInfoByCellId(stmt, cityId);
		if(cellIdToCessInfo.size() <= 0) {
			error="该区域"+cityId+"不存在系统小区信息";
			return "fail";
		}
		//下载的4g方位角计算文件名称
		String dlFileName = pciTaksInfo.get(0).get("DL_FILE_NAME").toString();
		//下载的4g方位角计算文件全路径
		DateUtil dateUtil = new DateUtil();
		Calendar calendar = new GregorianCalendar();
		Date createDate = dateUtil.parseDateArbitrary(
				pciTaksInfo.get(0).get("CREATE_TIME").toString());
		calendar.setTime(createDate);
		String path = ServletActionContext.getServletContext().getRealPath(
				"/op/rno/ana_result/");
		String dlFileRealdir = path+"/"+calendar.get(Calendar.YEAR)+"/"
				+ (calendar.get(Calendar.MONTH) + 1) + "/";
		String dlFileRealPath = dlFileRealdir + dlFileName;
		File realdir=new File(dlFileRealdir);
		if(!realdir.exists()){
			realdir.mkdirs();
		}
		File realfile=new File(dlFileRealPath);
		if(!realfile.exists()){
			try {
				realfile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//4g方位角计算数据源文件的hdfs路径
		String resFilePath = pciTaksInfo.get(0).get("RESULT_DIR").toString();
		//4g方位角计算数据源文件的名称,源数据文件有两个，一个是current后缀，一个是backup后缀
		/*String currentFileName = pciTaksInfo.get(0).get("RD_FILE_NAME").toString() + ".current";
		String backupFileName = pciTaksInfo.get(0).get("RD_FILE_NAME").toString() + ".backup";*/
		String currentFileName = pciTaksInfo.get(0).get("RD_FILE_NAME").toString();
		File currentFile = FileTool.getFile(resFilePath+"/"+currentFileName);
//		File backupFile = FileTool.getFile(resFilePath+"/"+backupFileName);
		
		//两个源文件不存在，不能提供下载
		if(currentFile == null/* && backupFile == null*/) {
			log.info("4g方位角计算的源数据current文件不存在！currentFileName="+resFilePath+"/"+currentFileName);
//			log.info("4g方位角计算的源数据backup文件不存在！backupFileName="+resFilePath+"/"+backupFileName);
			error="4g方位角计算的源数据backup文件不存在！currentFileName="+resFilePath+"/"+currentFileName;
			return "fail";
		}
		
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null; //用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
		double totInterVal = 0;
		int totDataNum = 0;
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		Map<String,Object> one = null;
		String isFinished = "";
		
		String result = "success";
		String cellId="";
		String cellName="";
		String oldPci="";
		String earfcn="";
		String azimuth="";
		List<String> cells=null;
		String line="";
		try {
			if(currentFile != null){
				//创建字节输入流
				fis = new FileInputStream(currentFile);
				// 从文件系统中的某个文件中获取字节(注意中文)
			    isr = new InputStreamReader(fis,"utf-8");// InputStreamReader 是字节流通向字符流的桥梁,
			    br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
			   while ((line = br.readLine()) != null) {
				   one = new HashMap<String, Object>();
					//小区名，小区ID,现网方位角,计算方位角,两角之差
					one.put("cellName", line.split("#")[0]);
					one.put("cellId", line.split("#")[1]);
					one.put("oldAzimuth", line.split("#")[2]);
					one.put("newAzimuth", line.split("#")[3]);
					one.put("azimuthDiff",line.split("#")[4]);
					res.add(one);
			   }
				/*//创建一个长度为1024
		        byte[] bbuf = new byte[1024];
				//用于保存实际读取的字节数
				int hasRead = 0;
				//使用循环来重复取数据的过程
				while((hasRead = fis.read(bbuf))>0)
				{
					//取出数据中(字节),将字节数组转成字符串输入
					line=new String(bbuf,0,hasRead,"utf-8");
					one = new HashMap<String, Object>();
					//小区名，小区ID,现网方位角,计算方位角,两角之差
					one.put("cellName", line.split("#")[0]);
					one.put("cellId", line.split("#")[1]);
					one.put("oldAzimuth", line.split("#")[2]);
					one.put("newAzimuth", line.split("#")[3]);
					one.put("azimuthDiff",line.split("#")[4]);
					res.add(one);
				}*/
			} 
		}catch(Exception e) {
			e.printStackTrace();
			log.info("获取4g方位角计算结果源文件中，读取数据出错");
			error="获取4g方位角计算结果源文件中，读取数据出错";
			result = "error"; 
		}finally{
			try {
				fis.close();
				isr.close();
				br.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
			//可以生成excel结果文件
			try {
				setFileName(new String(dlFileName.getBytes(),"iso-8859-1"));
				File f = new File(dlFileRealPath);
				if (!f.exists()) {
					log.error("4g方位角计算结果文件不存在！");
					error="4g方位角计算结果文件不存在！,文件路径："+dlFileRealPath;
					return "fail";
				} else {
					try {
						exportInputStream = new FileInputStream(dlFileRealPath);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						result = "error"; 
						error=exportInputStream+"获取文件流异常,文件路径："+dlFileRealPath;
					}
				}
				boolean flag = create4GAzimuthExcelFile(dlFileRealPath,res);
				if(!flag){
					log.error("4g方位角计算结果写入excel文件失败！");
					error="4g方位角计算结果写入excel文件失败！,文件路径："+dlFileRealPath;
					return "fail";
				}
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		if(result.equals("error")){
			return "fail";
		}
		return result;
	}
	/**
	 * 
	 * @title 在项目的数据目录创建4g方位角计算任务结果excel文件
	 * @param fileRealPath
	 * @param res
	 * @return
	 * @author chao.xj
	 * @date 2015-4-29下午4:28:54
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public boolean create4GAzimuthExcelFile(String fileRealPath, List<Map<String,Object>> res) {
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileRealPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		
		Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet();
		Row row;
		Cell cell;
		row = sheet.createRow((short)0);
		cell = row.createCell(0);
		cell.setCellValue("小区名称");
		cell = row.createCell(1);
		cell.setCellValue("小区标识");
		cell = row.createCell(2);
		cell.setCellValue("现网方向角");
		cell = row.createCell(3);
		cell.setCellValue("计算方向角");
		cell = row.createCell(4);
		cell.setCellValue("方向角差值");
        //最终写入文件
        try {
        	for (int i = 0; i < res.size(); i++) {
    			row = sheet.createRow((short)i + 1);
    			cell = row.createCell(0);
    			cell.setCellValue(res.get(i).get("cellName").toString());
    			cell = row.createCell(1);
    			cell.setCellValue(res.get(i).get("cellId").toString());
    			cell = row.createCell(2);
    			cell.setCellValue(res.get(i).get("oldAzimuth").toString());
    			cell = row.createCell(3);
    			cell.setCellValue(res.get(i).get("newAzimuth").toString());
    			cell = row.createCell(4);
    			cell.setCellValue(res.get(i).get("azimuthDiff").toString());
    		}
			workbook.write(fos);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
        try {
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
        return true;
	}
	/**
	 * 
	 * @title 下载4g矩阵计算任务结果文件
	 * @return
	 * @author chao.xj
	 * @date 2015-4-29下午3:51:23
	 * @company 怡创科技
	 * @version 2.0.1
	 */
/*	public String download4GMatrixFileAction() {
		log.info("下载4g矩阵计算结果文件， jobId=" + jobId 
				);
		// 获取任务的信息。
		Connection connection = DataSourceConn.initInstance().getConnection();
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String sql = "select * from rno_4g_inter_martix_rec where job_id="+jobId;
		List<Map<String,Object>> pciTaksInfo = RnoHelper.commonQuery(stmt, sql);
		
		if(pciTaksInfo.size() <= 0) {
			error="不存在该"+jobId+"任务信息";
			return "fail";
		}
		//区域ID
		long cityId = Long.parseLong(pciTaksInfo.get(0).get("CITY_ID").toString());
		Map<String, List<String>> cellIdToCessInfo=rno4gPciService.getLteCellInfoByCellId(stmt, cityId);
		if(cellIdToCessInfo.size() <= 0) {
			error="该区域"+cityId+"不存在系统小区信息";
			return "fail";
		}
		//下载的4g矩阵计算文件名称
		String dlFileName = jobId+"_4G矩阵结果表.csv";
		//下载的4g矩阵计算文件全路径
		DateUtil dateUtil = new DateUtil();
		Calendar calendar = new GregorianCalendar();
		Date createDate = dateUtil.parseDateArbitrary(
				pciTaksInfo.get(0).get("CREATE_DATE").toString());
		calendar.setTime(createDate);
		String path = ServletActionContext.getServletContext().getRealPath(
				"/op/rno/ana_result/");
		String dlFileRealdir = path+"/"+calendar.get(Calendar.YEAR)+"/"
				+ (calendar.get(Calendar.MONTH) + 1) + "/";
		String dlFileRealPath = dlFileRealdir + dlFileName;
		File realdir=new File(dlFileRealdir);
		if(!realdir.exists()){
			realdir.mkdirs();
		}
		File realfile=new File(dlFileRealPath);
		if(!realfile.exists()){
			try {
				realfile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//4g矩阵计算数据源文件的hdfs路径
		String resFilePath = pciTaksInfo.get(0).get("FILE_PATH").toString();
		String currentFileName = pciTaksInfo.get(0).get("FILE_NAME").toString();
		File currentFile = FileTool.getFile(resFilePath+"/"+currentFileName);
		
		//源文件不存在，不能提供下载
		if(currentFile == null) {
			log.info("4g矩阵计算的源数据current文件不存在！currentFileName="+resFilePath+"/"+currentFileName);
			error="4g矩阵计算的源数据backup文件不存在！currentFileName="+resFilePath+"/"+currentFileName;
			return "fail";
		}
		
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null; //用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
		double totInterVal = 0;
		int totDataNum = 0;
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		Map<String,Object> one = null;
		String isFinished = "";
		
		String result = "success";
		String cellId="";
		String cellName="";
		String oldPci="";
		String earfcn="";
		String azimuth="";
		List<String> cells=null;
		String line="";
		try {
			if(currentFile != null){
				//创建字节输入流
				fis = new FileInputStream(currentFile);
				// 从文件系统中的某个文件中获取字节(注意中文)
			    isr = new InputStreamReader(fis,"utf-8");// InputStreamReader 是字节流通向字符流的桥梁,
			    br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
			   while ((line = br.readLine()) != null) {
				   one = new HashMap<String, Object>();
					//line=cell+"#"+ncellId+"#"+cosi+"#"+cellPci+"#"+ncellPci+"#"+cells+"#"+cellBcch+"#"+ncellBcch;
					one.put("cellId", line.split("#")[0]);
					one.put("ncellId", line.split("#")[1]);
					one.put("cosi", line.split("#")[2]);
					one.put("cellPci", line.split("#")[3]);
					one.put("ncellPci",line.split("#")[4]);
					one.put("cells",line.split("#")[5].replace(",", "_"));
					one.put("cellBcch",line.split("#")[6]);
					one.put("ncellBcch",line.split("#")[7]);
					res.add(one);
			   }
			} 
		}catch(Exception e) {
			e.printStackTrace();
			log.info("获取4g矩阵计算结果源文件中，读取数据出错");
			error="获取4g矩阵计算结果源文件中，读取数据出错";
			result = "error"; 
		}finally{
			try {
				fis.close();
				isr.close();
				br.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
			//可以生成excel结果文件
			try {
				setFileName(new String(dlFileName.getBytes(),"iso-8859-1"));
				File f = new File(dlFileRealPath);
				if (!f.exists()) {
					log.error("4g矩阵计算结果文件不存在！");
					error="4g矩阵计算结果文件不存在！,文件路径："+dlFileRealPath;
					return "fail";
				} else {
					try {
						exportInputStream = new FileInputStream(dlFileRealPath);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						result = "error"; 
						error=exportInputStream+"获取文件流异常,文件路径："+dlFileRealPath;
					}
				}
				boolean flag = create4GMatrixExcelFile(dlFileRealPath,res);
				if(!flag){
					log.error("4g矩阵计算结果写入excel文件失败！");
					error="4g矩阵计算结果写入excel文件失败！,文件路径："+dlFileRealPath;
					return "fail";
				}
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		if(result.equals("error")){
			return "fail";
		}
		return result;
	}*/
	/**
	 * 
	 * @title 下载4g矩阵计算任务结果文件
	 * @return
	 * @author chao.xj
	 * @date 2015-4-29下午3:51:23
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public String download4GMatrixFileAction() {
		log.info("下载4g矩阵计算结果文件， jobId=" + jobId 
				);
		// 获取任务的信息。
		DataSourceContextHolder.setDataSourceType(DataSourceConst.rnoDS);
		Connection connection = DataSourceConn.initInstance().getConnection();
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String sql = "select * from rno_4g_inter_martix_rec where job_id="+jobId;
		List<Map<String,Object>> pciTaksInfo = RnoHelper.commonQuery(stmt, sql);
		
		if(pciTaksInfo.size() <= 0) {
			error="不存在该"+jobId+"任务信息";
			return "fail";
		}
		//区域ID
		long cityId = Long.parseLong(pciTaksInfo.get(0).get("CITY_ID").toString());
		/*Map<String, List<String>> cellIdToCessInfo=rno4gPciService.getLteCellInfoByCellId(stmt, cityId);
		if(cellIdToCessInfo.size() <= 0) {
			error="该区域"+cityId+"不存在系统小区信息";
			return "fail";
		}*/
		//下载的4g矩阵计算文件名称
		String dlFileName = jobId+"_4G矩阵结果表.csv";
		//下载的4g矩阵计算文件全路径
		DateUtil dateUtil = new DateUtil();
		Calendar calendar = new GregorianCalendar();
		Date createDate = dateUtil.parseDateArbitrary(
				pciTaksInfo.get(0).get("CREATE_DATE").toString());
		calendar.setTime(createDate);
		String path = ServletActionContext.getServletContext().getRealPath(
				"/op/rno/ana_result/");
		String dlFileRealdir = path+"/"+calendar.get(Calendar.YEAR)+"/"
				+ (calendar.get(Calendar.MONTH) + 1) + "/";
		String dlFileRealPath = dlFileRealdir + dlFileName;
		File realdir=new File(dlFileRealdir);
		if(!realdir.exists()){
			realdir.mkdirs();
		}
		File realfile=new File(dlFileRealPath);
		if(!realfile.exists()){
			try {
				realfile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//4g矩阵计算数据源文件的hdfs路径
		String resFilePath = pciTaksInfo.get(0).get("FILE_PATH").toString();
		String currentFileName = pciTaksInfo.get(0).get("FILE_NAME").toString();
		File currentFile = FileTool.getFile(resFilePath+"/"+currentFileName);
		
		//源文件不存在，不能提供下载
		if(currentFile == null) {
			log.info("4g矩阵计算的源数据current文件不存在！currentFileName="+resFilePath+"/"+currentFileName);
			error="4g矩阵计算的源数据backup文件不存在！currentFileName="+resFilePath+"/"+currentFileName;
			return "fail";
		}
		
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null; //用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
		
		String result = "success";
		String line="";
		FileOutputStream fos = null;
		Writer out =null;
		try {
			setFileName(new String(dlFileName.getBytes(),"iso-8859-1"));
			File f = new File(dlFileRealPath);
			if (!f.exists()) {
				log.error("4g矩阵计算结果文件不存在！");
				error="4g矩阵计算结果文件不存在！,文件路径："+dlFileRealPath;
				return "fail";
			}else {
				try {
					exportInputStream = new FileInputStream(dlFileRealPath);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					result = "error"; 
					error=exportInputStream+"获取文件流异常,文件路径："+dlFileRealPath;
				}
			} 
			
			fos = new FileOutputStream(dlFileRealPath);
			out = new OutputStreamWriter(fos,"GBK");
			//标题头
			line="小区标识,"+"邻区标识,"+"关联度,"+"小区频点,"+"小区PCI,"+"邻区频点,"+"邻区PCI,"+"同站邻区"+"\n";
	        out.write(line);
	        
			if(currentFile != null){
				//创建字节输入流
				fis = new FileInputStream(currentFile);
				// 从文件系统中的某个文件中获取字节(注意中文)
			    isr = new InputStreamReader(fis,"utf-8");// InputStreamReader 是字节流通向字符流的桥梁,
			    br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
			   while ((line = br.readLine()) != null) {
//				   one = new HashMap<String, Object>();
					//line=cell+"#"+ncellId+"#"+cosi+"#"+cellPci+"#"+ncellPci+"#"+cells+"#"+cellBcch+"#"+ncellBcch;
					/*one.put("cellId", line.split("#")[0]);
					one.put("ncellId", line.split("#")[1]);
					one.put("cosi", line.split("#")[2]);
					one.put("cellPci", line.split("#")[3]);
					one.put("ncellPci",line.split("#")[4]);
					one.put("cells",line.split("#")[5].replace(",", "_"));
					one.put("cellBcch",line.split("#")[6]);
					one.put("ncellBcch",line.split("#")[7]);
					res.add(one);*/
//				   line=cellId+","+ncellId+","+cosi+","+cellBcch+","+cellPci+","+ncellBcch+","+ncellPci+","+cells+"\n";
//	        		fos.write(Bytes.toBytes(line));
	        		out.write(line.split("#")[0]); 
	        		out.write(","); 
	        		out.write(line.split("#")[1]); 
	        		out.write(","); 
	        		out.write(line.split("#")[2]); 
	        		out.write(","); 
	        		out.write(line.split("#")[6]); 
	        		out.write(","); 
	        		out.write(line.split("#")[3]); 
	        		out.write(","); 
	        		out.write(line.split("#")[7]); 
	        		out.write(","); 
	        		out.write(line.split("#")[4]); 
	        		out.write(","); 
	        		out.write(line.split("#")[5].replace(",", "_")); 
	        		out.write("\n"); 
				   
			   }
			} 
		}catch(Exception e) {
			e.printStackTrace();
			log.info("获取4g矩阵计算结果源文件中  ，读取数据出错");
			error="获取4g矩阵计算结果源文件中，读取数据出错";
			result = "error"; 
		}finally{
			try {
				fis.close();
				isr.close();
				br.close();
				
				out.flush();
	        	out.close();
				fos.flush();
				fos.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
			
			
		}
		if(result.equals("error")){
			return "fail";
		}
		return result;
	}
	/**
	 * 
	 * @title 在项目的数据目录创建4g矩阵计算任务结果excel文件
	 * @param fileRealPath
	 * @param res
	 * @return
	 * @author chao.xj
	 * @date 2015-4-29下午4:28:54
	 * @company 怡创科技
	 * @version 2.0.1
	 */
/*	public boolean create4GMatrixExcelFile(String fileRealPath, List<Map<String,Object>> res) {
		
		FileOutputStream fos = null;
		Writer out =null;
		try {
			fos = new FileOutputStream(fileRealPath);
			out = new OutputStreamWriter(fos,"GBK");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}catch (UnsupportedEncodingException e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		
		Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet();
		Row row;
		Cell cell;
		row = sheet.createRow((short)0);
		cell = row.createCell(0);
		cell.setCellValue("小区标识");
		cell = row.createCell(1);
		cell.setCellValue("邻区标识");
		cell = row.createCell(2);
		cell.setCellValue("关联度");
		cell = row.createCell(3);
		cell.setCellValue("小区频点");
		cell = row.createCell(4);
		cell.setCellValue("小区PCI");
		cell = row.createCell(5);
		cell.setCellValue("邻区频点");
		cell = row.createCell(6);
		cell.setCellValue("邻区PCI");
		cell = row.createCell(7);
		cell.setCellValue("同站邻区");
        //最终写入文件
//		line=cellId+"#"+ncellId+"#"+cosi+"#"+cellPci+"#"+ncellPci;
		
		String cellId,ncellId,cosi,cellPci,ncellPci,cellBcch,ncellBcch,cells,line;
		line="小区标识,"+"邻区标识,"+"关联度,"+"小区频点,"+"小区PCI,"+"邻区频点,"+"邻区PCI,"+"同站邻区"+"\n";
		
        try {
        	for (int i = 0; i < res.size(); i++) {
    			row = sheet.createRow((short)i + 1);
    			cell = row.createCell(0);
    			cell.setCellValue(res.get(i).get("cellId").toString());
    			cell = row.createCell(1);
    			cell.setCellValue(res.get(i).get("ncellId").toString());
    			cell = row.createCell(2);
    			cell.setCellValue(res.get(i).get("cosi").toString());
    			cell = row.createCell(3);
    			cell.setCellValue(res.get(i).get("cellBcch").toString());
    			cell = row.createCell(4);
    			cell.setCellValue(res.get(i).get("cellPci").toString());
    			cell = row.createCell(5);
    			cell.setCellValue(res.get(i).get("ncellBcch").toString());
    			cell = row.createCell(6);
    			cell.setCellValue(res.get(i).get("ncellPci").toString());
    			cell = row.createCell(7);
    			cell.setCellValue(res.get(i).get("cells").toString());
    		}
			workbook.write(fos);
//        	out.write(Bytes.toBytes(line));
        	out.write(line);
        	for (int i = 0; i < res.size(); i++) {
        		cellId=res.get(i).get("cellId").toString();
        		ncellId=res.get(i).get("ncellId").toString();
        		cosi=res.get(i).get("cosi").toString();
        		cellBcch=res.get(i).get("cellBcch").toString();
        		cellPci=res.get(i).get("cellPci").toString();
        		ncellBcch=res.get(i).get("ncellBcch").toString();
        		ncellPci=res.get(i).get("ncellPci").toString();
        		cells=res.get(i).get("cells").toString();
        		
        		line=cellId+","+ncellId+","+cosi+","+cellBcch+","+cellPci+","+ncellBcch+","+ncellPci+","+cells+"\n";
//        		fos.write(Bytes.toBytes(line));
        		out.write(line);
    		}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
        try {
        	out.flush();
        	out.close();
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
        return true;
	}*/
}
