<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<c:set var="op" value="add" />
<c:if test="${null!=order && null!=order.id}">
	<c:set var="op" value="update" />
</c:if>

<h2 class="contentTitle" align="center">订单信息</h2>
<form id="inputForm" action="${ctx}/stock/salesOrder!${op}.action?callbackType=closeCurrent&navTabId=tab_salesOrder" method="post" class="pageForm required-validate" onsubmit="if($(this).valid()){if($('#i_order_paymentType').val()=='1'||$('#i_order_paymentName').val()=='在线支付'){if(confirm('你确认这单付款方式 是“在线支付”而“非货到付款”吗？')){validateCallback(this,dialogAjaxDone);}}else{validateCallback(this,dialogAjaxDone);}} return false;">
<input type="hidden" name="order.id" value="${order.id }"/>
<input type="hidden" name="order.orderStatus" value="${order.orderStatus }"/>
<input type="hidden" id="editGoods" name="editGoods" value="false" />
<div class="pageContent">
	<div class="pageFormContent" layoutH="97">
		<fieldset>
			<legend>基本信息</legend>
			<dl>
				<dt>订单号：</dt>
				<dd><input name="order.orderCode" type="text" class="textInput required" value="${order.orderCode }" <c:if test="${op=='update'}">readonly="readonly"</c:if> /></dd>
			</dl>
			<dl>
				<dt>订单来源：</dt>
				<dd>
					<s:if test="@com.gionee.wms.common.WmsConstants@WMS_COMPANY==1">
						<s:select id="orderSource" name="order.orderSource" list="@com.gionee.wms.common.WmsConstants$OrderSourceGionee@values()" listKey="code" listValue="name" headerValue="请选择" headerKey="" cssClass="required"/>
					</s:if>
					<s:elseif test="@com.gionee.wms.common.WmsConstants@WMS_COMPANY==2">
						<s:select id="orderSource" name="order.orderSource" list="@com.gionee.wms.common.WmsConstants$OrderSourceIuni@values()" listKey="code" listValue="name" headerValue="请选择" headerKey="" cssClass="required"/>
					</s:elseif>
					<span class="info">不能修改</span>
				</dd>
			</dl>
			<dl>
				<dt>下单人：</dt>
				<dd><input name="order.orderUser" type="text" class="textInput valid" value="${order.orderUser }" /></dd>
			</dl>
			<dl>
				<dt>支付类型：</dt>
				<dd>
					<select name="order.paymentType" id="i_order_paymentType">
						<option value="<s:property value='@com.gionee.wms.common.WmsConstants$PaymentType@ONLINE.code' />" <s:if test="order.paymentType==@com.gionee.wms.common.WmsConstants$PaymentType@ONLINE.code">selected</s:if>><s:property value="@com.gionee.wms.common.WmsConstants$PaymentType@ONLINE.name"/></option>
						<option value="<s:property value='@com.gionee.wms.common.WmsConstants$PaymentType@COD.code' />" <s:if test="order.paymentType==@com.gionee.wms.common.WmsConstants$PaymentType@COD.code">selected</s:if>><s:property value="@com.gionee.wms.common.WmsConstants$PaymentType@COD.name"/></option>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>支付方式：</dt>
				<dd>
					<select  name="order.paymentName" id="i_order_paymentName">
						<option value="${order.paymentName }">${order.paymentName }</option>
						<option value="在线支付">在线支付</option>
						<option value="货到付款">货到付款</option>
						<option value="支付宝">支付宝</option>
						<option value="其它">其它</option>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>支付流水号：</dt>
				<dd><input name="order.payNo" type="text" class="textInput valid" value="${order.payNo }" /></dd>
			</dl>
			<dl>
				<dt>配送方式：</dt>
				<dd>
					<select  name="order.shippingId">
						<c:forEach items="${shippingList }" var="shipping">
                            <c:if test="${order == null}">
							    <option value="${shipping.id }" ${(shipping.shippingName=="东莞直发")?"selected='true'":""}>${shipping.shippingName }</option>
                            </c:if>
                            <c:if test="${order != null}">
                                <option value="${shipping.id }" ${(shipping.id==order.shippingId)?"selected='true'":""}>${shipping.shippingName }</option>
                            </c:if>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>配送单号：</dt>
				<dd><input name="order.shippingNo" type="text"  value="${order.shippingNo }"></dd>
			</dl>
			<dl>
				<dt>订单类型：</dt>
				<dd>
                    <select  name="order.type" id="i_order_type">
                        <option value=">${order.type }</option>
                        <option value="普通订单" ${(order.type =="普通订单") ? "selected='true'":""}>普通订单</option>
                        <option value="换货订单"  ${(order.type =="换货订单") ? "selected='true'":""}>换货订单</option>
                    </select>
                </dd>
			</dl>
		</fieldset>
		<fieldset>
			<legend>发票信息</legend>
			<dl>
				<dt>是否需要发票：</dt>
				<dd>
					<select name="order.invoiceEnabled">
						<option value="1" <s:if test="order.invoiceEnabled==1" >selected="true"</s:if>>是</option>
						<option value="0" <s:if test="order.invoiceEnabled==0" >selected="true"</s:if>>否</option>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>发票类型：</dt>
				<dd>
					<select name="order.invoiceType">
						<option></option>
						<option value="<s:property value='@com.gionee.wms.common.WmsConstants$InvoiceType@VAT.code'/>" <s:if test="order.invoiceType==@com.gionee.wms.common.WmsConstants$InvoiceType@VAT.code">selected="true"</s:if>><s:property value="@com.gionee.wms.common.WmsConstants$InvoiceType@VAT.name"/></option>
						<option value="<s:property value='@com.gionee.wms.common.WmsConstants$InvoiceType@PLAIN.code'/>" <s:if test="order.invoiceType==@com.gionee.wms.common.WmsConstants$InvoiceType@PLAIN.code">selected="true"</s:if>><s:property value="@com.gionee.wms.common.WmsConstants$InvoiceType@PLAIN.name"/></option>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>发票抬头：</dt>
				<dd><input name="order.invoiceTitle" type="text" maxlength="50" size="30" value="${order.invoiceTitle }" class="required" /></dd>
			</dl>
			<dl>
				<dt>发票金额：</dt>
				<dd><input name="order.invoiceAmount" id="order_invoiceAmount" type="text" class="textInput number valid required" value="${order.invoiceAmount eq null ? '0' : order.invoiceAmount }" /></dd>
			</dl>
			<dl>
				<dt>发票内容：</dt>
				<dd><input name="order.invoiceContent" type="text" maxlength="50" size="30" value="${order.invoiceContent }"/></dd>
			</dl>
			<dl>
				<dt>发票状态：</dt>
				<dd>
					<s:if test="order.invoiceStatus==0">未出</s:if>
					<s:if test="order.invoiceStatus==1">已出</s:if>
				</dd>
			</dl>
		</fieldset>
		<fieldset>
			<legend>收货信息</legend>
			<dl>
				<dt>收货人：</dt>
				<dd><input name="order.consignee" type="text" maxlength="50" size="30" value="${order.consignee }" class="required"/></dd>
			</dl>
			<dl>
				<dt>省：</dt>
				<dd><input name="order.province" type="text" maxlength="50" size="30" value="${order.province }" /></dd>
			</dl>
			<dl>
				<dt>市：</dt>
				<dd><input name="order.city" type="text" maxlength="50" size="30" value="${order.city }" /></dd>
			</dl>
			<dl>
				<dt>区：</dt>
				<dd><input name="order.district" type="text" maxlength="50" size="30" value="${order.district }" /></dd>
			</dl>
			<dl>
				<dt>地址：</dt>
				<dd><input name="order.address" type="text" maxlength="260" size="30" value="${order.address }" class="required"/></dd>
			</dl>
			<dl>
				<dt>电话：</dt>
				<dd><input name="order.tel" type="text" maxlength="50" size="30" value="${order.tel }" /></dd>
			</dl>
			<dl>
				<dt>手机：</dt>
				<dd><input name="order.mobile" type="text" maxlength="50" size="30" value="${order.mobile }" /></dd>
			</dl>
			<dl>
				<dt>邮编：</dt>
				<dd><input name="order.zipcode" type="text" maxlength="50" size="30" value="${order.zipcode }" /></dd>
			</dl>
			<dl>
				<dt>最佳送货时间：</dt>
				<dd><input name="order.bestTime" type="text" maxlength="50" size="30" value="${order.bestTime }"/></dd>
			</dl>
			<dl>
				<dt>客户订单附言：</dt>
				<dd><input name="order.postscript" type="text" maxlength="125" size="30" value=" ${order.postscript }"/></dd>
			</dl>
		</fieldset>
		<fieldset>
			<legend>费用信息</legend>
			<dl>
				<dt>商品总金额：</dt>
				<dd><input name="order.goodsAmount" id="order_goodsAmount" type="text" value="${order.goodsAmount eq null ? '0' : order.goodsAmount }" class="textInput number required" min="0"/></dd>
			</dl>
			<dl>
				<dt>订单总金额：</dt>
				<dd><input name="order.orderAmount" id="order_orderAmount" type="text" value="${order.orderAmount eq null ? '0' : order.orderAmount }" class="textInput number required" min="0"/></dd>
			</dl>
			<dl style="display: none">
				<dt>已支付金额：</dt>
				<dd><input name="order.paidAmount" type="text" value="${order.paidAmount eq null ? '0' : order.paidAmount }" class="textInput number required" min="0"/></dd>
			</dl>
			<dl>
				<dt>应付金额：</dt>
				<dd><input name="order.payableAmount" id="order_payableAmount" type="text" value="${order.payableAmount eq null ? '0' : order.payableAmount }" class="textInput number required" min="0" /></dd>
			</dl>
		</fieldset>
		<fieldset>
			<legend>其它信息</legend>
			<dl class="nowrap">
				<dt>备注：</dt>
				<dd><textarea name="order.remark" cols="80" rows="2" class="textInput valid" style="width: 566px;height: 50px;">${order.remark }</textarea></dd>
			</dl>
		</fieldset>
		
		<h2 class="contentTitle" align="center">商品信息</h2>
		<div id="jbox">
			<input name="goodsSize" id="goodsSize" value="<s:property value="null==goodsList?0:goodsList.size()"/>" type="hidden"/>
			<table class="list" width="100%" id="goodsTab">
				<thead>
					<tr>
						<th align="center">SKU编码</th>
						<th align="center">SKU名称</th>
						<th align="center">单位</th>
						<th align="center">单价</th>
						<th align="center">数量</th>
						<th align="center"></th>
						<th align="center"></th>
					</tr>
				</thead>
				<tbody>
					<s:set name="count" value="0"/>
					<s:iterator value="goodsList" status="index">
					<tr class="unitBox">
						<td>
							<input name="goodsList[<s:property value='#index.index'/>].skuId" value="${skuId }" type="hidden"/>
							<input class="textInput required readonly" name="goodsList[<s:property value='#index.index'/>].skuCode" type="text" value="${skuCode }" readonly="readonly" />
						</td>
						<td><input name="goodsList[<s:property value='#index.index'/>].skuName" value="${skuName }" readonly="readonly" class="readonly"/></td>
						<td><input name="goodsList[<s:property value='#index.index'/>].measureUnit" value="${measureUnit }" readonly="readonly" class="readonly"/></td>
						<td align="right"><input name="goodsList[<s:property value='#index.index'/>].unitPrice" value="${unitPrice }" class="textInput required number readonly" readonly="readonly" min="0"/></td>
						<td align="right"><input name="goodsList[<s:property value='#index.index'/>].quantity" value="${quantity }" class="textInput required digits readonly" readonly="readonly" min="0"/></td>
						<td align="right"></td>
						<td><div><a class="btnDel" href="javascript:void(0);" onclick="delGoods(this)">删除</a></div></td>
					</tr>
					<s:set name="count" value="#count+subtotalPrice"/>
					</s:iterator>
					<tr class="unitBox" id="goodsSum">
						<td align="right" colspan="5"><strong>合计：</strong></td>
						<td align="right"><s:property value="#count"/></td>
						<td></td>
					</tr>
				</tbody>
			</table>
		</div>
		<h2 class="contentTitle" align="center">添加新商品</h2>
		<div>
			<table class="list" width="100%">
				<thead>
					<tr>
						<th align="center">SKU编码</th>
						<th align="center">SKU名称</th>
						<th align="center">单位</th>
						<th align="center">单价</th>
						<th align="center">数量</th>
						<th align="center">操作</th>
					</tr>
				</thead>
				<tbody>
					<tr id="editGoods">
						<td>
							<input name="editSkuid" bringBackName="sku.id" value="" type="hidden"/>
							<input class="textInput readonly" name="editSkuCode" bringBackName="sku.skuCode" type="text" value="" lookupGroup="sku" readonly="readonly" />
							<a class="btnLook" href="${ctx}/wares/sku!lookup.action" lookupGroup="sku" width="1200">查找SKU</a>
						</td>
						<td><input name="editSkuName" bringBackName="sku.skuName" value="" readonly="readonly" class="readonly"/></td>
						<td><input name="editMeasureUnit" bringBackName="sku.measureUnit" value="" readonly="readonly" class="readonly"/></td>
						<td align="right"><input name="editUnitPrice" value="" class="textInput number"/></td>
						<td align="right"><input name="editQuantity" value="" class="textInput digits"/></td>
						<td><div><a class="btnSelect" href="javascript:void(0);" onclick="addGoods(this)">添加</a></div></td>
					</tr>
				</tbody>
			</table>
		</div>
		<s:if test="id != null">
		<h2 class="contentTitle" align="center">操作日志</h2>
		<div id="jbox">
			<table class="list" width="100%">
				<thead>
					<tr>
						<th align="center">订单ID</th>
						<th align="center">订单状态</th>
						<th align="center">操作用户</th>
						<th align="center">操作时间</th>
						<th align="center">备注</th>
					</tr>
				</thead>
				<tbody>
				   <s:iterator value="salesOrderLogList" status="index">
					<tr class="unitBox">
						<td>${orderId}</td>
						<td>
					<s:if test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@FILTERED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@FILTERED.name"/></s:if>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@PRINTED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@PRINTED.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@PICKING.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@PICKING.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@PICKED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@PICKED.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@SHIPPING.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@SHIPPING.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@SHIPPED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@SHIPPED.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@BACKED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@BACKED.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@BACKING.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@BACKING.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@CANCELED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@CANCELED.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@RECEIVED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@RECEIVED.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@REFUSEING.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@REFUSEING.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@REFUSED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@REFUSED.name"/></s:elseif>
				        </td>
						<td>${opUser}</td>
						<td><fmt:formatDate value="${opTime}"  pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td>${remark}</td>
					</tr>
					</s:iterator>
				</tbody>
			</table>
		</div>
		</s:if>
	</div>
	<div class="formBar">
		<ul>
			<li><div class="button"><div class="buttonContent"><button type="submit" onclick="return ckPrice();">保存</button></div></div></li>
			<li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
		</ul>
	</div>
	</form>
