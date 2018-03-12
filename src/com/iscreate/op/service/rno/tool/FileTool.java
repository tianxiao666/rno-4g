package com.iscreate.op.service.rno.tool;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FileTool {

	private static final int BUFFER_SIZE = 1024;

	private static Logger logger = Logger.getLogger(FileTool.class);

	// 一行的字段之间的默认分隔符
	private static String fieldSep = "##";
	// 最后一个字段以后的结尾字符串,因为split方法会丢弃空白。每行标题、值输出都加上，读取的时候去掉
	private static String fieldEndStr = "__End";

	public static boolean copy(File src, File dst) {
		boolean result = false;
		InputStream in = null;
		OutputStream out = null;

		String dstPath = dst.getAbsolutePath();
		String dstdir = dstPath.substring(0,
				dstPath.lastIndexOf(File.separator));
		// 目标路径
		File parDir = new File(dstdir);
		logger.debug("destination directory is :" + dstdir);
		// 目标路径不存在
		if (!parDir.exists()) {
			logger.debug("destination directory does'nt exist,create it.");
			parDir.mkdirs();
		}
		try {
			in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
			out = new BufferedOutputStream(new FileOutputStream(dst),
					BUFFER_SIZE);
			byte[] buffer = new byte[BUFFER_SIZE];
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * 解压 zip 文件，注意不能解压 rar 文件哦，只能解压 zip 文件 解压 rar 文件 会出现 java.io.IOException:
	 * Negative seek offset 异常 create date:2009- 6- 9 author:Administrator
	 * 
	 * @param zipfile
	 *            zip 文件，注意要是正宗的 zip 文件哦，不能是把 rar 的直接改为 zip 这样会出现
	 *            java.io.IOException: Negative seek offset 异常
	 * @param destDir
	 * @throws IOException
	 */
	// public static boolean unZip(String zipfile, String destDir) {
	//
	// destDir = destDir.endsWith("//") ? destDir : destDir + "//";
	// byte b[] = new byte[1024];
	// int length;
	//
	// ZipFile zipFile;
	// try {
	// zipFile = new ZipFile(new File(zipfile));
	// Enumeration enumeration = zipFile.entries();// zipFile.getEntries();
	//
	// ZipEntry zipEntry = null;
	//
	// while (enumeration.hasMoreElements()) {
	// zipEntry = (ZipEntry) enumeration.nextElement();
	// File loadFile = new File(destDir + zipEntry.getName());
	//
	// if (zipEntry.isDirectory()) {
	// // 这段都可以不要，因为每次都貌似从最底层开始遍历的
	// loadFile.mkdirs();
	// } else {
	// if (!loadFile.getParentFile().exists())
	// loadFile.getParentFile().mkdirs();
	//
	// OutputStream outputStream = new FileOutputStream(loadFile);
	// InputStream inputStream = zipFile.getInputStream(zipEntry);
	//
	// while ((length = inputStream.read(b)) > 0) {
	// outputStream.write(b, 0, length);
	// }
	// outputStream.close();
	// }
	// }
	// // System.out.println(" 文件解压成功 ");
	// return true;
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// return false;
	// }
	// }

	/**
	 * 删除目录
	 * 
	 * @param destDir
	 * @author brightming 2014-1-20 上午10:53:41
	 */
	public static void deleteDir(String destDir) {
		if (destDir == null || "".equals(destDir.trim())) {
			return;
		}

		File file = new File(destDir);
		if (!file.exists() || !file.isDirectory()) {
			// 不存在，或不是目录
			return;
		}
		delete(file);
	}

	/**
	 * 删除文件，或文件夹
	 * 
	 * @param file
	 * @author brightming 2014-1-20 上午10:55:42
	 */
	public static void delete(File file) {
		if (!file.exists()) {
			return;
		}
		if (file.isFile()) {
			file.delete();
			return;
		} else if (file.isDirectory()) {
			File[] fs = file.listFiles();
			for (File f : fs) {
				delete(f);
			}
			file.delete();// 删除目录本身
		}

	}

	public static boolean saveInputStream(String path, InputStream is) {
		if (path == null || "".equals(path) || is == null) {
			return false;
		}
		byte[] buf = new byte[1024];
		int len = -1;
		OutputStream out = null;
		File file = new File(path);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		try {
			out = new BufferedOutputStream(new FileOutputStream(path),
					BUFFER_SIZE);
			while ((len = is.read(buf)) != -1) {
				out.write(buf, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
			}
		}

		return true;
	}

	public static Map<String, List> getListRows(String excelPath,
			List<String> sheetName) {

		File file = new File(excelPath);
		InputStream is;
		// 根据输入流创建Workbook对象
		Workbook wb = null;
		try {
			is = new FileInputStream(file);
			wb = WorkbookFactory.create(is);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		Map<String, List> res = readFromExcelWb(wb, sheetName);
		return res;
	}

	private static Map<String, List> readFromExcelWb(Workbook wb,
			List<String> sheetNames) {
		ArrayList listRows = null;
		Map<String, List> res = new HashMap<String, List>();

		Map<String, Object> onerow = null;
		for (String sn : sheetNames) {
			listRows = new ArrayList();
			res.put(sn, listRows);
			Sheet sheet = (Sheet) wb.getSheet(sn);
			for (int j = 0; j < sheet.getLastRowNum() + 1; j++) {
				Row row = sheet.getRow(j);
				List cellsValue = new ArrayList();
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
		}
		return res;
	}

	/**
	 * 输出到各个sheet
	 * 
	 * @param sheetNames
	 *            sheet名称
	 * @param titleArrays
	 *            各sheet的标题
	 * @param keyArrays
	 *            各sheet标题对应的key
	 * @param dataLists
	 *            各sheet的数据
	 * @return
	 * @author brightming 2014-3-7 上午10:23:51
	 */
	public static ByteArrayOutputStream putDataOnExcelOutputStream(
			List<String> sheetNames, List<String[]> titleArrays,
			List<String[]> keyArrays, List<List<Map<String, Object>>> dataLists) {
		logger.info("进入putDataOnExcelOutputStream(String[] titleArray,String[] keyArray,List<Map<String, Object>> dataList)，生成excel文件流。");
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			// XSSFWorkbook wb = new XSSFWorkbook();//创建工作簿
			// SXSSFWorkbook
			Workbook wb = new SXSSFWorkbook(100);
			String[] titleArray = null;
			String[] keyArray = null;
			List<Map<String, Object>> dataList = null;
			for (int shcnt = 0; shcnt < sheetNames.size(); shcnt++) {
				Sheet sheet = wb.createSheet(sheetNames.get(shcnt));// 创建excel
																	// sheet

				// 创建一个样式
				CellStyle style = wb.createCellStyle();
				// 居中对齐
				style.setAlignment(CellStyle.ALIGN_CENTER);
				// 设定单元格背景颜色
				style.setFillForegroundColor(HSSFColor.AQUA.index);
				style.setFillPattern(CellStyle.SOLID_FOREGROUND);
				style.setBorderBottom(CellStyle.BORDER_THIN);
				style.setBorderTop(CellStyle.BORDER_THIN);
				style.setBorderLeft(CellStyle.BORDER_THIN);
				style.setBorderRight(CellStyle.BORDER_THIN);

				// 设置表头的内容
				titleArray = titleArrays.get(shcnt);
				keyArray = keyArrays.get(shcnt);
				dataList = dataLists.get(shcnt);
				if (titleArray != null && titleArray.length > 0) {
					int length = titleArray.length;
					Row row = sheet.createRow(0); // sheet 创建一行
					Cell cell = null;
					for (int j = 0; j < length; j++) {
						cell = row.createCell(j); // sheet 创建一列
						cell.setCellValue(titleArray[j]);// 列设值
						cell.setCellStyle(style); // 列样式
					}
					// 输出查询出的数据
					if (dataList != null && !dataList.isEmpty()
							&& keyArray != null && keyArray.length > 0) {
						int keyArrayLength = keyArray.length;
						int dataListSize = dataList.size();
						for (int i = 0; i < dataListSize; i++) {// 每行数据
							row = sheet.createRow(i + 1); // sheet 创建一行
							Map<String, Object> map = dataList.get(i);
							for (int j = 0; j < keyArrayLength; j++) {
								String curValue = map.get(keyArray[j]) + "";
								if ("null".equals(curValue)) {
									curValue = "";
								}
								cell = row.createCell(j); // sheet 创建一列
								cell.setCellValue(curValue);// 列设值
							}
						}
					}
				}
			}

			wb.write(os);
			os.flush();
			os.close();
			logger.info("退出putDataOnExcelOutputStream(String[] titleArray,String[] keyArray,List<Map<String, Object>> dataList)，生成excel文件流成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("退出putDataOnExcelOutputStream(String[] titleArray,String[] keyArray,List<Map<String, Object>> dataList)，生成excel文件流失败，exception："
					+ e.getMessage());
		}

		return os;
	}

	/**
	 * 输出到各个sheet
	 * 
	 * @param fileFullName
	 *            保存的excel文件全路径名
	 * @param sheetNames
	 *            sheet名称
	 * @param titleArrays
	 *            各sheet的标题
	 * @param keyArrays
	 *            各sheet标题对应的key
	 * @param dataLists
	 *            各sheet的数据
	 * @return
	 * @author brightming 2014-6-1 上午14:30:51
	 */
	public static boolean saveDataInExcelFile(String fileFullName,
			List<String> sheetNames, List<String[]> titleArrays,
			List<String[]> keyArrays, List<List<Map<String, Object>>> dataLists) {
		logger.info("进入saveDataInExcelFile，保存数据到excel文件。");

		try {
			File file = new File(fileFullName);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			// XSSFWorkbook wb = new XSSFWorkbook();//创建工作簿
			// SXSSFWorkbook
			Workbook wb = new SXSSFWorkbook(1000);
			String[] titleArray = null;
			String[] keyArray = null;
			List<Map<String, Object>> dataList = null;
			for (int shcnt = 0; shcnt < sheetNames.size(); shcnt++) {
				Sheet sheet = wb.createSheet(sheetNames.get(shcnt));// 创建excel
																	// sheet

				// 创建一个样式
				CellStyle style = wb.createCellStyle();
				// 居中对齐
				style.setAlignment(CellStyle.ALIGN_CENTER);
				// 设定单元格背景颜色
				style.setFillForegroundColor(HSSFColor.AQUA.index);
				style.setFillPattern(CellStyle.SOLID_FOREGROUND);
				style.setBorderBottom(CellStyle.BORDER_THIN);
				style.setBorderTop(CellStyle.BORDER_THIN);
				style.setBorderLeft(CellStyle.BORDER_THIN);
				style.setBorderRight(CellStyle.BORDER_THIN);

				// 设置表头的内容
				titleArray = titleArrays.get(shcnt);
				keyArray = keyArrays.get(shcnt);
				dataList = dataLists.get(shcnt);
				if (titleArray != null && titleArray.length > 0) {
					int length = titleArray.length;
					Row row = sheet.createRow(0); // sheet 创建一行
					Cell cell = null;
					for (int j = 0; j < length; j++) {
						cell = row.createCell(j); // sheet 创建一列
						cell.setCellValue(titleArray[j]);// 列设值
						cell.setCellStyle(style); // 列样式
					}
					// 输出查询出的数据
					if (dataList != null && !dataList.isEmpty()
							&& keyArray != null && keyArray.length > 0) {
						int keyArrayLength = keyArray.length;
						int dataListSize = dataList.size();
						for (int i = 0; i < dataListSize; i++) {// 每行数据
							row = sheet.createRow(i + 1); // sheet 创建一行
							Map<String, Object> map = dataList.get(i);
							for (int j = 0; j < keyArrayLength; j++) {
								String curValue = map.get(keyArray[j]) + "";
								if ("null".equals(curValue)) {
									curValue = "";
								}
								cell = row.createCell(j); // sheet 创建一列
								cell.setCellValue(curValue);// 列设值
							}
						}
					}
				}
			}

			FileOutputStream fos = new FileOutputStream(fileFullName);
			wb.write(fos);
			fos.flush();
			fos.close();
			logger.info("退出saveDataInExcelFile，保存excel文件成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("退出saveDataInExcelFile，保存excel文件失败，exception："
					+ e.getMessage());
			return false;
		}

		return true;
	}

	/**
	 * 保存一份sheet数据 未找到只需读取已有文件部分内容，而不需要全部加载进内存的方法，这个append方法没有多少好处
	 * 
	 * @param fileFullName
	 * @param sheetName
	 * @param titles
	 * @param keyArrays
	 * @param datas
	 * @return
	 * @author brightming 2014-6-12 下午2:45:16
	 */
	@Deprecated
	@SuppressWarnings("Deprecated")
	public static boolean saveDataInExcelFile(String fileFullName,
			boolean append, String sheetName, String[] titles, String[] keys,
			List<Map<String, Object>> datas) {

		if (append == true) {
			File file = new File(fileFullName);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			logger.debug("给已有的excel添加sheet数据。");
			SXSSFWorkbook writeWb = null;
			try {
				XSSFWorkbook readWb = new XSSFWorkbook(new FileInputStream(
						fileFullName));
				writeWb = new SXSSFWorkbook(readWb, 1000);
				Sheet sheet = writeWb.createSheet(sheetName);

				// 创建一个样式
				CellStyle style = writeWb.createCellStyle();
				// 居中对齐
				style.setAlignment(CellStyle.ALIGN_CENTER);
				// 设定单元格背景颜色
				style.setFillForegroundColor(HSSFColor.AQUA.index);
				style.setFillPattern(CellStyle.SOLID_FOREGROUND);
				style.setBorderBottom(CellStyle.BORDER_THIN);
				style.setBorderTop(CellStyle.BORDER_THIN);
				style.setBorderLeft(CellStyle.BORDER_THIN);
				style.setBorderRight(CellStyle.BORDER_THIN);

				// 设置表头的内容

				if (titles != null && titles.length > 0) {
					int length = titles.length;
					Row row = sheet.createRow(0); // sheet 创建一行
					Cell cell = null;
					for (int j = 0; j < length; j++) {
						cell = row.createCell(j); // sheet 创建一列
						cell.setCellValue(titles[j]);// 列设值
						cell.setCellStyle(style); // 列样式
					}
					// 输出查询出的数据
					if (datas != null && !datas.isEmpty() && keys != null
							&& keys.length > 0) {
						int keyArrayLength = keys.length;
						int dataListSize = datas.size();
						for (int i = 0; i < dataListSize; i++) {// 每行数据
							row = sheet.createRow(i + 1); // sheet 创建一行
							Map<String, Object> map = datas.get(i);
							for (int j = 0; j < keyArrayLength; j++) {
								String curValue = map.get(keys[j]) + "";
								if ("null".equals(curValue)) {
									curValue = "";
								}
								cell = row.createCell(j); // sheet 创建一列
								cell.setCellValue(curValue);// 列设值
							}
						}
					}
				}

				writeWb.write(new FileOutputStream(fileFullName));

			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {

			}
		} else {
			logger.debug("新建excel文件保存数据");
			List<String> sheetNames = Arrays.asList(sheetName);
			List<String[]> titleArrays = new ArrayList<String[]>();
			titleArrays.add(titles);
			List<String[]> keyArrays = new ArrayList<String[]>();
			keyArrays.add(keys);
			List<List<Map<String, Object>>> dataLists = new ArrayList<List<Map<String, Object>>>();
			dataLists.add(datas);
			return saveDataInExcelFile(fileFullName, sheetNames, titleArrays,
					keyArrays, dataLists);
		}
		return true;
	}

	/**
	 * 保存到csv文件
	 * 
	 * @param fileFullName
	 * @param append
	 * @param sheetName
	 * @param titles
	 * @param keys
	 * @param datas
	 * @return 输出的总行数
	 * @author brightming 2014-6-12 下午3:48:05
	 */
	public static long saveDataInTxtFile(String fileFullName, String[] keys,
			List<Map<String, Object>> datas) {

		long lineCnt = 0;

		try {
			File file = new File(fileFullName);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			OutputStreamWriter write = new OutputStreamWriter(
					new FileOutputStream(fileFullName), "utf-8");
			BufferedWriter bw = new BufferedWriter(write);
			int keyLen = keys.length;
			int keyLen_1 = keyLen - 1;
			for (int i = 0; i < keyLen; i++) {
				bw.write(keys[i]);
				if (i < keyLen_1) {
					bw.write(fieldSep);
				}
			}
			bw.newLine();
			lineCnt++;

			Object val = null;
			for (Map<String, Object> rec : datas) {
				for (int i = 0; i < keys.length; i++) {
					val = rec.get(keys[i]);
					if (val == null) {
						val = "";
					}
					bw.write(val.toString());
					if (i < keyLen_1) {
						bw.write(fieldSep);
					}
				}
				bw.newLine();
				lineCnt++;
			}
			bw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
		}
		return lineCnt;
	}

	/**
	 * 按照规定的格式输出各部分的内容：
	 * 第一行，分隔符信息，以及关于个dataType的起始行号和结束行号，如：sep=##,cellres=2-200,cluster=201-300
	 * 各字段信息以逗号分隔符分隔——这是固定的 其余行，要么是标题，要么是数据，行的字段之间用规定的分隔符
	 * 
	 * @param fileFullName
	 * @param dataTypes
	 * @param keyArrays
	 * @param dataArrays
	 * @return
	 * @author brightming 2014-6-12 下午4:22:16
	 */
	public static long saveDataInTxtFile(String fileFullName,
			List<String> dataTypes, List<String[]> keyArrays,
			List<List<Map<String, Object>>> dataArrays) {

		long totalCnt = 0;
		BufferedWriter bw = null;
		try {
			File file = new File(fileFullName);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			OutputStreamWriter write = new OutputStreamWriter(
					new FileOutputStream(fileFullName), "utf-8");
			bw = new BufferedWriter(write);

			// --行的计数从1开始--//
			// ---准备输出第一行---//
			List<Long[]> segPois = new ArrayList<Long[]>();
			long startPoi = 2;// 从第二行开始才是真正数据的行
			for (List<Map<String, Object>> oneTypeData : dataArrays) {
				Long[] poi = new Long[2];
				poi[0] = startPoi;
				poi[1] = startPoi + oneTypeData.size();// 包括标题行
				startPoi = poi[1] + 1;
				segPois.add(poi);
			}
			StringBuilder buf = new StringBuilder();
			buf.append("sep=" + fieldSep + ",");
			for (int i = 0; i < dataTypes.size(); i++) {
				Long[] poi = segPois.get(i);
				buf.append(dataTypes.get(i) + "=" + poi[0] + "-" + poi[1]);
				if (i < dataTypes.size() - 1) {
					buf.append(",");
				}
			}
			bw.write(buf.toString());
			bw.newLine();
			totalCnt++;
			// ----第一行输出完毕----//

			// --循环输出其他数据---//
			String[] keys = null;
			List<Map<String, Object>> datas;
			int keyLen = 0, keyLen_1 = -1;
			for (int i = 0; i < keyArrays.size(); i++) {
				buf.setLength(0);
				keys = keyArrays.get(i);
				keyLen = keys.length;
				keyLen_1 = keyLen - 1;

				// 输出标题
				for (int j = 0; j < keyLen; j++) {
					buf.append(keys[j]);
					// if (j < keyLen_1) {
					buf.append(fieldSep);
					// }
				}
				// 加上保护末尾
				buf.append(fieldEndStr);
				bw.write(buf.toString());
				bw.newLine();
				totalCnt++;

				// 输出内容
				datas = dataArrays.get(i);
				Object val;
				for (Map<String, Object> data : datas) {
					buf.setLength(0);
					for (int j = 0; j < keyLen; j++) {
						val = data.get(keys[j]);
						if (val == null) {
							val = "";
						}
						buf.append(val.toString());
						// if (j < keyLen_1) {
						buf.append(fieldSep);
						// }
					}
					// 加上保护末尾
					buf.append(fieldEndStr);
					bw.write(buf.toString());
					bw.newLine();
					totalCnt++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return totalCnt;
	}

	/**
	 * 读取指定文件的指定数据内容
	 * 
	 * @param fileFullName
	 * @param dataType
	 * @return
	 * @author brightming 2014-6-12 下午4:53:24
	 */
	public static List<Map<String, Object>> readDataFromTxt(
			String fileFullName, String dataType) {
		BufferedReader reader = null;
		InputStreamReader isr = null;
		String sep = "##";
		long curLineNum = 1;
		String line = null;
		List<Map<String, Object>> dataRes = new ArrayList<Map<String, Object>>();
		try {
			isr = new InputStreamReader(new FileInputStream(fileFullName),
					"utf-8");
			reader = new BufferedReader(isr);
			line = reader.readLine();
			String[] fields = line.split(",");
			if (fields == null || fields.length == 0) {
				logger.error("无效的文件格式！");
				isr.close();
				return null;
			}
			String[] onefield = fields[0].split("=");
			if (onefield == null || onefield.length != 2) {
				logger.error("无效的文件格式！");
				isr.close();
				return null;
			}

			if (!"sep".equals(onefield[0])) {
				logger.error("无效的文件格式！第一个字段必须是\"sep\"!");
				isr.close();
				return null;
			}
			// 字段分隔符
			sep = onefield[1];

			long startLine = -1, endLine = -1;
			for (int i = 0; i < fields.length; i++) {
				if (fields[i] != null) {
					onefield = fields[i].split("=");
					if (onefield != null && onefield.length == 2) {
						if (dataType.equals(onefield[0])) {
							logger.debug("find datatype,line range:"
									+ onefield[1]);
							try {
								String[] lineNum = onefield[1].split("-");
								startLine = Long.parseLong(lineNum[0]);
								endLine = Long.parseLong(lineNum[1]);
							} catch (Exception e2) {
								e2.printStackTrace();
								logger.error("无效的行范围表示！");
								isr.close();
								return null;
							}

							break;
						}
					}
				}

			}

			// 开始读数据
			if (startLine > 0 && endLine >= startLine) {
				// 读行，直到需要的内容第一行
				while (curLineNum < startLine - 1) {
					curLineNum++;
					line = reader.readLine();
					// System.out.println("line num="+curLineNum+",line="+line);
				}
				// 读第一行，此行为标题
				curLineNum++;
				line = reader.readLine();
				String[] titleFields = line.split(sep);
				int titleCnt = titleFields.length;

				Map<String, Object> oneres;
				// 读内容
				while (curLineNum <= endLine) {
					curLineNum++;
					line = reader.readLine();
					if (line == null) {
						continue;
					}
					onefield = line.split(sep);
					if (onefield.length != titleCnt) {
						continue;
					}
					oneres = new HashMap<String, Object>();
					for (int k = 0; k < titleCnt - 1; k++) {// 最后一列是保护字符，不需要
						oneres.put(titleFields[k], onefield[k]);
					}
					dataRes.add(oneres);
				}
			}

		} catch (Exception e) {
			logger.error("line num:" + curLineNum + ",content=" + line);
			e.printStackTrace();
		} finally {

		}
		return dataRes;
	}

	/**
	 * 读取结构优化分析中，指定类型的指定字段的内容，其他的忽略
	 * 
	 * @param fileFullName
	 * @param dataType
	 * @param needFields
	 * @return
	 * @author brightming 2014-7-17 下午6:47:01
	 */
	public static List<Map<String, Object>> readPartlyFieldsDataFromTxt(
			String fileFullName, String dataType, List<String> needFields) {
		BufferedReader reader = null;
		InputStreamReader isr = null;
		String sep = "##";
		long curLineNum = 1;
		String line = null;
		List<Map<String, Object>> dataRes = new ArrayList<Map<String, Object>>();
		try {
			isr = new InputStreamReader(new FileInputStream(fileFullName),
					"utf-8");
			reader = new BufferedReader(isr);
			line = reader.readLine();
			String[] fields = line.split(",");
			if (fields == null || fields.length == 0) {
				logger.error("无效的文件格式！");
				isr.close();
				return null;
			}
			String[] onefield = fields[0].split("=");
			if (onefield == null || onefield.length != 2) {
				logger.error("无效的文件格式！");
				isr.close();
				return null;
			}

			if (!"sep".equals(onefield[0])) {
				logger.error("无效的文件格式！第一个字段必须是\"sep\"!");
				isr.close();
				return null;
			}
			// 字段分隔符
			sep = onefield[1];

			long startLine = -1, endLine = -1;
			for (int i = 0; i < fields.length; i++) {
				if (fields[i] != null) {
					onefield = fields[i].split("=");
					if (onefield != null && onefield.length == 2) {
						if (dataType.equals(onefield[0])) {
							logger.debug("find datatype,line range:"
									+ onefield[1]);
							try {
								String[] lineNum = onefield[1].split("-");
								startLine = Long.parseLong(lineNum[0]);
								endLine = Long.parseLong(lineNum[1]);
							} catch (Exception e2) {
								e2.printStackTrace();
								logger.error("无效的行范围表示！");
								isr.close();
								return null;
							}

							break;
						}
					}
				}

			}

			// 开始读数据
			if (startLine > 0 && endLine >= startLine) {
				// 读行，直到需要的内容第一行
				while (curLineNum < startLine - 1) {
					curLineNum++;
					line = reader.readLine();
					// System.out.println("line num="+curLineNum+",line="+line);
				}
				// 读第一行，此行为标题

				curLineNum++;
				line = reader.readLine();
				String[] titleFields = line.split(sep);
				int titleCnt = titleFields.length;
				// 找到需要的字段的位置
				int needFieldCnt = needFields.size();
				int[] fieldIdxs = new int[needFieldCnt];
				for (int i = 0; i < needFieldCnt; i++) {
					fieldIdxs[i] = -1;
				}
				for (int j = 0; j < titleCnt; j++) {
					for (int i = 0; i < needFields.size(); i++) {
						// 忽略大小写
						if (titleFields[j].equalsIgnoreCase(needFields.get(i))) {
							fieldIdxs[i] = j;
							break;
						}
					}
				}

				Map<String, Object> oneres;
				// 读内容
				while (curLineNum <= endLine) {
					curLineNum++;
					line = reader.readLine();
					if (line == null) {
						continue;
					}
					onefield = line.split(sep);
					if (onefield.length != titleCnt) {
						continue;
					}
					oneres = new HashMap<String, Object>();
					// for(int k=0;k<titleCnt-1;k++){//最后一列是保护字符，不需要
					// oneres.put(titleFields[k], onefield[k]);
					// }
					for (int k = 0; k < needFieldCnt; k++) {
						if (fieldIdxs[k] < 0) {
							oneres.put(needFields.get(k).toLowerCase(), "#NVAL");
							logger.error("需要的字段：" + needFields.get(k)
									+ " 未找到！以#NVAL代替！");
							continue;
						}
						oneres.put(needFields.get(k), onefield[fieldIdxs[k]]);
					}
					dataRes.add(oneres);
				}
			}

		} catch (Exception e) {
			logger.error("line num:" + curLineNum + ",content=" + line);
			e.printStackTrace();
		} finally {

		}
		return dataRes;
	}

	/**
	 * 可以处理中文文件名
	 */
	private static final int buffer = 2048;

	/**
	 * 压缩包内不能有文件夹，直接就是文件
	 * 
	 * @param path
	 * @param savepath
	 * @return
	 * @author brightming 2014-8-20 下午12:32:19
	 */
	// @Deprecated
	// public static boolean unZip1(String path, String savepath) {
	// int count = -1;
	// int index = -1;
	// // String savepath = "";
	// boolean flag = false;
	//
	// File file = null;
	// InputStream is = null;
	// FileOutputStream fos = null;
	// BufferedOutputStream bos = null;
	//
	// // savepath = path.substring(0, path.lastIndexOf("\\")) + "\\";
	// if (!savepath.endsWith("/") && !savepath.endsWith("//")) {
	// savepath += "/";
	// }
	// File dir = new File(savepath);
	// if (!dir.exists()) {
	// dir.mkdirs();
	// }
	// try {
	// ZipFile zipFile = new ZipFile(path);
	//
	// Enumeration<?> entries = zipFile.getEntries();
	//
	// while (entries.hasMoreElements()) {
	// byte buf[] = new byte[buffer];
	//
	// ZipEntry entry = (ZipEntry) entries.nextElement();
	// if (entry.isDirectory()) {
	// continue;
	// }
	//
	// String filename = entry.getName();
	// index = filename.lastIndexOf("/");
	// if (index > -1)
	// filename = filename.substring(index + 1);
	//
	// filename = savepath + filename;
	//
	// // flag = isPics(filename);
	// // if(!flag)
	// // continue;
	//
	// file = new File(filename);
	// file.createNewFile();
	//
	// is = zipFile.getInputStream(entry);
	//
	// fos = new FileOutputStream(file);
	// bos = new BufferedOutputStream(fos, buffer);
	//
	// while ((count = is.read(buf)) > -1) {
	// bos.write(buf, 0, count);
	// }
	//
	// fos.close();
	//
	// is.close();
	// }
	//
	// zipFile.close();
	//
	// } catch (IOException ioe) {
	// ioe.printStackTrace();
	// return false;
	// }
	// return true;
	// }

	public static boolean isPics(String filename) {
		boolean flag = false;

		if (filename.endsWith(".jpg") || filename.endsWith(".gif")
				|| filename.endsWith(".bmp") || filename.endsWith(".png"))
			flag = true;

		return flag;
	}

	/**
	 * 利用第三方开源包cpdetector获取文件编码格式
	 * 
	 * @param path
	 *            要判断文件编码格式的源文件的路径
	 * @author huanglei
	 * @version 2012-7-12 14:05
	 */
	public static String getFileEncode(String path) {

		InputStream bin = null;
		try {
			bin = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		String code = null;
		byte[] head = new byte[3];
		try {
			bin.read(head);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				bin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (head[0] == -17 && head[1] == -69 && head[2] == -65) {
			code = "UTF-8";
		} else if (head[0] == (byte) 0xFF && head[1] == (byte) 0xFE) {
			code = "UTF-16LE";
			// checked = true;
		} else if (head[0] == (byte) 0xFE && head[1] == (byte) 0xFF) {
			code = "UTF-16BE";
			// checked = true;
		} else if (head[0] == -1 && head[1] == -2) {
			code = "UTF-16";
		} else if (head[0] == -2 && head[1] == -1) {
			code = "Unicode";
		} else if (head[0] == 10 && head[1] == 10 && head[2] == -63) {
			code = "GBK";
		} else if (head[0] == 82 && head[1] == 78 && head[2] == 79){
			//[82, 78, 79]
			code = "GBK";
		}
		if (code != null) {
			return code;
		}

		/*
		 * detector是探测器，它把探测任务交给具体的探测实现类的实例完成。
		 * cpDetector内置了一些常用的探测实现类，这些探测实现类的实例可以通过add方法 加进来，如ParsingDetector、
		 * JChardetFacade、ASCIIDetector、UnicodeDetector。
		 * detector按照“谁最先返回非空的探测结果，就以该结果为准”的原则返回探测到的
		 * 字符集编码。使用需要用到三个第三方JAR包：antlr.jar、chardet.jar和cpdetector.jar
		 * cpDetector是基于统计学原理的，不保证完全正确。
		 */
		java.nio.charset.Charset charset = null;
		synchronized (FileTool.class) {
			CodepageDetectorProxy detector = CodepageDetectorProxy
					.getInstance();
			/*
			 * ParsingDetector可用于检查HTML、XML等文件或字符流的编码,构造方法中的参数用于
			 * 指示是否显示探测过程的详细信息，为false不显示。
			 */
			detector.add(new ParsingDetector(false));
			/*
			 * JChardetFacade封装了由Mozilla组织提供的JChardet，它可以完成大多数文件的编码
			 * 测定。所以，一般有了这个探测器就可满足大多数项目的要求，如果你还不放心，可以
			 * 再多加几个探测器，比如下面的ASCIIDetector、UnicodeDetector等。
			 */
			detector.add(JChardetFacade.getInstance());// 用到antlr.jar、chardet.jar
			// ASCIIDetector用于ASCII编码测定
			detector.add(ASCIIDetector.getInstance());
			// UnicodeDetector用于Unicode家族编码的测定
			detector.add(UnicodeDetector.getInstance());
			File f = new File(path);
			try {
				charset = detector.detectCodepage(f.toURI().toURL());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (charset != null)
			return charset.name();
		else
			return null;
	}

	public static String copyFromLocalFile(String local, String hdfs,
			boolean deleteSrc) {
		if (StringUtils.isBlank(hdfs) || StringUtils.isBlank(local)) {
			return null;
		}

		Path localPath = new Path(local);

		if (hdfs.endsWith("/")) {
			File file = new File(local);
			hdfs += file.getName();
		}

		URI uri = null;
		if (hdfs.startsWith("hdfs")) {
			uri = URI.create(hdfs);
		} else {
			uri = URI.create("hdfs://" + (hdfs.startsWith("/") ? "" : "/")
					+ hdfs);
		}

		Path hdfsPath = new Path(uri);
		FileSystem hdfsFs = null;
		try {
			hdfsFs = hdfsPath.getFileSystem(new Configuration());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		try {
			hdfsFs.copyFromLocalFile(deleteSrc, true, localPath, hdfsPath);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return hdfs;
	}

	public static String copyToLocalFile(String hdfs, String local) {

		if (StringUtils.isBlank(hdfs) || StringUtils.isBlank(local)) {
			return null;
		}

		FileSystem hdfsFs = null;
		URI uri = null;
		if (hdfs.startsWith("hdfs")) {
			uri = URI.create(hdfs);
		} else {
			uri = URI.create("hdfs://" + (hdfs.startsWith("/") ? "" : "/")
					+ hdfs);
		}
		Path hdfsPath = new Path(uri);
		try {
			hdfsFs = hdfsPath.getFileSystem(new Configuration());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (hdfsFs.isDirectory(hdfsPath)) {
				logger.error("源hdfs:" + hdfs + " 不能是一个路径！");
				return null;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}

		File locFile = new File(local);
		locFile.mkdirs();
		if (locFile.isDirectory()) {
			local += "/" + hdfsPath.getName();
		}

		Path localPath = new Path(local);
		try {
			hdfsFs.copyToLocalFile(hdfsPath, localPath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return local;
	}

	public static boolean deleteHdfsFileOrDir(String hdfsFile) {
		if (StringUtils.isBlank(hdfsFile)) {
			return false;
		}
		Configuration conf = new Configuration();
		URI uri = null;
		if (hdfsFile.startsWith("hdfs")) {
			uri = URI.create(hdfsFile);
		} else {
			uri = URI.create("hdfs://" + (hdfsFile.startsWith("/") ? "" : "/")
					+ hdfsFile);
		}
		Path path = new Path(uri);
		FileSystem fs;
		try {
			fs = path.getFileSystem(conf);
			fs.delete(path, true);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static File getFile(String pathStr) {
		File file = null;
		if (pathStr.startsWith("hdfs:///")) {
			URI uri = URI.create(pathStr);
			Path hdfsPath = new Path(uri);
			try {
				FileSystem fs = hdfsPath.getFileSystem(new Configuration());
				if(!fs.exists(hdfsPath)){
					return null;
				}
				String localDst = "/tmp/"
						+ UUID.randomUUID().toString().replaceAll("-", "")
						+ hdfsPath.getName();
				Path localPath = new Path(localDst);
				fs.copyToLocalFile(hdfsPath, localPath);
				file = new File(localDst);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		} else if (pathStr.startsWith("file:///")) {
			pathStr = pathStr.substring("file:///".length());
			file = new File(pathStr);
		} else {
			file = new File(pathStr);// local
		}
		return file;
	}

	/**
	 * 删除本地或hdfs上的文件或文件夹
	 * 
	 * @param pathStr
	 * @return
	 * @author brightming 2014-12-15 下午1:50:03
	 */
	public static boolean deleteLocOrHdfsFileOrDir(String pathStr) {
		File file = null;
		if (pathStr.startsWith("hdfs:///")) {
			URI uri = URI.create(pathStr);
			Path hdfsPath = new Path(uri);
			try {
				FileSystem fs = hdfsPath.getFileSystem(new Configuration());
				return fs.delete(hdfsPath, true);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			if (pathStr.startsWith("file:///")) {
				pathStr = pathStr.substring("file:///".length());
			}
			file = new File(pathStr);
			if(file.exists()){
				delete(file);
			}
		}

		return true;
	}

	public static void main(String[] args) throws IOException {

		String pathStr = "hdfs:///tmp/tmp6";
		
		System.out.println(FileTool.deleteLocOrHdfsFileOrDir(pathStr));
		
//		pathStr = pathStr.substring("file:///".length());
//		System.out.println(pathStr);
//		Configuration conf = new Configuration();
//		URI uri = URI.create("file:///d:/tmp");
//		Path path = new Path(uri);
//		System.out.println(path.toString());
//		FileSystem hdfs = path.getFileSystem(conf);
//		System.out.println("fs=" + hdfs);
//		// // FileTool.deleteHdfsFileOrDir("");
//		// //
//		uri = URI.create("hdfs:///tmp/tmp5/");
//		path = new Path(uri);
//		hdfs = path.getFileSystem(conf);
//		FileStatus[] fs = hdfs.listStatus(path);
//		Path[] listPath = FileUtil.stat2Paths(fs);
//		for (Path p : listPath) {
//			System.out.println(p + ",  " + p.getName());
//
//		}
//
//		String res = "";
//		// res=FileTool.copyFromLocalFile("d:/tmp/script-WorkerNode-1.sh",
//		// "hdfs:///tmp/tmp5/");
//		// res=FileTool.copyToLocalFile("hdfs:///tmp/tmp5/script-WorkerNode-1.sh",
//		// "d:/tmp/tmp3/tmp5/tmp7/");
//		System.out.println(res);
//
//		File file = null;
//		file = FileTool.getFile("d:/tmp/jarjar-1.4.jar");
//		System.out.println("file=" + file);
//		file = FileTool.getFile("file:///d:/tmp/jarjar-1.4.jar");
//		System.out.println("file=" + file);
//		file = FileTool.getFile("hdfs:///tmp/tmp5/script-WorkerNode-1.sh");
//		System.out.println("file=" + file + ",filepath="
//				+ file.getAbsolutePath());

		// FileTool.deleteHdfsFileOrDir("hdfs:///tmp/tmp5");

		//
		// FileTool.copyFromLocalFile("d:/tmp/script-WorkerNode-1.sh",
		// "/tmp/tmp7/");
		//
		// FileTool.copyToLocalFile("/tmp/jarjar-1.4.jar", "d:/tmp/tmp2/");
	}

	public static void main1(String[] args) throws IOException {
		String str = "Hello world";
		Configuration configuration = new Configuration();
		FileSystem fs = FileSystem.get(configuration);
		java.io.OutputStream out = fs.create(new Path("/helloworld"));
		out.write(str.getBytes());
		out.flush();
		out.close();

		// String local = "d:/tmp/job.jar";
		// String hdfs = "/tmp/job.jar";
		// FileTool.copyFromLocalFile(local, hdfs);
		//
		// String local2 = "d:/tmp/job.jar2";
		// FileTool.copyToLocalFile(local2, hdfs);

		// FileTool.readHdfsFile("/tmp/a.txt");

		// String path =
		// "d:/tmp/东莞一套/(增加最新字段)_46-20140821090058(DGM31B3).csv";//
		// (未命名)_46-20140821090058(DGM31B3)
		// -
		// 副本.csv";
		//
		// String charset = getFileEncode(path);
		// System.out.println("charset=" + charset);

		// ZipFileHandler.unZip("D:\\tmp\\汕头\\dtkd-ncs-essrision汕头\\STM05B1_BARFIL00-0000001796.zip","D:\\Workspaces\\MyEclipse2013\\.metadata\\.me_tcat7\\webapps\\ops\\op\\rno\\upload\\2014-10-31\\3633366825f245069c01a2c20f8d68bb");

		// unZip(path,"d:/tmp/");

		// String path="d:/temp/jQuery-1.6-api.zip";
		// //结构优化.rar
		// String dest="d:/temp/aft";
		//
		// // unZip(path,dest);
		//
		// File file=new File(path);
		// path=file.getParentFile().getPath();
		// String destDir=path+"/"+UUID.randomUUID().toString().replaceAll("-",
		// "");
		//
		// System.out.println("path="+path+",destDir="+destDir);

		// path =
		// "D:\\Workspaces\\MyEclipse2013\\.metadata\\.me_tcat\\webapps\\ops\\op\\rno\\ana_result\\2014\\2\\ncs_res_26.xls";
		// path =
		// "C:\\Users\\brightming\\Downloads\\清远市Ncs汇总分析报告(2014-06-09 15_33_10).xls";
		// List<String> sheets = Arrays.asList(
		// RnoConstant.ReportConstant.NCS_REPORT_CELL_RES_SHEET,
		// RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_SHEET,
		// RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_CELL_SHEET,
		// RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_CELL_RELA_SHEET);
		// Map<String, List> allDatas = FileTool.getListRows(path, sheets);
		// if (allDatas == null) {
		// System.err.println("没有数据");
		// return;
		// }
		// for (String sh : sheets) {
		// System.out.println("---------------sheet:" + sh + " 数据\n");
		// System.out.println(allDatas.get(sh));
		// }

		// path = "C:\\Users\\brightming\\Desktop\\验算\\test.csv";
		//
		// List<String> segs = new ArrayList<String>();
		// List<List<Map<String, Object>>> dataArrays = new
		// ArrayList<List<Map<String, Object>>>();
		// List<String[]> keyArrays = new ArrayList<String[]>();
		// // --seg 1
		// segs.add("person");
		// String[] keys = new String[2];
		// keys[0] = "name";
		// keys[1] = "age";
		// keyArrays.add(keys);
		//
		// List<Map<String, Object>> datas = new ArrayList<Map<String,
		// Object>>();
		// Map<String, Object> data = new HashMap<String, Object>();
		// data.put("name", "高星");
		// data.put("age", 16);
		// datas.add(data);
		// dataArrays.add(datas);
		//
		// // seg2
		// segs.add("school");
		// keys = new String[3];
		// keys[0] = "poisition";
		// keys[1] = "class";
		// keys[2] = "student";
		// keyArrays.add(keys);
		//
		// datas = new ArrayList<Map<String, Object>>();
		// data = new HashMap<String, Object>();
		// data.put("poisition", "city");
		// data.put("class", "high");
		// data.put("student", 2000);
		// datas.add(data);
		// dataArrays.add(datas);
		// data = new HashMap<String, Object>();
		// data.put("poisition", "city2");
		// data.put("class", "high2");
		// data.put("student", 3000);
		// datas.add(data);
		// dataArrays.add(datas);
		//
		// data = new HashMap<String, Object>();
		// data.put("poisition", "city3");
		// data.put("class", "high3");
		// data.put("student", 4000);
		// datas.add(data);
		// dataArrays.add(datas);
		//
		// //long ok = saveDataInTxtFile(path, segs, keyArrays, dataArrays);
		// //System.out.println("ok---" + ok);
		//
		// int i=0;
		// path="D:\\Workspaces\\MyEclipse2013\\.metadata\\.me_tcat\\webapps\\ops\\op\\rno\\ana_result\\2014\\6\\ncs_res_126.xls.ro";
		// String type="clustercellrela";
		// List<Map<String,Object>> res=readDataFromTxt(path,type);
		// for(Map<String,Object> o:res){
		// System.out.println((i++)+"  o=="+o);
		// }
		// System.out.println("res size="+res.size());
		//
		//
		// String str="GQXKC4N##########2.1725############################";
		// String[] ps=str.split("##");
		// System.out.println("ps size="+ps.length);
		// i=0;
		// for(String p:ps){
		// System.out.println((i++)+"-- "+p);
		// }
	}
}
