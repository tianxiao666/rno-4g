package com.iscreate.plat.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TreeListHelper {
	/**
	 * @author duhw
	 * @create_time 2013-05-31
	 * 把列表转换为树形机构的列表
	 * 注：1.参数dataList必须是通过树形结构查询方法查询数据库所得的列表
	 *   2.查询数据库时不能加排序，否则此方法不适合转换为树形结构
	 *   3.查询的结果中必须有path参数，切path必须小写
	 * @param dataList
	 * @return 树形结构数据
	 */
	public static List<Map<String, Object>> getTreeListByDataList(List<Map<String,Object>> dataList){
		
		ArrayList<ArrayList<Map<String,Object>>> list = new ArrayList<ArrayList<Map<String,Object>>>();//存放节点列表
		for(int i=0;i<8;i++){//1定义最大数的级数，可以多，但是不可少(此处定义了8级)
			list.add(i, new ArrayList<Map<String,Object>>());
		}
		
		//判断当前数据的级别
		int dataGrade = 0;//区分当前数据的级别
		if(dataList!=null && dataList.size() > 0){//存在子节点数据
			String path[] = ((String) dataList.get(0).get("path")).split("/");
			dataGrade = path.length-2;
			if(dataGrade < 0){//数据不合法
				return null;
			}
		}else{
			return null;
		}
		int currentLevel = 0;
		for(int i=0;i<dataList.size();i++){//重新组合组织树形结构
		   String path[] = ((String) dataList.get(i).get("path")).split("/");
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
		   list.get(currentLevel).add(dataList.get(i));
		}
		for(int j=list.size()-1;j>dataGrade;j--){
		    if(list.get(j) != null && list.get(j).size() > 0){
		    	List<Map<String,Object>> lastList = list.get(j-1);//上级节点
		    	List<Map<String,Object>> currentList = list.get(j);//当前级节点
		    	list.get(j-1).get(lastList.size()-1).put("children", currentList);
		     }   
	   }
		return list.get(dataGrade);
	}
}
