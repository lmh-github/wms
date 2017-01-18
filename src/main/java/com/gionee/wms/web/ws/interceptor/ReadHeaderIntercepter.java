package com.gionee.wms.web.ws.interceptor;

import java.util.List;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.XMLUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ReadHeaderIntercepter extends AbstractSoapInterceptor {

	private static final Logger LOG = Logger.getLogger(ReadHeaderIntercepter.class.getName());
	// private static final Logger logger =
	// LoggerFactory.getLogger(SecuritySOAPHeaderIntercepter.class);
	private String namespaceURI;// 元素命名空间URI
	private String qualifiedName;// 元素类型名称
	private String userNameTag;// 用户名元素名称
	private String passwordTag;// 密码元素名称
	private String userName;// 用户名
	private String password;// 密码

	public ReadHeaderIntercepter() {
		super(Phase.PRE_PROTOCOL);// 这里指定在拦截器链中的执行阶段为PRE_PROTOCOL
	}

	@Override
	public void handleMessage(SoapMessage message) throws Fault {
		List<Header> headers = message.getHeaders();
		boolean authorized = false;
		if (null != headers && !headers.isEmpty()) {
			for (Header header : headers) {
				QName qName = header.getName();
				if (getQualifiedName().equals(qName.getLocalPart())
						&& getNamespaceURI().equals(qName.getNamespaceURI())) {
					Element element = (Element) header.getObject();
					XMLUtils.printDOM(element);
					NodeList userIdNodes = element.getElementsByTagName(getUserNameTag());
					NodeList pwdNodes = element.getElementsByTagName(getPasswordTag());
					if (userIdNodes.item(0) != null && getUserName().equals(userIdNodes.item(0).getTextContent())) {
						if (pwdNodes.item(0) != null && getPassword().equals(pwdNodes.item(0).getTextContent())) {
							authorized = true;
						}
					}
					// NodeList nodeList = element.getChildNodes();
					// for (int i = 0; i < nodeList.getLength(); i++) {
					// Node node = nodeList.item(i);
					// if (getUserNameTag().equals(node.getNodeName())
					// &&
					// getTokenValue().equals(node.getFirstChild().getNodeValue()))
					// {
					// authorized = true;
					// break;
					// }
					// }
				}
			}
		}

		if (!authorized) {
			throw new Fault("authorized error", LOG);
		}
	}

	public String getNamespaceURI() {
		return namespaceURI;
	}

	public void setNamespaceURI(String namespaceURI) {
		this.namespaceURI = namespaceURI;
	}

	public String getQualifiedName() {
		return qualifiedName;
	}

	public void setQualifiedName(String qualifiedName) {
		this.qualifiedName = qualifiedName;
	}

	public String getUserNameTag() {
		return userNameTag;
	}

	public void setUserNameTag(String userNameTag) {
		this.userNameTag = userNameTag;
	}

	public String getPasswordTag() {
		return passwordTag;
	}

	public void setPasswordTag(String passwordTag) {
		this.passwordTag = passwordTag;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}