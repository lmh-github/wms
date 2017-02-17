<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="pagerForm" method="post" action="#rel#">
	<input type="hidden" name="page.currentPage" value="1" />
	<input type="hidden" name="page.pageSize" value="${page.pageSize}" />

</form>

<div class="pageHeader">
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/stock/stockChange.action" method="post">
	<div class="searchBar">
		<ul class="searchContent">
			<li>
				<label>仓库：</label>
				<select class="required" name="warehouseId">
					<option value="">请选择</option>
					<c:forEach items="${warehouseList}" var="item">
						<option value=${item.id } ${(item.id==warehouseId)?"selected='true'":""}>${item.warehouseName }</option>
					</c:forEach>
				</select>
			</li>
			<li>
				<label>SKU编码：</label>
				<input type="text" name="skuCode" value="${skuCode}"/>
			</li>
			<li>
				<label>SKU名称：</label>
				<input type="text" name="skuName" value="${skuName}"/>
			</li>
			<li>
				<label>变动开始时间：</label>
				<input type="text" name="createTimeBegin" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${createTimeBegin }" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
            </li>
            <li>
                <label>变动截止时间：</label>
				<input type="text" name="createTimeEnd" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${createTimeEnd }" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			</li>
			<li>
				<label>库存类型：</label>
				<s:select name="stockType" list="@com.gionee.wms.common.WmsConstants$StockType@values()" listKey="code" listValue="name" headerValue="请选择" headerKey=""/>  
			</li>
			<li>
				<label>业务类型：</label>
				<s:select name="bizType" list="@com.gionee.wms.common.WmsConstants$StockBizType@values()" listKey="code" listValue="name" headerValue="请选择" headerKey=""/>  
			</li>
		</ul>
		<div class="subBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">检索</button></div></div></li>
                <li><div class="button"><div class="buttonContent"><button onclick="if(${page.totalRow}>10000){alert('查询记录大于1000条，请过滤条件后再导出！');return false;}else if(${page.totalRow}==0){alert('没有要导出的记录！');return false;}else{$(this).closest('li').next().find('a').trigger('click')}">导出excel</button></div></div></li>
                <li style="display: none;"><div class="buttonActive"><a href="${ctx}/stock/stockChange.action?exports=1" target="dwzExport" targettype="navTab"><span>导出excel</span></a></div></li>
			</ul>
		</div>
	</div>
	</form>
