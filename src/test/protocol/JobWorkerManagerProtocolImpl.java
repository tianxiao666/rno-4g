package test.protocol;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.ipc.ProtocolSignature;

import com.iscreate.op.service.rno.job.JobAction;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.common.JobWorkerStatus;
import com.iscreate.op.service.rno.job.server.JobManager;

public class JobWorkerManagerProtocolImpl implements JobWorkerManagerProtocol {

	JobManager distributedJobManager;
	public JobWorkerManagerProtocolImpl(JobManager jobManager){
		distributedJobManager=jobManager;
	}
	@Override
	public ProtocolSignature getProtocolSignature(String protocol,
			long clientVersion, int clientMethodsHash) throws IOException {
		return ProtocolSignature.getProtocolSignature(this, protocol,
				clientVersion, clientMethodsHash);
	}

	@Override
	public long getProtocolVersion(String arg0, long arg1) throws IOException {
		return JobWorkerManagerProtocol.versionID;
	}

	@Override
	public JobWorkerStatus register(JobWorkerStatus client) {
		// TODO Auto-generated method stub
//		return DistributedJobManager;
		return distributedJobManager.register(client);
	}

	@Override
	public List<JobAction> heartbeat(JobWorkerStatus jobWorkerStatus,
			List<JobReport> reports, List<JobStatus> updateJobStatusList) {
		
		return distributedJobManager.heartbeat(jobWorkerStatus, reports, updateJobStatusList);
	}
	@Override
	public boolean requireToUpgrade(JobWorkerStatus client) {
		
		return false;
	}

}
