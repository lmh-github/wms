<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>

<div class="panelBar">
	<span>共${quantity}条</span>
</div>
<table class="list" width="100%" layoutH="90">
	<thead>
		<tr align="center">
			<th>批次号</th>
			<th>订单号</th>
			<th>快递号</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="deliveryList" >
		<tr align="center">
			<td>${batchCode }</td>
			<td>${originalCode }</td>
			<td>${shippingNo }</td>
		</tr>
		</s:iterator>
	</tbody>
</table>