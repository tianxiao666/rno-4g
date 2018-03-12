package com.iscreate.plat.system.util;

import java.security.MessageDigest;

public class Md5PasswordEncoder implements AuthorityPasswordEncoder {
	
	/**
	 * 获取加密器的类型
	 * @return
	 */
	public int getType(){
		return EncryptType.md5.getType();
	}
	
	public String encodePassword(String password) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = password.getBytes("utf-8");
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static void main(String[] args) {
		Md5PasswordEncoder en=new Md5PasswordEncoder();
		System.out.println(en.encodePassword("123456"));
	}
}


