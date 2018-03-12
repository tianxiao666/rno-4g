package com.iscreate.op.action.database;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.hibernate.mapping.Map;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.iscreate.op.pojo.database.DataBaseConfig;




public final class DataBaseConfigAction  extends DefaultHandler implements ServletContextListener {  
    public void contextInitialized(ServletContextEvent event) {  
    	try{
    	    String path = DataBaseConfigAction.class.getClassLoader().getResource("").getPath();
    	    String source_file = "";//源配置文件
    	    String target_file = "";//需要修改配置文件
    	    source_file = path+"spring/datasource-appcontx.xml";
    	    target_file = path+"jbpm.hibernate.cfg.xml";
    	 
    	    if(!new File(source_file).exists()){
    	    	System.out.println("找不到源数据库配置文件!");
    	    }else if(!new File(target_file).exists()){
    	    	System.out.println("找不到jbpm数据库配置文件!");
    	    }else{
    	    	 DataBaseConfig  dataBase = getDataBaseInfo(source_file); //数据库配置信息实体类
    	    	
    	    	 if(writeTargetConfig(target_file,dataBase)){
    	    		 System.out.println("JBPM数据源配置更新成功!");
    	    	 }else{
    	    		 System.out.println("JBPM数据源配置更新失败!");
    	    	 }
    	    }
           
        } catch (Throwable e) { 
        	e.printStackTrace();
            // System.out.println("JBPM数据源信息更新失败!");           
        }  
    }

	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	} 
    public Node getNode(NodeList nodes ){
    	//找到数据库配置所在的node节点
    	 for(int i=0;i< nodes.getLength();i++){
         	Node result = nodes.item(i);
         	if(result.getNodeType() == Node.ELEMENT_NODE && result.getNodeName().equals("bean")){
         		NamedNodeMap resultAttrList =  result.getAttributes();//节点的属性信息
         		for(int j = 0; j < resultAttrList.getLength(); j++){
         			Node attr = resultAttrList.item(j);
         			if(attr.getNodeName().equals("id") && attr.getNodeValue().equals("oracleDataSource")){
         				return result;
         			}else{
         				continue;
         			}
         		}
         	}
    	 }	
    	 return null;   		   
    }
    
    /*
     * 获取源数据源配置信息
     */
    public DataBaseConfig getDataBaseInfo(String fileName) throws ParserConfigurationException, SAXException, IOException{
    	 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         DocumentBuilder db = factory.newDocumentBuilder();
         Document doc = db.parse(new File(fileName));
         
         Element elmtInfo = doc.getDocumentElement();
         NodeList nodes = elmtInfo.getChildNodes();
         Node result = getNode(nodes);//数据库配置信息所在的xml节点
    	
    	 NodeList  nodeList = result.getChildNodes();
    	 DataBaseConfig dataBase = new DataBaseConfig();
         for(int j = 0; j < nodeList.getLength(); j++){//解析数据库配置信息
            Node node = nodeList.item(j);
            if(node.getNodeType() == Node.ELEMENT_NODE &&node.getNodeName().equals("property")){
            	NamedNodeMap attrList =  node.getAttributes();
            	if(attrList.getLength() > 0){
            			String name = attrList.getNamedItem("name").getNodeValue();
            			String value = attrList.getNamedItem("value").getNodeValue();
        			    if(name.equals("driverClass") && !value.isEmpty()){
        			    	 dataBase.setDriver(value);
        			    }else if(name.equals("jdbcUrl") && !value.isEmpty()){
        			    	 dataBase.setJdbcUrl(value);
        			    }else if(name.equals("user") && !value.isEmpty()){
        			    	 dataBase.setUser(value);
        			    }else if(name.equals("password") && !value.isEmpty()){
        			    	 dataBase.setPassword(value);
        			    }else if(name.equals("maxPoolSize") && !value.isEmpty()){
        			    	 dataBase.setMaxPoolSize(value);
        			    }else if(name.equals("minPoolSize") && !value.isEmpty()){
        			    	 dataBase.setMinPoolSize(value);
        			    }else if(name.equals("initialPoolSize") && !value.isEmpty()){
        			    	 dataBase.setInitialPoolSize(value);
        			    }else if(name.equals("maxIdleTime") && !value.isEmpty()){
        			    	 dataBase.setMaxIdleTime(value);
        			    }else if(name.equals("idleConnectionTestPeriod") && !value.isEmpty()){
        			    	 dataBase.setIdleConnectionTestPeriod(value);
        			    }
            	}
            }
        }
        return dataBase;
    }
    /*
     * 更新JBPM数据库文件配置信息
     */
   public boolean writeTargetConfig(String target_file,DataBaseConfig dataBase){
	   try{
				   DocumentBuilderFactory target = DocumentBuilderFactory.newInstance();
			       DocumentBuilder target_db = target.newDocumentBuilder();
			       target_db.setEntityResolver(new NoOpEntityResolver());
			       Document target_doc = target_db.parse(new File(target_file));
			   
			       Element eltRoot=(Element) target_doc.getDocumentElement().getElementsByTagName("session-factory").item(0);
			   
			       NodeList childList =  eltRoot.getElementsByTagName("property");
			       for(int i=0;i<childList.getLength();i++){//更改XML数据库配置数据
				       	Node child = childList.item(i);
//				       	if(child.getAttributes().getNamedItem("name").getNodeValue().equals("hibernate.connection.driver_class")){
//				       	     childList.item(i).setTextContent(dataBase.getDriver());
//				       	}else if(child.getAttributes().getNamedItem("name").getNodeValue().equals("hibernate.connection.url")){
//				       	     childList.item(i).setTextContent(dataBase.getJdbcUrl());
//				       	}else if(child.getAttributes().getNamedItem("name").getNodeValue().equals("hibernate.connection.username")){
//				       	     childList.item(i).setTextContent(dataBase.getUser());
//				       	}else if(child.getAttributes().getNamedItem("name").getNodeValue().equals("hibernate.connection.password")){
//				       	     childList.item(i).setTextContent(dataBase.getPassword());
//				       	}else if(child.getAttributes().getNamedItem("name").getNodeValue().equals("hibernate.c3p0.max_size")){
//				       	     childList.item(i).setTextContent(dataBase.getMaxPoolSize());
//				       	}else if(child.getAttributes().getNamedItem("name").getNodeValue().equals("hibernate.c3p0.min_size")){
//				       	     childList.item(i).setTextContent(dataBase.getMinPoolSize());
//				       	}else if(child.getAttributes().getNamedItem("name").getNodeValue().equals("hibernate.c3p0.idle_test_period")){
//				       	     childList.item(i).setTextContent(dataBase.getIdleConnectionTestPeriod());
//				       	}
			       }
			      
			       //开始写入XML
			       TransformerFactory tff = TransformerFactory.newInstance();
			       Transformer tf = tff.newTransformer();
			       tf.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd");
			     
			       tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			       DOMSource source = new DOMSource(target_doc);
			       StreamResult result = new StreamResult(new File(target_file));
			       tf.transform(source, result);
			       return true;
	   }catch(Exception e){
		    return false;
	   }
   }
   
   public class NoOpEntityResolver implements EntityResolver {
	
        public InputSource resolveEntity(String publicId, String systemId)
			throws SAXException, IOException {
		// TODO Auto-generated method stub
		return  new InputSource(new StringBufferInputStream(""));
	}
	 }

} 