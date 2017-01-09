<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="pagerForm" method="post" action="#rel#">
	<input type="hidden" name="page.currentPage" value="1" />
	<input type="hidden" name="page.pageSize" value="${page.pageSize}" />
	<input type="hidden" name="skuCode" value="${skuCode }" />
	<input type="hidden" name="outerSkuCode" value="${outerSkuCode }" />
	<input type="hidden" name="outerCode" value="${outerCode }" />
</form>

<div class="pageHeader">
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/wares/skuMap.action" method="post">
	<div class="searchBar">
		<ul class="searchContent">
			<li>
				<label style="width:100px">系统SKU编码：</label>
				<input type="text" name="skuMap.skuCode" bringBackName="sku.skuCode"  value="${skuMap.skuCode }" lookupGroup="sku" style="float:left"/>
				<a class="btnLook" href="${ctx}/wares/sku!lookup.action" lookupGroup="sku" width="1200">查找SKU</a>
			</li>
			<li>
				<label style="width:100px">第三方SKU编码：</label>
				<input type="text" name="skuMap.outerSkuCode" value="${skuMap.outerSkuCode}"/>
			</li>
			<li>
				<label style="width:100px">第三方公司编码：</label>
				<!--  
				<input type="text" name="skuMap.outerCode" value="${skuMap.outerCode}"/>(唯品会:vip)
				-->
                <select id='outerCode' name="skuMap.outerCode">
                    <option value="">请选择</option>
                    <option value="vip" <c:if test="${skuMap.outerCode == 'vip'}">selected</c:if>>vip(唯品会)</option>
                    <option value="sf" <c:if test="${skuMap.outerCode == 'sf'}">selected</c:if>>sf(顺丰)</option>
				</select>
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
			<li>
			<a class="add" href="${ctx}/wares/skuMap!input.action?&editEnabled=true" target="dialog" rel="dlg_skuMap" mask="true" width="800" height="600"><span>添加商品SKU映射</span></a>
			</li>
		</ul>
	</div>
	<table class="list" width="100%" layoutH="115">
		<thead>
			<tr>
				<th>系统SKU编码</th>
				<th>第三方SKU编码</th>
				<th>第三方公司编码</th>
				<th>推送状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${skuMapList}" var="item">
			<tr target="sid_user" rel="1">
				<td>${item.skuCode }</td>
				<td>${item.outerSkuCode }</td>
				<td>${item.outerCode }</td>
				<td><c:if test="${item.outerCode=='sf'}">${item.skuPushStatus==1?"已推送":"未推送"}</c:if></td>
				<td>
					<a title="确实要删除吗？" target="ajaxTodo" href="${ctx}/wares/skuMap!delete.action?id=${item.id}&navTabId=tab_skuMap" class="btnDel"></a>
					<a title="编辑" target="dialog"  rel="dlg_skuMap" mask="true" width="800" height="600" href="${ctx}/wares/skuMap!input.action?id=${item.id}&editEnabled=${item.skuPushStatus!=1}" class="btnEdit"></a>
				</td>

			</tr>
			</c:forEach>
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