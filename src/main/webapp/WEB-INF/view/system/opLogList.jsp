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
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/report/salesOutReport!listSummary.action" method="post">
	<input type="hidden" name="stockCheckId" value="${stockCheckId }" />
	<div class="searchBar">
		<ul class="searchContent">
			<li>
				<label>业务类型：</label>
				<select class="required" name="warehouseCode">
					<option value="">请选择</option>
					<c:forEach items="${warehouseList}" var="item">
						<option value=${item.warehouseCode } ${(item.warehouseCode==warehouseCode)?"selected='true'":""}>${item.warehouseName }</option>
					</c:forEach>
				</select>
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
				<label>出库时间从：</label>
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
	<table class="list" width="1200" layoutH="138">
		<thead>
			<tr align="center">
				<th width="60">业务类型</th>
				<th width="60">业务关键标识</th>
				<th width="60">操作人</th>
				<th width="60">操作时间</th>
				<th width="60">操作IP</th>
				<th width="60">操作前内容</th>
				<th width="60">操作后内容</th>
				<th width="60">备注</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="summaryList" status="status">
			<tr target="sid_user" rel="${status.index }">
				<td>${opType }</td>
				<td>${opKey }</td>
				<td>${operator }</td>
				<td><fmt:formatDate value="${opTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${opIp }</td>
				<td>${beforeContent }</td>
				<td>${afterContent }</td>
				<td>${remark }</td>
			</tr>
			</s:iterator>
		</tbody>
	</table>
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