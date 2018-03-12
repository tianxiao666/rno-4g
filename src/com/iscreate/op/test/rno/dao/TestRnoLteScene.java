package com.iscreate.op.test.rno.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.iscreate.op.dao.rno.RnoJobDao;
import com.iscreate.op.dao.rno.RnoJobDaoImpl;
import com.iscreate.op.dao.rno.RnoLteSceneManageDao;
import com.iscreate.op.dao.rno.RnoLteSceneManageDaoImpl;
import com.iscreate.op.dao.rno.RnoStructureQueryDao;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.common.JobState;
import com.iscreate.op.test.rno.TestBase;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;

public class TestRnoLteScene extends TestBase {
	RnoLteSceneManageDao dao;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		dao = (RnoLteSceneManageDao) super.context
				.getBean("rnoLteSceneManageDao");
		assertNotNull(dao);
	}

	
	public void testGetRunningJob(){
		List<Map<String, Object>> aa=dao.queryLteGridDataByCityId(91);
		for (Map<String, Object> map : aa) {
			System.out.println(map.get("GRID_ID")+"------"+map.get("GRID_LNGLATS"));
//			System.out.println(map.get("GRID_ID"));
		}
	}
 }
