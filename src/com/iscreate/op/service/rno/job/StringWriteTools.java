package com.iscreate.op.service.rno.job;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class StringWriteTools {

	public static void writeStr(DataOutput dop,String str) throws IOException{
		if(str==null||str.length()==0){
			dop.writeInt(0);
		}else{
			byte[] bs=str.getBytes("utf-8");
			dop.writeInt(bs.length);
			dop.write(bs);
		}
	}
	
	public static String readStr(DataInput dip) throws IOException{
		int cnt=dip.readInt();
		if(cnt==0){
			return null;
		}
		byte[] bs=new byte[cnt];
		dip.readFully(bs, 0, cnt);
		return new String(bs,"utf-8");
	}
}