</div>

<script type="text/javascript">
var index=$("#goodsSize").val();

function addGoods(obj) {
	var editGoods = $(obj).closest("tr");
	var skuId = editGoods.find("input[name=editSkuid]").val();
	var skuCode = editGoods.find("input[name=editSkuCode]").val();
	var skuName = editGoods.find("input[name=editSkuName]").val();
	var measureUnit = editGoods.find("input[name=editMeasureUnit]").val();
	var unitPrice = editGoods.find("input[name=editUnitPrice]").val();
	var quantity=editGoods.find("input[name=editQuantity]").val();
	if(""==skuCode || ""==unitPrice || ""==quantity) {
		alert("请将编码,单价和数量补充完整");
		return false;
	}
	var html = "<tr class=\"unitBox\">"
		+ "<td><input name=\"goodsList["+index+"].skuId\" value=\""+skuId+"\" type=\"hidden\"/>"
		+ "<input name=\"goodsList["+index+"].skuCode\" type=\"text\" value=\""+skuCode+"\" readonly=\"readonly\" class=\"required readonly\"/></td>"
		+ "<td><input name=\"goodsList["+index+"].skuName\" value=\""+skuName+"\" readonly=\"readonly\" class=\"readonly\"/></td>"
		+ "<td><input name=\"goodsList["+index+"].measureUnit\" value=\""+measureUnit+"\" readonly=\"readonly\" class=\"readonly\"/></td>"
		+ "<td align=\"right\"><input name=\"goodsList["+index+"].unitPrice\" value=\""+unitPrice+"\" class=\"textInput required number readonly\" readonly=\"readonly\" min=\"0\"/></td>"
		+ "<td align=\"right\"><input name=\"goodsList["+index+"].quantity\" value=\""+quantity+"\" class=\"textInput required digits readonly\" readonly=\"readonly\" min=\"0\"/></td>"
		+ "<td align=\"right\"></td>"
		+"<td><a class=\"btnDel\" href=\"javascript:void(0);\" onclick=\"delGoods(this)\">删除</a></div></td>"
		+ "</tr>";
	$("#goodsSum").before(html);
	index++;
	$("#editGoods",  $.pdialog.getCurrent()).val("true");
	// 添加一个统计功能
	totalPrice();
}
// 统计总价
function totalPrice() {
	var count = 0;
	$("#goodsTab tbody tr").each(function() {
		var tr = $(this), price = tr.find(":input[name$=unitPrice]").val() || 0, quantity = tr.find(":input[name$=quantity]").val() || 0;
		count += (price * quantity);
	});
	$("#order_invoiceAmount,#order_goodsAmount,#order_orderAmount,#order_payableAmount").val(count);
}

// 验证货到付款时，应付金额大于等于商品总金额
function ckPrice() {
	if($('#i_order_paymentType').val() == '2' || $('#i_order_paymentName').val() == '货到付款') {
		var opa = $("#order_payableAmount").val(), oga = $("#order_goodsAmount").val();
		if(/\d+/.test(opa) && /\d+/.test(oga)) {
			if(+opa < +oga) {
				return confirm("通常情况下：“货到付款”时则“应付金额”必须大于等于“商品总金额”\r\n您确认要继续吗？");
				
			}
		}
	}
	return true;
}

function delGoods(obj) {
	$(obj).closest("tr").remove();
	totalPrice();
	$("#editGoods",  $.pdialog.getCurrent()).val("true");
}

$(function() {
		if($("#inputForm select[name='order.invoiceEnabled']").val()=="0") {
			$("#inputForm input[name='order.invoiceTitle']").removeClass("required");
		}
	
		$("#inputForm select[name='order.invoiceEnabled']").change(function() {
			if(this.value=="0") {
				$("#inputForm input[name='order.invoiceTitle']").removeClass("required");
			} else {
				$("#inputForm input[name='order.invoiceTitle']").addClass("required");
			}
		});

});
</script>
