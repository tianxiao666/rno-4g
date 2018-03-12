package test.protocol;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.ipc.ProtocolSignature;

public class PrintProtocolImpl implements PrintProtocol {
	public PrintProtocolImpl() {
//		try {
			// RPC.Builder builder= new RPC.Builder(conf);
//			builder.setProtocol(PrintProtocolImpl.class);
//			builder.setInstance(this);
//			builder.setPort(8888);
//			builder.setBindAddress("localhost");
//			server = builder.build();
//			server.addProtocol(RpcKind.RPC_BUILTIN, PrintProtocolImpl.class, this);
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public Text println(Text t) {
		System.out.println(t);
		return new Text("finish");
	}

	@Override
	public ProtocolSignature getProtocolSignature(String protocol,
			long clientVersion, int clientMethodsHash) throws IOException {
		return ProtocolSignature.getProtocolSignature(this, protocol,
				clientVersion, clientMethodsHash);
	}

	@Override
	public long getProtocolVersion(String protocol, long clientVersion)
			throws IOException {
		return PrintProtocol.versionID;
	}

}
