package com.iscreate.op.service.rno.job.vmhostctrl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExecuteShell {

	public static List<String> simpleExecute(List<String> cmds) {

		List<String> results = new ArrayList<String>();
		ProcessBuilder builder = new ProcessBuilder(cmds);
		builder.redirectErrorStream(true);
		try {
			Process process = builder.start();
			int ret = process.waitFor();

			System.out.println("ret=" + ret + ",exitValue="
					+ process.exitValue());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return results;
	}

	
	public static void executeShellScript(String scriptFile) throws IOException, InterruptedException{
		ProcessBuilder builder = new ProcessBuilder("bash", scriptFile);
		builder.redirectErrorStream(true);
		Process process = builder.start();
	}
	
	/**
	 * 给点执行脚本
	 * 
	 * @param scriptFile
	 * @param outFilePath
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static List<String> execute(List<String> cmds)
			throws IOException, InterruptedException {

		//随机生成一个文件存储结果
		String outFilePath = "output-"
				+ UUID.randomUUID().toString().replaceAll("-", "");
		
		String scriptFileName = "script-" + outFilePath;
		File scriptFile = new File(scriptFileName);
		FileOutputStream fos = new FileOutputStream(scriptFile);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		bw.write("#!/bin/bash");
		bw.newLine();
		for (String cmd : cmds) {
			bw.write(cmd + " ");
		}
		bw.write("1>" + outFilePath + " 2>" + outFilePath);
		bw.close();

		// System.out.println("文件：["+scriptFileName+"]是否存在？"+scriptFile.exists());
		List<String> results = new ArrayList<String>();
		ProcessBuilder builder = new ProcessBuilder("bash", scriptFileName);
		builder.redirectErrorStream(true);
		Process process = builder.start();
		int ret = process.waitFor();

//		System.out.println("ret=" + ret + ",exitValue=" + process.exitValue());

		// 读取结果文件
		File outFile=new File(outFilePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(outFile)));
		String line = "";
		while ((line = br.readLine()) != null) {
			results.add(line);
		}
		br.close();
		
		//干掉临时脚本文件，结果文件
		scriptFile.delete();
		outFile.delete();
		return results;
	}

	public static void main(String[] args) {
		List<String> cmds = new ArrayList<String>();
		if (args != null) {
			System.out.println("args.size=" + args.length);
			for (String ar : args) {
				System.out.println("args:" + ar);
				cmds.add(ar);
			}
		} else {
			cmds.add("ls");
			System.out.println("args null");
		}

		List<String> results=null;
		try {
			results = ExecuteShell.execute(cmds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("----get results:");
		System.out.println(results);
	}
}
