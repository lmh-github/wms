<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<h2 class="contentTitle"></h2>
<form action="<s:if test='id == null'>${ctx}/basis/supplier!add.action?callbackType=closeCurrent&navTabId=tab_supplier</s:if><s:else>${ctx}/basis/supplier!update.action?callbackType=closeCurrent&navTabId=tab_supplier</s:else>" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
<input type="hidden" name="id" value="${id}"/>
<div class="pageContent">
	<div class="pageFormContent" layoutH="97">
		<fieldset>
			<legend></legend>
			<dl class="nowrap">
				<dt>供应商编号：</dt>
				<dd><input name="supplierCode" ${id==null?"":"readonly='readonly'" } class="required" type="text" maxlength="10" size="20" value="${supplierCode }"/></dd>
			</dl>
			<dl>
				<dt>供应商名称：</dt>
				<dd><input name="supplierName" ${id==null?"":"readonly='readonly'" } class="required" type="text" maxlength="10" size="20" value="${supplierName }" /></dd>
			</dl>
			<dl>
				<dt>供应商地址：</dt>
				<dd><input name="supplierAddress" type="text" maxlength="50" size="30" value="${supplierAddress }" /></dd>
			</dl>
			<dl>
				<dt>供应商电话：</dt>
				<dd><input name="supplierPhone" type="text" maxlength="20" size="20" value="${supplierPhone }" /></dd>
			</dl>
			<dl>
				<dt>供应商联系人：</dt>
				<dd><input name="supplierContact" type="text" maxlength="10" size="20" value="${supplierContact }" /></dd>
			</dl>
			<dl class="nowrap">
				<dt>备注：</dt>
				<dd><textarea name="remark" cols="80" rows="1">${remark }</textarea></dd>
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




