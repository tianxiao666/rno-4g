
package com.iscreate.op.service.workmanage.util;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author che.yd
 */
public class OrderIdUtil {
	private static final Log logger = LogFactory.getLog(OrderIdUtil.class);

	public static synchronized String generateWorkOrderId(String workOrderType) {
		long start = System.currentTimeMillis();
		String workOrderId = "";
		if (workOrderType != null && workOrderType.length() > 0)
			workOrderType = workOrderType.trim();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String _date = sdf.format(new Date());
		String workOrderIdKey = workOrderType + "-" + _date + "-";
//		long code = Unique.nextLong(workOrderIdKey);
		long code=0;
		try {
			code = IdGeneratorUtil.nextLong(workOrderIdKey);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("生成工单流水号失败");
		}
		String codeStr = String.valueOf(code);		
		while(codeStr.trim().length()<4){
			codeStr = "0"+codeStr;
		}
		workOrderId = workOrderIdKey+codeStr;
		long end = System.currentTimeMillis();
		logger.debug("--------------------生成工单号："+workOrderId+",耗时："+(end-start)+"ms");
		return workOrderId;
	}
	
	public static synchronized String generateTaskOrderId(String workOrderId) {
		long start = System.currentTimeMillis();
		String taskOrderId = "";
		if (workOrderId != null && workOrderId.length() > 0)
			workOrderId = workOrderId.trim();
		
//		long code = Unique.nextLong(workOrderId);
		long code=0;
		try {
			code = IdGeneratorUtil.nextLong(workOrderId);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("生成任务单流水号失败");
		}
		String codeStr = String.valueOf(code);		
		while(codeStr.trim().length()<4){
			codeStr = "0"+codeStr;
		}
		taskOrderId = workOrderId+"-"+codeStr;
		long end = System.currentTimeMillis();
		logger.debug("--------------------生成任务号："+taskOrderId+",耗时："+(end-start)+"ms");
		return taskOrderId;
	}
	
	
}
