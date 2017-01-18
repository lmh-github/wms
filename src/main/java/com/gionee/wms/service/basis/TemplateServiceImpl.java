package com.gionee.wms.service.basis;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.service.ServiceException;

@Service("templateService")
public class TemplateServiceImpl implements TemplateService {

	@Override
	public String readContentFromTemplate(String templatePath) throws ServiceException {
		//String templatePath = getShoppingListTemplatePath(request);
		try {
			return FileUtils.readFileToString(new File(templatePath), WmsConstants.DEFAULT_ENCODING);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void writeContentToTemplate(String templatePath,String templateContent) throws ServiceException {
		//String templatePath = getShoppingListTemplatePath(request);
		try {
			FileUtils.writeStringToFile(new File(templatePath), templateContent, WmsConstants.DEFAULT_ENCODING);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
	}

	@Deprecated
	private String getShoppingListTemplatePath(HttpServletRequest request) {
		//return ActionUtils.getClassPath() + WmsConstants.SHOPPING_LIST_FTL_PATH;
/*		String s=request.getContextPath() + WmsConstants.SHOPPING_LIST_FTL_PATH;
		String s2=request.getRealPath(WmsConstants.SHOPPING_LIST_FTL_PATH);
		String s3=request.getRequestURI();
		String s4=request.getRequestURL().toString();
		String s5=request.getPathInfo();*/
		return request.getRealPath(WmsConstants.SHOPPING_LIST_FTL_PATH);
	}

}
