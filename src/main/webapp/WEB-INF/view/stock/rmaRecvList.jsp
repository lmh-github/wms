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
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/stock/rmaRecv.action" method="post">
	<div class="searchBar">
		<ul class="searchContent" style="height: 50%;">
			<li>
				<label>仓库：</label>
				<s:select name="warehouseId" list="warehouseList" listKey="id" listValue="warehouseName" headerValue="请选择" headerKey=""/>
			</li>
			<li>
				<label>退货编号：</label>
				<input type="text" name="receiveCode" value="${receiveCode}"/>
			</li>
			<li style="width:380px;">
				<label>制单时间从：</label>
				<input type="text" name="preparedTimeBegin" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${preparedTimeBegin }" pattern="yyyy-MM-dd"/>"/>
				到：
				<input type="text" name="preparedTimeEnd" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${preparedTimeEnd }" pattern="yyyy-MM-dd"/>"/>
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
		<!-- 
			<li><a class="add" href="${ctx}/stock/rmaRecv!inputInit.action" rel="dlg_rmaRecvInit" target="dialog" mask="true" width="1024" height="700"><span>创建退货入库</span></a></li>
			<li class="line">line</li> -->
			<li><a class="edit" href="${ctx}/stock/rmaRecv!input.action?id={sid_rmaRecv}" rel="dlg_rmaRecvEdit" target="dialog" mask="true" width="1024" height="600" warn="请选择退货单"><span>查看详情</span></a></li>
		</ul>
	</div>
	<table class="list" width="100%" layoutH="138">
		<thead>
			<tr align="center">
				<th>制单时间</th>
				<th>退货编号</th>
				<th>仓库</th>
				<th>销售订单号</th>
				<th>制单人</th>
				<th>备注</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="receiveList" status="status">
			<tr target="sid_rmaRecv" rel="${id }" align="center">
				<td><fmt:formatDate value="${preparedTime }" pattern="yyyy-MM-dd"/></td>
				<td>${receiveCode }</td>
				<td>${warehouseName }</td>
				<td>${originalCode }</td>
				<td>${preparedBy }</td>
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