package com.iscreate.op.service.rno.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.selectors.FileSelector;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
public class ZipFileHandler {
    private static final int buffer = 1024;
 
    private static void createDirectory(String directory, String subDirectory) {
        String dir[];
        File fl = new File(directory);
        try {
            if (subDirectory == "" && fl.exists() != true) {
                fl.mkdir();
            } else if (subDirectory != "") {
                dir = subDirectory.replace('\\', '/').split("/");
                for (int i = 0; i < dir.length; i++) {
                    File subFile = new File(directory + File.separator + dir[i]);
                    if (subFile.exists() == false) {
                        subFile.mkdir();
                    }
                    directory += File.separator + dir[i];
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
 
    @SuppressWarnings("unchecked")
    public static boolean unZip(String zipFilePath, String outputDirectory) {
        boolean flag = false;
        try {
            ZipFile zipFile = new ZipFile(zipFilePath,"gbk");
            Enumeration e = zipFile.getEntries();
            ZipEntry zipEntry = null;
            createDirectory(outputDirectory, "");
            while (e.hasMoreElements()) {
                zipEntry = (ZipEntry) e.nextElement();
//              System.out.println("unzip " + zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    String name = new String(zipEntry.getName().getBytes("gbk"),"utf-8");
//                    System.out.println("dir="+zipEntry.getName()+"--"+name);
                    name = name.substring(0, name.length() - 1);
                    File f = new File(outputDirectory + File.separator + name);
                    if (!f.exists()) {
                        f.mkdir();
                    }
 
                } else {
                    String fileName = new String(zipEntry.getName());
                    fileName = fileName.replace('\\', '/');
                    if (fileName.indexOf("/") != -1) {
                        createDirectory(outputDirectory, fileName.substring(0,
                                fileName.lastIndexOf("/")));
                        fileName = fileName
                                .substring(fileName.lastIndexOf("/") + 1);
                    }
                    /*File f = new File(outputDirectory + File.separator
                            + zipEntry.getName());*/
                  
                    File f = new File(outputDirectory + File.separator
                            + new String(zipEntry.getName()));
                    f.createNewFile();
                    InputStream in = zipFile.getInputStream(zipEntry);
                    FileOutputStream out = new FileOutputStream(f);
                    byte[] by = new byte[buffer];
                    int c;
                    while ((c = in.read(by)) != -1) {
                        out.write(by, 0, c);
                    }
                    in.close();
                    out.close();
                }
            }
            flag = true;
//            System.out.println("unzip finished");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }       
        return flag;
    }
 
    
    
    public static boolean zip(String srcDirName,String excludes, String zipFilePath) {
        boolean flag = false;
        try {
            File srcdir = new File(srcDirName);
            if (!srcdir.exists())
                throw new RuntimeException(srcDirName + " is not exist!");
 
            Project prj = new Project();
            Zip zip_ = new Zip();
            zip_.setProject(prj);
            zip_.setDestFile(new File(zipFilePath));
 
            FileSet fileSet = new FileSet();
            fileSet.setProject(prj);
            fileSet.setDir(srcdir);
            fileSet.setExcludes(excludes);
            zip_.addFileset(fileSet);
 
            zip_.execute();
            flag = true;
//            System.out.println("zip finished");
        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
        	ex.printStackTrace();
        	flag=false;
        }
        return flag;
    }
 
    public static void main(String[] args) {
        ZipFileHandler zh = new ZipFileHandler();
//        zh.unZip("d:/tmp/中文excel.zip", "d:/tmp/中文excel/");
        zh.zip("d:/tmp/中文excel","**/*5000.xlsx", "d:/tmp/中文excel压缩后.zip");
    }
 
    static class SuffixFileSelector implements FileSelector{

		@Override
		public boolean isSelected(File dir, String fileName, File handFile)
				throws BuildException {
			System.out.println("\r\n---begin----");
			System.out.println(dir.getAbsolutePath());
			System.out.println(fileName);
			System.out.println(handFile.getAbsolutePath());
			System.out.println("---end----\r\n");
			return false;
		}
    	
    }
}
