package com.gionee.wms.web.ws.interceptor;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.collect.Lists;

@Component("accessController")
public class AccessController {
	private static Logger logger = LoggerFactory.getLogger(AccessController.class);

	public List<String> allowedIpList = Lists.newArrayList();
	public List<String> deniedIpList = Lists.newArrayList();

	public List<String> getAllowedIpList() {
		return allowedIpList;
	}

	public List<String> getDeniedIpList() {
		return deniedIpList;
	}

	/**
	 * 系统启动时完成IP黑白名单的初始化工作
	 */
	@PostConstruct
	public void initIpAddress() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(getClass().getClassLoader().getResource("config/ipList.xml").toString());
			Element e = doc.getDocumentElement();
			Element rootEle = (Element) e.getElementsByTagName("purchase-ws").item(0);
			Element ipEle = (Element) rootEle.getElementsByTagName("ip").item(0);
			String allowedIp = ipEle.getElementsByTagName("allowed").item(0).getTextContent();
			allowedIpList = Lists.newArrayList(StringUtils.split(allowedIp, ","));
			String deniedIp = ipEle.getElementsByTagName("denied").item(0).getTextContent();
			deniedIpList = Lists.newArrayList(StringUtils.split(deniedIp, ","));
		} catch (Exception e) {
			logger.error("加载ipList.xml时出错", e);
			throw new RuntimeException("加载ipList.xml时出错", e);
		}
	}
}
