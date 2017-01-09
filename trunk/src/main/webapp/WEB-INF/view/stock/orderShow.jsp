﻿<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<h2 class="contentTitle" align="center">订单信息</h2>
<div class="pageContent">
	<div class="pageFormContent" layoutH="97">
		<fieldset>
			<legend>基本信息</legend>
			<dl>
				<dt>订单号：</dt>
				<dd>${order.orderCode }</dd>
			</dl>
			<dl>
				<dt>下单人：</dt>
				<dd>${order.orderUser }</dd>
			</dl>
			<dl>
				<dt>下单时间：</dt>
				<dd><fmt:formatDate value="${order.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/></dd>
			</dl>
			<dl>
				<dt>支付类型：</dt>
				<dd>
					<s:if test="order.paymentType==@com.gionee.wms.common.WmsConstants$PaymentType@ONLINE.code"><s:property value="@com.gionee.wms.common.WmsConstants$PaymentType@ONLINE.name"/></s:if>
					<s:elseif test="order.paymentType==@com.gionee.wms.common.WmsConstants$PaymentType@COD.code"><s:property value="@com.gionee.wms.common.WmsConstants$PaymentType@COD.name"/></s:elseif>
				</dd>
			</dl>
			<dl>
				<dt>支付方式：</dt>
				<dd>${order.paymentName }</dd>
			</dl>
			<dl>
				<dt>支付流水号：</dt>
				<dd>${order.payNo }</dd>
			</dl>
			<dl>
				<dt>已支付金额：</dt>
				<dd>${order.paidAmount }</dd>
			</dl>
			<dl>
				<dt>配送方式：</dt>
				<dd>${order.shippingName }</dd>
			</dl>
			<dl>
				<dt>配送单号：</dt>
				<dd>${order.shippingNo }</dd>
			</dl>
			<dl>
				<dt>订单状态：</dt>
				<dd>
					<s:if test="order.orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@FILTERED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@FILTERED.name"/></s:if>
					<s:elseif test="order.orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@PICKING.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@PICKING.name"/></s:elseif>
					<s:elseif test="order.orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@PICKED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@PICKED.name"/></s:elseif>
					<s:elseif test="order.orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@SHIPPING.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@SHIPPING.name"/></s:elseif>
					<s:elseif test="order.orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@SHIPPED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@SHIPPED.name"/></s:elseif>
					<s:elseif test="order.orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@BACKED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@BACKED.name"/></s:elseif>
				</dd>
			</dl>
		</fieldset>
		<fieldset>
			<legend>发票信息</legend>
			<dl>
				<dt>是否需要发票：</dt>
				<dd>${order.invoiceEnabled==1?"是":"否" }</dd>
			</dl>
			<dl>
				<dt>发票类型：</dt>
				<dd>
					<s:if test="order.invoiceType==@com.gionee.wms.common.WmsConstants$InvoiceType@PLAIN.code"><s:property value="@com.gionee.wms.common.WmsConstants$InvoiceType@PLAIN.name"/></s:if>
					<s:elseif test="order.invoiceType==@com.gionee.wms.common.WmsConstants$InvoiceType@VAT.code"><s:property value="@com.gionee.wms.common.WmsConstants$InvoiceType@VAT.name"/></s:elseif>
				</dd>
			</dl>
			<dl>
				<dt>发票抬头：</dt>
				<dd>${order.invoiceTitle }</dd>
			</dl>
			<dl>
				<dt>发票金额：</dt>
				<dd>${order.invoiceAmount }</dd>
			</dl>
			<dl>
				<dt>发票内容：</dt>
				<dd>${order.invoiceContent }</dd>
			</dl>
			<dl>
				<dt>发票状态：</dt>
				<dd>${order.invoiceStatus==1?"已出":"未出" }</dd>
			</dl>
		</fieldset>
		<fieldset>
			<legend>收货信息</legend>
			<dl>
				<dt>收货人：</dt>
				<dd>${order.consignee }</dd>
			</dl>
			<dl>
				<dt>地址：</dt>
				<dd>${order.fullAddress }</dd>
			</dl>
			<dl>
				<dt>电话：</dt>
				<dd>${order.tel }</dd>
			</dl>
			<dl>
				<dt>手机：</dt>
				<dd>${order.mobile }</dd>
			</dl>
			<dl>
				<dt>邮编：</dt>
				<dd>${order.zipcode }</dd>
			</dl>
			<dl>
				<dt>最佳送货时间：</dt>
				<dd>${order.bestTime }</dd>
			</dl>
			<dl>
				<dt>客户订单附言：</dt>
				<dd>${order.postscript }</dd>
			</dl>
		</fieldset>
		<fieldset>
			<legend>费用信息</legend>
			<dl>
				<dt>商品总金额：</dt>
				<dd>${order.goodsAmount }</dd>
			</dl>
			<dl>
				<dt>订单总金额：</dt>
				<dd>${order.orderAmount }</dd>
			</dl>
			<dl style="display: none">
				<dt>已支付金额：</dt>
				<dd>${order.paidAmount }</dd>
			</dl>
			<dl style="display: none">
				<dt>应付金额：</dt>
				<dd>${order.payableAmount }</dd>
			</dl>
		</fieldset>
		
		<h2 class="contentTitle" align="center">商品信息</h2>
		<div>
			<table class="list" width="100%">
				<thead>
					<tr>
						<th align="center">SKU编码</th>
						<th align="center">SKU名称</th>
						<th align="center">单位</th>
						<th align="center">单价</th>
						<th align="center">数量</th>
						<th align="center">小计</th>
					</tr>
				</thead>
				<tbody>
					<s:set name="count" value="0"/>
					<s:iterator value="goodsList">
					<tr class="unitBox">
						<td>${skuCode }</td>
						<td>${skuName }</td>
						<td>${measureUnit }</td>
						<td align="right">${unitPrice }</td>
						<td align="right">${quantity }</td>
						<td align="right">${subtotalPrice }</td>
					</tr>
					<s:set name="count" value="#count+subtotalPrice"/>
					</s:iterator>
					<tr class="unitBox">
						<td align="right" colspan="5"><strong>合计：</strong></td>
						<td align="right"><s:property value="#count"/></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<div class="formBar">
		<ul>
			<li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
		</ul>
	</div>
</div>
