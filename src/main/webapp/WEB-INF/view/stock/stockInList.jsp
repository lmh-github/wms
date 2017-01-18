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
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/stock/stockIn.action" method="post">
	<div class="searchBar">
		<ul class="searchContent">
			<li>
				<label>仓库：</label>
				<select class="required" name="warehouse.warehouseCode">
					<option value="">请选择</option>
					<c:forEach items="${warehouseList}" var="item">
						<option value=${item.warehouseCode } ${(item.warehouseCode==warehouse.warehouseCode)?"selected='true'":""}>${item.warehouseName }</option>
					</c:forEach>
				</select>
			</li>
			<li>
				<label>入库类型：</label>
				<s:select name="stockInType" list="stockInTypes" listKey="code" listValue="name" headerValue="请选择" headerKey=""/>  
			</li>
			<li>
				<label>入库单编号：</label>
				<input type="text" name=stockInCode value="${stockInCode}"/>
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
			<li>
			<!-- <a class="add" href="${ctx}/stock/stock!input.action" target="dialog" rel="dlg_stock" mask="true" width="900" height="800"><span>添加库存信息</span></a> -->
			</li>
		</ul>
	</div>
	<table class="table" width="1200" layoutH="138">
		<thead>
			<tr>
				<th width="50">入库类型</th>
				<th width="50">入库单号</th>
				<th width="100">入库日期</th>
				<th width="50">仓库</th>
				<th width="50">来往</th>
				<th width="50">原始单号</th>
				<th width="50">经手人</th>
				<th width="50">制单人</th>
				<th width="50">制单时间</th>
				<th width="50">处理状态</th>
				<th width="200">备注</th>
				<th width="50">操作</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="stockInList" >
			<tr target="sid_user" rel="1">
				<td>
					<s:if test="stockInType==@com.gionee.wms.common.WmsConstants$ReceiveType@PURCHASE.code">采购入库</s:if>
					<s:elseif test="stockInType==@com.gionee.wms.common.WmsConstants$ReceiveType@RMA.code">退货入库</s:elseif>
				</td>
				<td>${stockInCode }</td>
				<td><fmt:formatDate value="${handledDate}" pattern="yyyy-MM-dd"/></td>
				<td>${warehouse.warehouseName }</td>
				<td>${opposite }</td>
				<td>${originalCode }</td>
				<td>${handledBy }</td>
				<td>${preparedBy }</td>
				<td><fmt:formatDate value="${preparedTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>
					<c:if test="${handlingStatus==0}">待处理</c:if>
					<c:if test="${handlingStatus==1}">已完成</c:if>
				</td>
				<td>${remark }</td>
				<td>
					<s:if test="stockInType==@com.gionee.wms.common.WmsConstants$ReceiveType@PURCHASE.code">
						<c:if test="${handlingStatus==0}">
						<a title="确实要删除吗？" target="ajaxTodo" href="${ctx}/stock/stockIn!delete.action?id=${id}&navTabId=tab_stockIn" class="btnDel"></a>
						<a title="编辑" target="navTab" rel="tab_inputPurchaseIn" href="${ctx}/stock/stockIn!inputPurchaseIn.action?stockInCode=${stockInCode}" class="btnEdit"></a>
						</c:if>
						<c:if test="${handlingStatus==1}">
						<a title="查看" target="navTab" rel="tab_showPurchaseIn" href="${ctx}/stock/stockIn!showPurchaseIn.action?id=${id}" class="btnView"></a>
						</c:if>
					</s:if>
					<s:elseif test="stockInType==@com.gionee.wms.common.WmsConstants$ReceiveType@RMA.code">
						<c:if test="${handlingStatus==0}">
						<a title="确实要删除吗？" target="ajaxTodo" href="${ctx}/stock/rmaIn!delete.action?id=${id}&navTabId=tab_stockIn" class="btnDel"></a>
						<a title="编辑" target="navTab" rel="tab_inputRmaIn" href="${ctx}/stock/rmaIn!inputRmaIn.action?stockInCode=${stockInCode}" class="btnEdit"></a>
						</c:if>
						<c:if test="${handlingStatus==1}">
						<a title="查看" target="navTab" rel="tab_showRmaIn" href="${ctx}/stock/rmaIn!show.action?id=${id}" class="btnView"></a>
						</c:if>
					</s:elseif>
					
				</td>

			</tr>
			</s:iterator>
			
		</tbody>
	</table>
	<div class="panelBar">
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value})">
				<option value="20">20</option>
				<option value="50">50</option>
				<option value="100">100</option>
				<option value="200">200</option>
			</select>
			<span>条，共${page.totalRow}条</span>
		</div>
		
		<div class="pagination" targetType="navTab" totalCount="${page.totalRow }" numPerPage="${page.pageSize }" pageNumShown="10" currentPage="${page.currentPage}"></div>

	</div>
</div>