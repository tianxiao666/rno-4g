package com.iscreate.op.dao.system;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.ldap.core.CollectingNameClassPairCallbackHandler;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.constant.InformationConstant;
import com.iscreate.op.pojo.system.SysArea;

public class SysAreaDaoImpl implements SysAreaDao{
	private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	/**
	 * @author:duhw
	 * @create_time:2013-05-29
	 * 通过区域标识得到所有父区域的信息
	 * area_id：区域标识(此标识等于0时，此方法可以得到所有区域信息)
	 * return:区域列表
	 */
	public List<Map<String,Object>> getParentAreaListByAreaId(long area_id){
		String sql = "select "+getAreaTableAttributeAlias("sys_area","a")+" from sys_area a connect by prior a.parent_id=a.area_id"
            +" start with a.area_id="+area_id;
		 List<Map<String,Object>> areaList =  (List<Map<String,Object>>)this.executeSqlForObject(sql);
		 return areaList;
	}
	/**
	 * @author:du.hw
	 * @create_time:2013-05-29
	 * 通过区域标识得到上一级区域信息
	 * areaId:区域标识
	 */
	 public Map<String,Object> getParentAreaByAreaId(long areaId){
		 String sql = "select "+getAreaTableAttributeAlias("sys_area","a")+"from sys_area a " +
		         " where area_id=(select nvl(parent_id) from sys_area where area_id="+areaId+")";
		 List<Map<String,Object>> areaList =  (List<Map<String,Object>>)this.executeSqlForObject(sql);
		 if(areaList != null){
			 return areaList.get(0);
		 }else{
			 return null;
		 }
	 }
	/**
	 * @author:duhw
	 * @create_time:2013-05-11
	 * 通过区域标识得到所有子区域的信息
	 * area_id：区域标识(此标识等于0时，此方法可以得到所有区域信息)
	 *         区域标识不等于0时，此方法获取的区域列表中，不包含当前区域
	 * deepth:树的深度
	 * return:区域列表
	 */
	public List<Map<String,Object>> getAreaList(int area_id,int deepth) {
		String sql = "";
		if(deepth <= 0){
			deepth = 20;//拿所有树节点，这里给的默认树深度,一般整棵树深度不会达到20
		}
		if(area_id > 0){
			sql = "select * from (select level grade, a.* from SYS_AREA a connect by prior a.area_id=a.parent_id"
                +" start with a.parent_id="+area_id+") where grade<="+deepth+" order by path";
		}else{
			sql = "select * from (select level grade, a.* from SYS_AREA a connect by prior a.area_id=a.parent_id"
                +" start with a.parent_id is null or a.parent_id = 0) where grade<="+deepth+" order by path";
		}
		return (List<Map<String,Object>>)this.executeSqlForObject(sql);
		

	}
	/**
	 * @author:duhw
	 * @create_time:2013-05-13
	 * 通过用户标识得到用户关联的区域列表(查询用户关联的组织所关联的区域)
	 * org_user_id：用户标识
	 * return:区域列表
	 */
	public List<Map<String,Object>> getAreaListByOrgUserId(long org_user_id){
		String sql = "select "+getAreaTableAttributeAlias("sys_area","a")+"from sys_area a " +
				         " inner join sys_org_rela_area b on b.area_id=a.area_id"+
                         " inner join sys_user_rela_org c on c.org_id=b.org_id and c.org_user_id="+org_user_id;
		return (List<Map<String,Object>>)this.executeSqlForObject(sql);
	}
	/**
	 * @author:duhw
	 * @create_time:2013-05-11
	 * 查询sql
	 */
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
	public String getAreaTableAttributeAlias(String tableName,String tableAliasName){
		String s = "";
		if(!"".equals(tableAliasName)){
			tableAliasName+=".";
		}
		if("sys_area".equalsIgnoreCase(tableName)){
			s=tableAliasName+"area_id \"areaId\","
			+tableAliasName+"name \"name\","
			+tableAliasName+"area_level \"areaLevel\","
			+tableAliasName+"entity_type \"entityType\","
			+tableAliasName+"entity_id \"entityId\","
			+tableAliasName+"longitude \"longitude\","
			+tableAliasName+"latitude \"latitude\","
			+tableAliasName+"parent_Id \"parentId\","
			+tableAliasName+"path \"path\","
			+tableAliasName+"createtime \"createtime\","
			+tableAliasName+"updatetime \"updatetime\"";	
		}
		return s;
	}
	
