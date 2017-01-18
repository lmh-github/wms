package com.gionee.wms.web.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gionee.wms.common.ResultCode;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.dto.ShiroUser;
import com.gionee.wms.dto.SsoLoginCheckResult;
import com.gionee.wms.web.client.SsoClient;

/**
 * 授权过滤器
 * @author Administrator
 *
 */
public class ShiroAuthorizationFilter extends UserFilter {
	private static final Logger logger = LoggerFactory.getLogger(ShiroAuthorizationFilter.class);
	private SsoClient ssoClient;

	/* 
	 * 方法重写，调用IUC SSO接口，发送重定向
	 * @see org.apache.shiro.web.filter.AccessControlFilter#redirectToLogin(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	 */
	@Override
	protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		if ("XMLHttpRequest".equalsIgnoreCase(httpRequest.getHeader("X-Requested-With"))
				|| httpRequest.getParameter("ajax") != null) {
			//DWZ的ajax请求超时处理
			String msg = "{\"statusCode\":\"301\", \"message\":\"登录超时! 请重新登录!\"}";
			httpResponse.setContentType("application/json;charset=UTF-8");
			try {
				httpResponse.getWriter().write(msg);
				httpResponse.getWriter().flush();
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		} else {
		//普通超时处理
		String callbackUrl = "http://" + httpRequest.getServerName() + ":" + httpRequest.getServerPort()
				+ httpRequest.getContextPath() + WmsConstants.UUC_AUTHC_CALLBACK_URI;
		String ssoUrl = WmsConstants.UUC_URL+WmsConstants.UUC_AUTHC_TICKET_URI + "service=" + callbackUrl;
		//调用IUC SSO接口，发送重定向 ，类似： ssoUrl=http://passport.cm.com/iuc/sso?service=http://18.8.0.28:8080/wms/authcCallback
		httpResponse.sendRedirect(httpResponse.encodeRedirectURL(ssoUrl));
	   }
	}

	/**
	 * 重写isAccessAllowed，改变shiro的验证逻辑，所有的请求都会先通过这个方法过滤拦截。如没有登录，则调用redirectToLogin方法向IUC用户认证中心发送认证请求。
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		if (isLoginRequest(request, response)) {
			return true;
		} else {
			Subject subject = getSubject(request, response);
			ShiroUser user = (ShiroUser) subject.getPrincipal();
			HttpSession session = ((HttpServletRequest)request).getSession();
			if (user != null && StringUtils.isNotBlank(user.getSsoTgt())) {
				// 拿当前的ssoTgt去用户中心检测是否在线
				try {
					SsoLoginCheckResult result = ssoClient.checkLogin(user.getSsoTgt());
					if (result == null) {
						logger.error("sso check login error: result is null");
						return false;
					}
					if(!(ResultCode.SUCCESS+"").equals(result.getCode())){
						subject.logout();
						logger.error("sso check login error: "+ResultCode.parseResultCode(result.getCode()));
						return false;
					}
					// 保存用户信息及权限
					user.setId(StringUtils.isBlank(result.getId())?0L:new Long(result.getId()));
					user.setLoginName(result.getUsername());
					user.setType(result.getType());
					user.setPermission(result.getPermission());
					if (session != null) {
						session.setAttribute("user", user);
					}
				} catch (Exception e) {
					logger.error("sso check login error: ",e);
					return false;
				}
					return true;
			} else {
				return false;
			}
		
			// If principal is not null, then the user is known and should be
			// allowed access.
			// return subject.getPrincipal() != null;
		}
	}

	@Autowired
	public void setSsoClient(SsoClient ssoClient) {
		this.ssoClient = ssoClient;
	}

}