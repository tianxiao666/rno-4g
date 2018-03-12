package com.iscreate.op.dao.system;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.system.SysDictionary;
/**
 * 
 * @filename: SysDictionaryDaoImpl.java
 * @classpath: com.iscreate.op.dao.system
 * @description:数据字典dao
 * @author：
 * @date：Aug 20, 2013 3:44:41 PM
 * @version：
 */
public class SysDictionaryDaoImpl implements SysDictionaryDao{
	private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	
	/**
	 * 
	 * @description: 保存数据字典
	 * @author：yuan.yw
	 * @param entity
	 * @return     
	 * @return Serializable     
	 * @date：Aug 20, 2013 3:09:01 PM
	 */
	public Serializable saveSysDictionary(Object entity){
		return this.hibernateTemplate.save(entity);
	}
	/**
	 * 
	 * @description: 更新数据字典
	 * @author：yuan.yw
	 * @param entity
	 * @return     
	 * @return void     
	 * @date：Aug 20, 2013 3:09:06 PM
	 */
	public void updateSysDictionary(Object entity){
		this.hibernateTemplate.update(entity);
	}
	/**
	 * 
	 * @description: 根据字典Id 字符串（“,”隔开）批量更新数据字典状态
	 * @author：yuan.yw
	 * @param dictionaryIds
	 * @param status
	 * @param orgUserId
	 * @return     
	 * @return boolean     
	 * @date：Aug 20, 2013 3:10:40 PM
	 */
	public boolean updateSysDictionaryStatusByIds(String dictionaryIds,String status,long orgUserId){
		if("".equals(dictionaryIds) || dictionaryIds==null){
			return false;
		}
		String sql =" update sys_dictionary set status='"+status+"' , mod_user_id="+orgUserId+" , mod_time = sysdate  where data_type_id in("+dictionaryIds+")";
		return this.executeUpdateSql(sql);
	}
	/**
	 * 
	 * @description: 根据字典Id获取数据字典信息(包括父级信息)
	 * @author：yuan.yw
	 * @param dictionaryId
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Aug 20, 2013 3:13:12 PM
	 */
	public Map<String,Object> getSysDictionaryDetailById(long dictionaryId){
		if(dictionaryId==0){
			return null;
		}
		String sql = "select sd.*,(select replace(wm_concat(s.name),',','→') from ( "
					+" select so.*,1 flag from sys_dictionary so "
					+" connect by prior so.parent_id=so.data_type_id "
					+" start with so.data_type_id="+dictionaryId+" order by level desc "
					+" ) s) parent_name,sou.name create_user,sou1.name mod_user  from sys_dictionary sd " +
					" left join sys_org_user sou on sou.org_user_id = sd.create_user_id " +
					" left join sys_org_user sou1 on sou1.org_user_id = sd.mod_user_id " +
					"where sd.data_type_id="+dictionaryId;
		List<Map<String,Object>> list =  this.executeSqlForObject(sql);
		Map<String,Object> resultMap =  null;
		if(list!=null && list.size()>0){
			resultMap = list.get(0);
		}
		return resultMap; 
	}
	/**
	 * 
	 * @description: 根据字典Id获取数据字典信息
	 * @author：yuan.yw
	 * @param dictionaryId
	 * @return     
	 * @return SysDictionary     
	 * @date：Aug 20, 2013 3:13:12 PM
	 */
	public SysDictionary getSysDictionaryById(long dictionaryId){
		if(dictionaryId==0){
			return null;
		}
		String sql = " from SysDictionary where dataTypeId= "+dictionaryId;
		List<SysDictionary> list = this.hibernateTemplate.find(sql);
		SysDictionary s =  null;
		if(list!=null && list.size()>0){
			s = list.get(0);
		}
		return s; 
	}
	/**
	 * 
	 * @description: 根据条件分页获取第一级的数据字典
	 * @author：yuan.yw
	 * @param conditionMap
	 * @param indexStart
	 * @param indexEnd
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Aug 20, 2013 3:15:32 PM
	 */
	public Map<String,Object> getFirstLevelDictionaryByConditionForPage(Map<String,Object> conditionMap,int indexStart,int indexEnd){
		String sql = "select sd.*,ROWNUM  num from sys_dictionary sd where (sd.parent_id=0 or sd.parent_id is null) ";
		String countSql = "select count(*) \"count\" from sys_dictionary sd where (sd.parent_id=0 or sd.parent_id is null)  ";
		if(conditionMap != null && !conditionMap.isEmpty()){
			for(String key:conditionMap.keySet()){
				if(key.equals("status")){
					if(conditionMap.get(key) != null && !conditionMap.get(key).equals("")){
						sql += " and "+key+" = '"+conditionMap.get(key)+"'";
						countSql += " and "+key+" = '"+conditionMap.get(key)+"'";
					}
				}else{
					sql += " and instr("+key+",'"+conditionMap.get(key)+"')>0";
					countSql += " and instr("+key+",'"+conditionMap.get(key)+"')>0";
				}
				
			}
		}
		sql = "select s.data_type_id from ("+sql+" order by sd.create_time desc) s  where s.num>="+indexStart+" and s.num<="+indexEnd;
		sql = "select sc.*,su.name create_user from sys_dictionary sc  left join sys_org_user su on su.org_user_id=sc.create_user_id connect by prior sc.data_type_id=sc.parent_id start with sc.data_type_id in("+sql+")";
		List<Map<String,Object>> rList = this.executeSqlForObject(sql);
		List<Map<String,Object>> countList =  this.executeSqlForObject(countSql);
		Map<String,Object> resultMap = null;
		if(countList!=null && !countList.isEmpty()){
			resultMap =  new HashMap<String,Object>();
			String count = countList.get(0).get("count")+"";
			resultMap.put("count", count);
			resultMap.put("entityList", rList);
		}
		return resultMap;
	}
	/**
	 * 
	 * @description: 根据上级id,标识flag(获取status判断)获取下级数据字典
	 * @author：yuan.yw
	 * @param parentId
	 * @param flag
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 20, 2013 3:18:35 PM
	 */
	public List<Map<String,Object>> getDictionaryByParId(long parentId,boolean flag){
		String sql ="";
		String status="";
		if(flag){
			status=" and sd.status='A' ";
		}
		if(parentId==0){
			sql ="select sd.*,sou.name create_user,sou1.name mod_user  from sys_dictionary sd" +
				" left join sys_org_user sou on sou.org_user_id = sd.create_user_id " +
				" left join sys_org_user sou1 on sou1.org_user_id = sd.mod_user_id " +
				" where 1=1  "+status+" and (sd.parent_id=0 or sd.parent_id is null) order by sd.order_num asc";
		}else{
			sql ="select sd.*,sou.name create_user,sou1.name mod_user  from sys_dictionary sd" +
				" left join sys_org_user sou on sou.org_user_id = sd.create_user_id " +
				" left join sys_org_user sou1 on sou1.org_user_id = sd.mod_user_id " +
				 " where 1=1  "+status+" and sd.parent_id ="+parentId+" order by sd.order_num asc";
		}
		
		return this.executeSqlForObject(sql);
	}
	/**
	 * 
	 * @description: 根据id（不等于）名称或编码获取数据字典
	 * @author：yuan.yw
	 * @param parentId
	 * @param id
	 * @param name
	 * @param code
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 21, 2013 11:36:02 AM
	 */
	public List<Map<String,Object>> getDictionaryByNameOrCode(String id,String parentId,String name,String code){
		if((code==null||"".equals(code)) && (name==null||"".equals(name))){
			return null;
		}
		String sql ="select sd.* from sys_dictionary sd where  1=1 ";
		if(code!=null && !"".equals(code)){
			sql +=" and sd.code = '"+code+"'";
		}
		if(name!=null && !"".equals(name)){
			sql +=" and sd.name = '"+name+"'";
		}
		if(id!=null && !"".equals(id)){
			sql +=" and sd.data_type_id != '"+id+"'";
		}
		if(parentId!=null && !"".equals(parentId) && !"0".equals(parentId)){
			sql +=" and sd.parent_id = '"+parentId+"'";
		}
		return this.executeSqlForObject(sql);
	}
	/**
	 * 
	 * @description: 获取自身及子下级字典列表
	 * @author：yuan.yw
	 * @param dicIds
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 22, 2013 11:13:31 AM
	 */
	public List<Map<String,Object>> getSelfAndChildDictionaryListByIds(String dicIds){
		if(dicIds==null || "".equals(dicIds)){
			return null;
		}
		String sql = " select sd.* from sys_dictionary sd connect by prior sd.data_type_id=sd.parent_id start with sd.data_type_id in("+dicIds+")";
		return this.executeSqlForObject(sql);
	}
	
	
	/**
	 * 
	 * @description: 获取自身及子下级字典列表
	 * @author：yuan.yw
	 * @param dicIds
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 22, 2013 11:13:31 AM
	 */
	public List<Map<String,Object>> getSelfAndChildDictionaryListByCode(String code){
		if(code==null || "".equals(code)){
			return null;
		}
		String sql = " select sd.* from sys_dictionary sd connect by prior sd.data_type_id=sd.parent_id and sd.status != 'X' start with sd.code = '"+code+"'";
		return this.executeSqlForObject(sql);
	}
	
