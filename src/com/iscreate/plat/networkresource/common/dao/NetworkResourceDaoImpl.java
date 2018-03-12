package com.iscreate.plat.networkresource.common.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.plat.networkresource.common.tool.ResourceCommon;

public class NetworkResourceDaoImpl implements NetworkResourceDao {
	private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	private List<Map<String, Object>> findQueryByHibernate(final String sql){
		List<Map<String, Object>> list =  this.hibernateTemplate
		.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
						List<Map<String, Object>> find = null;
							SQLQuery query = session.createSQLQuery(sql);
							//org.hibernate.Query query = session.createQuery(sql);
							query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
							find = query.list();
				return find;
			}
		});
		return list;
	}
	

	/**
	 * 根据父级资源获取其所属指定类型资源集合
	* @author ou.jh
	* @date Jun 21, 2013 1:55:08 PM
	* @Description: TODO 
	* @param @param parentId
	* @param @param parentType
	* @param @param resourceType
	* @param @return        
	* @throws
	 */
	public List<Map<String,Object>> getResourceByResourceType(long parentId,String parentType,String resourceType){
		if(parentType == null || parentType.equals("")){
			//父级资源类型为空
			return null;
		}
		if(resourceType == null || resourceType.equals("")){
			//目标资源类型为空
			return null;
		}
		String sql = " select "+ResourceCommon.getSelectSqlAttributsString(resourceType,"re")+" "
						+"  from "+resourceType+" re, Figurenode fn "
						+" where re.entity_id = fn.entityid "
						+"   and fn.path like '%/"+parentType+"/"+parentId+"/%' ";
		List<Map<String,Object>> findQueryByHibernate = this.findQueryByHibernate(sql);
		return findQueryByHibernate;
	}
	
	
	/**
	 * 根据指定区域获取其所属指定类型资源集合
	* @author ou.jh
	* @date Jun 21, 2013 2:04:20 PM
	* @Description: TODO 
	* @param @param areaId
	* @param @param resourceType
	* @param @return        
	* @throws
	 */
	public List<Map<String,Object>> getResourceByAreaAndResourceType(long areaId,String resourceType){
		if(resourceType == null || resourceType.equals("")){
			//目标资源类型为空
			return null;
		}
		String sql = " select "+ResourceCommon.getSelectSqlAttributsString(resourceType,"re")+" "
						+"  from "+resourceType+" re, Figurenode fn "
						+" where re.entity_id = fn.entityid "
						+"   and fn.area_path like '%/"+areaId+"/%' ";
		List<Map<String,Object>> findQueryByHibernate = this.findQueryByHibernate(sql);
		return findQueryByHibernate;
	}
}
