package com.iscreate.op.dao.rno;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.rno.RnoHandover;
import com.iscreate.op.pojo.rno.RnoUserDefinedFormul;

public class RnoUserDefinedDaoImpl implements RnoUserDefinedDao {

	private static Log log = LogFactory.getLog(RnoUserDefinedDaoImpl.class);
	// ---注入----//
	private HibernateTemplate hibernateTemplate;

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	/**
	 * 保存一条用户自定义公式记录
	 * @param rnoUserDefinedFormul
	 * @author chao.xj
	 * @date 2014-1-6下午04:28:19
	 */
	public void saveOneUserDefinedFormul(RnoUserDefinedFormul rnoUserDefinedFormul) {
		hibernateTemplate.save(rnoUserDefinedFormul);
	}
	/**
	 * 插入一条用户自定义公式记录
	 * @param rnoUserDefinedFormul
	 * @return
	 * @author chao.xj
	 * @date 2014-1-6下午05:06:21
	 */
	public int insertOneUserDefinedFormul(final RnoUserDefinedFormul rnoUserDefinedFormul) {
		final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return hibernateTemplate.execute(new HibernateCallback<Integer>(){

			public Integer doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				// TODO Auto-generated method stub
				int a=0;
				String sql="INSERT INTO RNO_USER_DEFINED_FORMUL VALUES(SEQ_RNO_USER_DEFINED_FORMUL.nextval,'"+rnoUserDefinedFormul.getName()+"','"+rnoUserDefinedFormul.getCondition()+"','"+rnoUserDefinedFormul.getStyle()+"','"+rnoUserDefinedFormul.getApplyScope()+"','"+rnoUserDefinedFormul.getScopeValue()+"','"+rnoUserDefinedFormul.getModuleType()+"','"+rnoUserDefinedFormul.getCreater()+"',TO_DATE('"+sdf.format(rnoUserDefinedFormul.getCreateTime())+"','yyyy-MM-dd hh24:mi:ss'),TO_DATE('"+sdf.format(rnoUserDefinedFormul.getModTime())+"','yyyy-MM-dd hh24:mi:ss'),'Y')";
				SQLQuery query=arg0.createSQLQuery(sql);
				/*query.setString(0, rnoUserDefinedFormul.getName());
				query.setString(1, rnoUserDefinedFormul.getCondition());
				query.setString(2, rnoUserDefinedFormul.getStyle());
				query.setString(3, rnoUserDefinedFormul.getApplyScope());
				query.setString(4, rnoUserDefinedFormul.getScopeValue());
				query.setString(5, rnoUserDefinedFormul.getModuleType());
				query.setString(6, rnoUserDefinedFormul.getCreater());
*/				
				a=query.executeUpdate();
				
				return a;
			}
			
		
		});
	}
}
