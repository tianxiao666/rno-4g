package com.iscreate.op.service.rno.job.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.service.rno.job.JobCapacity;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.common.JobWorkerStatus;

public class FifoJobScheduler implements JobScheduler {

	private static Log log = LogFactory.getLog(FifoJobScheduler.class);
	JobManager jobManager;

	@Override
	public void setJobManager(JobManager jm) {
		this.jobManager = jm;
	}

//	@Deprecated
//	public List<JobProfile> assignJobs_1(JobWorkerStatus jobWorker) {
//		int curJobs = jobWorker.getCurrentJobs();
//		int maxJobs = jobWorker.getMaxJobSlots();
//		log.debug("jobworker:" + jobWorker + "的资源情况：curJobs=" + curJobs
//				+ ",maxJobs=" + maxJobs);
//		LocalJobManager jm = (LocalJobManager) jobManager;
//		List<JobProfile> jps = new ArrayList<JobProfile>();
//		JobProfile job;
//		if (maxJobs > curJobs) {
//			int remain = maxJobs - curJobs;
//			log.debug("jobworker:" + jobWorker + "还可以补充：" + (remain) + "个job");
//			synchronized (jm.waitJobQueues) {
//				Set<Long> tmpWaitQueueJobIds = new TreeSet<Long>(
//						jm.waitJobQueues.keySet());
//				if (jm.waitJobQueues.size() > 0) {
//					for (long jobId : tmpWaitQueueJobIds) {
//						job = jm.waitJobQueues.get(jobId);
//						jps.add(job);
//						jm.waitJobQueues.remove(jobId);
//						log.debug("分配job:" + jobId + "给jobWorker:" + jobWorker);
//						// 需从waitJobQueues删除
//
//						// 建立双向对应关系
//						synchronized (jm.runningJobQueues) {
//							jm.runningJobQueues.get(jobWorker).add(job);
//						}
//						synchronized (jm.jobToWorkers) {
//							jm.jobToWorkers.put(job.getJobId(), jobWorker);
//						}
//
//						remain--;
//						if (remain == 0) {
//							// 达到一个jobWorker处理的容量
//							break;
//						}
//					}
//				}
//			}
//		}
//		return jps;
//	}

	public List<JobProfile> assignJobs(JobWorkerStatus jobWorker) {
		Map<String, JobCapacity> jcs = jobWorker.getJobCapacities();
		LocalJobManager jm = (LocalJobManager) jobManager;
		log.debug("jobworker:" + jobWorker + "的资源情况：" + jcs+",等待分配的job情况："+jm.waitJobQueues);
		List<JobProfile> jps = new ArrayList<JobProfile>();
		JobProfile job;

		try{
		if (jm.waitJobQueues.size() > 0) {
			int totalJobsCnt=jm.waitJobQueues.size();
			Set<Long> tmpWaitQueueJobIds = new TreeSet<Long>(
					jm.waitJobQueues.keySet());
			Set<Long> hanledJobId=new TreeSet<Long>();
			for (JobCapacity jc : jcs.values()) {
				if (jc.hasFreeSlot()) {
					int remain = jc.getMaxSlots()-jc.getCurSlots();
					// 寻找合适任务分配
					for (long jobId : tmpWaitQueueJobIds) {
						job = jm.waitJobQueues.get(jobId);
						if(jc.getJobType().equals(job.getJobType())){
							//找到，进行分配
							totalJobsCnt--;
							hanledJobId.add(jobId);//
							jps.add(job);
							jm.waitJobQueues.remove(jobId);
							log.debug("分配job:" + jobId + "给jobWorker:" + jobWorker);
							// 需从waitJobQueues删除

							// 建立双向对应关系
							synchronized (jm.runningJobQueues) {
								jm.runningJobQueues.get(jobWorker).add(job);
							}
							synchronized (jm.jobToWorkers) {
								jm.jobToWorkers.put(job.getJobId(), jobWorker);
							}

							remain--;
							if (remain == 0) {
								//该jobworker的该类型的容量已饱和
								break;
							}
						}
					}
					tmpWaitQueueJobIds.removeAll(hanledJobId);//将已经处理过的jobid移除掉
					//所有任务已经分配完成
					if(totalJobsCnt<=0){
						break;
					}
				}
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return jps;
	}


}
