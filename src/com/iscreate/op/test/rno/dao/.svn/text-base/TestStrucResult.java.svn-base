package com.iscreate.op.test.rno.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.sun.org.apache.xml.internal.serialize.LineSeparator;

public class TestStrucResult {

	public static void readandsave(String dir, String beginwith, int needFieldCnt,
			String[] fieldNames, String[] needVals) {
		//对于每个fieldName都判断是否是needvals之一，如果是，就处理
		String[] keyArray = { "RNO_NCS_DESC_ID", "CELL", "CHGR", "BSIC",
				"ARFCN", "DEFINED_NEIGHBOUR", "RECTIMEARFCN", "REPARFCN",
				"TIMES", "NAVSS", "TIMES1", "NAVSS1", "TIMES2", "NAVSS2",
				"TIMES3", "NAVSS3", "TIMES4", "NAVSS4", "TIMES5", "NAVSS5",
				"TIMES6", "NAVSS6", "TIMESRELSS", "TIMESRELSS2", "TIMESRELSS3",
				"TIMESRELSS4", "TIMESRELSS5", "TIMESABSS", "TIMESALONE",
				"NCELL", "DISTANCE", "INTERFER", "CA_INTERFER", "NCELLS",
				"CELL_FREQ_CNT", "NCELL_FREQ_CNT", "SAME_FREQ_CNT",
				"ADJ_FREQ_CNT", "CI_DIVIDER", "CA_DIVIDER", "CELL_INDOOR",
				"NCELL_INDOOR" };
		String[] titleArray = { "RNO_NCS_DESC_ID", "CELL", "CHGR", "BSIC",
				"ARFCN", "DEFINED_NEIGHBOUR", "RECTIMEARFCN", "REPARFCN",
				"TIMES", "NAVSS", "TIMES1", "NAVSS1", "TIMES2", "NAVSS2",
				"TIMES3", "NAVSS3", "TIMES4", "NAVSS4", "TIMES5", "NAVSS5",
				"TIMES6", "NAVSS6", "TIMESRELSS", "TIMESRELSS2", "TIMESRELSS3",
				"TIMESRELSS4", "TIMESRELSS5", "TIMESABSS", "TIMESALONE",
				"NCELL", "DISTANCE", "INTERFER", "CA_INTERFER", "NCELLS",
				"CELL_FREQ_CNT", "NCELL_FREQ_CNT", "SAME_FREQ_CNT",
				"ADJ_FREQ_CNT", "CI_DIVIDER", "CA_DIVIDER", "CELL_INDOOR",
				"NCELL_INDOOR" };

		int needValCnt=needVals.length;
		if(needValCnt==0){
			System.err.println("没有指定需要的数据！");
			return;
		}
		int[] fieldIndex = new int[needFieldCnt];
		int maxFieldIndex = -1;
		String t = "";
		for (int k = 0; k < titleArray.length; k++) {
			t = titleArray[k];
			for (int i = 0; i < needFieldCnt; i++) {
				if (t.equals(fieldNames[i])) {
					fieldIndex[i] = k;
					if (maxFieldIndex < k) {
						maxFieldIndex = k;
					}
					break;
				}
			}
		}
		for (int i = 0; i < needFieldCnt; i++) {
			System.out.println("字段："+fieldNames[i]+"，对应位置："+fieldIndex[i]+",maxFieldIndex="+maxFieldIndex);
		}
		// 读取文件
		File dirFile = new File(dir);
		String allFiles[] = dirFile.list();
		List<String> needFiles = new ArrayList<String>();
		int fileCnt = 0;
		for (String fn : allFiles) {
			if (fn.equals(beginwith)) {
				needFiles.add(fn);
				fileCnt++;
			}
		}

		System.out.println("总共需要处理：" + fileCnt + "个文件");
		if (needFiles.isEmpty()) {
			return;
		}
		List<String> allNeedRows = new ArrayList<String>();
		BufferedReader br = null;
		String line = null;
		String fields[] = null;
		int fileNum=0;
		int lineCnt=0;
		Set<String> cells=new HashSet<String>();
		Set<String> ncells=new HashSet<String>();
		boolean find=false;
		for (String fn : needFiles) {
			try {
				fileNum++;
				br = new BufferedReader(new InputStreamReader(
						new FileInputStream(dir + "/" + fn)));
				line = br.readLine();
				lineCnt++;
				if(fileNum==1){
					allNeedRows.add(line);
				}
				//判断标题是否包含要求的字段
				fields = line.split("\t");
				for(int i=0;i<needFieldCnt;i++){
					System.out.println(fields[fieldIndex[i]]+"="+fieldNames[i]+"?"+(fields[fieldIndex[i]].equals(fieldNames[i])));
				}
				
				// 确定需要的标题的位置
				while ((line = br.readLine()) != null) {
					lineCnt++;
//					if(lineCnt % 1000==0){
//						System.out.println("处理第："+lineCnt+"行，line="+line);
//					}
					fields = line.split("\t");
					
//					cells.add(fields[1]);
//					ncells.add(fields[29]);
					
//					continue;
					
					if (fields.length < (maxFieldIndex + 1)) {
						System.out.println("错误");
						continue;
					}
					for (int j = 0; j < needFieldCnt; j++) {
						find=false;
						for(int m=0;m<needValCnt;m++){
							if (needVals[m].equals(fields[fieldIndex[j]])) {
								// 任何一个
								System.out.println("find ...");
								allNeedRows.add(line);
								find=true;
								break;
							}
						}
						if(find){
							break;
						}
					}
//					if(fields[1].equals(needVals[0])
//							|| fields[29].equals(needVals[0])){
//						System.out.println("find ...");
//						allNeedRows.add(line);
//					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		String lineSep=System.getProperty("line.separator");
//		System.out.println("cells.size="+cells.size()+",ncells.size="+ncells.size());
//		allNeedRows.add("cells:-----------"+lineSep);
//		for(String c:cells){
//			allNeedRows.add(c);
//		}
//		allNeedRows.add("ncells:-----------"+lineSep);
//		for(String c:ncells){
//			allNeedRows.add(c);
//		}
		System.out.println("总共得到：" + allNeedRows.size() + "个有效记录");
		// 输出
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(dir + "/" + beginwith + "-"
							+ fieldNames[0] + "-" + needVals[0] + ".out")));

			for(String one:allNeedRows){
				writer.write(one);
				writer.write(lineSep);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		String dir = "D:/tmp";
		String beginWith = "ncs_mid_data-1407222946817.csv";
		String[] fieldNames = {"CELL","NCELL"};
		String[] needCells = {"D30SMC1"};

//		System.out.println("a\tb");
		readandsave(dir, beginWith,fieldNames.length, fieldNames, needCells);

	}
//
//	public static List<List<String>> getListStringRows(File excelfile,
//			int sheetIndex, int fieldIndex, String needVal) {
//		List<List<String>> listRows = new ArrayList<List<String>>();
//		InputStream is;
//		// 根据输入流创建Workbook对象
//		Workbook wb = null;
//		try {
//			is = new FileInputStream(excelfile);
//			wb = WorkbookFactory.create(is);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		Sheet sheet = wb.getSheetAt(sheetIndex);
//		DateFormat format = new java.text.SimpleDateFormat(
//				"yyyy-MM-dd HH:mm:ss");
//		Row row = null;
//		Cell cell = null;
//		for (int j = 0; j < sheet.getLastRowNum() + 1; j++) {
//			System.out.println("第" + j + "行");
//			row = sheet.getRow(j);
//			ArrayList<String> cellsValue = new ArrayList<String>();
//			if (row != null) {
//				// 判断是否是需要的
//				cell = row.getCell(fieldIndex);
//				if (cell != null) {
//					if (!needVal.equals(cell.getStringCellValue())) {
//						continue;
//					}
//				}
//				for (int i = 0; i < row.getLastCellNum(); i++) {
//					// cell.getCellType是获得cell里面保存的值的type
//					cell = row.getCell(i);
//					if (cell == null) {
//						cellsValue.add("");
//					} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
//						// 读取String
//						cellsValue.add(cell.getStringCellValue());
//					} else {
//						cellsValue.add("");
//					}
//				}
//				listRows.add(cellsValue);
//			} else {
//				listRows.add(cellsValue);
//			}
//		}
//		return listRows;
//	}

}
