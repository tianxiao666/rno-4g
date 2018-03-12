package com.iscreate.op.service.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.dao.system.SysAreaDao;
import com.iscreate.op.pojo.system.SysAccount;

public class SysAreaServiceImpl implements SysAreaService{
	private static Log log = LogFactory.getLog(SysAreaServiceImpl.class);

	private SysAreaDao sysAreaDao;

	public SysAreaDao getSysAreaDao() {
		return sysAreaDao;
	}

	public void setSysAreaDao(SysAreaDao sysAreaDao) {
		this.sysAreaDao = sysAreaDao;
	}
	
	/**
	 * @author:duhw
	 * @create_time:2013-05-11
	 * 通过区域标识得到所有子区域的信息,对数据格式重组，得到树形结构
	 * area_id：区域标识(此标识等于0时，此方法可以得到所有区域信息)
	 * deepth:树的深度(此标识等于0时，获取符合条件的所有树节点)
	 * @return:区域列表属性结构
	 */
	public List<Map<String, Object>> getAreaTreeList(int area_id,int deepth){
		log.info("进入getAreaTreeList(int area_id,int deepth)方法");
		List<Map<String,Object>> areaList = sysAreaDao.getAreaList(area_id,deepth);//初始区域列表
		ArrayList<ArrayList<Map<String,Object>>> list = new ArrayList<ArrayList<Map<String,Object>>>();//存放节点列表
		//ArrayList<Map<String,Object>>  mapList = new ArrayList<Map<String,Object>>();//存放不同级的单个节点
		for(int i=0;i<8;i++){//1定义最大数的级数，可以多，但是不可少(此处定义了8级)
			list.add(i, new ArrayList<Map<String,Object>>());
			//mapList.add(i,new TreeMap<String,Object>());
		}
		
		//判断当前组织的级别
		int areaGrade = 0;//区分当前组织的级别
		if(areaList!=null && areaList.size() > 0){//存在子节点数据
			String path[] = ((String) areaList.get(0).get("PATH")).split("/");
			areaGrade = path.length-2;
			if(areaGrade < 0){//数据不合法
				return null;
			}
		}else{
			return null;
		}
		int currentLevel = 0;
		for(int i=0;i<areaList.size();i++){//重新组合组织树形结构
		   Map<String,Object> sysArea = reBuild(areaList.get(i));//对一条区域信息map的key值重新组合
		   areaList.set(i, sysArea);//重新赋值给组织列表
		   String path[] = ((String) areaList.get(i).get("path")).split("/");
		   int pathLength = path.length;
		   currentLevel = pathLength-2;//当前节点所在树的级别，0是最顶端
		   for(int j=list.size()-1;j>currentLevel;j--){
			    if(list.get(j) != null && list.get(j).size() > 0){
			    	List<Map<String,Object>> lastList = list.get(j-1);//上级节点
			    	List<Map<String,Object>> currentList = list.get(j);//当前级节点
			    	list.get(j-1).get(lastList.size()-1).put("children", currentList);
			    	list.set(j, new ArrayList<Map<String,Object>>());//当前节点已经加到上级节点中,删除当前子节点
			     }   
		   }
		   //mapList.add(currentLevel,areaList.get(i));
		   list.get(currentLevel).add(areaList.get(i));
		}
		for(int j=list.size()-1;j>areaGrade;j--){
		    if(list.get(j) != null && list.get(j).size() > 0){
		    	List<Map<String,Object>> lastList = list.get(j-1);//上级节点
		    	List<Map<String,Object>> currentList = list.get(j);//当前级节点
		    	list.get(j-1).get(lastList.size()-1).put("children", currentList);
		     }   
	   }
		log.info("退出getAreaTreeList(int area_id,int deepth)，返回结果list"+list);
		return list.get(areaGrade);
	}
	/*public List<Map<String, Object>> getAreaTreeList(int area_id,int deepth){
		List<Map<String,Object>> areaList = sysAreaDao.getAreaList(area_id,deepth);//初始区域列表
		List<Map<String,Object>> provTreeList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> cityTreeList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> distinctTreeList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> streetTreeList = new ArrayList<Map<String,Object>>();
		Map<String,Object> prov = new TreeMap<String,Object>();
		Map<String,Object> city = new TreeMap<String,Object>();
		Map<String,Object> distinct = new TreeMap<String,Object>();
		Map<String,Object> street = new TreeMap<String,Object>();
		
		//判断当前区域的类型(省||市||区||街道)
		int areaGrade = 0;//区分当前区域的级别(1:省，2：市，3：区/县，4：街道)
		if(areaList!=null && areaList.size() > 0){//存在子节点数据
			String path[] = ((String) areaList.get(0).get("PATH")).split("/");
			areaGrade = path.length-1;
			if(areaGrade < 1){//数据不合法
				return null;
			}
		}else{
			return null;
		}
		System.out.println("areaGrade"+areaGrade);
		for(int i=0;i<areaList.size();i++){//重新组合区域树形结构
		   Map<String,Object> sysArea = reBuild(areaList.get(i));//对一条区域信息map的key值重新组合
		   areaList.set(i, sysArea);//重新赋值给区域列表
		   String path[] = ((String) areaList.get(i).get("path")).split("/");
		   int pathLength = path.length;
		   System.out.println("pathLength"+pathLength);
		   if(pathLength == 2){//省份
				   if(streetTreeList!=null && streetTreeList.size() > 0){
					   distinctTreeList.get(distinctTreeList.size()-1).put("children", streetTreeList);
					   streetTreeList = new ArrayList<Map<String,Object>>();
				   }
				   if(distinctTreeList!=null && distinctTreeList.size() > 0){
					   cityTreeList.get(cityTreeList.size()-1).put("children", distinctTreeList);
					   distinctTreeList = new ArrayList<Map<String,Object>>();
					   streetTreeList = new ArrayList<Map<String,Object>>();
				   }
				   if(cityTreeList!=null && cityTreeList.size() > 0){
					   provTreeList.get(provTreeList.size()-1).put("children", cityTreeList);
					   cityTreeList = new ArrayList<Map<String,Object>>();
					   distinctTreeList = new ArrayList<Map<String,Object>>();
					   streetTreeList = new ArrayList<Map<String,Object>>();
				   }
				  
				   prov = areaList.get(i);
				   provTreeList.add(prov);
		   }
		   if(pathLength == 3){//城市
			   if(streetTreeList!=null && streetTreeList.size() > 0){
				   distinctTreeList.get(distinctTreeList.size()-1).put("children", streetTreeList);
				   streetTreeList = new ArrayList<Map<String,Object>>();
			   }
			   if(distinctTreeList!=null && distinctTreeList.size() > 0){
				   cityTreeList.get(cityTreeList.size()-1).put("children", distinctTreeList);
				   distinctTreeList = new ArrayList<Map<String,Object>>();
				   streetTreeList = new ArrayList<Map<String,Object>>();
			   }
				city = areaList.get(i);
			    cityTreeList.add(city);
		   }
		   if(pathLength == 4){//区
			   if(streetTreeList!=null && streetTreeList.size() > 0){
				   distinctTreeList.get(distinctTreeList.size()-1).put("children", streetTreeList);
				   streetTreeList = new ArrayList<Map<String,Object>>();
			   }
			   distinct = areaList.get(i);
			   distinctTreeList.add(distinct);
			   
		   }
		   if(pathLength == 5){//街道
			   street = areaList.get(i);
			   streetTreeList.add(street);
		   }
		}
		
		//最后的子项加入到父节点中
		if(distinctTreeList != null && distinctTreeList.size()>0){
			distinctTreeList.get(distinctTreeList.size()-1).put("children", streetTreeList);
		}
		if(cityTreeList != null && cityTreeList.size()>0){
			cityTreeList.get(cityTreeList.size()-1).put("children", distinctTreeList);
		}
		if(provTreeList != null && provTreeList.size()>0){
			provTreeList.get(provTreeList.size()-1).put("children", cityTreeList);
		}
		
		if(areaGrade == 1){//省
			return provTreeList;
		}else if(areaGrade == 2){//市
			return cityTreeList;
		}else if(areaGrade == 3){//区/县
			return distinctTreeList;
		}else if(areaGrade == 4){//街道
			return streetTreeList;
		}else{
			return null;
		}
	}*/
	/**
	 * @author:duhw
	 * @create_time:2013-05-13
	 * 通过用户标识得到用户关联的区域标识
	 * （用户关联组织，组织关联区域）
	 * @param org_user_id:用户标识(不是账号标识，注意区分)
	 * @return 区域列表
	 */
	public List<Map<String,Object>> getRelaAreaListByOrgUserId(long org_user_id){
		List<Map<String,Object>> areaList = sysAreaDao.getAreaListByOrgUserId(org_user_id);//初始区域列表
	    return areaList;	
	}
	  /**
	   * @author:duhw
	   * @create_time:2013-05-11
	   * 转换map的key与SysDao中的定义名称一样
	   * map：需要转换的map
	   * @return map
	   */
		public Map<String,Object> reBuild(Map map){
			Map<String,Object> returnMap = new TreeMap<String,Object>();
			returnMap.put("areaId", map.get("AREA_ID"));
			returnMap.put("name", map.get("NAME"));
			returnMap.put("areaLevel", map.get("AREA_LEVEL"));
			returnMap.put("entityType", map.get("ENTITY_TYPE"));
			returnMap.put("entityId", map.get("ENTITY_ID"));
			returnMap.put("longitude", map.get("LONGITUDE"));
			returnMap.put("latitude", map.get("LATITUDE"));
			returnMap.put("parentId", map.get("PARENT_ID"));
			returnMap.put("path", map.get("PATH"));
			returnMap.put("createtime", map.get("CREATETIME"));
			returnMap.put("updatetime", map.get("UPDATETIME"));
			return returnMap;
		}
		
		/**
		 * 根据人员账号获取人员对应区域
		* @author ou.jh
		* @date May 28, 2013 10:16:55 AM
		* @Description: TODO 
		* @param @param account        
		* @throws
		 */
		public List<Map<String,Object>> getAreaByAccount(String account){
			log.info("进入getAreaByAccount方法");
			List<Map<String,Object>> areaByAccount = this.sysAreaDao.getAreaByAccount(account);
			log.info("执行getAreaByAccount方法成功，实现了”根据人员账号获取人员对应区域“的功能");
			log.info("退出getAreaByAccount方法,返回List<Map<String,Object>>"+areaByAccount);
			return areaByAccount;
		}
}
