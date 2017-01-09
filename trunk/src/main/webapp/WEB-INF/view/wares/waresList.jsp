<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="pagerForm" method="post" action="#rel#">
	<input type="hidden" name="page.currentPage" value="1" />
	<input type="hidden" name="page.pageSize" value="${page.pageSize}" />
	<input type="hidden" name="wares.category.catPath" value="${wares.category.catPath }" />
	<input type="hidden" name="waresCode" value="${waresCode }" />
	<input type="hidden" name="waresName" value="${waresName }" />
</form>

<div class="pageHeader">
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/wares/wares.action" method="post">
	<div class="searchBar">
		<ul class="searchContent">
			<li>
				<select  name="category.catPath">
				<c:forEach items="${categoryList}" var="cat">
					<option value="${cat.catPath }" ${(cat.catPath==category.catPath)?"selected='true'":""}>
					<c:set var="level" value="${fn:length(fn:split(cat.catPath,','))}"/>
					<c:forEach begin="1" end="${level-1}">&nbsp;&nbsp;&nbsp;</c:forEach>${cat.catName }
					</option>
				</c:forEach>
				</select>
			</li>
			<li>
				<label>商品编码：</label>
				<input type="text" name="waresCode" value="${waresCode}"/>
			</li>
			<li>
				<label>商品名称：</label>
				<input type="text" name="waresName" value="${waresName}"/>
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
			<li><a class="add" href="${ctx}/wares/wares!input.action" target="dialog" mask="true" width="800" height="600"><span>添加商品</span></a></li>
		</ul>
	</div>
	<table class="table" width="1200" layoutH="138">
		<thead>
			<tr>
				<th width="100">商品编码</th>
				<th width="70">商品名称</th>
				<th width="70">商品分类</th>
				<th width="70">商品品牌</th>
				<th width="70">计量单位</th>
				<th width="70">身份码管理</th>
				<th width="70">创建时间</th>
				<th width="150">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${waresList}" var="item">
			<tr target="sid_user" rel="1">
				<td>${item.waresCode }</td>
				<td>${item.waresName }</td>
				<td>${item.category.catName }</td>
				<td>${item.waresBrand }</td>
				<td>${item.measureUnit }</td>
				<td>${item.indivEnabled==1?"是":"否" }</td>
				<td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>
					<a title="查看商品SKU列表" href="${ctx}/wares/sku.action?wares.id=${item.id}"  target="navTab" rel="tab_sku" class="btnView"></a>
					<a title="确实要删除吗？" target="ajaxTodo" href="${ctx}/wares/wares!delete.action?id=${item.id}&navTabId=tab_wares" class="btnDel"></a>
			        <a title="编辑" target="dialog" mask="true" width="800" height="600" href="${ctx}/wares/wares!input.action?id=${item.id}" class="btnEdit"></a>
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