</div>
<div class="pageContent" >
	<table class="list" width="100%" layoutH="115">
		<thead>
			<tr>
				<th>SKU编码</th>
				<th>SKU名称</th>
				<th>仓库</th>
				<th>库存类型</th>
				<th>变动时间</th>
				<th>业务类型</th>
				<th>原单号</th>
				<th>期初结存</th>
				<th>变动数量</th>
				<th>期后结余</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="changeList">
			<tr target="sid_user" rel="1">
				<td>${stock.sku.skuCode }</td>
				<td>${stock.sku.skuName }</td>
				<td>${stock.warehouse.warehouseName }</td>
				<td>
					<s:if test="stockType==@com.gionee.wms.common.WmsConstants$StockType@STOCK_SALES.code">可销售库存</s:if>
					<s:elseif test="stockType==@com.gionee.wms.common.WmsConstants$StockType@STOCK_OCCUPY.code">占用库存</s:elseif>
					<s:elseif test="stockType==@com.gionee.wms.common.WmsConstants$StockType@STOCK_UNSALES.code">不可销售库存</s:elseif>
					<s:elseif test="stockType==@com.gionee.wms.common.WmsConstants$StockType@STOCK_TOTAL.code">总库存</s:elseif>
			    </td>
			    <td><fmt:formatDate value="${createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			    <td>
					<s:if test="bizType==@com.gionee.wms.common.WmsConstants$StockBizType@IN_PURCHASE.code"><s:property value="@com.gionee.wms.common.WmsConstants$StockBizType@IN_PURCHASE.name"/></s:if>
					<s:elseif test="bizType==@com.gionee.wms.common.WmsConstants$StockBizType@IN_RMA.code"><s:property value="@com.gionee.wms.common.WmsConstants$StockBizType@IN_RMA.name"/></s:elseif>
					<s:elseif test="bizType==@com.gionee.wms.common.WmsConstants$StockBizType@IN_REFUSE.code"><s:property value="@com.gionee.wms.common.WmsConstants$StockBizType@IN_REFUSE.name"/></s:elseif>
					<s:elseif test="bizType==@com.gionee.wms.common.WmsConstants$StockBizType@IN_BACK_TRANSFER.code"><s:property value="@com.gionee.wms.common.WmsConstants$StockBizType@IN_BACK_TRANSFER.name"/></s:elseif>
					<s:elseif test="bizType==@com.gionee.wms.common.WmsConstants$StockBizType@OUT_SALES.code"><s:property value="@com.gionee.wms.common.WmsConstants$StockBizType@OUT_SALES.name"/></s:elseif>
					<s:elseif test="bizType==@com.gionee.wms.common.WmsConstants$StockBizType@CONVERT_ORDER_OCCUPY.code"><s:property value="@com.gionee.wms.common.WmsConstants$StockBizType@CONVERT_ORDER_OCCUPY.name"/></s:elseif>
					<s:elseif test="bizType==@com.gionee.wms.common.WmsConstants$StockBizType@CONVERT_SALES2UNSALES.code"><s:property value="@com.gionee.wms.common.WmsConstants$StockBizType@CONVERT_SALES2UNSALES.name"/></s:elseif>
					<s:elseif test="bizType==@com.gionee.wms.common.WmsConstants$StockBizType@CONVERT_UNSALES2SALES.code"><s:property value="@com.gionee.wms.common.WmsConstants$StockBizType@CONVERT_UNSALES2SALES.name"/></s:elseif>
					<s:elseif test="bizType==@com.gionee.wms.common.WmsConstants$StockBizType@CONVERT_CANCEL_ORDER.code"><s:property value="@com.gionee.wms.common.WmsConstants$StockBizType@CONVERT_CANCEL_ORDER.name"/></s:elseif>
					<s:elseif test="bizType==@com.gionee.wms.common.WmsConstants$StockBizType@CONVERT_UPDATE_ORDER.code"><s:property value="@com.gionee.wms.common.WmsConstants$StockBizType@CONVERT_UPDATE_ORDER.name"/></s:elseif>
					<s:elseif test="bizType==@com.gionee.wms.common.WmsConstants$StockBizType@OUT_TRANSFER.code"><s:property value="@com.gionee.wms.common.WmsConstants$StockBizType@OUT_TRANSFER.name"/></s:elseif>
					<s:elseif test="bizType==@com.gionee.wms.common.WmsConstants$StockBizType@CONVERT_CANCEL_TRANS.code"><s:property value="@com.gionee.wms.common.WmsConstants$StockBizType@CONVERT_CANCEL_TRANS.name"/></s:elseif>
					<s:elseif test="bizType==@com.gionee.wms.common.WmsConstants$StockBizType@CHECK_IN.code"><s:property value="@com.gionee.wms.common.WmsConstants$StockBizType@CHECK_IN.name"/></s:elseif>
					<s:elseif test="bizType==@com.gionee.wms.common.WmsConstants$StockBizType@CHECK_OUT.code"><s:property value="@com.gionee.wms.common.WmsConstants$StockBizType@CHECK_OUT.name"/></s:elseif>
					<s:elseif test="bizType==@com.gionee.wms.common.WmsConstants$StockBizType@IN_PURCHARMA.code"><s:property value="@com.gionee.wms.common.WmsConstants$StockBizType@IN_PURCHARMA.name"/></s:elseif>
					<s:elseif test="bizType==@com.gionee.wms.common.WmsConstants$StockBizType@IN_SHUADAN.code"><s:property value="@com.gionee.wms.common.WmsConstants$StockBizType@IN_SHUADAN.name"/></s:elseif>
			    </td>
			    <td>${originalCode }</td>
			    <td align="right">${openingStock}</td>
			    <td align="right">${quantity}</td>
			    <td align="right">${closingStock}</td>
			</tr>
			</s:iterator>
		</tbody>
	</table>
	<div class="panelBar">
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value})">
				<option value="1" ${page.pageSize==1?"selected='true'":""}>1</option>
				<option value="2" ${page.pageSize==2?"selected='true'":""}>2</option>
				<option value="100" ${page.pageSize==100?"selected='true'":""}>100</option>
				<option value="200" ${page.pageSize==200?"selected='true'":""}>200</option>
			</select>
			<span>条，共${page.totalRow}条</span>
		</div>
		
		<div class="pagination" targetType="navTab" totalCount="${page.totalRow }" numPerPage="${page.pageSize }" pageNumShown="10" currentPage="${page.currentPage}"></div>

	</div>
</div>