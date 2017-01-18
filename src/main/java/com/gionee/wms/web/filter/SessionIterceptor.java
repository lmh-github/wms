package com.gionee.wms.web.filter;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;



/**
 * 判断session是否超时的拦截器
 * 超时跳到登录页面
 * @author kevin
 */
public class SessionIterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 7579862236766378267L;

	@Override  
    public String intercept(ActionInvocation actionInvocation) throws Exception { 
		HttpServletRequest request = ServletActionContext.getRequest();
    	if ("XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With")) || request.getParameter("ajax") != null) {
    		
    		if(!SecurityUtils.getSubject().isAuthenticated()){
    			String msg = "{\"statusCode\":\"301\", \"message\":\"Session Timeout! Please re-sign in!\"}";
    			ServletActionContext.getResponse().getWriter().write(msg);
    			return null;
    		}
    	}
       return actionInvocation.invoke();  
    }
}