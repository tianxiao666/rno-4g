package com.iscreate.op.service.rno.tool;

public class TranslateTools {
	public static byte[] reverse(byte[] buf, int len) {
		byte[] ano = new byte[len];
		for (int i = 0; i < len; i++) {
			ano[len - i - 1] = buf[i];
		}
		return ano;
	}

	public static int makeIntFromByteArray(byte[] buf, int offset, int len) {
		return TranslateTools.byte2Int(
				TranslateTools.subByte(buf, offset, len), len);
	}
	
	/**
	 * 
	 * @param b
	 * 最低位字节在0位置
	 * 最高位字节在b.length-1位置
	 * @param len
	 * @return
	 */
	public static String byte2hex(byte[] b, int len) {
		if (b.length < len) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		String tmp = "";
		for (int i = len-1; i >=0; i--) {
			tmp = Integer.toHexString(b[i] & 0XFF);
			if (tmp.length() == 1) {
				sb.append("0" + tmp);
			} else {
				sb.append(tmp);
			}

		}
		return sb.toString();
	}
	
	public static int byte2Int(byte[] b,int len){
		String s=byte2hex(b,len);
		return Integer.parseInt(s, 16);
	}
	
	public static long byte2Long(byte[] b,int len){
		String s=byte2hex(b,len);
		return Long.parseLong(s, 16);
	}
	
	/**
	 * 返回的字节数组，
	 * 低位在0位置
	 * 高位在length-1位置
	 * @param i
	 * @return
	 */
	public static byte[] intToByteArray(int i) {

		byte[] result = new byte[4];

		// 由高位到低位

		result[3] = (byte) ((i >> 24) & 0xFF);

		result[2] = (byte) ((i >> 16) & 0xFF);

		result[1] = (byte) ((i >> 8) & 0xFF);

		result[0] = (byte) (i & 0xFF);

		return result;

	}
	
	/**
	 * 
	 * @param bytes
	 * 低位在0位置
	 * 高位在length-1位置
	 * @return
	 */
	public static int byteArrayToInt(byte[] bytes) {

		int value = 0;

		// 由低位到高位

		for (int i = 0; i < 4; i++) {

			int shift = (i) * 8;

			value += (bytes[i] & 0x000000FF) << shift;// 往高位游
		}

		return value;

	}
	
	public static byte[] spliceByte(byte[] buf,int len){
		if(len<=0){
			return null;
		}
		byte[] ano=new byte[len>buf.length?buf.length:len];
		for(int i=0;i<len&&i<buf.length;i++){
			ano[i]=buf[i];
		}
		return ano;
	}
	
	/**
	 * 截取一段
	 * @param buf
	 * @param start
	 * @param len
	 * @return
	 */
	public static byte[] subByte(byte[] buf,int start,int len){
		if(buf==null){
			return null;
		}
		if(start<0){
			start=0;
		}else if(start>buf.length-1){
			start=buf.length-1;
		}
		
		if(len>(buf.length-start)){
			len=buf.length-start;
		}
		
		byte[] ano=new byte[len];
		for(int i=0;i<len;i++){
			ano[i]=buf[start+i];
		}
		return ano;
	}
	
	public static byte[] mergeByte(byte[] typeByte, byte[] lenByte,
			byte[] content) {
		int len1 = typeByte.length;
		int len2 = lenByte.length;
		int len3 = content.length;
		byte[] whole = new byte[len1 + len2 + len3];
		whole[0] = typeByte[0];
		whole[1] = lenByte[0];
		whole[2] = lenByte[1];
		int start = 3;
		for (int i = 0; i < len3; i++) {
			whole[start + i] = content[i];
		}
		return whole;
	}
	
	public static void main(String[] args){
		byte[] buf=new byte[5];
		buf[0]=1;
		buf[1]=2;
		buf[2]=3;
		buf[3]=4;
		buf[4]=5;
		byte[] ano=subByte(buf,-1,5);
		for(int i=0;i<ano.length;i++){
			System.out.println(ano[i]);
		}
	}

	public static long makeLongFromByteArray(byte[] buf, int offset, int len) {
		return TranslateTools.byte2Long(
				TranslateTools.subByte(buf, offset, len), len);
	}
	
}
