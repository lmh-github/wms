﻿<%@ page contentType="text/html;charset=UTF-8" %>
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
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/stock/salesOrder.action" method="post">
	<input type="hidden" name="stockCheckId" value="${stockCheckId }" />
	<div class="searchBar">
		<ul class="searchContent">
			<li style="width:380px;">
				<label>订单时间从：</label>
				<input type="text" name="orderTimeBegin" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${orderTimeBegin }" pattern="yyyy-MM-dd"/>"/>
				到：
				<input type="text" name="orderTimeEnd" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${orderTimeEnd }" pattern="yyyy-MM-dd"/>"/>
			</li>
			<li>
				<label>订单状态：</label>
				<s:select name="orderStatus" list="@com.gionee.wms.common.WmsConstants$OrderStatus@values()" listKey="code" listValue="name" headerValue="请选择" headerKey=""/>  
			</li>
			<li>
				<label>订单号：</label>
				<input type="text" name="orderCode" value="${orderCode}"/>
			</li>
			<li>
				<label>下单人：</label>
				<input type="text" name="orderUser" value="${orderUser}"/>
			</li>
			<li>
				<label>收货人：</label>
				<input type="text" name="consignee" value="${consignee}"/>
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
			<shiro:hasPermission name="out:addSalesOut">
			<li>选择仓库：<s:select id="warehouseCode" name="warehouseCode" list="warehouseList" listKey="id" listValue="warehouseName"  onchange="doChange(this);"/></li>
			<li><a id="addLink" title="确实要根据选择的订单生成批量出库单吗?" target="selectedTodo" targetType="navTab" rel="orderCodes" href="${ctx}/stock/stockOut!addStockOutWithOrders.action?callbackType=forward&navTabId=tab_stockOut&warehouseCode=${warehouse.warehouseCode}&forwardUrl=${ctx}/stock/stockOut.action" class="add"><span>批量生成出库单</span></a></li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="list" width="1200" layoutH="138">
		<thead>
			<tr>
				<th width="22"><input type="checkbox" group="orderCodes" class="checkboxCtrl"></th>
				<th width="100">订单号</th>
				<th width="120">下单时间</th>
				<th width="200">收货人</th>
				<th width="50">总金额</th>
				<th width="60">配送状态</th>
				<th width="60">支付方式</th>
				<th width="120">订单接收时间</th>
				<th width="50">通知状态</th>
				<th width="120">通知时间</th>
				<th width="100">操作</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="salesOrderList" status="status">
			<tr target="sid_user" rel="${status.index }">
				<td><s:if test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@FILTERED.code"><input name="orderCodes" value="${orderCode }" type="checkbox"></s:if></td>
				<td>${orderCode }</td>
				<td><fmt:formatDate value="${orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${consignee }<br>${fullAddress }</td>
				<td>${goodsAmount+shippingFee }</td>
				<td>
					<s:if test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@FILTERED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@FILTERED.name"/></s:if>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@PICKED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@PICKED.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@SHIPPED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@SHIPPED.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@BACKED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@BACKED.name"/></s:elseif>
				</td>
				<td>
					<s:if test="paymentCode==@com.gionee.wms.common.WmsConstants$PaymentType@ALIPAY.code"><s:property value="@com.gionee.wms.common.WmsConstants$PaymentType@ALIPAY.name"/></s:if>
					<s:elseif test="PaymentCode==@com.gionee.wms.common.WmsConstants$PaymentType@COD.code"><s:property value="@com.gionee.wms.common.WmsConstants$PaymentType@COD.name"/></s:elseif>
				</td>
				<td><fmt:formatDate value="${joinTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>
					<s:if test="notifyStatus==@com.gionee.wms.common.WmsConstants$OrderNotifyStatus@UNNOTIFIED.code">未通知</s:if>
					<s:elseif test="notifyStatus==@com.gionee.wms.common.WmsConstants$OrderNotifyStatus@NOTIFIED_SUCCESS.code">通知成功</s:elseif>
					<s:elseif test="notifyStatus==@com.gionee.wms.common.WmsConstants$OrderNotifyStatus@NOTIFIED_FAIL.code">通知失败</s:elseif>
				</td>
				<td><fmt:formatDate value="${notifyTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>
					<a title="查看" target="navTab" rel="tab_showSalesOrder" href="${ctx}/stock/salesOrder!showSalesOrder.action?orderCode=${orderCode}" class="btnView"></a>
				</td>
			</tr>
			</s:iterator>
		</tbody>
	</table>
	<div class="panelBar">
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="page.pageSize" onchange="dialogPageBreak({numPerPage:this.value})">
				<option value="1" ${page.pageSize==1?"selected='true'":""}>1</option>
				<option value="50" ${page.pageSize==50?"selected='true'":""}>50</option>
				<option value="100" ${page.pageSize==100?"selected='true'":""}>100</option>
				<option value="200" ${page.pageSize==200?"selected='true'":""}>200</option>
			</select>
			<span>条，共${page.totalRow}条</span>
		</div>
		
		<div class="pagination" targetType="dialog" totalCount="${page.totalRow }" numPerPage="${page.pageSize }" pageNumShown="10" currentPage="${page.currentPage}"></div>

	</div>
</div>
<script>
function doChange(theSelect){
	$("#addLink").attr("href","${ctx}/stock/stockOut!addStockOutWithOrders.action?callbackType=forward&forwardUrl=${ctx}/stock/stockOut.action&navTabId=tab_salesOrder&warehouseCode="+$("#warehouseCode").val());
}
</script>