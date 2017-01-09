package com.gionee.wms.web.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gionee.wms.common.ResultCode;
import com.gionee.wms.dto.SsoValidateTicketResult;
import com.gionee.wms.dto.TrustedSsoAuthenticationToken;
import com.gionee.wms.web.client.SsoClient;

/**
 * 认证过滤器
 * 
 * @author kevin
 * 
 */
public class ShiroAuthenticationFilter extends FormAuthenticationFilter {
	private static final Logger log = LoggerFactory.getLogger(ShiroAuthenticationFilter.class);

	private SsoClient ssoClient;

	/**
	 * 覆盖默认实现，重写shiro访问拒绝后的逻辑
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		if (isLoginRequest(request, response)) {
			if (isLoginSubmission(request, response)) {
				if (log.isTraceEnabled()) {
					log.trace("Login submission detected.  Attempting to execute login.");
				}
				return executeLogin(request, response);
			} else {
				// SSO认证完成后处理回调逻辑
				String ticket = request.getParameter("ticket");
				if (StringUtils.isNotBlank(ticket)) {
					return executeLogin(request, response);
				}
				if (log.isTraceEnabled()) {
					log.trace("Login page view.");
				}
				// allow them to see the login page ;)
				return true;
			}
		} else {
			if (log.isTraceEnabled()) {
				log.trace("Attempting to access a path which requires authentication.  Forwarding to the "
						+ "Authentication url [" + getLoginUrl() + "]");
			}
			// 若没登录，则发送重定向
			saveRequestAndRedirectToLogin(request, response);
			return false;
		}
	}

	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
		AuthenticationToken token = createToken(request, response);
		if (token == null) {
			String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken "
					+ "must be created in order to execute a login attempt.";
			throw new IllegalStateException(msg);
		}
		try {
			Subject subject = getSubject(request, response);
			subject.login(token);
			return onLoginSuccess(token, subject, request, response);
		} catch (AuthenticationException e) {
			return onLoginFailure(token, e, request, response);
		}
	}

	// /*
	// * 覆盖默认实现，打印日志便于调试，查看具体登录是什么错误。（可以扩展把错误写入数据库之类的。） (non-Javadoc)
	// *
	// * @see
	// *
	// org.apache.shiro.web.filter.authc.FormAuthenticationFilter#onLoginFailure
	// * (org.apache.shiro.authc.AuthenticationToken,
	// * org.apache.shiro.authc.AuthenticationException,
	// * javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	// */
	// @Override
	// protected boolean onLoginFailure(AuthenticationToken token,
	// AuthenticationException e, ServletRequest request,
	// ServletResponse response) {
	// if (log.isDebugEnabled()) {
	// Class<?> clazz = e.getClass();
	// if (clazz.equals(AuthenticationException.class)) {
	// // log.debug(Exceptions.getStackTraceAsString(e));
	// }
	// }
	//
	// return super.onLoginFailure(token, e, request, response);
	// }
	//
	// /**
	// * 覆盖默认实现，用sendRedirect直接跳出框架，以免造成js框架重复加载js出错。
	// *
	// * @param token
	// * @param subject
	// * @param request
	// * @param response
	// * @return
	// * @throws Exception
	// * @see
	// org.apache.shiro.web.filter.authc.FormAuthenticationFilter#onLoginSuccess(org.apache.shiro.authc.AuthenticationToken,
	// * org.apache.shiro.subject.Subject, javax.servlet.ServletRequest,
	// * javax.servlet.ServletResponse)
	// */
	// @Override
	// protected boolean onLoginSuccess(AuthenticationToken token,
	// Subject subject, ServletRequest request, ServletResponse response)
	// throws Exception {
	// // issueSuccessRedirect(request, response);
	// // we handled the success redirect directly, prevent the chain from
	// // continuing:
	// HttpServletRequest httpServletRequest = (HttpServletRequest) request;
	// HttpServletResponse httpServletResponse = (HttpServletResponse) response;
	// // 处理SSO
	// // String tgt = processSso(request, response);
	// // if (!"XMLHttpRequest".equalsIgnoreCase(httpServletRequest
	// // .getHeader("X-Requested-With"))
	// // || request.getParameter("ajax") == null) {
	// // String service = request.getParameter("service");
	// // // 转发到指定的service
	// // if (service != null && !"".equals(service)) {
	// // String ticket = this.centralAuthenticationService
	// // .grantServiceTicket(tgt, WebUtils.getService(
	// // argumentExtractors,
	// // (HttpServletRequest) request));
	// // if (service.indexOf("?") > 0) {
	// // service = service + "&ticket=" + ticket;
	// // } else {
	// // service = service + "?ticket=" + ticket;
	// // }
	// // httpServletResponse.sendRedirect(service);
	// // return false;
	// // }
	// // // 不是ajax请求
	// // if (sucUrl == null)
	// // httpServletResponse.sendRedirect(httpServletRequest
	// // .getContextPath() + this.getSuccessUrl());
	// // else
	// // httpServletResponse.sendRedirect(sucUrl);
	// // } else {
	// // httpServletResponse.sendRedirect(httpServletRequest
	// // .getContextPath() + "/login/timeout/success");
	// // }
	// return false;
	// }

	/**
	 * 覆盖默认实现，创建自定义shiro token
	 */
	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		String ssoTicket = request.getParameter("ticket");

		if (StringUtils.isNotBlank(ssoTicket)) {
			try {
				// 调用户中心的SSO接口校验ticket值，并取回TGT票据
				SsoValidateTicketResult result = ssoClient.validateTicket(ssoTicket);
				if (result == null) {
					log.error("sso validate ticket error: result is null");
					return null;
				}
				if (!(ResultCode.SUCCESS + "").equals(result.getCode())) {
					log.error("sso validate ticket error: " + ResultCode.parseResultCode(result.getCode()));
					return null;
				}
				// 保存用户信息及权限
				// String ssoTgt =
				// "TGT-1-RgUbodDbgr9lOQHOm0OojGuuhfBNbsQBNq46eY6bNDiWibfxMO-sso";
				// 远程校验通过后，将返回的TGT绑定到shiro的自定义token对象
				if (StringUtils.isNotBlank(result.getTgt())) {
					return new TrustedSsoAuthenticationToken(result.getTgt());
				}
			} catch (Exception e) {
				log.error("sso validate ticket error: ", e);
				return null;
			}
		} else {
			return null;
		}
		return null;
	}

	@Autowired
	public void setSsoClient(SsoClient ssoClient) {
		this.ssoClient = ssoClient;
	}

}
