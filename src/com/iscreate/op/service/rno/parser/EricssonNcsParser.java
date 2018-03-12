package com.iscreate.op.service.rno.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.dao.rno.RnoStructureAnalysisDao;
import com.iscreate.op.service.rno.parser.vo.NcsAdmRecord;
import com.iscreate.op.service.rno.parser.vo.NcsCellData;
import com.iscreate.op.service.rno.parser.vo.NcsNcellData;
import com.iscreate.op.service.rno.parser.vo.NcsRecord;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.FileTool;
import com.iscreate.op.service.rno.tool.TranslateTools;
import com.iscreate.op.service.rno.tool.ZipFileHandler;

public class EricssonNcsParser extends TxtFileParserBase {
	private static Log log = LogFactory.getLog(EricssonNcsParser.class);
	private static int AdministratorRecordLen = 52;

	private RnoStructureAnalysisDao rnoStructureAnalysisDao;
	// 语句
	// 管理头部信息
	private static String insertAdmDataSql = "insert into RNO_NCS_DESCRIPTOR (RNO_NCS_DESC_ID,AREA_ID,NAME,START_TIME,RECORD_COUNT,SEGTIME,RELSSN,ABSS,NUMFREQ,RECTIME,NET_TYPE,VENDOR,CREATE_TIME,MOD_TIME,STATUS,FILE_FORMAT,RID,TERM_REASON,ECNOABSS,RELSS_SIGN,RELSS,RELSS2_SIGN,RELSS2,RELSS3_SIGN,RELSS3,RELSS4_SIGN,RELSS4,RELSS5_SIGN,RELSS5,NCELLTYPE,NUCELLTYPE,TFDDMRR,NUMUMFI,TNCCPERM_INDICATOR,TNCCPERM_BITMAP,TMBCR,CITY_ID)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	// 插入小区信息
	private static String insertCellDataSql = "insert into rno_ncs_cell_data(ID,RNO_NCS_DESC_ID,CELL,CHGR,REP,REPHR,REPUNDEFGSM,AVSS) values(SEQ_RNO_NCS_CELL_DATA.NEXTVAL,?,?,?,?,?,?,?)";
	// 邻区测量信息
	private static String insertNcellToMidDataSql = "insert into RNO_NCS_MID(RNO_NCS_DESC_ID,CELL,CHGR,BSIC,ARFCN,DEFINED_NEIGHBOUR,RECTIMEARFCN,REPARFCN,TIMES,NAVSS,TIMES1,NAVSS1,TIMES2,NAVSS2,TIMES3,NAVSS3,TIMES4,NAVSS4,TIMES5,NAVSS5,TIMES6,NAVSS6,TIMESRELSS,TIMESRELSS2,TIMESRELSS3,TIMESRELSS4,TIMESRELSS5,TIMESABSS,TIMESALONE,NCELL) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,' ')";

	public RnoStructureAnalysisDao getRnoStructureAnalysisDao() {
		return rnoStructureAnalysisDao;
	}

	public void setRnoStructureAnalysisDao(
			RnoStructureAnalysisDao rnoStructureAnalysisDao) {
		this.rnoStructureAnalysisDao = rnoStructureAnalysisDao;
	}

