<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="pagerForm2" method="post" action="#rel#">
	<input type="hidden" name="attrSet.id" value="${attrSet.id }" />
</form>

<div class="pageHeader">
	<form rel="pagerForm2" onsubmit="return navTabSearch(this);" action="${ctx}/wares/skuAttr.action" method="post">
	<div class="searchBar">
		<ul class="searchContent">
			<li>
				<label>商品类型：</label>
				<select class="combox" name="attrSet.id" onchange="return navTabSearch(this.form);">
					<c:forEach items="${attrSetList}" var="item">
						<option value=${item.id } ${(item.id==attrSet.id)?"selected='true'":""}>${item.attrSetName }</option>
					</c:forEach>
				</select>
			</li>
		</ul>
	</div>
	</form>
</div>

<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="${ctx}/wares/skuAttr!input.action?attrSet.id=${attrSet.id}" target="dialog" mask="true" width="800" height="600"><span>添加属性</span></a></li>
		</ul>
	</div>
	<table class="list" width="1200">
		<thead>
			<tr>
				<th width="100">属性名称</th>
				<th width="70">商品类型</th>
				<th width="70">可选项值</th>
				<th width="150">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${skuAttrList}" var="skuAttr">
			<tr target="sid_user" rel="1">
				<td>${skuAttr.attrName }</td>
				<td>${skuAttr.attrSet.attrSetName }</td>
				<td>
					<c:forEach items="${skuAttr.itemList }" var="skuItem">
						${skuItem.itemName },
					</c:forEach>
				</td>
				<td>
					<a title="确实要删除吗？" target="ajaxTodo" href="${ctx}/wares/skuAttr!delete.action?id=${skuAttr.id}&navTabId=tab_skuAttr" class="btnDel"></a>
			        <a title="编辑" target="dialog" mask="true" width="800" height="600" href="${ctx}/wares/skuAttr!input.action?id=${skuAttr.id}" class="btnEdit"></a>
				</td>

			</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
