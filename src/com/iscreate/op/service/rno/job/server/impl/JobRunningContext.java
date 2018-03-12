package com.iscreate.op.service.rno.job.server.impl;

import java.sql.Connection;
import java.sql.Statement;

public class JobRunningContext {

	private JobWorkerNode workerNode;
	private Connection conn;
	private Statement stmt;

	public JobWorkerNode getWorkerNode() {
		return workerNode;
	}

	public void setWorkerNode(JobWorkerNode workerNode) {
		this.workerNode = workerNode;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public Statement getStmt() {
		return stmt;
	}

	public void setStmt(Statement stmt) {
		this.stmt = stmt;
	}

}
