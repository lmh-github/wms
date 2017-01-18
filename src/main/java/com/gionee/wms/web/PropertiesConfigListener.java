package com.gionee.wms.web;

import java.io.File;
import java.util.Iterator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Application Lifecycle Listener implementation class PropertiesConfigListener
 *
 */
public class PropertiesConfigListener implements ServletContextListener {
	private static Logger logger = LoggerFactory.getLogger(PropertiesConfigListener.class);
	private static final String SRPING_BEAN_ID = "placeholderConfigurer";

	/**
	 * 初始化系统变量(通过解析spring-sso-jdbc.xml取得外部资源文件，保存各property到System,从而保证系统常量值的统一)
	 */
	public void contextInitialized(ServletContextEvent sce) {
//		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//		try {
//			DocumentBuilder db = dbf.newDocumentBuilder();
//			Document doc = db.parse(getClass().getClassLoader().getResource("config/spring/spring-wms-jdbc.xml").toString());
//			Element e = doc.getDocumentElement();
//			NodeList beanNodeList = e.getElementsByTagName("bean");
//			for (int i = 0; i < beanNodeList.getLength(); i++) {
//				Node beanNode = beanNodeList.item(i);
//				if (SRPING_BEAN_ID.equals(beanNode.getAttributes().getNamedItem("id").getNodeValue())) {
//					NodeList childNodes = beanNode.getChildNodes();
//					for (int j = 0; j < childNodes.getLength(); j++) {
//						Node childNode = childNodes.item(j);
//						// 如果这个节点属于Element ,再进行取值   
//						if (childNode instanceof Element && "property".equals(childNode.getNodeName())) {
//							if ("locations".equals(childNode.getAttributes().getNamedItem("name").getNodeValue())) {
//								Element propertyElement = (Element) childNode;
//								NodeList valueNodeList = propertyElement.getElementsByTagName("value");
//								boolean propertiesFinded = false;
//								for (int k = 0; k < valueNodeList.getLength(); k++) {
//									//逐个查找文件，找到即停止，若一个都未找到则抛出异常
//									try {
//										String propertyFileUrl = valueNodeList.item(k).getTextContent();
//										System.out.println("property file:"+propertyFileUrl);
//										File file = ResourceUtils.getFile(propertyFileUrl);
//										PropertiesConfiguration config = new PropertiesConfiguration(file);
//										Iterator itr = config.getKeys();
//										while (itr.hasNext()) {
//											String key = (String) itr.next();
//											System.setProperty(key, config.getString(key));
//											propertiesFinded = true;
//											System.out.println("key:"+key+" , value:"+config.getString(key));
//										}
////										if(propertiesFinded){
////											break;
////										}
//									} catch (Exception e1) {
//										
//									}
//								}
//								if (!propertiesFinded) {
//									throw new RuntimeException("未找到资源文件 ");
//								}
//								break;
//							}
//
//						}
//					}
//					break;
//				}
//			}
//
//		} catch (Exception e) {
//			throw new RuntimeException("初始化WMS系统变量时出错",e);
//		}
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
	}

}
