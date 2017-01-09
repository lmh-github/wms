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

<div class="pageHeader" id="page1">
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/stock/delivery!listForBatchSearch.action?batchId=${batchId}" method="post">
	<div class="searchBar">
		<ul class="searchContent" style="height: 50%;">
			<li>
				<label>拣货编号：</label>
				<input type="text" value="${deliveryBatch.batchCode}" readonly="true"/>
			</li>
			<li>
				<label>仓库：</label>
				<input type="text" value="${deliveryBatch.warehouseName}" readonly="true"/>
			</li>
			
			<li>
				<label>物流公司：</label>
				<s:select name="shippingId" list="shippingList" listKey="id" listValue="shippingName" headerValue="请选择" headerKey=""/>
			</li>
			<li>
				<label>商品名称：</label>
				<input name="skuName" type="text" value="${skuName}"/>
			</li>
			<li>
				<label>IMEI：</label>
				<input name="indivCode" type="text" value="${indivCode}"/>
			</li>
			<li>
				<label>订单号：</label>
				<input name="orderNum" type="text" value="${orderNum}"/>
			</li>
			<li>
				<label>收货人：</label>
				<input name="consignee" type="text" value="${consignee}"/>
			</li>
			<li>
				<label>手机号：</label>
				<input name="mobile" type="text" value="${mobile}"/>
			</li>
			<li style="width:380px;">
				<label>订单时间从：</label>
				<input type="text" name="orderTimeBegin" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${orderTimeBegin }" pattern="yyyy-MM-dd"/>"/>
				到：
				<input type="text" name="orderTimeEnd" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${orderTimeEnd }" pattern="yyyy-MM-dd"/>"/>
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
			<li><a class="edit" href="${ctx}/stock/salesOrder!setInvoiceStatus.action" target="selectedTodo" rel="orderIds" title="确定要设置吗？"><span>设置发票已出</span></a></li>
		</ul>
	</div>
	<table class="list" width="100%" layoutH="120">
		<thead>
			<tr align="center">
				<th width="22"><input type="checkbox" group="orderIds" class="checkboxCtrl"></th>
				<th>发货编号</th>
				<th>拣货编号</th>
				<th>销售订单号</th>
				<th>收货人</th>
				<th>支付类型</th>
				<th>订单状态</th>
				<th>配送方式</th>
				<th>配送单号</th>
				<th>发票状态</th>
				<th>发票抬头</th>
				<th>发票金额</th>
				<th>商品名称</th>
				<th>数量</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="orderList" status="status">
			<tr target="sid_user" rel="${status.index }" align="center">
				<td><input name="orderIds" value="${id }" type="checkbox"></td>
				<td>${deliveryCode }</td>
				<td>${batchCode }</td>
				<td><a title="订单详情" target="navTab" rel="tab_showSalesOrder" href="${ctx}/stock/salesOrder!showSalesOrder.action?id=${id}"><span style="color:#3C7FB1">${orderCode }</span></a></td>
				<td>${consignee }<br>${fullAddress }</td>
				<td>
					<s:if test="paymentType==@com.gionee.wms.common.WmsConstants$PaymentType@ONLINE.code"><s:property value="@com.gionee.wms.common.WmsConstants$PaymentType@ONLINE.name"/></s:if>
					<s:elseif test="PaymentType==@com.gionee.wms.common.WmsConstants$PaymentType@COD.code"><s:property value="@com.gionee.wms.common.WmsConstants$PaymentType@COD.name"/></s:elseif>
				</td>
				<td>
					<s:if test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@FILTERED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@FILTERED.name"/></s:if>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@PRINTED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@PRINTED.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@PICKING.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@PICKING.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@PICKED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@PICKED.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@SHIPPING.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@SHIPPING.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@SHIPPED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@SHIPPED.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@BACKED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@BACKED.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@CANCELED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@CANCELED.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@RECEIVED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@RECEIVED.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@REFUSEING.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@REFUSEING.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@REFUSED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@REFUSED.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@BACKING.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@BACKING.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@UNFILTER.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@UNFILTER.name"/></s:elseif>
				</td>
				<td>${shippingName }</td>
				<td>${shippingNo }</td>
				<td>
					<s:if test="invoiceStatus==1"><span style='color:green;'>已出</span></s:if>
					<s:elseif test="invoiceStatus==0"><span style='color:red;'>未出</span></s:elseif>
				</td>
				<td>${invoiceTitle}</td>
				<td>${invoiceAmount}</td>
				<td>
					<s:iterator value="goodsList" id="goods">
					${goods.skuName}<br/>
					</s:iterator>
				</td>
				<td>
					<s:iterator value="goodsList" id="goods">
					${goods.quantity}<br/>
					</s:iterator>
				</td>
				<td>
					<a title="查看" target="navTab" rel="tab_showSalesOrder" href="${ctx}/stock/salesOrder!showSalesOrder.action?id=${id}" class="btnView"></a>
				</td>
			</tr>
			</s:iterator>
		</tbody>
	</table>
</div>
