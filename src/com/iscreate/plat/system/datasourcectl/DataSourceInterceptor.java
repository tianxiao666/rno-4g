package com.iscreate.plat.system.datasourcectl;

import org.aspectj.lang.JoinPoint;

public class DataSourceInterceptor {
	public void setRnoDataSource(JoinPoint jp) {
//		System.out.println("--使用 rno 数据库 threadId:"+Thread.currentThread().getId()+",time:"+System.currentTimeMillis());
		DataSourceContextHolder.setDataSourceType(DataSourceConst.rnoDS);
	}
	public void setAuthDataSource(JoinPoint jp) {
//		System.out.println("--使用 权限 数据库 threadId:"+Thread.currentThread().getId()+",time:"+System.currentTimeMillis());
		DataSourceContextHolder.setDataSourceType(DataSourceConst.authDs);
//		Thread.currentThread().dumpStack();
	}
	public void setNetResDataSource(JoinPoint jp){
//		System.out.println("--使用 网络资源 数据库 threadId:"+Thread.currentThread().getId()+",time:"+System.currentTimeMillis());
		DataSourceContextHolder.setDataSourceType(DataSourceConst.netResDs);
	}
}
