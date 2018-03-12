package com.iscreate.op.dao.system;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.dao.common.BaseDao;
import com.iscreate.op.pojo.system.SysPermission;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;

public class SysPermissionDaoImpl extends BaseDao implements SysPermissionDao {
	private HibernateTemplate hibernateTemplate;

	/**
	 * 获取第一级权限
	 * 
	 * @return
	 */
	public List<SysPermission> getRootPermission() {
		String hql = "select s from SysPermission s where parentId is null or parentId =''";
		return (List<SysPermission>) hibernateTemplate.find(hql);
	}

	/**
	 * 根据业务模块获取其权限树
	 */
	public List<Map<String, Object>> getPermissionTreeByType() {
		return hibernateTemplate.executeFind(new HibernateCallback() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "SELECT q.NAME,q.TITLE,q.URL,q.PARAMETER,q.DESCRIPTION,q.PID,q.PARENTID,q.IS_SHOW_WORKPLAT,spt.PER_TYPE_ID TYPEID,spt.NAME TYPENAME,spt.CODE TYPECODE,sp.NAME PTNAME ,SP.PER_TYPE_ID PTPID ,q.ENALBED ,q.CODE from (select level l,name,TITLE,ENALBED,IS_SHOW_WORKPLAT,URL,PARAMETER,DESCRIPTION, PERMISSION_ID PID,PARENT_ID PARENTID,PER_TYPE_ID, CODE from SYS_PERMISSION sqt connect by prior PERMISSION_ID=PARENT_ID start with PARENT_ID is NULL order by l,name) q join SYS_PERMISSION_TYPE spt on q.PER_TYPE_ID = SPT.PER_TYPE_ID LEFT JOIN (SELECT * from SYS_PERMISSION_TYPE ) sp on spt.PARENT_ID = sp.PER_TYPE_ID";
				
				/*String sql = "SELECT q.NAME,"+
						"       q.TITLE,"+
						"       q.URL,"+
						"       q.PARAMETER,"+
						"       q.PID,"+
						"       q.PARENTID,"+
						"       q.IS_SHOW_WORKPLAT,"+
						"       q.ENALBED,"+
						"       q.CODE,"+
						"       q.pro_code"+
						"  from (select level            l,"+
						"               name,"+
						"               TITLE,"+
						"               ENALBED,"+
						"               IS_SHOW_WORKPLAT,"+
						"               URL,"+
						"               PARAMETER,"+
						"               PERMISSION_ID    PID,"+
						"               PARENT_ID        PARENTID,"+
						"               PRO_CODE,"+
						"               CODE"+
						"          from SYS_PERMISSION sqt"+
						"         where pro_code = 'PM'"+
						"        connect by prior PERMISSION_ID = PARENT_ID"+
						"         start with PARENT_ID is NULL"+
						"         order by l, name) q";*/
				//System.out.println(sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String, Object>> list = query.list();
				if (list == null || list.isEmpty()) {
					return null;
				} else {
					return list;
				}

			}
		});
	}
	
	/**
	 * 根据角色获取其权限树
	 */
	public List<Map<String, Object>> getPermissionTreeByRole(final long roleId) {
		return hibernateTemplate.executeFind(new HibernateCallback() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
//				String sql = "SELECT q.NAME,q.TITLE,q.URL,q.PARAMETER,q.DESCRIPTION,q.PID,q.PARENTID,q.IS_SHOW_WORKPLAT,spt.NAME TYPENAME,spt.CODE TYPECODE,sp.NAME PTNAME ,SP.PER_TYPE_ID PTPID ,q.ENALBED ,q.IS_SHOW_WORKPLAT,q.CODE,srrp.AUTH_TYPE,srrp.PER_ACCESS from (select level l,name,TITLE,ENALBED,URL,PARAMETER,DESCRIPTION, PERMISSION_ID PID,PARENT_ID PARENTID,PER_TYPE_ID, CODE,IS_SHOW_WORKPLAT from SYS_PERMISSION sqt connect by prior PERMISSION_ID=PARENT_ID start with PARENT_ID is NULL order by l) q join SYS_PERMISSION_TYPE spt on q.PER_TYPE_ID = SPT.PER_TYPE_ID LEFT JOIN (SELECT * from SYS_PERMISSION_TYPE ) sp on spt.PARENT_ID = sp.PER_TYPE_ID left join (select * from SYS_ROLE_RELA_PERMISSION where ROLE_ID = ?) srrp on q.PID = srrp.PERMISSION_ID ";
				/*
				String sql = "SELECT q.NAME,"+
						"       q.TITLE,"+
						"       q.URL,"+
						"       q.PARAMETER,"+
						"       q.PID,"+
						"       q.PARENTID,"+
						"       q.IS_SHOW_WORKPLAT,"+
						"       q.ENALBED,"+
						"       q.IS_SHOW_WORKPLAT,"+
						"       q.CODE"+
						"  from (select level            l,"+
						"               name,"+
						"               TITLE,"+
						"               ENALBED,"+
						"               URL,"+
						"               PARAMETER,"+
						"               PERMISSION_ID    PID,"+
						"               PARENT_ID        PARENTID,"+
						"               CODE,"+
						"               IS_SHOW_WORKPLAT"+
						"          from SYS_PERMISSION sqt"+
						"        connect by prior PERMISSION_ID = PARENT_ID"+
						"         start with PARENT_ID is NULL"+
						"         order by l) q"+
						"  left join (select * from SYS_ROLE_RELA_PERMISSION where ROLE_ID = ?) srrp"+
						"    on q.PID = srrp.PERMISSION_ID";*/
				
				String sql = "select * from sys_role_rela_permission where role_id = ? and status = 'A'";
				SQLQuery query = session.createSQLQuery(sql);
				query.setLong(0, roleId);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String, Object>> list = query.list();
				if (list == null || list.isEmpty()) {
					return null;
				} else {
					return list;
				}

			}
		});
	}
	

	/**
	 * 根据角色获取权限资源
	 */
	@SuppressWarnings("unchecked")
	public List<SysPermission> getPermissionByRole(final long roleId) {
		String hql = "select target from SysRoleRelaPermission as rela, SysPermission as target";
		hql += " where rela.roleId='" + roleId + "'";
		hql += " and rela.permissionId = target.permissionId";
		List<SysPermission> find = hibernateTemplate.find(hql);
		if (find == null || find.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		return find;
	}

	/**
	 * 根据角色 获取权限资源对象和角色资源关联对象(SysPermission,SysRoleRelaPermission)
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPermissionAndRoleRelaPermissionMapByRole(
			final long roleId) {
		List<Map<String, Object>> list = this.hibernateTemplate
				.execute(new HibernateCallback<List<Map<String, Object>>>() {

					public List<Map<String, Object>> doInHibernate(
							Session session) throws HibernateException,
							SQLException {
						List find = null;
						String sql = "select p.PERMISSION_ID \"permissionId\",p.code \"code\",p.name \"name\",p.title \"title\"," +
								" p.url \"url\",p.parameter \"parameter\",p.PER_TYPE_ID \"perTypeId\"," +
								" p.PARENT_ID \"parentId\", p.path \"path\",p.description \"description\"," +
								" p.enalbed \"enalbed\",p.is_show_workplat \"isShowWorkplat\"," +
								" p.createtime \"createtime\" ,p.updatetime \"updatetime\","
								+ " r.ROLE_ID \"roleId\", r.PER_ACCESS \"perAccess\",r.AUTH_TYPE \"authType\" "// r.PERMISSION_ID
																												// \"permissionId\",r.createtime
																												// \"createtime\",r.updatetime
																												// \"updatetime\"
								+ " from SYS_PERMISSION p, SYS_ROLE_RELA_PERMISSION r "
								+ " where p.PERMISSION_ID = r.PERMISSION_ID and r.ROLE_ID='"
								+ roleId + "'";

						SQLQuery query = session.createSQLQuery(sql);
						query
								.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						find = query.list();
						return find;
					}
				});
		return list;
	}

	/**
	 * 删除权限
	 * 
	 * @param permissionId
	 * @return
	 */
	public boolean deletePermissionById(final long permissionId) {
		final long fid = permissionId;
		hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				SysPermission resource = (SysPermission) session
						.createCriteria(SysPermission.class).add(
								Restrictions.eq("permissionId", fid))
						.uniqueResult();
				if (resource == null) {
					throw new UserDefinedException("不存在id=" + fid
							+ "的资源，无法执行删除操作！");
				}

				// 删除与业务模块的关联关系
				String sql = "delete from SysPermission as module where module.permissionId=? ";
				Query query = session.createQuery(sql);
				query.setLong(0, fid);
				int cnt = query.executeUpdate();
				return null;
			}

		});
		return true;
	}
	
	/**
	 * 删除权限与角色关联关系
	* @author ou.jh
	* @date Jun 19, 2013 1:56:50 PM
	* @Description: TODO 
	* @param @param permissionId
	* @param @param roleId
	* @param @return        
	* @throws
	 */
	public boolean deleteRoleRelaPermissionById(final long permissionId,final long roleId) {
		hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {

				// 删除与业务模块的关联关系
				String sql = "delete from sys_role_rela_permission module where module.permission_id=? and module.role_id=?";
				Query query = session.createSQLQuery(sql);
				query.setLong(0, permissionId);
				query.setLong(1, roleId);
				int cnt = query.executeUpdate();
				return null;
			}

		});
		return true;
	}
	
	
	/**
	 * 根据角色ID删除角色与权限关联关系
	 * @author li.hb
	 * @date 2014-1-13 下午3:17:33
	 * @Description: TODO 
	 * @param @param roleId
	 * @param @return        
	 * @throws
	 */
	public boolean deleteRoleRelaPermissionByRoleId(final long roleId) {
		hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {

				// 删除与业务模块的关联关系
				String sql = "update sys_role_rela_permission module set module.status = 'X' where module.role_id=?";
				Query query = session.createSQLQuery(sql);
				query.setLong(0, roleId);
				int cnt = query.executeUpdate();
				return null;
			}

		});
		return true;
	}
	
	
	
	
	/**
	 * @author du.hw
	 * @create_time 2013-06-06
	 * 根据用户账号标识得到用户权限（返回的是所有需要控制的权限列表，当前用户拥有的权限，其权限数据中标识flag为1,否则为0）
	 * 注：此处得到是所有的可用权限（即：需要验证的权限）
	 */
	public List<Map<String,Object>> getAllEnablePermissionByAccountId(long account_id){
		 String sql = "select nvl2(b.role_id,1,0) \"flag\"," +
		 		       "a.permission_id \"permissionId\"," +
		 		       "a.code \"code\"," +
		 		       "a.name \"name\"," +
		 		       "a.title \"title\"," +
		 		       "a.url \"url\"," +
		 		       "a.enalbed \"enalbed\"," +
		 		       "a.parent_id \"parentId\"," +
		 		       "a.parameter \"parameter\"," +
		 		       "a.per_type_id \"perTypeId\"" +
		 		       " from SYS_PERMISSION a "+
                       " left join (select * from sys_role_rela_permission where role_id in " +
                       " (select role_id from sys_user_rela_role where account_id="+account_id+")) "+
					   "	b on b.permission_id=a.permission_id"+
					   "	where a.enalbed=1";
		 List<Map<String,Object>> returnList = this.executeSqlForList(sql, hibernateTemplate);
		 return returnList;
	}

	
	
	
	
	/**
	 * 根据账号获取模块(PM项目管理)
	* @author ou.jh
	* @date Jun 7, 2013 2:15:42 PM
	* @Description: TODO 
	* @param @param account        
	* @throws
	 */
	public List<Map<String, Object>> getPMPermissionModuleByAccount(long org_user_id){
		String sql=	" select distinct sp.* "
					+"	    from sys_permission           sp, "
					+"        sys_permission_type      spt, "
					+"        sys_role_rela_permission srrp, "
					+"        sys_role                 sr, "
					+"        sys_account              sa, "
					+"        sys_user_rela_role       surr "
					+"  where sp.per_type_id = spt.per_type_id "
					+"   and sp.permission_id = srrp.permission_id "
					+"   and sr.role_id = srrp.role_id "
					+"   and sr.role_id = surr.role_id "
					+"   and sa.account_id = surr.account_id "
					+"   and sp.parent_id in (  select distinct sp.permission_id "
					+"   from sys_permission           sp, "
					+"        sys_permission_type      spt, "
					+"        sys_role_rela_permission srrp, "
					+"        sys_role                 sr, "
					+"        sys_account              sa, "
					+"        sys_user_rela_role       surr "
					+"  where sp.per_type_id = spt.per_type_id "
					+"    and sp.permission_id = srrp.permission_id "
					+"    and sr.role_id = srrp.role_id "
					+"    and sr.role_id = surr.role_id "
					+"    and sa.account_id = surr.account_id "
					+"    and spt.code = 'module' "
					+"    and sa.org_user_id = " + org_user_id
					+"    and sp.code like 'PM_%' "
					+" ) "
					+"    and sp.code like 'PM_%' "
					+"    and sa.org_user_id = " + org_user_id
					+"   ORDER by sp.sequenceindex asc, sp.createtime asc ";
		return this.executeSqlForList(sql, hibernateTemplate);
	}
	
	  

	
	/**
	 * 根据父ID与账号获取模块
	* @author ou.jh
	* @date Jun 7, 2013 2:58:43 PM
	* @Description: TODO 
	* @param @param parent_id
	* @param @param account
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getPermissionByParentIdAndAccount(long parent_id,String account){
		String sql= "	select distinct sp.* "
					+"		  from sys_permission           sp, "
					+"		       sys_permission_type      spt, "
					+"		       sys_role_rela_permission srrp, "
					+"		       sys_role                 sr, "
					+"		       sys_account              sa, "
					+"		       sys_user_rela_role       surr "
					+"		 where sp.per_type_id = spt.per_type_id "
					+"		   and sp.permission_id = srrp.permission_id "
					+"		   and sr.role_id = srrp.role_id "
					+"		   and sr.role_id = surr.role_id "
					+"		   and sa.account_id = surr.account_id "
					+"    	   and sp.code  not like 'PM_%' "
					+"		   and sp.parent_id = "+parent_id+" "
					+"		   and sa.account = '"+account+"'  ORDER by sp.sequenceindex desc, sp.createtime asc ";
		return this.executeSqlForList(sql, hibernateTemplate);
	}
	
	/**
	 * 根据父ID与账号获取个人工作空间
	* @author ou.jh
	* @date Jun 7, 2013 2:58:43 PM
	* @Description: TODO 
	* @param @param parent_id
	* @param @param account
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getPermissionWorkPlatByAccount(String account){
		String sql= "	select distinct sp.* "
					+"		  from sys_permission           sp, "
					+"		       sys_permission_type      spt, "
					+"		       sys_role_rela_permission srrp, "
					+"		       sys_role                 sr, "
					+"		       sys_account              sa, "
					+"		       sys_user_rela_role       surr "
					+"		 where sp.permission_id = srrp.permission_id "
					+"		   and sr.role_id = srrp.role_id "
					+"		   and sr.role_id = surr.role_id "
					+"		   and sa.org_user_id = surr.org_user_id "
					+"		   and sp.is_show_workplat = 1 "
					+"		   and sa.account = '"+account+"'  ORDER by  sp.create_time asc ";
		return this.executeSqlForList(sql, hibernateTemplate);
	}

	/**
	 * 
	 * @description: 根据项目编码 类型获取权限list
	 * @author：yuan.yw
	 * @param proCode
	 * @param type
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jan 14, 2014 10:42:58 AM
	 */
	public List<Map<String, Object>> getPermissionListByProCodeAndType(String proCode,String type){
		if(proCode==null || proCode.isEmpty() || type==null || type.isEmpty()){
			return null;
		}
		String sql = "select level l,spp.PRO_CODE, "
	               +" spp.NAME, "
	               +" spp.TITLE, "
	               +" spp.ENALBED, "
	               +" spp.IS_SHOW_WORKPLAT, "
	               +" spp.URL, "
	               +" spp.PARAMETER, "
	               +" spp.NOTE DESCRIPTION, "
	               +" spp.PERMISSION_ID PID, "
	               +" spp.PARENT_ID PARENTID, "
	               +" spp.TYPE, "
	               +" spp.CODE, "
	               +" spp.PATH "
	               +" from sys_permission  spp "
	               +" connect by prior spp.permission_id=spp.parent_id "
	               +" start with (spp.parent_id is NULL or spp.parent_id='' or spp.parent_id=0) "
	               +" and spp.type = '"+type+"' "
	               +" and spp.pro_code='"+proCode+"'"; 
		return super.executeSqlForList(sql, hibernateTemplate);
	}
	/**
	 * 
	 * @description: 根据权限id删除本权限及子权限
	 * @author：yuan.yw
	 * @param permissionId
	 * @return     
	 * @return boolean     
	 * @date：Jan 14, 2014 4:15:55 PM
	 */
	public boolean detletSelfAndChildPermissionByPermissionId(long permissionId){
		String sql = "delete from sys_permission spp where spp.permission_id in ( "
					+"select  s.permission_id from sys_permission s  "
					+"connect by prior s.permission_id=s.parent_id "
					+"start with s.permission_id= "+permissionId
					+")";
		int result = super.executeSql(sql, hibernateTemplate);
		if(result>-1){
			return true;
		}
		return false;
	}


	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	/**
	 * 
	 * @description: 根据角色获取权限
	 * @author：zhang.wy1
	 * @param roleids
	 * @return
	 * @return: String        
	 * @date：2014-1-9 下午4:32:21
	 */
	public String getPermissionIdsByRoleIds(String roleids){
		// TODO Auto-generated method stub
		if(roleids==null||roleids.equals("")){
			return "";
		}
		String sql = "select permission_id from SYS_role_rela_PERMISSION  where role_id in("+roleids+") and status='A'";
		String permissionIds ="";
		//拼接权限id为以“，”分割的字符串
		for (Map map:this.executeSqlForList(sql, hibernateTemplate)) {
			if(permissionIds.equals("")){
				permissionIds+=map.get("PERMISSION_ID");
			}else{
				permissionIds+=","+map.get("PERMISSION_ID");
			}
		}
		return permissionIds;
	}
	/**
	 * 
	 * @description: 根据用户获取权限
	 * @author：zhang.wy1
	 * @param orgUserId
	 * @return
	 * @return: List<Map<String,Object>>          
	 * @date：2014-1-9 下午4:48:21
	 */
	public List<Map<String, Object>> getPermissionListByUserNew(long orgUserId) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 
	 * @description: 删除用户权限
	 * @author：zhang.wy1
	 * @param orgUserId
	 * @return
	 * @return: int          
	 * @date：2014-1-15 下午12:53:21
	 */
	public int deleteUserPermission(final long orgUserId) {
		// TODO Auto-generated method stub
		hibernateTemplate.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(
					Session session) throws HibernateException,
					SQLException {
				// 删除与业务模块的关联关系
				String sql = "update  sys_user_rela_permission set status='X' , mod_time=sysdate  where org_user_id=?  and permission_id in(select permission_id from SYS_PERMISSION where type='PM_MenuResource')";
				Query query = session.createSQLQuery(sql);
				query.setLong(0, orgUserId);
				int cnt = query.executeUpdate();
				return cnt;				
			}
		});
		return 0;
	}
	/**
	 * 
	 * @description: 添加用户权限
	 * @author：zhang.wy1
	 * @param orgUserId
	 * @param pid
	 * @return
	 * @return: int          
	 * @date：2014-1-15 下午12:54:21
	 */
	public int addUserPermission(final long orgUserId,final String pid) {
		// TODO Auto-generated method stub
		hibernateTemplate.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(
					Session session) throws HibernateException,
					SQLException {
				// 删除与业务模块的关联关系
				String sql = "insert into  sys_user_rela_permission  values("+orgUserId+","+pid+",sysdate,null,'A') ";
				Query query = session.createSQLQuery(sql);
				int cnt = query.executeUpdate();
				return cnt;				
			}
		});
		return 0;
	}
	/**
	 * 根据多个角色获取其权限树
	 */
	public List<Map<String, Object>> getPermissionTreeByRoles(final String roleIds) {
		return hibernateTemplate.executeFind(new HibernateCallback() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "SELECT distinct q.PID,q.NAME,q.TITLE,q.URL,q.PARAMETER,q.DESCRIPTION,q.PARENTID,q.IS_SHOW_WORKPLAT,spt.NAME TYPENAME,spt.CODE TYPECODE,sp.NAME PTNAME ,SP.PER_TYPE_ID PTPID ,q.ENALBED ,q.IS_SHOW_WORKPLAT,q.CODE,srrp.AUTH_TYPE,srrp.PER_ACCESS from (select level l,name,TITLE,ENALBED,URL,PARAMETER,DESCRIPTION, PERMISSION_ID PID,PARENT_ID PARENTID,PER_TYPE_ID, CODE,IS_SHOW_WORKPLAT from SYS_PERMISSION sqt connect by prior PERMISSION_ID=PARENT_ID start with PARENT_ID is NULL order by l) q join SYS_PERMISSION_TYPE spt on q.PER_TYPE_ID = SPT.PER_TYPE_ID LEFT JOIN (SELECT * from SYS_PERMISSION_TYPE ) sp on spt.PARENT_ID = sp.PER_TYPE_ID left join (select * from SYS_ROLE_RELA_PERMISSION where ROLE_ID in("+roleIds+") ) srrp on q.PID = srrp.PERMISSION_ID ";
				// System.out.println(sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String, Object>> list = query.list();
				if (list == null || list.isEmpty()) {
					return null;
				} else {
					return list;
				}

			}
		});
	}

	
	/**
	 * 根据用户获取其菜单权限树 -new
	 */
	public List<Map<String, Object>> getPermissionTreeByUserId(final long orgUserId) {
		return hibernateTemplate.executeFind(new HibernateCallback() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
//				String sql = "SELECT distinct q.PID ,q.NAME, q.URL,q.PARAMETER,q.PARENTID, q.ENALBED,q.IS_SHOW_WORKPLAT,q.CODE,q.type,(case when srrp.org_user_id is null then null else 1 end) flag"+
//                                   " from (select level  l, name,ENALBED,URL, PARAMETER,PERMISSION_ID PID, PARENT_ID PARENTID, type,CODE, IS_SHOW_WORKPLAT from SYS_PERMISSION sqt where type='MENU'"+
//                                   " connect by prior PERMISSION_ID = PARENT_ID start with PARENT_ID =0 order by l) q"+
//                                   " left join (select * from SYS_USER_RELA_DATA where org_user_id =? ) srrp on q.PID = srrp.PERMISSION_ID";
				// System.out.println(sql);
				String sql = "SELECT q.NAME,q.TITLE,q.URL,q.PARAMETER,q.DESCRIPTION,q.PID,q.PARENTID,q.IS_SHOW_WORKPLAT,spt.NAME TYPENAME,spt.CODE TYPECODE,sp.NAME PTNAME ,SP.PER_TYPE_ID PTPID ,q.ENALBED ,q.IS_SHOW_WORKPLAT,q.CODE,       q.CODE AUTH_TYPE,(case when srrp.org_user_id is null then null else 1 end) PER_ACCESS from (select level l,name,TITLE,ENALBED,URL,PARAMETER,DESCRIPTION, PERMISSION_ID PID,PARENT_ID PARENTID,PER_TYPE_ID, CODE,IS_SHOW_WORKPLAT from SYS_PERMISSION sqt connect by prior PERMISSION_ID=PARENT_ID start with PARENT_ID is NULL order by l) q join SYS_PERMISSION_TYPE spt on q.PER_TYPE_ID = SPT.PER_TYPE_ID LEFT JOIN (SELECT * from SYS_PERMISSION_TYPE ) sp on spt.PARENT_ID = sp.PER_TYPE_ID left join (select * from SYS_USER_RELA_PERMISSION where org_user_id =? ) srrp on q.PID = srrp.PERMISSION_ID  ";

				SQLQuery query = session.createSQLQuery(sql);
				query.setLong(0, orgUserId);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String, Object>> list = query.list();
				if (list == null || list.isEmpty()) {
					return null;
				} else {
					return list;
				}
			}
		});
	}  
}
