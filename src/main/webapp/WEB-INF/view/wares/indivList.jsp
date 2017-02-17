<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="pagerForm" method="post" action="#rel#">
	<input type="hidden" name="page.currentPage" value="1" />
	<input type="hidden" name="page.pageSize" value="${page.pageSize}" />

</form>

<div class="pageHeader">
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/wares/indiv.action" method="post">
	<div class="searchBar">
		<ul class="searchContent" style="height: 50px;">
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
				<label>商品状态：</label>
				<s:select name="waresStatus" list="@com.gionee.wms.common.WmsConstants$IndivWaresStatus@values()" listKey="code" listValue="name" headerValue="请选择" headerKey=""/>  
			</li>
			<li>
				<label>库存状态：</label>
				<s:select name="stockStatus" list="@com.gionee.wms.common.WmsConstants$IndivStockStatus@values()" listKey="code" listValue="name" headerValue="请选择" headerKey=""/>  
			</li>
			<li>
				<label>个体编码：</label>
				<input type="text" name="indivCode" value="${indivCode}"/>
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
				<label>入库开始时间：</label>
				<input type="text" name="stockInTimeBegin" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${stockInTimeBegin }" pattern="yyyy-MM-dd"/>"/>
            </li>
            <li>
                <label>入库截止时间：</label>
				<input type="text" name="stockInTimeEnd" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${stockInTimeEnd }" pattern="yyyy-MM-dd"/>"/>
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
<div class="pageContent"><!-- 
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="${ctx}/stock/stock!input.action" target="dialog" rel="dlg_stock" mask="true" width="900" height="800"><span>添加库存信息</span></a></li>
		</ul>
	</div> -->
	<table class="list" width="100%" layoutH="115">
		<thead>
			<tr align="center">
				<th>个体编码</th>
				<th>SKU编码</th>
				<th>SKU名称</th>
				<th>所在仓库</th>
				<th>商品状态</th>
				<th>库存状态</th>
				<th>库存状态操作</th>
				<th>入库日期</th>
				<th>收货编号</th>
				<th>批次号</th>
				<th>出库日期</th>
				<th>发货编号</th>
				<th>销售订单号</th>
				<th>退货日期</th>
				<th>退货编号</th>
				<th>退货次数</th>
				<th>备注</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="indivList">
			<tr align="center">
				<td>${indivCode }</td>
				<td>${skuCode }</td>
				<td>${skuName }</td>
				<td>${warehouseName }</td>
				<td>
					<s:if test="waresStatus==@com.gionee.wms.common.WmsConstants$IndivWaresStatus@NON_DEFECTIVE.code">良品</s:if>
					<s:elseif test="waresStatus==@com.gionee.wms.common.WmsConstants$IndivWaresStatus@DEFECTIVE.code"><span style="color:red;">次品</span></s:elseif>
			    </td>
				<td>
					<s:if test="stockStatus==@com.gionee.wms.common.WmsConstants$IndivStockStatus@IN_WAREHOUSE.code">在库</s:if>
					<s:elseif test="stockStatus==@com.gionee.wms.common.WmsConstants$IndivStockStatus@OUT_WAREHOUSE.code">出库</s:elseif>
					<s:elseif test="stockStatus==@com.gionee.wms.common.WmsConstants$IndivStockStatus@STOCKIN_HANDLING.code">入库中</s:elseif>
					<s:elseif test="stockStatus==@com.gionee.wms.common.WmsConstants$IndivStockStatus@STOCKOUT_HANDLING.code">出库中</s:elseif>
				</td>
				<td>
				<shiro:hasPermission name="indiv:modifyImeiStatus">
					<a href="${ctx}/wares/indiv!updateStockStatus.action?id=${id}&indiv.stockStatus=<s:property value='@com.gionee.wms.common.WmsConstants$IndivStockStatus@IN_WAREHOUSE.code'/>" target="ajaxTodo" title="确定要设为在库吗?" class="btnAdd">设为在库</a>
					<a href="${ctx}/wares/indiv!updateStockStatus.action?id=${id}&indiv.stockStatus=<s:property value='@com.gionee.wms.common.WmsConstants$IndivStockStatus@OUT_WAREHOUSE.code'/>" target="ajaxTodo" title="确定要设为出库吗?" class="btnDel">设为出库</a>
				</shiro:hasPermission>
				</td>
				<td><fmt:formatDate value="${inTime}" pattern="yyyy-MM-dd"/></td>
				<td>${inCode }</td>
				<td>${productBatchNo }</td>
				<td><fmt:formatDate value="${outTime}" pattern="yyyy-MM-dd"/></td>
				<td>${outCode }</td>
				<td>${orderCode }</td>
				<td><fmt:formatDate value="${rmaTime}" pattern="yyyy-MM-dd"/></td>
				<td>${rmaCode }</td>
				<td>${rmaCount }</td>
				<td>${remark }</td>
				<td>
					<a title="更改商品状态" target="dialog" rel="dlg_indiv" mask="true" width="800" height="600" href="${ctx}/wares/indiv!input.action?id=${id}" class="btnEdit"></a>
				</td>
			</tr>
			</s:iterator>
		</tbody>
	</table>
	<div class="panelBar">
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value})">
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