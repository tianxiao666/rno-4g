package com.iscreate.plat.tools.excelhelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Sheet;

import com.iscreate.tools.ReadExcelService;

public class ExcelServiceImpl implements ExcelService {

	/**
	 * @param sheet
	 *            excel的Sheet对象
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @author wu_gb 新增日期：2010-11-16
	 * @return 在rowNum行cellNum列的值
	 */
	private Object getExcelValue(Sheet sheet, int rowNum, int cellNum) {
		Object value = null;
		Row row = sheet.getRow(rowNum);
		if (row != null) {
			Cell cell = row.getCell(cellNum);
			if (cell == null) {
				value = null;
			} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
				value = cell.getBooleanCellValue();
			} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {

				// 先看是否是日期格式
				if (DateUtil.isCellDateFormatted(cell)) {
					// 读取日期格式
					value = cell.getDateCellValue();
				} else {
					// 读取数字
					value = cell.getNumericCellValue();
				}
			} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				// 读取String
				value = cell.getStringCellValue();
			} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
				// 读取公式
				value = cell.getCellFormula();
			} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
				// 读取空白
				value = "";
			} else {
				value = "";
			}
		}
		return value;
	}

	/**
	 * @param excelPath
	 *            excel 文件路径
	 * @param sheetNum
	 *            excel文件中的第sheetNum个表(sheetNum由0开始)
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @author wu_gb 新增日期：2010-11-16
	 * @return 返回文件excelPath的第sheetNum个表中的第rowNum行cellNum列的值
	 * 
	 */
	public Object getExcelValue(String excelPath, int sheetNum, int rowNum,
			int cellNum) {
		Object value = null;
		File file = new File(excelPath);
		InputStream is;
		// 根据输入流创建Workbook对象
		Workbook wb = null;
		try {
			is = new FileInputStream(file);
			wb = WorkbookFactory.create(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Sheet sheet = (Sheet) wb.getSheetAt(sheetNum);
		value = this.getExcelValue(sheet, rowNum, cellNum);
		return value;
	}

	public Object getExcelValue(File file, int sheetNum, int rowNum, int cellNum) {
		Object value = null;
		InputStream is;
		// 根据输入流创建Workbook对象
		Workbook wb = null;
		try {
			is = new FileInputStream(file);
			wb = WorkbookFactory.create(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Sheet sheet = (Sheet) wb.getSheetAt(sheetNum);
		value = this.getExcelValue(sheet, rowNum, cellNum);
		return value;
	}

	/**
	 * @param file
	 *            excel file
	 * @param sheetNum
	 *            excel文件中的第sheetNum个表(sheetNum由0开始)
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @author wu_gb 新增日期：2010-11-16
	 * @return 返回文件excelPath的第sheetNum个表中的第rowNum行cellNum列的值
	 * 
	 */
	public String getExcelValueToString(File file, int sheetNum, int rowNum,
			int cellNum) {
		String value = null;
		InputStream is;
		// 根据输入流创建Workbook对象
		Workbook wb = null;
		try {
			is = new FileInputStream(file);
			wb = WorkbookFactory.create(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Sheet sheet = (Sheet) wb.getSheetAt(sheetNum);
		Row row = sheet.getRow(rowNum);
		if (row != null) {
			Cell cell = row.getCell(cellNum);
			if (cell == null) {
				value = "";
			} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
				value = cell.getBooleanCellValue() + "";
			} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				// 先看是否是日期格式
				if (DateUtil.isCellDateFormatted(cell)) {
					// 读取日期格式
					DateFormat format = new java.text.SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					value = format.format(cell.getDateCellValue());
				} else {
					// 读取数字
					HSSFCellStyle st = (HSSFCellStyle) wb.createCellStyle();
					// 建立新的cell样式
					DecimalFormat formatCell = (DecimalFormat) NumberFormat
							.getPercentInstance();
					formatCell.applyPattern("##.############");
					double strCell = cell.getNumericCellValue();
					value = formatCell.format(strCell);
				}
			} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				// 读取String
				value = cell.getStringCellValue();
			} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
				// 读取公式
				value = cell.getCellFormula() + "";
			} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
				// 读取空白
				value = "";
			} else {
				value = "";
			}
		}
		return value;
	}

	/**
	 * @param excelPath
	 *            excel 文件路径
	 * @param sheetNum
	 *            excel文件中的第sheetNum个表(sheetNum由0开始)
	 * @param rowNum
	 *            行值
	 * @param cellNum
	 *            列值
	 * @author wu_gb 新增日期：2010-11-16
	 * @return 返回文件excelPath的第sheetNum个表中的第rowNum行cellNum列的值
	 * 
	 */
	public String getExcelValueToString(String excelPath, int sheetNum,
			int rowNum, int cellNum) {
		String value = null;
		File file = new File(excelPath);
		InputStream is;
		// 根据输入流创建Workbook对象
		Workbook wb = null;
		try {
			is = new FileInputStream(file);
			wb = WorkbookFactory.create(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Sheet sheet = (Sheet) wb.getSheetAt(sheetNum);
		Row row = sheet.getRow(rowNum);
		if (row != null) {
			Cell cell = row.getCell(cellNum);
			if (cell == null) {
				value = null;
			} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
				value = cell.getBooleanCellValue() + "";
			} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {

				// 先看是否是日期格式
				if (DateUtil.isCellDateFormatted(cell)) {
					// 读取日期格式
					DateFormat format = new java.text.SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					value = format.format(cell.getDateCellValue()) + "";
				} else {
					// 读取数字
					DecimalFormat formatCell = (DecimalFormat) NumberFormat
							.getPercentInstance();
					formatCell.applyPattern("##.############");
					double strCell = cell.getNumericCellValue();
					value = formatCell.format(strCell);
				}
			} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				// 读取String
				value = cell.getStringCellValue();
			} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
				// 读取公式
				value = cell.getCellFormula() + "";
			} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
				// 读取空白
				value = "";
			} else {
				value = "";
			}
		}
		return value;
	}

	/**
	 * @param excelPath
	 *            excel 文件路径
	 * @param sheetNum
	 *            excel文件中的第sheetNum个表(sheetNum由0开始)
	 * @author wu_gb 新增日期：2010-11-16
	 * @return 返回文件excelPath的第sheetNum个表行的集合
	 */
	public List getListRows(String excelPath, int sheetNum) {

		ArrayList listRows = new ArrayList();
		File file = new File(excelPath);
		InputStream is;
		// 根据输入流创建Workbook对象
		Workbook wb = null;
		try {
			is = new FileInputStream(file);
			wb = WorkbookFactory.create(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Sheet sheet = (Sheet) wb.getSheetAt(sheetNum);
		for (int j = 0; j < sheet.getLastRowNum() + 1; j++) {
			Row row = sheet.getRow(j);
			ArrayList cellsValue = new ArrayList();
			if (row != null) {
				for (int i = 0; i < row.getLastCellNum(); i++) {
					// cell.getCellType是获得cell里面保存的值的type
					Cell cell = row.getCell(i);

					if (cell == null) {
						cellsValue.add("");
					} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
						;
						cellsValue.add(cell.getBooleanCellValue());
					} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						// 先看是否是日期格式
						if (DateUtil.isCellDateFormatted(cell)) {
							// 读取日期格式
							cellsValue.add(cell.getDateCellValue());
						} else {
							// 读取数字
							cellsValue.add(cell.getNumericCellValue());
						}
					} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						// 读取String
						cellsValue.add(cell.getStringCellValue());

					} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
						// 读取公式
						cellsValue.add(cell.getCellFormula());
					} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
						// 读取空白
						cellsValue.add("");
					} else {
						cellsValue.add("");
					}
				}
				listRows.add(cellsValue);
			} else {
				listRows.add(cellsValue);
			}
		}
		return listRows;
	}

	public List getListRows(File excelfile, int sheetNum) {
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
		Sheet sheet = (Sheet) wb.getSheetAt(sheetNum);
		for (int j = 0; j < sheet.getLastRowNum() + 1; j++) {
			Row row = sheet.getRow(j);
			ArrayList cellsValue = new ArrayList();
			if (row != null) {
				for (int i = 0; i < row.getLastCellNum(); i++) {
					// cell.getCellType是获得cell里面保存的值的type
					Cell cell = row.getCell(i);

					if (cell == null) {
						cellsValue.add("");
					} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
						;
						cellsValue.add(cell.getBooleanCellValue());
					} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						// 先看是否是日期格式
						if (DateUtil.isCellDateFormatted(cell)) {
							// 读取日期格式
							cellsValue.add(cell.getDateCellValue());
						} else {
							// 读取数字
							cellsValue.add(cell.getNumericCellValue());
						}
					} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						// 读取String
						cellsValue.add(cell.getStringCellValue());

					} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
						// 读取公式
						cellsValue.add(cell.getCellFormula());
					} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
						// 读取空白
						cellsValue.add("");
					} else {
						cellsValue.add("");
					}
				}
				listRows.add(cellsValue);
			} else {
				listRows.add(cellsValue);
			}
		}
		return listRows;
	}

	/**
	 * @param excelPath
	 *            excelfile 文件
	 * @param sheetName
	 *            excelfile文件中的sheetName
	 * @author wu_gb 新增日期：2010-3-18
	 * @return 返回文件 excelfile文件中的sheetName
	 */
	public List getListRows(File excelfile, String sheetName) {

		ArrayList listRows = new ArrayList();
		File file = excelfile;
		InputStream is;
		// 根据输入流创建Workbook对象
		Workbook wb = null;
		try {
			is = new FileInputStream(file);
			wb = WorkbookFactory.create(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Sheet sheet = (Sheet) wb.getSheet(sheetName);
		for (int j = 0; j < sheet.getLastRowNum() + 1; j++) {
			Row row = sheet.getRow(j);
			ArrayList cellsValue = new ArrayList();
			if (row != null) {
				for (int i = 0; i < row.getLastCellNum(); i++) {
					// cell.getCellType是获得cell里面保存的值的type
					Cell cell = row.getCell(i);

					if (cell == null) {
						cellsValue.add("");
					} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
						cellsValue.add(cell.getBooleanCellValue());
					} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						// 先看是否是日期格式
						if (DateUtil.isCellDateFormatted(cell)) {
							// 读取日期格式
							cellsValue.add(cell.getDateCellValue());
						} else {
							// 读取数字
							cellsValue.add(cell.getNumericCellValue());
						}
					} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						// 读取String
						cellsValue.add(cell.getStringCellValue());

					} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
						// 读取公式
						cellsValue.add(cell.getCellFormula());
					} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
						// 读取空白
						cellsValue.add("");
					} else {
						cellsValue.add("");
					}
				}
				listRows.add(cellsValue);
			} else {
				listRows.add(cellsValue);
			}
		}
		return listRows;
	}

	/**
	 * @param excelPath
	 *            excel 文件路径
	 * @param sheetName
	 *            excelfile文件中的sheetName
	 * @author wu_gb 新增日期：2010-11-16
	 * @return 返回文件 excelfile文件中的sheetName
	 */
	public List getListRows(String excelPath, String sheetName) {
		ArrayList listRows = new ArrayList();
		File file = new File(excelPath);
		InputStream is;
		// 根据输入流创建Workbook对象
		Workbook wb = null;
		try {
			is = new FileInputStream(file);
			wb = WorkbookFactory.create(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Sheet sheet = (Sheet) wb.getSheet(sheetName);
		for (int j = 0; j < sheet.getLastRowNum() + 1; j++) {
			Row row = sheet.getRow(j);
			ArrayList cellsValue = new ArrayList();
			if (row != null) {
				for (int i = 0; i < row.getLastCellNum(); i++) {
					// cell.getCellType是获得cell里面保存的值的type
					Cell cell = row.getCell(i);

					if (cell == null) {
						cellsValue.add("");
					} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
						cellsValue.add(cell.getBooleanCellValue());
					} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						// 先看是否是日期格式
						if (DateUtil.isCellDateFormatted(cell)) {
							// 读取日期格式
							cellsValue.add(cell.getDateCellValue());
						} else {
							// 读取数字
							cellsValue.add(cell.getNumericCellValue());
						}
					} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						// 读取String
						cellsValue.add(cell.getStringCellValue());

					} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
						// 读取公式
						cellsValue.add(cell.getCellFormula());
					} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
						// 读取空白
						cellsValue.add("");
					} else {
						cellsValue.add("");
					}
				}
				listRows.add(cellsValue);
			} else {
				listRows.add(cellsValue);
			}
		}
		return listRows;
	}

	/**
	 * @param excelPath
	 *            excel 文件路径
	 * @param sheetNum
	 *            excel文件中的第sheetNum个表(sheetNum由0开始)
	 * @author wu_gb 新增日期：2010-03-30
	 * @return 返回文件excelPath的第sheetNum个表行的集合(数据类型都为String)
	 */
	public List<List<String>> getListStringRows(String excelPath, int sheetNum) {
		ArrayList listRows = new ArrayList();
		File file = new File(excelPath);
		InputStream is;
		// 根据输入流创建Workbook对象
		Workbook wb = null;
		try {
			is = new FileInputStream(file);
			wb = WorkbookFactory.create(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Sheet sheet = wb.getSheetAt(sheetNum);
		DateFormat format = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		for (int j = 0; j < sheet.getLastRowNum() + 1; j++) {
			Row row = sheet.getRow(j);
			ArrayList cellsValue = new ArrayList();
			if (row != null) {
				for (int i = 0; i < row.getLastCellNum(); i++) {
					// cell.getCellType是获得cell里面保存的值的type
					Cell cell = row.getCell(i);
					if (cell == null) {
						cellsValue.add("");
					} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
						cellsValue.add(cell.getBooleanCellValue() + "");
					} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						// 先看是否是日期格式
						if (DateUtil.isCellDateFormatted(cell)) {
							// 读取日期格式
							cellsValue.add(format.format(cell
									.getDateCellValue()));
						} else {
							// 读取数字
							// 建立新的cell样式
							DecimalFormat formatCell = (DecimalFormat) NumberFormat
									.getPercentInstance();
							formatCell.applyPattern("##.############");
							double strCell = cell.getNumericCellValue();
							cellsValue.add(formatCell.format(strCell));
						}
					} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						// 读取String
						cellsValue.add(cell.getStringCellValue());
					} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
						// 读取公式
						cellsValue.add(cell.getCellFormula() + "");
					} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
						// 读取空白
						cellsValue.add("");
					} else {
						cellsValue.add("");
					}
				}
				listRows.add(cellsValue);
			} else {
				listRows.add(cellsValue);
			}
		}
		return listRows;
	}

	/**
	 * @param excelPath
	 *            excel 文件路径
	 * @param sheetNum
	 *            excel文件中的第sheetNum个表(sheetNum由0开始)
	 * @author wu_gb 新增日期：2010-03-30
	 * @return 返回文件excelPath的第sheetNum个表行的集合(数据类型都为String)
	 */
	public List<List<String>> getListStringRows(InputStream is, int sheetNum) {
		ArrayList listRows = new ArrayList();
		// 根据输入流创建Workbook对象
		Workbook wb = null;
		try {
			wb = WorkbookFactory.create(is);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Sheet sheet = wb.getSheetAt(sheetNum);
		DateFormat format = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		for (int j = 0; j < sheet.getLastRowNum() + 1; j++) {
			Row row = sheet.getRow(j);
			ArrayList cellsValue = new ArrayList();
			if (row != null) {
				for (int i = 0; i < row.getLastCellNum(); i++) {
					// cell.getCellType是获得cell里面保存的值的type
					Cell cell = row.getCell(i);
					if (cell == null) {
						cellsValue.add("");
					} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
						cellsValue.add(cell.getBooleanCellValue() + "");
					} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						// 先看是否是日期格式
						if (DateUtil.isCellDateFormatted(cell)) {
							// 读取日期格式
							cellsValue.add(format.format(cell
									.getDateCellValue()));
						} else {
							// 读取数字
							// 建立新的cell样式
							DecimalFormat formatCell = (DecimalFormat) NumberFormat
									.getPercentInstance();
							formatCell.applyPattern("##.############");
							double strCell = cell.getNumericCellValue();
							cellsValue.add(formatCell.format(strCell));
						}
					} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						// 读取String
						cellsValue.add(cell.getStringCellValue());
					} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
						// 读取公式
						cellsValue.add(cell.getCellFormula() + "");
					} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
						// 读取空白
						cellsValue.add("");
					} else {
						cellsValue.add("");
					}
				}
				listRows.add(cellsValue);
			} else {
				listRows.add(cellsValue);
			}
		}
		return listRows;
	}

	/**
	 * @param excelPath
	 *            excelfile 文件
	 * @param sheetNum
	 *            excel文件中的第sheetNum个表(sheetNum由0开始)
	 * @author wu_gb 新增日期：2010-03-30
	 * @return 返回文件excelfile的第sheetNum个表行的集合(数据类型都为String)
	 */
	public List<List<String>> getListStringRows(File excelfile, int sheetNum) {
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
		DateFormat format = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		for (int j = 0; j < sheet.getLastRowNum() + 1; j++) {
			Row row = sheet.getRow(j);
			ArrayList cellsValue = new ArrayList();
			if (row != null) {
				for (int i = 0; i < row.getLastCellNum(); i++) {
					// cell.getCellType是获得cell里面保存的值的type
					Cell cell = row.getCell(i);
					if (cell == null) {
						cellsValue.add("");
					} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
						cellsValue.add(cell.getBooleanCellValue() + "");
					} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						// 先看是否是日期格式
						if (DateUtil.isCellDateFormatted(cell)) {
							// 读取日期格式
							cellsValue.add(format.format(cell
									.getDateCellValue()));
						} else {
							// 读取数字
							// 建立新的cell样式
							DecimalFormat formatCell = (DecimalFormat) NumberFormat
									.getPercentInstance();
							formatCell.applyPattern("##.############");
							double strCell = cell.getNumericCellValue();
							cellsValue.add(formatCell.format(strCell));
						}
					} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						// 读取String
						cellsValue.add(cell.getStringCellValue());
					} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
						// 读取公式
						cellsValue.add(cell.getCellFormula() + "");
					} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
						// 读取空白
						cellsValue.add("");
					} else {
						cellsValue.add("");
					}
				}
				listRows.add(cellsValue);
			} else {
				listRows.add(cellsValue);
			}
		}
		return listRows;
	}

	/**
	 * @param excelPath
	 *            excel 文件路径
	 * @param sheetName
	 *            excelfile文件中的sheetName
	 * @author wu_gb 新增日期：2010-3-30
	 * @return 返回文件excelfile的sheetName表行的集合(数据类型都为String)
	 */
	public List<List<String>> getListStringRows(String excelPath,
			String sheetName) {

		ArrayList listRows = new ArrayList();
		File file = new File(excelPath);
		InputStream is;
		// 根据输入流创建Workbook对象
		Workbook wb = null;
		try {
			is = new FileInputStream(file);
			wb = WorkbookFactory.create(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Sheet sheet = wb.getSheet(sheetName);
		DateFormat format = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		for (int j = 0; j < sheet.getLastRowNum() + 1; j++) {
			Row row = sheet.getRow(j);
			ArrayList<String> cellsValue = new ArrayList<String>();
			if (row != null) {
				for (int i = 0; i < row.getLastCellNum(); i++) {
					// cell.getCellType是获得cell里面保存的值的type
					Cell cell = row.getCell(i);
					if (cell == null) {
						cellsValue.add("");
					} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
						cellsValue.add(cell.getBooleanCellValue() + "");
					} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						// 先看是否是日期格式
						if (DateUtil.isCellDateFormatted(cell)) {
							// 读取日期格式
							cellsValue.add(format.format(cell
									.getDateCellValue()));
						} else {
							// 读取数字
							// 建立新的cell样式
							DecimalFormat formatCell = (DecimalFormat) NumberFormat
									.getPercentInstance();
							formatCell.applyPattern("##.############");
							double strCell = cell.getNumericCellValue();
							cellsValue.add(formatCell.format(strCell));
						}
					} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						// 读取String
						cellsValue.add(cell.getStringCellValue());
					} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
						// 读取公式
						cellsValue.add(cell.getCellFormula() + "");
					} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
						// 读取空白
						cellsValue.add("");
					} else {
						cellsValue.add("");
					}
				}
				listRows.add(cellsValue);
			} else {
				listRows.add(cellsValue);
			}
		}
		return listRows;
	}

	/**
	 * @param excelPath
	 *            excelfile 文件
	 * @param sheetName
	 *            excelfile文件中的sheetName
	 * @author wu_gb 新增日期：2010-3-30
	 * @return 返回文件excelfile的sheetName表行的集合(数据类型都为String)
	 */
	public List<List<String>> getListStringRows(File excelfile, String sheetName) {
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
		Sheet sheet = wb.getSheet(sheetName);
		DateFormat format = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		for (int j = 0; j < sheet.getLastRowNum() + 1; j++) {
			Row row = sheet.getRow(j);
			ArrayList<String> cellsValue = new ArrayList<String>();
			if (row != null) {
				for (int i = 0; i < row.getLastCellNum(); i++) {
					// cell.getCellType是获得cell里面保存的值的type
					Cell cell = row.getCell(i);
					if (cell == null) {
						cellsValue.add("");
					} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
						cellsValue.add(cell.getBooleanCellValue() + "");
					} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						// 先看是否是日期格式
						if (DateUtil.isCellDateFormatted(cell)) {
							// 读取日期格式
							cellsValue.add(format.format(cell
									.getDateCellValue()));
						} else {
							// 读取数字
							// 建立新的cell样式
							DecimalFormat formatCell = (DecimalFormat) NumberFormat
									.getPercentInstance();
							formatCell.applyPattern("##.############");
							double strCell = cell.getNumericCellValue();
							cellsValue.add(formatCell.format(strCell));
						}
					} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {

						// 读取String
						cellsValue.add(cell.getStringCellValue());
					} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
						// 读取公式
						cellsValue.add(cell.getCellFormula() + "");
					} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
						// 读取空白
						cellsValue.add("");
					} else {
						cellsValue.add("");
					}
				}
				listRows.add(cellsValue);
			} else {
				listRows.add(cellsValue);
			}
		}
		return listRows;
	}

}
