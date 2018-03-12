package com.iscreate.plat.tools.excelhelper;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface ExcelService {
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
			int cellNum);

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
	public Object getExcelValue(File file, int sheetNum, int rowNum, int cellNum);

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
	 *         （注意单元格返回的数字类型返回的是Double类型格式）
	 * 
	 */
	public String getExcelValueToString(File file, int sheetNum, int rowNum,
			int cellNum);

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
	 *         （注意单元格的数字返回的是Double类型格式）
	 */
	public String getExcelValueToString(String excelPath, int sheetNum,
			int rowNum, int cellNum);

	/**
	 * @param excelPath
	 *            excel 文件路径
	 * @param sheetNum
	 *            excel文件中的第sheetNum个表(sheetNum由0开始)
	 * @author wu_gb 新增日期：2010-11-16
	 * @return 返回文件excelPath的第sheetNum个表行的集合
	 */
	public List getListRows(String excelPath, int sheetNum);

	/**
	 * @param excelPath
	 *            Excelfile 文件
	 * @param sheetNum
	 *            excel文件中的第sheetNum个表(sheetNum由0开始)
	 * @author wu_gb 新增日期：2010-3-18
	 * @return 返回文件 Excelfile文件中的第sheetNum个表行的集合
	 * 
	 */
	public List getListRows(File Excelfile, int sheetNum);

	/**
	 * @param excelPath
	 *            excel 文件路径
	 * @param sheetName
	 *            excelfile文件中的sheetName
	 * @author wu_gb 新增日期：2010-11-16
	 * @return 返回文件 excelfile文件中的sheetName
	 */
	public List getListRows(String excelPath, String sheetName);

	/**
	 * @param excelPath
	 *            excelfile 文件
	 * @param sheetName
	 *            excelfile文件中的sheetName
	 * @author wu_gb 新增日期：2010-3-18
	 * @return 返回文件 excelfile文件中的sheetName
	 * 
	 */
	public List getListRows(File excelfile, String sheetName);

	/**
	 * @param excelPath
	 *            excel 文件路径
	 * @param sheetNum
	 *            excel文件中的第sheetNum个表(sheetNum由0开始)
	 * @author wu_gb 新增日期：2010-03-30
	 * @return 返回文件excelPath的第sheetNum个表行的集合(数据类型都为String)
	 *         （注意单元格返回的数字类型返回的是Double类型格式）
	 */
	public List<List<String>> getListStringRows(String excelPath, int sheetNum);

	/**
	 * @param excelPath
	 *            excelfile 文件
	 * @param sheetNum
	 *            excel文件中的第sheetNum个表(sheetNum由0开始)
	 * @author wu_gb 新增日期：2010-03-30
	 * @return 返回文件excelfile的第sheetNum个表行的集合(数据类型都为String)
	 */
	public List<List<String>> getListStringRows(File excelfile, int sheetNum);

	public List<List<String>> getListStringRows(InputStream is, int sheetNum);

	/**
	 * @param excelPath
	 *            excel 文件路径
	 * @param sheetName
	 *            excelfile文件中的sheetName
	 * @author wu_gb 新增日期：2010-3-30
	 * @return 返回文件excelfile的sheetName表行的集合(数据类型都为String)
	 */
	public List<List<String>> getListStringRows(String excelPath,
			String sheetName);

	/**
	 * @param excelPath
	 *            excelfile 文件
	 * @param sheetName
	 *            excelfile文件中的sheetName
	 * @author wu_gb 新增日期：2010-3-30
	 * @return 返回文件excelfile的sheetName表行的集合(数据类型都为String)
	 */
	public List<List<String>> getListStringRows(File excelfile, String sheetName);

}
