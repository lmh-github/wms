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
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/stock/salesOrder.action" method="post">
	<input type="hidden" name="stockCheckId" value="${stockCheckId }" />
	<div class="searchBar">
		<ul class="searchContent">
			<li>
				<label>订单开始时间：</label>
				<input type="text" name="orderTimeBegin" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${orderTimeBegin }" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			</li>
            <li>
                <label>订单截止时间：</label>
                <input type="text" name="orderTimeEnd" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${orderTimeEnd }" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
            </li>
			<li>
				<label>支付开始时间：</label>
				<input type="text" name="paymentTimeBegin" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${paymentTimeBegin }" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			</li>
            <li>
                <label>支付截止时间：</label>
                <input type="text" name="paymentTimeEnd" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${paymentTimeEnd }" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
            </li>
			<li>
				<label>发货开始时间：</label>
				<input type="text" name="shippingTimeBegin" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${shippingTimeBegin }" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			</li>
            <li>
                <label>发货截止时间：</label>
                <input type="text" name="shippingTimeEnd" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${shippingTimeEnd }" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
            </li>
			<li>
				<label>订单状态：</label>
				<s:select name="orderStatus" list="@com.gionee.wms.common.WmsConstants$OrderStatus@values()" listKey="code" listValue="name" headerValue="请选择" headerKey=""/>  
			</li>
			<li>
				<label>订单来源：</label>
				<s:if test="@com.gionee.wms.common.WmsConstants@WMS_COMPANY==1">
					<s:select name="orderSource" list="@com.gionee.wms.common.WmsConstants$OrderSourceGionee@values()" listKey="code" listValue="name" headerValue="请选择" headerKey=""/>
				</s:if><s:elseif test="@com.gionee.wms.common.WmsConstants@WMS_COMPANY==2">
					<s:select name="orderSource" list="@com.gionee.wms.common.WmsConstants$OrderSourceIuni@values()" listKey="code" listValue="name" headerValue="请选择" headerKey=""/>
				</s:elseif>
			</li>
            <li>
              <label>支付类型：</label>
              <select name="paymentType">
                <option value="" ${paymentType == null ? "selected" : "" }>请选择</option>
                <option value="<s:property value='@com.gionee.wms.common.WmsConstants$PaymentType@ONLINE.code' />" <s:if test="paymentType==@com.gionee.wms.common.WmsConstants$PaymentType@ONLINE.code">selected</s:if>><s:property value="@com.gionee.wms.common.WmsConstants$PaymentType@ONLINE.name"/></option>
                <option value="<s:property value='@com.gionee.wms.common.WmsConstants$PaymentType@COD.code' />" <s:if test="paymentType==@com.gionee.wms.common.WmsConstants$PaymentType@COD.code">selected</s:if>><s:property value="@com.gionee.wms.common.WmsConstants$PaymentType@COD.name"/></option>
              </select>
            </li>
            <li>
              <label>支付方式：</label>
              <select name="paymentName">
                <option value="" ${paymentName == null ? "selected" : "" }>请选择</option>
                <option value="在线支付" ${paymentName == "在线支付" ? "selected" : "" }>在线支付</option>
                <option value="货到付款" ${paymentName == "货到付款" ? "selected" : "" }>货到付款</option>
                <option value="支付宝" ${paymentName == "支付宝" ? "selected" : "" }>支付宝</option>
                <option value="其它" ${paymentName == "其它" ? "selected" : "" }>其它</option>
              </select>
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
				<label>手机号：</label>
				<input type="text" name="mobile" value="${mobile}" />
			</li>
			<li>
				<label>物流单号：</label>
				<input type="text" name="shippingNo" value="${shippingNo}" />
			</li>
			<li>
				<label>SKU编码：</label>
				<select name="condition" style="width: 40px;">
					<option value="1" ${(1==condition)?"selected='true'":""}>含</option>
					<option value="2" ${(2==condition)?"selected='true'":""}>等于</option>
					<option value="3" ${(3==condition)?"selected='true'":""}>不含</option>
				</select>
				<input name="skuCode" type="text" value="${skuCode}" style="width: 117px;"/>
			</li>
			<li>
				<label>是否推送：</label>
				<select name="orderPushStatus">
                  <option value="" ${orderPushStatus == null ? "selected" : "" }>请选择</option>
                  <option value="1" <s:property value="orderPushStatus == 1 ? 'selected' : ''"/>>是</option>
                  <option value="0" <s:property value="orderPushStatus == 0 ? 'selected' : ''"/>>否</option>
                </select>
			</li>
			<li>
				<label>地区：</label>
                <input type="text" name="province" value="${province}" />
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
<div class="pageContent" >
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="${ctx}/stock/salesOrder!input.action" target="dialog" rel="dlg_orderInput" mask="true" width="1024" height="768"><span>添加订单</span></a></li>
			<li class="line">line</li>
			<li><a class="edit" href="${ctx}/stock/salesOrder!input.action?id={sid_order}" target="dialog" rel="dlg_orderInput" mask="true" width="1024" height="768" warn="请选择销售订单"><span>查看/修改订单</span></a></li>
			<li class="line">line</li>
			<li><a class="edit" href="${ctx}/stock/salesOrder!inputShippingInfo.action?id={sid_order}" target="dialog" rel="dlg_shippingInput" mask="true" width="600" height="600" warn="请选择销售订单"><span>编辑物流</span></a></li>
			<li class="line">line</li>
			<li><a class="edit" href="${ctx}/stock/salesOrder!toConfigAutoPush.action" target="dialog" rel="dlg_configInput" mask="true" width="400" height="200" warn="自动推送配置"><span>自动推送配置</span></a></li>
			<li class="line">line</li>
			<li><a class="edit" href="${ctx}/stock/salesOrder!toConfigPushCheck.action" target="dialog" rel="dlg_configInput" mask="true" width="400" height="200" warn="推送库存校验配置"><span>推送库存校验配置</span></a></li>
			<li class="line">line</li>
			<li><a class="edit" href="${ctx}/stock/salesOrder!copy.action?id={sid_order}" target="ajaxTodo" title="<span style='color:#e91e63;font-size:15px;'>请确定原订单已经取消，如果货已发出需通知物流截件，继续操作请点击&nbsp;确定！</span>" mask="true"><span>一键复制</span></a></li>
			<li class="line">line</li>
			<li><a class="icon" href="${ctx}/stock/salesOrder.action?exports=1" target="dwzExport" targettype="navTab"><span>导出excel</span></a></li>
            <li class="line">line</li>
            <li><a class="icon" href="${ctx}/stock/salesOrder!toUp.action" target="dialog" rel="dlg_orderInput" mask="true" width="500" height="300"><span>导入excel</span></a></li>
        </ul>
	</div>
	<table class="list" width="100%" layoutH="170">
		<thead>
			<tr align="center">
				<th>订单号</th>
				<th>订单来源</th>
				<th>支付时间</th>
				<th>收货信息</th>
				<th>订单总金额</th>
				<th>订单类型</th>
				<th>支付类型</th>
				<th>物流公司</th>
				<th>物流单号</th>
				<th>订单状态</th>
				<th>已退货备注</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="orderList" status="status" var="b">
			<tr target="sid_order" rel="${id }" align="center">
				<td>${orderCode }</td>
				<td>
                    <s:property value="@com.gionee.wms.common.WmsConstants$OrderSource@values().{?#this.code==#b.orderSource}[0].name"/>
				</td>
				<td><fmt:formatDate value="${paymentTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${consignee } ${mobile }<br>${fullAddress }</td>
				<td>${orderAmount }</td>
				<td>${type }</td>
				<td>
					<s:if test="paymentType==@com.gionee.wms.common.WmsConstants$PaymentType@ONLINE.code"><s:property value="@com.gionee.wms.common.WmsConstants$PaymentType@ONLINE.name"/></s:if>
					<s:elseif test="PaymentType==@com.gionee.wms.common.WmsConstants$PaymentType@COD.code"><s:property value="@com.gionee.wms.common.WmsConstants$PaymentType@COD.name"/></s:elseif>
				</td>
				<td>${shippingName }</td>
				<td>${shippingNo }</td>
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
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@UNFILTER.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@UNFILTER.name"/></s:elseif>
				</td>
				<td>${remarkBacked}</td>
				<td>
					<s:if test="@com.gionee.wms.common.WmsConstants@WMS_COMPANY==1">
					<s:if test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@SHIPPED.code || orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@RECEIVED.code">
						<a target="dialog" mask="true" width="800" height="600" href="${ctx}/stock/rmaRecv!inputInit.action?orderId=${id }"><span>退货</span></a>	
					</s:if>
					</s:if>
					<%-- <s:if test="orderStatus!=@com.gionee.wms.common.WmsConstants$OrderStatus@SHIPPED.code && orderStatus!=@com.gionee.wms.common.WmsConstants$OrderStatus@CANCELED.code
					&& orderStatus!=@com.gionee.wms.common.WmsConstants$OrderStatus@BACKED.code && orderStatus!=@com.gionee.wms.common.WmsConstants$OrderStatus@RECEIVED.code
					&& orderStatus!=@com.gionee.wms.common.WmsConstants$OrderStatus@REFUSEING.code && orderStatus!=@com.gionee.wms.common.WmsConstants$OrderStatus@REFUSED.code
					&& orderStatus!=@com.gionee.wms.common.WmsConstants$OrderStatus@BACKING.code && orderSource!=@com.gionee.wms.common.WmsConstants$OrderSource@OFFICIAL_IUNI.code
					&& orderSource!=@com.gionee.wms.common.WmsConstants$OrderSource@OFFICIAL_GIONEE.code">
						<a target="ajaxTodo" title="确定要取消${orderCode }的订单吗?" href="${ctx }/stock/salesOrder!cancelOrder.action?orderCode=${orderCode }"><span>取消</span></a>
					</s:if> --%>
                    <s:if test="orderStatus == @com.gionee.wms.common.WmsConstants$OrderStatus@FILTERED.code
                    || orderStatus == @com.gionee.wms.common.WmsConstants$OrderStatus@PRINTED.code">
                      <a target="ajaxTodo" title="确定要取消${orderCode }的订单吗?" href="${ctx }/stock/salesOrder!cancelOrder.action?orderCode=${orderCode }"><span>取消</span></a>
                    </s:if>
					<s:if test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@SHIPPED.code || orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@REFUSEING.code">
						<a target="dialog" mask="true" width="800" height="600" href="${ctx}/stock/rmaRecv!inputRefuse.action?orderId=${id }"><span>拒收</span></a>	
					</s:if>                    
                    <s:if test="shippingName == '顺丰速运' && orderStatus == @com.gionee.wms.common.WmsConstants$OrderStatus@FILTERED.code && (orderPushStatus == null || orderPushStatus == @com.gionee.wms.common.WmsConstants$OrderPushStatusEnum@UN_PUSHED.code)">
                      &nbsp;|&nbsp;<a target="ajaxTodo" title="&lt;b style='color:red;font-size:14px;'&gt;推送前请仔细检查下订单的发票金额！&lt;/b&gt;" href="${ctx }/stock/salesOrder!pushToSF.action?id=${id}"><span>顺丰发货</span></a>
                    </s:if>
                    <s:if test="shippingName == '顺丰速运' && 
                    (orderStatus == @com.gionee.wms.common.WmsConstants$OrderStatus@SHIPPED.code 
                    || orderStatus == @com.gionee.wms.common.WmsConstants$OrderStatus@RECEIVED.code 
                    || orderStatus == @com.gionee.wms.common.WmsConstants$OrderStatus@BACKED.code) 
                    && (orderPushStatus != null && orderPushStatus == @com.gionee.wms.common.WmsConstants$OrderPushStatusEnum@PUSHED.code)">
                      &nbsp;|&nbsp;<a target="dialog" title="查看串号" mask="true" width="300" href="${ctx }/stock/salesOrder!queryImeis.action?orderCode=${orderCode}"><span>串号</span></a>
                    </s:if>
                    
                    <s:if test="orderStatus != -1 && (orderPushStatus != null && orderPushStatus == @com.gionee.wms.common.WmsConstants$OrderPushStatusEnum@PUSHED.code)">
                      |&nbsp;<a target="ajaxTodo" title="该功能用于推送到顺丰后然后取消定订单，确定要取消${orderCode }的订单吗?" href="${ctx }/stock/salesOrder!cancelOrder.action?orderCode=${orderCode }"><span>取消顺丰订单</span></a>
                    </s:if>
                    <s:if test="orderStatus != -1 && (orderPushStatus != null && orderPushStatus == @com.gionee.wms.common.WmsConstants$OrderPushStatusEnum@PUSHED.code)">
                      |&nbsp;<a target="ajaxTodo" title="该功能用于直接修改订单状态为已签收，切勿随意使用，确定要签收：${orderCode }的订单吗?" href="${ctx }/stock/salesOrder!qinaShou.action?orderCode=${orderCode }"><span>改签收</span></a>
                    </s:if>
                    <s:if test="orderStatus != -1 && orderStatus == @com.gionee.wms.common.WmsConstants$OrderStatus@RECEIVED.code">
                      |&nbsp;<a  href="${ctx}/stock/salesOrder!input.action?id=${id}&type=1" target="dialog" rel="dlg_orderInput" mask="true" width="1024" height="768" warn="请选择销售订单"><span>换货</span></a>
                    </s:if>
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

function exports() {
	$("#pagerForm input[name=exports]", navTab.getCurrentPanel()).val("1");
	var param=$("#pagerForm").serialize();
	location="${ctx}/stock/salesOrder.action?"+param;
	$("#pagerForm input[name=exports]", navTab.getCurrentPanel()).val("0");
}
</script>