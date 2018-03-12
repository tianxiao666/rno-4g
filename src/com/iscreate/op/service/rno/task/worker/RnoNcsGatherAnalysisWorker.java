package com.iscreate.op.service.rno.task.worker;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.iscreate.op.dao.rno.RnoStructureAnalysisDao;
import com.iscreate.op.dao.rno.RnoTaskDao;
import com.iscreate.op.pojo.rno.RnoTask;
import com.iscreate.op.service.rno.RnoStructureService;
import com.iscreate.op.service.rno.task.ExecutorManager;
import com.iscreate.op.service.rno.task.RnoRenderer;
import com.iscreate.op.service.rno.task.RnoTaskWorker;
import com.iscreate.op.service.rno.task.TaskStatus;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.iscreate.plat.system.datasourcectl.DataSourceConst;
import com.iscreate.plat.system.datasourcectl.DataSourceContextHolder;

public class RnoNcsGatherAnalysisWorker implements RnoTaskWorker {

	private static Log log = LogFactory
			.getLog(RnoNcsGatherAnalysisWorker.class);
	// ---注入----//
	private RnoTaskDao rnoTaskDao;
	private RnoStructureAnalysisDao rnoStructureAnalysisDao;
	private RnoStructureService rnoStructureService;
	private RnoRenderer rnoRenderer;

	public void setRnoTaskDao(RnoTaskDao rnoTaskDao) {
		this.rnoTaskDao = rnoTaskDao;
	}

	public void setRnoStructureAnalysisDao(
			RnoStructureAnalysisDao rnoStructureAnalysisDao) {
		this.rnoStructureAnalysisDao = rnoStructureAnalysisDao;
	}

	public RnoStructureService getRnoStructureService() {
		return rnoStructureService;
	}

	public void setRnoStructureService(RnoStructureService rnoStructureService) {
		this.rnoStructureService = rnoStructureService;
	}

	public RnoRenderer getRnoRenderer() {
		return rnoRenderer;
	}

	public void setRnoRenderer(RnoRenderer rnoRenderer) {
		this.rnoRenderer = rnoRenderer;
	}

