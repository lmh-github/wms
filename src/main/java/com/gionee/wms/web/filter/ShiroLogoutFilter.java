package com.gionee.wms.web.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.wms.dto.ShiroUser;

public class ShiroLogoutFilter extends LogoutFilter {
	private static final Logger log = LoggerFactory.getLogger(ShiroLogoutFilter.class);

	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		Subject subject = getSubject(request, response);
		String redirectUrl = getRedirectUrl(request, response, subject);
		// try/catch added for SHIRO-298:
		try {
			ShiroUser user = (ShiroUser) subject.getPrincipal();
			if (StringUtils.isNotBlank(user.getSsoTgt())) {
				user.setSsoTgt(null);
			}
			subject.logout();

		} catch (SessionException ise) {
			log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
		}
		issueRedirect(request, response, redirectUrl);
		return false;
	}
}
