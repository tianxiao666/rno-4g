package com.iscreate.op.action.rno.upload.accept;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.action.rno.upload.FileAcceptStatus;

public class EriNcsFileAcceptableImpl extends BaseFileAcceptableImpl{

	private static Log log=LogFactory.getLog(EriNcsFileAcceptableImpl.class);
	@Override
	public FileAcceptStatus isAcceptable(long size, String dataType,
			Map<String, Object> attmsg) {
		
		FileAcceptStatus status=super.isAcceptable(size, dataType, attmsg);
		
		if(!status.isFlag()){
			return status;
		}
		//判断是否有相应的日期的小区数据
		//如果没有，需要拒绝
		
		return status;
	}
}
