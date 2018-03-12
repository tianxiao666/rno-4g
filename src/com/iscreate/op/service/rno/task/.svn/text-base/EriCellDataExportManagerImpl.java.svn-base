package com.iscreate.op.service.rno.task;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.iscreate.op.service.rno.tool.FileTool;


public class EriCellDataExportManagerImpl implements EriCellDataExportManager {

	private static Log log = LogFactory.getLog(EriCellDataExportManagerImpl.class);

	// 所有的exportToken信息
	private static Map<String, ExportToken> allTokens = new HashMap<String, ExportToken>();

	/**
	 * 新建导出任务，并返回token
	 * 每次新建导出任务，会对前天以前旧缓存数据文件进行清理
	 * @return
	 * @author peng.jm
	 * @date 2014-11-6下午06:04:23
	 */
	public String assignExportToken() {
		//获取当前日期
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR) ;
		int month = (cal.get(Calendar.MONTH) + 1);
		int day = (cal.get(Calendar.DAY_OF_MONTH));
		//获取根目录全路径
		String path = ServletActionContext.getServletContext().getRealPath(
			"/op/rno/");
		
		//删除临时目录里的旧数据
		//删除去年数据
		FileTool.deleteDir(path+"/eri_cell_ana_data/"+(year-1));
		//删除其他月份的数据
		for (int m = 1; m < month; m++) {
			FileTool.deleteDir(path+"/eri_cell_ana_data/"+year+"/"+m);
		}
		//删除当前月中昨天以前的数据
		for (int d = 1; d < day-1; d++) {
			FileTool.deleteDir(path+"/eri_cell_ana_data/"+year+"/"+month+"/"+d);
		}
		
		String token = java.util.UUID.randomUUID().toString().replaceAll("-", "");
		ExportToken et = new ExportToken(token, System.currentTimeMillis());
		allTokens.put(token, et);
		return token;
	}

	/**
	 * 通过token获取导出任务
	 * @param token
	 * @return
	 * @author peng.jm
	 * @date 2014-11-6下午06:04:42
	 */
	public ExportToken getExportToken(String token) {
		if (allTokens.containsKey(token)) {
			return allTokens.get(token);
		} else {
			return null;
		}
	}

	/**
	 * 更新导出任务信息
	 * @param token
	 * @param msg
	 * @return
	 * @author peng.jm
	 * @date 2014-11-6下午06:05:09
	 */
	public boolean updateTokenMsg(String token, String msg) {
		if (allTokens.containsKey(token)) {
			allTokens.get(token).msg=msg;
			return true;
		}
		return false;
	}
	
	/**
	 * 设置导出任务为失败状态
	 * @param token
	 * @return
	 * @author peng.jm
	 * @date 2014-11-6下午06:05:26
	 */
	public boolean tokenFail(String token) {
		if (allTokens.containsKey(token)) {
			allTokens.get(token).fail = true;
			return true;
		}
		return false;
	}
	
	/**
	 * 设置导出任务为完成状态
	 * @param token
	 * @return
	 * @author peng.jm
	 * @date 2014-11-6下午06:05:38
	 */
	public boolean tokenFinished(String token) {
		if (allTokens.containsKey(token)) {
			allTokens.get(token).finished = true;
			return true;
		}
		return false;
	}

	/**
	 * 销毁导出任务
	 * @param token
	 * @return
	 * @author peng.jm
	 * @date 2014-11-6下午06:05:49
	 */
	public boolean destroyToken(String token) {
		allTokens.remove(token);
		return true;
	}

	/**
	 * 保存导出任务的文件路径
	 * @param token
	 * @param filePath
	 * @return
	 * @author peng.jm
	 * @date 2014-11-6下午06:06:00
	 */
	public boolean saveTokenFilePath(String token, String filePath) {
		if (allTokens.containsKey(token)) {
			allTokens.get(token).filePath = filePath;
			return true;
		}
		return false;
	}
	
	public class ExportToken {
		String token;
		long createTime;
		boolean fail = false;
		boolean finished = false;
		String msg = "";
		String filePath = "";

		public ExportToken(String token, long createTime) {
			this.token = token;
			this.createTime = createTime;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public long getCreateTime() {
			return createTime;
		}

		public void setCreateTime(long createTime) {
			this.createTime = createTime;
		}

		public boolean isFail() {
			return fail;
		}

		public void setFail(boolean fail) {
			this.fail = fail;
		}

		public boolean isFinished() {
			return finished;
		}

		public void setFinished(boolean finished) {
			this.finished = finished;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}
		
	}




}
