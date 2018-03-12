package com.iscreate.plat.networkresource.dataservice;


import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.engine.SessionFactoryImplementor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * 
 * @filename: DataSourceConn.java
 * @classpath: com.iscreate.plat.networkresource.dataservice
 * @description: 获取数据库连接 类
 * @author：
 * @date：Apr 22, 2013 9:48:27 AM
 * @version：
 */
public class DataSourceConn {
	private static final Log log = LogFactory.getLog(DataSourceConn.class);
	static	DataSourceConn     dataSourceConn;
	static SessionFactory sessionFactory;
	static{
		if(sessionFactory==null){
			ClassPathResource resource=new ClassPathResource("spring/datasource-appcontx.xml"); 
			BeanFactory factory=new XmlBeanFactory(resource); 
			sessionFactory = (SessionFactory)factory.getBean("sessionFactory");
		}
	}
	public DataSourceConn(){
		
	}

	public static DataSourceConn  initInstance(){
		if(dataSourceConn==null){
			dataSourceConn = new DataSourceConn();
		}
		return dataSourceConn;
	}
	/**
	 * 
	 * @description:获取connect连接
	 * @author：
	 * @return     
	 * @return Connection     
	 * @date：Apr 22, 2013 9:48:08 AM
	 */
	public Connection getConnection() {
		log.info("进入getConnection()方法，获取数据库连接");
		if(sessionFactory==null){
			log.error("退出getConnection()方法，获取不到sessionFactory值，请检查datasource配置");
			return null;
		}
		ConnectionProvider cp = ((SessionFactoryImplementor)sessionFactory).getConnectionProvider();
		Connection connection=null;
		try {
			connection = cp.getConnection();
			log.info("退出getConnection()方法，获取到数据库连接connect="+connection);
		} catch (SQLException e) {
			log.error("退出getConnection()方法，获取不到数据库连接connect，请检查datasource配置");
			e.printStackTrace();
		}
		return connection;
	}
	
	/*public static void main(String args[]){
		DataSourceConn d = DataSourceConn.initInstance();
		Connection conn = d.getConnection();
		System.out.println(conn);
	}*/
}

