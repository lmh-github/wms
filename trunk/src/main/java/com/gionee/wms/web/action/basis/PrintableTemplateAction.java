package com.gionee.wms.web.action.basis;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.PermissionConstants;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.account.AccountService;
import com.gionee.wms.service.basis.TemplateService;
import com.gionee.wms.web.AccessException;
import com.gionee.wms.web.action.AjaxActionSupport;

@Controller("PrintableTemplateAction")
@Scope("prototype")
public class PrintableTemplateAction extends AjaxActionSupport {
	private static final Logger logger = LoggerFactory.getLogger(PrintableTemplateAction.class);

	private TemplateService templateService;
	private AccountService accountService;
	private String templateName;

	/** 页面相关属性 **/
	private String templateContent; // 模板内容
	
	@Override
	public String execute()throws Exception{
		return SUCCESS;
	}

	/**
	 * 进入购物清单模板编辑界面
	 */
	public String inputShoppingListTemplate() throws Exception {
		try {
			String templatePath = ActionUtils.getProjectPath() + "WEB-INF/ftl/shoppingList.ftl";
			templateContent = templateService.readContentFromTemplate(templatePath);
		} catch (ServiceException e) {
			logger.error("读取模板内容时出错", e);
			ajaxError("读取模板内容失败：" + e.getMessage());
			return null;
		}
		return "input_shopping_list";
	}

	/**
	 * 进入运单模板编辑界面
	 */
	public String inputShippingTemplate() throws Exception {
		Validate.notEmpty(templateName);
		try {
			String templatePath = ActionUtils.getProjectPath() + "/WEB-INF/ftl/" + templateName;
			templateContent = templateService.readContentFromTemplate(templatePath);
		} catch (ServiceException e) {
			logger.error("读取模板内容时出错", e);
			ajaxError("读取模板内容失败：" + e.getMessage());
			return null;
		}
		return "input_shipping";
	}

	/**
	 * 更新购物清单模板
	 */
	public String updateShoppingListTemplate() throws Exception {
		if (!accountService.isPermitted(PermissionConstants.TEMPLATE_SHOPPING_LIST_EDIT)) {
			throw new AccessException();
		}
		Validate.notEmpty(templateContent);
		try {
			String templatePath = request.getRealPath(WmsConstants.SHOPPING_LIST_FTL_PATH);
			templateService.writeContentToTemplate(templatePath, templateContent);
			ajaxSuccess("保存模板成功");
		} catch (Exception e) {
			logger.error("保存模板内容时出错", e);
			ajaxError("保存模板内容失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 更新运单模板
	 */
	public String updateShippingTemplate() throws Exception {
		if (!accountService.isPermitted(PermissionConstants.TEMPLATE_SHIPPING_EDIT)) {
			throw new AccessException();
		}
		Validate.notEmpty(templateName);
		Validate.notEmpty(templateContent);
		try {
			String templatePath = ActionUtils.getProjectPath() + "/WEB-INF/ftl/" + templateName;
			templateService.writeContentToTemplate(templatePath, templateContent);
			ajaxSuccess("保存模板成功");
		} catch (Exception e) {
			logger.error("保存模板内容时出错", e);
			ajaxError("保存模板内容失败：" + e.getMessage());
		}
		return null;
	}

	public String getTemplateContent() {
		return templateContent;
	}

	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	@Autowired
	public void setTemplateService(TemplateService templateService) {
		this.templateService = templateService;
	}

	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
}
