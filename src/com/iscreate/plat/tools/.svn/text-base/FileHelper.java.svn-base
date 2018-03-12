/**
 * @Title:SaveFileHelper.java
 * @Package:com.iscreate.util
 * @Description:TODO
 * @author:sunny
 * @date 2011-6-24下午03:02:25 
 * @Version 
 */
package com.iscreate.plat.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionContext;

/**
 * @title SaveFileHelper.java
 * @description 存储文件
 * @author sunny
 * @date 2011-6-24 下午03:02:25
 * 
 */
public class FileHelper {

	private static final int BUFFER_SIZE = 1024;

	private static Logger logger = Logger.getLogger(FileHelper.class);
	
	/**
	 * 生成二级目录的文件上传
	 * @title SaveFileHelper.java
	 * @description 保存临时文件，返回存储的文件的路径
	 * @param upload
	 * @param uploadFileName
	 * @return 保存后的文件名，包括路径
	 * @throws UnsupportedEncodingException
	 * @author sunny
	 * @date 2011-6-24 下午03:31:29
	 */
	public static List<String> saveFiles(String prefixPath,File[] upload, String[] uploadFileName) {

		List<String> filepaths = new ArrayList<String>();
		File[] srcFiles = upload;
		List<String> successFileList = new ArrayList<String>();
		// 获取保存目录的路径
//		ActionContext ctx = ActionContext.getContext();
//		HttpServletRequest request = (HttpServletRequest) ctx
//				.get(StrutsStatics.HTTP_REQUEST);
		// String saveDir =
		// request.getSession().getServletContext().getRealPath(
		// "/upload");
//		String saveDirRoot = request.getSession().getServletContext()
//				.getRealPath("/upload");
		logger.debug("共有 " + srcFiles.length + " 个文件");
		// 处理每个要上传的文件
		for (int i = 0; i < srcFiles.length; i++) {
			String saveDir = prefixPath;
			// 获取uuid，以uuid作为文件名
			String uuid = UUID.randomUUID().toString();
			uuid = uuid.replace("-", "").toUpperCase();

			logger.debug("处理文件：" + uploadFileName[i]);
			// 后缀名
			String subfix = uploadFileName[i].substring(uploadFileName[i]
					.lastIndexOf(".") + 1);
			// 		   	
			// 根据服务器的文件保存地址和原文件名创建目录文件全路径
			String cordir = prefixPath;//
			String dirtips = uuid.substring(0, 2);
			for (int j = 0; j < dirtips.length(); j++) {// 取uuid前面两位，各创建一级文件夹
				char c = dirtips.charAt(j);
				saveDir += "/" + c;
				cordir += "/" + c;
			}
			// 创建文件夹

			File f = new File(saveDir);
			logger.debug("save dir is:"+saveDir);
			
			if (!f.exists()) {// 路径不存在
				logger.debug("try to create save dir!");
				boolean result = f.mkdirs();// 创建文件夹以及上级文件夹
				if (!result) {
					logger.error("fail to create folder :" + saveDir);
					continue;
				}
			}else{
				logger.debug("save dir exists!");
			}
			 

			String dstPath = saveDir + "/" + uuid + "." + subfix;
			cordir += "/" + uuid + "." + subfix;// 用于返回的相对路径
			filepaths.add(cordir);// 记录保存路径
			logger.debug("save path:" + dstPath);
			logger.debug("return filepath:" + cordir);
			File dstFile = new File(saveDir, uuid + "." + subfix);
			if (copy(srcFiles[i], dstFile)) {// 从临时文件夹拷到指定文件夹
				successFileList.add(uploadFileName[i]);
			}
		}
		logger.debug("成功处理 " + successFileList.size() + "个文件");
		return filepaths;
	}

	/**
	 * 指定路径的文件上传
	 * @title SaveFileHelper.java
	 * @description 保存临时文件，返回存储的文件的路径
	 * @param upload
	 * @param uploadFileName
	 * @return 保存后的文件名，包括路径
	 * @throws UnsupportedEncodingException
	 * @author sunny
	 * @date 2011-6-24 下午03:31:29
	 */
	public static List<String> saveFileByPath(String prefixPath,File[] upload, String[] uploadFileName){
		List<String> filepaths = new ArrayList<String>();
		File[] srcFiles = upload;
		List<String> successFileList = new ArrayList<String>();
		logger.debug("共有 " + srcFiles.length + " 个文件");
		// 处理每个要上传的文件
		for (int i = 0; i < srcFiles.length; i++) {
			
			// 创建文件夹
			File f = new File(prefixPath);
			
			if (!f.exists()) {// 路径不存在
				logger.debug("try to create save dir!");
				boolean result = f.mkdirs();// 创建文件夹以及上级文件夹
				if (!result) {
					logger.error("fail to create folder :" + prefixPath);
					continue;
				}
			}else{
				logger.debug("save dir exists!");
			}
			String dstPath = prefixPath + "/" + uploadFileName[i];
			File dstFile = new File(dstPath);
			if (copy(srcFiles[i], dstFile)) {// 从临时文件夹拷到指定文件夹
				successFileList.add(uploadFileName[i]);
				filepaths.add(dstPath);
			}
		}
		logger.debug("成功处理 " + successFileList.size() + "个文件");
		return filepaths;
	}
	
