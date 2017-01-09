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
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/stock/salesOrder!listForDely.action" method="post">
	<input type="hidden" name="stockCheckId" value="${stockCheckId }" />
	<div class="searchBar">
		<ul class="searchContent" style="height: 50%;">
			<li style="width:380px;">
				<label>订单时间从：</label>
				<input type="text" name="orderTimeBegin" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${orderTimeBegin }" pattern="yyyy-MM-dd"/>"/>
				到：
				<input type="text" name="orderTimeEnd" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${orderTimeEnd }" pattern="yyyy-MM-dd"/>"/>
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
			<li>
				<label>物流公司：</label>
				<s:select name="shippingId" list="shippingList" listKey="id" listValue="shippingName" headerValue="请选择" headerKey=""/>
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
			<!--li><a id="addLink" title="确实要根据选择的订单生成拣货批次吗?" target="selectedTodo" targetType="navTab" rel="orderIds" href="${ctx}/stock/deliveryBatch!add.action?callbackType=forward&navTabId=tab_deliveryBatch&forwardUrl=${ctx}/stock/deliveryBatch.action" warn="请先选择销售订单" class="add"><span>生成拣货批次</span></a></li-->
			<span>生成拣货批次:&nbsp;</span>
			<s:iterator value="shippingSumList" id="shippingSum">
				<a title="确实生成${shippingName}的批次?" target="ajaxTodo" rel="tab_deliveryBatch" href="${ctx}/stock/deliveryBatch!add.action?shippingId=${shippingId}&callbackType=forward&navTabId=tab_deliveryBatch&forwardUrl=${ctx}/stock/deliveryBatch.action" class="add"><span>${shippingName}(${count})</span></a>
			</s:iterator>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="list" width="100%" layoutH="142">
		<thead>
			<tr>
				<!--th width="22"><input type="checkbox" group="orderIds" class="checkboxCtrl"></th-->
				<th>订单号</th>
				<th>下单时间</th>
				<th>收货人</th>
				<th>订单总金额</th>
				<th>支付类型</th>
				<th>订单状态</th>
				<th>物流公司</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="orderList" status="status">
			<tr target="sid_user" rel="${status.index }">
				<!--td><s:if test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@FILTERED.code"><input name="orderIds" value="${id }" type="checkbox"></s:if></td-->
				<td>${orderCode }</td>
				<td><fmt:formatDate value="${orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${consignee }<br>${fullAddress }</td>
				<td><fmt:formatNumber value="${orderAmount}" pattern="#.00" /></td>
				<td>
					<s:if test="paymentType==@com.gionee.wms.common.WmsConstants$PaymentType@ONLINE.code"><s:property value="@com.gionee.wms.common.WmsConstants$PaymentType@ONLINE.name"/></s:if>
					<s:elseif test="PaymentType==@com.gionee.wms.common.WmsConstants$PaymentType@COD.code"><s:property value="@com.gionee.wms.common.WmsConstants$PaymentType@COD.name"/></s:elseif>
				</td>
				<td>
					<s:if test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@FILTERED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@FILTERED.name"/></s:if>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@PICKED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@PICKED.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@SHIPPED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@SHIPPED.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@BACKED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@BACKED.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@CANCELED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@CANCELED.name"/></s:elseif>
				</td>
				<td>
					${shippingName }
				</td>
				<td>
					<a title="订单详情" target="navTab" rel="tab_showSalesOrder" href="${ctx}/stock/salesOrder!showSalesOrder.action?id=${id}" class="btnView"></a>
				</td>
			</tr>
			</s:iterator>
		</tbody>
	</table>
	<div class="panelBar">
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="page.pageSize" onchange="navTabPageBreak({numPerPage:this.value})">
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
<script>
function doChange(theSelect){
	$("#addLink").attr("href","${ctx}/stock/deliveryBatch!add.action?callbackType=forward&forwardUrl=${ctx}/stock/deliveryBatch.action&navTabId=tab_salesOrder&warehouseId="+$("#warehouseId").val());
}
</script>