<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<h2 class="contentTitle">打印模板</h2>

<div class="pageFormContent" layoutH="60">
	<fieldset>
		<legend>运单模板</legend>
		<dl class="nowrap">
			<dt>顺丰快递：</dt>
			<dd><a title="编辑顺丰运单模板" target="navTab" rel="tab_printableTemplateInput" href="${ctx}/basis/printableTemplate!inputShippingTemplate.action?templateName=shippingSF_gionee.ftl" class="btnEdit"></a></dd>
		</dl>
		<dl class="nowrap">
			<dt>申通快递：</dt>
			<dd><a title="编辑申通运单模板" target="navTab" rel="tab_printableTemplateInput" href="${ctx}/basis/printableTemplate!inputShippingTemplate.action?templateName=shippingST.ftl" class="btnEdit"></a></dd>
		</dl>
		<dl class="nowrap">
			<dt>EMS：</dt>
			<dd><a title="编辑EMS运单模板" target="navTab" rel="tab_printableTemplateInput" href="${ctx}/basis/printableTemplate!inputShippingTemplate.action?templateName=shippingEMS_gionee.ftl" class="btnEdit"></a></dd>
		</dl>
		<dl class="nowrap">
			<dt>圆通快递：</dt>
			<dd><a title="编辑圆通运单模板" target="navTab" rel="tab_printableTemplateInput" href="${ctx}/basis/printableTemplate!inputShippingTemplate.action?templateName=shippingYT.ftl" class="btnEdit"></a></dd>
		</dl>
	</fieldset>
	
	<fieldset>
		<legend>其它模板</legend>
		<dl class="nowrap">
			<dt>购物清单：</dt>
			<dd><a title="编辑购物清单模板" target="navTab" rel="tab_printableTemplateInput" href="${ctx}/basis/printableTemplate!inputShoppingListTemplate.action" class="btnEdit"></a></dd>
		</dl>
	</fieldset>
</div>