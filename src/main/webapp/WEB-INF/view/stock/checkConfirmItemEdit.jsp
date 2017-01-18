<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />   
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<h2 class="contentTitle">确认盘点商品</h2>
<form action="${ctx}/stock/stockCheck!confirm.action?callbackType=closeCurrent&navTabId=tab_checkItem" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
<input type="hidden" name="id" value="${id}"/>
<input type="hidden" name="check.checkType" value="${check.checkType}"/>
<input type="hidden" name="confirmType" value="${confirmType}"/>
<input type="hidden" name="skuCode" value="${skuCode}"/>
<input type="hidden" name="check.warehouseId" value="${check.warehouseId}"/>
<div class="pageContent">
	<div class="pageFormContent" layoutH="105">
		<fieldset>
			<dl>
				<dt>盘点编号：</dt>
				<dd>${check.checkCode }
			</dl>
			<dl class="nowrap">
				<s:if test="confirmType=='in'">
				<dt>入库备注：</dt>
				</s:if>
				<s:if test="confirmType=='out'">
				<dt>出库备注：</dt>
				</s:if>
				<dd><textarea name="remark" cols="50" rows="2" class="required">${remark}</textarea></dd>
			</dl>
		</fieldset>
	</div>
	<span style="color:red;"> * 确认盘点结果，对应商品库存数量将会根据实盘库存进行校准！！！</span>
	<div class="formBar">
		<ul>
			<li><div class="buttonActive"><div class="buttonContent"><button type="submit">确认</button></div></div></li>
			<li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
		</ul>
	</div>
</div>
</form>
