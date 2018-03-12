package com.iscreate.op.service.rno.task;

import com.iscreate.op.service.rno.task.EriCellDataExportManagerImpl.ExportToken;

public interface EriCellDataExportManager {

	/**
	 * 新建导出任务，并返回token
	 * @return
	 * @author peng.jm
	 * @date 2014-11-6下午06:04:23
	 */
	public String assignExportToken();
	/**
	 * 通过token获取导出任务
	 * @param token
	 * @return
	 * @author peng.jm
	 * @date 2014-11-6下午06:04:42
	 */
	public ExportToken getExportToken(String token);
	/**
	 * 更新导出任务信息
	 * @param token
	 * @param msg
	 * @return
	 * @author peng.jm
	 * @date 2014-11-6下午06:05:09
	 */
	public boolean updateTokenMsg(String token, String msg);
	/**
	 * 设置导出任务为失败状态
	 * @param token
	 * @return
	 * @author peng.jm
	 * @date 2014-11-6下午06:05:26
	 */
	public boolean tokenFail(String token);
	/**
	 * 设置导出任务为完成状态
	 * @param token
	 * @return
	 * @author peng.jm
	 * @date 2014-11-6下午06:05:38
	 */
	public boolean tokenFinished(String token);
	/**
	 * 销毁导出任务
	 * @param token
	 * @return
	 * @author peng.jm
	 * @date 2014-11-6下午06:05:49
	 */
	public boolean destroyToken(String token);
	/**
	 * 保存导出任务的文件路径
	 * @param token
	 * @param filePath
	 * @return
	 * @author peng.jm
	 * @date 2014-11-6下午06:06:00
	 */
	public boolean saveTokenFilePath(String token, String filePath);



}
