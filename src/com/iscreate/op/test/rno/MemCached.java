package com.iscreate.op.test.rno;
import java.io.IOException;

import junit.framework.TestCase;
import net.rubyeye.xmemcached.Counter;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.transcoders.StringTranscoder;

import org.junit.Test;

import com.mchange.v2.resourcepool.TimeoutException;
public class MemCached extends TestCase{

	private MemcachedClient memCached;

	public MemcachedClient getMemCached() {
		return memCached;
	}
	public void setMemCached(MemcachedClient memCached) {
		this.memCached = memCached;
	}
	@Test
    public void testGet2() throws IOException, TimeoutException, InterruptedException, MemcachedException{
        /*MemcachedClientBuilder builder = new XMemcachedClientBuilder(
                AddrUtil.getAddresses("192.168.127.129:11211"));
        MemcachedClient memcachedClient = builder.build();*/
		System.out.println("memcachedClient===========>>>>>>>>>>>>>>"+memCached);
        String value ="";
        try {
        	memCached.set("NAME", 60, "�ܸ�");
			value = memCached.get("NAME");
			 System.out.println(value);
			 
			 memCached.set("NAME", 60, "jack");
			 value = memCached.get("NAME");
			 System.out.println(value);
			 
			 memCached.add("NAME1", 60, "jack1");
			 value = memCached.get("NAME1");
			 System.out.println(value);
			 
			 memCached.delete("NAME1");
			 value = memCached.get("NAME1");
			 System.out.println(value);
			 
			//ԭ���滻
			/*GetsResponse<Integer> result= client.gets("NAME");
			long cas = result.getCas();
			if(!client.cas("NAME", 0, 2, cas)){
				System.err.println("cas error");
			}
			System.out.println(client.get("NAME"));*/
			 
			//ͷβ��׷��
			 memCached.prepend("NAME","hello");
			 memCached.append("NAME", "good person");
			value = memCached.get("NAME",new StringTranscoder());
			 System.out.println(value);
			 /** 
	         * ��ɾ���������ͨ��deleteWithNoReply�������������ɾ����ݲ��Ҹ���memcached 
	         * ���÷���Ӧ����������������ȴ�Ӧ��ֱ�ӷ��أ��ر��ʺ����������� 
	         */  
//	        client.deleteWithNoReply("NAME"); 
	        
	        /** 
	         * ��һ������ָ��������key��ƣ� �ڶ�������ָ�������ķ�ȴ�С�� ���������ָ����key�����ڵ�����µĳ�ʼֵ�� 
	         * ������������ط���ʡ���˵��������Ĭ��ָ��Ϊ0�� 
	         * ��Ҫ��������
	         * 1.Run -> Run Configurations -> Argumentsҳǩ -> VM arguments�ı����м��϶��Կ����ı�־:-enableassertions ����-ea �Ϳ�����
			   2.��myEclipse��,Windows -> Preferences ->Java ->Installed JREs ->�����ʹ�õ�JDK ->Edit ->Default VM Arguments�ı���������:-ea�����˲����˴˷�����
	         */  
	        assert (1 == memCached.incr("a", 5, 1));  
	        assert (6 == memCached.incr("a", 5));  
	        assert (10 == memCached.incr("a", 4));  
	        assert (9 == memCached.decr("a", 1));  
	        assert (7 == memCached.decr("a", 2));  
	        
	        assert false : "����ʧ�ܣ��˱��ʽ����Ϣ�������׳��쳣��ʱ�������";
	        System.out.println("����2û�����⣬Go��");
	        
	        //counter
	        Counter counter = memCached.getCounter("counter", 0);  
	        counter.incrementAndGet();  
	        counter.decrementAndGet();  
	        counter.addAndGet(-10); 
	        
	        System.out.println(counter.get());
		} catch (java.util.concurrent.TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			memCached.shutdown();
		}
       
       
    }
}
