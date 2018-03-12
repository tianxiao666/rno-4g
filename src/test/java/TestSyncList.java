package test.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TestSyncList {

	static class Po {
		String name="";
	}
	
	public static void main(String[] args) {
		List<Po> pos=Collections.synchronizedList(new ArrayList<Po>());
		Po p=new Po();
		pos.add(p);
		p=new Po();
		pos.add(p);
		p=new Po();
		pos.add(p);
		
		System.out.println("--old---");
		for(Po np:pos){
			System.out.println("addr:"+np);
		}
		
		System.out.println("----new");
		List<Po> npos=pos.subList(0, pos.size());
		for(Po np:npos){
			System.out.println("addr:"+np);
		}
		
		int avg=(int) Math.ceil(5*1.0f/6);
		System.out.println(avg);
		
		List<Integer> ints=new ArrayList<Integer>();
		ints.add(5);
		ints.add(1);
		ints.add(7);
		ints.add(2);
		
		System.out.println(ints);
		Collections.sort(ints,new Comparator<Integer>(){
			@Override
			public int compare(Integer o1, Integer o2) {
				System.out.println("compare "+o1+" : "+o2);
				if(o1>o2){
					System.out.println("--正1");
					return 1;
				}else if(o1<o2){
					System.out.println("--负1");
					return -1;
				}
				System.out.println("--0");
				return 0;
			}
		});
		
		System.out.println(ints);
		Collections.sort(ints);
		System.out.println(ints);
	}
}
