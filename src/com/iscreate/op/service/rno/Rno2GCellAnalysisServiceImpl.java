package com.iscreate.op.service.rno;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.iscreate.op.dao.rno.Rno2GCellAnalysisDao;
import com.iscreate.op.pojo.rno.CobsicCells;
import com.iscreate.op.pojo.rno.CobsicCellsExpand;
import com.iscreate.op.service.rno.parser.FileParserManager.ParserToken;
import com.iscreate.op.service.rno.task.EriCellDataExportManager;
import com.iscreate.op.service.rno.task.EriCellDataExportManagerImpl.ExportToken;
import com.iscreate.op.service.rno.tool.FileTool;
import com.iscreate.op.service.rno.tool.ZipFileHandler;
import com.iscreate.plat.tools.LatLngHelper;
import com.sun.org.apache.bcel.internal.generic.LNEG;

public class Rno2GCellAnalysisServiceImpl implements Rno2GCellAnalysisService {
	private static Log log = LogFactory.getLog(Rno2GCellAnalysisServiceImpl.class);

	// -------注入-------------//
	private Rno2GCellAnalysisDao rno2GCellAnalysisDao;
	private EriCellDataExportManager eriCellDataExportManager;
	
	public Rno2GCellAnalysisDao getRno2GCellAnalysisDao() {
		return rno2GCellAnalysisDao;
	}
	public void setRno2GCellAnalysisDao(Rno2GCellAnalysisDao rno2GCellAnalysisDao) {
		this.rno2GCellAnalysisDao = rno2GCellAnalysisDao;
	}

	public EriCellDataExportManager getEriCellDataExportManager() {
		return eriCellDataExportManager;
	}
	public void setEriCellDataExportManager(
			EriCellDataExportManager eriCellDataExportManager) {
		this.eriCellDataExportManager = eriCellDataExportManager;
	}
	
