package com.iscreate.op.test.rno.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.LifeCycle;
import com.iscreate.op.service.rno.job.client.JobWorker;
import com.iscreate.op.service.rno.job.client.api.JobClient;
import com.iscreate.op.service.rno.job.common.JobState;
import com.iscreate.op.service.rno.job.server.JobManager;
import com.iscreate.op.test.rno.TestBase;

public class TestLocalJobManager extends TestBase {

	JobManager jobManager;
	JobWorker jobWorker;
	JobClient jobClient;
	boolean begin = false;
	Object obj = new Object();

	CountDownLatch waitObjCnt = null;
	long t1 = -1, t2 = -1;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		t1 = System.currentTimeMillis();
		jobManager = (JobManager) super.context
				.getBean("localJobManager");
		assertNotNull(jobManager);
//		((LifeCycle)jobManager).start();
		
		jobWorker=(JobWorker)super.context.getBean("localJobWorker");
		assertNotNull(jobWorker);
		
		jobClient=(JobClient)super.context.getBean("localJobClient");
		assertNotNull(jobClient);
		
		
	}

	@Override
	protected void tearDown() throws Exception {
		
		waitObjCnt.await();

		super.tearDown();
		((LifeCycle)jobManager).stop();
		t2 = System.currentTimeMillis();
		System.out.println("take time:" + (t2 - t1) + "ms");
	}

	@Test
	public void testAddAndAssignJob() {
		waitObjCnt = new CountDownLatch(2);
		addJobs();
//		fakejobworker();
		
		new Thread(){
			public void run() {
				try {
					Thread.currentThread().sleep(60*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized(waitObjCnt){
					waitObjCnt.countDown();
				}
			};
		}.start();
		
		
		
	}
	
//	private void realJobWorks(){
//		
//	}
	
	private void addJobs(){
		// 线程1：提交任务
				new Thread(new Runnable() {
					@Override
					public void run() {
						List<Long> jobIds = new ArrayList<Long>();
						begin = true;
						int sum = 1;
						while (sum < 5) {
							try {
								Thread.currentThread().sleep(2 * 1000);
							} catch (InterruptedException e) {
							}
							System.out.println("提交job...");
							JobProfile job = new JobProfile();
							job.setAccount("liu.f");
							job.setDescription("a job " + sum);
							job.setJobName("韶关六月结构分析任务" + sum);
							job.setJobType("testjob");
//							job.setJobRunningStatus(JobRunningStatus.Initiate
//									.toString());
							job.modifyJobState(JobState.Initiate, "");
							job.setPriority(1);
							job.setSubmitTime(new Date());

							JobStatus status = jobManager.addJob(job);
							jobIds.add(status.getJobId());
							System.out.println("add job,jobId=" + status.getJobId());
							sum++;

							// if(jobIds.size()>1){
							// JobStatus st2=new JobStatus();
							// st2.setJobId(jobIds.get(jobIds.size()-2));
							// st2.setJobRunningStatus(JobRunningStatus.Succeded);
							// //end previous job
							// st2=jobManager.updateJobStatus(st2);
							// }
						}
						synchronized (waitObjCnt) {
							waitObjCnt.countDown();
						}
						synchronized(obj){
							obj.notify();
						}
						//直接kill
						JobProfile job=new JobProfile();
						long job_id=127L;
						job.setJobId(job_id);
						String account="linda";
						String reason="选错数据，无效。";
						System.out.println("-----kill job "+job_id+"----");
						jobClient.killJob(job, account, reason);
						
						//休眠一段时间后kill
						try {
							Thread.currentThread().sleep(3*1000);
						} catch (InterruptedException e) {
						}
						job=new JobProfile();
						job_id++;
						job.setJobId(job_id);
						account="sunny";
						reason="时间不对，重新运算。";
						System.out.println("-----kill job "+job_id+"----");
						jobClient.killJob(job, account, reason);
						
					}

				}).start();

	}
	
	private void fakejobworker(){
		// 线程2：心跳检测
			/*	new Thread(new Runnable() {
					@Override
					public void run() {
						
						while (!begin) {
							try {
								Thread.currentThread().sleep(2 * 1000);
							} catch (InterruptedException e) {
							}
						}
						//等线程1跑完
						synchronized(obj){
							try {
								obj.wait();
							} catch (InterruptedException e) {
							}
						}
						
//						// 2个jobWorker
		//
//						// --1
						JobWorkerStatus jw1 = new JobWorkerStatus();
						jw1.setHost("jw1");
						jw1.setCurrentJobs(0);
						jw1.setMaxJobSlots(1);
						List<JobAction> ja1 = jobManager.heartbeat(jw1, null, null);
						System.out.println("ja1="+ja1);
						// --2
						JobWorkerStatus jw2 = new JobWorkerStatus();
						jw2.setHost("jw2");
						jw2.setCurrentJobs(0);
						jw2.setMaxJobSlots(1);
						List<JobAction> ja2 = jobManager.heartbeat(jw2, null, null);
						System.out.println("ja2="+ja2);
						while (ja1.size() > 0 || ja2.size()>0) {
							// 等待一定时间，提交状态
							try {
								Thread.currentThread().sleep(3 * 1000);
							} catch (InterruptedException e) {
							}
							JobReport rep = null;
							JobStatus st = null;
							List<JobReport> rep1 = new ArrayList<JobReport>();
							List<JobStatus> sts1 = new ArrayList<JobStatus>();
							for (JobAction ja : ja1) {
								rep = new JobReport();
								rep.setJobId(ja.getJobProfile().getJobId());
								rep.setStage("完成了" + System.currentTimeMillis());
								rep.setFinishState(JobRunningStatus.Succeded.toString());
								rep1.add(rep);

								st = new JobStatus();
								st.setJobId(ja.getJobProfile().getJobId());
								st.setJobRunningStatus(JobRunningStatus.Succeded);
								st.setUpdateTime(new Date());
								sts1.add(st);
							}
							System.out.println("jw1 heart beat");
							ja1 = jobManager.heartbeat(jw1, rep1, sts1);
							System.out.println("jw1 heart beat return:ja1="+ja1);
							
							//jw2
							rep1.clear();
							sts1.clear();
							for (JobAction ja : ja2) {
								rep = new JobReport();
								rep.setJobId(ja.getJobProfile().getJobId());
								rep.setStage("完成了" + System.currentTimeMillis());
								rep.setFinishState(JobRunningStatus.Succeded.toString());
								rep1.add(rep);

								st = new JobStatus();
								st.setJobId(ja.getJobProfile().getJobId());
								st.setJobRunningStatus(JobRunningStatus.Succeded);
								st.setUpdateTime(new Date());
								sts1.add(st);
							}
							System.out.println("jw2 heart beat");
							ja2 = jobManager.heartbeat(jw2, rep1, sts1);
							System.out.println("jw2 heart beat return:ja2="+ja2);
						}
						
						System.out.println("---退出jobworker线程。。。。。");
						synchronized (waitObjCnt) {
							waitObjCnt.countDown();
						}
					}

				}).start();*/
	}

//	private void testAddReport() throws LifeCycleException {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				begin = true;
//				int sum = 5;
//				while (sum > 0) {
//					try {
//						Thread.currentThread().sleep(2 * 1000);
//					} catch (InterruptedException e) {
//					}
//					JobReport report = new JobReport();
//					report.setJobId(1);
//					report.setStage("解析文件" + sum);
//					report.setBegTime(new Date());
//					report.setEndTime(new Date());
//					report.setAttMsg("文件名：duo" + sum + ".xls");
//					jobManager.addJobReport(report);
//					sum--;
//				}
//				synchronized (obj) {
//					obj.notifyAll();
//				}
//			}
//
//		}).start();
//
//	}

//	private void addReport(JobManager jm, JobReport report) {
//		jm.addJobReport(report);
//	}
}
