package com.iscreate.op.service.publicinterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.iscreate.op.service.outlinking.OutLinkingService;
import com.iscreate.plat.tools.xmlhelper.XmlServiceImpl;

public class SystemConfigService {

	private static SystemConfigService instance;
	private static String code;
	private static Map<String,Map<String,String>> mapUrl;
	private static File mappingFile;


	// 单例模式中获取唯一的实例
	public static SystemConfigService getInstance() {
		if (null == instance) {
			instance = new SystemConfigService();
		}
		return instance;
	}
	
	public void init(){
		OutLinkingService outLinking = new OutLinkingService();
		this.mapUrl = outLinking.getAllUrl();
		this.code = outLinking.getEnviromentCode();
//		System.out.println(this.mapUrl);
//		System.out.println(this.code);
		//saveMemory();
		//readFile();
		//mappingFile();
	}

	public void readFile(){
		try {
			RandomAccessFile raFile = new RandomAccessFile(this.mappingFile, "r");
			raFile.seek(0);
			String str = "";
			System.out.println("读取========================================");
			while(((str = raFile.readLine()) != null)){
//				byte[] b_utf8 = str.getBytes("GBK");
//				String str1 = new String(b_utf8,"GBK");
				str = new String(str.getBytes("8859_1"),"gb2312");
				System.out.println(str.trim());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void saveMemory(){
		try {
//			System.out.println(mappingFile);
			if(this.mappingFile ==null || !this.mappingFile.exists()){
				this.mappingFile = File.createTempFile("SystemConf", ".xml");
			}
			System.out.println(mappingFile);
			String path = SystemConfigService.class.getClassLoader().getResource("/").getPath();
			path += "\\temp";
			File file = new File(path);
			File[] fileList = file.listFiles();
			if(fileList!=null){
				RandomAccessFile raFile = new RandomAccessFile(this.mappingFile, "rw");
				for (File f : fileList) {
					InputStreamReader read = new InputStreamReader(new FileInputStream(f),"utf-8");    
					BufferedReader bufferedReader = new BufferedReader(read);    
					String lineXML = null;    
					while ((lineXML = bufferedReader.readLine()) != null) {
						//System.out.println(lineXML);
						raFile.write(lineXML.trim().getBytes());
						raFile.writeUTF("\r\n");
					}
					read.close();
				}
				raFile.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/*
		String path = SystemConfigService.class.getClassLoader().getResource("/").getPath();
		path += "\\mapping";
		File file = new File(path);
		//获取改目录下的所有文件
		fileList = file.listFiles();
		mappingFile(fileList[0]);
		*/
		/*
		if(fileList!=null){
			for (File f : fileList) {
				//获取根节点
				Element root = this.getRootElement(f.getAbsolutePath());
				NodeList childNodes = root.getChildNodes();
				if(childNodes.getLength()>0){
					for(int i=0;i < childNodes.getLength();i++){
						Node item = childNodes.item(i);
						if (item != null && item.getNodeType() == Node.ELEMENT_NODE) {
							 if (item != null && item.getNodeType() == Node.ELEMENT_NODE) {
							 NodeList childNodes2 = item.getChildNodes();
							 	if(childNodes2 == null)continue;
								for(int j = 0 ; j< childNodes2.getLength(); j++){
									Node item2 = childNodes2.item(j);
									if (item2 != null && item2.getNodeType() == Node.ELEMENT_NODE) {  
										NodeList childNodes3 = item2.getChildNodes();
										if(childNodes3 == null)continue;
										for(int k = 0 ; k< childNodes3.getLength(); k++){
											Node item3 = childNodes3.item(k);
											if (item3 != null && item3.getNodeType() == Node.ELEMENT_NODE) {
												NamedNodeMap attributes = item3.getAttributes();
											}
										}
									}
								}
							 }
						 }
					}
				}
			}
		}
		*/
	}
	
	

	public void mappingFile(File f){
		try {
			File file = new File("employee.xml");
			  if(!file.exists()){
			   file.createTempFile("employee", ".xml");
			  }

			RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
			//获取根节点
			Element root = this.getRootElement(f.getAbsolutePath());
			System.out.println(root.getTagName());
			NodeList childNodes = root.getChildNodes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		try{
//			File tempFile = File.createTempFile ("mmaptest", null);  
//			RandomAccessFile file = new RandomAccessFile (tempFile, "rw");  
//			FileChannel channel = file.getChannel();  
//			ByteBuffer temp = ByteBuffer.allocate (100);  
//			 // put something in the file, starting at location 0  
//			 temp.put ("This is the file content".getBytes());  
//			 temp.flip();  
//			 channel.write (temp, 0);
//			 // Put something else in the file, starting at location 8192  
//			 // 8192 is 8k, almost certainly a different memory/FS page.  
//			 // This may cause a file hole, depending on the  
//			 // filesystem page size.  
//			 temp.clear();  
//			 temp.put ("This is more file content".getBytes());  
//			 temp.flip();  
//			 channel.write (temp, 8192);  
//			 // create three types of mappings to the same file  
//			 MappedByteBuffer ro = channel.map (FileChannel.MapMode.READ_ONLY, 0, channel.size());  
//			 MappedByteBuffer rw = channel.map (FileChannel.MapMode.READ_WRITE, 0, channel.size());  
//			 MappedByteBuffer cow = channel.map (FileChannel.MapMode.PRIVATE, 0, channel.size());  
//			 // The buffer states before any modifications  
//			 System.out.println ("Begin");  
//			 showBuffers (ro, rw, cow);
//			 // modify the Copy-On-Write buffer  
//			 cow.position (8);  
//			 cow.put ("COW".getBytes());  
//			 System.out.println ("Change to COW buffer");  
//			 showBuffers (ro, rw, cow);  
//			 // Modify the Read/Write buffer  
//			 rw.position (9);  
//			 rw.put (" R/W ".getBytes());  
//			 rw.position (8194);  
//			 rw.put (" R/W ".getBytes());  
//			 rw.force();  
//			 System.out.println ("Change to R/W buffer");  
//			 showBuffers (ro, rw, cow);  
//			 // Write to the file through the channel, hit both pages  
//			 temp.clear();  
//			 temp.put ("Channel write ".getBytes());  
//			 temp.flip();  
//			 channel.write (temp, 0);
//			 temp.rewind();  
//			 channel.write (temp, 8202);
//			 System.out.println ("Write on channel");  
//			 showBuffers (ro, rw, cow);  
//			 // modify the Copy-On-Write buffer again  
//			 cow.position (8207);  
//			 cow.put (" COW2 ".getBytes());  
//			 System.out.println ("Second change to COW buffer");  
//			 showBuffers (ro, rw, cow);  
//			 // Modify the Read/Write buffer  
//			 rw.position (0);  
//			 rw.put (" R/W2 ".getBytes());  
//			 rw.position (8210);  
//			 rw.put (" R/W2 ".getBytes());  
//			 rw.force();  
//			 System.out.println ("Second change to R/W buffer");  
//			 showBuffers (ro, rw, cow);  
//			 // cleanup  
//			channel.close();
//			file.close(); 
//			tempFile.delete();  
//		}catch(Exception e){
//			e.printStackTrace();
//		}
	}
	
	private void showBuffers (ByteBuffer ro, ByteBuffer rw, ByteBuffer cow) {  
		dumpBuffer ("R/O", ro);  
		dumpBuffer ("R/W", rw);  
		dumpBuffer ("COW", cow);  
		System.out.println ("");  
	}  

	private void dumpBuffer (String prefix, ByteBuffer buffer) {  
		 System.out.print (prefix + ": '");  
		 int nulls = 0;  
		 int limit = buffer.limit();  
		 for (int i = 0; i < limit; i++) {  
			 char c = (char) buffer.get (i);  
			 if (c == '\u0000') {  
				 nulls++;  
				 continue;  
			 }
			 if (nulls != 0) {  
				 System.out.print ("|[" + nulls  
						 + " nulls]|");  
				 nulls = 0;  
			 }
			 System.out.print (c);  
		 }  
		 System.out.println ("'");  
	}  

	
	public Map<String,Map<String,String>> saveXmlToMemoryByPath(String filePath){
		
		return null;
	}
	
	/**
	 * 获取指定xml路径的根节点
	 * 
	 * @param filePath
	 * @return
	 */
	public Element getRootElement(String filePath) {
		Element root = null;
		try {
			File file = new File(filePath);
			FileInputStream is = new FileInputStream(file);
			//File file = new File(filePath);
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(is);
			root = dom.getDocumentElement();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return root;
	}
	
	public static String getCode() {
		return code;
	}

	public static Map<String, Map<String, String>> getMapUrl() {
		return mapUrl;
	}


	public static void main(String[] args) {
//		SystemConfigService.saveMemory();
//		SystemConfigService s = new SystemConfigService();
//		s.saveMemory();
		
	}
	
}
