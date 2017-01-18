package com.gionee.wms.web.ws.interceptor;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WriteHeaderInterceptor extends AbstractSoapInterceptor {
	private String namespaceURI;// 元素命名空间URI
	private String qualifiedName;// 元素类型名称
	private String userNameTag;// 用户名元素名称
	private String passwordTag;// 密码元素名称
	private String userName;// 用户名
	private String password;// 密码

	public WriteHeaderInterceptor() {
		super(Phase.WRITE);
	}

	public void handleMessage(SoapMessage message) throws Fault {
		List<Header> headers = message.getHeaders();
		headers.add(getHeader());
	}

	private Header getHeader() {
		QName qName = new QName(namespaceURI, qualifiedName);
		Document doc = DOMUtils.createDocument();
		Element root = doc.createElementNS(namespaceURI, qualifiedName);

		Element userNameElement = doc.createElement(userNameTag);
		userNameElement.setTextContent(userName);

		Element passwordElement = doc.createElement(passwordTag);
		passwordElement.setTextContent(password);

		root.appendChild(userNameElement);
		root.appendChild(passwordElement);
//		XMLUtils.printDOM(root);// 只是打印xml内容到控制台,可删除

		SoapHeader header = new SoapHeader(qName, root);
		return (header);
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