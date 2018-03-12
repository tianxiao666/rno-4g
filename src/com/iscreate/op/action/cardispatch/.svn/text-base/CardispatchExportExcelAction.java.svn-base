package com.iscreate.op.action.cardispatch;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.iscreate.op.service.cardispatch.CardispatchExportExcelService;
import com.opensymphony.xwork2.ActionContext;

public class CardispatchExportExcelAction {
	InputStream excelStream;
    String filename;
    String startDate;
    String endDate;
    	  
	public InputStream getExcelStream() {
		return excelStream;
	}
    
    private CardispatchExportExcelService CardispatchExportExcelService;

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) throws UnsupportedEncodingException {
		this.filename = filename;

	}

	public String execute() throws UnsupportedEncodingException
	{   
		
		HttpServletResponse response = ServletActionContext.getResponse();
		
		response.setCharacterEncoding("GBK");
		
    	
		
    	Map session = ActionContext.getContext().getSession();
		
    	List<String>  list  =  (List<String>) session.get("carDate");
    	
    	startDate = list.get(0);
    	
    	endDate = list.get(list.size()-1);
    	
//    	date=new SimpleDateFormat().format(carDate);
    	
		List<Map<String, String>> list_map= (List<Map<String, String>>) session.get("carData");
		
		CardispatchExportExcelService es = new CardispatchExportExcelService();   
		
        excelStream = es.getExcelInputStream(list,list_map); 
		
        String downFileName = "mileage-"; 
        
        String lastDay = endDate.substring(5, 10);
        
		this.filename=downFileName+startDate+"to"+lastDay+".xls";
		
        return "success";   
    }   
}
