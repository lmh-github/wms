<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />   
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="pageContent">
	<form method="post" action="${ctx}/stock/delivery!updateShippingInfo.action?callbackType=closeCurrent&navTabId=tab_delivery" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<input type="hidden" name="id" value="${id}"/>
		<div class="pageFormContent" layoutH="56">
			<p>
				<label>发货编号：</label>
				<input type="text" name="delivery.deliveryCode" class="readonly" readonly="readonly" value="${delivery.deliveryCode }" />
			</p>
			<p>
				<label>配送方式：</label>
				<select class="required" name="delivery.shippingId">
					<c:forEach items="${shippingList}" var="item">
						<option value=${item.id } ${(item.id==delivery.shippingId)?"selected='true'":""}>${item.shippingName }</option>
					</c:forEach>
				</select>
			</p>
			<p>
				<label>配送单号：</label>
				<input type="text" name="delivery.shippingNo" class="required alphanumeric" minlength="10" maxlength="15" value="${delivery.shippingNo }"/>
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
