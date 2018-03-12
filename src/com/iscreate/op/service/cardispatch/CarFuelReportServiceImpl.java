package com.iscreate.op.service.cardispatch;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Blank;
import jxl.write.Label;
import jxl.write.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.dao.cardispatch.CarFuelReportDao;

public class CarFuelReportServiceImpl implements CarFuelReportService {
	private final Log log = LogFactory.getLog(this.getClass());
	private CarFuelReportDao carFuelReportDao;
	
	/**
	 * 
	 * @description: 根据组织id分页获取时间月份的车辆费用信息
	 * @author：yuan.yw
	 * @param orgId
	 * @param currentPage
	 * @param pageSize
	 * @param date
	 * @return     
	 * @return Map<String,Object>    
	 * @date：Aug 2, 2013 11:42:42 AM
	 */
	public Map<String,Object> getCarFuelInfoForPage(String orgId,int currentPage,int pageSize,String date){
		log.info("进入getCarFuelInfoForPage(String orgId,int currentPage,int pageSize,String date)，根据组织id分页获取时间月份的车辆费用信息");
		log.info("参数orgId="+orgId+",currentPage="+currentPage+",date="+date);
		int indexStart = 1;//开始记录下标
		int indexEnd = 0;//结束记录下标
		indexStart = (currentPage-1)*pageSize+1;
		indexEnd= currentPage*pageSize;
		if(indexEnd<=indexStart){
			indexEnd = indexStart;
		}
		Map<String,Object> result =null;
		List<Map<String,Object>> carInfoList = this.carFuelReportDao.getCarInfoByOrgId(orgId, indexStart, indexEnd);//分页获取车辆信息
		String count = "0";//数量
		List<Map<String,Object>> carList = null;
		if(carInfoList!=null && !carInfoList.isEmpty()){//获取车辆相关信息
			Map<String,Object> carInfoMap = carInfoList.get(0); 
			carList = (List<Map<String,Object>>)carInfoMap.get("carList");
			count = carInfoMap.get("count")+"";
		}
		StringBuffer carIdStr = new StringBuffer();
		if(carList!=null && !carList.isEmpty()){
			for(Map<String,Object> mp:carList){
				carIdStr.append(","+mp.get("ID"));//车辆id
			}
			String carIds = carIdStr.substring(1)+"";
			//获取当前月份初始 结束日期
			String[] dateString = null;
			int lastDay = 0;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cDay = Calendar.getInstance();   
			try {
				cDay.setTime(sdf.parse(date+"-01"));
			} catch (ParseException e) {
				lastDay=0;
			}
		    lastDay = cDay.getActualMaximum(Calendar.DAY_OF_MONTH);   
		    if(lastDay!=0){
		    	dateString = new String[2];
		    	dateString[0] = date+"-01";
		    	dateString[1] = date+"-"+lastDay;
		    }
		    //获取油费相关信息
			List<Map<String,Object>> carFuelList = this.carFuelReportDao.getCarFuelInfoByDateAndCarId(dateString, carIds,orgId);//获取车辆费用相关信息
			result = new LinkedHashMap<String,Object>();//结果map 保存
			List<Map<String,Object>> resultCarFuelInfoList  = new ArrayList<Map<String,Object>>();
			String bizId="";//组织id
			String bizName = "";
			int i=0;
			for(Map<String,Object> map:carFuelList){
				String carOrgId = map.get("CARBIZID")+"";//车辆组织
				String carOrgName = map.get("ORGNAME")+"";//车辆组织
				if("".equals(bizId)){
					bizId = carOrgId;
					bizName = carOrgName;
				}
				if( !bizId.equals(carOrgId)){
					result.put(bizName, resultCarFuelInfoList);
					resultCarFuelInfoList = new ArrayList<Map<String,Object>>();
					resultCarFuelInfoList.add(map);
					if(i==carFuelList.size()-1){
						result.put(map.get("ORGNAME")+"", resultCarFuelInfoList);
					}else{
						bizId=carOrgId;
						bizName = carOrgName;
					}
				}else if( i==carFuelList.size()-1){
					resultCarFuelInfoList.add(map);
					result.put(bizName, resultCarFuelInfoList);
				}else{
					resultCarFuelInfoList.add(map);
				}
				i++;
			}
		}
		Map<String,Object> carFuelResult = new HashMap<String,Object>();
		carFuelResult.put("resultMap", result);
		carFuelResult.put("count", count);
		log.info("退出getCarFuelInfoForPage(String orgId,int currentPage,int pageSize,String date)，carFuelResult:"+carFuelResult);
		return carFuelResult;
	}
	/**
	 * 
	 * @description: 导出油费报表
	 * @author：yuan.yw
	 * @param orgId
	 * @param orgName
	 * @param date
	 * @return     
	 * @return InputStream     
	 * @date：Aug 5, 2013 10:28:20 AM
	 */
	public InputStream exportCarFuelBillsService(String orgId,String orgName,String date){
		log.info("进入exportCarFuelBillsService(String orgId,String date)，导出油费报表");
		log.info("参数orgId="+orgId+",date="+date);	
		//获取当前月份初始 结束日期
		String[] dateString = null;
		int lastDay = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cDay   =   Calendar.getInstance();   
		try {
			cDay.setTime(sdf.parse(date+"-01"));
		} catch (ParseException e) {
			lastDay=0;
		}
	    lastDay = cDay.getActualMaximum(Calendar.DAY_OF_MONTH);   
	    if(lastDay!=0){
	    	dateString = new String[2];
	    	dateString[0] = date+"-01";
	    	dateString[1] = date+"-"+lastDay;
	    }
		//获取油费相关信息
		List<Map<String,Object>> carFuelList = this.carFuelReportDao.getCarFuelInfoByDateAndOrgId(dateString, orgId);//获取车辆费用相关信息
		//将OutputStream转化为InputStream   
	    ByteArrayOutputStream out = new ByteArrayOutputStream();   
	    List<String> list = new ArrayList<String>();
	    //表头
	    list.add(date+orgName+"车辆油费统计");
	    list.add("组织名称");
	    list.add("车牌号");
	    list.add("油费(元)");
	    list.add("GPS里程数(公里)");
	    list.add("GPS油耗(元/公里)");
	    list.add("仪表读数(公里)");
	    list.add("仪表里程油耗(元/公里)");
	    putDataOnExcelOutputStream(out,list,carFuelList);   
		log.info("退出exportCarFuelBillsService(String orgId,String date)，返回结果out.toByteArray():"+out.toByteArray());
		return new ByteArrayInputStream(out.toByteArray());
	}
	/**
	 * 
	 * @description: 生成excel文件流
	 * @author：yuan.yw
	 * @param os
	 * @param list
	 * @param dataList     
	 * @return void     
	 * @date：Aug 5, 2013 10:14:16 AM
	 */
	private void putDataOnExcelOutputStream(OutputStream os,List<String> list,List<Map<String, Object>> dataList) {  	
		log.info("进入putDataOnOutputStream(OutputStream os,List<String> list,List<Map<String, Object>> dataList)，生成excel文件。");
      try {   
           WritableWorkbook wwb = Workbook.createWorkbook(os); 
		   WritableSheet ws  = wwb.createSheet("sheet1",0);
		   ws.setColumnView(0, 30);//单元格宽度
		   ws.setColumnView(1, 20);
		   ws.setColumnView(2, 15);
		   ws.setColumnView(3, 15);
		   ws.setColumnView(4, 15);
		   ws.setColumnView(5, 15);
		   ws.setColumnView(6, 15);
		   //设置字体，内容的居中形式
		   WritableFont  wf = new WritableFont(WritableFont.TIMES,10,WritableFont.BOLD); 
		   WritableCellFormat wff = new WritableCellFormat(wf);
		   wff.setVerticalAlignment(VerticalAlignment.CENTRE);
		   wff.setAlignment(jxl.write.Alignment.CENTRE);
		   wff.setBackground(Colour.ICE_BLUE);
		   
		   WritableCellFormat wff1 = new WritableCellFormat();
		   wff1.setVerticalAlignment(VerticalAlignment.CENTRE);
		   wff1.setAlignment(jxl.write.Alignment.CENTRE);
		   //设置表头的内容
		   Label totalHead =  new Label(0,0,list.get(0),wff);
		   ws.addCell(totalHead);
		   //设置表头的合并
		   ws.mergeCells(0, 0, 6, 0);
		   for(int j=1;j<list.size();j++)
		   {   
			   Label label =  new Label(j-1,1,list.get(j),wff);
			   ws.addCell(label);
		   }
		   //输出查询出的车辆油费数据
		   if(dataList!=null && !dataList.isEmpty()){
			   String bizId = "";
			   String bizName = "";
			   int startIndex = 0;
			   for(int i=0;i<dataList.size();i++){
				  Map<String,Object> map = dataList.get(i);
				  String orgId = map.get("CARBIZID")+"";//车辆组织id
				  String orgName = map.get("ORGNAME")+"";//车辆组织名
				  if(i==0){
					  bizId = orgId;
					  bizName = orgName;
					  startIndex=i+2;
				  }
				  Label  label =  new Label(0,i+2,orgName,wff1);//组织
				  ws.addCell(label);
				  label =  new Label(1,i+2,map.get("CARNUMBER")+"",wff1);//车牌
				  ws.addCell(label);
				  if(map.get("FUELBILLS")!=null){
					  label =  new Label(2,i+2,map.get("FUELBILLS")+"",wff1);//油费
				  }else{
					  label = new Label(2,i+2,"0",wff1);
				  }
				  ws.addCell(label);
				  if(map.get("GPSMILEAGE")!=null){
					  label =  new Label(3,i+2,map.get("GPSMILEAGE")+"",wff1);//gps里程
				  }else{
					  label = new Label(3,i+2,"0",wff1);
				  }
				  ws.addCell(label);
				  if(map.get("GPSFUEL")!=null){
					  label =  new Label(4,i+2,map.get("GPSFUEL")+"",wff1);//gps油耗
				  }else{
					  label = new Label(4,i+2,"0",wff1);
				  }
				  ws.addCell(label);
				  if(map.get("INSREADING")!=null){
					  label =  new Label(5,i+2,map.get("INSREADING")+"",wff1);//仪表读数
				  }else{
					  label = new Label(5,i+2,"0",wff1);
				  }
				  ws.addCell(label);
				  if(map.get("INSFUEL")!=null){
					  label =  new Label(6,i+2,map.get("INSFUEL")+"",wff1);//仪表里程油耗
				  }else{
					  label = new Label(6,i+2,"0",wff1);
				  }
				  ws.addCell(label);
				  if(!bizId.equals(orgId)){
					  if(startIndex!=i+1){
						  for(int j=startIndex+1;j<=i+1;j++){
							  Blank b = new Blank(0,j);  
				              ws.addCell(b);   
						  }
						  ws.mergeCells(0, startIndex, 0, i+1);
						 
					  }
					  bizId = orgId;
					  bizName = orgName;
					  startIndex = i+2;
				  }else if(i==dataList.size()-1){
					  if(startIndex!=i+2){
						  for(int j=startIndex+1;j<=i+2;j++){
							  Blank b = new Blank(0,j);    
				              ws.addCell(b);  
						  }
						  ws.mergeCells(0, startIndex, 0, i+2);
					  }
				  }
			   }
		   }
		  wwb.write();   
		  wwb.close();   
		  log.info("退出putDataOnOutputStream(OutputStream os,List<String> list,List<Map<String, Object>> dataList)，生成excel文件流成功");
      } catch (Exception e) {   
    	  log.error("退出putDataOnOutputStream(OutputStream os,List<String> list,List<Map<String, Object>> dataList)，生成excel文件流失败，exception："+e.getMessage());   
      }   

  }   
	public CarFuelReportDao getCarFuelReportDao() {
		return carFuelReportDao;
	}

	public void setCarFuelReportDao(CarFuelReportDao carFuelReportDao) {
		this.carFuelReportDao = carFuelReportDao;
	}

	
	
}
