package com.iscreate.op.test.rno.service;

import net.rubyeye.xmemcached.MemcachedClient;

import com.iscreate.op.test.rno.TestBase;

public class TestMemCachedClient extends TestBase {

	MemcachedClient client;
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.out.println("context="+context);
		client=(MemcachedClient)context.getBean("memCached");
	}
	
	
	public void testAdd() throws Exception {
//		try {
//			boolean ok=client.set("GSMCELLFILEa86ef1a615c84a82a6236fa50bf4c386", 1000*60*5, "good");
//			System.out.println("ok=="+ok);
//		} catch (TimeoutException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (MemcachedException e) {
//			e.printStackTrace();
//		}
//		System.out.println(" get add:"+client.get("GSMCELLFILEa86ef1a615c84a82a6236fa50bf4c386"));
//		
//		
//	    Thread t=new Thread(){
//	    	@Override
//	    	public void run() {
//	    		String token="GSMCELLFILE88b5be4d84a3470ab537f6081b65ecdc111";
//	    		try {
//					boolean ok=client.set(token,RnoConstant.TimeConstant.TokenTime,"导入很多条记录！");
//					System.out.println("ok===="+ok);
//				} catch (TimeoutException e) {
//					e.printStackTrace();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				} catch (MemcachedException e) {
//					e.printStackTrace();
//				}
//	    	}
//	    };
//	    t.start();
//	    t.join();
//	    System.out.println("ret1= "+client.get("GSMCELLFILE88b5be4d84a3470ab537f6081b65ecdc111"));
//	    
		String token="GSMCELLFILE5e6a1c87cc474a44b0e29100d98a0c1b";
//	    client.set(token, 3600, "确实导入了很多啊");
	    System.out.println("ret2= "+client.get(token));
	    
//	    client.set("GSMCELLFILE88b5be4d84a3470ab537f6081b65ecdc123", 3600, "真的确实导入了很多啊");
//	    System.out.println("ret3= "+client.get("GSMCELLFILE88b5be4d84a3470ab537f6081b65ecdc123"));
	    
//	    System.out.println("delete ="+client.delete(token));
	}
	
//	@Test
//	public void testGet() throws Exception {
//		String re=client.get("GSMCELLFILE88b5be4d84a3470ab537f6081b65ecdc");
//		System.out.println("ret = "+re);
//	}
//	
//	public void testReplace() throws TimeoutException, InterruptedException, MemcachedException{
//		System.out.println("add:"+client.add("another", 1000*60*2, "another"));
//		System.out.println("replace:"+client.replace("three", 1000*20, "three"));
//		
//		System.out.println(" get add:"+client.get("another"));
//		System.out.println(" get replace:"+client.get("three"));
//		
//		System.out.println("replace another:"+client.replace("another", 1000*60*1, "be replaced"));
//		System.out.println(" get replace another:"+client.get("another"));
//		
//	}
}
