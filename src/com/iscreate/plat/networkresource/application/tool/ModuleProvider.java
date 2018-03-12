package com.iscreate.plat.networkresource.application.tool;



import com.google.gson.Gson;

public class ModuleProvider {
	static XMLAEMLibrary lib;
	static Gson gson = new Gson();

	ModuleProvider(XMLAEMLibrary lib) {
		ModuleProvider.lib = lib;
	}

	public static ApplicationModule getModule(String moduleName) {
		ApplicationModule am = lib.getModule(moduleName);
		return am;
	}

	public static Gson getGson() {
		return gson;
	}
}
