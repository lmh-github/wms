<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<form id="pagerForm" method="post" action="#rel#">
	<input type="hidden" name="page.currentPage" value="1" />
	<input type="hidden" name="page.pageSize" value="${page.pageSize}" />
</form>

<div class="pageHeader">
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/report/dailyStockDetail.action" method="post">
	<div class="searchBar">
		<ul class="searchContent">
			<li>
				<label>SKU编码：</label>
				<input type="text" name="skuCode" value="${skuCode}"/>
			</li>
			<li style="width:380px;">
				<label>报表日期 从：</label>
				<input type="text" name="startDate" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${startDate }" pattern="yyyy-MM-dd"/>"/>
				到：
				<input type="text" name="endDate" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${endDate }" pattern="yyyy-MM-dd"/>"/>
			</li>
		</ul>
		<div class="subBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">检索</button></div></div></li>
			</ul>
		</div>
	</div>
	</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li class="">
			<a class="icon" href="/wms/report/dailyStockDetail!exportExcel.action" target="dwzExport" targettype="navTab">
			<span>导出excel</span></a>
			</li>
		</ul>
	</div>
	<table class="list" width="100%" layoutH="115">
		<thead>
			<tr align="center">
				<th>SKU编码</th>
				<th>报表日期</th>
				<th>期初总库存</th>
				<th>本期出库</th>
				<th>未发订单占用数</th>
				<th>期末库存</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="dailyStockList" status="status">
			<tr target="sid_user" rel="${status.index }">
				<td>${skuCode}</td>
				<td><fmt:formatDate value="${reportDate}" pattern="yyyy-MM-dd"/></td>
				<td>${startStockQty }</td>
				<td>${outStockQty}</td>
				<td>${occupyStockQty}</td>
				<td>${endStockQty}</td>
			</tr>
			</s:iterator>
		</tbody>
	</table>
	<div class="panelBar">
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value})">
				<option value="20" ${page.pageSize==20?"selected='true'":""}>20</option>
				<option value="50" ${page.pageSize==50?"selected='true'":""}>50</option>
				<option value="100" ${page.pageSize==100?"selected='true'":""}>100</option>
				<option value="200" ${page.pageSize==200?"selected='true'":""}>200</option>
			</select>
			<span>条，共${page.totalRow}条</span>
		</div>
		
		<div class="pagination" targetType="navTab" totalCount="${page.totalRow }" numPerPage="${page.pageSize }" pageNumShown="10" currentPage="${page.currentPage}"></div>

	</div>
</div>