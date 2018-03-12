/**
 * CopyRight IsCreate 
 * Project support 
 * JDK 1.5_10
 * @author zhang_m2 
 * Version v1.0 
 * Date 2010-07-06 
 * Description xml����ʵ���࣬�ṩDom4j��ȡxml�ڵ�ֵ�Ľӿڡ� 
 * Modify History 
 * 2010-07-06	chen_by1	�����ļ�����дxml������
 * 2010-08-11 	chen_by1	���һ��˽�з���getFilePath�������ļ��ĺϷ���
 * 2010-12-03   zhang_m2    ��getFilePath()�������޸ģ�֧���ļ�·�����ġ��ո�������ַ��������Ӷ�����ӽڵ����ԡ�ɾ��ڵ㡢�޸Ľڵ����ԡ��޸Ķ����Լ���ѯ����ȷ���
 */
package com.iscreate.plat.tools.xmlhelper;

import java.io.IOException;
import java.util.List;
import org.dom4j.DocumentException;

public interface XmlService
{
	/**
	 * ���xml��ڵ�
	 * 
	 * @param file
	 * @param rootName
	 * @return
	 * @throws Exception
	 */
	public boolean createRootElement(String file, String rootName)
			throws DocumentException, IOException;

	/**
	 * ���xml�ڵ�
	 * 
	 * @param file
	 *            �ļ�����ļ�·�������ļ����ļ����������·������)
	 * @param path
	 *            �ڵ�·��
	 * @param name
	 *            ����ڵ����
	 * @param value
	 *            ����ڵ�ֵ
	 * @return ����ɹ����(true/false)
	 * @throws DocumentException
	 * @throws IOException
	 */
	public boolean addXmlNode(String file, String path, String name,
			String value) throws DocumentException, IOException;
	
	public String addXmlNodeByXmlContent(String content, String path,
			String name, String value) throws DocumentException, IOException;

	/**
	 * Ϊָ��xml�ڵ��������
	 * 
	 * @param file
	 *            �ļ�����ļ�·�������ļ����ļ����������·������)
	 * @param path
	 *            �ڵ�·��
	 * @param attributeName
	 *            �������
	 * @param value
	 *            ����ֵ
	 * @return ����ɹ����(true/false)
	 * @throws DocumentException
	 * @throws IOException
	 */
	public boolean addXmlNodeAttribute(String file, String path,
			String attributeName, String value) throws DocumentException,
			IOException;
	
	public String addXmlNodeAttributeByXmlContent(String content, String path,
			String attributeName, String value) throws DocumentException,
			IOException;

	/**
	 * ��Ӷ���xml�ڵ�
	 * 
	 * @param file
	 *            �ļ�����ļ�·�������ļ����ļ����������·������)
	 * @param path
	 *            �ڵ�·��
	 * @param obj
	 *            ��Ӷ���
	 * @return ����ɹ����(true/false)
	 * @throws Exception
	 */
	public boolean addXmlNodeByObject(String file, String path, Object obj)
			throws Exception;

	/**
	 * ɾ��xml�ڵ�
	 * 
	 * @param file
	 *            �ļ�����ļ�·�������ļ����ļ����������·������)
	 * @param path
	 *            �ڵ�·��
	 * @return ����ɹ����(true/false)
	 * @throws DocumentException
	 * @throws IOException
	 */
	public boolean deleteXmlNode(String file, String path)
			throws DocumentException, IOException;

	/**
	 * �޸�xml�ļ�ָ���ڵ�ֵ
	 * 
	 * @param file
	 *            �ļ�����ļ�·�������ļ����ļ����������·������)
	 * 
	 * @param node
	 *            �ڵ����(��ʽ��//root/node/node)
	 * 
	 * @param value
	 *            �޸�ֵ
	 * 
	 * @return �޸Ĳ���ɹ����
	 * 
	 * @throws DocumentException
	 *             , IOException
	 */
	public boolean modifyXmlValue(String file, String node, String value)
			throws DocumentException, IOException;

	/**
	 * �޸�xml�ڵ�����ֵ
	 * 
	 * @param file
	 *            �ļ�����ļ�·�������ļ����ļ����������·������)
	 * @param node
	 *            �ڵ����(��ʽ��//root/node/node)
	 * @param value
	 *            ����ֵ
	 * @return ����ɹ����
	 * @throws DocumentException
	 * @throws IOException
	 */
	public boolean modifyElementAttributeValue(String file, String node,
			String value) throws DocumentException, IOException;

	/**
	 * ������id�޸Ķ���ڵ�ֵ
	 * 
	 * @param file
	 *            �ļ�����ļ�·�������ļ����ļ����������·������)
	 * @param pathNode
	 *            ��ǰ�ڵ�ĸ��ڵ�·��(��ʽ��//root/node/node)
	 * @param nodeName
	 *            ����ڵ����
	 * @param obj
	 *            �ڵ����ʵ��
	 * @return ����ɹ����
	 * @throws Exception
	 */
	public boolean modifyObjectById(String file, String pathNode,
			String nodeName, Object obj) throws Exception;

	/**
	 * ��ȡ�ж�ֵ�ڵ������ֵ
	 * 
	 * @param file
	 *            �ļ�����ļ�·�������ļ����ļ����������·������)
	 * 
	 * @param node
	 *            �ڵ���(��ʽ��//root/node/node)
	 * 
	 * @return List<String> ��ֵ�ڵ������ֵ
	 * 
	 * @throws DocumentException
	 */
	public List<String> getElementValue(String file, String node)
			throws DocumentException;

	/**
	 * ��ȡֻ�е�ֵ�ڵ��ֵ
	 * 
	 * @param file
	 *            �ļ�����ļ�·�������ļ����ļ����������·������)
	 * 
	 * @param node
	 *            �ڵ���(��ʽ��//root/node/node)
	 * 
	 * @return String ����ڵ��ֵ
	 * 
	 * @throws DocumentException
	 */
	public String getSingleElementValue(String file, String node)
			throws DocumentException;

	/**
	 * ��ȡ�ڵ���ӽڵ㼯�ϣ����ط�װ����
	 * 
	 * @param file
	 *            �ļ�����ļ�·�������ļ����ļ����������·������)
	 * @param pathNode
	 *            ���ڵ�·��
	 * @param nodeName
	 *            ��ǰ�ڵ����
	 * @param obj
	 *            ��װ����
	 * @return ��װ���󼯺�
	 * @throws Exception
	 */
	public List getElementValueByParentNode(String file, String pathNode,
			String nodeName, Object obj) throws Exception;

	/**
	 * ��ȡ�ڵ���ӽڵ㼯�ϣ����ط�װ����
	 * 
	 * @param file
	 *            �ļ�����ļ�·�������ļ����ļ����������·������)
	 * @param pathNode
	 *            ���ڵ�·��
	 * @param nodeName
	 *            ��ǰ�ڵ����
	 * @param obj
	 *            ��װ����
	 * @param id
	 *            �ڵ�id
	 * @return ��װ���󼯺�
	 * @throws Exception
	 */
	public List getElementValueByParentNode(String file, String pathNode,
			String nodeName, Object obj, String id) throws Exception;
}
