package com.iscreate.op.service.rno.job.vmhostctrl;

import java.net.InetSocketAddress;

import org.apache.avro.AvroRemoteException;
import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.apache.hadoop.conf.Configuration;
import org.apache.log4j.Logger;

import com.iscreate.op.service.rno.job.avro.proto.VmCtrlRequest;
import com.iscreate.op.service.rno.job.avro.proto.VmCtrlResponse;
import com.iscreate.op.service.rno.job.avro.proto.VmHostCtrlProtocolAvro;
import com.iscreate.op.service.rno.job.avro.proto.VmInfoList;

public class VmHostCtrlClientImpl extends VmHostCtrlClient {

	private static Logger log=Logger.getLogger(VmHostCtrlClientImpl.class);
	NettyTransceiver netty = null;
	VmHostCtrlProtocolAvro proxy = null;

	private String host = "localhost";
	private int port = 65000;

	public VmHostCtrlClientImpl() {
		this("localhost", 65000);
	}

	public VmHostCtrlClientImpl(String host, int port) {
		super("VmHostCtrlClientImpl");

		this.host = host;
		this.port = port;
	}

	@Override
	protected void serviceInit(Configuration conf) throws Exception {
		
		netty = new NettyTransceiver(new InetSocketAddress(host, port));
		proxy = (VmHostCtrlProtocolAvro) SpecificRequestor.getClient(
				VmHostCtrlProtocolAvro.class, netty);
		
		super.serviceInit(conf);
	}

	@Override
	protected void serviceStop() throws Exception {
		netty.close();
		super.serviceStop();
	}
	
	@Override
	public VmCtrlResponse createVm(VmCtrlRequest request)
			throws AvroRemoteException {
		VmCtrlResponse response=proxy.createVm(request);
		log.debug("createVm resp="+response);
		return response;
	}

	@Override
	public VmInfoList listVm() throws AvroRemoteException {
		log.debug("try to get listvm:");
        VmInfoList resp=proxy.listVm();
        log.debug("get listvm resp="+resp);
		return resp;
	}

	@Override
	public VmCtrlResponse destroyVm(VmCtrlRequest request)
			throws AvroRemoteException {
		VmCtrlResponse response=proxy.destroyVm(request);
		log.debug("destroy response:"+response);
		return response;
	}

	@Override
	public VmCtrlResponse startVm(VmCtrlRequest request)
			throws AvroRemoteException {
		VmCtrlResponse response=proxy.startVm(request);
		return response;
	}

	@Override
	public CharSequence getVmState(CharSequence vmName)
			throws AvroRemoteException {
		String state="";
		try {
			state=proxy.getVmState(vmName).toString();
		} catch (AvroRemoteException e) {
			e.printStackTrace();
		}
		return state;
	}

}
