package com.iscreate.op.dao.informationmanage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.iscreate.op.pojo.informationmanage.Area;

@SuppressWarnings("unchecked")
public class InformationManageAreaDaoImpl extends BaseDaoImpl<Area> implements InformationManageAreaDao {

	/**
	 * 获取所有省份信息
	 * @return 省份信息集合
	 */
	public List<Map<String, String>> getAllProvince () {
		String sql = 	"	SELECT " +
						"		a.* " +
						"	FROM " +
						"		networkresourcemanage.area as a" +
						"		INNER JOIN networkresourcemanage.figurenode as fn ON a._entityType = fn.entityType and a._entityId = fn.entityId " +
						"		where  fn.id not in (SELECT ff.rightId from networkresourcemanage.figureline as ff where ff.linkType = 'CLAN')";
		List<Map<String, String>> list = super.executeFindList(sql);
		return list;
	}
	
	
	
	
	
}
