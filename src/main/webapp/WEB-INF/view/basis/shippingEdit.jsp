<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<h2 class="contentTitle"></h2>
<form action="<s:if test='id == null'>${ctx}/basis/shipping!add.action?callbackType=closeCurrent&navTabId=tab_shipping</s:if><s:else>${ctx}/basis/shipping!update.action?callbackType=closeCurrent&navTabId=tab_shipping</s:else>" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
<input type="hidden" name="id" value="${id}"/>
<div class="pageContent">
	<div class="pageFormContent" layoutH="97">
		<fieldset>
			<legend>基本信息</legend>
			<dl class="nowrap">
				<dt>配送方式编号：</dt>
				<dd><input name="shippingCode" ${id==null?"":"readonly='readonly'" } class="required" type="text" maxlength="20" size="20"  value="${shippingCode }"/></dd>
			</dl>
			<dl class="nowrap">
				<dt>配送公司代码：</dt>
				<dd><input name="companyCode" class="required" type="text" maxlength="20" size="20" value="${companyCode }"/></dd>
			</dl>
			<dl>
				<dt>配送方式名称：</dt>
				<dd><input name="shippingName" class="required" type="text" maxlength="10" size="20" value="${shippingName }" /></dd>
			</dl>
			<dl>
				<dt>联系电话：</dt>
				<dd><input name="phone" type="text" maxlength="20" size="20" value="${phone }" /></dd>
			</dl>
			<dl>
				<dt>联系人：</dt>
				<dd><input name="contact" type="text" maxlength="10" size="20" value="${contact }" /></dd>
			</dl>
			<dl class="nowrap">
				<dt>备注：</dt>
				<dd><textarea name="remark" cols="80" rows="1">${remark }</textarea></dd>
			</dl>
		</fieldset>
		
		<fieldset>
			<legend>模板设置</legend>
			<dl>
				<dt>运单模板名称：</dt>
				<dd><input name="templateName" type="text" maxlength="30" size="50" value="${templateName }" /></dd>
			</dl>
		</fieldset>
		
		<div class="tabs">
		</div>
		
	</div>
	<div class="formBar">
		<ul>
			<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
			<li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
		</ul>
	</div>
</div>
</form>




