package com.iscreate.plat.networkresource.common.tool;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QuickSortExtends<T> extends QuickSort<T> {
    /**
     * 对象对比 
     */
	public int compare(T obja, T objb, String param) {
		int result = 0;
		String val1 = "";
		String val2 = "";
		if (obja != null && objb != null&& !param.equals("")) {
			
			if (obja instanceof Map && objb instanceof Map) {
				if (((HashMap) obja).get(param) != null
						&& ((HashMap) objb).get(param) != null) {
					val1 = ((HashMap) obja).get(param).toString();
					val2 = ((HashMap) objb).get(param).toString();
					if (param.equals("id")||param.equals("orderID")) {
						if (Integer.valueOf(val1) > Integer.valueOf(val2)) {
							result = 1;
						}
					} else {
						if(val1.matches("^[a-zA-Z]+[/-~]*[-]*[0-9][0-9]*$")&&val2.matches("^[a-zA-Z]+[/-~]*[-]*[0-9][0-9]*$")){//对字母+数字类型 做特殊处理
							String[] val1Array=val1.split("[0-9]+");
							String[] val2Array=val2.split("[0-9]+");
							Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);//拼音排序
							String []a= {val1Array[0],val2Array[0]};
							Arrays.sort(a, cmp);
							if(!a[0].equals(val1Array[0])){
								result=1;	
							}else if(val2Array[0].equals(val1Array[0])){
								for(String s:val1Array){
									val1 = val1.replace(s,"");
								}
								for(String s:val2Array){
									val2 = val2.replace(s,"");
								}
								if (Integer.valueOf(val1) > Integer.valueOf(val2)) {
									result = 1;
								}
							}
						}else if(val1.matches("[a-zA-Z0-9]+[/-~]*[-]*[a-zA-Z0-9]+$")&&val2.matches("[a-zA-Z0-9]+[/-~]*[-]*[a-zA-Z0-9]+$")){
							String[] val1Array=val1.split("");
							String[] val2Array=val2.split("");
							int arrayLength = 0;
							if(val1Array.length>val2Array.length){
								arrayLength =val2Array.length;
							}else{
								arrayLength =val1Array.length;
							}
							val1="";
							val2="";
							boolean flag = false;
							boolean strFlag1 = false;
							boolean strFlag2 = false;
							String val1Str="";
							String val2Str="";
							for(int i=0;i<arrayLength;i++){
								if(i>0&&i<arrayLength-1){
									if(val1Array[i].matches("[0-9]")&&val1Array[i+1].matches("[0-9]") ){
										val1 = val1Array[i]+val1Array[i+1];	
									}else if(val1Array[i].matches("[0-9]")){
										val1 = val1Array[i];
									}else if(val1Array[i].matches("[A-Za-z]")){
										val1Str =val1Str+val1Array[i];
									}else if(val1Array[i].matches("[/-~]*[-]*")){
										strFlag1=true;
									}
									if(val2Array[i].matches("[0-9]")&&val2Array[i+1].matches("[0-9]")){
										val2 = val2Array[i]+val2Array[i+1];
									}else if(val2Array[i].matches("[0-9]")){
										val2 = val2Array[i];
									}else if(val2Array[i].matches("[A-Za-z]")){
										val2Str = val2Str+val2Array[i];
									}else if(val2Array[i].matches("[/-~]*[-]*")){
										strFlag2=true;
									}
									if(val1!="" && val2!=""){
										if(Integer.valueOf(val1)>Integer.valueOf(val2)){
											result=1;
											break;
										}else if(Integer.valueOf(val1)<Integer.valueOf(val2)){
											break;
										}else{
											if(strFlag1&&strFlag2){
												if(val1Str.compareTo(val2Str)>0){
													result=1;
													break;
												}else if(val1Str.compareTo(val2Str)<0){
													break;
												}else{
													flag=true;
													break;
												}
											}
											
										}
									}
									
								}
							}
							if(flag){
								val1="";
								val2="";
								for(int i=val1Array.length-1;i>0;i--){
									if(val1Array[i].matches("[0-9]")){
										val1 = val1Array[i]+val1;
									}
									if(val1Array[i].matches("[a-zA-Z]")){
										val1 = val1Array[i]+val1;
										break;
									}
								}
								for(int i=val2Array.length-1;i>0;i--){
									if(val2Array[i].matches("[0-9]")){
										val2 = val2Array[i]+val2;
									}
									if(val2Array[i].matches("[a-zA-Z]")){
										val2 = val2Array[i]+val2;
										break;
									}
								}
								if(val1.compareTo(val2)>0){
									result=1;
								}
							}
						}else if(val1.matches("[0-9]+$")&&val2.matches("[0-9]+$")){
							if (Integer.valueOf(val1) > Integer.valueOf(val2)) {
								result = 1;
							}
						}else{
							Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);//拼音排序
							String []a= {val1,val2};
							Arrays.sort(a, cmp);
							if(!a[0].equals(val1)){
								result=1;	
							}
						}
						
					}
					
				} else {
					if (param == "name") {
						if (((HashMap) obja).get("label") != null
								&& ((HashMap) objb).get("label") != null) {
							val1 = ((HashMap) obja).get("label").toString();
							val2 = ((HashMap) objb).get("label").toString();
							if(val1.matches("[a-zA-Z]+[/-~]*[-]*[0-9][0-9]*$")&&val2.matches("[a-zA-Z]+[/-~]*[-]*[0-9][0-9]*$")){//对字母+数字类型 做特殊处理
								String[] val1Array=val1.split("[0-9]+");
								String[] val2Array=val2.split("[0-9]+");
								Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);//拼音排序
								String []a= {val1Array[0],val2Array[0]};
								Arrays.sort(a, cmp);
								if(!a[0].equals(val1Array[0])){
									result=1;	
								}else if(val2Array[0].equals(val1Array[0])){
									for(String s:val1Array){
										val1 = val1.replace(s,"");
									}
									for(String s:val2Array){
										val2 = val2.replace(s,"");
									}
									if (Integer.valueOf(val1) > Integer.valueOf(val2)) {
										result = 1;
									}
								}
							}else if(val1.matches("[a-zA-Z0-9]+[/-~]*[-]*[a-zA-Z0-9]+$")&&val2.matches("[a-zA-Z0-9]+[/-~]*[-]*[a-zA-Z0-9]+$")){
								String[] val1Array=val1.split("");
								String[] val2Array=val2.split("");
								int arrayLength = 0;
								if(val1Array.length>val2Array.length){
									arrayLength =val2Array.length;
								}else{
									arrayLength =val1Array.length;
								}
								val1="";
								val2="";
								boolean flag=false;
								boolean strFlag1 = false;
								boolean strFlag2 = false;
								String val1Str="";
								String val2Str="";
								for(int i=0;i<arrayLength;i++){
									if(i<arrayLength-1){
										if(val1Array[i].matches("[0-9]")&&val1Array[i+1].matches("[0-9]") ){
											val1 = val1Array[i]+val1Array[i+1];	
										}else if(val1Array[i].matches("[0-9]")){
											val1 = val1Array[i];
										}else if(val1Array[i].matches("[A-Za-z]")){
											val1Str =val1Str+val1Array[i];
										}else if(val1Array[i].matches("[/-~]*[-]*")){
											strFlag1=true;
										}
										if(val2Array[i].matches("[0-9]")&&val2Array[i+1].matches("[0-9]")){
											val2 = val2Array[i]+val2Array[i+1];
										}else if(val2Array[i].matches("[0-9]")){
											val2 = val2Array[i];
										}else if(val2Array[i].matches("[A-Za-z]")){
											val2Str = val2Str+val2Array[i];
										}else if(val2Array[i].matches("[/-~]*[-]*")){
											strFlag2=true;
										}
										if(val1!="" && val2!=""){
											if(Integer.valueOf(val1)>Integer.valueOf(val2)){
												result=1;
												break;
											}else if(Integer.valueOf(val1)<Integer.valueOf(val2)){
												break;
											}else{
												if(strFlag1&strFlag2){
													//System.out.println(val1Str+"----++"+val2Str);
													if(val1Str.compareTo(val2Str)>0){
														result=1;
														break;
													}else if(val1Str.compareTo(val2Str)<0){
														break;
													}else{
														flag=true;
														break;
													}
												}
												
											}
										}
										
									}
								}
								if(flag){
									val1="";
									val2="";
									for(int i=val1Array.length-1;i>0;i--){
										if(val1Array[i].matches("[0-9]")){
											val1 = val1Array[i]+val1;
										}
										if(val1Array[i].matches("[a-zA-Z]")){
											val1 = val1Array[i]+val1;
											break;
										}
									}
									for(int i=val2Array.length-1;i>0;i--){
										if(val2Array[i].matches("[0-9]")){
											val2 = val2Array[i]+val2;
										}
										if(val2Array[i].matches("[a-zA-Z]")){
											val2 = val2Array[i]+val2;
											break;
										}
									}
									if(val1.compareTo(val2)>0){
										result=1;
									}
								}
								
							}else if(val1.matches("[0-9]+$")&&val2.matches("[0-9]+$")){
								if (Integer.valueOf(val1) > Integer.valueOf(val2)) {
									result = 1;
								}
							}else{
								Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);//拼音排序
								String []a= {val1,val2};
								Arrays.sort(a, cmp);
								if(!a[0].equals(val1)){
									result=1;	
								}
							}
													
						}
					}
				}
			}
		}
		return result;
	}
	/**
	 * 
	 * @description: 若map1中的key存在与map2的value值中，对map1中的那些key-value排序，其余不考虑。map2的key值应为数字字符串
	 * @author：
	 * @param map1
	 * @param map2
	 * @return     
	 * @return LinkedHashMap     
	 * @date：2012-4-10 上午09:30:40
	 */
	public LinkedHashMap<String,Object> sortMap(Map<String,Object> map1,Map<String,Object> map2 ){
		try{
			LinkedHashMap<String,Object> resultMap1 = new LinkedHashMap<String,Object>();
			LinkedHashMap<String,Object> resultMap2 = new LinkedHashMap<String,Object>();//linkedHashMap 先后插入排序
			Iterator i = map2.keySet().iterator();
			Set<String> sets= map2.keySet();
		    List<String> list=new ArrayList<String>(sets);
		    Object[]a = list.toArray();
		    Object temp=null;
			for(int p=0;p<a.length;p++){ //map2的key值排序
				for(int q=p;q<a.length;q++){
					if(Integer.valueOf((String)a[p])>Integer.valueOf((String)a[q])){
						temp=a[p];
						a[p]=a[q];
						a[q]= temp;
					}		
			     }
			}
		    list.clear();
		    for(int j=0;j<a.length;j++){
		    	list.add((String)a[j]);
		    }
		    for(String key:list){
		    	resultMap1.put(key,map2.get(key));//map2的key值排序后map2值插入resultMap1;
		    }
		    i=resultMap1.keySet().iterator();
		    while(i.hasNext()){
		    	String key=(String)i.next();
		    	if(map1.containsKey(resultMap1.get(key))){
		    		resultMap2.put(resultMap1.get(key).toString(),map1.get(resultMap1.get(key)));//对复合条件的map1的值插入resultMap2
		    	}
		    }
		    i = map1.keySet().iterator();
		    while(i.hasNext()){
		    	String key =(String)i.next();
		    	if(!resultMap2.containsKey(key)){
		    		resultMap2.put(key, map1.get(key));//map1剩下的值插入resultMap;
		    	}
		    }
		    return resultMap2;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	    
		
	}
/*	public static void main(String[] args) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new  LinkedHashMap<String, Object>();
		
		map.put("id", 965);
		map.put("label","云步");
		list.add(map);
		map = new  LinkedHashMap<String, Object>();
		map.put("label","佛桃");
		map.put("id", 456);
		
		list.add(map);
		map = new  LinkedHashMap<String, Object>();
		
		map.put("id", 354);
		map.put("label","dfsfew");
		list.add(map);
		map = new  LinkedHashMap<String, Object>();
	
		map.put("id", 34);
		map.put("label","dfsfffde3ew");
		list.add(map);
		
		new QuickSortExtends().sort(list, "name");
		
		System.out.println(list.toArray()[0] + "**********");
		System.out.println(list.toArray()[1] + "**********");
		System.out.println(list.toArray()[2] + "**********");
		System.out.println(list.toArray()[3] + "**********");
		
		 map=new HashMap<String, Object>();
		  map.put("4", "four");
		  map.put("456", "nine");
		  map.put("400", "ten");
		 HashMap map1=new HashMap<String, Object>();
		 map1.put( "name","nidfh");
		 map1.put( "dfd","fdlf");
		 map1.put( "f","de3");
		 map1.put( "kab","3dgdf");
		 map1.put( "four","4");
		 map1.put( "nine","456");
		 map1.put( "ten","400");
		 LinkedHashMap map2=new QuickSortExtends().sortMap(map1, map);
		 if(map2!=null){
			 Iterator i = map2.keySet().iterator();
			 while(i.hasNext()){
				 String key =(String) i.next();
				 System.out.println(key+","+map2.get(key)+",");
			 }
		 }else{
			 System.out.println("ok");
		 }	
	}*/
}
