package com.iscreate.plat.tools.excelhelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.RichTextString;


//注意：单元格的样式要在最后实现，如果在实现的前面设置，样式可能会被覆盖
//生成Excel文件
public class WriteExcel2003ServiceImpl implements WriteExcel2003Service {
	private static int stringValue = 1;
	private static int doubleValue = 2;
	private static int intValue = 3;
	private static int calendarValue = 4;
	private static int richTextStringValue = 5;
	private static int dateValue = 6;
	private HSSFWorkbook wb = null;
	private HSSFSheet sheet = null;
	private String sheetName = "Sheet1";
	private String excelName = null;

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
	 *            输入数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	private void setCellValue(Object cellValue, String excelPath,
			String sheetName, int rowNum, int cellNum, int sign)
			throws Exception {
		// 建立新HSSFWorkbook对象
		File fileIn = new File(excelPath);
		InputStream is = new FileInputStream(fileIn);
		HSSFWorkbook wb = new HSSFWorkbook(is);
		is.close();
		// 建立新sheet对象
		HSSFSheet sheet = (HSSFSheet) wb.getSheet(sheetName);
		if (sheet == null) {
			sheet = (HSSFSheet) wb.createSheet(sheetName);
		}
		// 建立新row（行）对象
		HSSFRow row = sheet.getRow(rowNum);
		if (row == null) {
			row = sheet.createRow(rowNum);
		}
		// 建立新cell（单元格）对象
		HSSFCell cell = row.createCell(cellNum);
		// 输入整型类型
		if (sign == intValue) {
			cell.setCellValue((Integer) cellValue);
		}
		// 输入Double类型
		else if (sign == doubleValue) {
			cell.setCellValue((Double) cellValue);
		}
		// 输入字符类型
		else if (sign == stringValue) {
			cell.setCellValue((String) cellValue);
		}
		// 输入Date类型
		else if (sign == dateValue) {
			HSSFCellStyle cellStyle = wb.createCellStyle();
			// 设置cell样式为定制的日期格式
			cellStyle.setDataFormat(wb.createDataFormat().getFormat(
					"yyyy/mm/dd hh:mm:ss"));
			// 设置cell为日期类型的值
			cell.setCellValue((Date) cellValue);
			// 设置该cell日期的显示格式
			cell.setCellStyle(cellStyle);

		}
		// 输入Calendar类型
		else if (sign == calendarValue) {
			cell.setCellValue((Calendar) cellValue);
		}
		// 输入RichTextString类型
		else if (sign == richTextStringValue) {
			cell.setCellValue((RichTextString) cellValue);
		}
		// 写入数据到excel中
		File fileOut = new File(excelPath);
		FileOutputStream fos = new FileOutputStream(fileOut);
		wb.write(fos);
		fos.close();
	}

