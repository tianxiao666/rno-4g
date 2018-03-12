package com.iscreate.plat.mobile.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import sun.misc.BASE64Decoder;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class MobileContentHelper {
	//表单非文件上传提交的JSON数据封装成的Map
	private Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
	//表单文件上传提交的JSON数据封装成的Map
	private Map<String, Map<String, String>> fileDataMap = new HashMap<String, Map<String, String>>();

	public MobileContentHelper() {
	}

	private Map<String, Map<String, String>> setInitData(String initData) {
		Gson gson = new Gson();
		Map<String, Map<String, String>> jsonMap = null;
		if (initData != null) {
			try {
				jsonMap = gson.fromJson(initData,
						new TypeToken<Map<String, Map<String, String>>>() {
						}.getType());
			} catch (JsonParseException e) {
				jsonMap = new HashMap<String, Map<String, String>>();
			}
		}
		return jsonMap;
	}
	//初始化formData
	public void setContent(String content) {
		this.map = setInitData(content);
	}
	//初始化fileData
	public void setFileData(String fileData) {
		this.fileDataMap = setInitData(fileData);
	}

	/**
	 * 根据key获取组数据
	 * 
	 * @param key
	 * @return
	 */
	public Map<String, String> getGroupByKey(String key) {
		//传入的是文件上传的key，返回文件上传的JSON数据Map
		if("fileDataRequest".equals(key)) {
			if(this.fileDataMap != null) {
				return this.fileDataMap.get(key);
			} else {
				return null;
			}
		}
		//其他情况，返回非文件上传的表单JSON数据Map
		return this.map.get(key);
	}

	/**
	 * 添加组数据
	 * 
	 * @param key
	 * @param m
	 */
	public void addGroup(String key, Map<String, String> m) {
		this.map.put(key, m);
	}

	/**
	 * 将map转换成json
	 * 
	 * @return
	 */
	public String mapToJson() {
		return new Gson().toJson(this.map);
	}
	
	/**
	 * 复制图片(从终端获取文件数据)
	 * @param fileData 终端获取的文件数据
	 * @param targetPath 目标文件保存路径
	 */
	public void copyPhoto(String fileData, String targetPath) {
		BASE64Decoder base64 = new BASE64Decoder();
		try {
			byte[] decodeBuffer = base64.decodeBuffer(fileData);
			copy(decodeBuffer, new File(targetPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 复制文件
	 * @param buffer 文件的byte数组
	 * @param dst 目标文件
	 */
	private static void copy(byte[] buffer, File dst) {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(dst));
            out.write(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
