package com.gionee.wms.web.ws.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gionee.wms.common.ActionUtils;

/**
 * IP地址拦截器
 * 可在ipList.xml文件中配置允许和拒绝访问的IP地址
 * 
 * @author Kevin
 * 
 */
public class IpAddressInterceptor extends AbstractSoapInterceptor {
	private static Logger logger = LoggerFactory.getLogger(IpAddressInterceptor.class);
	@Autowired
	private AccessController accessController;

	public IpAddressInterceptor() {
		super(Phase.RECEIVE);
	}

	public void handleMessage(SoapMessage message) throws Fault {
		HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
		List<String> allowedList = accessController.getAllowedIpList(); // 允许访问的IP地址
		List<String> deniedList = accessController.getDeniedIpList(); // 拒绝访问的IP地址
		String ipAddress = ActionUtils.getRemoteAddr(request); // 取客户端IP地址
		// 处理IP黑名单
		for (String deniedIpAddress : deniedList) {
			if (deniedIpAddress.equals(ipAddress)) {
				throw new Fault(new IllegalAccessException("IP address " + ipAddress + " is denied"));
			}
		}
		// 处理IP白名单
		if (allowedList.size() > 0) {
			boolean flag = false;
			for (String allowedIpAddress : allowedList) {
				if (allowedIpAddress.equals(ipAddress)) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				throw new Fault(new IllegalAccessException("IP address " + ipAddress + " is not allowed"));
			}
		}
	}

}