	/**
	 * @param fileIn
	 *            excel File
	 * @param sheetName
	 *            excel的Sheet名
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param sign
	 *            输入数据类型状态标识
	 * @param cellValue
	 *            输入数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	private void setCellValue(Object cellValue, File fileIn, String sheetName,
			int rowNum, int cellNum, int sign) throws Exception {
		// 建立新HSSFWorkbook对象
		InputStream is = new FileInputStream(fileIn);
		HSSFWorkbook wb = new HSSFWorkbook(is);
		is.close();
		// 建立新sheet对象
		HSSFSheet sheet = (HSSFSheet) wb.getSheet(sheetName);
		if (sheet == null) {
			sheet = (HSSFSheet) wb.createSheet(sheetName);
		}
		// 建立新row（行）对象
		HSSFRow row = sheet.getRow(rowNum);
		if (row == null) {
			row = sheet.createRow(rowNum);
		}
		// 建立新cell（单元格）对象
		HSSFCell cell = row.createCell(cellNum);
		// 输入整型类型
		if (sign == intValue) {
			cell.setCellValue((Integer) cellValue);
		}
		// 输入Double类型
		else if (sign == doubleValue) {
			cell.setCellValue((Double) cellValue);
		}
		// 输入字符类型
		else if (sign == stringValue) {
			cell.setCellValue((String) cellValue);
		}
		// 输入Date类型
		else if (sign == dateValue) {
			HSSFCellStyle cellStyle = wb.createCellStyle();
			// 设置cell样式为定制的日期格式
			cellStyle.setDataFormat(wb.createDataFormat().getFormat(
					"yyyy/mm/dd hh:mm:ss"));
			// 设置cell为日期类型的值
			cell.setCellValue((Date) cellValue);
			// 设置该cell日期的显示格式
			cell.setCellStyle(cellStyle);
		}
		// 输入Calendar类型
		else if (sign == calendarValue) {
			cell.setCellValue((Calendar) cellValue);
		}
		// 输入RichTextString类型
		else if (sign == richTextStringValue) {
			cell.setCellValue((RichTextString) cellValue);
		}
		// 写入数据到excel中
		File fileOut = fileIn;
		FileOutputStream fos = new FileOutputStream(fileOut);
		wb.write(fos);
		fos.close();
	}

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
			String sheetName, int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, excelPath, sheetName, rowNum, cellNum,
				dateValue);
	}

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
			String sheetName, int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, excelPath, sheetName, rowNum, cellNum,
				stringValue);
	}

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
			String sheetName, int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, excelPath, sheetName, rowNum, cellNum,
				doubleValue);
	}

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
	public void setCellValue(Integer cellValue, String excelPath,
			String sheetName, int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, excelPath, sheetName, rowNum, cellNum,
				intValue);
	}

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
			String sheetName, int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, excelPath, sheetName, rowNum, cellNum,
				calendarValue);
	}

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
			String sheetName, int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, excelPath, sheetName, rowNum, cellNum,
				richTextStringValue);
	}

	/**
	 * @param fileIn
	 *            excel File
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
			int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, fileIn, sheetName, rowNum, cellNum,
				dateValue);
	}

	/**
	 * @param fileIn
	 *            excel File
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
			int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, fileIn, sheetName, rowNum, cellNum,
				stringValue);
	}

	/**
	 * @param fileIn
	 *            excel File
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
			int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, fileIn, sheetName, rowNum, cellNum,
				doubleValue);
	}

	/**
	 * @param fileIn
	 *            excel File
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
			int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, fileIn, sheetName, rowNum, cellNum,
				intValue);
	}

	/**
	 * @param fileIn
	 *            excel File
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
			int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, fileIn, sheetName, rowNum, cellNum,
				calendarValue);
	}

	/**
	 * @param fileIn
	 *            excel File
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
			String sheetName, int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, fileIn, sheetName, rowNum, cellNum,
				richTextStringValue);
	}

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
	 *            输入数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	private void setCellValue(Object cellValue, String excelPath, int sheetNum,
			int rowNum, int cellNum, int sign) throws Exception {
		// 建立新HSSFWorkbook对象
		File fileIn = new File(excelPath);
		InputStream is = new FileInputStream(fileIn);
		HSSFWorkbook wb = new HSSFWorkbook(is);
		is.close();
		// 建立新sheet对象
		HSSFSheet sheet = (HSSFSheet) wb.getSheetAt(sheetNum);
		if (sheet == null) {
			sheet = (HSSFSheet) wb.createSheet(sheetNum + "");
		}
		// 建立新row（行）对象
		HSSFRow row = sheet.getRow(rowNum);
		if (row == null) {
			row = sheet.createRow(rowNum);
		}
		// 建立新cell（单元格）对象
		HSSFCell cell = row.createCell(cellNum);
		// 输入整型类型
		if (sign == intValue) {
			cell.setCellValue((Integer) cellValue);
		}
		// 输入Double类型
		else if (sign == doubleValue) {
			cell.setCellValue((Double) cellValue);
		}
		// 输入字符类型
		else if (sign == stringValue) {
			cell.setCellValue((String) cellValue);
		}
		// 输入Date类型
		else if (sign == dateValue) {
			HSSFCellStyle cellStyle = wb.createCellStyle();
			// 设置cell样式为定制的日期格式
			cellStyle.setDataFormat(wb.createDataFormat().getFormat(
					"yyyy/mm/dd hh:mm:ss"));
			// 设置cell为日期类型的值
			cell.setCellValue((Date) cellValue);
			// 设置该cell日期的显示格式
			cell.setCellStyle(cellStyle);
		}
		// 输入Calendar类型
		else if (sign == calendarValue) {
			cell.setCellValue((Calendar) cellValue);
		}
		// 输入RichTextString类型
		else if (sign == richTextStringValue) {
			cell.setCellValue((RichTextString) cellValue);
		}
		// 写入数据到excel中
		File fileOut = new File(excelPath);
		FileOutputStream fos = new FileOutputStream(fileOut);
		wb.write(fos);
		fos.close();
	}

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
			int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, excelPath, sheetNum, rowNum, cellNum,
				dateValue);
	}

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
			int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, excelPath, sheetNum, rowNum, cellNum,
				stringValue);
	}

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
			int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, excelPath, sheetNum, rowNum, cellNum,
				doubleValue);
	}

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
			int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, excelPath, sheetNum, rowNum, cellNum,
				intValue);
	}

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
			int sheetNum, int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, excelPath, sheetNum, rowNum, cellNum,
				calendarValue);
	}

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
			int sheetNum, int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, excelPath, sheetNum, rowNum, cellNum,
				richTextStringValue);
	}

	/**
	 * @param fileIn
	 *            excel File
	 * @param sheetNum
	 *            excel中第sheetNum张Sheet表(由0开始)
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @param sign
	 *            输入数据类型状态标识
	 * @param cellValue
	 *            输入数据
	 * @author wu_gb 新增日期：2010-11-17
	 */
	private void setCellValue(Object cellValue, File fileIn, int sheetNum,
			int rowNum, int cellNum, int sign) throws Exception {
		// 建立新HSSFWorkbook对象

		InputStream is = new FileInputStream(fileIn);
		HSSFWorkbook wb = new HSSFWorkbook(is);
		is.close();
		// 建立新sheet对象
		HSSFSheet sheet = (HSSFSheet) wb.getSheetAt(sheetNum);
		if (sheet == null) {
			sheet = (HSSFSheet) wb.createSheet(sheetNum + "");
		}
		// 建立新row（行）对象
		HSSFRow row = sheet.getRow(rowNum);
		if (row == null) {
			row = sheet.createRow(rowNum);
		}
		// 建立新cell（单元格）对象
		HSSFCell cell = row.createCell(cellNum);
		// 输入整型类型
		if (sign == intValue) {
			cell.setCellValue((Integer) cellValue);
		}
		// 输入Double类型
		else if (sign == doubleValue) {
			cell.setCellValue((Double) cellValue);
		}
		// 输入字符类型
		else if (sign == stringValue) {
			cell.setCellValue((String) cellValue);
		}
		// 输入Date类型
		else if (sign == dateValue) {
			HSSFCellStyle cellStyle = wb.createCellStyle();
			// 设置cell样式为定制的日期格式
			cellStyle.setDataFormat(wb.createDataFormat().getFormat(
					"yyyy/mm/dd hh:mm:ss"));
			// 设置cell为日期类型的值
			cell.setCellValue((Date) cellValue);
			// 设置该cell日期的显示格式
			cell.setCellStyle(cellStyle);
		}
		// 输入Calendar类型
		else if (sign == calendarValue) {
			cell.setCellValue((Calendar) cellValue);
		}
		// 输入RichTextString类型
		else if (sign == richTextStringValue) {
			cell.setCellValue((RichTextString) cellValue);
		}
		// 写入数据到excel中
		File fileOut = fileIn;
		FileOutputStream fos = new FileOutputStream(fileOut);
		wb.write(fos);
		fos.close();
	}

