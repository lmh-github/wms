/**
* @author jay_liang
* @date 2014-3-21 下午4:10:34
*/
package com.sf.integration.expressservice.service;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

/**
 * @=======================================
 * @Description TODO 
 * @author jay_liang
 * @date 2014-3-21 下午4:10:34
 * @=======================================
 */
public class SfTest {
	public static void main(String[] args){
		String xml = "<Request service=\"RouteService\" lang=\"zh-CN\">" 
				+ "<Head>7699476943,_DQcPl7DO8[}p2wH</Head>" 
				+ "<Body>" 
				+ "<RouteRequest tracking_type=\"1\" method_type=\"1\" tracking_number=\"587000256204\"/>" 
				+ "</Body>"
				+ "</Request>";
		System.out.println(xml);
		JaxWsDynamicClientFactory dynamicClient = JaxWsDynamicClientFactory.newInstance();
		Client client = dynamicClient.createClient("http://219.134.187.132:9090/bsp-ois/ws/expressService?wsdl");
		try {
			Object[] rspArr = client.invoke("sfexpressService", xml);
			if (null != rspArr && rspArr.length > 0) {
				for (int i = 0; i < rspArr.length; i++) {
					System.out.println(rspArr[i]);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
