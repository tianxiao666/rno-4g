package com.iscreate.op.service.rno.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.rno.RnoStructureAnalysisDao;
import com.iscreate.op.service.rno.parser.vo.MrrActulTimingAdvanceRec;
import com.iscreate.op.service.rno.parser.vo.MrrAdmRecord;
import com.iscreate.op.service.rno.parser.vo.MrrFrameErasureRateRec;
import com.iscreate.op.service.rno.parser.vo.MrrNumOfMeaResultsRec;
import com.iscreate.op.service.rno.parser.vo.MrrPathLossDifferenceRec;
import com.iscreate.op.service.rno.parser.vo.MrrPathLossRec;
import com.iscreate.op.service.rno.parser.vo.MrrRecord;
import com.iscreate.op.service.rno.parser.vo.MrrSignalQualityRec;
import com.iscreate.op.service.rno.parser.vo.MrrSignalStrengthRec;
import com.iscreate.op.service.rno.parser.vo.MrrTransmitPowerLevRec;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.FileTool;
import com.iscreate.op.service.rno.tool.TranslateTools;
import com.iscreate.op.service.rno.tool.ZipFileHandler;

public class EricssonMrrParser  extends TxtFileParserBase {
	
	private static Log log = LogFactory.getLog(EricssonMrrParser.class);
	
	private RnoStructureAnalysisDao rnoStructureAnalysisDao;
	private AreaLockManager areaLockManager;
	
	
	//语句
	//mrr任务信息sql
	private static String insertEriMrrDescDataSql = "insert into RNO_ERI_MRR_DESCRIPTOR (ERI_MRR_DESC_ID,MEA_DATE,FILE_NAME,BSC,CITY_ID,AREA_ID) values (?,?,?,?,?,?)";
	//管理信息sql
	private static String insertEriMrrAdmTempDataSql = "insert into RNO_ERI_MRR_ADM_TEMP (ERI_MRR_DESC_ID,FILE_FORMAT,START_DATE,RECORD_INFO,RID,TOTAL_TIME,MEASURE_LIMIT,MEASURE_SIGN,MEASURE_INTERVAL,MEASURE_TYPE,MEASURING_LINK,MEASURE_LIMIT2,MEASURE_LIMIT3,MEASURE_LIMIT4,CONNECTION_TYPE,DTM_FILTER,CITY_ID) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	//实时预警信息sql
	private static String insertEriMrrTaTempDataSql = "insert into RNO_ERI_MRR_TA_TEMP (ERI_MRR_DESC_ID,CELL_NAME,SUBCELL,CHANNEL_GROUP_NUM,TAVAL0,TAVAL1,TAVAL2,TAVAL3,TAVAL4,TAVAL5,TAVAL6,TAVAL7,TAVAL8,TAVAL9,TAVAL10,TAVAL11,TAVAL12,TAVAL13,TAVAL14,TAVAL15,TAVAL16,TAVAL17,TAVAL18,TAVAL19,TAVAL20,TAVAL21,TAVAL22,TAVAL23,TAVAL24,TAVAL26,TAVAL28,TAVAL29,TAVAL30,TAVAL31,TAVAL32,TAVAL33,TAVAL34,TAVAL35,TAVAL36,TAVAL37,TAVAL38,TAVAL39,TAVAL40,TAVAL41,TAVAL25,TAVAL27,TAVAL42,TAVAL43,TAVAL44,TAVAL45,TAVAL46,TAVAL47,TAVAL48,TAVAL49,TAVAL50,TAVAL51,TAVAL52,TAVAL53,TAVAL54,TAVAL55,TAVAL56,TAVAL57,TAVAL58,TAVAL59,TAVAL60,TAVAL61,TAVAL62,TAVAL63,TAVAL64,TAVAL65,TAVAL66,TAVAL67,TAVAL68,TAVAL69,TAVAL70,TAVAL71,TAVAL72,TAVAL73,TAVAL74,TAVAL75,CITY_ID) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	//帧消除上下行速率信息sql
	private static String insertEriMrrFerTempDataSql = "insert into RNO_ERI_MRR_FER_TEMP (ERI_MRR_DESC_ID,CELL_NAME,SUBCELL,CHANNEL_GROUP_NUM,FER_UPLINK0,FER_UPLINK1,FER_UPLINK2,FER_UPLINK3,FER_UPLINK4,FER_UPLINK5,FER_UPLINK6,FER_UPLINK7,FER_UPLINK8,FER_UPLINK9,FER_UPLINK10,FER_UPLINK11,FER_UPLINK12,FER_UPLINK13,FER_UPLINK14,FER_UPLINK15,FER_UPLINK16,FER_UPLINK17,FER_UPLINK18,FER_UPLINK19,FER_UPLINK20,FER_UPLINK21,FER_UPLINK22,FER_UPLINK23,FER_UPLINK24,FER_UPLINK25,FER_UPLINK26,FER_UPLINK27,FER_UPLINK28,FER_UPLINK29,FER_UPLINK30,FER_UPLINK31,FER_UPLINK32,FER_UPLINK33,FER_UPLINK34,FER_UPLINK35,FER_UPLINK36,FER_UPLINK37,FER_UPLINK38,FER_UPLINK39,FER_UPLINK40,FER_UPLINK41,FER_UPLINK42,FER_UPLINK43,FER_UPLINK44,FER_UPLINK45,FER_UPLINK46,FER_UPLINK47,FER_UPLINK48,FER_UPLINK49,FER_UPLINK50,FER_UPLINK51,FER_UPLINK52,FER_UPLINK53,FER_UPLINK54,FER_UPLINK55,FER_UPLINK56,FER_UPLINK57,FER_UPLINK58,FER_UPLINK59,FER_UPLINK60,FER_UPLINK61,FER_UPLINK62,FER_UPLINK63,FER_UPLINK64,FER_UPLINK65,FER_UPLINK66,FER_UPLINK67,FER_UPLINK68,FER_UPLINK69,FER_UPLINK70,FER_UPLINK71,FER_UPLINK72,FER_UPLINK73,FER_UPLINK74,FER_UPLINK75,FER_UPLINK76,FER_UPLINK77,FER_UPLINK78,FER_UPLINK79,FER_UPLINK80,FER_UPLINK81,FER_UPLINK82,FER_UPLINK83,FER_UPLINK84,FER_UPLINK85,FER_UPLINK86,FER_UPLINK87,FER_UPLINK88,FER_UPLINK89,FER_UPLINK90,FER_UPLINK91,FER_UPLINK92,FER_UPLINK93,FER_UPLINK94,FER_UPLINK95,FER_UPLINK96,FER_DOWNLINK0,FER_DOWNLINK1,FER_DOWNLINK2,FER_DOWNLINK3,FER_DOWNLINK4,FER_DOWNLINK5,FER_DOWNLINK6,FER_DOWNLINK7,FER_DOWNLINK8,FER_DOWNLINK9,FER_DOWNLINK10,FER_DOWNLINK11,FER_DOWNLINK12,FER_DOWNLINK13,FER_DOWNLINK14,FER_DOWNLINK15,FER_DOWNLINK16,FER_DOWNLINK17,FER_DOWNLINK18,FER_DOWNLINK19,FER_DOWNLINK20,FER_DOWNLINK21,FER_DOWNLINK22,FER_DOWNLINK23,FER_DOWNLINK24,FER_DOWNLINK25,FER_DOWNLINK26,FER_DOWNLINK27,FER_DOWNLINK28,FER_DOWNLINK29,FER_DOWNLINK30,FER_DOWNLINK31,FER_DOWNLINK32,FER_DOWNLINK33,FER_DOWNLINK34,FER_DOWNLINK35,FER_DOWNLINK36,FER_DOWNLINK37,FER_DOWNLINK38,FER_DOWNLINK39,FER_DOWNLINK40,FER_DOWNLINK41,FER_DOWNLINK42,FER_DOWNLINK43,FER_DOWNLINK44,FER_DOWNLINK45,FER_DOWNLINK46,FER_DOWNLINK47,FER_DOWNLINK48,FER_DOWNLINK49,FER_DOWNLINK50,FER_DOWNLINK51,FER_DOWNLINK52,FER_DOWNLINK53,FER_DOWNLINK54,FER_DOWNLINK55,FER_DOWNLINK56,FER_DOWNLINK57,FER_DOWNLINK58,FER_DOWNLINK59,FER_DOWNLINK60,FER_DOWNLINK61,FER_DOWNLINK62,FER_DOWNLINK63,FER_DOWNLINK64,FER_DOWNLINK65,FER_DOWNLINK66,FER_DOWNLINK67,FER_DOWNLINK68,FER_DOWNLINK69,FER_DOWNLINK70,FER_DOWNLINK71,FER_DOWNLINK72,FER_DOWNLINK73,FER_DOWNLINK74,FER_DOWNLINK75,FER_DOWNLINK76,FER_DOWNLINK77,FER_DOWNLINK78,FER_DOWNLINK79,FER_DOWNLINK80,FER_DOWNLINK81,FER_DOWNLINK82,FER_DOWNLINK83,FER_DOWNLINK84,FER_DOWNLINK85,FER_DOWNLINK86,FER_DOWNLINK87,FER_DOWNLINK88,FER_DOWNLINK89,FER_DOWNLINK90,FER_DOWNLINK91,FER_DOWNLINK92,FER_DOWNLINK93,FER_DOWNLINK94,FER_DOWNLINK95,FER_DOWNLINK96,CITY_ID) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	//测量结果信息sql
	private static String insertEriMrrMeaResultsTempDataSql = "insert into RNO_ERI_MRR_MEA_RESULTS_TEMP (ERI_MRR_DESC_ID,CELL_NAME,SUBCELL,CHANNEL_GROUP_NUM,REP,REP_FER_UPLINK,REP_FER_DOWNLINK,REP_FER_BL,REP_FER_THL,CITY_ID) values (?,?,?,?,?,?,?,?,?,?)";
	//路径损耗差异信息sql
	private static String insertEriMrrPldTempDataSql = "insert into RNO_ERI_MRR_PLD_TEMP (ERI_MRR_DESC_ID,CELL_NAME,SUBCELL,CHANNEL_GROUP_NUM,PLDIFF0,PLDIFF1,PLDIFF2,PLDIFF3,PLDIFF4,PLDIFF5,PLDIFF6,PLDIFF7,PLDIFF8,PLDIFF9,PLDIFF10,PLDIFF11,PLDIFF12,PLDIFF13,PLDIFF14,PLDIFF15,PLDIFF16,PLDIFF17,PLDIFF18,PLDIFF19,PLDIFF20,PLDIFF21,PLDIFF22,PLDIFF23,PLDIFF24,PLDIFF25,PLDIFF26,PLDIFF27,PLDIFF28,PLDIFF29,PLDIFF30,PLDIFF31,PLDIFF32,PLDIFF33,PLDIFF34,PLDIFF35,PLDIFF36,PLDIFF37,PLDIFF38,PLDIFF39,PLDIFF40,PLDIFF41,PLDIFF42,PLDIFF43,PLDIFF44,PLDIFF45,PLDIFF46,PLDIFF47,PLDIFF48,PLDIFF49,PLDIFF50,CITY_ID) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	//上下行路径损耗信息sql
	private static String insertEriMrrPlTempDataSql = "insert into RNO_ERI_MRR_PL_TEMP (ERI_MRR_DESC_ID,CELL_NAME,SUBCELL,CHANNEL_GROUP_NUM,PLOSSUL0,PLOSSUL1,PLOSSUL2,PLOSSUL3,PLOSSUL4,PLOSSUL5,PLOSSUL6,PLOSSUL7,PLOSSUL8,PLOSSUL9,PLOSSUL10,PLOSSUL11,PLOSSUL12,PLOSSUL13,PLOSSUL14,PLOSSUL15,PLOSSUL16,PLOSSUL17,PLOSSUL18,PLOSSUL19,PLOSSUL20,PLOSSUL21,PLOSSUL22,PLOSSUL23,PLOSSUL24,PLOSSUL25,PLOSSUL26,PLOSSUL27,PLOSSUL28,PLOSSUL29,PLOSSUL30,PLOSSUL31,PLOSSUL32,PLOSSUL33,PLOSSUL34,PLOSSUL35,PLOSSUL36,PLOSSUL37,PLOSSUL38,PLOSSUL39,PLOSSUL40,PLOSSUL41,PLOSSUL42,PLOSSUL43,PLOSSUL44,PLOSSUL45,PLOSSUL46,PLOSSUL47,PLOSSUL48,PLOSSUL49,PLOSSUL50,PLOSSUL51,PLOSSUL52,PLOSSUL53,PLOSSUL54,PLOSSUL55,PLOSSUL56,PLOSSUL57,PLOSSUL58,PLOSSUL59,PLOSSDL0,PLOSSDL1,PLOSSDL2,PLOSSDL3,PLOSSDL4,PLOSSDL5,PLOSSDL6,PLOSSDL7,PLOSSDL8,PLOSSDL9,PLOSSDL10,PLOSSDL11,PLOSSDL12,PLOSSDL13,PLOSSDL14,PLOSSDL15,PLOSSDL16,PLOSSDL17,PLOSSDL18,PLOSSDL19,PLOSSDL20,PLOSSDL21,PLOSSDL22,PLOSSDL23,PLOSSDL24,PLOSSDL25,PLOSSDL26,PLOSSDL27,PLOSSDL28,PLOSSDL29,PLOSSDL30,PLOSSDL31,PLOSSDL32,PLOSSDL33,PLOSSDL34,PLOSSDL35,PLOSSDL36,PLOSSDL37,PLOSSDL38,PLOSSDL39,PLOSSDL40,PLOSSDL41,PLOSSDL42,PLOSSDL43,PLOSSDL44,PLOSSDL45,PLOSSDL46,PLOSSDL47,PLOSSDL48,PLOSSDL49,PLOSSDL50,PLOSSDL51,PLOSSDL52,PLOSSDL53,PLOSSDL54,PLOSSDL55,PLOSSDL56,PLOSSDL57,PLOSSDL58,PLOSSDL59,PLOSSDL60,PLOSSDL61,PLOSSDL62,PLOSSDL63,PLOSSDL64,CITY_ID) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	//上下行信号质量信息sql
	private static String insertEriMrrQualityTempDataSql = "insert into RNO_ERI_MRR_QUALITY_TEMP (ERI_MRR_DESC_ID,CELL_NAME,SUBCELL,CHANNEL_GROUP_NUM,RXQUALUL0,RXQUALUL1,RXQUALUL2,RXQUALUL3,RXQUALUL4,RXQUALUL5,RXQUALUL6,RXQUALUL7,RXQUALDL0,RXQUALDL1,RXQUALDL2,RXQUALDL3,RXQUALDL4,RXQUALDL5,RXQUALDL6,RXQUALDL7,CITY_ID) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	//上下行信号强度信息sql
	private static String insertEriMrrStrengthTempDataSql = "insert into RNO_ERI_MRR_STRENGTH_TEMP (ERI_MRR_DESC_ID,CELL_NAME,SUBCELL,CHANNEL_GROUP_NUM,RXLEVUL0,RXLEVUL1,RXLEVUL2,RXLEVUL3,RXLEVUL4,RXLEVUL5,RXLEVUL6,RXLEVUL7,RXLEVUL8,RXLEVUL9,RXLEVUL10,RXLEVUL11,RXLEVUL12,RXLEVUL13,RXLEVUL14,RXLEVUL15,RXLEVUL16,RXLEVUL17,RXLEVUL18,RXLEVUL19,RXLEVUL20,RXLEVUL21,RXLEVUL22,RXLEVUL23,RXLEVUL24,RXLEVUL25,RXLEVUL26,RXLEVUL27,RXLEVUL28,RXLEVUL29,RXLEVUL30,RXLEVUL31,RXLEVUL32,RXLEVUL33,RXLEVUL34,RXLEVUL35,RXLEVUL36,RXLEVUL37,RXLEVUL38,RXLEVUL39,RXLEVUL40,RXLEVUL41,RXLEVUL42,RXLEVUL43,RXLEVUL44,RXLEVUL45,RXLEVUL46,RXLEVUL47,RXLEVUL48,RXLEVUL49,RXLEVUL50,RXLEVUL51,RXLEVUL52,RXLEVUL53,RXLEVUL54,RXLEVUL55,RXLEVUL56,RXLEVUL57,RXLEVUL58,RXLEVUL59,RXLEVUL60,RXLEVUL61,RXLEVUL62,RXLEVUL63,RXLEVDL0,RXLEVDL1,RXLEVDL2,RXLEVDL3,RXLEVDL4,RXLEVDL5,RXLEVDL6,RXLEVDL7,RXLEVDL8,RXLEVDL9,RXLEVDL10,RXLEVDL11,RXLEVDL12,RXLEVDL13,RXLEVDL14,RXLEVDL15,RXLEVDL16,RXLEVDL17,RXLEVDL18,RXLEVDL19,RXLEVDL20,RXLEVDL21,RXLEVDL22,RXLEVDL23,RXLEVDL24,RXLEVDL25,RXLEVDL26,RXLEVDL27,RXLEVDL28,RXLEVDL29,RXLEVDL30,RXLEVDL31,RXLEVDL32,RXLEVDL33,RXLEVDL34,RXLEVDL35,RXLEVDL36,RXLEVDL37,RXLEVDL38,RXLEVDL39,RXLEVDL40,RXLEVDL41,RXLEVDL42,RXLEVDL43,RXLEVDL44,RXLEVDL45,RXLEVDL46,RXLEVDL47,RXLEVDL48,RXLEVDL49,RXLEVDL50,RXLEVDL51,RXLEVDL52,RXLEVDL53,RXLEVDL54,RXLEVDL55,RXLEVDL56,RXLEVDL57,RXLEVDL58,RXLEVDL59,RXLEVDL60,RXLEVDL61,RXLEVDL62,RXLEVDL63,CITY_ID) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; 
	//传输功率级别信息sql
	private static String insertEriMrrPowerTempDataSql = "insert into RNO_ERI_MRR_POWER_TEMP (ERI_MRR_DESC_ID,CELL_NAME,SUBCELL,CHANNEL_GROUP_NUM,MSPOWER0,MSPOWER1,MSPOWER2,MSPOWER3,MSPOWER4,MSPOWER5,MSPOWER6,MSPOWER7,MSPOWER8,MSPOWER9,MSPOWER10,MSPOWER11,MSPOWER12,MSPOWER13,MSPOWER14,MSPOWER15,MSPOWER16,MSPOWER17,MSPOWER18,MSPOWER19,MSPOWER20,MSPOWER21,MSPOWER22,MSPOWER23,MSPOWER24,MSPOWER25,MSPOWER26,MSPOWER27,MSPOWER28,MSPOWER29,MSPOWER30,MSPOWER31,BSPOWER0,BSPOWER1,BSPOWER2,BSPOWER3,BSPOWER4,BSPOWER5,BSPOWER6,BSPOWER7,BSPOWER8,BSPOWER9,BSPOWER10,BSPOWER11,BSPOWER12,BSPOWER13,BSPOWER14,BSPOWER15,CITY_ID) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	
	public RnoStructureAnalysisDao getRnoStructureAnalysisDao() {
		return rnoStructureAnalysisDao;
	}

