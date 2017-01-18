<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<h2 class="contentTitle"></h2>
<form action="<s:if test='id == null'>${ctx}/wares/skuMap!add.action?callbackType=closeCurrent&navTabId=tab_skuMap</s:if><s:else>${ctx}/wares/skuMap!update.action?callbackType=closeCurrent&navTabId=tab_skuMap</s:else>" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
<input type="hidden" name="skuMap.id" value="${id}"/>
<div class="pageContent">
	<div class="pageFormContent" layoutH="97">
		<fieldset>
			<legend></legend>
			<dl>
				<dt>系统SKU编码：</dt>
				<dd>
				<input name="skuMap.skuCode" id="skuCode" class="required" type="text" maxlength="10" size="20" value="${skuMap.skuCode }" ${editEnabled?"":"readonly='readonly'"} bringBackName="sku.skuCode" lookupGroup="sku" />
				<c:if test="${editEnabled}">
					<a class="btnLook" href="${ctx}/wares/sku!lookup.action" lookupGroup="sku" width="1200">查找SKU</a>
				</c:if>
				<span class="info">(查找SKU)</span>	
				</dd>
			</dl>
			<dl>
				<dt>第三方SKU编码：</dt>
				<dd><input name="skuMap.outerSkuCode" id="outerSkuCode" class="required" type="text" maxlength="20" size="20" value="${skuMap.outerSkuCode }" ${editEnabled?"":"readonly='readonly'"} /></dd>
			</dl>
			<dl>
				<dt>第三方公司编码：</dt>
				<dd>
				   <!-- 
				   <input name="skuMap.outerCode" id="outerCode" class="required" type="text" maxlength="13" size="20" value="${skuMap.outerCode }" ${editEnabled?"":"readonly='readonly'"}/>(唯品会:vip)
				  -->
				<select id='outerCode' name="skuMap.outerCode" ${editEnabled?"":"disabled='disabled'"}>
                    <option value="vip" <c:if test="${skuMap.outerCode == 'vip'}">selected</c:if>>vip(唯品会)</option>
                    <option value="sf" <c:if test="${skuMap.outerCode == 'sf'}">selected</c:if>>sf(顺丰)</option>
				</select>
				</dd>
			</dl>
		</fieldset>	
	</div>
	<div class="formBar">
		<ul>
			<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
			<li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
		</ul>
	</div>
</div>
</form>




