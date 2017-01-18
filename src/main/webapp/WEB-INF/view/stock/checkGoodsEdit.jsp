<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="pagerForm" method="post" action="#rel#">
	<input type="hidden" name="page.currentPage" value="1" />
	<input type="hidden" name="page.pageSize" value="${page.pageSize}" />
	<input type="hidden" name="catPath" value="${catPath }" />
	<input type="hidden" name="skuCode" value="${skuCode }" />
	<input type="hidden" name="skuName" value="${skuName }" />
</form>

<div class="pageHeader">
	<form rel="pagerForm" onsubmit="return dialogSearch(this);" action="${ctx}/stock/stockCheck!inputGoods.action" method="post">
	<input type="hidden" name="id" value="${check.id }" />
	<div class="searchBar">
		<ul class="searchContent">
			<li>
				<label>仓库: </label>
				<input type="text" value="${check.warehouseName }" readonly="readonly">
			</li>
			<li>
				<select  name="catPath">
				<c:forEach items="${categoryList}" var="cat">
					<option value="${cat.catPath }" ${(cat.catPath==catPath)?"selected='true'":""}>
					<c:set var="level" value="${fn:length(fn:split(cat.catPath,','))}"/>
					<c:forEach begin="1" end="${level-1}">&nbsp;&nbsp;&nbsp;</c:forEach>${cat.catName }
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
			</ul>
		</div>
	</div>
	</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a title="确实要添加这些商品到盘点商品清单吗?" warn="请选择商品" target="selectedTodo" targetType="dialog" rel="skuIds" href="${ctx}/stock/stockCheck!addGoods.action?id=${check.id}&callbackType=closeCurrent&navTabId=tab_checkGoods" class="add"><span>添加到盘点商品清单</span></a></li>
			<li class="line">line</li>
		</ul>
	</div>
	<table class="table" width="1200" layoutH="138">
		<thead>
			<tr>
				<th width="22"><input type="checkbox" group="skuIds" class="checkboxCtrl"></th>
				<th width="50">SKU编码</th>
				<th width="100">SKU名称</th>
				<th width="50">计量单位</th>
				<th width="200">备注</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${stockList}" var="item" varStatus="status">
			<tr target="sid_user" rel="${status.index }">
				<td><input name="skuIds" value="${item.sku.id }" type="checkbox"></td>
				<td>${item.sku.skuCode }</td>
				<td>${item.sku.skuName }</td>
				<td>${item.sku.wares.measureUnit }</td>
				<td>${item.sku.remark }</td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="panelBar">
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="page.pageSize" onchange="dialogPageBreak({numPerPage:this.value})">
				<option value="1" ${page.pageSize==1?"selected='true'":""}>1</option>
				<option value="50" ${page.pageSize==50?"selected='true'":""}>50</option>
				<option value="100" ${page.pageSize==100?"selected='true'":""}>100</option>
				<option value="200" ${page.pageSize==200?"selected='true'":""}>200</option>
			</select>
			<span>条，共${page.totalRow}条</span>
		</div>
		
		<div class="pagination" targetType="dialog" totalCount="${page.totalRow }" numPerPage="${page.pageSize }" pageNumShown="10" currentPage="${page.currentPage}"></div>

	</div>
</div>