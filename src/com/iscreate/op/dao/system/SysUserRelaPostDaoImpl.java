package com.iscreate.op.dao.system;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.dao.common.BaseDao;

public class SysUserRelaPostDaoImpl extends BaseDao implements SysUserRelaPostDao {
	
	private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private HibernateTemplate hibernateTemplate;
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}


	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}


	/**
	 * 保存用户和岗位关系
	* @author ou.jh
	* @date Jan 13, 2014 9:27:22 AM
	* @Description: TODO 
	* @param @param sysUserRelaPost
	* @param @return        
	* @throws
	 */
	public int saveSysUserRelaPost(Map<String, Object> infoMap){
		Set<String> keySet = null;
		if(infoMap != null){
			keySet = infoMap.keySet();
		}else{
			return 0;
		}
		//INSERT语句
		String insertSql = "INSERT INTO SYS_USER_RELA_POST s (CREATE_TIME";
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
	 * 更新用户和岗位关系状态
	* @author ou.jh
	* @date Sep 3, 2013 2:28:34 PM
	* @Description: TODO 
	* @param @param infoMap
	* @param @param id
	* @param @return        
	* @throws
	 */
	public int updateSysUserRelaPostStatus(String status,long orgUserId){
		//update 表 set 字段1 = 值,字段2 = 值 where 字段3 = 值
		//INSERT语句
		String sql = "update SYS_USER_RELA_POST s set s.STATUS = '" + status + "',s.mod_time = sysdate where s.ORG_USER_ID = " + orgUserId;
		return this.executeSql(sql,hibernateTemplate);
	}
	
	
	/**
	 * 根据orguserid获取用户和岗位关系
	* @author ou.jh
	* @date Jan 13, 2014 4:06:14 PM
	* @Description: TODO 
	* @param @param orgUserId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getSysUserRelaPostListByorgUserId(long orgUserId){
		if(orgUserId == 0){
			return null;
		}
		String sql = " select s.*,o.name,o.org_type from sys_user_rela_post s,sys_org o where s.org_id = o.org_id and s.status != 'X' and s.org_user_id = " + orgUserId;

		return this.executeSqlForList(sql, hibernateTemplate);
	}
	/**
	 * 
	 * @description: 根据岗位code字符串(","分隔) 部门id 获取人员信息
	 * @author：yuan.yw
	 * @param postCodes
	 * @param orgId
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jan 15, 2014 9:12:41 AM
	 */
	public List<Map<String,Object>> getUserListByPostCodesAndOrgId(String postCodes,long orgId){
		
		String conditionSql = "";//条件组装string
		if(postCodes!=null && !"".equals(postCodes)){
			conditionSql +=" and surp.post_code in ("+postCodes+")";
		}
		if(orgId>0){
			conditionSql +=" and surp.org_id="+orgId;
		}
		
		String sql =" select su.* from "
					+" sys_org_user su "
					+" where su.org_user_id in( "
					+" select  "
					+" sou.org_user_id "
					+" from sys_org_user sou,sys_user_rela_post surp  "
					+" where  "
					+" sou.org_user_id=surp.org_user_id  "
					+" and surp.status!='X'  "
					+conditionSql
					+" )";
		return super.executeSqlForList(sql, hibernateTemplate);
	}
}
