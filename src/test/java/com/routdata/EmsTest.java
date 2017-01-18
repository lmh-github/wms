/**
* @author jay_liang
* @date 2014-3-31 下午3:12:54
*/
package com.routdata;

import java.security.MessageDigest;
import java.util.ArrayList;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

import com.routdata.zzfw.webservice.service.AddSoapHeader;
import com.routdata.zzfw.webservice.service.BusinessException_Exception;
import com.routdata.zzfw.webservice.service.IMailQueryService;
import com.routdata.zzfw.webservice.service.SQLException_Exception;
import com.routdata.zzfw.webservice.service.SysException_Exception;

/**
 * @=======================================
 * @Description TODO 
 * @author jay_liang
 * @date 2014-3-31 下午3:12:54
 * @=======================================
 */
public class EmsTest {

	public static void main(String[] args) throws Exception {
		String content = "{\"mailcode\":\"EC100851782CS\",\"vuserno\":\"test\",\"password\":\"test\"}";
		System.out.println(content);
		String sign = encrypt(content, "TEST_ITCO", "utf-8");
		System.out.println(sign);
		dyna(content, sign);
	}
	
	public static void factory(String content, String sign) throws SQLException_Exception, BusinessException_Exception, SysException_Exception {
		AddSoapHeader ash = new AddSoapHeader();
		ArrayList list = new ArrayList();
		// 添加soap header 信息
		list.add(ash);
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setOutInterceptors(list);
		factory.setServiceClass(IMailQueryService.class);// 实例化ws
		factory.setAddress("http://219.134.187.38:7010/sdzzfwservice/webservice/mailQueryService");
		Object obj = factory.create();
		IMailQueryService mailService = (IMailQueryService) obj;
		System.out.println(mailService.queryMail(content, sign, "UTF-8"));
	}
	
	public static void dyna(String content, String sign) throws Exception {
		JaxWsDynamicClientFactory clientFactory = JaxWsDynamicClientFactory.newInstance();
		Client client = clientFactory.createClient("http://219.134.187.38:7010/sdzzfwservice/webservice/mailQueryService?wsdl");
		Object[] result = client.invoke("queryMail", content, sign, "UTF-8");
		System.out.println(result[0]);
	}

	// 签名程序代码片段
	public static String encrypt(String content, String keyValue, String charset)
			throws Exception {
		if (keyValue != null) {
			return base64(MD5(content + keyValue, charset), charset);
		}
		return base64(MD5(content, charset), charset);
	}

	/**
	 * 对传入的字符串进行MD5加密
	 * 
	 * @param plainText
	 * @return
	 */
	public static String MD5(String plainText, String charset) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(plainText.getBytes(charset));
		byte b[] = md.digest();
		int i;
		StringBuffer buf = new StringBuffer("");
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				buf.append("0");
			buf.append(Integer.toHexString(i));
		}
		return buf.toString();
	}

	/**
	 * base64编码
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String base64(String str, String charset) throws Exception {
		return (new sun.misc.BASE64Encoder()).encode(str.getBytes(charset));
	}

}
