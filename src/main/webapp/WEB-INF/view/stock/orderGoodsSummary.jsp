<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="pageContent">
	<div style="height: 90px;"></div>
	<div id="page1">
	<div id="orderGoodsSummary">
	<div align="center">拣货编号：${batchCode}&nbsp;&nbsp;&nbsp;订单数量：${orderNum}</div>
	<table width="100%" layoutH="90" border="1" cellspacing="0">
		<thead>
			<tr align="center">
				<th>SKU编码</th>
				<th>SKU名称</th>
				<th>单位</th>
				<th>汇总数量</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="orderGoodsSummary" status="status">
			<tr target="sid_user" rel="${status.index }" align="center">
				<td>${skuCode }</td>
				<td>${skuName }</td>
				<td>${measureUnit }</td>
				<td>${quantity }</td>
			</tr>
			</s:iterator>
		</tbody>
	</table>
	</div>
	</div>
</div>