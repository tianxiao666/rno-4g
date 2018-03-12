package com.iscreate.op.service.rno.parser.jobrunnable;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.iscreate.op.pojo.rno.RnoDataCollectRec;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.common.JobState;
import com.iscreate.op.service.rno.parser.DataParseProgress;
import com.iscreate.op.service.rno.parser.DataParseStatus;
import com.iscreate.op.service.rno.parser.jobmanager.FileInterpreter;
import com.iscreate.op.service.rno.parser.vo.G4PmDateRec;
import com.iscreate.op.service.rno.parser.vo.G4PmFileHeaderRec;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.FileTool;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.op.service.rno.tool.ZipFileHandler;

public class G4StsDataParserJobRunnable extends DbParserBaseJobRunnable {

	private static Log log = LogFactory.getLog(G4StsDataParserJobRunnable.class);

	private File file=null;
	private String filePath=null;
	
	// 构建insert语句
	private static String stsDataDescTab = "RNO_4G_STS_DESC";
	private static String stsDataMeaTab = "RNO_4G_STS_MEA_DATA";

	public G4StsDataParserJobRunnable() {
		super();
		super.setJobType("4GSTSDATAFILE");
	}

	
	@Override
	public JobStatus runJobInternal(JobProfile job, Connection connection,	Statement stmt) {

		long jobId = job.getJobId();
		JobStatus status = new JobStatus(jobId);
		JobReport report = new JobReport(jobId);

		// 获取job相关的信息
		String sql = "select * from RNO_DATA_COLLECT_REC where JOB_ID=" + jobId;
		List<Map<String, Object>> recs = RnoHelper.commonQuery(stmt, sql);
		RnoDataCollectRec dataRec = null;
		if (recs != null && recs.size() > 0) {
			dataRec = RnoHelper.commonInjection(RnoDataCollectRec.class,
					recs.get(0), new DateUtil());
		}
		// log.debug("jobId=" + jobId + ",对应的dataRec=" + dataRec);
		if (dataRec == null) {
			log.error("转换RnoDataCollectRec失败！");
			// 失败了
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());

			// 报告
			report.setFinishState("失败");
			report.setStage("获取上传文件");
			report.setBegTime(new Date());
			report.setReportType(1);
			report.setAttMsg("未找到上传文件！");
			addJobReport(report);

			return status;
		}

		// 准备
		long cityId = dataRec.getCityId();
		String fileName = dataRec.getFileName();
		filePath = FileInterpreter.makeFullPath(dataRec.getFullPath());
//		file = new File(filePath);
		file=FileTool.getFile(filePath);
		long dataId = dataRec.getDataCollectId();

		String msg = "";

