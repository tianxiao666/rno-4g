package com.iscreate.op.service.rno.task;

import java.util.Map;

public interface RnoTaskWorker {

	/**
	 * 运行任务
	 * @param taskId
	 * @param params
	 * 额外参数
	 * @return
	 * @author brightming
	 * 2014-1-23 下午3:57:32
	 */
	public TaskStatus doWork(long taskId,Map<String,String> params);
}
