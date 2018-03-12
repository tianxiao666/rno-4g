package com.iscreate.op.action.download;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.struts2.ServletActionContext;

/**
 * 
 * 客户端apk下载action
 * 
 * @author li.hb
 * @create_time 2013.07.03
 * 
 * 
 */
public class DownloadApkAction 
{
	
	private String fileName;
    public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	private String filePath;
      
    public InputStream getInputStream() 
    {
    	String file = "/"+this.getFilePath()+fileName;
    	return ServletActionContext.getServletContext().getResourceAsStream(file);
    }
    
    public String execute() throws UnsupportedEncodingException
    {
    	HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("ISO8859-1");
		
		this.setFileName("IOSM.apk");
		this.setFilePath("download/app/");
		
		return "success";
    }
}
