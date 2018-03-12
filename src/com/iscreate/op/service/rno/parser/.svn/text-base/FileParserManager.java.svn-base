package com.iscreate.op.service.rno.parser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.iscreate.op.action.rno.RnoResourceManagerAction;

public class FileParserManager implements IFileParserManager,
		ApplicationContextAware {

	// -----------类静态-------------//
	private static Log log = LogFactory.getLog(RnoResourceManagerAction.class);
	private static ConcurrentHashMap<String, FileParser> fileParsers = new ConcurrentHashMap<String, FileParser>();
	private static Map<String, String> fileCodeToFileParser = new HashMap<String, String>();// 文件编码对应文件解析类的全路径

	private static Map<String, ParserToken> allTokens = new HashMap<String, ParserToken>();// 所有的token信息

	private static FileParserManager instance = new FileParserManager();

	// ----//
	private ApplicationContext context = null;

	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		context = arg0;
	}

	public void init() {
		// 在这里，先用这种方法初始化
		//gsm小区
		fileCodeToFileParser.put("GSMCELLFILE", "gsmCellFileParser");
		//gsm邻区
		fileCodeToFileParser.put("GSMNCELLFILE", "gsmNcellFileParser");
		//地图打点的excel数据
		fileCodeToFileParser.put("GSMCELLBRIEFFILE", "gsmCellBriefFileParser");
		//gsm语音话务统计
		fileCodeToFileParser.put("GSMAUDIOTRAFFICSTATICSFILE", "gsmAudioTrafficStaticsFileParser");
		//gsm数据话务统计
		fileCodeToFileParser.put("GSMDATATRAFFICSTATICSFILE", "gsmDataTrafficStaticsFileParser");
		//城市网络质量统计
		fileCodeToFileParser.put("GSMCITYNETQUALITYFILE", "gsmCityNetQualityFileParser");
		//gsm小区配置导入
		fileCodeToFileParser.put("GSMPLANCELLFILE", "gsmPlanCellFileParser");
		//切换统计数据导入
		fileCodeToFileParser.put("GSMCHANNELSWITCHSTATICFILE", "gsmChannelSwitchStatisticsFileParser");
		//干扰数据的导入
		fileCodeToFileParser.put("GSMMRINTERFERENCEEXCELFILE", "gsmInterferenceExcelFileParser");
		//ncs数据
		fileCodeToFileParser.put("GSMNCSEXCELFILE", "gsmNcsExcelFileParser");
		//小区结构指标数据
		fileCodeToFileParser.put("GSMCELLSTRUCTEXCELFILE", "gsmCellStructureExcelFileParser");
	    //dt数据
		
		fileCodeToFileParser.put("GSMDTDLLOGFILE", "gsmDTDLTxtFileParser");
		
		
		//ncs原始数据解析
		fileCodeToFileParser.put("ERICSSONNCSFILE", "ericssonNcsParser");
		
		//LTE 小区数据导入解析
		fileCodeToFileParser.put("LTECELLFILE", "rnoLteCellParser");
		
		//华为ncs记录导入
		fileCodeToFileParser.put("HUAWEINCSFILE", "huaweiNcsParser");
		
		//爱立信Mrr记录导入
		fileCodeToFileParser.put("ERICSSONMRRFILE", "ericssonMrrParser");
		
		//华为 2G MRR记录导入
		fileCodeToFileParser.put("2GHWMRRFILE", "gsm2GHWMrrTxtFileParser");
		
		//BSC excel数据导入
		fileCodeToFileParser.put("BSCEXCELFILE", "bscExcelFileParser");
		
		//LTE 小区数据导入解析
		fileCodeToFileParser.put("LTENCELLFILE", "rnoLteNcellParser");
	}

	/**
	 * 分解数据
	 * 
	 * @param File
	 * @param fileCode
	 * @param needPersist
	 * @param autoload
	 * @param update
	 * @param oldConfigId
	 * @param areaId
	 * @return Sep 9, 2013 12:19:29 PM gmh
	 */
	public String parserData(final File file, final String fileCode,
			final Boolean needPersist, final Boolean autoload,
			final Boolean update, final Long oldConfigId, final Long areaId,final  Map<String,Object> attachParams) {

		log.debug("进入：parserData。 File=" + file + ",fileCode=" + fileCode
				+ ",needPersist=" + needPersist + ",autoload=" + autoload
				+ ",update=" + update + ",oldConfigId=" + oldConfigId
				+ ",areaId=" + areaId);
		final String token = assignToken(fileCode);
		if (token == null) {
			log.error("分配token失败！");
			return null;
		}
		final FileParser parser = getFileParserByFileCode(fileCode);
		if (parser == null) {
			log.error("不支持这种类型的文件的解析！");
			destroyToken(token);
			return null;
		} else {
			// start thread
			new Thread() {
				@Override
				public void run() {
					parser.parseData(token, file, needPersist, update,
							oldConfigId, areaId,autoload,attachParams);
				}
			}.start();
		}
		return token;

	}

	/**
	 * 更新进度
	 * 
	 * @param token
	 * @param progress
	 * @return Sep 10, 2013 4:45:05 PM gmh
	 */
	public boolean updateTokenProgress(String token, float progress) {
		if (allTokens.containsKey(token)) {
			allTokens.get(token).progress = progress;
			return true;
		}
		return false;
	}

	/**
	 * 附带消息
	 * @param token
	 * @param progress
	 * @param msg
	 * @return
	 * @author brightming
	 * 2014-1-20 上午11:07:13
	 */
	public boolean updateTokenProgress(String token, float progress,String msg){
		if (allTokens.containsKey(token)) {
			allTokens.get(token).progress = progress;
			allTokens.get(token).msg=msg;
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @param token
	 * @param msg
	 * @return
	 * @author brightming
	 * 2014-1-20 上午11:10:10
	 */
	public boolean updateTokenProgress(String token,String msg){
		if (allTokens.containsKey(token)) {
			allTokens.get(token).msg=msg;
			return true;
		}
		return false;
	}
	/**
	 * 获取token
	 * @param token
	 * @return
	 * Sep 12, 2013 5:23:16 PM
	 * gmh
	 */
	public ParserToken getToken(String token) {
		if (allTokens.containsKey(token)) {
			return allTokens.get(token);
		} else {
			return null;
		}
	}

	/**
	 * token对应的任务失败！
	 * 
	 * @param token
	 * @return Sep 10, 2013 4:53:35 PM gmh
	 */
	public boolean tokenFail(String token) {
		if (allTokens.containsKey(token)) {
			allTokens.get(token).fail = true;
			return true;
		}
		return false;
	}

	/**
	 * 产生token
	 * 
	 * @param fileCode
	 * @return Sep 9, 2013 12:19:20 PM gmh
	 */
	public String assignToken(String fileCode) {
		if (fileCode == null && fileCode.trim().isEmpty()) {
			return null;
		}
		String token = fileCode
				+ java.util.UUID.randomUUID().toString().replaceAll("-", "");
		ParserToken pt = new ParserToken(token, System.currentTimeMillis());
		allTokens.put(token, pt);
		return token;
	}

	/**
	 * 销毁token
	 * 
	 * @param token
	 * @return Sep 9, 2013 12:40:53 PM gmh
	 */
	public boolean destroyToken(String token) {
		allTokens.remove(token);
		return true;
	}

	/**
	 * 根据文件类型编码获取像样的文件解析器
	 * 
	 * @param fileCode
	 * @return Sep 9, 2013 12:34:22 PM gmh
	 */
	private synchronized FileParser getFileParserByFileCode(String fileCode) {
		log.debug("进入 getFileParserByFileCode。 fileCode=" + fileCode);
		if (fileCode == null || fileCode.trim().isEmpty()) {
			log.error("根据fileCode=[" + fileCode + "]获取FileParser失败！");
			return null;
		}
		FileParser parser = null;
		if (fileParsers.containsKey(fileCode)) {
			log.debug("已经存在fileCode=[" + fileCode + "]对应的解析类对象。");
			// 是否已经加载过该类对象
			parser = fileParsers.get(fileCode);
		} else {
			log.debug("当前不存在fileCode=[" + fileCode + "]对应的解析类对象，尝试获取对应的类全路径。");
			// if (fileCodeToFileParser.containsKey(fileCode)) {
			// String classFullPath = fileCodeToFileParser.get(fileCode);
			// log.debug("fileCode=[" + fileCode + "]对应的解析类全路径为："
			// + classFullPath);
			// try {
			// Class<?> classz = Class.forName(classFullPath);
			// parser = (FileParser) classz.newInstance();// 必须提供默认构造函数
			//
			// // 放进列表
			// fileParsers.put(fileCode, parser);
			// log.debug("发射生成类:" + classFullPath + "的对象成功！");
			// } catch (ClassNotFoundException e) {
			// log.error("反射类：" + classFullPath + "时出错！");
			// e.printStackTrace();
			// } catch (InstantiationException e) {
			// log.error("实例化类：" + classFullPath + "的对象时出错！");
			// e.printStackTrace();
			// } catch (IllegalAccessException e) {
			// log.error("实例化类：" + classFullPath + "的对象时出错！");
			// e.printStackTrace();
			// }

			// }

			if (context != null && fileCodeToFileParser
					.get(fileCode)!=null) {
				log.debug("尝试从spring容器获取:" + fileCode + "的对象。");
				try {
					parser = (FileParser) context.getBean(fileCodeToFileParser
							.get(fileCode));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (parser != null) {
				// 放进列表
				fileParsers.put(fileCode, parser);
			}
		}

		log.debug("退出 getFileParserByFileCode。获取到的FileParser=" + parser);
		return parser;
	}

	public class ParserToken {
		String token;
		long createTime;
		float progress;
		boolean fail;
		String msg="";

		public ParserToken(String token, long createTime) {
			this.token = token;
			this.createTime = createTime;
			progress = 0f;
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

		public float getProgress() {
			return progress;
		}

		public void setProgress(float progress) {
			this.progress = progress;
		}

		public boolean isFail() {
			return fail;
		}

		public void setFail(boolean fail) {
			this.fail = fail;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		

	}

}
