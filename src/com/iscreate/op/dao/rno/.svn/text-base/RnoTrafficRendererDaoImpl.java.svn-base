package com.iscreate.op.dao.rno;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import com.iscreate.op.pojo.rno.RnoTrafficRendererConfig;

public class RnoTrafficRendererDaoImpl implements RnoTrafficRendererDao {
	
	// ---注入----//
	private HibernateTemplate hibernateTemplate;

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	/**
	 * 根据区域ID与话务指标类型获取默认话务性能渲染配置
	* @author ou.jh
	* @date Oct 28, 2013 2:15:29 PM
	* @Description: TODO 
	* @param @param trafficCode
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getDefaultRnoTrafficRenderer(String trafficCode){
		return getRnoTrafficRendererByTrafficCodeAndAreaId(trafficCode, -1);
	}
	
	/**
	 * 根据话务指标类型获取对应类型数据
	* @author ou.jh
	* @date Oct 28, 2013 3:01:40 PM
	* @Description: TODO 
	* @param @param trafficCode
	* @param @return        
	* @throws
	 */
	public Map<String, Object> getRnoTraffic(String trafficCode){
		String sql = "select * from RNO_TRAFFIC_INDEX_TYPE rtit where rtit.code = '" + trafficCode + "' ";
		return this.executeSqlForMap(sql, hibernateTemplate);
	}
	
	/**
	 * 根据区域ID与话务指标类型获取话务性能渲染配置
	* @author ou.jh
	* @date Oct 28, 2013 2:14:51 PM
	* @Description: TODO 
	* @param @param trafficCode
	* @param @param areaId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getRnoTrafficRendererByTrafficCodeAndAreaId(String trafficCode,long areaId){
		String whereSql = "";
		if(areaId > 0){
			whereSql += "   and rtrc.area_id = " + areaId;
		}else if(areaId == -1){
			whereSql += "   and (rtrc.area_id is null or rtrc.area_id = " + areaId + " ) ";
		}
		if(trafficCode != null && !trafficCode.trim().isEmpty()){
			whereSql += "   and rtit.code = '" + trafficCode + "'";
		}
		String sql = "select rtit.*,rtrc.*,rtrc.name RENDERERNAME "+
					"  from RNO_TRAFFIC_INDEX_TYPE rtit, RNO_TRAFFIC_RENDERER_CONFIG rtrc "+
					" where rtit.traffic_index_id = rtrc.TRAFFIC_TYPE ";
		String sqlString = sql + whereSql + " order by rtrc.CONFIG_ORDER desc Nulls last ";
		return this.executeSqlForList(sqlString, hibernateTemplate);
	}
	
	/**
	 * 添加话务性能渲染配置
	* @author ou.jh
	* @date Oct 28, 2013 4:34:28 PM
	* @Description: TODO 
	* @param @param bno
	* @param @return        
	* @throws
	 */
	public Long insertBno(RnoTrafficRendererConfig bno) {
		Long id = (Long) hibernateTemplate.save(bno);
		return id;
	}
	
	/**
	 * 添加话务性能渲染配置
	* @author ou.jh
	* @date Oct 28, 2013 4:34:28 PM
	* @Description: TODO 
	* @param @param bno
	* @param @return        
	* @throws
	 */
	public void updateBno(RnoTrafficRendererConfig bno) {
		hibernateTemplate.update(bno);
	}
	
	public List<Map<String, Object>> executeSqlForList(final String sqlString ,HibernateTemplate hibernateTemplate) {
		List<Map<String, Object>> list = hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(
							Session session) throws HibernateException,
							SQLException {
						SQLQuery query = session.createSQLQuery(sqlString);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> find = query.list();
						return find;
					}
				});
		return list;
	}
	
	public Map<String, Object> executeSqlForMap(final String sqlString ,HibernateTemplate hibernateTemplate) {
		List<Map<String, Object>> list = hibernateTemplate
		.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(
					Session session) throws HibernateException,
					SQLException {
				SQLQuery query = session.createSQLQuery(sqlString);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String, Object>> find = query.list();
				return find;
			}
		});
		if(list != null && list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}

}
