/**
* @author jay_liang
* @date 2014-3-31 下午4:30:28
*/
package com.routdata.zzfw.webservice.service;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.helpers.XMLUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @=======================================
 * @Description TODO 
 * @author jay_liang
 * @date 2014-3-31 下午4:30:28
 * @=======================================
 */
public class AddSoapHeader extends AbstractSoapInterceptor {

	public static final String xml_namespaceUR_att = "http://service.webservice.zzfw.routdata.com/";
	public static final String xml_header_el = "soap:Header";
	public static final String xml_authentication_el = "auth:authentication";
	public static final String xml_userID_el = "spid";
	public static final String xml_password_el = "sppwd";

	public AddSoapHeader() {
	// 指定该拦截器在哪个阶段被激发
		super(Phase.WRITE);
	}

	public void handleMessage(SoapMessage message) throws Fault {
		String userId = "XT_TEST_ITCO";
		String password = "9C3D630624D27F37A54F4D776E7BFC0C";

		QName qname = new QName("RequestSOAPHeader");//这个值暂时不清楚具体做什么用，可以随便写

		Document doc = (Document) DOMUtils.createDocument();
		Element root = doc.createElement(xml_header_el);
		Element eUserId = doc.createElement(xml_userID_el);
		eUserId.setTextContent(userId);
		Element ePwd = doc.createElement(xml_password_el);
		ePwd.setTextContent(password);
		Element child = doc.createElement("RequestSOAPHeader");
		child.appendChild(eUserId);
		child.appendChild(ePwd);
		root.appendChild(child);
//		XMLUtils.printDOM(root);// 只是打印xml内容到控制台,可删除
		SoapHeader head = new SoapHeader(qname, root);
		List<Header> headers = message.getHeaders();
		headers.add(head);
		
	}
}
