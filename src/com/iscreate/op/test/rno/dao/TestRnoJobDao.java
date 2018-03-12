package com.iscreate.op.test.rno.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import com.iscreate.op.dao.rno.RnoJobDao;
import com.iscreate.op.dao.rno.RnoJobDaoImpl;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.common.JobState;
import com.iscreate.op.test.rno.TestBase;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;

public class TestRnoJobDao extends TestBase {
	RnoJobDao jobDao=new RnoJobDaoImpl();
	Connection conn=null;
	Statement stmt=null;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		conn=DataSourceConn.initInstance().getConnection();
		try {
			stmt=conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	}
//	public void testAddJob(){
//		Statement stmt=null;
//		try {
//			stmt=conn.createStatement();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return;
//		}
//		
//		
//		JobProfile job=new JobProfile();
//		job.setAccount("liu.s");
//		job.setDescription("个人任务");
//		job.setJobName("6月结构分析");
//		job.setJobType("struct_ana");
//		job.setPriority(1);
//		job.setSubmitTime(new Date());
//		job.setJobRunningStatus(JobRunningStatus.Initiate.toString());
//		
//		job=jobDao.addOneJob(stmt, job);
//		System.out.println("job="+job);
//	}
	
	public void testGetRunningJob(){
		List<JobProfile> jobs=jobDao.getRunningJobs(stmt);
		System.out.println("---size="+jobs.size());
		for(JobProfile job:jobs){
			System.out.println("--"+job);
			JobStatus status=new JobStatus();
			status.setJobId(job.getJobId());
//			status.setJobRunningStatus(JobRunningStatus.Succeded);
			status.setJobState(JobState.Finished);
			status.setUpdateTime(new Date());
			jobDao.endOneJob(stmt, status);
		}
	}
 }
