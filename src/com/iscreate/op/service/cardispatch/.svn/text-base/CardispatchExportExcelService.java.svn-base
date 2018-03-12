package com.iscreate.op.service.cardispatch;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.Number;

public class CardispatchExportExcelService 
{
	public InputStream getExcelInputStream(List<String> list,List<Map<String, String>> map) 
	{
	//将OutputStream转化为InputStream   
      ByteArrayOutputStream out = new ByteArrayOutputStream();   
      putDataOnOutputStream(out,list,map);   
      return new ByteArrayInputStream(out.toByteArray());   
	}
	
	
	private void putDataOnOutputStream(OutputStream os,List<String> list,List<Map<String, String>> list_map) 
	{  	
      try {   
           WritableWorkbook wwb = Workbook.createWorkbook(os); 
           
		   WritableSheet ws  = wwb.createSheet("sheet1",0);
		   
		   ws.setColumnView(0,20);
		   ws.setColumnView(1,25);
		   
		   ws.setRowView(0,500);//设置行高
		   ws.setRowView(1,500);//设置行高
		   
		   for(int i=2;i<=list.size()+1;i++)
		   {
			   ws.setColumnView(i,10);
		   }
		   
		   WritableCellFormat cellFormat=new WritableCellFormat();
	       cellFormat.setAlignment(jxl.format.Alignment.CENTRE);
	       cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
	       cellFormat.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); 
	       
	       
	       WritableCellFormat blue=new WritableCellFormat();
	       
	       blue.setBackground(Colour.SKY_BLUE);
	       blue.setAlignment(jxl.format.Alignment.CENTRE);
	       blue.setVerticalAlignment(VerticalAlignment.CENTRE);
	       blue.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); 
	       
	       WritableCellFormat orange=new WritableCellFormat();
	       
	       orange.setBackground(Colour.ORANGE);
	       orange.setAlignment(jxl.format.Alignment.CENTRE);
	       orange.setVerticalAlignment(VerticalAlignment.CENTRE);
	       orange.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); 
	       
	       
	       WritableFont wfc = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.GRAY_25);
	       
	       WritableCellFormat wcfFC = new WritableCellFormat(wfc);
	       wcfFC.setAlignment(jxl.format.Alignment.CENTRE);
	       wcfFC.setVerticalAlignment(VerticalAlignment.CENTRE);
	       wcfFC.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); 
	       
	       
		   
		   ws.setColumnView(list.size()+2,20);
		   ws.setColumnView(list.size()+3,20);
		   //设置字体，内容的居中形式
		   WritableFont  wf = new WritableFont(WritableFont.TIMES, 10); 
		   WritableCellFormat wff = new WritableCellFormat(wf);
		   wff.setVerticalAlignment(VerticalAlignment.CENTRE);
		   wff.setAlignment(jxl.write.Alignment.CENTRE);
		   wff.setBackground(Colour.PALE_BLUE);
		   wff.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
		   
		   WritableCellFormat wff1 = new WritableCellFormat(wf);
		   wff1.setVerticalAlignment(VerticalAlignment.CENTRE);
		   wff1.setAlignment(jxl.write.Alignment.CENTRE);
		   wff1.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); 
		   //设置表头的内容
		   Label label1 = new Label(0,0,"  车辆牌照",wff);
		   Label label5 = new Label(1,0,"  所属项目",wff);
		   Label label2 = new Label(2,0,"GPS里程(公里)",wff);
		   Label label3 = new Label(list.size()+2,0,"GPS总里程(公里)",wff);
		   Label label4 = new Label(list.size()+3,0,"仪表总里程(公里)",wff);
		   Label []arrayList =new Label[list.size()];
		   jxl.write.Number []arrayListJxl =new jxl.write.Number[list.size()];
		   
		  
		   //输入查询的日期显示
		   for(int j=0;j<list.size();j++)
		   {
			   arrayList[j]=new Label(2+j,1,list.get(j).toString().substring(8,10)+"日",wff);
			   ws.addCell(arrayList[j]);
		   }

		   //设置表头的合并
		   ws.mergeCells(0, 0, 0, 1);
		   ws.mergeCells(1, 0, 1, 1);
		   
		   ws.mergeCells(2, 0, list.size()+1, 0);
		   ws.mergeCells(list.size()+2, 0, list.size()+2, 1);
		   ws.mergeCells(list.size()+3, 0, list.size()+3, 1);
		   ws.addCell(label1);
		   ws.addCell(label2);
		   ws.addCell(label3);
		   ws.addCell(label4);
		   ws.addCell(label5);
		   
		   
		   
		   //输出查询出车牌和车的里程数据
		   
		   
		   int gpsmileage;
		   
		   for(int i=0;i<list_map.size();i++)
		   {
			   Map<String, String> map = list_map.get(i);
			   
			   int total_gps_mileage = Integer.parseInt(map.get("COUNT"));
			   
			   int total_instrument_read = Integer.parseInt(map.get("instrumentcount"));
			   
			   
			   
			   String carNumber = map.get("carNumber");
			   
			   String carBizName = map.get("carBizName");
			   
			   Label carNumberLabel = new Label(0,i+2,carNumber,cellFormat);
			   
			   Label carBizNameLabel = new Label(1,i+2,carBizName,cellFormat);
			   
			   Number total_gps_mileageLabel;
			   Number total_instrument_readLabel;
			   
			   if(total_gps_mileage==0)
			   {
				   total_gps_mileageLabel = new Number(list.size()+2,i+2,total_gps_mileage,wcfFC);
			   }
			   else
			   {
				   total_gps_mileageLabel = new Number(list.size()+2,i+2,total_gps_mileage,cellFormat);
			   }
			   
			   if(total_instrument_read==0)
			   {
				   total_instrument_readLabel = new Number(list.size()+3,i+2,total_instrument_read,wcfFC);
			   }
			   else
			   {
				   total_instrument_readLabel = new Number(list.size()+3,i+2,total_instrument_read,cellFormat);
			   }
			   
			   ws.addCell(carNumberLabel);
			   ws.addCell(carBizNameLabel);
			   ws.addCell(total_gps_mileageLabel);
			   ws.addCell(total_instrument_readLabel);
			   
			   for(int k = 0;k<list.size();k++)
			   {
				   gpsmileage = Integer.parseInt(((String)map.get(list.get(k))).equals("null")?"0":(String)map.get(list.get(k)));
				   
				   if(gpsmileage>400)
				   {
					   ws.addCell(new jxl.write.Number(2+k,2+i,gpsmileage,orange));
					   
				   }
				   else if(gpsmileage>200)
				   {
					   ws.addCell(new jxl.write.Number(2+k,2+i,gpsmileage,blue));
					   
				   }
				   else if(gpsmileage==0)
				   {
					   ws.addCell(new jxl.write.Number(2+k,2+i,gpsmileage,wcfFC));
				   }
				   else
				   {
					   ws.addCell(new jxl.write.Number(2+k,2+i,gpsmileage,cellFormat));
				   }
				   
			   }
			   ws.setRowView(i+2,400);//设置行高
			   
		   }
		   
		   
		   
		  wwb.write();   
		  wwb.close();   
      } catch (Exception e) {   

          e.printStackTrace();   

      }   

  }   
}
