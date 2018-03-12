package com.iscreate.plat.exceptioninteceptor.service;

import java.io.InputStreamReader;
import java.io.LineNumberReader;


public class ExceptionHandlerService {
	
	private static final int CPUTIME = 30;

	private static final int PERCENT = 100;

	private static final int FAULTLENGTH = 10;
	
	/**
	 * 异常处理实例(判断异常类型)
	 * @param exName
	 * @param ex
	 * @return
	 */
	public String exceptionHandler(String exName,Exception ex){
		String result = "";
		if("com.iscreate.plat.exceptioninteceptor.service.UserDefinedException".equals(exName)){
			result = userDefinedExMsgService(exName,ex);
		}else{
			result = systemExMsgService(exName,ex);
		}
		return result;
	}
	
	//内部通用方法===========================================================
	/**
	 * 自定义异常Service
	 * @param exName
	 * @param ex
	 * @return
	 */
	private String userDefinedExMsgService(String exName,Exception ex){
		String msg = printExMsg(exName,ex);
		return msg;
	}
	
	/**
	 * 系统异常Service
	 * @param exName
	 * @param ex
	 * @return
	 */
	private String systemExMsgService(String exName,Exception ex){
		String msg = printExMsg(exName,ex);
		return msg;
	}
	
	/**
	 * 拼接异常信息
	 * @param exName
	 * @param ex
	 * @return
	 */
	private String printExMsg(String exName,Exception ex){
		int i=0;
		String msg = "";
		String msgTitle = "";
		String msgDetail = ex.getMessage();
		if(msgDetail!=null || "".equals(msgDetail)){
			msgDetail = " : "+msgDetail;
		}else{
			msgDetail = "";
		}
		msgTitle = "<p>"+exName + msgDetail + "</p>";
		StackTraceElement[] stackTrace = ex.getStackTrace();
		for (StackTraceElement ste : stackTrace) {
			//PC端显示
			msg += "<p>"+ste+"</p>";
			if(ste.toString().indexOf("ActionForMobile")>-1){
				i = 1;
				break;
			}
		}
		if(i==1){
			msg = "";
			//终端显示
			for (StackTraceElement ste : stackTrace) {
				if(ste.toString().indexOf("iscreate")>-1){
					msg += "<p>"+ste+"</p>";
				}
			}
		}
		msg = msgTitle + msg;
		return msg;
	}
	
	/**
	 * 获得CPU使用率.
	 * 
	 * @return 返回cpu使用率
	 */
	private double getCpuRatioForWindows() {
		try {
			String procCmd = System.getenv("windir")
					+ "\\system32\\wbem\\wmic.exe process get Caption,CommandLine,"
					+ "KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
			// 取进程信息
			long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));
			Thread.sleep(CPUTIME);
			long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));
			if (c0 != null && c1 != null) {
				long idletime = c1[0] - c0[0];
				long busytime = c1[1] - c0[1];
				return Double.valueOf(
						PERCENT * (busytime) / (busytime + idletime))
						.doubleValue();
			} else {
				return 0.0;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0.0;
		}
	}

	/**
	 * 
	 * 读取CPU信息.
	 * 
	 * @param proc
	 */
	private long[] readCpu(final Process proc) {
		long[] retn = new long[2];
		Bytes bytes = new Bytes();
		try {
			proc.getOutputStream().close();
			InputStreamReader ir = new InputStreamReader(proc.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			String line = input.readLine();
			if (line == null || line.length() < FAULTLENGTH) {
				return null;
			}
			int capidx = line.indexOf("Caption");
			int cmdidx = line.indexOf("CommandLine");
			int rocidx = line.indexOf("ReadOperationCount");
			int umtidx = line.indexOf("UserModeTime");
			int kmtidx = line.indexOf("KernelModeTime");
			int wocidx = line.indexOf("WriteOperationCount");
			long idletime = 0;
			long kneltime = 0;
			long usertime = 0;
			while ((line = input.readLine()) != null) {
				if (line.length() < wocidx) {
					continue;
				}
				// 字段出现顺序：Caption,CommandLine,KernelModeTime,ReadOperationCount,
				// ThreadCount,UserModeTime,WriteOperation
				String caption = bytes.substring(line, capidx, cmdidx - 1)
						.trim();
				String cmd = bytes.substring(line, cmdidx, kmtidx - 1).trim();
				if (cmd.indexOf("wmic.exe") >= 0) {
					continue;
				}
				// log.info("line="+line);
				if (caption.equals("System Idle Process")
						|| caption.equals("System")) {
					idletime += Long.valueOf(
							bytes.substring(line, kmtidx, rocidx - 1).trim())
							.longValue();
					idletime += Long.valueOf(
							bytes.substring(line, umtidx, wocidx - 1).trim())
							.longValue();
					continue;
				}

				kneltime += Long.valueOf(
						bytes.substring(line, kmtidx, rocidx - 1).trim())
						.longValue();
				usertime += Long.valueOf(
						bytes.substring(line, umtidx, wocidx - 1).trim())
						.longValue();
			}
			retn[0] = idletime;
			retn[1] = kneltime + usertime;
			return retn;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				proc.getInputStream().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	class Bytes {
		public String substring(String src, int start_idx, int end_idx) {
			byte[] b = src.getBytes();
			String tgt = "";
			for (int i = start_idx; i <= end_idx; i++) {
				tgt += (char) b[i];
			}
			return tgt;
		}
	}
}
