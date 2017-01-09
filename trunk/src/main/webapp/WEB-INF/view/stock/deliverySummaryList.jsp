<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<form id="pagerForm" method="post" action="#rel#">
	<input type="hidden" name="page.currentPage" value="1" />
	<input type="hidden" name="page.pageSize" value="${page.pageSize}" />
</form>

<div class="pageHeader">
	<div class="searchBar">
		<ul class="searchContent">
			<li>
				<label>出库编号：</label>
				<input type="text" value="${deliveryBatch.batchCode}" readonly="true"/>
			</li>
			<li>
				<label>仓库：</label>
				<input type="text" value="${deliveryBatch.warehouseName}" readonly="true"/>
			</li>
		</ul>
	</div>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
		<shiro:hasPermission name="out:printSalesOutSummary">
			<li><a class="icon" href="javascript:$.printBox('deliverySummary')"><span>打印</span></a></li>
		</shiro:hasPermission>
		</ul>
	</div>
	<div id="deliverySummary">
	<table class="list" width="100%" layoutH="90">
		<thead>
			<tr align="center">
				<th>SKU编码</th>
				<th>SKU名称</th>
				<th>单位</th>
				<th>汇总数量</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="deliverySummaryList" status="status">
			<tr target="sid_user" rel="${status.index }">
				<td>${skuCode }</td>
				<td>${skuName }</td>
				<td>${measureUnit }</td>
				<td>${quantity }</td>
			</tr>
			</s:iterator>
		</tbody>
	</table>
	</div>
	<div class="panelBar">
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value})">
				<option value="1" ${page.pageSize==1?"selected='true'":""}>1</option>
				<option value="50" ${page.pageSize==50?"selected='true'":""}>50</option>
				<option value="100" ${page.pageSize==100?"selected='true'":""}>100</option>
				<option value="200" ${page.pageSize==200?"selected='true'":""}>200</option>
			</select>
			<span>条，共${page.totalRow}条</span>
		</div>
		
		<div class="pagination" targetType="navTab" totalCount="${page.totalRow }" numPerPage="${page.pageSize }" pageNumShown="10" currentPage="${page.currentPage}"></div>

	</div>
</div>