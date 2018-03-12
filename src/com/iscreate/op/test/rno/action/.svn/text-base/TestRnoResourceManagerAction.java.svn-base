package com.iscreate.op.test.rno.action;

import com.iscreate.op.action.rno.RnoResourceManagerAction;
import com.iscreate.op.test.rno.TestBase;

public class TestRnoResourceManagerAction extends TestBase {

	RnoResourceManagerAction action;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		action=(RnoResourceManagerAction)super.context.getBean("rnoResourceManagerAction");
		assertNotNull(action);
	}
	
	public void testGetBscsResideInAreaForAjaxAction(){
		action.setAreaId(14921);
		action.getBscsResideInAreaForAjaxAction();
	}
}
