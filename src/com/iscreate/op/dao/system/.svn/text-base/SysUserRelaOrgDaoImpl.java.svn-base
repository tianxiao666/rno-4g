package com.iscreate.op.dao.system;


import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.dao.common.BaseDao;
import com.iscreate.op.pojo.system.SysUserRelaOrg;

public class SysUserRelaOrgDaoImpl extends BaseDao implements SysUserRelaOrgDao {
	private HibernateTemplate hibernateTemplate;
	
	private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	/**
	 * @author:duhw
	 * @create_time:2013-05-29
	 * 根据人员标识得到人员所有的上级领导
	 */
	public List<Map<String,Object>> getAllTopUserByOrgUserId(long org_user_id){
		String sql = "select a.account \"account\",a.org_user_id \"orgUserId\",b.name \"name\" from sys_account a"+
						" inner join sys_org_user b on b.org_user_id=a.org_user_id"+
						" inner join (select * from sys_org connect by prior parent_id=org_id"+
						"		start with org_id in (select distinct a.org_id from sys_user_rela_org a"+ 
						"	 inner join sys_account b on b.org_user_id=a.org_user_id and b.org_user_id="+org_user_id+"))"+
						"     c on c.org_user_id=b.org_user_id";
		return (List<Map<String,Object>>)this.executeSqlForObject(sql);
	}
	/**
	 * @author:duhw
	 * @create_time:2013-05-13
	 * @param org_user_id:用户标识
	 * @return 组织列表(包括上级组织和下级组织)
	 */
	public List<Map<String,Object>> getAllTreeOrgByOrgUserId(long org_user_id){
		String sql = "select * from (select level grade, a.* from sys_org a connect by prior a.parent_id=a.org_id"+
                       " start with a.org_id in (select org_id from sys_user_rela_org where org_user_id="+org_user_id+")"+ 
                       " union"+
                       " select level grade, a.* from sys_org a connect by prior a.org_id=a.parent_id"+
                       " start with a.parent_id in (select org_id from sys_user_rela_org where org_user_id="+org_user_id+"))" +
                       " order by path";
		return (List<Map<String,Object>>)this.executeSqlForObject(sql);
	}
	
	
	/**
	 * 保存用户和组织关系
	* @author ou.jh
	* @date Jan 13, 2014 9:27:22 AM
	* @Description: TODO 
	* @param @param sysUserRelaPost
	* @param @return        
	* @throws
	 */
	public int saveSysUserRelaOrg(Map<String, Object> infoMap){
		Set<String> keySet = null;
		if(infoMap != null){
			keySet = infoMap.keySet();
		}else{
			return 0;
		}
		//INSERT语句
		String insertSql = "INSERT INTO SYS_USER_RELA_ORG s (CREATETIME";
		//VALUES语句
		String valueSql = " VALUES (sysdate";
		//INSERT语句
		for(String key:keySet){
			if(infoMap.get(key) != null){
				insertSql = insertSql + ",s."+key;
				//判断类型(date)
				if(infoMap.get(key).getClass().toString().indexOf("java.util.Date")  > - 1){
					String format = sf.format(infoMap.get(key));
					valueSql = valueSql + ",to_date('"+format+"','yyyy-mm-dd hh24:mi:ss')";
				}
				//判断类型(String)
				else if(infoMap.get(key).getClass().toString().indexOf("java.lang.String")  > - 1){
					valueSql = valueSql + ",'"+infoMap.get(key).toString().replace("'","''") + "'";
				}
				//判断类型（其他）
				else {
					valueSql = valueSql + ","+infoMap.get(key).toString().replace("'","''");
				}
			}else{
				continue;
			}
		}
		insertSql = insertSql + ")";
		valueSql = valueSql + ")";
		String sql = insertSql + valueSql;
		return executeSql(sql,hibernateTemplate);
	}
	
	
	/**
	 * 
	 * @description: 根据组织orgId 和 orgUserIds 获取组织与人员关系
	 * @author：
	 * @param orgId
	 * @param orgUserIds
	 * @return     
	 * @return List<SysUserRelaOrgDao>     
	 * @date：May 13, 2013 3:49:48 PM
	 */
	public List<SysUserRelaOrg> getSysUserRelaOrgListByOrgUserIdsAndOrgId(
			long orgId, String orgUserIds) {
		if("0".equals(orgId)||"".equals(orgUserIds)){
			return null;
		}
		String hql = "from SysUserRelaOrg sr where sr.orgId="+orgId+" and sr.orgUserId in ("+orgUserIds+")";
		
		return (List<SysUserRelaOrg>) hibernateTemplate.find(hql);
	}
	/**
	 * 
	 * @description: 根据账号获取组织列表(下级组织)
	 * @author：yuan.yw
	 * @param account
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：May 23, 2013 2:59:49 PM
	 */
	public List<Map<String,Object>> getAllTreeOrgByAccount(String account){
		String sql = "select * from ( select level grade, a.* from sys_org a connect by prior a.org_id=a.parent_id"+
                       " start with a.org_id in (select org_id from sys_account sa,sys_org_user su,sys_user_rela_org sr where  sa.org_user_id=su.org_user_id and su.org_user_id=sr.org_user_id and  sa.account='"+account+"'))" +
                       " order by path";
		return (List<Map<String,Object>>)this.executeSqlForObject(sql);
	}
	
	/**
	 * 更新用户和部门关系状态
	* @author ou.jh
	* @date Sep 3, 2013 2:28:34 PM
	* @Description: TODO 
	* @param @param infoMap
	* @param @param id
	* @param @return        
	* @throws
	 */
	public int updateSysUserRelaOrgStatus(String status,long orgUserId){
		//INSERT语句
		String sql = "update sys_user_rela_org s set s.STATUS = '" + status + "',s.updatetime = sysdate where s.ORG_USER_ID = " + orgUserId;
		return this.executeSql(sql,hibernateTemplate);
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
}
