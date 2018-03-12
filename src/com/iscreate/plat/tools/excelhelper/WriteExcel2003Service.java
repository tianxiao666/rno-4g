package com.iscreate.plat.tools.excelhelper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.RichTextString;

public interface WriteExcel2003Service {

	/**
	 * @param excelPath
	 *            excel的路径+文件名，如c:\\workbook.xls
	 * @param sheetName
	 *            excel的Sheet名
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param sign
	 *            输入数据类型状态标识
	 * @param cellValue
	 *            输入Date 类型数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	public void setCellValue(Date cellValue, String excelPath,
			String sheetName, int rowNum, int cellNum) throws Exception;

	/**
	 * @param excelPath
	 *            excel的路径+文件名，如c:\\workbook.xls
	 * @param sheetName
	 *            excel的Sheet名
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param sign
	 *            输入数据类型状态标识
	 * @param cellValue
	 *            输入String 类型数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	public void setCellValue(String cellValue, String excelPath,
			String sheetName, int rowNum, int cellNum) throws Exception;

	/**
	 * @param excelPath
	 *            excel的路径+文件名，如c:\\workbook.xls
	 * @param sheetName
	 *            excel的Sheet名
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param sign
	 *            输入数据类型状态标识
	 * @param cellValue
	 *            输入Double 类型数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	public void setCellValue(Double cellValue, String excelPath,
			String sheetName, int rowNum, int cellNum) throws Exception;

	/**
	 * @param excelPath
	 *            excel的路径+文件名，如c:\\workbook.xls
	 * @param sheetName
	 *            excel的Sheet名
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param sign
	 *            输入数据类型状态标识
	 * @param cellValue
	 *            输入整型 类型数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	public void setCellValue(Integer cellValue, String excelPath, String sheetName,
			int rowNum, int cellNum) throws Exception;

	/**
	 * @param excelPath
	 *            excel的路径+文件名，如c:\\workbook.xls
	 * @param sheetName
	 *            excel的Sheet名
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param sign
	 *            输入数据类型状态标识
	 * @param cellValue
	 *            输入Calendar 类型数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	public void setCellValue(Calendar cellValue, String excelPath,
			String sheetName, int rowNum, int cellNum) throws Exception;

	/**
	 * @param excelPath
	 *            excel的路径+文件名，如c:\\workbook.xls
	 * @param sheetName
	 *            excel的Sheet名
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param sign
	 *            输入数据类型状态标识
	 * @param cellValue
	 *            输入RichTextString 类型数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	public void setCellValue(RichTextString cellValue, String excelPath,
			String sheetName, int rowNum, int cellNum) throws Exception;

	/**
	 * @param excelPath
	 *            excel的路径+文件名，如c:\\workbook.xls
	 * @param sheetNum
	 *            excel中第sheetNum张Sheet表(由0开始)
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param sign
	 *            输入数据类型状态标识
	 * @param cellValue
	 *            输入Date 类型数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	public void setCellValue(Date cellValue, String excelPath, int sheetNum,
			int rowNum, int cellNum) throws Exception;

	/**
	 * @param excelPath
	 *            excel的路径+文件名，如c:\\workbook.xls
	 * @param sheetNum
	 *            excel中第sheetNum张Sheet表(由0开始)
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param sign
	 *            输入数据类型状态标识
	 * @param cellValue
	 *            输入String 类型数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	public void setCellValue(String cellValue, String excelPath, int sheetNum,
			int rowNum, int cellNum) throws Exception;

	/**
	 * @param excelPath
	 *            excel的路径+文件名，如c:\\workbook.xls
	 * @param sheetNum
	 *            excel中第sheetNum张Sheet表(由0开始)
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param sign
	 *            输入数据类型状态标识
	 * @param cellValue
	 *            输入Double 类型数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	public void setCellValue(Double cellValue, String excelPath, int sheetNum,
			int rowNum, int cellNum) throws Exception;

	/**
	 * @param excelPath
	 *            excel的路径+文件名，如c:\\workbook.xls
	 * @param sheetNum
	 *            excel中第sheetNum张Sheet表(由0开始)
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param sign
	 *            输入数据类型状态标识
	 * @param cellValue
	 *            输入整型 类型数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	public void setCellValue(int cellValue, String excelPath, int sheetNum,
			int rowNum, int cellNum) throws Exception;

	/**
	 * @param excelPath
	 *            excel的路径+文件名，如c:\\workbook.xls
	 * @param sheetNum
	 *            excel中第sheetNum张Sheet表(由0开始)
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param sign
	 *            输入数据类型状态标识
	 * @param cellValue
	 *            输入Calendar 类型数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	public void setCellValue(Calendar cellValue, String excelPath,
			int sheetNum, int rowNum, int cellNum) throws Exception;

	/**
	 * @param excelPath
	 *            excel的路径+文件名，如c:\\workbook.xls
	 * @param sheetNum
	 *            excel中第sheetNum张Sheet表(由0开始)
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param sign
	 *            输入数据类型状态标识
	 * @param cellValue
	 *            输入RichTextString 类型数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	public void setCellValue(RichTextString cellValue, String excelPath,
			int sheetNum, int rowNum, int cellNum) throws Exception;

	/**
	 * @param fileIn
	 *            excel 文件
	 * @param sheetName
	 *            excel的Sheet名
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param sign
	 *            输入数据类型状态标识
	 * @param cellValue
	 *            输入Date 类型数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	public void setCellValue(Date cellValue, File fileIn, String sheetName,
			int rowNum, int cellNum) throws Exception;

	/**
	 * @param fileIn
	 *            excel 文件
	 * @param sheetName
	 *            excel的Sheet名
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param sign
	 *            输入数据类型状态标识
	 * @param cellValue
	 *            输入String 类型数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	public void setCellValue(String cellValue, File fileIn, String sheetName,
			int rowNum, int cellNum) throws Exception;

	/**
	 * @param fileIn
	 *            excel 文件
	 * @param sheetName
	 *            excel的Sheet名
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param sign
	 *            输入数据类型状态标识
	 * @param cellValue
	 *            输入Double 类型数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	public void setCellValue(Double cellValue, File fileIn, String sheetName,
			int rowNum, int cellNum) throws Exception;

	/**
	 * @param fileIn
	 *            excel 文件
	 * @param sheetName
	 *            excel的Sheet名
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param sign
	 *            输入数据类型状态标识
	 * @param cellValue
	 *            输入整型 类型数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	public void setCellValue(int cellValue, File fileIn, String sheetName,
			int rowNum, int cellNum) throws Exception;

	/**
	 * @param fileIn
	 *            excel 文件
	 * @param sheetName
	 *            excel的Sheet名
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param sign
	 *            输入数据类型状态标识
	 * @param cellValue
	 *            输入Calendar 类型数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	public void setCellValue(Calendar cellValue, File fileIn, String sheetName,
			int rowNum, int cellNum) throws Exception;

	/**
	 * @param fileIn
	 *            excel 文件
	 * @param sheetName
	 *            excel的Sheet名
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param sign
	 *            输入数据类型状态标识
	 * @param cellValue
	 *            输入RichTextString 类型数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	public void setCellValue(RichTextString cellValue, File fileIn,
			String sheetName, int rowNum, int cellNum) throws Exception;

	/**
	 * @param fileIn
	 *            excel 文件
	 * @param sheetNum
	 *            excel中第sheetNum张Sheet表(由0开始)
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param sign
	 *            输入数据类型状态标识
	 * @param cellValue
	 *            输入Date 类型数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	public void setCellValue(Date cellValue, File fileIn, int sheetNum,
			int rowNum, int cellNum) throws Exception;

	/**
	 * @param fileIn
	 *            excel 文件
	 * @param sheetNum
	 *            excel中第sheetNum张Sheet表(由0开始)
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param sign
	 *            输入数据类型状态标识
	 * @param cellValue
	 *            输入String 类型数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	public void setCellValue(String cellValue, File fileIn, int sheetNum,
			int rowNum, int cellNum) throws Exception;

	/**
	 * @param fileIn
	 *            excel 文件
	 * @param sheetNum
	 *            excel中第sheetNum张Sheet表(由0开始)
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param sign
	 *            输入数据类型状态标识
	 * @param cellValue
	 *            输入Double 类型数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	public void setCellValue(Double cellValue, File fileIn, int sheetNum,
			int rowNum, int cellNum) throws Exception;

	/**
	 * @param fileIn
	 *            excel 文件
	 * @param sheetNum
	 *            excel中第sheetNum张Sheet表(由0开始)
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param sign
	 *            输入数据类型状态标识
	 * @param cellValue
	 *            输入整型 类型数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	public void setCellValue(int cellValue, File fileIn, int sheetNum,
			int rowNum, int cellNum) throws Exception;

	/**
	 * @param fileIn
	 *            excel 文件
	 * @param sheetNum
	 *            excel中第sheetNum张Sheet表(由0开始)
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param sign
	 *            输入数据类型状态标识
	 * @param cellValue
	 *            输入Calendar 类型数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	public void setCellValue(Calendar cellValue, File fileIn, int sheetNum,
			int rowNum, int cellNum) throws Exception;

	/**
	 * @param fileIn
	 *            excel 文件
	 * @param sheetNum
	 *            excel中第sheetNum张Sheet表(由0开始)
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param sign
	 *            输入数据类型状态标识
	 * @param cellValue
	 *            输入RichTextString 类型数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	public void setCellValue(RichTextString cellValue, File fileIn,
			int sheetNum, int rowNum, int cellNum) throws Exception;

	/**
	 * @param excelPath
	 *            excel的路径+文件名，如c:\\workbook.xls
	 * @param sheetNum
	 *            excel的第sheetNum个表
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param align
	 *            字体对齐方式
	 * @param backgroundColor
	 *            单元格背景颜色
	 * @param fontColor
	 *            字体颜色
	 * @param fontName
	 *            字体形状
	 * @param fontHeightInPoints
	 *            字体大小
	 * @author wu_gb 新增日期：2010-11-17
	 * @throws Exception
	 */
	public void setCellType(String excelPath, int sheetNum, int rowNum,
			int cellNum, short align, short backgroundColor, short fontColor,
			String fontName, short fontHeightInPoints) throws Exception;
	