	@Override
	protected boolean parseDataInternal(String token, File file,
			boolean needPersist, boolean update, long oldConfigId, long areaId,
			boolean autoload, Map<String, Object> attachParams) {
		log.debug("进入EricssonNcsParser方法：parseDataInternal。 token=" + token
				+ ",file=" + file + ",needPersist=" + needPersist + ",update="
				+ update + ",oldConfigId=" + oldConfigId + ",areaId=" + areaId
				+ ",attachParams=" + attachParams);
		// 如果是压缩包，需要先解压缩
		if (file == null) {
			return false;
		}

		setTokenInfo(token, -1f, "正在检查文件有效性");
		long st = System.currentTimeMillis();
		String msg = "";
		// 检查必要参数
		if (attachParams == null || attachParams.isEmpty()) {
			msg = "缺少bsc信息、区域信息、频段信息！";
			log.error(msg);
			super.setCachedInfo(token, msg);
			return false;
		}
//		String bscName ="";// attachParams.get("bsc") + "";

//		if (bscName == null || "".equals(bscName.trim())) {
//			msg = "缺少bsc信息";
//			log.error(msg);
//			super.setCachedInfo(token, msg);
//			return false;
//		}
//		String freqSection ="";// attachParams.get("freqsection") + "";
//		if (freqSection == null || "".equals(freqSection.trim())) {
//			msg = "缺少频段信息！";
//			log.error(msg);
//			super.setCachedInfo(token, msg);
//			return false;
//		}

		String city=attachParams.get("cityId")+"";
		long cityId=-1;
		try{
			cityId=Long.parseLong(city);
		}catch(Exception e){
			e.printStackTrace();
			msg = "城市信息有误！";
			log.error(msg);
			super.setCachedInfo(token, msg);
			return false;
		}
//		bscName = bscName.trim();
//		freqSection = freqSection.trim();

//		if (!"gsm900".equalsIgnoreCase(freqSection)
//				&& !"gsm1800".equalsIgnoreCase(freqSection)) {
//			msg = "频段信息无效！";
//			log.error(msg);
//			super.setCachedInfo(token, msg);
//			return false;
//		}

		String fileName = (String) attachParams.get("fileName");
		List<File> allNcsFiles = new ArrayList<File>();// 将所有待处理的ncs文件放置在这个列表里
		boolean fromZip = false;
		String destDir = "";
		if (fileName.endsWith(".zip")
				||fileName.endsWith("ZIP")
				|| fileName.endsWith("Zip")
				) {
			setTokenInfo(token, "正在解析压缩包");
			fromZip = true;
			// 压缩包
			log.info("上传的ncs文件是一个压缩包。");

			// 进行解压
			String path = file.getParentFile().getPath();
			destDir = path + "/"
					+ UUID.randomUUID().toString().replaceAll("-", "");

			//
			boolean unzipOk =false;
			try{
				unzipOk=ZipFileHandler.unZip(file.getAbsolutePath(), destDir);
			}catch(Exception e){
				msg="压缩包解析失败！请确认压缩包文件是否被破坏！";
				log.error(msg);
				super.setCachedInfo(token, msg);
				clearResource(destDir, null);
				return false;
			}
			if (!unzipOk) {
				msg = "解压失败 ！仅支持zip格式的压缩包！ ";
				log.error(msg);
				super.setCachedInfo(token, msg);
				clearResource(destDir, null);
				return false;
			}
			file = new File(destDir);
			File[] files = file.listFiles();
			//判断文件数量，不要超过规定数量
			if(files.length>50){
				msg="一次上传的ncs文件数量:["+files.length+"]超过了规定的最大数量：50";
				log.error(msg);
				super.setCachedInfo(token, msg);
				clearResource(destDir, null);
				return false;
			}
			for (File f : files) {
				// 只要文件，不要目录
				if (f.isFile() && !f.isHidden()) {
					allNcsFiles.add(f);
				}
			}
		}else if(fileName.endsWith(".rar")){
			msg = "请用zip格式压缩文件！";
			log.error(msg);
			super.setCachedInfo(token, msg);
			clearResource(destDir, null);
			return false;
		}
		else {
			log.info("上传的ncs是一个普通文件。");
			allNcsFiles.add(file);

		}

		if (allNcsFiles.isEmpty()) {
			msg = "未上传有效的ncs文件！zip包里不能再包含有文件夹！";
			log.error(msg);
			super.setCachedInfo(token, msg);
			clearResource(destDir, null);
			return false;
		}

		// 开始分析
		ParserContext context = new ParserContext();
		// 设置必要的信息
		// 1、设置statment
		// 创建statement
		PreparedStatement insertAdmStmt = null;
		try {
			insertAdmStmt = connection.prepareStatement(insertAdmDataSql);
		} catch (SQLException e1) {
			e1.printStackTrace();
			msg = "数据处理出错！code=ncs-1";
			log.error(msg);
			super.setCachedInfo(token, msg);
			clearResource(destDir, context);
			return false;
		}
		context.setPreparedStatment("NcsAdmRecord", insertAdmStmt);

		PreparedStatement insertCellDataStmt = null;
		try {
			insertCellDataStmt = connection.prepareStatement(insertCellDataSql);
		} catch (SQLException e1) {
			e1.printStackTrace();
			msg = "数据处理出错！code=ncs-2";
			log.error(msg);
			super.setCachedInfo(token, msg);
			clearResource(destDir, context);
			return false;
		}
		context.setPreparedStatment("NcsCellData", insertCellDataStmt);

		PreparedStatement insertNCellDataStmt = null;
		try {
			insertNCellDataStmt = connection
					.prepareStatement(insertNcellToMidDataSql);
		} catch (SQLException e1) {
			e1.printStackTrace();
			msg = "数据处理出错！code=ncs-3";
			log.error(msg);
			super.setCachedInfo(token, msg);
			clearResource(destDir, context);
			return false;
		}
		context.setPreparedStatment("NcsNcellData", insertNCellDataStmt);

		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
			msg = "数据处理出错！code=ncs-4";
			log.error(msg);
			super.setCachedInfo(token, msg);
			clearResource(destDir, context);
			return false;
		}

		// 2、设置bscname，area，freqsection
		context.setAreaId(areaId);
		context.setCityId(cityId);
//		context.setBscName(bscName);
//		context.setFreqSection(freqSection);

		// -------------
		for (File f : allNcsFiles) {
			setTokenInfo(token, "正在解析NCS文件");
			try {
				if (fromZip) {
					parseNcs(f.getName(), f, context);
				} else {
					parseNcs(fileName, f, context);
				}
			} catch (Exception e) {
				e.printStackTrace();
				msg = "解析文件出错！请检查是否按要求上传文件！";
				log.error(msg);
				super.setCachedInfo(token, msg);
				clearResource(destDir, context);
				return false;
			}
		}

