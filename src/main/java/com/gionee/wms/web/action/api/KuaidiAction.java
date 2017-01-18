/*
 * @(#)KuaiDiAction.java 2013-7-30
 *
 * Copyright 2013 Shenzhen Gionee,Inc. All rights reserved.
 */
package com.gionee.wms.web.action.api;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.Validate;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.JsonUtils;
import com.gionee.wms.common.WmsConstants;
import com.routdata.zzfw.webservice.service.AddSoapHeader;
import com.routdata.zzfw.webservice.service.EMSUtil;
import com.routdata.zzfw.webservice.service.IMailQueryService;
import com.sf.integration.expressservice.bean.OrderFilterRespBean;
import com.sf.integration.expressservice.bean.RouteRespBean;

/**
 * WMS与顺丰EMS快递查询
 * 
 * @author ZuoChangjun 2013-7-30
 */
@Controller("kuaidiAction")
@Scope("prototype")
public class KuaidiAction {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
//	protected Logger kuaidiLog = LoggerFactory.getLogger("kuaidiLog");
	
	private JsonUtils jsonUtils = new JsonUtils(Inclusion.NON_NULL);
	private static final SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd HH:mm:ss");
	
	private String mailno;
	private String orderid;
	private String daddress;
	private String dprovince;
	private String dcity;
	private String dcountry;
	private String mailCode;
	
	private String type="1";
	
	public String querySFRoute() throws Exception {
		Validate.notBlank(mailno);
		JAXBContext context=null;
	    StringReader reader=null;
		String json="";
		String xml="";
		StringBuffer xmlBuff = new StringBuffer("<Request service=\"RouteService\" lang=\"zh-CN\">") 
				.append("<Head>").append(WmsConstants.SHUNFENG_CUSTID).append(",").append(WmsConstants.SHUNFENG_CHECKWORK).append("</Head>")
				.append("<Body>") 
				.append("<RouteRequest tracking_type=\"1\" method_type=\"1\" tracking_number=\"").append(mailno).append("\"/>")
				.append("</Body>")
				.append("</Request>");
		JaxWsDynamicClientFactory dynamicClient = JaxWsDynamicClientFactory.newInstance();
		System.out.println(xmlBuff.toString());
		Client client = dynamicClient.createClient("http://219.134.187.132:9090/bsp-ois/ws/expressService?wsdl");
		try {
			Object[] rspArr = client.invoke("sfexpressService", xmlBuff.toString());
			if (null != rspArr && rspArr.length > 0) {
				xml = (String)rspArr[0];
				logger.debug(xml);
				context = JAXBContext.newInstance(RouteRespBean.class);
				reader = new StringReader(xml);
		        Unmarshaller unmar = context.createUnmarshaller();
		        RouteRespBean respBean = (RouteRespBean)unmar.unmarshal(reader);
		        json=jsonUtils.toJson(respBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			context=null;
			reader.close();
		}
		if("1".equals(type)) {
			ActionUtils.outputJson(json);
		} else {
			ActionUtils.outputText(xml);
		}
		return null;
	}
	
	public String queryOrderFilter() throws Exception {
		JAXBContext context=null;
	    StringReader reader=null;
		String xml="";
		String json="";
		StringBuffer req = new StringBuffer("<Request service=\"OrderFilterService\" lang=\"zh-CN\">")
			.append("<Head>").append(WmsConstants.SHUNFENG_CUSTID).append(",").append(WmsConstants.SHUNFENG_CHECKWORK).append("</Head>")
			.append("<Body>") 
			.append("<OrderFilter filter_type=\"1\" ");
		if(null!=orderid) {
			req.append("orderid=\"").append(orderid).append("\"");
		}
		req.append("d_address=\"").append(daddress).append("\">").append("<OrderFilterOption ");
		if(null!=dprovince) {
			req.append("d_province=\"").append(dprovince).append("\" ");
		}
		if(null!=dcity) {
			req.append("d_city=\"").append(dcity).append("\" ");
		}
		if(null!=dcountry) {
			req.append("d_county=\"").append(dcountry).append("\" ");
		}
		req.append("/>")
			.append("</OrderFilter>")
			.append("</Body>")
			.append("</Request>");
		System.out.println(req.toString());
		JaxWsDynamicClientFactory dynamicClient = JaxWsDynamicClientFactory.newInstance();
		Client client = dynamicClient.createClient("http://219.134.187.132:9090/bsp-ois/ws/expressService?wsdl");
		try {
			Object[] rspArr = client.invoke("sfexpressService", req.toString());
			if (null != rspArr && rspArr.length > 0) {
				xml = (String)rspArr[0];
				logger.debug(xml);
				context = JAXBContext.newInstance(OrderFilterRespBean.class);
				reader = new StringReader(xml);
		        Unmarshaller unmar = context.createUnmarshaller();
		        OrderFilterRespBean respBean = (OrderFilterRespBean)unmar.unmarshal(reader);
		        json=jsonUtils.toJson(respBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			context=null;
			reader.close();
		}
		if("1".equals(type)) {
			ActionUtils.outputJson(json);
		} else {
			ActionUtils.outputText(xml);
		}
		return null;
	}
	
	public String queryEmsRoute() {
		String content = "{\"mailcode\":\""+mailCode+"\",\"vuserno\":\""+WmsConstants.EMS_VUSERNO+"\",\"password\":\""+WmsConstants.EMS_PASSWORD+"\"}";
		String sign;
		try {
			sign = EMSUtil.encrypt(content, "TEST_ITCO", "utf-8");
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
			String json = mailService.queryMail(content, sign, "UTF-8");
			ActionUtils.outputJson(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String getMailno() {
		return mailno;
	}

	public void setMailno(String mailno) {
		this.mailno = mailno;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getDaddress() {
		return daddress;
	}

	public void setDaddress(String daddress) {
		this.daddress = daddress;
	}

	public String getDprovince() {
		return dprovince;
	}

	public void setDprovince(String dprovince) {
		this.dprovince = dprovince;
	}

	public String getDcity() {
		return dcity;
	}

	public void setDcity(String dcity) {
		this.dcity = dcity;
	}

	public String getDcountry() {
		return dcountry;
	}

	public void setDcountry(String dcountry) {
		this.dcountry = dcountry;
	}

	public String getMailCode() {
		return mailCode;
	}

	public void setMailCode(String mailCode) {
		this.mailCode = mailCode;
	}
}
