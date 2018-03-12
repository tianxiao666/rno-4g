package com.iscreate.plat.system.datasourcectl;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class DynamicDataSource extends org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource {

	protected Object determineCurrentLookupKey() {
		String dstype=DataSourceContextHolder.getDataSourceType();
//		System.out.println("--选取数据源："+dstype+" threadId:"+Thread.currentThread().getId()+",time:"+System.currentTimeMillis());
//		Thread.currentThread().dumpStack();
		return dstype;
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) {
		Exception e = new Exception("this is a log");
		e.printStackTrace();
		
		Thread.currentThread().dumpStack();
	}

}