	/**
	 * 汇总分析ncs
	 * 
	 * @param taskId
	 * @param extraParams
	 *            key:SAVE_PATH结果文件的保存目录 额外参数
	 * @return
	 * @author brightming 2014-1-23 下午3:57:32
	 */
	public TaskStatus doWork(long taskId, Map<String, String> extraParams) {
		log.info("在RnoNcsGatherAnalysisWorker doWork 方法中.taskId=" + taskId);

		List<Map<String, Object>> params = rnoTaskDao.getTaskParams(taskId);
		log.info("task id=" + taskId + "的参数情况：" + params);
		long t1, t2;
		RnoTask task = rnoTaskDao.getTaskById(taskId);
		if (task == null) {
			log.error("不存在指定ID=[" + taskId + "]的任务！");
			return TaskStatus.ABORT;
		}
		//判断线程是否被停止
		//ExecutorManager.printMap();
		if(ExecutorManager.isTheTaskThreadInterrupted(taskId)) {
			log.error("ID=[" + taskId + "]的任务被停止了！");
			task.setTaskGoingStatus(TaskStatus.CANCEL.getCode());
			task.setResult("用户" + extraParams.get("creator") + "在" + new java.sql.Timestamp(new Date().getTime()) + "取消了该任务");
			rnoTaskDao.updateTask(task);
			//ExecutorManager.printMap();
			return TaskStatus.CANCEL;
		}

		java.sql.Timestamp startTime=new java.sql.Timestamp(new Date().getTime());
		task.setStartTime(startTime);
		rnoTaskDao.updateTask(task);

		// 检查结果文件保存参数
		String savePath = extraParams == null ? null : extraParams
				.get("SAVE_PATH");
		if (savePath == null || "".equals(savePath.trim())) {
			log.error("未指明结果文件的保存路径！不予执行！");
			task.setTaskGoingStatus(TaskStatus.ABORT.getCode());
			task.setResult("未指明结果文件的保存路径！不予执行！");
			rnoTaskDao.updateTask(task);
			return TaskStatus.ABORT;
		}

		if (params == null || params.isEmpty()) {
			log.error("task[" + taskId + "]无法执行，因为其参数为空！");
			task.setTaskGoingStatus(TaskStatus.FAIL.getCode());
			task.setResult("需要的参数为空，无法进行运算！");
			rnoTaskDao.updateTask(task);
			return TaskStatus.FAIL;
		}

		long cityId = -1;
		List<Long> oriNcsIds = new ArrayList<Long>();
		String ncsIds = "", v;
		for (Map<String, Object> one : params) {
			if (one == null || one.isEmpty()) {
				continue;
			}
			if ("NCSID".equals(one.get("PARAM_NAME") + "")) {// hard code : key
																// is NCSID
				v = (String) one.get("PARAM_VALUE");
				if (v != null && !"".equals(v.trim())) {
					ncsIds += v + ",";
					oriNcsIds.add(Long.parseLong(v));
				}
			} else if ("CITYID".equals(one.get("PARAM_NAME") + "")) {
				v = (String) one.get("PARAM_VALUE");
				cityId = Long.parseLong(v);
			}
		}
		if (ncsIds.length() > 0) {
			ncsIds = ncsIds.substring(0, ncsIds.length() - 1);
		}
		log.info("ncsIds=" + ncsIds);

		String sql = "";
		// ---------准备数据库连接----------//
		DataSourceContextHolder.setDataSourceType(DataSourceConst.rnoDS);
		Connection connection = DataSourceConn.initInstance().getConnection();
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
			log.error("task[" + taskId + "]无法执行，发生系统错误，无法连接数据库！");
			task.setTaskGoingStatus(TaskStatus.FAIL.getCode());
			task.setResult("无法连接数据库，无法进行运算！");
			rnoTaskDao.updateTask(task);
			return TaskStatus.FAIL;
		}
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("task[" + taskId + "]无法执行，发生系统错误，无法创建数据库执行器！");
			task.setTaskGoingStatus(TaskStatus.FAIL.getCode());
			task.setResult("无法创建数据执行器，无法进行运算！");
			rnoTaskDao.updateTask(task);
			return TaskStatus.FAIL;
		}

		// 转移到临时分析表以后，统一为一个ncsid
		// long staticNcsId = 100l;
		// List<Long> staticNcsIds = Arrays.asList(staticNcsId);

		// --准备保存路径---//
		Calendar calendar = new GregorianCalendar();
		Date dt=new Date(startTime.getTime());
		calendar.setTime(dt);
		String dir = savePath + "/" + calendar.get(Calendar.YEAR) + "/"
				+ (calendar.get(Calendar.MONTH) + 1) + "/";
		String cellClusterPath = dir + "ncs_res_" + taskId + ".xls";

		//判断线程是否被停止
		//ExecutorManager.printMap();
		if(ExecutorManager.isTheTaskThreadInterrupted(taskId)) {
			log.error("ID=[" + taskId + "]的任务被停止了！");
			task.setTaskGoingStatus(TaskStatus.CANCEL.getCode());
			task.setResult("用户" + extraParams.get("creator") + "在" + new java.sql.Timestamp(new Date().getTime()) + "取消了该任务");
			rnoTaskDao.updateTask(task);
			//ExecutorManager.printMap();
			return TaskStatus.CANCEL;
		}
		Map<String, Object> result = rnoStructureAnalysisDao.doNcsAnalysis(connection, stmt,
				cellClusterPath, cityId, oriNcsIds,taskId);
		
		try {
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			stmt.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if(result.get("flag") == "cancel" || result.get("flag").equals("cancel")) {
			log.error("ID=[" + taskId + "]的任务被停止了！");
			task.setTaskGoingStatus(TaskStatus.CANCEL.getCode());
			task.setResult("用户" + extraParams.get("creator") + "在" + new java.sql.Timestamp(new Date().getTime()) + "取消了该任务");
			rnoTaskDao.updateTask(task);
			//ExecutorManager.printMap();
			return TaskStatus.CANCEL;
		}
		
		if ((Boolean) result.get("flag") == true) {
			log.info("汇总分析成功。");
			
			//生成默认显示的渲染图
			String filePath = extraParams.get("SAVE_PATH");//路径
			String ncsRendererType = "BE_INTERFER";	//默认参数
			Map<String, Object> res = new HashMap<String, Object>();
			res = rnoRenderer.getRenderImg(ncsRendererType, taskId, filePath);
			if(res.get("flag").equals("error")) {
				log.error("默认渲染图生成失败");
			}
			
			// 更新任务状态
			task.setTaskGoingStatus(TaskStatus.FINISH.getCode());
			task.setCompleteTime(new java.sql.Timestamp(new Date().getTime()));
			task.setResult("完成");
			rnoTaskDao.updateTask(task);
			return TaskStatus.FINISH;
		} else {
			log.error("task[" + taskId + "]无法执行！" + result.get("msg"));
			task.setTaskGoingStatus(TaskStatus.FAIL.getCode());
			task.setResult("" + result.get("msg"));
			rnoTaskDao.updateTask(task);
			return TaskStatus.FAIL;
		}

	}

}