	/**
	 * 
	 * @description: 根据编码获取下（子）级数据字典
	 * @author：yuan.yw
	 * @param code
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 26, 2013 10:10:43 AM
	 */
	public List<Map<String,Object>> getChildDictionaryListByCode(String code){
		if(code==null && "".equals(code)){
			return null;
		}
		String sql = "select sd1.* from sys_dictionary sd1,sys_dictionary sd2 where sd1.parent_id=sd2.data_type_id and sd1.status != 'X' and sd2.code='"+code+"'";
		return this.executeSqlForObject(sql);
	}
	/**
	 * 
	 * @description: 根据编码获取其下全部下级字典（或者包括自身 selfFlag判断）
	 * @author：yuan.yw
	 * @param code
	 * @param selfFlag 是否获取自身
	 * @param status   为空值获取全部状态数据字典
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 26, 2013 10:10:43 AM
	 */
	public List<Map<String,Object>> getAllChildDictionaryListByCode(String code,boolean selfFlag,String status){
		if(code==null && "".equals(code)){
			return null;
		}
		String statusSql="where 1=1 ";
		if(status!=null && !"".equals(status)){
			statusSql += "and sd.status='"+status+"'";
		}
		String sql = "";
		if(selfFlag){
			sql = "select sd.* from sys_dictionary sd "+statusSql+" connect by prior sd.data_type_id = sd.parent_id  start with sd.code='"+code+"'";
		}else{
			sql = "select sd.* from sys_dictionary sd "+statusSql+" and level>1 connect by prior sd.data_type_id = sd.parent_id  start with sd.code='"+code+"'";
		}
		return this.executeSqlForObject(sql);
	}
	/**
	 * 
	 * @description: 根据自身编码及父编码获取下（子）级数据字典
	 * @author：yuan.yw
	 * @param code
	 * @param parentCode
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 26, 2013 10:19:37 AM
	 */
	public List<Map<String,Object>> getChildDictionaryListByCodeAndParCode(String code,String parentCode){
		if(code==null && "".equals(code) && parentCode==null && "".equals(parentCode)){
			return null;
		}
		String sql = " select sd1.*"
					 +" from sys_dictionary sd1, sys_dictionary sd2, sys_dictionary sd3"
					 +" where sd1.parent_id = sd2.data_type_id"
					 +"  and sd2.parent_id = sd3.data_type_id"
					 +"  and sd2.code='"+code+"'"
					 +"  and sd3.code='"+parentCode+"'";
		return this.executeSqlForObject(sql);

	}
	/**
	 * 
	 * @description: 根据自身编码及父编码获取其下全部下级字典（或者包括自身 selfFlag判断）
	 * @author：yuan.yw
	 * @param code
	 * @param parentCode
	 * @param selfFlag 是否获取自身
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 26, 2013 10:20:08 AM
	 */
	public List<Map<String,Object>> getAllChildDictionaryListByCodeAndParCode(String code,String parentCode,boolean selfFlag){
		if(code==null && "".equals(code) && parentCode==null && "".equals(parentCode)){
			return null;
		}
		String sql = "";
		if(selfFlag){
			sql = "select sd1.* from sys_dictionary  sd1 "
				+" connect by prior sd1.data_type_id = sd1.parent_id"
				+" start with  sd1.data_type_id in (select sd2.data_type_id"
				+"   from  sys_dictionary sd2, sys_dictionary sd3"
				+"  where sd2.parent_id = sd3.data_type_id"
				+"    and sd2.code='"+code+"'"
				+"    and sd3.code='"+parentCode+"')";
		}else{
			sql = "select sd1.* from sys_dictionary  sd1 where level>1"
				+" connect by prior sd1.data_type_id = sd1.parent_id"
				+" start with  sd1.data_type_id in (select sd2.data_type_id"
				+"   from  sys_dictionary sd2, sys_dictionary sd3"
				+"  where sd2.parent_id = sd3.data_type_id"
				+"    and sd2.code='"+code+"'"
				+"    and sd3.code='"+parentCode+"')";
		}
		return this.executeSqlForObject(sql);
	}
	
