package com.iscreate.op.dao.rno;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.DbValInject;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.iscreate.plat.system.datasourcectl.DataSourceConst;
import com.iscreate.plat.system.datasourcectl.DataSourceContextHolder;

public class AuthDsDataDaoImpl {

	public static class SysArea {
		// AREA_ID NOT NULL NUMBER(12)
		@DbValInject(dbField = "AREA_ID", type = "Long")
		long areaId;
		// NAME NOT NULL VARCHAR2(256)
		@DbValInject(dbField = "NAME", type = "String")
		String name;
		// AREA_LEVEL NOT NULL VARCHAR2(64)
		@DbValInject(dbField = "AREA_LEVEL", type = "String")
		String areaLevel;
		// ENTITY_TYPE VARCHAR2(256)
		// ENTITY_ID NUMBER(12)
		// LONGITUDE NUMBER(30,20)
		@DbValInject(dbField = "LONGITUDE", type = "Double")
		double longitude;
		// LATITUDE NUMBER(30,20)
		@DbValInject(dbField = "LATITUDE", type = "Double")
		double latitude;
		// PARENT_ID NUMBER(12)
		@DbValInject(dbField = "PARENT_ID", type = "Long")
		long parent_id;

		// PATH NOT NULL VARCHAR2(256)
		// CREATETIME DATE
		// UPDATETIME DATE
		public long getAreaId() {
			return areaId;
		}

		public void setAreaId(long areaId) {
			this.areaId = areaId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAreaLevel() {
			return areaLevel;
		}

		public void setAreaLevel(String areaLevel) {
			this.areaLevel = areaLevel;
		}

		public double getLongitude() {
			return longitude;
		}

		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}

		public double getLatitude() {
			return latitude;
		}

		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}

		public long getParent_id() {
			return parent_id;
		}

		public void setParent_id(long parent_id) {
			this.parent_id = parent_id;
		}

		@Override
		public String toString() {
			return "SysArea [areaId=" + areaId + ", name=" + name
					+ ", areaLevel=" + areaLevel + ", longitude=" + longitude
					+ ", latitude=" + latitude + ", parent_id=" + parent_id
					+ "]";
		}

	}

	public static Map<Long, List<Long>> cityToSubAreaS = new HashMap<Long, List<Long>>();

	public static Map<Long, SysArea> idToAreas = new HashMap<Long, SysArea>();
	volatile static boolean hasInit = false;

	private static void initData() {
		synchronized (AuthDsDataDaoImpl.class) {
			if (!hasInit) {
				hasInit = true;
				String sql = "select  * from sys_area ORDER BY PARENT_ID";
				String oriDs = DataSourceContextHolder.getDataSourceType();
				DataSourceContextHolder
						.setDataSourceType(DataSourceConst.authDs);
				Connection connection = DataSourceConn.initInstance()
						.getConnection();
				DataSourceContextHolder.setDataSourceType(oriDs);
				Statement areaStmt = null;
				try {
					areaStmt = connection.createStatement();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				List<Map<String, Object>> areaMaps = RnoHelper.commonQuery(
						areaStmt, sql);
				try {
					areaStmt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				try {
					connection.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				if (areaMaps != null) {
					SysArea sa = null;
					long id = -1, parentId = -1;
					List<Long> subIds = null;
					DateUtil dateUtil=new DateUtil();
					for (Map<String, Object> areaMap : areaMaps) {
						if (areaMap.get("AREA_ID") != null) {
							try {
								id = Long.parseLong(areaMap.get("AREA_ID")
										.toString());
								parentId = Long.parseLong(areaMap.get(
										"PARENT_ID").toString());
								subIds = cityToSubAreaS.get(parentId);
								if (subIds == null) {
									subIds = new ArrayList<Long>();
									cityToSubAreaS.put(parentId, subIds);
								}
								subIds.add(id);
							} catch (Exception e) {

							}

							//
							sa = RnoHelper.commonInjection(SysArea.class,
									areaMap,dateUtil);
							if (sa != null) {
								idToAreas.put(sa.getAreaId(), sa);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 根据id获取区域信息
	 * 
	 * @param areaId
	 * @return
	 * @author brightming 2014-9-29 上午11:18:50
	 */
	public static SysArea getSysAreaByAreaId(long areaId) {
		if (!hasInit) {
			initData();
		}
		return idToAreas.get(areaId);
	}

	/**
	 * 获取city下的直接的子区域id
	 * 
	 * @param cityId
	 * @return
	 * @author brightming 2014-9-28 下午5:26:10
	 */
	public static List<Long> getSubAreaIdsByCityId(long cityId) {
		if (hasInit) {
			if (cityToSubAreaS.containsKey(cityId)) {
				return cityToSubAreaS.get(cityId);
			} else {
				return Collections.EMPTY_LIST;
			}
		}

		initData();

		if (cityToSubAreaS.containsKey(cityId)) {
			return cityToSubAreaS.get(cityId);
		} else {
			return Collections.EMPTY_LIST;
		}
	}

	/**
	 * 获取包括自身以及子区域id 在内的逗号分隔的字符串
	 * @param parId
	 * @return
	 * @author brightming
	 * 2014-9-29 上午11:31:06
	 */
	public static String getSubAreaAndSelfIdListStrByParentId(long parId){
		if (!hasInit){
			initData();
		}
		List<Long> subIds=cityToSubAreaS.get(parId);
		if(subIds!=null && subIds.size()>0){
			String tmp=parId+",";
			for(long id:subIds){
				tmp+=id+",";
			}
			if(tmp.length()>0){
				tmp=tmp.substring(0, tmp.length()-1);
			}
			return tmp;
		}
		return null;
		
	}
	
	/**
	 * 获取某个区域的详情
	 * 
	 * @param areaId
	 * @return
	 * @author brightming 2014-9-28 下午6:01:38
	 */
	public static Map<String, Object> getAreaData(long areaId) {
		String oriDs = DataSourceContextHolder.getDataSourceType();
		DataSourceContextHolder.setDataSourceType(DataSourceConst.authDs);
		Connection connection = DataSourceConn.initInstance().getConnection();
		String areaSql = "SELECT * FROM SYS_AREA WHERE AREA_ID=" + areaId;
		Statement areaStmt = null;
		try {
			areaStmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		List<Map<String, Object>> areaMaps = RnoHelper.commonQuery(areaStmt,
				areaSql);

		Map<String, Object> result = null;
		for (Map<String, Object> areaMap : areaMaps) {
			if (areaMap.get("AREA_ID") != null) {
				try {
					result = areaMap;
					break;
				} catch (Exception e) {

				}
			}
		}
		try {
			areaStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DataSourceContextHolder.setDataSourceType(oriDs);
		return result;
	}
}