	/**
	 * @param fileIn
	 *            excel File
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
			int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, fileIn, sheetNum, rowNum, cellNum,
				dateValue);
	}

	/**
	 * @param fileIn
	 *            excel File
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
			int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, fileIn, sheetNum, rowNum, cellNum,
				stringValue);
	}

	/**
	 * @param fileIn
	 *            excel File
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
			int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, fileIn, sheetNum, rowNum, cellNum,
				doubleValue);
	}

	/**
	 * @param fileIn
	 *            excel File
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
			int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, fileIn, sheetNum, rowNum, cellNum,
				intValue);
	}

	/**
	 * @param fileIn
	 *            excel File
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
			int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, fileIn, sheetNum, rowNum, cellNum,
				calendarValue);
	}

	/**
	 * @param fileIn
	 *            excel File
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
			int sheetNum, int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, fileIn, sheetNum, rowNum, cellNum,
				richTextStringValue);
	}

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
	public void setCellType(String excelPath, String sheetName, int rowNum,
			int cellNum, short align, short backgroundColor, short fontColor,
			String fontName, short fontHeightInPoints) throws Exception {
		File fileIn = new File(excelPath);
		InputStream is = new FileInputStream(fileIn);
		HSSFWorkbook wb = new HSSFWorkbook(is);
		is.close();
		HSSFSheet sheet = (HSSFSheet) wb.getSheet(sheetName);
		if (sheet == null) {
			sheet = (HSSFSheet) wb.createSheet(sheetName);
		}
		HSSFRow row = sheet.getRow(rowNum);
		if (row == null) {
			row = sheet.createRow(rowNum);
		}
		HSSFCell cell = row.getCell(cellNum);
		if (cell == null) {
			cell = row.createCell(cellNum);
		}

		HSSFCellStyle cellStyle = wb.createCellStyle();// 单元格的值的格式

		cellStyle.setAlignment(align);// 字体对齐形式

		cellStyle.setFillForegroundColor(backgroundColor);//
		// 单元格颜色，黄色
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//
		// 设置单元格颜色要加上这个方法

		HSSFFont font = wb.createFont();
		font.setColor(fontColor);// 字体颜色
		font.setFontName(fontName);// 字体形状
		font.setFontHeightInPoints(fontHeightInPoints);// 字体大小
		cellStyle.setFont(font);

		// 时间格式
		cellStyle.setDataFormat(HSSFDataFormat
				.getBuiltinFormat("yyyy/MM/dd/ hh:mm:ss"));
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);// 日期居中

		cell.setCellStyle(cellStyle);

		File fileOut = new File(excelPath);
		FileOutputStream fos = new FileOutputStream(fileOut);
		wb.write(fos);
		fos.flush();
		fos.close();
	}

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
			String fontName, short fontHeightInPoints) throws Exception {
		File fileIn = new File(excelPath);
		InputStream is = new FileInputStream(fileIn);
		HSSFWorkbook wb = new HSSFWorkbook(is);
		is.close();
		// 建立新sheet对象
		HSSFSheet sheet = (HSSFSheet) wb.getSheetAt(sheetNum);
		if (sheet == null) {
			sheet = (HSSFSheet) wb.createSheet(sheetNum + "");
		}
		HSSFRow row = sheet.getRow(rowNum);
		if (row == null) {
			row = sheet.createRow(rowNum);
		}
		HSSFCell cell = row.getCell(cellNum);
		if (cell == null) {
			cell = row.createCell(cellNum);
		}

		HSSFCellStyle cellStyle = wb.createCellStyle();// 单元格的值的格式

		cellStyle.setAlignment(align);// 字体对齐形式

		cellStyle.setFillForegroundColor(backgroundColor);//
		// 单元格颜色，黄色
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//
		// 设置单元格颜色要加上这个方法

		HSSFFont font = wb.createFont();
		font.setColor(fontColor);// 字体颜色
		font.setFontName(fontName);// 字体形状
		font.setFontHeightInPoints(fontHeightInPoints);// 字体大小
		cellStyle.setFont(font);

		// 时间格式
		cellStyle.setDataFormat(HSSFDataFormat
				.getBuiltinFormat("yyyy/MM/dd/ hh:mm:ss"));
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);// 日期居中

		cell.setCellStyle(cellStyle);

		File fileOut = new File(excelPath);
		FileOutputStream fos = new FileOutputStream(fileOut);
		wb.write(fos);
		fos.flush();
		fos.close();
	}

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
			String fontName, short fontHeightInPoints) throws Exception {
		InputStream is = new FileInputStream(fileIn);
		HSSFWorkbook wb = new HSSFWorkbook(is);
		is.close();
		HSSFSheet sheet = (HSSFSheet) wb.getSheet(sheetName);
		if (sheet == null) {
			sheet = (HSSFSheet) wb.createSheet(sheetName);
		}
		HSSFRow row = sheet.getRow(rowNum);
		if (row == null) {
			row = sheet.createRow(rowNum);
		}
		HSSFCell cell = row.getCell(cellNum);
		if (cell == null) {
			cell = row.createCell(cellNum);
		}

		HSSFCellStyle cellStyle = wb.createCellStyle();// 单元格的值的格式

		cellStyle.setAlignment(align);// 字体对齐形式

		cellStyle.setFillForegroundColor(backgroundColor);//
		// 单元格颜色，黄色
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//
		// 设置单元格颜色要加上这个方法

		HSSFFont font = wb.createFont();
		font.setColor(fontColor);// 字体颜色
		font.setFontName(fontName);// 字体形状
		font.setFontHeightInPoints(fontHeightInPoints);// 字体大小
		cellStyle.setFont(font);

		// 时间格式
		cellStyle.setDataFormat(HSSFDataFormat
				.getBuiltinFormat("yyyy/MM/dd/ hh:mm:ss"));
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);// 日期居中

		cell.setCellStyle(cellStyle);

		File fileOut = fileIn;
		FileOutputStream fos = new FileOutputStream(fileIn);
		wb.write(fos);
		fos.flush();
		fos.close();
	}

	/**
	 * @param filein
	 *            excel filein
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
	public void setCellType(File fileIn, int sheetNum, int rowNum, int cellNum,
			short align, short backgroundColor, short fontColor,
			String fontName, short fontHeightInPoints) throws Exception {
		InputStream is = new FileInputStream(fileIn);
		HSSFWorkbook wb = new HSSFWorkbook(is);
		is.close();
		// 建立新sheet对象
		HSSFSheet sheet = (HSSFSheet) wb.getSheetAt(sheetNum);
		if (sheet == null) {
			sheet = (HSSFSheet) wb.createSheet(sheetNum + "");
		}
		HSSFRow row = sheet.getRow(rowNum);
		if (row == null) {
			row = sheet.createRow(rowNum);
		}
		HSSFCell cell = row.getCell(cellNum);
		if (cell == null) {
			cell = row.createCell(cellNum);
		}

		HSSFCellStyle cellStyle = wb.createCellStyle();// 单元格的值的格式

		cellStyle.setAlignment(align);// 字体对齐形式

		cellStyle.setFillForegroundColor(backgroundColor);//
		// 单元格颜色，黄色
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//
		// 设置单元格颜色要加上这个方法

		HSSFFont font = wb.createFont();
		font.setColor(fontColor);// 字体颜色
		font.setFontName(fontName);// 字体形状
		font.setFontHeightInPoints(fontHeightInPoints);// 字体大小
		cellStyle.setFont(font);

		// 时间格式
		cellStyle.setDataFormat(wb.createDataFormat().getFormat(
				"yyyy/mm/dd hh:mm:ss"));
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);// 日期居中

		cell.setCellStyle(cellStyle);

		File fileOut = fileIn;
		FileOutputStream fos = new FileOutputStream(fileIn);
		wb.write(fos);
		fos.flush();
		fos.close();
	}

	/**
	 * 
	 * @param biaotou
	 *            表头数据
	 * @param data
	 *            内容
	 * @return ByteArrayInputStream 可以处理的数据类型有int
	 *         ,Integer,Date，double，Double，Float，float，Calendar，RichTextString
	 */
	public ByteArrayInputStream creatExcel2003InputStream(List biaotou,
			List data) {
		ByteArrayOutputStream out = null;
		try {
			HSSFRow row = null;
			HSSFCell cell = null;
			wb = new HSSFWorkbook();
			sheet = (HSSFSheet) wb.createSheet();
			row = sheet.createRow(0);
			for (int i = 0; i < biaotou.size(); i++) {
				// 建立新cell（单元格）对象
				cell = row.createCell(i);
				cell.setCellValue((String) biaotou.get(i));
			}
			Object object = null;
			if (data.size() > 0) {
				object = data.get(0);
			}
			Class cls = object.getClass();
			Field[] fields = cls.getDeclaredFields();
			for (int i = 0; i < data.size(); i++) {
				// 建立新row（行）对象
				row = sheet.createRow(i + 1);
				object = data.get(i);
				cls = object.getClass();
				fields = cls.getDeclaredFields();// 这个类里声明的Field
				for (int j = 0; j < fields.length; j++) {
					cell = row.createCell(j);
					
					String fieldName=fields[j].getName();
					
					Method m = fields[j].getDeclaringClass().getMethod(
							"get" + this.toUpperCase(fieldName));
					if (m.getGenericReturnType().equals(new Date().getClass())) {
						HSSFCellStyle cellStyle = wb.createCellStyle();
						// 设置cell样式为定制的日期格式
						cellStyle.setDataFormat(wb.createDataFormat()
								.getFormat("yyyy/mm/dd hh:mm:ss"));
						// 设置cell为日期类型的值
						cell.setCellValue((Date) m.invoke(object));
						// 设置该cell日期的显示格式
						cell.setCellStyle(cellStyle);
					}
					// 输入整型类型
					else if (m.getGenericReturnType().equals(Integer.class)
							|| m.getGenericReturnType().equals(int.class)) {
						cell.setCellValue((Integer) m.invoke(object));
					}
					// 输入Double类型
					else if (m.getGenericReturnType().equals(double.class)
							|| m.getGenericReturnType().equals(Double.class)) {
						cell.setCellValue((Double) m.invoke(object));
					}
					// 输入Float类型
					else if (m.getGenericReturnType().equals(Float.class)
							|| m.getGenericReturnType().equals(float.class)) {
						BigDecimal b = new BigDecimal(String.valueOf((Float) m
								.invoke(object)));
						double d = b.doubleValue();
						cell.setCellValue(d);
					}

					// 输入字符类型
					else if (m.getGenericReturnType().equals(String.class)) {
						cell.setCellValue((String) m.invoke(object));
					}

					// 输入Calendar类型
					else if (m.getGenericReturnType().equals(Calendar.class)) {
						cell.setCellValue((Calendar) m.invoke(object));
					}
					// 输入RichTextString类型
					else if (m.getGenericReturnType().equals(
							RichTextString.class)) {
						cell.setCellValue((Calendar) m.invoke(object));
					}
					// 输入其他类型
					else {
						cell.setCellValue(m.invoke(object) + "");
					}

				}
			}

			out = new ByteArrayOutputStream();
			wb.write(out);
			out.close();
		} catch (Exception e) {
			return null;
		}
		return new ByteArrayInputStream(out.toByteArray());
	}
	
	
	public ByteArrayInputStream creatExcel2003InputStreamForMap(Map<String,String> columnHead,List data){
		
		ByteArrayOutputStream out = null;
		try {
			HSSFRow row = null;
			HSSFCell cell = null;
			wb = new HSSFWorkbook();
			sheet = (HSSFSheet) wb.createSheet();
			row = sheet.createRow(0);
//			for (int i = 0; i < biaotou.size(); i++) {
//				// 建立新cell（单元格）对象
//				cell = row.createCell(i);
//				cell.setCellValue((String) biaotou.get(i));
//			}
//
//			for (int i = 0; i < data.size(); i++) {
//				// 建立新row（行）对象
//				row = sheet.createRow(i + 1);
//				Map<String,Object> entryMap=(Map<String,Object>)data.get(i);
//				if(entryMap!=null && !entryMap.isEmpty()){
//					int index=0;
//					for(Map.Entry<String, Object> entry:entryMap.entrySet()){
//						HSSFCellStyle cellStyle = wb.createCellStyle();
//						cell = row.createCell(index);
//						// 设置cell为日期类型的值
//						cell.setCellValue(entry.getValue()==null?"":entry.getValue().toString());
//						// 设置该cell日期的显示格式
//						cell.setCellStyle(cellStyle);
////						System.out.println("key："+entry.getKey()+"，value===");
//						index++;
//					}
//				}
//			}
			
			Map<String,Integer> headIndexMap=new HashMap<String,Integer>();
			
			int head_index=0;
			for(Map.Entry<String,String> entry:columnHead.entrySet()){
				cell = row.createCell(head_index);
				cell.setCellValue(entry.getValue());
				headIndexMap.put(entry.getKey(), head_index);
				head_index++;
			}
			
			for (int i = 0; i < data.size(); i++) {
				// 建立新row（行）对象
				row = sheet.createRow(i + 1);
				Map<String,Object> entryMap=(Map<String,Object>)data.get(i);
				if(entryMap!=null && !entryMap.isEmpty()){
					int index=0;
					for(Map.Entry<String, Object> entry:entryMap.entrySet()){
						if(columnHead.containsKey(entry.getKey())){
							
							cell = row.createCell(headIndexMap.get(entry.getKey()));
							
							//设置cell为日期类型的值
							cell.setCellValue(entry.getValue()==null?"":entry.getValue().toString());
							index++;
						}
					}
				}
			}

			out = new ByteArrayOutputStream();
			wb.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return new ByteArrayInputStream(out.toByteArray());
		
	}
	
	
	
	

	/**
	 * 
	 * @param data
	 *            内容
	 * @return ByteArrayInputStream 可以处理的数据类型有int
	 *         ,Integer,Date，double，Double，Float，float，Calendar，RichTextString
	 */
	public ByteArrayInputStream creatExcel2003InputStream(List data) {
		ByteArrayOutputStream out = null;
		System.out.println("data   "  +data.size());
		try {
			HSSFRow row = null;
			HSSFCell cell = null;
			wb = new HSSFWorkbook();
			sheet = (HSSFSheet) wb.createSheet();
			for (int i = 0; i < data.size(); i++) {
				row = sheet.createRow(i);
				List list = (List) data.get(i);
				System.out.println("lisr   "  +list.size());
				for (int j = 0; j < list.size(); j++) {
					System.out.println(list.get(j).getClass()
							+ "   list.get(j).getClass()");
					cell = row.createCell(j);
					if (list.get(j).getClass().equals(new Date().getClass())) {
						HSSFCellStyle cellStyle = wb.createCellStyle();
						// 设置cell样式为定制的日期格式
						cellStyle.setDataFormat(wb.createDataFormat()
								.getFormat("yyyy/mm/dd hh:mm:ss"));
						// 设置cell为日期类型的值
						cell.setCellValue((Date) list.get(j));
						// 设置该cell日期的显示格式
						cell.setCellStyle(cellStyle);
					}
					// 输入整型类型
					else if (list.get(j).getClass().equals(Integer.class)
							|| list.get(j).getClass().equals(int.class)) {
						cell.setCellValue((Integer) list.get(j));
					}
					// 输入Double类型
					else if (list.get(j).getClass().equals(double.class)
							|| list.get(j).getClass().equals(Double.class)) {
						cell.setCellValue((Double) list.get(j));
					}
					// 输入Float类型
					else if (list.get(j).getClass().equals(Float.class)
							|| list.get(j).getClass().equals(float.class)) {
						BigDecimal b = new BigDecimal(String
								.valueOf((Float) list.get(j)));
						double d = b.doubleValue();
						cell.setCellValue(d);
					}

					// 输入字符类型
					else if (list.get(j).getClass().equals(String.class)) {
						cell.setCellValue((String) list.get(j));
					}

					// 输入Calendar类型
					else if (list.get(j).getClass().equals(Calendar.class)) {
						cell.setCellValue((Calendar) list.get(j));
					}
					// 输入RichTextString类型
					else if (list.get(j).getClass()
							.equals(RichTextString.class)) {
						cell.setCellValue((Calendar) list.get(j));
					}
					// 输入其他类型
					else {
						cell.setCellValue(list.get(j) + "");
					}

				}
			}
			out = new ByteArrayOutputStream();
			wb.write(out);
			out.close();
		} catch (Exception e) {
			return null;
		}
		return new ByteArrayInputStream(out.toByteArray());
	}

	private String toUpperCase(String lowerCase) {
		String s = lowerCase.substring(0, 1);
		s = s.toUpperCase();
		s = s + lowerCase.substring(1);
		return s;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String getExcelName() {
		return excelName;
	}

	public void setExcelName(String excelName) {
		this.excelName = excelName;
	}
}
