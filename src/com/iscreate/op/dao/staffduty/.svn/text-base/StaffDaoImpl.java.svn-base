package com.iscreate.op.dao.staffduty;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.pojo.organization.Staff;
import com.iscreate.op.pojo.staffduty.StaffdutyDutytemplate;
import com.iscreate.op.pojo.staffduty.Staffskill;
import com.iscreate.plat.tools.TimeFormatHelper;

public class StaffDaoImpl implements StaffDao {
	
	private HibernateTemplate hibernateTemplate;
	
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}



	/**
	 * 条件获取人员列表
	 * @param orgId 组织架构Id
	 * @param staffName 人员姓名
	 * @return
	 */
	public List<Staff> getStaffListByConditions(String orgId, String staffName) {
		String hql ="";
		if(!StringUtil.isNullOrEmpty(staffName)){
			staffName=staffName.trim();
			if(StringUtil.isNullOrEmpty(staffName)){
				hql = "select s from Staff s,SysUserRelaOrg r,SysAccount sa where r.orgId in ("+orgId+")  and s.account=sa.account and r.orgUserId = sa.orgUserId ";
			}else{
				hql = "select s from Staff s,SysUserRelaOrg r,SysAccount sa where r.orgId in ("+orgId+") and instr(s.name,'"+staffName+"')>0 and s.account=sa.account and r.orgUserId = sa.orgUserId ";
			}
		}else{
			hql = "select s from Staff s,SysUserRelaOrg r,SysAccount sa where r.orgId in ("+orgId+") and  s.account=sa.account and r.orgUserId = sa.orgUserId ";
		}
		return hibernateTemplate.find(hql);
	}
	
	/**
	 * 按条件获取人员列表
	 * @param skillId 技能Id
	 * @param experienceAge 工作年限
	 * @param sex 性别
	 * @param startDutyTime 值班开始时间
	 * @param endDutyTime 值班结束时间
	 * @return
	 */
	public List<Staff> getStaffListByConditions(String skillId,String experienceAge,String sex,String startDutyTime,String endDutyTime) {
		String hql = "select s from Staff s ";
		if(!StringUtil.isNullOrEmpty(skillId)){
			hql+=" ,Staffskill ss ";
		}
		if(!StringUtil.isNullOrEmpty(startDutyTime)){
			hql+=",StaffdutyDutytemplate st ";
		}
		hql+="where 1=1 ";
		if(!StringUtil.isNullOrEmpty(sex)){
			hql+=" AND s.sex='"+sex+"'";
		}
		if(!StringUtil.isNullOrEmpty(experienceAge)){
			//人员暂无经验年限啊 
			
		}
		if(!StringUtil.isNullOrEmpty(skillId)){
			hql+=" and ss.staffAccount=s.account and ss.skillId="+skillId;
		}
		if(!StringUtil.isNullOrEmpty(startDutyTime)){
			if(StringUtil.isNullOrEmpty(endDutyTime)){
				endDutyTime = TimeFormatHelper.getTimeFormatByFree(new Date(), "yyyy-MM-dd");
			}
			hql+=" and s.account=st.userId and to_char(st.dutyDate,'yyyy-MM-dd') BETWEEN '"+startDutyTime+"' AND '"+endDutyTime+"' ";
		}
		return hibernateTemplate.find(hql);
	}
	
	/**
	 * 执行查询操作
	 * @param hql
	 * @return
	 */
	public List executeHqlFind(String hql) {
		/*final String sqlString = hql;
		return hibernateTemplate.executeFind(new HibernateCallback<List<Staff>>() {
			public List<Staff> doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(sqlString);
				List<Staff> find = query.list();
				return find;
			}
		});*/
		return hibernateTemplate.find(hql);
	}
}
