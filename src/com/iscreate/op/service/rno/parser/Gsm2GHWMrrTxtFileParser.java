package com.iscreate.op.service.rno.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.rno.RnoBscDao;
import com.iscreate.op.dao.rno.RnoStructAnaV2;
import com.iscreate.op.dao.rno.RnoStructAnaV2Impl;
import com.iscreate.op.dao.rno.RnoStructureAnalysisDao;
import com.iscreate.op.dao.system.SysAreaDao;
import com.iscreate.op.pojo.rno.ResultInfo;
import com.iscreate.op.service.rno.parser.vo.HW2GMrrFRateRec;
import com.iscreate.op.service.rno.parser.vo.HW2GMrrHRateRec;
import com.iscreate.op.service.rno.parser.vo.HW2GMrrRecord;
import com.iscreate.op.service.rno.tool.FileTool;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.op.service.rno.tool.ZipFileHandler;

public class Gsm2GHWMrrTxtFileParser extends TxtFileParserBase {

	private static Log log = LogFactory.getLog(Gsm2GHWMrrTxtFileParser.class);
	// ---spring 注入----//
	public RnoBscDao rnoBscDao;

	private RnoStructAnaV2 rnoStructAnaV2 = new RnoStructAnaV2Impl();
	
	private SysAreaDao sysAreaDao;
	private AreaLockManager areaLockManager;
	private static Gson gson = new GsonBuilder().create();// 线程安全
	private static SimpleDateFormat sdfYyyymmddhhmmss = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	private static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	private RnoStructureAnalysisDao rnoStructureAnalysisDao;

	public RnoStructureAnalysisDao getRnoStructureAnalysisDao() {
		return rnoStructureAnalysisDao;
	}

	public void setRnoStructureAnalysisDao(
			RnoStructureAnalysisDao rnoStructureAnalysisDao) {
		this.rnoStructureAnalysisDao = rnoStructureAnalysisDao;
	}

	public RnoBscDao getRnoBscDao() {
		return rnoBscDao;
	}

	public void setRnoBscDao(RnoBscDao rnoBscDao) {
		this.rnoBscDao = rnoBscDao;
	}

	public SysAreaDao getSysAreaDao() {
		return sysAreaDao;
	}

	public void setSysAreaDao(SysAreaDao sysAreaDao) {
		this.sysAreaDao = sysAreaDao;
	}

	public AreaLockManager getAreaLockManager() {
		return areaLockManager;
	}

	public void setAreaLockManager(AreaLockManager areaLockManager) {
		this.areaLockManager = areaLockManager;
	}

