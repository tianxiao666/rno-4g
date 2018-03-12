package com.iscreate.op.dao.cardispatch;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.action.informationmanage.common.ArrayUtil;
import com.iscreate.op.action.informationmanage.common.DBUtil;
import com.iscreate.op.action.informationmanage.common.ObjectUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.constant.CardispatchConstant;
import com.iscreate.op.dao.informationmanage.BaseDaoImpl;
import com.iscreate.op.pojo.cardispatch.CardispatchCar;
import com.iscreate.op.pojo.cardispatch.CardispatchCarterminalpair;
import com.iscreate.op.pojo.cardispatch.CardispatchDriver;
import com.iscreate.op.pojo.cardispatch.CardispatchDrivercarpair;
import com.iscreate.op.pojo.cardispatch.CardispathTerminal;
import com.iscreate.op.pojo.organization.Staff;

@SuppressWarnings({"rawtypes","unchecked"})
public class CardispatchDriverDaoImpl  extends BaseDaoImpl<CardispatchDriver> implements CardispatchDriverDao {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	
	public List<Map<String,Object>> findDriverList ( Map param_map ) {
		List<Map<String, Object>> list = findDriverList(param_map, null);
		return list;
	}
	
	/**
	 * 根据id,获取司机对象
	 * @param driverId - 司机id
	 * @return (Map<String,String>) 司机信息
	 */
	public Map<String,String> findDriverById ( String driverId ) {
		Map<String,String> driver_map = new LinkedHashMap<String, String>();
		String sql = " SELECT " + CardispatchConstant.DRIVER_COLUMNTEXT + " FROM (SELECT d.* , id as driverId FROM " + DBUtil.getTableName(CardispatchDriver.class) + " d ) driver where driverId = " + driverId ;
		List<Map<String, String>> list = super.executeFindList(sql);
		if ( list != null && list.size() > 0 ) {
			driver_map = list.get(0);
		}
		return driver_map;
	}
	
	
	public boolean checkDriverAccountId ( String accountId ) {
		String whereString = " WHERE 1=1 ";
		if ( !StringUtil.isNullOrEmpty(accountId) ) {
			whereString += " AND accountId ='" + accountId + "' ";
		}
		String sql = " SELECT  " + CardispatchConstant.DRIVER_COLUMNTEXT + "  FROM " + DBUtil.getTableName(CardispatchDriver.class) + " driver " + whereString ;
		List<Map<String, String>> list = super.executeFindList(sql);
		boolean flag = false;
		if ( !ArrayUtil.isNullOrEmpty(list) ) {
			flag = true;
		}
		return flag;
	}
	
	
	/**
	 * 查询司机信息集合
	 * @param param_map - 参数集合 ( key:数据库字段名 , value：参数值)
	 * @return 司机信息集合
	 */
	public List<Map<String,Object>> findDriverList ( Map param_map , Boolean isFree) {
		String whereString = super.getWhereString(param_map, new DBWhereCallBack() {
			public String callBack(String columnName, StringBuffer opera,
					StringBuffer value) {
				if ( columnName.equals("carNumber") || columnName.equals("driverName") || columnName.equals("accountId") ) {
					String v = value.toString();
					v = StringUtil.handleDbSensitiveString(v);
					opera.delete( 0 , opera.length() );
					opera.append("LIKE");
					return "%" + v + "%";
				}
				return null;
			}
		});
		if ( isFree != null ) {
			if ( isFree ) {
				whereString += " AND carId IS NULL ";
			} else {
				whereString += " AND carId IS NOT NULL ";
			}
		}
		String selectString = CardispatchConstant.CAR_COLUMNTEXT + " , " + CardispatchConstant.DRIVER_COLUMNTEXT + " , " + CardispatchConstant.TERMINAL_COLUMNTEXT + " , carBizName \"carBizName\" , driverBizName \"driverBizName\" , driverPair.cardriverpairId \"cardriverpairId\" ";
		String sql = 	"	SELECT " + 
								selectString + 
						"	FROM  " + 
						"		( SELECT   " + 
						"					d.* , d.id AS driverId , o.name AS driverBizName " +  
						"				FROM   " + 
						"					" + DBUtil.getTableName(CardispatchDriver.class) + " d  " + 
						"					LEFT JOIN sys_org o ON o.org_id = d.driverBizId ) driver " + 
						"		LEFT JOIN (SELECT p.* , id as cardriverpairId FROM " + DBUtil.getTableName(CardispatchDrivercarpair.class) + " p ) driverPair ON driverPair.driver_id = driver.driverId   " + 
						"		LEFT JOIN ( SELECT c.* , c.id as carId , o.name AS carBizName FROM " + DBUtil.getTableName(CardispatchCar.class) + " c LEFT JOIN sys_org o ON o.org_id = c.carBizId ) car    ON car.carId = driverPair.car_id	" +  
						"		LEFT JOIN " + DBUtil.getTableName(CardispatchCarterminalpair.class) + " terminalpair ON terminalpair.car_id = car.id  " + 
						"		LEFT JOIN " + DBUtil.getTableName(CardispathTerminal.class) + " terminal ON terminalpair.terminal_id = terminal.id "
							+ whereString ;
		List<Map<String, Object>> list = super.executeFindListMapObject(sql);
		return list;
	}
	
	
	
	
	/**
	 * 获取非司机的人员信息
	 * @param accountOrName - 账号或姓名
	 * @param bizIds - 所在组织id集合
	 * @return (List<Map<String,String>>) 人员信息集合
	 */
	public List<Map<String,String>> getNotDriverStaffListByNameOrAccount ( String accountOrName , List<Long> bizIds ) {
		String bizIdsString = DBUtil.list2InString(bizIds, "driverBizId");
		String whereString = " WHERE 1=1 ";
		if ( !StringUtil.isNullOrEmpty(accountOrName) ) {
			whereString += " AND staff.name LIKE '%" + accountOrName  + "%' OR  staff.account LIKE '%" + accountOrName + "%' " ;
		}
		String sql = 	"	SELECT " + 
						" 		staff.account \"account\" , staff.name \"name\" , " + CardispatchConstant.DRIVER_COLUMNTEXT + 
						" 	FROM  " + 
						" 		(SELECT s.* , id AS staffId FROM staff s ) staff  " + 
						" 	LEFT JOIN ( SELECT d.* , id AS driverId FROM  " + DBUtil.getTableName(CardispatchDriver.class) + " d WHERE 1=1 " + bizIdsString + " ) driver ON driver.accountId = staff.account AND driver.id IS NULL "
						 + whereString;
		List<Map<String, String>> list = super.executeFindList(sql);
		return list;
	}
	
