package test;

import java.util.ArrayList;
import java.util.List;

import com.iscreate.op.service.rno.job.IsConfiguration;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.client.api.JobClient;
import com.iscreate.op.service.rno.job.common.NodeResource;
import com.iscreate.op.service.rno.job.request.GetJobDetailResponse;
import com.iscreate.op.service.rno.job.server.JobAddCallback;
import com.iscreate.op.service.rno.job.start.JobNodeFacade;

public class TestWorkerNode {
	public static void main(String[] args) {
		System.setProperty("hadoop.home.dir",
				"D:\\插件\\hadoop-common-2.2.0-bin-master-winutils");
		JobNodeFacade facade = new JobNodeFacade("JobNodeFacade");
		facade.setCanBeAManager(false);

		facade.springInit();

		runclient();

		try {
			Thread.currentThread().sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		facade.shutdown();

	}

	private static void runclient() {

		new Thread() {

			public void run() {
				testClient();
			}

			private void testClient() {
				JobClient client = JobClient
						.createJobClient(new IsConfiguration());
				System.out.println(client.getName() + "---"
						+ client.getStartTime() + "---"
						+ client.getServiceState());

				client.start();

				// // 所有的node
				List<NodeResource> nodes = client.getAllNodeMetrics();
				if (nodes != null && nodes.size() > 0) {
					System.out.println("nodes beg----");
					for (NodeResource n : nodes) {
						System.out.println(n);
					}
					System.out.println("nodes end----");
				} else {
					System.out.println("no nodes ");
				}

				/**
				 * manager node
				 */
				NodeResource manageNode = client.getManagerNode();
				System.out.println("manager node = " + manageNode);

				List<JobProfile> jobs = new ArrayList<JobProfile>();
				JobProfile job = null;
				for (int i = 0; i < 5; i++) {
					job = new JobProfile();
					job.setJobId(1L);
					job.setAccount("zhang.s");
					job.setJobName("测试");
					job.setJobType("type1");
					job = client.submitJob(job,
							new JobAddCallback<JobProfile>() {
								@Override
								public JobProfile doWhenJobAdded(JobProfile job) {
									return job;
								}

							});
					jobs.add(job);
				}
				int i = 0;
//				while (i++ < 100) {
//					try {
//						System.out.println("\n\n------");
//						Thread.sleep(2000);
//						for (JobProfile j : jobs) {
//							// 查询工作
//							GetJobDetailResponse resp = client.getJobData(j
//									.getJobId());
//							System.out.println("job detail:" + resp);
//
//						}
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}

//				}

				// kill掉刚才的job
				// KillJobRequest request=new KillJobRequest();
				// request.setAccount("zhang.s");
				// request.setJob(job);
				// request.setReason("no reason");
				// KillJobResponse resp=client.killJob(request);
				// System.out.println("kill job 的结果："+resp);

				client.stop();
			}

		}.start();

	}
}