	public void setRnoStructureAnalysisDao(
			RnoStructureAnalysisDao rnoStructureAnalysisDao) {
		this.rnoStructureAnalysisDao = rnoStructureAnalysisDao;
	}
	
	public void setAreaLockManager(AreaLockManager areaLockManager) {
		this.areaLockManager = areaLockManager;
	}
	
	@Override
	protected boolean parseDataInternal(String token, File file,
			boolean needPersist, boolean update, long oldConfigId, long areaId,
			boolean autoload, Map<String, Object> attachParams) {
		log.debug("进入EricssonMrrParser方法：parseDataInternal。 token=" + token
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
		
		//锁定区域
		Set<Long> affectAreaIds = new HashSet<Long>();
		affectAreaIds.add(Long.parseLong(attachParams.get("cityId").toString()));
		affectAreaIds.add(areaId);
		OperResult lockRes = areaLockManager.lockAreasForMrr(affectAreaIds);
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
		if (fileName.endsWith(".zip")
				||fileName.endsWith("ZIP")
				|| fileName.endsWith("Zip")
				) {
			setTokenInfo(token, "正在解析压缩包");
			fromZip = true;
			// 压缩包
			log.info("上传的mrr文件是一个压缩包。");

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
				areaLockManager.unlockAreasForMrr(affectAreaIds);
				return false;
			}
			if (!unzipOk) {
				msg = "解压失败 ！仅支持zip格式的压缩包！ ";
				log.error(msg);
				super.setCachedInfo(token, msg);
				clearResource(destDir, null);
				areaLockManager.unlockAreasForMrr(affectAreaIds);
				return false;
			}
			file = new File(destDir);
			File[] files = file.listFiles();
			//判断文件数量，不要超过规定数量
//			if(files.length>50){
//				msg="一次上传的mrr文件数量:["+files.length+"]超过了规定的最大数量：50";
//				log.error(msg);
//				setTokenInfo(token, msg);
//				super.setCachedInfo(token, msg);
//				clearResource(destDir, null);
//				areaLockManager.unlockAreasForMrr(affectAreaIds);
//				return false;
//			}
			for (File f : files) {
				// 只要文件，不要目录
				if (f.isFile() && !f.isHidden()) {
					allMrrFiles.add(f);
				}
			}
		}else if(fileName.endsWith(".rar")){
			msg = "请用zip格式压缩文件！";
			log.error(msg);
			super.setCachedInfo(token, msg);
			clearResource(destDir, null);
			areaLockManager.unlockAreasForMrr(affectAreaIds);
			return false;
		}
		else {
			log.info("上传的mrr是一个普通文件。");
			allMrrFiles.add(file);

		}
		
		if (allMrrFiles.isEmpty()) {
			msg = "未上传有效的mrr文件！zip包里不能再包含有文件夹！";
			log.error(msg);
			super.setCachedInfo(token, msg);
			clearResource(destDir, null);
			areaLockManager.unlockAreasForMrr(affectAreaIds);
			return false;
		}
		
		MrrParserContext context = new MrrParserContext();
		
		context.setAreaId(areaId);
		context.setCityId(cityId);

		for (File f : allMrrFiles) {
			setTokenInfo(token, "正在解析MRR文件");
			//每次完成一个文件就清空一次statement集合
			if (context != null) {
				context.closeAllStatement();
			}
			try {
				if (fromZip) {
					parseMrr(f.getName(), f, context, destDir,affectAreaIds, token);
				} else {
					parseMrr(fileName, f, context, destDir,affectAreaIds, token);
				}
			} catch (Exception e) {
				e.printStackTrace();
				msg = "解析文件出错！请检查是否按要求上传文件！";
				log.error(msg);
				super.setCachedInfo(token, msg);
				clearResource(destDir, context);
				areaLockManager.unlockAreasForMrr(affectAreaIds);
				return false;
			}
		}
		
		long et = System.currentTimeMillis();
		log.info("导入mrr文件，共耗时：" + (et-st) + "ms");
		
		log.info("退出EricssonMrrParser解析方法");
		
		clearResource(destDir, context);
		log.info("解除区域锁定");
		areaLockManager.unlockAreasForMrr(affectAreaIds);
		super.setCachedInfo(token, "解析完成！");
		setTokenInfo(token, context.getErrorMsg());
		

		return true;
	}

