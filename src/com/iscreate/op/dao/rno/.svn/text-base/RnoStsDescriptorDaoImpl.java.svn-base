package com.iscreate.op.dao.rno;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.rno.RnoBsc;
import com.iscreate.op.pojo.rno.RnoBscRelaArea;
import com.iscreate.op.pojo.rno.RnoStsDescriptor;

public class RnoStsDescriptorDaoImpl implements RnoStsDescriptorDao {

	private static Log log = LogFactory.getLog(RnoStsDescriptorDaoImpl.class);

	// ---注入----//
	private HibernateTemplate hibernateTemplate;

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public List<RnoStsDescriptor> getAllRnoStsDescriptor() {
		log.debug("进入类RnoStsDescriptorDaoImpl方法：getAllBsc。");
		List<RnoStsDescriptor> rnoStss = hibernateTemplate.loadAll(RnoStsDescriptor.class);
		log.debug("退出类RnoStsDescriptorDaoImpl方法：getAllBsc。获取记录数量："
				+ (rnoStss == null ? 0 : rnoStss.size()));
		return rnoStss;
	}

	/**
	 * 在指定区域插入新的RnoStsDescriptor对象
	* @author ou.jh
	* @date Oct 10, 2013 2:22:57 PM
	* @Description: TODO 
	* @param @param rnoSts
	* @param @return        
	* @throws
	 */
	public Long insertRnoStsDescriptor(RnoStsDescriptor rnoSts) {
		log.debug("进入类RnoStsDescriptorDaoImpl方法：insertRnoStsDescriptor。rnoSts="
				+ rnoSts);
		Long id = (Long) hibernateTemplate.save(rnoSts);
		log.debug("退出类RnoStsDescriptorDaoImpl方法：insertBsc。得到插入的id=" + id == null ? "空"
				: id.longValue());
		return id;
	}
	
	/**
	 * 获取指定区域下的RnoStsDescriptor
	* @author ou.jh
	* @date Oct 10, 2013 2:22:52 PM
	* @Description: TODO 
	* @param @param areaId
	* @param @return        
	* @throws
	 */
	public List<RnoStsDescriptor> getRnoStsDescriptorInArea(final long areaId){
		return hibernateTemplate.executeFind(new HibernateCallback<List<RnoStsDescriptor>>(){
			public List<RnoStsDescriptor> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				
				SQLQuery query=arg0.createSQLQuery("select r.sts_desc_id,r.net_type,r.spec_type,r.sts_date,r.sts_period,r.create_time,r.mod_time,r.status,r.area_id from RNO_STS_DESCRIPTOR r where r.area_id = ?");
				query.setLong(0, areaId);
				//System.out.println(query.getQueryString());
				List<RnoStsDescriptor> bscs=query.addEntity(RnoStsDescriptor.class).list();
				return bscs;
			}
		});
	}

}
