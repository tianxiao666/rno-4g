package com.iscreate.op.test.rno.upload;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;

public class DataUploadProgressListener implements ProgressListener {
	private HttpSession session;
    private String tokenId;
	
	
	public DataUploadProgressListener(HttpServletRequest request){
		session=request.getSession();
		tokenId=(String)request.getAttribute("fileUploadToken");
		if(tokenId==null){
			tokenId="notoken";
		}
		DataUploadStatus dus=new DataUploadStatus();
		Map<String,DataUploadStatus> uploadStatus=(Map<String,DataUploadStatus>)session.getAttribute("currentUploadStatus");
		if(uploadStatus==null){
			uploadStatus=new HashMap<String,DataUploadStatus>();
			session.setAttribute("currentUploadStatus", uploadStatus);
		}
		uploadStatus.put(tokenId,new DataUploadStatus());
		
	}
	@Override
	public void update(long readedBytes, long totalBytes, int currentItem) {
		System.out.println("...update progress.readedBytes="+readedBytes+",totalBytes="+totalBytes+",currentItem="+currentItem);
		   DataUploadStatus status =((Map<String,DataUploadStatus>) session.getAttribute("currentUploadStatus")).get(tokenId);
	       status.setReadedBytes(readedBytes);
	       status.setTotalBytes(totalBytes);
	       status.setCurrentItem(currentItem);
	}

}