	/**
	 * @param excelPath
	 *            excel的路径+文件名，如c:\\workbook.xls
	 * @param sheetName
	 *            excel的Sheet名
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param align
	 *            字体对齐方式
	 * @param backgroundColor
	 *            单元格背景颜色 字体颜色
	 * @param fontName
	 *            字体形状
	 * @param fontHeightInPoints
	 *            字体大小
	 * @author wu_gb 新增日期：2010-11-17
	 * @throws Exception
	 */
	public void setCellType(String excelPath, String sheetName, int rowNum,
			int cellNum, short align, short backgroundColor, short fontColor,
			String fontName, short fontHeightInPoints) throws Exception;

	/**
	 * @param filein
	 *            excel filein
	 * @param sheetName
	 *            excel的Sheet名
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param align
	 *            字体对齐方式
	 * @param backgroundColor
	 *            单元格背景颜色
	 * @param fontColor
	 *            字体颜色
	 * @param fontName
	 *            字体形状
	 * @param fontHeightInPoints
	 *            字体大小
	 * @author wu_gb 新增日期：2010-11-17
	 * @throws Exception
	 */
	public void setCellType(File fileIn, int sheetNum, int rowNum, int cellNum,
			short align, short backgroundColor, short fontColor,
			String fontName, short fontHeightInPoints) throws Exception;