	public void parseMrr(String fileName, File file, 
			MrrParserContext context, String destDir,Set<Long> affectAreaIds, String token) throws IOException {
		
		String msg = "";

		PreparedStatement insertEriMrrDescStmt = null;
		try {
			insertEriMrrDescStmt = connection.prepareStatement(insertEriMrrDescDataSql);
		} catch (SQLException e1) {
			e1.printStackTrace();
			msg = "数据处理出错！code=mrr-1";
			log.error(msg);
			super.setCachedInfo(token, msg);
			if (context != null) {
				context.closeAllStatement();
			}
			areaLockManager.unlockAreasForMrr(affectAreaIds);
			return ;
		}
		context.setPreparedStatment("EriMrrDesc", insertEriMrrDescStmt);
		
		PreparedStatement insertEriMrrAdmTempStmt = null;
		try {
			insertEriMrrAdmTempStmt = connection.prepareStatement(insertEriMrrAdmTempDataSql);
		} catch (SQLException e1) {
			e1.printStackTrace();
			msg = "数据处理出错！code=mrr-2";
			log.error(msg);
			super.setCachedInfo(token, msg);
			if (context != null) {
				context.closeAllStatement();
			}
			areaLockManager.unlockAreasForMrr(affectAreaIds);
			return ;
		}
		context.setPreparedStatment("EriMrrAdmRecord", insertEriMrrAdmTempStmt);
		
		PreparedStatement insertEriMrrTaTempStmt = null;
		try {
			insertEriMrrTaTempStmt = connection.prepareStatement(insertEriMrrTaTempDataSql);
		} catch (SQLException e1) {
			e1.printStackTrace();
			msg = "数据处理出错！code=mrr-3";
			log.error(msg);
			super.setCachedInfo(token, msg);
			if (context != null) {
				context.closeAllStatement();
			}
			areaLockManager.unlockAreasForMrr(affectAreaIds);
			return ;
		}
		context.setPreparedStatment("EriMrrTaRecord", insertEriMrrTaTempStmt);
		
		PreparedStatement insertEriMrrFerTempStmt = null;
		try {
			insertEriMrrFerTempStmt = connection.prepareStatement(insertEriMrrFerTempDataSql);
		} catch (SQLException e1) {
			e1.printStackTrace();
			msg = "数据处理出错！code=mrr-4";
			log.error(msg);
			super.setCachedInfo(token, msg);
			if (context != null) {
				context.closeAllStatement();
			}
			areaLockManager.unlockAreasForMrr(affectAreaIds);
			return ;
		}
		context.setPreparedStatment("EriMrrFerRecord", insertEriMrrFerTempStmt);
		
		PreparedStatement insertEriMrrMeaResultsTempStmt = null;
		try {
			insertEriMrrMeaResultsTempStmt = connection.prepareStatement(insertEriMrrMeaResultsTempDataSql);
		} catch (SQLException e1) {
			e1.printStackTrace();
			msg = "数据处理出错！code=mrr-5";
			log.error(msg);
			super.setCachedInfo(token, msg);
			if (context != null) {
				context.closeAllStatement();
			}
			areaLockManager.unlockAreasForMrr(affectAreaIds);
			return ;
		}
		context.setPreparedStatment("EriMrrMeaResultsRecord", insertEriMrrMeaResultsTempStmt);
		
		
		PreparedStatement insertEriMrrPldTempStmt = null;
		try {
			insertEriMrrPldTempStmt = connection.prepareStatement(insertEriMrrPldTempDataSql);
		} catch (SQLException e1) {
			e1.printStackTrace();
			msg = "数据处理出错！code=mrr-6";
			log.error(msg);
			super.setCachedInfo(token, msg);
			if (context != null) {
				context.closeAllStatement();
			}
			areaLockManager.unlockAreasForMrr(affectAreaIds);
			return ;
		}
		context.setPreparedStatment("EriMrrPldRecord", insertEriMrrPldTempStmt);
		
		PreparedStatement insertEriMrrPlTempStmt = null;
		try {
			insertEriMrrPlTempStmt = connection.prepareStatement(insertEriMrrPlTempDataSql);
		} catch (SQLException e1) {
			e1.printStackTrace();
			msg = "数据处理出错！code=mrr-7";
			log.error(msg);
			super.setCachedInfo(token, msg);
			if (context != null) {
				context.closeAllStatement();
			}
			areaLockManager.unlockAreasForMrr(affectAreaIds);
			return ;
		}
		context.setPreparedStatment("EriMrrPlRecord", insertEriMrrPlTempStmt);
		
		PreparedStatement insertEriMrrQualityTempStmt = null;
		try {
			insertEriMrrQualityTempStmt = connection.prepareStatement(insertEriMrrQualityTempDataSql);
		} catch (SQLException e1) {
			e1.printStackTrace();
			msg = "数据处理出错！code=mrr-8";
			log.error(msg);
			super.setCachedInfo(token, msg);
			if (context != null) {
				context.closeAllStatement();
			}
			areaLockManager.unlockAreasForMrr(affectAreaIds);
			return ;
		}
		context.setPreparedStatment("EriMrrQualityRecord", insertEriMrrQualityTempStmt);
		
		PreparedStatement insertEriMrrStrengthTempStmt = null;
		try {
			insertEriMrrStrengthTempStmt = connection.prepareStatement(insertEriMrrStrengthTempDataSql);
		} catch (SQLException e1) {
			e1.printStackTrace();
			msg = "数据处理出错！code=mrr-9";
			log.error(msg);
			super.setCachedInfo(token, msg);
			if (context != null) {
				context.closeAllStatement();
			}
			areaLockManager.unlockAreasForMrr(affectAreaIds);
			return ;
		}
		context.setPreparedStatment("EriMrrStrengthRecord", insertEriMrrStrengthTempStmt);
		
		PreparedStatement insertEriMrrPowerTempStmt = null;
		try {
			insertEriMrrPowerTempStmt = connection.prepareStatement(insertEriMrrPowerTempDataSql);
		} catch (SQLException e1) {
			e1.printStackTrace();
			msg = "数据处理出错！code=mrr-10";
			log.error(msg);
			super.setCachedInfo(token, msg);
			if (context != null) {
				context.closeAllStatement();
			}
			areaLockManager.unlockAreasForMrr(affectAreaIds);
			return ;
		}
		context.setPreparedStatment("EriMrrPowerRecord", insertEriMrrPowerTempStmt);
	
		
		
		InputStream is = new FileInputStream(file);
		//System.out.println(is.available());
		byte[] typeByte = new byte[1];
		byte[] lenByte = new byte[2];
		byte[] content = new byte[1024];
		
		int len = -1;
		int type = -1;
		MrrRecord rec=null;
		long mrrId = -1;

		Date startTime = new Date();	
		int result = 0;

		while ((len = is.read(typeByte, 0, 1)) != -1) {
			type = -1;
			len = is.read(lenByte, 0, 2);
			if (len != 2) {
				log.error("异常中断或格式错误！");
				context.appedErrorMsg("文件:[" + fileName
						+ "]异常中断或格式错误！<br/>");
				break;
			}
			len = TranslateTools.makeIntFromByteArray(lenByte, 0, 2);

			if (len <= 0) {
				log.error("长度信息有误！");
				//context.appedErrorMsg("文件:[" + fileName
				//		+ "]的长度信息有误！<br/>");
				break;
			}
			is.read(content, 0, len - 3);// 减去type占用的1个字节，length内容占用的2个字节
			byte[] wholeSection = TranslateTools.mergeByte(typeByte, lenByte,
					TranslateTools.subByte(content, 0, len - 3));

			type = TranslateTools.makeIntFromByteArray(typeByte, 0, 1);
			rec = handleSection(type, wholeSection);
			
			// 处理rec
			if (rec != null) {
				//文件是否有效数据
				result++;
				//获取测量日期
				if(rec.getRecType() == 30) {
					MrrAdmRecord mrrAdmRecord = (MrrAdmRecord) rec;
					startTime = mrrAdmRecord.getStartTime(context.getDateUtil());
				}
				
				if (mrrId == -1) {
					// 申请id
					mrrId = super.getNextSeqValue("SEQ_ERI_MRR_DESCRIPTOR",
							connection);
				}
				// 转换成sql语句
				prepareRecordSql(mrrId, rec, context);
			}
		}

		//数据处理
		setTokenInfo(token, "正在进行数据处理");
		if(mrrId != -1) {
			dataProcess(fileName, mrrId, startTime, context, token);
		} else {
			log.error("数据信息有误！");
			context.appedErrorMsg("文件:[" + fileName
					+ "]的数据信息有误！<br/>");
		}

		is.close();
		try {
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 设置Mrr文件描述信息
	 * @param fileName
	 * @param stmt
	 * @param mrrId
	 * @param areaId
	 * @param cityId
	 * @author peng.jm
	 * @date 2014-7-21下午02:08:32
	 */
	private void setMrrDescToStmt(String fileName, Date startTime, PreparedStatement stmt,
			long mrrId, long areaId, long cityId) {
		if (stmt == null) {
			return;
		}

		int index = 1;
		try {
			stmt.setLong(index++, mrrId);
			stmt.setTimestamp(index++, new java.sql.Timestamp(startTime.getTime()));
			stmt.setString(index++, fileName);
			stmt.setString(index++, "");//bsc后面自动匹配
			stmt.setLong(index++, cityId);
			stmt.setLong(index++, areaId);
			
			stmt.addBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 准备批处理插入语句
	 * @param mrrId
	 * @param rec
	 * @param context
	 * @author peng.jm
	 * @date 2014-7-21上午10:25:23
	 */
	private void prepareRecordSql(long mrrId, MrrRecord rec,
			MrrParserContext context) {
		if (rec instanceof MrrAdmRecord) {
			PreparedStatement stmt = context
					.getPreparedStatement("EriMrrAdmRecord");
			setAdmRecordToStmt(stmt, mrrId, (MrrAdmRecord) rec,context.getCityId(),context.getDateUtil());
		} else if (rec instanceof MrrActulTimingAdvanceRec) {
			PreparedStatement stmt = context
					.getPreparedStatement("EriMrrTaRecord");
			setTaRecordToStmt(stmt, mrrId, (MrrActulTimingAdvanceRec) rec,context.getCityId());
		} else if (rec instanceof MrrFrameErasureRateRec) {
			PreparedStatement stmt = context
					.getPreparedStatement("EriMrrFerRecord");
			setFerRecordToStmt(stmt, mrrId, (MrrFrameErasureRateRec) rec,context.getCityId());
		} else if (rec instanceof MrrNumOfMeaResultsRec) {
			PreparedStatement stmt = context
					.getPreparedStatement("EriMrrMeaResultsRecord");
			setMeaResultsRecordToStmt(stmt, mrrId, (MrrNumOfMeaResultsRec) rec,context.getCityId());
		} else if (rec instanceof MrrPathLossDifferenceRec) {
			PreparedStatement stmt = context
					.getPreparedStatement("EriMrrPldRecord");
			setPldRecordToStmt(stmt, mrrId, (MrrPathLossDifferenceRec) rec,context.getCityId());
		} else if (rec instanceof MrrPathLossRec) {
			PreparedStatement stmt = context
					.getPreparedStatement("EriMrrPlRecord");
			setPlRecordToStmt(stmt, mrrId, (MrrPathLossRec) rec,context.getCityId());
		} else if (rec instanceof MrrSignalQualityRec) {
			PreparedStatement stmt = context
					.getPreparedStatement("EriMrrQualityRecord");
			setQualityRecordToStmt(stmt, mrrId, (MrrSignalQualityRec) rec,context.getCityId());
		} else if (rec instanceof MrrSignalStrengthRec) {
			PreparedStatement stmt = context
					.getPreparedStatement("EriMrrStrengthRecord");
			setStrengthRecordToStmt(stmt, mrrId, (MrrSignalStrengthRec) rec,context.getCityId());
		} else if (rec instanceof MrrTransmitPowerLevRec) {
			PreparedStatement stmt = context
					.getPreparedStatement("EriMrrPowerRecord");
			setPowerRecordToStmt(stmt, mrrId, (MrrTransmitPowerLevRec) rec,context.getCityId());
		}
		else {
			log.warn("暂时不处理此类型记录！");
		}

	}
	
	/**
	 * 设置传输功率级别信息
	 * @param stmt
	 * @param mrrId
	 * @param rec
	 * @author peng.jm
	 * @date 2014-7-21下午03:31:03
	 */
	private void setPowerRecordToStmt(PreparedStatement stmt, long mrrId,
			MrrTransmitPowerLevRec rec,long cityId) {
		if (stmt == null || rec == null) {
			return;
		}

		int index = 1;
		try {
			stmt.setLong(index++, mrrId);
			stmt.setString(index++, rec.getCellName());
			stmt.setInt(index++, rec.getSubCell());
			stmt.setInt(index++, rec.getChannelGroupNumber());
			Map<String, Integer> msPowers = rec.getMsPowers();
			int msPower = 0;
			for (int i = 0; i < msPowers.size(); i++) {
				msPower = msPowers.get("msPowers" + i);
				stmt.setInt(index++, msPower);
			}
			Map<String, Integer> bsPowers = rec.getBsPowers();
			int bsPower = 0;
			for (int i = 0; i < bsPowers.size(); i++) {
				bsPower = bsPowers.get("bsPowers" + i);
				stmt.setInt(index++, bsPower);
			}

			stmt.setLong(index++, cityId);
			stmt.addBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * 设置上下行信号强度信息
	 * @param stmt
	 * @param mrrId
	 * @param rec
	 * @author peng.jm
	 * @date 2014-7-21下午03:27:20
	 */
	private void setStrengthRecordToStmt(PreparedStatement stmt, long mrrId,
			MrrSignalStrengthRec rec,long cityId) {
		if (stmt == null || rec == null) {
			return;
		}

		int index = 1;
		try {
			stmt.setLong(index++, mrrId);
			stmt.setString(index++, rec.getCellName());
			stmt.setInt(index++, rec.getSubCell());
			stmt.setInt(index++, rec.getChgr());
			Map<String, Integer> rxLevUls = rec.getRxLevUls();
			int rxLevUl = 0;
			for (int i = 0; i < rxLevUls.size(); i++) {
				rxLevUl = rxLevUls.get("rxLevUl" + i);
				stmt.setInt(index++, rxLevUl);
			}
			Map<String, Integer> rxLevDls = rec.getRxLevDls();
			int rxLevDl = 0;
			for (int i = 0; i < rxLevDls.size(); i++) {
				rxLevDl = rxLevDls.get("rxLevDl" + i);
				stmt.setInt(index++, rxLevDl);
			}

			stmt.setLong(index++, cityId);
			stmt.addBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * 设置上下行信号质量信息
	 * @param stmt
	 * @param mrrId
	 * @param rec
	 * @author peng.jm
	 * @date 2014-7-21下午03:24:49
	 */
	private void setQualityRecordToStmt(PreparedStatement stmt, long mrrId,
			MrrSignalQualityRec rec,long cityId) {
		if (stmt == null || rec == null) {
			return;
		}

		int index = 1;
		try {
			stmt.setLong(index++, mrrId);
			stmt.setString(index++, rec.getCellName());
			stmt.setInt(index++, rec.getSubCell());
			stmt.setInt(index++, rec.getChannelGroupNumber());
			Map<String, Integer> rxQualUls = rec.getRxQualUls();
			int rxQualUl = 0;
			for (int i = 0; i < rxQualUls.size(); i++) {
				rxQualUl = rxQualUls.get("rxQualUl" + i);
				stmt.setInt(index++, rxQualUl);
			}
			Map<String, Integer> rxQualDls = rec.getRxQualDls();
			int rxQualDl = 0;
			for (int i = 0; i < rxQualDls.size(); i++) {
				rxQualDl = rxQualDls.get("rxQualDl" + i);
				stmt.setInt(index++, rxQualDl);
			}

			stmt.setLong(index++, cityId);
			stmt.addBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * 设置上下行路径损耗信息
	 * @param stmt
	 * @param mrrId
	 * @param rec
	 * @author peng.jm
	 * @date 2014-7-21下午03:20:03
	 */
	private void setPlRecordToStmt(PreparedStatement stmt, long mrrId,
			MrrPathLossRec rec,long cityId) {
		if (stmt == null || rec == null) {
			return;
		}

		int index = 1;
		try {
			stmt.setLong(index++, mrrId);
			stmt.setString(index++, rec.getCellName());
			stmt.setInt(index++, rec.getSubCell());
			stmt.setInt(index++, rec.getChannelGroupNumber());
			Map<String, Integer> pLossUls = rec.getPLossUls();
			int pLossUl = 0;
			for (int i = 0; i < pLossUls.size(); i++) {
				pLossUl = pLossUls.get("pLossUls" + i);
				stmt.setInt(index++, pLossUl);
			}
			Map<String, Integer> pLossDls = rec.getPLossDls();
			int pLossDl = 0;
			for (int i = 0; i < pLossDls.size(); i++) {
				pLossDl = pLossDls.get("pLossDls" + i);
				stmt.setInt(index++, pLossDl);
			}

			stmt.setLong(index++, cityId);
			stmt.addBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置路径损耗差异信息
	 * @param stmt
	 * @param mrrId
	 * @param rec
	 * @author peng.jm
	 * @date 2014-7-21下午03:16:20
	 */
	private void setPldRecordToStmt(PreparedStatement stmt, long mrrId,
			MrrPathLossDifferenceRec rec,long cityId) {
		if (stmt == null || rec == null) {
			return;
		}

		int index = 1;
		try {
			stmt.setLong(index++, mrrId);
			stmt.setString(index++, rec.getCellName());
			stmt.setInt(index++, rec.getSubCell());
			stmt.setInt(index++, rec.getChannelGroupNumber());
			Map<String, Integer> pLDiffs = rec.getPLDiffs();
			int pLDiff = 0;
			for (int i = 0; i < pLDiffs.size(); i++) {
				pLDiff = pLDiffs.get("pLDiffs" + i);
				stmt.setInt(index++, pLDiff);
			}

			stmt.setLong(index++, cityId);
			
			stmt.addBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 设置测量结果信息
	 * @param stmt
	 * @param mrrId
	 * @param rec
	 * @author peng.jm
	 * @date 2014-7-21下午03:13:24
	 */
	private void setMeaResultsRecordToStmt(PreparedStatement stmt, long mrrId,
			MrrNumOfMeaResultsRec rec,long cityId) {
		if (stmt == null || rec == null) {
			return;
		}

		int index = 1;
		try {
			stmt.setLong(index++, mrrId);
			stmt.setString(index++, rec.getCellName());
			stmt.setInt(index++, rec.getSubCell());
			stmt.setInt(index++, rec.getChannelGroupNumber());
			stmt.setInt(index++, rec.getRep());
			stmt.setInt(index++, rec.getRepferUl());
			stmt.setInt(index++, rec.getRepferDl());
			stmt.setInt(index++, rec.getRepferBl());
			stmt.setInt(index++, rec.getRepferTHL());
			
			stmt.setLong(index++, cityId);

			stmt.addBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 设置帧消除上下行速率信息
	 * @param stmt
	 * @param mrrId
	 * @param rec
	 * @author peng.jm
	 * @date 2014-7-21下午03:09:35
	 */
	private void setFerRecordToStmt(PreparedStatement stmt, long mrrId,
			MrrFrameErasureRateRec rec,long cityId) {
		if (stmt == null || rec == null) {
			return;
		}

		int index = 1;
		try {
			stmt.setLong(index++, mrrId);
			stmt.setString(index++, rec.getCellName());
			stmt.setInt(index++, rec.getSubCell());
			stmt.setInt(index++, rec.getChannelGroupNumber());
			Map<String, Integer> ferUls = rec.getFerUls();
			int ferUl = 0;
			for (int i = 0; i < ferUls.size(); i++) {
				ferUl = ferUls.get("ferUls" + i);
				stmt.setInt(index++, ferUl);
			}
			Map<String, Integer> ferDls = rec.getFerDls();
			int ferDl = 0;
			for (int i = 0; i < ferDls.size(); i++) {
				ferDl = ferDls.get("ferDls" + i);
				stmt.setInt(index++, ferDl);
			}

			stmt.setLong(index++, cityId);
			
			stmt.addBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置实时预警信息
	 * @param stmt
	 * @param mrrId
	 * @param rec
	 * @author peng.jm
	 * @date 2014-7-21上午11:03:38
	 */
	private void setTaRecordToStmt(PreparedStatement stmt, long mrrId,
			MrrActulTimingAdvanceRec rec,long cityId) {
		if (stmt == null || rec == null) {
			return;
		}

		int index = 1;
		try {
			stmt.setLong(index++, mrrId);
			stmt.setString(index++, rec.getCellName());
			stmt.setInt(index++, rec.getSubCell());
			stmt.setInt(index++, rec.getChannelGroupNumber());
			Map<String, Integer> tavals = rec.getTavals();
			int taval = 0;
			for (int i = 0; i < tavals.size(); i++) {
				taval = tavals.get("tavals" + i);
				stmt.setInt(index++, taval);
			}

			stmt.setLong(index++,cityId);
			
			stmt.addBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置管理信息
	 * @param stmt
	 * @param mrrId
	 * @param rec
	 * @author peng.jm
	 * @date 2014-7-21上午11:03:43
	 */
	private void setAdmRecordToStmt(PreparedStatement stmt, long mrrId,
			MrrAdmRecord rec,long cityId,DateUtil dateUtil) {
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
		
		try {
			stmt.setLong(index++, mrrId);
			stmt.setInt(index++, rec.getFileFormat());
			stmt.setTimestamp(index++, new java.sql.Timestamp(st.getTime()));
			stmt.setInt(index++, rec.getRecInfo());
			stmt.setString(index++, rec.getRid());
			stmt.setInt(index++, rec.getTotalTime());
			stmt.setInt(index++, rec.getMeasureLimit());
			stmt.setInt(index++, rec.getMeasureSign());
			stmt.setInt(index++, rec.getMeasureInterval());
			stmt.setInt(index++, rec.getMeasureType());
			stmt.setInt(index++, rec.getMeasureLink());
			stmt.setInt(index++, rec.getMeasureLimit2());
			stmt.setInt(index++, rec.getMeasureLimit3());
			stmt.setInt(index++, rec.getMeasureLimit4());
			stmt.setInt(index++, rec.getConnectionType());
			stmt.setInt(index++, rec.getDtmFilter());
			stmt.setLong(index++, cityId);

			stmt.addBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public  MrrRecord handleSection(int type, byte[] wholeSection) {
		MrrRecord rec = null;
		switch (type) {
		case 30:
			rec=handleMrrAdministrator(wholeSection);
			break;
		case 31:
			rec=handleMrrSignalStrength(wholeSection);
			break;
		case 32:
			rec=handleMrrSignalQuality(wholeSection);
			break;
		case 33:
			rec=handleMrrTransmitPowerLev(wholeSection);
			break;
		case 34:
			rec=handleMrrActulTimingAdvance(wholeSection);
			break;
		case 35:
			rec=handleMrrPathLoss(wholeSection);
			break;
		case 36:
			rec=handleMrrPathLossDifference(wholeSection);
			break;
		case 37:
			rec=handleMrrNumOfMeaResults(wholeSection);
			break;
		case 38:
			rec=handleMrrFrameErasureRate(wholeSection);
			break;
		default:
			log.debug("type "+type+",跳过....");
			break;
		}

		return rec;
	}

	/**
	 * 处理mrr记录管理头部信息
	 * @param wholeSection
	 * @return
	 * @author brightming
	 * 2014-5-23 上午10:40:06
	 */
	private  MrrRecord handleMrrAdministrator(byte[] wholeSection) {
		
		MrrAdmRecord rec=new MrrAdmRecord();
		
		int start=0;
		int len=1;
		
		int intVal=0;
		String strVal="";
		
		//len
		start=1;
		len=2;
		intVal=TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setRecLen(intVal);
//		System.out.println("--Administrator,record len="+intVal);
		
		
		//file format
		start=3;
		len=1;
		intVal=TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setFileFormat(intVal);
		
		//start year
		start=4;
		len=1;
		intVal=TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setStartYear(intVal);
		
		//start month
		start=5;
		len=1;
		intVal=TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setStartMonth(intVal);
		
		//start day
		start=6;
		len=1;
		intVal=TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setStartDay(intVal);
		
		//start hour
		start=7;
		len=1;
		intVal=TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setStartHour(intVal);
		
		//start minute
		start=8;
		len=1;
		intVal=TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setStartMinute(intVal);
		
		
		//start second
		start=9;
		len=1;
		intVal=TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setStartSecond(intVal);
		
		
//		Record information
		start=10;
		len=2;
		intVal=TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setRecInfo(intVal);
		
		
		//rid
		start=12;
		len=7;
		strVal=new String(TranslateTools.subByte(wholeSection, start, len));
		rec.setRid(strVal);
		
		//Total time recording has been active, in minutes
		start=19;
		len=2;
		intVal=TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setTotalTime(intVal);
		
		//Measurement limit
		start=21;
		len=1;
		intVal=TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setMeasureLimit(intVal);
		
		//MEASLIM  sign
//		0 - Positive
//		1 - Negative
//		Only valid if position 24 equals 9 (See notes 3 and 4)
		start=22;
		len=1;
		intVal=TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setMeasureSign(intVal);
		
		//Measurement interval
		//Not valid if position 24 equals 0, 7 or 13
		start=23;
		len=1;
		intVal=TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setMeasureInterval(intVal);
		
		//Measurement type
		start=24;
		len=1;
		intVal=TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setMeasureType(intVal);
		
		
//		Measuring link
//		Only valid if position 24 equals 6, 7, 8, 12, or 13
		start=25;
		len=1;
		intVal=TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setMeasureLink(intVal);
		
//		MEASLIM2
//		Measurement limit
//		Only valid if position 24 equals 6, 7, 8, 12, or 13 
		start=26;
		len=1;
		intVal=TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setMeasureLimit2(intVal);
		
//		MEASLIM3
//		Measurement limit
//		Only valid if position 24 equals 6, 7, 8, 12, or 13
		start=27;
		len=1;
		intVal=TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setMeasureLimit3(intVal);
		
//		MEASLIM4
//		Measurement limit
//		Only valid if position 24 equals 6, or 12 
		start=28;
		len=1;
		intVal=TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setMeasureLimit4(intVal);
		
		//Connection type
		start=29;
		len=1;
		intVal=TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setConnectionType(intVal);
		
		//Dual Transfer Mode (DTM) filter
		start=30;
		len=1;
		intVal=TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setDtmFilter(intVal);
		
		return rec;
	}
	
	/**
	 *  Record UPLINK AND DOWNLINK SIGNAL STRENGTH CELL DATA
	 * @param wholeSection
	 * @return
	 * @author brightming
	 * 2014-5-23 上午11:48:47
	 */
	private  MrrRecord handleMrrSignalStrength(byte[] wholeSection){
		MrrSignalStrengthRec rec=new MrrSignalStrengthRec();
		
		int start=0;
		int len=1;
		
		int intVal=0;
		String strVal="";
		
		//len
		start=1;
		len=2;
		intVal=TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setRecLen(intVal);
		//System.out.println("signal strength len hex="+TranslateTools.byte2hex(TranslateTools.subByte(wholeSection, start, len),len));
		
		//cellName
		// 文档说是8位，但实际只有7位是有效的，最后一位是看不见的字符
		start=3;
		len=7;
		strVal=new String(TranslateTools.subByte(wholeSection, start, len));
		rec.setCellName(strVal);
		
		//sub cell
		start=11;
		len=1;
		intVal=TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setSubCell(intVal);
		
		//chgr
		start=12;
		len=1;
		intVal=TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setChgr(intVal);
		
		//RXLEVUL  连续64个
		start=13;
		len=4;
		for(int i=0;i<=63;i++){
			try{
			intVal=TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
			rec.addRxLevUl("rxLevUl"+i, intVal);
			}catch(Exception e){
				e.printStackTrace();
				System.err.println("start="+start+",i="+i+",wholeSection.len="+wholeSection.length);
			}
			start+=len;
		}
		
		//RXLEVDL
		start=265;
		len=4;
		for(int i=0;i<=63;i++){
			try{
				intVal=TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
				rec.addRxLevDl("rxLevDl"+i, intVal);
				}catch(Exception e){
					e.printStackTrace();
					System.err.println("start="+start+",i="+i+",wholeSection.len="+wholeSection.length);
				}
				start+=len;
		}
		
		return rec;
	}
	
	/**
	 * Record UPLINK AND DOWNLINK SIGNAL QUALITY CELL DATA
	 * @param wholeSection 符合条件的字节流
	 * @return MrrRecord  解析完成的对象
	 * @author peng.jm
	 */
	private  MrrRecord handleMrrSignalQuality(byte[] wholeSection) {
		MrrSignalQualityRec rec = new MrrSignalQualityRec();
		
		int start = 0; //初始化起始位置
		int len = 1;   //初始化获取长度
		
		int intVal=0;
		String strVal="";
		
		//len
		start = 1;
		len = 2;
		intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setRecLen(intVal);
		
		//cellName
		// 文档说是8位，但实际只有7位是有效的，最后一位是看不见的字符
		start = 3;
		len = 7;
		strVal = new String(TranslateTools.subByte(wholeSection, start, len));
		rec.setCellName(strVal);
		
		//subCell
		start = 11;
		len = 1;
		intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setSubCell(intVal);
		
		//Channel group number
		start = 12;
		len = 1;
		intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setChannelGroupNumber(intVal);
		
		//rxQualUl
		start = 13;
		len = 4;
		for (int i = 0; i <= 7; i++) {
			try {
				intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
				rec.addRxQualUl("rxQualUl"+i, intVal);
			} catch (Exception e) {
				System.err.println("start="+start+",i="+i+",wholeSection.len="+wholeSection.length);
			}
			start += len;
		}
		
		//rxQualDl
		start = 45;
		len = 4;
		for (int i = 0; i <= 7; i++) {
			try {
				intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
				rec.addRxQualDl("rxQualDl"+i, intVal);
			} catch (Exception e) {
				System.err.println("start="+start+",i="+i+",wholeSection.len="+wholeSection.length);
			}
			start += len;
		}
		
		return rec;
	}
	
	/**
	 * Record BTS AND MS TRANSMIT POWER LEVEL CELL DATA
	 * @param wholeSection 符合条件的字节流
	 * @return MrrRecord  解析完成的对象
	 * @author peng.jm
	 */
	private  MrrRecord handleMrrTransmitPowerLev(byte[] wholeSection) {
		MrrTransmitPowerLevRec rec = new MrrTransmitPowerLevRec();
		
		int start = 0; //初始化起始位置
		int len = 1;   //初始化获取长度
		
		int intVal=0;
		String strVal="";
		
		//len
		start = 1;
		len = 2;
		intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setRecLen(intVal);
		
		//cellName
		// 文档说是8位，但实际只有7位是有效的，最后一位是看不见的字符
		start = 3;
		len = 7;
		strVal = new String(TranslateTools.subByte(wholeSection, start, len));
		rec.setCellName(strVal);
		
		//subCell
		start = 11;
		len = 1;
		intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setSubCell(intVal);
		
		//Channel group number
		start = 12;
		len = 1;
		intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setChannelGroupNumber(intVal);
		
		//msPowers,共32个，每个长4比特
		start = 13;
		len = 4;
		for (int i = 0; i <= 31; i++) {
			try {
				intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
				rec.addMsPowers("msPowers"+i, intVal);
			} catch (Exception e) {
				System.err.println("start="+start+",i="+i+",wholeSection.len="+wholeSection.length);
			}
			start += len;
		}
		
		//bsPowers,共16个，每个长4比特
		start = 141;
		len = 4;
		for (int i = 0; i <= 15; i++) {
			try {
				intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
				rec.addBsPowers("bsPowers"+i, intVal);
			} catch (Exception e) {
				System.err.println("start="+start+",i="+i+",wholeSection.len="+wholeSection.length);
			}
			start += len;
		}
		
		return rec;
	}
	
	/**
	 * Record ACTUAL TIMING ADVANCE CELL DATA
	 * @param wholeSection 符合条件的字节流
	 * @return MrrRecord  解析完成的对象
	 * @author peng.jm
	 */
	private  MrrRecord handleMrrActulTimingAdvance(byte[] wholeSection) {
		MrrActulTimingAdvanceRec rec = new MrrActulTimingAdvanceRec();
		
		int start = 0; //初始化起始位置
		int len = 1;   //初始化获取长度
		
		int intVal=0;
		String strVal="";
		
		//len
		start = 1;
		len = 2;
		intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setRecLen(intVal);
		
		//cellName
		// 文档说是8位，但实际只有7位是有效的，最后一位是看不见的字符
		start = 3;
		len = 7;
		strVal = new String(TranslateTools.subByte(wholeSection, start, len));
		rec.setCellName(strVal);
		
		//subCell
		start = 11;
		len = 1;
		intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setSubCell(intVal);
		
		//Channel group number
		start = 12;
		len = 1;
		intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setChannelGroupNumber(intVal);
		
		//tavals,共76个，每个长4比特
		start = 13;
		len = 4;
		for (int i = 0; i <= 75; i++) {
			try {
				intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
				rec.addTavals("tavals"+i, intVal);
			} catch (Exception e) {
				System.err.println("start="+start+",i="+i+",wholeSection.len="+wholeSection.length);
			}
			start += len;
		}
		
		return rec;
	}

	/**
	 * Record UPLINK AND DOWNLINK PATH LOSS CELL DATA
	 * @param wholeSection 符合条件的字节流
	 * @return MrrRecord  解析完成的对象
	 * @author peng.jm
	 */
	private  MrrRecord handleMrrPathLoss(byte[] wholeSection) {
		MrrPathLossRec rec = new MrrPathLossRec();
		
		int start = 0; //初始化起始位置
		int len = 1;   //初始化获取长度
		
		int intVal=0;
		String strVal="";
		
		//len
		start = 1;
		len = 2;
		intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setRecLen(intVal);
		
		//cellName
		// 文档说是8位，但实际只有7位是有效的，最后一位是看不见的字符
		start = 3;
		len = 7;
		strVal = new String(TranslateTools.subByte(wholeSection, start, len));
		rec.setCellName(strVal);
		
		//subCell
		start = 11;
		len = 1;
		intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setSubCell(intVal);
		
		//Channel group number
		start = 12;
		len = 1;
		intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setChannelGroupNumber(intVal);
		
		//pLossUls,共60个，每个长4比特
		start = 13;
		len = 4;
		for (int i = 0; i <= 59; i++) {
			try {
				intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
				rec.addPLossUls("pLossUls"+i, intVal);
			} catch (Exception e) {
				System.err.println("start="+start+",i="+i+",wholeSection.len="+wholeSection.length);
			}
			start += len;
		}
		
		//pLossDls,共65个，每个长4比特
		start = 253;
		len = 4;
		for (int i = 0; i <= 64; i++) {
			try {
				intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
				rec.addPLossDls("pLossDls"+i, intVal);
			} catch (Exception e) {
				System.err.println("start="+start+",i="+i+",wholeSection.len="+wholeSection.length);
			}
			start += len;
		}
		
		return rec;
	}
	
	/**
	 * Record PATH LOSS DIFFERENCE CELL DATA
	 * @param wholeSection 符合条件的字节流
	 * @return MrrRecord  解析完成的对象
	 * @author peng.jm
	 */
	private  MrrRecord handleMrrPathLossDifference(byte[] wholeSection) {
		MrrPathLossDifferenceRec rec = new MrrPathLossDifferenceRec();
		
		int start = 0; //初始化起始位置
		int len = 1;   //初始化获取长度
		
		int intVal=0;
		String strVal="";
		
		//len
		start = 1;
		len = 2;
		intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setRecLen(intVal);
		
		//cellName
		// 文档说是8位，但实际只有7位是有效的，最后一位是看不见的字符
		start = 3;
		len = 7;
		strVal = new String(TranslateTools.subByte(wholeSection, start, len));
		rec.setCellName(strVal);
		
		//subCell
		start = 11;
		len = 1;
		intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setSubCell(intVal);
		
		//Channel group number
		start = 12;
		len = 1;
		intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setChannelGroupNumber(intVal);
		
		//pLossUls,共51个，每个长4比特
		start = 13;
		len = 4;
		for (int i = 0; i <= 50; i++) {
			try {
				intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
				rec.addPLDiffs("pLDiffs"+i, intVal);
			} catch (Exception e) {
				System.err.println("start="+start+",i="+i+",wholeSection.len="+wholeSection.length);
			}
			start += len;
		}
		
		return rec;
	}
	
	/**
	 * Record NUMBER OF MEASUREMENT RESULTS CELL DATA
	 * @param wholeSection 符合条件的字节流
	 * @return MrrRecord  解析完成的对象
	 * @author peng.jm
	 */
	private  MrrRecord handleMrrNumOfMeaResults(byte[] wholeSection) {
		MrrNumOfMeaResultsRec rec = new MrrNumOfMeaResultsRec();
		
		int start = 0; //初始化起始位置
		int len = 1;   //初始化获取长度
		
		int intVal=0;
		String strVal="";
		
		//len
		start = 1;
		len = 2;
		intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setRecLen(intVal);
		
		//cellName
		// 文档说是8位，但实际只有7位是有效的，最后一位是看不见的字符
		start = 3;
		len = 7;
		strVal = new String(TranslateTools.subByte(wholeSection, start, len));
		rec.setCellName(strVal);
		
		//subCell
		start = 11;
		len = 1;
		intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setSubCell(intVal);
		
		//Channel group number
		start = 12;
		len = 1;
		intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setChannelGroupNumber(intVal);
		
		//rep
		start = 13;
		len = 4;
		intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setRep(intVal);
		
		//RepferUl
		start = 17;
		len = 4;
		intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setRepferUl(intVal);
		
		//RepferDl
		start = 21;
		len = 4;
		intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setRepferDl(intVal);
		
		//RepferBl
		start = 25;
		len = 4;
		intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setRepferBl(intVal);
		
		//RepferTHL
		start = 29;
		len = 4;
		intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setRepferTHL(intVal);
		
		return rec;
	}
	
	/**
	 * Record UPLINK AND DOWNLINK FRAME ERASURE RATE CELL DATA
	 * @param wholeSection 符合条件的字节流
	 * @return MrrRecord  解析完成的对象
	 * @author peng.jm
	 */
	private  MrrRecord handleMrrFrameErasureRate(byte[] wholeSection) {
		MrrFrameErasureRateRec rec = new MrrFrameErasureRateRec();
		
		int start = 0; //初始化起始位置
		int len = 1;   //初始化获取长度
		
		int intVal=0;
		String strVal="";
		
		//len
		start = 1;
		len = 2;
		intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setRecLen(intVal);
		
		//cellName
		// 文档说是8位，但实际只有7位是有效的，最后一位是看不见的字符
		start = 3;
		len = 7;
		strVal = new String(TranslateTools.subByte(wholeSection, start, len));
		rec.setCellName(strVal);
		
		//subCell
		start = 11;
		len = 1;
		intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setSubCell(intVal);
		
		//Channel group number
		start = 12;
		len = 1;
		intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
		rec.setChannelGroupNumber(intVal);
		
		//ferUls,共97个，每个长4比特
		start = 13;
		len = 4;
		for (int i = 0; i <= 96; i++) {
			try {
				intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
				rec.addFerUls("ferUls"+i, intVal);
			} catch (Exception e) {
				System.err.println("start="+start+",i="+i+",wholeSection.len="+wholeSection.length);
			}
			start += len;
		}
		
		//ferDls,共97个，每个长4比特
		start = 401;
		len = 4;
		for (int i = 0; i <= 96; i++) {
			try {
				intVal = TranslateTools.byte2Int(TranslateTools.subByte(wholeSection, start, len),len);
				rec.addFerDls("ferDls"+i, intVal);
			} catch (Exception e) {
				System.err.println("start="+start+",i="+i+",wholeSection.len="+wholeSection.length);
			}
			start += len;
		}
		return rec;
	}

	/**
	 * mrr文件的数据处理
	 * @param fileName
	 * @param mrrId
	 * @param startTime
	 * @param context
	 * @param token
	 * @author peng.jm
	 * @date 2014-7-22下午04:47:10
	 */
	private void dataProcess(String fileName, long mrrId, Date startTime, MrrParserContext context, String token) {
		
		/**** 插入临时表信息 ****/
		
		log.info("准备批处理插入Mrr管理信息。。。");
		PreparedStatement stmt = context.getPreparedStatement("EriMrrAdmRecord");
		try {
			stmt.executeBatch();
			stmt.clearBatch();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
		log.info("准备批处理插入ta信息。。。");
		stmt = context.getPreparedStatement("EriMrrTaRecord");
		try {
			stmt.executeBatch();
			stmt.clearBatch();
		} catch (SQLException e2) {
			e2.printStackTrace();
			log.error("批处理插入ta信息出错！");
		}
		
		log.info("准备批处理插入帧消除上下行速率信息。。。");
		stmt = context.getPreparedStatement("EriMrrFerRecord");
		try {
			stmt.executeBatch();
			stmt.clearBatch();
		} catch (SQLException e2) {
			e2.printStackTrace();
			log.error("批处理插入帧消除上下行速率信息出错！");
		}
		
		log.info("准备批处理插入测量结果信息。。。");
		stmt = context.getPreparedStatement("EriMrrMeaResultsRecord");
		try {
			stmt.executeBatch();
			stmt.clearBatch();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
		log.info("准备批处理插入路径损耗差异信息。。。");
		stmt = context.getPreparedStatement("EriMrrPldRecord");
		try {
			stmt.executeBatch();
			stmt.clearBatch();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
		log.info("准备批处理插入上下行路径损耗信息。。。");
		stmt = context.getPreparedStatement("EriMrrPlRecord");
		try {
			stmt.executeBatch();
			stmt.clearBatch();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
		log.info("准备批处理插入上下行信号质量信息。。。");
		stmt = context.getPreparedStatement("EriMrrQualityRecord");
		try {
			stmt.executeBatch();
			stmt.clearBatch();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
		log.info("准备批处理插入上下行信号强度信息。。。");
		stmt = context.getPreparedStatement("EriMrrStrengthRecord");
		try {
			stmt.executeBatch();
			stmt.clearBatch();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
		log.info("准备批处理插入传输功率级别信息。。。");
		stmt = context.getPreparedStatement("EriMrrPowerRecord");
		try {
			stmt.executeBatch();
			stmt.clearBatch();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
		Statement statement = null;
		String msg = "";
		try {
			statement = connection.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
			msg = "createStatement出错！code=mrr-1";
			log.error(msg);
			super.setCachedInfo(token, msg);
			return ;
		}
		
		//检查文件是否对应区域
		log.info("验证"+fileName+"文件是否对应所选区域");
		boolean flag = rnoStructureAnalysisDao.checkMrrArea(connection, mrrId, context.getCityId());
		if(!flag) {
			log.error(fileName+"文件所属区域不是选择的区域！");
			context.appedErrorMsg("文件:[" + fileName
					+ "]所属区域不是选择的区域！！<br/>");
			return ;
		}
		
		/**** 将临时表信息转移到正式表 ****/
		//保存mrr描述信息
		stmt = context.getPreparedStatement("EriMrrDesc");
		setMrrDescToStmt(fileName, startTime, stmt, mrrId, context.getAreaId(),
					context.getCityId());

		log.info("插入Mrr描述信息。。。");
		try {
			stmt.executeBatch();
			stmt.clearBatch();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
		//匹配BSC，保存在mrr描述表
		log.info(">>>>>>>>>>>>>>>开始自动计算mrr所测量的bsc");
		setTokenInfo(token, "正在匹配mrr文件所测量的bsc");
		rnoStructureAnalysisDao.matchMrrBsc(connection,"RNO_ERI_MRR_QUALITY_TEMP","RNO_ERI_MRR_DESCRIPTOR",mrrId+"");
		log.info("<<<<<<<<<<<<<<<完成计算mrr所测量的bsc。");

		java.sql.Timestamp meaDate = new java.sql.Timestamp(startTime.getTime());
		String meaDateStr = meaDate.toString();
		
		//转移管理信息中间表数据到目标表
		String fields = "ERI_MRR_DESC_ID,FILE_FORMAT,START_DATE,RECORD_INFO,RID,TOTAL_TIME,MEASURE_LIMIT,MEASURE_SIGN,MEASURE_INTERVAL,MEASURE_TYPE,MEASURING_LINK,MEASURE_LIMIT2,MEASURE_LIMIT3,MEASURE_LIMIT4,CONNECTION_TYPE,DTM_FILTER,CITY_ID";
		String translateSql = "insert into RNO_ERI_MRR_ADM(ADM_ID,MEA_DATE," + fields
				+ ") SELECT SEQ_RNO_MRR_ADM_ID.nextval,TO_DATE('" + meaDateStr.substring(0,meaDateStr.lastIndexOf(".")) + "','YYYY-MM-DD HH24:MI:SS'),t.*" 
				+ " from RNO_ERI_MRR_ADM_TEMP t where t.ERI_MRR_DESC_ID =" + mrrId;
		
		log.info(">>>>>>>>>>>>>>>转移管理信息中间表数据到目标表的sql：" + translateSql);
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
			msg = "数据处理出错！code=mrr-11.sql="+translateSql;
			log.error(msg);
			super.setCachedInfo(token, msg);
			try {
				stmt.close();
			} catch (Exception e3) {
		
			}
			return ;
		}
		log.info("<<<<<<<<<<<<<<<完成转移管理信息中间表数据到目标表");
		
		//转移ta信息中间表数据到目标表
		fields = "ERI_MRR_DESC_ID,CELL_NAME,SUBCELL,CHANNEL_GROUP_NUM,TAVAL0,TAVAL1,TAVAL2,TAVAL3,TAVAL4,TAVAL5,TAVAL6,TAVAL7,TAVAL8,TAVAL9,TAVAL10,TAVAL11,TAVAL12,TAVAL13,TAVAL14,TAVAL15,TAVAL16,TAVAL17,TAVAL18,TAVAL19,TAVAL20,TAVAL21,TAVAL22,TAVAL23,TAVAL24,TAVAL26,TAVAL28,TAVAL29,TAVAL30,TAVAL31,TAVAL32,TAVAL33,TAVAL34,TAVAL35,TAVAL36,TAVAL37,TAVAL38,TAVAL39,TAVAL40,TAVAL41,TAVAL25,TAVAL27,TAVAL42,TAVAL43,TAVAL44,TAVAL45,TAVAL46,TAVAL47,TAVAL48,TAVAL49,TAVAL50,TAVAL51,TAVAL52,TAVAL53,TAVAL54,TAVAL55,TAVAL56,TAVAL57,TAVAL58,TAVAL59,TAVAL60,TAVAL61,TAVAL62,TAVAL63,TAVAL64,TAVAL65,TAVAL66,TAVAL67,TAVAL68,TAVAL69,TAVAL70,TAVAL71,TAVAL72,TAVAL73,TAVAL74,TAVAL75,CITY_ID";
		translateSql = "insert into RNO_ERI_MRR_TA(TA_ID,MEA_DATE," + fields
				+ ") SELECT SEQ_RNO_MRR_TA_ID.nextval,TO_DATE('" + meaDateStr.substring(0,meaDateStr.lastIndexOf(".")) + "','YYYY-MM-DD HH24:MI:SS'),t.*"
				+ " from RNO_ERI_MRR_TA_TEMP t where t.ERI_MRR_DESC_ID =" + mrrId;
		
		log.info(">>>>>>>>>>>>>>>转移ta信息中间表数据到目标表的sql：" + translateSql);
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
			msg = "数据处理出错！code=mrr-11.sql="+translateSql;
			log.error(msg);
			super.setCachedInfo(token, msg);
			try {
				stmt.close();
			} catch (Exception e3) {
		
			}
			return ;
		}
		log.info("<<<<<<<<<<<<<<<完成转移实时预警信息中间表数据到目标表");
		
		//转移帧消除上下行速率信息中间表数据到目标表
		fields = "ERI_MRR_DESC_ID,CELL_NAME,SUBCELL,CHANNEL_GROUP_NUM,FER_UPLINK0,FER_UPLINK1,FER_UPLINK2,FER_UPLINK3,FER_UPLINK4,FER_UPLINK5,FER_UPLINK6,FER_UPLINK7,FER_UPLINK8,FER_UPLINK9,FER_UPLINK10,FER_UPLINK11,FER_UPLINK12,FER_UPLINK13,FER_UPLINK14,FER_UPLINK15,FER_UPLINK16,FER_UPLINK17,FER_UPLINK18,FER_UPLINK19,FER_UPLINK20,FER_UPLINK21,FER_UPLINK22,FER_UPLINK23,FER_UPLINK24,FER_UPLINK25,FER_UPLINK26,FER_UPLINK27,FER_UPLINK28,FER_UPLINK29,FER_UPLINK30,FER_UPLINK31,FER_UPLINK32,FER_UPLINK33,FER_UPLINK34,FER_UPLINK35,FER_UPLINK36,FER_UPLINK37,FER_UPLINK38,FER_UPLINK39,FER_UPLINK40,FER_UPLINK41,FER_UPLINK42,FER_UPLINK43,FER_UPLINK44,FER_UPLINK45,FER_UPLINK46,FER_UPLINK47,FER_UPLINK48,FER_UPLINK49,FER_UPLINK50,FER_UPLINK51,FER_UPLINK52,FER_UPLINK53,FER_UPLINK54,FER_UPLINK55,FER_UPLINK56,FER_UPLINK57,FER_UPLINK58,FER_UPLINK59,FER_UPLINK60,FER_UPLINK61,FER_UPLINK62,FER_UPLINK63,FER_UPLINK64,FER_UPLINK65,FER_UPLINK66,FER_UPLINK67,FER_UPLINK68,FER_UPLINK69,FER_UPLINK70,FER_UPLINK71,FER_UPLINK72,FER_UPLINK73,FER_UPLINK74,FER_UPLINK75,FER_UPLINK76,FER_UPLINK77,FER_UPLINK78,FER_UPLINK79,FER_UPLINK80,FER_UPLINK81,FER_UPLINK82,FER_UPLINK83,FER_UPLINK84,FER_UPLINK85,FER_UPLINK86,FER_UPLINK87,FER_UPLINK88,FER_UPLINK89,FER_UPLINK90,FER_UPLINK91,FER_UPLINK92,FER_UPLINK93,FER_UPLINK94,FER_UPLINK95,FER_UPLINK96,FER_DOWNLINK0,FER_DOWNLINK1,FER_DOWNLINK2,FER_DOWNLINK3,FER_DOWNLINK4,FER_DOWNLINK5,FER_DOWNLINK6,FER_DOWNLINK7,FER_DOWNLINK8,FER_DOWNLINK9,FER_DOWNLINK10,FER_DOWNLINK11,FER_DOWNLINK12,FER_DOWNLINK13,FER_DOWNLINK14,FER_DOWNLINK15,FER_DOWNLINK16,FER_DOWNLINK17,FER_DOWNLINK18,FER_DOWNLINK19,FER_DOWNLINK20,FER_DOWNLINK21,FER_DOWNLINK22,FER_DOWNLINK23,FER_DOWNLINK24,FER_DOWNLINK25,FER_DOWNLINK26,FER_DOWNLINK27,FER_DOWNLINK28,FER_DOWNLINK29,FER_DOWNLINK30,FER_DOWNLINK31,FER_DOWNLINK32,FER_DOWNLINK33,FER_DOWNLINK34,FER_DOWNLINK35,FER_DOWNLINK36,FER_DOWNLINK37,FER_DOWNLINK38,FER_DOWNLINK39,FER_DOWNLINK40,FER_DOWNLINK41,FER_DOWNLINK42,FER_DOWNLINK43,FER_DOWNLINK44,FER_DOWNLINK45,FER_DOWNLINK46,FER_DOWNLINK47,FER_DOWNLINK48,FER_DOWNLINK49,FER_DOWNLINK50,FER_DOWNLINK51,FER_DOWNLINK52,FER_DOWNLINK53,FER_DOWNLINK54,FER_DOWNLINK55,FER_DOWNLINK56,FER_DOWNLINK57,FER_DOWNLINK58,FER_DOWNLINK59,FER_DOWNLINK60,FER_DOWNLINK61,FER_DOWNLINK62,FER_DOWNLINK63,FER_DOWNLINK64,FER_DOWNLINK65,FER_DOWNLINK66,FER_DOWNLINK67,FER_DOWNLINK68,FER_DOWNLINK69,FER_DOWNLINK70,FER_DOWNLINK71,FER_DOWNLINK72,FER_DOWNLINK73,FER_DOWNLINK74,FER_DOWNLINK75,FER_DOWNLINK76,FER_DOWNLINK77,FER_DOWNLINK78,FER_DOWNLINK79,FER_DOWNLINK80,FER_DOWNLINK81,FER_DOWNLINK82,FER_DOWNLINK83,FER_DOWNLINK84,FER_DOWNLINK85,FER_DOWNLINK86,FER_DOWNLINK87,FER_DOWNLINK88,FER_DOWNLINK89,FER_DOWNLINK90,FER_DOWNLINK91,FER_DOWNLINK92,FER_DOWNLINK93,FER_DOWNLINK94,FER_DOWNLINK95,FER_DOWNLINK96,CITY_ID";
		translateSql = "insert into RNO_ERI_MRR_FER(FER_ID,MEA_DATE," + fields
				+ ") SELECT SEQ_RNO_MRR_FER_ID.nextval,TO_DATE('" + meaDateStr.substring(0,meaDateStr.lastIndexOf(".")) + "','YYYY-MM-DD HH24:MI:SS'),t.*"
				+ " from RNO_ERI_MRR_FER_TEMP t where t.ERI_MRR_DESC_ID =" + mrrId;
		
		log.info(">>>>>>>>>>>>>>>转移帧消除上下行速率信息中间表数据到目标表的sql：" + translateSql);
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
			msg = "数据处理出错！code=mrr-11,sql="+translateSql;
			log.error(msg);
			super.setCachedInfo(token, msg);
			try {
				stmt.close();
			} catch (Exception e3) {
		
			}
			return ;
		}
		log.info("<<<<<<<<<<<<<<<完成转移帧消除上下行速率信息中间表数据到目标表");
		
		//转移测量结果信息中间表数据到目标表
		fields = "ERI_MRR_DESC_ID,CELL_NAME,SUBCELL,CHANNEL_GROUP_NUM,REP,REP_FER_UPLINK,REP_FER_DOWNLINK,REP_FER_BL,REP_FER_THL,CITY_ID";
		translateSql = "insert into RNO_ERI_MRR_MEA_RESULTS(MEASURE_RESULTS_ID,MEA_DATE," + fields
				+ ") SELECT SEQ_RNO_MRR_MEA_RES_ID.nextval,TO_DATE('" + meaDateStr.substring(0,meaDateStr.lastIndexOf(".")) + "','YYYY-MM-DD HH24:MI:SS'),t.*"
				+ " from RNO_ERI_MRR_MEA_RESULTS_TEMP t where t.ERI_MRR_DESC_ID =" + mrrId;
		
		log.info(">>>>>>>>>>>>>>>转移测量结果信息中间表数据到目标表的sql：" + translateSql);
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
			msg = "数据处理出错！code=mrr-11,sql="+translateSql;
			log.error(msg);
			super.setCachedInfo(token, msg);
			try {
				stmt.close();
			} catch (Exception e3) {
		
			}
			return ;
		}
		log.info("<<<<<<<<<<<<<<<完成转移测量结果信息中间表数据到目标表");
		
		//转移路径损耗差异信息中间表数据到目标表
		fields = "ERI_MRR_DESC_ID,CELL_NAME,SUBCELL,CHANNEL_GROUP_NUM,PLDIFF0,PLDIFF1,PLDIFF2,PLDIFF3,PLDIFF4,PLDIFF5,PLDIFF6,PLDIFF7,PLDIFF8,PLDIFF9,PLDIFF10,PLDIFF11,PLDIFF12,PLDIFF13,PLDIFF14,PLDIFF15,PLDIFF16,PLDIFF17,PLDIFF18,PLDIFF19,PLDIFF20,PLDIFF21,PLDIFF22,PLDIFF23,PLDIFF24,PLDIFF25,PLDIFF26,PLDIFF27,PLDIFF28,PLDIFF29,PLDIFF30,PLDIFF31,PLDIFF32,PLDIFF33,PLDIFF34,PLDIFF35,PLDIFF36,PLDIFF37,PLDIFF38,PLDIFF39,PLDIFF40,PLDIFF41,PLDIFF42,PLDIFF43,PLDIFF44,PLDIFF45,PLDIFF46,PLDIFF47,PLDIFF48,PLDIFF49,PLDIFF50,CITY_ID";
		translateSql = "insert into RNO_ERI_MRR_PLD(PATH_LOSS_DIFF_ID,MEA_DATE," + fields
				+ ") SELECT SEQ_RNO_MRR_PLD_ID.nextval,TO_DATE('" + meaDateStr.substring(0,meaDateStr.lastIndexOf(".")) + "','YYYY-MM-DD HH24:MI:SS'),t.*"
				+ " from RNO_ERI_MRR_PLD_TEMP t where t.ERI_MRR_DESC_ID =" + mrrId;
		
		log.info(">>>>>>>>>>>>>>>转移路径损耗差异信息中间表数据到目标表的sql：" + translateSql);
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
			msg = "数据处理出错！code=mrr-11,sql="+translateSql;
			log.error(msg);
			super.setCachedInfo(token, msg);
			try {
				stmt.close();
			} catch (Exception e3) {
		
			}
			return ;
		}
		log.info("<<<<<<<<<<<<<<<完成转移路径损耗差异信息中间表数据到目标表");
		
		//转移上下行路径损耗信息中间表数据到目标表
		fields = "ERI_MRR_DESC_ID,CELL_NAME,SUBCELL,CHANNEL_GROUP_NUM,PLOSSUL0,PLOSSUL1,PLOSSUL2,PLOSSUL3,PLOSSUL4,PLOSSUL5,PLOSSUL6,PLOSSUL7,PLOSSUL8,PLOSSUL9,PLOSSUL10,PLOSSUL11,PLOSSUL12,PLOSSUL13,PLOSSUL14,PLOSSUL15,PLOSSUL16,PLOSSUL17,PLOSSUL18,PLOSSUL19,PLOSSUL20,PLOSSUL21,PLOSSUL22,PLOSSUL23,PLOSSUL24,PLOSSUL25,PLOSSUL26,PLOSSUL27,PLOSSUL28,PLOSSUL29,PLOSSUL30,PLOSSUL31,PLOSSUL32,PLOSSUL33,PLOSSUL34,PLOSSUL35,PLOSSUL36,PLOSSUL37,PLOSSUL38,PLOSSUL39,PLOSSUL40,PLOSSUL41,PLOSSUL42,PLOSSUL43,PLOSSUL44,PLOSSUL45,PLOSSUL46,PLOSSUL47,PLOSSUL48,PLOSSUL49,PLOSSUL50,PLOSSUL51,PLOSSUL52,PLOSSUL53,PLOSSUL54,PLOSSUL55,PLOSSUL56,PLOSSUL57,PLOSSUL58,PLOSSUL59,PLOSSDL0,PLOSSDL1,PLOSSDL2,PLOSSDL3,PLOSSDL4,PLOSSDL5,PLOSSDL6,PLOSSDL7,PLOSSDL8,PLOSSDL9,PLOSSDL10,PLOSSDL11,PLOSSDL12,PLOSSDL13,PLOSSDL14,PLOSSDL15,PLOSSDL16,PLOSSDL17,PLOSSDL18,PLOSSDL19,PLOSSDL20,PLOSSDL21,PLOSSDL22,PLOSSDL23,PLOSSDL24,PLOSSDL25,PLOSSDL26,PLOSSDL27,PLOSSDL28,PLOSSDL29,PLOSSDL30,PLOSSDL31,PLOSSDL32,PLOSSDL33,PLOSSDL34,PLOSSDL35,PLOSSDL36,PLOSSDL37,PLOSSDL38,PLOSSDL39,PLOSSDL40,PLOSSDL41,PLOSSDL42,PLOSSDL43,PLOSSDL44,PLOSSDL45,PLOSSDL46,PLOSSDL47,PLOSSDL48,PLOSSDL49,PLOSSDL50,PLOSSDL51,PLOSSDL52,PLOSSDL53,PLOSSDL54,PLOSSDL55,PLOSSDL56,PLOSSDL57,PLOSSDL58,PLOSSDL59,PLOSSDL60,PLOSSDL61,PLOSSDL62,PLOSSDL63,PLOSSDL64,CITY_ID";
		translateSql = "insert into RNO_ERI_MRR_PL(PATH_LOSS_ID,MEA_DATE," + fields
				+ ") SELECT SEQ_RNO_MRR_PL_ID.nextval,TO_DATE('" + meaDateStr.substring(0,meaDateStr.lastIndexOf(".")) + "','YYYY-MM-DD HH24:MI:SS'),t.*"
				+ " from RNO_ERI_MRR_PL_TEMP t where t.ERI_MRR_DESC_ID =" + mrrId;
		
		log.info(">>>>>>>>>>>>>>>转移上下行路径损耗信息中间表数据到目标表的sql：" + translateSql);
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
			msg = "数据处理出错！code=mrr-11,sql="+translateSql;
			log.error(msg);
			super.setCachedInfo(token, msg);
			try {
				stmt.close();
			} catch (Exception e3) {
		
			}
			return ;
		}
		log.info("<<<<<<<<<<<<<<<完成转移上下行路径损耗信息中间表数据到目标表");

		//转移上下行信号质量信息中间表数据到目标表
		fields = "ERI_MRR_DESC_ID,CELL_NAME,SUBCELL,CHANNEL_GROUP_NUM,RXQUALUL0,RXQUALUL1,RXQUALUL2,RXQUALUL3,RXQUALUL4,RXQUALUL5,RXQUALUL6,RXQUALUL7,RXQUALDL0,RXQUALDL1,RXQUALDL2,RXQUALDL3,RXQUALDL4,RXQUALDL5,RXQUALDL6,RXQUALDL7,CITY_ID";
		translateSql = "insert into RNO_ERI_MRR_QUALITY(SIGNAL_QUALITY_ID,MEA_DATE," + fields
				+ ") SELECT SEQ_RNO_MRR_QUALITY_ID.nextval,TO_DATE('" + meaDateStr.substring(0,meaDateStr.lastIndexOf(".")) + "','YYYY-MM-DD HH24:MI:SS'),t.*"
				+ " from RNO_ERI_MRR_QUALITY_TEMP t where t.ERI_MRR_DESC_ID =" + mrrId;
		
		log.info(">>>>>>>>>>>>>>>转移上下行信号质量信息中间表数据到目标表的sql：" + translateSql);
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
			msg = "数据处理出错！code=mrr-11,sql="+translateSql;
			log.error(msg);
			super.setCachedInfo(token, msg);
			try {
				stmt.close();
			} catch (Exception e3) {
		
			}
			return ;
		}
		log.info("<<<<<<<<<<<<<<<完成转移上下行信号质量信息中间表数据到目标表");
		
		//转移上下行信号强度信息中间表数据到目标表
		fields = "ERI_MRR_DESC_ID,CELL_NAME,SUBCELL,CHANNEL_GROUP_NUM,RXLEVUL0,RXLEVUL1,RXLEVUL2,RXLEVUL3,RXLEVUL4,RXLEVUL5,RXLEVUL6,RXLEVUL7,RXLEVUL8,RXLEVUL9,RXLEVUL10,RXLEVUL11,RXLEVUL12,RXLEVUL13,RXLEVUL14,RXLEVUL15,RXLEVUL16,RXLEVUL17,RXLEVUL18,RXLEVUL19,RXLEVUL20,RXLEVUL21,RXLEVUL22,RXLEVUL23,RXLEVUL24,RXLEVUL25,RXLEVUL26,RXLEVUL27,RXLEVUL28,RXLEVUL29,RXLEVUL30,RXLEVUL31,RXLEVUL32,RXLEVUL33,RXLEVUL34,RXLEVUL35,RXLEVUL36,RXLEVUL37,RXLEVUL38,RXLEVUL39,RXLEVUL40,RXLEVUL41,RXLEVUL42,RXLEVUL43,RXLEVUL44,RXLEVUL45,RXLEVUL46,RXLEVUL47,RXLEVUL48,RXLEVUL49,RXLEVUL50,RXLEVUL51,RXLEVUL52,RXLEVUL53,RXLEVUL54,RXLEVUL55,RXLEVUL56,RXLEVUL57,RXLEVUL58,RXLEVUL59,RXLEVUL60,RXLEVUL61,RXLEVUL62,RXLEVUL63,RXLEVDL0,RXLEVDL1,RXLEVDL2,RXLEVDL3,RXLEVDL4,RXLEVDL5,RXLEVDL6,RXLEVDL7,RXLEVDL8,RXLEVDL9,RXLEVDL10,RXLEVDL11,RXLEVDL12,RXLEVDL13,RXLEVDL14,RXLEVDL15,RXLEVDL16,RXLEVDL17,RXLEVDL18,RXLEVDL19,RXLEVDL20,RXLEVDL21,RXLEVDL22,RXLEVDL23,RXLEVDL24,RXLEVDL25,RXLEVDL26,RXLEVDL27,RXLEVDL28,RXLEVDL29,RXLEVDL30,RXLEVDL31,RXLEVDL32,RXLEVDL33,RXLEVDL34,RXLEVDL35,RXLEVDL36,RXLEVDL37,RXLEVDL38,RXLEVDL39,RXLEVDL40,RXLEVDL41,RXLEVDL42,RXLEVDL43,RXLEVDL44,RXLEVDL45,RXLEVDL46,RXLEVDL47,RXLEVDL48,RXLEVDL49,RXLEVDL50,RXLEVDL51,RXLEVDL52,RXLEVDL53,RXLEVDL54,RXLEVDL55,RXLEVDL56,RXLEVDL57,RXLEVDL58,RXLEVDL59,RXLEVDL60,RXLEVDL61,RXLEVDL62,RXLEVDL63,CITY_ID";
		translateSql = "insert into RNO_ERI_MRR_STRENGTH(SIGNAL_STRENGTH_ID,MEA_DATE," + fields
				+ ") SELECT SEQ_RNO_MRR_STRENGTH_ID.nextval,TO_DATE('" + meaDateStr.substring(0,meaDateStr.lastIndexOf(".")) + "','YYYY-MM-DD HH24:MI:SS'),t.*"
				+ " from RNO_ERI_MRR_STRENGTH_TEMP t where t.ERI_MRR_DESC_ID =" + mrrId;
		
		log.info(">>>>>>>>>>>>>>>转移上下行信号强度信息中间表数据到目标表的sql：" + translateSql);
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
			msg = "数据处理出错！code=mrr-11,sql="+translateSql;
			log.error(msg);
			super.setCachedInfo(token, msg);
			try {
				stmt.close();
			} catch (Exception e3) {
		
			}
			return ;
		}
		log.info("<<<<<<<<<<<<<<<完成转移上下行信号强度信息中间表数据到目标表");
		
		//转移传输功率级别信息中间表数据到目标表
		fields = "ERI_MRR_DESC_ID,CELL_NAME,SUBCELL,CHANNEL_GROUP_NUM,MSPOWER0,MSPOWER1,MSPOWER2,MSPOWER3,MSPOWER4,MSPOWER5,MSPOWER6,MSPOWER7,MSPOWER8,MSPOWER9,MSPOWER10,MSPOWER11,MSPOWER12,MSPOWER13,MSPOWER14,MSPOWER15,MSPOWER16,MSPOWER17,MSPOWER18,MSPOWER19,MSPOWER20,MSPOWER21,MSPOWER22,MSPOWER23,MSPOWER24,MSPOWER25,MSPOWER26,MSPOWER27,MSPOWER28,MSPOWER29,MSPOWER30,MSPOWER31,BSPOWER0,BSPOWER1,BSPOWER2,BSPOWER3,BSPOWER4,BSPOWER5,BSPOWER6,BSPOWER7,BSPOWER8,BSPOWER9,BSPOWER10,BSPOWER11,BSPOWER12,BSPOWER13,BSPOWER14,BSPOWER15,CITY_ID";
		translateSql = "insert into RNO_ERI_MRR_POWER(TRANSMIT_POWER_ID,MEA_DATE," + fields
				+ ") SELECT SEQ_RNO_MRR_POWER_ID.nextval,TO_DATE('" + meaDateStr.substring(0,meaDateStr.lastIndexOf(".")) + "','YYYY-MM-DD HH24:MI:SS'),t.*"
				+ " from RNO_ERI_MRR_POWER_TEMP t where t.ERI_MRR_DESC_ID =" + mrrId;
		
		log.info(">>>>>>>>>>>>>>>转移传输功率级别信息中间表数据到目标表的sql：" + translateSql);
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
			msg = "数据处理出错！code=mrr-11,sql="+translateSql;
			log.error(msg);
			super.setCachedInfo(token, msg);
			try {
				stmt.close();
			} catch (Exception e3) {
		
			}
			return ;
		}
		log.info("<<<<<<<<<<<<<<<完成转移传输功率级别信息中间表数据到目标表");
		
		try {
			statement.close();
		} catch (Exception e3) {

		}
	}
	
	/**
	 * 清理资源
	 * @param destDir
	 * @param context
	 * @author peng.jm
	 * @date 2014-7-18上午11:31:00
	 */
	private void clearResource(String destDir, MrrParserContext context) {
		FileTool.deleteDir(destDir);
		if (context != null) {
			context.closeAllStatement();
		}
	}
}