	/**
	 * 判断某个日期是否存在爱立信小区数据
	 * @param cityId
	 * @param bscStr
	 * @param paramType
	 * @param date
	 * @return
	 * @author peng.jm
	 * @date 2014-10-22下午04:39:12
	 */
	public boolean isEriCellDataExistedOnTheDate(long cityId, String paramType,
			String bscStr, String date) {
		return rno2GCellAnalysisDao
			.isEriCellDataExistedOnTheDate(cityId,paramType,bscStr,date);
	}
	/**
	 * 通过BSC名称获取BSC信息
	 * @param bscStr2
	 * @return
	 * @author peng.jm
	 * @date 2014-11-11上午11:22:12
	 */
	public List<Map<String, Object>> getBscDetailByBscs(String bscStr) {
		return rno2GCellAnalysisDao.getBscDetailByBscs(bscStr);
	}
	/**
	 * 获取爱立信小区某两天的参数对比数据
	 * @param cityId
	 * @param paramType
	 * @param paramStr
	 * @param bscStr
	 * @param date1
	 * @param date2
	 * @return
	 * @author peng.jm
	 * @date 2014-10-22下午04:03:12
	 */
	public List<Map<String,Object>> eriCellParamsCompare(long cityId,
			String paramType, String paramStr, String bscStr, String date1, String date2) {
		
		//获取bsc列
		List<Map<String, Object>> bscList = rno2GCellAnalysisDao.getBscByBscIdStr(bscStr);
		if(bscList.size() == 0) {
			log.debug("bscIdStr="+bscStr+"条件下，查询不到BSC！");
		}
		
		List<Map<String, Object>> res = rno2GCellAnalysisDao
				.getEriCellParamsCompareResByTypeAndDate(cityId, paramType,
						paramStr, bscStr, date1, date2);

		//结果加入BSC名称
		String bscIdFromBsc = "";
		String bscIdFromRes = "";
		String engName = "";
		for (Map<String, Object> map : res) {
			if(map.get("BSC") != null) {
				bscIdFromRes = map.get("BSC").toString();
			}
			for (Map<String, Object> bsc : bscList) {
				if(bsc.get("BSC_ID") != null) {
					bscIdFromBsc = bsc.get("BSC_ID").toString();
				}
				if(bscIdFromRes.equals(bscIdFromBsc)) {
					if(bsc.get("ENGNAME") != null) {
						engName = bsc.get("ENGNAME").toString();
					}
				}
			}
			map.put("BSC_ENGNAME", engName);
		}
		//按照BSC名称排序
		List<String> bscNameList = new ArrayList<String>();
		for (Map<String, Object> map : res) {
			bscNameList.add(map.get("BSC_ENGNAME").toString());
		}
		Collections.sort(bscNameList);
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		for (String bscName : bscNameList) {
			for (Map<String, Object> map : res) {
				if(bscName.equals(map.get("BSC_ENGNAME").toString())) {
					result.add(map);
					break;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 导出爱立信小区参数对比结果数据
	 * @param cityId
	 * @param paramType
	 * @param paramStr
	 * @param bscStr
	 * @param date1
	 * @param date2
	 * @param path
	 * @return
	 * @author peng.jm
	 * @date 2014-11-5下午06:14:24
	 */
	public String exportEriCellCompareData(final long cityId,
			final String paramType, final String paramStr, final String bscStr,
			final String date1, final String date2, final String path) {
		
		final String token = eriCellDataExportManager.assignExportToken();
		if (token == null) {
			log.error("分配token失败！");
			return null;
		}
		
		new Thread() {
			@Override
			public void run() {
				exportEriCellCompareDataInFile(token, cityId, paramType, paramStr,
						bscStr, date1, date2, path);
			}
		}.start();
	
		return token;
	}
	
	/**
	 * 查询导出结果文件进度
	 * @param token
	 * @return
	 * @author peng.jm
	 * @date 2014-11-6下午03:41:26
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
	 * 获取导出任务的文件路径
	 * @param token
	 * @return
	 * @author peng.jm
	 * @date 2014-11-6下午05:57:55
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
	 * 获取爱立信小区参数对比结果数据，生成文件
	 * @param token
	 * @param cityId
	 * @param paramType
	 * @param paramStr
	 * @param bscStr
	 * @param date1
	 * @param date2
	 * @param path
	 * @author peng.jm
	 * @date 2014-11-6上午11:41:36
	 */
	public void exportEriCellCompareDataInFile(String token,
			long cityId, String paramType, String paramStr,
			String bscStr, String date1, String date2, String path) {
		
		//获取bsc列
		List<Map<String, Object>> bscList = rno2GCellAnalysisDao.getBscByBscIdStr(bscStr);
		if(bscList.size() == 0) {
			log.debug("bscIdStr="+bscStr+"条件下，查询不到BSC！");
		}
		
		eriCellDataExportManager.updateTokenMsg(token, "数据整理中...");
		
		//获取参数对比结果集详情
		List<Map<String, Object>> res = rno2GCellAnalysisDao
				.exportEriCellCompareData(cityId, paramType, paramStr, bscStr,
						date1, date2);
		if(res.size() == 0) {
			log.info("两个日期之间不存在差异参数，不再执行文件生成！");
			eriCellDataExportManager.updateTokenMsg(token, "两个日期之间不存在差异参数");
			eriCellDataExportManager.tokenFail(token);
			return;
		}
		
		List<Map<String,Object>> resData = new ArrayList<Map<String,Object>>();
		String params[] = paramStr.split(",");
		String p1 = "";
		String p2 = "";
		Map<String,Object> map = null;
		for (Map<String, Object> one : res) {
			if (one.get("BSC") == null || one.get("CELL") == null) {
				continue;
			}
			for (String param : params) {
				if(one.get(param+"_D1") == null || one.get(param+"_D2") == null) {
					continue;
				}
				p1 = one.get(param+"_D1").toString();
				p2 = one.get(param+"_D2").toString();
				if(!(p1).equals(p2)) {
					map = new HashMap<String, Object>();
					map.put("BSC", one.get("BSC").toString());
					map.put("CELL", one.get("CELL").toString());
					if(("channel").equals(paramType)) {
						map.put("CHGR", one.get("CH_GROUP").toString());
					}
					else if(("neighbour").equals(paramType)) {
						map.put("N_CELL", one.get("N_CELL").toString());
					}
					map.put("PNAME", param);
					map.put("PARAM1", p1);
					map.put("PARAM2", p2);
					resData.add(map);
				}
			}
		}
		
		//获取当前日期，组成临时目录路径
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR) ;
		int month = (cal.get(Calendar.MONTH) + 1);
		int day = (cal.get(Calendar.DAY_OF_MONTH));

		String realPath = path+"/eri_cell_ana_data/"+year+"/"+month+"/"+day+"/"+token;
	
		//将数据写进csv文件
		File file = new File(realPath);
		if(!file.exists()) {
			file.mkdirs();
		}
		//保存导出任务的文件所属路径
		String filePath = realPath + "/参数对比检查结果.csv";
		eriCellDataExportManager.saveTokenFilePath(token,filePath);
		
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filePath, true), "gbk"));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("创建爱立信小区参数对比结果文件流失败！");
			eriCellDataExportManager.updateTokenMsg(token, "文件导出失败");
			eriCellDataExportManager.tokenFail(token);
			return;
		}
		if(("cell").equals(paramType)) {
			try {
				//标题
				bw.write("BSC,CELL,参数,"+date1+","+date2);
				bw.newLine();
				for (Map<String, Object> one : resData) {
					bw.write("\""+one.get("BSC").toString()
								+"\",\""+one.get("CELL").toString()
								+"\",\""+one.get("PNAME").toString()
								+"\",\""+one.get("PARAM1").toString()
								+"\",\""+one.get("PARAM2").toString()+"\"");
					bw.newLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
				log.error("爱立信小区参数对比结果文件的数据写入失败！");
				eriCellDataExportManager.updateTokenMsg(token, "文件导出失败");
				eriCellDataExportManager.tokenFail(token);
				try {
					bw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return;
			}
		}
		else if(("channel").equals(paramType)) {
			try {
				//标题
				bw.write("BSC,CELL,CHGR,参数,"+date1+","+date2);
				bw.newLine();
				for (Map<String, Object> one : resData) {
					bw.write("\""+one.get("BSC").toString()
								+"\",\""+one.get("CELL").toString()
								+"\",\""+one.get("CHGR").toString()
								+"\",\""+one.get("PNAME").toString()
								+"\",\""+one.get("PARAM1").toString()
								+"\",\""+one.get("PARAM2").toString()+"\"");
					bw.newLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
				log.error("爱立信小区参数对比结果文件的数据写入失败！");
				eriCellDataExportManager.updateTokenMsg(token, "文件导出失败");
				eriCellDataExportManager.tokenFail(token);
				try {
					bw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return;
			}			
		}
		else if(("neighbour").equals(paramType)) {
			try {
				//标题
				bw.write("BSC,CELL,NCELL,参数,"+date1+","+date2);
				bw.newLine();
				for (Map<String, Object> one : resData) {
					bw.write("\""+one.get("BSC").toString()
								+"\",\""+one.get("CELL").toString()
								+"\",\""+one.get("N_CELL").toString()
								+"\",\""+one.get("PNAME").toString()
								+"\",\""+one.get("PARAM1").toString()
								+"\",\""+one.get("PARAM2").toString()+"\"");
					bw.newLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
				log.error("爱立信小区参数对比结果文件的数据写入失败！");
				eriCellDataExportManager.updateTokenMsg(token, "文件导出失败");
				eriCellDataExportManager.tokenFail(token);
				try {
					bw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return;
			}			
		}
		
		//导出完成
		eriCellDataExportManager.tokenFinished(token);
		
		try {
			bw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 导出爱立信小区参数一致性检查的结果 建立导出任务
	 * @param items
	 * @param bscIdStr
	 * @param date1
	 * @param cityId
	 * @param settings
	 * @param path
	 * @return
	 * @author peng.jm
	 * @date 2014-11-10下午06:05:29
	 */
	public String exportEriCellCheckData(final String items,final String bscIdStr,final String date1,
			final long cityId, final Map<String, String> settings, final String path) {
		
		final String token = eriCellDataExportManager.assignExportToken();
		if (token == null) {
			log.error("分配token失败！");
			return null;
		}
		
		new Thread() {
			@Override
			public void run() {
				exportEriCellCheckDataInFile(token, items, bscIdStr, date1,
						cityId, settings, path);
			}
		}.start();
	
		return token;
	}
	
	/**
	 * 导出爱立信小区参数一致性检查的结果，生成文件
	 * @param token
	 * @param items
	 * @param bscStr
	 * @param date1
	 * @param cityId
	 * @param settings
	 * @param path
	 * @author peng.jm
	 * @date 2014-11-10下午06:19:09
	 */
	private void exportEriCellCheckDataInFile(String token,
			String items, String bscIdStr, String date, long cityId,
			Map<String, String> settings, String path) {
		
		eriCellDataExportManager.updateTokenMsg(token, "数据整理中...");

		//检查项
		String checkTypes[] = items.split(","); 
		
		/**** 导出所有检查项的一致性结果数据 ****/
		//获取当前日期，组成临时目录路径
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR) ;
		int month = (cal.get(Calendar.MONTH) + 1);
		int day = (cal.get(Calendar.DAY_OF_MONTH));
		
		String realPath = path+"/eri_cell_ana_data/"+year+"/"+month+"/"+day+"/"+token;
		File file = new File(realPath);
		if(!file.exists()) {
			file.mkdirs();
		}
		
		String filePath = realPath + "/一致性检查结果_"+date+".xlsx";
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			eriCellDataExportManager.updateTokenMsg(token, "爱立信小区参数一致性检查文件导出失败");
			eriCellDataExportManager.tokenFail(token);
			return;
		}
		
		//工作薄
		Workbook workbook = new SXSSFWorkbook(100);
		Sheet sheet;
		Row row;
		Cell cell;
		//创建总指令sheet,先创建标题
		sheet = workbook.createSheet("总指令");
		
		//指令收集器，将每项检查项的指令保存在集合中
		List<Map<String,Object>> cmdResult = new ArrayList<Map<String,Object>>();
		boolean flag = false;
		
		for (String type : checkTypes) {
			//功率检查
			if (("powerCheck").equals(type)) {
				flag = exportEriCellPowerCheckResult(token, workbook,
						cmdResult, bscIdStr, date, cityId);
				if(!flag) {
					eriCellDataExportManager.updateTokenMsg(token, "功率检查数据导出失败...");
				}
			} 
			//跳频检查
			else if (("freqHopCheck").equals(type)) {
				flag = exportEriCellFreqHopCheckResult(token, workbook,
						cmdResult, bscIdStr, date, cityId, settings);
				if(!flag) {
					eriCellDataExportManager.updateTokenMsg(token, "跳频数据导出失败...");
				}
			}
			//NCCPERM检查
			else if (("nccperm").equals(type)) {
				flag = exportEriCellNccpermResult(token, workbook,
						cmdResult, bscIdStr, date, cityId, settings);
				if(!flag) {
					eriCellDataExportManager.updateTokenMsg(token, "NCCPERM检查导出失败...");
				}
			}
			//测量频点多定义
			else if (("meaFreqMultidefined").equals(type)) {
				flag = exportEriCellMeaFreqMultidefineResult(token, workbook,
						cmdResult, bscIdStr, date, cityId, settings);
				if(!flag) {
					eriCellDataExportManager.updateTokenMsg(token, "测量频点多定义导出失败...");
				}
			}
			//测量频点漏定义
			else if (("meaFreqMomit").equals(type)) {
				flag = exportEriCellMeaFreqMomitResult(token, workbook,
						cmdResult, bscIdStr, date, cityId, settings);
				if(!flag) {
					eriCellDataExportManager.updateTokenMsg(token, "测量频点漏定义导出失败...");
				}
			}
			//BA表个数检查
			else if (("baNumCheck").equals(type)) {
				flag = exportEriCellBaNumCheckResult(token, workbook,
						cmdResult, bscIdStr, date, cityId, settings);
				if(!flag) {
					eriCellDataExportManager.updateTokenMsg(token, "BA表个数检查导出失败...");
				}
			}
			//TALIM_MAXTA检查
			else if (("talimMaxTa").equals(type)) {
				flag = exportEriCellTalimMaxTaCheckResult(token, workbook,
						cmdResult, bscIdStr, date, cityId, settings);
				if(!flag) {
					eriCellDataExportManager.updateTokenMsg(token, "TALIM_MAXTA检查导出失败...");
				}
			}
			//同频同bsic检查
			else if (("sameFreqBsicCheck").equals(type)) {
				flag = exportEriCellCoBsicCheckResult(token, workbook,
						cmdResult, bscIdStr, date, cityId, settings);
				if(!flag) {
					eriCellDataExportManager.updateTokenMsg(token, "同频同bsic数据导出失败...");
				}
				
			}
			//邻区过多过少检查
			else if (("ncellNumCheck").equals(type)) {
				flag = exportEriCellNcellNumCheckResult(token, workbook,
						cmdResult, bscIdStr, date, cityId, settings);
				if(!flag) {
					eriCellDataExportManager.updateTokenMsg(token, "邻区过多过少数据导出失败...");
				}
			}
			//本站邻区漏定义
			else if (("ncellMomit").equals(type)) {
				flag = exportEriCellNcellMomitCheckResult(token, workbook,
						cmdResult, bscIdStr, date, cityId, settings);
				if(!flag) {
					eriCellDataExportManager.updateTokenMsg(token, "本站邻区漏定义数据导出失败...");
				}
			}
			//单向邻区检查
			else if (("unidirNcell").equals(type)) {
				flag = exportEriCellUnidirNcellResult(token, workbook,
						cmdResult, bscIdStr, date, cityId, settings);
				if(!flag) {
					eriCellDataExportManager.updateTokenMsg(token, "单向邻区检查数据导出失败...");
				}
			}
			//同邻频检查
			else if (("sameNcellFreqCheck").equals(type)) {
				flag = exportEriCellSameNcellFreqCheckResult(token, workbook,
						cmdResult, bscIdStr, date, cityId, settings);
				if(!flag) {
					eriCellDataExportManager.updateTokenMsg(token, "同邻频检查数据导出失败...");
				}
			}
		}

		eriCellDataExportManager.updateTokenMsg(token, "整理总指令中...");
		row = sheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("名称");
		cell = row.createCell(1);
		cell.setCellValue("指令");
		
		String bscTemp = "";
		String bsc = "";
		String name = "";
		String nameTemp = "";
		int num = 1;
		
		if(cmdResult.size() > 0) {
			//第一行为连接bsc指令
			bsc = cmdResult.get(0).get("bsc").toString();
			name = cmdResult.get(0).get("name").toString();

			row = sheet.createRow(num);
			cell = row.createCell(0);
			cell.setCellValue(cmdResult.get(0).get("name").toString());
			cell = row.createCell(1);
			cell.setCellValue("@CONNECT(\""+bsc+"\")");
			bscTemp = bsc;
			nameTemp = name;
			log.debug("cmdResult.size=" + cmdResult.size());
			for (int i = 0; i < cmdResult.size(); i++) {
				bsc = cmdResult.get(i).get("bsc").toString();
				name = cmdResult.get(i).get("name").toString();
				if(!bsc.equals(bscTemp) || !name.equals(nameTemp)) {
					num++;
					row = sheet.createRow(num);
					cell = row.createCell(0);
					cell.setCellValue(nameTemp);
					cell = row.createCell(1);
					cell.setCellValue("@DISCONNECT(\""+bscTemp+"\")");
					num++;
					row = sheet.createRow(num);
					cell = row.createCell(0);
					cell.setCellValue(name);
					cell = row.createCell(1);
					cell.setCellValue("@CONNECT(\""+bsc+"\")");
					num++;
					row = sheet.createRow(num);
					cell = row.createCell(0);
					cell.setCellValue(name);
					cell = row.createCell(1);
					cell.setCellValue(cmdResult.get(i).get("command").toString());
					bscTemp = bsc;
					nameTemp = name;
				} else {
					num++;
					row = sheet.createRow(num);
					cell = row.createCell(0);
					cell.setCellValue(cmdResult.get(i).get("name").toString());
					cell = row.createCell(1);
					cell.setCellValue(cmdResult.get(i).get("command").toString());
				}
			}
			//最后一行断开bsc指令
			num++;
			row = sheet.createRow(num);
			cell = row.createCell(0);
			cell.setCellValue(cmdResult.get(cmdResult.size()-1).get("name").toString());
			cell = row.createCell(1);
			cell.setCellValue("@DISCONNECT(\""
					+ cmdResult.get(cmdResult.size() - 1).get("bsc").toString()+ "\")");
		}
		
		//写入文件
		try {
			workbook.write(fos);
			fos.flush();
		} catch (IOException e) {
			e.printStackTrace();
			eriCellDataExportManager.updateTokenMsg(token, "爱立信小区参数一致性检查文件导出失败");
			eriCellDataExportManager.tokenFail(token);
			return;
		}
		
		//打包
		String zipPath = realPath + "/一致性检查结果_"+date+".zip";
		ZipFileHandler.zip(realPath, "", zipPath);
		//保存压缩包路径
		eriCellDataExportManager.saveTokenFilePath(token, zipPath);
		
		//导出任务完成
		eriCellDataExportManager.tokenFinished(token);
		
		try {
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * 导出TALIM_MAXTA检查sheet到文件
	 * @param token
	 * @param fos
	 * @param workbook
	 * @param cmdResult
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author peng.jm
	 * @date 2014-11-11下午04:21:02
	 */
	private boolean exportEriCellTalimMaxTaCheckResult(String token, Workbook workbook,
			List<Map<String, Object>> cmdResult, String bscIdStr, String date,
			long cityId, Map<String, String> settings) {
		eriCellDataExportManager.updateTokenMsg(token, "导出TALIM_MAXTA检查数据中...");
		List<Map<String, Object>> result = getEriCellTalimAndMaxtaCheckResult(
				bscIdStr, date, cityId, settings);
		Sheet sheet;
		Row row;
		Cell cell;
		
		sheet = workbook.createSheet("TALIM_MAXTA检查");
		row = sheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("CREATE_TIME");
		cell = row.createCell(1);
		cell.setCellValue("BSC");
		cell = row.createCell(2);
		cell.setCellValue("CELL");
		cell = row.createCell(3);
		cell.setCellValue("TALIM");
		cell = row.createCell(4);
		cell.setCellValue("MAXTA");
		
		Map<String,Object> one = null;
		for (int i = 0; i < result.size(); i++) {
			one = result.get(i);
			row = sheet.createRow(i + 1);
			cell = row.createCell(0);
			cell.setCellValue(one.get("CREATE_TIME").toString());
			cell = row.createCell(1);
			cell.setCellValue(one.get("BSC").toString());
			cell = row.createCell(2);
			cell.setCellValue(one.get("CELL").toString());
			cell = row.createCell(3);
			cell.setCellValue(one.get("TALIM").toString());
			cell = row.createCell(4);
			cell.setCellValue(one.get("MAXTA").toString());
		}
//		try {
//			workbook.write(fos);
//			fos.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
		return true;
	}
	
	/**
	 * 导出BA表个数检查sheet到文件
	 * @param token
	 * @param fos
	 * @param workbook
	 * @param cmdResult
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author peng.jm
	 * @date 2014-11-11下午04:12:39
	 */
	private boolean exportEriCellBaNumCheckResult(String token, Workbook workbook,
			List<Map<String, Object>> cmdResult, String bscIdStr, String date,
			long cityId, Map<String, String> settings) {
		eriCellDataExportManager.updateTokenMsg(token, "导出BA表个数检查数据中...");
		
		List<Map<String, Object>> result = getEriCellBaNumCheckResult(
				bscIdStr, date, cityId, settings);
		Sheet sheet;
		Row row;
		Cell cell;
		
		sheet = workbook.createSheet("BA表个数检查");
		row = sheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("BSC");
		cell = row.createCell(1);
		cell.setCellValue("CELL");
		cell = row.createCell(2);
		cell.setCellValue("LISTTYPE");
		cell = row.createCell(3);
		cell.setCellValue("NUM");
		
		Map<String,Object> one = null;
		for (int i = 0; i < result.size(); i++) {
			one = result.get(i);
			row = sheet.createRow(i + 1);
			cell = row.createCell(0);
			cell.setCellValue(one.get("BSC").toString());
			cell = row.createCell(1);
			cell.setCellValue(one.get("CELL").toString());
			cell = row.createCell(2);
			cell.setCellValue(one.get("LISTTYPE").toString());
			cell = row.createCell(3);
			cell.setCellValue(one.get("NUM").toString());
		}
//		try {
//			workbook.write(fos);
//			fos.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
		return true;
	}
	
	/**
	 * 导出测量频点漏定义sheet到文件
	 * @param token
	 * @param fos
	 * @param workbook
	 * @param cmdResult
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author peng.jm
	 * @date 2014-11-11下午04:08:53
	 */
	private boolean exportEriCellMeaFreqMomitResult(String token, Workbook workbook,
			List<Map<String, Object>> cmdResult, String bscIdStr, String date,
			long cityId, Map<String, String> settings) {
		eriCellDataExportManager.updateTokenMsg(token, "导出测量频点漏定义数据中...");
		
		List<Map<String, Object>> result = getEriCellMeaFreqMomitResult(
				bscIdStr, date, cityId, settings);
		Sheet sheet;
		Row row;
		Cell cell;
		
		sheet = workbook.createSheet("测量频点漏定义");
		row = sheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("BSC");
		cell = row.createCell(1);
		cell.setCellValue("CELL");
		cell = row.createCell(2);
		cell.setCellValue("LISTTYPE");
		cell = row.createCell(3);
		cell.setCellValue("漏定义频点");
		cell = row.createCell(4);
		cell.setCellValue("指令");
		
		Map<String,Object> one = null;
		Map<String,Object> map = null;
		for (int i = 0; i < result.size(); i++) {
			one = result.get(i);
			row = sheet.createRow(i + 1);
			cell = row.createCell(0);
			cell.setCellValue(one.get("BSC").toString());
			cell = row.createCell(1);
			cell.setCellValue(one.get("CELL").toString());
			cell = row.createCell(2);
			cell.setCellValue(one.get("LISTTYPE").toString());
			cell = row.createCell(3);
			cell.setCellValue(one.get("LEAK_DEFINE").toString());
			cell = row.createCell(4);
			cell.setCellValue(one.get("COMMAND").toString());
			map = new HashMap<String, Object>();
			map.put("name", "测量频点漏定义");
			map.put("bsc", one.get("BSC").toString());
			map.put("command", one.get("COMMAND").toString());
			cmdResult.add(map);
		}
//		try {
//			workbook.write(fos);
//			fos.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
		return true;
	}
	
	/**
	 * 导出测量频点多定义sheet到文件
	 * @param token
	 * @param fos
	 * @param workbook
	 * @param cmdResult
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author peng.jm
	 * @date 2014-11-11下午02:58:55
	 */
	private boolean exportEriCellMeaFreqMultidefineResult(String token, Workbook workbook,
			List<Map<String, Object>> cmdResult, String bscIdStr, String date,
			long cityId, Map<String, String> settings) {
		eriCellDataExportManager.updateTokenMsg(token, "导出测量频点多定义数据中...");
		
		List<Map<String, Object>> result = getEriCellMeaFreqMultidefineResult(
				bscIdStr, date, cityId, settings);
		Sheet sheet;
		Row row;
		Cell cell;
		
		sheet = workbook.createSheet("测量频点多定义");
		row = sheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("BSC");
		cell = row.createCell(1);
		cell.setCellValue("CELL");
		cell = row.createCell(2);
		cell.setCellValue("LISTTYPE");
		cell = row.createCell(3);
		cell.setCellValue("多定义频点");
		cell = row.createCell(4);
		cell.setCellValue("指令");
		
		Map<String,Object> one = null;
		Map<String,Object> map = null;
		for (int i = 0; i < result.size(); i++) {
			one = result.get(i);
			row = sheet.createRow(i + 1);
			cell = row.createCell(0);
			cell.setCellValue(one.get("BSC").toString());
			cell = row.createCell(1);
			cell.setCellValue(one.get("CELL").toString());
			cell = row.createCell(2);
			cell.setCellValue(one.get("LISTTYPE").toString());
			cell = row.createCell(3);
			cell.setCellValue(one.get("OVER_DEFINE").toString());
			cell = row.createCell(4);
			cell.setCellValue(one.get("COMMAND").toString());
			map = new HashMap<String, Object>();
			map.put("name", "测量频点多定义");
			map.put("bsc", one.get("BSC").toString());
			map.put("command", one.get("COMMAND").toString());
			cmdResult.add(map);
		}
//		try {
//			workbook.write(fos);
//			fos.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
		return true;
	}
	/**
	 * 导出NCCPERM检查sheet到文件
	 * @param token
	 * @param fos
	 * @param workbook
	 * @param cmdResult
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @return
	 * @author peng.jm
	 * @date 2014-11-11上午10:07:52
	 */
	private boolean exportEriCellNccpermResult(String token, Workbook workbook,
			List<Map<String, Object>> cmdResult, String bscIdStr, String date,
			long cityId, Map<String, String> settings) {
		eriCellDataExportManager.updateTokenMsg(token, "导出NCCPERM检查数据中...");
		
		List<Map<String, Object>> result = getEriCellNccpermResult(
				bscIdStr, date, cityId, settings);
		Sheet sheet;
		Row row;
		Cell cell;
		
		sheet = workbook.createSheet("NCCPERM检查");
		row = sheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("BSC");
		cell = row.createCell(1);
		cell.setCellValue("CELL");
		cell = row.createCell(2);
		cell.setCellValue("NCCPERM");
		cell = row.createCell(3);
		cell.setCellValue("缺失的NCC");
		cell = row.createCell(4);
		cell.setCellValue("指令");
		
		Map<String,Object> one = null;
		Map<String,Object> map = null;
		for (int i = 0; i < result.size(); i++) {
			one = result.get(i);
			row = sheet.createRow(i + 1);
			cell = row.createCell(0);
			cell.setCellValue(one.get("BSC").toString());
			cell = row.createCell(1);
			cell.setCellValue(one.get("CELL").toString());
			cell = row.createCell(2);
			cell.setCellValue(one.get("NCCPERM").toString());
			cell = row.createCell(3);
			cell.setCellValue(one.get("LEAK_NCC").toString());
			cell = row.createCell(4);
			cell.setCellValue(one.get("COMMAND").toString());
			map = new HashMap<String, Object>();
			map.put("name", "NCCPERM检查");
			map.put("bsc", one.get("BSC").toString());
			map.put("command", one.get("COMMAND").toString());
			cmdResult.add(map);
		}
//		try {
//			workbook.write(fos);
//			fos.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
		return true;
	}
	/**
	 * 导出跳频检查sheet到文件
	 * @param token
	 * @param fos
	 * @param workbook
	 * @param cmdResult
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @return
	 * @author peng.jm
	 * @date 2014-11-11上午10:07:52
	 */
	private boolean exportEriCellFreqHopCheckResult(String token, Workbook workbook,
			List<Map<String, Object>> cmdResult, String bscIdStr, String date,
			long cityId, Map<String, String> settings) {
		eriCellDataExportManager.updateTokenMsg(token, "导出跳频检查数据中...");
		
		List<Map<String, Object>> result = getEriCellFreqHopCheckResult(
				bscIdStr, date, cityId, settings);
		Sheet sheet;
		Row row;
		Cell cell;
		
		sheet = workbook.createSheet("跳频检查");
		row = sheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("BSC");
		cell = row.createCell(1);
		cell.setCellValue("CELL");
		cell = row.createCell(2);
		cell.setCellValue("CHG");
		cell = row.createCell(3);
		cell.setCellValue("HOP");
		cell = row.createCell(4);
		cell.setCellValue("频点数");
		cell = row.createCell(5);
		cell.setCellValue("频点列表");
		cell = row.createCell(6);
		cell.setCellValue("指令");	
		
		Map<String,Object> one = null;
		Map<String,Object> map = null;
		for (int i = 0; i < result.size(); i++) {
			one = result.get(i);
			row = sheet.createRow(i + 1);
			cell = row.createCell(0);
			cell.setCellValue(one.get("BSC").toString());
			cell = row.createCell(1);
			cell.setCellValue(one.get("CELL").toString());
			cell = row.createCell(2);
			cell.setCellValue(one.get("CH_GROUP").toString());
			cell = row.createCell(3);
			cell.setCellValue(one.get("HOP").toString());
			cell = row.createCell(4);
			cell.setCellValue(one.get("DCHNO").toString());
			cell = row.createCell(5);
			cell.setCellValue(one.get("DCH").toString());
			cell = row.createCell(6);
			cell.setCellValue(one.get("COMMAND").toString());
			map = new HashMap<String, Object>();
			map.put("name", "跳频检查");
			map.put("bsc", one.get("BSC").toString());
			map.put("command", one.get("COMMAND").toString());
			cmdResult.add(map);
		}
//		try {
//			workbook.write(fos);
//			fos.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
		return true;
	}
	/**
	 * 导出功率检查sheet到文件
	 * @param token
	 * @param fos
	 * @param workbook
	 * @param cmdResult
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @return
	 * @author peng.jm
	 * @date 2014-11-11上午10:07:52
	 */
	private boolean exportEriCellPowerCheckResult(String token, Workbook workbook,
			List<Map<String, Object>> cmdResult, 
			String bscIdStr, String date,long cityId) {
		
		eriCellDataExportManager.updateTokenMsg(token, "导出功率检查数据中...");
		
		List<Map<String, Object>> result = getEriCellPowerCheckResult(bscIdStr,
				date, cityId);
		Sheet sheet;
		Row row;
		Cell cell;
		
		sheet = workbook.createSheet("功率检查");
		row = sheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("BSC");
		cell = row.createCell(1);
		cell.setCellValue("CELL");
		cell = row.createCell(2);
		cell.setCellValue("BSPWRB");
		cell = row.createCell(3);
		cell.setCellValue("BSPWRT");
		cell = row.createCell(4);
		cell.setCellValue("指令");
		
		Map<String,Object> one = null;
		Map<String,Object> map = null;
		for (int i = 0; i < result.size(); i++) {
			one = result.get(i);
			row = sheet.createRow(i + 1);
			cell = row.createCell(0);
			cell.setCellValue(one.get("BSC").toString());
			cell = row.createCell(1);
			cell.setCellValue(one.get("CELL").toString());
			cell = row.createCell(2);
			cell.setCellValue(one.get("BSPWRB").toString());
			cell = row.createCell(3);
			cell.setCellValue(one.get("BSPWRT").toString());
			cell = row.createCell(4);
			cell.setCellValue(one.get("COMMAND").toString());
			map = new HashMap<String, Object>();
			map.put("name", "功率检查");
			map.put("bsc", one.get("BSC").toString());
			map.put("command", one.get("COMMAND").toString());
			cmdResult.add(map);
		}
//		try {
//			workbook.write(fos);
//			fos.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
		return true;
	}
	/**
	 * 
	 * @title 导出同频同bsic检查sheet到文件
	 * @param token
	 * @param fos
	 * @param workbook
	 * @param cmdResult
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author chao.xj
	 * @date 2014-11-11下午12:04:35
	 * @company 怡创科技
	 * @version 1.2
	 */
	private boolean exportEriCellCoBsicCheckResult(String token,
			Workbook workbook,List<Map<String, Object>> cmdResult, 
			String bscIdStr, String date,long cityId,Map<String, String> settings) {
		
		eriCellDataExportManager.updateTokenMsg(token, "导出同频同bsic数据中...");
		
		List<Map<String, Object>> result = getEriCellCoBsicCheckResult(bscIdStr, date, cityId, settings);
		Sheet sheet;
		Row row;
		Cell cell;
		
		sheet = workbook.createSheet("同频同BSIC检查");
		row = sheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("BSIC");
		cell = row.createCell(1);
		cell.setCellValue("BCCHNO");
		cell = row.createCell(2);
		cell.setCellValue("BSC1");
		cell = row.createCell(3);
		cell.setCellValue("CELL1");
		cell = row.createCell(4);
		cell.setCellValue("CELL1_NAME");
		cell = row.createCell(5);
		cell.setCellValue("BSC2");
		cell = row.createCell(6);
		cell.setCellValue("CELL2");
		cell = row.createCell(7);
		cell.setCellValue("CELL2_NAME");
		cell = row.createCell(8);
		cell.setCellValue("DISTANCE(M)");
		cell = row.createCell(9);
		cell.setCellValue("指令");
//		Map<String,Object> one = null;
//		Map<String,Object> map = null;
		CobsicCells cobsicCells=null;
		String combinedCell="";
		int m=0;
		for (int i = 0; i < result.size(); i++) {
			for (String  key : result.get(i).keySet()) {
				cobsicCells=(CobsicCells)result.get(i).get(key);
				for (int j = 0; j < cobsicCells.getCombinedCells().size(); j++) {
					combinedCell=cobsicCells.getCombinedCells().get(j).getCombinedCell();
					row = sheet.createRow(m + 1);
					cell = row.createCell(0);
					cell.setCellValue(cobsicCells.getBsic());
					cell = row.createCell(1);
					cell.setCellValue(cobsicCells.getBcch());
					cell = row.createCell(2);
					cell.setCellValue(combinedCell.split(",")[0].split("-")[0]);
					cell = row.createCell(3);
					cell.setCellValue(combinedCell.split(",")[0].split("-")[1]);
					cell = row.createCell(4);
					cell.setCellValue(combinedCell.split(",")[0].split("-")[2]);
					cell = row.createCell(5);
					cell.setCellValue(combinedCell.split(",")[1].split("-")[0]);
					cell = row.createCell(6);
					cell.setCellValue(combinedCell.split(",")[1].split("-")[1]);
					cell = row.createCell(7);
					cell.setCellValue(combinedCell.split(",")[1].split("-")[2]);
					cell = row.createCell(8);
					cell.setCellValue(Math.round(cobsicCells.getCombinedCells().get(j).getMeaDis()));
					cell = row.createCell(9);
					cell.setCellValue(cobsicCells.getCombinedCells().get(j).getMml());
//					map = new HashMap<String, Object>();
//					map.put("name", "同频同BSIC检查");
//					map.put("command", cobsicCells.getCombinedCells().get(j).getMml());
//					cmdResult.add(map);
				}
				m++;
			}
			
		}
		
//		try {
//			workbook.write(fos);
//			fos.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
		return true;
	}
	/**
	 * 
	 * @title 导出邻区过多过少检查sheet到文件
	 * @param token
	 * @param fos
	 * @param workbook
	 * @param cmdResult
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author chao.xj
	 * @date 2014-11-11下午12:04:35
	 * @company 怡创科技
	 * @version 1.2
	 */
	private boolean exportEriCellNcellNumCheckResult(String token,
			Workbook workbook,List<Map<String, Object>> cmdResult, 
			String bscIdStr, String date,long cityId,Map<String, String> settings) {
		
		eriCellDataExportManager.updateTokenMsg(token, "导出邻区过多过少检查数据中...");
		
		List<Map<String, Object>> result = getEriCellNcellNumCheckResult(bscIdStr, date, cityId, settings);
		Sheet sheet;
		Row row;
		Cell cell;
		
		sheet = workbook.createSheet("邻区过多过少检查");
		row = sheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("BSC");
		cell = row.createCell(1);
		cell.setCellValue("CELL");
		cell = row.createCell(2);
		cell.setCellValue("邻区数量");
		cell = row.createCell(3);
		cell.setCellValue("邻区信息");
//		Map<String,Object> map = null;
		Map<String,Object> one = null;
		for (int i = 0; i < result.size(); i++) {
			one = result.get(i);
			row = sheet.createRow(i + 1);
			cell = row.createCell(0);
			cell.setCellValue(one.get("BSC").toString());
			cell = row.createCell(1);
			cell.setCellValue(one.get("CELL").toString());
			cell = row.createCell(2);
			cell.setCellValue(one.get("N_CELL_NUM").toString());
			cell = row.createCell(3);
			cell.setCellValue(one.get("N_CELLS").toString());
//			map = new HashMap<String, Object>();
//			map.put("name", "邻区过多过少检查");
//			map.put("command", one.get("COMMAND").toString());
//			cmdResult.add(map);
		}
		
//		try {
//			workbook.write(fos);
//			fos.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
		return true;
	}
	/**
	 * 
	 * @title 导出本站邻区漏定义检查sheet到文件
	 * @param token
	 * @param fos
	 * @param workbook
	 * @param cmdResult
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author chao.xj
	 * @date 2014-11-11下午12:04:35
	 * @company 怡创科技
	 * @version 1.2
	 */
	private boolean exportEriCellNcellMomitCheckResult(String token,
			Workbook workbook,List<Map<String, Object>> cmdResult, 
			String bscIdStr, String date,long cityId,Map<String, String> settings) {
		
		eriCellDataExportManager.updateTokenMsg(token, "导出本站邻区漏定义数据中...");
		
		List<Map<String, Object>> result = getEriCellNcellMomitCheckResult(bscIdStr, date, cityId, settings);
		Sheet sheet;
		Row row;
		Cell cell;
		
		sheet = workbook.createSheet("本站邻区漏定义检查");
		row = sheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("BSC");
		cell = row.createCell(1);
		cell.setCellValue("CELL");
		cell = row.createCell(2);
		cell.setCellValue("CELLR");
		cell = row.createCell(3);
		cell.setCellValue("CELLR_BSC");
		cell = row.createCell(4);
		cell.setCellValue("CS");
		cell = row.createCell(5);
		cell.setCellValue("指令");
		Map<String,Object> map = null;
		Map<String,Object> one = null;
		for (int i = 0; i < result.size(); i++) {
			one = result.get(i);
			row = sheet.createRow(i + 1);
			cell = row.createCell(0);
			cell.setCellValue(one.get("BSC").toString());
			cell = row.createCell(1);
			cell.setCellValue(one.get("CELL").toString());
			cell = row.createCell(2);
			cell.setCellValue(one.get("CELLR").toString());
			cell = row.createCell(3);
			cell.setCellValue(one.get("CELLR_BSC").toString());
			cell = row.createCell(4);
			cell.setCellValue(one.get("CS")==null?" ":one.get("CS").toString());
			cell = row.createCell(5);
			cell.setCellValue(one.get("MML").toString());
			map = new HashMap<String, Object>();
			map.put("name", "本站邻区漏定义检查");
			map.put("bsc", one.get("BSC").toString());
			map.put("command", one.get("MML").toString());
			cmdResult.add(map);
		}
		
//		try {
//			workbook.write(fos);
//			fos.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
		return true;
	}
	/**
	 * 
	 * @title 导出单向邻区检查sheet到文件
	 * @param token
	 * @param fos
	 * @param workbook
	 * @param cmdResult
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author chao.xj
	 * @date 2014-11-11下午12:04:35
	 * @company 怡创科技
	 * @version 1.2
	 */
	private boolean exportEriCellUnidirNcellResult(String token,
			Workbook workbook,List<Map<String, Object>> cmdResult, 
			String bscIdStr, String date,long cityId,Map<String, String> settings) {
		
		eriCellDataExportManager.updateTokenMsg(token, "导出单向邻区检查数据中...");
		
		List<Map<String, Object>> result = getEriCellUnidirNcellResult(bscIdStr, date, cityId, settings);
		Sheet sheet;
		Row row;
		Cell cell;
		
		sheet = workbook.createSheet("单向邻区");
		row = sheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("BSC");
		cell = row.createCell(1);
		cell.setCellValue("CELL");
		cell = row.createCell(2);
		cell.setCellValue("CELLR");
		cell = row.createCell(3);
		cell.setCellValue("DIR");
		cell = row.createCell(4);
		cell.setCellValue("CELLR_BSC");
		cell = row.createCell(5);
		cell.setCellValue("同BSC");
		cell = row.createCell(6);
		cell.setCellValue("指令");
		Map<String,Object> map = null;
		Map<String,Object> one = null;
		for (int i = 0; i < result.size(); i++) {
			one = result.get(i);
			row = sheet.createRow(i + 1);
			cell = row.createCell(0);
			cell.setCellValue(one.get("BSC").toString());
			cell = row.createCell(1);
			cell.setCellValue(one.get("CELL").toString());
			cell = row.createCell(2);
			cell.setCellValue(one.get("CELLR").toString());
			cell = row.createCell(3);
			cell.setCellValue(one.get("DIR").toString());
			cell = row.createCell(4);
			cell.setCellValue(one.get("CELLR_BSC").toString());
			cell = row.createCell(5);
			cell.setCellValue(one.get("IS_SAME_BSC").toString());
			cell = row.createCell(6);
			cell.setCellValue(one.get("COMMAND").toString());
			
			map = new HashMap<String, Object>();
			map.put("name", "单向邻区");
			map.put("bsc", one.get("BSC").toString());
			map.put("command", one.get("COMMAND").toString());
			cmdResult.add(map);
		}
		
//		try {
//			workbook.write(fos);
//			fos.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
		return true;
	}
	/**
	 * 
	 * @title 导出同邻频检查sheet到文件
	 * @param token
	 * @param fos
	 * @param workbook
	 * @param cmdResult
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author chao.xj
	 * @date 2014-11-11下午12:04:35
	 * @company 怡创科技
	 * @version 1.2
	 */
	private boolean exportEriCellSameNcellFreqCheckResult(String token,
			Workbook workbook,List<Map<String, Object>> cmdResult, 
			String bscIdStr, String date,long cityId,Map<String, String> settings) {
		
		eriCellDataExportManager.updateTokenMsg(token, "导出同邻频检查数据中...");
		
		List<Map<String, Object>> result = getEriCellSameNcellFreqCheckResult(bscIdStr, date, cityId, settings);
		Sheet sheet;
		Row row;
		Cell cell;
		
		sheet = workbook.createSheet("同邻频检查");
		row = sheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("BSC");
		cell = row.createCell(1);
		cell.setCellValue("CELL");
		cell = row.createCell(2);
		cell.setCellValue("CELLR");
		cell = row.createCell(3);
		cell.setCellValue("CELL_BCCH");
		cell = row.createCell(4);
		cell.setCellValue("CELLR_BCCH");
		cell = row.createCell(5);
		cell.setCellValue("CELL_问题频点");
		cell = row.createCell(6);
		cell.setCellValue("CELLR_问题频点");
		cell = row.createCell(7);
		cell.setCellValue("DIR");
		cell = row.createCell(8);
		cell.setCellValue("CS");
		cell = row.createCell(9);
		cell.setCellValue("DISTANCE");
		cell = row.createCell(10);
		cell.setCellValue("备注");
//		Map<String,Object> map = null;
		Map<String,Object> one = null;
		for (int i = 0; i < result.size(); i++) {
			one = result.get(i);
			row = sheet.createRow(i + 1);
			cell = row.createCell(0);
			cell.setCellValue(one.get("BSC").toString());
			cell = row.createCell(1);
			cell.setCellValue(one.get("CELL").toString());
			cell = row.createCell(2);
			cell.setCellValue(one.get("CELLR").toString());
			cell = row.createCell(3);
			cell.setCellValue(one.get("CELL_BCCH").toString());
			cell = row.createCell(4);
			cell.setCellValue(one.get("CELLR_BCCH").toString());
			cell = row.createCell(5);
			cell.setCellValue(one.get("CELL_FREQ").toString());
			cell = row.createCell(6);
			cell.setCellValue(one.get("CELLR_FREQ").toString());
			cell = row.createCell(7);
			cell.setCellValue(one.get("DIR").toString());
			cell = row.createCell(8);
			cell.setCellValue(one.get("CS").toString());
			cell = row.createCell(9);
			cell.setCellValue(one.get("DISTANCE").toString());
			cell = row.createCell(10);
			cell.setCellValue(one.get("COMMENT").toString());
//			map = new HashMap<String, Object>();
//			map.put("name", "同邻频检查");
//			map.put("command", one.get("COMMAND").toString());
//			cmdResult.add(map);
		}
		
//		try {
//			workbook.write(fos);
//			fos.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
		return true;
	}
	/**
	 * 获取爱立信小区某两个日期的参数不一致的详情
	 * 
	 * @param cityId
	 * @param bsc
	 * @param param
	 * @param paramType
	 * @param date1
	 * @param date2
	 * @return
	 * @author peng.jm
	 * @date 2014-10-23下午06:03:45
	 */
	public List<Map<String, Object>> getEriCellParamsDiffDetail(long cityId,
			String bsc, String paramType, String param, String date1, String date2) {
		return rno2GCellAnalysisDao.getEriCellParamsDiffDetail(cityId, bsc,
				paramType, param, date1, date2);
	}
	
	/**
	 * 通过检查类型获取爱立信小区的参数一致性结果
	 * @param checkType
	 * @param bscStr
	 * @param date1
	 * @param cityId
	 * @param settings
	 * @return
	 * @author peng.jm
	 * @date 2014-10-27下午02:11:19
	 */
	public List<Map<String, Object>> getEriCellParamCheckByType(
			String checkType, String bscStr, String date, long cityId, Map<String,String> settings) {

		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		//为bsc字符串加入引号
		String bscs[] = bscStr.split(",");
		String bscStr2 = "";
		for (String bsc : bscs) {
			bscStr2 += "'"+bsc+"',";
		}
		bscStr2 = bscStr2.substring(0,bscStr2.length()-1);
		//通过bsc名称字符串获取bscId字符串
		List<Map<String, Object>> bscList = rno2GCellAnalysisDao.getBscDetailByBscs(bscStr2);
		if(bscList.size() == 0) {
			log.debug("bscIdStr="+bscStr+"条件下，查询不到BSC！");
		}
		//获取bscId列
		String bscIdStr = "";
		for (Map<String, Object> bsc : bscList) {
			if(bsc.get("BSC_ID") != null) {
				bscIdStr += bsc.get("BSC_ID").toString() + ",";
			}
		}
		bscIdStr = bscIdStr.substring(0,bscIdStr.length()-1);
		
		//功率检查
		if (("powerCheck").equals(checkType)) {
			result = getEriCellPowerCheckResult(bscIdStr, date, cityId);
		} 
		//跳频检查
		else if (("freqHopCheck").equals(checkType)) {
			result = getEriCellFreqHopCheckResult(bscIdStr, date, cityId, settings);
		}
		//NCCPERM检查
		else if (("nccperm").equals(checkType)) {
			result = getEriCellNccpermResult(bscIdStr, date, cityId, settings);
		}
		//测量频点多定义
		else if (("meaFreqMultidefined").equals(checkType)) {
			result = getEriCellMeaFreqMultidefineResult(bscIdStr, date, cityId, settings);
		}
		//测量频点漏定义
		else if (("meaFreqMomit").equals(checkType)) {
			result = getEriCellMeaFreqMomitResult(bscIdStr, date, cityId, settings);
		}
		//BA表个数检查
		else if (("baNumCheck").equals(checkType)) {
			result = getEriCellBaNumCheckResult(bscIdStr, date, cityId, settings);
		}
		//TALIM_MAXTA检查
		else if (("talimMaxTa").equals(checkType)) {
			result = getEriCellTalimAndMaxtaCheckResult(bscIdStr, date, cityId, settings);
		}
		//同频同bsic检查
		else if (("sameFreqBsicCheck").equals(checkType)) {
			result = getEriCellCoBsicCheckResult(bscIdStr, date, cityId, settings);
		}
		//邻区过多过少检查
		else if (("ncellNumCheck").equals(checkType)) {
			result = getEriCellNcellNumCheckResult(bscIdStr, date, cityId, settings);
		}
		//本站邻区漏定义
		else if (("ncellMomit").equals(checkType)) {
			result = getEriCellNcellMomitCheckResult(bscIdStr, date, cityId, settings);
		}
		//单向邻区检查
		else if (("unidirNcell").equals(checkType)) {
			result = getEriCellUnidirNcellResult(bscIdStr, date, cityId, settings);
		}
		//同邻频检查
		else if (("sameNcellFreqCheck").equals(checkType)) {
			result = getEriCellSameNcellFreqCheckResult(bscIdStr, date, cityId, settings);
		}
		//邻区数据检查
		else if (("ncellDataCheck").equals(checkType)) {
			//result = getEriCellNcellDataCheckResult(bscIdStr, date, cityId, settings);
		}
		return result;
	}

	/**
	 * 获取爱立信小区功率检查结果
	 */
	public List<Map<String, Object>> getEriCellPowerCheckResult(
			String bscIdStr, String date, long cityId) {
		List<Map<String, Object>> result = rno2GCellAnalysisDao
				.getEriCellPowerCheckResult(bscIdStr, date, cityId);
		return result;
	}
	/**
	 * 获取爱立信小区跳频检查结果
	 */
	public List<Map<String, Object>> getEriCellFreqHopCheckResult(
			String bscIdStr, String date, long cityId, Map<String,String> settings) {
		List<Map<String, Object>> result = rno2GCellAnalysisDao
				.getEriCellFreqHopCheckResult(bscIdStr, date, cityId, settings);
		return result;
	} 
	/**
	 * 获取爱立信小区Nccperm检查结果
	 */
	private List<Map<String, Object>> getEriCellNccpermResult(String bscIdStr,
			String date, long cityId, Map<String, String> settings) {
		
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		
		List<Map<String, Object>> res = rno2GCellAnalysisDao
				.getEriCellNccpermResult(bscIdStr, date, cityId, settings);
		
		String cell ="";
		String nccpermStr = ""; //小区的nccperm字符串
		String ncellNccStr = ""; //小区对应邻区的ncc组成的字符串
		String leakNcc = "";  //缺失的ncc
		String command = ""; //指令
		String nccStr = ""; //NCCPERM指令
		
		String nccperm[] = {};
		String ncellNcc[] = {};
		List<String> nccpermList = new ArrayList<String>();
		
		//判断nccperm是否存在漏定，有则加入结果集
		for (Map<String, Object> one : res) {
			//初始化
			nccStr = "";
			leakNcc = "";
			if(one.get("NCCPERM") != null && one.get("NCELL_NCC") != null) {
				nccpermStr = one.get("NCCPERM").toString();
				ncellNccStr = one.get("NCELL_NCC").toString();
				if(nccpermStr.equals(ncellNccStr)) {
					continue;
				} else {
					nccperm = nccpermStr.split(",");
					ncellNcc = ncellNccStr.split(",");
					nccpermList = Arrays.asList(nccperm);
					for (String ncc : nccperm) {
						nccStr += ncc + "&";
					}
					for (String ncc : ncellNcc) {
						if(!nccpermList.contains(ncc)) {
							leakNcc += ncc + ",";
							nccStr += ncc + "&";
						}
					}
					if(("").equals(leakNcc)) {
						//初始化，不存在漏定
						continue;
					}
					leakNcc = leakNcc.substring(0,leakNcc.length()-1);
					nccStr = nccStr.substring(0,nccStr.length()-1);
					//加入缺失ncc元素
					one.put("LEAK_NCC", leakNcc);
					//加入指令元素
					cell = one.get("CELL").toString();
					command = "RLSSC:CELL="+cell+",NCCPERM="+nccStr+";";
					one.put("COMMAND", command);
					result.add(one);
				}
			}
		}
		return result;
	}

	/**
	 * 获取爱立信小区测量频点多定义结果
	 */
	private List<Map<String, Object>> getEriCellMeaFreqMultidefineResult(
			String bscIdStr, String date, long cityId, Map<String, String> settings) {
		
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		
		List<Map<String, Object>> res = rno2GCellAnalysisDao
				.getEriCellMeaFreqResult(bscIdStr, date, cityId, settings);
		
		String activeStr = "";
		String idleStr = "";
		String ncellBcchStr = "";
		
		String actives[] = {};
		String idles[] = {};
		String ncellBcchs[] = {};
		
		List<String> ncellBcchList = new ArrayList<String>();
		
		String overActiveStr = "";
		String overActiveComm = "";
		String overIdleStr = "";
		String overIdleComm = "";
		String command = "";
		
		Map<String,Object> map = null;
		
		//判断active与idle是否多定义
		for (Map<String, Object> one : res) {
			//初始化
			overActiveStr = "";
			overActiveComm = "";
			overIdleStr = "";
			overIdleComm = "";
			
			if (one.get("ACTIVE") != null && one.get("IDLE") != null
					&& one.get("NCELL_BCCH") != null) {
				activeStr = one.get("ACTIVE").toString();
				idleStr = one.get("IDLE").toString();
				ncellBcchStr = one.get("NCELL_BCCH").toString();
				actives = activeStr.split(",");
				idles = idleStr.split(",");
				ncellBcchs = ncellBcchStr.split(",");
				//转为list
				ncellBcchList = Arrays.asList(ncellBcchs);
				//判断active是否多定义
				for (String active : actives) {
					if(!ncellBcchList.contains(active)) {
						overActiveStr += active + ",";
						overActiveComm += active + "&";
					}
				}
				if(!("").equals(overActiveStr) && !("").equals(overActiveComm)) {
					overActiveStr = overActiveStr.substring(0,overActiveStr.length()-1);
					overActiveComm = overActiveComm.substring(0,overActiveComm.length()-1);
					if(one.get("BSC") != null && one.get("CELL") != null) {
						map = new HashMap<String, Object>();
						map.put("BSC", one.get("BSC"));
						map.put("CELL", one.get("CELL"));
						map.put("LISTTYPE", "ACTIVE");
						map.put("OVER_DEFINE", overActiveStr);
						command = "RLMFC:CELL="+one.get("CELL").toString()
							+",MBCCHNO="+overActiveComm+",MRNIC,LISTTYPE=ACTIVE;";
						map.put("COMMAND", command);
						result.add(map);
					}
				}
				//判断idle是否多定义
				for (String idle : idles) {
					if(!ncellBcchList.contains(idle)) {
						overIdleStr += idle + ",";
						overIdleComm += idle + "&";
					}
				}
				if(!("").equals(overIdleStr) && !("").equals(overIdleComm)) {
					overIdleStr = overIdleStr.substring(0,overIdleStr.length()-1);
					overIdleComm = overIdleComm.substring(0,overIdleComm.length()-1);
					if(one.get("BSC") != null && one.get("CELL") != null) {
						map = new HashMap<String, Object>();
						map.put("BSC", one.get("BSC"));
						map.put("CELL", one.get("CELL"));
						map.put("LISTTYPE", "IDLE");
						map.put("OVER_DEFINE", overIdleStr);
						command = "RLMFC:CELL="+one.get("CELL").toString()
							+",MBCCHNO="+overIdleComm+",MRNIC,LISTTYPE=IDLE;";
						map.put("COMMAND", command);
						result.add(map);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * 获取爱立信小区单向邻区检查
	 */
	private List<Map<String, Object>> getEriCellUnidirNcellResult(
			String bscIdStr, String date, long cityId, Map<String, String> settings) {
		List<Map<String, Object>> result = rno2GCellAnalysisDao
				.getEriCellUnidirNcellResult(bscIdStr, date, cityId, settings);
		return result;
	}
	
	/**
	 * 获取爱立信小区同邻频检查结果
	 */
	private List<Map<String, Object>> getEriCellSameNcellFreqCheckResult(
			String bscIdStr, String date, long cityId, Map<String, String> settings) {
		
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		
		List<Map<String, Object>> res = rno2GCellAnalysisDao
				.getEriCellSameNcellFreqData(bscIdStr, date, cityId, settings);
		log.info("获取爱立信小区同邻频数据数量:size="+res.size());
		String comment = ""; //备注
		
		String bsc = "";
		String cell = "";
		String ncell = "";
		String bcch = "";
		String tch = "";
		String nbcch = "";
		String ntch = "";
		String cs = "";
		
		String tchStr[] = {};
		String ntchStr[] = {};
		
		List<String> tchList = new ArrayList<String>();
		List<String> ntchList = new ArrayList<String>();
		
		Map<String,Object> map = null;
		String distance = "";
		String bcchR = "";
		String bcchL = "";
		String nbcchR = "";
		String nbcchL = "";
		String tchR = "";
		String tchL = "";
		
		for (Map<String, Object> one : res) {
			//初始化
			comment = "";
			
			if (one.get("CELL") == null || one.get("N_CELL") == null
					|| one.get("BCCHNO") == null || one.get("N_BCCHNO") == null
					|| one.get("TCH") == null || one.get("N_TCH") == null
					|| one.get("DISTANCE") == null) {
				continue;
			}
			
			bsc = one.get("BSC").toString();
			cell = one.get("CELL").toString();
			ncell = one.get("N_CELL").toString();
			bcch = one.get("BCCHNO").toString();
			nbcch = one.get("N_BCCHNO").toString();
			tch = one.get("TCH").toString();
			ntch = one.get("N_TCH").toString();
			cs = one.get("CS").toString();
			distance = one.get("DISTANCE").toString();
			
			if(("YES").equals(cs)) {
				comment = "同站";
			} else if(("NO").equals(cs)) {
				comment = "邻站";
			}
			
			tchStr = tch.split(",");
			ntchStr = ntch.split(",");
			tchList = Arrays.asList(tchStr);
			ntchList = Arrays.asList(ntchStr);
			
			//同BCCH
			if(ntchList.contains(bcch)) {
				map = new HashMap<String, Object>();
				map.put("BSC", bsc);
				map.put("CELL", cell);
				map.put("CELLR", ncell);
				map.put("CELL_BCCH", bcch);
				map.put("CELLR_BCCH", nbcch);
				map.put("CELL_FREQ", bcch);
				map.put("CELLR_FREQ", bcch);
				map.put("DIR", "MUTAUL");
				map.put("CS", cs);
				map.put("DISTANCE", distance);
				map.put("COMMENT", comment + "同BCCH");
				result.add(map);
			}
			
			if(tchList.contains(nbcch)) {
				map = new HashMap<String, Object>();
				map.put("BSC", bsc);
				map.put("CELL", cell);
				map.put("CELLR", ncell);
				map.put("CELL_BCCH", bcch);
				map.put("CELLR_BCCH", nbcch);
				map.put("CELL_FREQ", nbcch);
				map.put("CELLR_FREQ", nbcch);
				map.put("DIR", "MUTAUL");
				map.put("CS", cs);
				map.put("DISTANCE", distance);
				map.put("COMMENT", comment + "同BCCH");
				result.add(map);
			}
			//同TCH
			for (String t : tchList) {
				//过滤tch中的bcch，以免重复
				if(t.equals(bcch)) {
					continue;
				}
				if(ntchList.contains(t)) {
					//过滤ntch中的nbcch，以免重复
					if(t.equals(nbcch)) {
						continue;
					}
					map = new HashMap<String, Object>();
					map.put("BSC", bsc);
					map.put("CELL", cell);
					map.put("CELLR", ncell);
					map.put("CELL_BCCH", bcch);
					map.put("CELLR_BCCH", nbcch);
					map.put("CELL_FREQ", t);
					map.put("CELLR_FREQ", t);
					map.put("DIR", "MUTAUL");
					map.put("CS", cs);
					map.put("DISTANCE", distance);
					map.put("COMMENT", comment + "同TCH");
					result.add(map);
				}
			}
			//邻BCCH
			bcchR = (Integer.parseInt(bcch) + 1) + ""; //加1
			bcchL = (Integer.parseInt(bcch) - 1) + ""; //减1
			nbcchR = (Integer.parseInt(bcch) + 1) + ""; 
			nbcchL = (Integer.parseInt(bcch) - 1) + ""; 
			if(ntchList.contains(bcchR)) {
				map = new HashMap<String, Object>();
				map.put("BSC", bsc);
				map.put("CELL", cell);
				map.put("CELLR", ncell);
				map.put("CELL_BCCH", bcch);
				map.put("CELLR_BCCH", nbcch);
				map.put("CELL_FREQ", bcch);
				map.put("CELLR_FREQ", bcchR);
				map.put("DIR", "MUTAUL");
				map.put("CS", cs);
				map.put("DISTANCE", distance);
				map.put("COMMENT", comment + "邻BCCH");
				result.add(map);
			}
			if(ntchList.contains(bcchL)) {
				map = new HashMap<String, Object>();
				map.put("BSC", bsc);
				map.put("CELL", cell);
				map.put("CELLR", ncell);
				map.put("CELL_BCCH", bcch);
				map.put("CELLR_BCCH", nbcch);
				map.put("CELL_FREQ", bcch);
				map.put("CELLR_FREQ", bcchL);
				map.put("DIR", "MUTAUL");
				map.put("CS", cs);
				map.put("DISTANCE", distance);
				map.put("COMMENT", comment + "邻BCCH");
				result.add(map);
			}
			if(tchList.contains(nbcchR)) {
				map = new HashMap<String, Object>();
				map.put("BSC", bsc);
				map.put("CELL", cell);
				map.put("CELLR", ncell);
				map.put("CELL_BCCH", bcch);
				map.put("CELLR_BCCH", nbcch);
				map.put("CELL_FREQ", nbcchR);
				map.put("CELLR_FREQ", nbcch);
				map.put("DIR", "MUTAUL");
				map.put("CS", cs);
				map.put("DISTANCE", distance);
				map.put("COMMENT", comment + "邻BCCH");
				result.add(map);
			}
			if(tchList.contains(nbcchL)) {
				map = new HashMap<String, Object>();
				map.put("BSC", bsc);
				map.put("CELL", cell);
				map.put("CELLR", ncell);
				map.put("CELL_BCCH", bcch);
				map.put("CELLR_BCCH", nbcch);
				map.put("CELL_FREQ", nbcchL);
				map.put("CELLR_FREQ", nbcch);
				map.put("DIR", "MUTAUL");
				map.put("CS", cs);
				map.put("DISTANCE", distance);
				map.put("COMMENT", comment + "邻BCCH");
				result.add(map);
			}
			//邻TCH
			for (String t : tchList) {
				//过滤tch中的bcch，以免重复
				if(t.equals(bcch)) {
					continue;
				}
				tchR = (Integer.parseInt(t) + 1) + ""; 
				tchL = (Integer.parseInt(t) - 1) + ""; 
				//过滤tch中的邻nbcch，以免重复
				if(tchR.equals(nbcch)) {
					continue;
				}
				if(ntchList.contains(tchR)) {
					map = new HashMap<String, Object>();
					map.put("BSC", bsc);
					map.put("CELL", cell);
					map.put("CELLR", ncell);
					map.put("CELL_BCCH", bcch);
					map.put("CELLR_BCCH", nbcch);
					map.put("CELL_FREQ", t);
					map.put("CELLR_FREQ", tchR);
					map.put("DIR", "MUTAUL");
					map.put("CS", cs);
					map.put("DISTANCE", distance);
					map.put("COMMENT", comment + "邻TCH");
					result.add(map);
				}
				//过滤tch中的邻nbcch，以免重复
				if(tchL.equals(nbcch)) {
					continue;
				}
				if(ntchList.contains(tchL)) {
					map = new HashMap<String, Object>();
					map.put("BSC", bsc);
					map.put("CELL", cell);
					map.put("CELLR", ncell);
					map.put("CELL_BCCH", bcch);
					map.put("CELLR_BCCH", nbcch);
					map.put("CELL_FREQ", t);
					map.put("CELLR_FREQ", tchL);
					map.put("DIR", "MUTAUL");
					map.put("CS", cs);
					map.put("DISTANCE", distance);
					map.put("COMMENT", comment + "邻TCH");
					result.add(map);
				}
			}
		}
		log.info("爱立信小区同邻频检查结果数量："+result.size());
		return result;
	}

	/**
	 * 获取爱立信小区邻区数据检查
	 */
	private List<Map<String, Object>> getEriCellNcellDataCheckResult(
			String bscIdStr, String date, long cityId,Map<String, String> settings) {
		List<Map<String, Object>> result = rno2GCellAnalysisDao
				.getEriCellNcellDataCheckResult(bscIdStr, date, cityId, settings);
		return result;
	}
	
	/**
	 * 获取爱立信小区测量频点漏定义结果
	 */
	private List<Map<String, Object>> getEriCellMeaFreqMomitResult(
			String bscIdStr, String date, long cityId,
			Map<String, String> settings) {
		
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		
		List<Map<String, Object>> res = rno2GCellAnalysisDao
				.getEriCellMeaFreqResult(bscIdStr, date, cityId, settings);
		
		String activeStr = "";
		String idleStr = "";
		String ncellBcchStr = "";
		
		String actives[] = {};
		String idles[] = {};
		String ncellBcchs[] = {};
		
		List<String> activeList = new ArrayList<String>();
		List<String> idleList = new ArrayList<String>();
		
		String leakActiveStr = "";
		String leakActiveComm = "";
		String leakIdleStr = "";
		String leakIdleComm = "";
		String command = "";
		
		Map<String,Object> map = null;
		
		//判断active与idle是否多定义
		for (Map<String, Object> one : res) {
			//初始化
			leakActiveStr = "";
			leakActiveComm = "";
			leakIdleStr = "";
			leakIdleComm = "";
			
			if (one.get("ACTIVE") != null && one.get("IDLE") != null
					&& one.get("NCELL_BCCH") != null) {
				activeStr = one.get("ACTIVE").toString();
				idleStr = one.get("IDLE").toString();
				ncellBcchStr = one.get("NCELL_BCCH").toString();
				actives = activeStr.split(",");
				idles = idleStr.split(",");
				ncellBcchs = ncellBcchStr.split(",");
				//转为list
				activeList = Arrays.asList(actives);
				idleList =  Arrays.asList(idles);
				//判断active是否多定义
				for (String bcch : ncellBcchs) {
					if(!activeList.contains(bcch)) {
						leakActiveStr += bcch + ",";
						leakActiveComm += bcch + "&";
					}
				}
				if(!("").equals(leakActiveStr) && !("").equals(leakActiveComm)) {
					leakActiveStr = leakActiveStr.substring(0,leakActiveStr.length()-1);
					leakActiveComm = leakActiveComm.substring(0,leakActiveComm.length()-1);
					if(one.get("BSC") != null && one.get("CELL") != null) {
						map = new HashMap<String, Object>();
						map.put("BSC", one.get("BSC"));
						map.put("CELL", one.get("CELL"));
						map.put("LISTTYPE", "ACTIVE");
						map.put("LEAK_DEFINE", leakActiveStr);
						command = "RLMFC:CELL="+one.get("CELL").toString()
							+",MBCCHNO="+leakActiveComm+",MRNIC,LISTTYPE=ACTIVE;";
						map.put("COMMAND", command);
						result.add(map);
					}
				}
				//判断idle是否多定义
				for (String bcch : ncellBcchs) {
					if(!idleList.contains(bcch)) {
						leakIdleStr += bcch + ",";
						leakIdleComm += bcch + "&";
					}
				}
				if(!("").equals(leakIdleStr) && !("").equals(leakIdleComm)) {
					leakIdleStr = leakIdleStr.substring(0,leakIdleStr.length()-1);
					leakIdleComm = leakIdleComm.substring(0,leakIdleComm.length()-1);
					if(one.get("BSC") != null && one.get("CELL") != null) {
						map = new HashMap<String, Object>();
						map.put("BSC", one.get("BSC"));
						map.put("CELL", one.get("CELL"));
						map.put("LISTTYPE", "IDLE");
						map.put("LEAK_DEFINE", leakIdleStr);
						command = "RLMFC:CELL="+one.get("CELL").toString()
							+",MBCCHNO="+leakIdleComm+",MRNIC,LISTTYPE=IDLE;";
						map.put("COMMAND", command);
						result.add(map);
					}
				}
			}
		}
		return result;
	}
	/**
	* 
	* @title 获取BA表个数检查结果
	* @param bscIdStr
	* @param date
	* @param cityId
	* @param settings
	* @return
	* @author chao.xj
	* @date 2014-10-29上午10:35:38
	* @company 怡创科技
	* @version 1.2
	*/
	private List<Map<String, Object>> getEriCellBaNumCheckResult(
			String bscIdStr, String date, long cityId,
			Map<String, String> settings) {

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, Object> baMap;
		int maxNum = 20;
		int minNum = 5;
		List<Map<String, Object>> res = rno2GCellAnalysisDao
				.getEriCellBaNumCheckResult(bscIdStr, date, cityId, settings);

		String activeStr = "";
		String idleStr = "";
		String ncellBcchStr = "";
		String bscStr = "";
		String cellStr = "";

		String isCheckBaNum = settings.get("isCheckBaNum") != null ? settings
				.get("isCheckBaNum").toString() : "";
		if (("true").equals(isCheckBaNum)) {
			maxNum = Integer.parseInt(settings.get("MAXNUM"));
			minNum = Integer.parseInt(settings.get("MINNUM"));
		} else {
			//
			maxNum = 20;
			minNum = 5;
		}
		//
		for (Map<String, Object> one : res) {
			if (one.get("ACTIVE") != null && one.get("IDLE") != null) {
				activeStr = one.get("ACTIVE").toString();
				idleStr = one.get("IDLE").toString();
				bscStr = one.get("BSC").toString();
				cellStr = one.get("CELL").toString();
				// 一行变两行
				// active
				if (maxNum < Integer.parseInt(activeStr)
						|| minNum > Integer.parseInt(activeStr)) {

					baMap = new HashMap<String, Object>();
					baMap.put("BSC", bscStr);
					baMap.put("CELL", cellStr);
					baMap.put("LISTTYPE", "ACTIVE");
					baMap.put("NUM", activeStr);
					result.add(baMap);
				}
				// idle
				if (maxNum < Integer.parseInt(idleStr)
						|| minNum > Integer.parseInt(idleStr)) {

					baMap = new HashMap<String, Object>();
					baMap.put("BSC", bscStr);
					baMap.put("CELL", cellStr);
					baMap.put("LISTTYPE", "IDLE");
					baMap.put("NUM", idleStr);
					result.add(baMap);
				}
			}
		}

		return result;
	}
	/**
	 * 
	 * @title 获取爱立信小区TALIM_MAXTA检查结果集
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author chao.xj
	 * @date 2014-10-29下午1:55:51
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getEriCellTalimAndMaxtaCheckResult(
			String bscIdStr, String date, long cityId, Map<String, String> settings){
		return rno2GCellAnalysisDao
				.getEriCellTalimAndMaxtaCheckResult(bscIdStr, date, cityId, settings);
		
	}
	/**
	 * 
	 * @title 通过相同bcchbsic的组合cobsic下有两个或多个label,从而保存CobsicCells对象集合数据
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author chao.xj
	 * @date 2014-10-29下午2:54:18
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<CobsicCells> saveCobsicCellsByCoBsicKeys(
			String bscIdStr, String date, long cityId, Map<String, String> settings){
		
		List<CobsicCells> coblists = new ArrayList<CobsicCells>();
		 List<Map<String, Object>> res=rno2GCellAnalysisDao
				.getEriCellCoBsicCheckResult(bscIdStr, date, cityId, settings);
		 log.info("CoBsicCellslist.size():"+res.size());
		 boolean flag = false;
		 String listbcch ="";
		 String listbsic ="";
		 String label ="";
			for (int i = 0; i < res.size(); i++) {
				Map map = res.get(i);
				listbcch = map.get("BCCH").toString();
				listbsic = map.get("BSIC").toString();
				label = map.get("CELL").toString();
				List a = Arrays.asList(label);
				
				List arrayList = new ArrayList(a);
				CobsicCells onecobsicCell = new CobsicCells();
				onecobsicCell.setBcch(Long.parseLong(listbcch));
				onecobsicCell.setBsic(listbsic);
				onecobsicCell.setCells(arrayList);
				for (int j = 0; j < coblists.size(); j++) {
					
					boolean bcchbool = listbcch.equals(Long.toString(coblists.get(j)
							.getBcch()));
					boolean bsicbool = listbsic.equals(coblists.get(j)
							.getBsic());
					if (bcchbool && bsicbool) {
						coblists.get(j).getCells().add(label);
						flag = true;
						break;
					} else {
						flag = false;
					}
				}
				if (!flag) {
					coblists.add(onecobsicCell);
				}
			}
			log.info("退出saveCobsicCellsByCoBsicKeys　coblists："+coblists);
			return coblists;
	}
	/**
	 * 
	 * @title 获取小区间的距离
	 * @param sourcecell
	 * @param targetcell
	 * @return
	 * @author chao.xj
	 * @date 2014-10-29下午3:12:29
	 * @company 怡创科技
	 * @version 1.2
	 */
	public double getDistanceBetweenTheCells(String sourcecell, String targetcell){
//		List<String> lnglats=rno2GCellAnalysisDao.getLnglatsBySourceCellAndTargetCell(sourcecell, targetcell);
		double dis = 0;
		/*if(lnglats!=null && lnglats.size()!=0){
			String lnglat[]=lnglats.get(0).split(",");
			if(lnglat.length==4){
				dis=LatLngHelper.Distance(Double.parseDouble(lnglat[0]), Double.parseDouble(lnglat[1]), Double.parseDouble(lnglat[2]), Double.parseDouble(lnglat[3]));
			}
		}*/
		//ZHM01B2-S1BXSH4-新世界海滨花园SN4-113.38645,22.07978
//		System.out.println("sourcecell:"+sourcecell+"---------------targetcell:"+targetcell);
		String lng1=sourcecell.substring(sourcecell.lastIndexOf("-")+1).split(",")[0];
		String lat1=sourcecell.substring(sourcecell.lastIndexOf("-")+1).split(",")[1];
		String lng2=targetcell.substring(targetcell.lastIndexOf("-")+1).split(",")[0];
		String lat2=targetcell.substring(targetcell.lastIndexOf("-")+1).split(",")[1];
		
		if(!"空".equals(lng1) && !"空".equals(lng2) ){
			dis=LatLngHelper.Distance(Double.parseDouble(lng1), Double.parseDouble(lat1), Double.parseDouble(lng2), Double.parseDouble(lat2));
		}
		return dis;
	}	
	/**
 * 
 * @title 获取爱立信小区同频同bsic检查结果集
 * @param bscIdStr
 * @param date
 * @param cityId
 * @param settings
 * @return
 * @author chao.xj
 * @date 2014-10-29下午1:55:51
 * @company 怡创科技
 * @version 1.2
 */
public List<Map<String, Object>> getEriCellCoBsicCheckResult(String bscIdStr, String date, long cityId, Map<String, String> settings) {
	// List<String>
	String isCheckCoBsic = settings.get("isCheckCoBsic") != null ? settings
			.get("isCheckCoBsic").toString() : "";
	int distance=15*1000;//默认距离
	double meaDis=0;
	if (("true").equals(isCheckCoBsic)) {
		distance = Integer.parseInt(settings.get("DISTANCE"));
	} 
	List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
	List<CobsicCells> cobsicLists =saveCobsicCellsByCoBsicKeys(bscIdStr, date, cityId, settings);
	
	Map<String, Object> map = new HashMap<String, Object>();
	Map<String, Object> cobsicMap = new HashMap<String, Object>();
	CobsicCells cobsicCells;
	// String interfercells[];
	// System.out.println(cobsicLists);
	// System.out.println(cobsicLists.size());
	List<String> interfercells = new ArrayList();
	List<String> cellLists = null;
	String bcch="";
	String bsic="";
	if (cobsicLists != null && cobsicLists.size() != 0) {
		//最外范围循环 bcch,bsic,list<label>
		for (int k = 0; k < cobsicLists.size(); k++) {
			cellLists = cobsicLists.get(k).getCells();
			bcch=cobsicLists.get(k).getBcch()+"";
			bsic=cobsicLists.get(k).getBsic();
			// System.out.println("cobsicLists.get(0).getCells()="+cellLists.size());
			String labels[] = new String[cellLists.size()];
			//某个bcch,bsic对下的label集合对象以label数组方式存储起来
			for (int i = 0; i < cellLists.size(); i++) {
				labels[i] = cellLists.get(i).toString();
				// System.out.println("labels="+labels[i]);
			}
			//某个bcch,bsic对下的label集合对象循环	
			for (int i = 0; i < labels.length - 1; i++) {
				// 循环比较两两间是否　距离小区15公里
				for (int j = i + 1; j < labels.length; j++) {
					// 判断－距离小区15公里 getDistanceBetweenTheCells
					//改造：由sql计算距离改为由java通过经纬度计算距离
//					meaDis = getDistanceBetweenTheCells(labels[i].split("-")[1], labels[j].split("-")[1]);
					meaDis = getDistanceBetweenTheCells(labels[i], labels[j]);
					// System.out.println("distance="+distance);
					if (meaDis!=0 && meaDis < distance) {
						// (co-bsic距离小于15公里
						if (cobsicMap.containsKey(bcch+","+bsic)) {
							//通过bcch,bsic为key从map中获取已存在的对象集合
							cobsicCells=(CobsicCells)cobsicMap.get(bcch+","+bsic);
							/*cobsicCells.getCells().add(labels[i]);
							cobsicCells.getCells().add(labels[j]);*/
							//获取cobsic拓展的组合对象集合
							List<CobsicCellsExpand> cobsicexpanList=cobsicCells.getCombinedCells();
//							log.info("cobsicCells.getCells():"+cobsicexpanList);
							//新建cobsic拓展对象
							CobsicCellsExpand cellsExpand=new CobsicCellsExpand();
							cellsExpand.setCombinedCell(labels[i].substring(0, labels[i].lastIndexOf("-")) + "," + labels[j].substring(0, labels[j].lastIndexOf("-")));
							cellsExpand.setMeaDis(meaDis);
							cellsExpand.setMml("");
							//为bcch,bsic的所在拓展的对象集合内新增对象
							cobsicexpanList.add(cellsExpand);
							cobsicCells.setCombinedCells(cobsicexpanList);
							cobsicMap.put(bcch+","+bsic, cobsicCells);
						}else {
							cobsicCells=new CobsicCells();
							CobsicCellsExpand cellsExpand=new CobsicCellsExpand();
							cellsExpand.setCombinedCell(labels[i].substring(0, labels[i].lastIndexOf("-")) + "," + labels[j].substring(0, labels[j].lastIndexOf("-")));
							cellsExpand.setMeaDis(meaDis);
							cellsExpand.setMml("");
							cobsicCells.setBcch(Long.parseLong(bcch));
							cobsicCells.setBsic(bsic);
							List<CobsicCellsExpand> list=new ArrayList<CobsicCellsExpand>();
							//String combination="{"+labels[i] + "," + labels[j]+":"+cobsicCells.getCommonNcell()+"}";
							//向拓展cobsic集合中注入数据
							list.add(cellsExpand);
							//设置cobsic集合对象
							cobsicCells.setCombinedCells(list);
							//通过bcch,bsic为key向map中增加cobsic对象集合
							cobsicMap.put(bcch+","+bsic, cobsicCells);
						}
					}
				}
			}
		}
		
	}
	result.add(cobsicMap);
	return result;
}
	/**
 * 
 * @title 获取爱立信小区邻区过多过少检查结果集
 * @param bscIdStr
 * @param date
 * @param cityId
 * @param settings
 * @return
 * @author chao.xj
 * @date 2014-10-29下午1:55:51
 * @company 怡创科技
 * @version 1.2
 */
public List<Map<String, Object>> getEriCellNcellNumCheckResult(String bscIdStr, String date, long cityId, Map<String, String> settings){
	
	return rno2GCellAnalysisDao.getEriCellNcellNumCheckResult(bscIdStr, date, cityId, settings);
}
/**
 * 
 * @title 获取爱立信小区本站邻区漏定义检查结果集
 * @param bscIdStr
 * @param date
 * @param cityId
 * @param settings
 * @return
 * @author chao.xj
 * @date 2014-10-30下午14:55:51
 * @company 怡创科技
 * @version 1.2
 */
public List<Map<String, Object>> getEriCellNcellMomitCheckResult(
		String bscIdStr, String date, long cityId, Map<String, String> settings){
	return rno2GCellAnalysisDao.getEriCellNcellMomitCheckResult(bscIdStr, date, cityId, settings);
}
}
