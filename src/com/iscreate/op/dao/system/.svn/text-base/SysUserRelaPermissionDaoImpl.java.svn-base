package com.iscreate.op.dao.system;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.dao.common.BaseDao;

public class SysUserRelaPermissionDaoImpl  extends BaseDao  implements SysUserRelaPermissionDao{
	private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	/**
	 * @author duhw
	 * @create_time 2014-01-14
	 * 通过用户标识得到用户的权限
	 * permissionType:(菜单权限)、(数据权限)
	 * flag:true(得到需要验证的权限),false(所有的权限)
	 * 注：用户的权限包括用户sys_user_rela_permission表中的权限和sys_role_rela_permission中的权限
	 * * return:所有的权限列表（如果用户有权限，则相应的权限数据flag=1）
	 */
	public List<Map<String,Object>> getPermissionListByUserId(long user_id,String system,String permissionType,boolean flag){
		//所有的权限
		 String per_sql = "select level grade,  a.* from sys_permission a  connect by prior a.permission_id=a.parent_id " +
		 		          " start with (a.parent_id is null or a.parent_id=0)";
		 //and a.pro_code='PM' and a.type='PM_MenuResource' order siblings by a.sequenceindex
         if(!system.equals("")){
        	 per_sql += " and a.pro_code='"+system+"'";
         }
         if(!permissionType.equals("")){
        	 per_sql += " and a.type='"+permissionType+"'";
         }
         if(flag){
        	 per_sql += " and a.enalbed=1";
         }
         //用户直接关联的权限
         String user_sql = " select permission_id from sys_user_rela_permission where status='A' and org_user_id="+user_id;
         //用户角色关联的权限
         String role_sql = " select distinct permission_id from sys_role_rela_permission where status='A' and role_id in "+
                           "    (select role_id from sys_user_rela_role where org_user_id="+user_id+" and status='A')";
         String sql = "select  distinct A.*,nvl2(B.permission_id||c.permission_id,1,0) flag,nvl2(B.permission_id,1,0) user_flag," +
         		      " nvl2(C.permission_id,1,0) role_flag from ("+per_sql+") A"+
                      " left join ("+user_sql+") B on b.permission_id=A.permission_id"+
                      " left join ("+role_sql+") C on c.permission_id=A.permission_id";
         return this.executeSqlForList(sql, hibernateTemplate);
	}
	
	/**
	 * 根据用户ID与权限父级ID获取的权限列表
	* @author ou.jh
	* @date Jan 15, 2014 10:11:36 AM
	* @Description: TODO 
	* @param @param orgUserId
	* @param @param parentId
	* @param @return        
	* @throws
	 */
	public List<Map<String,Object>> getSysPermissionListByOrgUserIdAndParentId(long orgUserId , long parentId){
		String sql = ""
			+" select distinct * "
			+"  from (select sp.*, sou.org_user_id "
			+"          from sys_permission           sp, "
			+"               sys_role_rela_permission srrp, "
			+"               sys_user_rela_role       surr, "
			+"              sys_org_user                   sou "
			+"        where sp.permission_id = srrp.permission_id "
			+"          and srrp.role_id = surr.role_id "
			+"          and surr.org_user_id = sou.org_user_id "
			+"         and srrp.status = 'A' "
			+"         and surr.status = 'A' "
			+"         and sou.org_user_id =  " +orgUserId
			+"         and sp.parent_id =  " +parentId
			+"       "
			+"      union all "
			+"      select sp.*, sou.org_user_id "
			+"        from sys_permission     sp, "
			+"             sys_user_rela_permission surp, "
			+"             sys_org_user             sou "
			+"       where sp.permission_id = surp.permission_id "
			+"         and surp.org_user_id = sou.org_user_id "
			+"         and surp.status = 'A' "
			+"         and surp.org_user_id =  " +orgUserId
			+"         and sp.parent_id = "+parentId+")  order by sequenceindex asc ";
		return this.executeSqlForList(sql, hibernateTemplate);
	}
	
