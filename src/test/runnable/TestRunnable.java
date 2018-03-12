package test.runnable;

import java.util.Date;

import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.client.BaseJobRunnable;
import com.iscreate.op.service.rno.job.common.JobState;

public class TestRunnable extends BaseJobRunnable {

	public TestRunnable(String jobType) {
		super(jobType);
	}
	public TestRunnable(){
		this("type1");
	}

	@Override
	public JobStatus runJob(JobProfile job) throws InterruptedException {
		job.modifyJobState(JobState.Running, "20%");
		JobStatus js=job.getJobStatus();
		System.out.println("i am doing job.....");
		updateProgress(js);
		JobReport jr=new JobReport(job.getJobId());
		jr.setBegTime(new Date());
		jr.setEndTime(new Date());
		jr.setAttMsg("i am doing job.....");
		super.addJobReport(jr);
		long stime=60000;
		System.out.println("准备休眠"+stime+"ms，now="+System.currentTimeMillis());
		try {
			Thread.sleep(stime);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("休眠被中断，返回");
			job.modifyJobState(JobState.Killed, "休眠被中断");
			updateProgress(job.getJobStatus());
			return job.getJobStatus();
		}
		System.out.println("休眠醒来。。。。now="+System.currentTimeMillis());
		job.modifyJobState(JobState.Finished, "100%");
		updateProgress(js);
		return js;
	}

	@Override
	public void updateOwnProgress(JobStatus jobStatus) {
		System.out.println("i update my own progress..."+jobStatus);
	}
	@Override
	public void releaseRes() {
		System.out.println("release res...");
		
	}

}
