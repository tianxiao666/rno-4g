//package com.iscreate.op.service.rno;
//
//import java.util.Date;
//
//import com.danga.MemCached.MemCachedClient;
//import com.danga.MemCached.SockIOPool;
//
//public class MemCached {
//
//	protected static MemCachedClient mcc=new MemCachedClient();
//	//protected static MemCached memCached=new MemCached();
//	
//	static{
//		String[] servers={"192.168.208.111:11211"};
//		Integer[] weights={3};
//		
//		SockIOPool pool=SockIOPool.getInstance();
//		
//		pool.setServers(servers);
//		pool.setWeights(weights);
//		
//		pool.setInitConn(5);
//		pool.setMinConn(5);
//		pool.setMaxConn(250);
//		pool.setMaxIdle(1000*60*60);
//		
//		pool.setMaintSleep(30);
//		
//		pool.setNagle(false);
//		pool.setSocketTO(3000);
//		pool.setSocketConnectTO(0);
//		
//		pool.initialize();
//		
//	
//	}
//	
//	protected MemCached(){}
//	
//	public static MemCachedClient getMemCachedClient(){
//		return mcc;
//	}
//	
//	public boolean add(String key,Object value){
//		return mcc.add(key, value);
//	}
//	
//	public boolean add(String key,Object value,Date expire){
//		return mcc.add(key, value,expire);
//	}
//	
//	public boolean replace(String key,Object value){
//		return mcc.replace(key, value);
//	}
//	
//	public boolean replace(String key,Object value,Date expiry){
//		return mcc.replace(key,value,expiry);
//	}
//	
//	public Object get(String key){
//		return mcc.get(key);
//	}
//	
//	public boolean delete(String key){
//		return mcc.delete(key);
//	}
//	
//	public boolean set(String key,Object value){
//		return mcc.set(key,value);
//	}
//	
//	public boolean set(String key,Object value,Date expiry){
//		
//		return mcc.set(key,value,expiry);
//		
//	}
//	
//	public static void main(String[] args){
//		MemCachedClient cache=MemCached.getMemCachedClient();
//		//System.out.println(cache.add("hello", "world",10));
//	    
//		System.out.println("get value:"+cache.get("GSMCELLFILE466d8f121dc04b319a9143aced07f4c0"));
//	    
////	    try {
////			Thread.currentThread().sleep(20*1000);
////		} catch (InterruptedException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//	    //System.out.println(cache.replace("hello", "nice"));
//	    //System.out.println("get value:"+cache.get("hello"));
//	    
//	  // System.out.println(cache.delete("hello"));
//	    
//	}
//	
////	public static void main(String[] args) {
////		MemCached cache=MemCached.getInstance();
//////		System.out.println(cache.add("hello", "world"));
//////		System.out.println("get value:"+cache.get("hello"));
////		List<Cell> cells=new ArrayList<Cell>();
////		double lng=113.34d;
////		double lat=23.432d;
////		Cell cell=null;
////		for(int i=0;i<1000;i++){
////			cell=new Cell("cell_"+i,lng,lat);
////			cells.add(cell);
////		}
////		System.out.println(cache.add("cells2", cells));
////		List<Cell> ano=(List<Cell>)cache.get("cells2");
////		System.out.println("size="+ano.size());
////		for(int i=0;i<ano.size();i++){
////			System.out.println("--"+ano.get(i).getName());
////		}
////	}
//	
//}
