<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="pageContent">
	<form method="post" action="<s:if test='id == null'>${ctx}/wares/attrSet!add.action?callbackType=closeCurrent&navTabId=tab_attrSet</s:if><s:else>${ctx}/wares/attrSet!update.action?callbackType=closeCurrent&navTabId=tab_attrSet</s:else>" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
		<input type="hidden" name="id" value="${id}"/>
		<div class="pageFormContent" layoutH="56">
			<fieldset>
				<legend></legend>
				<dl class="nowrap">
					<dt>类型名称：</dt>
					<dd><input name="attrSetName" class="required" type="text" maxlength="20" size="30" value="${attrSetName}" alt="请输入分类名称"/></dd>
				</dl>
				<dl class="nowrap">
					<dt>类型描述：</dt>
					<dd><textarea name="remark" cols="80" rows="2">${remark }</textarea></dd>
				</dl>
			</fieldset>
		</div>
		<div class="formBar">
			<ul>
				<!--<li><a class="buttonActive" href="javascript:;"><span>保存</span></a></li>-->
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
</div>
