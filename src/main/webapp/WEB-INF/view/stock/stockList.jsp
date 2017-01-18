<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="pagerForm" method="post" action="#rel#">
	<input type="hidden" name="page.currentPage" value="1" />
	<input type="hidden" name="page.pageSize" value="${page.pageSize}" />

</form>

<div class="pageHeader">
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/stock/stock.action" method="post">
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
				<label>商品分类：</label>
				<select  name="catPath">
				<c:forEach items="${categoryList}" var="cat">
					<option value="${cat.catPath }" ${(cat.catPath==catPath)?"selected='true'":""}>
					<c:set var="level" value="${fn:length(fn:split(cat.catPath,','))}"/>
					<c:forEach begin="1" end="${level-1}">&nbsp;&nbsp;</c:forEach>${cat.catName }
					</option>
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
		</ul>
		<div class="subBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">检索</button></div></div></li>
				<li><div class="buttonActive"><a href="${ctx}/stock/stock.action?exports=1" target="dwzExport" targettype="navTab"><span>导出excel</span></a></div></li>
			</ul>
		</div>
	</div>
	</form>
</div>
<div class="pageContent">
	<table class="list" width="100%" layoutH="90">
		<thead>
			<tr>
				<th>SKU编码</th>
				<th>SKU名称</th>
				<th>仓库</th>
				<th>计量单位</th>
				<th>总库存</th>
				<th>可销售库存</th>
				<th>占用库存</th>
				<th>不可销售库存</th>
				<th>库存下限</th>
				<th>库存上限</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="stockList">
			<tr target="sid_user" rel="1">
				<td>${sku.skuCode }</td>
				<td>${sku.skuName }</td>
				<td>${warehouse.warehouseName }</td>
				<td>${sku.wares.measureUnit }</td>
				<td>
					<!-- 
					<s:if test="sku.wares.indivEnabled==1">
						<a title="查看商品个体" href="${ctx}/wares/indiv.action?warehouseId=${warehouse.id}&skuCode=${sku.skuCode }"  target="navTab" rel="tab_indiv" class="">${totalQuantity+nonDefectivePl+defectivePl } = (${totalQuantity} + ${nonDefectivePl} + ${defectivePl })</a>
					</s:if>
					<s:else>
						${totalQuantity+nonDefectivePl+defectivePl } = (${totalQuantity} + ${nonDefectivePl} + ${defectivePl })
					</s:else>
					 -->
					 ${salesQuantity+occupyQuantity+unsalesQuantity}
					<a title="查看账面库存流水" href="${ctx}/stock/stockChange.action?skuCode=${sku.skuCode}&warehouseId=${warehouse.id}&stockType=<s:property value='@com.gionee.wms.common.WmsConstants$StockType@STOCK_TOTAL.code'/>"  target="navTab" rel="tab_stockChange" class="btnView">
				</td>
				<td>
					<s:if test="limitLower==-1 && limitUpper==-1">${salesQuantity}</s:if>
					<s:elseif test="limitLower>-1 && (salesQuantity+nonDefectivePl)<=limitLower"><span style="color:red">${salesQuantity}</span></s:elseif>
					<s:elseif test="limitUpper>-1 && (salesQuantity+nonDefectivePl)>=limitUpper"><span style="color:red">${salesQuantity}</span></s:elseif>
					<s:else>${salesQuantity}</s:else>
					<a title="查看账面库存流水" href="${ctx}/stock/stockChange.action?skuCode=${sku.skuCode}&warehouseId=${warehouse.id}&stockType=<s:property value='@com.gionee.wms.common.WmsConstants$StockType@STOCK_SALES.code'/>"  target="navTab" rel="tab_stockChange" class="btnView">
				</td>
				<td>
					${occupyQuantity}
					<a title="查看库存流水" href="${ctx}/stock/stockChange.action?skuCode=${sku.skuCode}&warehouseId=${warehouse.id}&stockType=<s:property value='@com.gionee.wms.common.WmsConstants$StockType@STOCK_OCCUPY.code'/>"  target="navTab" rel="tab_stockChange" class="btnView">
				</td>
				<td>
					<!-- <s:if test="sku.wares.indivEnabled==1"><a title="查看商品个体" href="${ctx}/wares/indiv.action?warehouseId=${warehouse.id}&skuCode=${sku.skuCode }&waresStatus=<s:property value="@com.gionee.wms.common.WmsConstants$IndivWaresStatus@DEFECTIVE.code"/>"  target="navTab" rel="tab_indiv" class="">${unsalesQuantity+defectivePl }</a></s:if>
					<s:else>${unsalesQuantity+defectivePl }</s:else>
					 -->
					 ${unsalesQuantity}
					<a title="查看库存流水" href="${ctx}/stock/stockChange.action?skuCode=${sku.skuCode}&warehouseId=${warehouse.id}&stockType=<s:property value='@com.gionee.wms.common.WmsConstants$StockType@STOCK_UNSALES.code'/>"  target="navTab" rel="tab_stockChange" class="btnView">
				</td>
				<td>
					<s:if test="limitLower>-1">${limitLower}</s:if>
				</td>
				<td>
					<s:if test="limitUpper>-1">${limitUpper}</s:if>
				</td>
				<td>
					<a title="设置安全库存" target="dialog" mask="true" width="500" height="600" href="${ctx}/stock/stock!inputLimit.action?id=${id }">设置安全库存</a>
                    <s:if test="warehouse.warehouseName == '顺丰仓' ">&nbsp;|&nbsp;<a href="${ctx}/stock/stock!queryRealTimeInvBalance.action?skuCode=${sku.skuCode}" target="dialog" rel="dlg_RealTimeInventoryBalance" mask="true" width="600" height="500" warn="查看实时库存">查看实时库存</a></s:if>
				</td>
			</tr>
			</s:iterator>
		</tbody>
	</table>
	<div class="panelBar">
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value})">
				<option value="10" ${page.pageSize==10?"selected='true'":""}>10</option>
				<option value="20" ${page.pageSize==20?"selected='true'":""}>20</option>
				<option value="100" ${page.pageSize==100?"selected='true'":""}>100</option>
				<option value="200" ${page.pageSize==200?"selected='true'":""}>200</option>
			</select>
			<span>条，共${page.totalRow}条</span>
		</div>
		
		<div class="pagination" targetType="navTab" totalCount="${page.totalRow }" numPerPage="${page.pageSize }" pageNumShown="10" currentPage="${page.currentPage}"></div>

	</div>
</div>