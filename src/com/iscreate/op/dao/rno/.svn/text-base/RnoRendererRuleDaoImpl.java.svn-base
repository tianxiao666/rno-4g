package com.iscreate.op.dao.rno;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.rno.RnoTrafficRendererConfig;

public class RnoRendererRuleDaoImpl implements RnoRendererRuleDao {
	private static Log log = LogFactory.getLog(RnoRendererRuleDaoImpl.class);
	// ---注入----//
	private HibernateTemplate hibernateTemplate;

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * 获取指定名称的渲染规则的细节
	 * 
	 * @param name
	 * @return
	 * @author brightming 2013-10-10 下午3:31:05
	 */
	public List<RnoTrafficRendererConfig> getRendererRuleByRuleCode(final String code,final long areaId) {
		return hibernateTemplate.executeFind(new HibernateCallback<List<RnoTrafficRendererConfig>>() {
			public List<RnoTrafficRendererConfig> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				
				String sql="select TRAFFIC_REN_ID,TRAFFIC_TYPE,NAME,MIN_VALUE,MAX_VALUE,STYLE,AREA_ID,CONFIG_ORDER,DISABLED_FIELDS,PARAMS from RNO_TRAFFIC_RENDERER_CONFIG where TRAFFIC_TYPE in (select TRAFFIC_INDEX_ID from RNO_TRAFFIC_INDEX_TYPE where CODE=? and AREA_ID=?) ORDER BY TRAFFIC_REN_ID ASC ";
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setString(0, code);
				query.setLong(1, areaId);
				query.addEntity(RnoTrafficRendererConfig.class);
				List list= query.list();
				if(list==null || list.isEmpty()){
					sql="select TRAFFIC_REN_ID,TRAFFIC_TYPE,NAME,MIN_VALUE,MAX_VALUE,STYLE,AREA_ID,CONFIG_ORDER,DISABLED_FIELDS,PARAMS from RNO_TRAFFIC_RENDERER_CONFIG where TRAFFIC_TYPE in (select TRAFFIC_INDEX_ID from RNO_TRAFFIC_INDEX_TYPE where CODE=?) ORDER BY TRAFFIC_REN_ID ASC ";
					query = arg0.createSQLQuery(sql);
					query.setString(0, code);
					query.addEntity(RnoTrafficRendererConfig.class);
					list=query.list();
				}
				return list;
			}
		});
	}
	
	/**
	 * 修改某个话统渲染的具体规则 
	 * @param ruleId
	 * @param configs
	 * @author brightming
	 * 2013-10-22 下午4:16:48
	 */
	public void modifyStsRendererRuleDetail(final long ruleId,final List<RnoTrafficRendererConfig> configs){
		if(configs==null || configs.isEmpty()){
			return;
		}
		hibernateTemplate.execute(new HibernateCallback<Void>(){
			public Void doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				String sql="select TRAFFIC_REN_ID,TRAFFIC_TYPE,NAME,MIN_VALUE,MAX_VALUE,STYLE from RNO_TRAFFIC_RENDERER_CONFIG where TRAFFIC_TYPE="+ruleId;
				SQLQuery query = arg0.createSQLQuery(sql);
				query.addEntity(RnoTrafficRendererConfig.class);
				List<RnoTrafficRendererConfig> exists=query.list();
				if(exists==null || exists.isEmpty()){
					return null;
				}
				
				boolean find=false;
				
				for(RnoTrafficRendererConfig one:exists){
					find=false;
					for(RnoTrafficRendererConfig change:configs){
						if(one.getTrafficRenId()==change.getTrafficRenId()){
							find=true;
							one.setMaxValue(change.getMaxValue());
							one.setMinValue(change.getMinValue());
							one.setName(change.getName());
							one.setStyle(change.getStyle());
							break;
						}
					}
					if(find){
						//更新
						arg0.update(one);
					}
				}
				
				
				return null;
			}
			
		});
	}

}
