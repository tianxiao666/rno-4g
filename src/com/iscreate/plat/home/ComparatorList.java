package com.iscreate.plat.home;

import java.util.Comparator;
import java.util.Map;

public class ComparatorList implements Comparator{
	private String key;
	
	public ComparatorList(String key){
		this.key = key;
	}
	
	public int compare(Object arg0, Object arg1) {
		Map m = (Map)arg0;
		Map m1 = (Map)arg1;
		if(m != null && m1 != null){
			if(m.get(key) != null && m1.get(key) != null){
				long parseLong = Long.parseLong(m.get(key).toString());
				long parseLong1 = Long.parseLong(m1.get(key).toString());
				if(parseLong < parseLong1){
					return -1;
				}else{
					return 1;
				}
			}else{
				return 0;
			}
		}else{
			return 0;
		}
	}
}
