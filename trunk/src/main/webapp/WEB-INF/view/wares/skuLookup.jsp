<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="pagerForm" method="post" action="#rel#">
	<input type="hidden" name="page.currentPage" value="1" />
	<input type="hidden" name="page.pageSize" value="${page.pageSize}" />
	<input type="hidden" name="wares.category.catPath" value="${wares.category.catPath }" />
	<input type="hidden" name="skuCode" value="${skuCode }" />
	<input type="hidden" name="skuName" value="${skuName }" />
	<input type="hidden" name="wares.id" value="${wares.id }" />
</form>

<div class="pageHeader">
	<form rel="pagerForm" onsubmit="return dialogSearch(this);" action="${ctx}/wares/sku!lookup.action" method="post">
	<input type="hidden" name="wares.indivEnabled" value="${wares.indivEnabled }" />
	<div class="searchBar">
		<ul class="searchContent">
			<li>
				<select  name="wares.category.catPath">
				<c:forEach items="${categorys}" var="cat">
					<option value="${cat.catPath }" ${(cat.catPath==wares.category.catPath)?"selected='true'":""}>
					<c:set var="level" value="${fn:length(fn:split(cat.catPath,','))}"/>
					<c:forEach begin="1" end="${level-1}">&nbsp;&nbsp;&nbsp</c:forEach>${cat.catName }
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
			<li><a class="add" href="${ctx}/wares/sku!input.action?wares.id=${wares.id}" target="dialog" mask="true" width="800" height="600"><span>添加商品SKU</span></a></li>
		</ul>
	</div>
	<table class="table" width="1200" layoutH="138">
		<thead>
			<tr>
				<th width="50">SKU编码</th>
				<th width="100">SKU名称</th>
				<th width="50">SKU条形码</th>
				<th width="50">计量单位</th>
				<th width="200">SKU属性</th>
				<th width="200">备注</th>
				<th width="80">选择带回</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${skuList}" var="item">
			<tr target="sid_user" rel="1">
				<td>${item.skuCode }</td>
				<td>${item.skuName }</td>
				<td>${item.skuBarcode }</td>
				<td>${item.wares.measureUnit }</td>
				<td>
					<c:forEach items="${item.itemList}" var="skuItem">
						${skuItem.skuAttr.attrName }:${skuItem.itemName } | 
					</c:forEach>
				</td>
				<td>${item.remark }</td>
				<td>
					<a class="btnSelect" href="javascript:$.bringBack({id:'${item.id }', skuName:'${item.skuName }', skuCode:'${item.skuCode }', measureUnit:'${item.wares.measureUnit }', indivEnabled:'${item.wares.indivEnabled}'})" title="选择">选择</a>
				</td>

			</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="panelBar">
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="page.pageSize" onchange="dialogPageBreak({numPerPage:this.value})">
				<option value="20" ${page.pageSize==20?"selected='true'":""}>20</option>
				<option value="50" ${page.pageSize==50?"selected='true'":""}>50</option>
				<option value="100" ${page.pageSize==100?"selected='true'":""}>100</option>
				<option value="200" ${page.pageSize==200?"selected='true'":""}>200</option>
			</select>
			<span>条，共${page.totalRow}条</span>
		</div>
		
		<div class="pagination" targetType="dialog" totalCount="${page.totalRow }" numPerPage="${page.pageSize }" pageNumShown="10" currentPage="${page.currentPage}"></div>

	</div>
</div>