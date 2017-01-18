/**
* @author jay_liang
* @date 2014-3-21 下午4:10:34
*/
package com.sf;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

/**
 * @=======================================
 * @Description TODO 
 * @author jay_liang
 * @date 2014-3-21 下午4:10:34
 * @=======================================
 */
public class SfTest1 {
	public static void main(String[] args){
		String xml = "<Request service=\"OrderFilterService\" lang=\"zh-CN\">" 
				+ "<Head>7699476943,_DQcPl7DO8[}p2wH</Head>" 
				+ "<Body>" 
				+ "<OrderFilter filter_type=\"1\" orderid=\"1234567890\" d_address=\"鸿翔花园\">" 
//				+ "<OrderFilterOption d_province=\"广东省\"  d_city=\"深圳市\" d_county=\"罗湖区\"/>"
				+"</OrderFilter>"
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