	/**
	 * 根据人员账号获取人员对应区域
	* @author ou.jh
	* @date May 28, 2013 10:16:55 AM
	* @Description: TODO 
	* @param @param account        
	* @throws
	 */
	public List<Map<String,Object>> getAreaByAccount(String account){
		String sql = 
					" select sarea.* "
					+"  from SYS_ACCOUNT       sa, "
					+"       SYS_USER_RELA_ORG suro, "
					+"       SYS_ORG_RELA_AREA sora, "
					+"       SYS_AREA          sarea "
					+" where sa.org_user_id = suro.org_user_id "
					+"   and suro.org_id = sora.org_id "
					+"   and sarea.area_id = sora.area_id ";	
		if(account != null && !account.equals("")){
			sql = sql +"   and sa.account = '"+account+"'";
		}else{
			return null;
		}
		return (List<Map<String,Object>>)this.executeSqlForObject(sql);
	}
	/**
	 * 
	 * @description: 通过区域标识字符串得到所有父区域的信息
	 * @author：yuan.yw
	 * @param area_ids 区域标识字符串 (逗号隔开)
	 * @return     区域列表
	 * @return List<Map<String,Object>>     
	 * @date：Jun 19, 2013 1:48:47 PM
	 */
	public List<Map<String,Object>> getParentAreaListByAreaIds(String area_ids){
		if(area_ids==null || "".equals(area_ids)){
			return null;
		}
		String sql = "select "+getAreaTableAttributeAlias("sys_area","a")+" from sys_area a connect by prior a.parent_id=a.area_id"
	        +" start with a.area_id in("+area_ids+")";
		 List<Map<String,Object>> areaList =  (List<Map<String,Object>>)this.executeSqlForObject(sql);
		 return areaList;
	}
	/**
	 * 
	 * @description: 通过组织id标识得到关联区域的信息
	 * @author：yuan.yw
	 * @param orgId 组织id标识 
	 * @return     区域列表
	 * @return List<Map<String,Object>>     
	 * @date：Jun 19, 2013 1:48:47 PM
	 */
	public List<Map<String,Object>> getAreaListByOrgId(String orgId){
		if(orgId==null || "".equals(orgId)){
			return null;
		}
		String sql = "select "+getAreaTableAttributeAlias("sys_area","a")+" from sys_area a,SYS_ORG_RELA_AREA so where so.area_id=a.area_id and so.org_id="+orgId;
		 List<Map<String,Object>> areaList =  (List<Map<String,Object>>)this.executeSqlForObject(sql);
		 return areaList;
	}
	/**
	 * 
	 * @description: 通过账户名account获取关联区域的市级别(level)区域 比如广州市
	 * @author：yuan.yw
	 * @param account
	 * @return     
	 * @return Map<String,Object>    
	 * @date：Jun 25, 2013 11:05:39 AM
	 */
	public Map<String,Object> getAreaListInCityLevelByAccount(String account){
		if(account == null || "".equals(account)){
			return null;
		}
		String sql = "select distinct area.* "
			 +" from SYS_AREA area  where area.area_level='市' "
			 +" connect by prior area.parent_id = area.area_id  "
			 +" start with area.area_id in                   "
			 +"          (select sarea.area_id              "
			 +"             from SYS_ACCOUNT       sa,      "
			 +"                   SYS_USER_RELA_ORG suro,  "
			 +"                   SYS_ORG_RELA_AREA sora,  "
			 +"                   SYS_AREA          sarea  "
			 +"             where sa.org_user_id = suro.org_user_id  "
			 +"               and suro.org_id = sora.org_id  "
			 +"               and sarea.area_id = sora.area_id and sa.account='"+account+"') ";	
		List<Map<String,Object>> list =this.executeSqlForObject(sql);
		Map<String,Object> mp=null;
		if(list!=null && !list.isEmpty()){
			mp = list.get(0);
		}
		return mp;
	}
	
	/**
	 * 获取指定区域下的指定类型的子区域
	 * @param area_ids
	 * @param area_level
	 * @return
	 * @author brightming
	 * 2013-9-26 下午2:56:06
	 */
	public List<Map<String,Object>> getSubAreasInSpecAreaLevel(long[] area_ids,String area_level){
		
		if(area_ids.length==0){
			return Collections.EMPTY_LIST;
		}
		StringBuilder buf=new StringBuilder();
		buf.append("(");
		for(int i=0;i<area_ids.length;i++){
			buf.append(area_ids[i]+",");
		}
		buf.deleteCharAt(buf.length()-1);
		buf.append(")");
		String sql = "select a.* from sys_area a start with a.area_id in " +buf.toString()+" connect by prior a.area_id=a.parent_id and area_level='"+area_level+"'";
//		System.out.println("sql:"+sql); 
		List<Map<String,Object>> areaList =  (List<Map<String,Object>>)this.executeSqlForObject(sql);
		 return areaList;
	}
	
	/**
	 * 根据id获取area
	 * @param areaId
	 * @return
	 * @author brightming
	 * 2013-10-29 下午4:39:57
	 */
	public SysArea getAreaById(long areaId){
		return hibernateTemplate.get(SysArea.class, areaId);
	}
	/**
	 * 
	 * @title 获取指定区域下的BSC信息
	 * @param areaId
	 * @return
	 * @author chao.xj
	 * @date 2014-8-7下午5:11:52
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String,Object>> getBscInSpecAreaLevel(long areaId){
		String sql = "select * from rno_bsc where bsc_id in (select bsc_id from rno_bsc_rela_area where area_id in (select area_id from sys_area where sys_area.parent_id = "+areaId+"))";
//		System.out.println("sql:"+sql); 
		List<Map<String,Object>> areaList =  (List<Map<String,Object>>)this.executeSqlForObject(sql);
		 return areaList;
	}
	
	/**
	 * @title 获取指定区域名称的区域信息
	 * @param areaName
	 * @return
	 * @author peng.jm
	 * @date 2014-10-8下午04:57:59
	 */
	public Map<String,Object> getAreaByAreaName(String areaName){
		String sql = "select * from sys_area where sys_area.name = '"+areaName+"' ";
		List<Map<String,Object>> areaList =  (List<Map<String,Object>>)this.executeSqlForObject(sql);
		Map<String, Object> result = new HashMap<String, Object>();
		if(areaList.size() > 0) {
			result = areaList.get(0);
		}
		return result;
	}
}
