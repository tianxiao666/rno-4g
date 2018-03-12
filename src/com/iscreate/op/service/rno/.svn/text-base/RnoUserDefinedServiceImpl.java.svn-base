package com.iscreate.op.service.rno;

import com.iscreate.op.dao.rno.RnoUserDefinedDao;
import com.iscreate.op.pojo.rno.RnoUserDefinedFormul;

public class RnoUserDefinedServiceImpl implements RnoUserDefinedService {

	//注入
	private RnoUserDefinedDao rnoUserDefinedDao;

	public RnoUserDefinedDao getRnoUserDefinedDao() {
		return rnoUserDefinedDao;
	}

	public void setRnoUserDefinedDao(RnoUserDefinedDao rnoUserDefinedDao) {
		this.rnoUserDefinedDao = rnoUserDefinedDao;
	}
	
	/**
	 * 保存一条用户自定义公式记录
	 * @param rnoUserDefinedFormul
	 * @author chao.xj
	 * @date 2014-1-6下午04:28:19
	 */
	public void saveOneUserDefinedFormul(RnoUserDefinedFormul rnoUserDefinedFormul)
	{
		
		rnoUserDefinedDao.saveOneUserDefinedFormul(rnoUserDefinedFormul);
	}
	/**
	 * 插入一条用户自定义公式记录
	 * @param rnoUserDefinedFormul
	 * @return
	 * @author chao.xj
	 * @date 2014-1-6下午05:06:21
	 */
	public int insertOneUserDefinedFormul(final RnoUserDefinedFormul rnoUserDefinedFormul)
	{
		return rnoUserDefinedDao.insertOneUserDefinedFormul(rnoUserDefinedFormul);
	}
}
