package com.iscreate.op.dao.rno;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.iscreate.op.pojo.rno.RnoStsDescriptor;

public interface RnoStsDescriptorDao {
	public List<RnoStsDescriptor> getAllRnoStsDescriptor();

	/**
	 * 在指定区域插入新的RnoStsDescriptor对象
	* @author ou.jh
	* @date Oct 10, 2013 2:22:57 PM
	* @Description: TODO 
	* @param @param rnoSts
	* @param @return        
	* @throws
	 */
	public Long insertRnoStsDescriptor(RnoStsDescriptor rnoSts);
	
	/**
	 * 获取指定区域下的RnoStsDescriptor
	* @author ou.jh
	* @date Oct 10, 2013 2:22:52 PM
	* @Description: TODO 
	* @param @param areaId
	* @param @return        
	* @throws
	 */
	public List<RnoStsDescriptor> getRnoStsDescriptorInArea(final long areaId);
}
