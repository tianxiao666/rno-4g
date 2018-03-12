package com.iscreate.op.service.rno;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.Eri2GCellChannelQueryCond;
import com.iscreate.op.action.rno.model.Eri2GCellDescQueryCond;
import com.iscreate.op.action.rno.model.Eri2GCellQueryCond;
import com.iscreate.op.action.rno.model.Eri2GNcellQueryCond;
import com.iscreate.op.action.rno.model.Eri2GNcsDescQueryCond;
import com.iscreate.op.dao.rno.AuthDsDataDaoImpl;
import com.iscreate.op.dao.rno.Rno2GEriCellManageDao;
import com.iscreate.op.dao.rno.Rno2GEriCellManageDaoImpl;
import com.iscreate.op.dao.rno.AuthDsDataDaoImpl.SysArea;
import com.iscreate.op.service.rno.parser.jobrunnable.Eri2GCellParserJobRunnable;
import com.iscreate.op.service.rno.parser.jobrunnable.Eri2GCellParserJobRunnable.DBFieldToLogTitle;
import com.iscreate.op.service.rno.task.EriCellDataExportManager;
import com.iscreate.op.service.rno.task.EriCellDataExportManagerImpl.ExportToken;
import com.iscreate.op.service.rno.tool.RnoHelper;

public class Rno2GEriCellManageServiceImpl implements Rno2GEriCellManageService {

	private static Log log = LogFactory
			.getLog(Rno2GEriCellManageServiceImpl.class);
//	private Rno2GEriCellManageDao eriCellManageDao=new Rno2GEriCellManageDaoImpl();
	private Rno2GEriCellManageDao rno2gEriCellManageDao;
	private EriCellDataExportManager eriCellDataExportManager;
	
	public EriCellDataExportManager getEriCellDataExportManager() {
		return eriCellDataExportManager;
	}

	public void setEriCellDataExportManager(
			EriCellDataExportManager eriCellDataExportManager) {
		this.eriCellDataExportManager = eriCellDataExportManager;
	}

	public Rno2GEriCellManageDao getRno2gEriCellManageDao() {
		return rno2gEriCellManageDao;
	}

	public void setRno2gEriCellManageDao(Rno2GEriCellManageDao rno2gEriCellManageDao) {
		this.rno2gEriCellManageDao = rno2gEriCellManageDao;
	}

