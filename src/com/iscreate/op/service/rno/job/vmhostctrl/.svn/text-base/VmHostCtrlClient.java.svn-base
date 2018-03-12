package com.iscreate.op.service.rno.job.vmhostctrl;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.service.AbstractService;

import com.iscreate.op.service.rno.job.avro.proto.VmHostCtrlProtocolAvro;

public abstract class VmHostCtrlClient extends AbstractService implements
		VmHostCtrlProtocolAvro {

	public VmHostCtrlClient(String name) {
		super(name);
	}

	public synchronized static VmHostCtrlClient getClient(Configuration conf,
			String host, int port) {
		VmHostCtrlClientImpl impl = new VmHostCtrlClientImpl(host, port);
		impl.init(conf);
		return impl;
	}

}
