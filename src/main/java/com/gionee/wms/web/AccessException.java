package com.gionee.wms.web;

import org.apache.shiro.authz.UnauthorizedException;

/**
 * 
 * @ClassName: ServiceException
 * @Description: 公共访问异常类
 * @author Kevin
 * @date 2013-4-18 上午09:41:55
 * 
 */
public class AccessException extends UnauthorizedException {
	private static final long serialVersionUID = 6749927827951239780L;

	public AccessException() {
		super();
	}

	public AccessException(String message) {
		super(message);
	}

	public AccessException(Throwable cause) {
		super(cause);
	}

	public AccessException(String message, Throwable cause) {
		super(message, cause);
	}
}
