package com.iscreate.op.test.rno.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.GisCellQueryResult;
import com.iscreate.op.pojo.rno.RnoGisCell;
import com.iscreate.op.service.rno.Rno2GEriCellManageService;
import com.iscreate.op.service.rno.RnoResourceManagerService;
import com.iscreate.op.test.rno.TestBase;


public class TestRnoResourceService extends TestBase {

	RnoResourceManagerService service;
	Rno2GEriCellManageService eriservice;
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		/*service=(RnoResourceManagerService)super.context.getBean("rnoResourceManagerService");
		assertNotNull(service);*/
		eriservice=(Rno2GEriCellManageService)super.context.getBean("rno2gEriCellManageService");
		assertNotNull(eriservice);
	}
	public void testGetBscResideInArea(){
		Map<String, Integer> bscs=eriservice.queryBscByCityId(89);
		System.out.println("output bscs:");
		Map<String, Object> bsc;
		
			for (String key : bscs.keySet()) {
				System.out.println(key+"===="+bscs.get(key));
				
			}
			System.out.println(bscs.get("abc"));
		
	}
	/*
	public void testGetBscResideInArea(){
		List<RnoBsc> bscs=service.getBscsResideInAreaByAreaId(190);
		System.out.println("output bscs:");
		if(bscs!=null){
			for(RnoBsc bsc:bscs){
				System.out.println("--  "+bsc.getChinesename());
			}
		}
	}
	*/
	/*public void testGetGisCellIArea(){
		long areaId=190;
		Page page=new Page();
		page.setCurrentPage(1);
		page.setPageSize(5);
		GisCellQueryResult queryResult=service.getGisCellByPage(areaId, page);
		assertNotNull(queryResult);
		List<RnoGisCell> gisCells=queryResult.getGisCells();
		for(RnoGisCell gisCell:gisCells){
			System.out.println("--"+gisCell);
		}
		page.setCurrentPage(3);
		
		queryResult=service.getGisCellByPage(areaId, page);
		assertNotNull(queryResult);
		gisCells=queryResult.getGisCells();
		for(RnoGisCell gisCell:gisCells){
			System.out.println("--"+gisCell);
		}
	}*/
}
