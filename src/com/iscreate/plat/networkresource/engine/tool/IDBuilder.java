package com.iscreate.plat.networkresource.engine.tool;



import java.util.UUID;

public class IDBuilder {
	public static String randomId() {
		String id = UUID.randomUUID().toString().replace("-", "");
		return id;
	}
}