		setTokenInfo(token, "正在进行数据处理");
		// 执行批处理插入
		// ----ncs报告信息----//
		log.info("准备批处理插入ncs报告信息。。。");
		PreparedStatement stmt = context.getPreparedStatement("NcsAdmRecord");
		try {
			stmt.executeBatch();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		// ---小区数据-----//
		log.info("准备批处理插入ncs小区测量信息。。。");
		stmt = context.getPreparedStatement("NcsCellData");
		try {
			stmt.executeBatch();
		} catch (SQLException e2) {
			e2.printStackTrace();
			clearResource(destDir, context);
			try {
				stmt.close();
			} catch (Exception e3) {

			}
			return false;
		}

		// ---邻区测量数据-----//
		log.info("准备批处理插入ncs邻区测量信息。。。");
		stmt = context.getPreparedStatement("NcsNcellData");
		try {
			stmt.executeBatch();
		} catch (SQLException e2) {
			e2.printStackTrace();
			clearResource(destDir, context);
			try {
				stmt.close();
			} catch (Exception e3) {

			}
			return false;
		}

		long t1, t2;
		List<Long> ncsIds = context.getAllNcsId();
		// 邻区匹配
		log.info(">>>>>>>>>>>>>>>开始邻区匹配...");
		setTokenInfo(token, "正在进行邻区数据处理");
		t1 = System.currentTimeMillis();
		rnoStructureAnalysisDao
				.matchNcsNcell(connection, "RNO_NCS_MID", ncsIds,cityId);
		t2 = System.currentTimeMillis();
		log.info("<<<<<<<<<<<<<<<完成邻区匹配。耗时:" + (t2 - t1) + "ms");

		// 计算干扰度
		//2014-7-11 gmh 注释掉干扰度的计算，由分析时进行计算
//		log.info(">>>>>>>>>>>>>>>开始计算干扰度...");
//		setTokenInfo(token, "正在计算干扰度");
//		t1 = t2;
//		rnoStructureAnalysisDao.calculateInterfer(connection, "RNO_NCS_MID",
//				ncsIds);
//		t2 = System.currentTimeMillis();
//		log.info("<<<<<<<<<<<<<<<完成计算干扰度。耗时:" + (t2 - t1) + "ms");
//		//------------------以下转为用到的时候才计算---------------------//
//
//		// 计算干扰矩阵
//		log.info(">>>>>>>>>>>>>>>开始计算干扰矩阵...");
//		setTokenInfo(token, "正在计算干扰矩阵");
//		t1 = t2;
//		rnoStructureAnalysisDao.calculateInterferMatrix(connection,
//				"RNO_NCS_MID", "", ncsIds);
//		t2 = System.currentTimeMillis();
//		log.info("<<<<<<<<<<<<<<<完成计算干扰矩阵。耗时:" + (t2 - t1) + "ms");
//
//		// 最大连通簇
//		log.info(">>>>>>>>>>>>>>>开始计算最大连通簇...");
//		setTokenInfo(token, "正在计算最大连通簇");
//		t1 = t2;
//		rnoStructureAnalysisDao.calculateConnectedCluster(connection,
//				"RNO_NCS_MID", ncsIds, "RNO_NCS_CLUSTER", "RNO_NCS_CLUSTER_CELL","RNO_NCS_CLUSTER_CELL_RELA",0.05f);
//		t2 = System.currentTimeMillis();
//		log.info("<<<<<<<<<<<<<<<完成计算最大连通簇。耗时:" + (t2 - t1) + "ms");
//
//		// 簇约束因子
//		log.info(">>>>>>>>>>>>>>>开始计算簇约束因子...");
//		setTokenInfo(token, "正在计算簇约束因子");
//		t1 = t2;
//		rnoStructureAnalysisDao.calculateClusterConstrain(connection, 
//				"RNO_NCS_CLUSTER", "RNO_NCS_CLUSTER_CELL", ncsIds);
//		t2 = System.currentTimeMillis();
//		log.info("<<<<<<<<<<<<<<<完成计算簇约束因子。耗时:" + (t2 - t1) + "ms");
//
//		// 簇权重
//		log.info(">>>>>>>>>>>>>>>开始计算簇权重...");
//		setTokenInfo(token, "正在计算簇权重");
//		t1 = t2;
//		rnoStructureAnalysisDao.calculateNcsClusterWeight(connection,
//				"RNO_NCS_MID","RNO_NCS_CLUSTER","RNO_NCS_CLUSTER_CELL", ncsIds, Arrays.asList(-1L, -2L), true, 0.5f);
//		t2 = System.currentTimeMillis();
//		log.info("<<<<<<<<<<<<<<<完成计算簇权重。耗时:" + (t2 - t1) + "ms");
//		
//		//计算小区相关的指标
//		log.info(">>>>>>>>>>>>>>>开始计算小区相关指标...");
//		setTokenInfo(token, "正在计算小区相关指标");
//		t1 = t2;
//		rnoStructureAnalysisDao.calculateCellRes(connection,"RNO_NCS_MID","RNO_NCS_CELL_ANA_RESULT","RNO_NCS_CLUSTER","RNO_NCS_CLUSTER_CELL",ncsIds,null);
//		t2 = System.currentTimeMillis();
//		log.info("<<<<<<<<<<<<<<<完成计算小区相关指标。耗时:" + (t2 - t1) + "ms");
//		
		//--------------------以上用到的时候才计算------------------------//
		
		
		//通过程序来判断bsc、频段，不需要用户填写
		log.info(">>>>>>开始自动计算ncs所测量的bsc、测量小区的频段");
		setTokenInfo(token, "正在匹配ncs所测量的bsc、小区频段");
		t1 = t2;
		rnoStructureAnalysisDao.matchNcsBscAndFreqSection(connection,"RNO_NCS_MID","RNO_NCS_DESCRIPTOR",ncsIds);
		t2 = System.currentTimeMillis();
		log.info("<<<<<<<<<<<<<<<完成计算ncs所测量的bsc、测量小区的频段。耗时:" + (t2 - t1) + "ms");
		
		
		// 转移中间表数据到正式表
		String fields = "RNO_NCS_DESC_ID,CELL,NCELL,CHGR,BSIC,ARFCN,DEFINED_NEIGHBOUR,RECTIMEARFCN,REPARFCN,TIMES,NAVSS,TIMES1,NAVSS1,TIMES2,NAVSS2,TIMES3,NAVSS3,TIMES4,NAVSS4,TIMES5,NAVSS5,TIMES6,NAVSS6,TIMESRELSS,TIMESRELSS2,TIMESRELSS3,TIMESRELSS4,TIMESRELSS5,TIMESABSS,TIMESALONE,DISTANCE,INTERFER,CA_INTERFER,NCELLS,CELL_FREQ_CNT,NCELL_FREQ_CNT,SAME_FREQ_CNT,ADJ_FREQ_CNT,CI_DIVIDER,CA_DIVIDER";
		String translateSql = "insert into rno_ncs(rno_ncs_id," + fields
				+ ") SELECT SEQ_RNO_NCS.nextval," + fields
				+ " from rno_ncs_mid";
		t1 = t2;
		log.info(">>>>>>>>>>>>>>>转移中间表数据到目标表的sql：" + translateSql);
		t1 = t2;
		try {
			statement.executeUpdate(translateSql);
		} catch (SQLException e) {
			e.printStackTrace();
			context.closeAllStatement();
			try {
				statement.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			msg = "数据处理出错！code=ncs-5";
			log.error(msg);
			super.setCachedInfo(token, msg);
			clearResource(destDir, context);
			try {
				stmt.close();
			} catch (Exception e3) {

			}
			return false;
		}
		t2 = System.currentTimeMillis();
		log.info("<<<<<<<<<<<<<<<完成转移中间表数据到目标表，耗时:" + (t2 - t1) + "ms");
		long et = System.currentTimeMillis();
		log.info("退出EricssonNcsParser解析方法，耗时：" + (et - st) + "ms");
		clearResource(destDir, context);
		try {
			stmt.close();
		} catch (Exception e3) {

		}
		super.setCachedInfo(token, "解析完成！");
		return true;
	}

	/**
	 * 清理资源
	 * 
	 * @param destDir
	 * @param context
	 * @author brightming 2014-1-20 上午10:51:14
	 */
	private void clearResource(String destDir, ParserContext context) {
		FileTool.deleteDir(destDir);
		if (context != null) {
			context.closeAllStatement();
		}
	}

	public Map<String, Long> parseNcs(String fileName, File file,
			ParserContext context) throws Exception {
		InputStream is = new FileInputStream(file);
		byte[] typeByte = new byte[1];
		byte[] lenByte = new byte[2];
		byte[] content = new byte[512];

//		int cnt = 0;

		int len = -1;
		int type = -1;
		NcsRecord rec = null;
		Map<String, Long> result = new HashMap<String, Long>();
		long ncsId = -1;// 预先申请的ncsid
		while ((len = is.read(typeByte, 0, 1)) != -1) {
			type = -1;
			len = is.read(lenByte, 0, 2);
			if (len != 2) {
				log.error("异常中断或格式错误！");
				context.appedErrorMsg("文件:[" + file.getName()
						+ "]异常中断或格式错误！<br/>");
				break;
			}
			len = TranslateTools.makeIntFromByteArray(lenByte, 0, 2);
			if (len <= 0) {
				log.error("长度信息有误！");
				context.appedErrorMsg("文件:[" + file.getName()
						+ "]的长度信息有误！<br/>");
				break;
			}
			is.read(content, 0, len - 3);// 减去type占用的1个字节，length内容占用的2个字节
			byte[] wholeSection = mergeByte(typeByte, lenByte,
					TranslateTools.subByte(content, 0, len - 3));

			type = TranslateTools.makeIntFromByteArray(typeByte, 0, 1);
			rec = handleSection(type, wholeSection, context);

			// 处理rec
			if (rec != null) {
				if (ncsId == -1) {
					// 申请id
					ncsId = super.getNextSeqValue("seq_rno_ncs_descriptor",
							connection);
				}
				// 转换成sql语句
				prepareRecordSql(ncsId, rec, context);
			}
		}

		if (ncsId != -1) {
			PreparedStatement stmt = context
					.getPreparedStatement("NcsAdmRecord");
			setAdmRecordToStmt(fileName, stmt, ncsId, context.getAreaId(),
					context.getCityId(),
					context.getLastNcsAdmRecord(ncsId),context.getDateUtil());
		}
		is.close();

		return result;
	}

	/**
	 * 准备批处理插入语句
	 * 
	 * @param ncsId
	 * @param rec
	 * @param context
	 * @author brightming 2014-1-13 下午2:10:46
	 */
	private void prepareRecordSql(long ncsId, NcsRecord rec,
			ParserContext context) {
		if (rec instanceof NcsAdmRecord) {
			// 特别处理
			context.addNcsAdmRecord(ncsId, (NcsAdmRecord) rec);
		} else if (rec instanceof NcsCellData) {
			PreparedStatement stmt = context
					.getPreparedStatement("NcsCellData");
			setCellDataToStmt(stmt, ncsId, (NcsCellData) rec);
		} else if (rec instanceof NcsNcellData) {
			PreparedStatement stmt = context
					.getPreparedStatement("NcsNcellData");
			setNcellDataToStmt(stmt, ncsId, (NcsNcellData) rec);
		} else {
			log.warn("暂时不处理此类型记录！");
		}

	}

	/**
	 * 设置管理头部信息
	 * 
	 * @param stmt
	 * @param ncsId
	 * @param rec
	 * @author brightming 2014-1-13 下午3:33:40
	 */
	private void setAdmRecordToStmt(String fileName, PreparedStatement stmt,
			long ncsId, long areaId, long cityId,
			NcsAdmRecord rec,DateUtil dateUtil) {
		if (stmt == null || rec == null) {
			return;
		}
		int index = 1;
		Date st = rec.getStartTime(dateUtil);
		if (st == null) {
			log.error("管理头部：" + rec + " 的记录开始时间有问题！");
			st = new Date();
			return;
		}
		java.sql.Timestamp now = new java.sql.Timestamp(new Date().getTime());
		try {
			stmt.setLong(index++, ncsId);
			stmt.setLong(index++, areaId);
			stmt.setString(index++, fileName);
//			stmt.setString(index++, bsc);
//			stmt.setString(index++, freqSection);

			stmt.setTimestamp(index++, new java.sql.Timestamp(st.getTime()));
			stmt.setLong(index++, rec.getRecordInfo());// 最后一个的改字段为record count
			stmt.setInt(index++, rec.getSegTime());
			stmt.setNull(index++, Types.INTEGER);
			stmt.setInt(index++, rec.getAbss());
			stmt.setInt(index++, rec.getNumFreq());

			stmt.setInt(index++, rec.getRecTime());
			stmt.setString(index++, "GSM");
			stmt.setString(index++, "ERICSSON");
			stmt.setTimestamp(index++, now);
			stmt.setTimestamp(index++, now);
			stmt.setString(index++, "N");

			stmt.setInt(index++, rec.getFileFormat());
			stmt.setString(index++, rec.getRid());
			stmt.setInt(index++, rec.getTermReason());
			stmt.setInt(index++, rec.getEcnoAbss());

			stmt.setInt(index++, rec.getRelssSign());
			stmt.setInt(index++, rec.getRelss());
			stmt.setInt(index++, rec.getRelss2Sign());
			stmt.setInt(index++, rec.getRelss2());
			stmt.setInt(index++, rec.getRelss3Sign());
			stmt.setInt(index++, rec.getRelss3());
			stmt.setInt(index++, rec.getRelss4Sign());
			stmt.setInt(index++, rec.getRelss4());
			stmt.setInt(index++, rec.getRelss5Sign());
			stmt.setInt(index++, rec.getRelss5());

			stmt.setInt(index++, rec.getNcellType());
			stmt.setInt(index++, rec.getNUcellType());
			stmt.setInt(index++, rec.getTfddMrr());
			stmt.setInt(index++, rec.getNumUmfi());
			stmt.setInt(index++, rec.getTnccpermValIndi());
			stmt.setInt(index++, rec.getTnccpermBitmap());
			stmt.setInt(index++, rec.getTmbcr());
			stmt.setLong(index++, cityId);

			stmt.addBatch();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置小区测量信息
	 * 
	 * @param stmt
	 * @param ncsId
	 * @param rec
	 * @author brightming 2014-1-13 下午3:16:46
	 */
	private void setCellDataToStmt(PreparedStatement stmt, long ncsId,
			NcsCellData rec) {
		if (stmt == null || rec == null) {
			return;
		}

		int index = 1;
		// RNO_NCS_DESC_ID,CELL,CHGR,REP,REPHR,REPUNDEFGSM,AVSS
		try {
			stmt.setLong(index++, ncsId);
			stmt.setString(index++, rec.getCellName());
			stmt.setInt(index++, rec.getChgr());
			stmt.setLong(index++, rec.getRep());
			stmt.setLong(index++, rec.getRepHr());
			stmt.setLong(index++, rec.getRepUndefGsm());
			stmt.setInt(index++, rec.getAvss());

			stmt.addBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置ncell的测量结果到批处理stmt
	 * 
	 * @param stmt
	 * @param ncsId
	 * @param rec
	 * @author brightming 2014-1-13 下午2:40:37
	 */
	private void setNcellDataToStmt(PreparedStatement stmt, long ncsId,
			NcsNcellData rec) {
		if (stmt == null || rec == null) {
			return;
		}

		int index = 1;
		try {
			stmt.setLong(index++, ncsId);
			stmt.setString(index++, rec.getCellName());
			stmt.setInt(index++, rec.getChgr());
			stmt.setString(index++, rec.getNcellBsicStr());
			stmt.setInt(index++, rec.getArfcn());
			stmt.setInt(index++, rec.getDefinedAsNcell());
			stmt.setLong(index++, rec.getRecTimeArfcn());
			stmt.setLong(index++, rec.getRepArfcn());
			stmt.setLong(index++, rec.getTimes());
			stmt.setInt(index++, rec.getNavss());
			stmt.setLong(index++, rec.getTimes1());
			stmt.setInt(index++, rec.getNavss1());
			stmt.setLong(index++, rec.getTimes2());
			stmt.setInt(index++, rec.getNavss2());

			stmt.setLong(index++, rec.getTimes3());
			stmt.setInt(index++, rec.getNavss3());
			stmt.setLong(index++, rec.getTimes4());
			stmt.setInt(index++, rec.getNavss4());
			stmt.setLong(index++, rec.getTimes5());
			stmt.setInt(index++, rec.getNavss5());
			stmt.setLong(index++, rec.getTimes6());
			stmt.setInt(index++, rec.getNavss6());

			stmt.setLong(index++, rec.getTimesRelss());
			stmt.setLong(index++, rec.getTimesRelss2());
			stmt.setLong(index++, rec.getTimesRelss3());
			stmt.setLong(index++, rec.getTimesRelss4());
			stmt.setLong(index++, rec.getTimesRelss5());

			stmt.setLong(index++, rec.getTimesAbss());
			stmt.setLong(index++, rec.getTimesAlone());

			stmt.addBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static byte[] mergeByte(byte[] typeByte, byte[] lenByte,
			byte[] content) {
		int len1 = typeByte.length;
		int len2 = lenByte.length;
		int len3 = content.length;
		byte[] whole = new byte[len1 + len2 + len3];
		whole[0] = typeByte[0];
		whole[1] = lenByte[0];
		whole[2] = lenByte[1];
		int start = 3;
		for (int i = 0; i < len3; i++) {
			whole[start + i] = content[i];
		}
		return whole;
	}

	public NcsRecord handleSection(int type, byte[] wholeSection,
			ParserContext context) {
		NcsRecord rec = null;
		switch (type) {
		case 50:
			rec = parseAdministrator(wholeSection, context);
			break;
		case 51:
			rec = parseCellDataSection(wholeSection, context);
			break;
		case 52:
			rec = parseNcellData(wholeSection, context);
			break;
		default:
			// System.out.print("无法处理type=" + type + "的内容！");
			break;
		}

		return rec;
	}

	/**
	 * 解析管理字段
	 * 
	 * @param buf
	 */
	public NcsRecord parseAdministrator(byte[] buf, ParserContext context) {
		if (buf.length < AdministratorRecordLen) {
			return null;
		}
		NcsAdmRecord rec = context.getNcsAdmRecord();
//		StringBuilder builder = new StringBuilder();
		// builder.append("\r\n----------------administrator section -------------------\r\n");
		// 1 Record type
		int recType = TranslateTools.byte2Int(buf, 1);
		// System.out.println("1 recType=" + recTypes.get(recType));
		rec.setRecType(recType);
		// 2 Record length
		int recLen = TranslateTools.byte2Int(TranslateTools.subByte(buf, 1, 2),
				2);
		// System.out.println("2 recLen=" + recLen);
		rec.setLength(recLen);

		// 3 File format rev. number = 60
		int fileFormat = TranslateTools.byte2Int(
				TranslateTools.subByte(buf, 3, 1), 1);
		// System.out.println("3 fileFormat=" + fileFormat);
		rec.setFileFormat(fileFormat);

		// 4 Year Digit String 0-99
		String year = TranslateTools.byte2Int(
				TranslateTools.subByte(buf, 4, 1), 1) + "";
		if (year.length() == 1) {
			year = "0" + year;
		}
		// System.out.println("4 Year=" + year);
		rec.setYear(year);

		// 5 Month Digit String 01-12
		String month = TranslateTools.byte2Int(
				TranslateTools.subByte(buf, 5, 1), 1)
				+ "";
		if (month.length() == 1) {
			month = "0" + month;
		}
		// System.out.println("5 Month=" + month);
		rec.setMonth(month);

		// 6 Day Digit String 01-31
		String day = TranslateTools.byte2Int(TranslateTools.subByte(buf, 6, 1),
				1) + "";
		if (day.length() == 1) {
			day = "0" + day;
		}
		// System.out.println("6 day=" + day);
		rec.setDay(day);

		// 7 Hour
		String hour = TranslateTools.byte2Int(
				TranslateTools.subByte(buf, 7, 1), 1) + "";
		if (hour.length() == 1) {
			hour = "0" + hour;
		}
		// System.out.println("7 hour=" + hour);
		rec.setHour(hour);

		// 8 minute
		String minute = TranslateTools.byte2Int(
				TranslateTools.subByte(buf, 8, 1), 1)
				+ "";
		if (minute.length() == 1) {
			minute = "0" + minute;
		}
		// System.out.println("8 minute=" + minute);
		rec.setMinute(minute);

		// 9 second
		String second = TranslateTools.byte2Int(
				TranslateTools.subByte(buf, 9, 1), 1)
				+ "";
		if (second.length() == 1) {
			second = "0" + second;
		}
		// System.out.println("9 second=" + second);
		rec.setSecond(second);

		// 10 Record information
		int recInfo = TranslateTools.byte2Int(
				TranslateTools.subByte(buf, 10, 4), 4);
		// System.out.println("10 recInfo=" + recInfo);
		rec.setRecordInfo(recInfo);

		// 11 RID
		String rid = "";
		try {
			rid = new String(TranslateTools.subByte(buf, 14, 7), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// System.out.println("11 rid=" + rid);
		rec.setRid(rid);

		// 12 Start date for the recording Year
		String startDateYear = TranslateTools.makeIntFromByteArray(buf, 21, 1)
				+ "";
		if (startDateYear.length() == 1) {
			startDateYear = "0" + startDateYear;
		}
		// System.out.println("12 startDateYear=" + startDateYear);
		rec.setStartDateYear(startDateYear);

		// 13 Start date for the recording Month
		String startDateMonth = TranslateTools.makeIntFromByteArray(buf, 22, 1)
				+ "";
		if (startDateMonth.length() == 1) {
			startDateMonth = "0" + startDateMonth;
		}
		// System.out.println("13 startDateMonth=" + startDateMonth);
		rec.setStartMonth(startDateMonth);

		// 14 Start date for the recording Day
		String startDateDay = TranslateTools.makeIntFromByteArray(buf, 23, 1)
				+ "";
		if (startDateDay.length() == 1) {
			startDateDay = "0" + startDateDay;
		}
		// System.out.println("14 startDateDay=" + startDateDay);
		rec.setStartDay(startDateDay);

		// 15 Start time for the recording Hour
		String startDateHour = TranslateTools.makeIntFromByteArray(buf, 24, 1)
				+ "";
		if (startDateHour.length() == 1) {
			startDateHour = "0" + startDateHour;
		}
		// System.out.println("15 startDateHour=" + startDateHour);
		rec.setStartHour(startDateHour);

		// 16 Start time for the recording Minute
		String startDateMinute = TranslateTools
				.makeIntFromByteArray(buf, 25, 1) + "";
		if (startDateMinute.length() == 1) {
			startDateMinute = "0" + startDateMinute;
		}
		// System.out.println("16 startDateMinute=" + startDateMinute);
		rec.setStartMinute(startDateMinute);

		// 17 Start time for the recording Second
		String startDateSecond = TranslateTools
				.makeIntFromByteArray(buf, 26, 1) + "";
		if (startDateSecond.length() == 1) {
			startDateSecond = "0" + startDateSecond;
		}
		// System.out.println("17 startDateSecond=" + startDateSecond);
		rec.setStartSecond(startDateSecond);

		// 18 ABSS
		int abss = TranslateTools.makeIntFromByteArray(buf, 27, 1);
		// System.out.println("18 abss=" + abss);
		rec.setAbss(abss);

		// 19 RELSS sign
		int relssSign = TranslateTools.makeIntFromByteArray(buf, 28, 1);
		// System.out.println("19 relssSign=" + relssSign);
		rec.setRelssSign(relssSign);

		// 20 RELSS
		int relss = TranslateTools.makeIntFromByteArray(buf, 29, 1);
		// System.out.println("20 relss=" + relss);
		rec.setRelss(relss);

		// 21 RELSS2 sign
		int relss2Sign = TranslateTools.makeIntFromByteArray(buf, 30, 1);
		// System.out.println("21 relss2Sign=" + relss2Sign);
		rec.setRelss2Sign(relss2Sign);

		// 22 RELSS2
		int relss2 = TranslateTools.makeIntFromByteArray(buf, 31, 1);
		// System.out.println("22 relss2=" + relss2);
		rec.setRelss2(relss2);

		// 23 relss3 sign
		int relssSign3 = TranslateTools.makeIntFromByteArray(buf, 32, 1);
		// System.out.println("23 relssSign3=" + relssSign3);
		rec.setRelss3Sign(relssSign3);

		// 24 relss3
		int relss3 = TranslateTools.makeIntFromByteArray(buf, 33, 1);
		// System.out.println("24 relss3=" + relss3);
		rec.setRelss3(relss3);

		// 25 relss4 sign
		int relssSign4 = TranslateTools.makeIntFromByteArray(buf, 34, 1);
		// System.out.println("25 relssSign4=" + relssSign4);
		rec.setRelss4Sign(relssSign4);

		// 26 relss3
		int relss4 = TranslateTools.makeIntFromByteArray(buf, 35, 1);
		// System.out.println("26 relss4=" + relss4);
		rec.setRelss4(relss4);

		// 27 relss4 sign
		int relssSign5 = TranslateTools.makeIntFromByteArray(buf, 36, 1);
		// System.out.println("27 relssSign5=" + relssSign5);
		rec.setRelss5Sign(relssSign5);

		// 28 relss3
		int relss5 = TranslateTools.makeIntFromByteArray(buf, 37, 1);
		// System.out.println("28 relss5=" + relss5);
		rec.setRelss5(relss5);

		// 29 NCELLTYPE
		int ncellType = TranslateTools.makeIntFromByteArray(buf, 38, 1);
		// System.out.println("29 ncellType=" + ncellType);
		rec.setNcellType(ncellType);

		// NUMFREQ
		int numFreq = TranslateTools.makeIntFromByteArray(buf, 39, 1);
		// System.out.println("NUMFREQ =" + numFreq);
		rec.setNumFreq(numFreq);

		// SEGTIME
		int segTime = TranslateTools.makeIntFromByteArray(buf, 40, 2);
		// System.out.println("segTime =" + segTime);
		rec.setSegTime(segTime);

		// Termination reason
		int terminalReason = TranslateTools.makeIntFromByteArray(buf, 42, 1);
		// System.out.println("terminalReason =" + terminalReason);
		rec.setTermReason(terminalReason);

		// RECTIME
		int recTime = TranslateTools.makeIntFromByteArray(buf, 43, 2);
		// System.out.println("recTime =" + recTime);
		rec.setRecTime(recTime);

		// ECNOABSS
		int ecnoAbss = TranslateTools.makeIntFromByteArray(buf, 45, 1);
		// System.out.println("ecnoAbss =" + ecnoAbss);
		rec.setEcnoAbss(ecnoAbss);

		// NUCELLTYPE
		int nucellType = TranslateTools.makeIntFromByteArray(buf, 46, 1);
		// System.out.println("nucellType =" + nucellType);
		rec.setNUcellType(nucellType);

		// TFDDMRR
		int tfddMrr = TranslateTools.makeIntFromByteArray(buf, 47, 1);
		// System.out.println("tfddMrr =" + tfddMrr);
		rec.setTfddMrr(tfddMrr);

		// NUMUMFI
		int numUmfi = TranslateTools.makeIntFromByteArray(buf, 48, 1);
		// System.out.println("numUmfi =" + numUmfi);
		rec.setNumUmfi(numUmfi);

		// TNCCPERM validity indicator
		int tnccPermValIndicator = TranslateTools.makeIntFromByteArray(buf, 49,
				1);
		// System.out.println("tnccPermValIndicator =" + tnccPermValIndicator);
		rec.setTnccpermValIndi(tnccPermValIndicator);

		// TNCCPERM bitmap
		int tnccPermBitmap = TranslateTools.makeIntFromByteArray(buf, 50, 1);
		// System.out.println("tnccPermBitmap =" + tnccPermBitmap);
		rec.setTnccpermBitmap(tnccPermBitmap);

		// TMBCR
		int tmbcr = TranslateTools.makeIntFromByteArray(buf, 51, 1);
		// System.out.println("tmbcr =" + tmbcr);
		rec.setTmbcr(tmbcr);

//		builder.append(rec.toString());

		return rec;
	}

	/**
	 * 解析cell data部分的数据
	 * 
	 * @param buf
	 */
	public NcsRecord parseCellDataSection(byte[] buf, ParserContext context) {
//		StringBuilder builder = new StringBuilder();
//		builder.append("\r\n--------cell Data ---------\r\n");
		NcsCellData rec = context.getNcsCellData();
		// rectype
		int recType = TranslateTools.makeIntFromByteArray(buf, 0, 1);
		// builder.append("recType="+recType);
		rec.setRecType(recType);

		// record length
		int recLen = TranslateTools.makeIntFromByteArray(buf, 1, 2);
		// builder.append(",recLen="+recLen);
		rec.setLength(recLen);

		// 文档说是8位，但实际只有7位是有效的，最后一位是看不见的字符
		// cell name
		String cellName = new String(TranslateTools.subByte(buf, 3, 7));
		// builder.append(",cellName="+cellName);
		rec.setCellName(cellName);

		// chgr
		int chgr = TranslateTools.makeIntFromByteArray(buf, 11, 1);
		// builder.append(",chgr="+chgr);
		rec.setChgr(chgr);

		// rep total number of received measurement reports
		int rep = TranslateTools.makeIntFromByteArray(buf, 12, 4);
		// builder.append(",totalNumber="+rep);
		rec.setRep(rep);

		// rephr
		int repHr = TranslateTools.makeIntFromByteArray(buf, 16, 4);
		// builder.append(",repHr="+repHr);
		rec.setRepHr(repHr);

		// REPUNDEFGSM
		int repUndefinedGsm = TranslateTools.makeIntFromByteArray(buf, 20, 4);
		// builder.append(",repUndefinedGsm="+repUndefinedGsm);
		rec.setRepUndefGsm(repUndefinedGsm);

		// avss
		int avss = TranslateTools.makeIntFromByteArray(buf, 24, 1);
		// builder.append(",avss="+avss);
		rec.setAvss(avss);

//		builder.append(rec.toString());
		// 输出

		return rec;
	}

	public NcsRecord parseNcellData(byte[] buf, ParserContext context) {
		NcsNcellData rec = context.getNcsNcellData();
//		StringBuilder builder = new StringBuilder();
//		builder.append("\r\n--------- ncell data --------\r\n");

		int recType = TranslateTools.makeIntFromByteArray(buf, 0, 1);
		rec.setRecType(recType);

		int len = TranslateTools.makeIntFromByteArray(buf, 1, 2);
		rec.setLength(len);

		// 文档说是8位，但实际只有7位是有效的，最后一位是看不见的字符
		String cellName = new String(TranslateTools.subByte(buf, 3, 7));
		rec.setCellName(cellName);

		int chgr = TranslateTools.makeIntFromByteArray(buf, 11, 1);
		rec.setChgr(chgr);

		int ncellBsic = Integer.parseInt(Integer.toOctalString(TranslateTools.makeIntFromByteArray(buf, 12, 1)));
		rec.setNcellBsic(ncellBsic);

		int arfcn = TranslateTools.makeIntFromByteArray(buf, 13, 2);
		rec.setArfcn(arfcn);

		int defined = TranslateTools.makeIntFromByteArray(buf, 15, 1);
		rec.setDefinedAsNcell(defined);

		int recTimeArfcn = TranslateTools.makeIntFromByteArray(buf, 16, 2);
		rec.setRecTimeArfcn(recTimeArfcn);

		int repArfcn = TranslateTools.makeIntFromByteArray(buf, 18, 4);
		rec.setRepArfcn(repArfcn);

		int times = TranslateTools.makeIntFromByteArray(buf, 22, 4);
		rec.setTimes(times);

		int navss = TranslateTools.makeIntFromByteArray(buf, 26, 1);
		rec.setNavss(navss);

		int times1 = TranslateTools.makeIntFromByteArray(buf, 27, 4);
		rec.setTimes1(times1);

		int navss1 = TranslateTools.makeIntFromByteArray(buf, 31, 1);
		rec.setNavss1(navss1);

		int times2 = TranslateTools.makeIntFromByteArray(buf, 32, 4);
		rec.setTimes2(times2);

		int navss2 = TranslateTools.makeIntFromByteArray(buf, 36, 1);
		rec.setNavss2(navss2);

		int times3 = TranslateTools.makeIntFromByteArray(buf, 37, 4);
		rec.setTimes3(times3);

		int navss3 = TranslateTools.makeIntFromByteArray(buf, 41, 1);
		rec.setNavss3(navss3);

		int times4 = TranslateTools.makeIntFromByteArray(buf, 42, 4);
		rec.setTimes4(times4);

		int navss4 = TranslateTools.makeIntFromByteArray(buf, 46, 1);
		rec.setNavss4(navss4);

		int times5 = TranslateTools.makeIntFromByteArray(buf, 47, 4);
		rec.setTimes5(times5);

		int navss5 = TranslateTools.makeIntFromByteArray(buf, 51, 1);
		rec.setNavss5(navss5);

		int times6 = TranslateTools.makeIntFromByteArray(buf, 52, 4);
		rec.setTimes6(times6);

		int navss6 = TranslateTools.makeIntFromByteArray(buf, 56, 1);
		rec.setNavss6(navss6);

		int timesRelss = TranslateTools.makeIntFromByteArray(buf, 57, 4);
		rec.setTimesRelss(timesRelss);

		int timesRelss2 = TranslateTools.makeIntFromByteArray(buf, 61, 4);
		rec.setTimesRelss2(timesRelss2);

		int timesRelss3 = TranslateTools.makeIntFromByteArray(buf, 65, 4);
		rec.setTimesRelss3(timesRelss3);

		int timesRelss4 = TranslateTools.makeIntFromByteArray(buf, 69, 4);
		rec.setTimesRelss4(timesRelss4);

		int timesRelss5 = TranslateTools.makeIntFromByteArray(buf, 73, 4);
		rec.setTimesRelss5(timesRelss5);

		int timesAbss = TranslateTools.makeIntFromByteArray(buf, 77, 4);
		rec.setTimesAbss(timesAbss);

		int timesAlone = TranslateTools.makeIntFromByteArray(buf, 81, 4);
		rec.setTimesAlone(timesAlone);

//		builder.append(rec.toString());
		return rec;
	}
}
