<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="rand"><%= java.lang.Math.round(java.lang.Math.random() * 100000) %></c:set>
<OBJECT id=m_ObjSoapKp classid="clsid:31EE4CA3-5469-4151-AF4B-01ED9ABD1E35" width=0 height=0 align="center" hspace=0 vspace=0 CODEBASE="SoapKpClient.cab#Version=1,0,0,0"> </OBJECT>
<form id="pagerForm" method="post" action="#rel#">
	<input type="hidden" name="page.currentPage" value="1" />
	<input type="hidden" name="page.pageSize" value="${page.pageSize}" />
</form>

<div class="pageHeader">
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/stock/salesOrder!sfOrderPrint.action" method="post">
	<input type="hidden" name="stockCheckId" value="${stockCheckId }" />
	<input id="sf_shipping_id" type="hidden" name="shippingId" value="${shippingId}" />
	<div class="searchBar">
		<ul class="searchContent" style="height: 50%;">
			<li style="width:380px;">
				<label>下单时间从：</label>
				<input type="text" name="orderTimeBegin" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${orderTimeBegin }" pattern="yyyy-MM-dd"/>"/>
				到：
				<input type="text" name="orderTimeEnd" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${orderTimeEnd }" pattern="yyyy-MM-dd"/>"/>
			</li>
			<li>
				<label>订单号：</label>
				<input type="text" name="orderCode" value="${orderCode}"/>
			</li>
			<li>
				<label>收货人：</label>
				<input type="text" name="consignee" value="${consignee}"/>
			</li>
            <li>
              <label>运单号：</label>
              <input type="text" name="shippingNo" value="${shippingNo}"/>            
            </li>
			<li style="width:380px;">
				<label>推送时间：</label>
				<input type="text" name="orderPushTimeBegin" class="date" size="10" dateFmt="yyyy-MM-dd HH:mm:00" value="<fmt:formatDate value="${orderPushTimeBegin }" pattern="yyyy-MM-dd HH:mm"/>"/>
				到：
				<input type="text" name="orderPushTimeEnd" class="date" size="10" dateFmt="yyyy-MM-dd HH:mm:00" value="<fmt:formatDate value="${orderPushTimeEnd }" pattern="yyyy-MM-dd HH:mm"/>"/>
			</li>
            <li>
              <label>SKU编码：</label>
              <input type="text" name="skuCode" value="${skuCode}"/>            
            </li>
		</ul>
		<div class="subBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button id="but_submit" type="submit">检索</button></div></div></li>
			</ul>
		</div>
		
	</div>
	</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="icon" id="printInvoiceLink${rand}"><span>打发票</span></a></li>
			<!-- 
			<li class="line">line</li>
			<li><a class="edit" onclick="inputShipping()"><span>编辑配送信息</span></a></li>
			 -->
			 <!-- 
			<li class="line">line</li>
			<li><a class="edit" href="${ctx}/stock/salesOrder!setInvoiceStatus.action" target="selectedTodo" rel="orderIds" title="确定要设置吗？"><span>设置发票已出</span></a></li>
			 -->
		</ul>
	</div>
	<table class="list" width="100%" layoutH="169">
		<thead>
			<tr>
				<th width="22"><input type="checkbox" group="orderIds" class="checkboxCtrl"></th>
				<th>订单号</th>
				<th>支付时间</th>
				<th>收货人</th>
				<th>订单总金额</th>
				<th>支付类型</th>
				<th>订单状态</th>
				<th>物流公司</th>
				<th>配送单号</th>
                <th>推送时间</th>
                <th>是否要发票</th>
				<th>发票状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody id="tbody${rand}">
			<s:iterator value="orderList" status="status">
			<tr target="sid_user" rel="${status.index }">
				<td>
                  <s:if test="invoiceEnabled != null and invoiceEnabled ==1 and invoiceAmount gt 0">
                    <input name="orderIds" value="${id }" type="checkbox" />                  
                  </s:if>
                  <s:else>
                    <input name="orderIds" value="${id }" type="hidden" />
                  </s:else>
                  <input type="hidden" value="${invoiceEnabled}" />
                  <input type="hidden" value="${invoiceAmount}" />
                </td>
				<td>${orderCode }</td>
				<td><fmt:formatDate value="${paymentTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${consignee }<br>${fullAddress }</td>
				<td><fmt:formatNumber value="${orderAmount}" pattern="#.##" /></td>
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
				<td>
					${shippingName }
				</td>
				<td>
					${shippingNo}
				</td>
                <td><fmt:formatDate value="${orderPushTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                <td>
                  <s:if test="invoiceEnabled != null and invoiceEnabled ==1">是</s:if>
                  <s:else>否</s:else>
                </td>
				<td>
					<s:if test="invoiceStatus==1"><span style='color:green;'>已出</span></s:if>
					<s:elseif test="invoiceStatus==0"><span style='color:red;'>未出</span></s:elseif>
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
				<option value="40" ${page.pageSize==40?"selected='true'":""}>40</option>
				<option value="50" ${page.pageSize==50?"selected='true'":""}>50</option>
				<option value="100" ${page.pageSize==100?"selected='true'":""}>100</option>
				<option value="200" ${page.pageSize==200?"selected='true'":""}>200</option>
			</select>
			<span>条，共${page.totalRow}条</span>
		</div>
		
		<div class="pagination" targetType="navTab" totalCount="${page.totalRow }" numPerPage="${page.pageSize }" pageNumShown="10" currentPage="${page.currentPage}"></div>

	</div>
