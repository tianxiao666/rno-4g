package com.iscreate.op.dao.home;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.home.HomeItem;
import com.iscreate.op.pojo.home.HomeItemRelaRole;

/**
 * 门户组件信息DAO
	 * 
	 * @author ou.jh
	 * @date Jun 5, 2013
	 * @Description: TODO
	 * @param 
	 * @return 
	 * @throws
 */
public class HomeItemDaoImpl implements HomeItemDao{
	
	private HibernateTemplate hibernateTemplate;
	
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	/**
	 * 根据角色ID与账号获取门户组件信息
	* @author ou.jh
	* @date Jun 5, 2013 11:16:14 AM
	* @Description: TODO 
	* @param @param roleId
	* @param @param account
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getHmoeItemByRoleIdAndAccount(long roleId , String account){
		String sql = 
			"	select hi.home_item_id       \"home_item_id\", "
			+"       hi.title         \"title\", "
			+"       hi.showtitle     \"showtitle\", "
			+"       hi.type          \"type\", "
			+"       hi.url           \"url\", "
			+"       hi.maxurl        \"maxurl\", "
			+"       hirr.item_height \"item_height\", "
			+"       hirr.item_width  \"item_width\", "
			+"       hirr.item_row      \"item_row\", "
			+"       hirr.item_column   \"item_column\", "
			+"       hi.status        \"status\", "
			+"       hi.createtime    \"createtime\", "
			+"       hi.updatetime    \"updatetime\", "
			+"		 hirr.role_id     \"role_id\" "
			+"  from HOME_ITEM hi "
			+" inner join HOME_ITEM_RELA_ROLE hirr on hi.home_item_id = hirr.home_item_id "
			+" inner join sys_role sr on sr.role_id = hirr.role_id "
			+" where hirr.role_id = "+roleId+" "
			+"   and hi.status = 1 "
			+"   and hi.home_item_id not in (select hs.home_item_id "
			+"                            from HOME_SETTINGS hs,sys_account sa "
			+"                           where hs.org_user_id = sa.org_user_id "
            +"                             and sa.account = '"+account+"' "
			+"                             and hs.status = 0) ";
		return (List<Map<String, Object>>)this.executeSqlForObject(sql);
	}
	
	/**
	 * 根据账号获取门户组件信息
	* @author ou.jh
	* @date Jun 5, 2013 11:16:14 AM
	* @Description: TODO 
	* @param @param roleId
	* @param @param account
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getHmoeItemByAccount(String account){
		String sql = 
			"	select hi.home_item_id       \"home_item_id\", "
			+"       hi.title         \"title\", "
			+"       hi.showtitle     \"showtitle\", "
			+"       hi.type          \"type\", "
			+"       hi.url           \"url\", "
			+"       hi.maxurl        \"maxurl\", "
			+"       hirr.item_height \"item_height\", "
			+"       hirr.item_width  \"item_width\", "
			+"       hirr.item_row      \"item_row\", "
			+"       hirr.item_column   \"item_column\", "
			+"       hi.status        \"status\", "
			+"       hi.createtime    \"createtime\", "
			+"       hi.updatetime    \"updatetime\", "
			+"		 hirr.role_id     \"role_id\" "
			+"  from HOME_ITEM hi "
			+" inner join HOME_ITEM_RELA_ROLE hirr on hi.home_item_id = hirr.home_item_id "
			+" inner join sys_role sr on sr.role_id = hirr.role_id "
			+" where hirr.role_id in "
	        +" (select sr.role_id "
	        +"    from sys_role sr, sys_user_rela_role surr, sys_account sysa "
	        +"   where surr.role_id = sr.role_id "
	        +"   and sysa.org_user_id = surr.org_user_id "
	        +"   and sysa.account = '"+account+"') "
			+"   and hi.status = 1 "
			+"   and hi.home_item_id not in (select hs.home_item_id "
			+"                            from HOME_SETTINGS hs,sys_account sa "
			+"                           where hs.org_user_id = sa.org_user_id "
            +"                             and sa.account = '"+account+"' "
			+"                             and hs.status = 0) ";
		return (List<Map<String, Object>>)this.executeSqlForObject(sql);
	}
	/**
	 * 
	 * @description: 获取全部门户组件
	 * @author：yuan.yw
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jun 6, 2013 3:52:39 PM
	 */
	public List<Map<String,Object>> getAllHomeItemList(){
		String sql = "	select hi.home_item_id       \"id\", "
					+"       hi.title         \"title\", "
					+"       hi.showtitle     \"showTitle\", "
					+"       hi.type          \"type\", "
					+"       hi.url           \"url\", "
					+"       hi.maxurl        \"maxUrl\", "
					+"       hi.status        \"status\", "
					+"       hi.defaultwidth    \"defaultWidth\", "
					+"       hi.defaultheight    \"defaultHeight\", "
					+"       hi.createtime    \"createtime\", "
					+"       hi.updatetime    \"updatetime\" "
					+"  from HOME_ITEM hi ";
		return (List<Map<String,Object>>)this.executeSqlForObject(sql);
	}
	/**
	 * 
	 * @description: 保存实体
	 * @author：yuan.yw
	 * @param entity
	 * @return     
	 * @return Serializabel     
	 * @date：Jun 6, 2013 5:21:54 PM
	 */
	public Serializable saveEntity(Object entity){
		if(entity!=null){
			return this.hibernateTemplate.save(entity);
		}else{
			return null;
		}
	}
	/**
	 * 
	 * @description: 删除实体
	 * @author：yuan.yw
	 * @param entity     
	 * @return void     
	 * @date：Jun 6, 2013 5:50:47 PM
	 */
	public void deleteEntity(Object entity){
		if(entity!=null){
			this.hibernateTemplate.delete(entity);
		}
		
	}
	/**
	 * 
	 * @description:根据id获取门户组件
	 * @author：
	 * @param itemId
	 * @return     
	 * @return HomeItem     
	 * @date：Jun 6, 2013 5:51:15 PM
	 */
	public HomeItem getHomeItemById(String itemId){
		if(itemId!=null&&!"".equals(itemId)){
			String hql="from HomeItem hi where hi.homeItemId= "+itemId;
			List<HomeItem> hiList = this.hibernateTemplate.find(hql);
			HomeItem hi = null;
			if(hiList!=null){
				hi = hiList.get(0);
			}
			return hi;
		}else{
			return null;
		}
	}
	/**
	 * 
	 * @description: 更新实体
	 * @author：yuan.yw
	 * @param entity
	 * @return     
	 * @return void     
	 * @date：Jun 6, 2013 5:21:54 PM
	 */
	public void updateEntity(Object entity){
		if(entity!=null){
			this.hibernateTemplate.update(entity);
		}
	}
	
