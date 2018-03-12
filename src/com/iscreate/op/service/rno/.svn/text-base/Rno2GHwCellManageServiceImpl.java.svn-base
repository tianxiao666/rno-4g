package com.iscreate.op.service.rno;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.Eri2GCellDescQueryCond;
import com.iscreate.op.action.rno.model.Eri2GNcellQueryCond;
import com.iscreate.op.action.rno.model.Hw2GCellDescQueryCond;
import com.iscreate.op.action.rno.model.Hw2GCellQueryCond;
import com.iscreate.op.action.rno.model.Hw2GNcellQueryCond;
import com.iscreate.op.dao.rno.AuthDsDataDaoImpl;
import com.iscreate.op.dao.rno.Rno2GHwCellManageDao;
import com.iscreate.op.service.rno.parser.jobrunnable.Eri2GCellParserJobRunnable;
import com.iscreate.op.service.rno.parser.jobrunnable.Eri2GCellParserJobRunnable.DBFieldToLogTitle;
import com.iscreate.op.service.rno.parser.jobrunnable.Hw2GCellParserJobRunnable;
import com.iscreate.op.service.rno.parser.jobrunnable.HwNcsParserJobRunnable.DBFieldToTitle;
import com.iscreate.op.service.rno.task.EriCellDataExportManager;
import com.iscreate.op.service.rno.task.EriCellDataExportManagerImpl.ExportToken;
import com.iscreate.op.service.rno.tool.RnoHelper;

public class Rno2GHwCellManageServiceImpl implements Rno2GHwCellManageService {

	private static Log log = LogFactory
			.getLog(Rno2GHwCellManageServiceImpl.class);
	private Rno2GHwCellManageDao rno2gHwCellManageDao;
	private EriCellDataExportManager eriCellDataExportManager;
	
