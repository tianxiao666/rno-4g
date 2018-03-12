package test.java;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.iscreate.op.service.rno.job.JobCapacity;

public class TestXml {

	public void getdata() {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		docBuilderFactory.setIgnoringComments(true);

		docBuilderFactory.setNamespaceAware(true);
		try {
			docBuilderFactory.setXIncludeAware(true);
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		}
		DocumentBuilder builder = null;
		try {
			builder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}

		Document doc = null;
		Element root = null;
		boolean returnCachedProperties = false;

		Map<String, JobCapacity> jobCapacities = new HashMap<String, JobCapacity>();
		String capacityFile = "jobnode-capacity.xml";
		InputStream is = this.getClass().getClassLoader()
				.getResourceAsStream(capacityFile);
		try {
			doc = builder.parse(is);
		} catch (IOException e1) {
			e1.printStackTrace();
		}catch (SAXException e2) {
			e2.printStackTrace();
		}

		root = doc.getDocumentElement();
		NodeList nodeLists = root.getChildNodes();
		if (nodeLists != null && nodeLists.getLength() > 0) {
			String jobType = null, maxSlot = null, runnable = null, runnableMgr = null;
			for (int ni = 0; ni < nodeLists.getLength(); ni++) {
				Node node = nodeLists.item(ni);
				System.out.println("-" + node.getNodeName() + ":"
						+ node.getLocalName());
				NodeList childs = node.getChildNodes();
				if (childs.getLength() <= 0) {
					continue;
				}
				jobType = null;
				maxSlot = null;
				runnable = null;
				runnableMgr = null;
				for (int ci = 0; ci < childs.getLength(); ci++) {
					Node child = childs.item(ci);
					System.out.println("-nodeName:" + child.getNodeName()
							+ " localName=" + child.getLocalName() + " value="
							+ child.getTextContent());
					if ("type".equals(child.getLocalName())) {
						jobType = child.getTextContent();
					} else if ("maxSlot".equals(child.getLocalName())) {
						maxSlot = child.getTextContent();
					} else if ("runnable".equals(child.getLocalName())) {
						runnable = child.getTextContent();
					} else if ("runnableMgr".equals(child.getLocalName())) {
						runnableMgr = child.getTextContent().trim();
					}
				}
				System.out.println("type="+jobType+",maxSlot="+maxSlot+",runnable="+runnable+",mgr="+runnableMgr);
				//
				if(jobType==null||"".equals(jobType.trim())
						||runnable==null || "".equals(runnable.trim()))
						{
					continue;
				}
				int ms=Integer.parseInt(maxSlot);
				if(ms<=0){
					continue;
				}
				if(runnableMgr==null || "".equals(runnableMgr.trim())){
					
				}
			}
		}

		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestXml xml = new TestXml();
		xml.getdata();
	}
}