</div>

<div id="sf_audioArea" style="display: none"></div>

<script type="text/javascript">
$(function() {
	$("#printInvoiceLink${rand}").click(function() {
		if(!/MSIE/ig.test(navigator.userAgent)) {
			return alertMsg.error("当前浏览器不支持此操作，请用Internet Explorer进行尝试！");
		}
		var ids = [];
		$("#tbody${rand} input:checked").each(function() {
			ids.push(this.value);
		});
		
		if(ids.length == 0) {;
			return alertMsg.error('请选择订单！');
		}
		
		$.post("${ctx}/stock/salesOrder!getPrintInvoiceData.action", {ids: ids.join(",")}, function(data) {
			if (data.message) {
				return alertMsg.error(data.message);
			}
			var successOrder = [], successBatch = {};
			$.each(data, function() {
				try{
    				var m_ObjSoapKp = document.getElementById("m_ObjSoapKp");
    				m_ObjSoapKp.SoapAdd="http://127.0.0.1/wsdl/";
    				//m_ObjSoapKp.SoapAdd="http://localhost:8081/P/data";
    				m_ObjSoapKp.LoginTaxNo="<%=com.gionee.wms.common.WmsConstants.INVOICE_SELLER_NO%>";
    				m_ObjSoapKp.LoginPass="<%=com.gionee.wms.common.WmsConstants.INVOICE_SELLER_PASSWORD%>";
    				m_ObjSoapKp.DJID = this.ID;
    				$.extend(m_ObjSoapKp, this);
    				m_ObjSoapKp.MakeInvoice();
    				
    				if (m_ObjSoapKp.RetCode == '5011') { // 发票打印成功
    					successOrder.push(this.orderId);
    					if(this.batchCode != null && this.batchId != null) {
    						successBatch[this.batchCode] = this.batchId;
    					}
    				}
				} catch(e) {
					alertMsg.error(e.message);
				}
			});
			
			var postData = {};
			$.each(successOrder, function(i, v) {
				postData['orderIds[' + i + ']'] = v;
			});
			$.each(successBatch, function(k, v) {
				postData["batchMap['" + k + "']"] = v;
			});
			if ($.isEmptyObject(postData)) {
				return;
			}
			$.post("${ctx}/stock/salesOrder!successInvoice.action", postData, function(data) {
				if (data.statusCode == 200) {
					alertMsg.correct(data.message);
				}
			}, "json");
		}, "json");
	});
});
</script>