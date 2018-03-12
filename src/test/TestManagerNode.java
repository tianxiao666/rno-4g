package test;

import com.iscreate.op.service.rno.job.start.JobNodeFacade;

public class TestManagerNode {

	public static void main(String[] args) {
		System.setProperty("hadoop.home.dir", "D:\\插件\\hadoop-common-2.2.0-bin-master-winutils");
		JobNodeFacade facade=new JobNodeFacade("JobNodeFacade");
		facade.setCanBeAWorker(false);
		facade.springInit();
	}
}
