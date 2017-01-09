<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />   
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="pageContent">
	<form method="post" action="${ctx}/stock/salesOrder!updateLogisticsInfo.action?callbackType=closeCurrent&navTabId=tab_delivery" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="56">
			<p>
				<label>订单号：</label>
				<input type="text" name="orderCode" class="readonly" readonly="readonly" value="${orderCode }" />
			</p>
			<p>
				<label>快递公司：</label>
				<select class="required" name="logistics.logisticsCode">
					<c:forEach items="${logisticsList}" var="item">
						<option value=${item.logisticsCode } ${(item.logisticsCode==logistics.logisticsCode)?"selected='true'":""}>${item.logisticsName }</option>
					</c:forEach>
				</select>
			</p>
			<p>
				<label>快递单号：</label>
				<input type="text" name="trackingNumber" class="required alphanumeric" maxlength="15" value="${trackingNumber }"/>
			</p>
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