	/**
	 * 
	 * @description: 根据自身编码及父编码获取数据字典
	 * @author：yuan.yw
	 * @param code
	 * @param parentCode
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 26, 2013 10:19:37 AM
	 */
	public Map<String,Object> getDictionaryMapByCodeAndParCode(String code,String parentCode){
		if(code==null && "".equals(code) && parentCode==null && "".equals(parentCode)){
			return null;
		}
		String sql = 	"select sd1.*"
						+"   from  sys_dictionary sd1, sys_dictionary sd2"
						+"  where sd1.parent_id = sd2.data_type_id"
						+"    and sd1.code='"+code+"'"
						+"    and sd2.code='"+parentCode+"' and sd1.status='A'";
		List<Map<String,Object>> list = this.executeSqlForObject(sql);
		Map<String,Object> map = null;
		if(list!=null && !list.isEmpty()){
			map = list.get(0);
		}
		return map;
	}
	
	/**
	 * 
	 * @description: 根据自身编码及父编码获取数据字典
	 * @author：yuan.yw
	 * @param code
	 * @param parentCode
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 26, 2013 10:19:37 AM
	 */
	public Map<String,Object> getDictionaryMapByCode(String code){
		String sql = 	"select sd1.* "
						+"   from  sys_dictionary sd1 "
						+"   where sd1.code='"+code+"'";
		List<Map<String,Object>> list = this.executeSqlForObject(sql);
		Map<String,Object> map = null;
		if(list!=null && !list.isEmpty()){
			map = list.get(0);
		}
		return map;
	}
	/**
	 * 
	 * @description: 根据id 获取字典标识对应名称map
	 * @author：yuan.yw
	 * @param dicIds
	 * @return     
	 * @return List<Map<String,Object>>  
	 * @date：Dec 19, 2013 9:58:16 AM
	 */
	public List<Map<String,Object>> getDictionaryListByIds(String dicIds){
		String sql = 	"select sd1.* "
			+"   from  sys_dictionary sd1 "
			+"   where sd1.data_type_id in ("+dicIds+") order by sd1.create_time asc";
		List<Map<String,Object>> list = this.executeSqlForObject(sql);
		return list;
	}
	
