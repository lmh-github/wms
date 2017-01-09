<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />   
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<h2 class="contentTitle">确认盘点单</h2>
<form action="${ctx}/stock/stockCheck!confirmCheck.action?callbackType=closeCurrent&navTabId=tab_checkGoods" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
<input type="hidden" name="check.id" value="${id}"/>
<input type="hidden" name="check.checkType" value="${check.checkType}"/>
<input type="hidden" name="check.handlingStatus" value="${check.handlingStatus }"/>
<div class="pageContent">
	<div class="pageFormContent" layoutH="105">
		<fieldset>
			<dl>
				<dt>盘点编号：</dt>
				<dd>${check.checkCode }
			</dl>
			<dl>
				<dt>盘点仓库：</dt>
				<dd>${check.warehouseName }</dd>
			</dl>
			<dl class="nowrap">
				<dt>备注：</dt>
				<dd><textarea name="check.remark" cols="50" rows="2">${check.remark }</textarea></dd>
			</dl>
		</fieldset>
	</div>
	<span style="color:red;"> * 确认盘点结果，确认后盘点单不可再修改！！！</span>
	<div class="formBar">
		<ul>
			<li><div class="buttonActive"><div class="buttonContent"><button type="submit">确认</button></div></div></li>
			<li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
		</ul>
	</div>
</div>
</form>
