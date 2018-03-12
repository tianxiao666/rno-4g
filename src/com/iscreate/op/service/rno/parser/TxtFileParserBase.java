package com.iscreate.op.service.rno.parser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.iscreate.plat.system.datasourcectl.DataSourceConst;
import com.iscreate.plat.system.datasourcectl.DataSourceContextHolder;

public abstract class TxtFileParserBase extends BaseParser {
	private static Log log = LogFactory.getLog(TxtFileParserBase.class);

	protected Connection connection;

	public boolean parseData(String token, File file, boolean needPersist,
			boolean update, long oldConfigId, long areaId, boolean autoload,
			Map<String, Object> attachParams) {
		log.debug("进入TxtFileParserBase方法：parseData。  token=" + token + ",file="
				+ file + ",needPersist=" + needPersist + ",update=" + update
				+ ",oldConfigId=" + oldConfigId + ",autoload=" + autoload
				+ ",attachParams=" + attachParams);

		boolean ok = true;

		DataSourceContextHolder.setDataSourceType(DataSourceConst.rnoDS);
		connection = DataSourceConn.initInstance().getConnection();
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime, "数据连接失败！");
			ok = false;
		}

		if (ok) {
			if (file == null) {
				ok = false;
				setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
						"文件解析失败，文件为空！");
			} else {
				try {
					ok = parseDataInternal(token, file, needPersist, update,
							oldConfigId, areaId, autoload, attachParams);
				} catch (Exception e) {
					ok = false;
					e.printStackTrace();
					log.error("解析文件失败！");
					setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
							"文件解析失败，请选择txt文件！");
				}
			}
		}
		if (!ok) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				connection.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			connection.setAutoCommit(true);
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 关闭文件

		// 通知
		if (ok) {
			log.debug("token对应的解析任务成功完成。");
			fileParserManager.updateTokenProgress(token, 1f);
		} else {
			log.warn("token对应的解析任务失败了！");
			fileParserManager.tokenFail(token);
			fileParserManager.updateTokenProgress(token, 1f);
		}
		log.debug("退出TxtFileParserBase方法：parseData。 结果：" + ok);
		return ok;
	}

	protected abstract boolean parseDataInternal(String token, File file,
			boolean needPersist, boolean update, long oldConfigId, long areaId,
			boolean autoload, Map<String, Object> attachParams);

	/**
	 * 中间执行出错后的处理 关闭connection,将出错信息输出到缓存
	 * 
	 * @param connection
	 * @param token
	 * @param errMsg
	 * @author brightming 2013-11-25 上午9:59:20
	 */
	protected void fail(Connection connection, String token, String errMsg) {
		log.error("中间出错！msg=" + errMsg);
		if (connection != null) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			memCached.set(token, RnoConstant.TimeConstant.TokenTime, "文件解析失败!"
					+ errMsg);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	/**
	 * 统计文本文件行数
	 * 
	 * @param file
	 * @return
	 * @author brightming 2013-11-28 上午11:28:03
	 */
	protected int getTxtLineNumber(File file) {
		if (file == null) {
			return 0;
		}
		long fileLength = file.length();
		long start = System.currentTimeMillis();

		LineNumberReader rf = null;
		int lines = 0;
		try {
			rf = new LineNumberReader(new FileReader(file));

			if (rf != null) {
				rf.skip(fileLength);
				lines = rf.getLineNumber();
				rf.close();
			}
		} catch (IOException e) {
			if (rf != null) {
				try {
					rf.close();
				} catch (IOException ee) {
				}
			}
		}
		long end = System.currentTimeMillis();
		log.info("Use Time: " + (end - start) + " Line Num: " + lines);
		return lines;
	}
}
