package com.gionee.wms.service.stock;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gionee.wms.common.WmsConstants;
import com.sf.integration.expressservice.bean.OrderFilterRespBean;
import com.sf.integration.expressservice.bean.RouteRespBean;

@Service("kuaidiService")
public class KuaidiServiceImpl implements KuaidiService {
	private static Logger logger = LoggerFactory	.getLogger(KuaidiServiceImpl.class);

	@Override
	public RouteRespBean sfRoute(String xml) {
		JAXBContext context=null;
	    StringReader reader=null;
	    RouteRespBean respBean = null;
		StringBuffer xmlBuff = new StringBuffer("<Request service=\"RouteService\" lang=\"zh-CN\">") 
				.append("<Head>").append(WmsConstants.SHUNFENG_CUSTID).append(",").append(WmsConstants.SHUNFENG_CHECKWORK).append("</Head>")
				.append("<Body>") 
				.append(xml)
				.append("</Body>")
				.append("</Request>");
		JaxWsDynamicClientFactory dynamicClient = JaxWsDynamicClientFactory.newInstance();
		if(logger.isDebugEnabled()) {
			logger.debug(xmlBuff.toString());
		}
		Client client = dynamicClient.createClient(WmsConstants.SHUNFENG_WS_URL);
		try {
			Object[] rspArr = client.invoke("sfexpressService", xmlBuff.toString());
			if (null != rspArr && rspArr.length > 0) {
				String respXml = (String)rspArr[0];
				logger.debug(respXml);
				context = JAXBContext.newInstance(RouteRespBean.class);
				reader = new StringReader(respXml);
		        Unmarshaller unmar = context.createUnmarshaller();
		        respBean = (RouteRespBean)unmar.unmarshal(reader);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			context=null;
			reader.close();
		}
		return respBean;
	}

	@Override
	public OrderFilterRespBean sfOrderFilter(String xml) {
		JAXBContext context=null;
	    StringReader reader=null;
        OrderFilterRespBean respBean=null;
		StringBuffer xmlBuff = new StringBuffer("<Request service=\"OrderFilterService\" lang=\"zh-CN\">")
			.append("<Head>").append(WmsConstants.SHUNFENG_CUSTID).append(",").append(WmsConstants.SHUNFENG_CHECKWORK).append("</Head>")
			.append("<Body>") 
			.append(xml)
			.append("</Body>")
			.append("</Request>");
		if(logger.isDebugEnabled()) {
			logger.debug(xmlBuff.toString());
		}
		JaxWsDynamicClientFactory dynamicClient = JaxWsDynamicClientFactory.newInstance();
		Client client = dynamicClient.createClient(WmsConstants.SHUNFENG_WS_URL);
		try {
			Object[] rspArr = client.invoke("sfexpressService", xmlBuff.toString());
			if (null != rspArr && rspArr.length > 0) {
				String respXml = (String)rspArr[0];
				logger.debug(respXml);
				System.out.println(respXml);
				context = JAXBContext.newInstance(OrderFilterRespBean.class);
				reader = new StringReader(respXml);
		        Unmarshaller unmar = context.createUnmarshaller();
		        respBean = (OrderFilterRespBean)unmar.unmarshal(reader);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			context=null;
			reader.close();
		}
		return respBean;
	}

}
