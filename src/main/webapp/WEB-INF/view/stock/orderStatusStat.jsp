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
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/stock/salesOrder!orderStatusStat.action?queryFrom=button" method="post">
	<input type="hidden" name="stockCheckId" value="${stockCheckId }" />
	<div class="searchBar">
		<ul class="searchContent" style="height: 80px;">
			<li style="width:380px;">
				<label>订单时间从：</label>
				<input type="text" name="orderTimeBegin" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${orderTimeBegin }" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
				到：
				<input type="text" name="orderTimeEnd" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${orderTimeEnd }" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			</li>
			<li>
				<label>订单来源：</label>
				<s:if test="@com.gionee.wms.common.WmsConstants@WMS_COMPANY==1">
					<s:select id="orderSource" name="orderSource" list="@com.gionee.wms.common.WmsConstants$OrderSourceGionee@values()" listKey="code" listValue="name" headerValue="请选择" headerKey=""/>
				</s:if>
				<s:elseif test="@com.gionee.wms.common.WmsConstants@WMS_COMPANY==2">
					<s:select id="orderSource" name="orderSource" list="@com.gionee.wms.common.WmsConstants$OrderSourceIuni@values()" listKey="code" listValue="name" headerValue="请选择" headerKey=""/>
				</s:elseif>
			<li>
				<label style="width:100px">含商品：</label>
				<input type="text" id="skuCode" name="skuCode" value="${skuCode }"/>
			</li>
			</li>
			<!-- 
			<li>
				<label style="width:100px">含商品名称：</label>
				<input type="text" id="skuName" name="skuName" bringBackName="sku.skuName"  value="${skuName }" lookupGroup="sku" style="float:left"/>
				<a class="btnLook" href="" lookupGroup="sku" width="1200">查找SKU</a>
			</li>
			<li>
				<label style="width:100px">含商品编码：</label>
				<input type="text" id="skuCode" name="skuCode" bringBackName="sku.skuCode"  value="${skuCode }" lookupGroup="sku" style="float:left" readonly="readonly"/>
			</li>
			 -->
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
	<table class="list" width="100%" layoutH="25">
		<thead>
			<tr align="center">
				<th>快递公司</th>
				<!-- 
				<th>已取消</th>
				-->
				<th>已筛单</th>
				<th>已打单</th>
				<th>配货中</th>
				<th>已配货</th>
				<th>待出库</th>
				<th>已出库</th>
				<th>已签收</th>
				<!-- 
				<th>拒收中</th>
				<th>已拒收</th>
				<th>退货中</th>
				<th>已退货</th>
				 -->
			</tr>
		</thead>
		<tbody>
			<s:iterator value="orderStatusStatVoList" status="status">
			<tr target="sid_order" rel="${id }" align="center">
				<td>${shippingName }</td>
				<!-- 
				<td><a rel="tab_salesOrder" target="navTab" title="销售订单"  href="${ctx}/stock/salesOrder.action?shippingId=${shippingId}&orderStatus=<%=com.gionee.wms.common.WmsConstants.OrderStatus.CANCELED.getCode()%>&orderSource=${orderSource}&orderTimeBegin=<fmt:formatDate value="${orderTimeBegin }" pattern="yyyy-MM-dd HH:mm:ss"/>&orderTimeEnd=<fmt:formatDate value="${orderTimeEnd }" pattern="yyyy-MM-dd HH:mm:ss"/>">${canceledCount }</a></td>
			    -->
				<td><a rel="tab_salesOrder" target="navTab" title="销售订单"  href="${ctx}/stock/salesOrder.action?shippingId=${shippingId}&orderStatus=<%=com.gionee.wms.common.WmsConstants.OrderStatus.FILTERED.getCode()%>&orderSource=${orderSource}&orderTimeBegin=<fmt:formatDate value="${orderTimeBegin }" pattern="yyyy-MM-dd HH:mm:ss"/>&orderTimeEnd=<fmt:formatDate value="${orderTimeEnd }" pattern="yyyy-MM-dd HH:mm:ss"/>&skuCode=${skuCode}">${filteredCount }</a></td>
				<td><a rel="tab_salesOrder" target="navTab" title="销售订单"  href="${ctx}/stock/salesOrder.action?shippingId=${shippingId}&orderStatus=<%=com.gionee.wms.common.WmsConstants.OrderStatus.PRINTED.getCode()%>&orderSource=${orderSource}&orderTimeBegin=<fmt:formatDate value="${orderTimeBegin }" pattern="yyyy-MM-dd HH:mm:ss"/>&orderTimeEnd=<fmt:formatDate value="${orderTimeEnd }" pattern="yyyy-MM-dd HH:mm:ss"/>&skuCode=${skuCode}">${printedCount }</a></td>
				<td><a rel="tab_salesOrder" target="navTab" title="销售订单"  href="${ctx}/stock/salesOrder.action?shippingId=${shippingId}&orderStatus=<%=com.gionee.wms.common.WmsConstants.OrderStatus.PICKING.getCode()%>&orderSource=${orderSource}&orderTimeBegin=<fmt:formatDate value="${orderTimeBegin }" pattern="yyyy-MM-dd HH:mm:ss"/>&orderTimeEnd=<fmt:formatDate value="${orderTimeEnd }" pattern="yyyy-MM-dd HH:mm:ss"/>&skuCode=${skuCode}">${pickingCount }</a></td>
				<td><a rel="tab_salesOrder" target="navTab" title="销售订单"  href="${ctx}/stock/salesOrder.action?shippingId=${shippingId}&orderStatus=<%=com.gionee.wms.common.WmsConstants.OrderStatus.PICKED.getCode()%>&orderSource=${orderSource}&orderTimeBegin=<fmt:formatDate value="${orderTimeBegin }" pattern="yyyy-MM-dd HH:mm:ss"/>&orderTimeEnd=<fmt:formatDate value="${orderTimeEnd }" pattern="yyyy-MM-dd HH:mm:ss"/>&skuCode=${skuCode}">${pickedCount }</a></td>
				<td><a rel="tab_salesOrder" target="navTab" title="销售订单"  href="${ctx}/stock/salesOrder.action?shippingId=${shippingId}&orderStatus=<%=com.gionee.wms.common.WmsConstants.OrderStatus.SHIPPING.getCode()%>&orderSource=${orderSource}&orderTimeBegin=<fmt:formatDate value="${orderTimeBegin }" pattern="yyyy-MM-dd HH:mm:ss"/>&orderTimeEnd=<fmt:formatDate value="${orderTimeEnd }" pattern="yyyy-MM-dd HH:mm:ss"/>&skuCode=${skuCode}">${shippingCount }</a></td>
				<td><a rel="tab_salesOrder" target="navTab" title="销售订单"  href="${ctx}/stock/salesOrder.action?shippingId=${shippingId}&orderStatus=<%=com.gionee.wms.common.WmsConstants.OrderStatus.SHIPPED.getCode()%>&orderSource=${orderSource}&orderTimeBegin=<fmt:formatDate value="${orderTimeBegin }" pattern="yyyy-MM-dd HH:mm:ss"/>&orderTimeEnd=<fmt:formatDate value="${orderTimeEnd }" pattern="yyyy-MM-dd HH:mm:ss"/>&skuCode=${skuCode}">${shippedCount }</a></td>
				<td><a rel="tab_salesOrder" target="navTab" title="销售订单"  href="${ctx}/stock/salesOrder.action?shippingId=${shippingId}&orderStatus=<%=com.gionee.wms.common.WmsConstants.OrderStatus.RECEIVED.getCode()%>&orderSource=${orderSource}&orderTimeBegin=<fmt:formatDate value="${orderTimeBegin }" pattern="yyyy-MM-dd HH:mm:ss"/>&orderTimeEnd=<fmt:formatDate value="${orderTimeEnd }" pattern="yyyy-MM-dd HH:mm:ss"/>">${receivedCount }</a></td>
				<!-- 
				<td><a rel="tab_salesOrder" target="navTab" title="销售订单"  href="${ctx}/stock/salesOrder.action?shippingId=${shippingId}&orderStatus=<%=com.gionee.wms.common.WmsConstants.OrderStatus.REFUSEING.getCode()%>&orderSource=${orderSource}&orderTimeBegin=<fmt:formatDate value="${orderTimeBegin }" pattern="yyyy-MM-dd HH:mm:ss"/>&orderTimeEnd=<fmt:formatDate value="${orderTimeEnd }" pattern="yyyy-MM-dd HH:mm:ss"/>">${refuseingCount }</a></td>
				<td><a rel="tab_salesOrder" target="navTab" title="销售订单"  href="${ctx}/stock/salesOrder.action?shippingId=${shippingId}&orderStatus=<%=com.gionee.wms.common.WmsConstants.OrderStatus.REFUSED.getCode()%>&orderSource=${orderSource}&orderTimeBegin=<fmt:formatDate value="${orderTimeBegin }" pattern="yyyy-MM-dd HH:mm:ss"/>&orderTimeEnd=<fmt:formatDate value="${orderTimeEnd }" pattern="yyyy-MM-dd HH:mm:ss"/>">${refusedCount }</a></td>
				<td><a rel="tab_salesOrder" target="navTab" title="销售订单"  href="${ctx}/stock/salesOrder.action?shippingId=${shippingId}&orderStatus=<%=com.gionee.wms.common.WmsConstants.OrderStatus.BACKING.getCode()%>&orderSource=${orderSource}&orderTimeBegin=<fmt:formatDate value="${orderTimeBegin }" pattern="yyyy-MM-dd HH:mm:ss"/>&orderTimeEnd=<fmt:formatDate value="${orderTimeEnd }" pattern="yyyy-MM-dd HH:mm:ss"/>">${backingCount }</a></td>
				<td><a rel="tab_salesOrder" target="navTab" title="销售订单"  href="${ctx}/stock/salesOrder.action?shippingId=${shippingId}&orderStatus=<%=com.gionee.wms.common.WmsConstants.OrderStatus.BACKED.getCode()%>&orderSource=${orderSource}&orderTimeBegin=<fmt:formatDate value="${orderTimeBegin }" pattern="yyyy-MM-dd HH:mm:ss"/>&orderTimeEnd=<fmt:formatDate value="${orderTimeEnd }" pattern="yyyy-MM-dd HH:mm:ss"/>">${backedCount }</a></td>
				 -->
			</tr>
			</s:iterator>
		</tbody>
	</table>
	<!-- 
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
	 -->
</div>
<script type="text/javascript">
$(document).ready(function(){
  $("a.btnLook").click(function(){
	    var skuName=$("#skuName").val();
		//var skuCode=$("#skuCode").val();
		//this.href="${ctx}/wares/sku!lookup.action?skuName="+skuName+"&skuCode="+skuCode;
		this.href="${ctx}/wares/sku!lookup.action?skuName="+skuName;
  });
});
</script>