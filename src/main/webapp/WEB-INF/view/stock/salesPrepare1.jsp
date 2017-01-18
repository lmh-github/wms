<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />   
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div class="pageHeader">
	<div class="pageFormContent nowrap">
		<dl>
			<dt>发货单:</dt>
			<dd><input id="deliveryCode1" name="deliveryCode" class="textInput" type="text" size="30" value="">
			<span id="scanCode" style="color: red; font-size: 12pt; float: left;"></span></dd>
		</dl>
		<c:if test="${salesOrder!=null}">
		<dl id="gweight" >
			<dt>重量：</dt>
			<dd>${salesOrder.weight }</dd>
		</dl>
		<dl id="scanKuaidi">
			<dt>快递运单号：</dt>
			<dd>${salesOrder.shippingNo }</dd>
		</dl>
		</c:if>
	</div>
	<c:if test="${salesOrder!=null}">
	<div class="pageFormContent">
		<fieldset>
			<legend>订单信息</legend>
			<dl>
				<dt>订单号：</dt>
				<dd>${salesOrder.orderCode }</dd>
			</dl>
			<dl>
				<dt>配送方式：</dt>
				<dd>${salesOrder.shippingName }</dd>
			</dl>
			<dl>
				<dt>发票抬头：</dt>
				<dd>${salesOrder.invoiceTitle }</dd>
			</dl>
			<dl>
				<dt>发票金额：</dt>
				<dd>${salesOrder.invoiceAmount }</dd>
			</dl>
			<dl>
				<dt>收货人：</dt>
				<dd>${salesOrder.consignee }</dd>
			</dl>
			<dl>
				<dt>地址：</dt>
				<dd><span style="width: 100px">${salesOrder.fullAddress }</span></dd>
			</dl>
			<dl>
				<dt>电话：</dt>
				<dd>${salesOrder.tel }</dd>
			</dl>
			<dl>
				<dt>手机：</dt>
				<dd>${salesOrder.mobile }</dd>
			</dl>
			<dl>
				<dt>最佳送货时间：</dt>
				<dd>${salesOrder.bestTime }</dd>
			</dl>
			<dl>
				<dt>客户订单附言：</dt>
				<dd>${salesOrder.postscript }</dd>
			</dl>
		</fieldset>
	</div>
	</c:if>
	<div class="subBar">
		<span id="errorTips" style="color: red; font-size: 12pt; float: left;"></span>
	</div>
	<!--div class="formBar">
		<ul>
			<li><div class="buttonActive"><div class="buttonContent"><button type="submit">提交</button></div></div></li>
		</ul>
	</div-->
</div>
<c:if test="${goodsList!=null }">
<div class="pageContent">
	<table class="list" width="100%" layoutH="115">
		<thead>
			<tr>
				<th>商品名称</th>
				<th>sku编码</th>
				<th>申请数量</th>
				<th>已配货总数</th>
				<th>批量配货</th>
			</tr>
		</thead>
		<tbody id="prepare">
			<c:forEach items="${goodsList}" var="item">
			<tr target="sid_sku">
				<td>${item.skuName }</td>
				<td>${item.skuCode }</td>
				<td>${item.quantity }</td>
				<td>${item.quantity }</td>
				<td></td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
</c:if>
<script type="text/javascript">
$("#deliveryCode1", navTab.getCurrentPanel()).keydown(function(event) {
	var theEvent = event || window.event;
	var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
	if (code == 13) {
		var obj = this;
		scanDeliveryCode(obj.value);
	   	$(obj).val("");//清空输入框
		$(obj).focus();//聚焦本身
	}
});

function scanDeliveryCode(value) {
	scanCode(value);
	$.ajax({
	   url: "${ctx}/stock/salesPrepare!readyPrepare.action?deliveryCode=" + value,
	   success: function(data){
		   	if(data.ok==true) {
		   		navTab.reload("${ctx}/stock/salesPrepare!list.action?orderId=" + data.result.id, navTab.getCurrentPanel());
		   	} else {
		   		errorTips(data.message);
		   		soundError();
		   		return false;
		   	}
	   },
	   error: function (XMLHttpRequest, textStatus, errorThrown) {     
			alert(errorThrown);     
		}
	});
}

function scanCode(message) {
	$("#scanCode", navTab.getCurrentPanel()).html(message);
	errorTips("");
}

function errorTips(message) {
	$("#errorTips", navTab.getCurrentPanel()).html(message);
}

$(function(){
	setTimeout(function() {
		$("#deliveryCode1", navTab.getCurrentPanel()).focus();
	},1000);
});
</script>
