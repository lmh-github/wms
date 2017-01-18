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
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/report/financeReport!list.action" method="post">
	<div class="searchBar">
		<ul class="searchContent">
			<li style="width:380px;">
				<label>出库时间从：</label>
				<input type="text" name="shippingTimeBegin" class="date" size="15" dateFmt="yyyy-MM-dd" value="<fmt:formatDate value="${shippingTimeBegin }" pattern="yyyy-MM-dd"/>"/>
				到：
				<input type="text" name="shippingTimeEnd" class="date" size="15" dateFmt="yyyy-MM-dd" value="<fmt:formatDate value="${shippingTimeEnd }" pattern="yyyy-MM-dd"/>"/>
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
			<li><a class="icon" href="${ctx}/report/financeReport!list.action?exports=1" target="dwzExport" targettype="navTab"><span>导出excel</span></a></li>
		</ul>
	</div>
	<table class="list" width="100%" layoutH="115">
		<thead>
			<tr align="center">
				<th>订单日期</th>
				<th>订 单 号</th>
				<th>支付流水号</th>
				<th>机型</th>
				<th>物料编码</th>
				<th>数量</th>
				<th>单价</th>
				<th>金额</th>
				<th>是否开票</th>
				<th>发票金额</th>
				<th>渠道</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="statList" status="status">
			<tr target="sid_user" rel="${status.index }">
				<td><fmt:formatDate value="${shippingTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${orderCode }</td>
				<td>${payNo }</td>
				<td>${skuName }</td>
				<td>${materialCode }</td>
				<td>${quantity }</td>
				<td>${unitPrice }</td>
				<td>${orderAmount }</td>
				<td>${invoiceStatus }</td>
				<td>${invoiceAmount }</td>
				<td>${partnerName }</td>
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