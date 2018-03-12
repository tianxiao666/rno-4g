package com.iscreate.plat.mobile.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import sun.misc.BASE64Decoder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.iscreate.plat.mobile.pojo.MobilePackage;


public class MobilePackageUtil {
	private  static final  Log log = LogFactory.getLog(MobilePackageUtil.class);
	public static MobilePackage getMobilePackage()
	{
		MobilePackage mobilePackage =null;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession(true);
		String sessionId = session.getId();
		
		Gson gson = new Gson();
		
		String jsonStr = request.getParameter("jsonArrayObj"); // 获取请求参数
		String fileStr = request.getParameter("fileData"); // 获取请求参数
		
		if(jsonStr!=null && !jsonStr.trim().equals(""))
		{
			try {
				jsonStr=URLDecoder.decode(jsonStr,"UTF-8");
				mobilePackage = gson.fromJson(jsonStr,new TypeToken<MobilePackage>(){}.getType());
				if(fileStr!=null&&!"".equals(fileStr)){
					mobilePackage.setFileData(fileStr);
				}
				
			} catch (Exception e) {
				mobilePackage = null;
			}
		}
		
		return mobilePackage;
	}
	
	/**
	 * 将 mobilePackage 和结果传递至终端
	 * @param mobilePackage
	 * @param result
	 * @throws IOException 
	 */
	public static void responseMobilePacageAndResult(MobilePackage mobilePackage) throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");

		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		// 返回content的JSON字符串信息
		String resultPackageJsonStr = gson.toJson(mobilePackage);
		response.getWriter().write(resultPackageJsonStr);
	}
	
	/**
	 * 复制图片(从终端获取文件数据)
	 * @param fileData 终端获取的文件数据
	 * @param targetPath 目标文件保存路径
	 */
	public static String copyPhoto(String prefixPath,String suffixPath,String fileData) {
		BASE64Decoder base64 = new BASE64Decoder();
		try {
			byte[] decodeBuffer = base64.decodeBuffer(fileData);
			// 获取uuid，以uuid作为文件名
			String uuid = UUID.randomUUID().toString();
			uuid = uuid.replace("-", "").toUpperCase();

			// 根据服务器的文件保存地址和原文件名创建目录文件全路径
			String cordir = prefixPath;//
			String dirtips = uuid.substring(0, 2);
			for (int j = 0; j < dirtips.length(); j++) {// 取uuid前面两位，各创建一级文件夹
				char c = dirtips.charAt(j);
				suffixPath += "/" + c;
				cordir += "/" + c;
			}
			
			//保存路径
			String saveDir = prefixPath+suffixPath;
			
			// 创建文件夹
			File f = new File(saveDir);
			log.debug("save dir is:"+saveDir);
			if (!f.exists()) {// 路径不存在
				log.debug("try to create save dir!");
				boolean result = f.mkdirs();// 创建文件夹以及上级文件夹
				if (!result) {
					log.error("fail to create folder :" + saveDir);
					return "";
				}
			}else{
				log.debug("save dir exists!");
			}
			File dstFile = new File(saveDir, uuid+".jpg");
			
			copy(decodeBuffer,dstFile);
			
			return suffixPath+"/"+uuid+".jpg";
		} catch (IOException e) {
			log.error("fail to save picture " );
			return "";
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
