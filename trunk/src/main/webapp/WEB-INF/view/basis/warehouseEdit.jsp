<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<h2 class="contentTitle"></h2>
<form action="<s:if test='id == null'>${ctx}/basis/warehouse!add.action?callbackType=closeCurrent&navTabId=tab_warehouse</s:if><s:else>${ctx}/basis/warehouse!update.action?callbackType=closeCurrent&navTabId=tab_warehouse</s:else>" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
<input type="hidden" name="id" value="${id}"/>
<div class="pageContent">
	<div class="pageFormContent" layoutH="97">
		<fieldset>
			<legend></legend>
			<dl class="nowrap">
				<dt>仓库编号：</dt>
				<dd><input name="warehouseCode" ${id==null?"":"readonly='readonly'" } class="required" type="text" maxlength="10" size="20" value="${warehouseCode }"/></dd>
			</dl>
			<dl>
				<dt>仓库名称：</dt>
				<dd><input name="warehouseName" ${id==null?"":"readonly='readonly'" } class="required" type="text" maxlength="10" size="20" value="${warehouseName }" /></dd>
			</dl>
			<dl>
				<dt>仓库地址：</dt>
				<dd><input name="warehouseAddress" type="text" maxlength="50" size="30" value="${warehouseAddress }" /></dd>
			</dl>
			<dl>
				<dt>仓库电话：</dt>
				<dd><input name="warehousePhone" type="text" maxlength="20" size="20" value="${warehousePhone }" /></dd>
			</dl>
			<dl>
				<dt>仓库联系人：</dt>
				<dd><input name="warehouseContact" type="text" maxlength="10" size="20" value="${warehouseContact }" /></dd>
			</dl>
			<dl class="nowrap">
				<dt>仓库类型：</dt>
				<dd>
					<label><input type="radio" name="warehouseType" class="required" value="1" ${warehouseType==1?'checked="checked"':''}/>实仓</label>
					<label><input type="radio" name="warehouseType" class="required" value="0" ${warehouseType==0?'checked="checked"':''}/>虚仓</label>
				</dd>
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




