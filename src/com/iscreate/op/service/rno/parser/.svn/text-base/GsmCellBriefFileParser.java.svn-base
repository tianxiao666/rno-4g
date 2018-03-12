package com.iscreate.op.service.rno.parser;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.rno.RnoBscDao;
import com.iscreate.op.dao.rno.RnoCellDao;
import com.iscreate.op.pojo.rno.Cell;
import com.iscreate.op.pojo.rno.RnoBsc;
import com.iscreate.op.pojo.rno.RnoXlsCell;
import com.iscreate.op.service.rno.tool.CoordinateHelper;
import com.iscreate.plat.networkresource.common.service.StructureCommonService;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.iscreate.plat.tools.excelhelper.ExcelService;

/**
 * 导入gsm小区简明概述数据
 * 
 * @author brightming
 * 
 */
public class GsmCellBriefFileParser extends ExcelFileParserBase {

	private static Log log = LogFactory.getLog(GsmCellBriefFileParser.class);

	// ---spring 注入----//
	public MemcachedClient memCached;
	public ExcelService excelService;
	public IFileParserManager fileParserManager;
	
	private static List<String> realTitles = Arrays.asList("Site_No", "cellid",
			"BSC", "基站名", "基房经度", "基房纬度", "天线方向", "系统", "颜色类型",
			"Description");

	private static int titleSize = realTitles.size();

	public void setFileParserManager(IFileParserManager fileParserManager) {
		this.fileParserManager = fileParserManager;
		super.setFileParserManager(fileParserManager);
	}

	public void setMemCached(MemcachedClient memCached) {
		this.memCached = memCached;
		super.setMemCached(memCached);
		//System.out.println(memCached);
	}

	public void setExcelService(ExcelService excelService) {
		this.excelService = excelService;
	}

