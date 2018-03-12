package com.iscreate.op.dao.networkresourcemanage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;

/**
 * 此dao文件主要是网络资源的各种查询(针对新版，区域表是sys_area,区域和资源的关联表使用figurenode和figureline表)
 * 
 * @author du.hw
 * @create_time 2013-05-27
 */
public class NetworkResourceQueryDaoImpl implements NetworkResourceQueryDao {

	private HibernateTemplate hibernateTemplate;

	/**
	 * @author du.hw
	 * @create_time 2013-05-27 通过用户标识得到用户有权访问的网络资源 方法:通过用户组织关联区域，得到与区域关联的网络资源
	 *              org_user_id:用户标识 resourceType:网络资源类型
	 */
	public List<Map<String, Object>> getResourceListByUserId(long org_user_id,
			String resourceType) {
		String sql = "select distinct b.id figurenodeid,a.id \"objectIdentity\","
				+ " a.name \"objectName\","
				+ " a.entryattention \"entryAttention\","
				+ " a.buildingheight \"buildingHeight\","
				+ " a.id \"id\","
				+ " a.name \"name\","
				+ " a.label \"label\","
				+ " a.type \"type\","
				+ " a.belongtype \"belongType\","
				+ " a.entity_type \"_entityType\","
				+ " a.destinationtime \"destinationtime\","
				+ " a.maintainattention \"maintainAttention\","
				+ " a.address \"address\","
				+ " a.floornum \"floorNum\","
				+ " a.longitude \"longitude\","
				+ " a.latitude  \"latitude\","
				+ " a.entity_id \"_entityId\""
				+ " from station a"
				+ " inner join figurenode b on b.entityid=a.entity_id"
				+ " inner join figureline c on c.rightid=b.id and c.lefttype='Sys_Area' and c.righttype='"
				+ resourceType
				+ "'"
				+ " inner join (select distinct aa.id,bb.* from figurenode  aa"
				+ "			inner join "
				+ "             (select distinct * from sys_area connect by prior area_id=parent_id"
				+ "					start with area_id in (select a.area_id from sys_area a "
				+ "				inner join sys_org_rela_area b on b.area_id=a.area_id"
				+ "				inner join (select distinct org_id from sys_org connect by prior org_id=parent_id "
				+ "             start with org_id in (select org_id from sys_user_rela_org where org_user_id="
				+ org_user_id
				+ ")"
				+ "           ) c on c.org_id=b.org_id)) bb on bb.entity_id=aa.entityid"
				+ "     )" + " d on d.id=c.leftid";
		List<Map<String, Object>> returnList = this.queryList(sql);
		return returnList;
	}

	/**
	 * @author du.hw
	 * @create_time 2013-06-13 通过区域idString得到区域关联的机房 idString:由逗号分割的区域标识字符串
	 *              此查询是通过v_room视图查询
	 * @moder yuan.yw
	 * @moddate 2013-06-24 15:20:00
	 * @modreason 增加缺少的返回资源类型参数_entityType
	 */
	public List<Map<String, Object>> getRoomListByAreaIds(String idString) {
		String areaId = "";
		if (idString != null && !idString.equals("")) {
			areaId = " and ( ";
			String[] split = idString.split(",");
			for (String a : split) {
				if (a.equals("") || a == null) {
					continue;
				} else {
					areaId = areaId + " fn.path like '%/Sys_Area/" + a
							+ "/%'  or";
				}
			}
			areaId = areaId.substring(0, areaId.length() - 2) + " ) ";
		} else {
			// 区域ID为空
			return null;
		}
		String sql = " select "
				+ ResourceCommon.getSelectSqlAttributsString("Room", "re")
				+ " "
				+ " , substr(fn.path , instr(fn.path, '/Sys_Area/', -1, 1)+10,instr(fn.path, '/Station/', -1, 1)-10-instr(fn.path, '/Sys_Area/', -1, 1)) \"areaId\" "
				+ "  from Room re, Figurenode fn "
				+ " where re.entity_id = fn.entityid "
				+ " and re.entity_type = fn.entitytype " + areaId;

		List<Map<String, Object>> returnList = this.queryList(sql);
		return returnList;
	}

