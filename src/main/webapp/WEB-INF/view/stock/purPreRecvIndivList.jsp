<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div class="pageContent" style="height: 100%;">
	<table class="list" width="100%">
		<thead>
			<tr align="center">
				<th>商品编码</th>
				<th>SKU编码</th>
				<th>SKU名称</th>
				<th>生产批次号</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="indivList" status="status">
			<tr target="sid_user" rel="${status.index }"  align="center">
				<td>${indivCode }</td>
				<td>${skuCode }</td>
				<td>${skuName }</td>
				<td>${productBatchNo }</td>
			</tr>
			</s:iterator>
		</tbody>
	</table>
</div>