	/**
	 * 
	 * @description: 通过角色id type获取关联的门户组件
	 * @author：yuan.yw
	 * @param type 
	 * @param roleId 
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jun 7, 2013 10:48:26 AM
	 */
	public List<Map<String,Object>> getRoleHomeItemListByRoleIdAndType(String roleId,String type){
		if(roleId==null || "".equals(roleId)){
			return null;
		}
		String sql="";
		if(type==null){
			sql = "	select hi.home_item_id       \"id\", "
				+"       hi.title         \"title\", "
				+"       hi.showtitle     \"showTitle\", "
				+"       hi.type          \"type\", "
				+"       hi.url           \"url\", "
				+"       hi.maxurl        \"maxUrl\", "
				+"       hi.status        \"status\", "
				+"       hi.defaultwidth    \"defaultWidth\", "
				+"       hi.defaultheight    \"defaultHeight\", "
				+"       hi.createtime    \"createtime\", "
				+"       hi.updatetime    \"updatetime\", "
				+"       0    \"itemRow\", "
				+"       0    \"itemColumn\", "
				+"       300   \"itemHeight\", "
				+"       300    \"itemWidth\", "
				+"       "+roleId+"    \"orgRoleId\" "
				+"  from HOME_ITEM hi  where hi.home_item_id not in( select hrr.home_item_id from HOME_ITEM_RELA_ROLE hrr  where  hrr.role_id="+roleId+")";
		}else{
			sql = "	select hi.home_item_id       \"id\", "
				+"       hi.title         \"title\", "
				+"       hi.showtitle     \"showTitle\", "
				+"       hi.type          \"type\", "
				+"       hi.url           \"url\", "
				+"       hi.maxurl        \"maxUrl\", "
				+"       hi.status        \"status\", "
				+"       hi.defaultwidth    \"defaultWidth\", "
				+"       hi.defaultheight    \"defaultHeight\", "
				+"       hi.createtime    \"createtime\", "
				+"       hi.updatetime    \"updatetime\", "
				+"       hr.item_row    \"itemRow\", "
				+"       hr.item_column    \"itemColumn\", "
				+"       hr.item_height    \"itemHeight\", "
				+"       hr.item_width    \"itemWidth\", "
				+"       hr.role_id    \"orgRoleId\" "
				+"  from HOME_ITEM hi,HOME_ITEM_RELA_ROLE hr where hi.home_item_id=hr.home_item_id and hr.role_id="+roleId;
		}
		return (List<Map<String,Object>>)this.executeSqlForObject(sql);
	}
	/**
	 * 
	 * @description: 根据角色id 表名 组件id 字符串 删除门户相关实体
	 * @author：yuan.yw
	 * @param entityName
	 * @param roleId
	 * @param itemIds
	 * @return     
	 * @return int     
	 * @date：Jun 7, 2013 3:33:03 PM
	 */
	public int deleteEntityForHome(String entityName ,String roleId,String itemIds){
		if(entityName==null||"".equals(entityName)||roleId==null||"".equals(roleId)||itemIds==null||"".equals(itemIds)){
			return 0;
		}
		String sql ="delete from "+entityName+" t where t.roleId="+roleId+" and t.homeItemId in("+itemIds+")";
		return this.executeSqlForOperateObject(sql);
	}
	/**
	 * 
	 * @description:根据角色id 组件id 获取角色组件
	 * @author：yuan.yw
	 * @param roleId
	 * @param itemId
	 * @return     
	 * @return HomeItemRelaRole     
	 * @date：Jun 7, 2013 5:56:27 PM
	 */
	public HomeItemRelaRole getHomeItemByRoleIdAndItemId(String roleId,String itemId){
		if(itemId!=null&&!"".equals(itemId)&&roleId!=null&&!"".equals(roleId)){
			String hql="from HomeItemRelaRole hr where hr.homeItemId= "+itemId+" and hr.roleId="+roleId;
			List<HomeItemRelaRole> hrList = this.hibernateTemplate.find(hql);
			HomeItemRelaRole hr = null;
			if(hrList!=null){
				hr = hrList.get(0);
			}
			return hr;
		}else{
			return null;
		}
	}
	public List executeSqlForObject(final String sqlString) {
		List<Map<String, Object>> list = hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(
							Session session) throws HibernateException,
							SQLException {
						SQLQuery query = session.createSQLQuery(sqlString);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List find = query.list();
						return find;
					}
				});
		return list;
	}
	public int executeSqlForOperateObject(final String sqlString) {
		Integer i = hibernateTemplate
				.execute(new HibernateCallback<Integer>() {
					public Integer doInHibernate(
							Session session) throws HibernateException,
							SQLException {
						Query query=session.createQuery(sqlString);
						int i = query.executeUpdate();
						return i;
					}
				});
		return i;
	}
}
