package com.iscreate.plat.tools.excelhelper;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.iscreate.tools.WriteExcel2007Service;

public class WriteExcel2007ServiceImpl implements WriteExcel2007Service {
	private static int stringValue = 1;
	private static int doubleValue = 2;
	private static int intValue = 3;
	private static int calendarValue = 4;
	private static int richTextStringValue = 5;
	private File fileIn = null;
	private XSSFWorkbook wb = null;
	private File fileOut = null;
	private FileOutputStream fos = null;
	private XSSFSheet sheet = null;
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
		fileIn = new File(excelPath);
		InputStream is = new FileInputStream(fileIn);
		wb = new XSSFWorkbook(is);
		is.close();
		// 建立新sheet对象
		sheet = (XSSFSheet) wb.getSheet(sheetName);
		if (sheet == null) {
			sheet = (XSSFSheet) wb.createSheet(sheetName);
		}
		// 建立新row（行）对象
		XSSFRow row = sheet.getRow(rowNum);
		if (row == null) {
			row = sheet.createRow(rowNum);
		}
		// 建立新cell（单元格）对象
		XSSFCell cell = row.createCell(cellNum);
		// 输入字符类型
		if (sign == stringValue) {
			cell.setCellValue((String) cellValue);
		}
		// 输入Double类型
		else if (sign == doubleValue) {
			cell.setCellValue((Double) cellValue);
		}
		// 输入整型类型
		else if (sign == intValue) {
			cell.setCellValue((Integer) cellValue);
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
		fileOut = new File(excelPath);
		fos = new FileOutputStream(fileOut);
		wb.write(fos);
		fos.flush();
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

	 */
	public void setCellValue(Date cellValue, String excelPath,
			String sheetName, int rowNum, int cellNum) throws Exception {
		// 建立新XSSFWorkbook对象
		fileIn = new File(excelPath);
		InputStream is = new FileInputStream(fileIn);
		wb = new XSSFWorkbook(is);
		is.close();

		sheet = (XSSFSheet) wb.getSheet(sheetName);
		if (sheet == null) {
			sheet = (XSSFSheet) wb.createSheet(sheetName);
		}
		XSSFRow row = sheet.getRow(rowNum);
		if (row == null) {
			row = sheet.createRow(rowNum);
		}
		XSSFCell cell = row.createCell(cellNum);
		// 建立新的cell样式
		XSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置cell样式为定制的日期格式
		CreationHelper createHelper = wb.getCreationHelper();
		cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(
				"yyyy/MM/dd/ hh:mm:ss"));
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);// 日期居中
		cell.setCellValue(cellValue);
		// 设置该cell日期的显示格式
		cell.setCellStyle(cellStyle);

		fileOut = new File(excelPath);
		fos = new FileOutputStream(fileOut);
		wb.write(fos);
		fos.flush();
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
	 *            输入String 类型数据

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