	/**
	 * 
	 * @title 通过区域查询BSC/hibernate
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2014-10-15上午10:18:07
	 * @company 怡创科技
	 * @version 1.2
	 */
	public Map<String, Integer> queryBscByCityId(long cityId) {
		// TODO Auto-generated method stub
		log.debug("进入queryBscByCityId(long cityId)"+cityId);
		log.debug("rno2gEriCellManageDao:"+rno2gEriCellManageDao);
		List<Map<String, Object>> bscs= rno2gEriCellManageDao.queryBscByCityId(cityId);
		if(bscs==null){
			return null;
		}
		Map<String, Object> bsc;
		Map<String, Integer> bscToId=new HashMap<String, Integer>();
		String engName="";
		int bscId=0;
		for (int i = 0; i < bscs.size(); i++) {
			bsc= bscs.get(i);
			for (String key : bsc.keySet()) {
				if("ENGNAME".equals(key)){
					engName=bsc.get(key).toString();
				}
				if("BSC_ID".equals(key)){
					bscId=Integer.parseInt(bsc.get(key).toString());
				}
			}
			bscToId.put(engName, bscId);
		}
		log.debug("退出queryBscByCityId(long cityId)"+bscToId);
		return bscToId;
	}
	/**
	 * 
	 * @title 通过区域查询BSC/非hibernate
	 * @param stmt
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2014-10-15下午4:38:24
	 * @company 怡创科技
	 * @version 1.2
	 */
	public Map<String, Integer> queryBscByCityId(Statement stmt,long cityId) {
		String areaStr=AuthDsDataDaoImpl.getSubAreaAndSelfIdListStrByParentId(cityId);
		String sql = "select bsc_id,engname from rno_bsc where bsc_id in(select bsc_id from rno_bsc_rela_area where area_id in("+areaStr+"))";
		List<Map<String, Object>> bscs=RnoHelper.commonQuery(stmt, sql);
		// TODO Auto-generated method stub
		log.debug("进入queryBscByCityId(Statement stmt,long cityId)"+stmt+"--"+cityId);
		log.debug("rno2gEriCellManageDao:"+rno2gEriCellManageDao);
		if(bscs==null){
			return null;
		}
		Map<String, Object> bsc;
		Map<String, Integer> bscToId=new HashMap<String, Integer>();
		String engName="";
		int bscId=0;
		for (int i = 0; i < bscs.size(); i++) {
			bsc= bscs.get(i);
			for (String key : bsc.keySet()) {
				if("ENGNAME".equals(key)){
					engName=bsc.get(key).toString();
				}
				if("BSC_ID".equals(key)){
					bscId=Integer.parseInt(bsc.get(key).toString());
				}
			}
			bscToId.put(engName, bscId);
		}
		log.debug("退出queryBscByCityId(long cityId)"+bscToId);
		return bscToId;
	}
	/**
	 * 
	 * @title 查询符合条件的爱立信2G小区描述记录数量
	 * @param eri2GCellDescQueryCond
	 * @return
	 * @author chao.xj
	 * @date 2014-10-16下午10:32:46
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long queryEriCellDescCnt(Eri2GCellDescQueryCond eri2GCellDescQueryCond) {
		return rno2gEriCellManageDao.queryEriCellDescCnt(eri2GCellDescQueryCond);
	}

	/**
	 * 
	 * @title 分页查询符合条件的爱立信2G小区描述记录
	 * @param eri2GCellDescQueryCond
	 * @param newPage
	 * @return
	 * @author chao.xj
	 * @date 2014-10-16下午10:32:22
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryEriCellDescByPage(
			Eri2GCellDescQueryCond eri2GCellDescQueryCond, Page newPage) {
		return rno2gEriCellManageDao.queryEriCellDescByPage(
				eri2GCellDescQueryCond, newPage);
	}
	/**
	 * 
	 * @title 获取爱立信2G小区、信道组、邻区字段参数集合
	 * @return
	 * @author chao.xj
	 * @date 2014-10-23下午1:56:04
	 * @company 怡创科技
	 * @version 1.2
	 */
	public Map<String, Map<String,DBFieldToLogTitle>> getEri2GCellChannelNcellParams() {
		log.debug("进入getEri2GCellChannelNcellParams");
		Map<String, Map<String,DBFieldToLogTitle>> map=new HashMap<String, Map<String,DBFieldToLogTitle>>();
		//爱立信2G小区字段对应标题
		Map<String,DBFieldToLogTitle> eri2GCellDbFieldsToTitles  = Eri2GCellParserJobRunnable.readDbToTitleCfgAndRecordTabFromXml("eri2GCellToTitle.xml","RNO_2G_ERI_CELL");
		//爱立信2G小区额外信息字段对应标题
		Map<String,DBFieldToLogTitle> eri2GCellExtraInfoDbFieldsToTitles  = Eri2GCellParserJobRunnable.readDbToTitleCfgAndRecordTabFromXml("eri2GCellExtraInfoToTitle.xml","RNO_2G_ERI_CELL_EXTRA_INFO");
		//将两个表字段属性合并
		eri2GCellDbFieldsToTitles.putAll(eri2GCellExtraInfoDbFieldsToTitles);
		//爱立信2G小区信道组字段对应标题
		Map<String,DBFieldToLogTitle> eri2GCellChGroupDbFieldsToTitles  = Eri2GCellParserJobRunnable.readDbToTitleCfgFromXml("eri2GCellChGroupToTitle.xml");
		//爱立信2G邻区参数字段对应标题
		Map<String,DBFieldToLogTitle> eri2GNcellParaDbFieldsToTitles  = Eri2GCellParserJobRunnable.readDbToTitleCfgFromXml("eri2GNcellParamToTitle.xml");
		map.put("CELL", eri2GCellDbFieldsToTitles);
		map.put("CHANNEL", eri2GCellChGroupDbFieldsToTitles);
		map.put("NCELL", eri2GNcellParaDbFieldsToTitles);
		log.debug("退出getEri2GCellChannelNcellParams"+map);
		return map;
	}
	/**
	 * 
	 * @title 通过城市ID查询所在地的BSC集合信息
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2014-10-23下午2:12:45
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryBscListByCityId(long cityId) {
		return rno2gEriCellManageDao.queryBscByCityId(cityId);
	}
	/**
	 * 
	 * @title 查询某市最近一个月的爱立信2G小区的日期信息
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2014-10-23下午3:21:51
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryLatelyOneMonthOfEri2GCellDateInfo(final long cityId){
		return rno2gEriCellManageDao.queryLatelyOneMonthOfEri2GCellDateInfo(cityId);
	}
	/**
	 * 
	 * @title 查询符合条件的爱立信2G小区数量
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-10-24上午9:28:17
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long queryEri2GCellCnt(Eri2GCellQueryCond cond) {
		return rno2gEriCellManageDao.queryEri2GCellCnt(cond);
	}

	/**
	 * 
	 * @title 分页查询符合条件的爱立信2G小区记录
	 * @param cond
	 * @param newPage
	 * @return
	 * @author chao.xj
	 * @date 2014-10-24上午9:29:48
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryEri2GCellByPage(
			Eri2GCellQueryCond cond, Page newPage) {
		return rno2gEriCellManageDao.queryEri2GCellByPage(cond, newPage);
	}
	/**
	 * 
	 * @title 查询符合条件的爱立信2G小区信道数量
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-10-24上午9:28:17
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long queryEri2GCellChannelCnt(Eri2GCellChannelQueryCond cond) {
		return rno2gEriCellManageDao.queryEri2GCellChannelCnt(cond);
	}

	/**
	 * 
	 * @title 分页查询符合条件的爱立信2G小区信道记录
	 * @param cond
	 * @param newPage
	 * @return
	 * @author chao.xj
	 * @date 2014-10-24上午9:29:48
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryEri2GCellChannelByPage(
			Eri2GCellChannelQueryCond cond, Page newPage) {
		return rno2gEriCellManageDao.queryEri2GCellChannelByPage(cond, newPage);
	}
	/**
	 * 
	 * @title 查询符合条件的爱立信2G小区邻区数量
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-10-24上午9:28:17
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long queryEri2GNcellCnt(Eri2GNcellQueryCond cond) {
		return rno2gEriCellManageDao.queryEri2GNcellCnt(cond);
	}
	/**
	 * 
	 * @title 分页查询符合条件的爱立信2G小区邻区记录
	 * @param cond
	 * @param newPage
	 * @return
	 * @author chao.xj
	 * @date 2014-10-24上午9:29:48
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryEri2GNcellByPage(
			Eri2GNcellQueryCond cond, Page newPage) {
		return rno2gEriCellManageDao.queryEri2GNcellByPage(cond, newPage);
	}
	/**
	 * 
	 * @title 导出爱立信2G小区结果数据总入口
	 * @param path
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-11-8下午2:40:26
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String exportEri2GCellData(final String path,final Eri2GCellQueryCond cond,final long cnt) {
		
		final String token = eriCellDataExportManager.assignExportToken();
		if (token == null) {
			log.error("分配token失败！");
			return null;
		}
		
		new Thread() {
			@Override
			public void run() {
				exportEri2GCellDataInFile(token, path,cond,cnt);
			}
		}.start();
	
		return token;
	}
	/**
	 * 
	 * @title 导出爱立信2G小区结果数据写文件入口
	 * @param token
	 * @param path
	 * @param cond
	 * @author chao.xj
	 * @date 2014-11-8下午2:40:54
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void exportEri2GCellDataInFile(String token,
			String path,Eri2GCellQueryCond cond,long cnt) {

		eriCellDataExportManager.updateTokenMsg(token, "数据整理中...");
		
		/*long cnt = rno2gEriCellManageDao.queryEri2GCellCnt(cond);
		if(cnt == 0) {
			log.debug("cond="+cond+"条件下，查询不到数据，不再执行文件生成！");
			eriCellDataExportManager.updateTokenMsg(token, "该条件不存在数据");
			eriCellDataExportManager.tokenFail(token);
			return;
		}*/
		//获取当前日期，组成临时目录路径
		Calendar cal = Calendar.getInstance();
		String year = cal.get(Calendar.YEAR) + "";
		String month = (cal.get(Calendar.MONTH) + 1) + "";
		String day = (cal.get(Calendar.DAY_OF_MONTH)) + "";
		//删除临时目录里今天以前的所有数据
		//打包操作
		String realPath = path+"/eri_cell_ana_data/"+year+"/"+month+"/"+day+"/"+token;
		//保存导出任务的文件所属路径
		String filePath = realPath + "/爱立信小区数据结果.csv";
		eriCellDataExportManager.saveTokenFilePath(token,filePath);
		getEri2GCellDataByPage(cond,cnt,filePath);
		//导出完成
		eriCellDataExportManager.tokenFinished(token);
	}
	/**
	 * 
	 * @title 分页获取爱立信2G小区数据
	 * @param cond
	 * @param cnt
	 * @param saveFullPath
	 * @return
	 * @author chao.xj
	 * @date 2014-11-8下午2:42:08
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void getEri2GCellDataByPage(
			Eri2GCellQueryCond cond,long cnt,String saveFullPath) {
		log.debug("进入getEri2GCellDataByPage(Statement stmt)"
				+  cond);
		
		String sql = "";
		List<Map<String, Object>> ress = null;
		//获取组装table
//		String table = cond.buildSelectCont();
		String field_out= cond.buildFieldOutCont();
		String field_inner = cond.buildFieldInnerAndFromCont();
		String where = cond.buildWhereCont();
		String whereResult = (where == null || where.trim()
				.isEmpty()) ? ("") : (" where " + where);
		String table = "select " + field_out + ",rn from (select "
				+ field_inner
				+ whereResult + " )";
		for (int row = 0; row < cnt; row++) {
			int pageSize = 10000;
			sql = "select * from (select *  from (" + table
					+ ") p where p.rn > " + row + ") q where rownum <="
					+ pageSize;
//			ress = RnoHelper.commonQuery(stmt, sql);
			ress = rno2gEriCellManageDao.queryEriData(sql);
			// log.debug("统一表数据表本次完成写入记录数为：" + ress.size());
			// 写入文件
			writeEri2GCellDataInFile(ress,saveFullPath,cond);
			row += pageSize - 1;// 步长
		}

	}
	/**
	 * 
	 * @title 将数据写入文件
	 * @param ress
	 * @param saveFullPath
	 * @param cellQueryCond
	 * @author chao.xj
	 * @date 2014-11-8下午3:45:07
	 * @company 怡创科技
	 * @version 1.2
	 */
	private void writeEri2GCellDataInFile(
			List<Map<String, Object>> ress,String saveFullPath,Eri2GCellQueryCond cellQueryCond) {

		BufferedWriter bw = null;
		// 判断是否存在该文件
		File file = new File(saveFullPath.substring(0, saveFullPath.lastIndexOf("/")));
		boolean fileExist = file.exists();
		if(!fileExist) {
			file.mkdirs();
		}
		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(saveFullPath, true), "gbk"));
			StringBuffer buf = new StringBuffer();
			if (ress != null || ress.size() > 0) {
//				List<String> titles = Arrays.asList("MEA_TIME", "CELL");
				String title=cellQueryCond.buildFieldExportCont();
				String titles[]=title.split(",");
				if (!fileExist) {
					for (String str : titles) {
						buf.append(str).append(",");
					}
					buf.deleteCharAt(buf.length() - 1);
					bw.write(buf.toString());
					bw.newLine();
				}
				for (Map<String, Object> res : ress) {
					buf.setLength(0);
					if (res == null || res.isEmpty()) {
						continue;
					}
					for (String str : titles) {
						if (res.get(str.trim()) == null) {
							buf.append("-,");
							continue;
						}
						buf.append(res.get(str.trim()).toString().replace(",", " ")).append(",");
					}
					buf.deleteCharAt(buf.length() - 1);
					bw.write(buf.toString());
					bw.newLine();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
			}
		}
	}
	/**
	 * 
	 * @title 导出爱立信2G小区信道结果数据总入口
	 * @param path
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-11-8下午2:40:26
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String exportEri2GCellChannelData(final String path,final Eri2GCellChannelQueryCond cond,final long cnt) {
		
		final String token = eriCellDataExportManager.assignExportToken();
		if (token == null) {
			log.error("分配token失败！");
			return null;
		}
		
		new Thread() {
			@Override
			public void run() {
				exportEri2GCellChannelDataInFile(token, path,cond,cnt);
			}
		}.start();
	
		return token;
	}
	/**
	 * 
	 * @title 导出爱立信2G小区信道结果数据写文件入口
	 * @param token
	 * @param path
	 * @param cond
	 * @author chao.xj
	 * @date 2014-11-8下午2:40:54
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void exportEri2GCellChannelDataInFile(String token,
			String path,Eri2GCellChannelQueryCond cond,long cnt) {

		eriCellDataExportManager.updateTokenMsg(token, "数据整理中...");
		
		/*long cnt = rno2gEriCellManageDao.queryEri2GCellCnt(cond);
		if(cnt == 0) {
			log.debug("cond="+cond+"条件下，查询不到数据，不再执行文件生成！");
			eriCellDataExportManager.updateTokenMsg(token, "该条件不存在数据");
			eriCellDataExportManager.tokenFail(token);
			return;
		}*/
		//获取当前日期，组成临时目录路径
		Calendar cal = Calendar.getInstance();
		String year = cal.get(Calendar.YEAR) + "";
		String month = (cal.get(Calendar.MONTH) + 1) + "";
		String day = (cal.get(Calendar.DAY_OF_MONTH)) + "";
		//删除临时目录里今天以前的所有数据
		//打包操作
		String realPath = path+"/eri_cell_ana_data/"+year+"/"+month+"/"+day+"/"+token;
		//保存导出任务的文件所属路径
		String filePath = realPath + "/爱立信小区信道数据结果.csv";
		eriCellDataExportManager.saveTokenFilePath(token,filePath);
		getEri2GCellChannelDataByPage(cond,cnt,filePath);
		//导出完成
		eriCellDataExportManager.tokenFinished(token);
	}
	/**
	 * 
	 * @title 分页获取爱立信2G小区信道数据
	 * @param cond
	 * @param cnt
	 * @param saveFullPath
	 * @return
	 * @author chao.xj
	 * @date 2014-11-8下午2:42:08
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void getEri2GCellChannelDataByPage(
			Eri2GCellChannelQueryCond cond,long cnt,String saveFullPath) {
		log.debug("进入getEri2GCellChannelDataByPage(Eri2GCellChannelQueryCond cond)"
				+  cond);
		
		String sql = "";
		List<Map<String, Object>> ress = null;
		//获取组装table
//		String table = cond.buildSelectCont();
		String field_out= cond.buildFieldOutCont();
		String field_inner = cond.buildFieldInnerCont();
		String where = cond.buildWhereCont();
		String from =cond.buildFromCont();
		String whereResult = (where == null || where.trim()
				.isEmpty()) ? ("") : (" where " + where);
		String table = "select " + field_out + ",rn from (select "
				+ field_inner
				+ ",rownum rn from "+from+"  "
				+ whereResult + " )";		
		for (int row = 0; row < cnt; row++) {
			int pageSize = 10000;
			sql = "select * from (select *  from (" + table
					+ ") p where p.rn > " + row + ") q where rownum <="
					+ pageSize;
//			ress = RnoHelper.commonQuery(stmt, sql);
			ress = rno2gEriCellManageDao.queryEriData(sql);
			// log.debug("统一表数据表本次完成写入记录数为：" + ress.size());
			// 写入文件
			writeEri2GCellChannelDataInFile(ress,saveFullPath,cond);
			row += pageSize - 1;// 步长
		}

	}
	/**
	 * 
	 * @title 将爱立信小区信道数据写入文件
	 * @param ress
	 * @param saveFullPath
	 * @param cond
	 * @author chao.xj
	 * @date 2014-11-8下午3:45:07
	 * @company 怡创科技
	 * @version 1.2
	 */
	private void writeEri2GCellChannelDataInFile(
			List<Map<String, Object>> ress,String saveFullPath,Eri2GCellChannelQueryCond cond) {

		BufferedWriter bw = null;
		// 判断是否存在该文件
		File file = new File(saveFullPath.substring(0, saveFullPath.lastIndexOf("/")));
		boolean fileExist = file.exists();
		if(!fileExist) {
			file.mkdirs();
		}
		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(saveFullPath, true), "gbk"));
			StringBuffer buf = new StringBuffer();
			if (ress != null || ress.size() > 0) {
//				List<String> titles = Arrays.asList("MEA_TIME", "CELL");
				String title=cond.buildFieldExportCont();
				String titles[]=title.split(",");
				if (!fileExist) {
					for (String str : titles) {
						buf.append(str).append(",");
					}
					buf.deleteCharAt(buf.length() - 1);
					bw.write(buf.toString());
					bw.newLine();
				}
				for (Map<String, Object> res : ress) {
					buf.setLength(0);
					if (res == null || res.isEmpty()) {
						continue;
					}
					for (String str : titles) {
						if (res.get(str.trim()) == null) {
							buf.append("-,");
							continue;
						}
						buf.append(res.get(str.trim()).toString().replace(",", " ")).append(",");
					}
					buf.deleteCharAt(buf.length() - 1);
					bw.write(buf.toString());
					bw.newLine();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
			}
		}
	}
	/**
	 * 
	 * @title 导出爱立信2G小区邻区结果数据总入口
	 * @param path
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-11-8下午2:40:26
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String exportEri2GCellNcellData(final String path,final Eri2GNcellQueryCond cond,final long cnt) {
		
		final String token = eriCellDataExportManager.assignExportToken();
		if (token == null) {
			log.error("分配token失败！");
			return null;
		}
		
		new Thread() {
			@Override
			public void run() {
				exportEri2GCellNcellDataInFile(token, path,cond,cnt);
			}
		}.start();
	
		return token;
	}
	/**
	 * 
	 * @title 导出爱立信2G小区邻区结果数据写文件入口
	 * @param token
	 * @param path
	 * @param cond
	 * @author chao.xj
	 * @date 2014-11-8下午2:40:54
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void exportEri2GCellNcellDataInFile(String token,
			String path,Eri2GNcellQueryCond cond,long cnt) {

		eriCellDataExportManager.updateTokenMsg(token, "数据整理中...");
		
		/*long cnt = rno2gEriCellManageDao.queryEri2GCellCnt(cond);
		if(cnt == 0) {
			log.debug("cond="+cond+"条件下，查询不到数据，不再执行文件生成！");
			eriCellDataExportManager.updateTokenMsg(token, "该条件不存在数据");
			eriCellDataExportManager.tokenFail(token);
			return;
		}*/
		//获取当前日期，组成临时目录路径
		Calendar cal = Calendar.getInstance();
		String year = cal.get(Calendar.YEAR) + "";
		String month = (cal.get(Calendar.MONTH) + 1) + "";
		String day = (cal.get(Calendar.DAY_OF_MONTH)) + "";
		//删除临时目录里今天以前的所有数据
		//打包操作
		String realPath = path+"/eri_cell_ana_data/"+year+"/"+month+"/"+day+"/"+token;
		//保存导出任务的文件所属路径
		String filePath = realPath + "/爱立信小区邻区数据结果.csv";
		eriCellDataExportManager.saveTokenFilePath(token,filePath);
		getEri2GCellNcellDataByPage(cond,cnt,filePath);
		//导出完成
		eriCellDataExportManager.tokenFinished(token);
	}
	/**
	 * 
	 * @title 分页获取爱立信2G小区邻区数据
	 * @param cond
	 * @param cnt
	 * @param saveFullPath
	 * @return
	 * @author chao.xj
	 * @date 2014-11-8下午2:42:08
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void getEri2GCellNcellDataByPage(
			Eri2GNcellQueryCond cond,long cnt,String saveFullPath) {
		log.debug("进入getEri2GCellNcellDataByPage(Eri2GCellChannelQueryCond cond)"
				+  cond);
		
		String sql = "";
		List<Map<String, Object>> ress = null;
		//获取组装table
//		String table = cond.buildSelectCont();
		String field_out= cond.buildFieldOutCont();
		String field_inner = cond.buildFieldInnerCont();
		String where = cond.buildWhereCont();
		String from =cond.buildFromCont();
		String whereResult = (where == null || where.trim()
				.isEmpty()) ? ("") : (" where " + where);
		String table = "select " + field_out + ",rn from (select "
				+ field_inner
				+ ",rownum rn from "+from+"  "
				+ whereResult + " )";
		for (int row = 0; row < cnt; row++) {
			int pageSize = 10000;
			sql = "select * from (select *  from (" + table
					+ ") p where p.rn > " + row + ") q where rownum <="
					+ pageSize;
//			ress = RnoHelper.commonQuery(stmt, sql);
			ress = rno2gEriCellManageDao.queryEriData(sql);
			// log.debug("统一表数据表本次完成写入记录数为：" + ress.size());
			// 写入文件
			writeEri2GCellNcellDataInFile(ress,saveFullPath,cond);
			row += pageSize - 1;// 步长
		}

	}
	/**
	 * 
	 * @title 将爱立信小区邻区数据写入文件
	 * @param ress
	 * @param saveFullPath
	 * @param cond
	 * @author chao.xj
	 * @date 2014-11-8下午3:45:07
	 * @company 怡创科技
	 * @version 1.2
	 */
	private void writeEri2GCellNcellDataInFile(
			List<Map<String, Object>> ress,String saveFullPath,Eri2GNcellQueryCond cond) {

		BufferedWriter bw = null;
		// 判断是否存在该文件
		File file = new File(saveFullPath.substring(0, saveFullPath.lastIndexOf("/")));
		boolean fileExist = file.exists();
		if(!fileExist) {
			file.mkdirs();
		}
		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(saveFullPath, true), "gbk"));
			StringBuffer buf = new StringBuffer();
			if (ress != null || ress.size() > 0) {
//				List<String> titles = Arrays.asList("MEA_TIME", "CELL");
				String title=cond.buildFieldExportCont();
				String titles[]=title.split(",");
				if (!fileExist) {
					for (String str : titles) {
						buf.append(str).append(",");
					}
					buf.deleteCharAt(buf.length() - 1);
					bw.write(buf.toString());
					bw.newLine();
				}
				for (Map<String, Object> res : ress) {
					buf.setLength(0);
					if (res == null || res.isEmpty()) {
						continue;
					}
					for (String str : titles) {
						if (res.get(str.trim()) == null) {
							buf.append("-,");
							continue;
						}
						buf.append(res.get(str.trim()).toString().replace(",", " ")).append(",");
					}
					buf.deleteCharAt(buf.length() - 1);
					bw.write(buf.toString());
					bw.newLine();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
			}
		}
	}
	/**
	 * 
	 * @title 查询爱立信小区结果文件进度
	 * @param token
	 * @return
	 * @author chao.xj
	 * @date 2014-11-8下午3:01:40
	 * @company 怡创科技
	 * @version 1.2
	 */
	public Map<String, Object> queryExportProgress(String token) {
		ExportToken tobj = eriCellDataExportManager.getExportToken(token);
		boolean fail = tobj.isFail();
		boolean finished = tobj.isFinished();
		String msg = tobj.getMsg();
		//任务失败，清除任务信息
		if(fail) {
			boolean flag = eriCellDataExportManager.destroyToken(token);
			log.debug("导出任务失败，删除任务信息="+flag);
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("fail", fail);
		result.put("finished", finished);
		result.put("msg", msg);
		return result;
	}
	
	/**
	 * 
	 * @title 获取爱立信小区导出任务的文件路径
	 * @param token
	 * @return
	 * @author chao.xj
	 * @date 2014-11-8下午3:02:22
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String queryExportTokenFilePath(String token) {
		ExportToken tobj = eriCellDataExportManager.getExportToken(token);
		String filePath = tobj.getFilePath();
		if(!("").equals(filePath)) {
			return filePath;
		} else {
			return null;
		}
	}
	/**
	 * 
	 * @title 确认符合条件的爱立信2G小区的数量
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-11-7上午10:01:01
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long confirmEri2GCellCnt(final Eri2GCellQueryCond cond){
		return rno2gEriCellManageDao.confirmEri2GCellCnt(cond);
	}
	/**
	 * 
	 * @title 删除2G爱立信小区重复数据根据日期及市ID
	 * @param stmt
	 * @param meaDate
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2014-11-7上午10:20:59
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean rmvEri2GCellRepeatingData(Statement stmt, String meaDate,
			long cityId) {
		String sql = "";
		int cnt[];
		String str[] = {
				"delete from rno_2g_eri_cell_desc where mea_date = to_date('"
						+ meaDate + "', 'yyyy-MM-dd') and city_id=" + cityId
						+ "",
				"delete from rno_2g_eri_cell where  mea_date = to_date('"
						+ meaDate + "', 'yyyy-MM-dd') and city_id=" + cityId
						+ "",
				"delete from rno_2g_eri_cell_extra_info where mea_date = to_date('"
						+ meaDate + "', 'yyyy-MM-dd') and city_id=" + cityId
						+ "",
				"delete from rno_2g_eri_cell_ch_group where  mea_date = to_date('"
						+ meaDate + "', 'yyyy-MM-dd') and city_id=" + cityId
						+ "",
				"delete from rno_2g_eri_ncell_param where mea_date = to_date('"
						+ meaDate + "', 'yyyy-MM-dd') and city_id=" + cityId
						+ "" };
		boolean flag = false;
		try {
			for (int i = 0; i < str.length; i++) {
				sql=str[i];
				stmt.addBatch(sql);
			}
			cnt=stmt.executeBatch();
		    flag=true;
			log.debug(cnt+"sql:" + sql);
		} catch (SQLException e) {
			log.debug("执行出错的sql:" + sql);
			e.printStackTrace();
		} finally {
		}
		return flag;
	}
	/**
	 * 
	 * @title 查询某市最近若干个月的爱立信2G小区的日期信息
	 * @param cityId
	 * @param monthNum 为负整数
	 * @return
	 * @author chao.xj
	 * @date 2014-11-12下午3:36:19
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryLatelySeveralMonthOfEri2GCellDateInfo(final long cityId,final int monthNum){
		return rno2gEriCellManageDao.queryLatelySeveralMonthOfEri2GCellDateInfo(cityId,monthNum);
	}
	public static void main(String[] args) {
		SysArea area=AuthDsDataDaoImpl.getSysAreaByAreaId(88);
		String areaStr=area.getName();
		
		System.out.println(areaStr.contains("广州"));
		System.out.println(areaStr.contains("深圳"));
		System.out.println(areaStr.contains("广"));
		System.out.println(areaStr.contains("广市"));
	}
	
}