	/**
	 * 根据code集合其孙子级数据字典(in)
	* @author ou.jh
	* @date Jan 10, 2014 2:27:24 PM
	* @Description: TODO 
	* @param @param code
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getSecondSysDictionaryListByCode(String code){
		if(code == null && code.isEmpty()){
			return null;
		}else{
			String sql = 
				"	select sd2.*, sd1.code first_code, sd1.name first_name, sd.name this_name , sd.code this_code "
				+"	  from sys_dictionary sd, sys_dictionary sd1, sys_dictionary sd2 "
				+"	 where sd2.parent_id = sd1.data_type_id "
				+"	   and sd1.parent_id = sd.data_type_id and sd2.STATUS = 'A' and sd1.STATUS = 'A' and sd.STATUS = 'A' "
				+"	   and sd.code in (" + code + ")";
			return this.executeSqlForObject(sql);
		}

	}
	
	
	/**
	 * 
	 * @description: 查询数据 返回List<Map<String,Object>>
	 * @author：yuan.yw
	 * @param sqlString
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 20, 2013 3:32:45 PM
	 */
	public List<Map<String,Object>> executeSqlForObject(final String sqlString) {
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
	/**
	 * 
	 * @description: 删除或更新操作
	 * @author：yuan.yw
	 * @param sqlString
	 * @return     
	 * @return Boolean     
	 * @date：Aug 20, 2013 3:37:19 PM
	 */
	public Boolean executeUpdateSql ( final String sqlString) {
		Boolean execute = hibernateTemplate.execute( new HibernateCallback<Boolean>() {
			public Boolean doInHibernate(Session session) throws HibernateException,
					SQLException {
				SQLQuery query = session.createSQLQuery(sqlString);
				int executeUpdate = query.executeUpdate();
				boolean flag = executeUpdate > 0 ;
				return flag;
			}
		});
		return execute;
	}

	/**
	 * 
	 * @description: 获取所有系统
	 * @author：zhang.wy1
	 * @return
	 * @return: List<Map<String,Object>>          
	 * @date：2014-1-9 下午4:02:26
	 */
	public List<Map<String, Object>> getAllSystem() {
		String sql = 	"select s2.* from SYS_DICTIONARY s1,SYS_DICTIONARY s2 where s1.code='System' and s1.data_type_id=s2.parent_id";
		List<Map<String,Object>> list = this.executeSqlForObject(sql);
		return list;
	} 
	
	
}
