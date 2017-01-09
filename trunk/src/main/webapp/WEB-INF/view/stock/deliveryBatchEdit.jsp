<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />   
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<h2 class="contentTitle"></h2>
<form action="${ctx}/stock/deliveryBatch!confirm.action?callbackType=closeCurrent&navTabId=tab_delivery" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
<input type="hidden" name="id" value="${id}"/>


<div class="pageContent">
	<div class="pageFormContent" layoutH="97">
		<fieldset>
			<legend>基本信息</legend>
			<dl>
				<dt>发货批次：</dt>
				<dd><input readonly="true" name="deliveryBatch.batchCode" type="text" value="${deliveryBatch.batchCode }"/></dd>
			</dl>
			<dl>
				<dt>仓库：</dt>
				<dd>
					<s:select name="deliveryBatch.warehouseId" disabled="true" list="warehouseList" listKey="id" listValue="warehouseName" headerValue="请选择" headerKey=""/>
				</dd>
			</dl>
			<!--  
			<dl>
				<dt>经手人：</dt>
				<dd><input class="required" name="handledBy" id="handledBy" type="text" value="${handledBy }" id="handleBy"/></dd>
			</dl>
			<dl>
				<dt>出库日期：</dt>
				<dd>
					<input type="text" name="handledDate" class="required date" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${handledDate }" pattern="yyyy-MM-dd"/>" id="handledDate"/>
					<a class="inputDateButton" href="javascript:;">选择</a>
				</dd>
			</dl>-->
			<dl class="nowrap">
				<dt>备注：</dt>
				<dd><textarea name="deliveryBatch.remark" cols="80" rows="1" id="remark">${deliveryBatch.remark }</textarea></dd>
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