	public EriCellDataExportManager getEriCellDataExportManager() {
		return eriCellDataExportManager;
	}
	public void setEriCellDataExportManager(
			EriCellDataExportManager eriCellDataExportManager) {
		this.eriCellDataExportManager = eriCellDataExportManager;
	}
	public Rno2GHwCellManageDao getRno2gHwCellManageDao() {
		return rno2gHwCellManageDao;
	}
	public void setRno2gHwCellManageDao(Rno2GHwCellManageDao rno2gHwCellManageDao) {
		this.rno2gHwCellManageDao = rno2gHwCellManageDao;
	}
	/**
	 * 
	 * @title 通过区域查询BSC/非hibernate
	 * @param stmt
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午10:14:18
	 * @company 怡创科技
	 * @version 1.2
	 */
	public Map<String, Integer> queryBscByCityId(Statement stmt,long cityId) {
		String areaStr=AuthDsDataDaoImpl.getSubAreaAndSelfIdListStrByParentId(cityId);
		String sql = "select bsc_id,engname from rno_bsc where bsc_id in(select bsc_id from rno_bsc_rela_area where area_id in("+areaStr+"))";
		List<Map<String, Object>> bscs=RnoHelper.commonQuery(stmt, sql);
		// TODO Auto-generated method stub
		log.debug("进入queryBscByCityId(Statement stmt,long cityId)"+stmt+"--"+cityId);
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
	 * @title 查询符合条件的华为2G小区描述记录数量
	 * @param hw2gCellDescQueryCond
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午10:18:11
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long queryHwCellDescCnt(Hw2GCellDescQueryCond hw2gCellDescQueryCond) {
		return rno2gHwCellManageDao.queryHwCellDescCnt(hw2gCellDescQueryCond);
	}

	/**
	 * 
	 * @title 分页查询符合条件的华为2G小区描述记录
	 * @param hw2GCellDescQueryCond
	 * @param newPage
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午10:19:04
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryHwCellDescByPage(
			Hw2GCellDescQueryCond hw2GCellDescQueryCond, Page newPage) {
		return rno2gHwCellManageDao.queryHwCellDescByPage(
				hw2GCellDescQueryCond, newPage);
	}
	/**
	 * 
	 * @title 获取华为2G小区、邻区字段参数集合
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午11:05:01
	 * @company 怡创科技
	 * @version 1.2
	 */
	public Map<String, Map<String,DBFieldToTitle>> getHw2GCellAndNcellParams() {
		log.debug("进入getHw2GCellAndNcellParams");
		Map<String, Map<String,DBFieldToTitle>> map=new HashMap<String, Map<String,DBFieldToTitle>>();
		//华为2G小区字段对应标题
		Map<String,DBFieldToTitle> hw2GCellDbFieldsToTitles  = Hw2GCellParserJobRunnable.readDbToTitleCfgFromXml("hw2GCellToTitle.xml");
		//华为2G邻区参数字段对应标题
		Map<String,DBFieldToTitle> hw2GNcellParaDbFieldsToTitles  = Hw2GCellParserJobRunnable.readDbToTitleCfgFromXml("hw2GNcellParamToTitle.xml");
		map.put("CELL", hw2GCellDbFieldsToTitles);
		map.put("NCELL", hw2GNcellParaDbFieldsToTitles);
		log.debug("退出getHw2GCellAndNcellParams"+map);
		return map;
	}
	/**
	 * 
	 * @title  通过城市ID查询所在地的BSC集合信息
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午11:25:07
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryBscListByCityId(long cityId) {
		return rno2gHwCellManageDao.queryBscByCityId(cityId);
	}
	/**
	 * 
	 * @title 查询某市最近一个月的华为2G小区的日期信息
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午11:27:39
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryLatelyOneMonthOfHw2GCellDateInfo(final long cityId){
		return rno2gHwCellManageDao.queryLatelyOneMonthOfHw2GCellDateInfo(cityId);
	}
	/**
	 * 
	 * @title 查询符合条件的华为2G小区的数量
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午11:36:54
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long queryHw2GCellCnt(final Hw2GCellQueryCond cond){
		return rno2gHwCellManageDao.queryHw2GCellCnt(cond);
	}
	/**
	 * 
	 * @title 分页查询符合条件的华为2G 小区的记录
	 * @param cond
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午11:38:33
	 * @company 怡创科技
	 * @version 1.2
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryHw2GCellByPage(
			final Hw2GCellQueryCond cond,final Page page){
		return rno2gHwCellManageDao.queryHw2GCellByPage(cond, page);
	}
	/**
	 * 
	 * @title 查询符合条件的华为2G小区邻区的数量
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午11:46:11
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long queryHw2GNcellCnt(final Hw2GNcellQueryCond cond){
		return rno2gHwCellManageDao.queryHw2GNcellCnt(cond);
	}
	/**
	 * 
	 * @title 分页查询符合条件的华为2G 小区邻区的记录
	 * @param cond
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午11:47:21
	 * @company 怡创科技
	 * @version 1.2
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryHw2GNcellByPage(
			final Hw2GNcellQueryCond cond,final Page page){
		return rno2gHwCellManageDao.queryHw2GNcellByPage(cond, page);
	}
	/**
	 * 
	 * @title 导出华为2G小区结果数据总入口
	 * @param path
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-11-8下午2:40:26
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String exportHw2GCellData(final String path,final Hw2GCellQueryCond cond,final long cnt) {
		
		final String token = eriCellDataExportManager.assignExportToken();
		if (token == null) {
			log.error("分配token失败！");
			return null;
		}
		
		new Thread() {
			@Override
			public void run() {
				exportHw2GCellDataInFile(token, path,cond,cnt);
			}
		}.start();
	
		return token;
	}
	/**
	 * 
	 * @title 导出华为2G小区结果数据写文件入口
	 * @param token
	 * @param path
	 * @param cond
	 * @author chao.xj
	 * @date 2014-11-8下午2:40:54
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void exportHw2GCellDataInFile(String token,
			String path,Hw2GCellQueryCond cond,long cnt) {

		eriCellDataExportManager.updateTokenMsg(token, "数据查询中...");
		
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
		String filePath = realPath + "/华为小区数据结果.csv";
		eriCellDataExportManager.saveTokenFilePath(token,filePath);
		getHw2GCellDataByPage(cond,cnt,filePath);
		//导出完成
		eriCellDataExportManager.tokenFinished(token);
	}
	/**
	 * 
	 * @title 分页获取华为2G小区数据
	 * @param cond
	 * @param cnt
	 * @param saveFullPath
	 * @return
	 * @author chao.xj
	 * @date 2014-11-8下午2:42:08
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void getHw2GCellDataByPage(
			Hw2GCellQueryCond cond,long cnt,String saveFullPath) {
		log.debug("进入getHw2GCellDataByPage(Hw2GCellQueryCond cond)"
				+  cond);
		
		String sql = "";
		List<Map<String, Object>> ress = null;
		//获取组装table
//		String table = cond.buildSelectCont();
		String field_out= cond.buildFieldOutCont();
		String field_inner = cond.buildFieldInnerCont();
		String where = cond.buildWhereCont();
		String from =cond.buildFromCont();
		log.debug("queryHw2GCellByPage ,where=" + where);
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
			ress = rno2gHwCellManageDao.queryHwData(sql);
			// log.debug("统一表数据表本次完成写入记录数为：" + ress.size());
			// 写入文件
			writeHw2GCellDataInFile(ress,saveFullPath,cond);
			row += pageSize - 1;// 步长
		}

	}
	/**
	 * 
	 * @title 将华为小区数据写入文件
	 * @param ress
	 * @param saveFullPath
	 * @param cond
	 * @author chao.xj
	 * @date 2014-11-8下午3:45:07
	 * @company 怡创科技
	 * @version 1.2
	 */
	private void writeHw2GCellDataInFile(
			List<Map<String, Object>> ress,String saveFullPath,Hw2GCellQueryCond cond) {

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
	 * @title 导出华为2G小区邻区结果数据总入口
	 * @param path
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-11-8下午2:40:26
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String exportHw2GCellNcellData(final String path,final Hw2GNcellQueryCond cond,final long cnt) {
		
		final String token = eriCellDataExportManager.assignExportToken();
		if (token == null) {
			log.error("分配token失败！");
			return null;
		}
		
		new Thread() {
			@Override
			public void run() {
				exportHw2GCellNcellDataInFile(token, path,cond,cnt);
			}
		}.start();
	
		return token;
	}
	/**
	 * 
	 * @title 导出华为2G小区邻区结果数据写文件入口
	 * @param token
	 * @param path
	 * @param cond
	 * @author chao.xj
	 * @date 2014-11-8下午2:40:54
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void exportHw2GCellNcellDataInFile(String token,
			String path,Hw2GNcellQueryCond cond,long cnt) {

		eriCellDataExportManager.updateTokenMsg(token, "数据查询中...");
		
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
		String filePath = realPath + "/华为小区邻区数据结果.csv";
		eriCellDataExportManager.saveTokenFilePath(token,filePath);
		getHw2GCellNcellDataByPage(cond,cnt,filePath);
		//导出完成
		eriCellDataExportManager.tokenFinished(token);
	}
	/**
	 * 
	 * @title 分页获取华为2G小区邻区数据
	 * @param cond
	 * @param cnt
	 * @param saveFullPath
	 * @return
	 * @author chao.xj
	 * @date 2014-11-8下午2:42:08
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void getHw2GCellNcellDataByPage(
			Hw2GNcellQueryCond cond,long cnt,String saveFullPath) {
		log.debug("进入getHw2GCellNcellDataByPage(Hw2GNcellQueryCond cond)"
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
			ress = rno2gHwCellManageDao.queryHwData(sql);
			// log.debug("统一表数据表本次完成写入记录数为：" + ress.size());
			// 写入文件
			writeHw2GCellNcellDataInFile(ress,saveFullPath,cond);
			row += pageSize - 1;// 步长
		}

	}
	/**
	 * 
	 * @title 将华为小区邻区数据写入文件
	 * @param ress
	 * @param saveFullPath
	 * @param cond
	 * @author chao.xj
	 * @date 2014-11-8下午3:45:07
	 * @company 怡创科技
	 * @version 1.2
	 */
	private void writeHw2GCellNcellDataInFile(
			List<Map<String, Object>> ress,String saveFullPath,Hw2GNcellQueryCond cond) {

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
	 * @title 查询华为小区结果文件进度
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
	 * @title 获取华为小区导出任务的文件路径
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
	 * @title 查询某市最近若干个月的华为2G小区的日期信息
	 * @param cityId
	 * @param monthNum 为负整数
	 * @return
	 * @author chao.xj
	 * @date 2014-11-12下午3:36:19
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryLatelySeveralMonthOfHw2GCellDateInfo(final long cityId,final int monthNum){
		return rno2gHwCellManageDao.queryLatelySeveralMonthOfHw2GCellDateInfo(cityId, monthNum);
	}
}
