package com.iscreate.op.service.rno;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.DataUploadQueryCond;
import com.iscreate.op.dao.rno.RnoFileUploadDao;
import com.iscreate.op.pojo.rno.RnoDataCollectRec;

public class RnoFileUploadServiceImpl implements RnoFileUploadService {

	private static Log log=LogFactory.getLog(RnoFileUploadServiceImpl.class);
	private RnoFileUploadDao rnoFileUploadDao;
	
	public void setRnoFileUploadDao(RnoFileUploadDao rnoFileUploadDao) {
		this.rnoFileUploadDao = rnoFileUploadDao;
	}

	/**
	 * 分页查询数据记录
	 * @param cond
	 * @param page
	 * @return
	 * @author brightming
	 * 2014-8-22 上午9:23:49
	 */
	public List<RnoDataCollectRec> queryUploadDataByPage(DataUploadQueryCond cond,Page page){
		return rnoFileUploadDao.queryUploadDataByPage(cond, page);
	}
	
	/**
	 * 查询满足条件的数据
	 * @param cond
	 * @return
	 * @author brightming
	 * 2014-8-22 上午9:24:18
	 */
	public long queryUploadDataCnt(DataUploadQueryCond cond){
		return rnoFileUploadDao.queryUploadDataCnt(cond);
	}

	/**
	 * 查询某job的报告数量
	 * @param jobId
	 * @return
	 * @author brightming
	 * 2014-8-22 下午4:32:57
	 */
	public int queryJobReportCnt(Long jobId){
		return rnoFileUploadDao.queryJobReportCnt(jobId);
	}

	/**
	 * 分页查询某job的报告
	 * @param jobId
	 * @param newPage
	 * @return
	 * @author brightming
	 * 2014-8-22 下午4:33:20
	 */
	public List<Map<String, Object>> queryJobReportByPage(Long jobId,
			Page newPage){
		return rnoFileUploadDao.queryJobReportByPage(jobId,newPage);
	}
}
