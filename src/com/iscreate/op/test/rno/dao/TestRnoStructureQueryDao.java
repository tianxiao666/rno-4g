package com.iscreate.op.test.rno.dao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.iscreate.op.action.rno.vo.Threshold;
import com.iscreate.op.action.staffduty.staffDutyActionForMobile;
import com.iscreate.op.dao.rno.RnoStructureQueryDao;
import com.iscreate.op.pojo.rno.RnoNcsDescriptor;
import com.iscreate.op.pojo.rno.RnoThreshold;
import com.iscreate.op.pojo.rno.RnoThreshold;
import com.iscreate.op.test.rno.TestBase;

public class TestRnoStructureQueryDao extends TestBase {
	RnoStructureQueryDao dao;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		dao = (RnoStructureQueryDao) super.context
				.getBean("rnoStructureQueryDao");
		assertNotNull(dao);
	}

//	public void testGetNcsDescriptorCount(){
//		Map<String,String> cond=new HashMap<String,String>();
//		//cond.put("areaid", "193");
//		cond.put("bsc","ZHM03B");
//		long cnt=dao.getNcsDescriptorCount(cond);
//		System.out.println("cnt="+cnt);
//		
//		List<Map<String,Object>> desc=dao.queryNcsDescriptorByPage(cond, 0, 10);
//		System.out.println(desc);
//		
//	}
	
	/*public void testQueryAreaDamageFactor(){
		List<Map<String,Object>> desc=dao.queryAreaDamageFactor(Arrays.asList(199l,216l));
		System.out.println(desc);
	}
	public void testQueryAreaNormalizeFactor(){
		List<Map<String,Object>> desc=dao.queryAreaNormalizeFactor(Arrays.asList(199l,216l));
		System.out.println(desc);
	}*/
	public void testQueryAreaDamageFactor(){
		/*Threshold threshold=dao.getThresholdByModType("STRUCTANA");
		System.out.println(threshold);*/
		/*Map<String, Object> map=dao.getStructAnaSummaryInfoForTimeRange(88, "2014-03-30", "2014-03-30", "ERI");
		Iterator<Entry<String, Object>> e=map.entrySet().iterator();
		while (e.hasNext()) {
			Entry<String, Object> entry=e.next();
			System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
			
		}*/
		/*List<RnoThreshold> thresholds=dao.getThresholdsByModuleType("STRUCTANA");
		for (RnoThreshold threshold : thresholds) {
			System.out.println(threshold);
		}*/
		List<RnoThreshold> thresholds=dao.getThresholdsByModuleType("STRUCTANA");
		for (RnoThreshold threshold : thresholds) {
			System.out.println(threshold);
		}
	}
}