	@Override
	protected boolean parseDataInternal(String token, File file,
			boolean needPersist, boolean update, long oldConfigId, long areaId,boolean autoload,Map<String,Object> attachParams) {
		log.debug("进入CellFileParser方法：parseDataInternal。 token=" + token
				+ ",file=" + file + ",needPersist=" + needPersist + ",update="
				+ update + ",oldConfigId=" + oldConfigId + ",areaId=" + areaId);
		// Date expiry = new Date(System.currentTimeMillis() + 1 * 60 * 60);//
		// 1h

		
		// 获取全部数据
		List<List<String>> allDatas = excelService.getListStringRows(file, 0);

		if (allDatas == null || allDatas.size() < 1) {
			try {
				memCached.set(token, RnoConstant.TimeConstant.TokenTime,
						"文件解析失败！因为文件是空的");
			} catch (TimeoutException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (MemcachedException e) {
				e.printStackTrace();
			}
			return false;
		}

		int total = allDatas.size() - 1;// excel有效记录数

		// 从excel解析得到的。如果是不需要持久化的数据，那么这些需要放置在缓存中
		List<RnoXlsCell> allCellsFromExcel = new ArrayList<RnoXlsCell>();

		
		boolean exists = true;
		List<String> oneData;
		StringBuilder buf = new StringBuilder();

		RnoXlsCell oneCell;
		for (int i = 1; i < allDatas.size(); i++) {
			oneData = allDatas.get(i);
			// 产生小区对象
			oneCell = createCellFromExcelLine(oneData, i, buf);
			if (oneCell == null) {
				// 数据不符合要求
				continue;
			}
			allCellsFromExcel.add(oneCell);
		}
		try {
			//System.out.println(memCached);
			memCached.set(token, RnoConstant.TimeConstant.TokenTime, allCellsFromExcel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 标识完成
		boolean ok=fileParserManager.updateTokenProgress(token, 1.0f);// 如果过早的把进度标为100%，但是解析的结果还未传动到cache中，就会导致获取不了解析结果
		// System.out.println("memCached.replace. token="+token+" 结果:"+ok);
		return ok;
	}

	/**
	 * 判断数据有效性，如果数据有效，返回创建的小区；否则返回null
	 * 
	 * @param oneData
	 * @param i
	 * @param buf
	 * @return Sep 11, 2013 3:43:54 PM gmh
	 */
	private RnoXlsCell createCellFromExcelLine(List<String> oneData, int i,
			StringBuilder buf) {

		// 字段数据
		String siteno=null;
		String cellid=null;
		String bsc=null;
		String btsname=null;
		String btslng=null;
		String btslat=null;
		String antbearing=null;
		String system=null;
		String colortype=null;
		String description=null;

		String msg = "";
		// --------开始 检查数据有效性-----------------------------------------------//
		if (oneData == null) {
			msg = "第[" + (i + 1) + "]行错误！数据为空！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		if (oneData.size() < titleSize) {
			msg = "第[" + (i + 1) + "]行错误！数据不齐全！";
			buf.append(msg + "<br/>");
			return null;
		}
		// 检查数据有效性
		siteno = oneData.get(0);
		if (siteno == null || siteno.trim().isEmpty()) {
			msg = "第[" + (i + 1) + "]行错误！数据未包含站点编号！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		// label
		cellid = oneData.get(1);
		if (cellid == null || cellid.trim().isEmpty()) {
			msg = "第[" + (i + 1) + "]行错误！数据未包含小区ID！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		// name
		bsc = oneData.get(2);
		if (bsc == null || bsc.trim().isEmpty()) {
			msg = "第[" + (i + 1) + "]行错误！数据未包含bsc名！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		// "LAC", "CI", "ARFCN", "BSIC", "NON_BCCH", "天线方向",
		// lac
		try {
			btsname = oneData.get(3);
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的基站名！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		// ci
		try {
			btslng = oneData.get(4);
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的基站经度！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		// bcch
		try {
			btslat = oneData.get(5);
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的基站纬度！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		// bsic
		try {
			antbearing = oneData.get(6);
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的天线方位！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		// tch不检查
		system = oneData.get(7);
		// 天线方向
		try {
			colortype = oneData.get(8);
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的颜色类型！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		// "天线下倾", "基站类型", "天线高度", "天线类型", "LON", "LAT", "覆盖范围"
		
			description = oneData.get(9);
		// --------结束 检查数据有效性-----------------------------------------------//


		// }

		RnoXlsCell oneCell = new RnoXlsCell();
		oneCell.setSiteno(siteno);
		oneCell.setCellid(cellid);
		oneCell.setBsc(bsc);
		oneCell.setBtsname(btsname);
		oneCell.setBtslng(btslng);
		oneCell.setBtslat(btslat);
		oneCell.setAntbearing(antbearing);
		oneCell.setSystem(system);
		oneCell.setColortype(colortype);
		String interString="<table width=\"100%\" border=\"0\" bgcolor=\"#009999\" style=\"font-size:12px\">"+
  "<tr>"+
  "  <th colspan=\"2\" align=\"center\" style=\"font-weight:bold\"><b>"+description+"</b></th>"+
  "</tr>"+
  "<tr>"+
  "  <td align=\"right\"  nowrap=\"nowrap\">基站名：</td>"+
  "  <td width=\"135px\" align=\"left\">"+btsname+"</td>"+
  "</tr>"+
  "<tr>"+
  "  <td align=\"right\" nowrap=\"nowrap\">站点编号：</td>"+
  "  <td align=\"left\">"+siteno+"</td>"+
  "</tr>"+
  "<tr>"+
  "  <td align=\"right\" nowrap=\"nowrap\">小区ID：</td>"+
  "  <td align=\"left\">"+cellid+"</td>"+
  "</tr>"+
  "<tr>"+
  "  <td align=\"right\" nowrap=\"nowrap\">归属BSC：</td>"+
  "  <td align=\"left\">"+bsc+"</td>"+
  "</tr>"+
  "<tr>"+
  "  <td align=\"right\" nowrap=\"nowrap\">经度：</td>"+
  "  <td align=\"left\">"+btslng+"</td>"+
  "</tr>"+
  "<tr>"+
  "  <td align=\"right\" nowrap=\"nowrap\">纬度：</td>"+
  "  <td align=\"left\">"+btslat+"</td>"+
  "</tr>"+
  "<tr>"+
  "  <td align=\"right\" nowrap=\"nowrap\">天线方向：</td>"+
  "  <td align=\"left\">"+antbearing+"</td>"+
  "</tr>"+
  "<tr>"+
  "  <td align=\"right\" nowrap=\"nowrap\">颜色类型：</td>"+
  "  <td align=\"left\">"+colortype+"</td>"+
  "</tr>"+
  "</table>";
		oneCell.setDescription(interString);
		return oneCell;
	}

}