		// 开始解析
		List<File> allFiles = new ArrayList<File>();// 将所有待处理的DT文件放置在这个列表里
		boolean fromZip = false;
		String destDir = "";
		Date date1 = new Date(), date2;
		if (fileName.endsWith(".zip") || fileName.endsWith("ZIP")
				|| fileName.endsWith("Zip")) {
			date1 = new Date();
			fromZip = true;
			// 压缩包
			log.info("上传的Pm文件是一个压缩包。");

			// 进行解压
			String path = file.getParentFile().getPath();
			destDir = path + "/"
					+ UUID.randomUUID().toString().replaceAll("-", "") + "/";

			//
			boolean unzipOk = false;
			try {
				unzipOk = ZipFileHandler.unZip(
						FileInterpreter.makeFullPath(file.getAbsolutePath()),
						destDir);
			} catch (Exception e) {
				msg = "压缩包解析失败！请确认压缩包文件是否被破坏！";
				log.error(msg);
				e.printStackTrace();
				// super.setCachedInfo(token, msg);
				// clearResource(destDir, null);

				// job报告
				date2 = new Date();
				report.setFinishState(DataParseStatus.Failall.toString());
				report.setStage(DataParseProgress.Decompress.toString());
				report.setBegTime(date1);
				report.setEndTime(date2);
				report.setAttMsg("压缩包解析失败！请确认压缩包文件是否被破坏！");
				addJobReport(report);

				// 数据记录本身的状态
				sql = "update rno_data_collect_rec set FILE_STATUS='"
						+ DataParseStatus.Failall.toString()
						+ "' where DATA_COLLECT_ID="
						+ dataId;
				try {
					stmt.executeUpdate(sql);
					connection.commit();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				// job status
				status.setJobState(JobState.Failed);
				status.setUpdateTime(date2);
				return status;

			}
			if (!unzipOk) {
				msg = "解压失败 ！仅支持zip格式的压缩包！ ";
				log.error(msg);
				// super.setCachedInfo(token, msg);
				// clearResource(destDir, null);

				// job报告
				date2 = new Date();
				report.setFinishState(DataParseStatus.Failall.toString());
				report.setStage(DataParseProgress.Decompress.toString());
				report.setBegTime(date1);
				report.setEndTime(date2);
				report.setAttMsg("解压失败 ！仅支持zip格式的压缩包！");
				addJobReport(report);

				// 数据记录本身的状态
				sql = "update rno_data_collect_rec set FILE_STATUS='"
						+ DataParseStatus.Failall.toString()
						+ "' where DATA_COLLECT_ID="
						+ dataId;
				try {
					stmt.executeUpdate(sql);
					connection.commit();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				// job status
				status.setJobState(JobState.Failed);
				status.setUpdateTime(date2);
				return status;

			}
			file = new File(destDir);
			File[] files = file.listFiles();
			for (File f : files) {
				// 只要文件，不要目录
				if (f.isFile() && !f.isHidden()) {
					allFiles.add(f);
				}
			}

			// ---报告----//
			date2 = new Date();
			report.setBegTime(date1);
			report.setEndTime(date2);
			report.setFinishState(DataParseStatus.Succeded.toString());
			report.setStage(DataParseProgress.Decompress.toString());
			report.setAttMsg("解压文件：" + fileName + ",大小："
					+ RnoHelper.getPropSizeExpression(dataRec.getFileSize()));
			addJobReport(report);
		} else if (fileName.endsWith(".rar")) {
			msg = "请用zip格式压缩文件！";
			log.error(msg);
			// super.setCachedInfo(token, msg);
			// clearResource(destDir, null);

			// job报告
			date2 = new Date();
			report.setFinishState(DataParseStatus.Failall.toString());
			report.setStage(DataParseProgress.Decompress.toString());
			report.setBegTime(date1);
			report.setEndTime(date2);
			report.setAttMsg("解压失败 ！请用zip格式压缩文件！");
			addJobReport(report);

			// 数据记录本身的状态
			sql = "update rno_data_collect_rec set FILE_STATUS='"
					+ DataParseStatus.Failall.toString()
					+ "' where DATA_COLLECT_ID=" + dataId;
			try {
				stmt.executeUpdate(sql);
				connection.commit();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			// job status
			status.setJobState(JobState.Failed);
			status.setUpdateTime(date2);
			return status;

		} else {
			log.info("上传的PM是一个普通文件。");
			allFiles.add(file);
		}
			if (allFiles.isEmpty()) {
				msg = "未上传有效的DT文件！zip包里不能再包含有文件夹！";
				log.error(msg);
			// super.setCachedInfo(token, msg);
			// clearResource(destDir, null);
			// job报告
			date2 = new Date();
			report.setFinishState(DataParseStatus.Failall.toString());
			report.setStage(DataParseProgress.Decompress.toString());
			report.setBegTime(date1);
			report.setEndTime(date2);
			report.setAttMsg("未上传有效的PM文件！注意zip包里不能再包含有文件夹！");
			addJobReport(report);

			// 数据记录本身的状态
			sql = "update rno_data_collect_rec set FILE_STATUS='"
					+ DataParseStatus.Failall.toString()
					+ "' where DATA_COLLECT_ID=" + dataId;
			try {
				stmt.executeUpdate(sql);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			// job status
			status.setJobState(JobState.Failed);
			status.setUpdateTime(date2);
			return status;
		}

		// 正在解析
		sql = "update rno_data_collect_rec set FILE_STATUS='"
				+ DataParseStatus.Parsing.toString()
				+ "' where DATA_COLLECT_ID=" + dataId;
		try {
			stmt.executeUpdate(sql);
			connection.commit();
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		String tmpFileName = fileName;
		int sucCnt = 0;
		boolean parseOk = false;
		
		
		int totalFileCnt = allFiles.size();
		int i = 0;
		for (File f : allFiles) {
			try {
				// 每一个文件的解析都应该是独立的
				if (fromZip) {
					tmpFileName = f.getName();
				}
				date1 = new Date();
				G4PmFileHeaderRec fileHeader=parserPmToFh(f);
				//System.out.println(tmpFileName);
				parseOk = parsePm(fileHeader, connection,stmt,cityId,tmpFileName);
				i++;
				date2 = new Date();
				report.setStage("文件处理总结");
				report.setReportType(1);
				if (parseOk) {
					report.setFinishState(DataParseStatus.Succeded.toString());
				} else {
					report.setFinishState(DataParseStatus.Failall.toString());
				}
				report.setBegTime(date1);
				report.setEndTime(date2);
				if (parseOk) {
					report.setAttMsg("成功完成文件（" + i + "/" + totalFileCnt + "）:"
							+ tmpFileName);
					sucCnt++;
				} else {
					report.setAttMsg("失败完成文件（" + i + "/" + totalFileCnt + "）:"
							+ tmpFileName);
				}
				addJobReport(report);
			} catch (Exception e) {
				e.printStackTrace();
				date2 = new Date();
				msg = tmpFileName + "文件解析出错！";
				report.setStage("文件处理总结");
				report.setFinishState(DataParseStatus.Failall.toString());
				report.setBegTime(date1);
				report.setEndTime(date2);
				report.setReportType(1);
				report.setAttMsg("文件解析出错（" + i + "/" + totalFileCnt + "）:"
						+ tmpFileName);
				addJobReport(report);
				log.error(msg);
				}
			}

		if (sucCnt > 0) {
			status.setJobState(JobState.Finished);
			status.setUpdateTime(new Date());
		} else {
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
		}

		if (sucCnt == allFiles.size()) {
			// 全部成功
			sql = "update rno_data_collect_rec set file_status='"
					+ DataParseStatus.Succeded.toString()
					+ "' where data_collect_id=" + dataId;
		} else {
			if (sucCnt > 0) {
				sql = "update rno_data_collect_rec set file_status='"
						+ DataParseStatus.Failpartly.toString()
						+ "' where data_collect_id="
						+ dataId;
			} else {
				sql = "update rno_data_collect_rec set file_status='"
						+ DataParseStatus.Failall.toString()
						+ "' where data_collect_id="
						+ dataId;
			}
		}
		log.debug("更新结果状态 rno_data_collect_rec  sql:" + sql);
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;
	}


	@Override
	public void updateOwnProgress(JobStatus jobStatus) {
		if (jobStatus == null) {
			return;
		}
		if (stmt != null) {
			try {
				String prog = jobStatus.getProgress();
				if (prog == null) {
					prog = "";
				}
				prog = prog.trim();
				if ("".equals(prog)) {
					prog = jobStatus.getJobState().getCode();
				}
				String str = "update RNO_DATA_COLLECT_REC set file_status='"
						+ prog + "' where job_id=" + jobStatus.getJobId();
				stmt.executeUpdate(str);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}
	/**
	 * 
	 * @title 判断正负、整数小数即是否为数字
	 * @param str
	 * @return
	 * @author chao.xj
	 * @date 2015-7-4下午4:33:12
	 * @company 怡创科技
	 * @version 2.0.1
	 */
/*	public  boolean isNum(String str) {
		//可以判断正负、整数小数
	    Boolean strResult = str.matches("-?[0-9]+.*[0-9]*");
	    return strResult;
	}*/
/*	public Map<String, DBFieldToTitle> readDbToTitleCfgFromXml(String conFileName) {
		Map<String, DBFieldToTitle> dbCfgs = new TreeMap<String, DBFieldToTitle>();
		try {
			InputStream in = new FileInputStream(new File(
					G4DtDataServiceParserJobRunnable.class.getResource(
							conFileName).getPath()));
			SAXReader reader = new SAXReader();
			Document doc = reader.read(in);
			Element root = doc.getRootElement();
			for (Object o : root.elements()) {
				Element e = (Element) o;
				DBFieldToTitle dt = new DBFieldToTitle();
				for (Object obj : e.elements()) {
					Element e1 = (Element) obj;
					String key = e1.getName();
					String val = e1.getTextTrim();
					if ("name".equals(key)) {
						dt.setDbField(val);
					}
					if ("type".equals(key)) {
						dt.setDbType(val);
					}
					if ("essential".equals(key)) {
						if (StringUtils.equals(val, "1")) {
							// mandaroty
							dt.setMandatory(true);
						} else {
							dt.setMandatory(false);
						}
					}
					if ("match".equals(key)) {
						dt.setMatchType(Integer.parseInt(val));
					}
					if ("exceltitle".equals(key)) {
						String[] v = val.split(",");
						for (String vo : v) {
							dt.addTitle(vo);
						}
					}

				}
				dbCfgs.put(dt.dbField, dt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dbCfgs;
	}*/

	
	public G4PmFileHeaderRec parserPmToFh(File file) {
		G4PmFileHeaderRec fh=new G4PmFileHeaderRec();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = factory.newDocumentBuilder();
			//org.w3c.dom.Document doc = db.parse(new File(fileName));
			org.w3c.dom.Document doc = db.parse(file);
			org.w3c.dom.Element elmtInfo = doc.getDocumentElement(); 
			NodeList nodes = elmtInfo.getChildNodes(); 
			Node fileHeader = getNode(nodes, "FileHeader");
			NodeList fileHeaderList = fileHeader.getChildNodes();
			
			String InfoModelReferenced = getNode(fileHeaderList,"InfoModelReferenced").getTextContent();			
			fh.setInfoModelReferenced(InfoModelReferenced);
			
			String DnPrefix = getNode(fileHeaderList, "DnPrefix").getTextContent();
			fh.setDnPrefix(DnPrefix);

			String SenderName = getNode(fileHeaderList, "SenderName")	.getTextContent();
			fh.setSenderName(SenderName);

			String VendorName = getNode(fileHeaderList, "VendorName").getTextContent();
			fh.setVendorName(VendorName);
			
			String JobId = getNode(fileHeaderList, "JobId").getTextContent();
			fh.setJobId(JobId);
			
			String BeginTime = getNode(fileHeaderList, "BeginTime").getTextContent();
			fh.setBeginTime(BeginTime);

			String EndTime = getNode(fileHeaderList, "EndTime").getTextContent();
			fh.setEndTime(EndTime);
			
			Node Measurements = getNode(nodes, "Measurements");
			Node pmName = getNode(Measurements.getChildNodes(), "PmName");
			fh.setPmName(pmName);
			
			Node pmData = getNode(Measurements.getChildNodes(), "PmData");
			fh.setPmData(pmData);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fh;
	}
    public Node getNode(NodeList nodes,String cnName ){
    	//找到数据库配置所在的node节点
    	 for(int i=0;i< nodes.getLength();i++){
         	Node result = nodes.item(i);
         	if(result.getNodeType() == Node.ELEMENT_NODE && result.getNodeName().equals(cnName))
         		return result;
    	 }	
    	 return null;   		   
    }
    
	private boolean parsePm(G4PmFileHeaderRec fh, Connection connection,Statement stmt,	long cityId,String fileName) throws Exception {
		
		boolean flag=false;
		String vSql="select id,filename from  "+stsDataDescTab+
				" where jobid="+Integer.parseInt(fh.getJobId().replace("{","").replace("}", ""))+
				" and senderName='"+fh.getSenderName()+
				"' and infomodelreferenced='"+fh.getInfoModelReferenced()+
				"' and begintime=to_date('"+fh.getBeginTime().replace("T", " ").replace("+08:00", "")+"','yyyy-mm-dd hh24:mi:ss')" +
				" and endtime=to_date('"+fh.getEndTime().replace("T", " ").replace("+08:00", "")+"','yyyy-mm-dd hh24:mi:ss')";
		ResultSet vrs = stmt.executeQuery(vSql);
		while(vrs.next()){
			String dbFileName = vrs.getString("FILENAME");
			if(fileName.equals(dbFileName)){
				String uSql = "update "+stsDataDescTab+" set area_id="+cityId+", mod_time = sysdate where id="+vrs.getInt("ID");
				flag=stmt.executeUpdate(uSql)>0?true:false;
				return flag;
			}
		}
		Node pmName=fh.getPmName();
		NodeList pmNameList = pmName.getChildNodes();
		Map<String, String> namelist = new HashMap<String, String>();
		for (int i = 0; i < pmNameList.getLength(); i++) {
			Node nameNode = pmNameList.item(i);
			if (nameNode.getNodeType() == Node.ELEMENT_NODE
					&& nameNode.getNodeName().equals("N")) {
				NamedNodeMap nameAttrList = nameNode.getAttributes();
				if (nameAttrList.getLength() > 0) {
					String id = nameAttrList.getNamedItem("i").getNodeValue();
					namelist.put(id, nameNode.getTextContent());
				}
			}
		}
		Node pmData = fh.getPmData();
		NodeList pmDataList = pmData.getChildNodes();
		List<G4PmDateRec> pmDataModelList=new ArrayList<G4PmDateRec>();
		//Map<String, Node> cellName = new HashMap<String, Node>();
		//Map<String, Map<String, Map<String, String>>> datas = new HashMap<String, Map<String, Map<String, String>>>();
		for (int j = 0; j < pmDataList.getLength(); j++) {// 解析数据库配置信息
			Node node = pmDataList.item(j);
			if (node.getNodeType() == Node.ELEMENT_NODE
					&& node.getNodeName().equals("Pm")) {
				G4PmDateRec pmDataM=new G4PmDateRec();
				NamedNodeMap attrList = node.getAttributes();
				if (attrList.getLength() > 0) {
					String pmDn = attrList.getNamedItem("Dn").getNodeValue();
					pmDataM.setPmDn(pmDn);
					String pmUserLabel = attrList.getNamedItem("UserLabel").getNodeValue();
					pmDataM.setPmUserLabel(pmUserLabel);
				}
				NodeList pmNodeList=node.getChildNodes();
				Map<String, String> data = new HashMap<String, String>();
				for(int k=0;k<pmNodeList.getLength();k++){
					Node pmNode=pmNodeList.item(k);
					if (pmNode.getNodeType() == Node.ELEMENT_NODE
							&& pmNode.getNodeName().equals("V")) {
						NamedNodeMap pmDataAttrList = pmNode.getAttributes();
						if (pmDataAttrList.getLength() > 0) {
							String i = pmDataAttrList.getNamedItem("i").getNodeValue();
							String name=namelist.get(i);							
							data.put(name, pmNode.getTextContent());
						}
					}else if(pmNode.getNodeType() == Node.ELEMENT_NODE
							&& pmNode.getNodeName().equals("CV")){
/*						NamedNodeMap pmDataAttrList = pmNode.getAttributes();
						if(pmDataAttrList.getLength() > 0){
							String i = pmDataAttrList.getNamedItem("i").getNodeValue();*/
							NodeList list = pmNode.getChildNodes();
							List<String> snList = new ArrayList<String>();
							List<String> svList = new ArrayList<String>();
							for (int a = 0; a < list.getLength(); a++) {
								Node snode = list.item(a);
								if (snode.getNodeType() == Node.ELEMENT_NODE
										&& snode.getNodeName().equals("SN")) {
									snList.add(snode.getTextContent());
								} else if (snode.getNodeType() == Node.ELEMENT_NODE
										&& snode.getNodeName().equals("SV")) {
									svList.add(snode.getTextContent());
								}
							}
							if (snList.size() == svList.size()) {
								for (int b = 0; b < snList.size(); b++) {
									data.put(snList.get(b), svList.get(b));
								}
							}						
//						}
					}
				}
				pmDataM.setContext_AttRelEnb(Float.parseFloat(data.get("CONTEXT.AttRelEnb")));
				pmDataM.setContext_AttRelEnb_Normal(Float.parseFloat(data.get("CONTEXT.AttRelEnb.Normal")));
				pmDataM.setContext_SuccInitalSetup(Float.parseFloat(data.get("CONTEXT.SuccInitalSetup")));
				pmDataM.setErab_HoFail(Float.parseFloat(data.get("ERAB.HoFail")));
				pmDataM.setErab_HoFail_1(Float.parseFloat(data.get("ERAB.HoFail.1")));
				pmDataM.setErab_HoFail_2(Float.parseFloat(data.get("ERAB.HoFail.2")));
				pmDataM.setErab_HoFail_3(Float.parseFloat(data.get("ERAB.HoFail.3")));
				pmDataM.setErab_HoFail_4(Float.parseFloat(data.get("ERAB.HoFail.4")));
				pmDataM.setErab_HoFail_5(Float.parseFloat(data.get("ERAB.HoFail.5")));
				pmDataM.setErab_HoFail_6(Float.parseFloat(data.get("ERAB.HoFail.6")));
				pmDataM.setErab_HoFail_7(Float.parseFloat(data.get("ERAB.HoFail.7")));
				pmDataM.setErab_HoFail_8(Float.parseFloat(data.get("ERAB.HoFail.8")));
				pmDataM.setErab_HoFail_9(Float.parseFloat(data.get("ERAB.HoFail.9")));
				pmDataM.setErab_NbrAttEstab(Float.parseFloat(data.get("ERAB.NbrAttEstab")));
				pmDataM.setErab_NbrAttEstab_1(Float.parseFloat(data.get("ERAB.NbrAttEstab.1")));
				pmDataM.setErab_NbrAttEstab_2(Float.parseFloat(data.get("ERAB.NbrAttEstab.2")));
				pmDataM.setErab_NbrAttEstab_3(Float.parseFloat(data.get("ERAB.NbrAttEstab.3")));
				pmDataM.setErab_NbrAttEstab_4(Float.parseFloat(data.get("ERAB.NbrAttEstab.4")));
				pmDataM.setErab_NbrAttEstab_5(Float.parseFloat(data.get("ERAB.NbrAttEstab.5")));
				pmDataM.setErab_NbrAttEstab_6(Float.parseFloat(data.get("ERAB.NbrAttEstab.6")));
				pmDataM.setErab_NbrAttEstab_7(Float.parseFloat(data.get("ERAB.NbrAttEstab.7")));
				pmDataM.setErab_NbrAttEstab_8(Float.parseFloat(data.get("ERAB.NbrAttEstab.8")));
				pmDataM.setErab_NbrAttEstab_9(Float.parseFloat(data.get("ERAB.NbrAttEstab.9")));
				pmDataM.setErab_NbrHoInc_1(Float.parseFloat(data.get("ERAB.NbrHoInc.1")));
				pmDataM.setErab_NbrHoInc_2(Float.parseFloat(data.get("ERAB.NbrHoInc.2")));
				pmDataM.setErab_NbrHoInc_3(Float.parseFloat(data.get("ERAB.NbrHoInc.3")));
				pmDataM.setErab_NbrHoInc_4(Float.parseFloat(data.get("ERAB.NbrHoInc.4")));
				pmDataM.setErab_NbrHoInc_5(Float.parseFloat(data.get("ERAB.NbrHoInc.5")));
				pmDataM.setErab_NbrHoInc_6(Float.parseFloat(data.get("ERAB.NbrHoInc.6")));
				pmDataM.setErab_NbrHoInc_7(Float.parseFloat(data.get("ERAB.NbrHoInc.7")));
				pmDataM.setErab_NbrHoInc_8(Float.parseFloat(data.get("ERAB.NbrHoInc.8")));
				pmDataM.setErab_NbrHoInc_9(Float.parseFloat(data.get("ERAB.NbrHoInc.9")));
				pmDataM.setErab_NbrLeft_1(Float.parseFloat(data.get("ERAB.NbrLeft.1")));
				pmDataM.setErab_NbrLeft_2(Float.parseFloat(data.get("ERAB.NbrLeft.2")));
				pmDataM.setErab_NbrLeft_3(Float.parseFloat(data.get("ERAB.NbrLeft.3")));
				pmDataM.setErab_NbrLeft_4(Float.parseFloat(data.get("ERAB.NbrLeft.4")));
				pmDataM.setErab_NbrLeft_5(Float.parseFloat(data.get("ERAB.NbrLeft.5")));
				pmDataM.setErab_NbrLeft_6(Float.parseFloat(data.get("ERAB.NbrLeft.6")));
				pmDataM.setErab_NbrLeft_7(Float.parseFloat(data.get("ERAB.NbrLeft.7")));
				pmDataM.setErab_NbrLeft_8(Float.parseFloat(data.get("ERAB.NbrLeft.8")));
				pmDataM.setErab_NbrLeft_9(Float.parseFloat(data.get("ERAB.NbrLeft.9")));
				pmDataM.setErab_NbrReqRelEnb(Float.parseFloat(data.get("ERAB.NbrReqRelEnb")));
				pmDataM.setErab_NbrReqRelEnb_1(Float.parseFloat(data.get("ERAB.NbrReqRelEnb.1")));
				pmDataM.setErab_NbrReqRelEnb_2(Float.parseFloat(data.get("ERAB.NbrReqRelEnb.2")));
				pmDataM.setErab_NbrReqRelEnb_3(Float.parseFloat(data.get("ERAB.NbrReqRelEnb.3")));
				pmDataM.setErab_NbrReqRelEnb_4(Float.parseFloat(data.get("ERAB.NbrReqRelEnb.4")));
				pmDataM.setErab_NbrReqRelEnb_5(Float.parseFloat(data.get("ERAB.NbrReqRelEnb.5")));
				pmDataM.setErab_NbrReqRelEnb_6(Float.parseFloat(data.get("ERAB.NbrReqRelEnb.6")));
				pmDataM.setErab_NbrReqRelEnb_7(Float.parseFloat(data.get("ERAB.NbrReqRelEnb.7")));
				pmDataM.setErab_NbrReqRelEnb_8(Float.parseFloat(data.get("ERAB.NbrReqRelEnb.8")));
				pmDataM.setErab_NbrReqRelEnb_9(Float.parseFloat(data.get("ERAB.NbrReqRelEnb.9")));
				pmDataM.setErab_NbrReqRelEnb_Normal(Float.parseFloat(data.get("ERAB.NbrReqRelEnb.Normal")));
				pmDataM.setErab_NbrReqRelEnb_Normal_1(Float.parseFloat(data.get("ERAB.NbrReqRelEnb.Normal.1")));
				pmDataM.setErab_NbrReqRelEnb_Normal_2(Float.parseFloat(data.get("ERAB.NbrReqRelEnb.Normal.2")));
				pmDataM.setErab_NbrReqRelEnb_Normal_3(Float.parseFloat(data.get("ERAB.NbrReqRelEnb.Normal.3")));
				pmDataM.setErab_NbrReqRelEnb_Normal_4(Float.parseFloat(data.get("ERAB.NbrReqRelEnb.Normal.4")));
				pmDataM.setErab_NbrReqRelEnb_Normal_5(Float.parseFloat(data.get("ERAB.NbrReqRelEnb.Normal.5")));
				pmDataM.setErab_NbrReqRelEnb_Normal_6(Float.parseFloat(data.get("ERAB.NbrReqRelEnb.Normal.6")));
				pmDataM.setErab_NbrReqRelEnb_Normal_7(Float.parseFloat(data.get("ERAB.NbrReqRelEnb.Normal.7")));
				pmDataM.setErab_NbrReqRelEnb_Normal_8(Float.parseFloat(data.get("ERAB.NbrReqRelEnb.Normal.8")));
				pmDataM.setErab_NbrReqRelEnb_Normal_9(Float.parseFloat(data.get("ERAB.NbrReqRelEnb.Normal.9")));
				pmDataM.setErab_NbrSuccEstab(Float.parseFloat(data.get("ERAB.NbrSuccEstab")));
				pmDataM.setErab_NbrSuccEstab_1(Float.parseFloat(data.get("ERAB.NbrSuccEstab.1")));
				pmDataM.setErab_NbrSuccEstab_2(Float.parseFloat(data.get("ERAB.NbrSuccEstab.2")));
				pmDataM.setErab_NbrSuccEstab_3(Float.parseFloat(data.get("ERAB.NbrSuccEstab.3")));
				pmDataM.setErab_NbrSuccEstab_4(Float.parseFloat(data.get("ERAB.NbrSuccEstab.4")));
				pmDataM.setErab_NbrSuccEstab_5(Float.parseFloat(data.get("ERAB.NbrSuccEstab.5")));
				pmDataM.setErab_NbrSuccEstab_6(Float.parseFloat(data.get("ERAB.NbrSuccEstab.6")));
				pmDataM.setErab_NbrSuccEstab_7(Float.parseFloat(data.get("ERAB.NbrSuccEstab.7")));
				pmDataM.setErab_NbrSuccEstab_8(Float.parseFloat(data.get("ERAB.NbrSuccEstab.8")));
				pmDataM.setErab_NbrSuccEstab_9(Float.parseFloat(data.get("ERAB.NbrSuccEstab.9")));
				pmDataM.setHo_AttOutInterEnbS1(Float.parseFloat(data.get("HO.AttOutInterEnbS1")));
				pmDataM.setHo_AttOutInterEnbX2(Float.parseFloat(data.get("HO.AttOutInterEnbX2")));
				pmDataM.setHo_AttOutIntraEnb(Float.parseFloat(data.get("HO.AttOutIntraEnb")));
				pmDataM.setHo_SuccOutInterEnbS1(Float.parseFloat(data.get("HO.SuccOutInterEnbS1")));
				pmDataM.setHo_SuccOutInterEnbX2(Float.parseFloat(data.get("HO.SuccOutInterEnbX2")));
				pmDataM.setHo_SuccOutIntraEnb(Float.parseFloat(data.get("HO.SuccOutIntraEnb")));
				pmDataM.setPdcp_UpOctDl(Float.parseFloat(data.get("PDCP.UpOctDl")));
				pmDataM.setPdcp_UpOctDl_1(Float.parseFloat(data.get("PDCP.UpOctDl.1")));
				pmDataM.setPdcp_UpOctDl_2(Float.parseFloat(data.get("PDCP.UpOctDl.2")));
				pmDataM.setPdcp_UpOctDl_3(Float.parseFloat(data.get("PDCP.UpOctDl.3")));
				pmDataM.setPdcp_UpOctDl_4(Float.parseFloat(data.get("PDCP.UpOctDl.4")));
				pmDataM.setPdcp_UpOctDl_5(Float.parseFloat(data.get("PDCP.UpOctDl.5")));
				pmDataM.setPdcp_UpOctDl_6(Float.parseFloat(data.get("PDCP.UpOctDl.6")));
				pmDataM.setPdcp_UpOctDl_7(Float.parseFloat(data.get("PDCP.UpOctDl.7")));
				pmDataM.setPdcp_UpOctDl_8(Float.parseFloat(data.get("PDCP.UpOctDl.8")));
				pmDataM.setPdcp_UpOctDl_9(Float.parseFloat(data.get("PDCP.UpOctDl.9")));
				pmDataM.setPdcp_UpOctUl(Float.parseFloat(data.get("PDCP.UpOctUl")));
				pmDataM.setPdcp_UpOctUl_1(Float.parseFloat(data.get("PDCP.UpOctUl.1")));
				pmDataM.setPdcp_UpOctUl_2(Float.parseFloat(data.get("PDCP.UpOctUl.2")));
				pmDataM.setPdcp_UpOctUl_3(Float.parseFloat(data.get("PDCP.UpOctUl.3")));
				pmDataM.setPdcp_UpOctUl_4(Float.parseFloat(data.get("PDCP.UpOctUl.4")));
				pmDataM.setPdcp_UpOctUl_5(Float.parseFloat(data.get("PDCP.UpOctUl.5")));
				pmDataM.setPdcp_UpOctUl_6(Float.parseFloat(data.get("PDCP.UpOctUl.6")));
				pmDataM.setPdcp_UpOctUl_7(Float.parseFloat(data.get("PDCP.UpOctUl.7")));
				pmDataM.setPdcp_UpOctUl_8(Float.parseFloat(data.get("PDCP.UpOctUl.8")));
				pmDataM.setPdcp_UpOctUl_9(Float.parseFloat(data.get("PDCP.UpOctUl.9")));
				pmDataM.setRrc_AttConnEstab(Float.parseFloat(data.get("RRC.AttConnEstab")));
				pmDataM.setRrc_AttConnReestab(Float.parseFloat(data.get("RRC.AttConnReestab")));
				pmDataM.setRrc_SuccConnEstab(Float.parseFloat(data.get("RRC.SuccConnEstab")));
			pmDataModelList.add(pmDataM);
			}
		}
		
		PreparedStatement insertFh=null;
		PreparedStatement insertPmData=null;
		String seqsql = "select Seq_"+stsDataDescTab+".nextval id from dual";
		ResultSet  seqrs=stmt.executeQuery(seqsql);
		int seq=0;
		while(seqrs.next()){
			seq=seqrs.getInt("ID");
		}
		String fhField=" (id,area_id,create_time,infomodelreferenced,dnprefix,sendername,vendorname,jobid,begintime,endtime,CNT,FILENAME,MOD_TIME)";
		String insertFhSql="insert into "+stsDataDescTab+fhField+"values("+seq+","+cityId+",sysdate,?,?,?,?,?,?,?,?,'"+fileName+"',sysdate)";
		insertFh=connection.prepareStatement(insertFhSql);
		insertFh.setString(1,fh.getInfoModelReferenced());
		insertFh.setString(2,fh.getDnPrefix());
		insertFh.setString(3,fh.getSenderName());
		insertFh.setString(4,fh.getVendorName());
		insertFh.setInt(5,Integer.parseInt(fh.getJobId().replace("{","").replace("}", "")));
		insertFh.setTimestamp(6,toDate(fh.getBeginTime()));
		insertFh.setTimestamp(7,toDate(fh.getEndTime()));
		insertFh.setInt(8,pmDataModelList.size());
		insertFh.addBatch();
		String pdField="(ID,fhead_id,pmdn,pmuserlabel,CONTEXT_AttRelEnb,CONTEXT_AttRelEnb_Normal,CONTEXT_SuccInitalSetup,ERAB_HoFail,ERAB_HoFail_1,ERAB_HoFail_2,ERAB_HoFail_3,ERAB_HoFail_4,ERAB_HoFail_5,ERAB_HoFail_6,ERAB_HoFail_7,ERAB_HoFail_8,ERAB_HoFail_9,ERAB_NbrAttEstab,ERAB_NbrAttEstab_1,ERAB_NbrAttEstab_2,ERAB_NbrAttEstab_3,ERAB_NbrAttEstab_4,ERAB_NbrAttEstab_5,ERAB_NbrAttEstab_6,ERAB_NbrAttEstab_7,ERAB_NbrAttEstab_8,ERAB_NbrAttEstab_9,ERAB_NbrHoInc_1,ERAB_NbrHoInc_2,ERAB_NbrHoInc_3,ERAB_NbrHoInc_4,ERAB_NbrHoInc_5,ERAB_NbrHoInc_6,ERAB_NbrHoInc_7,ERAB_NbrHoInc_8,ERAB_NbrHoInc_9,ERAB_NbrLeft_1,ERAB_NbrLeft_2,ERAB_NbrLeft_3,ERAB_NbrLeft_4,ERAB_NbrLeft_5,ERAB_NbrLeft_6,ERAB_NbrLeft_7,ERAB_NbrLeft_8,ERAB_NbrLeft_9,ERAB_NbrReqRelEnb,ERAB_NbrReqRelEnb_1,ERAB_NbrReqRelEnb_2,ERAB_NbrReqRelEnb_3,ERAB_NbrReqRelEnb_4,ERAB_NbrReqRelEnb_5,ERAB_NbrReqRelEnb_6,ERAB_NbrReqRelEnb_7,ERAB_NbrReqRelEnb_8,ERAB_NbrReqRelEnb_9,ERAB_NbrReqRelEnb_Normal,ERAB_NbrReqRelEnb_Normal_1,ERAB_NbrReqRelEnb_Normal_2,ERAB_NbrReqRelEnb_Normal_3,ERAB_NbrReqRelEnb_Normal_4,ERAB_NbrReqRelEnb_Normal_5,ERAB_NbrReqRelEnb_Normal_6,ERAB_NbrReqRelEnb_Normal_7,ERAB_NbrReqRelEnb_Normal_8,ERAB_NbrReqRelEnb_Normal_9,ERAB_NbrSuccEstab,ERAB_NbrSuccEstab_1,ERAB_NbrSuccEstab_2,ERAB_NbrSuccEstab_3,ERAB_NbrSuccEstab_4,ERAB_NbrSuccEstab_5,ERAB_NbrSuccEstab_6,ERAB_NbrSuccEstab_7,ERAB_NbrSuccEstab_8,ERAB_NbrSuccEstab_9,HO_AttOutInterEnbS1,HO_AttOutInterEnbX2,HO_AttOutIntraEnb,HO_SuccOutInterEnbS1,HO_SuccOutInterEnbX2,HO_SuccOutIntraEnb,PDCP_UpOctDl,PDCP_UpOctDl_1,PDCP_UpOctDl_2,PDCP_UpOctDl_3,PDCP_UpOctDl_4,PDCP_UpOctDl_5,PDCP_UpOctDl_6,PDCP_UpOctDl_7,PDCP_UpOctDl_8,PDCP_UpOctDl_9,PDCP_UpOctUl,PDCP_UpOctUl_1,PDCP_UpOctUl_2,PDCP_UpOctUl_3,PDCP_UpOctUl_4,PDCP_UpOctUl_5,PDCP_UpOctUl_6,PDCP_UpOctUl_7,PDCP_UpOctUl_8,PDCP_UpOctUl_9,RRC_AttConnEstab,RRC_AttConnReestab,RRC_SuccConnEstab)";
		String insertPdSql="insert into "+stsDataMeaTab+pdField+"values(SEQ_"+stsDataMeaTab+".nextval,"+seq+",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		insertPmData=connection.prepareStatement(insertPdSql);
		
		for(G4PmDateRec pm:pmDataModelList){
			insertPmData.setString(1, pm.getPmDn());
			insertPmData.setString(2, pm.getPmUserLabel());
			insertPmData.setFloat(3,pm.getContext_AttRelEnb());
			insertPmData.setFloat(4,pm.getContext_AttRelEnb_Normal());
			insertPmData.setFloat(5,pm.getContext_SuccInitalSetup());
			insertPmData.setFloat(6,pm.getErab_HoFail());
			insertPmData.setFloat(7,pm.getErab_HoFail_1());
			insertPmData.setFloat(8,pm.getErab_HoFail_2());
			insertPmData.setFloat(9,pm.getErab_HoFail_3());
			insertPmData.setFloat(10,pm.getErab_HoFail_4());
			insertPmData.setFloat(11,pm.getErab_HoFail_5());
			insertPmData.setFloat(12,pm.getErab_HoFail_6());
			insertPmData.setFloat(13,pm.getErab_HoFail_7());
			insertPmData.setFloat(14,pm.getErab_HoFail_8());
			insertPmData.setFloat(15,pm.getErab_HoFail_9());
			insertPmData.setFloat(16,pm.getErab_NbrAttEstab());
			insertPmData.setFloat(17,pm.getErab_NbrAttEstab_1());
			insertPmData.setFloat(18,pm.getErab_NbrAttEstab_2());
			insertPmData.setFloat(19,pm.getErab_NbrAttEstab_3());
			insertPmData.setFloat(20,pm.getErab_NbrAttEstab_4());
			insertPmData.setFloat(21,pm.getErab_NbrAttEstab_5());
			insertPmData.setFloat(22,pm.getErab_NbrAttEstab_6());
			insertPmData.setFloat(23,pm.getErab_NbrAttEstab_7());
			insertPmData.setFloat(24,pm.getErab_NbrAttEstab_8());
			insertPmData.setFloat(25,pm.getErab_NbrAttEstab_9());
			insertPmData.setFloat(26,pm.getErab_NbrHoInc_1());
			insertPmData.setFloat(27,pm.getErab_NbrHoInc_2());
			insertPmData.setFloat(28,pm.getErab_NbrHoInc_3());
			insertPmData.setFloat(29,pm.getErab_NbrHoInc_4());
			insertPmData.setFloat(30,pm.getErab_NbrHoInc_5());
			insertPmData.setFloat(31,pm.getErab_NbrHoInc_6());
			insertPmData.setFloat(32,pm.getErab_NbrHoInc_7());
			insertPmData.setFloat(33,pm.getErab_NbrHoInc_8());
			insertPmData.setFloat(34,pm.getErab_NbrHoInc_9());
			insertPmData.setFloat(35,pm.getErab_NbrLeft_1());
			insertPmData.setFloat(36,pm.getErab_NbrLeft_2());
			insertPmData.setFloat(37,pm.getErab_NbrLeft_3());
			insertPmData.setFloat(38,pm.getErab_NbrLeft_4());
			insertPmData.setFloat(39,pm.getErab_NbrLeft_5());
			insertPmData.setFloat(40,pm.getErab_NbrLeft_6());
			insertPmData.setFloat(41,pm.getErab_NbrLeft_7());
			insertPmData.setFloat(42,pm.getErab_NbrLeft_8());
			insertPmData.setFloat(43,pm.getErab_NbrLeft_9());
			insertPmData.setFloat(44,pm.getErab_NbrReqRelEnb());
			insertPmData.setFloat(45,pm.getErab_NbrReqRelEnb_1());
			insertPmData.setFloat(46,pm.getErab_NbrReqRelEnb_2());
			insertPmData.setFloat(47,pm.getErab_NbrReqRelEnb_3());
			insertPmData.setFloat(48,pm.getErab_NbrReqRelEnb_4());
			insertPmData.setFloat(49,pm.getErab_NbrReqRelEnb_5());
			insertPmData.setFloat(50,pm.getErab_NbrReqRelEnb_6());
			insertPmData.setFloat(51,pm.getErab_NbrReqRelEnb_7());
			insertPmData.setFloat(52,pm.getErab_NbrReqRelEnb_8());
			insertPmData.setFloat(53,pm.getErab_NbrReqRelEnb_9());
			insertPmData.setFloat(54,pm.getErab_NbrReqRelEnb_Normal());
			insertPmData.setFloat(55,pm.getErab_NbrReqRelEnb_Normal_1());
			insertPmData.setFloat(56,pm.getErab_NbrReqRelEnb_Normal_2());
			insertPmData.setFloat(57,pm.getErab_NbrReqRelEnb_Normal_3());
			insertPmData.setFloat(58,pm.getErab_NbrReqRelEnb_Normal_4());
			insertPmData.setFloat(59,pm.getErab_NbrReqRelEnb_Normal_5());
			insertPmData.setFloat(60,pm.getErab_NbrReqRelEnb_Normal_6());
			insertPmData.setFloat(61,pm.getErab_NbrReqRelEnb_Normal_7());
			insertPmData.setFloat(62,pm.getErab_NbrReqRelEnb_Normal_8());
			insertPmData.setFloat(63,pm.getErab_NbrReqRelEnb_Normal_9());
			insertPmData.setFloat(64,pm.getErab_NbrSuccEstab());
			insertPmData.setFloat(65,pm.getErab_NbrSuccEstab_1());
			insertPmData.setFloat(66,pm.getErab_NbrSuccEstab_2());
			insertPmData.setFloat(67,pm.getErab_NbrSuccEstab_3());
			insertPmData.setFloat(68,pm.getErab_NbrSuccEstab_4());
			insertPmData.setFloat(69,pm.getErab_NbrSuccEstab_5());
			insertPmData.setFloat(70,pm.getErab_NbrSuccEstab_6());
			insertPmData.setFloat(71,pm.getErab_NbrSuccEstab_7());
			insertPmData.setFloat(72,pm.getErab_NbrSuccEstab_8());
			insertPmData.setFloat(73,pm.getErab_NbrSuccEstab_9());
			insertPmData.setFloat(74,pm.getHo_AttOutInterEnbS1());
			insertPmData.setFloat(75,pm.getHo_AttOutInterEnbX2());
			insertPmData.setFloat(76,pm.getHo_AttOutIntraEnb());
			insertPmData.setFloat(77,pm.getHo_SuccOutInterEnbS1());
			insertPmData.setFloat(78,pm.getHo_SuccOutInterEnbX2());
			insertPmData.setFloat(79,pm.getHo_SuccOutIntraEnb());
			insertPmData.setFloat(80,pm.getPdcp_UpOctDl());
			insertPmData.setFloat(81,pm.getPdcp_UpOctDl_1());
			insertPmData.setFloat(82,pm.getPdcp_UpOctDl_2());
			insertPmData.setFloat(83,pm.getPdcp_UpOctDl_3());
			insertPmData.setFloat(84,pm.getPdcp_UpOctDl_4());
			insertPmData.setFloat(85,pm.getPdcp_UpOctDl_5());
			insertPmData.setFloat(86,pm.getPdcp_UpOctDl_6());
			insertPmData.setFloat(87,pm.getPdcp_UpOctDl_7());
			insertPmData.setFloat(88,pm.getPdcp_UpOctDl_8());
			insertPmData.setFloat(89,pm.getPdcp_UpOctDl_9());
			insertPmData.setFloat(90,pm.getPdcp_UpOctUl());
			insertPmData.setFloat(91,pm.getPdcp_UpOctUl_1());
			insertPmData.setFloat(92,pm.getPdcp_UpOctUl_2());
			insertPmData.setFloat(93,pm.getPdcp_UpOctUl_3());
			insertPmData.setFloat(94,pm.getPdcp_UpOctUl_4());
			insertPmData.setFloat(95,pm.getPdcp_UpOctUl_5());
			insertPmData.setFloat(96,pm.getPdcp_UpOctUl_6());
			insertPmData.setFloat(97,pm.getPdcp_UpOctUl_7());
			insertPmData.setFloat(98,pm.getPdcp_UpOctUl_8());
			insertPmData.setFloat(99,pm.getPdcp_UpOctUl_9());
			insertPmData.setFloat(100,pm.getRrc_AttConnEstab());
			insertPmData.setFloat(101,pm.getRrc_AttConnReestab());
			insertPmData.setFloat(102,pm.getRrc_SuccConnEstab());
			
			insertPmData.addBatch();

		}
		int[] state=insertPmData.executeBatch();
		if(state.length==pmDataModelList.size()){
			insertFh.executeBatch();
			flag=true;
		}
		insertFh.close();
		insertPmData.close();
		return flag;
	}
	
	public  java.sql.Timestamp toDate(String st) {
		String datest=st.split("\\+")[0].replace("T", " ");
		//String date="to_date("+datest+",'yyyy-MM-dd HH24:mi:ss'";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date date = null;
		try {
			date = sdf.parse(datest);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new java.sql.Timestamp(date.getTime());
	}
	
	public void releaseRes() {
		try {
			if (file != null && file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		FileTool.deleteLocOrHdfsFileOrDir(filePath);
		super.releaseRes();
	}
}

