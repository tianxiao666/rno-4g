package com.iscreate.op.dao.system;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;


import com.iscreate.op.pojo.system.SysOrgRelaArea;

public class SysAreaRelaOrgDaoImpl implements SysAreaRelaOrgDao{
	private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	/**
	 * @author:du.hw
	 * @create_time:2013-05-30
	 * 通过区域标识获取关联的组织
	 * 
	 */
		public List<SysOrgRelaArea> getOrgListByAreaId(Long area_id){
			String hql = "from sys_org_rela_area r where r.area_id=" + area_id;
	          return (List<SysOrgRelaArea>) hibernateTemplate.find(hql);
		
		}
		/**
		 * 
		 * @description: 根据组织id删除相关关联区域关系
		 * @author：yuan.yw
		 * @param orgId 组织id
		 * @return 操作成功标志flag    
		 * @return boolean     
		 * @date：Jun 20, 2013 9:19:23 AM
		 */
		public boolean deleteSysOrgRelaAreaByOrgId(long orgId){
			if(orgId==0){
				return false;
			}
			String sql = "delete from SysOrgRelaArea where orgId="+orgId;
			return this.executeSqlForOperateObject(sql);
		}
		/**
		 * 
		 * @description: sql操作
		 * @author：yuan.yw
		 * @param sqlString
		 * @return 操作标志flag
		 * @return boolean     
		 * @date：Jun 20, 2013 9:27:15 AM
		 */
		public boolean executeSqlForOperateObject(final String sqlString) {
			boolean flag = hibernateTemplate
					.execute(new HibernateCallback<Boolean>() {
						public Boolean doInHibernate(
								Session session) throws HibernateException,
								SQLException {
							Query query=session.createQuery(sqlString);
							int i = query.executeUpdate();
							if(i>=0){
								return true;
							}else{
								return false;
							}
							
						}
					});
			return flag;
		}
		
}