	/**
	 * @param filein
	 *            excel filein
	 * @param sheetName
	 *            excel的Sheet名
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param align
	 *            字体对齐方式
	 * @param backgroundColor
	 *            单元格背景颜色
	 * @param fontColor
	 *            字体颜色
	 * @param fontName
	 *            字体形状
	 * @param fontHeightInPoints
	 *            字体大小
	 * @author wu_gb 新增日期：2010-11-17
	 * @throws Exception
	 */
	public void setCellType(File fileIn, String sheetName, int rowNum,
			int cellNum, short align, short backgroundColor, short fontColor,
			String fontName, short fontHeightInPoints) throws Exception;

	/**
	 * 
	 * @param biaotou
	 *            表头数据
	 * @param data
	 *            内容
	 * @return ByteArrayInputStream
	 */
	public ByteArrayInputStream creatExcel2003InputStream(List biaotou,
			List data);
	
	
	
	public ByteArrayInputStream creatExcel2003InputStreamForMap(Map<String,String> columnHead,List data);
	
	/**
	 * 
	 * @param data
	 *            内容
	 * @return ByteArrayInputStream 可以处理的数据类型有int
	 *         ,Integer,Date，double，Double，Float，float，Calendar，RichTextString
	 */
	public ByteArrayInputStream creatExcel2003InputStream(List data);
	
	
	
}
