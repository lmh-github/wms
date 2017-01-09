<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />   
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<h2 class="contentTitle">收货单</h2>
<form id="pagerForm" name="purchaseRecvForm" action="${ctx}/stock/purchaseRecv!createReceiveManual.action?callbackType=closeCurrent&navTabId=tab_purchaseRecv" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">

<div class="pageContent">
	<div class="pageFormContent" layoutH="97">
		<fieldset>
			<legend>基本信息</legend>
			<dl>
				<dt>仓库：</dt>
				<dd>
					<s:select name="receive.warehouseId" list="warehouseList" listKey="id" listValue="warehouseName" headerValue="请选择" headerKey=""/>
				</dd>
			</dl>
			<dl>
				<dt>供应商：</dt>
				<dd>
					<s:select name="receive.supplierId" list="supplierList" listKey="id" listValue="supplierName" headerValue="请选择" headerKey=""/>
				</dd>
			</dl>
			<dl>
				<dt>采购类型：</dt>
				<dd>
					<select name="receive.receiveType">
					<option value="101"><s:property value="@com.gionee.wms.common.WmsConstants$ReceiveType@PURCHASE.name"/></option>
					<option value="105" ><s:property value="@com.gionee.wms.common.WmsConstants$ReceiveType@PURCHARMA.name"/></option>
					<option value="106"><s:property value="@com.gionee.wms.common.WmsConstants$ReceiveType@SHUADAN.name"/></option>
				</select>
				</dd>
			</dl>
			<dl class="nowrap">
				<dt>备注：</dt>
				<dd><textarea name="receive.remark" cols="80" rows="1"></textarea></dd>
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