	/**
	 * @author ou.jh
	 * @create_time 2014-01-14
	 * 通过用户标识得到一级用户的权限
	 * permissionType:MENU(菜单权限),DATA(数据权限)
	 * flag:true(得到需要验证的权限),false(所有验的权限)
	 * 注：用户的权限包括用户sys_user_rela_permission表中的权限和sys_role_rela_permission中的权限
	 * * return:所有的权限列表（如果用户有权限，则相应的权限数据flag=1）
	 */
	public List<Map<String,Object>> getFirstPermissionListByUserId(long user_id,String system,String permissionType,boolean flag){
		//所有的权限
		 String per_sql = "select a.* from sys_permission a " +
		 		          " where (a.parent_id is null or a.parent_id=0)";
		 //and a.pro_code='PM' and a.type='PM_MenuResource' order siblings by a.sequenceindex
         if(!system.equals("")){
        	 per_sql += " and a.pro_code='"+system+"'";
         }
         if(!permissionType.equals("")){
        	 per_sql += " and a.type='"+permissionType+"'";
         }
         if(flag){
        	 per_sql += " and a.enalbed=1";
         }
         //用户直接关联的权限
         String user_sql = " select permission_id from sys_user_rela_permission where status='A' and org_user_id="+user_id;
         //用户角色关联的权限
         String role_sql = " select permission_id from sys_role_rela_permission where status='A' and role_id in "+
                           "    (select role_id from sys_user_rela_role where org_user_id="+user_id+" and status='A')";
         String sql = "select distinct A.*,nvl2(B.permission_id||c.permission_id,1,0) flag,nvl2(B.permission_id,1,0) user_flag," +
         		      " nvl2(C.permission_id,1,0) role_flag from ("+per_sql+") A"+
                      " left join ("+user_sql+") B on b.permission_id=A.permission_id"+
                      " left join ("+role_sql+") C on c.permission_id=A.permission_id";
    
         return this.executeSqlForList(sql, hibernateTemplate);
	}
	/**
	 * 
	 * @description: 根据资源所属系统编码 资源类型 关联的SERV_TYPE获取系统资源list(关联用户和权限关系表查询 记录判断用户是否已关联资源)
	 * @author：yuan.yw
	 * @param proCode
	 * @param type
	 * @param servType
	 * @param orgUserId
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jan 9, 2014 6:30:57 PM
	 */
	public List<Map<String,Object>> getSysPermissionListByProCodeAndTypeAndServType(String proCode,String type,String servType,long orgUserId){
		if("".equals(proCode) || proCode==null || "".equals(type) || type==null||"".equals(servType) || servType==null){
			return null;
		}
		if(orgUserId<0){
			orgUserId=0L;
		}
		String sql = " select s.* from "
		+" (select sp.*, "
        +"       case when sp.serv_type='PM_Project' "
        +"       then "
        +"       (select sou.name from pro_info pi,sys_org_user sou where pi.manage_user_id=sou.org_user_id and pi.pro_id=sp.serv_id) "
        +"       else "
        +"       null "
        +"       end "
        +"       PROJECTMANAGER, "
		+" 	       nvl(surd.permission_id, 0) RELA_PERMISSION_ID, "
		+" 	       surd.org_user_id RELA_ORG_USER_ID from sys_permission sp "
		+" 	left join sys_user_rela_permission surd on surd.org_user_id = "+orgUserId
		+" 	                                   and surd.permission_id = sp.permission_id and surd.status!='X' "
		+" 	where sp.pro_code = '"+proCode+"' and sp.type = '"+type+"') s"
		+" 	connect by prior s.permission_id = s.parent_id"
		+" 	         start with  s.serv_type = '"+servType+"'"
		+" 	                and (s.parent_id = 0 or s.parent_id is null)";
		return super.executeSqlForList(sql,hibernateTemplate);
	}
	/**
	 * 
	 * @description:  （PM）根据资源所属系统编码 资源类型 关联的SERV_TYPE 删除用户关联数据权限
	 * @author：yuan.yw
	 * @return     
	 * @return boolean     
	 * @date：Jan 13, 2014 10:49:06 AM
	 */
	public boolean deleteUserPMDataPermissionByProCodeAndTypeAndServType(String proCode,String type,String servType,long orgUserId){
		if("".equals(proCode) || proCode==null || "".equals(type) || type==null||"".equals(servType) || servType==null|| orgUserId<0){
			return false;
		}
		String sql = "update sys_user_rela_permission surd  set surd.status='X',surd.mod_time=sysdate "
					+" where surd.org_user_id="+orgUserId+" "
					+" and surd.permission_id"
					+" in "
					+" ("
					+" select sp.permission_id from sys_permission sp"
					+" where sp.pro_code='"+proCode+"'"
					+" and sp.type='"+type+"'"
					+" and sp.serv_type in ("+servType+")"
					+" ) and surd.status!='X' ";
		int resultInt = super.executeSql(sql, hibernateTemplate);
		if(resultInt>-1){
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @description: 保存用户数据范围
	 * @author：yuan.yw
	 * @param orgUserId
	 * @param permissionId
	 * @return     
	 * @return boolean     
	 * @date：Jan 13, 2014 11:08:39 AM
	 */
	public boolean savePMUserRelaData(long orgUserId,long permissionId){
		if(orgUserId<0 || permissionId<0){
			return false;
		}
	    //INSERT语句
		String insertSql = "INSERT INTO sys_user_rela_permission(ORG_USER_ID,PERMISSION_ID,CREATE_TIME,STATUS)";
		//VALUES语句
		String valueSql = " VALUES ("+orgUserId+","+permissionId+",sysdate,'A')";
		String sql = insertSql + valueSql;
		if(executeSql(sql,hibernateTemplate) > 0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 
	 * @description: 根据权限id删除用户权限
	 * @author：yuan.yw
	 * @param permissionId
	 * @return     
	 * @return boolean     
	 * @date：Jan 14, 2014 1:47:40 PM
	 */
	public boolean deleteUserRelaPermissionByPermissionId(long permissionId){
		String sql = " update sys_user_rela_permission surd  set surd.status='X',surd.mod_time=sysdate "
					+" where surd.status!='X' "
					+" and surd.permission_id in ( "
					+" select  s.permission_id from sys_permission s  "
					+" connect by prior s.permission_id=s.parent_id "
					+" start with s.permission_id= "+permissionId
					+")";
		int resultInt = super.executeSql(sql, hibernateTemplate);
		if(resultInt>-1){
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @description: 通过业务id 业务类型获取权限资源map
	 * @author：yuan.yw
	 * @param servId
	 * @param servType
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Jan 14, 2014 5:52:04 PM
	 */
	public Map<String,Object> getPermissionMapByServIdAndServType(long servId,String servType){
		if(servId<0||"".equals(servType)||servType==null){
			return null;
		}
		String sql =" select * from sys_permission sp where sp.serv_id="+servId+" and serv_type='"+servType+"'";
		return super.executeSqlForMap(sql, hibernateTemplate);
	}
	
	/**
	 * 
	 * @description: 通过权限id获取用户关联权限关系list
	 * @author：yuan.yw
	 * @param permissionId
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jan 15, 2014 10:00:41 AM
	 */
	public List<Map<String,Object>> getUserRelaPermissionListByPermissionId(long permissionId){
		if(permissionId<0){
			return null;
		}
		String sql = "select surp.* from sys_user_rela_permission surp where surp.status!='X' and surp.permission_id="+permissionId;
		return super.executeSqlForList(sql, hibernateTemplate);
	}
	/**
	 * 
	 * @description: 通过用户id获取用户的权限部门list(PM项目使用)
	 * @author：yuan.yw
	 * @param orgUserId 用户id
	 * @return     部门ORG信息list
	 * @return     List<Map<String,Object>> 
	 * @date：Jan 16, 2014 10:27:21 AM
	 */
	public List<Map<String,Object>> getUserPermissionOrgListByUserId(long orgUserId){
		if(orgUserId<=0){
			return null;
		}
		String sql = "select  "
					+" so.*  "
					+" from sys_org so,sys_permission sp,sys_user_rela_permission surp  "
					+" where sp.serv_id=so.org_id  "
					+" and sp.permission_id=surp.permission_id  "
					+" and surp.org_user_id="+orgUserId
					+" and surp.status!='X' "
					+" and sp.type='PM_DataResource' "
					+" and sp.serv_type='PM_Org'  "
					+" and sp.pro_code ='PM' ";
		return super.executeSqlForList(sql, hibernateTemplate);
	}
	
	/**
	 * 
	 * @description: 通过部门id 用户id获取用户的权限项目list(PM项目使用)
	 * @author：yuan.yw
	 * @param orgId 部门id  (部门id为0 查询用户所有权限项目list)
	 * @param orgUserId 用户id
	 * @return    项目PROJECT信息List 
	 * @return List<Map<String,Object>>     
	 * @date：Jan 16, 2014 10:31:42 AM
	 */
	public List<Map<String,Object>> getUserPermissionProjectListByOrgIdAndUserId(long orgId,long orgUserId){
		if(orgId<0||orgUserId<=0){
			return null;
		}
		String orgSql = "";
		if(orgId>0){
			orgSql+=" inner join ( "
			+" select s.permission_id "
			+" from sys_permission s  "
			+" where s.serv_id="+orgId
			+" and s.type='PM_DataResource' "
			+" and s.serv_type='PM_Org'  "
			+" and s.pro_code ='PM') o on o.permission_id = sp.parent_id ";
		}
		
		String sql = "select  "
					+" pi.* "
					+" from pro_info pi,sys_user_rela_permission surp, "
					+" sys_permission sp "
					+orgSql
					+" where sp.serv_id=pi.pro_id "
					+" and sp.permission_id=surp.permission_id  "
					+" and surp.org_user_id="+orgUserId
					+" and surp.status!='X' "
					+" and sp.type='PM_DataResource' "
					+" and sp.serv_type='PM_Project'  "
					+" and sp.pro_code ='PM' ";
		return super.executeSqlForList(sql, hibernateTemplate);
	}
	/**
	 * 
	 * @description: 通过用户id 系统编码code获取用户权限菜单list
	 * @author：yuan.yw
	 * @param orgUserId 用户id
	 * @param systemCode 系统编码code
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jan 20, 2014 3:56:54 PM
	 */
	public List<Map<String,Object>> getUserPermissionMenuListByUserIdAndSystemCode(long orgUserId,String systemCode){
		if(orgUserId<=0 || systemCode==null || "".equals(systemCode)){
			return null;
		}
		String sql = ""
			+" select distinct * "
			+"  from (select sp.*, sou.org_user_id "
			+"          from sys_permission           sp, "
			+"               sys_role_rela_permission srrp, "
			+"               sys_user_rela_role       surr, "
			+"              sys_org_user                   sou "
			+"        where sp.permission_id = srrp.permission_id "
			+"          and srrp.role_id = surr.role_id "
			+"          and surr.org_user_id = sou.org_user_id "
			+"         and srrp.status = 'A' "
			+"         and surr.status = 'A' "
			+"         and sou.org_user_id =  " +orgUserId
			+"         and sp.pro_code =  '" +systemCode+"'"
			+"        and sp.type='"+systemCode+"_MenuResource'"
			+"      union all "
			+"      select sp.*, sou.org_user_id "
			+"        from sys_permission     sp, "
			+"             sys_user_rela_permission surp, "
			+"             sys_org_user             sou "
			+"       where sp.permission_id = surp.permission_id "
			+"         and surp.org_user_id = sou.org_user_id "
			+"         and surp.status = 'A' "
			+"         and surp.org_user_id =  " +orgUserId
			+"         and sp.pro_code = '"+systemCode+"'" 
			+"        and sp.type='"+systemCode+"_MenuResource'"
			+" ) ";
		return super.executeSqlForList(sql, hibernateTemplate);
		
	}
}
