package com.gionee.wms.service.basis;

import com.gionee.wms.service.ServiceException;

public interface TemplateService {
	/**
	 * 读取打印模板的内容
	 */
	String readContentFromTemplate(String templatePath) throws ServiceException;

	/**
	 * 写入内容到打印模板
	 */
	void writeContentToTemplate(String templatePath,String templateContent) throws ServiceException;
}
