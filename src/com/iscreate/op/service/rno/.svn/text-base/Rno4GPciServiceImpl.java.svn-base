package com.iscreate.op.service.rno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import oracle.jdbc.OracleConnection;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.jdbc.support.nativejdbc.C3P0NativeJdbcExtractor;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.G4MrDescQueryCond;
import com.iscreate.op.dao.rno.Rno4GPciDao;
import com.iscreate.op.pojo.rno.RnoThreshold;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.client.api.JobClient;
import com.iscreate.op.service.rno.job.client.api.JobClientDelegate;
import com.iscreate.op.service.rno.job.server.JobAddCallback;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.iscreate.plat.system.datasourcectl.DataSourceConst;
import com.iscreate.plat.system.datasourcectl.DataSourceContextHolder;

public class Rno4GPciServiceImpl implements Rno4GPciService {

	private static Log log = LogFactory.getLog(Rno4GPciServiceImpl.class);

	static Object lockObj = new Object();// 锁对象
	
	// ---注入----//
	private Rno4GPciDao rno4gPciDao;

	public Rno4GPciDao getRno4gPciDao() {
		return rno4gPciDao;
	}

	public void setRno4gPciDao(Rno4GPciDao rno4gPciDao) {
		this.rno4gPciDao = rno4gPciDao;
	}
	
	/**
	 * 
	 * @title 分页查询4g干扰矩阵信息
	 * @param condition
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15上午10:52:43
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> query4GInterferMartixByPage(
			Map<String, String> condition, Page page) {
		log.info("进入方法：query4GInterferMartixByPage。condition=" + condition
				+ ",page=" + page);
		if (page == null) {
			log.warn("方法：query4GInterferMartixByPage的参数：page 是空！无法查询!");
			return Collections.EMPTY_LIST;
		}
		long totalCnt = page.getTotalCnt();
		if (totalCnt < 0) {
			totalCnt = rno4gPciDao.get4GInterferMartixCount(condition);
			page.setTotalCnt((int) totalCnt);
		}
		int startIndex = page.calculateStart();
		int cnt = page.getPageSize();
		List<Map<String, Object>> res = rno4gPciDao.query4GInterferMartixByPage(condition, startIndex, cnt);
		return res;
	}
	/**
	 * 
	 * @title 分页获取Hbase的Mr数据描述表的数据
	 * @param cond
	 * @param newPage
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15上午11:35:13
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, String>> queryMrDataFromHbaseByPage(
			G4MrDescQueryCond cond, Page newPage){
		return rno4gPciDao.queryMrDataFromHbaseByPage(cond, newPage);
	}
	/**
	 * 
	 * @title 检查这周是否计算过4g pci干扰矩阵
	 * @param areaId
	 * @param thisMonday
	 * @param today
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15下午1:59:46
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> check4GInterMartixThisWeek(long areaId,
			String thisMonday, String today) {
		return rno4gPciDao.check4GInterMartixThisWeek(areaId, thisMonday, today);
	}
	/**
	 * 
	 * @title 新增4g pci 干扰矩阵
	 * @param condition
	 * @param account
	 * @author chao.xj
	 * @date 2015-4-15下午2:58:38
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void add4GInterMartixByMr(Map<String, String> condition,
			String account) {
		log.info("进入方法：add4GInterMartixByMr。condition=" + condition
				+ ", account=" + account);
//		long s = System.currentTimeMillis();
		// 创建主键
		final long martixRecId = rno4gPciDao
				.getNextSeqValue("SEQ_4G_INTER_MARTIX_REC");
		// 区域id
		final long cityId = Long.parseLong(condition.get("cityId").toString());
		// 创建日期
		Calendar cal = Calendar.getInstance();
		cal = Calendar.getInstance();
		final String createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(cal.getTime());
		// mr数据的开始测量日期
		final String startMeaDate = condition.get("begTime").toString();
		// mr数据的结束测量日期
		final String endMeaDate = condition.get("endTime").toString();
		// 数据量
		final long recordNum = rno4gPciDao.queryMrDataRecordsCount(
				cityId, startMeaDate, endMeaDate);
		//任务名taskName
		final String taskName = condition.get("taskName").toString();
//		System.out.println("统计数据量耗时："+((System.currentTimeMillis()-s)/1000));
//		s = System.currentTimeMillis();
		// 结果文件路径
		final String filePath = "hdfs:///rno_data/4gmatrix/" + cal.get(Calendar.YEAR) + "/"
				+ (cal.get(Calendar.MONTH) + 1);
		final String fileName=UUID.randomUUID().toString().replaceAll("-", "");
//		final String realDiskPath = ServletActionContext.getServletContext()
//				.getRealPath(filePath);
		// 创建job
		JobProfile job = new JobProfile();
		job.setAccount(account);
		job.setJobName("计算4gpci干扰矩阵");
		String jobType = condition.get("jobType").toString();
		log.debug("jobType=" + jobType);
		job.setJobType(jobType);
		job.setSubmitTime(new Date());
		job.setDescription("4g pci干扰矩阵job");

		JobClient jobClient=JobClientDelegate.getJobClient();
		jobClient.submitJob(job, new JobAddCallback<Boolean>() {
			@Override
			public Boolean doWhenJobAdded(JobProfile job) {
				// 获取jobId
				long jobId = job.getJobId();
				// 新建一个干扰矩阵计算任务
				Map<String, Object> interMartix = new HashMap<String, Object>();
				interMartix.put("martix_rec_id", martixRecId);
				interMartix.put("city_id", cityId);
				interMartix.put("create_date", createDate);
				interMartix.put("start_mea_date", startMeaDate);
				interMartix.put("end_mea_date", endMeaDate);
				interMartix.put("record_num", recordNum);
				interMartix.put("file_path", filePath);
				interMartix.put("type", "MR"); // “MR”表示干扰矩阵是通过计算MR数据得出
				interMartix.put("work_status", "排队中");
				interMartix.put("job_id", jobId); // 系统分配的工作id
				interMartix.put("status", "Y"); // 是否在系统中删除
				interMartix.put("fileName", fileName);//文件名
				interMartix.put("taskName", taskName);//任务名
				// 在数据库创建干扰矩阵计算任务
				boolean flag = rno4gPciDao.createMr4GInterMartixRec(interMartix);
				if (!flag) {
					log.error(martixRecId + "的4g pci 干扰矩阵任务创建失败");
					return false;
				}
				return true;
			}

		});
//		System.out.println("提交完成耗时："+((System.currentTimeMillis()-s)/1000));
//		s = System.currentTimeMillis();
	}
	/**
	 * 
	 * @title 查询对应条件的MR数据记录数量
	 * @param condition
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15下午3:06:30
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public int queryMrDataCountByCond(Map<String, String> condition) {
		return (int) rno4gPciDao.getMrDataCount(condition);
	}

	/**
	 * 获取最近十次lte干扰矩阵信息
	 * @title 
	 * @return
	 * @author peng.jm
	 * @date 2015年4月13日18:32:31
	 * @company 怡创科技
	 */
	public List<Map<String, Object>> getLatelyLteMatrixByCityId(long cityId) {
		// TODO Auto-generated method stub
		return rno4gPciDao.getLatelyLteMatrixByCityId(cityId);
	}

