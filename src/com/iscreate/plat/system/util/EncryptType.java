package com.iscreate.plat.system.util;

public enum EncryptType {
	md5("MD5",1);
	EncryptType(String name,int type){
		this.name=name;
	}
	private String name;
	private int type;
	public String getName(){
		return this.name;
	}
	public int getType(){
		return this.type;
	}
}