	/**
	 * 根据司机id,删除司机信息
	 * @param id - 司机id
	 * @return 操作是否成功(true ： 删除成功)
	 */
	public boolean deleteDriverById ( String id ) {
		String sql = "DELETE FROM " + DBUtil.getTableName(CardispatchDriver.class) + " WHERE id = " + id ;
		Boolean flag = super.executeSql(sql);
		return flag;
	}
	
	public boolean deleteDriverByIds ( List<String> ids ){
		String list2InString = DBUtil.list2InString(ids, "id");
		String sql = "DELETE FROM  " + DBUtil.getTableName(CardispatchDriver.class) + "  WHERE 1=1 " + list2InString ;
		Boolean flag = super.executeSql(sql);
		return flag;
	}
	
	
	public boolean saveDriver (Map driver_map ) {
		CardispatchDriver driver = null;
		try {
			driver = ObjectUtil.map2Object(driver_map, CardispatchDriver.class);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		//保存司机信息
		Long driverId = super.txinsert(driver);
		driver_map.put("driverId", driverId);
		boolean flag = driverId > 0 ;
		return flag;
	}
	
	
	public boolean updateDriverById ( Map driver_map , String driverId ) {
		Map<String,String> map = new LinkedHashMap<String, String>();
		map.put("id", driverId);
		int num = super.txupdate( map , driver_map );
		boolean flag = num > 0 ;
		return flag;
	}
	
	
	
	
}


