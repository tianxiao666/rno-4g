package com.iscreate.op.service.rno.job.client;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.iscreate.op.service.rno.job.JobProfile;

public class SimpleJobRunnableMgr implements JobRunnableManager {

	// private List<JobRunnable> runnables=new ArrayList<JobRunnable>();
	private Map<String, Class<? extends JobRunnable>> jobTypeToRunCls = new HashMap<String, Class<? extends JobRunnable>>();

	@Override
	public void addJobRunnableCls(String jobType, String fullClsName) {
		if(jobType==null||"".equals(jobType.trim())
				|| fullClsName==null || "".equals(fullClsName.trim())
				){
			return;
		}
		if(jobTypeToRunCls.containsKey(jobType)){
			return;
		}
		try {
			Class cls=Class.forName(fullClsName);
			jobTypeToRunCls.put(jobType, cls);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public JobRunnable findJobRunnable(JobProfile job) {
		for (String k : jobTypeToRunCls.keySet()) {
			if (k.equals(job.getJobType())) {
				return createRunnable(jobTypeToRunCls.get(k), k);
			}
		}
		return null;
	}

	public JobRunnable createRunnable(Class cls, String jobType) {
		try {
			Object obj=null;
			Constructor[] cons=cls.getConstructors();
			Constructor hasJobTypeCon=null;
			Constructor noParaCon=null;
			for(Constructor con:cons){
				Class[] paraClss=con.getParameterTypes();
				if(paraClss.length==0){
					noParaCon=con;
				}else if(paraClss.length==1 && paraClss[0].getName().equals(String.class.getName())){
					hasJobTypeCon=con;
				}
			}
			if(hasJobTypeCon==null){
				//尝试通过无参数构造函数创建对象
				obj=cls.newInstance();
			}else{
				obj = hasJobTypeCon.newInstance(jobType);
			}
			if (obj instanceof JobRunnable) {
				return ((JobRunnable) obj);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return null;
	}

	
}
