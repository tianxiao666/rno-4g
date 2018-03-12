package test.protocol;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.ipc.VersionedProtocol;

public interface PrintProtocol extends VersionedProtocol {
	public static final long versionID = 1L;
     public Text println(Text t);
 }
