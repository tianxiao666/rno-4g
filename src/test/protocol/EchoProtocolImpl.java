package test.protocol;

import java.io.IOException;

import org.apache.hadoop.ipc.ProtocolSignature;

public class EchoProtocolImpl implements EchoProtocol {

	@Override
	public long getProtocolVersion(String protocol, long clientVersion)
			throws IOException {
		return EchoProtocol.versionID;
	}

	@Override
	public ProtocolSignature getProtocolSignature(String protocol,
			long clientVersion, int clientMethodsHash) throws IOException {
		return ProtocolSignature.getProtocolSignature(this, protocol, clientVersion, clientMethodsHash);
	}

	@Override
	public String echo(String msg) {
		System.out.println("send back what ever received from client ");
		return msg;
	}

}
