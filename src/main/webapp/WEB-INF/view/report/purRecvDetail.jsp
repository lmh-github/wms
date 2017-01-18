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
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/report/receiveReport!listPurRecvDetail.action" method="post">
	<input type="hidden" name="stockCheckId" value="${stockCheckId }" />
	<div class="searchBar">
		<ul class="searchContent">
			<li>
				<label>仓库：</label>
				<s:select name="warehouseId" list="warehouseList" listKey="id" listValue="warehouseName" headerValue="请选择" headerKey=""/>
			</li>
			<li>
				<label>SKU编码：</label>
				<input type="text" name="skuCode" value="${skuCode}"/>
			</li>
			<li>
				<label>SKU名称：</label>
				<input type="text" name="skuName" value="${skuName}"/>
			</li>
			<li style="width:380px;">
				<label>入库日期从：</label>
				<input type="text" name="finishedTimeBegin" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${finishedTimeBegin }" pattern="yyyy-MM-dd"/>"/>
				到：
				<input type="text" name="finishedTimeEnd" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${finishedTimeEnd }" pattern="yyyy-MM-dd"/>"/>
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
			<li></li>
		</ul>
	</div>
	<table class="list" width="100%" layoutH="115">
		<thead>
			<tr align="center">
				<th>供应商</th>
				<th>收货时间</th>
				<th>仓库</th>
				<th>收货编号</th>
				<th>SKU编码</th>
				<th>SKU名称</th>
				<th>单位</th>
				<th>数量</th>
				<th>备注</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="goodsList" status="status">
			<tr target="sid_user" rel="${status.index }">
				<td>${receive.supplierName }</td>
				<td><fmt:formatDate value="${receive.handledTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${receive.warehouseName }</td>
				<td>${receive.receiveCode }</td>
				<td>${skuCode }</td>
				<td>${skuName }</td>
				<td>${measureUnit }</td>
				<td align="right">${quantity }</td>
				<td>${remark }</td>
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