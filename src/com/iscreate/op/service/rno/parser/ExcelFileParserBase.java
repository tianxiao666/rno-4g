package com.iscreate.op.service.rno.parser;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.iscreate.plat.system.datasourcectl.DataSourceConst;
import com.iscreate.plat.system.datasourcectl.DataSourceContextHolder;
import com.iscreate.plat.tools.excelhelper.ExcelService;

public abstract class ExcelFileParserBase extends BaseParser {
	private static Log log = LogFactory.getLog(ExcelFileParserBase.class);
	protected static Gson gson = new Gson();
	// --spring 注入---//
//	public IFileParserManager fileParserManager;
//	public MemcachedClient memCached;
	public ExcelService excelService;
	
	protected Connection connection;

	public IFileParserManager getFileParserManager() {
		return fileParserManager;
	}

	public void setFileParserManager(IFileParserManager fileParserManager) {
		this.fileParserManager = fileParserManager;
	}

	public MemcachedClient getMemCached() {
		return memCached;
	}

	public void setMemCached(MemcachedClient memCached) {
		this.memCached = memCached;
	}

	public void setExcelService(ExcelService excelService) {
		this.excelService = excelService;
	}

	public ExcelFileParserBase() {

	}

	/**
	 * 
	 */
	public boolean parseData(String token, File file, boolean needPersist,
			boolean update, long oldConfigId, long areaId, boolean autoload,
			Map<String, Object> attachParams) {

		log.debug("进入ExcelFileParserBase方法：parseData。  token=" + token
				+ ",file=" + file + ",needPersist=" + needPersist + ",update="
				+ update + ",oldConfigId=" + oldConfigId + ",autoload="
				+ autoload + ",attachParams=" + attachParams);

		boolean ok = false;
		DataSourceContextHolder.setDataSourceType(DataSourceConst.rnoDS);
		connection = DataSourceConn.initInstance().getConnection();
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime, "数据连接失败！");
			ok = false;
		}
		try {
			ok = parseDataInternal(token, file, needPersist, update,
					oldConfigId, areaId, autoload, attachParams);
		} catch (Exception e) {
			ok = false;
			e.printStackTrace();
			log.error("解析文件失败！");
			try {
				memCached.set(token, RnoConstant.TimeConstant.TokenTime,
						"文件解析失败，请选择excel文件！");
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		// 关闭文件
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
		
		// 通知
		if (ok) {
			log.debug("token对应的解析任务成功完成。");
			fileParserManager.updateTokenProgress(token, 1f);
		} else {
			log.warn("token对应的解析任务失败了！");
			fileParserManager.tokenFail(token);
			fileParserManager.updateTokenProgress(token, "token对应的解析任务失败了！");
			fileParserManager.updateTokenProgress(token, 1f);
		}
		log.debug("退出ExcelFileParserBase方法：parseData。 结果：" + ok);
		return ok;
	}

	protected abstract boolean parseDataInternal(String token, File file,
			boolean needPersist, boolean update, long oldConfigId, long areaId,
			boolean autoload, Map<String, Object> attachParams);

	public boolean checkTitles(List<String> needTitles, List<String> realTitles) {
		if (needTitles == null || realTitles == null) {
			return false;
		}
		if (needTitles.size() > realTitles.size()) {
			return false;
		}
		int size = needTitles.size();
		for (int i = 0; i < size; i++) {
			if (!needTitles.get(i).equals(realTitles.get(i))) {
				log.error("第[" + (i +1)+ "]列标题：needTitles.get(i)=["
						+ needTitles.get(i) + "],realTitles.get(i)=["
						+ realTitles.get(i)+"]不相同！");
				return false;
			}
		}

		return true;
	}

	/**
	 * 中间执行出错后的处理 关闭connection,将出错信息输出到缓存
	 * 
	 * @param connection
	 * @author brightming 2013-11-6 下午4:22:21
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
	 * 返回excel标题的总个数，同时把excel的列的编号填充到excelTitlesColumn信息中
	 * @param titles
	 * excel的实际标题
	 * @param dbFieldsToExcelTitles
	 * 配置好的数据库字段为key，excel列为value的映射数据
	 * @param excelTitlesColumn
	 * 用于返回的excel的列标题
	 * @param msg
	 * @return
	 * @author brightming
	 * 2014-5-16 上午10:28:23
	 */
	public int calculateExcelTitleColumn(List<String> titles,Map<String, String> dbFieldsToExcelTitles,
			Map<String, Integer> excelTitlesColumn, StringBuilder msg) {
		if (titles == null || titles.size() == 0) {
			msg.append("标题为空");
			return -1;
		}
		int col = 0;
		if (dbFieldsToExcelTitles == null || dbFieldsToExcelTitles.isEmpty()) {
			msg.append("系统配置出错！");
			return -1;
		}

		Collection<String> allAllowedExcelTitles = dbFieldsToExcelTitles
				.values();

		String oneTitle = null;
		for (col = 0; col < titles.size(); col++) {
			oneTitle = titles.get(col);
			if (oneTitle == null || "".equals(oneTitle.trim())) {
				// 遇到空格或null就退出
				return col;
			}
			oneTitle = oneTitle.trim();
			if (allAllowedExcelTitles.contains(oneTitle)) {
				if (excelTitlesColumn.containsKey(oneTitle)) {
					// 标题重复
					msg.append("excel标题重复");
					return -2;
				}
				excelTitlesColumn.put(oneTitle, col);
			}
		}
		return col;
	}
	
	/**
	 * 检查必须要去的excel标题是否存在
	 * @param excelTitlesColumn
	 * @param mandatoryExcelTitles2
	 * @param msg
	 * @return
	 * @author brightming
	 * 2014-5-16 上午10:34:33
	 */
	public boolean baseCheckIfExcelTitleValide(
			Map<String, Integer> excelTitlesColumn,
			List<String> mandatoryExcelTitles2, StringBuilder msg) {

		if (mandatoryExcelTitles2 == null || mandatoryExcelTitles2.isEmpty()) {
			return true;

		}
		if (excelTitlesColumn == null || excelTitlesColumn.isEmpty()) {
			return false;
		}

		boolean ok = true;
		for (String one : mandatoryExcelTitles2) {
			if (!excelTitlesColumn.containsKey(one)
					|| excelTitlesColumn.get(one) == null
					|| excelTitlesColumn.get(one) < 0) {
				msg.append(one + ",");
				ok = false;
			}
		}
		if (ok == false) {
			msg.append("excel缺少以下有效标题：" + msg.substring(0, msg.length() - 1));
		}
		return ok;
	}

	
	/**
	 * 返回excel表中没有出现的标题
	 * @param excelTitlesColumn
	 * 某份excel中已经出现的标题，以及它们的列位置（从0开始）
	 * @param excelTitlesToDbFields
	 * 所有有效的标题列名称以及对应的db列名称，只用到标题列名称。
	 * @return
	 * @author brightming
	 * 2014-5-16 上午10:37:18
	 */
	public List<String> getNotListExcelTitles(
			Map<String, Integer> excelTitlesColumn,Map<String, String> excelTitlesToDbFields) {
		List<String> re = new ArrayList<String>();
		for (String s : excelTitlesToDbFields.keySet()) {
			if (!excelTitlesColumn.containsKey(s)) {
				re.add(s);
			}
		}
		return re;
	}
}
