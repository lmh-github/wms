<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div layoutH="100">
<input id="finished" type="hidden" name="finished" value="${finished }" />
<table class="list" width="100%" height="100%">
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
		<c:forEach items="${goodsList}" var="item" varStatus="status">
			<c:set var="trStyle" value='finish="false"' />
			<c:choose>
				<c:when test="${item.preparedNum>0 && item.preparedNum<item.quantity}">
					<c:set var="trStyle" value='style="color:#ffffff; background-color:coral" finish="false"' />
				</c:when>
				<c:when test="${item.preparedNum>=item.quantity}">
					<c:set var="trStyle" value='style="color:#ffffff; background-color:green" finish="true"' />
				</c:when>
			</c:choose>
			<tr ${trStyle} skuCode="${item.skuCode }">
				<td>${item.skuName }</td>
				<td>${item.skuCode }</td>
				<td>${item.quantity }</td>
				<td>${item.preparedNum }</td>
				<td>
					<c:if test="${item.indivEnabled == 0 && item.preparedNum<item.quantity}"><a href="javascript:void;" onclick="batchPrepare('${item.id}','${item.quantity }')">批量配货</a></c:if>
					<input type="hidden" name="goodsList[${status.index }].id" value="${item.id }"/>
					<input type="hidden" name="goodsList[${status.index }].skuCode" value="${item.skuCode }"/>
					<input type="hidden" name="goodsList[${status.index }].quantity" value="${item.quantity }"/>
					<input type="hidden" name="goodsList[${status.index }].indivEnabled" value="${item.indivEnabled }"/>
					<input type="hidden" name="goodsList[${status.index }].preparedNum" value="${item.preparedNum }"/>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<h2 class="contentTitle" align="center">已扫描列表</h2>
<table class="list" width="100%" height="100%">
	<thead>
		<tr>
			<th>个体编码</th>
			<th>sku编号</th>
			<th>库存状态</th>
		</tr>
	</thead>
	<tbody id="prepare">
		<s:iterator value="indivList">
		<tr>
			<td>${indivCode }</td>
			<td>${skuCode }</td>
			<td>
				<s:if test="stockStatus==@com.gionee.wms.common.WmsConstants$IndivStockStatus@IN_WAREHOUSE.code">在库</s:if>
				<s:elseif test="stockStatus==@com.gionee.wms.common.WmsConstants$IndivStockStatus@OUT_WAREHOUSE.code">出库</s:elseif>
				<s:elseif test="stockStatus==@com.gionee.wms.common.WmsConstants$IndivStockStatus@STOCKIN_HANDLING.code">入库中</s:elseif>
				<s:elseif test="stockStatus==@com.gionee.wms.common.WmsConstants$IndivStockStatus@STOCKOUT_HANDLING.code">出库中</s:elseif>
			</td>
		</tr>
		</s:iterator>
	</tbody>
</table>
</div>