	/**
	 * 
	 * @description: 通过资源类型 坐标 区域id 获取资源列表（终端使用）（站址 机房 基站）
	 * @author：yuan.yw
	 * @param AetName
	 *            资源类型
	 * @param GPSMap
	 *            坐标位置map
	 * @param conditionMap
	 *            条件
	 * @param areaId
	 *            区域id
	 * @param longitude
	 *            经度
	 * @param latitude
	 *            纬度
	 * @param start
	 *            开始记录
	 * @param end
	 *            结束记录
	 * @return
	 * @return List<Map<String,Object>>
	 * @date：Jun 25, 2013 1:48:38 PM
	 */
	public List<Map<String, Object>> getResourceWithPagingByAetNameAndGPSMapAndAreaIds(
			String AetName, Map<String, Object> GPSMap,
			Map<String, Object> conditionMap, String areaId, double longitude,
			double latitude, int start, int end) {

		String sql = "";
		String countSql = "";
		String tableAlias = "";//别名
		if ("Room".equals(AetName)) {
			sql = "select v.address \"address\",s.longitude \"longitude\",s.latitude \"latitude\",v.id \"id\",v.name \"name\",v.entity_type \"_entityType\",GetDistance(s.latitude, s.longitude, "
					+ latitude
					+ ", "
					+ longitude
					+ ")*1000 \"GPSsum\" "
					+"  from "+AetName+" v, figurenode fn  "
					+"  left join figurenode f "
					+"  on f.id = fn.parent_figurenode_id "
					+"  left join Station s "
					+"  on s.entity_id = f.entityid and s.entity_type=f.entitytype "
					+"  where v.entity_id = fn.entityid "
					+"   and v.entity_type = fn.entitytype ";
			countSql = "select count(*) \"count\" from "+AetName+" v, figurenode fn  "
						+"  left join figurenode f "
						+"  on f.id = fn.parent_figurenode_id "
						+"  left join Station s "
						+"  on s.entity_id = f.entityid and s.entity_type=f.entitytype "
						+"  where v.entity_id = fn.entityid "
						+"   and v.entity_type = fn.entitytype ";
			tableAlias = "s";
		} else if (AetName.indexOf("BaseStation") >= 0) {
			sql = "select v.address \"address\",v.longitude \"longitude\",v.latitude \"latitude\",v.id \"id\",v.name \"name\",v.entity_type \"_entityType\",GetDistance(v.latitude, v.longitude, "
					+ latitude
					+ ", "
					+ longitude
					+ ")*1000 \"GPSsum\" from "
					+ AetName
					+ "  v,figurenode fn where 1=1 and v.entity_type=fn.entitytype and v.entity_id=fn.entityid ";
			countSql = "select count(*) \"count\" from "
					+ AetName
					+ "  v,figurenode fn where 1=1 and v.entity_type=fn.entitytype and v.entity_id=fn.entityid";
			tableAlias = "v";
		} else {
			sql = "select v.address \"address\",v.longitude \"longitude\",v.latitude \"latitude\",v.id \"id\",v.name \"name\",v.entity_type \"_entityType\",GetDistance(v.latitude, v.longitude, "
					+ latitude
					+ ", "
					+ longitude
					+ ")*1000 \"GPSsum\" from "
					+ AetName
					+ "  v,figurenode fn where 1=1 and v.entity_type=fn.entitytype and v.entity_id=fn.entityid ";
			countSql = "select count(*) \"count\" from "
					+ AetName
					+ "  v,figurenode fn where 1=1 and v.entity_type=fn.entitytype and v.entity_id=fn.entityid";
			tableAlias = "v";
		}
		if (!"".equals(areaId)) {
			sql += " and  fn.path like '%/Sys_Area/" + areaId + "/%'";
			countSql += " and  fn.path like '%/Sys_Area/" + areaId + "/%'";
		}
		if (GPSMap != null && GPSMap.size() > 0) {// 坐标范围
			sql = sql + " and (("+tableAlias+".longitude between " + GPSMap.get("outerleft")
					+ " and " + GPSMap.get("outerright") + " ) "
					+ " and ("+tableAlias+".latitude between " + GPSMap.get("outerbottom")
					+ " and " + GPSMap.get("outertop") + " )) "
					+ " and ((("+tableAlias+".latitude>" + GPSMap.get("innertop")
					+ ") or ("+tableAlias+".latitude<" + GPSMap.get("innerbottom")
					+ ")) or (("+tableAlias+".longitude>" + GPSMap.get("innerright")
					+ ") or ("+tableAlias+".longitude<" + GPSMap.get("innerleft") + "))) ";
			countSql = countSql + " and (("+tableAlias+".longitude between "
					+ GPSMap.get("outerleft") + " and "
					+ GPSMap.get("outerright") + " ) "
					+ " and ("+tableAlias+".latitude between " + GPSMap.get("outerbottom")
					+ " and " + GPSMap.get("outertop") + " )) "
					+ " and ((("+tableAlias+".latitude>" + GPSMap.get("innertop")
					+ ") or ("+tableAlias+".latitude<" + GPSMap.get("innerbottom")
					+ ")) or (("+tableAlias+".longitude>" + GPSMap.get("innerright")
					+ ") or ("+tableAlias+".longitude<" + GPSMap.get("innerleft") + "))) ";
		}
		if (conditionMap != null && conditionMap.size() > 0) {// 条件
			sql += " and (  ";
			countSql += " and (  ";
			for (String key : conditionMap.keySet()) {
				sql = sql + " v." + key + " like '" + conditionMap.get(key)
						+ "' or ";
				countSql = countSql + " v." + key + " like '"
						+ conditionMap.get(key) + "' or ";
			}
			if (sql != null && sql != "") {
				sql = sql.substring(0, sql.length() - 3);
				countSql = countSql.substring(0, countSql.length() - 3);

			}
			sql = sql + ")  ";
			countSql = countSql + ")  ";
		}
		String sqlStr = "";
		String countSqlStr = "";
		if (AetName.indexOf("BaseStation") >= 0){//基站特殊处理
			sqlStr=sql;
			countSqlStr=countSql;
			sqlStr += " union "+sql.replace("BaseStation", "BaseStation_GSM");
			sqlStr += " union "+sql.replace("BaseStation", "BaseStation_repeater");
			sqlStr += " union "+sql.replace("BaseStation", "BaseStation_TD");
			sqlStr += " union "+sql.replace("BaseStation", "BaseStation_WLAN");
			countSqlStr += " union "+countSql.replace("BaseStation", "BaseStation_GSM");
			countSqlStr += " union "+countSql.replace("BaseStation", "BaseStation_repeater");
			countSqlStr += " union "+countSql.replace("BaseStation", "BaseStation_TD");
			countSqlStr += " union "+countSql.replace("BaseStation", "BaseStation_WLAN");
			sql=sqlStr;
			countSql =" select sum(c.\"count\") \"count\" from ("+ countSqlStr+") c";
			
		}
		sql += " order by \"GPSsum\" ";
		sql = "SELECT * FROM " + " ( SELECT A.*, ROWNUM RN " + " FROM (" + sql
				+ ") A " + " WHERE ROWNUM <= " + end + "" + " ) "
				+ " WHERE RN >= " + start + " ";
		List<Map<String, Object>> countList = this.queryList(countSql);// 总数
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		if (countList != null && countList.size() > 0) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("totalCount", countList.get(0).get("count") + "");
			List<Map<String,Object>> entityList = this.queryList(sql);
			for(Map<String,Object> mp:entityList){
				if(mp.get("address")==null){
					mp.put("address"," ");
				}
			}
			resultMap.put("entityList", entityList);
			resultList.add(resultMap);
		} else {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("totalCount", "0");
			resultMap.put("entityList", null);
			resultList.add(resultMap);
		}
		return resultList;

	}

	/**
	 * @author du.hw 查询
	 */
	public List queryList(final String sql) {
		List<Map<String, Object>> list = hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(
							Session session) throws HibernateException,
							SQLException {
						SQLQuery query = session.createSQLQuery(sql);
						query
								.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List find = query.list();
						return find;
					}
				});
		return list;
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

}
