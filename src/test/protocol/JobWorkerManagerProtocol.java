package test.protocol;

import java.util.List;

import org.apache.hadoop.ipc.VersionedProtocol;

import com.iscreate.op.service.rno.job.JobAction;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.common.JobWorkerStatus;

public interface JobWorkerManagerProtocol extends VersionedProtocol{

	public static final long versionID=1L;
	
	/**
	 * 
	 * @param me
	 * @return
	 */
	public JobWorkerStatus register(JobWorkerStatus client);
	
	public List<JobAction> heartbeat(JobWorkerStatus jobWorkerStatus,
			List<JobReport> reports, List<JobStatus> updateJobStatusList) ;
	
	/**
	 * 这个jobWorker希望代替jobManager成为管理节点
	 * @param client
	 * @return
	 */
	public boolean requireToUpgrade(JobWorkerStatus client);
}
