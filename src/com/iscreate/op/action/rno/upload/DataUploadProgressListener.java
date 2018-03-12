package com.iscreate.op.action.rno.upload;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.constant.RnoConstant;

public class DataUploadProgressListener implements ProgressListener {
	private static Log log=LogFactory.getLog(DataUploadProgressListener.class);
	private HttpSession session;
    private String tokenId;
	private long lastRBytes=0;
	
	public DataUploadProgressListener(HttpServletRequest request){
		session=request.getSession();
		
		tokenId="uploadtoken";//hard code
		
		DataUploadStatus dus=new DataUploadStatus();
		Map<String,DataUploadStatus> uploadStatus=(Map<String,DataUploadStatus>)session.getAttribute(RnoConstant.SessionConstant.uploadStatusSessionKey);
		if(uploadStatus==null){
			log.debug("listener ,session ,progress empty.set it");
			uploadStatus=new HashMap<String,DataUploadStatus>();
			session.setAttribute(RnoConstant.SessionConstant.uploadStatusSessionKey, uploadStatus);
		}
		log.debug("set progress obj into session,token="+tokenId+",status="+dus);
		uploadStatus.put(tokenId,dus);
	}
	@Override
	public void update(long readedBytes, long totalBytes, int currentItem) {
//		System.out.println("...update progress.readedBytes="+readedBytes+",totalBytes="+totalBytes+",currentItem="+currentItem);
//		   if(readedBytes==totalBytes||
//				   readedBytes/){
//			   
//		   }
		
		
		//总数量小于1M，先不更新
		//与上一次的读取数量小与1M，不更新
		//
		if(readedBytes<totalBytes){
			if(readedBytes<1024000){
				return;
			}
			else if(readedBytes-lastRBytes<1024000){
				return;
			}
			//如果超过了1M，则更新
			lastRBytes=readedBytes;
		}
		
		   DataUploadStatus status =((Map<String,DataUploadStatus>) session.getAttribute(RnoConstant.SessionConstant.uploadStatusSessionKey)).get(tokenId);
		   //log.debug("update status:token="+tokenId+",status="+status);
	       status.setReadedBytes(readedBytes);
	       status.setTotalBytes(totalBytes);
	       status.setCurrentItem(currentItem);
	}
	public String getTokenId() {
		return tokenId;
	}

}
