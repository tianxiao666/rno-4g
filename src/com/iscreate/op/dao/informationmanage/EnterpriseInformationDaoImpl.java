package com.iscreate.op.dao.informationmanage;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.iscreate.op.action.informationmanage.common.DBUtil;
import com.iscreate.op.constant.InformationConstant;
import com.iscreate.op.pojo.informationmanage.InformationEnterprise;

/**
 * 企业信息管理数据类
 * @author andy
 */
@SuppressWarnings("unused")
public class EnterpriseInformationDaoImpl extends BaseDaoImpl<InformationEnterprise> implements EnterpriseInformationDao {

	/************ 依赖注入 **************/
	
	/************ 属性 **************/
	private static final String enterprise_table = " ( SELECT ep.* , id as enterPriseId FROM " + DBUtil.getTableName(InformationEnterprise.class) + " ep ) enterprise";
	
	
	public boolean checkEnterpriseRegisterNumberExists ( String registerNumber , String id ) {
		boolean exists = false;
		String sql = "SELECT " + InformationConstant.ENTERPRISE_COLUMNTEXT + " FROM " + DBUtil.getTableName(InformationEnterprise.class) + " enterprise WHERE id != " + id + " AND registerNumber = '" + registerNumber + "' ";
		List<Map<String,String>> list = executeFindList(sql);
		exists = list != null && list.size() > 0;
		return exists;
	}
	
	
	public List<InformationEnterprise> find ( Map paraMap ) {
		List<InformationEnterprise> find = hibernateTemplate.execute( new HibernateCallback<List<InformationEnterprise>>() {
			public List<InformationEnterprise> doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = "FROM InformationEnterprise";
				Query query = session.createQuery(sql);
				List<InformationEnterprise> list = query.list();
				return list;
			}
			
		});
		return find;
	}
	
	
	/**
	 * 根据企业id,获取企业信息
	 * @param enterPriseId - 企业id
	 * @return
	 */
	public Map<String,String> findEnterPriseByEnterPriseId ( String enterPriseId ) {
		String sql = " SELECT " + InformationConstant.ENTERPRISE_COLUMNTEXT + " FROM " + enterprise_table + " WHERE enterPriseId = " + enterPriseId;
		List<Map<String,String>> list = executeFindList(sql);
		Map<String,String> result_map = null;
		if ( list != null && list.size() > 0 ) {
			result_map = list.get(0);
		}
		return result_map;
	}
	
	/*************** getter setter ****************/
	
	
	
	
}
