package com.iscreate.plat.workflow;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class SessionFactory {

	
	public static Session getSession() {
		org.hibernate.SessionFactory sf = new Configuration().configure("jbpm.hibernate.cfg.xml")
				.buildSessionFactory();
		Session session = sf.openSession();
		return session;
	}

}