	@Override
	public List<Map<String, Object>> getLteMatrixById(long jobId) {
		// TODO Auto-generated method stub
		return rno4gPciDao.getLteMatrixById(jobId);
	}

	/**
	 * 获取同站lte小区和pci
	 * @title 
	 * @return
	 * @author peng.jm
	 * @date 2015年4月13日18:32:31
	 * @company 怡创科技
	 */
	public List<Map<String, String>> getSameStationCellsByLteCellId(
			String lteCell) {
		return rno4gPciDao.getSameStationCellsByLteCellId(lteCell);
	}

	@Override
	public boolean changeLteCellPci(String cell1, String pci1, String cell2,
			String pci2) {
		// TODO Auto-generated method stub
		return rno4gPciDao.changeLteCellPci(cell1,pci1,cell2,pci2);
	}
	/**
	 * 
	 * @title 提交4g方位角计算任务
	 * @param account
	 * @param taskInfo
	 * @return
	 * @author chao.xj
	 * @date 2015-4-29下午3:22:32
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public Map<String, Object> submit4GAzimuthCalcTask(String account,
			
			final Map<String, Object> taskInfo) {
		log.debug("进入submit4GAzimuthCalcTask account=" + account
				+  ",taskInfo=" + taskInfo);
		Map<String, Object> result = new HashMap<String, Object>();
		// 创建job
		JobProfile job = new JobProfile();
		job.setAccount(account);
		job.setJobName(taskInfo.get("TASKNAME").toString());
		job.setJobType("RNO_4G_AZIMUTH_CALC");
		
		job.setSubmitTime(new Date());
		job.setDescription(taskInfo.get("TASKDESC").toString());
		
		// 保存4g方位角任务
		final String begMeaTime = taskInfo.get("STARTTIME").toString();
		final String endMeaTime = taskInfo.get("ENDTIME").toString();
		System.out.println("cityId:"+taskInfo.get("CITYID").toString());
		final long cityId = Long.parseLong(taskInfo.get("CITYID").toString());
		
		// jobClient.submitJob(job);
		JobClient jobClient =JobClientDelegate.getJobClient();
		result = jobClient.submitJob(job,
				new JobAddCallback<Map<String, Object>>() {
					@Override
					public Map<String, Object> doWhenJobAdded(JobProfile job) {
						Map<String, Object> result = new HashMap<String, Object>();
						// 获取jobId
						long jobId = job.getJobId();
						if (jobId == 0) {
							log.error("创建jobId失败！");
							result.put("flag", false);
							result.put("desc", "提交任务失败！");
						}
						// 下载文件名
						String dlFileName = jobId + "_4G方位角对比表.xlsx";
						// 读取文件名
						String rdFileName = jobId + "_4g_azimuth_data";
						// 创建日期
						Calendar cal = Calendar.getInstance();
						cal = Calendar.getInstance();
						String createTime = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss").format(cal.getTime());
						// 文件保存路径
						String resultDir = "hdfs:///rno_data/4g_azimuth/" + cal.get(Calendar.YEAR) 
								+ "/" + (cal.get(Calendar.MONTH) + 1);
						String finishState = "排队中";
						// 更新日期
						String modTime = createTime;

						// 保存对应的门限值
						String paramVal;
						String paramCode;

						String oriDs = DataSourceContextHolder
								.getDataSourceType();
						DataSourceContextHolder
								.setDataSourceType(DataSourceConst.rnoDS);
						Connection connection = DataSourceConn.initInstance()
								.getConnection();
						DataSourceContextHolder.setDataSourceType(oriDs);
						PreparedStatement pstmt = null;
						
						try {
							

							String insertSql = "insert into rno_4g_azimuth_job	(JOB_ID,"
									+ "BEG_MEA_TIME,"
									+ "END_MEA_TIME,"
									+ "CITY_ID,"
									+ "DL_FILE_NAME,"
									+ "RD_FILE_NAME,"
									+ "RESULT_DIR,"
									+ "FINISH_STATE,"
									+ "STATUS,"
									+ "CREATE_TIME,"
									+ "MOD_TIME)"
									+ "	    values											" + "		  ("
									+ jobId
									+ ",											"
									+ "		   to_date('"
									+ begMeaTime
									+ "', 'yyyy-MM-dd HH24:mi:ss'),	"
									+ "		   to_date('"
									+ endMeaTime
									+ "', 'yyyy-MM-dd HH24:mi:ss'),	"
									+ "		   "
									+ cityId
									+ ",											"
									+ "		   '"
									+ dlFileName
									+ "',											"
									+ "		   '"
									+ rdFileName
									+ "',											"
									+ "		   '"
									+ resultDir
									+ "',											"
									+ "		   '"
									+ finishState
									+ "',											"
									+ "		   'N',												"
									+ "		   to_date('"
									+ createTime
									+ "', 'yyyy-MM-dd HH24:mi:ss'),"
									+ "		   to_date('"
									+ modTime
									+ "', 'yyyy-MM-dd HH24:mi:ss'))";

							pstmt = connection.prepareStatement(insertSql);

							pstmt.addBatch();
							
							// 执行
							try {
								pstmt.executeBatch();
								result.put("flag", true);
								result.put("desc", "提交任务成功！");
							} catch (SQLException e) {
								e.printStackTrace();
								log.error("jobId=" + jobId + "，保存4g方位角计算任务失败！");
								result.put("flag", false);
								result.put("desc", "提交任务失败！");
							}
						} catch (Exception ee) {
							ee.printStackTrace();
							log.error("jobId=" + jobId + "，保存4g方位角计算任务失败！");
							result.put("flag", false);
							result.put("desc", "提交任务失败！");
						} finally {
							try {
								if (pstmt != null)
									pstmt.clearBatch();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							try {
								if (pstmt != null)
									pstmt.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							try {
								connection.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						return result;
					}
				});
		log.debug("退出submit4GAzimuthCalcTask result=" + result);
		return result;
	}
	/**
	 * 
	 * @title 查询4g方位角计算任务
	 * @param cond
	 * @param page
	 * @param account
	 * @return
	 * @author chao.xj
	 * @date 2015-4-29下午3:40:24
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> query4GAzimuthCalcTaskByPage(
			Map<String, String> cond, Page page, String account) {
		log.info("进入方法：query4GAzimuthCalcTaskByPage。condition=" + cond
				+ ",page=" + page);
		if (page == null) {
			log.warn("方法：query4GAzimuthCalcTaskByPage的参数：page 是空！无法查询!");
			return Collections.EMPTY_LIST;
		}
		long totalCnt = page.getTotalCnt();
		if (totalCnt < 0) {
			totalCnt = rno4gPciDao.get4GAzimuthCalcTaskCount(cond, account);
			page.setTotalCnt((int) totalCnt);
		}
		int startIndex = page.calculateStart();
		int cnt = page.getPageSize();
		List<Map<String, Object>> res = rno4gPciDao.query4GAzimuthCalcTaskByPage(cond, account, startIndex, cnt);
				
		return res;
	}
	/**
	 * 
	 * @title 通过区域ID获取LTE小区标识对应的小区信息
	 * @param stmt
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2015-4-29下午3:40:24
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public Map<String, List<String>> getLteCellInfoByCellId(Statement stmt,long cityId) {
		return rno4gPciDao.getLteCellInfoByCellId(stmt, cityId);
	}
}
