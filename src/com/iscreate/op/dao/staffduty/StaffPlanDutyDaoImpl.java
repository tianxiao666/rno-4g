package com.iscreate.op.dao.staffduty;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.action.informationmanage.common.DBUtil;
import com.iscreate.op.constant.CardispatchConstant;
import com.mockrunner.util.common.StringUtil;


@SuppressWarnings("unchecked")
public class StaffPlanDutyDaoImpl implements StaffPlanDutyDao {

	/************ 依赖注入 ***********/
	private HibernateTemplate hibernateTemplate;

	/***************** 属性 *******************/

	
	
	/****************** dao *******************/
	
	/**
	 * 根据业务单元、日期、账号,获取条件符合的排班信息
	 * (请求参数) 	dutyDate 日期List集合
	 *  			account 人员账号
	 *  			freId 班次List集合
	 *  			bizId 人员所在组织List集合
	 * @return (List<Map<String,Object>>) 排班信息集合
	 */
	public List<Map<String,Object>> getStaffDutyList ( String staffId,List<String> dutyDate , List<String> bizId , List<String> freId , String name ) {
		String dutyDateWhere = DBUtil.list2InString(dutyDate, "to_char(dutyDate,'yyyy-mm-dd')");
		String bizIdWhere = DBUtil.list2InString(bizId, "org_id");
		String freIdWhere = DBUtil.list2InString(freId, "freId");
		String accountWhere = "";
		if ( name != null && !name.isEmpty() ) {
			accountWhere = " AND  instr(name,'"+name+"')>0 ";
		}
		if(staffId != null && !"".equals(staffId)){
			accountWhere += " AND account='" + staffId+"'";
		}
		/*String selectString = " duty.dutyId  , staff.account , staffName , staffId  , fre.freId , org.bizId , org.bizName , fre.* ,  DATE_FORMAT(DUTYDATE,'%Y-%m-%d') as dutyDate , fre.frequencyName , freq_eng  ";			//查询结果列
		String sqlString = 		"	SELECT " + 
										selectString + 
								"	FROM  " + 
								"		(SELECT * , id AS dutyId FROM staffduty_dutytemplate WHERE 1=1 " + dutyDateWhere + " ) duty " +  
								"		INNER JOIN ( SELECT * , id AS freId , beginTime as startTime , CASE WHEN FREQUENCYNAME = '白班' THEN 'morning' ELSE 'night'   END AS freq_eng FROM staffduty_frequency WHERE 1=1 " + freIdWhere + ") fre ON fre.freId = duty.frequencyId " + 
								"		INNER JOIN ( SELECT * , id AS staffId , NAME AS staffName FROM staff WHERE 1=1 " + accountWhere + ") staff ON staff.account = duty.userId " +
								"		INNER JOIN org_account_relation org_rel ON org_rel.account = staff.account " + 
								"		INNER JOIN ( SELECT * , id AS bizId , NAME AS bizName  FROM org_providerorganization WHERE 1=1 " + bizIdWhere + " ) org ON org.bizId = org_rel.orgId " ;
		*/
		
	    String staffString = " id \"staffId\","
					        +"account \"account\","
					        +"jobNumber \"jobNumber\","
					        +"name \"staffName\","
					        +"sex \"sex\","
					        +"identityCard \"identityCard\","
					        +"contactPhone \"contactPhone\","
					        +"cellPhoneNumber \"cellPhoneNumber\","
					        +"qqNumber \"qqNumber\","
					        +"age \"age\","
					        +"birthday \"birthday\","
					        +"graduateDate \"graduateDate\","
					        +"picture \"picture\","
					        +"degree \"degree\","
					        +"enterpriseId \"enterpriseId\" ";
	    String dutytemString = "id \"dutyId\",frequencyId \"frequencyId\",dutyDate \"dutyDate\",userId \"userId\" ";
	 /*   String orgproString = " org_id \"bizId\","
						        +"type \"type\","
						        +"name \"bizName\","
						        +"parentOrgName \"parentOrgName\","
						        +"encampment \"encampment\","
						        +"address \"address\","
						        +"status \"status\","
						        +"orgDuty \"orgDuty\","
						        +"dutyPerson \"dutyPerson\","
						        +"dutyPersonPhone \"dutyPersonPhone\","
						        +"contactPhone \"contactPhone\","
						        +"longitude \"longitude\","
						        +"latitude \"latitude\","
						        +"orgAttribute \"orgAttribute\","
						        +"enterpriseId \"enterpriseId\","
						        +"foundationTime \"foundationTime\" ";*/
		String selectString = " \"dutyId\" ,\"account\",\"staffName\",  \"staffId\" ,\"freId\" ,\"bizId\",\"bizName\",\"lastEditTime\",\"endTime\",\"freId\",\"startTime\",\"shiftsId\",  to_char(\"dutyDate\",'yyyy-mm-dd') \"dutyDate\" , \"frequencyName\",\"freq_eng\" ";			//查询结果列
		String sqlString = 		"	SELECT " + 
										selectString + 
								"	FROM  " + 
								"		(SELECT "+dutytemString+" FROM staff_dutytemplate WHERE 1=1 " + dutyDateWhere + " ) duty " +  
								"		INNER JOIN ( SELECT  frequencyName \"frequencyName\",id  \"freId\" , beginTime \"startTime\" ,shiftsId \"shiftsId\",endTime \"endTime\",lastEditTime \"lastEditTime\",CASE WHEN FREQUENCYNAME = '白班' THEN 'morning' ELSE 'night'   END \"freq_eng\" FROM staff_frequency WHERE 1=1 " + freIdWhere + ") fre ON fre.\"freId\" = duty.\"frequencyId\" " + 
								"		INNER JOIN ( SELECT "+staffString+" FROM staff WHERE 1=1 " + accountWhere + ") staff ON staff.\"account\" = duty.\"userId\" " +
								"       inner join sys_account acc on acc.account=staff.\"account\""+
								"		INNER JOIN sys_user_rela_org org_rel on org_rel.org_user_id=acc.org_user_id " + 
								"		INNER JOIN ( SELECT org_id as \"bizId\",name as \"bizName\"  FROM sys_org WHERE 1=1 " + bizIdWhere + " ) org ON org.\"bizId\" = org_rel.org_id " ;
		List<Map<String, Object>> list = executeSqlForObject(sqlString);
		return list;
	}
	
	
	/**
	 * 读取人员班次信息集合
	 * @return 人员班次信息集合
	 */
	public List<Map<String, String>> loadStaffDutyFreq () {
		//TODO
		String freString = " id \"id\","
	        +"shiftsId \"shiftsId\","
	        +"frequencyName \"frequencyName\","
	        +"beginTime \"beginTime\","
	        +"endTime \"endTime\","
	        +"lastEditTime \"lastEditTime\" ";
		String sql = " SELECT "+freString+" FROM staff_frequency ";
		List<Map<String, String>> list= executeSql(sql);
		return list;
	}
	
	
	/**
	 * 根据人员姓名,查询人员的信息集合
	 * @param staffName - 人员姓名 
	 * @return (List<Map<String,String>>) 人员信息集合
	 */
	public List<Map<String,String>> findStaffListByStaffName ( String staffName , List<String> bizIds ) {
		//TODO
		String bizIdString = DBUtil.list2InString(bizIds, "org.\"bizId\"");
		
		/*String sql = 	"	SELECT " + 
						"		* " + 
						"	FROM  " + 
						"		( SELECT * , id AS staffId , NAME AS staffName FROM staff ) staff " + 
						"		LEFT JOIN org_account_relation org_rel ON org_rel.account = staff.account " + 
						"		LEFT JOIN ( SELECT * , id AS bizId , NAME AS bizName FROM org_providerorganization) org ON org.bizId = org_rel.orgId " +
						"		LEFT JOIN cardispatch_driver driver ON driver.accountId = staff.account AND driver.id IS NULL " + 
						"	WHERE " +
						"		staff.staffName LIKE '%" + staffName + "%' " +bizIdString ;*/
		String staffString =" id \"staffId\","
			        +"account \"account\","
			        +"jobNumber \"jobNumber\","
			        +"name \"staffName\","
			        +"sex \"sex\","
			        +"identityCard \"identityCard\","
			        +"contactPhone \"contactPhone\","
			        +"cellPhoneNumber \"cellPhoneNumber\","
			        +"qqNumber \"qqNumber\","
			        +"age \"age\","
			        +"to_char(birthday,'yyyy-mm-dd hh24:mi:ss') \"birthday\","
			        +"to_char(graduateDate,'yyyy-mm-dd hh24:mi:ss') \"graduateDate\","
			        +"picture \"picture\","
			        +"degree \"degree\","
			        +"enterpriseId \"enterpriseId\" ";
		/*String org_relString = " id \"id\","
				        +"orgId \"orgId\","
				        +"account \"account\" ";*/
		String org_proString = " org_id \"bizId\","
				        //+"type \"type\","
				        +"name \"bizName\","
				        //+"parentOrgName \"parentOrgName\","
				        //+"encampment \"encampment\","
				        +"address \"address\","
				        +"status \"status\","
				        //+"orgDuty \"orgDuty\","
				       // +"dutyPerson \"dutyPerson\","
				        //+"dutyPersonPhone \"dutyPersonPhone\","
				        +"mobile \"contactPhone\","
				        +"longitude \"longitude\","
				        +"latitude \"latitude\","
				        //+"orgAttribute \"orgAttribute\","
				        +"enterprise_id \"enterpriseId\"";
				       // +"to_char(foundationTime,'yyyy-mm-dd hh24:mi:ss') \"foundationTime\" ";
		/*	String driverString = " id \"id\","
				        +"accountId \"accountId\","
				        +"driverName \"driverName\","
				        +"identificationId \"identificationId\","
				        +"driverPhone \"driverPhone\","
				        +"driverLicenseClass \"driverLicenseClass\","
				        +"driverLicenseNumber \"driverLicenseNumber\","
				        +"driverStatus \"driverStatus\","
				        +"driverLicenseType \"driverLicenseType\","
				        +"drivingOfAge \"drivingOfAge\","
				        +"driverPic \"driverPic\","
				        +"driverAddress \"driverAddress\","
				        +"wage \"wage\","
				        +"driverAge \"driverAge\","
				        +"driverBizId \"driverBizId\" ";*/
		String sql="";
		if("".equals(staffName)){
			sql = 	"	SELECT staff.*,org.* "+
			"	FROM  " + 
			"		( SELECT "+staffString+" FROM staff ) staff " +
			"       left join sys_account acc on acc.account=staff.\"account\"" + 
			"		LEFT JOIN sys_user_rela_org org_rel ON org_rel.org_user_id = acc.org_user_id " + 
			"		LEFT JOIN ( SELECT "+org_proString+" FROM sys_org) org ON org.\"bizId\" = org_rel.org_id " +
			"		LEFT JOIN car_driver driver ON driver.accountId = staff.\"account\" AND driver.id IS NULL " + 
			"	WHERE " +
			"	1=1 " +bizIdString ;
		}else{
			sql = 	"	SELECT staff.*,org.* "+
			"	FROM  " + 
			"		( SELECT "+staffString+" FROM staff ) staff " +
			"       left join sys_account acc on acc.account = staff.\"account\"" + 
			"		LEFT JOIN sys_user_rela_org org_rel ON org_rel.org_user_id = acc.org_user_id " + 
			"		LEFT JOIN ( SELECT "+org_proString+" FROM sys_org) org ON org.\"bizId\" = org_rel.org_id " +
			"		LEFT JOIN car_driver driver ON driver.accountId = staff.\"account\" AND driver.id IS NULL " + 
			"	WHERE " +
			"	instr(staff.\"staffName\",'"+staffName+"')>0 " +bizIdString ;
		}
		
		List<Map<String, String>> list = executeSql(sql);
		return list;
	}
	
	
	/**
	 * 根据条件删除排班信息
	 * @param dutyDate - 值班日期
	 * @param freId - 班次
	 * @param account - 人员账号 
	 * @return (Integer) 是否成功
	 */
	public Integer deleteStaffDutyTemplate ( String dutyDate , String freId , String account ) {
		String whereString = " WHERE 1=1  ";
		if ( !StringUtil.isEmptyOrNull(dutyDate) ) {
			whereString += " AND dutyDate = to_date('" + dutyDate + "','yyyy-mm-dd')";
		}
		if ( !StringUtil.isEmptyOrNull(freId) ) {
			whereString += " AND frequencyId = '" + freId + "'";
		}
		if ( !StringUtil.isEmptyOrNull(account) ) {
			whereString += " AND userId = '" + account + "'";
		}
		final String sql = " DELETE FROM staff_dutytemplate " + whereString;
		Integer num = hibernateTemplate.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql);
				int num = query.executeUpdate();
				return num;
			}
		});
		return num;
	}
	
	
	/**
	 * 根据条件,查询排班信息集合
	 * @param dutyDate - 排班日期
	 * @param freId - 班次id
	 * @param account - 人员账号
	 * @return (List<Map<String,String>>) 排班信息集合
	 */
	public List<Map<String,String>> findStaffDutyTemplate ( String dutyDate , String freId , String account ) {
		String whereString = " WHERE 1=1  ";
		if ( !StringUtil.isEmptyOrNull(dutyDate) ) {
			whereString += " AND dutyDate = to_date('" + dutyDate + "','yyyy-mm-dd')";
		}
		if ( !StringUtil.isEmptyOrNull(freId) ) {
			whereString += " AND frequencyId = '" + freId + "'";
		}
		if ( !StringUtil.isEmptyOrNull(dutyDate) ) {
			whereString += " AND userId = '" + account + "'";
		}
		//TODO
		String sql = " SELECT * FROM staff_dutytemplate " + whereString ;
		List<Map<String, String>> list = executeSql(sql);
		return list;
	}
	
	
	/**
	 * 保存人员排班
	 * @param dutyDate - 排班日期
	 * @param freId - 班次id
	 * @param account - 人员账号
	 * @return (Integer) 是否成功
	 */
	public Integer saveStaffDutyTemplate ( String dutyDate , String freId , String account ) {
		//TODO 加序列
		String id = "SEQ_STAFF_DUTYTEMPLATE.nextval";
		final String sql = " INSERT INTO staff_dutytemplate(id,dutyDate,frequencyId,userId) VALUES("+id+",to_date('" + dutyDate + "','yyyy-mm-dd'),'" + freId + "','" + account + "')";
		Integer num = hibernateTemplate.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql);
				int num = query.executeUpdate();
				return num;
			}
		});
		return num;
	}
	
	
	
	/************* 公共sql **************/
	
	public List<Map<String,String>> executeSql ( final String sqlString ) {
		List<Map<String, String>> list = hibernateTemplate.executeFind( new HibernateCallback<List<Map<String,String>>>() {
			public List<Map<String, String>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sqlString);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String, String>> find = query.list();
				return find;
			}
		});
		return list;
	}	
	
	public List<Map<String,Object>> executeSqlForObject ( final String sqlString ) {
		List<Map<String, Object>> list = hibernateTemplate.executeFind( new HibernateCallback<List<Map<String,Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sqlString);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String, Object>> find = query.list();
				return find;
			}
		});
		return list;
	}
	
	/***************** getter setter *******************/
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}




	
	
}