	// 语句
	// 2G_HW_MRR_DESC sql
	private static String insert2GHwMrrDescDataSql = "insert into RNO_2G_HW_MRR_DESC (MRR_DESC_ID,MEA_DATE,BSC,CELL_NUM,USER_ACCOUNT,CYCLE,CITY_ID) values (?,?,?,?,?,?,?)";
	// 2G_HW_MRR_HRATE sql
	private static String insert2GHwMrrHRateTempDataSql = "insert into RNO_2G_HW_MRR_HRATE_T (MEA_DATE,BSC,CELL,CELL_IDX,LAC,CI,TRX_IDX,INTEGRITY,S4100C,S4101C,S4102C,S4103C,S4104C,S4105C,S4106C,S4107C,S4110C,S4111C,S4112C,S4113C,S4114C,S4115C,S4116C,S4117C,S4120C,S4121C,S4122C,S4123C,S4124C,S4125C,S4126C,S4127C,S4130C,S4131C,S4132C,S4133C,S4134C,S4135C,S4136C,S4137C,S4140C,S4141C,S4142C,S4143C,S4144C,S4145C,S4146C,S4147C,S4150C,S4151C,S4152C,S4153C,S4154C,S4155C,S4156C,S4157C,S4160C,S4161C,S4162C,S4163C,S4164C,S4165C,S4166C,S4167C,S4170C,S4171C,S4172C,S4173C,S4174C,S4175C,S4176C,S4177C,S4100D,S4101D,S4102D,S4103D,S4104D,S4105D,S4106D,S4107D,S4110D,S4111D,S4112D,S4113D,S4114D,S4115D,S4116D,S4117D,S4120D,S4121D,S4122D,S4123D,S4124D,S4125D,S4126D,S4127D,S4130D,S4131D,S4132D,S4133D,S4134D,S4135D,S4136D,S4137D,S4140D,S4141D,S4142D,S4143D,S4144D,S4145D,S4146D,S4147D,S4150D,S4151D,S4152D,S4153D,S4154D,S4155D,S4156D,S4157D,S4160D,S4161D,S4162D,S4163D,S4164D,S4165D,S4166D,S4167D,S4170D,S4171D,S4172D,S4173D,S4174D,S4175D,S4176D,S4177D,CITY_ID) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	// 2G_HW_MRR_FRATE sql
	private static String insert2GHwMrrFRateTempDataSql = "insert into RNO_2G_HW_MRR_FRATE_T (MEA_DATE,BSC,CELL,CELL_IDX,LAC,CI,TRX_IDX,INTEGRITY,S4100A,S4101A,S4102A,S4103A,S4104A,S4105A,S4106A,S4107A,S4110A,S4111A,S4112A,S4113A,S4114A,S4115A,S4116A,S4117A,S4120A,S4121A,S4122A,S4123A,S4124A,S4125A,S4126A,S4127A,S4130A,S4131A,S4132A,S4133A,S4134A,S4135A,S4136A,S4137A,S4140A,S4141A,S4142A,S4143A,S4144A,S4145A,S4146A,S4147A,S4150A,S4151A,S4152A,S4153A,S4154A,S4155A,S4156A,S4157A,S4160A,S4161A,S4162A,S4163A,S4164A,S4165A,S4166A,S4167A,S4170A,S4171A,S4172A,S4173A,S4174A,S4175A,S4176A,S4177A,S4100B,S4101B,S4102B,S4103B,S4104B,S4105B,S4106B,S4107B,S4110B,S4111B,S4112B,S4113B,S4114B,S4115B,S4116B,S4117B,S4120B,S4121B,S4122B,S4123B,S4124B,S4125B,S4126B,S4127B,S4130B,S4131B,S4132B,S4133B,S4134B,S4135B,S4136B,S4137B,S4140B,S4141B,S4142B,S4143B,S4144B,S4145B,S4146B,S4147B,S4150B,S4151B,S4152B,S4153B,S4154B,S4155B,S4156B,S4157B,S4160B,S4161B,S4162B,S4163B,S4164B,S4165B,S4166B,S4167B,S4170B,S4171B,S4172B,S4173B,S4174B,S4175B,S4176B,S4177B,CITY_ID) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	@Override
	protected boolean parseDataInternal(String token, File file,
			boolean needPersist, boolean update, long oldConfigId, long areaId,
			boolean autoload, Map<String, Object> attachParams) {
		log.debug("进入2GHwMrrTxtFileParser方法：parseDataInternal。 token=" + token
				+ ",file=" + file + ",needPersist=" + needPersist + ",update="
				+ update + ",oldConfigId=" + oldConfigId + ",areaId=" + areaId
				+ ",attachParams=" + attachParams);

		if (file == null) {
			return false;
		}

		setTokenInfo(token, -1f, "正在检查文件有效性");
		long st = System.currentTimeMillis();
		String msg = "";
		// 检查必要参数
		if (attachParams == null || attachParams.isEmpty()) {
			msg = "缺少必要参数！";
			log.error(msg);
			super.setCachedInfo(token, msg);
			return false;
		}

		String city = attachParams.get("cityId") + "";
		long cityId = -1;
		try {
			cityId = Long.parseLong(city);
		} catch (Exception e) {
			e.printStackTrace();
			msg = "城市信息有误！";
			log.error(msg);
			super.setCachedInfo(token, msg);
			return false;
		}
		// 获取所有bsc信息指定区域下
		List<Map<String, Object>> allBscs = sysAreaDao
				.getBscInSpecAreaLevel(cityId);
		if (allBscs == null || allBscs.isEmpty()) {
			log.error("city=" + cityId + " 下没有BSC设置！");
			super.setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"指定的导入区域下不存在BSC，请先导入小区信息！");
			return false;
		}
		log.info("allBscs=" + allBscs);
		// BSC名字到val值的映射存储
		Map<String, String> bscNameToIds = new HashMap<String, String>();
		String bscname = "";
		for (Map<String, Object> one : allBscs) {
			bscname = one.get("ENGNAME").toString();
			bscNameToIds.put(bscname, bscname);
		}
		log.info("bscNameToIds=" + bscNameToIds);
		// 锁定区域
		Set<Long> affectAreaIds = new HashSet<Long>();
		affectAreaIds
				.add(Long.parseLong(attachParams.get("cityId").toString()));
		affectAreaIds.add(areaId);
		OperResult lockRes = areaLockManager.lockAreas(affectAreaIds,
				"LOCKAREAFORIMPORT2GHWMRR");
		log.debug("锁定区域:" + affectAreaIds + " 的申请结果为： " + lockRes);
		if (!lockRes.isFlag()) {
			log.error("导入Mrr数据时，锁定区域失败！冲突区域为：" + lockRes.getMessage());
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"指定的导入区域当前有其他用户在导入，请稍后再试！");
			return false;
		}

		// 如果是压缩包，需要先解压缩
		String fileName = (String) attachParams.get("fileName");
		List<File> allMrrFiles = new ArrayList<File>();// 将所有待处理的mrr文件放置在这个列表里
		boolean fromZip = false;
		String destDir = "";
		if (fileName.endsWith(".zip") || fileName.endsWith("ZIP")
				|| fileName.endsWith("Zip")) {
			setTokenInfo(token, "正在解析压缩包");
			super.setCachedInfo(token, "正在解析压缩包");
			fromZip = true;
			// 压缩包
			log.info("上传的mrr文件是一个压缩包。");

			String path = file.getParentFile().getPath();
			destDir = path + "/"
					+ UUID.randomUUID().toString().replaceAll("-", "");
			msg = "上传文件为zip压缩包文件,！只允许上传CSV正确文件格式！";
			log.debug("zipfile:" + file.getAbsolutePath()
					+ "-----------destDir:" + destDir);
			boolean unzipOk = false;
			try {
				unzipOk = ZipFileHandler.unZip(file.getAbsolutePath(), destDir);
			} catch (Exception e) {
				msg = "压缩包解析失败！请确认压缩包文件是否被破坏！";
				log.error(msg);
				super.setCachedInfo(token, msg);
				clearResource(destDir, null);
				areaLockManager.unlockAreas(affectAreaIds,
						"LOCKAREAFORIMPORT2GHWMRR");
				return false;
			}
			if (!unzipOk) {
				msg = "解压失败 ！仅支持zip格式的压缩包！ ";
				log.error(msg);
				super.setCachedInfo(token, msg);
				clearResource(destDir, null);
				areaLockManager.unlockAreas(affectAreaIds,
						"LOCKAREAFORIMPORT2GHWMRR");
				return false;
			}
			file = new File(destDir);
			File[] files = file.listFiles();
			// 判断文件数量，不要超过规定数量
			if (files.length != 2) {
				msg = "一次上传的mrr文件数量:[" + files.length + "]规定的数量：2，即半速率与全速率各一个";
				log.error(msg);
				setTokenInfo(token, msg);
				super.setCachedInfo(token, msg);
				clearResource(destDir, null);
				areaLockManager.unlockAreas(affectAreaIds,
						"LOCKAREAFORIMPORT2GHWMRR");
				return false;
			}
			for (File f : files) {
				// 只要文件，不要目录
				if (f.isFile() && !f.isHidden()) {
					allMrrFiles.add(f);
				}
			}

		} else if (fileName.endsWith(".rar")) {
			msg = "请用zip格式压缩文件！";
			log.error(msg);
			super.setCachedInfo(token, msg);
			clearResource(destDir, null);
			areaLockManager.unlockAreas(affectAreaIds,
					"LOCKAREAFORIMPORT2GHWMRR");
			return false;
		} else {
			msg = "上传的mrr是一个非压缩文件。！";
			log.error(msg);
			super.setCachedInfo(token, msg);
			clearResource(destDir, null);
			areaLockManager.unlockAreas(affectAreaIds,
					"LOCKAREAFORIMPORT2GHWMRR");
			return false;
		}

		if (allMrrFiles.isEmpty()) {
			msg = "未上传有效的mrr文件！";
			log.error(msg);
			super.setCachedInfo(token, msg);
			clearResource(destDir, null);
			areaLockManager.unlockAreas(affectAreaIds,
					"LOCKAREAFORIMPORT2GHWMRR");
			return false;
		}

		HW2GMrrParserContext context = new HW2GMrrParserContext();

		PreparedStatement insert2GHWMrrDescStmt = null;
		try {
			insert2GHWMrrDescStmt = connection
					.prepareStatement(insert2GHwMrrDescDataSql);
		} catch (SQLException e1) {
			e1.printStackTrace();
			msg = "数据处理出错！code=mrr-1";
			log.error(msg);
			super.setCachedInfo(token, msg);
			clearResource(destDir, context);
			areaLockManager.unlockAreas(affectAreaIds,
					"LOCKAREAFORIMPORT2GHWMRR");
			return false;
		}
		context.setPreparedStatment("2GHWMrrDesc", insert2GHWMrrDescStmt);

		PreparedStatement insert2GHwMrrHRateTempStmt = null;
		try {
			insert2GHwMrrHRateTempStmt = connection
					.prepareStatement(insert2GHwMrrHRateTempDataSql);
		} catch (SQLException e1) {
			e1.printStackTrace();
			msg = "数据处理出错！code=mrr-2";
			log.error(msg);
			super.setCachedInfo(token, msg);
			clearResource(destDir, context);
			areaLockManager.unlockAreas(affectAreaIds,
					"LOCKAREAFORIMPORT2GHWMRR");
			return false;
		}
		context.setPreparedStatment("2GHwMrrHRateRecord",
				insert2GHwMrrHRateTempStmt);

		PreparedStatement insert2GHwMrrFRateTempStmt = null;
		try {
			insert2GHwMrrFRateTempStmt = connection
					.prepareStatement(insert2GHwMrrFRateTempDataSql);
		} catch (SQLException e1) {
			e1.printStackTrace();
			msg = "数据处理出错！code=mrr-3";
			log.error(msg);
			super.setCachedInfo(token, msg);
			clearResource(destDir, context);
			areaLockManager.unlockAreas(affectAreaIds,
					"LOCKAREAFORIMPORT2GHWMRR");
			return false;
		}
		context.setPreparedStatment("2GHwMrrFRateRecord",
				insert2GHwMrrFRateTempStmt);

		context.setAreaId(areaId);
		context.setCityId(cityId);

		for (File f : allMrrFiles) {
			setTokenInfo(token, "正在解析MRR的" + f.getName() + "文件");
			super.setCachedInfo(token, "正在解析MRR的" + f.getName() + "文件");
			log.debug("解压出来的文件名：" + f.getName());
			try {
				if (fromZip) {
					parseMrr(f.getName(), f, context, token, bscNameToIds);
				}
			} catch (Exception e) {
				e.printStackTrace();
				msg = "解析文件出错！请检查是否按要求上传文件！";
				log.error(msg);
				super.setCachedInfo(token, msg);
				clearResource(destDir, context);
				areaLockManager.unlockAreas(affectAreaIds,
						"LOCKAREAFORIMPORT2GHWMRR");
				return false;
			}
		}
		// =======================================
		long mrrId = -1;
		// 申请id
		mrrId = super.getNextSeqValue("SEQ_RNO_2G_HW_MRR_DESC", connection);
		// 数据处理
		setTokenInfo(token, "正在进行数据处理");
		super.setCachedInfo(token, "正在进行数据处理");
		if (mrrId != -1) {
			HttpSession session = (HttpSession) attachParams.get("session");
			String account = session.getAttribute("userId").toString();
			log.debug("account:" + account);
			dataProcess(fileName, account, mrrId, context, token);
		} else {
			log.error("数据信息有误！");
			context.appedErrorMsg("文件:[" + file.getName() + "]的数据信息有误！<br/>");
		}
		// =========================================
		long et = System.currentTimeMillis();
		log.info("导入mrr文件，共耗时：" + (et - st) + "ms");

		log.info("退出Gsm2GHWMrrTxtFileParser解析方法");

		clearResource(destDir, context);
		log.info("解除区域锁定");
		areaLockManager.unlockAreas(affectAreaIds, "LOCKAREAFORIMPORT2GHWMRR");
		setTokenInfo(token, context.getErrorMsg());
		super.setCachedInfo(token, "解析完成！");
		return true;
	}

	/*
	 * public void parseMrr(String fileName, File file, HW2GMrrParserContext
	 * context, String token) throws IOException {
	 * 
	 * 
	 * HW2GMrrRecord rec=null; long mrrId = -1;
	 * 
	 * Date startTime = new Date(); try { BufferedReader br = new
	 * BufferedReader(new InputStreamReader(new FileInputStream(file),"GBK"));
	 * // 读取直到最后一行 String line = ""; int lineNum=0; int i=0;
	 * 
	 * while ((line = br.readLine()) != null) { lineNum++; // 把一行数据分割成多个字段
	 * StringTokenizer st = new StringTokenizer(line, ","); if(lineNum==1){ if
	 * (st.countTokens() != 136) { log.error("表头格式错误！");
	 * context.appedErrorMsg("文件:[" + fileName + "]异常中断表头格式错误！<br/>"); break; }
	 * rec=fRateOrHrate(line); continue; } rec=handleSection(st,rec); //获取测量日期
	 * if(rec.getRecType().equals("S4100A")) { HW2GMrrFRateRec fRateRec =
	 * (HW2GMrrFRateRec) rec; startTime = fRateRec.getMeaTime(); //
	 * log.debug("startTime:"+new java.sql.Date(startTime.getTime())); } //
	 * 处理rec if (rec != null) { if (mrrId == -1) { // 申请id mrrId =
	 * super.getNextSeqValue("SEQ_RNO_2G_HW_MRR_DESC", connection); } //
	 * 转换成sql语句 prepareRecordSql(mrrId, rec, context); } } br.close(); }catch
	 * (IOException e) { // 捕获BufferedReader对象关闭时的异常 e.printStackTrace(); }
	 * 
	 * //数据处理 setTokenInfo(token, "正在进行数据处理"); if(mrrId != -1) {
	 * dataProcess(fileName, mrrId, startTime, context, token); } else {
	 * log.error("数据信息有误！"); context.appedErrorMsg("文件:[" + file.getName() +
	 * "]的数据信息有误！<br/>"); } }
	 */
	public void parseMrr(String fileName, File file,
			HW2GMrrParserContext context, String token,
			Map<String, String> bscNameToIds) throws IOException {

		HW2GMrrRecord rec = null;
		long mrrId = -1;

		Date startTime = new Date();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "GBK"));
			// 读取直到最后一行
			String line = "";
			int lineNum = 0;
			int i = 0;
			StringBuilder buf = new StringBuilder();
			String msg = "";
			while ((line = br.readLine()) != null) {
				lineNum++;
				// 把一行数据分割成多个字段
				StringTokenizer st = new StringTokenizer(line, ",");
				if (st.countTokens() < 136) {
					log.debug("有问题！linenum=" + lineNum + ",line=" + line);
					continue;
				}
				if (lineNum == 1) {
					if (st.countTokens() != 136) {
						log.error("表头格式错误！");
						context.appedErrorMsg("文件:[" + fileName
								+ "]异常中断表头格式错误！<br/>");
						break;
					}
					rec = fRateOrHrate(line);
					continue;
				}
				// 当某些不符合要求时返回为空，则下面不组织sql语句
				rec = handleSection(st, rec, bscNameToIds);
				// 获取测量日期
				if (rec.getRecType().equals("S4100A")) {
					HW2GMrrFRateRec fRateRec = (HW2GMrrFRateRec) rec;
					startTime = fRateRec.getMeaTime();
					// log.debug("startTime:"+new
					// java.sql.Date(startTime.getTime()));
				}
				// 处理rec
				if (rec != null) {
					// 转换成sql语句
					prepareRecordSql(mrrId, rec, context);
				} else {
					msg = "文件:[" + fileName + "]第[" + (lineNum)
							+ "]行BSC为所选区域外的网元！";
					buf.append(msg + "<br/>");
					log.error("文件:[" + fileName + "]第[" + (lineNum)
							+ "]行BSC为所选区域外的网元！");
					context.appedErrorMsg("文件:[" + fileName + "]第[" + (lineNum)
							+ "]行BSC为所选区域外的网元！！<br/>");
					super.setCachedInfo(token, buf.toString());
				}
			}
			br.close();
		} catch (IOException e) {
			// 捕获BufferedReader对象关闭时的异常
			e.printStackTrace();
		}

	}

	private HW2GMrrRecord fRateOrHrate(String line) {
		boolean frateFlag = false;
		boolean hrateFlag = false;
		// 半速率
		if (line.contains("S4100C")) {
			HW2GMrrHRateRec hRateRec = new HW2GMrrHRateRec();
			return hRateRec;
		}
		// 全速率
		if (line.contains("S4100A")) {
			HW2GMrrFRateRec fRateRec = new HW2GMrrFRateRec();
			return fRateRec;
		}
		log.warn("未知类型的数据");
		return null;
	}

	/**
	 * 设置Mrr文件描述信息
	 * 
	 * @param fileName
	 * @param stmt
	 * @param mrrId
	 * @param areaId
	 * @param cityId
	 * @author chao.xj
	 * @date 2014-7-21下午02:08:32
	 */
	/*
	 * private void setMrrDescToStmt(String fileName, Date startTime,
	 * PreparedStatement stmt, long mrrId, long areaId, long cityId) { if (stmt
	 * == null) { return; }
	 * 
	 * int index = 1; try { stmt.setLong(index++, mrrId);
	 * stmt.setTimestamp(index++, new java.sql.Timestamp(startTime.getTime()));
	 * stmt.setString(index++, "BSC"); // stmt.setString(index++, "");
	 * stmt.setInt(index++, 200);//bsc后面自动匹配 stmt.setString(index++,
	 * "chao.xj@iscreate.com"); stmt.setLong(index++, 300);
	 * stmt.setLong(index++, 91);
	 * 
	 * stmt.addBatch(); } catch (SQLException e) { e.printStackTrace(); } }
	 */

	/**
	 * 准备批处理插入语句
	 * 
	 * @param mrrId
	 * @param rec
	 * @param context
	 * @author chao.xj
	 * @date 2014-7-21上午10:25:23
	 */
	private void prepareRecordSql(long mrrId, HW2GMrrRecord rec,
			HW2GMrrParserContext context) {
		if (rec instanceof HW2GMrrFRateRec) {
			PreparedStatement stmt = context
					.getPreparedStatement("2GHwMrrHRateRecord");
			setHW2GMrrFRateRecToStmt(stmt, mrrId, (HW2GMrrFRateRec) rec,
					context.getCityId());
		} else if (rec instanceof HW2GMrrHRateRec) {
			PreparedStatement stmt = context
					.getPreparedStatement("2GHwMrrFRateRecord");
			setHW2GMrrHRateRecToStmt(stmt, mrrId, (HW2GMrrHRateRec) rec,
					context.getCityId());
		} else {
			log.warn("暂时不处理此类型记录！");
		}

	}

	/**
	 * 
	 * @title 设置华为2G MRR全速率测量数据
	 * @param stmt
	 * @param mrrId
	 * @param rec
	 * @author chao.xj
	 * @date 2014-8-4下午12:17:08
	 * @company 怡创科技
	 * @version 1.2
	 */
	private void setHW2GMrrFRateRecToStmt(PreparedStatement stmt, long mrrId,
			HW2GMrrFRateRec rec, long cityId) {
//		log.debug("进入setHW2GMrrFRateRecToStmt");
		if (stmt == null || rec == null) {
			return;
		}

		int index = 1;
		try {
			// stmt.setLong(index++, mrrId);
			stmt.setTimestamp(index++, new java.sql.Timestamp(rec.getMeaTime().getTime()));
			stmt.setString(index++, rec.getBscName());
			stmt.setString(index++, rec.getCellName());
			stmt.setInt(index++, rec.getCellIdx());
			stmt.setInt(index++, rec.getLac());
			stmt.setInt(index++, rec.getCi());
			stmt.setInt(index++, rec.getTrxIdx());
			stmt.setString(index++, rec.getIntegrity());
//			log.debug(rec.getFieldValues());
			Map<String, Integer> fRateUls = rec.getfRateUls();
//			log.debug("fRateUls.size():" + fRateUls.size());
			int fRateUl = 0;
			for (int i = 0; i < fRateUls.size() / 8; i++) {
				for (int j = 0; j < 8; j++) {
					fRateUl = fRateUls.get("S41" + i + j + "A");
					stmt.setInt(index++, fRateUl);
				}

			}
			Map<String, Integer> fRateDls = rec.getfRateDls();
//			log.debug("fRateDls.size():" + fRateDls.size());
			int fRateDl = 0;
			for (int i = 0; i < fRateDls.size() / 8; i++) {
				for (int j = 0; j < 8; j++) {
					fRateDl = fRateDls.get("S41" + i + j + "B");
					stmt.setInt(index++, fRateDl);
				}
			}

			stmt.setLong(index++, cityId);
			stmt.addBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @title 设置华为2G MRR半速率测量数据
	 * @param stmt
	 * @param mrrId
	 * @param rec
	 * @author chao.xj
	 * @date 2014-8-4下午12:22:12
	 * @company 怡创科技
	 * @version 1.2
	 */
	private void setHW2GMrrHRateRecToStmt(PreparedStatement stmt, long mrrId,
			HW2GMrrHRateRec rec, long cityId) {
//		log.debug("进入setHW2GMrrHRateRecToStmt");
		if (stmt == null || rec == null) {
			return;
		}

		int index = 1;
		try {
			// stmt.setLong(index++, mrrId);
			stmt.setTimestamp(index++, new java.sql.Timestamp(rec.getMeaTime().getTime()));
			stmt.setString(index++, rec.getBscName());
			stmt.setString(index++, rec.getCellName());
			stmt.setInt(index++, rec.getCellIdx());
			stmt.setInt(index++, rec.getLac());
			stmt.setInt(index++, rec.getCi());
			stmt.setInt(index++, rec.getTrxIdx());
			stmt.setString(index++, rec.getIntegrity());
			Map<String, Integer> hRateUls = rec.gethRateUls();
//			log.debug("hRateUls行大小：" + hRateUls.size());
//			log.debug(rec.getFieldValues());
			int hRateUl = 0;
			for (int i = 0; i < hRateUls.size() / 8; i++) {
				for (int j = 0; j < 8; j++) {
					hRateUl = hRateUls.get("S41" + i + j + "C");
					stmt.setInt(index++, hRateUl);
				}

			}
			Map<String, Integer> hRateDls = rec.gethRateDls();
//			log.debug("hRateDls行大小：" + hRateDls.size());
			int hRateDl = 0;
			for (int i = 0; i < hRateDls.size() / 8; i++) {
				for (int j = 0; j < 8; j++) {
					hRateDl = hRateDls.get("S41" + i + j + "D");
					stmt.setInt(index++, hRateDl);
				}
			}

			stmt.setLong(index++, cityId);
			stmt.addBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public HW2GMrrRecord handleSection(StringTokenizer st, HW2GMrrRecord rec,
			Map<String, String> bscNameToIds) {

		if (rec instanceof HW2GMrrFRateRec) {
			rec = handleHW2GMrrFRate(st, bscNameToIds);
		}
		if (rec instanceof HW2GMrrHRateRec) {
			rec = handleHW2GMrrHRate(st, bscNameToIds);
		}
		return rec;
	}

	private HW2GMrrRecord handleHW2GMrrFRate(StringTokenizer st,
			Map<String, String> bscNameToIds) {
//		log.debug("进入handleHW2GMrrFRate");
		HW2GMrrFRateRec fRateRec = new HW2GMrrFRateRec();
		String bscname = "";
		try {
			fRateRec.setMeaTime(sdf.parse(st.nextToken()));
			bscname = st.nextToken();
//			log.debug("bscNameToIds.get(" + bscname + "):"
//					+ bscNameToIds.get(bscname));
			if ("".equals(bscNameToIds.get(bscname))) {
				return null;
			}
			fRateRec.setBscName(bscname);
			fRateRec.setCellName(st.nextToken());
			fRateRec.setCellIdx(Integer.parseInt(st.nextToken()));
			fRateRec.setLac(Integer.parseInt(st.nextToken()));
			fRateRec.setCi(Integer.parseInt(st.nextToken()));
			fRateRec.setTrxIdx(Integer.parseInt(st.nextToken()));
			fRateRec.setIntegrity(st.nextToken());
			// 全速率上行
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					fRateRec.addFRateUls("S41" + i + j + "A",
							Integer.parseInt(st.nextToken()));
				}
			}
			// 全速率下行
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					fRateRec.addFRateDls("S41" + i + j + "B",
							Integer.parseInt(st.nextToken()));
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fRateRec;
	}

	private HW2GMrrRecord handleHW2GMrrHRate(StringTokenizer st,
			Map<String, String> bscNameToIds) {
//		log.debug("进入handleHW2GMrrHRate");
		HW2GMrrHRateRec hRateRec = new HW2GMrrHRateRec();
		String bscname = "";
		try {
			hRateRec.setMeaTime(sdf.parse(st.nextToken()));
			bscname = st.nextToken();
//			log.debug("bscNameToIds.get(" + bscname + "):"
//					+ bscNameToIds.get(bscname));
			if ("".equals(bscNameToIds.get(bscname))) {

				return null;
			}
			hRateRec.setBscName(bscname);
			hRateRec.setCellName(st.nextToken());
			hRateRec.setCellIdx(Integer.parseInt(st.nextToken()));
			hRateRec.setLac(Integer.parseInt(st.nextToken()));
			hRateRec.setCi(Integer.parseInt(st.nextToken()));
			hRateRec.setTrxIdx(Integer.parseInt(st.nextToken()));
			hRateRec.setIntegrity(st.nextToken());
			// 全速率上行
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					hRateRec.addHRateUls("S41" + i + j + "C",
							Integer.parseInt(st.nextToken()));
				}
			}
			// 全速率下行
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					hRateRec.addHRateDls("S41" + i + j + "D",
							Integer.parseInt(st.nextToken()));
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hRateRec;
	}

	/**
	 * 
	 * @title mrr文件的数据处理
	 * @param fileName
	 * @param mrrId
	 * @param context
	 * @param token
	 * @author chao.xj
	 * @date 2014-8-6下午3:04:05
	 * @company 怡创科技
	 * @version 1.2
	 */
	private void dataProcess(String fileName, String account, long mrrId,
			HW2GMrrParserContext context, String token) {

		boolean hasHrate=false,hasFrate=false;
		
		Statement statement = null;
		String msg = "";
		try {
			statement = connection.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
			msg = "数据处理出错！code=mrr-11";
			log.error(msg);
			super.setCachedInfo(token, msg);
			return;
		}
		
		/**** 插入临时表信息 ****/

		PreparedStatement stmt = context
				.getPreparedStatement("2GHwMrrHRateRecord");
		if (stmt != null) {
			hasHrate=true;
			log.info("准备批处理插入半速率。。。");
			setTokenInfo(token, "准备批处理插入半速率。。。");
			super.setCachedInfo(token, "准备批处理插入半速率。。。");
			try {
				stmt.executeBatch();
				stmt.clearBatch();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
//			printTmpTable("rno_2g_hw_mrr_hrate_t", stmt);
			//处理半速率的描述信息
			ResultInfo resultInfo=rnoStructAnaV2.generateHwMrrDesc("HALF","RNO_2G_HW_MRR_HRATE_T","RNO_2G_HW_MRR_DESC",statement,connection,context.getCityId());
			log.debug("处理半速率的描述信息 resultInfo"+resultInfo);
		}

		stmt = context.getPreparedStatement("2GHwMrrFRateRecord");
		if (stmt != null) {
			hasFrate=true;
			log.info("准备批处理插入全速率。。。");
			setTokenInfo(token, "准备批处理插入全速率。。。");
			super.setCachedInfo(token, "准备批处理插入全速率。。。");
			try {
				stmt.executeBatch();
				stmt.clearBatch();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
//			printTmpTable("rno_2g_hw_mrr_frate_t", stmt);
			//处理全速率的描述信息
			ResultInfo resultInfo=rnoStructAnaV2.generateHwMrrDesc("FULL","RNO_2G_HW_MRR_FRATE_T","RNO_2G_HW_MRR_DESC",statement,connection,context.getCityId());
			log.debug("处理全速率的描述信息 resultInfo"+resultInfo);
		}
		

		// 检查文件是否对应区域
		/*
		 * log.info("验证"+fileName+"文件是否对应所选区域"); boolean flag =
		 * rnoStructureAnalysisDao.checkMrrArea(connection, mrrId,
		 * context.getCityId()); if(!flag) {
		 * log.error(fileName+"文件所属区域不是选择的区域！"); context.appedErrorMsg("文件:[" +
		 * fileName + "]所属区域不是选择的区域！！<br/>"); super.setCachedInfo(token, "文件:["
		 * + fileName + "]所属区域不是选择的区域！！<br/>"); return ; }
		 */
		/**** 将临时表信息转移到正式表 ****/
		// 保存mrr描述信息
		long cityId = context.getCityId();

		//半速率
		if(hasHrate){
			String fields = "MEA_DATE,BSC,CELL,TRX_IDX,S4100C,S4101C,S4102C,S4103C,S4104C,S4105C,S4106C,S4107C,S4110C,S4111C,S4112C,S4113C,S4114C,S4115C,S4116C,S4117C,S4120C,S4121C,S4122C,S4123C,S4124C,S4125C,S4126C,S4127C,S4130C,S4131C,S4132C,S4133C,S4134C,S4135C,S4136C,S4137C,S4140C,S4141C,S4142C,S4143C,S4144C,S4145C,S4146C,S4147C,S4150C,S4151C,S4152C,S4153C,S4154C,S4155C,S4156C,S4157C,S4160C,S4161C,S4162C,S4163C,S4164C,S4165C,S4166C,S4167C,S4170C,S4171C,S4172C,S4173C,S4174C,S4175C,S4176C,S4177C,S4100D,S4101D,S4102D,S4103D,S4104D,S4105D,S4106D,S4107D,S4110D,S4111D,S4112D,S4113D,S4114D,S4115D,S4116D,S4117D,S4120D,S4121D,S4122D,S4123D,S4124D,S4125D,S4126D,S4127D,S4130D,S4131D,S4132D,S4133D,S4134D,S4135D,S4136D,S4137D,S4140D,S4141D,S4142D,S4143D,S4144D,S4145D,S4146D,S4147D,S4150D,S4151D,S4152D,S4153D,S4154D,S4155D,S4156D,S4157D,S4160D,S4161D,S4162D,S4163D,S4164D,S4165D,S4166D,S4167D,S4170D,S4171D,S4172D,S4173D,S4174D,S4175D,S4176D,S4177D,CITY_ID";
			String sql="insert into RNO_2G_HW_MRR_HRATE(MRR_DESC_ID,"+fields+") select RNO_2G_HW_MRR_DESC_ID,"+fields + " from RNO_2G_HW_MRR_HRATE_T";
			
			log.info(">>>>>>>>>>>>>>>转移半速率信息中间表数据到目标表的sql：" + sql);
			try {
				statement.executeUpdate(sql);
			} catch (SQLException e) {
				log.error("转移半速率信息中间表数据到目标表的sql出错：" + sql);
				e.printStackTrace();
				context.closeAllStatement();
				try {
					statement.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				msg = "数据处理出错！code=mrr-11";
				log.error(msg);
				super.setCachedInfo(token, msg);
				try {
					stmt.close();
				} catch (Exception e3) {

				}
				return;
			}
			log.info("<<<<<<<<<<<<<<<完成转移半速率信息中间表数据到目标表");
		
		}
		
		//全速率
		if(hasFrate){
			String fields = "MEA_DATE,BSC,CELL,TRX_IDX,S4100A,S4101A,S4102A,S4103A,S4104A,S4105A,S4106A,S4107A,S4110A,S4111A,S4112A,S4113A,S4114A,S4115A,S4116A,S4117A,S4120A,S4121A,S4122A,S4123A,S4124A,S4125A,S4126A,S4127A,S4130A,S4131A,S4132A,S4133A,S4134A,S4135A,S4136A,S4137A,S4140A,S4141A,S4142A,S4143A,S4144A,S4145A,S4146A,S4147A,S4150A,S4151A,S4152A,S4153A,S4154A,S4155A,S4156A,S4157A,S4160A,S4161A,S4162A,S4163A,S4164A,S4165A,S4166A,S4167A,S4170A,S4171A,S4172A,S4173A,S4174A,S4175A,S4176A,S4177A,S4100B,S4101B,S4102B,S4103B,S4104B,S4105B,S4106B,S4107B,S4110B,S4111B,S4112B,S4113B,S4114B,S4115B,S4116B,S4117B,S4120B,S4121B,S4122B,S4123B,S4124B,S4125B,S4126B,S4127B,S4130B,S4131B,S4132B,S4133B,S4134B,S4135B,S4136B,S4137B,S4140B,S4141B,S4142B,S4143B,S4144B,S4145B,S4146B,S4147B,S4150B,S4151B,S4152B,S4153B,S4154B,S4155B,S4156B,S4157B,S4160B,S4161B,S4162B,S4163B,S4164B,S4165B,S4166B,S4167B,S4170B,S4171B,S4172B,S4173B,S4174B,S4175B,S4176B,S4177B,CITY_ID";
			String sql="insert into RNO_2G_HW_MRR_FRATE(MRR_DESC_ID,"+fields+") select RNO_2G_HW_MRR_DESC_ID,"+fields + " from RNO_2G_HW_MRR_FRATE_T";
			
			log.info(">>>>>>>>>>>>>>>转移全速率信息中间表数据到目标表的sql：" + sql);
			try {
				statement.executeUpdate(sql);
			} catch (SQLException e) {
				log.error("转移全速率信息中间表数据到目标表的sql出错：" + sql);
				e.printStackTrace();
				context.closeAllStatement();
				try {
					statement.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				msg = "数据处理出错！code=mrr-12";
				log.error(msg);
				super.setCachedInfo(token, msg);
				try {
					stmt.close();
				} catch (Exception e3) {

				}
				return;
			}
			log.info("<<<<<<<<<<<<<<<完成转移全速率信息中间表数据到目标表");
		
		}
		
//		String translateSql = " MERGE INTO RNO_2G_HW_MRR_DESC T1                                                                 "
//				+ " USING (select bsc, mea_date, count(cell) as cellcnt                                              "
//				+ "          from RNO_2G_HW_MRR_FRATE_T                                                              "
//				+ "         group by bsc, MEA_DATE                                                                   "
//				+ "         order by cellcnt desc nulls last) T2                                                     "
//				+ " ON (1 = 0)                                                                                       "
//				+ " WHEN NOT MATCHED THEN                                                                            "
//				+ "   INSERT                                                                                         "
//				+ "     (mrr_desc_id,mea_date ,bsc, cell_num,user_account,city_id)                             "
//				+ "   VALUES                                                                                         "
//				+ "     ("
//				+ mrrId
//				+ ",t2.MEA_DATE ,T2.bsc, T2.cellcnt,'"
//				+ account + "'," + cityId + ") ";
//
//		log.info(">>>>>>>>>>>>>>>转移mrr描述信息数据到目标表的sql：" + translateSql);
//		try {
//			statement.executeUpdate(translateSql);
//		} catch (SQLException e) {
//			log.error("mrr描述信息数据到目标表的sql出错：" + translateSql);
//			e.printStackTrace();
//			context.closeAllStatement();
//			try {
//				statement.close();
//			} catch (SQLException e1) {
//				e1.printStackTrace();
//			}
//			msg = "数据处理出错！code=mrr-11";
//			log.error(msg);
//			super.setCachedInfo(token, msg);
//			try {
//				stmt.close();
//			} catch (Exception e3) {
//
//			}
//			return;
//		}
//		log.info("<<<<<<<<<<<<<<<完成转移mrr描述信息数据到目标表");

		// 匹配BSC，保存在mrr描述表
		/*
		 * log.info(">>>>>>>>>>>>>>>开始自动计算mrr所测量的bsc"); setTokenInfo(token,
		 * "正在匹配mrr文件所测量的bsc");
		 * rnoStructureAnalysisDao.matchMrrBsc(connection,"RNO_ERI_MRR_QUALITY_TEMP"
		 * ,"RNO_ERI_MRR_DESCRIPTOR",mrrId+"");
		 * log.info("<<<<<<<<<<<<<<<完成计算mrr所测量的bsc。");
		 */

		/*
		 * java.sql.Timestamp meaDate = new
		 * java.sql.Timestamp(startTime.getTime()); String meaDateStr =
		 * meaDate.toString();
		 */

		// 转移全速率中间表数据到目标表
//		String updatetmpkeyToval = "T1.CITY_ID=T2.CITY_ID,T1.CELL       =T2.CELL       ,T1.TRX_IDX    =T2.TRX_IDX    ,T1.S4100A     =T2.S4100A     ,T1.S4101A     =T2.S4101A     ,T1.S4102A     =T2.S4102A     ,T1.S4103A     =T2.S4103A     ,T1.S4104A     =T2.S4104A     ,T1.S4105A     =T2.S4105A     ,T1.S4106A     =T2.S4106A     ,T1.S4107A     =T2.S4107A     ,T1.S4110A     =T2.S4110A     ,T1.S4111A     =T2.S4111A     ,T1.S4112A     =T2.S4112A     ,T1.S4113A     =T2.S4113A     ,T1.S4114A     =T2.S4114A     ,T1.S4115A     =T2.S4115A     ,T1.S4116A     =T2.S4116A     ,T1.S4117A     =T2.S4117A     ,T1.S4120A     =T2.S4120A     ,T1.S4121A     =T2.S4121A     ,T1.S4122A     =T2.S4122A     ,T1.S4123A     =T2.S4123A     ,T1.S4124A     =T2.S4124A     ,T1.S4125A     =T2.S4125A     ,T1.S4126A     =T2.S4126A     ,T1.S4127A     =T2.S4127A     ,T1.S4130A     =T2.S4130A     ,T1.S4131A     =T2.S4131A     ,T1.S4132A     =T2.S4132A     ,T1.S4133A     =T2.S4133A     ,T1.S4134A     =T2.S4134A     ,T1.S4135A     =T2.S4135A     ,T1.S4136A     =T2.S4136A     ,T1.S4137A     =T2.S4137A     ,T1.S4140A     =T2.S4140A     ,T1.S4141A     =T2.S4141A     ,T1.S4142A     =T2.S4142A     ,T1.S4143A     =T2.S4143A     ,T1.S4144A     =T2.S4144A     ,T1.S4145A     =T2.S4145A     ,T1.S4146A     =T2.S4146A     ,T1.S4147A     =T2.S4147A     ,T1.S4150A     =T2.S4150A     ,T1.S4151A     =T2.S4151A     ,T1.S4152A     =T2.S4152A     ,T1.S4153A     =T2.S4153A     ,T1.S4154A     =T2.S4154A     ,T1.S4155A     =T2.S4155A     ,T1.S4156A     =T2.S4156A     ,T1.S4157A     =T2.S4157A     ,T1.S4160A     =T2.S4160A     ,T1.S4161A     =T2.S4161A     ,T1.S4162A     =T2.S4162A     ,T1.S4163A     =T2.S4163A     ,T1.S4164A     =T2.S4164A     ,T1.S4165A     =T2.S4165A     ,T1.S4166A     =T2.S4166A     ,T1.S4167A     =T2.S4167A     ,T1.S4170A     =T2.S4170A     ,T1.S4171A     =T2.S4171A     ,T1.S4172A     =T2.S4172A     ,T1.S4173A     =T2.S4173A     ,T1.S4174A     =T2.S4174A     ,T1.S4175A     =T2.S4175A     ,T1.S4176A     =T2.S4176A     ,T1.S4177A     =T2.S4177A     ,T1.S4100B     =T2.S4100B     ,T1.S4101B     =T2.S4101B     ,T1.S4102B     =T2.S4102B     ,T1.S4103B     =T2.S4103B     ,T1.S4104B     =T2.S4104B     ,T1.S4105B     =T2.S4105B     ,T1.S4106B     =T2.S4106B     ,T1.S4107B     =T2.S4107B     ,T1.S4110B     =T2.S4110B     ,T1.S4111B     =T2.S4111B     ,T1.S4112B     =T2.S4112B     ,T1.S4113B     =T2.S4113B     ,T1.S4114B     =T2.S4114B     ,T1.S4115B     =T2.S4115B     ,T1.S4116B     =T2.S4116B     ,T1.S4117B     =T2.S4117B     ,T1.S4120B     =T2.S4120B     ,T1.S4121B     =T2.S4121B     ,T1.S4122B     =T2.S4122B     ,T1.S4123B     =T2.S4123B     ,T1.S4124B     =T2.S4124B     ,T1.S4125B     =T2.S4125B     ,T1.S4126B     =T2.S4126B     ,T1.S4127B     =T2.S4127B     ,T1.S4130B     =T2.S4130B     ,T1.S4131B     =T2.S4131B     ,T1.S4132B     =T2.S4132B     ,T1.S4133B     =T2.S4133B     ,T1.S4134B     =T2.S4134B     ,T1.S4135B     =T2.S4135B     ,T1.S4136B     =T2.S4136B     ,T1.S4137B     =T2.S4137B     ,T1.S4140B     =T2.S4140B     ,T1.S4141B     =T2.S4141B     ,T1.S4142B     =T2.S4142B     ,T1.S4143B     =T2.S4143B     ,T1.S4144B     =T2.S4144B     ,T1.S4145B     =T2.S4145B     ,T1.S4146B     =T2.S4146B     ,T1.S4147B     =T2.S4147B     ,T1.S4150B     =T2.S4150B     ,T1.S4151B     =T2.S4151B     ,T1.S4152B     =T2.S4152B     ,T1.S4153B     =T2.S4153B     ,T1.S4154B     =T2.S4154B     ,T1.S4155B     =T2.S4155B     ,T1.S4156B     =T2.S4156B     ,T1.S4157B     =T2.S4157B     ,T1.S4160B     =T2.S4160B     ,T1.S4161B     =T2.S4161B     ,T1.S4162B     =T2.S4162B     ,T1.S4163B     =T2.S4163B     ,T1.S4164B     =T2.S4164B     ,T1.S4165B     =T2.S4165B     ,T1.S4166B     =T2.S4166B     ,T1.S4167B     =T2.S4167B     ,T1.S4170B     =T2.S4170B     ,T1.S4171B     =T2.S4171B     ,T1.S4172B     =T2.S4172B     ,T1.S4173B     =T2.S4173B     ,T1.S4174B     =T2.S4174B     ,T1.S4175B     =T2.S4175B     ,T1.S4176B     =T2.S4176B     ,T1.S4177B     =T2.S4177B     ";
//		String inserttmpfields = "ID,MRR_DESC_ID,MEA_DATE,BSC,CELL,TRX_IDX,S4100A,S4101A,S4102A,S4103A,S4104A,S4105A,S4106A,S4107A,S4110A,S4111A,S4112A,S4113A,S4114A,S4115A,S4116A,S4117A,S4120A,S4121A,S4122A,S4123A,S4124A,S4125A,S4126A,S4127A,S4130A,S4131A,S4132A,S4133A,S4134A,S4135A,S4136A,S4137A,S4140A,S4141A,S4142A,S4143A,S4144A,S4145A,S4146A,S4147A,S4150A,S4151A,S4152A,S4153A,S4154A,S4155A,S4156A,S4157A,S4160A,S4161A,S4162A,S4163A,S4164A,S4165A,S4166A,S4167A,S4170A,S4171A,S4172A,S4173A,S4174A,S4175A,S4176A,S4177A,S4100B,S4101B,S4102B,S4103B,S4104B,S4105B,S4106B,S4107B,S4110B,S4111B,S4112B,S4113B,S4114B,S4115B,S4116B,S4117B,S4120B,S4121B,S4122B,S4123B,S4124B,S4125B,S4126B,S4127B,S4130B,S4131B,S4132B,S4133B,S4134B,S4135B,S4136B,S4137B,S4140B,S4141B,S4142B,S4143B,S4144B,S4145B,S4146B,S4147B,S4150B,S4151B,S4152B,S4153B,S4154B,S4155B,S4156B,S4157B,S4160B,S4161B,S4162B,S4163B,S4164B,S4165B,S4166B,S4167B,S4170B,S4171B,S4172B,S4173B,S4174B,S4175B,S4176B,S4177B";
//		String inserttmpvals = "T2.MEA_DATE   ,T2.BSC        ,T2.CELL       ,T2.TRX_IDX    ,T2.S4100A     ,T2.S4101A     ,T2.S4102A     ,T2.S4103A     ,T2.S4104A     ,T2.S4105A     ,T2.S4106A     ,T2.S4107A     ,T2.S4110A     ,T2.S4111A     ,T2.S4112A     ,T2.S4113A     ,T2.S4114A     ,T2.S4115A     ,T2.S4116A     ,T2.S4117A     ,T2.S4120A     ,T2.S4121A     ,T2.S4122A     ,T2.S4123A     ,T2.S4124A     ,T2.S4125A     ,T2.S4126A     ,T2.S4127A     ,T2.S4130A     ,T2.S4131A     ,T2.S4132A     ,T2.S4133A     ,T2.S4134A     ,T2.S4135A     ,T2.S4136A     ,T2.S4137A     ,T2.S4140A     ,T2.S4141A     ,T2.S4142A     ,T2.S4143A     ,T2.S4144A     ,T2.S4145A     ,T2.S4146A     ,T2.S4147A     ,T2.S4150A     ,T2.S4151A     ,T2.S4152A     ,T2.S4153A     ,T2.S4154A     ,T2.S4155A     ,T2.S4156A     ,T2.S4157A     ,T2.S4160A     ,T2.S4161A     ,T2.S4162A     ,T2.S4163A     ,T2.S4164A     ,T2.S4165A     ,T2.S4166A     ,T2.S4167A     ,T2.S4170A     ,T2.S4171A     ,T2.S4172A     ,T2.S4173A     ,T2.S4174A     ,T2.S4175A     ,T2.S4176A     ,T2.S4177A     ,T2.S4100B     ,T2.S4101B     ,T2.S4102B     ,T2.S4103B     ,T2.S4104B     ,T2.S4105B     ,T2.S4106B     ,T2.S4107B     ,T2.S4110B     ,T2.S4111B     ,T2.S4112B     ,T2.S4113B     ,T2.S4114B     ,T2.S4115B     ,T2.S4116B     ,T2.S4117B     ,T2.S4120B     ,T2.S4121B     ,T2.S4122B     ,T2.S4123B     ,T2.S4124B     ,T2.S4125B     ,T2.S4126B     ,T2.S4127B     ,T2.S4130B     ,T2.S4131B     ,T2.S4132B     ,T2.S4133B     ,T2.S4134B     ,T2.S4135B     ,T2.S4136B     ,T2.S4137B     ,T2.S4140B     ,T2.S4141B     ,T2.S4142B     ,T2.S4143B     ,T2.S4144B     ,T2.S4145B     ,T2.S4146B     ,T2.S4147B     ,T2.S4150B     ,T2.S4151B     ,T2.S4152B     ,T2.S4153B     ,T2.S4154B     ,T2.S4155B     ,T2.S4156B     ,T2.S4157B     ,T2.S4160B     ,T2.S4161B     ,T2.S4162B     ,T2.S4163B     ,T2.S4164B     ,T2.S4165B     ,T2.S4166B     ,T2.S4167B     ,T2.S4170B     ,T2.S4171B     ,T2.S4172B     ,T2.S4173B     ,T2.S4174B     ,T2.S4175B     ,T2.S4176B     ,T2.S4177B";
//		translateSql = " MERGE INTO RNO_2G_HW_MRR_FRATE T1                   "
//				+ " USING RNO_2G_HW_MRR_FRATE_T T2                      "
//				+ " ON (T1.BSC = T2.BSC AND T1.MEA_DATE = T2.MEA_DATE)  "
//				+ " WHEN MATCHED THEN                                   "
//				+ "   UPDATE SET " + updatetmpkeyToval + "                  "
//				+ "                                                     "
//				+ "                                                     "
//				+ " WHEN NOT MATCHED THEN                               "
//				+ "   INSERT                                            "
//				+ "     (                                               "
//				+ " 	   " + inserttmpfields + "                             "
//				+ " 	)                                                  "
//				+ "   VALUES                                            "
//				+ "     (seq_RNO_2G_HW_MRR_FRATE.nextval,               "
//				+ "      " + mrrId + ",                                     "
//				+ "       " + inserttmpvals + "                             "
//				+ " 	 )                                                 ";
//
//		log.info(">>>>>>>>>>>>>>>转移全速率信息中间表数据到目标表的sql：" + translateSql);
//		try {
//			statement.executeUpdate(translateSql);
//		} catch (SQLException e) {
//			log.error("转移全速率信息中间表数据到目标表的sql出错：" + translateSql);
//			e.printStackTrace();
//			context.closeAllStatement();
//			try {
//				statement.close();
//			} catch (SQLException e1) {
//				e1.printStackTrace();
//			}
//			msg = "数据处理出错！code=mrr-11";
//			log.error(msg);
//			super.setCachedInfo(token, msg);
//			try {
//				stmt.close();
//			} catch (Exception e3) {
//
//			}
//			return;
//		}
//		log.info("<<<<<<<<<<<<<<<完成转移全速率信息中间表数据到目标表");
//
//		// 转移半速率信息中间表数据到目标表
//		updatetmpkeyToval = "T1.CITY_ID=T2.CITY_ID,T1.CELL       =T2.CELL       ,T1.TRX_IDX    =T2.TRX_IDX    ,T1.S4100C     =T2.S4100C     ,T1.S4101C     =T2.S4101C     ,T1.S4102C     =T2.S4102C     ,T1.S4103C     =T2.S4103C     ,T1.S4104C     =T2.S4104C     ,T1.S4105C     =T2.S4105C     ,T1.S4106C     =T2.S4106C     ,T1.S4107C     =T2.S4107C     ,T1.S4110C     =T2.S4110C     ,T1.S4111C     =T2.S4111C     ,T1.S4112C     =T2.S4112C     ,T1.S4113C     =T2.S4113C     ,T1.S4114C     =T2.S4114C     ,T1.S4115C     =T2.S4115C     ,T1.S4116C     =T2.S4116C     ,T1.S4117C     =T2.S4117C     ,T1.S4120C     =T2.S4120C     ,T1.S4121C     =T2.S4121C     ,T1.S4122C     =T2.S4122C     ,T1.S4123C     =T2.S4123C     ,T1.S4124C     =T2.S4124C     ,T1.S4125C     =T2.S4125C     ,T1.S4126C     =T2.S4126C     ,T1.S4127C     =T2.S4127C     ,T1.S4130C     =T2.S4130C     ,T1.S4131C     =T2.S4131C     ,T1.S4132C     =T2.S4132C     ,T1.S4133C     =T2.S4133C     ,T1.S4134C     =T2.S4134C     ,T1.S4135C     =T2.S4135C     ,T1.S4136C     =T2.S4136C     ,T1.S4137C     =T2.S4137C     ,T1.S4140C     =T2.S4140C     ,T1.S4141C     =T2.S4141C     ,T1.S4142C     =T2.S4142C     ,T1.S4143C     =T2.S4143C     ,T1.S4144C     =T2.S4144C     ,T1.S4145C     =T2.S4145C     ,T1.S4146C     =T2.S4146C     ,T1.S4147C     =T2.S4147C     ,T1.S4150C     =T2.S4150C     ,T1.S4151C     =T2.S4151C     ,T1.S4152C     =T2.S4152C     ,T1.S4153C     =T2.S4153C     ,T1.S4154C     =T2.S4154C     ,T1.S4155C     =T2.S4155C     ,T1.S4156C     =T2.S4156C     ,T1.S4157C     =T2.S4157C     ,T1.S4160C     =T2.S4160C     ,T1.S4161C     =T2.S4161C     ,T1.S4162C     =T2.S4162C     ,T1.S4163C     =T2.S4163C     ,T1.S4164C     =T2.S4164C     ,T1.S4165C     =T2.S4165C     ,T1.S4166C     =T2.S4166C     ,T1.S4167C     =T2.S4167C     ,T1.S4170C     =T2.S4170C     ,T1.S4171C     =T2.S4171C     ,T1.S4172C     =T2.S4172C     ,T1.S4173C     =T2.S4173C     ,T1.S4174C     =T2.S4174C     ,T1.S4175C     =T2.S4175C     ,T1.S4176C     =T2.S4176C     ,T1.S4177C     =T2.S4177C     ,T1.S4100D     =T2.S4100D     ,T1.S4101D     =T2.S4101D     ,T1.S4102D     =T2.S4102D     ,T1.S4103D     =T2.S4103D     ,T1.S4104D     =T2.S4104D     ,T1.S4105D     =T2.S4105D     ,T1.S4106D     =T2.S4106D     ,T1.S4107D     =T2.S4107D     ,T1.S4110D     =T2.S4110D     ,T1.S4111D     =T2.S4111D     ,T1.S4112D     =T2.S4112D     ,T1.S4113D     =T2.S4113D     ,T1.S4114D     =T2.S4114D     ,T1.S4115D     =T2.S4115D     ,T1.S4116D     =T2.S4116D     ,T1.S4117D     =T2.S4117D     ,T1.S4120D     =T2.S4120D     ,T1.S4121D     =T2.S4121D     ,T1.S4122D     =T2.S4122D     ,T1.S4123D     =T2.S4123D     ,T1.S4124D     =T2.S4124D     ,T1.S4125D     =T2.S4125D     ,T1.S4126D     =T2.S4126D     ,T1.S4127D     =T2.S4127D     ,T1.S4130D     =T2.S4130D     ,T1.S4131D     =T2.S4131D     ,T1.S4132D     =T2.S4132D     ,T1.S4133D     =T2.S4133D     ,T1.S4134D     =T2.S4134D     ,T1.S4135D     =T2.S4135D     ,T1.S4136D     =T2.S4136D     ,T1.S4137D     =T2.S4137D     ,T1.S4140D     =T2.S4140D     ,T1.S4141D     =T2.S4141D     ,T1.S4142D     =T2.S4142D     ,T1.S4143D     =T2.S4143D     ,T1.S4144D     =T2.S4144D     ,T1.S4145D     =T2.S4145D     ,T1.S4146D     =T2.S4146D     ,T1.S4147D     =T2.S4147D     ,T1.S4150D     =T2.S4150D     ,T1.S4151D     =T2.S4151D     ,T1.S4152D     =T2.S4152D     ,T1.S4153D     =T2.S4153D     ,T1.S4154D     =T2.S4154D     ,T1.S4155D     =T2.S4155D     ,T1.S4156D     =T2.S4156D     ,T1.S4157D     =T2.S4157D     ,T1.S4160D     =T2.S4160D     ,T1.S4161D     =T2.S4161D     ,T1.S4162D     =T2.S4162D     ,T1.S4163D     =T2.S4163D     ,T1.S4164D     =T2.S4164D     ,T1.S4165D     =T2.S4165D     ,T1.S4166D     =T2.S4166D     ,T1.S4167D     =T2.S4167D     ,T1.S4170D     =T2.S4170D     ,T1.S4171D     =T2.S4171D     ,T1.S4172D     =T2.S4172D     ,T1.S4173D     =T2.S4173D     ,T1.S4174D     =T2.S4174D     ,T1.S4175D     =T2.S4175D     ,T1.S4176D     =T2.S4176D     ,T1.S4177D     =T2.S4177D  ";
//		inserttmpfields = "ID,MRR_DESC_ID,MEA_DATE,BSC,CELL,TRX_IDX,S4100C,S4101C,S4102C,S4103C,S4104C,S4105C,S4106C,S4107C,S4110C,S4111C,S4112C,S4113C,S4114C,S4115C,S4116C,S4117C,S4120C,S4121C,S4122C,S4123C,S4124C,S4125C,S4126C,S4127C,S4130C,S4131C,S4132C,S4133C,S4134C,S4135C,S4136C,S4137C,S4140C,S4141C,S4142C,S4143C,S4144C,S4145C,S4146C,S4147C,S4150C,S4151C,S4152C,S4153C,S4154C,S4155C,S4156C,S4157C,S4160C,S4161C,S4162C,S4163C,S4164C,S4165C,S4166C,S4167C,S4170C,S4171C,S4172C,S4173C,S4174C,S4175C,S4176C,S4177C,S4100D,S4101D,S4102D,S4103D,S4104D,S4105D,S4106D,S4107D,S4110D,S4111D,S4112D,S4113D,S4114D,S4115D,S4116D,S4117D,S4120D,S4121D,S4122D,S4123D,S4124D,S4125D,S4126D,S4127D,S4130D,S4131D,S4132D,S4133D,S4134D,S4135D,S4136D,S4137D,S4140D,S4141D,S4142D,S4143D,S4144D,S4145D,S4146D,S4147D,S4150D,S4151D,S4152D,S4153D,S4154D,S4155D,S4156D,S4157D,S4160D,S4161D,S4162D,S4163D,S4164D,S4165D,S4166D,S4167D,S4170D,S4171D,S4172D,S4173D,S4174D,S4175D,S4176D,S4177D";
//		inserttmpvals = "T2.MEA_DATE   ,T2.BSC        ,T2.CELL       ,T2.TRX_IDX    ,T2.S4100C     ,T2.S4101C     ,T2.S4102C     ,T2.S4103C     ,T2.S4104C     ,T2.S4105C     ,T2.S4106C     ,T2.S4107C     ,T2.S4110C     ,T2.S4111C     ,T2.S4112C     ,T2.S4113C     ,T2.S4114C     ,T2.S4115C     ,T2.S4116C     ,T2.S4117C     ,T2.S4120C     ,T2.S4121C     ,T2.S4122C     ,T2.S4123C     ,T2.S4124C     ,T2.S4125C     ,T2.S4126C     ,T2.S4127C     ,T2.S4130C     ,T2.S4131C     ,T2.S4132C     ,T2.S4133C     ,T2.S4134C     ,T2.S4135C     ,T2.S4136C     ,T2.S4137C     ,T2.S4140C     ,T2.S4141C     ,T2.S4142C     ,T2.S4143C     ,T2.S4144C     ,T2.S4145C     ,T2.S4146C     ,T2.S4147C     ,T2.S4150C     ,T2.S4151C     ,T2.S4152C     ,T2.S4153C     ,T2.S4154C     ,T2.S4155C     ,T2.S4156C     ,T2.S4157C     ,T2.S4160C     ,T2.S4161C     ,T2.S4162C     ,T2.S4163C     ,T2.S4164C     ,T2.S4165C     ,T2.S4166C     ,T2.S4167C     ,T2.S4170C     ,T2.S4171C     ,T2.S4172C     ,T2.S4173C     ,T2.S4174C     ,T2.S4175C     ,T2.S4176C     ,T2.S4177C     ,T2.S4100D     ,T2.S4101D     ,T2.S4102D     ,T2.S4103D     ,T2.S4104D     ,T2.S4105D     ,T2.S4106D     ,T2.S4107D     ,T2.S4110D     ,T2.S4111D     ,T2.S4112D     ,T2.S4113D     ,T2.S4114D     ,T2.S4115D     ,T2.S4116D     ,T2.S4117D     ,T2.S4120D     ,T2.S4121D     ,T2.S4122D     ,T2.S4123D     ,T2.S4124D     ,T2.S4125D     ,T2.S4126D     ,T2.S4127D     ,T2.S4130D     ,T2.S4131D     ,T2.S4132D     ,T2.S4133D     ,T2.S4134D     ,T2.S4135D     ,T2.S4136D     ,T2.S4137D     ,T2.S4140D     ,T2.S4141D     ,T2.S4142D     ,T2.S4143D     ,T2.S4144D     ,T2.S4145D     ,T2.S4146D     ,T2.S4147D     ,T2.S4150D     ,T2.S4151D     ,T2.S4152D     ,T2.S4153D     ,T2.S4154D     ,T2.S4155D     ,T2.S4156D     ,T2.S4157D     ,T2.S4160D     ,T2.S4161D     ,T2.S4162D     ,T2.S4163D     ,T2.S4164D     ,T2.S4165D     ,T2.S4166D     ,T2.S4167D     ,T2.S4170D     ,T2.S4171D     ,T2.S4172D     ,T2.S4173D     ,T2.S4174D     ,T2.S4175D     ,T2.S4176D     ,T2.S4177D  ";
//		translateSql = " MERGE INTO RNO_2G_HW_MRR_HRATE T1                   "
//				+ " USING RNO_2G_HW_MRR_HRATE_T T2                      "
//				+ " ON (T1.BSC = T2.BSC AND T1.MEA_DATE = T2.MEA_DATE)  "
//				+ " WHEN MATCHED THEN                                   "
//				+ "   UPDATE SET " + updatetmpkeyToval + "                  "
//				+ "                                                     "
//				+ "                                                     "
//				+ " WHEN NOT MATCHED THEN                               "
//				+ "   INSERT                                            "
//				+ "     (                                               "
//				+ " 	   " + inserttmpfields + "                             "
//				+ " 	)                                                  "
//				+ "   VALUES                                            "
//				+ "     (seq_RNO_2G_HW_MRR_HRATE.nextval,               "
//				+ "      " + mrrId + ",                                     "
//				+ "       " + inserttmpvals + "                             "
//				+ " 	 )                                                 ";
//		log.info(">>>>>>>>>>>>>>>转移半速率信息中间表数据到目标表的sql：" + translateSql);
//		try {
//			statement.executeUpdate(translateSql);
//		} catch (SQLException e) {
//			log.error("转移半速率信息中间表数据到目标表的sql出错：" + translateSql);
//			e.printStackTrace();
//			context.closeAllStatement();
//			try {
//				statement.close();
//			} catch (SQLException e1) {
//				e1.printStackTrace();
//			}
//			msg = "数据处理出错！code=mrr-11";
//			log.error(msg);
//			super.setCachedInfo(token, msg);
//			try {
//				stmt.close();
//			} catch (Exception e3) {
//
//			}
//			return;
//		}
//		log.info("<<<<<<<<<<<<<<<完成转移半速率信息中间表数据到目标表");

		try {
			statement.close();
		} catch (Exception e3) {

		}
	}

	/**
	 * 清理资源
	 * 
	 * @param destDir
	 * @param context
	 * @author chao.xj
	 * @date 2014-8-2上午11:31:00
	 */
	private void clearResource(String destDir, HW2GMrrParserContext context) {
		FileTool.deleteDir(destDir);
		if (context != null) {
			context.closeAllStatement();
		}
	}

	public static boolean unZip(String zipfile, String destDir) {

		destDir = destDir.endsWith("//") ? destDir : destDir + "//";
		byte b[] = new byte[1024];
		int length;

		ZipFile zipFile;
		try {
			zipFile = new ZipFile(new File(zipfile));
			Enumeration enumeration = zipFile.entries();// zipFile.getEntries();

			ZipEntry zipEntry = null;

			while (enumeration.hasMoreElements()) {
				zipEntry = (ZipEntry) enumeration.nextElement();
				File loadFile = new File(destDir + zipEntry.getName());
				System.out.println("loadFile:" + loadFile);
				if (zipEntry.isDirectory()) {
					// 这段都可以不要，因为每次都貌似从最底层开始遍历的
					System.out.println("loadFile.mkdirs()");
					loadFile.mkdirs();
				} else {
					if (!loadFile.getParentFile().exists())
						loadFile.getParentFile().mkdirs();
					OutputStream outputStream = new FileOutputStream(loadFile);
					System.out.println("zipEntry:" + zipEntry.getSize());
					InputStream inputStream = zipFile.getInputStream(zipEntry);
					System.out.println("inputStream:" + inputStream);
					while ((length = inputStream.read(b)) > 0) {
						outputStream.write(b, 0, length);
					}
					outputStream.close();
				}
			}
			// System.out.println(" 文件解压成功 ");
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) {

		String aa = "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";
		String bb[] = aa.split(",");
		System.out.println(bb.length);
		/*
		 * try { System.out.println(new Float("100%"));
		 * System.out.println(Float.parseFloat("100%".toString())); } catch
		 * (Exception e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		// unZip("D:\\frateandhrate.zip", "D:\\TMP");
		/*
		 * try { File csv = new File("D:\\半速率(0-7).csv"); // CSV文件
		 * BufferedReader br = new BufferedReader(new InputStreamReader(new
		 * FileInputStream(csv),"GBK")); // 读取直到最后一行 String line = ""; int
		 * validline=0; int splitj=0; while ((line = br.readLine()) != null) {
		 * splitj++; StringTokenizer st = new StringTokenizer(line, ","); if
		 * (splitj==1) { // 把一行数据分割成多个字段 System.out.println("line:"+line);
		 * System.out.println("st.countTokens():"+st.countTokens()); continue; }
		 * 
		 * 
		 * 
		 * 
		 * if(st.countTokens()!=136){ continue; }
		 * System.out.println("countTokens:"+st.countTokens());
		 * 
		 * while (st.hasMoreTokens()) { // 每一行的多个字段用TAB隔开表示
		 * 
		 * String aa=st.nextToken(); System.out.print(aa+splitj+"\t"); String
		 * bb=st.nextToken(); System.out.print(bb+splitj+"\t"); // }
		 * 
		 * validline++; }
		 * 
		 * br.close(); System.out.println("有效行："+validline); }catch (IOException
		 * e) { // 捕获BufferedReader对象关闭时的异常 e.printStackTrace(); }
		 */
	}
	private void printTmpTable(String tab, Statement stmt) {
		// TODO Auto-generated method stub
		String sql="select * from "+tab;
		log.debug("printTmpTable sql:"+sql);
		List<Map<String,Object>> res=RnoHelper.commonQuery(stmt, sql);
		if(res!=null&&res.size()>0){
			log.debug(tab+"\r\n---数据如下：");
			for(Map<String,Object> one:res){
				log.debug(one);
			}
			log.debug(tab+"---数据展示完毕\r\n");
		}else{
			log.debug(tab+"---数据为空！");
		}
	}
}