	/**
	 * 
	 * @title SaveFileHelper.java
	 * @description 保存到源文件到目标路径
	 * @param src
	 * @param dst
	 * @return
	 * @author sunny
	 * @date 2011-6-24 下午03:31:05
	 */
	private static boolean copy(File src, File dst) {
		boolean result = false;
		InputStream in = null;
		OutputStream out = null;
		
		String dstPath=dst.getAbsolutePath();
		String dstdir=dstPath.substring(0, dstPath.lastIndexOf(File.separator));
		//目标路径
		File parDir=new File(dstdir);
		logger.debug("destination directory is :"+dstdir);
        //目标路径不存在
		if(!parDir.exists()){
			logger.debug("destination directory does'nt exist,create it.");
			parDir.mkdirs();
		}
		try {
			in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
			out = new BufferedOutputStream(new FileOutputStream(dst),
					BUFFER_SIZE);
			byte[] buffer = new byte[BUFFER_SIZE];
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * @title SaveFileHelper.java
	 * @description 删除指定的文件
	 * @param filepaths
	 * @author sunny
	 * @date 2011-6-24 下午03:30:44
	 */
	public static void deleteFiles(List<String> filePaths) {
		if (filePaths == null || filePaths.size() == 0) {
			return;
		} else {
			ActionContext ctx = ActionContext.getContext();
			HttpServletRequest request = (HttpServletRequest) ctx
					.get(StrutsStatics.HTTP_REQUEST);
			String dirRoot = request.getSession().getServletContext()
					.getRealPath("/upload");
			for (String filePath : filePaths) {
				String realPath = dirRoot + "/" + filePath;
				File f = new File(dirRoot, filePath);
				if (!f.isFile()) {// 不是文件则不处理
					continue;
				}
				f.delete();// 删除该文件
			}
		}
	}

	/**
	 * 更新文件
	 * 
	 * @title FileHelper.java
	 * @description TODO
	 * @param upload
	 *            放于临时文件夹下的上传的文件
	 * @param filePath
	 *            更新文件需要放置的相对路径
	 * @param uploadFileName
	 *            上传的文件的原名称
	 * @param oldFileName
	 *            需要被更新的文件名称，只有名称和扩展名
	 * @return
	 * @author sunny
	 * @date 2011-7-5 下午08:12:37
	 */
	public static boolean overWrite(File upload, String filePath,
			String uploadFileName, String oldFileName) {
		boolean result = false;
		String filepaths = "";
		File srcFiles = upload;
		// 获取保存目录的路径
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ctx
				.get(StrutsStatics.HTTP_REQUEST);
		String saveDirRoot = request.getSession().getServletContext()
				.getRealPath("/upload");
		// 处理每个要上传的文件

		String saveDir = saveDirRoot + "/" + filePath;
		logger.debug("-->save dir is:" + saveDir);
		// 后缀名
		String subfix = uploadFileName.substring(uploadFileName
				.lastIndexOf(".") + 1);

		// uuid名称
		String uuidName = oldFileName.substring(0, oldFileName.indexOf("."));

		// 删除旧文件
		File oldFile = new File(saveDir, oldFileName);
		logger.debug("-->old file is:" + oldFile);
		if (oldFile.exists()) {
			if (oldFile.delete()) {
				logger.debug("delete old file:" + oldFileName
						+ " successfully!");
			} else {
				logger.error("fail to delete old file:" + oldFileName
						+ ",will exit from this function!");
				return false;
			}
		}
		// 新文件
		String dstPath = saveDir + "/" + uuidName + "." + subfix;
		logger.debug("-->dstPath is:" + dstPath);
		File dstFile = new File(saveDir, uuidName + "." + subfix);
		if (copy(srcFiles, dstFile)) {// 从临时文件夹拷到指定文件夹
			result = true;
		}
		return result;
	}
}