	 */
	public void setCellValue(int cellValue, String excelPath, String sheetName,
			int rowNum, int cellNum) throws Exception {
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

	 */
	public void setCellValue(RichTextString cellValue, String excelPath,
			String sheetName, int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, excelPath, sheetName, rowNum, cellNum,
				richTextStringValue);
	}

	
	/**
	 * @param fileIn
	 *            excel fileIn
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
	private void setCellValue(Object cellValue, File fileIn,
			String sheetName, int rowNum, int cellNum, int sign)
			throws Exception {
		// 建立新HSSFWorkbook对象
	
		InputStream is = new FileInputStream(fileIn);
		wb = new XSSFWorkbook(is);
		is.close();
		// 建立新sheet对象
		sheet = (XSSFSheet) wb.getSheet(sheetName);
		if (sheet == null) {
			sheet = (XSSFSheet) wb.createSheet(sheetName);
		}
		// 建立新row（行）对象
		XSSFRow row = sheet.getRow(rowNum);
		if (row == null) {
			row = sheet.createRow(rowNum);
		}
		// 建立新cell（单元格）对象
		XSSFCell cell = row.createCell(cellNum);
		// 输入字符类型
		if (sign == stringValue) {
			cell.setCellValue((String) cellValue);
		}
		// 输入Double类型
		else if (sign == doubleValue) {
			cell.setCellValue((Double) cellValue);
		}
		// 输入整型类型
		else if (sign == intValue) {
			cell.setCellValue((Integer) cellValue);
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
		fileOut = fileIn;
		fos = new FileOutputStream(fileOut);
		wb.write(fos);
		fos.flush();
		fos.close();
	}

	/**
	 * @param fileIn
	 *            excel fileIn
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

	 */
	public void setCellValue(Date cellValue, File fileIn,
			String sheetName, int rowNum, int cellNum) throws Exception {
		// 建立新XSSFWorkbook对象
	
		InputStream is = new FileInputStream(fileIn);
		wb = new XSSFWorkbook(is);
		is.close();

		sheet = (XSSFSheet) wb.getSheet(sheetName);
		if (sheet == null) {
			sheet = (XSSFSheet) wb.createSheet(sheetName);
		}
		XSSFRow row = sheet.getRow(rowNum);
		if (row == null) {
			row = sheet.createRow(rowNum);
		}
		XSSFCell cell = row.createCell(cellNum);
		// 建立新的cell样式
		XSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置cell样式为定制的日期格式
		CreationHelper createHelper = wb.getCreationHelper();
		cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(
				"yyyy/MM/dd/ hh:mm:ss"));
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);// 日期居中
		cell.setCellValue(cellValue);
		// 设置该cell日期的显示格式
		cell.setCellStyle(cellStyle);

		fileOut = fileIn;
		fos = new FileOutputStream(fileOut);
		wb.write(fos);
		fos.flush();
		fos.close();
	}

	/**
	 * @param fileIn
	 *            excel fileIn
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

	 */
	public void setCellValue(String cellValue, File fileIn,
			String sheetName, int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, fileIn, sheetName, rowNum, cellNum,
				stringValue);
	}

	/**
	 * @param fileIn
	 *            excel fileIn
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

	 */
	public void setCellValue(Double cellValue, File fileIn,
			String sheetName, int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, fileIn, sheetName, rowNum, cellNum,
				doubleValue);
	}

	/**
	 * @param fileIn
	 *            excel fileIn
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

	 */
	public void setCellValue(int cellValue, File fileIn, String sheetName,
			int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, fileIn, sheetName, rowNum, cellNum,
				intValue);
	}

	/**
	 * @param fileIn
	 *            excel fileIn
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

	 */
	public void setCellValue(Calendar cellValue, File fileIn,
			String sheetName, int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, fileIn, sheetName, rowNum, cellNum,
				calendarValue);
	}

	/**
	 * @param fileIn
	 *            excel fileIn
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

	 */
	private void setCellValue(Object cellValue, String excelPath, int sheetNum,
			int rowNum, int cellNum, int sign) throws Exception {
		// 建立新XSSFWorkbook对象
		fileIn = new File(excelPath);
		InputStream is = new FileInputStream(fileIn);
		wb = new XSSFWorkbook(is);
		is.close();
		// 建立新sheet对象
		sheet = (XSSFSheet) wb.getSheetAt(sheetNum);
		if (sheet == null) {
			sheet = (XSSFSheet) wb.createSheet(sheetNum + "");
		}
		// 建立新row（行）对象
		XSSFRow row = sheet.getRow(rowNum);
		if (row == null) {
			row = sheet.createRow(rowNum);
		}
		// 建立新cell（单元格）对象
		XSSFCell cell = row.createCell(cellNum);
		// 输入字符类型
		if (sign == stringValue) {
			cell.setCellValue((String) cellValue);
		}
		// 输入Double类型
		else if (sign == doubleValue) {
			cell.setCellValue((Double) cellValue);
		}
		// 输入整型类型
		else if (sign == intValue) {
			cell.setCellValue((Integer) cellValue);
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
		fileOut = new File(excelPath);
		fos = new FileOutputStream(fileOut);
		wb.write(fos);
		fos.flush();
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

	 */
	public void setCellValue(Date cellValue, String excelPath, int sheetNum,
			int rowNum, int cellNum) throws Exception {
		// 建立新XSSFWorkbook对象
		fileIn = new File(excelPath);
		InputStream is = new FileInputStream(fileIn);
		wb = new XSSFWorkbook(is);
		is.close();
		sheet = (XSSFSheet) wb.getSheetAt(sheetNum);
		if (sheet == null) {
			sheet = (XSSFSheet) wb.createSheet("" + 0);
		}
		XSSFRow row = sheet.getRow(rowNum);
		if (row == null) {
			row = sheet.createRow(rowNum);
		}
		XSSFCell cell = row.createCell(cellNum);
		// 建立新的cell样式
		XSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置cell样式为定制的日期格式
		CreationHelper createHelper = wb.getCreationHelper();
		cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(
				"yyyy/MM/dd/ hh:mm:ss"));
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);// 日期居中
		// 设置cell为日期类型的值
		cell.setCellValue(cellValue);
		// 设置该cell日期的显示格式
		cell.setCellStyle(cellStyle);
		fileOut = new File(excelPath);
		fos = new FileOutputStream(fileOut);
		wb.write(fos);
		fos.flush();
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
	 *            输入String 类型数据

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
	 */
	public void setCellValue(RichTextString cellValue, String excelPath,
			int sheetNum, int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, excelPath, sheetNum, rowNum, cellNum,
				richTextStringValue);
	}
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
	 *            输入数据

	 */
	private void setCellValue(Object cellValue, File fileIn, int sheetNum,
			int rowNum, int cellNum, int sign) throws Exception {
		// 建立新XSSFWorkbook对象
		
		InputStream is = new FileInputStream(fileIn);
		wb = new XSSFWorkbook(is);
		is.close();
		// 建立新sheet对象
		sheet = (XSSFSheet) wb.getSheetAt(sheetNum);
		if (sheet == null) {
			sheet = (XSSFSheet) wb.createSheet(sheetNum + "");
		}
		// 建立新row（行）对象
		XSSFRow row = sheet.getRow(rowNum);
		if (row == null) {
			row = sheet.createRow(rowNum);
		}
		// 建立新cell（单元格）对象
		XSSFCell cell = row.createCell(cellNum);
		// 输入字符类型
		if (sign == stringValue) {
			cell.setCellValue((String) cellValue);
		}
		// 输入Double类型
		else if (sign == doubleValue) {
			cell.setCellValue((Double) cellValue);
		}
		// 输入整型类型
		else if (sign == intValue) {
			cell.setCellValue((Integer) cellValue);
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
		fileOut = fileIn;
		fos = new FileOutputStream(fileOut);
		wb.write(fos);
		fos.flush();
		fos.close();
	}

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

	 */
	public void setCellValue(Date cellValue, File fileIn, int sheetNum,
			int rowNum, int cellNum) throws Exception {
		// 建立新XSSFWorkbook对象
		
		InputStream is = new FileInputStream(fileIn);
		wb = new XSSFWorkbook(is);
		is.close();
		sheet = (XSSFSheet) wb.getSheetAt(sheetNum);
		if (sheet == null) {
			sheet = (XSSFSheet) wb.createSheet("" + 0);
		}
		XSSFRow row = sheet.getRow(rowNum);
		if (row == null) {
			row = sheet.createRow(rowNum);
		}
		XSSFCell cell = row.createCell(cellNum);
		// 建立新的cell样式
		XSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置cell样式为定制的日期格式
		CreationHelper createHelper = wb.getCreationHelper();
		cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(
				"yyyy/MM/dd/ hh:mm:ss"));
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);// 日期居中
		// 设置cell为日期类型的值
		cell.setCellValue(cellValue);
		// 设置该cell日期的显示格式
		cell.setCellStyle(cellStyle);
		fileOut = fileIn;
		fos = new FileOutputStream(fileOut);
		wb.write(fos);
		fos.flush();
		fos.close();
	}

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

	 */
	public void setCellValue(String cellValue, File fileIn, int sheetNum,
			int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, fileIn, sheetNum, rowNum, cellNum,
				stringValue);
	}

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
	 *            输入Double 类型数据

	 */
	public void setCellValue(Double cellValue, File fileIn, int sheetNum,
			int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, fileIn, sheetNum, rowNum, cellNum,
				doubleValue);
	}

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
	 *            输入整型 类型数据

	 */
	public void setCellValue(int cellValue, File fileIn, int sheetNum,
			int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, fileIn, sheetNum, rowNum, cellNum,
				intValue);
	}

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
	 *            输入Calendar 类型数据

	 */
	public void setCellValue(Calendar cellValue, File fileIn,
			int sheetNum, int rowNum, int cellNum) throws Exception {
		this.setCellValue(cellValue, fileIn, sheetNum, rowNum, cellNum,
				calendarValue);
	}

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
	 *            输入RichTextString 类型数据
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
	public void setCellType(String excelPath, String sheetName, int rowNum,
			int cellNum, short align, Color backgroundColor, short fontColor,
			String fontName, Short fontHeightInPoints) throws Exception {
		fileIn = new File(excelPath);
		InputStream is = new FileInputStream(fileIn);
		wb = new XSSFWorkbook(is);
		is.close();
		sheet = (XSSFSheet) wb.getSheet(sheetName);
		if (sheet == null) {
			sheet = (XSSFSheet) wb.createSheet(sheetName);
		}
		XSSFRow row = sheet.getRow(rowNum);
		if (row == null) {
			row = sheet.createRow(rowNum);
		}
		XSSFCell cell = row.getCell(cellNum);
		if (cell == null) {
			cell = row.createCell(cellNum);
		}

		XSSFCellStyle cellStyle = wb.createCellStyle();// 单元格的值的格式

		cellStyle.setAlignment(align);// 字体对齐形式
		XSSFColor xSSFColor = new XSSFColor(backgroundColor);
		cellStyle.setFillForegroundColor(xSSFColor);//
		// 单元格颜色，黄色
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);//
		// 设置单元格颜色要加上这个方法

		XSSFFont font = wb.createFont();
		font.setColor(fontColor);// 字体颜色
		font.setFontName(fontName);// 字体形状
		font.setFontHeightInPoints(fontHeightInPoints);// 字体大小
		cellStyle.setFont(font);

		// 日期格式
		CreationHelper createHelper = wb.getCreationHelper();
		cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(
				"yyyy/MM/dd/ hh:mm:ss"));
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);// 日期居中

		cell.setCellStyle(cellStyle);

		fileOut = new File(excelPath);
		fos = new FileOutputStream(fileOut);
		wb.write(fos);
		fos.flush();
		fos.close();
	}
	
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
	public void setCellType(File fileIn, String sheetName, int rowNum,
			int cellNum, short align, Color backgroundColor, short fontColor,
			String fontName, Short fontHeightInPoints) throws Exception {
		InputStream is = new FileInputStream(fileIn);
		wb = new XSSFWorkbook(is);
		is.close();
		sheet = (XSSFSheet) wb.getSheet(sheetName);
		if (sheet == null) {
			sheet = (XSSFSheet) wb.createSheet(sheetName);
		}
		XSSFRow row = sheet.getRow(rowNum);
		if (row == null) {
			row = sheet.createRow(rowNum);
		}
		XSSFCell cell = row.getCell(cellNum);
		if (cell == null) {
			cell = row.createCell(cellNum);
		}

		XSSFCellStyle cellStyle = wb.createCellStyle();// 单元格的值的格式

		cellStyle.setAlignment(align);// 字体对齐形式
		XSSFColor xSSFColor = new XSSFColor(backgroundColor);
		cellStyle.setFillForegroundColor(xSSFColor);//
		// 单元格颜色，黄色
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);//
		// 设置单元格颜色要加上这个方法

		XSSFFont font = wb.createFont();
		font.setColor(fontColor);// 字体颜色
		font.setFontName(fontName);// 字体形状
		font.setFontHeightInPoints(fontHeightInPoints);// 字体大小
		cellStyle.setFont(font);

		// 日期格式
		CreationHelper createHelper = wb.getCreationHelper();
		cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(
				"yyyy/MM/dd/ hh:mm:ss"));
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);// 日期居中

		cell.setCellStyle(cellStyle);

		fileOut = fileIn;
		fos = new FileOutputStream(fileOut);
		wb.write(fos);
		fos.flush();
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
			String fontName, Short fontHeightInPoints) throws Exception {
		fileIn = new File(excelPath);
		InputStream is = new FileInputStream(fileIn);
		wb = new XSSFWorkbook(is);
		is.close();
		sheet = (XSSFSheet) wb.getSheetAt(sheetNum);
		if (sheet == null) {
			sheet = (XSSFSheet) wb.createSheet("" + 0);
		}
		XSSFRow row = sheet.getRow(rowNum);
		if (row == null) {
			row = sheet.createRow(rowNum);
		}
		XSSFCell cell = row.getCell(cellNum);
		if (cell == null) {
			cell = row.createCell(cellNum);
		}

		XSSFCellStyle cellStyle = wb.createCellStyle();// 单元格的值的格式

		cellStyle.setAlignment(align);// 字体对齐形式
		XSSFColor xSSFColor = new XSSFColor(backgroundColor);
		cellStyle.setFillForegroundColor(xSSFColor);//
		// 单元格颜色，黄色
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);//
		// 设置单元格颜色要加上这个方法

		XSSFFont font = wb.createFont();
		font.setColor(fontColor);// 字体颜色
		font.setFontName(fontName);// 字体形状
		font.setFontHeightInPoints(fontHeightInPoints);// 字体大小
		cellStyle.setFont(font);

		// 日期格式
		CreationHelper createHelper = wb.getCreationHelper();
		cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(
				"yyyy/MM/dd/ hh:mm:ss"));
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);// 日期居中

		cell.setCellStyle(cellStyle);

		fileOut = new File(excelPath);
		fos = new FileOutputStream(fileOut);
		wb.write(fos);
		fos.flush();
		fos.close();
	}
	
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
			String fontName, Short fontHeightInPoints) throws Exception {
		InputStream is = new FileInputStream(fileIn);
		wb = new XSSFWorkbook(is);
		is.close();
		sheet = (XSSFSheet) wb.getSheetAt(sheetNum);
		if (sheet == null) {
			sheet = (XSSFSheet) wb.createSheet("" + 0);
		}
		XSSFRow row = sheet.getRow(rowNum);
		if (row == null) {
			row = sheet.createRow(rowNum);
		}
		XSSFCell cell = row.getCell(cellNum);
		if (cell == null) {
			cell = row.createCell(cellNum);
		}

		XSSFCellStyle cellStyle = wb.createCellStyle();// 单元格的值的格式

		cellStyle.setAlignment(align);// 字体对齐形式
		XSSFColor xSSFColor = new XSSFColor(backgroundColor);
		cellStyle.setFillForegroundColor(xSSFColor);//
		// 单元格颜色，黄色
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);//
		// 设置单元格颜色要加上这个方法

		XSSFFont font = wb.createFont();
		font.setColor(fontColor);// 字体颜色
		font.setFontName(fontName);// 字体形状
		font.setFontHeightInPoints(fontHeightInPoints);// 字体大小
		cellStyle.setFont(font);

		// 日期格式
		CreationHelper createHelper = wb.getCreationHelper();
		cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(
				"yyyy/MM/dd/ hh:mm:ss"));
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);// 日期居中

		cell.setCellStyle(cellStyle);

		fileOut = fileIn;
		fos = new FileOutputStream(fileOut);
		wb.write(fos);
		fos.flush();
		fos.close();
	}
	
	
	/**
	 * 
	 * @param biaotou 表头内容
	 * @param data    数据
	 * @return ByteArrayInputStream
	 */
	public ByteArrayInputStream creatExcel2007InputStream(List biaotou,
			List data) {
		ByteArrayOutputStream out =null;
		try {
			XSSFRow row = null;
			XSSFCell cell = null;
			wb = new XSSFWorkbook();
			sheet = (XSSFSheet) wb.createSheet();
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
					Method m = fields[j].getDeclaringClass().getMethod(
							"get" + this.toUpperCase(fields[j].getName()));
					if (m.getGenericReturnType().equals(new Date().getClass())) {
						// 建立新的cell样式
						XSSFCellStyle cellStyle = wb.createCellStyle();
						// 设置cell样式为定制的日期格式
						CreationHelper createHelper = wb.getCreationHelper();
						cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(
								"yyyy/MM/dd/ hh:mm:ss"));
						cellStyle.setAlignment(CellStyle.ALIGN_CENTER);// 日期居中
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

	private String toUpperCase(String lowerCase) {
		String s = lowerCase.substring(0, 1);
		s = s.toUpperCase();
		s = s + lowerCase.substring(1);
		return s;
	}

}
