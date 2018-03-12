package com.iscreate.op.service.rno.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.NumberToTextConverter;


import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.rno.RnoTrafficStaticsDao;
import com.iscreate.op.pojo.rno.RnoCityQuality;
import com.iscreate.op.pojo.rno.RnoCityqulDetail;
import com.iscreate.op.pojo.rno.RnoXlsCell;
import com.iscreate.plat.networkresource.common.service.StructureCommonService;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.iscreate.plat.tools.excelhelper.ExcelService;

/**
 * 导入gsm城市网络质量指标文件数据
 * 
 * @author brightming
 * 
 */
public class GsmCityNetQualityFileParser extends ExcelFileParserBase {

	private static Log log = LogFactory.getLog(GsmCityNetQualityFileParser.class);

	// ---spring 注入----//
	public MemcachedClient memCached;
	public RnoTrafficStaticsDao rnoTrafficStaticsDao;
	public ExcelService excelService;
	public IFileParserManager fileParserManager;
	DateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd");
	/*private static List<String> realTitles = Arrays.asList("Site_No", "cellid",
			"BSC", "基站名", "基房经度", "基房纬度", "天线方向", "系统", "颜色类型",
			"Description");

	private static int titleSize = realTitles.size();*/

	public void setFileParserManager(IFileParserManager fileParserManager) {
		this.fileParserManager = fileParserManager;
		super.setFileParserManager(fileParserManager);
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
		//System.out.println("update:"+update);
		getNetQualityHeader(file);
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
		//System.out.println("total="+total);
		// 从excel解析得到的。如果是不需要持久化的数据，那么这些需要放置在缓存中
		List<RnoCityQuality> allCityQualitysFromExcel = new ArrayList<RnoCityQuality>();
		List<RnoCityqulDetail> allCityQulDetailsFromExcel = new ArrayList<RnoCityqulDetail>();
		
		boolean exists = true;
		List<String> oneData;
		StringBuilder buf = new StringBuilder();

		//RnoCityQuality rnoCityQuality;
		//RnoCityqulDetail rnoCityqulDetail;
		//如果读取合并单元格的excel表的时候开始读取的行是不同的
		boolean fine=true;//2013-12-27 gmh add
		int sucCnt=0,failCnt=0,overWriteCnt=0,omitCnt=0;//2013-12-27 gmh add
		
		for (int i = 3; i < allDatas.size()-1; i++) {
			fileParserManager.updateTokenProgress(token, i/total*0.95f);
			oneData = allDatas.get(i);
			//System.out.println("i="+i);
			List areaidList=rnoTrafficStaticsDao.getAreaIdByName(oneData.get(1).toString());
			Long areaidLong=Long.parseLong(areaidList.get(0).toString());
			
			if (update) {
				//System.out.println(areaidLong.longValue()+oneData.get(2).substring(0, 10));
				
				List listcityqulidexist=rnoTrafficStaticsDao.getCityQulIdByAreaAndDate(areaidLong, oneData.get(2).substring(0, 10));
				//System.out.println(listcityqulidexist.size());
				Long CityQulId=null;
				//System.out.println(listcityqulidexist.size());
				if (listcityqulidexist!=null && listcityqulidexist.size()>0) {
					CityQulId=Long.parseLong(listcityqulidexist.get(0).toString());
				}
				
				//System.out.println(listcityqulidexist.get(0).toString());
				if (listcityqulidexist!=null &&listcityqulidexist.size()>0) {
					//System.out.println("listcityqulidexist>0");
					rnoTrafficStaticsDao.deleteCityQualityById(CityQulId);
					rnoTrafficStaticsDao.deleteCityQualityDetailById(CityQulId);
					//saveCityNetQulAndDetailObeject(oneData, areaidLong);	
				}//else {
					//saveCityNetQulAndDetailObeject(oneData, areaidLong);
				//}
				fine=saveCityNetQulAndDetailObeject(oneData, areaidLong);
				if(fine){
					sucCnt++;
				}else{
					buf.append("第["+(i+1)+"]行处理失败！<br/>");
					failCnt++;
				}
			}else {
				List listcityqulidexist=rnoTrafficStaticsDao.getCityQulIdByAreaAndDate(areaidLong, oneData.get(2).substring(0, 10));
				//System.out.println(listcityqulidexist.size());
				Long CityQulId=null;
				if (listcityqulidexist!=null && listcityqulidexist.size()>0) {
					CityQulId=Long.parseLong(listcityqulidexist.get(0).toString());
				}
				//System.out.println(listcityqulidexist.get(0).toString());
				if (listcityqulidexist!=null &&listcityqulidexist.size()>0) {
					sucCnt++;//2013-12-27 gmh add
				   continue;	
				}else {
					fine=saveCityNetQulAndDetailObeject(oneData, areaidLong);
				}
				//2013-12-27 gmh add
				if(fine){
					sucCnt++;
				}else{
					buf.append("第["+(i+1)+"]行处理失败！<br/>");
					failCnt++;
				}
			}
			//System.out.println("\r\n");
				//System.out.println(areaidList.get(0).toString());
		}
		//2013-12-27 gmh add
		String msg="处理记录："+(sucCnt+failCnt)+"条。成功："+sucCnt+"条，失败："+failCnt+" 条。";
		if(buf.length()>0){
			msg+="详情如下：<br/>"+buf.toString();
		}
		try {
			memCached.set(token, RnoConstant.TimeConstant.TokenTime, msg);
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MemcachedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 标识完成
		boolean ok=fileParserManager.updateTokenProgress(token, 1.0f);// 如果过早的把进度标为100%，但是解析的结果还未传动到cache中，就会导致获取不了解析结果
		// System.out.println("memCached.replace. token="+token+" 结果:"+ok);
		return ok;
	}
	private boolean saveCityNetQulAndDetailObeject(List<String> oneData,
			Long areaidLong) {
		try {
			//生成网络质量指标对象并存储数据库
			RnoCityQuality rnoCityQuality2=new RnoCityQuality();
			rnoCityQuality2.setGrade(oneData.get(0).toString());
			rnoCityQuality2.setAreaId(areaidLong.longValue());
			rnoCityQuality2.setStaticTime(sDateFormat.parse(oneData.get(2)));
			rnoCityQuality2.setScore(Double.valueOf(oneData.get(3)).doubleValue());
			rnoCityQuality2.setCreateTime(sDateFormat.parse(sDateFormat.format(new Date())));
			rnoTrafficStaticsDao.saveRnoCityQuality(rnoCityQuality2);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		for (int j = 4; j < oneData.size(); j++) {
		//生成网络质量指标对象详情并存储数据库
		String indexvalue=oneData.get(j);
		List listMaxCityQulId=rnoTrafficStaticsDao.getMaxCityQulIdFromRNOCityQuality();
		Long MaxCityQulId=Long.parseLong(listMaxCityQulId.get(0).toString());
		RnoCityqulDetail rnoCityqulDetail2=new RnoCityqulDetail();
		String indexClass = oneData.get(2);
		String indexName = list1.get(j).toString();
		Double indexValue = Double.parseDouble(indexvalue);
		//rnoCityQulDetail.setCityquldetailid(cityquldetailid);
		rnoCityqulDetail2.setCityqulId(MaxCityQulId.longValue());
		rnoCityqulDetail2.setIndexClass(list.get(j).toString());
		rnoCityqulDetail2.setIndexName(indexName);
		rnoCityqulDetail2.setIndexValue(indexValue);
		rnoTrafficStaticsDao.saveRnoCityQulDetail(rnoCityqulDetail2);
		}
		return true;
	}


	/**
	 * 判断数据有效性，如果数据有效，返回创建的城市质量指标；否则返回null
	 * 
	 * @param oneData
	 * @param i
	 * @param buf
	 * @return Sep 11, 2013 3:43:54 PM gmh
	 */
	private RnoCityQuality createCityQualityFromExcelLine(List<String> oneData, int i,
			StringBuilder buf) {

		// 字段数据
		//Long cityqulid=null;
		String grade=null;
		Long areaId=null;
		String area=null;
		Double score=null;
		Date staticTime=null;
		Date createTime=null;

		String msg = "";
		// --------开始 检查数据有效性-----------------------------------------------//
		if (oneData == null) {
			msg = "第[" + (i + 1) + "]行错误！数据为空！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		// label
		grade = oneData.get(0);
		if (grade == null || grade.trim().isEmpty()) {
			msg = "第[" + (i + 1) + "]行错误！数据未包含区域类别！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		// name
		area=oneData.get(1);
		areaId=Long.parseLong(oneData.get(1));
		//long aa=areaId.longValue();
		if (areaId == null || Long.toString(areaId.longValue()).trim().isEmpty()) {
			msg = "第[" + (i + 1) + "]行错误！数据未包含区域名！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		//score = Double.parseDouble(oneData.get(3));
		score = Double.valueOf(oneData.get(3));
		//double bb=score.doubleValue();
		if (score == null || Double.toString(score.doubleValue()).trim().isEmpty()) {
			msg = "第[" + (i + 1) + "]行错误！数据未包含得分！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		try {
			staticTime = sDateFormat.parse(oneData.get(2));
			if (staticTime == null || sDateFormat.format(staticTime).trim().isEmpty()) {
				msg = "第[" + (i + 1) + "]行错误！数据未包含指标日期！";
				log.warn(msg);
				buf.append(msg + "<br/>");
				return null;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			createTime = sDateFormat.parse(sDateFormat.format(new Date()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// --------结束 检查数据有效性-----------------------------------------------//
		RnoCityQuality rnoCityQuality = new RnoCityQuality();
		//rnoCityQuality.setCityqulid(cityqulid);
		rnoCityQuality.setGrade(grade);
		rnoCityQuality.setAreaId(areaId.longValue());
		rnoCityQuality.setStaticTime(staticTime);
		rnoCityQuality.setScore(score.doubleValue());
		rnoCityQuality.setCreateTime(createTime);
		return rnoCityQuality;
	}

	/**
	 * 判断数据有效性，如果数据有效，返回创建的城市质量指标详情；否则返回null
	 * 
	 * @param oneData
	 * @param i
	 * @param buf
	 * @return Sep 11, 2013 3:43:54 PM gmh
	 */
	private RnoCityqulDetail createCityQuaDetailFromExcelLine(List<String> oneData, int i,
			StringBuilder buf) {

		// 字段数据
		//Long cityquldetailid=null;
		Long cityqulId=null;
		String indexClass=null;
		String indexName=null;
		Double indexValue=null;

		String msg = "";
		// --------开始 检查数据有效性-----------------------------------------------//
		if (oneData == null) {
			msg = "第[" + (i + 1) + "]行错误！数据为空！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		// name
		indexClass = oneData.get(2);
		if (indexClass == null || indexClass.trim().isEmpty()) {
			msg = "第[" + (i + 1) + "]行错误！数据未包含指标类别！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		indexName = oneData.get(3);
		if (indexName == null || indexName.trim().isEmpty()) {
			msg = "第[" + (i + 1) + "]行错误！数据未包含指标名称！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		indexValue = Double.parseDouble(oneData.get(4));
		if (indexValue == null || Double.toString(indexValue.doubleValue()).trim().isEmpty()) {
			msg = "第[" + (i + 1) + "]行错误！数据未包含得分！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		// --------结束 检查数据有效性-----------------------------------------------//
		RnoCityqulDetail rnoCityQulDetail = new RnoCityqulDetail();
		//rnoCityQulDetail.setCityquldetailid(cityquldetailid);
		rnoCityQulDetail.setCityqulId(cityqulId.longValue());
		rnoCityQulDetail.setIndexClass(indexClass);
		rnoCityQulDetail.setIndexName(indexName);
		rnoCityQulDetail.setIndexValue(indexValue.doubleValue());
		return rnoCityQulDetail;
	}
	//得到前两个表头集合
	List list=new ArrayList();
	List list1=new ArrayList();
	/**
	 * 传入EXCEL文件得到合并后的标题头
	 * @param file
	 */
	public void getNetQualityHeader(File file) {
		Sheet sheet=getSheet(file, 0);
		int sheetMergeCount = sheet.getNumMergedRegions();
		List<CellRangeAddress> listCombineCell=new ArrayList<CellRangeAddress>();
		for(int a = 0 ; a < sheetMergeCount ; a++){
			CellRangeAddress ca = sheet.getMergedRegion(a);
			listCombineCell.add(ca);
			}
			//List list=new ArrayList();
			//List list1=new ArrayList();
			
			for (int i = 0; i < 2; i++) {
				Row row = sheet.getRow(i);
			if (row != null && row.getRowNum()==0) {
				for (int j = 0; j < row.getLastCellNum(); j++) {
					// cell.getCellType是获得cell里面保存的值的type
					Cell cell = row.getCell(j);
					//System.out.println(isCombineCell(listCombineCell, cell, sheet));
					list.add(isCombineCell(listCombineCell, cell, sheet));
				}
				}
			//System.out.println("0行"+list.size());
			//System.out.println(row.getRowNum());
			if (row != null && row.getRowNum()==1) {
				for (int k = 0; k < row.getLastCellNum(); k++) {
					// cell.getCellType是获得cell里面保存的值的type
					Cell cell = row.getCell(k);
					if (k<4) {
						//System.out.println(isCombineCell(listCombineCell, cell, sheet));
						list1.add(isCombineCell(listCombineCell, cell, sheet));
					}else {
						//System.out.println(cell.getStringCellValue());
						list1.add(cell.getStringCellValue());
					}	
			}}
			}
	}
	/**
	 * 返回工作表对象
	 * @param excelPath
	 * @param sheetNum
	 * @return
	 */
	public Sheet getSheet(File excelfile, int sheetNum) {
		ArrayList listRows = new ArrayList();
		InputStream is;
		// 根据输入流创建Workbook对象
		Workbook wb = null;
		try {
			is = new FileInputStream(excelfile);
			wb = WorkbookFactory.create(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Sheet sheet = wb.getSheetAt(sheetNum);
		return sheet;
	}
	/**
	* 判断单元格是否为合并单元格，是的话则将单元格的值返回
	* @param listCombineCell 存放合并单元格的list
	* @param cell 需要判断的单元格
	* @param sheet sheet
	* @return
	*/
	public String isCombineCell(List<CellRangeAddress> listCombineCell,Cell cell,Sheet sheet){
	int firstC = 0;
	int lastC = 0;
	int firstR = 0;
	int lastR = 0;
	String cellValue = null;
	for(CellRangeAddress ca:listCombineCell){
	//获得合并单元格的起始行, 结束行, 起始列, 结束列
	firstC = ca.getFirstColumn();
	lastC = ca.getLastColumn();
	firstR = ca.getFirstRow();
	lastR = ca.getLastRow();
	if(cell.getColumnIndex() <= lastC && cell.getColumnIndex() >= firstC) {
	if(cell.getRowIndex() <= lastR && cell.getRowIndex() >= firstR) {
	Row fRow = sheet.getRow(firstR);
	Cell fCell = fRow.getCell(firstC);
	cellValue = getValue(fCell);
	break;
	}
	}
	else{
	cellValue = "";
	}
	}
	return cellValue;
	}
	public String getValue(Cell cell){
		
		switch (cell.getCellType()){
		case Cell.CELL_TYPE_BLANK:
		return "";
		case Cell.CELL_TYPE_BOOLEAN:
		return String.valueOf(cell.getBooleanCellValue());
		case Cell.CELL_TYPE_ERROR:
		break;
		case Cell.CELL_TYPE_FORMULA:
		return cell.getCellFormula();
		case Cell.CELL_TYPE_NUMERIC:
		if (DateUtil.isCellDateFormatted(cell)) {// 日期
		return sDateFormat.format(cell.getDateCellValue());
		} else {
		return NumberToTextConverter.toText(cell.getNumericCellValue());
		}
		case Cell.CELL_TYPE_STRING:
		return cell.getStringCellValue();
		default :
		return cell.getStringCellValue();
		}
		return cell.getStringCellValue();
		}
	public void setRnoTrafficStaticsDao(RnoTrafficStaticsDao rnoTrafficStaticsDao) {
		this.rnoTrafficStaticsDao = rnoTrafficStaticsDao;
	}
	public void setMemCached(MemcachedClient memCached) {
		this.memCached = memCached;
	}

}

