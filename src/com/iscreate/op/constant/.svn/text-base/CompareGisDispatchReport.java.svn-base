package com.iscreate.op.constant;

import java.util.Map;
import java.util.Comparator;

public class CompareGisDispatchReport implements Comparator<Map<String,Object>>{

	public CompareGisDispatchReport(){

	}
	
	
	public int compare(Map<String, Object> o1, Map<String, Object> o2) {

		int distance1=0;
		if(o1.get("unfinishedTasks") != null){
			distance1 = Integer.parseInt(o1.get("unfinishedTasks")+"");
		}
		int distance2=0;
		if(o2.get("unfinishedTasks") != null){
			distance2 = Integer.parseInt(o2.get("unfinishedTasks")+"");
		}
		return distance1>distance2?-1:(distance1==distance2?0:1);
	}

}
