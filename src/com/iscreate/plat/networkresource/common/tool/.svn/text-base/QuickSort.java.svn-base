package com.iscreate.plat.networkresource.common.tool;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class QuickSort<T> {
	
/**
 * 	
 * @description: 升序排序
 * @author：
 * @param list
 * @param param     
 * @return void     
 * @date：2012-4-10 上午11:04:00
 */
public void sort(List<T> list,String param){
	if(list!=null){
		Object[] array=list.toArray();
		sortArray((T[])array,param,0,array.length-1);
		list.clear();
		for(int i=0;i<array.length;i++){
			list.add((T)array[i]);
		}
	}
	
	
}
/**
 * 
 * @description: 比较 交换
 * @author：
 * @param array
 * @param param     
 * @return void     
 * @date：2012-4-10 上午11:04:34
 */
public void sortArray(T[] array,String param,int left,int right){
	int l= left;
	int r=right;
    if (l >= r) 
      return; 
    //确定指针方向的逻辑变量 
    boolean move=true; 
    while (l != r) { 
      if (compare(array[l],array[r],param)>0) { 
        //交换数字 
        T temp = array[l]; 
        array[l] = array[r]; 
        array[r] = temp; 
        //决定下标移动，还是上标移动 
        move = (move == true) ? false : true; 
      } 
      //将指针向前或者向后移动 
      if(move) 
        r--; 
      else 
        l++; 
    } 
    //将数组分开两半，确定每个数字的正确位置 
    l--; 
    r++; 
    sortArray(array,param,left,l);
    sortArray(array,param,r,right); 
}




/**
 * 
 * @description: 对比
 * @author：
 * @param obja
 * @param objb
 * @param param
 * @return     
 * @return int     
 * @date：2012-4-10 上午11:04:57
 */
public abstract int compare(T obja,T objb,String param);
/**
 * 
 * @description:若map1中的key存在与map2的value值中，对map1中的那些key-value排序，其余不考虑。map2的key值应为数字字符串
 * @author：
 * @param map1
 * @param map2
 * @return     
 * @return LinkedHashMap<String,Object>     
 * @date：2012-4-10 上午11:48:30
 */
public abstract  LinkedHashMap<String,Object> sortMap(Map<String,Object> map1,Map<String,Object> map2 );
}
