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

public class RnoBscDaoImpl implements RnoBscDao {

	private static Log log = LogFactory.getLog(RnoBscDaoImpl.class);

	// ---注入----//
	private HibernateTemplate hibernateTemplate;

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public List<RnoBsc> getAllBsc() {
		log.debug("进入类RnoBscDaoImpl方法：getAllBsc。");
		List<RnoBsc> rnoBscs = hibernateTemplate.loadAll(RnoBsc.class);
		log.debug("退出类RnoBscDaoImpl方法：getAllBsc。获取记录数量："
				+ (rnoBscs == null ? 0 : rnoBscs.size()));
		return rnoBscs;
	}

	/**
	 * 在指定区域插入新的bsc对象
	 */
	public Long insertBsc(long areaId, RnoBsc bsc) {
		log.debug("进入类RnoBscDaoImpl方法：insertBsc。areaId=" + areaId + ",bsc="
				+ bsc);
		Long id = (Long) hibernateTemplate.save(bsc);
		log.debug("退出类RnoBscDaoImpl方法：insertBsc。得到插入的id=" + id == null ? "空"
				: id.longValue());
		if (id != null) {
			RnoBscRelaArea rela = new RnoBscRelaArea();
			rela.setAreaId(areaId);
			rela.setBscId(id);
			hibernateTemplate.save(rela);
		}
		return id;
	}
	
	/**
	 * 获取指定区域下的bsc
	 * @param areaId
	 * @return
	 * Sep 16, 2013 11:18:27 AM
	 * gmh
	 */
	public List<RnoBsc> getBscsResideInArea(long areaId){
		
		String areaIdStr=AuthDsDataDaoImpl.getSubAreaAndSelfIdListStrByParentId(areaId);
		final String sql = "select bsc.BSC_ID, bsc.ENGNAME, bsc.CHINESENAME "+
						"	  from RNO_BSC bsc"+
						"	 where bsc.bsc_id in (select bsc_id "+
						"	                    from rno_bsc_rela_area "+
						"	                   where area_id in ("+areaIdStr+"))";
		log.info("获取指定区域下的bsc: sql="+sql);
		return hibernateTemplate.executeFind(new HibernateCallback<List<RnoBsc>>(){
			public List<RnoBsc> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				
				SQLQuery query=arg0.createSQLQuery(sql);
				//System.out.println(query.getQueryString());
				List<RnoBsc> bscs=query.addEntity(RnoBsc.class).list();
				return bscs;
			}
		});
	}
	
	/**
	 * 
	 * @author Liang YJ
	 * @date 2014-1-28 下午3:35:13
	 * @param areaId
	 * @return
	 * @description 根据areaId返回该区域下bscId和areaId关系的记录
	 */
	public List<RnoBscRelaArea> getBscRelaAreaByAreaId(long areaId)
	{
		final long temp = areaId;
		return hibernateTemplate.executeFind(new HibernateCallback<List<RnoBscRelaArea>>()
			{
			public List<RnoBscRelaArea> doInHibernate(Session arg0)throws HibernateException, SQLException
			{
				List<Long> subAreas = AuthDsDataDaoImpl
						.getSubAreaIdsByCityId(temp);
				String areaStrs = temp+",";
				for (Long id : subAreas) {
					areaStrs += id + ",";
				}
				if (areaStrs.length() > 0) {
					areaStrs = areaStrs.substring(0, areaStrs.length()-1);
				}else{
					areaStrs=temp+"";
				}
				
				String sql = "select rbra.bsc_area_id, rbra.bsc_id, rbra.area_id from rno_bsc_rela_area rbra where rbra.area_id in ( "+areaStrs+" )";
				SQLQuery query=arg0.createSQLQuery(sql);
				List<RnoBscRelaArea> rbraList=query.addEntity(RnoBscRelaArea.class).list();
				return rbraList;
			}
		});
	}

}
