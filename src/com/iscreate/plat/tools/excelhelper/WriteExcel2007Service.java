package com.iscreate.plat.tools.excelhelper;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.RichTextString;

public interface WriteExcel2007Service {
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
			String sheetName, int rowNum, int cellNum) throws Exception ;

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
			String sheetName, int rowNum, int cellNum) throws Exception ;

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
			String sheetName, int rowNum, int cellNum) throws Exception ;

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
	public void setCellValue(int cellValue, String excelPath, String sheetName,
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
			String sheetName, int rowNum, int cellNum)throws Exception;


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
			int rowNum, int cellNum) throws Exception ;

	/**
	 * @param fileIn
	 *           excel fileIn
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
	public void setCellValue(Date cellValue, File fileIn,
			String sheetName, int rowNum, int cellNum) throws Exception ;

	/**
	 * @param fileIn
	 *           excel fileIn
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
	public void setCellValue(String cellValue, File fileIn,
			String sheetName, int rowNum, int cellNum) throws Exception ;

	/**
	 * @param fileIn
	 *           excel fileIn
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
	public void setCellValue(Double cellValue, File fileIn,
			String sheetName, int rowNum, int cellNum) throws Exception ;

	/**
	 * @param fileIn
	 *           excel fileIn
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
	 *           excel fileIn
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
	public void setCellValue(Calendar cellValue, File fileIn,
			String sheetName, int rowNum, int cellNum) throws Exception;

	/**
	 * @param fileIn
	 *           excel fileIn
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
			String sheetName, int rowNum, int cellNum)throws Exception;


	/**
	 * @param fileIn
	 *           excel fileIn
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
	 *           excel fileIn
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
			int rowNum, int cellNum) throws Exception ;
	
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
	 *            输入RichTextString 类型数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	public void setCellValue(RichTextString cellValue, String excelPath,
			int sheetNum, int rowNum, int cellNum) throws Exception;

	/**
	 * @param fileIn
	 *            excel fileIn
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
	 *            excel fileIn
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
	 *            excel fileIn
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
	 * @param sheetName
	 *            excel的Sheet名
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param align
	 *            字体对齐方式
	 *@param backgroundColor
	 *            单元格背景颜色
	 *@param fontColor
	 *            字体颜色
	 *@param fontName
	 *            字体形状
	 *@param fontHeightInPoints
	 *            字体大小
	 * @author wu_gb 新增日期：2010-11-17
	 * @throws Exception
	 */
	public void setCellType(String excelPath, String sheetName, int rowNum,
			int cellNum, short align, Color backgroundColor, short fontColor,
			String fontName, Short fontHeightInPoints) throws Exception;
	/**
	 * @param fileIn
	 *            excel fileIn
	 * @param sheetName
	 *            excel的Sheet名
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param align
	 *            字体对齐方式
	 *@param backgroundColor
	 *            单元格背景颜色
	 *@param fontColor
	 *            字体颜色
	 *@param fontName
	 *            字体形状
	 *@param fontHeightInPoints
	 *            字体大小
	 * 
	 * @throws Exception
	 */
	public void setCellType(File fileIn, int sheetNum, int rowNum,
			int cellNum, short align, Color backgroundColor, short fontColor,
			String fontName, Short fontHeightInPoints) throws Exception ;
		
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
		 *@param backgroundColor
		 *            单元格背景颜色
		 *@param fontColor
		 *            字体颜色
		 *@param fontName
		 *            字体形状
		 *@param fontHeightInPoints
		 *            字体大小
		 * 
		 * @throws Exception
		 */
		public void setCellType(String excelPath, int sheetNum, int rowNum,
				int cellNum, short align, Color backgroundColor, short fontColor,
				String fontName, Short fontHeightInPoints) throws Exception ;
		
	/**
	 * 
	 * @param biaotou 表头内容
	 * @param data    数据
	 * @return ByteArrayInputStream
	 */
	public ByteArrayInputStream creatExcel2007InputStream(List biaotou,
			List data);


}
