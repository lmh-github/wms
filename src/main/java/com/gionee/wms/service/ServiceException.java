package com.gionee.wms.service;

/**
 * 
 * @ClassName: ServiceException 
 * @Description: 公共服务异常类 
 * @author Kevin
 * @date 2013-4-18 上午09:41:55 
 *
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 5153180652772541214L;

	public ServiceException() {
		super();
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
