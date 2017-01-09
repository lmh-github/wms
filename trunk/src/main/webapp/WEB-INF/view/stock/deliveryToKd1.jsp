<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="pageContent nowrap">
	<form action="${ctx}/stock/delivery!genDeliveryCode.action?callbackType=forward&forwardUrl=${ctx}/stock/delivery!deliveryToKd.action" method="post"  onsubmit="return validateCallback(this, navTabAjaxDone)" novalidate="novalidate" class="pageForm required-validate">
		<div class="pageFormContent" layoutH="900">
			<!-- 
			<dl>
				<dt>仓库: </dt>
				<dd><s:select name="warehouseId" list="warehouseList" listKey="id" listValue="warehouseName" headerValue="请选择" headerKey=""/>
					<span class="info">*如不选择仓库按默认仓库出货</span></dd>
			</dl>
			 -->
			<dl>
				<dt>批次号: </dt>
				<dd><input type="text" class="required digits" id="batchCode" name="batchCode" maxlength="10" min="1" max="99"/></dd>
			</dl>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">创建发货批次</button></div></div></li>
			</ul>
		</div>
	</form>
</div>
