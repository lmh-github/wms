<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<jsp:useBean id="now" class="java.util.Date" />   
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<h2 class="contentTitle">发货商品清单</h2>
<form id="pagerForm" name="orderEditForm" action="<s:if test='id==null'>${ctx}/stock/stockOut!add.action?callbackType=closeCurrent&navTabId=tab_stockOut</s:if><s:else>${ctx}/stock/stockOut!update.action?callbackType=closeCurrent&navTabId=tab_stockOut</s:else>" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone)">
<input type="hidden" name="stockOutCode" value="${stockOutCode }">
<div class="pageContent">
	<div class="pageFormContent" layoutH="97">
		<fieldset>
			<legend>基本信息</legend>
			<dl>
				<dt>出库编号：</dt>
				<dd>${delivery.deliveryCode }</dd>
			</dl>
			<dl>
				<dt>仓库名称：</dt>
				<dd>${delivery.warehouseName }</dd>
			</dl>
			<dl>
				<dt>出库批次：</dt>
				<dd>${delivery.batchCode }</dd>
			</dl>
			<dl>
				<dt>销售订单号：</dt>
				<dd><a title="订单详情" target="dialog" mask="true" width="1024" height="768" rel="dlg_showSalesOrder" href="${ctx}/stock/salesOrder!showSalesOrder.action?id=${delivery.originalId}"><span style="color:#3C7FB1">${delivery.originalCode }</span></a></dd>
			</dl>
			<dl>
				<dt>支付类型：</dt>
				<dd>
					<s:if test="delivery.paymentType==@com.gionee.wms.common.WmsConstants$PaymentType@ONLINE.code"><s:property value="@com.gionee.wms.common.WmsConstants$PaymentType@ONLINE.name"/></s:if>
					<s:elseif test="delivery.paymentType==@com.gionee.wms.common.WmsConstants$PaymentType@COD.code"><s:property value="@com.gionee.wms.common.WmsConstants$PaymentType@COD.name"/></s:elseif>
				</dd>
			</dl>
			<dl>
				<dt>配送方式：</dt>
				<dd>${delivery.shippingName }</dd>
			</dl>
			<dl>
				<dt>发货状态：</dt>
				<dd>
					<s:if test="delivery.handlingStatus==@com.gionee.wms.common.WmsConstants$DeliveryStatus@FILTERED.code"><span style='color:red;'><s:property value="@com.gionee.wms.common.WmsConstants$DeliveryStatus@FILTERED.name"/></span></s:if>
					<s:elseif test="delivery.handlingStatus==@com.gionee.wms.common.WmsConstants$DeliveryStatus@PICKED.code"><s:property value="@com.gionee.wms.common.WmsConstants$DeliveryStatus@PICKED.name"/></s:elseif>
					<s:elseif test="delivery.handlingStatus==@com.gionee.wms.common.WmsConstants$DeliveryStatus@SHIPPED.code"><s:property value="@com.gionee.wms.common.WmsConstants$DeliveryStatus@SHIPPED.name"/></s:elseif>
					<s:elseif test="delivery.handlingStatus==@com.gionee.wms.common.WmsConstants$DeliveryStatus@CANCELED.code"><s:property value="@com.gionee.wms.common.WmsConstants$DeliveryStatus@CANCELED.name"/></s:elseif>
				</dd>
			</dl>
		</fieldset>
		<fieldset>
			<legend>收货人信息</legend>
			<dl>
				<dt>收货人：</dt>
				<dd>${delivery.consignee }</dd>
			</dl>
			<dl>
				<dt>地址：</dt>
				<dd>${delivery.fullAddress }</dd>
			</dl>
			<dl>
				<dt>电话：</dt>
				<dd>${delivery.tel }</dd>
			</dl>
			<dl>
				<dt>手机：</dt>
				<dd>${delivery.mobile }</dd>
			</dl>
			<dl>
				<dt>邮编：</dt>
				<dd>${delivery.zipcode }</dd>
			</dl>
			<dl>
				<dt>最佳送货时间：</dt>
				<dd>${delivery.bestTime }</dd>
			</dl>
			<dl>
				<dt>客户订单附言：</dt>
				<dd>${delivery.postscript }</dd>
			</dl>
		</fieldset>
	
		<div>
			<table class="list" width="100%">
				<thead>
					<tr>
						<th width="60">SKU编码</th>
						<th width="60">SKU名称</th>
						<th width="60">单位</th>
						<th width="60">数量</th>
						<th width="60">商品编码</th>
						<th width="60">操作</th>
					</tr>
				</thead>
				<tbody>
					<s:iterator value="goodsList">
					<tr class="unitBox">
						<td>${skuCode }</td>
						<td>${skuName }</td>
						<td>${measureUnit }</td>
						<td>${quantity }</td>
						<td>
							<s:if test="indivFinished==1"><span style='color:green;'>已录入</span></s:if>
							<s:elseif test="indivEnabled==1 && indivFinished==0"><span style='color:red;'>未录入</span></s:elseif>
						</td>
						<td>
							<!-- <a title="确实要删除吗？" target="ajaxTodo" href="${ctx}/stock/stockOut!deleteStockOutItem.action?id=${id}&navTabId=tab_inputPurchaseIn" class="btnDel"></a> -->
							<s:if test="indivEnabled==1">
							<shiro:hasPermission name="out:editSalesOutIndiv">
							<a title="录入商品编码" target="dialog" mask="true" width="800" height="600" href="${ctx}/stock/delivery!inputIndiv.action?id=${id}" class="btnEdit"></a>
							</shiro:hasPermission>
							</s:if>
						</td>
					</tr>
					</s:iterator>
				</tbody>
			</table>
		</div>
		<div class="divider"></div>
		
	</div>
	<div class="formBar">
		<ul>
			<li><!--  <div class="buttonActive"><div class="buttonContent"><button type="button" onclick="doSubmit()">保存</button></div></div>--></li>
			<li>
				<div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div>
			</li>
		</ul>
	</div>
</div>
</form>
<script>
function doSubmit(){
	if($('#detailLength').val()<1){
		alert("入库商品明细不能为空");
		return false;
	}
	if(confirm("保存后商品库存将会相应增加，确定要提交吗？")){
		$("form[name=orderEditForm]").submit();
	}
}
</script>